package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

public class MyStaticText extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 92816793350454424L;
  private boolean           visible          = true;

  @Override
  public void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    sb.append("<span ");
    addAttribute(sb, "class", "form-control-static");
    addBaseAttributes(sb);
    if (!visible)
      sb.append(" style=\"display:none\" ");
    sb.append(">");
    if (bodyContent != null)
    {
      sb.append(bodyContent.getString());
    }
    sb.append("</span>");
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

}