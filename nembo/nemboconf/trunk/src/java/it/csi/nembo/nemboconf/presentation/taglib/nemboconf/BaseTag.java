package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import javax.el.ELContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import it.csi.nembo.nemboconf.util.NemboConstants;

public abstract class BaseTag extends BodyTagSupport
{
  /** serialVersionUID */
  private static final long  serialVersionUID = 7631646001329816975L;
  public static final Logger logger           = Logger
      .getLogger(NemboConstants.LOGGIN.LOGGER_NAME + ".presentation");

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
}
