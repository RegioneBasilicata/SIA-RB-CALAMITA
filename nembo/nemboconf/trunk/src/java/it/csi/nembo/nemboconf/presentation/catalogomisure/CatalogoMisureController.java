package it.csi.nembo.nemboconf.presentation.catalogomisure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LivelloDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("catalogomisure/")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
public class CatalogoMisureController
{
  @Autowired
  private ICatalogoMisureEJB catMisureEjb;

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB;

  @RequestMapping(value = "dettaglio", method = RequestMethod.GET)
  public String getIndex(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    session.removeAttribute("addInterventiToQuadri");
    List<MisuraDTO> elencoLivelli = catMisureEjb.getCatalogoMisure();
    model.addAttribute("misure", elencoLivelli);
    return "catalogomisure/catalogoMisure";
  }

  @RequestMapping(value = "dettaglio_{idLivello}", method = RequestMethod.GET)
  public String getIndex(ModelMap model,
      @PathVariable(value = "idLivello") long idLivello, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("idLivelloSelezionato", idLivello);
    return getIndex(model, session);
  }

  @RequestMapping(value = "getElencoMisureJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoMisureJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> misure = catMisureEjb.getElencoMisureNemboconf(
        NemboConstants.LIVELLO.TIPOLOGIA.ID.MISURA);
    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : misure)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoSottomisureJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoSottomisureJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> misure = catMisureEjb.getElencoMisureNemboconf(
        NemboConstants.LIVELLO.TIPOLOGIA.ID.SOTTOMISURA);
    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : misure)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoOperazioneJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoOperazioneJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> misure = catMisureEjb.getElencoMisureNemboconf(
        NemboConstants.LIVELLO.TIPOLOGIA.ID.OPERAZIONE);
    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : misure)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoSettoriJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoSettoriJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> misure = catMisureEjb
        .getElencoSettoriNemboconf();
    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : misure)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoFocusAreaJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoFocusAreaJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> misure = catMisureEjb
        .getElencoFocusAreaNemboconf();
    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (DecodificaDTO<String> item : misure)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  @RequestMapping(value = "getElencoCodiciLivelliSottoMisureJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliSottoMisureJson(
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = cruscottoEJB.getElencoLivelli();
    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : livelli)
    {
      if (!valList.contains(item.getCodiceSottoMisura()))
      {
        stato = new HashMap<String, Object>();
        stato.put("id", item.getCodiceSottoMisura());
        stato.put("label", item.getCodiceSottoMisura());
        ret.add(stato);
        valList.add(item.getCodiceSottoMisura());
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoCodiciOperazioneJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = cruscottoEJB.getElencoLivelli();
    List<LivelloDTO> liv = new LinkedList<LivelloDTO>();
    boolean aggiungi = true;

    for (LivelloDTO item : livelli)
    {
      for (LivelloDTO d : liv)
      {
        if (d.getCodiceLivello().compareTo(item.getCodiceLivello()) == 0)
        {
          aggiungi = false;
        }
      }

      if (aggiungi)
        liv.add(item);
      aggiungi = true;
    }

    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : liv)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodiceLivello());
      stato.put("label", item.getCodiceLivello());
      ret.add(stato);
    }

    return ret;
  }
}
