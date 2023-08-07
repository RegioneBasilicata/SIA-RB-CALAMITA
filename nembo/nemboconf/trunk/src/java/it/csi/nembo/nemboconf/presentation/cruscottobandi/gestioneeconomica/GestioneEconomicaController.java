package it.csi.nembo.nemboconf.presentation.cruscottobandi.gestioneeconomica;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GraduatoriaDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class GestioneEconomicaController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "gesteconomica_{idBando}")
  public String index(@PathVariable(value = "idBando") long idBando,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = cruscottoEJB.getInformazioniBando(idBando);
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    List<DecodificaDTO<String>> elencoQueryBando = cruscottoEJB
        .elencoQueryBando(idBando, Boolean.FALSE,
            NemboUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    boolean grafici = cruscottoEJB.graficiTabellariPresenti(idBando,
        NemboUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    if (elencoQueryBando != null && elencoQueryBando.size() > 0)
    {
      bando.setHaveChart(true);
      session.setAttribute("elencoQueryBando_" + idBando, elencoQueryBando);
    }
    if (grafici)
    {
      bando.setHaveReport(true);
    }
    List<GraduatoriaDTO> graduatorie = cruscottoEJB
        .getGraduatorieBando(idBando);
    if (graduatorie != null && graduatorie.size() > 0)
    {
      bando.setHaveGraduatorie(true);
    }

    session.setAttribute("bandoGestEconomica", bando);

    if (elencoQueryBando != null && elencoQueryBando.size() > 0)
    {
      return "redirect:elencoreport_" + bando.getIdBando() + ".do";
    }
    else
      if (grafici)
      {
        return "redirect:mainreport_" + bando.getIdBando() + ".do";
      }
      else
        if (graduatorie != null && graduatorie.size() > 0)
        {
          return "redirect:elencograduatorie_." + bando.getIdBando() + "do";
        }

    // In questo caso faccio redirect sulla pagina di cruscotto bandi che
    // gestisce i fondi
    model.addAttribute("messaggio",
        "Per il bando corrente non sono stati attivati report");
    return "dialog/soloWarning";
  }
}
