package it.csi.nembo.nemboconf.presentation.pianofinanziarioNemboconf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.csi.nembo.nemboconf.business.IPianoFinanziarioNemboEJB;
import it.csi.nembo.nemboconf.dto.FileAllegatoParametro;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.IterPianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.SottoMisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalException;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "/pianofinanziario")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE)
public class StoricoPianoFinanziarioController extends BaseController
{
  @Autowired
  private IPianoFinanziarioNemboEJB pianoFinanziarioNemboconf;

  @RequestMapping(value = "confermaInserimento")
  public String confermaInserimento(Model model)
      throws InternalUnexpectedException
  {
    setModelDialogWarning(
        model,
        "Attenzione, proseguendo con l'operazione verrà creato un nuovo piano finanziario in bozza, si vuole continuare?",
        "inserisci.do");
    return "dialog/conferma";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_PIANO_FINANZIARIO_REGIONALE)
  @RequestMapping(value = "storico")
  public String storico(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    boolean modificaUtente = isUtenteAbilitatoModificaMacroCdU(session,
        Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE);
    model.addAttribute("modificaUtente", modificaUtente);

    List<PianoFinanziarioDTO> list = pianoFinanziarioNemboconf
        .getElencoPianoFinanziari();

    if (list != null && !list.isEmpty())
    {
      model.addAttribute("elenco", list);
      ArrayList<Long> lIdUtente = new ArrayList<Long>();
      for (PianoFinanziarioDTO pianoFinanziario : list)
      {
        for (IterPianoFinanziarioDTO iter : pianoFinanziario.getIter())
        {
          lIdUtente.add(pianoFinanziario.getExtIdUtenteAggiornamento());
          Long idUtenteAggiornamento = iter.getExtIdUtenteAggiornamento();
          if (!lIdUtente.contains(idUtenteAggiornamento))
          {
            lIdUtente.add(idUtenteAggiornamento);
          }
        }
      }

      List<UtenteLogin> utenti = loadRuoloDescr(lIdUtente);
      for (PianoFinanziarioDTO pianoFinanziario : list)
      {
        for (IterPianoFinanziarioDTO iter : pianoFinanziario.getIter())
        {
          iter.setDescUtente(
              getUtenteDescrizione(iter.getExtIdUtenteAggiornamento(), utenti));
        }
      }
    }
    return "pianofinanziario/storico";
  }

  @RequestMapping(value = "inserisci", method = RequestMethod.POST)
  public String inserisci(Model model, HttpServletRequest request,
      final RedirectAttributes redirectAttributes)
      throws InternalUnexpectedException
  {
    try
    {
      pianoFinanziarioNemboconf
          .inserisciPianoFinanziario(((UtenteAbilitazioni) request.getSession()
              .getAttribute("utenteAbilitazioni")).getIdUtenteLogin());
    }
    catch (ApplicationException e)
    {
      addErrorOnRedirectAttribute(redirectAttributes, e.getMessage());
    }
    return "redirect:storico.do";
  }

  public void addErrorOnRedirectAttribute(
      final RedirectAttributes redirectAttributes, String errorMessage)
  {
    Errors errors = new Errors();
    errors.addError("error", errorMessage);
    redirectAttributes.addFlashAttribute("errors", errors);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE, idLivello = NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO)
  @RequestMapping(value = "confermaConsolidamento_{id}")
  public String confermaConsolidamento(Model model,
      @PathVariable("id") @NumberFormat(style = NumberFormat.Style.NUMBER) long id)
      throws InternalException
  {
    setModelDialogWarning(
        model,
        "Attenzione: confermando verrà creata e salvata a sistema una storicizzazione dello stato corrente del piano finanziario.",
        "consolida_" + id + ".do");
    return "pianofinanziario/popupConfermaStoricizza";
  }

  @RequestMapping(value = "confermaEliminazione_{id}")
  public String confermaEliminazione(Model model,
      @PathVariable("id") @NumberFormat(style = NumberFormat.Style.NUMBER) long id)
      throws InternalException
  {
    setModelDialogWarning(
        model,
        "Attenzione, proseguendo con l'operazione verrà eliminato questo piano finanziario, si vuole continuare?",
        "elimina_pf_" + id + ".do");
    return "dialog/conferma";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE, idLivello = NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO)
  @RequestMapping(value = "consolida_{id}", method = RequestMethod.POST)
  @ResponseBody
  public String consolida(Model model, HttpServletRequest request,
      @PathVariable("id") @NumberFormat(style = NumberFormat.Style.NUMBER) long id,
      @RequestParam(value = "nomepiano") String nomePianoStoricizzato,
      final RedirectAttributes redirectAttributes)
      throws InternalUnexpectedException, IOException
  {
    try
    {
      Errors errors = new Errors();
      model.addAttribute("preferRequest", Boolean.TRUE);
      errors.validateMandatoryFieldLength(nomePianoStoricizzato, 5, 4000,
          "nomepiano");

      if (errors.isEmpty())
      {

        FileAllegatoParametro fileAllegatoParametro = pianoFinanziarioNemboconf
            .getFileAllegatoParametro(
                NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO);
        String msgError = null;
        if (fileAllegatoParametro == null)
        {
          msgError = "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - il parametro "
              + NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO
              + " non è valorizzato";
        }
        else
        {
          if (fileAllegatoParametro.getFileAllegato() == null)
          {
            msgError = "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - parametro "
                + NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO
                + " presente ma senza template";
          }
        }
        if (msgError != null)
        {
          return msgError;
        }

        byte[] fileExcel = getExcelPianoFinanziario(fileAllegatoParametro, id);
        pianoFinanziarioNemboconf.consolidaPianoFinanziario(id,
            nomePianoStoricizzato, fileExcel,
            getIdUtenteLogin(request.getSession()));
        return "SUCCESS";
      }
      else
      {
        String msgError = errors.get("nomepiano");
        return msgError;
      }
    }
    catch (ApplicationException e)
    {
      addErrorOnRedirectAttribute(redirectAttributes, e.getMessage());
    }
    return "redirect:storico.do";
  }

  @RequestMapping(value = "elimina_pf_{id}", method = RequestMethod.POST)
  public String elimina(Model model, HttpServletRequest request,
      @PathVariable("id") @NumberFormat(style = NumberFormat.Style.NUMBER) long id,
      final RedirectAttributes redirectAttributes)
      throws IOException, InternalUnexpectedException
  {
    try
    {
      pianoFinanziarioNemboconf.eliminaPianoFinanziario(id);
    }
    catch (ApplicationException e)
    {
      addErrorOnRedirectAttribute(redirectAttributes, e.getMessage());
    }
    return "redirect:storico.do";
  }

  @RequestMapping(value = "download_{id}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> downloadExcel(
      @PathVariable(value = "id") long idPianoFinanziario, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    String dataInizio;
    try
    {
      dataInizio = NemboUtils.DATE.formatDate(
          pianoFinanziarioNemboconf.getPianoFinanziario(idPianoFinanziario)
              .getIter().get(0).getDataInizio());
    }
    catch (Exception e)
    {
      dataInizio = "corrente";
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/x-download");
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"PianoFinanziario_" + dataInizio + ".xls\"");

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        pianoFinanziarioNemboconf.getExcelPianoFinanziario(idPianoFinanziario),
        httpHeaders, HttpStatus.OK);
    return response;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_PIANO_FINANZIARIO_REGIONALE) // ,
                                                                                                                                   // idLivello
                                                                                                                                   // =
                                                                                                                                   // 60000)
  @RequestMapping(value = "excel_{id}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> excel(Model model, HttpServletRequest request,
      @PathVariable("id") long idPianoFinanziario,
      final RedirectAttributes redirectAttributes)
      throws IOException, InternalUnexpectedException
  {
    ResponseEntity<byte[]> result = null;

    FileAllegatoParametro fileAllegatoParametro = pianoFinanziarioNemboconf
        .getFileAllegatoParametro(
            NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO);
    String msgError = null;
    if (fileAllegatoParametro == null)
    {
      msgError = "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - il parametro "
          + NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO
          + " non è valorizzato";
    }
    else
    {
      if (fileAllegatoParametro.getFileAllegato() == null)
      {
        msgError = "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - parametro "
            + NemboConstants.PARAMETRO.FILE.TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO
            + " presente ma senza template";
      }
    }
    if (msgError != null)
    {
      addErrorOnRedirectAttribute(redirectAttributes, msgError);
      result = new ResponseEntity<byte[]>(HttpStatus.TEMPORARY_REDIRECT);
      result.getHeaders().add("location", "redirect:storico.do");
      return result;
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/x-download");
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + fileAllegatoParametro.getValore() + "\"");

    result = new ResponseEntity<byte[]>(
        getExcelPianoFinanziario(fileAllegatoParametro, idPianoFinanziario),
        httpHeaders, HttpStatus.OK);
    return result;
  }

  private byte[] getExcelPianoFinanziario(
      FileAllegatoParametro fileAllegatoParametro, long idPianoFinanziario)
      throws IOException, InternalUnexpectedException
  {
    ByteArrayInputStream template = new ByteArrayInputStream(
        fileAllegatoParametro.getFileAllegato());
    HSSFWorkbook workbook = new HSSFWorkbook(template);
    HSSFSheet sheet = workbook.getSheetAt(0);

    List<MisuraDTO> misure = pianoFinanziarioNemboconf
        .getElencoLivelli(idPianoFinanziario);
    int rowNum = 0;
    HSSFCellStyle styleStringRight = workbook.createCellStyle();
    styleStringRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    setCellBorder(styleStringRight, HSSFCellStyle.BORDER_THIN);
    HSSFCellStyle styleStringLeft = workbook.createCellStyle();
    styleStringLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    styleStringLeft.setWrapText(true);
    styleStringLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    setCellBorder(styleStringLeft, HSSFCellStyle.BORDER_THIN);
    HSSFCellStyle styleCurrency = workbook.createCellStyle();
    styleCurrency.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleCurrency.setDataFormat((short) 8);
    styleCurrency
        .setDataFormat(workbook.createDataFormat().getFormat("€* #,##0.00"));
    styleCurrency.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

    setCellBorder(styleCurrency, HSSFCellStyle.BORDER_THIN);
    HSSFCellStyle styleCurrencyCurrent = workbook.createCellStyle();
    styleCurrencyCurrent.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleCurrencyCurrent.setDataFormat((short) 8);
    styleCurrencyCurrent
        .setDataFormat(workbook.createDataFormat().getFormat("€* #,##0.00"));
    setCellBorder(styleCurrencyCurrent, HSSFCellStyle.BORDER_THIN);
    styleCurrencyCurrent
        .setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    styleCurrencyCurrent.setFillPattern(CellStyle.SOLID_FOREGROUND);
    HSSFCellStyle styleLegendLeft = workbook.createCellStyle();
    styleLegendLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    styleLegendLeft.setWrapText(true);
    setCellBorder(styleLegendLeft, HSSFCellStyle.BORDER_NONE);

    for (MisuraDTO misura : misure)
    {
      for (SottoMisuraDTO sottoMisuraDTO : misura.getElenco())
      {
        for (TipoOperazioneDTO tipoOperazione : sottoMisuraDTO.getElenco())
        {
          HSSFRow row = sheet.createRow(++rowNum);
          textCell(row, 0, tipoOperazione.getNumeroMisura())
              .setCellStyle(styleStringRight);
          textCell(row, 1, tipoOperazione.getNumeroSottoMisura())
              .setCellStyle(styleStringRight);
          textCell(row, 2, tipoOperazione.getNumeroTipoOperazione())
              .setCellStyle(styleStringRight);
          textCell(row, 3, tipoOperazione.getCodiceSettore())
              .setCellStyle(styleStringRight);
          ImportoFocusAreaDTO focusArea = tipoOperazione.getListFocusArea()
              .get(0);
          textCell(row, 4, focusArea.getCodice())
              .setCellStyle(styleStringRight);
          BigDecimal importo = focusArea.getImporto();
          numericCell(row, 5, importo).setCellStyle(styleCurrency);
          BigDecimal importoTrascinato = NemboUtils.NUMBERS
              .nvl(focusArea.getImportoTrascinato());
          numericCell(row, 6, importoTrascinato).setCellStyle(styleCurrency);
          BigDecimal totaleRisorseAttivate = tipoOperazione
              .getTotaleRisorseAttivate();
          numericCell(row, 7, totaleRisorseAttivate)
              .setCellStyle(styleCurrency);
          BigDecimal totaleEconomie = tipoOperazione.getTotaleEconomie();
          BigDecimal disponibile = importo
              .subtract(importoTrascinato, MathContext.DECIMAL128)
              .subtract(totaleRisorseAttivate, MathContext.DECIMAL128)
              .add(totaleEconomie, MathContext.DECIMAL128);
          numericCell(row, 8, disponibile).setCellStyle(styleCurrency);
          numericCell(row, 9, totaleEconomie).setCellStyle(styleCurrency);
          BigDecimal totalePagato = tipoOperazione.getTotalePagato();
          numericCell(row, 10, totalePagato).setCellStyle(styleCurrency);

        }
      }
    }
    workbook.setForceFormulaRecalculation(true);

    // Scrivo foglio storico particelle
    sheet = workbook.getSheetAt(2);
    rowNum = 4;
    for (MisuraDTO misura : misure)
    {
      for (SottoMisuraDTO sottoMisuraDTO : misura.getElenco())
      {
        for (TipoOperazioneDTO tipoOperazione : sottoMisuraDTO.getElenco())
        {
          List<ImportoFocusAreaDTO> storicoLivello = pianoFinanziarioNemboconf
              .getStoricoImportiPianoFinanziario(idPianoFinanziario,
                  tipoOperazione.getIdLivello());

          if (storicoLivello != null && !storicoLivello.isEmpty())
          {
            HSSFRow row = sheet.createRow(++rowNum);
            textCell(row, 0, tipoOperazione.getNumeroMisura())
                .setCellStyle(styleStringRight);
            textCell(row, 1, tipoOperazione.getNumeroSottoMisura())
                .setCellStyle(styleStringRight);
            textCell(row, 2, tipoOperazione.getNumeroTipoOperazione())
                .setCellStyle(styleStringRight);
            textCell(row, 3, tipoOperazione.getCodiceSettore())
                .setCellStyle(styleStringRight);
            ImportoFocusAreaDTO focusArea = tipoOperazione.getListFocusArea()
                .get(0);
            textCell(row, 4, focusArea.getCodice())
                .setCellStyle(styleStringRight);
            BigDecimal importo = focusArea.getImporto();
            numericCell(row, 5, importo).setCellStyle(styleCurrencyCurrent);
            BigDecimal importoTrascinato = NemboUtils.NUMBERS
                .nvl(focusArea.getImportoTrascinato());
            numericCell(row, 6, importoTrascinato)
                .setCellStyle(styleCurrencyCurrent);
            textCell(row, 7, " ").setCellStyle(styleStringLeft);
            textCell(row, 8, " ").setCellStyle(styleStringLeft);

            Map<Long, String> mapUtenti = new HashMap<>();
            for (ImportoFocusAreaDTO i : storicoLivello)
            {
              row = sheet.createRow(++rowNum);

              if (i.getMotivazioni() != null
                  && i.getMotivazioni().length() >= 50)
                row.setHeightInPoints(sheet.getDefaultRowHeightInPoints()
                    * ((i.getMotivazioni().length() / 50) + 1));

              textCell(row, 0, "").setCellStyle(styleStringRight);
              textCell(row, 1, "").setCellStyle(styleStringRight);
              textCell(row, 2, "").setCellStyle(styleStringRight);
              textCell(row, 3, "").setCellStyle(styleStringRight);
              textCell(row, 4, "").setCellStyle(styleStringRight);
              importo = i.getImporto();
              numericCell(row, 5, importo).setCellStyle(styleCurrency);
              importoTrascinato = NemboUtils.NUMBERS
                  .nvl(i.getImportoTrascinato());
              numericCell(row, 6, importoTrascinato)
                  .setCellStyle(styleCurrency);

              // mappa decodifica utenti
              if (!mapUtenti.containsKey(i.getExtIdUtenteAggiornamento()))
              {
                List<Long> idUtenti = new ArrayList<Long>();
                idUtenti.add(i.getExtIdUtenteAggiornamento());
                List<UtenteLogin> utentiList = loadRuoloDescr(idUtenti);
                String descr = super.getUtenteDescrizione(
                    i.getExtIdUtenteAggiornamento(), utentiList);
                i.setInfoModifica(i.getDataInserimentoStr() + " - " + descr);
                mapUtenti.put(i.getExtIdUtenteAggiornamento(), descr);
              }
              else
              {
                i.setInfoModifica(i.getDataInserimentoStr() + " - "
                    + mapUtenti.get(i.getExtIdUtenteAggiornamento()));
              }

              textCell(row, 7, i.getInfoModifica())
                  .setCellStyle(styleStringLeft);
              textCell(row, 8, i.getMotivazioni())
                  .setCellStyle(styleStringLeft);
            }
          }

        }
      }
    }
    workbook.setForceFormulaRecalculation(true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    workbook.write(baos);
    return baos.toByteArray();
  }

  protected void setCellBorder(HSSFCellStyle cellStyle, short borderStyle)
  {
    cellStyle.setBorderBottom(borderStyle);
    cellStyle.setBorderLeft(borderStyle);
    cellStyle.setBorderRight(borderStyle);
    cellStyle.setBorderTop(borderStyle);
  }

  protected HSSFCell numericCell(HSSFRow row, int rowNum, BigDecimal value)
  {
    HSSFCell cell = row.createCell(rowNum);
    if (value != null)
    {
      cell.setCellValue(value.doubleValue());
    }
    return cell;
  }

  protected HSSFCell textCell(HSSFRow row, int rowNum, String value)
  {
    HSSFCell cell = row.createCell(rowNum);
    if (value != null)
    {
      cell.setCellValue(value);
    }
    return cell;
  }

  protected boolean isModificabile(long idPianoFinanziario, HttpSession session)
      throws InternalUnexpectedException
  {
    boolean modificaUtente = NemboUtils.PAPUASERV
        .isUtenteReadWrite(getUtenteAbilitazioni(session));
    PianoFinanziarioDTO pianoCorrente = pianoFinanziarioNemboconf
        .getPianoFinanziarioCorrente(
            NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
    modificaUtente = pianoCorrente != null
        && pianoCorrente.getIdPianoFinanziario() == idPianoFinanziario
        && pianoCorrente.isModificabile();
    return modificaUtente;
  }

}
/* cose per file LIBREOFFICE .ods */
/*
 * @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU
 * = Security.VISUALIZZA_PIANO_FINANZIARIO_REGIONALE) // , idLivello = 60000)
 * 
 * @RequestMapping(value = "ods_{id}", method = RequestMethod.GET) public
 * ResponseEntity<byte[]> ods(Model model, HttpServletRequest
 * request, @PathVariable("id") long idPianoFinanziario, final
 * RedirectAttributes redirectAttributes) throws IOException,
 * InternalUnexpectedException { ResponseEntity<byte[]> result = null;
 * 
 * FileAllegatoParametro fileAllegatoParametro = pianoFinanziarioNemboconf
 * .getFileAllegatoParametro(NemboConstants.PARAMETRO.FILE.
 * TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO_ODS); String msgError = null;
 * if (fileAllegatoParametro == null) { msgError =
 * "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - il parametro "
 * + NemboConstants.PARAMETRO.FILE.
 * TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO_ODS + " non è valorizzato"; }
 * else { if (fileAllegatoParametro.getFileAllegato() == null) { msgError =
 * "Si è verificato un errore grave, contattare l'assistenza tecnica comunicando il seguente messaggio: Template excel non trovato - parametro "
 * + NemboConstants.PARAMETRO.FILE.
 * TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO_ODS +
 * " presente ma senza template"; } } if (msgError != null) {
 * addErrorOnRedirectAttribute(redirectAttributes, msgError); result = new
 * ResponseEntity<byte[]>(HttpStatus.TEMPORARY_REDIRECT);
 * result.getHeaders().add("location", "redirect:storico.do"); return result; }
 * HttpHeaders httpHeaders = new HttpHeaders(); httpHeaders.add("Content-type",
 * "application/x-download"); httpHeaders.add("Content-Disposition",
 * "attachment; filename=\"" + fileAllegatoParametro.getValore() + "\"");
 * 
 * result = new
 * ResponseEntity<byte[]>(getOdsPianoFinanziario(fileAllegatoParametro,
 * idPianoFinanziario), httpHeaders, HttpStatus.OK); return result; } private
 * byte[] getOdsPianoFinanziario(FileAllegatoParametro fileAllegatoParametro,
 * long idPianoFinanziario) throws IOException, InternalUnexpectedException {
 * 
 * File tempFile = File.createTempFile("PFTmp", ".ods", null); FileOutputStream
 * fos = new FileOutputStream(tempFile);
 * fos.write(fileAllegatoParametro.getFileAllegato()); fos.flush(); fos.close();
 * File f = new File(tempFile.getPath());
 * 
 * SpreadSheet spreadsheet = SpreadSheet.createFromFile(f); Sheet sheet =
 * spreadsheet.getSheet(0);
 * 
 * List<MisuraDTO> misure =
 * pianoFinanziarioNemboconf.getElencoLivelli(idPianoFinanziario); int rowNum =
 * 1; for (MisuraDTO misura : misure) { for (SottoMisuraDTO sottoMisuraDTO :
 * misura.getElenco()) { for (TipoOperazioneDTO tipoOperazione :
 * sottoMisuraDTO.getElenco()) { rowNum++; ImportoFocusAreaDTO focusArea =
 * tipoOperazione.getListFocusArea().get(0); BigDecimal importo =
 * focusArea.getImporto(); BigDecimal importoTrascinato =
 * NemboconfUtils.NUMBERS.nvl(focusArea.getImportoTrascinato()); BigDecimal
 * totaleRisorseAttivate = tipoOperazione.getTotaleRisorseAttivate(); BigDecimal
 * totaleEconomie = tipoOperazione.getTotaleEconomie(); BigDecimal disponibile =
 * importo.subtract(importoTrascinato,
 * MathContext.DECIMAL128).subtract(totaleRisorseAttivate,
 * MathContext.DECIMAL128) .add(totaleEconomie, MathContext.DECIMAL128);
 * BigDecimal totalePagato = tipoOperazione.getTotalePagato();
 * 
 * if(tipoOperazione.getNumeroMisura()!=null)
 * sheet.getCellAt("A"+rowNum).setValue(tipoOperazione.getNumeroMisura());
 * if(tipoOperazione.getNumeroSottoMisura()!=null)
 * sheet.getCellAt("B"+rowNum).setValue(tipoOperazione.getNumeroSottoMisura());
 * if(tipoOperazione.getNumeroTipoOperazione()!=null)
 * sheet.getCellAt("C"+rowNum).setValue(tipoOperazione.getNumeroTipoOperazione()
 * ); if(tipoOperazione.getCodiceSettore()!=null)
 * sheet.getCellAt("D"+rowNum).setValue(tipoOperazione.getCodiceSettore());
 * sheet.getCellAt("E"+rowNum).setValue(focusArea.getCodice());
 * sheet.getCellAt("F"+rowNum).setValue(importo);
 * sheet.getCellAt("G"+rowNum).setValue(importoTrascinato);
 * sheet.getCellAt("H"+rowNum).setValue(totaleRisorseAttivate);
 * sheet.getCellAt("I"+rowNum).setValue(disponibile);
 * sheet.getCellAt("J"+rowNum).setValue(totaleEconomie);
 * sheet.getCellAt("K"+rowNum).setValue(totalePagato); } } }
 * 
 * File outputFile = new File("EstrazionePianoFinanziario.ods");
 * sheet.getSpreadSheet().saveAs(outputFile);
 * 
 * return Files.readAllBytes(outputFile.toPath());} }
 */
