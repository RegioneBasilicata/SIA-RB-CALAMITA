package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

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
import it.csi.nembo.nemboconf.dto.FilieraDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class FiliereController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "filiere", method = RequestMethod.GET)
  public String interventiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(idBando);

    model.addAttribute("idBando", idBando);
    model.addAttribute("modificaAbilitata", modificaAbilitata);
    model.addAttribute("goBack", "filtrobeneficiari.do");
    if (session.getAttribute("mostraQuadroInterventi") != null)
    {
      model.addAttribute("goBack", "interventi.do");
    }

    return "cruscottobandi/filiere";
  }

  @RequestMapping(value = "elencoFiliere", produces = "application/json")
  @ResponseBody
  public List<FilieraDTO> elenco(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    List<FilieraDTO> tipiFiliere = cruscottoEJB
        .getTipiFiliereAssociate(getIdBando(session));
    return tipiFiliere != null ? tipiFiliere : new ArrayList<>();
  }

  @RequestMapping(value = "modificaFiliere", method = RequestMethod.GET)
  public String modificainterventiGet(ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("idBando", getIdBando(session));

    return "cruscottobandi/modificaFiliere";
  }

  private long getIdBando(HttpSession session)
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    return bando.getIdBando();
  }

  @RequestMapping(value = "loadFiliere", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadFiliere(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<FilieraDTO> elenco = cruscottoEJB
        .getTipiFiliereDisponibili(getIdBando(session));

    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (FilieraDTO f : elenco)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("idTipoFiliera", f.getIdTipoFiliera().toString());
        parameters.put("descrizione", f.getDescrizioneTipoFiliera());
        values.put(f.getIdTipoFiliera().toString(), parameters);
      }

    return values;
  }

  @RequestMapping(value = "loadFiliereSelezionate", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadFiliereSelezionate(HttpSession session)
      throws InternalUnexpectedException
  {
    List<FilieraDTO> elenco = cruscottoEJB
        .getTipiFiliereAssociate(getIdBando(session));
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (FilieraDTO f : elenco)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("idTipoFiliera", f.getIdTipoFiliera().toString());
        parameters.put("descrizione", f.getDescrizioneTipoFiliera());
        values.put(f.getIdTipoFiliera().toString(), parameters);
      }

    return values;
  }

  @RequestMapping(value = "eliminaFiliera_{idTipoFiliera}", method = RequestMethod.GET)
  public String eliminaintervento(
      @PathVariable(value = "idTipoFiliera") long idTipoFiliera,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(idBando);
    if (!modificaAbilitata)
    {
      model.addAttribute("msgErrore",
          "Il bando è attivo! La modifica non è abilitata!");
      return "Il bando è attivo! La modifica non è abilitata!";
    }
    model.addAttribute("message",
        "Stai per eliminare la filiera scelta, vuoi continuare? ");
    model.addAttribute("idTipoFiliera", idTipoFiliera);
    return "cruscottobandi/confermaEliminaFiliera";
  }

  @RequestMapping(value = "confermaEliminaTipoFiliera_{idTipoFiliera}", method = RequestMethod.POST)
  public String eliminaFilieraPost(HttpServletRequest request,
      @PathVariable(value = "idTipoFiliera") long idTipoFiliera,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {

    cruscottoEJB.eliminaTipoFiliera(getIdBando(session), idTipoFiliera);
    return "redirect:filiere.do";
  }

  @RequestMapping(value = "modificaFiliere", method = RequestMethod.POST)
  public String modificaFilierePost(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(idBando);
    model.addAttribute("idBando", idBando);
    model.addAttribute("modificaAbilitata", modificaAbilitata);

    if (!modificaAbilitata)
    {
      model.addAttribute("msgErrore",
          "Il bando è attivo! La modifica non è abilitata!");
      return "cruscottobandi/filiere";
    }
    // es: 64&65 --> idFiliere
    String selectedValue = request.getParameter("selectedValues");
    cruscottoEJB.updateFiliere(getIdBando(session), strToVector(selectedValue));

    model.addAttribute("goBack", "filtrobeneficiari.do");
    if (session.getAttribute("mostraQuadroInterventi") != null)
    {
      model.addAttribute("goBack", "interventi.do");
    }

    return "cruscottobandi/filiere";
  }

  private Vector<Long> strToVector(String val)
  {
    // es: 64&65 --> idFiliere
    Vector<Long> map = new Vector<Long>();
    if (val != null && val.compareTo("") != 0)
    {
      String[] values = val.split("&");
      if (values != null && values.length > 0)
        for (String l : values)
          map.add(Long.parseLong(l));
    }
    return map;
  }

}
