package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class MyInputAddOn extends BodyTagSupport
{
  /** serialVersionUID */
  private static final long serialVersionUID = -857481504758912081L;
  protected boolean         left;

  @Override
  public int doEndTag() throws JspException
  {
    String body = null;
    MyInputGroup ig = (MyInputGroup) findAncestorWithClass(this,
        MyInputGroup.class);
    if (ig != null && bodyContent != null)
    {
      body = getAddOnClass(bodyContent.getString());
      ig.addAddOn(left, body);
    }
    return super.doEndTag();
  }

  public static String getAddOnClass(String body)
  {
    return new StringBuilder("<span class=\"input-group-addon\">").append(body)
        .append("</span>").toString();
  }

  public boolean isLeft()
  {
    return left;
  }

  public void setLeft(boolean left)
  {
    this.left = left;
  }

}
