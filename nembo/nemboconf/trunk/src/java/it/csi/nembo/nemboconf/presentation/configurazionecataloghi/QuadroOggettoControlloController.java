package it.csi.nembo.nemboconf.presentation.configurazionecataloghi;

import java.util.ArrayList;
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
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroOggettoVO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/configurazionecataloghi")
public class QuadroOggettoControlloController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "gestisci_quadro_oggetto_controllo", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "configurazionecataloghi/quadroOggettoControllo";
  }

  @RequestMapping(value = "getElencoQuadriOggetti", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<QuadroOggettoVO> getElencoQuadriOggetti(HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoQuadroOggettoNemboconf();
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "gestisci_controlli_{idQuadroOggetto}", method = RequestMethod.GET)
  public String parametri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);

    List<QuadroOggettoVO> elenco = cruscottoEJB
        .getElencoQuadroOggettoNemboconf();
    for (QuadroOggettoVO qo : elenco)
    {
      if (qo.getIdQuadroOggetto() == idQuadroOggetto)
      {
        model.addAttribute("descrOggetto", qo.getDescrOggetto());
        model.addAttribute("descrQuadro", qo.getDescrQuadro());
        break;
      }
    }

    return "configurazionecataloghi/popupGestioneOggettoControllo";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizza_controlli_{idQuadroOggetto}", method = RequestMethod.GET)
  public String visualizzaControlli(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);

    List<QuadroOggettoVO> elenco = cruscottoEJB
        .getElencoQuadroOggettoNemboconf();
    for (QuadroOggettoVO qo : elenco)
    {
      if (qo.getIdQuadroOggetto() == idQuadroOggetto)
      {
        model.addAttribute("descrOggetto", qo.getDescrOggetto());
        model.addAttribute("descrQuadro", qo.getDescrQuadro());
        break;
      }
    }

    addCommonList(model);
    List<ControlloDTO> elencoControlli = cruscottoEJB
        .getElencoQuadriControlliSelezionati(idQuadroOggetto);
    model.addAttribute("elencoControlli", elencoControlli);
    return "configurazionecataloghi/popupVisualizzaOggettoControllo";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizza_controlli_{idQuadroOggetto}", method = RequestMethod.POST)
  public String visualizzaControlliPost(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {

    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
    String[] vIdQuadroOggettoControllo = request
        .getParameterValues("idQuadroOggettoControllo");

    List<ControlloDTO> newValues = new ArrayList<ControlloDTO>();
    ControlloDTO item = null;
    for (String id : vIdQuadroOggettoControllo)
    {
      item = new ControlloDTO();
      item.setIdQuadroOggettoControllo(Long.parseLong(id));
      item.setFlagObbligatorio(request.getParameter("flagObbligatorio_" + id));
      item.setGravita(request.getParameter("gravita_" + id));
      newValues.add(item);
    }

    cruscottoEJB.updateQuadroOggettoControlli(newValues);
    return "redirect:gestisci_quadro_oggetto_controllo.do";
  }

  private void addCommonList(ModelMap model)
  {
    List<DecodificaDTO<String>> listFlagObbligatorio = new ArrayList<DecodificaDTO<String>>();
    listFlagObbligatorio.add(new DecodificaDTO<String>("S", "Si"));
    listFlagObbligatorio.add(new DecodificaDTO<String>("N", "No"));
    model.addAttribute("listFlagObbligatorio", listFlagObbligatorio);

    List<DecodificaDTO<String>> listgravita = new ArrayList<DecodificaDTO<String>>();
    listgravita.add(new DecodificaDTO<String>("W", "Warning"));
    listgravita.add(new DecodificaDTO<String>("G", "Grave"));
    listgravita.add(new DecodificaDTO<String>("B", "Bloccante"));
    model.addAttribute("listgravita", listgravita);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "gestisci_controlli_{idQuadroOggetto}", method = RequestMethod.POST)
  public String parametriPost(Model model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
    String[] aIdControlli = request.getParameterValues("selectedList");
    cruscottoEJB.updateQuadroOggettoControllo(idQuadroOggetto, aIdControlli);
    return index(model, session);
  }

  @RequestMapping(value = "loadQuadriControlliDisponibili_{idQuadroOggetto}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<ControlloDTO> loadQuadriControlliDisponibili(HttpSession session,
      Model model,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoQuadriControlliDisponibili(idQuadroOggetto);
  }

  @RequestMapping(value = "loadQuadriControlliSelezionati_{idQuadroOggetto}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<ControlloDTO> loadQuadriControlliSelezionati(HttpSession session,
      Model model,
      @PathVariable(value = "idQuadroOggetto") long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    return cruscottoEJB.getElencoQuadriControlliSelezionati(idQuadroOggetto);
  }
}
