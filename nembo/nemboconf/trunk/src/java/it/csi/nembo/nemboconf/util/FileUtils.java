package it.csi.nembo.nemboconf.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

public abstract class FileUtils
{
  public static final Map<String, String> MAP_EXTENSIONS_TO_CSS  = new HashMap<String, String>();
  public static final Map<String, String> MAP_EXTENSIONS_TO_MIME = new HashMap<String, String>();
  static
  {
    MAP_EXTENSIONS_TO_CSS.put(".jpg", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".jpeg", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".png", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".bmp", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".tiff", "ico_image");
    MAP_EXTENSIONS_TO_CSS.put(".xls", "ico_excel");
    MAP_EXTENSIONS_TO_CSS.put(".xlsx", "ico_excel");
    MAP_EXTENSIONS_TO_CSS.put(".pdf", "ico_pdf");
    MAP_EXTENSIONS_TO_CSS.put(".doc", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".docx", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".rtf", "ico_word");
    MAP_EXTENSIONS_TO_CSS.put(".txt", "ico_text");

    MAP_EXTENSIONS_TO_MIME.put(".jpg", "image/jpeg");
    MAP_EXTENSIONS_TO_MIME.put(".jpeg", "image/jpeg");
    MAP_EXTENSIONS_TO_MIME.put(".png", "image/png");
    MAP_EXTENSIONS_TO_MIME.put(".bmp", "image/bmp");
    MAP_EXTENSIONS_TO_MIME.put(".tiff", "image/tiff");
    MAP_EXTENSIONS_TO_MIME.put(".xls", "application/vnd.ms-excel");
    MAP_EXTENSIONS_TO_MIME.put(".xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    MAP_EXTENSIONS_TO_MIME.put(".pdf", "application/pdf");
    MAP_EXTENSIONS_TO_MIME.put(".doc", "application/msword");
    MAP_EXTENSIONS_TO_MIME.put(".docx", "application/msword");
    MAP_EXTENSIONS_TO_MIME.put(".rtf", "application/rtf");
    MAP_EXTENSIONS_TO_MIME.put(".txt", "text/plain");
  }

  private String getFileExtension(String fileName)
  {
    try
    {
      int pos = fileName.lastIndexOf('.');
      if (pos < 0)
      {
        return "";
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  public String getDocumentCSSIconClass(String fileName)
  {
    String iconClass = null;
    if (!GenericValidator.isBlankOrNull(fileName))
    {
      iconClass = MAP_EXTENSIONS_TO_CSS.get(getFileExtension(fileName));
    }
    if (iconClass != null)
    {
      return iconClass;
    }
    else
    {
      return "ico_file";
    }
  }

  public String getMimeType(String fileName)
  {
    String iconClass = null;
    if (!GenericValidator.isBlankOrNull(fileName))
    {
      iconClass = MAP_EXTENSIONS_TO_MIME.get(getFileExtension(fileName));
    }
    if (iconClass != null)
    {
      return iconClass;
    }
    else
    {
      return "application/octet-stream";
    }
  }

}
