package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LivelloDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class AttivazioneController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB            cruscottoEJB        = null;
  @Autowired
  private IStampeEJB                    stampeEJB            = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "attivazione", method = RequestMethod.GET)
  public String oggettiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();

    List<GruppoOggettoDTO> oggettiBando = cruscottoEJB
        .getElencoGruppiOggetti(idBando);
    session.setAttribute("elencoOggettiBando", oggettiBando);

    model.addAttribute("stampe",
        stampeEJB.getMapCuStampePerBandoOggettoByIdBando(idBando));
    model.addAttribute("idBando", idBando);
    model.addAttribute("bando", bando);
    model.addAttribute("elenco", oggettiBando);

    return "cruscottobandi/attiva";
  }

  @RequestMapping(value = "attiva_{idBandoOggetto}", method = RequestMethod.GET)
  public String eliminaintervento(
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    // Verifico che ci sia almeno un controllo inserito per questo bando_oggetto
    // NEMBO_R_BANDO_OGGETTO_CONTROLL
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    if (!cruscottoEJB.checkQuadroInserito(idBandoOggetto))
    {
      model.addAttribute("errore",
          "Impossibile procedere con l'attivazione dell'oggetto. E' necessario prima inserire almeno un quadro!");
      return "dialog/soloErrore";
    }
    else
      if (!cruscottoEJB.checkControlliObbligatoriInseriti(idBandoOggetto))
      {
        model.addAttribute("errore",
            "Impossibile procedere con l'attivazione dell'oggetto. E' necessario prima confermare almeno i controlli obbligatori!");
        return "dialog/soloErrore";
      }
      else
        if (!cruscottoEJB.checkParametriControlliInseriti(idBandoOggetto))
        {
          model.addAttribute("errore",
              "Impossibile procedere con l'attivazione dell'oggetto. E' necessario prima inserire i parametri aggiuntivi per i controlli selezioanti!");
          return "dialog/soloErrore";
        }
        else
          if (cruscottoEJB.isOggettoDomandaPagamentoGAL(idBandoOggetto)
              && !utenteAbilitazioni.isUtenteOPR())
          {
            model.addAttribute("errore",
                "Per attivare/disattivare oggetti relativi alle domande di pagamento su bandi GAL accedere con ruolo OPR/ARPEA");
            return "dialog/soloErrore";
          }
          else
            if (!cruscottoEJB.controlloInfoCatalogo(idBandoOggetto))
            {
              model.addAttribute("errore",
                  "Impossibile procedere con l'attivazione: verificare che siano stati selezionati tutti gli elementi obbligatori del catalogo (Impegni, Allegati, Dichiarazioni).");
              return "dialog/soloErrore";
            }

    /*
     * CONTROLLO PRESENZA E VALIDITA' BUDGET All’attivazione del primo oggetto
     * di un bando, ovvero se non c’è alcun record su NEMBO_R_BANDO_OGGETTO con
     * FLAG_ATTIVO = ‘N’
     */
    if (cruscottoEJB
        .isPrimoOggettoAttivato(cruscottoEJB.getIdBando(idBandoOggetto)))
    {
      BigDecimal risorseDisponibili = BigDecimal.ZERO;
      BandoDTO bando = cruscottoEJB
          .getInformazioniBando(cruscottoEJB.getIdBando(idBandoOggetto));
      List<LivelloDTO> livelliBando = cruscottoEJB
          .getLivelliBando(cruscottoEJB.getIdBando(idBandoOggetto));
    }

    model.addAttribute("message",
        "Stai per attivare l'oggetto scelto, vuoi continuare? ");
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    return "cruscottobandi/confermaAttivazioneOggetto";

  }

  @RequestMapping(value = "confermaattivazione_{idBandoOggetto}", method = RequestMethod.POST)
  public String eliminainterventoPost(
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    Long idUtente = utenteAbilitazioni.getIdUtenteLogin();

    String oggetto = cruscottoEJB.getDescrizioneOggetto(idBandoOggetto);
    cruscottoEJB.attivaBandoOggetto(idBandoOggetto, idUtente, "ATTIVATO",
        "Attivato oggetto \"" + oggetto + "\"");
    return "redirect:attivazione.do";
  }

  @RequestMapping(value = "disattiva_{idBandoOggetto}", method = RequestMethod.GET)
  public String disattiva(
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {

    // Verifico che ci sia almeno un controllo inserito per questo bando_oggetto
    // NEMBO_R_BANDO_OGGETTO_CONTROLL
    // UtenteAbilitazioni utenteAbilitazioni =
    // (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
    /*
     * if(cruscottoEJB.isOggettoDomandaPagamentoGAL(idBandoOggetto) &&
     * !utenteAbilitazioni.isUtenteOPR() ){ model.addAttribute("errore",
     * "Per attivare/disattivare oggetti relativi alle domande di pagamento su bandi GAL accedere con ruolo OPR/ARPEA"
     * ); return "dialog/soloErrore"; } else {
     */
    String message = "Stai per disattivare l'oggetto scelto, vuoi continuare?";
    model.addAttribute("message", message);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    return "cruscottobandi/confermaDisattivazioneOggetto";
    // }

  }

  @RequestMapping(value = "confermadisattivazione_{idBandoOggetto}", method = RequestMethod.POST)
  public String confermadisattivazione(
      @PathVariable(value = "idBandoOggetto") long idBandoOggetto,
      ModelMap model, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    String message = "Stai per disattivare l'oggetto scelto, vuoi continuare?";
    model.addAttribute("message", message);
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    Long idUtente = utenteAbilitazioni.getIdUtenteLogin();

    model.addAttribute("preferRequest", Boolean.TRUE);
    Errors errors = new Errors();
    String note = request.getParameter("note");
    errors.validateMandatory(note, "note");
    errors.validateFieldMaxLength(note, "note", 4000);

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return "cruscottobandi/confermaDisattivazioneOggetto";
    }
    else
    {
      String oggetto = cruscottoEJB.getDescrizioneOggetto(idBandoOggetto);
      cruscottoEJB.disattivaBandoOggetto(idBandoOggetto, idUtente,
          "DISATTIVATO",
          "Disattivato oggetto \"" + oggetto + "\" con note: \"" + note + "\"");
    }

    return "cruscottobandi/allegaFileOk";
  }

}
