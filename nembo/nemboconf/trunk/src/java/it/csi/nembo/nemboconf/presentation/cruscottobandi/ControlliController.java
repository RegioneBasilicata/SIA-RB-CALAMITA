package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.SoluzioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@SuppressWarnings("unchecked")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class ControlliController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "controlli", method = RequestMethod.GET)
  public String controlliGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> elenco = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando);
    session.setAttribute("elencoGruppiOggettiControlli", elenco);
    model.addAttribute("idGruppoSelezionato", 0);
    model.addAttribute("elenco", elenco);
    model.addAttribute("idBando", idBando);
    return "cruscottobandi/controlli";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaControlli_{idGruppoOggetto}", method = RequestMethod.GET)
  public String visualizzaControlliGet(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable(value = "idGruppoOggetto") String idUnivoco)
      throws InternalUnexpectedException
  {
    return visualizzaControlli(model, request, session, idUnivoco);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaControlli", method = RequestMethod.POST)
  public String visualizzaControlli(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "selGruppoOggetto") String idUnivoco)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    long idGruppoOggetto = Long.parseLong(idUnivoco.split("&&")[0]);
    long idOggetto = Long.parseLong(idUnivoco.split("&&")[1]);
    String descrGruppo = getDescrGruppoSelezionato(idUnivoco, session);
    List<ControlloDTO> controlli = cruscottoEJB
        .getElencoControlliDisponibili(idBando, idGruppoOggetto, idOggetto);
    if (controlli == null || controlli.size() <= 0)
    {
      model.addAttribute("msgWarning",
          "Non sono stati trovati controlli relativi ai quadri selezionati per il gruppo \" "
              + descrGruppo + " \"");
    }

    session.setAttribute("elencoControlli", controlli);
    session.setAttribute("idGruppoSelezionatoSession", idUnivoco);
    session.setAttribute("idBando", idBando);

    model.addAttribute("descrGruppoContrSelezionato", descrGruppo);
    model.addAttribute("idGruppoSelezionato", idUnivoco);
    model.addAttribute("idGruppoOggettoSelezionato", idUnivoco);
    model.addAttribute("controlli", controlli);
    model.addAttribute("elenco",
        session.getAttribute("elencoGruppiOggettiControlli"));
    model.addAttribute("idBando", idBando);
    return "cruscottobandi/controlli";
  }

  private String getDescrGruppoSelezionato(String idUnivoco,
      HttpSession session)
  {
    List<GruppoOggettoDTO> elenco = (List<GruppoOggettoDTO>) session
        .getAttribute("elencoGruppiOggettiControlli");
    String descrGruppoSelezionato = "";
    if (elenco != null)
    {
      for (GruppoOggettoDTO item : elenco)
      {
        if (item.getIdUnivoco().equals(idUnivoco))
        {
          descrGruppoSelezionato = item.getDescrizioneEstesa();
        }
      }
    }
    return descrGruppoSelezionato;
  }

  @RequestMapping(value = "avanti", method = RequestMethod.POST)
  public String avanti(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idGruppoOggettoSelezionato") String idUnivoco)
      throws InternalUnexpectedException
  {

    /*
     * Per tutti i rek presenti sulla tabella NEMBO_R_BANDO_OGGETTO_CONTROLL
     * legati ai rek di NEMBO_R_BANDO_OGGETTO del bando in modifica (quindi non
     * solo quelli visualizzati nella pagina), se sono collegati ad un rek su
     * NEMBO_D_CONTROLLO con DESC_PARAMETRO not null devono avere almeno un rek
     * sulla tabella NEMBO_T_VALORI_PARAMETRI
     */
    String msgErrore = "";
    List<ControlloDTO> controlli = (List<ControlloDTO>) session
        .getAttribute("elencoControlli");
    if (controlli != null)
    {
      for (ControlloDTO item : controlli)
      {
        if (item.getIdControlloInserito() != 0
            && !GenericValidator.isBlankOrNull(item.getDescrParametro()))
        {
          if (!cruscottoEJB.esisteValoreParametro(item.getIdBandoOggetto(),
              item.getIdControllo()))
          {
            msgErrore = msgErrore + "\n"
                + "Non hai inserito nessun parametro sul controllo "
                + item.getCodice() + "!";
            model.addAttribute("controlli",
                session.getAttribute("elencoControlli"));
          }
          if (!cruscottoEJB
              .checkControlliObbligatoriInseriti(item.getIdBandoOggetto()))
          {
            msgErrore = "Impossibile procedere con l'attivazione dell'oggetto. E' necessario prima confermare almeno i controlli obbligatori!";
          }
        }
      }
    }
    // Per ciscun oggetto istanza devo vedere che sia stato messo almeno un
    // controllo
    if (!GenericValidator.isBlankOrNull(msgErrore))
    {
      model.addAttribute("msgErrore", msgErrore);
      model.addAttribute("idGruppoSelezionato",
          session.getAttribute("idGruppoSelezionatoSession"));
      model.addAttribute("idGruppoOggettoSelezionato", idUnivoco);
      model.addAttribute("elenco",
          session.getAttribute("elencoGruppiOggettiControlli"));
      model.addAttribute("idBando", session.getAttribute("idBando"));
      return "cruscottobandi/controlli";
    }
    return "redirect:dichiarazioni.do";
  }

  @RequestMapping(value = "conferma", method = RequestMethod.POST)
  public String conferma(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    String gravitaInserita = "";
    String giustificabileInserito = "";

    long idBandoOggetto = -1;
    String msgLogFinal = "";
    String msg = "";
    List<ControlloDTO> controlli = (List<ControlloDTO>) session
        .getAttribute("elencoControlli");

    if (controlli != null)
    {
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");
      long idBando = bando.getIdBando();
      String oggetto = "";
      List<GruppoOggettoDTO> elenco = cruscottoEJB
          .getElencoGruppiControlliDisponibili(idBando);
      for (GruppoOggettoDTO g : elenco)
        if (g.getIdOggetto() == controlli.get(0).getIdOggetto())
          oggetto = g.getDescrGruppo();
      msg += oggetto + " - " + controlli.get(0).getDescrOggetto() + " ";

      for (ControlloDTO item : controlli)
      {
        String msgLog = "";

        if (!"S".equals(item.getFlagAttivo()))
        {
          idBandoOggetto = item.getIdBandoOggetto();
          gravitaInserita = request
              .getParameter("gravitaInserita_" + item.getIdUnivoco());
          // Map<String, String[]> xx = request.getParameterMap();
          giustificabileInserito = request
              .getParameter("chkGiustificabilePrevisto_" + item.getIdUnivoco());
          if (giustificabileInserito == null
              || giustificabileInserito.compareTo("") == 0)
            giustificabileInserito = "N";
          else
            giustificabileInserito = "S";

          if (gravitaInserita.compareTo(" ") == 0
              && item.getGravitaInserita() != null)
          {
            if (msgLog == "")
              msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                  + "\" - Controllo: \"" + item.getDescrizione() + "\"";
            msgLog += " - Deselezionato";
          }
          else
            if (gravitaInserita != "" && item.getGravitaInserita() == null)
            {
              if (msgLog == "")
                msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                    + "\" - Controllo: \"" + item.getDescrizione() + "\"";
              msgLog += " - Selezionato con gravità: \"" + gravitaInserita
                  + "\"";
            }
            else
              if (gravitaInserita != null && gravitaInserita != ""
                  && item.getGravitaInserita() != null
                  && item.getGravitaInserita() != "")
              {
                if (item.getGravitaInserita().compareTo(gravitaInserita) != 0)
                {
                  if (msgLog == "")
                    msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                        + "\" - Controllo: \"" + item.getDescrizione() + "\"";
                  msgLog += " - Gravità modificata da \""
                      + item.getGravitaInserita() + "\" a \"" + gravitaInserita
                      + "\"";
                }
              }
          item.setGravitaInserita(gravitaInserita);
          if (GenericValidator.isBlankOrNull(gravitaInserita)
              && "S".equals(item.getFlagObbligatorio()))
          {
            item.setGravitaInserita(item.getGravita());
          }

          if (item.getGravitaInserita() != null
              && item.getGravitaInserita() != "")
          {
            if (item.getFlagGiustificabileBando() != null
                && item.getFlagGiustificabileBando()
                    .compareTo(giustificabileInserito) != 0)
            {
              if (msgLog == "")
                msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                    + "\" - Controllo: \"" + item.getDescrizione() + "\"";
              msgLog += " - Cambio flag giustificazione da "
                  + item.getFlagGiustificabileBando() + " a "
                  + giustificabileInserito;
            }
            if (item.getFlagGiustificabileBando() == null
                && giustificabileInserito != null)
            {
              if (msgLog == "")
                msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                    + "\" - Controllo: \"" + item.getDescrizione() + "\"";
              msgLog += " - Inserito flag giustificazione a "
                  + giustificabileInserito;
            }
          }
          else
            if (item.getFlagGiustificabileBando() != null
                && item.getFlagGiustificabileBando()
                    .compareTo(giustificabileInserito) != 0)
            {
              if (msgLog == "")
                msgLog += "\nQuadro : \"" + item.getDescrQuadro()
                    + "\" - Controllo: \"" + item.getDescrizione() + "\"";
              msgLog += " - Cambio flag giustificazione da "
                  + item.getFlagGiustificabileBando() + " a "
                  + giustificabileInserito;
            }
          item.setFlagGiustificabileBando(giustificabileInserito);

          if (msgLog != "")
            msgLogFinal += msgLog;
        }
      }
      cruscottoEJB.updateControlli(controlli,
          getUtenteAbilitazioni(session).getIdUtenteLogin(), idBandoOggetto,
          msg + msgLogFinal);

      return visualizzaControlli(model, request, session,
          (String) session.getAttribute("idGruppoSelezionatoSession"));
    }
    else
    {
      return controlliGet(model, request, session);
    }
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "parametri_{idControllo}_{idBandoOggetto}", method = RequestMethod.GET)
  public String parametri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idControllo") long idControllo,
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<ControlloDTO> controlli = (List<ControlloDTO>) session
        .getAttribute("elencoControlli");
    for (ControlloDTO item : controlli)
    {
      if ((item.getIdControllo() == idControllo)
          && (item.getIdBandoOggetto() == idBandoOggetto))
      {
        session.setAttribute("parametriControllo", item);
        if (item.getIdControlloInserito() == 0)
        {
          model.addAttribute("errore",
              "Prima di inserire i parametri sul controllo " + item.getCodice()
                  + " devi confermare le modifiche!");
          return "dialog/soloErrore";
        }
        else
        {
          List<DecodificaDTO<Long>> parametri = cruscottoEJB
              .getParametriControllo(item.getIdBandoOggetto(),
                  item.getIdControllo());
          model.addAttribute("idBando", idBando);
          model.addAttribute("codice", item.getCodice());
          model.addAttribute("descrizione", item.getDescrizione());
          model.addAttribute("datiTecnici", item.getDescrParametro());
          model.addAttribute("parametri", parametri);

          if ("S".equals(item.getFlagAttivo()))
          {
            model.addAttribute("bandoAttivo", Boolean.TRUE);
          }
          return "cruscottobandi/parametriControlli";
        }
      }
    }
    model.addAttribute("errore",
        "Non è possibile gestire i parametri aggiuntivi per questo controllo");
    return "dialog/soloErrore";
  }

  @RequestMapping(value = "confermaparametri", method = RequestMethod.POST)
  @ResponseBody
  public String confermaParametri(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    ControlloDTO controllo = (ControlloDTO) session
        .getAttribute("parametriControllo");
    String[] valoriParametri = request.getParameterValues("parametri");
    List<DecodificaDTO<Long>> paramControlloOld = cruscottoEJB
        .getParametriControllo(controllo.getIdBandoOggetto(),
            controllo.getIdControllo());
    cruscottoEJB.aggiornaValoriParametri(controllo.getIdBandoOggetto(),
        controllo.getIdControllo(), Arrays.asList(valoriParametri));
    List<DecodificaDTO<Long>> paramControlloNew = cruscottoEJB
        .getParametriControllo(controllo.getIdBandoOggetto(),
            controllo.getIdControllo());

    boolean inserted = false;
    String msgLog = controllo.getDescrOggetto() + "\nQuadro \""
        + controllo.getDescrQuadro() + "\" - Controllo: \""
        + controllo.getDescrizione()
        + "\" \nAggiornamento parametri aggiuntivi: ";

    msgLog += " \nParametri precedenti : \"";
    for (DecodificaDTO<Long> p : paramControlloOld)
    {
      msgLog += p.getDescrizione() + " - ";
      inserted = true;
    }
    if (inserted) // tolgo ultimi 3 caratteri se ho inserito dei valori. Tolgo
                  // "spazio trattino spazio" - solo per questione di
                  // visualizzazione
      msgLog = msgLog.substring(0, msgLog.length() - 3);

    inserted = false;
    msgLog += "\" \nParametri aggiornati : \"";

    for (DecodificaDTO<Long> p : paramControlloNew)
    {
      msgLog += p.getDescrizione() + " - ";
      inserted = true;
    }
    if (inserted)
      msgLog = msgLog.substring(0, msgLog.length() - 3);

    msgLog += "\"";

    boolean uguali = true;

    if (paramControlloOld.size() != paramControlloNew.size())
      uguali = false;
    else
    {
      Iterator<DecodificaDTO<Long>> itOld = paramControlloOld.iterator();
      Iterator<DecodificaDTO<Long>> itNew = paramControlloNew.iterator();

      while (itOld.hasNext() && itNew.hasNext())
        if (itOld.next().getDescrizione()
            .compareTo(itNew.next().getDescrizione()) != 0)
          uguali = false;

    }

    if (!uguali)
    {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");
      long idBando = bando.getIdBando();
      String oggetto = "";
      List<GruppoOggettoDTO> elenco = cruscottoEJB
          .getElencoGruppiControlliDisponibili(idBando);
      for (GruppoOggettoDTO g : elenco)
        if (g.getIdOggetto() == controllo.getIdOggetto())
          oggetto = g.getDescrGruppo();
      cruscottoEJB.logAttivitaBandoOggetto(controllo.getIdBandoOggetto(),
          idUtente, NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.CONTROLLI,
          oggetto + " - " + msgLog);

    }

    return "OK";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @IsPopup
  @RequestMapping(value = "getDettaglioRisoluzioni_{idControllo}", method = RequestMethod.GET)
  public String getDettaglioRisoluzioni(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable(value = "idControllo") String idControllo)
      throws InternalUnexpectedException
  {

    ControlloDTO controllo = cruscottoEJB.getControlloById(idControllo);
    List<SoluzioneDTO> soluzioni = cruscottoEJB
        .getSoluzioniControllo(idControllo);

    model.addAttribute("controllo", controllo);
    model.addAttribute("soluzioni", soluzioni);

    return "cruscottobandi/popupGiustificazioneControllo";
  }

}
