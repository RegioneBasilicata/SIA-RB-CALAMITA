package it.csi.nembo.nemboconf.util;

/**
 * Classe astratta per le funzioni di utilità sulle stringhe. La classe è
 * abstract perchè non deve essere usata direttamente ma solo dalla sua
 * implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public abstract class StringUtils
{
  public String nvl(Object obj, String defaultValue)
  {
    return obj == null ? defaultValue : obj.toString();
  }

  public String toUpperCaseTrim(String str)
  {
    return str == null ? null : str.trim().toUpperCase();
  }

  public String toLowerCaseTrim(String str)
  {
    return str == null ? null : str.trim().toLowerCase();
  }

  public String toUpperCase(String str)
  {
    return str == null ? null : str.toUpperCase();
  }

  public String toLowerCase(String str)
  {
    return str == null ? null : str.toLowerCase();
  }

  public String nvl(Object obj)
  {
    return nvl(obj, "");
  }

  public String trim(String str)
  {
    return str == null ? null : str.trim();
  }

  public String nvl(String obj, String defaultValue)
  {
    return obj == null ? defaultValue : obj;
  }

  public String nvl(String obj)
  {
    return nvl(obj, "");
  }

  public String safeHTMLText(String str)
  {
    return nvl(str).replace("<", "&lt;").replace(">", "&gt;").replace("\n",
        "<br/>");
  }

  public String compactText(String str)
  {
    return nvl(str).replace("\n", "").replace("\r", "").replace("\t", "");
  }
  
  public String replaceCRLFTabWithOneSpace(String str)
  {
    return nvl(str).replace("\n", " ").replace("\r", " ").replace("\t", " ");
  }

  public String replaceAWantedChar(String str)
  {
    return str.replace(Character.toString((char) 146), "'");
  }

  public String checkNull(Object object)
  {
    if (object != null)
    {
      return object.toString();
    }
    else
    {
      return null;
    }
  }

  public String concat(String separator, String... strings)
  {
    StringBuilder sb = new StringBuilder();
    if (strings != null)
    {
      for (String s : strings)
      {
        if (s != null)
        {
          if (sb.length() > 0)
          {
            sb.append(separator);
          }
          sb.append(s);
        }
      }
    }
    return sb.toString();
  }

  // Inserisce a sinistra il carattere di riempimento, fino alla lunghezza
  // specificata.
  public String lpad(String str, char padChar, int length)
  {
    if (str == null)
      return "";

    int chrMancanti = length - str.length();
    for (int i = 0; i < chrMancanti; i++)
      str = padChar + str;

    return str;
  }

  public String insertCharAt(String str, int index, char chr)
  {
    if (index > str.length() - 1)
      return str;
    return str.substring(0, index) + chr + str.substring(index);
  }

  public static boolean isNumeric(String str)
  {
    try
    {
      Double.parseDouble(str);
    }
    catch (NumberFormatException nfe)
    {
      return false;
    }
    return true;
  }

  public String escapeSpecialsChar(String text)
  {
    if (text == null)
    {
      return null;
    }
    // System.out.println((int)motivazione.charAt(0)); per sapere il numero
    return text.replace("&#61656;", "-")
        .replace(new Character((char) 149).charValue(), (char) 0xb7) // rimpiazzo
                                                                     // elenco
                                                                     // puntato
                                                                     // con
                                                                     // punto
        .replace(new Character((char) 146).charValue(), (char) 0x27) // rimpiazzo
                                                                     // apostrofo
                                                                     // con '
        .replace(new Character((char) 147).charValue(), (char) 0x22) // rimpiazzo
                                                                     // aperte
                                                                     // virgolette
                                                                     // con "
        .replace(new Character((char) 148).charValue(), (char) 0x22); // rimpiazzo
                                                                      // chiuse
                                                                      // virgolette
                                                                      // con ";
  }
}
