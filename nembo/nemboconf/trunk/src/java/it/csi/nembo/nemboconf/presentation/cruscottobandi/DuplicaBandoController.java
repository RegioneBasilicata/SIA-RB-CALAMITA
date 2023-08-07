package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.EsitoCallPckDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class DuplicaBandoController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "duplica_{idBando}", method = RequestMethod.GET)
  public String duplica(Model model, HttpServletRequest request,
      HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando", session,
        false);
    common.put("idBandoSelezionato", idBando);
    saveCommonInSession(common, session);
    if (common.get("tipoOperazione") != null)
    {
      Long tipoOperazione = (Long) common.get("tipoOperazione");
      model.addAttribute("tipoOperazione", tipoOperazione);
      if (tipoOperazione == COPIA_SU_NUOVO_BANDO
          || tipoOperazione == COPIA_SU_STESSO_BANDO)
      {
        model.addAttribute("denominazioneBando",
            common.get("denominazioneBando"));
      }
      else
      {
        model.addAttribute("denominazioneInserita",
            common.get("denominazioneInserita"));
      }

    }
    addBaseDataToModel(model);
    model.addAttribute("denominazioneBandoSelezionato",
        cruscottoEJB.getInformazioniBando(idBando).getDenominazione());
    return "cruscottobandi/duplicabando/step1";
  }

  private final static Long COPIA_SU_NUOVO_BANDO     = 0l;
  private final static Long COPIA_SU_BANDO_ESISTENTE = 1l;
  private final static Long COPIA_SU_STESSO_BANDO    = 2l;

  private void addBaseDataToModel(Model model)
  {

    List<DecodificaDTO<Long>> operazioni = new ArrayList<>();
    operazioni.add(
        new DecodificaDTO<Long>(COPIA_SU_NUOVO_BANDO, "Copia su nuovo bando"));
    operazioni.add(new DecodificaDTO<Long>(COPIA_SU_BANDO_ESISTENTE,
        "Copia su bando esistente"));
    operazioni.add(new DecodificaDTO<Long>(COPIA_SU_STESSO_BANDO,
        "Copia tra oggetti dello stesso bando"));

    model.addAttribute("operazioni", operazioni);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "cerca", method = RequestMethod.POST)
  @ResponseBody
  public List<BandoDTO> cerca(Model model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "value", required = true) String denominazione)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando", session,
        false);
    common.put("denominazioneInserita", denominazione);
    Long idBandoSel = (Long) common.get("idBandoSelezionato");
    saveCommonInSession(common, session);
    List<BandoDTO> bandi = cruscottoEJB
        .getElencoBandiByDenominazione(denominazione, idBandoSel);
    return bandi;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "goToStep2", method = RequestMethod.GET)
  public String goToStep2(Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando", session,
        false);

    if (common.get("idBandoObiettivo") != null)
    {
      model.addAttribute("idBandoObiettivo", common.get("idBandoObiettivo"));
      BandoDTO bandoOb = cruscottoEJB
          .getInformazioniBando((long) common.get("idBandoObiettivo"));
      model.addAttribute("denominazioneBandoObiettivo",
          bandoOb.getDenominazione());
    }
    else
      model.addAttribute("denominazioneBandoObiettivo",
          common.get("denominazioneBando"));

    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    if (common.get("isCopiaSuStessoBando") != null)
    {
      // copia tra oggetti dello stesso bando
      BandoDTO bandoSel = cruscottoEJB.getInformazioniBando(idBandoSelezionato);
      model.addAttribute("denominazioneBandoSelezionato",
          bandoSel.getDenominazione());
      model.addAttribute("denominazioneBandoObiettivo",
          bandoSel.getDenominazione());
      model.addAttribute("isCopiaSuStessoBando", true);
      model.addAttribute("idOggettoPartenza",
          (Long) common.get("idOggettoPartenza"));
      model.addAttribute("idOggettoDestinazione",
          (Long) common.get("idOggettoDestinazione"));

      List<GruppoOggettoDTO> getElencoGruppiOggetti = cruscottoEJB
          .getElencoGruppiOggettiAttivi(idBandoSelezionato);
      List<DecodificaDTO<Long>> elencoOggetti = new ArrayList<>();
      for (GruppoOggettoDTO gruppo : getElencoGruppiOggetti)
        for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
        {
          DecodificaDTO<Long> d = new DecodificaDTO<>(
              oggetto.getIdLegameGruppoOggetto(),
              gruppo.getDescrGruppo() + " - " + oggetto.getDescrOggetto());
          elencoOggetti.add(d);
        }
      model.addAttribute("elencoOggetti", elencoOggetti);
    }

    model.addAttribute("idBandoSelezionato", idBandoSelezionato);
    BandoDTO bandoSel = cruscottoEJB
        .getInformazioniBando((long) common.get("idBandoSelezionato"));
    model.addAttribute("denominazioneBandoSelezionato",
        bandoSel.getDenominazione());
    addBaseDataToModel(model);

    return "cruscottobandi/duplicabando/step2";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "goToStep2", method = RequestMethod.POST)
  public String goToStep2(Model model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "tipoOperazione", required = true) Long tipoOperazione,
      @RequestParam(value = "idBando", required = false) Long idBandoObiettivo,
      @RequestParam(value = "denominazioneBando", required = false) String nomeNuovoBando)
      throws InternalUnexpectedException
  {

    Errors errors = new Errors();
    Map<String, Object> common = getCommonFromSession("duplicaBando", session,
        false);
    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    errors.validateMandatory(tipoOperazione, "tipoOperazione");
    if (tipoOperazione != null)
      if (tipoOperazione == COPIA_SU_NUOVO_BANDO)
      {

        if (common.get("denominazioneInserita") != null)
          common.remove("denominazioneInserita");
        if (common.get("idBandoObiettivo") != null)
          common.remove("idBandoObiettivo");

        errors.validateMandatory(nomeNuovoBando, "denominazioneBando");
        errors.validateMandatoryFieldLength(nomeNuovoBando, 5, 500,
            "denominazioneBando");
        if (!cruscottoEJB.verificaUnivocitaNomeBando(nomeNuovoBando))
          errors.addError("denominazioneBando",
              "Esiste già un bando con la denominazione uguale a quella inserita.");

        BandoDTO bandoSel = cruscottoEJB
            .getInformazioniBando(idBandoSelezionato);
        model.addAttribute("denominazioneBandoSelezionato",
            bandoSel.getDenominazione());
        model.addAttribute("denominazioneBandoObiettivo", nomeNuovoBando);
        model.addAttribute("isNuovoBando", true);
      }
      else
        if (tipoOperazione == COPIA_SU_STESSO_BANDO)
        {
          // copia tra oggetti dello stesso bando
          BandoDTO bandoSel = cruscottoEJB
              .getInformazioniBando(idBandoSelezionato);
          model.addAttribute("denominazioneBandoSelezionato",
              bandoSel.getDenominazione());
          model.addAttribute("denominazioneBandoObiettivo",
              bandoSel.getDenominazione());
          model.addAttribute("isCopiaSuStessoBando", true);

          List<GruppoOggettoDTO> getElencoGruppiOggetti = cruscottoEJB
              .getElencoGruppiOggettiAttivi(idBandoSelezionato);
          List<DecodificaDTO<Long>> elencoOggetti = new ArrayList<>();
          for (GruppoOggettoDTO gruppo : getElencoGruppiOggetti)
            for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
            {
              DecodificaDTO<Long> d = new DecodificaDTO<>(
                  oggetto.getIdLegameGruppoOggetto(),
                  gruppo.getDescrGruppo() + " - " + oggetto.getDescrOggetto());
              elencoOggetti.add(d);
            }
          model.addAttribute("elencoOggetti", elencoOggetti);

        }
        else
        {

          if (common.get("denominazioneBando") != null)
            common.remove("denominazioneBando");

          errors.validateMandatory(idBandoObiettivo, "idBando");
          if (idBandoObiettivo == null)
            errors.addError("selezioneBando",
                "Non è stato selezionato nessun bando.");
          else
          {
            BandoDTO bandoSel = cruscottoEJB
                .getInformazioniBando(idBandoSelezionato);
            BandoDTO bandoOb = cruscottoEJB
                .getInformazioniBando(idBandoObiettivo);

            model.addAttribute("denominazioneBandoSelezionato",
                bandoSel.getDenominazione());
            model.addAttribute("denominazioneBandoObiettivo",
                bandoOb.getDenominazione());

            if (bandoSel != null && bandoOb != null && !bandoSel
                .getCodiceTipoBando().equals(bandoOb.getCodiceTipoBando()))
              errors.addError("selezioneBando",
                  "I due bandi selezionati devono essere della stessa tipologia.");
          }
        }

    if (!errors.isEmpty())
    {
      model.addAttribute("prfReqValues", true);
      model.addAttribute("errors", errors);
      addBaseDataToModel(model);
      model.addAttribute("denominazioneBandoSelezionato", cruscottoEJB
          .getInformazioniBando(idBandoSelezionato).getDenominazione());
      return "cruscottobandi/duplicabando/step1";
    }

    common.put("tipoOperazione", tipoOperazione);
    if (tipoOperazione == COPIA_SU_NUOVO_BANDO)
    {
      common.put("denominazioneBando", nomeNuovoBando);
      if (common.get("idBandoObiettivo") != null)
        common.remove("idBandoObiettivo");
    }
    else
      if (tipoOperazione == COPIA_SU_STESSO_BANDO)
      {
        common.put("isCopiaSuStessoBando", true);
        common.put("denominazioneBando", nomeNuovoBando);
      }
      else
      {
        common.put("idBandoObiettivo", idBandoObiettivo);
        if (common.get("denominazioneBando") != null)
          common.remove("denominazioneBando");
      }

    saveCommonInSession(common, session);
    model.addAttribute("idBandoSelezionato", common.get("idBandoSelezionato"));
    return "cruscottobandi/duplicabando/step2";
  }

  public static final int NON_ESISTE               = 1;
  public static final int ESISTE_DA_AGGIUNGERE     = 2;
  public static final int ESISTE_NON_DA_AGGIUNGERE = 3;

  @RequestMapping(value = "/load_elenco_oggetti", produces = "application/json")
  @ResponseBody
  public Map<String, Map<String, String>> loadElencoOggetti(Model model,
      HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("duplicaBando", session,
        false);
    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    Long tipoOperazione = (Long) common.get("tipoOperazione");
    Long idBandoObiettivo = (Long) common.get("idBandoObiettivo");

    List<GruppoOggettoDTO> gruppiOggettiBandoSelezionato = cruscottoEJB
        .getElencoGruppiOggettiAttivi(idBandoSelezionato);
    List<GruppoOggettoDTO> gruppiOggettiBandoObiettivo = null;
    if (tipoOperazione != null && tipoOperazione == 1
        && idBandoObiettivo != null)
      gruppiOggettiBandoObiettivo = cruscottoEJB
          .getElencoGruppiOggettiAttivi(idBandoObiettivo);

    // devo inserire gli oggetti NON presenti nel bando obiettivo E anche quelli
    // presenti ma che hanno elementi vuoti (dich, all, impeg, tv)
    Map<String, Map<String, String>> mapElenco = new HashMap<String, Map<String, String>>();
    for (GruppoOggettoDTO gruppo : gruppiOggettiBandoSelezionato)
    {
      for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
      {

        int cheffare = oggettoEsisteNellObiettivoMaHaRobeVuote(
            gruppiOggettiBandoSelezionato, gruppiOggettiBandoObiettivo,
            gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
            oggetto.getFlagIstanza());

        if (cheffare == ESISTE_DA_AGGIUNGERE)
        {
          Map<String, String> element = new HashMap<String, String>();
          String id = String.valueOf(oggetto.getIdLegameGruppoOggetto());
          element.put("idLegameGruppoOggetto", id);
          element.put("descrizioneOggetto", " * " + gruppo.getDescrGruppo()
              + " - " + oggetto.getDescrOggetto());
          element.put("gruppo", gruppo.getDescrGruppo());
          mapElenco.put(id, element);
        }
        else
          if (cheffare == NON_ESISTE)
          {
            Map<String, String> element = new HashMap<String, String>();
            String id = String.valueOf(oggetto.getIdLegameGruppoOggetto());
            element.put("idLegameGruppoOggetto", id);
            element.put("descrizioneOggetto",
                gruppo.getDescrGruppo() + " - " + oggetto.getDescrOggetto());
            element.put("gruppo", gruppo.getDescrGruppo());
            mapElenco.put(id, element);
          }
      }
    }

    return mapElenco;
  }

  private int oggettoEsisteNellObiettivoMaHaRobeVuote(
      List<GruppoOggettoDTO> gruppiOggettiBandoSelezionato,
      List<GruppoOggettoDTO> gruppiOggettiBandoObiettivo, long idGruppoOggetto,
      long idOggetto, String flagIstanza) throws InternalUnexpectedException
  {

    if (gruppiOggettiBandoObiettivo != null)
      for (GruppoOggettoDTO gruppo : gruppiOggettiBandoObiettivo)
      {
        for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
        {
          if (gruppo.getIdGruppoOggetto() == idGruppoOggetto
              && oggetto.getIdOggetto() == idOggetto)
          {
            // se questo oggetto ha delle robe vuote tra IMPEG ALLEG DICH e
            // TESTIVERB ok
            if (cruscottoEJB.haQualcosaDiVuoto(gruppo.getIdBando(),
                idGruppoOggetto, idOggetto, flagIstanza))
              return ESISTE_DA_AGGIUNGERE;
            else
              return ESISTE_NON_DA_AGGIUNGERE;
          }
        }
      }

    return NON_ESISTE;
  }

  @RequestMapping(value = "/selezionaOggetti", method = RequestMethod.POST)
  @ResponseBody
  public String postSelezionaInterventi(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("duplicaBando",
        request.getSession(), false);

    Errors errors = new Errors();
    String ids[] = request.getParameterValues("idOggetti");
    errors.validateMandatory(ids, "error",
        "Selezionare almeno un oggetto tra quelli disponibili.");
    if (!errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("idOggetti", ids);
      common.put("idsLegamiGruppiOggettiSelezionati", ids);
      saveCommonInSession(common, request.getSession());
      return "success";
    }

    return "error";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "confermaDuplicazioneBando", method = RequestMethod.POST)
  public String conferma(Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando",
        request.getSession(), false);

    String nomeNuovoBando = (String) common.get("denominazioneBando");
    Long idBandoObiettivo = (Long) common.get("idBandoObiettivo");
    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    @SuppressWarnings("unchecked")
    List<GruppoOggettoDTO> gruppi = (List<GruppoOggettoDTO>) common
        .get("gruppiFinali");

    if (gruppi != null)
      for (GruppoOggettoDTO gruppo : gruppi)
      {
        if (gruppo.isDaImportare())
          for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
          {
            if (oggetto.isDaImportare())
            {
              oggetto.setImportAllegati(request.getParameter(
                  "flagImportaAllegati_" + oggetto.getIdLegameGruppoOggetto()));
              oggetto.setImportImpegni(request.getParameter(
                  "flagImportaImpegni_" + oggetto.getIdLegameGruppoOggetto()));
              oggetto.setImportDichiarazioni(
                  request.getParameter("flagImportaDichiarazioni_"
                      + oggetto.getIdLegameGruppoOggetto()));
              oggetto.setImportTestiVerbali(request.getParameter(
                  "flagImportaTesti_" + oggetto.getIdLegameGruppoOggetto()));
            }
          }
      }

    Long idTipoOperazione = (Long) common.get("tipoOperazione");

    EsitoCallPckDTO esito = null;
    if (idTipoOperazione != null && idTipoOperazione == COPIA_SU_STESSO_BANDO)
    {
      OggettiIstanzeDTO oggettoPartenza = (OggettiIstanzeDTO) common
          .get("oggettoPartenza");
      OggettiIstanzeDTO oggettoDestinazione = (OggettiIstanzeDTO) common
          .get("oggettoDestinazione");

      if (gruppi != null)
        for (GruppoOggettoDTO gruppo : gruppi)
        {
          if (gruppo.isDaImportare())
            for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
            {
              if (oggetto.isDaImportare())
              {
                if ("on".equals(oggettoDestinazione.getImportAllegati()))
                {
                  esito = cruscottoEJB.callDuplicaBandoCopiaOggettiStessoBando(
                      oggettoPartenza.getIdBandoOggetto(),
                      oggettoDestinazione.getIdBandoOggetto(), 3);
                  if (esito.getRisultato() != 0)
                  {
                    model.addAttribute("messaggio", esito.getMessaggio());
                    return "errore/erroreInterno";
                  }
                }

                if ("on".equals(oggettoDestinazione.getImportImpegni()))
                {
                  esito = cruscottoEJB.callDuplicaBandoCopiaOggettiStessoBando(
                      oggettoPartenza.getIdBandoOggetto(),
                      oggettoDestinazione.getIdBandoOggetto(), 20);
                  if (esito.getRisultato() != 0)
                  {
                    model.addAttribute("messaggio", esito.getMessaggio());
                    return "errore/erroreInterno";
                  }
                }

                if ("on".equals(oggettoDestinazione.getImportDichiarazioni()))
                {
                  esito = cruscottoEJB.callDuplicaBandoCopiaOggettiStessoBando(
                      oggettoPartenza.getIdBandoOggetto(),
                      oggettoDestinazione.getIdBandoOggetto(), 2);
                  if (esito.getRisultato() != 0)
                  {
                    model.addAttribute("messaggio", esito.getMessaggio());
                    return "errore/erroreInterno";
                  }
                }

                cruscottoEJB.logAttivitaBandoOggetto(
                    oggettoPartenza.getIdBandoOggetto(),
                    getUtenteAbilitazioni(session).getIdUtenteLogin(),
                    "DUPLICA BANDO",
                    "E' stata effettuata una copia di configurazione dall'oggetto "
                        + oggettoPartenza.getDescrGruppoOggetto()
                        + " all'oggetto "
                        + oggettoDestinazione.getDescrGruppoOggetto()
                        + " nell'ambito del bando corrente.");
              }
            }
        }
      
      clearCommonInSession(session);
      return "redirect:datiidentificativi_" + idBandoSelezionato + ".do";
    }
    else
    {
      esito = cruscottoEJB.callDuplicaBando(idBandoSelezionato,
          idBandoObiettivo, gruppi, nomeNuovoBando);
      if (esito.getRisultato() != 0)
      {
        model.addAttribute("messaggio", esito.getMessaggio());
        return "errore/erroreInterno";
      }
      if (idTipoOperazione != null && idTipoOperazione == 1)
        for (GruppoOggettoDTO gruppo : gruppi)
        {
          if (gruppo.isDaImportare())
            for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
            {
              if (oggetto.isDaImportare())
              {
                cruscottoEJB.logAttivitaBandoOggetto(
                    cruscottoEJB.getFirstIdBandoOggetto(idBandoObiettivo),
                    getUtenteAbilitazioni(session).getIdUtenteLogin(),
                    "DUPLICA BANDO",
                    "L'oggetto " + oggetto.getDescrGruppoOggetto()
                        + " è stato copiato dal bando "
                        + cruscottoEJB.getInformazioniBando(idBandoSelezionato)
                            .getDenominazione());
              }
            }
        }
      
    }
    
    clearCommonInSession(session);
    return "redirect:datiidentificativi_" + esito.getIdBandoNew() + ".do";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "goToStep3", method = RequestMethod.POST)
  public String goToStep3(Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando",
        request.getSession(), false);
    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    Long idBandoObiettivo = (Long) common.get("idBandoObiettivo");
    String idsLegamiGruppiOggettiSelezionati[] = (String[]) common
        .get("idsLegamiGruppiOggettiSelezionati");

    List<GruppoOggettoDTO> gruppiOggettiBandoSelezionato = cruscottoEJB
        .getElencoGruppiOggettiAttivi(idBandoSelezionato);
    for (GruppoOggettoDTO gruppo : gruppiOggettiBandoSelezionato)
    {
      for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
      {
        for (String id : idsLegamiGruppiOggettiSelezionati)
        {
          Long idLGO = Long.parseLong(id);
          if (idLGO != null
              && idLGO.longValue() == oggetto.getIdLegameGruppoOggetto())
          {
            gruppo.setDaImportare(true);
            oggetto.setDaImportare(true);
            /*
             * Per gli oggetti che NON sono sul bando di arrivo ma che sono sul
             * bando di partenza e per quelli che ci sono ma a cui manca
             * qualcosa tra IMPEG DICH ALLEG (se hanno il quadro ma è vuoto)(con
             * flagInfoCatalogo a N) e TESTIVERB (se flagIstanza è a N),allora
             * la checkbox sarà abilitata e l'utente potrà scegliere quali
             * informazioni tra IMPEG, ALLEG, DICH e TESTIVERB importare.
             */
            oggetto.setHasAllegati(cruscottoEJB.hasQualcosaInQuadro(
                idBandoSelezionato, gruppo.getIdGruppoOggetto(),
                oggetto.getIdOggetto(), "ALLEG")
                && ((idBandoObiettivo == null) ? true
                    : !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                        gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                        "ALLEG")));
            oggetto.setHasImpegni(cruscottoEJB.hasQualcosaInQuadro(
                idBandoSelezionato, gruppo.getIdGruppoOggetto(),
                oggetto.getIdOggetto(), "IMPEG")
                && ((idBandoObiettivo == null) ? true
                    : !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                        gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                        "IMPEG")));
            oggetto.setHasDichiarazioni(
                cruscottoEJB.hasQualcosaInQuadro(idBandoSelezionato,
                    gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(), "DICH")
                    && ((idBandoObiettivo == null) ? true
                        : !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                            gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                            "DICH")));
            oggetto.setHasTestiVerbali(
                cruscottoEJB.hasTestiVerbali(idBandoSelezionato,
                    gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto())
                    && "N".equals(oggetto.getFlagIstanza())
                    && ((idBandoObiettivo == null) ? true
                        : !cruscottoEJB.hasTestiVerbali(idBandoObiettivo,
                            gruppo.getIdGruppoOggetto(),
                            oggetto.getIdOggetto())));

            // if(idBandoObiettivo!=null)oggetto.setHasQuadroAllegati(cruscottoEJB.hasQuadro(idBandoObiettivo,
            // oggetto.getIdOggetto(), gruppo.getIdGruppoOggetto(), "ALLEG"));
            // if(idBandoObiettivo!=null)oggetto.setHasQuadroImpegni(cruscottoEJB.hasQuadro(idBandoObiettivo,
            // oggetto.getIdOggetto(), gruppo.getIdGruppoOggetto(), "IMPEG"));
            // if(idBandoObiettivo!=null)oggetto.setHasQuadroDichiarazioni(cruscottoEJB.hasQuadro(idBandoObiettivo,
            // oggetto.getIdOggetto(), gruppo.getIdGruppoOggetto(), "DICH"));

            if (idBandoObiettivo != null)
            {
              int cheffare = oggettoEsisteNellObiettivoMaHaRobeVuote(
                  gruppiOggettiBandoSelezionato,
                  cruscottoEJB.getElencoGruppiOggettiAttivi(idBandoObiettivo),
                  gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                  oggetto.getFlagIstanza());
              if (cheffare == ESISTE_DA_AGGIUNGERE)
                oggetto.setDescrOggetto(" * " + oggetto.getDescrOggetto());
            }
          }
        }
      }
    }

    common.put("gruppiFinali", gruppiOggettiBandoSelezionato);
    model.addAttribute("denominazioneBandoSelezionato", cruscottoEJB
        .getInformazioniBando(idBandoSelezionato).getDenominazione());
    model.addAttribute("denominazioneBandoObiettivo",
        (idBandoObiettivo == null) ? common.get("denominazioneBando")
            : cruscottoEJB.getInformazioniBando(idBandoObiettivo)
                .getDenominazione());
    model.addAttribute("idBandoSelezionato", idBandoSelezionato);
    model.addAttribute("idBandoObiettivo", idBandoObiettivo);
    model.addAttribute("elencoGruppiOggetti", gruppiOggettiBandoSelezionato);

    return "cruscottobandi/duplicabando/step3";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "goToStep3CopiaOggettiStessoBando", method = RequestMethod.POST)
  public String goToStep3CopiaOggettiStessoBando(Model model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    Map<String, Object> common = getCommonFromSession("duplicaBando",
        request.getSession(), false);
    Long idBandoSelezionato = (Long) common.get("idBandoSelezionato");
    Errors errors = new Errors();

    Long idOggettoPartenza = errors.validateMandatoryLong(
        request.getParameter("idOggettoPartenza"), "idOggettoPartenza");
    Long idOggettoDestinazione = errors.validateMandatoryLong(
        request.getParameter("idOggettoDestinazione"), "idOggettoDestinazione");
    model.addAttribute("idBandoSelezionato", idBandoSelezionato);
    model.addAttribute("idBandoObiettivo", idBandoSelezionato);

    if (errors.isEmpty())
      if (idOggettoDestinazione.longValue() == idOggettoPartenza.longValue())
      {
        errors.addError("idOggettoPartenza",
            "Impossibile effettuare la copia se i due oggetti non sono diversi.");
        errors.addError("idOggettoDestinazione",
            "Impossibile effettuare la copia se i due oggetti non sono diversi.");
      }

    // copia tra oggetti dello stesso bando
    BandoDTO bandoSel = cruscottoEJB.getInformazioniBando(idBandoSelezionato);
    model.addAttribute("denominazioneBandoSelezionato",
        bandoSel.getDenominazione());
    model.addAttribute("denominazioneBandoObiettivo",
        bandoSel.getDenominazione());
    model.addAttribute("isCopiaSuStessoBando", true);
    if (!errors.isEmpty())
    {

      List<GruppoOggettoDTO> getElencoGruppiOggetti = cruscottoEJB
          .getElencoGruppiOggettiAttivi(idBandoSelezionato);
      List<DecodificaDTO<Long>> elencoOggetti = new ArrayList<>();
      for (GruppoOggettoDTO gruppo : getElencoGruppiOggetti)
        for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
        {
          DecodificaDTO<Long> d = new DecodificaDTO<>(
              oggetto.getIdLegameGruppoOggetto(),
              gruppo.getDescrGruppo() + " - " + oggetto.getDescrOggetto());
          elencoOggetti.add(d);
        }

      model.addAttribute("elencoOggetti", elencoOggetti);
      model.addAttribute("errors", errors);
      model.addAttribute("prfReqValues", true);

      return "cruscottobandi/duplicabando/step2";
    }

    String[] idOggettoDestinazioneList = new String[1];
    idOggettoDestinazioneList[0] = idOggettoDestinazione.toString();
    List<GruppoOggettoDTO> gruppiOggettiBandoSelezionato = cruscottoEJB
        .getElencoGruppiOggettiAttivi(idBandoSelezionato);
    OggettiIstanzeDTO oggPartenza = null;
    OggettiIstanzeDTO oggDestinazione = null;
    GruppoOggettoDTO gruppoPartenza = null;
    GruppoOggettoDTO gruppoDestinazine = null;

    for (GruppoOggettoDTO gruppo : gruppiOggettiBandoSelezionato)
    {
      for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
      {
        if (oggetto.getIdLegameGruppoOggetto() == idOggettoPartenza)
        {
          if (oggPartenza == null)
          {
            oggPartenza = oggetto;
            gruppoPartenza = gruppo;
            model.addAttribute("denomOggettoPartenza",
                gruppo.getDescrGruppo() + " - " + oggetto.getDescrOggetto());
          }
        }
      }
    }

    for (GruppoOggettoDTO gruppo : gruppiOggettiBandoSelezionato)
    {
      for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
      {
        if (oggetto.getIdLegameGruppoOggetto() == idOggettoDestinazione)
        {
          if (oggDestinazione == null)
          {
            oggDestinazione = oggetto;
            gruppo.setDaImportare(true);
            oggetto.setDaImportare(true);
            gruppoDestinazine = gruppo;
            Long idBandoObiettivo = idBandoSelezionato;

            oggetto.setHasAllegati(cruscottoEJB.hasQualcosaInQuadro(
                idBandoSelezionato, gruppoPartenza.getIdGruppoOggetto(),
                oggPartenza.getIdOggetto(), "ALLEG")
                && ((idBandoObiettivo == null) ? true
                    : cruscottoEJB.hasQuadro(idBandoObiettivo,
                        gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                        "ALLEG"))); // &&
                                    // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                    // gruppoDestinazine.getIdGruppoOggetto(),
                                    // oggDestinazione.getIdOggetto(),
                                    // "ALLEG")));
            oggetto.setHasImpegni(cruscottoEJB.hasQualcosaInQuadro(
                idBandoSelezionato, gruppoPartenza.getIdGruppoOggetto(),
                oggPartenza.getIdOggetto(), "IMPEG")
                && ((idBandoObiettivo == null) ? true
                    : cruscottoEJB.hasQuadro(idBandoObiettivo,
                        gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                        "IMPEG"))); // &&
                                    // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                    // gruppoDestinazine.getIdGruppoOggetto(),
                                    // oggDestinazione.getIdOggetto(),
                                    // "IMPEG")));
            oggetto.setHasDichiarazioni(cruscottoEJB.hasQualcosaInQuadro(
                idBandoSelezionato, gruppoPartenza.getIdGruppoOggetto(),
                oggPartenza.getIdOggetto(), "DICH")
                && ((idBandoObiettivo == null) ? true
                    : cruscottoEJB.hasQuadro(idBandoObiettivo,
                        gruppo.getIdGruppoOggetto(), oggetto.getIdOggetto(),
                        "DICH"))); // &&
                                   // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                   // gruppoDestinazine.getIdGruppoOggetto(),
                                   // oggDestinazione.getIdOggetto(), "DICH")));
          }
        }
      }
    }

    Long idBandoObiettivo = idBandoSelezionato;

    oggDestinazione
        .setHasAllegati(cruscottoEJB.hasQualcosaInQuadro(idBandoSelezionato,
            gruppoPartenza.getIdGruppoOggetto(), oggPartenza.getIdOggetto(),
            "ALLEG")
            && ((idBandoObiettivo == null) ? true
                : cruscottoEJB.hasQuadro(idBandoObiettivo,
                    gruppoDestinazine.getIdGruppoOggetto(),
                    oggDestinazione.getIdOggetto(), "ALLEG"))); // &&
                                                                // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                                                // gruppoDestinazine.getIdGruppoOggetto(),
                                                                // oggDestinazione.getIdOggetto(),
                                                                // "ALLEG")));
    oggDestinazione
        .setHasImpegni(cruscottoEJB.hasQualcosaInQuadro(idBandoSelezionato,
            gruppoPartenza.getIdGruppoOggetto(), oggPartenza.getIdOggetto(),
            "IMPEG")
            && ((idBandoObiettivo == null) ? true
                : cruscottoEJB.hasQuadro(idBandoObiettivo,
                    gruppoDestinazine.getIdGruppoOggetto(),
                    oggDestinazione.getIdOggetto(), "IMPEG"))); // &&
                                                                // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                                                // gruppoDestinazine.getIdGruppoOggetto(),
                                                                // oggDestinazione.getIdOggetto(),
                                                                // "IMPEG")));
    oggDestinazione.setHasDichiarazioni(cruscottoEJB.hasQualcosaInQuadro(
        idBandoSelezionato, gruppoPartenza.getIdGruppoOggetto(),
        oggPartenza.getIdOggetto(), "DICH")
        && ((idBandoObiettivo == null) ? true
            : cruscottoEJB.hasQuadro(idBandoObiettivo,
                gruppoDestinazine.getIdGruppoOggetto(),
                oggDestinazione.getIdOggetto(), "DICH"))); // &&
                                                           // !cruscottoEJB.hasQualcosaInQuadro(idBandoObiettivo,
                                                           // gruppoDestinazine.getIdGruppoOggetto(),
                                                           // oggDestinazione.getIdOggetto(),
                                                           // "DICH")));

    model.addAttribute("elencoGruppiOggetti", gruppiOggettiBandoSelezionato);
    model.addAttribute("isCopiaSuStessoBando", true);

    common.put("oggettoPartenza", oggPartenza);
    common.put("oggettoDestinazione", oggDestinazione);
    common.put("idOggettoPartenza", idOggettoPartenza);
    common.put("idOggettoDestinazione", idOggettoDestinazione);
    common.put("gruppiFinali", gruppiOggettiBandoSelezionato);
    common.put("idsLegamiGruppiOggettiSelezionati", idOggettoDestinazioneList);

    saveCommonInSession(common, session);

    return "cruscottobandi/duplicabando/step3";
  }

}
