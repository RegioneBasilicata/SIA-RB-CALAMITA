package it.csi.nembo.nemboconf.presentation;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = "")
@RequestMapping("/index")
public class IndexController extends BaseController
{
  @RequestMapping(method = RequestMethod.GET)
  public String getIndex(ModelMap model, HttpSession session)
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    if (utenteAbilitazioni.getRuolo().isUtenteMonitoraggio())
    {
      return "redirect:monitoraggio/index.do";
    }
    model.addAttribute("catalogoMisure",
        NemboUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,
            Security.VISUALIZZA_CATALOGO_MISURE));
//Temporarily disabled
//    model.addAttribute("pianoRegionale",
//        NemboUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,
//            Security.VISUALIZZA_PIANO_FINANZIARIO_REGIONALE));
    
    model.addAttribute("ambitiTematici", false);// NemboconfUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,Security.VISUALIZZA_AMBITI_TEMATICI));
    model.addAttribute("pianoLeader", false); // NemboconfUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,Security.VISUALIZZA_PIANO_FINANZIARIO_LEADER));
    model.addAttribute("cruscottoBandi",
        NemboUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,
            Security.GESTISCI_CRUSCOTTO_BANDI));
    model.addAttribute("monitoraggio", NemboUtils.PAPUASERV
        .isMacroCUAbilitato(utenteAbilitazioni, Security.MONITORAGGIO));
    model.addAttribute("messaggistica", Boolean.TRUE);
    model.addAttribute("configurazioneCataloghi",
        utenteAbilitazioni.isUtenteServiziAgri());
    model.addAttribute("reportistica",
        NemboUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,
            Security.GESTISCI_CRUSCOTTO_BANDI));
 
    model.addAttribute("gestioneeventi", 
    		NemboUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,Security.GESTISCI_CRUSCOTTO_BANDI));
    // remove table filters from session
    final String ID_ELENCO_BANDI = "elencoBandi";
    final String ID_ELENCO_REPORT = "dettEstrazioneTable";
    final String ID_ELENCO_GRADUATORIE = "graduatorieTable";
    @SuppressWarnings("unchecked")
    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(NemboConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    mapFilters.remove(ID_ELENCO_BANDI);
    mapFilters.remove(ID_ELENCO_REPORT);
    mapFilters.remove(ID_ELENCO_GRADUATORIE);

    return "index";
  }
}
