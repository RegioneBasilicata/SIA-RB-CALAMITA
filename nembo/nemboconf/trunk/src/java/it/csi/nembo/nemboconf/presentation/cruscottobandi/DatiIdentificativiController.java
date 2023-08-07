package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.business.IGestioneEventiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.CruscottoInterventiDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.dto.gestioneeventi.EventiDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class DatiIdentificativiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;
  
  @Autowired
  private IGestioneEventiEJB gestioneEventiEJB = null;
  
  private final String titolaritaRegionale = "N"; //rimossa titolarita regionale da nembo  
  private final String flagRibassoInterventi = "N";
  private final Long numeroAnniMantenimento = 0L;
  private final String numeroAnniMantenimentoStr = numeroAnniMantenimento.toString();

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "datiidentificativi_{idBando}", method = RequestMethod.GET)
  public String datiIdentificativiGet(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
	
	  
    if (session.getAttribute("ammCompetenzaAssociate") != null)
      session.removeAttribute("ammCompetenzaAssociate");
    if (session.getAttribute("tipiOperazioneAssociati") != null)
      session.removeAttribute("tipiOperazioneAssociati");
    if (session.getAttribute("ambitiTematiciAssociati") != null)
      session.removeAttribute("ambitiTematiciAssociati");

    if (cruscottoEJB.getInformazioniBando(idBando).getIdTipoLivello() == 3)
      model.addAttribute("isBandoGal", true);
    else
      model.addAttribute("isBandoGal", false);

    BandoDTO bando = cruscottoEJB.getInformazioniBando(idBando);
    session.setAttribute("bando", bando);
    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isDatiIdentificativiModificabili(idBando);
    model.addAttribute("modificaAbilitata", modificaAbilitata);
    model.addAttribute("idBando", idBando);
    model.addAttribute("tipologia", bando.getDescrTipoBando());

    // leggo titolarità regionale
    ArrayList<DecodificaDTO<String>> lTitolaritaRegionale = new ArrayList<DecodificaDTO<String>>();
    lTitolaritaRegionale.add(new DecodificaDTO<String>("S", "S", "Si"));
    lTitolaritaRegionale.add(new DecodificaDTO<String>("N", "N", "No"));
    model.addAttribute("titolaritaRegionale", lTitolaritaRegionale);

    // leggo bando multiplo
    ArrayList<DecodificaDTO<String>> lBandoMultiplo = new ArrayList<DecodificaDTO<String>>();
    lBandoMultiplo.add(new DecodificaDTO<String>("S", "S", "Si"));
    lBandoMultiplo.add(new DecodificaDTO<String>("N", "N", "No"));
    model.addAttribute("bandoMultiplo", lBandoMultiplo);

	model.addAttribute("isAvversitaAtmosferica", Boolean.TRUE);

    session.removeAttribute("mostraQuadroComuni");
    if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE){
    	session.setAttribute("mostraQuadroComuni", "true");
    }else{
    	//faccio sparire il quadro comuni
    	session.setAttribute("mostraQuadroComuni", "false");
    	//elimino i textfield eventi calamitosi    	 
    	model.addAttribute("isAvversitaAtmosferica", Boolean.FALSE);
    }
    
    session.removeAttribute("mostraQuadroInterventi");
    if ("I".equals(bando.getCodiceTipoBando())
        || "G".equals(bando.getCodiceTipoBando()))
    {
      session.setAttribute("mostraQuadroInterventi", "true");
    }

    // imposto i valori editabili se non arrivo dal controllo dati
    if (!model.containsKey("isErrorPostData"))
    {
      // Solo se il bando selezionato non è master leggo questi dati
      if ("N".equals(bando.getFlagMaster()))
      {
        // leggo elenco amministrazioni disponibili per il bando
        List<AmmCompetenzaDTO> ammCompetenzaAssociate = cruscottoEJB
            .getAmmCompetenzaAssociate(idBando, false);
        model.addAttribute("ammCompetenzaAssociate", ammCompetenzaAssociate);
        session.setAttribute("ammCompetenzaAssociate", ammCompetenzaAssociate);

        List<TipoOperazioneDTO> tipiOperazioneAssociati = cruscottoEJB
            .getTipiOperazioniAssociati(idBando);
        model.addAttribute("tipiOperazioneAssociati", tipiOperazioneAssociati);
        session.setAttribute("tipiOperazioneAssociati",
            tipiOperazioneAssociati);
        
      }
      if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE
    		  && bando.getIdCategoriaEvento() != null
    		  ){
    	  List<DecodificaDTO<Long>> listEventoCalamitoso = gestioneEventiEJB.getListEventiCalamitosi(bando.getIdCategoriaEvento());
          model.addAttribute("listEventoCalamitoso", listEventoCalamitoso);
      }
     
      
      model.addAttribute("dataApertura", bando.getDataInizioStr());
      model.addAttribute("dataChiusura", bando.getDataFineStr());
      model.addAttribute("dataEvento", bando.getDataEventoStr());
      model.addAttribute("idCategoriaEvento", bando.getIdCategoriaEvento());
      model.addAttribute("idEventoCalamitoso", bando.getIdEventoCalamitoso());
      model.addAttribute("descEventoCalamitoso", bando.getDescEventoCalamitoso());
      model.addAttribute("descCategoriaEvento", bando.getDescCatEvento());
      model.addAttribute("denominazione", bando.getDenominazione());
      model.addAttribute("emailReferenteBando", bando.getEmailReferenteBando());
      model.addAttribute("referenteBando", bando.getReferenteBando());
      model.addAttribute("vtitolaritaRegionale",
          bando.getFlagTitolaritaRegionale());
      model.addAttribute("vBandoMultiplo", bando.getFlagDomandaMultipla());
      model.addAttribute("vFlagRibassoInterventi",
          bando.getFlagRibassoInterventi());
      model.addAttribute("annoRif", bando.getAnnoCampagna());

      
      List<DecodificaDTO<Long>> listCategoriaEvento = gestioneEventiEJB.getListDecodificaCategorieEvento();
      model.addAttribute("listCategoriaEvento",listCategoriaEvento);
    }
    else
    {
      // Ricarico le le select per la videata
      if (model.containsKey("idAmministrazioni"))
      {
        // es: &7=Provincia di Alessandria&4=Provincia di Novara&3=Provincia di
        // Vercelli
        String vIdAmministrazioni = (String) model.get("idAmministrazioni");
        List<AmmCompetenzaDTO> ammCompetenzaAssociate = new Vector<AmmCompetenzaDTO>();
        String[] tmpStr = vIdAmministrazioni.split("&");
        for (int i = 1; i < tmpStr.length; i++)
        {
          ammCompetenzaAssociate.add(new AmmCompetenzaDTO(
              new Long(tmpStr[i].split("=")[0]), tmpStr[i].split("=")[1]));
        }
        model.addAttribute("ammCompetenzaAssociate", ammCompetenzaAssociate);
        session.setAttribute("ammCompetenzaAssociate", ammCompetenzaAssociate);
      }
      if (model.containsKey("idTipiOperazioni"))
      {
        // es: &7=Provincia di Alessandria&4=Provincia di Novara&3=Provincia di
        // Vercelli
        String vidTipiOperazioni = (String) model.get("idTipiOperazioni");
        List<TipoOperazioneDTO> tipiOpAssociati = new Vector<TipoOperazioneDTO>();
        String[] tmpStr = vidTipiOperazioni.split("&");
        for (int i = 1; i < tmpStr.length; i++)
        {
          tipiOpAssociati.add(new TipoOperazioneDTO(
              new Long(tmpStr[i].split("=")[0]), tmpStr[i].split("=")[1]));
        }
        model.addAttribute("tipiOperazioneAssociati", tipiOpAssociati);
        session.setAttribute("tipiOperazioneAssociati", tipiOpAssociati);
      }
    }

    return "cruscottobandi/datiIdentificativi";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "datiidentificativi_{idBando}", method = RequestMethod.POST)
  public String datiIdentificativiPost(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {

    String denominazione = request.getParameter("denominazione");
    String emailReferenteBando = request.getParameter("emailReferenteBando");
    String referenteBando = request.getParameter("referenteBando");
    String dataApertura = request.getParameter("dataApertura");
    String dataChiusura = request.getParameter("dataChiusura");
    
    BandoDTO bando = cruscottoEJB.getInformazioniBando(idBando);
    
    String idCategoriaEvento = "";
    String idEventoCalamitoso = "";
    
    if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE){
    	idCategoriaEvento = request.getParameter("idCategoriaEvento");
        idEventoCalamitoso = request.getParameter("idEventoCalamitoso");
    }
    
    
    String bandoMultiplo = request.getParameter("bandoMultiplo");
    

    String annoRif = request.getParameter("annoRif");
    String amministrazioni = request.getParameter("amministrazioniHidden");
    String tipiOperazioni = request.getParameter("tipiOpHidden");
    String ambitiTematici = request.getParameter("ambitiTematiciHidden");


     model.addAttribute("isBandoGal", false);

    Errors errors = new Errors();
    errors.validateMandatoryFieldLength(denominazione, 0, 500, "denominazione");
    errors.validateMandatoryFieldLength(referenteBando, 0, 500,
        "referenteBando");
    errors.validateMandatoryFieldLength(emailReferenteBando, 0, 100,
        "emailReferenteBando");
    Date dtApertura = errors.validateMandatoryDateTime(dataApertura, "dataApertura", false);
    if(dtApertura != null)
    {
    	GregorianCalendar dataInizio = new GregorianCalendar(1900,0,1);
    	errors.validateMandatoryDateInRange(dtApertura, "dataApertura", dataInizio.getTime(), null, true, false);
    }
    
    Date dtChiusura = errors.validateMandatoryDateTime(dataChiusura, "dataChiusura", false);
	if (dtChiusura != null)
    {
      if (!GenericValidator.isBlankOrNull(dataApertura))
      {
        if (dtChiusura != null && dtApertura != null
            && dtApertura.compareTo(dtChiusura) >= 0)
        {
          errors.addError("dataApertura",
              "La data di apertura deve essere inferiore alla data chiusura!");
        }
      }
    }
	Date dataEvento = null;
	EventiDTO eventoCalamitoso = null;
	if(idEventoCalamitoso != null && !idEventoCalamitoso.equals(""))
	{
		eventoCalamitoso = gestioneEventiEJB.getEventoCalamitoso(Long.parseLong(idEventoCalamitoso));
	}
	if(eventoCalamitoso != null)
	{
		dataEvento = eventoCalamitoso.getDataEvento();
		String dataEventoString = NemboUtils.DATE.formatDateTime(dataEvento);
		dataEvento = NemboUtils.DATE.parseDateTime(dataEventoString);
	}

	if(dataEvento != null)
    {
		if (!GenericValidator.isBlankOrNull(dataApertura))
    	{
            if (dataEvento != null && dtApertura != null
                    && dtApertura.compareTo(dataEvento) < 0)
            {
              errors.addError("dataApertura",
                  "La data di apertura del bando deve essere maggiore della data dell'evento!");
            }
    	}
    	
    	if (!GenericValidator.isBlankOrNull(dataChiusura))
    	{
            if (dataEvento != null && dtChiusura != null
                    && dtChiusura.compareTo(dataEvento) < 0)
            {
              errors.addError("dataChiusura",
                  "La data di chiusura del bando deve essere maggiore della data dell'evento!");
            }
    	}
    }
	
	if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE){
		errors.validateMandatoryLong(request.getParameter("idCategoriaEvento"), "idCategoriaEvento");
		errors.validateMandatoryLong(request.getParameter("idEventoCalamitoso"), "idEventoCalamitoso");
    }
	

    errors.validateMandatory(bandoMultiplo, "bandoMultiplo");
    errors.validateMandatory(amministrazioni, "amministrazione");
    errors.validateMandatory(tipiOperazioni, "tipiOperazioni");

    errors.validateMandatoryYear(annoRif, "annoRif");


    String[] paramNames = new String[1];
    paramNames[0] = "MAX_NUM_RANGE:ANNI";
    Map<String, String> params = cruscottoEJB.getParametri(paramNames);
    String ANNI = params.get("MAX_NUM_RANGE:ANNI");

    // Devo controllare che non stia cancellando livelli di cui abbia già
    // aggiunto interventi.
    // può capitare se faccio una seconda passata nella configurazione del bando

    if (errors.isEmpty())
    {
      List<CruscottoInterventiDTO> elenco = cruscottoEJB.getInterventi(idBando);
      if (elenco != null)
      {
        Vector<Long> idLIvelli = new Vector<>();
        idLIvelli = strToVector(tipiOperazioni);

        for (CruscottoInterventiDTO item : elenco)
        {
          if (!idLIvelli.contains(item.getIdLivello()))
          {
              errors.addError("tipiOperazioni",
                  "E' necessario inserire il tipo operazione "
                      + item.getOperazione()
                      + " perchè sono già stati abbinati interventi!");


          }
        }
      }
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      model.addAttribute("denominazione", denominazione);
      model.addAttribute("emailReferenteBando", emailReferenteBando);
      model.addAttribute("referenteBando", referenteBando);
      model.addAttribute("dataApertura", dataApertura);
      model.addAttribute("dataChiusura", dataChiusura);
      model.addAttribute("vBandoMultiplo", bandoMultiplo);
      model.addAttribute("vFlagRibassoInterventi", flagRibassoInterventi);
      model.addAttribute("annoRif", annoRif);
      model.addAttribute("idAmministrazioni", amministrazioni);
      model.addAttribute("idTipiOperazioni", tipiOperazioni);
      model.addAttribute("isErrorPostData", "isErrorPostData");
      model.addAttribute("idAmbitiTematici", ambitiTematici);
      model.addAttribute("numeroAnniMantenimento", numeroAnniMantenimentoStr);
      
      List<DecodificaDTO<Long>> listCategoriaEvento = gestioneEventiEJB.getListDecodificaCategorieEvento();
      model.addAttribute("listCategoriaEvento",listCategoriaEvento);
      model.addAttribute("idCategoriaEvento", idCategoriaEvento);
      
      if(idCategoriaEvento != null && !idCategoriaEvento.equals(""))
      {
    	  List<DecodificaDTO<Long>> listEventoCalamitoso = gestioneEventiEJB.getListEventiCalamitosi(Long.parseLong(idCategoriaEvento));
    	  model.addAttribute("listEventoCalamitoso", listEventoCalamitoso);
    	  model.addAttribute("idEventoCalamitoso", idEventoCalamitoso);
      }

      return datiIdentificativiGet(model, request, session, idBando);
    }
    else
    {
        	
      // Non ci sono errori, aggiorno i dati del bando
      bando = (BandoDTO) session.getAttribute("bando");

      String modified = "";
      if (bando.getDenominazione() != null)
        if (bando.getDenominazione().compareTo(denominazione) != 0)
          modified = modified.concat("Modificata denominazione da: \""
              + bando.getDenominazione() + "\" a: \"" + denominazione + "\"\n");
      bando.setDenominazione(denominazione);

      if (bando.getReferenteBando() != null)
        if (bando.getReferenteBando().compareTo(referenteBando) != 0)
          modified = modified
              .concat("Modificato referente da: \"" + bando.getReferenteBando()
                  + "\" a: \"" + referenteBando + "\"\n");
      bando.setReferenteBando(referenteBando);

      if (bando.getEmailReferenteBando() != null)
        if (bando.getEmailReferenteBando().compareTo(emailReferenteBando) != 0)
          modified = modified.concat("Modificata email referente da: \""
              + bando.getEmailReferenteBando() + "\" a: \""
              + emailReferenteBando + "\"\n");
      bando.setEmailReferenteBando(emailReferenteBando);

      if (bando.getDataInizio() != null)
        if (bando.getDataInizio()
            .compareTo(NemboUtils.DATE.parseDateTime(dataApertura)) != 0)
          modified = modified.concat("Modificata data inizio da: \""
              + bando.getDataInizioStr() + "\" a: \"" + dataApertura + "\"\n");
      bando
          .setDataInizio(NemboUtils.DATE.parseDateTime(dataApertura, true));

      if (bando.getDataFine() != null)
        if (bando.getDataFine()
            .compareTo(NemboUtils.DATE.parseDateTime(dataChiusura)) != 0)
          modified = modified.concat("Modificata data fine da: \""
              + bando.getDataFineStr() + "\" a: \"" + dataChiusura + "\"\n");
      bando.setDataFine(NemboUtils.DATE.parseDateTime(dataChiusura));
      
      
      if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE){
    	  DecodificaDTO<Long> eventoCalamitosoNew = gestioneEventiEJB.getListEventoCalamitoso(Long.parseLong(idCategoriaEvento), new long[]{Long.parseLong(idEventoCalamitoso)}).get(0);
      	  DecodificaDTO<Long> categoriaEventoNew = gestioneEventiEJB.getListDecodificaCategorieEvento(new long[]{Long.parseLong(idCategoriaEvento)}).get(0);  
      	  String descEventoCalamitosoNew = eventoCalamitosoNew.getDescrizione();
      	  String descCatEventoNew = eventoCalamitosoNew.getInfoAggiuntiva();
    	  if (bando.getDescCatEvento() != null)
              if (bando.getIdCategoriaEvento() != Long.parseLong(idCategoriaEvento))
                modified = modified.concat("Modificata categoria evento da: \""
                    + bando.getDescCatEvento() + "\" a: \"" + descCatEventoNew + "\"\n");
            bando.setIdCategoriaEvento(Long.parseLong(idCategoriaEvento));
            
    	    if (bando.getDescEventoCalamitoso() != null)
    	        if (bando.getIdEventoCalamitoso() != Long.parseLong(idEventoCalamitoso))
    	          modified = modified.concat("Modificata descrizione evento da: \""
    	              + bando.getDescEventoCalamitoso() + "\" a: \"" + descEventoCalamitosoNew + "\"\n");
    	      bando.setIdEventoCalamitoso(Long.parseLong(idEventoCalamitoso));
      }else{
    	  bando.setIdCategoriaEvento(null);
    	  bando.setIdEventoCalamitoso(null);
      }
      
        
      bando.setNumeroAnniMantenimento(numeroAnniMantenimento);

      String ammComp = "";
      if (session.getAttribute("ammCompetenzaAssociate") != null)
      {
        List<AmmCompetenzaDTO> v = (List<AmmCompetenzaDTO>) session
            .getAttribute("ammCompetenzaAssociate");
        Iterator<AmmCompetenzaDTO> iter = v.iterator();

        while (iter.hasNext())
        {
          AmmCompetenzaDTO a = iter.next();
          ammComp += "&" + a.getIdAmmCompetenza() + "=" + a.getDescrizione();
        }

        if (ammComp != null)
          if (amministrazioni.compareTo(ammComp) != 0)
          {

            String s = "";
            String[] splitted = ammComp.split("&");
            for (int i = 1; i < splitted.length; i++)
              s += splitted[i] + "  ";

            String s2 = "";
            String[] splitted2 = amministrazioni.split("&");
            for (int i = 1; i < splitted2.length; i++)
              s2 += splitted2[i] + "  ";

            modified = modified
                .concat("Modificato elenco amministrazioni da: \"" + s
                    + "\"  a: \"" + s2 + "\"\n");
          }
      }
      bando.setvIdAmministrazioni(strToVector(amministrazioni));

      if (bando.getAnnoCampagna() != null)
        if (bando.getAnnoCampagna().compareTo(annoRif) != 0)
          modified = modified.concat("Modificato anno di riferimento da: \""
              + bando.getAnnoCampagna() + "\" a: \"" + annoRif + "\"\n");
      bando.setAnnoCampagna(annoRif);

      bando.setFlagTitolaritaRegionale(titolaritaRegionale);

      if (bando.getFlagDomandaMultipla() != null)
        if (bando.getFlagDomandaMultipla().compareTo(bandoMultiplo) != 0)
          modified = modified.concat("Modificato flag bando multiplo da: \""
              + bando.getFlagDomandaMultipla() + "\" a: \"" + bandoMultiplo
              + "\"\n");
      bando.setFlagDomandaMultipla(bandoMultiplo);

      String tipiOp = "";

      if (session.getAttribute("tipiOperazioneAssociati") != null)
      {
        List<TipoOperazioneDTO> v = (List<TipoOperazioneDTO>) session
            .getAttribute("tipiOperazioneAssociati");
        Iterator<TipoOperazioneDTO> iter = v.iterator();

        while (iter.hasNext())
        {
          TipoOperazioneDTO a = iter.next();
          tipiOp += "&" + a.getIdLivello() + "=" + a.getCodice() + " "
              + a.getDescrizione();
        }

        if (tipiOp != null)
          if (tipiOperazioni.compareTo(tipiOp) != 0)
          {

            String s = "";
            String[] splitted = tipiOp.split("&");
            for (int i = 1; i < splitted.length; i++)
              s += splitted[i] + "  ";

            String s2 = "";
            String[] splitted2 = tipiOperazioni.split("&");
            for (int i = 1; i < splitted2.length; i++)
              s2 += splitted2[i] + "  ";

            modified = modified
                .concat("Modificato elenco tipi operazioni da: \"" + s
                    + "\" \na: \"" + s2 + "\"\n");
          }
      }
      bando.setvIdTipiOperazioni(strToVector(tipiOperazioni));

      bando.setFlagRibassoInterventi(flagRibassoInterventi);
      try
      {
        idBando = cruscottoEJB.aggiornaDatiIdentificativi(bando);

        if (modified != null && modified != "")
        {
          UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
              .getAttribute("utenteAbilitazioni");
          Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
          Long idBandoOggetto = cruscottoEJB.getFirstIdBandoOggetto(idBando);
          if (modified.length() > 3999)
            modified = "Campo troppo lungo per il DB - come fare?";
          if (idBandoOggetto != null)
            cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto.longValue(),
                idUtente,
                NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.DATI_IDENTIFICATIVI,
                modified);
        }

        if (session.getAttribute("ammCompetenzaAssociate") != null)
          session.removeAttribute("ammCompetenzaAssociate");
        if (session.getAttribute("tipiOperazioneAssociati") != null)
          session.removeAttribute("tipiOperazioneAssociati");
      }
      catch (InternalUnexpectedException e)
      {
        errors.addError("tipiOperazioni", e.getMessage());
        model.addAttribute("errors", errors);
        model.addAttribute("denominazione", denominazione);
        model.addAttribute("emailReferenteBando", emailReferenteBando);
        model.addAttribute("referenteBando", referenteBando);
        model.addAttribute("dataApertura", dataApertura);
        model.addAttribute("dataChiusura", dataChiusura);
        model.addAttribute("vtitolaritaRegionale", titolaritaRegionale);
        model.addAttribute("vBandoMultiplo", bandoMultiplo);
        model.addAttribute("vFlagRibassoInterventi", flagRibassoInterventi);
        model.addAttribute("annoRif", annoRif);
        model.addAttribute("idAmministrazioni", amministrazioni);
        model.addAttribute("idTipiOperazioni", tipiOperazioni);
        model.addAttribute("isErrorPostData", "isErrorPostData");
        model.addAttribute("numeroAnniMantenimento", numeroAnniMantenimentoStr);
        return datiIdentificativiGet(model, request, session, idBando);
      }
      bando.setIdBando(idBando);
      bando.setFlagMaster("N");
      session.setAttribute("bando", bando);
      session.setAttribute("idbando", idBando);

      if(bando.getIdProcedimentoAgricolo()==NemboConstants.TIPO_PROCEDIMENTO_AGRICOLO.AVVERSITA_ATMOSFERICHE){
    	  return "redirect:scelta_comuni.do";
      }
      return "redirect:attiPubblicati.do";
     
    }
  }

  @RequestMapping(value = "loadTipiOperazioniDisponibili_{idBando}", produces = "application/json")
  @ResponseBody
  public TreeMap<Long, Object> loadTipiOperazioniDisponibili(
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    List<TipoOperazioneDTO> tipiOperazioneDisponibili = new ArrayList<>();

    BandoDTO bando = cruscottoEJB.getInformazioniBando(idBando);
    tipiOperazioneDisponibili = cruscottoEJB.getTipiOperazioniDisponibili(idBando);

    session.setAttribute("tipiOperazioneDisponibili",
        tipiOperazioneDisponibili);
    TreeMap<Long, Object> values = new TreeMap<Long, Object>();
    TreeMap<String, String> parameters = null;

    if (tipiOperazioneDisponibili != null
        && tipiOperazioneDisponibili.size() > 0)
      for (TipoOperazioneDTO tipoOp : tipiOperazioneDisponibili)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("idLivello", String.valueOf(tipoOp.getIdLivello()));
        parameters.put("descrizioneEstesa", tipoOp.getDescrizioneEstesa());
        values.put(tipoOp.getIdLivello(), parameters);
      }

    return values;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "loadTipiOperazioniSelezionati_{idBando}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadTipiOperazioniSelezionati(
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    List<TipoOperazioneDTO> tipiOperazioneAssociatiDef = new Vector<TipoOperazioneDTO>();
    List<TipoOperazioneDTO> tipiOperazioneAssociati = cruscottoEJB
        .getTipiOperazioniAssociati(idBando);

    if (tipiOperazioneAssociati != null)
      tipiOperazioneAssociatiDef = tipiOperazioneAssociati;

    TreeMap<String, Object> values = new TreeMap<String, Object>();
    TreeMap<String, String> parameters = null;

    // Ora devo aggiungere (se non presenti) quelle già selezionate dall'utente.
    // questo è necessario perchè il plugin dual-list genera id statici quindi
    // bisogna ricaricarlo ogni volta che si aprono le popup amministrazioni e
    // tipi operazioni
    String[] idTipiOperazioni = request.getParameterValues("tipiOperazioni");
    if (idTipiOperazioni != null && idTipiOperazioni.length > 0)
    {
      boolean trovato;
      TipoOperazioneDTO ammTrovata = null;
      List<TipoOperazioneDTO> tipiOperazioneDisponibili = null;

      if (tipiOperazioneAssociati == null)
      {
        tipiOperazioneDisponibili = (List<TipoOperazioneDTO>) session
            .getAttribute("tipiOperazioneDisponibili");
      }

      for (int x = 0; x < idTipiOperazioni.length; x++)
      {
        String idAmm = idTipiOperazioni[x];
        trovato = false;
        if (tipiOperazioneAssociati == null)
        {
          for (int i = 0; i < tipiOperazioneDisponibili.size(); i++)
          {
            TipoOperazioneDTO amm = tipiOperazioneDisponibili.get(i);
            if (Long.parseLong(idAmm) == amm.getIdLivello())
            {
              trovato = true;
              ammTrovata = amm;
            }
          }
        }
        else
        {
          for (int i = 0; i < tipiOperazioneAssociati.size(); i++)
          {
            TipoOperazioneDTO amm = tipiOperazioneAssociati.get(i);
            if (Long.parseLong(idAmm) == amm.getIdLivello())
            {
              trovato = true;
              ammTrovata = amm;
            }
          }
        }
        if (trovato)
        {
          tipiOperazioneAssociatiDef.add(ammTrovata);
        }
      }
    }

    for (TipoOperazioneDTO tipoOp : tipiOperazioneAssociatiDef)
    {
      parameters = new TreeMap<String, String>();
      parameters.put("idLivello", String.valueOf(tipoOp.getIdLivello()));
      parameters.put("descrizioneEstesa", tipoOp.getDescrizioneEstesa());
      values.put(String.valueOf(tipoOp.getIdLivello()), parameters);
    }

    return values;
  }

  @RequestMapping(value = "loadAmministrazioniDisponibili_{idBando}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadAmministrazioniDisponibili(
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    List<AmmCompetenzaDTO> ammCompetenzaDisponibiliDef = null;
    List<AmmCompetenzaDTO> ammCompetenzaDisponibili = cruscottoEJB
        .getAmmCompetenzaDisponibili(idBando);
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    if (utenteAbilitazioni.isUtenteGAL())
    {
      Iterator<AmmCompetenzaDTO> iter = ammCompetenzaDisponibili.iterator();
      while (iter.hasNext())
      {
        if (iter.next().getIdAmmCompetenza() != utenteAbilitazioni
            .getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza())
          iter.remove();
      }
    }
    session.setAttribute("ammCompetenzaDisponibili", ammCompetenzaDisponibili);
    ammCompetenzaDisponibiliDef = ammCompetenzaDisponibili;
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (ammCompetenzaDisponibiliDef != null
        && ammCompetenzaDisponibiliDef.size() > 0)
    {
      for (AmmCompetenzaDTO amm : ammCompetenzaDisponibiliDef)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("IdAmmCompetenza",
            String.valueOf(amm.getIdAmmCompetenza()));
        if (amm.getDenominazioneuno() != null)
          parameters.put("descrizioneEstesa",
              amm.getDescrizioneEstesa() + " - " + amm.getDenominazioneuno());
        else
          parameters.put("descrizioneEstesa", amm.getDescrizioneEstesa());

        parameters.put("gruppo", amm.getDescEstesaTipoAmministraz());
        values.put(String.valueOf(amm.getIdAmmCompetenza()), parameters);
      }
    }

    return values;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "loadAmministrazioniSelezionate_{idBando}", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadAmministrazioniSelezionate(
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    List<AmmCompetenzaDTO> ammCompetenzaAssociateDef = new ArrayList<AmmCompetenzaDTO>();
    ;
    List<AmmCompetenzaDTO> ammCompetenzaAssociate = cruscottoEJB
        .getAmmCompetenzaAssociate(idBando, false);

    if (ammCompetenzaAssociate != null)
      ammCompetenzaAssociateDef = ammCompetenzaAssociate;

    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    // Ora devo aggiungere (se non presenti) quelle già selezionate dall'utente.
    // questo è necessario perchè il plugin dual-list genera id statici quindi
    // bisogna ricaricarlo ogni volta che si aprono le popup amministrazioni e
    // tipi operazioni
    String[] idAmmSelezionate = request.getParameterValues("amministrazione");
    if (idAmmSelezionate != null && idAmmSelezionate.length > 0)
    {
      boolean trovato;
      AmmCompetenzaDTO ammTrovata = null;
      List<AmmCompetenzaDTO> ammCompetenzaDisponibili = null;
      if (ammCompetenzaAssociate == null)
      {
        ammCompetenzaDisponibili = (List<AmmCompetenzaDTO>) session
            .getAttribute("ammCompetenzaDisponibili");
      }
      for (int x = 0; x < idAmmSelezionate.length; x++)
      {
        String idAmm = idAmmSelezionate[x];
        trovato = false;
        if (ammCompetenzaAssociate == null)
        {
          for (int i = 0; i < ammCompetenzaDisponibili.size(); i++)
          {
            AmmCompetenzaDTO amm = ammCompetenzaDisponibili.get(i);
            if (Long.parseLong(idAmm) == amm.getIdAmmCompetenza())
            {
              trovato = true;
              ammTrovata = amm;
            }
          }
        }
        else
        {
          for (int i = 0; i < ammCompetenzaAssociate.size(); i++)
          {
            AmmCompetenzaDTO amm = ammCompetenzaAssociate.get(i);
            if (Long.parseLong(idAmm) == amm.getIdAmmCompetenza())
            {
              trovato = true;
              ammTrovata = amm;
            }
          }
        }
        if (trovato)
        {
          ammCompetenzaAssociateDef.add(ammTrovata);
        }
      }
    }

    if (session.getAttribute("ammCompetenzaSelezionataGAL") != null)
      session.removeAttribute("ammCompetenzaSelezionataGAL");

    if (ammCompetenzaAssociateDef != null
        && ammCompetenzaAssociateDef.size() > 0)
    {
      for (AmmCompetenzaDTO amm : ammCompetenzaAssociateDef)
      {
        parameters = new TreeMap<String, String>();
        parameters.put("IdAmmCompetenza",
            String.valueOf(amm.getIdAmmCompetenza()));
        parameters.put("descrizioneEstesa", amm.getDescrizioneEstesa());
        parameters.put("gruppo", amm.getDescEstesaTipoAmministraz());
        values.put(String.valueOf(amm.getIdAmmCompetenza()), parameters);
      }
      session.setAttribute("ammCompetenzaSelezionataGAL",
          ammCompetenzaAssociateDef.get(0));
    }

    return values;
  }

  private Vector<Long> strToVector(String param)
  {
    Vector<Long> vct = new Vector<Long>();
    // es: &7=Provincia di Alessandria&4=Provincia di Novara&3=Provincia di
    // Vercelli
    String[] tmpStr = param.split("&");
    for (int i = 1; i < tmpStr.length; i++)
    {
      vct.add(new Long(tmpStr[i].split("=")[0]));
    }
    return vct;
  }

  private Date strToDateTime(String date)
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        "dd/MM/yyyy HH:mm:ss");
    try
    {
      if (GenericValidator.isBlankOrNull(date))
        return null;
      return simpleDateFormat.parse(date);
    }
    catch (ParseException ex)
    {
      return null;
    }
  }

  @RequestMapping(value = "salvaAmministrazione_{amm}", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String salvaAmministrazione(HttpServletRequest request,
      HttpSession session, @PathVariable("amm") String amm)
      throws InternalUnexpectedException
  {
    String amministrazioni = amm;
    if (amministrazioni != null && amministrazioni.compareTo("") != 0)
    {
      String s = amministrazioni.split("=")[0];
      String id = s.split("&")[1];
      session.setAttribute("idAmmCompSelezionata", id);
    }
    else
    {
      if (session.getAttribute("idAmmCompSelezionata") != null)
        session.removeAttribute("idAmmCompSelezionata");
    }

    return "";
  }

}
