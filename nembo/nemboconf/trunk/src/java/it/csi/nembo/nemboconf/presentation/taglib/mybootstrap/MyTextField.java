package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class MyTextField extends MyInputGroup
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5183034810591718296L;
  protected String          value;
  protected String          placeholder;
  protected Integer         maxlength;
  protected String          type;
  protected String          dataType;
  protected Boolean         preferRequestValues;

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    sb.append("<input");
    addAttribute(sb, "type", "text");
    addBaseAttributes(sb);
    String textValue = value;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      textValue = this.pageContext.getRequest().getParameter(name);
    }
    addAttributeIfNotNull(sb, "value", textValue);
    addAttributeIfNotNull(sb, "placeholder", escapeHtml(placeholder));

    if ("date".equals(type))
    {
      addAttribute(sb, "data-date-picker", "true");
    }

    if (!GenericValidator.isBlankOrNull(dataType))
    {
      addAttribute(sb, "data-type", dataType);
    }

    addAttributeIfNotNull(sb, "maxlength", maxlength);
    sb.append("/>");
  }

  @Override
  protected void addCssClassAttribute(StringBuilder sb, boolean errror)
  {
    addAttribute(sb, "class",
        NemboUtils.STRING.concat(" ", cssClass, "form-control"));
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getPlaceholder()
  {
    return placeholder;
  }

  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }

  public Integer getMaxlength()
  {
    return maxlength;
  }

  public void setMaxlength(Integer maxlength)
  {
    this.maxlength = maxlength;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
    if ("euro".equals(type))
    {
      this.addAddOn(true, null);
      this.addAddOn(false, MyInputAddOn.getAddOnClass("&euro;"));
    }
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }

}