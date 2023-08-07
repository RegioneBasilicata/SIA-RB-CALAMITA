package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class CriteriSelezioneController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "criteriSelezione", method = RequestMethod.GET)
  public String indexGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    model.addAttribute("elenco",
        cruscottoEJB.getElencoCriteriSelezione(idBando));
    return "cruscottobandi/criteriSelezione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "modificaCriteri", method = RequestMethod.GET)
  public String modificaGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    return "cruscottobandi/modificaCriteriSelezione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "modificaCriteri", method = RequestMethod.POST)
  public String inserisciPost(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    String selectedValue = request.getParameter("selectedValues");
    List<String> idsSelezionati;
    List<String> idsDeSelezionati = new ArrayList<>();
    if (selectedValue != null)
    {
      idsSelezionati = Arrays.asList(selectedValue.split("&"));
    }
    else
    {
      idsSelezionati = new ArrayList<>();
    }

    // Quando l’utente deseleziona dei criteri che erano stati precedentemente
    // inseriti, al conferma verificare che non esista alcun record
    // su NEMBO_T_PUNTEGGIO_CALCOLATO ne su NEMBO_T_PUNTEGGIO_ISTRUTTORIA con
    // quell ID_BANDO_LIVELLO_CRITERIO
    List<CriterioDiSelezioneDTO> criteriDB = cruscottoEJB
        .getElencoCriteriSelezione(getIdBando(session));

    // elimino gli id che sono stati deselezionati
    if (criteriDB != null && !criteriDB.isEmpty())
    {
      for (CriterioDiSelezioneDTO item : criteriDB)
      {
        if (idsSelezionati == null
            || !idsSelezionati.contains(item.getIdLivelloCriterio().toString()))
        {
          idsDeSelezionati.add(item.getIdLivelloCriterio().toString());
        }
      }

    }

    if (cruscottoEJB.checkCriteriConPunteggio(getIdBando(request.getSession()),
        idsDeSelezionati))
    {
      model.addAttribute("msgErrore",
          "Impossibile deselezionare criteri per i quali siano già stati calcolati punteggi");
      return "cruscottobandi/modificaCriteriSelezione";
    }

    cruscottoEJB.updateCriteri(getIdBando(request.getSession()),
        idsSelezionati);
    return "redirect:criteriSelezione.do";

  }

  @RequestMapping(value = "loadCriteriDisponibili", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadCriteriDisponibili(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<CriterioDiSelezioneDTO> elenco = cruscottoEJB
        .getElencoCriteriSelezioneDisponibili(getIdBando(session));
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (CriterioDiSelezioneDTO com : elenco)
      {
        String id = com.getIdLivelloCriterio() + "";
        parameters = new TreeMap<String, String>();
        parameters.put("idLivelloCriterio", com.getIdLivelloCriterio() + "");
        parameters.put("criterioDiSelEsteso", com.getCriterioDiSelEsteso());
        parameters.put("gruppo", com.getDescrPrincipiSelezione());
        values.put(id, parameters);
      }

    return values;
  }

  @RequestMapping(value = "loadCriteriSelezionati", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadCriteriSelezionati(HttpSession session)
      throws InternalUnexpectedException
  {
    List<CriterioDiSelezioneDTO> elenco = cruscottoEJB
        .getElencoCriteriSelezioneSelezionati(getIdBando(session));
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (CriterioDiSelezioneDTO com : elenco)
      {
        String id = com.getIdLivelloCriterio() + "";
        parameters = new TreeMap<String, String>();
        parameters.put("idLivelloCriterio", com.getIdLivelloCriterio() + "");
        parameters.put("criterioDiSelEsteso", com.getCriterioDiSelEsteso());
        parameters.put("gruppo", com.getDescrPrincipiSelezione());
        values.put(id, parameters);
      }

    return values;
  }

  private long getIdBando(HttpSession session)
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    return bando.getIdBando();
  }

}
