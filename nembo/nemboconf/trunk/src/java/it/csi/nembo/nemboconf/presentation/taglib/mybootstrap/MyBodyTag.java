package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import javax.el.ELContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;

import it.csi.nembo.nemboconf.util.CustomTagUtils;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.validator.Errors;

public abstract class MyBodyTag extends BodyTagSupport
{
  public static final String     CSS_ERROR_CLASSES = "has-error red-tooltip";
  protected final CustomTagUtils TAG_UTIL          = NemboUtils.TAG;
  protected String               id;
  protected String               name;
  protected String               cssClass;
  protected String               style;
  protected String               label;
  protected Integer              labelSize;
  protected Boolean              disabled;
  protected Integer              controlSize;
  protected String               onclick;
  protected String               onchange;
  protected String               onkeyup;
  private String                 additionalCssClass;

  /** serialVersionUID */
  private static final long      serialVersionUID  = -5111481346287406888L;

  public MyBodyTag()
  {

  }

  public MyBodyTag(String additionalCssClass)
  {
    this.additionalCssClass = additionalCssClass;
  }

  protected String processErrors()
  {
    return escapeHtml(getError(name));
  }

  protected void addErrorAttributes(StringBuilder sb, String errorMessage)
  {
    TAG_UTIL.addAttribute(sb, "data-original-title", errorMessage);
    TAG_UTIL.addAttribute(sb, "data-toggle", "error-tooltip");
  }

  @Override
  public int doEndTag() throws JspException
  {
    String errorMessage = processErrors();
    try
    {
      boolean error = errorMessage != null;
      StringBuilder sb = new StringBuilder();
      boolean group = label != null;
      if (group)
      {
        sb.append("<div class=\"form-group\"><label ");
        int lSize = 3;
        int cSize = 9;
        if (labelSize != null || controlSize != null)
        {
          if (labelSize != null && controlSize != null)
          {
            lSize = labelSize;
            cSize = controlSize;
          }
          else
          {
            if (labelSize != null)
            {
              lSize = labelSize;
              cSize = 12 - lSize;
            }
            else
            {
              cSize = controlSize;
              lSize = 12 - cSize;
            }
          }
        }
        TAG_UTIL.addAttribute(sb, "class", NemboUtils.STRING.concat(" ",
            "control-label", "col-sm-" + lSize));
        sb.append(">");
        sb.append(escapeHtml(label));
        sb.append("</label>");
        sb.append("<div ");
        TAG_UTIL.addAttribute(sb, "style", "padding-right:30px");
        if (error)
        {
          TAG_UTIL.addAttribute(sb, "class", NemboUtils.STRING.concat(" ",
              CSS_ERROR_CLASSES, "col-sm-" + cSize));
          addErrorAttributes(sb, errorMessage);
          errorMessage = null;
        }
        else
        {
          TAG_UTIL.addAttribute(sb, "class",
              NemboUtils.STRING.concat(" ", "col-sm-" + cSize));
        }
        sb.append(">");
      }
      else
      {
        if (error)
        {
          sb.append("<div ");
          TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES);
          addErrorAttributes(sb, errorMessage);
          errorMessage = null;
          sb.append(">");
        }
      }
      writeCustomTag(sb, errorMessage);
      if (group)
      {
        sb.append("</div></div>");
      }
      else
      {
        if (error)
        {
          sb.append("</div>");
        }
      }
      this.pageContext.getOut().write(sb.toString());
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  protected abstract void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception;

  protected Errors getErrors()
  {
    return (Errors) getELVariableValue("errors");
  }

  protected String getError(Errors errors, String errorName)
  {
    if (errors != null)
    {
      return errors.get(errorName);
    }
    return null;
  }

  protected String getError(String errorName)
  {
    return getError(getErrors(), errorName);
  }

  protected Object getELVariableValue(String base, String name)
  {
    ELContext elContext = this.pageContext.getELContext();
    return elContext.getELResolver().getValue(elContext, base, name);
  }

  protected Object getELVariableValue(String name)
  {
    return getELVariableValue(null, name);
  }

  protected String escapeHtml(String value)
  {
    return StringEscapeUtils.escapeHtml4(value);
  }

  protected void addAttribute(StringBuilder sb, String attrName,
      String attrValue)
  {
    sb.append(" ").append(attrName).append(" = \"").append(attrValue)
        .append("\"");
  }

  /**
   * 
   * @param sb
   */
  protected void addBaseAttributes(StringBuilder sb, boolean error)
  {
    addIdAttribute(sb, error);
    addNameAttribute(sb, error);
    addCssClassAttribute(sb, error);
    addStyleAttribute(sb, error);
    addDisableAttributeIfSet(sb, error);
    addOnClickAttribute(sb, error);
    addOnChangeAttribute(sb, error);
    addOnKeyUpAttribute(sb, error);
  }

  private void addOnKeyUpAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onkeyup", onkeyup);
  }

  protected void addOnChangeAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onchange", onchange);
  }

  protected void addOnClickAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "onclick", onclick);
  }

  protected void addDisableAttributeIfSet(StringBuilder sb, boolean error)
  {
    if (disabled != null && disabled.booleanValue())
    {
      TAG_UTIL.addAttribute(sb, "disabled", "disabled");
    }
  }

  protected void addBaseAttributes(StringBuilder sb)
  {
    addBaseAttributes(sb, false);
  }

  protected void addStyleAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "style", style);
  }

  protected void addCssClassAttribute(StringBuilder sb, boolean error)
  {
    String cSizeClass = null;
    if (label != null)
    {
      int cSize = 9;
      if (labelSize != null || controlSize != null)
      {
        if (labelSize != null && controlSize != null)
        {
          cSize = controlSize;
        }
        else
        {
          if (labelSize != null)
          {
            cSize = 12 - labelSize;
          }
          else
          {
            cSize = controlSize;
          }
        }
      }
      cSizeClass = "col-sm-" + cSize;
    }
    if (error)
    {
      TAG_UTIL.addAttributeIfNotNull(sb, "class", NemboUtils.STRING.concat(
          " ", CSS_ERROR_CLASSES, additionalCssClass, cSizeClass, cssClass));
    }
    else
    {
      TAG_UTIL.addAttributeIfNotNull(sb, "class", NemboUtils.STRING
          .concat(" ", cSizeClass, additionalCssClass, cssClass));
    }
  }

  protected void addNameAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "name", name);
  }

  protected void addIdAttribute(StringBuilder sb, boolean error)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "id", id);
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public String getStyle()
  {
    return style;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Integer getLabelSize()
  {
    return labelSize;
  }

  public void setLabelSize(Integer labelSize)
  {
    this.labelSize = labelSize;
  }

  public Integer getControlSize()
  {
    return controlSize;
  }

  public void setControlSize(Integer controlSize)
  {
    this.controlSize = controlSize;
  }

  public StringBuilder addAttribute(StringBuilder sb, String name, Object value)
  {
    return TAG_UTIL.addAttribute(sb, name, value);
  }

  public StringBuilder addAttributeIfNotNull(StringBuilder sb, String name,
      Object value)
  {
    return TAG_UTIL.addAttributeIfNotNull(sb, name, value);
  }

  public Boolean getDisabled()
  {
    return disabled;
  }

  public void setDisabled(Boolean disabled)
  {
    this.disabled = disabled;
  }

  public String getOnclick()
  {
    return onclick;
  }

  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  public String getOnchange()
  {
    return onchange;
  }

  public void setOnchange(String onchange)
  {
    this.onchange = onchange;
  }

  public String getOnkeyup()
  {
    return onkeyup;
  }

  public void setOnkeyup(String onkeyup)
  {
    this.onkeyup = onkeyup;
  }
}
