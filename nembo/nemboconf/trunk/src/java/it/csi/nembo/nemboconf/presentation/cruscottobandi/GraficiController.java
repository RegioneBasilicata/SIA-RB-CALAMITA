package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class GraficiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "grafici", method = RequestMethod.GET)
  public String oggettiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    model.addAttribute("elencografici",
        cruscottoEJB.elencoQueryBando(idBando, false));
    model.addAttribute("elencoreport",
        cruscottoEJB.elencoQueryBando(idBando, true));
    return "cruscottobandi/grafici";
  }

  @RequestMapping(value = "grafici", method = RequestMethod.POST)
  public String post(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    model.addAttribute("preferReqValues", Boolean.TRUE);
    String[] idsGrafici = request.getParameterValues("chkattivo");

    String msgLog = logOperazioniEffettuate(idBando, idsGrafici);
    cruscottoEJB.aggiornaGraficiBando(idBando, convertArray(idsGrafici),
        getUtenteAbilitazioni(session).getIdUtenteLogin(), msgLog);

    model.addAttribute("msgSuccess", "Aggiornamento eseguito correttamente");
    return oggettiGet(model, request, session);
  }

  private String logOperazioniEffettuate(long idBando, String[] idsGrafici)
      throws InternalUnexpectedException
  {

    // LOG
    ArrayList<Long> idsGraficiLong = new ArrayList<>();
    if (idsGrafici != null)
      for (String id : idsGrafici)
      {
        idsGraficiLong.add(Long.parseLong(id));
      }

    List<GraficoVO> grafici = cruscottoEJB.elencoQueryBando(idBando, false);
    List<GraficoVO> report = cruscottoEJB.elencoQueryBando(idBando, true);
    String msgLogGrafici = "";
    if (grafici != null)
      for (GraficoVO g : grafici)
      {
        if (g.getFlagVisibile().compareTo("S") == 0)
          if (!idsGraficiLong.contains(g.getIdElencoQuery()))
          {
            if (msgLogGrafici == "")
              msgLogGrafici = "Grafici: \n";
            msgLogGrafici += "Disattivato grafico \"" + g.getDescrBreve()
                + "\"\n";
          }
        if (g.getFlagVisibile().compareTo("N") == 0)
          if (idsGraficiLong.contains(g.getIdElencoQuery()))
          {
            if (msgLogGrafici == "")
              msgLogGrafici = "Grafici: \n";
            msgLogGrafici += "Attivato grafico \"" + g.getDescrBreve() + "\"\n";
          }
      }

    String msgLogReport = "";
    if (report != null)
      for (GraficoVO g : report)
      {
        if (g.getFlagVisibile().compareTo("S") == 0)
          if (!idsGraficiLong.contains(g.getIdElencoQuery()))
          {
            if (msgLogReport == "")
              msgLogReport = "Report: \n";
            msgLogReport += "Disattivato report \"" + g.getDescrBreve()
                + "\"\n";
          }
        if (g.getFlagVisibile().compareTo("N") == 0)
          if (idsGraficiLong.contains(g.getIdElencoQuery()))
          {
            if (msgLogReport == "")
              msgLogReport = "Report: \n";
            msgLogReport += "Attivato report \"" + g.getDescrBreve() + "\"\n";
          }
      }

    if (msgLogGrafici != null && msgLogGrafici != "")
      return msgLogGrafici + "\n" + msgLogReport;
    else
      return msgLogReport;

  }

  private Long[] convertArray(String[] input)
  {
    if (input == null || input.length <= 0)
    {
      return null;
    }
    Long[] list = new Long[input.length];
    for (int i = 0; i < input.length; i++)
    {
      list[i] = Long.parseLong(input[i]);
    }
    return list;
  }

}
