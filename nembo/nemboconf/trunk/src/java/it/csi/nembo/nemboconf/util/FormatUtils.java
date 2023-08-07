package it.csi.nembo.nemboconf.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Classe astratta per le funzioni di utilità sulle stringhe. La classe è
 * abstract perchè non deve essere usata direttamente ma solo dalla sua
 * implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public abstract class FormatUtils
{
  public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat(
      "###,##0.00");

  public String formatGenericNumber(BigDecimal number, int numDecimali,
      boolean decimaliObbligatori)
  {
    try
    {
      StringBuilder formatBuilder = new StringBuilder("0");
      if (numDecimali > 0)
      {
        formatBuilder.append(".");
        char decimale = decimaliObbligatori ? '0' : '#';
        for (int i = 0; i < numDecimali; ++i)
        {
          formatBuilder.append(decimale);
        }
      }
      return new DecimalFormat(formatBuilder.toString()).format(number);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public String formatCurrency(BigDecimal number)
  {
    try
    {
      if (number == null)
      {
        return "";
      }
      return CURRENCY_FORMAT.format(number);
    }
    catch (Exception e)
    {
      return "";
    }
  }
}