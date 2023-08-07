package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.Ricevuta;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class RicevutaController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "ricevuta", method = RequestMethod.GET)
  public String ricevutaGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> oggettiBando = cruscottoEJB
        .getElencoGruppiOggetti(idBando);
    model.addAttribute("elenco", oggettiBando);

    if (model.containsAttribute("idBandoOggetto"))
    {
      long idBandoOggetto = (Long) model.get("idBandoOggetto");
      for (GruppoOggettoDTO gruppo : oggettiBando)
      {
        if (gruppo.getOggetti() != null)
        {
          for (OggettiIstanzeDTO item : gruppo.getOggetti())
          {
            if (item.getIdBandoOggetto() == idBandoOggetto)
            {
              model.addAttribute("descrOggettoSelezionato",
                  item.getDescrGruppoOggetto());
              if (model.containsAttribute("checkFlagAttivo"))
              {
                model.addAttribute("flagAttivoRicevuta", item.getFlagAttivo());
                session.removeAttribute("flagAttivoRicevuta");
              }
            }
          }
        }
      }
    }

    return "cruscottobandi/ricevuta";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaricevuta", method = RequestMethod.POST)
  public String visualizzaRicevuta(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idBandoOggetto") long idBandoOggetto)
      throws InternalUnexpectedException
  {
    Ricevuta ricevuta = getDatiRicevutaMail(idBandoOggetto, model);
    model.addAttribute("ricevuta", ricevuta);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    model.addAttribute("flagIstanza",
        (cruscottoEJB.isOggettoIstanza(idBandoOggetto)
            ? NemboConstants.FLAGS.SI
            : NemboConstants.FLAGS.NO));
    model.addAttribute("flagIstanzaPag",
        (cruscottoEJB.isOggettoIstanzaPagamento(idBandoOggetto)
            ? NemboConstants.FLAGS.SI
            : NemboConstants.FLAGS.NO));
    model.addAttribute("checkFlagAttivo", true);
    model.addAttribute("elencoPlaceholder",
        cruscottoEJB.getElencoPlaceholder());
    return ricevutaGet(model, request, session);
  }

  private Ricevuta getDatiRicevutaMail(long idBandoOggetto, ModelMap model)
      throws InternalUnexpectedException
  {
    Ricevuta ricevuta = cruscottoEJB.getDatiRicevutaMail(idBandoOggetto);
    if (ricevuta == null || (ricevuta.getOggettoMail() == null
        && ricevuta.getCorpoMail() == null))
    {
      ricevuta = new Ricevuta();
      OggettiIstanzeDTO ogg = cruscottoEJB
          .getParametriDefaultRicevuta(idBandoOggetto);
      ricevuta.setCorpoMail(ogg.getCorpoRicevutaDefault());
      ricevuta.setOggettoMail(ogg.getOggettoRicevutaDefault());
      model.addAttribute("ricevutaDefault", Boolean.TRUE);
    }
    return ricevuta;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "confermaricevuta", method = RequestMethod.POST)
  public String confermaricevuta(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idBandoOggetto") long idBandoOggetto,
      @RequestParam(value = "oggettoMail") String oggettoMail,
      @RequestParam(value = "corpoMail") String corpoMail)
      throws InternalUnexpectedException
  {
    model.addAttribute("fromRequest", true);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    model.addAttribute("flagIstanza",
        (cruscottoEJB.isOggettoIstanza(idBandoOggetto)
            ? NemboConstants.FLAGS.SI
            : NemboConstants.FLAGS.NO));
    model.addAttribute("flagIstanzaPag",
        (cruscottoEJB.isOggettoIstanzaPagamento(idBandoOggetto)
            ? NemboConstants.FLAGS.SI
            : NemboConstants.FLAGS.NO));
    model.addAttribute("checkFlagAttivo", true);
    Ricevuta ricevuta = getDatiRicevutaMail(idBandoOggetto, model);
    model.addAttribute("ricevuta", ricevuta);
    Errors errors = new Errors();

    errors.validateFieldLength(oggettoMail, 0, 500, "oggettoMail", true);

    if (errors.isEmpty())
    {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      cruscottoEJB.updateRicevutaMail(
          utenteAbilitazioni.getIdUtenteLogin().longValue(), idBandoOggetto,
          oggettoMail, corpoMail);
      model.remove("ricevutaDefault");
    }
    else
    {
      model.addAttribute("errors", errors);

    }
    model.addAttribute("elencoPlaceholder",
        cruscottoEJB.getElencoPlaceholder());
    return ricevutaGet(model, request, session);
  }

  @RequestMapping(value = "avantiricevuta", method = RequestMethod.POST)
  public String avanti(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    return "redirect:documentirichiesti.do";
  }

}
