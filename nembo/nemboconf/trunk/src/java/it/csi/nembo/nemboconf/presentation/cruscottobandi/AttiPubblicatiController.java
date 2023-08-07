package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.FileAllegatoDTO;
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
public class AttiPubblicatiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "reloadattiPubblicati", method = RequestMethod.GET)
  public String reloadattiPubblicatiGet() throws InternalUnexpectedException
  {
    return "redirect:attiPubblicati.do?#allBando";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "attiPubblicati", method = RequestMethod.GET)
  public String attiPubblicatiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    // diamo una rinfrescata al bando in sessione perchè in questa pagina
    // aggiorno la descrizione del filtro proprio in questo oggetto
    bando = cruscottoEJB.getInformazioniBando(bando.getIdBando());
    session.setAttribute("bando", bando);

    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(bando.getIdBando());
    List<FileAllegatoDTO> allegati = cruscottoEJB
        .getElencoAllegati(bando.getIdBando());
    // session.setAttribute("allegati", allegati);

    model.addAttribute("idBando", bando.getIdBando());
    model.addAttribute("modificaAbilitata", modificaAbilitata);
    model.addAttribute("allegati", allegati);
    model.addAttribute("isAvversitaAtmosferica", bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE);
    return "cruscottobandi/attiPubblicati";
  }

  @RequestMapping(value = "attiPubblicati", method = RequestMethod.POST)
  public String attiPubblicatiPost(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {

    return "redirect:filtrobeneficiari.do";
  }

  @RequestMapping(value = "inserisciFile", method = RequestMethod.GET)
  public String inserisciGet(ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "cruscottobandi/allegaFile";
  }

  @RequestMapping(value = "inserisciFile", method = RequestMethod.POST)
  public String inserisciPost(HttpServletRequest request, ModelMap model,
      HttpSession session,
      @ModelAttribute("errors") Errors errors,
      @RequestParam(value = "descrAllegato", required = false) String descrAllegato,
      @RequestParam(value = "fileDaAllegare", required = false) MultipartFile fileAllegato)
      throws InternalUnexpectedException, IOException
  {
	  String nomeAllegato = null;
    model.addAttribute("prfReqValueAlleg", Boolean.TRUE);
    errors.validateMandatory(descrAllegato, "descrAllegato");
    if (fileAllegato == null || fileAllegato.isEmpty())
    {
      errors.addError("fileDaAllegare", Errors.ERRORE_CAMPO_OBBLIGATORIO);
    }else{
   	 nomeAllegato = fileAllegato.getOriginalFilename();
   	 if(nomeAllegato.indexOf(".")<0)
        {
       	 errors.addError("fileDaAllegare", "E' necessario utilizzare un file con estensione nota."); 
        }
    }  
    
    errors.validateFieldLength(descrAllegato, 0, 200, "descrAllegato");
    if (errors.isEmpty())
    {
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");

      if (GenericValidator.isBlankOrNull(descrAllegato))
        descrAllegato = " ";

      FileAllegatoDTO fileDTO = new FileAllegatoDTO();
      fileDTO.setIdBando(bando.getIdBando());
      // fileDTO.setNomeFile(getFileName(fileAllegato.getOriginalFilename()));
      fileDTO.setNomeFile(nomeAllegato);
      fileDTO.setFileAllegato(fileAllegato.getBytes());
      fileDTO.setDescrizione(descrAllegato);
      cruscottoEJB.insertFileAllegato(fileDTO);

      // LOG
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();

      Long idBandoOggetto = cruscottoEJB
          .getFirstIdBandoOggetto(bando.getIdBando());
      if (idBandoOggetto != null)
        cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
            NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ATTI_PUBBLICATI,
            "Inserito allegato con nome: \"" + nomeAllegato
                + "\" e descrizione: \"" + descrAllegato + "\"");

      return "cruscottobandi/allegaFileOk";
    }
    return "cruscottobandi/allegaFile";
  }

  @RequestMapping(value = "modifica_allegato_{idAllegatiBando}", method = RequestMethod.GET)
  @IsPopup
  public String modificaAllegatoGet(ModelMap model, HttpSession session,
      @ModelAttribute("idAllegatiBando") @PathVariable("idAllegatiBando") long idAllegatiBando)
      throws InternalUnexpectedException
  {
    FileAllegatoDTO allegato = getAllegato(session, idAllegatiBando);

    model.addAttribute("idAllegatiBando", idAllegatiBando);
    model.addAttribute("nomeAllegato", allegato.getNomeFile());
    model.addAttribute("descrAllegato", allegato.getDescrizione());
    return "cruscottobandi/modificaAllegato";
  }

  @RequestMapping(value = "modifica_allegato_{idAllegatiBando}", method = RequestMethod.POST)
  public String modificaAllegatoPost(ModelMap model, HttpSession session,
      @PathVariable("idAllegatiBando") long idAllegatiBando,
      @ModelAttribute("nomeAllegato") String nomeAllegato,
      @ModelAttribute("descrAllegato") String descrAllegato)
      throws InternalUnexpectedException
  {
    model.addAttribute("descrAllegato", descrAllegato);
  	model.addAttribute("nomeAllegato", nomeAllegato);

    Errors errors = new Errors();
    errors.validateMandatoryFieldLength(descrAllegato, 0, 200, "descrAllegato");

    if (errors.isEmpty())
    {
      cruscottoEJB.updateAllegato(idAllegatiBando, descrAllegato);

      FileAllegatoDTO oldAllegato = getAllegato(session, idAllegatiBando);

      // LOG
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      Long idBandoOggetto = cruscottoEJB.getFirstIdBandoOggetto(bando.getIdBando());
      if(idBandoOggetto!=null)
    	  cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
    	          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ATTI_PUBBLICATI,
    	          "Modificato allegato con id: \"" + idAllegatiBando
    	              + "\"\nNome prima della modifica: \"" + oldAllegato.getNomeFile()
    	              + "\" - Nome dopo la modifica: \"" + nomeAllegato
    	              + "\"\nDescrizione prima della modifica: \""
    	              + oldAllegato.getDescrizione()
    	              + "\" - Descrizione dopo la modifica: \"" + descrAllegato + "\"");

    return "cruscottobandi/allegaFileOk";        
    }
    else
    {
      model.addAttribute("errors", errors);
      return "cruscottobandi/erroreAllegato";
    }
  }

  @RequestMapping(value = "modificaOrdine_{idAllegatiBandoPartenza}_{idAllegatiBandoArrivo}", method = RequestMethod.GET)
  public String modificaOrdineGet(ModelMap model, HttpSession session,
      @PathVariable("idAllegatiBandoPartenza") long idAllegatiBandoPartenza,
      @PathVariable("idAllegatiBandoArrivo") long idAllegatiBandoArrivo)
      throws InternalUnexpectedException
  {

    cruscottoEJB.updateOrdineAllegato(idAllegatiBandoPartenza,
        idAllegatiBandoArrivo);

    FileAllegatoDTO filePartenza = cruscottoEJB
        .getAllegato(idAllegatiBandoPartenza);
    FileAllegatoDTO fileArrivo = cruscottoEJB
        .getAllegato(idAllegatiBandoArrivo);

    String ordinePartenzaTmp = filePartenza.getOrdine();
    String ordineArrivoTmp = fileArrivo.getOrdine();

    String msgLog = "File con id \"" + filePartenza.getIdAllegatiBando()
        + "\" da posizione \"" + ordinePartenzaTmp + "\" in posizione \""
        + ordineArrivoTmp + "\"\n";
    msgLog += " - File con id \"" + fileArrivo.getIdAllegatiBando()
        + "\" da posizione \"" + ordineArrivoTmp + "\" in posizione \""
        + ordinePartenzaTmp + "\"";

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
    long idBandoOggetto = cruscottoEJB
        .getFirstIdBandoOggetto(bando.getIdBando());
    cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
        NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ATTI_PUBBLICATI,
        "Modifica ordine allegati: " + msgLog);

    return "cruscottobandi/allegaFileOk";
  }

  @RequestMapping(value = "conferma_elimina_{idAllegatiBando}", method = RequestMethod.GET)
  public String confermaElimina(ModelMap model, HttpSession session,
      @ModelAttribute("idAllegatiBando") @PathVariable("idAllegatiBando") long idAllegatiBando)
      throws InternalUnexpectedException
  {
    return "cruscottobandi/confermaElimina";
  }

  @RequestMapping(value = "elimina_{idAllegatiBando}", method = RequestMethod.GET)
  public String eliminaAllegato(ModelMap model, HttpSession session,
      @ModelAttribute("idAllegatiBando") @PathVariable("idAllegatiBando") long idAllegatiBando)
      throws InternalUnexpectedException
  {
    try
    {
      cruscottoEJB.deleteFileAllegato(idAllegatiBando);

      // LOG
      BandoDTO bando = (BandoDTO) session.getAttribute("bando");
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      long idBandoOggetto = cruscottoEJB
          .getFirstIdBandoOggetto(bando.getIdBando());
      cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ATTI_PUBBLICATI,
          "Eliminato allegato con id: \"" + idAllegatiBando + "\"");

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "cruscottobandi/confermaElimina";
  }

  @RequestMapping(value = "downloadAlleg_{idBando}_{idAllegatiBando}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(@PathVariable("idBando") long idBando,
      @PathVariable("idAllegatiBando") long idAllegatiBando,
      HttpSession session) throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO allegato = getAllegato(session, idAllegatiBando);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        NemboUtils.FILE.getMimeType(allegato.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + allegato.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        allegato.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }

  private FileAllegatoDTO getAllegato(HttpSession session, long idAllegatiBando)
      throws InternalUnexpectedException
  {
    /*
     * @SuppressWarnings("unchecked") List<FileAllegatoDTO> allegati =
     * (List<FileAllegatoDTO>)session.getAttribute("allegati");
     */

    FileAllegatoDTO allegato = cruscottoEJB.getAllegato(idAllegatiBando);

    /*
     * for(FileAllegatoDTO all:allegati) { if(all.getIdAllegatiBando() ==
     * idAllegatiBando) { allegato = all; break; } }
     */
    return allegato;
  }

}
