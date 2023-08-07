package it.csi.nembo.nemboconf.presentation.catalogomisure;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("catalogomisure/settoridiproduzione/")
public class SettoriDiProduzioneController
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
  public String getSettoriDiProduzione(
      @PathVariable(value = "idLivello") long idLivello,
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

    List<SettoriDiProduzioneDTO> elenco = catMisureEJB
        .getSettoriDiProduzione(idLivello);
    model.addAttribute("elenco", elenco);
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/elencoSettoriDiProduzione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "inserisci_{idLivello}", method = RequestMethod.GET)
  public String inserisciGet(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {

    setTestataDetails(idLivello, model);

    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/inserisciSettoriDiProduzione";
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
          "Devi selezionare almeno un settore di produzione!");
    }
    else
    {
      Vector<String> vctSettori = new Vector<String>(
          Arrays.asList(selectedValue.split("&")));

      try
      {
        if (vctSettori.size() > 0)
          catMisureEJB.insertSettori(idLivello, vctSettori);
      }
      catch (ApplicationException e)
      {
        model.addAttribute("msgErrore", e.getMessage());
        setTestataDetails(idLivello, model);
        return "catalogomisure/inserisciSettoriDiProduzione";
      }

      return "redirect:dettaglio_" + idLivello + ".do";
    }

    return "catalogomisure/inserisciSettoriDiProduzione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadSettori_{idLivello}", produces = "application/json")
  @ResponseBody
  public List<SettoriDiProduzioneDTO> loadSettori(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<SettoriDiProduzioneDTO> elenco = catMisureEJB
        .getElencoSettoriSelezionabili(idLivello);
    if (elenco == null)
      elenco = new LinkedList<SettoriDiProduzioneDTO>();

    return elenco;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "loadSettoriSelezionati_{idLivello}", produces = "application/json")
  @ResponseBody
  public List<SettoriDiProduzioneDTO> loadSettoriSeleizonati(
      @PathVariable(value = "idLivello") long idLivello, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<SettoriDiProduzioneDTO> elenco = catMisureEJB
        .getElencoSettoriSelezionati(idLivello);

    if (elenco == null)
      elenco = new LinkedList<SettoriDiProduzioneDTO>();

    return elenco;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "eliminaSettore_{idSettore}_{idLivello}", method = RequestMethod.GET)
  public String eliminaSettore(
      @PathVariable(value = "idSettore") long idSettore,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    model.addAttribute("message",
        "Stai per eliminare il settore di produzione scelto, vuoi continuare? ");
    model.addAttribute("idSettore", idSettore);
    model.addAttribute("idLivello", idLivello);
    return "catalogomisure/confermaEliminaSettoreDiProduzione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "eliminaSettore_{idSettore}_{idLivello}", method = RequestMethod.POST)
  public String eliminaSettorePost(
      @PathVariable(value = "idSettore") long idSettore,
      @PathVariable(value = "idLivello") long idLivello,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    try
    {
      catMisureEJB.eliminaSettore(idSettore, idLivello);
    }
    catch (InternalUnexpectedException e)
    {
      model.addAttribute("msgErrore", e.getMessage());
      return "catalogomisure/elencoSettoriDiProduzione";
    }
    return "redirect:dettaglio_" + idLivello + ".do";
  }

}
