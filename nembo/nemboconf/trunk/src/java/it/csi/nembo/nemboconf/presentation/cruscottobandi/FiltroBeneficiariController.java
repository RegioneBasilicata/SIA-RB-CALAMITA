package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class FiltroBeneficiariController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "filtrobeneficiari", method = RequestMethod.GET)
  public String filtrobeneficiariGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    // diamo una rinfrescata al bando in sessione perchè in questa pagina
    // aggiorno la descrizione del filtro proprio in questo oggetto
    bando = cruscottoEJB.getInformazioniBando(bando.getIdBando());
    session.setAttribute("bando", bando);

    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(bando.getIdBando());
    List<DecodificaDTO<String>> elencoFiltriAziende = cruscottoEJB
        .getElencoFiltriSelezioneAziende();
    List<BeneficiarioDTO> beneficiari = cruscottoEJB
        .getBeneficiari(bando.getIdBando());
    List<BeneficiarioDTO> elenco = cruscottoEJB
        .getElencoBeneficiariDisponibili(bando.getIdBando());

    model.addAttribute("formeDisponibili",
        ((elenco != null && elenco.size() > 0) ? true
            : ((beneficiari != null && !beneficiari.isEmpty()) ? true
                : false)));
    model.addAttribute("formeGiuridiche", beneficiari);
    session.setAttribute("elencoFiltriAziende", elencoFiltriAziende);
    model.addAttribute("elencoFiltriAziende", elencoFiltriAziende);
    model.addAttribute("idBando", bando.getIdBando());
    model.addAttribute("descrizioneFiltro", bando.getDescrizioneFiltro());
    model.addAttribute("modificaAbilitata", modificaAbilitata);
    return "cruscottobandi/filtroBeneficiari";
  }

  @RequestMapping(value = "filtrobeneficiari", method = RequestMethod.POST)
  public String filtrobeneficiariPost(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @RequestParam(value = "idDescrizioneFiltro", required = false) String idDescrizioneFiltro)
      throws InternalUnexpectedException
  {

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    String istruzioneSql = null;
    String descrizioneFiltro = null;
    if (!GenericValidator.isBlankOrNull(idDescrizioneFiltro))
    {
      @SuppressWarnings("unchecked")
      List<DecodificaDTO<String>> elencoFiltriAziende = (List<DecodificaDTO<String>>) session
          .getAttribute("elencoFiltriAziende");
      for (DecodificaDTO<String> filtro : elencoFiltriAziende)
      {
        if (filtro.getId().equals(idDescrizioneFiltro))
        {
          istruzioneSql = filtro.getCodice().replace("<br/>", "\n");
          descrizioneFiltro = filtro.getDescrizione().replace("<br/>", "\n");
        }
      }

      if (idDescrizioneFiltro.equals("0"))
      {
        istruzioneSql = null;
        descrizioneFiltro = null;
      }

      String oldDescription = cruscottoEJB
          .getDescrizioneFiltro(bando.getIdBando());
      cruscottoEJB.updateFiltroBando(bando.getIdBando(), istruzioneSql,
          descrizioneFiltro);

      // LOG
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      Long idBandoOggetto = cruscottoEJB
          .getFirstIdBandoOggetto(bando.getIdBando());
      if (idBandoOggetto != null)
      {
        if (descrizioneFiltro == null || descrizioneFiltro == "")
          descrizioneFiltro = "-- nessuno --";
        if (oldDescription == null)
          oldDescription = "-- nessuno --";

        cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
            NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.FILTRO_BENEFICIARI,
            "Modifica ulteriore filtro da: \"" + oldDescription + "\" a: \""
                + descrizioneFiltro + "\"\n");
      }
    }

    /*
     * Se il bando ha su ID_TIPO_LIVELLO un valore che si riferisce ad un rek su
     * NEMBO_D_TIPO_LIVELLO con CODICE = ‘I’ apre la pagine di inserimento degli
     * interventi legati al bando, altrimenti passa allo step di apertura della
     * pagina attivazione degli oggetti legati al bando
     */

    String codiceTipoBando = bando.getCodiceTipoBando();
    if ("I".equals(codiceTipoBando)
        || NemboConstants.TIPO_BANDO.GAL.equals(codiceTipoBando))
    {
      return "redirect:interventi.do";
    }
    else
    {
      return "redirect:oggetti.do";
    }

  }

  @RequestMapping(value = "filtrobeneficiariget", method = RequestMethod.POST)
  public String filtrobeneficiariget(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    return filtrobeneficiariGet(model, request, session);
  }

  @RequestMapping(value = "loadFormeDisponibili_{idBando}", produces = "application/json")
  @ResponseBody
  public List<BeneficiarioDTO> loadFormeDisponibili(HttpServletRequest request,
      HttpSession session, @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    List<BeneficiarioDTO> elenco = cruscottoEJB
        .getElencoBeneficiariDisponibili(idBando);
    if (elenco == null)
    {
      elenco = new ArrayList<BeneficiarioDTO>();
    }
    return elenco;
  }

  @RequestMapping(value = "loadFormeSelezionate_{idBando}", produces = "application/json")
  @ResponseBody
  public List<BeneficiarioDTO> loadFormeSelezionate(HttpServletRequest request,
      HttpSession session, @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    List<BeneficiarioDTO> elenco = cruscottoEJB
        .getElencoBeneficiariSelezionati(idBando);
    if (elenco == null)
    {
      elenco = new ArrayList<BeneficiarioDTO>();
    }
    return elenco;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "salva_forme_giuridiche_{idBando}", method = RequestMethod.POST)
  @ResponseBody
  public String salvaForme(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable(value = "idBando") long idBando)
      throws InternalUnexpectedException
  {
    String[] aIdFgTipologia = request.getParameterValues("selectedList");
    cruscottoEJB.updateFormeGiuridiche(idBando, aIdFgTipologia);
    return "OK";
  }

}
