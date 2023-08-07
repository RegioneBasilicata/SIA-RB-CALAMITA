package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PaginationIconTag extends SimpleTagSupport
{

  private String  cssClass;
  private String  href;
  private boolean headerIcon = false;
  private boolean visible    = true;
  private String  tooltip    = null;
  private String  onclick;

  @Override
  public void doTag() throws JspException, IOException
  {
    ColumnTag parent = (ColumnTag) findAncestorWithClass(this,
        ColumnTag.class);
    if (parent != null)
    {
      TableIcon ti = new TableIcon();
      ti.setCssClass(cssClass);
      ti.setHeaderIcon(headerIcon);
      ti.setHref(href);
      ti.setVisible(visible);
      ti.setOnclick(onclick);
      ti.setTooltip(tooltip);
      parent.addIcon(ti);
    }
    reset();
    super.doTag();
  }

  private void reset()
  {
    cssClass = null;
    href = null;
    headerIcon = false;
    visible = true;
    onclick = null;
  }

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