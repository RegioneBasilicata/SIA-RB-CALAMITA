package it.csi.nembo.nemboconf.presentation.configurazionecataloghi;

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
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/configurazionecataloghi")
public class QuadroOggettoController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "gestisci_quadro_oggetto", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    return "configurazionecataloghi/quadroOggetto";
  }

  @RequestMapping(value = "getElencoOggetti", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<OggettiIstanzeDTO> getElencoOggetti(HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoOggettiNemboconf();
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "gestisci_oggetto_{idOggetto}", method = RequestMethod.GET)
  public String parametri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("idOggetto", idOggetto);
    model.addAttribute("descrOggetto",
        cruscottoEJB.getDescrizioneOggettoById(idOggetto));

    return "configurazionecataloghi/popupGestioneOggetto";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizza_oggetto_{idOggetto}", method = RequestMethod.GET)
  public String visualizza(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    List<QuadroDTO> elenco = cruscottoEJB.getElencoQuadriSelezionati(idOggetto);
    model.addAttribute("idOggetto", idOggetto);
    model.addAttribute("descrOggetto",
        cruscottoEJB.getDescrizioneOggettoById(idOggetto));
    model.addAttribute("elenco", elenco);
    return "configurazionecataloghi/popupVisualizzaOggetto";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "gestisci_oggetto_{idOggetto}", method = RequestMethod.POST)
  public String parametriPost(Model model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("idOggetto", idOggetto);
    String[] aIdQuadri = request.getParameterValues("selectedList");
    cruscottoEJB.updateQuadroOggetto(idOggetto, aIdQuadri);
    return index(model, session);
  }

  @RequestMapping(value = "loadQuadriDisponibili_{idOggetto}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<QuadroDTO> loadQuadriDisponibili(HttpSession session, Model model,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoQuadriDisponibili(idOggetto);
  }

  @RequestMapping(value = "loadQuadriSelezionati_{idOggetto}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<QuadroDTO> loadQuadriSelezionati(HttpSession session, Model model,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoQuadriSelezionati(idOggetto);
  }

  @RequestMapping(value = "modificaOrdineQuadri_{idOggetto}_{idQuadroPartenza}_{idQuadroArrivo}", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String modificaOrdineGet(ModelMap model, HttpSession session,
      HttpServletRequest request,
      @PathVariable(value = "idQuadroPartenza") long idQuadroPartenza,
      @PathVariable(value = "idQuadroArrivo") long idQuadroArrivo,
      @PathVariable(value = "idOggetto") long idOggetto)
      throws InternalUnexpectedException
  {
    cruscottoEJB.updateOrdineQuadroOggetto(idOggetto, idQuadroPartenza,
        idQuadroArrivo);
    return visualizza(model, request, session, idOggetto);
  }

}
