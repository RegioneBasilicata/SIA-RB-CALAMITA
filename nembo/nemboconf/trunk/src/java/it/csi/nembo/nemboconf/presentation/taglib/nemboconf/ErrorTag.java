package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.servlet.jsp.JspException;

public class ErrorTag extends BaseTag
{
  /** serialVersionUID */
  protected static final long serialVersionUID = -7620410490326047187L;
  public String               name;

  public int doEndTag() throws JspException
  {
    StringBuilder buffer = new StringBuilder();
    @SuppressWarnings("unchecked")
    Map<String, String> errors = (Map<String, String>) getELVariableValue(
        "errors");
    if (errors != null)
    {
      String error = errors.get(name);
      if (error != null)
      {
        int randomID = new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
        buffer.append("&nbsp;<span class=\"error_mark\" id=\"error_id_prova_")
            .append(randomID).append("\"><span class=\"no-display\">")
            .append(error).append("</span></span> ");
        buffer
            .append("<script  type=\"text/javascript\">{  $('#error_id_prova_")
            .append(randomID).append("').attr(\"title\",$('#error_id_prova_");
        buffer.append(randomID).append(" .no-display').html());$(\"[name='")
            .append(name).append("']\").addClass('error');");
        buffer.append("$(\"[name='").append(name)
            .append("']\").attr('title',$('#error_id_prova_");
        buffer.append(randomID).append(" .no-display').html());}</script>");
        try
        {
          this.pageContext.getOut().write(buffer.toString());
        }
        catch (IOException e)
        {
          e.printStackTrace();
          throw new JspException(e);
        }
      }
    }
    return super.doEndTag();
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

}