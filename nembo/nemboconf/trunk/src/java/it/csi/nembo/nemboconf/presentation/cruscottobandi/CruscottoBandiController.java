package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.business.IGestioneEventiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.FileAllegatoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LivelloDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
public class CruscottoBandiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;
  
  @Autowired
  private IGestioneEventiEJB gestioneEventiEJB = null;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "index")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    final String ID_ELENCO_REPORT = "dettEstrazioneTable";
    final String ID_ELENCO_GRADUATORIE = "graduatorieTable";
    final String ID_ELENCO_LOG = "logTable";

    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(NemboConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    mapFilters.remove(ID_ELENCO_REPORT);
    mapFilters.remove(ID_ELENCO_GRADUATORIE);
    mapFilters.remove(ID_ELENCO_LOG);

    if (session.getAttribute("ammCompetenzaAssociate") != null)
      session.removeAttribute("ammCompetenzaAssociate");
    if (session.getAttribute("tipiOperazioneAssociati") != null)
      session.removeAttribute("tipiOperazioneAssociati");
    if (session.getAttribute("ambitiTematiciAssociati") != null)
      session.removeAttribute("ambitiTematiciAssociati");
    if (session.getAttribute("idAmmCompSelezionata") != null)
      session.removeAttribute("idAmmCompSelezionata");
    if (session.getAttribute("idsOperazioni") != null)
      session.removeAttribute("idsOperazioni");

    clearCommonInSession(session);

    return "cruscottobandi/index";
  }

  @RequestMapping(value = "getElencoBandiCruscotto", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoBandiCruscotto(HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<Map<String, Object>> bandi = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> bando = null;
    List<BandoDTO> elencoBandi = new ArrayList<>();
    // se l'utente è gal, faccio vedere SOLO i bandi di sua competenza
    if (utenteAbilitazioni.isUtenteGAL())
    {
      List<BandoDTO> idBandiDisponibili = cruscottoEJB
          .getIdElencoBandiDisponibili();
      List<Long> idsBandiGal = new ArrayList<>();
      for (BandoDTO b : idBandiDisponibili)
      {
        b.setAmministrazioniCompetenza(
            cruscottoEJB.getAmmCompetenzaAssociate(b.getIdBando(), false));
        if (b.getAmministrazioniCompetenza() != null)
          for (AmmCompetenzaDTO a : b.getAmministrazioniCompetenza())
          {
            if (a.getIdAmmCompetenza() == utenteAbilitazioni
                .getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza())
            {
              idsBandiGal.add(a.getIdBando());
            }
          }
      }
      elencoBandi = cruscottoEJB.getElencoBandiDisponibili(false, true,
          idsBandiGal); // è bando gal
    }
    else
      elencoBandi = cruscottoEJB.getElencoBandiDisponibili(false, false, null); // non
                                                                                // è
                                                                                // bando
                                                                                // gal

    if (elencoBandi != null && elencoBandi.size() > 0)
    {
      for (BandoDTO item : elencoBandi)
      {
        bando = new HashMap<String, Object>();
        bando.put("denominazione", item.getDenominazione());
        bando.put("annoCampagna", item.getAnnoCampagna());
        bando.put("dataInizioStr", item.getDataInizioStr());
        bando.put("dataFineStr", item.getDataFineStr());
        bando.put("descrTipoBando", item.getDescrTipoBando());
        bando.put("flagTitolaritaRegionale", item.getFlagTitolaritaRegionale());
        bando.put("idBando", item.getIdBando());
        bando.put("idEventoCalamitoso", item.getIdEventoCalamitoso());
        bando.put("descEventoCalamitoso", item.getDescEventoCalamitoso());
        bando.put("idCategoriaEvento", item.getIdCategoriaEvento());
        bando.put("descCatEvento", item.getDescCatEvento());
        bando.put("dataEventoStr", item.getDataEventoStr());

        if (item.isBandoAttivo())
        {
          bando.put("procesistente", NemboConstants.FLAGS.SI);
        }
        else
        {
          bando.put("procesistente", NemboConstants.FLAGS.NO);
        }
        bando.put("livelli", item.getLivelli());
        bando.put("elencoDescrizioniLivelli",
            item.getElencoDescrizioniLivelli());
        bando.put("elencoCodiciOperazione",
            item.getElencoCodiciLivelliOperazione());
        bando.put("elencoCodiciLivelliMisure",
            item.getElencoCodiciLivelliMisure());
        bando.put("elencoCodiciLivelliSottoMisure",
            item.getElencoCodiciLivelliSottoMisure());
        bando.put("elencoCodiciLivelliMisureHtml",
            item.getElencoCodiciLivelliMisureHtml());
        bando.put("elencoSettori", item.getElencoSettoriStr());
        bando.put("elencoFocusArea", item.getElencoFocusAreaStr());
        bando.put("allegati", item.getAllegati());
        bando.put("amministrazioniCompetenzaHtml",
            item.getAmministrazioniCompetenzaHtml());
        bando.put("idsAmministrazioniCompetenza",
            item.getIdsAmministrazioniCompetenza());

        bandi.add(bando);
      }
    }

    return bandi;
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
  
  
  @RequestMapping(value = "getElencoTipiBandoJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoTipiBandoJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<BandoDTO> tipiBando = cruscottoEJB.getElencoBandiDisponibili(Boolean.FALSE, Boolean.FALSE, null);
    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    
    for (BandoDTO item : tipiBando)
    {
      if (!valList.contains(item.getDescrTipoBando()))
      {
        stato = new HashMap<String, Object>();
        stato.put("id", item.getDescrTipoBando());
        stato.put("label", item.getDescrTipoBando());
        ret.add(stato);
        valList.add(item.getDescrTipoBando());
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoCodiciLivelliMisureJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciLivelliMisureJson(Model model,
      HttpSession session, HttpServletRequest request)
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
      if (!valList.contains(item.getCodiceMisura()))
      {
        stato = new HashMap<String, Object>();
        stato.put("id", item.getCodiceMisura());
        stato.put("label", item.getCodiceMisura());
        ret.add(stato);
        valList.add(item.getCodiceMisura());
      }
    }

    return ret;
  }

  @RequestMapping(value = "getElencoAmministrazioniCompetenzaJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoAmministrazioniCompetenzaJson(
      Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<BandoDTO> elencoBandi = cruscottoEJB.getIdElencoBandiDisponibili();

    Map<String, Object> stato;
    List<String> valList = new ArrayList<String>();
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (BandoDTO item : elencoBandi)
    {
      if (item.getFlagMaster() == "S")
        item.setAmministrazioniCompetenza(
            cruscottoEJB.getAmmCompetenzaAssociate(item.getIdBando(), true));
      else
        item.setAmministrazioniCompetenza(
            cruscottoEJB.getAmmCompetenzaAssociate(item.getIdBando(), false));

      for (AmmCompetenzaDTO a : item.getAmministrazioniCompetenza())
      {
        if (!valList.contains(a.getDescrizione()))
        {
          stato = new HashMap<String, Object>();
          stato.put("id", a.getIdAmmCompetenza());
          stato.put("label", a.getDescrizione());
          ret.add(stato);
          valList.add(a.getDescrizione());
        }
      }
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

  @RequestMapping(value = "getElencoSettoriJson", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<Map<String, Object>> getElencoSettoriJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<SettoriDiProduzioneDTO> livelli = cruscottoEJB.getElencoSettoriBandi();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (SettoriDiProduzioneDTO item : livelli)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getDescrizione());
      stato.put("label", item.getDescrizione());
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
    List<FocusAreaDTO> livelli = cruscottoEJB.getElencoFocusAreaBandi();
    Map<String, Object> stato;
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (FocusAreaDTO item : livelli)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

  /* *********************************** */
  /* Gestione configurazione nuovo bando */
  /* *********************************** */

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "sceltabandomaster", method = RequestMethod.GET)
  public String sceltabandomaster(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<BandoDTO> bandiMaster = cruscottoEJB.getDettaglioBandiMaster();

    List<BandoDTO> bandiGal = new ArrayList<>();
    if (utenteAbilitazioni.isUtenteGAL())
    {
      for (BandoDTO b : bandiMaster)
      {
        List<AmmCompetenzaDTO> ammComp = cruscottoEJB
            .getAmmCompetenzaAssociate(b.getIdBando(), true);
        if (ammComp != null)
        {
          b.setAmministrazioniCompetenza(ammComp);

          for (AmmCompetenzaDTO a : ammComp)
          {
            if (a.getIdAmmCompetenza() == utenteAbilitazioni
                .getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza())
              bandiGal.add(b);
          }
        }
      }
      session.setAttribute("bandiMaster", bandiGal);
      model.addAttribute("elenco", bandiGal);
      return "cruscottobandi/popupSceltaBandoMaster";
    }
    session.setAttribute("bandiMaster", bandiMaster);
    model.addAttribute("elenco", bandiMaster);
    return "cruscottobandi/popupSceltaBandoMaster";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "download_{idAllegatiBando}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(
      @PathVariable("idAllegatiBando") long idAllegatiBando,
      HttpSession session) throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO allegato = cruscottoEJB.getAllegato(idAllegatiBando);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        NemboUtils.FILE.getMimeType(allegato.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + allegato.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        allegato.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }
  
  @RequestMapping(value = "load_eventi_calamitosi_{idCategoriaEvento}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<DecodificaDTO<Long>> loadEventiCalamitosi(
		  HttpSession session,
		  Model model,
		  HttpServletRequest request,
		  @PathVariable("idCategoriaEvento") long idCategoriaEvento
		  ) throws InternalUnexpectedException
  {
	  List<DecodificaDTO<Long>> list = gestioneEventiEJB.getListEventiCalamitosi(idCategoriaEvento);
	  return list;
  }	

}
