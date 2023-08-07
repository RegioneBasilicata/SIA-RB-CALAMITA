package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.nembo.nemboconf.dto.catalogomisura.InfoMisurazioneIntervento;
import it.csi.nembo.nemboconf.dto.cruscottobandi.CruscottoInterventiDTO;

public class ExcelBuilderElencoInterventiCruscottoBandi
    extends AbstractExcelView
{

  @SuppressWarnings("unchecked")
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    List<CruscottoInterventiDTO> elenco = (List<CruscottoInterventiDTO>) model
        .get("elenco");

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Interventi");
    sheet.setDefaultColumnWidth(30);

    // create style for header cells
    HSSFCellStyle style = ((HSSFWorkbook) workbook).createCellStyle();
    HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
    // palette.setColorAtIndex((short)57, (byte)224, (byte)233, (byte)242); //
    // color #E0E9F2

    palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color
                                                                            // #428BCA

    palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color
                                                                             // #F9F9F9

    style.setFillForegroundColor(palette.getColor(57).getIndex());
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    // CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
    // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    style.setAlignment(CellStyle.ALIGN_CENTER);
    style.setBorderBottom(CellStyle.BORDER_MEDIUM);
    style.setBorderTop(CellStyle.BORDER_MEDIUM);
    style.setBorderLeft(CellStyle.BORDER_MEDIUM);
    style.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    style.setFont(font);

    // CELL STYLE PER I VALORI
    HSSFCellStyle styleVal = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
    // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleVal.setBorderBottom(CellStyle.BORDER_THIN);
    styleVal.setBorderTop(CellStyle.BORDER_THIN);
    styleVal.setBorderLeft(CellStyle.BORDER_THIN);
    styleVal.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleVal.setFont(font);

    // CELL STYLE GENERICO, DI DEFAULT
    HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleValDef.setBorderBottom(CellStyle.BORDER_THIN);
    styleValDef.setBorderTop(CellStyle.BORDER_THIN);
    styleValDef.setBorderLeft(CellStyle.BORDER_THIN);
    styleValDef.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleValDef.setFont(font);

    // create header row
    int col = -1;
    HSSFRow header = sheet.createRow(0);

    col++;
    header.createCell(col).setCellValue("Tipo intervento");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Descrizione intervento");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Localizzazione dell'intervento");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Operazione");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Costo Unit Min");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Costo Unit Max");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    col++;
    header.createCell(col).setCellValue("Dato / UM");
    header.getCell(col).setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col));

    // create data rows
    int rowCount = 0;
    int umHeight = 0;
    String datoUm = "";
    HSSFRow aRow;
    HashMap<Integer, Integer> mapRowToAutoHeight = new HashMap<Integer, Integer>();

    HSSFCellStyle styleWrap = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleWrap.setBorderBottom(CellStyle.BORDER_THIN);
    styleWrap.setBorderTop(CellStyle.BORDER_THIN);
    styleWrap.setBorderLeft(CellStyle.BORDER_THIN);
    styleWrap.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleWrap.setFont(font);
    styleWrap.setWrapText(true);
    HSSFCell infoCell;

    for (CruscottoInterventiDTO item : elenco)
    {
      datoUm = "";
      col = 0;
      rowCount++;
      umHeight = 1;

      aRow = sheet.createRow(rowCount);
      aRow.createCell(col).setCellValue(item.getTipoIntervento());
      aRow.getCell(col).setCellStyle(styleValDef);

      col++;
      aRow.createCell(col).setCellValue(item.getDescrIntervento());
      aRow.getCell(col).setCellStyle(styleValDef);

      col++;
      aRow.createCell(col).setCellValue(item.getLocalizzazione());
      aRow.getCell(col).setCellStyle(styleValDef);

      col++;
      aRow.createCell(col).setCellValue(item.getOperazione());
      aRow.getCell(col).setCellStyle(styleValDef);

      col++;
      aRow.createCell(col).setCellValue(item.getCostoUnitMinimoStr());
      aRow.getCell(col).setCellStyle(styleValDef);

      col++;
      aRow.createCell(col).setCellValue(item.getCostoUnitMassimoStr());
      aRow.getCell(col).setCellStyle(styleValDef);

      for (int i = 0; i < item.getInfoMisurazioni().size(); i++)
      {
        InfoMisurazioneIntervento info = item.getInfoMisurazioni().get(i);
        if (i > 0)
        {
          datoUm = datoUm + " \n";
          umHeight++;
        }
        datoUm = datoUm + info.getDescMisurazione();
        if (!GenericValidator.isBlankOrNull(info.getCodiceUnitaMisura())
            && !"NO_MISURA".equals(info.getCodiceUnitaMisura()))
        {
          datoUm = datoUm + " " + info.getCodiceUnitaMisura();
        }
      }

      col++;
      infoCell = aRow.createCell(col);
      infoCell.setCellValue(datoUm);
      infoCell.setCellStyle(styleWrap);
      if (umHeight > 1)
      {
        mapRowToAutoHeight.put(rowCount, umHeight);
      }
    }

    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
    sheet.autoSizeColumn(2);
    sheet.autoSizeColumn(3);

    for (Entry<Integer, Integer> entry : mapRowToAutoHeight.entrySet())
    {
      Integer key = entry.getKey();
      Integer value = entry.getValue();
      sheet.getRow(key)
          .setHeightInPoints(sheet.getDefaultRowHeightInPoints() * value);
    }
  }

}
