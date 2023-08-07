package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class AllegatiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "allegati", method = RequestMethod.GET)
  public String controlliGet(ModelMap model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();

    List<DecodificaDTO<String>> gruppiDisponibili = cruscottoEJB
        .getElencoGruppiControlliDisponibili(idBando,
            "ALLEG");

    List<DecodificaDTO<String>> listaFlagAllegati = new ArrayList<DecodificaDTO<String>>();
    listaFlagAllegati.add(new DecodificaDTO<String>("N", "No"));
    listaFlagAllegati.add(new DecodificaDTO<String>("O", "Obbligatorio"));
    listaFlagAllegati.add(new DecodificaDTO<String>("F", "Facoltativo"));
    session.setAttribute("listaFlagAllegati", listaFlagAllegati);

    List<DecodificaDTO<String>> listaTipiFile = cruscottoEJB
        .getElencoTipiFileDisponibili();
    session.setAttribute("listaTipiFile", listaTipiFile);
    model.addAttribute("listaTipiFile", listaTipiFile);

    List<DecodificaDTO<String>> tipiVincoli = cruscottoEJB
        .getElencoTipiVincoli();
    session.setAttribute("tipiVincoliDich", tipiVincoli);
    session.removeAttribute("vincoliTrovati");
    session.removeAttribute("mapIdLegamiInfo");
    session.removeAttribute("elencoVincoli");
    session.removeAttribute("sessionProcedimentoOggettoQuadroInserito");
    session.removeAttribute("flagAttivoDichiarazione");

    session.setAttribute("gruppiDisponibiliDich", gruppiDisponibili);
    session.setAttribute("elencoGruppiInfo", null);
    model.addAttribute("idBando", idBando);
    model.addAttribute("idQuadroOggetto", 0);
    model.addAttribute("idBandoOggetto", 0);
    model.addAttribute("gruppiDisponibili", gruppiDisponibili);
    model.addAttribute("descrGruppoSelezionato", "");

    return "cruscottobandi/allegati";
  }

  @SuppressWarnings("unchecked")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaallegati", method = RequestMethod.POST)
  public String visualizzaDichiarazioni(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @RequestParam(value = "idQuadroOggetto") String idQuadroOggettoEBandoOggetto)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");

    Long idQuadroOggetto = Long
        .parseLong(idQuadroOggettoEBandoOggetto.split("_")[0]);
    Long idBandoOggetto = Long
        .parseLong(idQuadroOggettoEBandoOggetto.split("_")[1]);

    List<DecodificaDTO<String>> gruppiDisponibili = (List<DecodificaDTO<String>>) session
        .getAttribute("gruppiDisponibiliDich");
    List<GruppoInfoDTO> dichiarazioni = cruscottoEJB.getElencoDettagliInfo(
        bando.getIdBando(), idQuadroOggetto, idBandoOggetto);
    List<GruppoInfoDTO> elencoGruppiInfoCatalogo = cruscottoEJB
        .getElencoDettagliInfoCatalogo(bando.getIdBando(), idQuadroOggetto,
            idBandoOggetto);

    session.setAttribute("elencoGruppiInfo", dichiarazioni);
    session.setAttribute("elencoGruppiInfoCatalogo", elencoGruppiInfoCatalogo);

    if(gruppiDisponibili!=null && gruppiDisponibili.size()>0){
    	for (DecodificaDTO<String> item : gruppiDisponibili)
        {
          if (item.getId().compareTo(idQuadroOggetto + "_" + idBandoOggetto) == 0)
          {
            model.addAttribute("descrGruppoSelezionato",
                item.getDescrizioneEstesa());
            session.setAttribute("descrGruppoSelezionato",
                item.getDescrizioneEstesa());
            break;
          }
        }
    }
    
    DecodificaDTO<Long> infoBandoOggetto = cruscottoEJB.getInfoBandoOggetto(
        bando.getIdBando(), idQuadroOggetto, idBandoOggetto);
    session.setAttribute("flagAttivoDichiarazione",
        infoBandoOggetto.getDescrizione());

    if (!model.containsKey("noRicalcoloVincoli"))
    {
      List<DecodificaDTO<String>> vincoliTrovati = cruscottoEJB.getIdLegameInfo(
          infoBandoOggetto.getId(),
          idQuadroOggetto);
      prepareVincoli(vincoliTrovati, session);
    }

    // Controllo se sono già stati inseriti dei procedimenti per questo
    // bando, perchè in questo caso devo limitare le azioni sulla pagina
    session.setAttribute("sessionProcedimentoOggettoQuadroInserito",
        cruscottoEJB
            .isProcedimentoOggettoQuadroInserito(bando.getIdBando(),
                infoBandoOggetto.getId(), idQuadroOggetto));

    model.addAttribute("idBando", bando.getIdBando());
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
    model.addAttribute("idBandoOggetto", infoBandoOggetto.getId());
    model.addAttribute("gruppiDisponibili", gruppiDisponibili);
    model.addAttribute("elencoGruppiInfo", dichiarazioni);
    model.addAttribute("listaTipiFile", session.getAttribute("listaTipiFile"));
    model.addAttribute("elencoGruppiInfoNoCatalogo",
        eliminaCatalogo(dichiarazioni));
    model.addAttribute("elencoGruppiInfoCatalogo", elencoGruppiInfoCatalogo);
    return "cruscottobandi/allegati";
  }

  @SuppressWarnings(
  { "unchecked" })
  @RequestMapping(value = "confermaallegati", method = RequestMethod.POST)
  public String conferma(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idQuadroOggetto") long idQuadroOggetto,
      @RequestParam(value = "idBandoOggetto") long idBandoOggetto)
      throws InternalUnexpectedException
  {

    model.addAttribute("preferRqParameter", Boolean.TRUE);
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<GruppoInfoDTO> dichiarazioni = (List<GruppoInfoDTO>) session
        .getAttribute("elencoGruppiInfo");
    List<GruppoInfoDTO> dichiarazioniOld = dichiarazioni;
    Errors errors = new Errors();
    if (dichiarazioni != null)
    {
      dichiarazioni = validaDichiarazioni(model, request, session,
          idQuadroOggetto, idBandoOggetto, dichiarazioni, errors);

      if (errors.isEmpty())
      {

        cruscottoEJB.inserisciDichiarazioni(bando.getIdBando(), idBandoOggetto,
            idQuadroOggetto, dichiarazioni);
        model.remove("nuoveDichiarazioni"); // Forzo il refresh dei dati
        model.addAttribute("noRicalcoloVincoli", "noRicalcoloVincoli");

        List<DecodificaDTO<String>> gruppiDisponibili = (List<DecodificaDTO<String>>) session
            .getAttribute("gruppiDisponibiliDich");
        String nomeQuadro = "";
        if(gruppiDisponibili!=null && gruppiDisponibili.size()>0){
        	for (DecodificaDTO<String> item : gruppiDisponibili)
            {
              if (item.getId()
                  .compareTo(idQuadroOggetto + "_" + idBandoOggetto) == 0)
              {
                nomeQuadro = item.getDescrizione();
                break;
              }
            }
        }
        
        logOperationGruppoInfoDTO("allegato", idBandoOggetto, nomeQuadro,
            dichiarazioni, dichiarazioniOld, session);

        return visualizzaDichiarazioni(model, request, session,
            idQuadroOggetto + "_" + idBandoOggetto);
      }
    }

    return "cruscottobandi/allegati";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "avantiallegati", method = RequestMethod.POST)
  public String avanti(ModelMap model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException
  {

    /*
     * Per ciascun rek di NEMBO_R_BANDO_OGGETTO_QUADRO, relativo a
     * ID_QUADRO_OGGETTO che fa riferimento a un quadro con CODICE = ‘DICH’
     * occorre che - Sia stato inserito almeno un rek su NEMBO_D_GRUPPO_INFO -
     * Siano stati inseriti su NEMBO_D_GRUPPO_INFO tutti i rek che su
     * NEMBO_D_GRUPPO_INFO_CATALOGO risultavano con FLAG_INFO_OBBLIGATORIA = ‘S’
     * che su NEMBO_R_INFO_CATALOGO fanno riferimento ai livelli a cui il bando
     * è legato per ciascun ID_QUADRO_OGGETTO;
     */

    String msgErrore = "";
    List<DecodificaDTO<String>> gruppiDisponibili = (List<DecodificaDTO<String>>) session
        .getAttribute("gruppiDisponibiliDich");

    if (gruppiDisponibili != null && gruppiDisponibili.size()>0)
    {
      for (DecodificaDTO<String> item : gruppiDisponibili)
      {
        Long idQuadroOggettoItem = Long.parseLong(item.getId().split("_")[0]);
        Long idBandoOggettoItem = Long.parseLong(item.getId().split("_")[1]);

        if (idBandoOggettoItem != 0 && !cruscottoEJB
            .isDichiarazionePresente(idBandoOggettoItem, idQuadroOggettoItem))
        {
          msgErrore = msgErrore
              + "Non hai inserito alcun allegato per l'oggetto/istanza "
              + item.getDescrizioneEstesa() + ". <br>";
        }
      }
    }
    if (!GenericValidator.isBlankOrNull(msgErrore))
    {
      model.addAttribute("msgErrore", msgErrore);
      return controlliGet(model, request, session);
    }
    return "redirect:ricevuta.do";
  }

  private void prepareVincoli(List<DecodificaDTO<String>> vincoli,
      HttpSession session)
  {
    List<DecodificaDTO<String>> elencoVincoli = new ArrayList<DecodificaDTO<String>>();
    String cod;

    if (vincoli != null)
    {
      // Creo una mappa che serve a inizializzare le select dei vincoli
      // <ID_LEGAME_INFO, identificativo ( U/O + indice)
      HashMap<String, String> mapIdLegamiInfo = new HashMap<String, String>();
      for (int i = 0; i < vincoli.size(); i++)
      {
        cod = vincoli.get(i).getDescrizione() + "_" + i;
        mapIdLegamiInfo.put(vincoli.get(i).getId(), cod);
        elencoVincoli.add(new DecodificaDTO<String>(cod, cod));
      }

      session.setAttribute("elencoVincoli", elencoVincoli);
      session.setAttribute("mapIdLegamiInfo", mapIdLegamiInfo);
      session.setAttribute("vincoliTrovati", vincoli);
    }
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "refreshGruppoInfoAllegati", method = RequestMethod.POST)
  public String refreshGruppoInfo(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idQuadroOggetto") long idQuadroOggetto,
      @RequestParam(value = "idBandoOggetto") long idBandoOggetto)
      throws InternalUnexpectedException
  {
    model.addAttribute("preferRqParameter", Boolean.TRUE);
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<GruppoInfoDTO> dichImportateCatalogo = (List<GruppoInfoDTO>) session
        .getAttribute("elencoGruppiInfo");
    List<GruppoInfoDTO> elencoGruppiInfoCatalogo = (List<GruppoInfoDTO>) session
        .getAttribute("elencoGruppiInfoCatalogo");
    List<GruppoInfoDTO> elencoGruppiInfo = new ArrayList<GruppoInfoDTO>();
    // Aggiorno i gruppi info sulla base di quelli scelti nella popup
    // dall'utente
    String[] vIdGruppoInfo = request.getParameterValues("chkGruppoCatalogo");
    if(vIdGruppoInfo!=null && vIdGruppoInfo.length>0){
    	for (String idGruppo : vIdGruppoInfo)
        {
          if (!GenericValidator.isBlankOrNull(idGruppo))
          {
            for (GruppoInfoDTO item : elencoGruppiInfoCatalogo)
            {
              if (item.getIdGruppoInfo() == Long.parseLong(idGruppo))
              {
                elencoGruppiInfo.add(item);
              }
            }
          }
        }
    }
    
    validaDichiarazioni(model, request, session, idQuadroOggetto,
        idBandoOggetto, dichImportateCatalogo,
        new Errors());

    session.setAttribute("elencoGruppiInfo", elencoGruppiInfo);
    model.addAttribute("idBando", bando.getIdBando());
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    model.addAttribute("gruppiDisponibili",
        session.getAttribute("gruppiDisponibili"));
    model.addAttribute("elencoGruppiInfo", elencoGruppiInfo);
    model.addAttribute("elencoGruppiInfoNoCatalogo",
        eliminaCatalogo(elencoGruppiInfo));
    model.addAttribute("elencoGruppiInfoCatalogo", elencoGruppiInfoCatalogo);
    session.setAttribute("descrGruppoSelezionato",
        session.getAttribute("descrGruppoSelezionato"));

    return "cruscottobandi/allegati";
  }

  private List<GruppoInfoDTO> eliminaCatalogo(List<GruppoInfoDTO> dichiarazioni)
  {
    List<GruppoInfoDTO> dichNoCatalogo = new ArrayList<GruppoInfoDTO>();
    if(dichiarazioni!=null && dichiarazioni.size()>0){
    	for (GruppoInfoDTO item : dichiarazioni)
        {
          if ("N".equals(item.getFlagInfoCatalogo()))
          {
            dichNoCatalogo.add(item);
          }
        }
    }
    
    return dichNoCatalogo;
  }

  @SuppressWarnings(
  { "unchecked", "static-access" })
  private List<GruppoInfoDTO> validaDichiarazioni(ModelMap model,
      HttpServletRequest request, HttpSession session, long idQuadroOggetto,
      long idBandoOggetto, List<GruppoInfoDTO> dichiarazioni, Errors errors)
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<GruppoInfoDTO> nuoveDichiarazioni = new ArrayList<GruppoInfoDTO>();
    List<GruppoInfoDTO> dichImportateCatalogo = new ArrayList<GruppoInfoDTO>();
    GruppoInfoDTO gruppo = null;
    DettaglioInfoDTO dettaglio = null;
    List<DettaglioInfoDTO> elencoDettagli = null;
    List<DecodificaDTO<String>> vincoliTrovati = new ArrayList<DecodificaDTO<String>>();
    int cIdGruppo = 0;
    int cIdDettaglio = 0;

    // Questi due bellissimi contatori servono per cercare eventuali "buchi"
    // lasciati dall'utente nel caso in cui
    // si fosse divertito a inserire e eliminare le dichiarazioni prima di
    // confermare
    int tentativiGruppo = 0;
    int tentativiDett = 0;

    boolean elabora = true;
    boolean elaboraDettagli = true;
    boolean trovato = true;
    String titoloDich;
    String dettaglioDich;
    String flagObbl;
    String flagGestFile;
    String idTipoDocumento;
    String vincolo;
    String idGruppoInfo;
    String idDettaglioInfo;
    String[] aVincolo;
    String vincoloCompleto;

    List<DecodificaDTO<String>> tipiVincoli = (List<DecodificaDTO<String>>) session
        .getAttribute("tipiVincoliDich");

    if(dichiarazioni!=null && dichiarazioni.size()>0){
    	for (GruppoInfoDTO item : dichiarazioni)
        {
          if ("S".equals(item.getFlagInfoCatalogo()))
          {
            dichImportateCatalogo.add(item);
          }
        }

    }
    
    while (elabora)
    {
      titoloDich = request.getParameter("txtAreaGruppo_" + cIdGruppo);
      if (titoloDich == null && tentativiGruppo == 30)
      {
        elabora = false;
      }
      else
        if (titoloDich == null)
        {
          tentativiGruppo++;
          cIdGruppo++;
        }
        else
        {
          errors.validateMandatory(titoloDich, "txtAreaGruppo_" + cIdGruppo);
          if (titoloDich.indexOf("$$") != -1)
          {
            errors.addError("txtAreaGruppo_" + cIdGruppo,
                "Non puoi inserire $$ nel titolo dell'allegato!");
          }

          gruppo = new GruppoInfoDTO();
          gruppo.setDescrizione(
              NemboUtils.STRING.replaceAWantedChar(titoloDich));
          elencoDettagli = new ArrayList<DettaglioInfoDTO>();
          gruppo.setElencoDettagliInfo(elencoDettagli);
          gruppo.setFlagInfoCatalogo("N");

          idGruppoInfo = request.getParameter("idGruppoInfo_" + cIdGruppo);
          if (idGruppoInfo != null && idGruppoInfo.trim().length() > 0)
          {
            gruppo.setIdGruppoInfo(Long.parseLong(idGruppoInfo));
          }

          nuoveDichiarazioni.add(gruppo);
          tentativiDett = 0;
          cIdDettaglio = 0;
          elaboraDettagli = true;
          trovato = false;

          // Per ogni titolo inserito devo eseguire i controlli e preparare i
          // DTO
          while (elaboraDettagli)
          {
            dettaglioDich = request.getParameter(
                "txtDettaglioGruppo_" + cIdDettaglio + "_" + cIdGruppo);
            if (dettaglioDich == null && tentativiDett == 30)
            {
              elaboraDettagli = false;
            }
            else
              if (dettaglioDich == null)
              {
                tentativiDett++;
                cIdDettaglio++;
              }
              else
              {
                trovato = true;
                errors.validateMandatory(dettaglioDich,
                    "txtDettaglioGruppo_" + cIdDettaglio + "_" + cIdGruppo);
                controlloVariabili(
                    "txtDettaglioGruppo_" + cIdDettaglio + "_" + cIdGruppo,
                    dettaglioDich, errors);
                flagObbl = request.getParameter(
                    "flagObblDetailGruppo_" + cIdDettaglio + "_" + cIdGruppo);
                vincolo = request.getParameter(
                    "selvincolo_" + cIdDettaglio + "_" + cIdGruppo); // es O_1 /
                                                                     // U_2
                dettaglio = new DettaglioInfoDTO();
                dettaglio.setDescrizione(
                    NemboUtils.STRING.replaceAWantedChar(dettaglioDich));
                dettaglio.setFlagObbligatorio(
                    (!GenericValidator.isBlankOrNull(flagObbl)) ? "S" : "N");
                // dettaglio.setFlagGestioneFile("N");

                flagGestFile = "O";
                idTipoDocumento = request.getParameter(
                    "idTipoDocumento_" + cIdDettaglio + "_" + cIdGruppo);
                vincolo = request.getParameter(
                    "selvincolo_" + cIdDettaglio + "_" + cIdGruppo); // es
                // O_1
                // /
                // U_2

                if (!GenericValidator.isBlankOrNull(flagGestFile))
                {
                  if ((flagGestFile.equals("O") || flagGestFile.equals("F"))
                      && GenericValidator.isBlankOrNull(idTipoDocumento))
                  {
                    errors.addError(
                        "idTipoDocumento_" + cIdDettaglio + "_" + cIdGruppo,
                        "Campo obbligatorio");
                  }
                }

                dettaglio.setFlagGestioneFile(flagGestFile);
                dettaglio.setExtIdTipoDocumento(
                    (!GenericValidator.isBlankOrNull(idTipoDocumento))
                        ? new Long(idTipoDocumento)
                        : null);

                idDettaglioInfo = request.getParameter(
                    "idDettaglioInfo_" + cIdDettaglio + "_" + cIdGruppo);
                if (idDettaglioInfo != null
                    && idDettaglioInfo.trim().length() > 0)
                {
                  dettaglio.setIdDettaglioInfo(Long.parseLong(idDettaglioInfo));
                }

                if (idDettaglioInfo == null || "0".equals(idDettaglioInfo))
                {
                  model.addAttribute("isNuovoInserimento_" + cIdGruppo,
                      Boolean.TRUE);
                  model.addAttribute(
                      "isNuovoInserimento_" + cIdDettaglio + "_" + cIdGruppo,
                      Boolean.TRUE);
                }

                if (!GenericValidator.isBlankOrNull(flagObbl)
                    && !GenericValidator.isBlankOrNull(vincolo))
                {
                  errors.addError(
                      "flagObblDetailGruppo_" + cIdDettaglio + "_" + cIdGruppo,
                      "Non è possibile inserire i vincoli su allegati obbligatori");
                }

                if (!GenericValidator.isBlankOrNull(vincolo))
                {
                  /*
                   * Vincolo può essere UNIVOCO_X / OBBLIGATORIO_X /
                   * UPZIONALE_UNIVOCO_X
                   */
                  aVincolo = vincolo.split("_");
                  vincoloCompleto = aVincolo[0];
                  if (!NemboUtils.STRING.isNumeric(aVincolo[1]))
                  {
                    vincoloCompleto = vincoloCompleto + "_" + aVincolo[1];
                  }

                  dettaglio.setIdVincolo(
                      Long.parseLong(aVincolo[aVincolo.length - 1]) + 1);
                  if(tipiVincoli!=null && tipiVincoli.size()>0){
                	  for (DecodificaDTO<String> item : tipiVincoli)
                      {
                        if (item.getDescrizione().startsWith(vincoloCompleto))
                        {
                          dettaglio.setIdVincoloDichiarazione(
                              Long.parseLong(item.getId()));
                        }
                      }
                  }
                  
                }

                elencoDettagli.add(dettaglio);
                cIdDettaglio++;
              }
          }
          if (!trovato)
          {
            errors.addError("txtAreaGruppo_" + cIdGruppo,
                "Devi inserire almeno un dettaglio!");
          }
          cIdGruppo++;
        }
    }

    String identifVincolo = "";
    /* Ricreo i vincoli */
    for (int i = 0; i < 20; i++)
    {
      identifVincolo = request.getParameter("tipiVincoli_" + i);
      if (identifVincolo != null)
      {
    	  if(tipiVincoli!=null && tipiVincoli.size()>0){
    		  for (DecodificaDTO<String> item : tipiVincoli)
    	        {
    	          if (item.getCodice().equals(identifVincolo))
    	          {
    	            vincoliTrovati.add(new DecodificaDTO<String>(identifVincolo,
    	                identifVincolo, item.getDescrizione()));
    	          }
    	        }
    	  }
        
      }
    }
    prepareVincoli(vincoliTrovati, session);

    model.addAttribute("idBando", bando.getIdBando());
    model.addAttribute("idQuadroOggetto", idQuadroOggetto);
    model.addAttribute("idBandoOggetto", idBandoOggetto);
    model.addAttribute("gruppiDisponibili",
        session.getAttribute("gruppiDisponibiliDich"));
    model.addAttribute("elencoGruppiInfo", dichiarazioni);
    model.addAttribute("elencoGruppiInfoCatalogo",
        session.getAttribute("elencoGruppiInfoCatalogo"));
    model.addAttribute("elencoGruppiInfoNoCatalogo",
        eliminaCatalogo(dichiarazioni));
    model.addAttribute("descrGruppoSelezionato",
        session.getAttribute("descrGruppoSelezionato"));
    model.addAttribute("nuoveDichiarazioni", nuoveDichiarazioni);

    if (errors.isEmpty())
    {
      // metto insieme le dichiarazioni importate dal catalogo e quelle gestite
      // dall'utente
      dichImportateCatalogo.addAll(nuoveDichiarazioni);
    }
    else
    {
      model.addAttribute("errors", errors);
    }

    return dichImportateCatalogo;
  }

  private void controlloVariabili(String fieldName, String dettaglioDich,
      Errors errors)
  {
    int startIndex = 0;
    int idx;
    int newIdx;
    while (dettaglioDich.indexOf("$$", startIndex) != -1)
    {
      idx = dettaglioDich.indexOf("$$", startIndex);
      newIdx = dettaglioDich.indexOf(" ", idx);
      if (newIdx == -1)
      {
        newIdx = dettaglioDich.length();
      }
      String var = dettaglioDich.substring(idx, newIdx);
      if (!var.toUpperCase().equals("$$STRING")
          && !var.toUpperCase().equals("$$STRING")
          && !var.toUpperCase().equals("$$NUMBER")
          && !var.toUpperCase().equals("$$INTEGER")
          && !var.toUpperCase().equals("$$DATE"))
      {
        errors.addError(fieldName, "la variabile " + var + " non è ammessa!");
      }
      startIndex = newIdx;
    }
  }

}
