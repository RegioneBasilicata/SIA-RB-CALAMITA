package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TableIcon extends SimpleTagSupport
{

  private String  cssClass;
  private String  href;
  private boolean headerIcon = false;
  private boolean visible    = true;
  private String  tooltip;
  private String  onclick;

  public String getCssClass()
  {
    return cssClass;
  }

  public void setCssClass(String cssClass)
  {
    this.cssClass = cssClass;
  }

  public String getHref()
  {
    return href;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

  public boolean isHeaderIcon()
  {
    return headerIcon;
  }

  public void setHeaderIcon(boolean headerIcon)
  {
    this.headerIcon = headerIcon;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  public String getOnclick()
  {
    return onclick;
  }

  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
}
