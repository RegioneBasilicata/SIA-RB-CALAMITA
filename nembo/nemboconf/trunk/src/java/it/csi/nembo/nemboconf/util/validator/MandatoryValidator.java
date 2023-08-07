package it.csi.nembo.nemboconf.util.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class MandatoryValidator extends OptionalValidator
{
  /** serialVersionUID */
  private static final long  serialVersionUID          = 807054363415376393L;
  public static final String ERRORE_CAMPO_NON_PERMESSO = "Campo non permesso";
  public static final String ERRORE_CAMPO_OBBLIGATORIO = "Campo obbligatorio";

  public boolean validateMandatory(String fieldValue, String fieldName,
      String errorMessage)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateNotAllowed(String fieldValue, String fieldName,
      String errorMessage)
  {
    if (GenericValidator.isBlankOrNull(fieldValue))
    {
      return true;
    }
    addError(fieldName, errorMessage);
    return false;
  }

  public boolean validateNotAllowed(String fieldValue, String fieldName)
  {
    return validateNotAllowed(fieldValue, fieldName, ERRORE_CAMPO_NON_PERMESSO);
  }

  public boolean validateMandatory(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public boolean validateMandatory(Object fieldValue, String fieldName,
      String errorMessage)
  {
    if (fieldValue == null)
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateMandatory(List<?> fieldValue, String fieldName,
      String errorMessage)
  {
    if (fieldValue == null || fieldValue.size() == 0)
    {
      addError(fieldName, errorMessage);
      return false;
    }
    return true;
  }

  public boolean validateMandatory(List<?> fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO);
  }

  public Date validateMandatoryDate(String fieldValue, String fieldName,
      boolean onlyDate)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateDate(fieldValue, fieldName, onlyDate);
    }
    return null;
  }

  public boolean validateMandatoryYear(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName, ERRORE_CAMPO_OBBLIGATORIO))
    {
      Long val = validateLongInRange(fieldValue, fieldName, new Long("1900"),
          new Long("2500"));
      if (val != null)
      {
        return true;
      }
    }
    return false;
  }

  public Date validateMandatoryDateTime(String fieldValue, String fieldName,
      boolean timeIsOptional)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateDateTime(fieldValue, fieldName, timeIsOptional);
    }
    return null;
  }

  public boolean validateMandatoryDateInRange(Date fieldValue, String fieldName,
      Date dateMin, Date dateMax, boolean equalsAccepted, boolean onlyDate)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateDateInRange(fieldValue, fieldName, dateMin, dateMax,
            equalsAccepted, onlyDate);
  }

  public Long validateMandatoryLong(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateLong(fieldValue, fieldName);
    }
    return null;
  }

  public Double validateMandatoryDouble(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateDouble(fieldValue, fieldName);
    }
    return null;
  }

  public Long validateMandatoryLongInRange(String fieldValue, String fieldName,
      Long min, Long max)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateLongInRange(fieldValue, fieldName, min, max);
    }
    return null;
  }

  public BigDecimal validateMandatoryBigDecimal(String fieldValue,
      String fieldName, int numDecimalDigit)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateBigDecimal(fieldValue, fieldName, numDecimalDigit);
    }
    return null;
  }

  public boolean validateMandatoryBigDecimalInRange(BigDecimal fieldValue,
      String fieldName, BigDecimal min, BigDecimal max)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateBigDecimalInRange(fieldValue, fieldName, min, max);
  }

  public BigDecimal validateMandatoryBigDecimalInRange(String fieldValue,
      String fieldName, int numDecimalDigit, BigDecimal min, BigDecimal max)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return super.validateBigDecimalInRange(fieldValue, fieldName,
          numDecimalDigit, min, max);
    }
    return null;
  }

  public boolean validateMandatoryFieldLength(String fieldValue, int min,
      int max, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldLength(fieldValue, min, max, fieldName);
  }

  public boolean validateMandatoryFieldLength(String fieldValue, int min,
      int max, String fieldName, boolean trim)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldLength(fieldValue, min, max, fieldName, trim);
  }

  public boolean validateMandatoryFieldMaxLength(String fieldValue,
      String fieldName, int max)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateFieldMaxLength(fieldValue, fieldName, max);
  }

  public boolean validateMandatoryEmail(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateEmail(fieldValue, fieldName);
  }

  public boolean validateMandatoryCAP(String fieldValue, String fieldName)
  {
    return validateMandatory(fieldValue, fieldName)
        && super.validateCAP(fieldValue, fieldName);
  }

  public boolean validateCuaa(String cuaa, String fieldName)
  {
    if (NemboUtils.VALIDATION.isEmpty(cuaa))
    {
      addError(fieldName, NemboConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() != 11 && cuaa.length() != 16)
    {
      addError(fieldName, NemboConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() == 16 && !NemboUtils.VALIDATION.controlloCf(cuaa))
    {
      addError(fieldName, NemboConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    if (cuaa.length() == 11 && !NemboUtils.VALIDATION.controlloPIVA(cuaa))
    {
      addError(fieldName, NemboConstants.GENERIC_ERRORS.CUAA_ERRATO);
      return false;
    }

    return true;
  }

  public Long validateMandatoryID(String fieldValue, String fieldName)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      return validateLong(fieldValue, fieldName);
    }
    else
    {
      return null;
    }
  }
  
  public String validateMandatoryValueList(String fieldValue, String fieldName,
	      String[] values)
  {
    if (validateMandatory(fieldValue, fieldName))
    {
      int len = values == null ? 0 : values.length;
      for (int i = 0; i < len; ++i)
      {
        if (fieldValue.equals(values[i]))
        {
          return fieldValue;
        }
      }
      addError(fieldValue, "Valore non consentito");
      return null;
    }
    else
    {
      return null;
    }
  }

}
