package it.csi.nembo.nemboconf.util;

import java.math.BigDecimal;
import java.math.MathContext;

public class NumberUtils
{

  /**
   * Somme e sottrazioni vengono arrotondate correttamente
   * 
   * @param numero
   * @param precisione
   * @return double
   */
  public double arrotonda(double numero, int precisione)
  {
    return new java.math.BigDecimal(numero)
        .setScale(precisione, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * Verifica se il valore è un numero
   * 
   * @param value
   * @return boolean
   */
  public boolean isNumericValue(String value)
  {
    boolean result = true;

    try
    {
      new Long(value);
    }
    catch (NumberFormatException ex)
    {
      result = false;
    }

    return result;
  }

  /**
   * Restituisce un numero oppure null se la stringa contiene caratteri
   * alfanumerici
   * 
   * @param value
   * @return Long
   */
  public Long getNumericValue(String value)
  {
    Long result = null;

    try
    {
      result = new Long(value);
    }
    catch (NumberFormatException ex)
    {
      // throw ex;
    }

    return result;
  }

  public Double somma(Double totale, Double addendo, int precisione)
  {
    if (addendo == null)
      return totale;

    if (totale == null)
      totale = new Double(0);

    BigDecimal bdTotale = new BigDecimal(totale.doubleValue());
    BigDecimal bdAddendo = new BigDecimal(addendo.doubleValue());

    bdTotale = bdTotale.add(bdAddendo).setScale(precisione,
        BigDecimal.ROUND_HALF_UP);
    return new Double(bdTotale.doubleValue());
  }

  /**
   * Verifica che la stringa contenga un Long - Restituisce 0 non null
   * 
   * @param val
   * @return Long
   */
  public Long checkLong(String val)
  {
    try
    {
      return new Long(val);
    }
    catch (Exception ex)
    {
      return new Long(0);
    }
  }

  /**
   * Verifica che la stringa contenga un Double
   * 
   * @param val
   * @return
   */
  public Double checkDouble(String val)
  {
    try
    {
      return new Double(val);
    }
    catch (Exception ex)
    {
      return new Double(0);
    }
  }

  public BigDecimal nvl(BigDecimal value)
  {
    return nvl(value, BigDecimal.ZERO);
  }

  public BigDecimal nvl(BigDecimal value, BigDecimal defaultValue)
  {
    return value == null ? defaultValue : value;
  }

  public BigDecimal add(BigDecimal bd1, BigDecimal bd2)
  {
    return nvl(bd2).add(nvl(bd1), MathContext.UNLIMITED);
  }

  public long getValue(Long val)
  {
    return (val != null ? val.longValue() : (new Long("0")).longValue());
  }
}