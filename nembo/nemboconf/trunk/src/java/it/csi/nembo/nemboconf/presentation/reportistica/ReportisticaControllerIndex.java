package it.csi.nembo.nemboconf.presentation.reportistica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.web.servlet.ModelAndView;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.ElencoQueryBandoDTO;
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.dto.reportistica.ParametriQueryReportVO;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.presentation.cruscottobandi.ExcelTemplateReport;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.FileUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI) // ,
// idLivello=60000)
@RequestMapping(value = "/reportisticaIndex")
public class ReportisticaControllerIndex extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoBandiEJB;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "index", method = RequestMethod.GET)
  public String index(HttpSession session, Model model)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<ElencoQueryBandoDTO> report = cruscottoBandiEJB
        .getElencoReport(utenteAbilitazioni.getAttori()[0].getCodice());
    model.addAttribute("report", report);

    return "reportistica/index/elencoReport";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "indexGrafici", method = RequestMethod.GET)
  public String indexGrafici(HttpSession session, Model model)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<ElencoQueryBandoDTO> grafici = cruscottoBandiEJB
        .getElencoGrafici(utenteAbilitazioni.getAttori()[0].getCodice());
    model.addAttribute("grafici", grafici);

    return "reportistica/index/elencoGrafici";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizza_report_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglioReport(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, Model model) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params.setCodEnteCaa(
        NemboUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    GraficoVO graficoVO = cruscottoBandiEJB.getGrafico(idElencoQuery, params);

    model.addAttribute("tabella", graficoVO);
    session.setAttribute("reportSelzionatoSession", graficoVO);

    return "reportistica/index/dettaglioReport";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizza_grafico_{idElencoQuery}", method = RequestMethod.GET)
  public Object dettaglioGrafico(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, Model model) throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params.setCodEnteCaa(
        NemboUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    GraficoVO graficoVO = cruscottoBandiEJB.getGrafico(idElencoQuery, params);

    return graficoVO;
  }

  @RequestMapping(value = "getDettaglioGrafico_{idElencoQuery}", produces = "application/json")
  @ResponseBody
  public Object getData(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params.setCodEnteCaa(
        NemboUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    GraficoVO graficoVO = cruscottoBandiEJB.getGrafico(idElencoQuery, params);

    return graficoVO;
  }

  @RequestMapping(value = "stampa_{idElencoQuery}", method = RequestMethod.GET)
  public String stampa(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/index/stampaGrafico";
  }

  @RequestMapping(value = "dettaglio_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglio(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/index/dettaglioGrafico";
  }

  @RequestMapping(value = "downloadExcelReport")
  public ModelAndView downloadExcel(HttpSession session, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    GraficoVO graficoVO = (GraficoVO) session
        .getAttribute("reportSelzionatoSession");
    if (graficoVO.isExcelTemplate())
    {
      return new ModelAndView("redirect:downloadExcelReportTemplate.xls");
    }
    else
    {
      return new ModelAndView("excelReportView", "graficoVO", graficoVO);
    }
  }

  @RequestMapping(value = "downloadExcelReportTemplate")
  public ResponseEntity<byte[]> downloadExcelTemplate(
      HttpServletRequest request, HttpSession session, Model model)
      throws Exception
  {
    GraficoVO graficoVO = (GraficoVO) session
        .getAttribute("reportSelzionatoSession");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        FileUtils.MAP_EXTENSIONS_TO_MIME.get(".xlsx"));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"downloadExcelReportTemplate.xlsx\"");
    byte[] bytes = cruscottoBandiEJB
        .getExcelParametroDiElencoQuery(graficoVO.getIdElencoQuery());
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    Workbook workbook = WorkbookFactory.create(is);
    is.close();
    bytes = null;
    ExcelTemplateReport.buildExcelDocument(workbook, graficoVO);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    workbook.write(baos);
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(
        baos.toByteArray(),
        httpHeaders, HttpStatus.OK);
    return responseEntity;
  }

}