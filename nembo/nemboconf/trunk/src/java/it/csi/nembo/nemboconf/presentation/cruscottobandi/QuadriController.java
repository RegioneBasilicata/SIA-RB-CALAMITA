package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.ComuneBandoDTO;
import it.csi.nembo.nemboconf.dto.ComuneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDualListDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class QuadriController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "quadri", method = RequestMethod.GET)
  public String interventiGet(ModelMap model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();

    List<GruppoOggettoDTO> elenco = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando);
    model.addAttribute("elencoGruppiOggetto", elenco);
    session.removeAttribute("descrGruppoQuadrSelezionato");
    if (!model.containsKey("fromRequest"))
      model.addAttribute("fromRequest", false);

    model.addAttribute("idBando", idBando);
    return "cruscottobandi/quadri";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaquadri", method = RequestMethod.POST)
  public String visualizzaquadri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "selGruppoOggetto") String idUnivoco)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    long idGruppoOggetto = Long.parseLong(idUnivoco.split("&&")[0]);
    long idOggetto = Long.parseLong(idUnivoco.split("&&")[1]);
    List<QuadroDTO> elencoRisultati = cruscottoEJB
        .getElencoQuadriDisponibili(idBando, idGruppoOggetto, idOggetto);
    List<GruppoOggettoDTO> gruppiDisponibili = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando);

    for (GruppoOggettoDTO item : gruppiDisponibili)
    {
      if (item.getIdUnivoco().equals(idUnivoco))
      {
        model.addAttribute("descrGruppoQuadrSelezionato",
            item.getDescrizioneEstesa());
        session.setAttribute("descrGruppoQuadrSelezionato",
            item.getDescrizioneEstesa());
        break;
      }
    }
    model.addAttribute("idGruppoOggettoSelezionato", idUnivoco);
    model.addAttribute("elencoGruppiOggetto", gruppiDisponibili);
    model.addAttribute("elenco", elencoRisultati);
    model.addAttribute("selGruppoOggetto", idUnivoco);
    return "cruscottobandi/quadri";
  }

  @RequestMapping(value = "quadri", method = RequestMethod.POST)
  public String interventiPost(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idGruppoOggettoSelezionato") String idUnivoco)
      throws InternalUnexpectedException
  {
    String chkObbligatorio;
    String chkPrevisto;
    boolean tuttoAttivo = true;
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    long idBandoOggetto = -1;
    long idQuadroOggetto;
    long idGruppoOggetto = Long.parseLong(idUnivoco.split("&&")[0]);
    long idOggetto = Long.parseLong(idUnivoco.split("&&")[1]);
    String descrizioneOggetto = null;
    Errors errors = new Errors();
    Long idQuadroInterventiConPartecipanti = null, idQuadroInterventi = null, idQuadroInterventiInfrastrutture =null;
    String msg = "";
    List<QuadroDTO> elencoRisultati = cruscottoEJB
        .getElencoQuadriDisponibili(idBando, idGruppoOggetto, idOggetto);
    
    boolean hasInterventi = false;
    boolean hasInterventiConPartecipanti = false;
    boolean hasInterventiInfrastrutture = false;
    
    boolean esisteAlmenoUnQuadroInterventi = false;
    for (QuadroDTO quadro : elencoRisultati)
    {
      if ("N".equals(quadro.getFlagAttivo()))
      {
        tuttoAttivo = false;

        if (("INTER".equals(quadro.getCodice())
            || "INTEP".equals(quadro.getCodice())
            || "INTCI".equals(quadro.getCodice())
        		)
            && (quadro.getDataFine() == null
                || quadro.getDataFine().compareTo(new Date()) > 0))
          esisteAlmenoUnQuadroInterventi = true;

        descrizioneOggetto = quadro.getDescrOggetto();
        idBandoOggetto = quadro.getIdBandoOggetto();
        idQuadroOggetto = quadro.getIdQuadroOggettoMaster();
        chkObbligatorio = request
            .getParameter("chkObbligatorioHidden_" + idQuadroOggetto);
        chkPrevisto = request.getParameter("chkPrevisto_" + idQuadroOggetto);
        if (!GenericValidator.isBlankOrNull(chkPrevisto)
            || (!GenericValidator.isBlankOrNull(chkObbligatorio)
                && chkObbligatorio.equalsIgnoreCase("true")))
        {
          quadro.setIdQuadroOggettoNew(idQuadroOggetto);

        }

        if (quadro.getIdQuadroOggetto() != quadro.getIdQuadroOggettoNew())
        {
          // if(quadro.getFlagObbligatorio().compareTo("S")!=0) //può
          // servire nel caso in cui si voglia non loggare
          // l'operazione di check automatica del flag "Previsto" dei
          // quadri obbligatori.
          // {
          msg = msg.concat("\nQuadro: \"" + quadro.getDescrizione() + "\"");

          if (quadro.getIdQuadroOggettoNew() == 0)
          {
            msg = msg.concat(" - Deselezionata checkbox \"Previsto\"");

          }
          else
          {
            msg = msg.concat(" - Selezionata checkbox \"Previsto\"");

            // Se sono stati selezionati i quadri Interventi e Interventi con
            // partecipanti, setto il flag e mi salvo l'id
            if ("INTER".equals(quadro.getCodice()))
            {
              hasInterventi = true;
              idQuadroInterventi = quadro.getIdQuadroOggettoMaster();
            }
            if ("INTEP".equals(quadro.getCodice()))
            {
              hasInterventiConPartecipanti = true;
              idQuadroInterventiConPartecipanti = quadro.getIdQuadroOggettoMaster();
            }
            if ("INTCI".equals(quadro.getCodice()))
            {
              hasInterventiInfrastrutture = true;
              idQuadroInterventiInfrastrutture = quadro.getIdQuadroOggettoMaster();
            }         
            
          }
          // }

        }
        else
        {

          // se non sono cambiati quadri Interventi e Interventi con
          // partecipanti, ma erano già selezionati, setto il flag e mi salvo
          // l'id
          if ("INTER".equals(quadro.getCodice()) && quadro.isQuadroEsistente())
          {
            hasInterventi = true;
            idQuadroInterventi = quadro.getIdQuadroOggettoMaster();
          }
          if ("INTEP".equals(quadro.getCodice()) && quadro.isQuadroEsistente())
          {
            hasInterventiConPartecipanti = true;
            idQuadroInterventiConPartecipanti = quadro
                .getIdQuadroOggettoMaster();
          }
          if ("INTCI".equals(quadro.getCodice()) && quadro.isQuadroEsistente())
          {
            hasInterventiInfrastrutture = true;
            idQuadroInterventiInfrastrutture = quadro.getIdQuadroOggettoMaster();
          }
        }

      }
    }

    // se sono stati selezionati entrambi i quadri Interventi e Interventi con
    // partecipanti segno errore e non faccio update
    if (hasInterventi && hasInterventiConPartecipanti)
    {
      errors.addError("error",
          "Non si possono selezionare entrambi i quadri \"Interventi\" e \"Interventi con partecipanti\".");
      errors.addError("chkPrevisto_" + idQuadroInterventi,
          "Non si possono selezionare entrambi i quadri \"Interventi\" e \"Interventi con partecipanti\".");
      errors.addError("chkPrevisto_" + idQuadroInterventiConPartecipanti,
          "Non si possono selezionare entrambi i quadri \"Interventi\" e \"Interventi con partecipanti\".");

      model.addAttribute("errors", errors);
      model.addAttribute("fromRequest", true);

    }

    // se è presente almeno un quadro interventi, deve essere selezionato (uno e
    // uno solo)
    else
      if (!hasInterventi && !hasInterventiConPartecipanti && !hasInterventiInfrastrutture
          && esisteAlmenoUnQuadroInterventi)
      {
        errors.addError("error",
            "Impossibile procedere, occorre selezionare un quadro di interventi.");
        errors.addError("chkPrevisto_" + idQuadroInterventi,
            "Impossibile procedere, occorre selezionare un quadro di interventi.");
        errors.addError("chkPrevisto_" + idQuadroInterventiConPartecipanti,
            "Impossibile procedere, occorre selezionare un quadro di interventi.");
        errors.addError("chkPrevisto_" + idQuadroInterventiInfrastrutture,
        		"Impossibile procedere, occorre selezionare un quadro di interventi.");

        model.addAttribute("errors", errors);
        model.addAttribute("fromRequest", true);
      }
      else
      {
        if (!tuttoAttivo)
        {
          try
          {
            cruscottoEJB.aggiornaQuadri(elencoRisultati);
          }
          catch (Exception e)
          {

            if (e.getCause() != null && e.getCause().toString() != null
                && e.getCause().toString()
                    .contains("DataIntegrityViolationException"))
              errors.addError("error",
                  "Impossibile procedere. E' stato deselezionato un quadro a cui sono già stati associati dei procedimenti.");

            model.addAttribute("errors", errors);
            model.addAttribute("fromRequest", true);
            return visualizzaquadri(model, request, session, idUnivoco);
          }

          UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
              .getAttribute("utenteAbilitazioni");
          Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
          if (msg != "")
            cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
                NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.QUADRI,
                "Oggetto: \"" + descrizioneOggetto + "\"" + msg);
        }
      }

    return visualizzaquadri(model, request, session, idUnivoco);
  }

  @RequestMapping(value = "getImmagine_{idQuadro}", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String getImmagine(@PathVariable(value = "idQuadro") long idQuadro,
      HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    model.addAttribute("idQuadro", idQuadro);
    return "cruscottobandi/visualizzaImmagine";
  }

  @RequestMapping(value = "immagine_{idQuadro}", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public ResponseEntity<byte[]> immagine(
      @PathVariable(value = "idQuadro") long idQuadro, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    byte[] immagine = cruscottoEJB.getImmagineByIdQuadro(idQuadro);

    ResponseEntity<byte[]> response = null;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "image/jpg");
    response = new ResponseEntity<byte[]>(immagine, httpHeaders, HttpStatus.OK);

    model.addAttribute("idQuadro", idQuadro);

    return response;
  }
  
  @RequestMapping(value = "scelta_comuni", method = RequestMethod.GET)
  public String sceltaComuni(
		  Model model,
		  HttpServletRequest request, 
		  HttpSession session)  throws InternalUnexpectedException
  {
	  BandoDTO bando = (BandoDTO) session.getAttribute("bando");
	  long idBando = bando.getIdBando();
	  List<ComuneBandoDTO> comuni = cruscottoEJB.getListComuniBando(idBando);
	  boolean modificaAbilitata = (bando.getFlagMaster().equals("N"));
	  model.addAttribute("modificaAbilitata", modificaAbilitata);
	  model.addAttribute("idBando", idBando);
	  model.addAttribute("comuni", comuni);
	  return "cruscottobandi/sceltaComuni";
  }
  
  @RequestMapping(value = "/popup_seleziona_comuni", method = RequestMethod.GET)
  public String popupSelezionaComuni(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
	  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
	  model.addAttribute("idBando", idBando);
	  return "cruscottobandi/popupSelezionaComuni";
  }
  
  @RequestMapping(value = "/popup_tutti_fogli_comune_{idBandoComune}", method = RequestMethod.GET)
  @IsPopup
  public String popupSelezionaTuttiFogliComuni(HttpSession session, Model model,
		  @PathVariable("idBandoComune") long idBandoComune)
				  throws InternalUnexpectedException
  {
	  model.addAttribute("idBandoComune", idBandoComune);
	  return "cruscottobandi/popupTuttiFogliComune";
  }
  
  @RequestMapping(value = "/tutti_fogli_comune_{idBandoComune}", method = RequestMethod.POST)
  public String tuttiFogliComune(
		  Model model,
		  HttpSession session, 
		  HttpServletRequest request,
		  @PathVariable("idBandoComune") long idBandoComune)
      throws InternalUnexpectedException
  {
	  cruscottoEJB.inserisciTuttiFogliComuneComune(idBandoComune);
	  return "dialog/success";
  }
  
  @RequestMapping(value = "/load_list_comuni", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public List<DecodificaDualListDTO<String>> loadListComuni(
		  HttpSession session, 
		  Model model)
		  throws InternalUnexpectedException
  {
	  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
	  List<ComuneDTO> listComuni = cruscottoEJB.getComuniNonSelezionatiPerBando("17", null, "N", "N",idBando);
	  List<DecodificaDualListDTO<String>> list = new ArrayList<DecodificaDualListDTO<String>>();
	  if(listComuni == null)
	  {
		  listComuni = new ArrayList<ComuneDTO>();
	  }
	  for(ComuneDTO comune : listComuni)
	  {
		  list.add(new DecodificaDualListDTO<String>(
				  comune.getIstatComune(),
				  comune.getDescrizioneComune(),
				  comune.getDescrizioneComune(),
				  comune.getDescrizioneProvincia()
				  ));
	  }
	  return list;
  }

  
  
  @RequestMapping(value = "/inserisci_comuni", method = RequestMethod.POST)
  public String inserisciComuni(
		  Model model,
		  HttpSession session, 
		  HttpServletRequest request)
      throws InternalUnexpectedException
  {
	  String[] arrayComune = request.getParameterValues("comuniDualList");
	  if(arrayComune != null && arrayComune.length != 0)
	  {
		  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
		  cruscottoEJB.inserisciComuniBando(idBando,arrayComune);
	  }
	  return "redirect:../cruscottobandi/scelta_comuni.do";
  }
  
  @RequestMapping(value = "/popup_seleziona_fogli_comune_{idBandoComune}_{fromCheckbox}", method = RequestMethod.GET)
  @IsPopup
  public String popupSelezionaFogliComune(HttpSession session, Model model,
		  @PathVariable("idBandoComune") long idBandoComune,
		  @PathVariable("fromCheckbox") boolean fromCheckbox)
      throws InternalUnexpectedException
  {
	 
	  List<Integer> fogli = cruscottoEJB.getListFogliBandoComune(idBandoComune);
	  StringBuilder sc = new StringBuilder();
	  for(Integer f : fogli)
	  {
		  if(sc.length()>0)
		  {
			  sc.append("\r\n").append(f.toString());
		  }
		  else
		  {
			  sc.append(f.toString());
		  }
	  }
	  model.addAttribute("fogli", sc.toString());
	  model.addAttribute("idBandoComune", idBandoComune);
	  model.addAttribute("fromCheckbox",fromCheckbox);
	  return "cruscottobandi/popupSelezionaFogliComune";
  }
  
  

	  @RequestMapping(value = "/inserisci_fogli_comune_{idBandoComune}", method = RequestMethod.POST)
	  public String inserisciFogliComune(
			  Model model,
			  HttpSession session, 
			  HttpServletRequest request,
			  @PathVariable("idBandoComune") long idBandoComune)
					  throws InternalUnexpectedException
	  {
	  Errors errors = new Errors();
	  String errorFogliErrati="";
	  String errorFogliNonTrovati="";
	  String errorFogliDuplicati="";
	  Map<Integer, Integer> mapFogliDuplicati = new HashMap<Integer,Integer>();
	  
	  String fogli = request.getParameter("txtFogli");
	  fogli = fogli.trim();

	  if(fogli.equals(""))
	  {
		  errorFogliErrati = "Non sono stati inseriti fogli. Inserire almeno un foglio valido per proseguire";
		  model.addAttribute("errore",errorFogliErrati);
		  return "dialog/errorNoButton";
	  }
	  else
	  {
		  fogli = NemboUtils.STRING.trim(fogli);
		  fogli = fogli.replaceAll("[;]", " ");
		  fogli =  NemboUtils.STRING.replaceCRLFTabWithOneSpace(fogli);
		  fogli = fogli.replaceAll("( )+", " ");
		  String arrayFogliInseriti[] = fogli.split(" ");
		  
		  Integer[] arrayFogliInseritiInteger = new Integer[arrayFogliInseriti.length];
		  //gestione errori fogli inesistenti
		  int i=0;
		  for(String f : arrayFogliInseriti)
		  {
			  try
			  {
				  arrayFogliInseritiInteger[i] = Integer.parseInt(f);
			  }
			  catch(Exception e)
			  {
				  if(errorFogliErrati.length()<300)
				  {
					  errorFogliErrati = errorFogliErrati + ", " + f;
				  }
			  }
			  finally 
			  {
				i++;
			  }
		  }
		  errorFogliErrati = errorFogliErrati.replaceFirst(", ", "");
		  if(arrayFogliInseritiInteger.length == 0)
		  {
			  errorFogliErrati = "Inserire almeno un foglio valido per proseguire";
		  }
		  if(errorFogliErrati.equals(""))
		  {
			  //gestione errori fogli inesistenti nel comune
			  List<Integer> fogliComune = cruscottoEJB.getFogliValidiByIdBandoComune(idBandoComune);
			  Map<Integer, Boolean> mappaFogliComune = new HashMap<Integer,Boolean>();
			  for(Integer f : fogliComune)
			  {
				  mappaFogliComune.put(f, Boolean.TRUE);
			  }
			  
			  for(Integer f : arrayFogliInseritiInteger)
			  {
				  if(!mappaFogliComune.containsKey(f))
				  {
						if(errorFogliNonTrovati.length() < 300)
						{
							errorFogliNonTrovati = errorFogliNonTrovati + ", " + f.toString();
						}
				  }
				  if(mapFogliDuplicati.containsKey(f))
				  {
					  mapFogliDuplicati.put(f, mapFogliDuplicati.get(f)+1);
				  }
				  else
				  {
					  mapFogliDuplicati.put(f, 1);
				  }
			  }
			  errorFogliNonTrovati = errorFogliNonTrovati.replaceFirst(", ", "");
		  }
		  
		  for(Map.Entry<Integer, Integer> e : mapFogliDuplicati.entrySet())
		  {
			  if(e.getValue()>1){
				  if(errorFogliDuplicati.length()<300)
				  {
					  errorFogliDuplicati = errorFogliDuplicati + ", " + e.getKey().toString();
				  }
			  }
		  }
		  errorFogliDuplicati = errorFogliDuplicati.replaceFirst(", ", "");
		  
		  if(errorFogliErrati.equals("") 
				  && errorFogliNonTrovati.equals("")
				  		&& errorFogliDuplicati.equals(""))
		  {
			  cruscottoEJB.inserisciFogliComuneBando(idBandoComune,arrayFogliInseritiInteger);
			  return "dialog/success";
		  }else
		  {
			  String errore="";
			  errors.addToModelIfNotEmpty(model);
			  if(!errorFogliErrati.equals(""))
			  {
				  errore = "Sono stati inseriti fogli non validi (Fogli: " + errorFogliErrati + ")\n";
			  }
			  else if(!errorFogliNonTrovati.equals(""))
			  {
				  errore = errore + 
						  "Sono stati inseriti fogli non presenti nel comune (Fogli: " + errorFogliNonTrovati + ")\n"; 
			  }
			  else if(!errorFogliDuplicati.equals(""))
			  {
				  errore = errore +
						  "Sono stati inseriti fogli duplicati (Fogli " + errorFogliDuplicati + ")\n)";
			  }
			  model.addAttribute("errore",errore);
			  return "dialog/errorNoButton";
		  }
	  }
  }
  
  @RequestMapping(value = "/popup_eliminazione_comune_{idBandoComune}", method = RequestMethod.GET)
  @IsPopup
  public String popupEliminazioneComune(
		  HttpSession session, 
		  Model model,
		  @PathVariable("idBandoComune") long idBandoComune
		  )
				  throws InternalUnexpectedException
  {
	  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
	  model.addAttribute("idBando", idBando);
	  model.addAttribute("idBandoComune", idBandoComune);
	  return "cruscottobandi/popupEliminazioneComuni";
  }
  
  @RequestMapping(value = "/popup_eliminazione_comuni", method = RequestMethod.GET)
  @IsPopup
  public String popupEliminazioneComuni(HttpSession session, Model model)
				  throws InternalUnexpectedException
  {
	  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
	  model.addAttribute("idBando", idBando);
	  model.addAttribute("eliminazioneMultipla", true);
	  return "cruscottobandi/popupEliminazioneComuni";
  }
  
  @RequestMapping(value = "/elimina_comuni", method = RequestMethod.POST)
  public String eliminaComuni(
		  Model model,
		  HttpSession session, 
		  HttpServletRequest request)
      throws InternalUnexpectedException
  {
	  String returnValue = "";
	  String[] arrayChkIdBandoComune = request.getParameterValues("chkIdBandoComune");
	  long[] arrayIdBandoComune = NemboUtils.ARRAY.toLong(arrayChkIdBandoComune);
	  long idBando = ((BandoDTO)session.getAttribute("bando")).getIdBando();
	  cruscottoEJB.eliminaComuniBando(idBando,arrayIdBandoComune);
	  returnValue="dialog/success";
	  return returnValue;
  }
  
}
