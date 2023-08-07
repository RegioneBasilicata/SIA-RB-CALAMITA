package it.csi.nembo.nemboconf.presentation.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.business.IEconomieEJB;
import it.csi.nembo.nemboconf.business.IPianoFinanziarioNemboEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.EconomiaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.RisorsaAttivataDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.TipoImportoDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.SottoMisuraDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class GestFondiController extends BaseController
{
  //FIXME: this class is temporarily disabled. To enable the access open CruscottoBandiHeader.java
  @Autowired
  private IEconomieEJB                  economieEJB         = null;
  @Autowired
  private ICruscottoBandiEJB            cruscottoEJB        = null;
  @Autowired
  private IPianoFinanziarioNemboEJB pianoFinanziarioEJB = null;

  @RequestMapping(value = "gestfondi")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    model.addAttribute("bando", bando);

    economieEJB.cleanFondiVuoti(bando.getIdBando());
    List<TipoImportoDTO> elencoTipiImporti = economieEJB
        .getElencoFondiByIdBando(bando.getIdBando());
    if (elencoTipiImporti != null && elencoTipiImporti.size() > 0)
    {
      ArrayList<Long> lIdUtente = new ArrayList<Long>();
      for (TipoImportoDTO item : elencoTipiImporti)
      {
        lIdUtente.add(item.getIdUtenteAggiornamento());
        Long idUtenteAggiornamento = item.getIdUtenteAggiornamento();
        if (!lIdUtente.contains(idUtenteAggiornamento))
        {
          lIdUtente.add(idUtenteAggiornamento);
        }
      }

      List<UtenteLogin> utenti = loadRuoloDescr(lIdUtente);
      for (TipoImportoDTO item : elencoTipiImporti)
      {
        item.setDescrizioneUtenteAggiornamento(
            getUtenteDescrizione(item.getIdUtenteAggiornamento(), utenti));
      }
      model.addAttribute("elencoTipiImporti", elencoTipiImporti);
      session.setAttribute("elencoTipiImportiSession", elencoTipiImporti);
    }
    return "gesteconomica/fondi/dettaglioDati";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "dettaglioEconomie_{idRisorseLivelloBando}", method = RequestMethod.GET)
  public String dettaglioEconomie(
      @PathVariable(value = "idRisorseLivelloBando") long idRisorseLivelloBando,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    @SuppressWarnings("unchecked")
    List<TipoImportoDTO> elencoTipiImporti = (List<TipoImportoDTO>) session
        .getAttribute("elencoTipiImportiSession");
    if (elencoTipiImporti != null)
    {
      for (TipoImportoDTO item : elencoTipiImporti)
      {
        if (item.getRisorseAttivateList() != null)
        {
          for (RisorsaAttivataDTO ris : item.getRisorseAttivateList())
          {
            if (ris.getIdRisorseLivelloBando() == idRisorseLivelloBando)
            {
              BandoDTO bando = (BandoDTO) session.getAttribute("bando");
              model.addAttribute("descrBando", bando.getDenominazione());
              model.addAttribute("descrTipoImporto", item.getDescrizione());
              model.addAttribute("elencoEconomie", ris.getElencoEconomie());
              model.addAttribute("totaleEconomie",
                  ris.getTotaleEconomieFormatted());
              return "gesteconomica/fondi/popupDettaglioEconomie";
            }
          }
        }
      }
    }
    model.addAttribute("errore",
        "Non è possibile visualizzare le economie per questo tipo importo");
    return "dialog/soloErrore";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "modificaEconomie_{idRisorseLivelloBando}", method = RequestMethod.GET)
  public String modificaEconomie(
      @PathVariable(value = "idRisorseLivelloBando") long idRisorseLivelloBando,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    @SuppressWarnings("unchecked")
    List<TipoImportoDTO> elencoTipiImporti = (List<TipoImportoDTO>) session
        .getAttribute("elencoTipiImportiSession");
    if (elencoTipiImporti != null)
    {
      for (TipoImportoDTO item : elencoTipiImporti)
      {
        if (item.getRisorseAttivateList() != null)
        {
          for (RisorsaAttivataDTO ris : item.getRisorseAttivateList())
          {
            if (ris.getIdRisorseLivelloBando() == idRisorseLivelloBando)
            {
              BandoDTO bando = (BandoDTO) session.getAttribute("bando");
              model.addAttribute("idRisorseLivelloBando",
                  ris.getIdRisorseLivelloBando());
              model.addAttribute("descrBando", bando.getDenominazione());
              model.addAttribute("descrTipoImporto", item.getDescrizione());
              if (!model.containsKey("elencoEconomie"))
              {
                model.addAttribute("elencoEconomie", ris.getElencoEconomie());
              }
              return "gesteconomica/fondi/popupModificaEconomie";
            }
          }
        }
      }
    }
    model.addAttribute("errore",
        "Non è possibile visualizzare le economie per questo tipo importo");
    return "dialog/soloErrore";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "modificaEconomie_{idRisorseLivelloBando}", method = RequestMethod.POST)
  @IsPopup
  public String modificaEconomiePost(
      @PathVariable(value = "idRisorseLivelloBando") long idRisorseLivelloBando,
      ModelMap model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<EconomiaDTO> economie = new ArrayList<EconomiaDTO>();
    EconomiaDTO economia;
    Errors error = new Errors();
    BigDecimal importobd;
    BigDecimal importobdTotale = BigDecimal.ZERO;
    String[] rowsId = request.getParameterValues("rowIndex");
    if (rowsId != null && rowsId.length > 0)
    {
      for (String id : rowsId)
      {
        if (id.equals("$$index"))
          continue;
        String descrizione = request.getParameter("descr_" + id);
        String importo = request.getParameter("val_" + id);

        error.validateMandatoryFieldMaxLength(descrizione, "descr_" + id, 200);
        importobd = error.validateBigDecimal(importo, "val_" + id, 2);
        if (importobd != null)
        {
          error.validateMandatoryBigDecimalInRange(importobd, "val_" + id,
              new BigDecimal("0.01"), new BigDecimal("9999999999.99"));
        }
        if (error.isEmpty())
        {
          importobdTotale = importobdTotale.add(importobd);
          economia = new EconomiaDTO();
          economia.setDescrizione(descrizione);
          economia.setImportoEconomia(importobd);
          economia.setIdRisorseLivelloBando(idRisorseLivelloBando);
          economie.add(economia);
        }
      }

    }

    if (!error.isEmpty())
    {

      model.addAttribute("prfEconomieRqValues", true);
      model.addAttribute("errors", error);
      return modificaEconomie(idRisorseLivelloBando, model, session);
    }
    else
    {
      try
      {
        UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
            .getAttribute("utenteAbilitazioni");
        RisorsaAttivataDTO risorsa = economieEJB
            .getRisorsa(idRisorseLivelloBando);
        economieEJB.inserisciEconomie(risorsa.getIdBando(),
            risorsa.getIdTipoImporto(), utenteAbilitazioni.getIdUtenteLogin(),
            idRisorseLivelloBando, economie);
      }
      catch (ApplicationException e)
      {
        model.addAttribute("prfEconomieRqValues", true);
        model.addAttribute("msgErrore", e.getMessage());
        model.addAttribute("elencoEconomie", economie);
        return modificaEconomie(idRisorseLivelloBando, model, session);
      }
      return "gesteconomica/fondi/success";
    }
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "sceltatipoimporto", method = RequestMethod.GET)
  public String sceltaTipoImporto(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<TipoImportoDTO> tipiImportoDisponibili = economieEJB
        .getElencoTipiImportoDisponibili(bando.getIdBando());

    if (tipiImportoDisponibili == null || tipiImportoDisponibili.size() <= 0)
    {
      model.addAttribute("msgErrore",
          "Non sono presenti tipi di importi selezionabili.");
    }
    else
    {
      model.addAttribute("elenco", tipiImportoDisponibili);
    }
    return "gesteconomica/fondi/popupSceltaTipoImporto";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "confermasceltatipoimporto_{idTipoImporto}", method = RequestMethod.GET)
  public String confermasceltatipoimporto(Model model,
      HttpServletRequest request, HttpSession session,
      @PathVariable("idTipoImporto") long idTipoImporto)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    economieEJB.insertTipoFondo(bando.getIdBando(), idTipoImporto,
        utenteAbilitazioni.getIdUtenteLogin().longValue());
    return "redirect:modificaFondi_" + idTipoImporto + ".do";
  }

  @RequestMapping(value = "modificaFondi_{idTipoImporto}", method = RequestMethod.GET)
  public String modifica(
      @PathVariable(value = "idTipoImporto") long idTipoImporto, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    model.addAttribute("bando", bando);

    List<TipoImportoDTO> elencoTipiImporti = economieEJB
        .getElencoFondiByIdBando(bando.getIdBando());
    if (elencoTipiImporti != null && elencoTipiImporti.size() > 0)
    {
      for (TipoImportoDTO item : elencoTipiImporti)
      {
        if (idTipoImporto == item.getIdTipoImporto())
        {
          if (item.getRisorseAttivateList() == null
              || item.getRisorseAttivateList().size() <= 0)
          {
            List<RisorsaAttivataDTO> risorse = new ArrayList<RisorsaAttivataDTO>();
            RisorsaAttivataDTO risorsa = new RisorsaAttivataDTO();
            risorsa.setIdLivello(new Long("0"));
            risorse.add(risorsa);
            item.setRisorseAttivateList(risorse);
          }
          else
          {
            // Inizializzo l'indice di ogni record visualizzato
            long pageIndex = 0;
            for (RisorsaAttivataDTO tmpRis : item.getRisorseAttivateList())
            {
              tmpRis.setPageIndex(pageIndex);
              pageIndex++;
            }
          }
          model.addAttribute("tipoImporto", item);
          session.setAttribute("tipoImportoSelezionato", item);
          break;
        }
      }
    }

    List<TipoOperazioneDTO> elencoOperazioni = cruscottoEJB
        .getTipiOperazioniAssociati(bando.getIdBando());
    model.addAttribute("elencoOperazioniRisorsa", elencoOperazioni);
    return "gesteconomica/fondi/modificaDati";
  }

  @RequestMapping(value = "modificaFondi_{idTipoImporto}", method = RequestMethod.POST)
  public String modificaPost(
      @PathVariable(value = "idTipoImporto") long idTipoImporto,
      @RequestParam(value = "maxRowIndex", required = true) Long maxRowIndex,
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("prfRequestValues", Boolean.TRUE);
    Errors errors = new Errors();
    // Valido i campi e creo oggetto per il salvataggio
    TipoImportoDTO tipoImporto = (TipoImportoDTO) session
        .getAttribute("tipoImportoSelezionato");

    TipoImportoDTO tipoImportoOld = new TipoImportoDTO();
    List<RisorsaAttivataDTO> risorseOld = new ArrayList<>();
    for (RisorsaAttivataDTO r : tipoImporto.getRisorseAttivateList())
    {
      risorseOld.add(r);
    }
    tipoImportoOld.setIdBando(tipoImporto.getIdBando());
    tipoImportoOld.setIdTipoImporto(tipoImporto.getIdTipoImporto());
    tipoImportoOld.setRisorseAttivateList(risorseOld);
    session.setAttribute("tipoImportoSelezionato", tipoImportoOld);

    tipoImporto.setRisorseAttivateList(new ArrayList<RisorsaAttivataDTO>());
    RisorsaAttivataDTO risorsa = null;
    Date dataFineTmp = null;
    Date dataInizioTmp = null;
    List<AmmCompetenzaDTO> elencoAmmCompetenza = null;
    AmmCompetenzaDTO ammCompetenza;
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<AmmCompetenzaDTO> ammCompetenzaDisponibili = cruscottoEJB
        .getAmmCompetenzaDisponibili(bando.getIdBando());
    BigDecimal minimoRisorsa = new BigDecimal("0.01");

    for (int i = 0; i <= maxRowIndex.longValue(); i++)
    {
      dataFineTmp = null;
      if (request.getParameter("testRow_" + i) != null)
      {
        risorsa = new RisorsaAttivataDTO();
        risorsa.setPageIndex(i);
        tipoImporto.getRisorseAttivateList().add(risorsa);
        // inizio validazione
        String idRisorseLivelloBando = request
            .getParameter("idRisorseLivelloBando_" + i);
        String descrizione = request.getParameter("descrizione_" + i);
        String risAttivate = request.getParameter("risAttivate_" + i);
        String dataInizio = request.getParameter("dataInizio_" + i);
        String dataFine = request.getParameter("dataFine_" + i);
        String idLivello = request.getParameter("operazione_" + i);
        String raggruppamento = request.getParameter("raggruppamento_" + i);
        String fondoBloccato = request.getParameter("fondoBloccato_" + i);
        String[] aIdAmmComp = request
            .getParameterValues("selectedAmmCompetenza_" + i);

        minimoRisorsa = new BigDecimal("0.01");
        if (!GenericValidator.isBlankOrNull(idRisorseLivelloBando))
        {
          risorsa.setIdRisorseLivelloBando(Long.valueOf(idRisorseLivelloBando));
          minimoRisorsa = economieEJB.getTotaleImportoLiquidatoEconomia(
              Long.valueOf(idRisorseLivelloBando));
          if (minimoRisorsa.compareTo(BigDecimal.ZERO) == 0)
          {
            minimoRisorsa = new BigDecimal("0.01");
          }
        }

        if (!GenericValidator.isBlankOrNull(raggruppamento))
        {
          errors.validateFieldMaxLength(raggruppamento, "raggruppamento_" + i,
              200);
        }

        errors.validateMandatoryFieldMaxLength(descrizione, "descrizione_" + i,
            200);

        BigDecimal bdRisAttivate = null;
        if (errors.validateMandatory(risAttivate, "risAttivate_" + i)
            && (bdRisAttivate = errors.validateBigDecimal(risAttivate,
                "risAttivate_" + i, 2)) != null)
        {
          errors.validateBigDecimalInRange(bdRisAttivate, "risAttivate_" + i,
              minimoRisorsa, new BigDecimal("9999999999.99"),
              ", valore dato dagli importi già inseriti in liste di liquidazione sommato a eventuali economie");
        }

        dataInizioTmp = errors.validateMandatoryDate(dataInizio,
            "dataInizio_" + i, true);
        if (!GenericValidator.isBlankOrNull(dataFine))
        {
          dataFineTmp = errors.validateDate(dataFine, "dataFine_" + i, true);
          if (dataFineTmp != null && dataInizioTmp != null)
          {
            errors.validateMandatoryDateInRange(dataInizioTmp,
                "dataInizio_" + i, null, dataFineTmp, false, true);
          }
        }
        errors.validateMandatory(idLivello, "operazione_" + i);

        if (aIdAmmComp != null && aIdAmmComp.length > 0)
        {
          elencoAmmCompetenza = new ArrayList<AmmCompetenzaDTO>();
          for (String id : aIdAmmComp)
          {
            ammCompetenza = new AmmCompetenzaDTO();
            ammCompetenza.setIdAmmCompetenza(Long.valueOf(id));
            if (ammCompetenzaDisponibili != null)
            {
              for (AmmCompetenzaDTO amm : ammCompetenzaDisponibili)
              {
                if (amm.getIdAmmCompetenza() == ammCompetenza
                    .getIdAmmCompetenza())
                {
                  ammCompetenza.setDescBreveTipoAmministraz(
                      amm.getDescBreveTipoAmministraz());
                  ammCompetenza.setDescEstesaTipoAmministraz(
                      amm.getDescEstesaTipoAmministraz());
                  ammCompetenza.setDescrizione(amm.getDescrizione());
                  break;
                }
              }
            }
            elencoAmmCompetenza.add(ammCompetenza);
          }
          risorsa.setElencoAmmCompetenza(elencoAmmCompetenza);
          risorsa.setFlagAmmCompetenza("N");
        }
        else
        {
          risorsa.setFlagAmmCompetenza("S"); // TUTTE LE AMM DEL BANDO MASTER
        }

        if (errors.isEmpty())
        {
          risorsa.setDescrizione(descrizione);
          risorsa.setRisorseAttivate(
              new BigDecimal(risAttivate.replace(",", ".")));
          risorsa.setDataInizio(dataInizioTmp);
          risorsa.setRaggruppamento(raggruppamento);
          risorsa.setDataFine(dataFineTmp);
          risorsa.setIdLivello(new Long(idLivello));
          String risEliminabile = request.getParameter("risEliminabile_" + i);
          risorsa.setRisorsaEliminabile("true".equals(risEliminabile));
          if (!GenericValidator.isBlankOrNull(fondoBloccato)
              && "S".equals(fondoBloccato))
          {
            risorsa.setFlagBloccato("S");
          }
          else
          {
            risorsa.setFlagBloccato("N");
          }
        }
      }
    }

    if (errors.isEmpty())
    {

      for (RisorsaAttivataDTO ris : tipoImporto.getRisorseAttivateList())
      {
        BigDecimal risorseDisponibiliPF = BigDecimal.ZERO;
      List<MisuraDTO> elencoLivelli = pianoFinanziarioEJB
          .getElencoLivelli(1);
      for (MisuraDTO m : elencoLivelli)
        for (SottoMisuraDTO s : m.getElenco())
          for (it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO t : s
              .getElenco())
            if (ris.getIdLivello() == t.getIdLivello() && t
                .getIdTipoLivello() == NemboConstants.TIPO_LIVELLO.INVESTIMENTO)
            {
              risorseDisponibiliPF = t.getRisorseDisponibili();
            }

        BigDecimal newTotTipoImporto = tipoImporto
            .getTotaleRisorseAttivateLiv(ris.getIdLivello());
        if (newTotTipoImporto == null)
          newTotTipoImporto = BigDecimal.ZERO;

        BigDecimal oldTotTipoImporto = tipoImportoOld
            .getTotaleRisorseAttivateLiv(ris.getIdLivello());
        if (oldTotTipoImporto == null)
          oldTotTipoImporto = BigDecimal.ZERO;

        BigDecimal risAttivate = newTotTipoImporto.subtract(oldTotTipoImporto);

        if ((bando
            .getIdTipoLivello() == NemboConstants.TIPO_LIVELLO.INVESTIMENTO
            && risAttivate.compareTo(risorseDisponibiliPF) > 0))
        {

          for (int i = 0; i <= maxRowIndex.longValue(); i++)
          {
            String idLivello = request.getParameter("operazione_" + i);
            Long longIdLivello = null;
            try
            {
              longIdLivello = new Long(idLivello);
            }
            catch (NumberFormatException e)
            {
              longIdLivello = null;
            }
            if (longIdLivello != null && longIdLivello == ris.getIdLivello())
              errors.addError("risAttivate_" + i,
                  "Gli importi inseriti eccedono la capienza dichiarata nel piano finanziario.");
          }
        }
      }
    }

    if (errors.isEmpty())
    {
      try
      {
        UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
            .getAttribute("utenteAbilitazioni");
        economieEJB.updateRisorseTipoImporto(tipoImporto.getIdBando(),
            tipoImporto.getIdTipoImporto(),
            utenteAbilitazioni.getIdUtenteLogin(),
            tipoImporto.getRisorseAttivateList());
      }
      catch (ApplicationException e)
      {
        model.addAttribute("bando", bando);
        List<TipoOperazioneDTO> elencoOperazioni = cruscottoEJB
            .getTipiOperazioniAssociati(bando.getIdBando());
        model.addAttribute("elencoOperazioniRisorsa", elencoOperazioni);
        model.addAttribute("tipoImporto", tipoImporto);
        model.addAttribute("msgErrore", e.getMessage());
        return "gesteconomica/fondi/modificaDati";
      }
      return "redirect:gestfondi.do";
    }
    else
    {
      model.addAttribute("bando", bando);
      List<TipoOperazioneDTO> elencoOperazioni = cruscottoEJB
          .getTipiOperazioniAssociati(bando.getIdBando());
      model.addAttribute("elencoOperazioniRisorsa", elencoOperazioni);
      model.addAttribute("tipoImporto", tipoImporto);
      model.addAttribute("errors", errors);
      return "gesteconomica/fondi/modificaDati";
    }
  }

  @RequestMapping(value = "confermaeliminarisorsa", method = RequestMethod.POST)
  @ResponseBody
  public String confermaeliminarisorsa(
      @RequestParam(value = "idRisorseLivelloBando", required = true) long idRisorseLivelloBando,
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    try
    {
      economieEJB.deleteRisorsa(idRisorseLivelloBando);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      return "KO";
    }
    return "OK";
  }

  @RequestMapping(value = "loadAmministrazioniFondiDisponibili", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadAmministrazioniFondiDisponibili(
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    List<AmmCompetenzaDTO> ammCompetenzaDisponibiliDef = null;
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<AmmCompetenzaDTO> ammCompetenzaDisponibili = cruscottoEJB
        .getAmmCompetenzaDisponibiliFondi(bando.getIdBando());
    session.setAttribute("ammCompetenzaDisponibili", ammCompetenzaDisponibili);
    ammCompetenzaDisponibiliDef = ammCompetenzaDisponibili;
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (ammCompetenzaDisponibiliDef != null
        && ammCompetenzaDisponibiliDef.size() > 0)
    {
      for (AmmCompetenzaDTO amm : ammCompetenzaDisponibiliDef)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("IdAmmCompetenza",
            String.valueOf(amm.getIdAmmCompetenza()));
        parameters.put("descrizioneEstesa", amm.getDescrizioneEstesa());
        parameters.put("gruppo", amm.getDescEstesaTipoAmministraz());
        values.put(String.valueOf(amm.getIdAmmCompetenza()), parameters);
      }
    }

    return values;
  }

  @RequestMapping(value = "loadAmministrazioniFondiSelezionate_{rowIndex}_{idRisorseLivelloBando}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadAmministrazioniFondiSelezionate(
      @PathVariable(value = "rowIndex") long rowIndex,
      @PathVariable(value = "idRisorseLivelloBando") long idRisorseLivelloBando,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (idRisorseLivelloBando <= 0)
    {
      return values;
    }

    List<AmmCompetenzaDTO> ammCompetenzaAssociateDef = new ArrayList<AmmCompetenzaDTO>();
    List<AmmCompetenzaDTO> ammCompetenzaAssociate = cruscottoEJB
        .getAmmCompetenzaRisorsa(idRisorseLivelloBando);

    if (ammCompetenzaAssociate != null)
      ammCompetenzaAssociateDef = ammCompetenzaAssociate;

    // Ora devo aggiungere (se non presenti) quelle già selezionate dall'utente.
    // questo è necessario perchè il plugin dual-list genera id statici quindi
    // bisogna ricaricarlo ogni volta che si aprono le popup amministrazioni e
    // tipi operazioni
    /*
     * String[] idAmmSelezionate =
     * request.getParameterValues("selectedAmmCompetenza_"+rowIndex);
     * if(idAmmSelezionate != null && idAmmSelezionate.length>0) { boolean
     * trovato; AmmCompetenzaDTO ammTrovata = null; List<AmmCompetenzaDTO>
     * ammCompetenzaDisponibili = null; if(ammCompetenzaAssociate == null){
     * ammCompetenzaDisponibili =
     * (List<AmmCompetenzaDTO>)session.getAttribute("ammCompetenzaDisponibili");
     * } for(int x=0; x<idAmmSelezionate.length; x++) { String idAmm =
     * idAmmSelezionate[x]; trovato = false; if(ammCompetenzaAssociate == null)
     * { for(int i=0; i<ammCompetenzaDisponibili.size(); i++) { AmmCompetenzaDTO
     * amm = ammCompetenzaDisponibili.get(i); if(Long.parseLong(idAmm) ==
     * amm.getIdAmmCompetenza()) { trovato = true; ammTrovata = amm; } } } else
     * { for(int i=0; i<ammCompetenzaAssociate.size(); i++) { AmmCompetenzaDTO
     * amm = ammCompetenzaAssociate.get(i); if(Long.parseLong(idAmm) ==
     * amm.getIdAmmCompetenza()) { trovato = true; ammTrovata = amm; } } }
     * if(trovato) { ammCompetenzaAssociateDef.add(ammTrovata); } } }
     */

    if (ammCompetenzaAssociateDef != null
        && ammCompetenzaAssociateDef.size() > 0)
      for (AmmCompetenzaDTO amm : ammCompetenzaAssociateDef)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("IdAmmCompetenza",
            String.valueOf(amm.getIdAmmCompetenza()));
        parameters.put("descrizioneEstesa", amm.getDescrizioneEstesa());
        parameters.put("gruppo", amm.getDescEstesaTipoAmministraz());
        values.put(String.valueOf(amm.getIdAmmCompetenza()), parameters);
      }

    return values;
  }

}
