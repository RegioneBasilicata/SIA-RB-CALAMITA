package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.util.List;

public class PaginationVO<T>
{

  private List<T>    pageElement;
  private List<Long> elementForPage;
  private int        currPage;
  private int        totElement;

  public List<T> getPageElement()
  {
    return pageElement;
  }

  public void setPageElement(List<T> pageElement)
  {
    this.pageElement = pageElement;
  }

  public int getCurrPage()
  {
    return currPage;
  }

  public void setCurrPage(int currPage)
  {
    this.currPage = currPage;
  }

  public int getTotElement()
  {
    return totElement;
  }

  public void setTotElement(int totElement)
  {
    this.totElement = totElement;
  }

  public List<Long> getElementForPage()
  {
    return elementForPage;
  }

  public void setElementForPage(List<Long> elementForPage)
  {
    this.elementForPage = elementForPage;
  }
}
