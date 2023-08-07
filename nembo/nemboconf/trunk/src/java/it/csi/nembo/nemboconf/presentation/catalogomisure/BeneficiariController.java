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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("catalogomisure/beneficiari/")
public class BeneficiariController
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
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    DecodificaDTO<String> tipoLivello = catMisureEJB
        .getInfoTipoLivello(idLivello);

    String codice = tipoLivello.getCodice();
    if ("I".equals(codice) || NemboConstants.TIPO_BANDO.GAL.equals(codice))
    {
      session.setAttribute("addInterventiToQuadri", Boolean.TRUE);
    }

    model.addAttribute("elenco", catMisureEJB.getBeneficiari(idLivello));
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/elencoBeneficiari";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "inserisci_{idLivello}", method = RequestMethod.GET)
  public String inserisciGet(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/inserisciBeneficiari";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "inserisci_{idLivello}", method = RequestMethod.POST)
  public String inserisciPost(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("idLivello", String.valueOf(idLivello));
    String selectedValue = request.getParameter("selectedValues");
    if (selectedValue == null || selectedValue.trim().length() <= 0)
    {
      setTestataDetails(idLivello, model);
      model.addAttribute("msgErrore",
          "Devi selezionare almeno un beneficiario!");
    }
    else
    {
      Vector<String> vctIdBeneficiari = new Vector<String>(
          Arrays.asList(selectedValue.split("&")));

      try
      {
        catMisureEJB.insertBeneficiari(idLivello, vctIdBeneficiari);
      }
      catch (ApplicationException e)
      {
        model.addAttribute("msgErrore", e.getMessage());
        setTestataDetails(idLivello, model);
        return "catalogomisure/inserisciBeneficiari";
      }

      return "redirect:dettaglio_" + idLivello + ".do";
    }

    return "catalogomisure/inserisciBeneficiari";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "eliminabeneficiario_{idFgTipologia}_{idLivello}", method = RequestMethod.GET)
  public String eliminabeneficiario(
      @PathVariable(value = "idFgTipologia") long idFgTipologia,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    model.addAttribute("message",
        "Stai per eliminare il beneficiario scelto, vuoi continuare? ");
    model.addAttribute("idFgTipologia", idFgTipologia);
    model.addAttribute("idLivello", idLivello);
    return "catalogomisure/confermaEliminaBeneficiari";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "eliminabeneficiario_{idFgTipologia}_{idLivello}", method = RequestMethod.POST)
  public String eliminabeneficiarioPost(
      @PathVariable(value = "idFgTipologia") long idFgTipologia,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    try
    {
      catMisureEJB.eliminaBeneficiario(idFgTipologia, idLivello);
    }
    catch (ApplicationException e)
    {
      model.addAttribute("msgErrore", e.getMessage());
      return getIndex(idLivello, model, session);
    }
    return "redirect:dettaglio_" + idLivello + ".do";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadBeneficiari_{idLivello}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadBeneficiari(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<BeneficiarioDTO> elenco = catMisureEJB
        .getElencoBeneficiariSelezionabili(idLivello);
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (BeneficiarioDTO com : elenco)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("idFgTipologia", String.valueOf(com.getIdFgTipologia()));
        parameters.put("descrizione", com.getDescrizione());
        parameters.put("gruppo", com.getDescTipologiaAzienda());
        values.put(String.valueOf(com.getIdFgTipologia()), parameters);
      }

    return values;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadBeneficiariSelezionati_{idLivello}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadBeneficiariSelezionati(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session)
      throws InternalUnexpectedException
  {
    List<BeneficiarioDTO> elenco = catMisureEJB
        .getElencoBeneficiariSelezionati(idLivello);
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (BeneficiarioDTO com : elenco)
      {
        if (!values.containsKey(String.valueOf(com.getIdFgTipologia())))
        {
          parameters = new TreeMap<String, String>();
          parameters.put("idFgTipologia",
              String.valueOf(com.getIdFgTipologia()));
          parameters.put("descrizione", com.getDescrizione());
          parameters.put("gruppo", com.getDescTipologiaAzienda());
          values.put(String.valueOf(com.getIdFgTipologia()), parameters);
        }
      }

    return values;
  }

}
