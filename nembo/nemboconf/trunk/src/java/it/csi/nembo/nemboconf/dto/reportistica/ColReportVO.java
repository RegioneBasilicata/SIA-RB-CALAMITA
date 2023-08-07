package it.csi.nembo.nemboconf.dto.reportistica;

import java.io.Serializable;
import java.util.HashMap;

public class ColReportVO implements Serializable
{
  private static final long       serialVersionUID = 9104726695231817976L;

  public static final String      TYPE_BOOLEAN     = "boolean";
  public static final String      TYPE_NUMBER      = "number";
  public static final String      TYPE_BLOB        = "blob";
  public static final String      TYPE_STRING      = "string";
  public static final String      TYPE_DATE        = "date";
  public static final String      TYPE_DATETIME    = "datetime";
  public static final String      TYPE_TIMEOFDAY   = "timeofday";

  private String                  type;
  private String                  id;
  private String                  label;
  private String                  pattern;
  private String                  commento;
  private boolean                 nullable;
  private int                     maxSize;
  private HashMap<String, Object> p;

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }

  public HashMap<String, Object> getP()
  {
    return p;
  }

  public void setP(HashMap<String, Object> p)
  {
    this.p = p;
  }

  public boolean isNullable()
  {
    return nullable;
  }

  public void setNullable(boolean nullable)
  {
    this.nullable = nullable;
  }

  public int getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize(int maxSize)
  {
    this.maxSize = maxSize;
  }

  public String getCommento()
  {
    return commento;
  }

  public void setCommento(String commento)
  {
    this.commento = commento;
  }

}
