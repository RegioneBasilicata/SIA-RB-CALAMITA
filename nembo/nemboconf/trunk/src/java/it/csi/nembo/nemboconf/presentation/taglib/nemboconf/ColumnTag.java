package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;

public class ColumnTag extends BaseTag
{

  /**
   * 
   */
  private static final long serialVersionUID = -4256576842921452053L;
  private InfoColonnaVO     infoColonnaVO    = new InfoColonnaVO();

  public int doEndTag() throws JspException
  {
    PaginationTag parent = (PaginationTag) findAncestorWithClass(this,
        PaginationTag.class);
    if (parent != null)
    {
      parent.setColonna(infoColonnaVO);
      infoColonnaVO = new InfoColonnaVO();
    }
    return super.doEndTag();
  }

  public void addIcon(TableIcon ti)
  {
    if (infoColonnaVO.getIcons() == null)
    {
      infoColonnaVO.setIcons(new ArrayList<TableIcon>());
    }
    infoColonnaVO.getIcons().add(ti);
  }

  public InfoColonnaVO getInfoColonnaVO()
  {
    return infoColonnaVO;
  }

  public void setInfoColonnaVO(InfoColonnaVO infoColonnaVO)
  {
    this.infoColonnaVO = infoColonnaVO;
  }

  public void setTitle(String title)
  {
    infoColonnaVO.setTitle(title);
  }

  public void setName(String name)
  {
    infoColonnaVO.setName(name);
  }

  public void setPropertyName(String propertyName)
  {
    infoColonnaVO.setPropertyName(propertyName);
  }

  public void setSortable(Boolean val)
  {
    infoColonnaVO.setSortable(val);
  }

}