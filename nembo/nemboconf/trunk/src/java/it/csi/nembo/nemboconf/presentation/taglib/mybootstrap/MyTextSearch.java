package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import org.apache.commons.validator.GenericValidator;

public class MyTextSearch extends MyBodyTag
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
  protected String          buttonText;
  protected String          actionUrl;
  protected String          valueProperty;
  protected String          textProperty;
  protected String          textValue;

  public String getTextValue()
  {
    return textValue;
  }

  public void setTextValue(String textValue)
  {
    this.textValue = textValue;
  }

  private boolean escapeHtml = true;

  public MyTextSearch()
  {
    super("form-control");
  }

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {

    sb.append("<div class='input-group'>");

    sb.append("<input");
    addAttribute(sb, "type", "text");
    addAttributeIfNotNull(sb, "placeholder", escapeHtml(placeholder));
    addAttributeIfNotNull(sb, "onfocus", escapeHtml(onfocus));
    addAttributeIfNotNull(sb, "value", textValue);
    addBaseAttributes(sb);
    TAG_UTIL.addAttributeIfNotNull(sb, "cols", cols);

    if (readonly)
    {
      TAG_UTIL.addAttribute(sb, "readonly", "readonly");
    }

    sb.append(">");

    // append button di ricerca
    sb.append(
        "<span class='group-span-filestyle input-group-btn' tabindex='0'>"
            + "<label class='btn btn-primary' onclick=\"loadDataTextSearch('"
            + id + "','" + actionUrl + "',$('#" + id
            + "')[0].value,'" + escapeHtml(valueProperty) + "','"
            + escapeHtml(textProperty)
            + "');return false;\" ><span class='glyphicon glyphicon-search'>&nbsp;"
            + escapeHtml(buttonText)
            + "</span></label></span>");
    sb.append("</div>");

  }

  public String getButtonText()
  {
    return buttonText;
  }

  public void setButtonText(String buttonText)
  {
    this.buttonText = buttonText;
  }

  public String getActionUrl()
  {
    return actionUrl;
  }

  public void setActionUrl(String actionUrl)
  {
    this.actionUrl = actionUrl;
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

  public String getValueProperty()
  {
    return valueProperty;
  }

  public void setValueProperty(String valueProperty)
  {
    this.valueProperty = valueProperty;
  }

  public String getTextProperty()
  {
    return textProperty;
  }

  public void setTextProperty(String textProperty)
  {
    this.textProperty = textProperty;
  }
}