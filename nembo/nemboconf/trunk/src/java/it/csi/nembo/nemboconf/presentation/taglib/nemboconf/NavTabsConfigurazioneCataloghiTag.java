package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class NavTabsConfigurazioneCataloghiTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4558965659815071438L;

  public static enum TABS
  {
    ELENCO_CDU, QUADRI, OGGETTI, ICONE, AZIONI, QUADRIOGGETTI, CONTROLLI, QUADRIOGGETTICONTROLLI
  };

  private String activeTab;

  @Override
  public int doEndTag() throws JspException
  {
    JspWriter writer = this.pageContext.getOut();
    try
    {
      writer.write("<div class=\"container-fluid\">");
      writer.write("<ul class=\"nav nav-tabs \">");
      writer.write("<li role=\"presentation\" "
          + (TABS.QUADRI.toString().equals(activeTab) ? "class=\"active\"" : "")
          + "><a href=\""
          + (TABS.QUADRI.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.QUADRI.toString() + ".do")
          + "\" >Quadri</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.OGGETTI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a href=\""
          + (TABS.OGGETTI.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.OGGETTI.toString() + ".do")
          + "\" >Oggetti</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.ICONE.toString().equals(activeTab) ? "class=\"active\"" : "")
          + "><a href=\""
          + (TABS.ICONE.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.ICONE.toString() + ".do")
          + "\" >Icone</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.ELENCO_CDU.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a href=\""
          + (TABS.ELENCO_CDU.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.ELENCO_CDU.toString() + ".do")
          + "\" >CDU</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.AZIONI.toString().equals(activeTab) ? "class=\"active\"" : "")
          + "><a href=\""
          + (TABS.AZIONI.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.AZIONI.toString() + ".do")
          + "\" >Azioni</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.CONTROLLI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a href=\""
          + (TABS.CONTROLLI.toString().equals(activeTab) ? "#"
              : "visualizza_tabella_" + TABS.CONTROLLI.toString() + ".do")
          + "\" >Controlli</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.QUADRIOGGETTI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a href=\""
          + (TABS.QUADRIOGGETTI.toString().equals(activeTab) ? "#"
              : "gestisci_quadro_oggetto.do")
          + "\" >Quadro - Oggetto</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.QUADRIOGGETTICONTROLLI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a href=\""
          + (TABS.QUADRIOGGETTICONTROLLI.toString().equals(activeTab) ? "#"
              : "gestisci_quadro_oggetto_controllo.do")
          + " \" >Q.O. - Controllo</a></li>");
      writer.write("</ul>");
      writer.write("</div>");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return super.doEndTag();
  }

  public String getActiveTab()
  {
    return activeTab;
  }

  public void setActiveTab(String activeTab)
  {
    this.activeTab = activeTab;
  }

}
