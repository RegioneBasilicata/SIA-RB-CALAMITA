package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.IGestioneEventiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.gestioneeventi.EventiDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("/gestioneeventi")
public class GestioneEventiController extends BaseController
{

  @Autowired
  private IGestioneEventiEJB gestioneEventiEJB = null;
  
  private final String fieldNameDescrizioneEvento ="txtDescrizioneEvento";
  private final String fieldNameDataEvento = "txtDataEvento";
  private final String fieldNameCategoriaEvento = "slcCategoriaEvento";

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "index")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String index(HttpSession session, HttpServletRequest request)
  {
	  return "gestioneeventi/index";
  }
  
  
  @RequestMapping(value = "inserisci")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String inserisci(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
	  List<DecodificaDTO<Long>> listCategoriaEvento = gestioneEventiEJB.getListDecodificaCategorieEvento();
	  model.addAttribute("listCategoriaEvento",listCategoriaEvento);
	  model.addAttribute("azione", "inserisci_conferma.do");
	  return "gestioneeventi/inserisci";
  }
  
  @RequestMapping(value = "modifica_{idEventoCalamitoso}")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String modifica(Model model, HttpSession session, HttpServletRequest request,
		  @PathVariable("idEventoCalamitoso") long idEventoCalamitoso) throws InternalUnexpectedException
  {
	  EventiDTO evento = gestioneEventiEJB.getEventoCalamitoso(idEventoCalamitoso);
	  List<DecodificaDTO<Long>> listCategoriaEvento = gestioneEventiEJB.getListDecodificaCategorieEvento();
	  model.addAttribute("listCategoriaEvento",listCategoriaEvento);
	  model.addAttribute("isModifica", Boolean.TRUE);
	  model.addAttribute("evento", evento);
	  model.addAttribute("azione", "modifica_conferma_" + idEventoCalamitoso + ".do");
	  return "gestioneeventi/inserisci";
  }
  
  @RequestMapping(value = "modifica_conferma_{idEventoCalamitoso}")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String modificaConferma(Model model, HttpSession session, HttpServletRequest request,
		  @PathVariable("idEventoCalamitoso") long idEventoCalamitoso) throws InternalUnexpectedException, ApplicationException
  {
	  String returnValue ="";
	  Errors errors = new Errors();

	  String fieldDescrizioneEvento;
	  String fieldDataEvento;
	  String fieldCategoriaEvento;
	  fieldCategoriaEvento = request.getParameter(fieldNameCategoriaEvento);
	  fieldDescrizioneEvento = request.getParameter(fieldNameDescrizioneEvento);
	  fieldDataEvento = request.getParameter(fieldNameDataEvento);
	  
	  errors.validateMandatoryFieldLength(fieldDescrizioneEvento, 1, 500, fieldNameDescrizioneEvento, false);
	  Date dataEvento = errors.validateDate(fieldDataEvento, fieldNameDataEvento, true);
	  if(dataEvento != null)
	  {
		  Calendar calendar = new GregorianCalendar(1900,0,1,0,0,0);
		  Calendar today = Calendar.getInstance();
		  errors.validateMandatoryDateInRange(dataEvento, fieldNameDataEvento, calendar.getTime(), today.getTime(), true, true);
	  }
	  if(errors.addToModelIfNotEmpty(model))
	  {
		  returnValue = modifica(model, session, request,idEventoCalamitoso);
		  model.addAttribute("preferRequest", Boolean.TRUE);
	  }
	  else
	  {
		  int modificato;
		  EventiDTO evento = new EventiDTO();
		  evento.setIdEventoCalamitoso(idEventoCalamitoso);
		  evento.setDataEvento(dataEvento);
		  evento.setDescEvento(fieldDescrizioneEvento);
		  modificato = gestioneEventiEJB.modificaEventoCalamitoso(evento);
		  returnValue = "redirect:./index.do";
	  }
	  return returnValue;
  }
  
  @RequestMapping(value = "inserisci_conferma", method= RequestMethod.POST)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String inserisciConferma(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException
  {
	  String returnValue ="";
	  Errors errors = new Errors();

	  String fieldDescrizioneEvento;
	  String fieldDataEvento;
	  String fieldCategoriaEvento;
	  fieldCategoriaEvento = request.getParameter(fieldNameCategoriaEvento);
	  fieldDescrizioneEvento = request.getParameter(fieldNameDescrizioneEvento);
	  fieldDataEvento = request.getParameter(fieldNameDataEvento);
	  
	  errors.validateMandatoryFieldLength(fieldDescrizioneEvento, 1, 500, fieldNameDescrizioneEvento, false);
	  Date dataEvento = errors.validateDate(fieldDataEvento, fieldNameDataEvento, true);
	  
	  if(dataEvento != null)
	  {
		  Calendar calendar = new GregorianCalendar(1900,0,1,0,0,0);
		  Calendar today = Calendar.getInstance();
		  errors.validateMandatoryDateInRange(dataEvento, fieldNameDataEvento, calendar.getTime(), today.getTime(), true, true);
	  }
	  Long idCategoriaEvento = errors.validateMandatoryLong(fieldCategoriaEvento, fieldNameCategoriaEvento);
	  errors.validateMandatory(fieldCategoriaEvento, fieldNameCategoriaEvento);
	  if(errors.addToModelIfNotEmpty(model))
	  {
		  returnValue = inserisci(model, session, request);
		  model.addAttribute("preferRequest", Boolean.TRUE);
	  }
	  else
	  {
		  EventiDTO evento = new EventiDTO();
		  evento.setDataEvento(dataEvento);
		  evento.setIdCategoriaEvento(idCategoriaEvento);
		  evento.setDescEvento(fieldDescrizioneEvento);
		  gestioneEventiEJB.inserisciEventoCalamitoso(evento);
		  returnValue="redirect:./index.do";
	  }
	  return returnValue;
  }
  
  @RequestMapping(value = "get_list_eventi_disponibili", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<EventiDTO> getListEventiDisponibili(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
	  return gestioneEventiEJB.getListEventiDisponibili();
  }
  
  @IsPopup
  @RequestMapping(value = "elimina_{idEventoCalamitoso}")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String elimina(
		  Model model,
		  HttpSession session,
		  HttpServletRequest request,
		  @PathVariable("idEventoCalamitoso") long idEventoCalamitoso
		  ) throws InternalUnexpectedException
  {
	  long nBandi  = gestioneEventiEJB.getNBandiAssociatiAdEvento(idEventoCalamitoso);
	  model.addAttribute("idEventoCalamitoso", idEventoCalamitoso);
	  model.addAttribute("nBandi",nBandi);
	  return "gestioneeventi/popupConfermaElimina";
  }
  
  @RequestMapping(value = "elimina_conferma_{idEventoCalamitoso}", method=RequestMethod.POST)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String eliminaConferma(Model model, HttpSession session, HttpServletRequest request,
		  @PathVariable("idEventoCalamitoso") long idEventoCalamitoso) throws InternalUnexpectedException, ApplicationException
  {
	  String returnValue = "";
	  long nBandi = gestioneEventiEJB.eliminaEventoCalamitoso(idEventoCalamitoso);
	  if(nBandi == NemboConstants.ERRORI.EVENTI_CALAMITOSI.BANDI_ESISTENTI)
	  {
		  returnValue= index(session,request);
		  model.addAttribute("showErroreBandi",Boolean.TRUE);
		  model.addAttribute("idEventoCalamitoso",idEventoCalamitoso);
	  }
	  else
	  {
		  returnValue = "redirect:./index.do";
	  }
	  return returnValue;

  }
  
}
