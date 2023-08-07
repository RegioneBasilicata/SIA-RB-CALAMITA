package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import org.apache.commons.validator.GenericValidator;

public class MyTextArea extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 92816793350454424L;
  protected Integer         cols;
  protected Integer         rows;
  protected String          placeholder;
  protected String          error;
  protected String          onfocus;
  protected boolean         readonly         = false;
  private Boolean           preferRequestValues;
  private boolean           escapeHtml       = true;

  public MyTextArea()
  {
    super("form-control");
  }

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    sb.append("<textarea");
    addAttribute(sb, "type", "text");
    addAttributeIfNotNull(sb, "placeholder", escapeHtml(placeholder));
    addAttributeIfNotNull(sb, "onfocus", escapeHtml(onfocus));
    addBaseAttributes(sb);
    TAG_UTIL.addAttributeIfNotNull(sb, "cols", cols);

    if (readonly)
    {
      TAG_UTIL.addAttribute(sb, "readonly", "readonly");
    }

    TAG_UTIL.addAttributeIfNotNull(sb, "rows", rows);
    sb.append(">");
    String textValue = null;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      textValue = this.pageContext.getRequest().getParameter(name);
    }
    else
    {
      if (bodyContent != null)
      {
        textValue = bodyContent.getString();
      }
    }
    if (textValue != null)
    {
      if (escapeHtml)
      {
        sb.append(escapeHtml(textValue));
      }
      else
      {
        sb.append(textValue);
      }
    }
    sb.append("</textarea>");
  }

  public Integer getCols()
  {
    return cols;
  }

  public void setCols(Integer cols)
  {
    this.cols = cols;
  }

  public Integer getRows()
  {
    return rows;
  }

  public void setRows(Integer rows)
  {
    this.rows = rows;
  }

  public String getPlaceholder()
  {
    return placeholder;
  }

  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public String getError()
  {
    return error;
  }

  public void setError(String error)
  {
    this.error = error;
  }

  @Override
  protected String processErrors()
  {
    if (GenericValidator.isBlankOrNull(error))
    {
      return super.processErrors();
    }
    else
    {
      return error;
    }
  }

  public String getOnfocus()
  {
    return onfocus;
  }

  public void setOnfocus(String onfocus)
  {
    this.onfocus = onfocus;
  }

  public boolean isEscapeHtml()
  {
    return escapeHtml;
  }

  public void setEscapeHtml(boolean escapeHtml)
  {
    this.escapeHtml = escapeHtml;
  }
}