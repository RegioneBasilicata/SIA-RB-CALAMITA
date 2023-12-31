package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import java.io.IOException;

import javax.el.ELContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import it.csi.nembo.nemboconf.util.CustomTagUtils;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.validator.Errors;

public abstract class MySimpleTag extends SimpleTagSupport
{
  protected final CustomTagUtils TAG_UTIL = NemboUtils.TAG;
  protected String               id;
  protected String               name;
  protected String               cssClass;
  protected String               style;

  @Override
  public void doTag() throws JspException, IOException
  {
    StringBuilder sb = new StringBuilder();
    try
    {
      writeCustomTag(sb);
    }
    catch (Exception e)
    {
      throw new JspException(e);
    }
    getJspContext().getOut().write(sb.toString());
    super.doTag();
  }

  protected abstract void writeCustomTag(StringBuilder sb) throws Exception;

  /**
   * 
   * @param sb
   * @param customParameter
   *          E' un parametro generico e opzionale che viene passato al metodo
   *          dal tag. Non viene usato nell'implementazione di base, pu� essere
   *          utile nelle classi derivate. Utile nelle classi derivate per
   *          specificare informazioni particolari (es tipi di classe da
   *          aggiungere su condizioni particolari, es in presenza di errori)
   */
  protected void addBaseAttributes(StringBuilder sb, Object customParameter)
  {
    addIdAttribute(sb, customParameter);
    addNameAttribute(sb, customParameter);
    addCssClassAttribute(sb, customParameter);
    addStyleAttribute(sb, customParameter);
  }

  protected void addBaseAttributes(StringBuilder sb)
  {
    addBaseAttributes(sb, null);
  }

  protected void addStyleAttribute(StringBuilder sb, Object customParameter)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "style", style);
  }

  protected void addCssClassAttribute(StringBuilder sb, Object customParameter)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "class", cssClass);
  }

  protected void addNameAttribute(StringBuilder sb, Object customParameter)
  {
    TAG_UTIL.addAttributeIfNotNull(sb, "name", name);
  }

  protected void addIdAttribute(StringBuilder sb, Object customParameter)
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

  protected String getError(String errorName)
  {
    return getError(getErrors(), errorName);
  }

  protected String getError(Errors errors, String errorName)
  {
    if (errors != null)
    {
      return errors.get(errorName);
    }
    return null;
  }

  protected Errors getErrors()
  {
    return (Errors) getELVariableValue("errors");
  }

  protected Object getELVariableValue(String base, String name)
  {
    ELContext elContext = getJspContext().getELContext();
    return elContext.getELResolver().getValue(elContext, base, name);
  }

  protected Object getELVariableValue(String name)
  {
    return getELVariableValue(null, name);
  }
}