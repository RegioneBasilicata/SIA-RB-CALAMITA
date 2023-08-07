package it.csi.nembo.nemboconf.presentation.catalogomisure;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.dto.catalogomisura.InterventiDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("catalogomisure/interventi/")
public class InterventiController
{
  @Autowired
  private ICatalogoMisureEJB catMisureEJB = null;

  private void setTestataDetails(long idLivello, ModelMap model)
      throws InternalUnexpectedException
  {
    TipoOperazioneDTO tipoOp = catMisureEJB.getAlberaturaOperazione(idLivello);
    model.addAttribute("dettagliTestata", tipoOp);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "dettaglio_{idLivello}", method = RequestMethod.GET)
  public String getIndex(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("elenco", catMisureEJB.getInterventi(idLivello));
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/elencoInterventi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "eliminaintervento_{idDescrizioneIntervento}_{idLivello}", method = RequestMethod.GET)
  public String eliminaintervento(
      @PathVariable(value = "idDescrizioneIntervento") long idDescrizioneIntervento,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    model.addAttribute("message",
        "Stai per eliminare l'intervento scelto, vuoi continuare? ");
    model.addAttribute("idDescrizioneIntervento", idDescrizioneIntervento);
    model.addAttribute("idLivello", idLivello);
    return "catalogomisure/confermaEliminaInterventi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "confermaeliminaintervento_{idDescrizioneIntervento}_{idLivello}", method = RequestMethod.POST)
  public String eliminainterventoPost(
      @PathVariable(value = "idDescrizioneIntervento") long idDescrizioneIntervento,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    try
    {
      catMisureEJB.eliminaIntervento(idLivello, idDescrizioneIntervento);
    }
    catch (ApplicationException e)
    {
      model.addAttribute("msgErrore", e.getMessage());
      return getIndex(idLivello, model);
    }
    return "redirect:dettaglio_" + idLivello + ".do";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "inserisci_{idLivello}", method = RequestMethod.GET)
  public String inserisciGet(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/inserisciInterventi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "inserisci_{idLivello}", method = RequestMethod.POST)
  public String inserisciPost(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model, HttpServletRequest request)
      throws InternalUnexpectedException
  {

    String selectedValue = request.getParameter("selectedValues");
    if (selectedValue == null || selectedValue.trim().length() <= 0)
    {
      setTestataDetails(idLivello, model);
      model.addAttribute("msgErrore", "Devi selezionare almeno un intervento!");
      model.addAttribute("idLivello", String.valueOf(idLivello));
    }
    else
    {
      Vector<String> vctIdIDescrIntervento = new Vector<String>(
          Arrays.asList(selectedValue.split("&")));
      try
      {
        catMisureEJB.insertInterventi(idLivello, vctIdIDescrIntervento);

      }
      catch (ApplicationException e)
      {
        model.addAttribute("msgErrore", e.getMessage());
        setTestataDetails(idLivello, model);
        return "catalogomisure/inserisciInterventi";
      }
      return "redirect:dettaglio_" + idLivello + ".do";
    }

    return "catalogomisure/inserisciInterventi";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadInterventi_{idLivello}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadInterventi(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<InterventiDTO> elenco = catMisureEJB
        .getElencoInterventiSelezionabili(idLivello);
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (InterventiDTO com : elenco)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("gruppoMaster", com.getDescCategoria());
        parameters.put("gruppo", com.getDescTipoIntervento());
        parameters.put("idDescrizioneIntervento",
            String.valueOf(com.getIdDescrizioneIntervento()));
        parameters.put("descrizione", com.getDescIntervento());
        values.put(String.valueOf(com.getIdDescrizioneIntervento()),
            parameters);
      }

    return values;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadInterventiSelezionati_{idLivello}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadInterventiSelezionati(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session)
      throws InternalUnexpectedException
  {
    List<InterventiDTO> elenco = catMisureEJB
        .getElencoInterventiSelezionati(idLivello);
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (InterventiDTO com : elenco)
      {
        if (!values
            .containsKey(String.valueOf(com.getIdDescrizioneIntervento())))
        {
          parameters = new TreeMap<String, String>();
          parameters.put("idDescrizioneIntervento",
              String.valueOf(com.getIdDescrizioneIntervento()));
          parameters.put("descrizione", com.getDescIntervento());
          parameters.put("gruppo", com.getDescTipoIntervento());
          values.put(String.valueOf(com.getIdDescrizioneIntervento()),
              parameters);
        }
      }

    return values;
  }

  @RequestMapping(value = "downloadExcel")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  public ModelAndView downloadExcel(
      @RequestParam(value = "id", required = true) long idLivello, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    List<InterventiDTO> elenco = catMisureEJB.getInterventi(idLivello);
    return new ModelAndView("excelElencoInterventiView", "elenco", elenco);
  }

}
