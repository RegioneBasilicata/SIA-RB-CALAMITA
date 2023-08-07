package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.util.ArrayList;
import java.util.List;

public class InfoColonnaVO
{
  private String            title;
  private String            name;
  private String            propertyName;
  private boolean           sortable = false;
  protected List<TableIcon> icons;

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortable(boolean sortable)
  {
    this.sortable = sortable;
  }

  public void addIcon(TableIcon ti)
  {
    if (icons == null)
    {
      icons = new ArrayList<TableIcon>();
    }
    icons.add(ti);
  }

  public List<TableIcon> getIcons()
  {
    return icons;
  }

  public void setIcons(List<TableIcon> icons)
  {
    this.icons = icons;
  }
}
