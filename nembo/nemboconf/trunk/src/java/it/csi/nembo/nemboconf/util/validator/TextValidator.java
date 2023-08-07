package it.csi.nembo.nemboconf.util.validator;

import org.apache.commons.validator.GenericValidator;

public class TextValidator extends BasicValidator
{
  /** serialVersionUID */
  private static final long  serialVersionUID               = 807054363415376393L;
  public static final String ERRORE_LUNGHEZZA_MINIMA_TESTO  = "Campo troppo corto, inserire almeno ${caratteri} caratteri";
  public static final String ERRORE_LUNGHEZZA_MASSIMA_TESTO = "Campo troppo lungo, inserire non più di ${caratteri} caratteri";

  public boolean validateFieldLength(String fieldValue, int min, int max,
      String fieldName)
  {
    return validateFieldLength(fieldValue, min, max, fieldName, false);
  }

  public boolean validateFieldLength(String fieldValue, int min, int max,
      String fieldName, boolean trim)
  {
    int len = 0;
    if (fieldValue != null)
    {
      if (trim)
      {
        fieldValue = fieldValue.trim();
      }
      len = fieldValue.length();
    }
    if (len < min)
    {
      addError(fieldName, ERRORE_LUNGHEZZA_MINIMA_TESTO.replace("${caratteri}",
          String.valueOf(min)));
      return false;
    }
    else
    {
      if (len > max)
      {
        addError(fieldName, ERRORE_LUNGHEZZA_MASSIMA_TESTO
            .replace("${caratteri}", String.valueOf(max)));
        return false;
      }
    }
    return true;
  }

  public boolean validateFieldMaxLength(String fieldValue, String fieldName,
      int max)
  {
    return validateFieldLength(fieldValue, 0, max, fieldName);
  }

  public boolean validateEmail(String fieldValue, String fieldName)
  {
    if (!GenericValidator.isEmail(fieldValue))
    {
      addError(fieldName, "Formato dell'email non valido");
      return false;
    }
    return true;
  }

  public boolean validateCAP(String fieldValue, String fieldName)
  {
    if (fieldValue.matches("\\d\\d\\d\\d\\d"))
    {
      return true;
    }
    else
    {
      addError(fieldName, "CAP non valido");
    }
    return false;
  }

}
