package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloAmministrativoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LivelloDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class ControlliTecnicoAmministrativiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "controlliAmm", method = RequestMethod.GET)
  public String controlliGet(ModelMap model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<DecodificaDTO<String>> elenco = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando, "CTRAM");

    List<DecodificaDTO<String>> elencoAMP = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando, "CTAMP");

    if (elenco != null)
    {
      if (elencoAMP != null)
        elenco.addAll(elencoAMP);
    }
    else
      if (elencoAMP != null)
        elenco = elencoAMP;

    populateCommonModel(model, request, session, 0, 0L);
    model.addAttribute("gruppiDisponibili", elenco);
    model.addAttribute("idGruppoSelezionato", 0);

    return "cruscottobandi/controlliAmministrativi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaControlliAmm", method = RequestMethod.POST)
  public String visualizzaControlli(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idQuadroOggettoEBandoOggetto") String idQuadroOggettoEBandoOggetto)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");

    Long idQuadroOggetto = Long
        .parseLong(idQuadroOggettoEBandoOggetto.split("_")[0]);
    Long idBandoOggetto = Long
        .parseLong(idQuadroOggettoEBandoOggetto.split("_")[1]);

    model.addAttribute("idQuadroOggettoEBandoOggetto",
        idQuadroOggettoEBandoOggetto);

    DecodificaDTO<Long> infoBandoOggetto = cruscottoEJB.getInfoBandoOggetto(
        bando.getIdBando(), idQuadroOggetto, idBandoOggetto);
    List<ControlloAmministrativoDTO> elenco = cruscottoEJB
        .getElencoControlliAmministrativiByIdQuadroOggetto(
            idQuadroOggetto, infoBandoOggetto.getId().longValue());

    if (elenco != null)
    {
      if (elenco != null && elenco.size() > 0
          && "S".equals(elenco.get(0).getFlagAttivo()))
      {
        model.addAttribute("bandoOggettoAttivo", "S");
      }

      for (ControlloAmministrativoDTO ctrl : elenco)
        for (ControlloAmministrativoDTO figlio : ctrl.getControlliFigli())
          if (figlio.getFlagObbligatorio().compareTo("S") == 0)
            figlio.setFlagSelezionato("S");

      model.addAttribute("elencoControlliTecnici", elenco);
      model.addAttribute("idQuadroOggetto", idQuadroOggetto);
      model.addAttribute("idBandoOggetto", idBandoOggetto);

    }
    else
    {
      model.addAttribute("msgNessunControllo", Boolean.TRUE);
    }
    model.addAttribute("ricercaeffettuata", Boolean.TRUE);
    populateCommonModel(model, request, session, idQuadroOggetto,
        idBandoOggetto);
    return "cruscottobandi/controlliAmministrativi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "gestione_livelli_{idBandoOggetto}_{idQuadroOggControlloAmm}", method = RequestMethod.GET)
  public String parametri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      @PathVariable(value = "idQuadroOggControlloAmm") long idQuadroOggControlloAmm)
      throws InternalUnexpectedException
  {
    model.addAttribute("idQuadroOggControlloAmm", idQuadroOggControlloAmm);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    return "cruscottobandi/popupGestioneLivelli";
  }

  @RequestMapping(value = "loadLivelliDisponibili_{idBandoOggetto}_{idQuadroOggControlloAmm}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<LivelloDTO> loadLivelliDisponibili(HttpSession session,
      Model model,
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      @PathVariable(value = "idQuadroOggControlloAmm") long idQuadroOggControlloAmm)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoLivelliDisponibili(idBandoOggetto,
        idQuadroOggControlloAmm);
  }

  @RequestMapping(value = "loadLivelliSelezionati_{idBandoOggetto}_{idQuadroOggControlloAmm}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<LivelloDTO> loadLivelliSelezionati(HttpSession session,
      Model model,
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      @PathVariable(value = "idQuadroOggControlloAmm") long idQuadroOggControlloAmm)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoLivelliSelezionati(idBandoOggetto,
        idQuadroOggControlloAmm);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "conferma_livelli_{idBandoOggetto}_{idQuadroOggControlloAmm}", method = RequestMethod.POST)
  public String parametriPost(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      @PathVariable(value = "idQuadroOggControlloAmm") long idQuadroOggControlloAmm)
      throws InternalUnexpectedException
  {
    String[] aIdControlli = request.getParameterValues("selectedList");
    cruscottoEJB.updateControlliLivelli(idBandoOggetto, idQuadroOggControlloAmm,
        aIdControlli);
    return controlliGet(model, request, session);
  }

  public void populateCommonModel(ModelMap model, HttpServletRequest request,
      HttpSession session,
      long idQuadroOggetto, Long idBandoOggetto)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<DecodificaDTO<String>> elencoGruppiDisponibili = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando,
            "CTRAM");
    List<DecodificaDTO<String>> elencoAMP = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando, "CTAMP");

    if (elencoGruppiDisponibili != null)
    {
      if (elencoAMP != null)
        elencoGruppiDisponibili.addAll(elencoAMP);
    }
    else
      if (elencoAMP != null)
        elencoGruppiDisponibili = elencoAMP;

    if (elencoGruppiDisponibili != null)
    {
      for (DecodificaDTO<String> item : elencoGruppiDisponibili)
      {

        if (item.getId().compareTo(idQuadroOggetto + "_" + idBandoOggetto) == 0)
        {
          model.addAttribute("descrGruppoContrSelezionato",
              item.getDescrizioneEstesa());
          break;
        }
      }
      model.addAttribute("gruppiDisponibili", elencoGruppiDisponibili);
    }
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
  }

  @RequestMapping(value = "confermacontrollitecnici", method = RequestMethod.POST)
  public String conferma(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String[] vIds = request.getParameterValues("idQuadroOggControlloAmm"); // ogni
    // value
    // è
    // una
    // stringa
    // del
    // tipo
    // idQuadroOggControlloAmm&&idControlloPadre
    String codice;
    String descrizione = null;
    String strIdQuadroOggControlloAmm;
    String strIdControlloPadre;
    Errors errors = new Errors();
    model.addAttribute("preferRqParam", Boolean.TRUE);

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    DecodificaDTO<Long> infoBandoOggetto = cruscottoEJB
        .getIdBandoOggetto(bando.getIdBando(), idQuadroOggetto);
    long idBandoOggetto = infoBandoOggetto.getId().longValue();
    List<ControlloAmministrativoDTO> elencoTmp = cruscottoEJB
        .getElencoControlliAmministrativiByIdQuadroOggetto(idQuadroOggetto,
            idBandoOggetto);
    List<ControlloAmministrativoDTO> elencoOld = new LinkedList<ControlloAmministrativoDTO>();

    // clone list per il log
    for (ControlloAmministrativoDTO ctrl : elencoTmp)
    {
      ControlloAmministrativoDTO nuovo = new ControlloAmministrativoDTO();
      nuovo.setCodice(ctrl.getCodice());
      nuovo.setDescrizione(ctrl.getDescrizione());
      nuovo.setFlagSelezionato(ctrl.getFlagObbligatorio());

      List<ControlloAmministrativoDTO> figli = new LinkedList<ControlloAmministrativoDTO>();

      for (ControlloAmministrativoDTO c : ctrl.getControlliFigli())
      {
        ControlloAmministrativoDTO nuovoFiglio = new ControlloAmministrativoDTO();
        nuovoFiglio.setCodice(c.getCodice());
        nuovoFiglio.setDescrizione(c.getDescrizione());
        nuovoFiglio.setFlagSelezionato(c.getFlagSelezionato());
        figli.add(nuovoFiglio);

      }
      nuovo.setControlliFigli(figli);
      elencoOld.add(nuovo);
    }

    for (ControlloAmministrativoDTO ctrl : elencoTmp)
    {
      ctrl.setFlagSelezionato("N");
      for (ControlloAmministrativoDTO child : ctrl.getControlliFigli())
      {
        child.setFlagSelezionato("N");
      }
    }

    // Valido e ricreo oggetto per la view e per gli aggiornamenti
    for (String item : vIds)
    {
      if (item.indexOf("idUnivoco") > 0)
      {
        continue;
      }
      if (item.indexOf("&&") >= 0)
      {
        // In questo caso ho selezionato un figlio
        strIdQuadroOggControlloAmm = item.split("&&")[0];
        strIdControlloPadre = item.split("&&")[1];
      }
      else
      {
        // In questo caso ho selezionato un padre
        strIdQuadroOggControlloAmm = null;
        strIdControlloPadre = item;
      }

      ControlloAmministrativoDTO dto = new ControlloAmministrativoDTO();
      long idRif = Long.parseLong((strIdQuadroOggControlloAmm == null) ? "0"
          : strIdQuadroOggControlloAmm);
      long idRifPadre = Long.parseLong(strIdControlloPadre);

      if (idRif < 0)
      {
        codice = request.getParameter("codice_" + strIdQuadroOggControlloAmm);
        descrizione = request
            .getParameter("descrizione_" + strIdQuadroOggControlloAmm);
        errors.validateMandatoryFieldLength(codice, 1, 20,
            "codice_" + strIdQuadroOggControlloAmm, true);
        errors.validateMandatoryFieldLength(descrizione, 1, 500,
            "descrizione_" + strIdQuadroOggControlloAmm,
            true);
        dto.setCodice(codice);
        dto.setDescrizione(descrizione);
        dto.setIdQuadroOggControlloAmm(idRif);
        dto.setIdControlloAmministratPadre(idRifPadre);
        dto.setFlagSelezionato("S");
        dto.setFlagObbligatorio("S");
        // Cerco il padre e gli aggancio il figlio
        for (ControlloAmministrativoDTO ctrl : elencoTmp)
        {
          if (ctrl.getIdControlloAmministrativo() == idRifPadre)
          {
            ctrl.getControlliFigli().add(dto);
          }
        }
      }
      else
      {
        // Sistemo il flag selezionato per fare in modo di checkare il
        // check button
        for (ControlloAmministrativoDTO ctrl : elencoTmp)
        {
          if (ctrl.getIdControlloAmministrativo() == idRifPadre)
          {
            if (strIdQuadroOggControlloAmm == null)
            {
              ctrl.setFlagSelezionato("S");
              for (ControlloAmministrativoDTO figlio : ctrl.getControlliFigli())
              {
                figlio.setFlagSelezionato("S");
              }
            }
            else
            {
              for (ControlloAmministrativoDTO figlio : ctrl.getControlliFigli())
              {
                if (figlio.getIdQuadroOggControlloAmm() == idRif)
                {
                  figlio.setFlagSelezionato("S");
                }
              }
            }
            break;
          }
        }
      }
    }
    if (!errors.isEmpty())
    {
      model.addAttribute("elencoControlliTecnici", elencoTmp);
      model.addAttribute("errors", errors);

      populateCommonModel(model, request, session, idQuadroOggetto,
          idBandoOggetto);
      return "cruscottobandi/controlliAmministrativi";
    }
    else
    {

      cruscottoEJB.updateControlliTecnici(idBandoOggetto, idQuadroOggetto,
          elencoTmp);
      logOperation("controlli tecnico amm", idBandoOggetto, idQuadroOggetto,
          elencoOld, elencoTmp, session);

      return visualizzaControlli(model, request, session,
          idQuadroOggetto + "_" + idBandoOggetto);
    }

  }

  public void logOperation(String type, long idBandoOggetto,
      long idQuadroOggetto,
      List<ControlloAmministrativoDTO> elencoOld,
      List<ControlloAmministrativoDTO> elencoTmp, HttpSession session)
      throws InternalUnexpectedException
  {
    String msgLog = "";
    String msgDescr = "";
    boolean modified = false;

    if (elencoOld.size() != elencoTmp.size())
    {
      modified = true;
    }
    else
    {
      // se invece modifico quelli esistenti riporto solo le modifiche
      // effettuate
      Iterator<ControlloAmministrativoDTO> itPrec = elencoOld.iterator();
      Iterator<ControlloAmministrativoDTO> itSucc = elencoTmp.iterator();
      while (itPrec.hasNext() && itSucc.hasNext())
      {
        ControlloAmministrativoDTO p = itPrec.next();
        ControlloAmministrativoDTO s = itSucc.next();

        if (p.getControlliFigli().size() != s.getControlliFigli().size())
        {
          modified = true;

          Iterator<ControlloAmministrativoDTO> itDetPrec = p.getControlliFigli()
              .iterator();
          Iterator<ControlloAmministrativoDTO> itDetSucc = s.getControlliFigli()
              .iterator();
          ControlloAmministrativoDTO ds = null;

          while (itDetSucc.hasNext())
          {
            if (itDetPrec.hasNext())
            {
              itDetSucc.next();
              itDetPrec.next();
            }
            else
            {
              ds = itDetSucc.next();
              msgLog += "\nAggiunto nel controllo padre: \""
                  + s.getDescrizione()
                  + "\" il figlio con codice: \"" + ds.getCodice()
                  + "\" e descrizione: \""
                  + ds.getDescrizione() + "\" e flag: \""
                  + ds.getFlagSelezionato() + "\"";
            }
          }

        }

        Iterator<ControlloAmministrativoDTO> itDetPrec = p.getControlliFigli()
            .iterator();
        Iterator<ControlloAmministrativoDTO> itDetSucc = s.getControlliFigli()
            .iterator();
        while (itDetPrec.hasNext() && itDetSucc.hasNext())
        {
          ControlloAmministrativoDTO dp = itDetPrec.next();
          ControlloAmministrativoDTO ds = itDetSucc.next();

          if (dp.getFlagSelezionato().compareTo(ds.getFlagSelezionato()) != 0)
          {
            modified = true;
            msgLog += "\nControllo padre: \"" + s.getDescrizione() + "\"";
            msgLog += "\nModifica del flag del figlio \"" + dp.getCodice()
                + "\" con codice \""
                + dp.getDescrizione() + "\" da: \"" + dp.getFlagSelezionato()
                + "\" a: \""
                + ds.getFlagSelezionato() + "\"";
          }
        }
      }
    }

    msgDescr += msgLog;
    msgLog = msgDescr;

    if (modified)
    {
      // get descrizione gruppo selezionato
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");
      long idBando = bando.getIdBando();
      String descrGruppoSel = "";
      List<DecodificaDTO<String>> elencoGruppiDisponibili = cruscottoEJB
          .getElencoGruppiControlliDisponibili(idBando,
              "CTRAM");
      List<DecodificaDTO<String>> elencoAMP = cruscottoEJB
          .getElencoGruppiControlliDisponibili(idBando, "CTAMP");

      if (elencoGruppiDisponibili != null)
      {
        if (elencoAMP != null)
          elencoGruppiDisponibili.addAll(elencoAMP);
      }
      else
        if (elencoAMP != null)
          elencoGruppiDisponibili = elencoAMP;

      if (elencoGruppiDisponibili != null)
      {
        for (DecodificaDTO<String> item : elencoGruppiDisponibili)
        {
          if (item.getId()
              .compareTo(idQuadroOggetto + "_" + idBandoOggetto) == 0)
          {
            descrGruppoSel = item.getDescrizioneEstesa();
            break;
          }
        }
      }

      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.CONTROLLI_TEC_AMM,
          descrGruppoSel + " " + msgLog);
    }

  }

}
