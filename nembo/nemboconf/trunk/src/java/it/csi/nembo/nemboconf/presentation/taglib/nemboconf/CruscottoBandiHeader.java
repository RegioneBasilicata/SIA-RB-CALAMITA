package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class CruscottoBandiHeader extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4558965659815071438L;

  public enum TABS
  {
    DATI_IDENTIFICATIVI, FILTRO_BENEFICIARIO, ATTI_PUBBLICATI, INTERVENTI, FILIERE, OGGETTI, QUADRI, CONTROLLI_TECNICI, CONTROLLI, GRAFICI, DICHIARAZIONI, IMPEGNI, ALLEGATI, RICEVUTA, DOCUMENTI_RICHIESTI, TESTO_VERBALI, FONDI, CONTRIBUTO, ATTIVAZIONE, CRITERI_SELEZIONE, COMUNI
  };

  private String activeTab;
  private String cu;

  @Override
  public int doEndTag() throws JspException
  {
    JspWriter writer = this.pageContext.getOut();
    try
    {
      BandoDTO bando = (BandoDTO) this.pageContext.getSession()
          .getAttribute("bando");
      String mostraQuadroInterventi = (String) this.pageContext.getSession()
          .getAttribute("mostraQuadroInterventi");
      String mostraQuadroComuni = (String) this.pageContext.getSession()
              .getAttribute("mostraQuadroComuni");
      writer.write("<div class=\"container-fluid\">");
      if (bando != null)
      {
        writer.write(
            "<div style=\"display: inline-block;vertical-align: middle;\" class=\"alert alert-info col-lg-12\" role=\"alert\">"
                + "<div class=\"col-lg-10\">Configurazione bando: <b>"
            	+ bando.getDenominazioneEscaped()
                + "</b></div>"
                );
        writer.write(
            "<div class=\"pull-right col-lg-2\" style=\"padding-top:2em;\"><form action='showLog_"
                + bando.getIdBando()
                + ".do'> <button type='submit'  style='margin-top:-1.6em' class='btn btn-default pull-right'  >Log attività</button></form></div>");
        writer.write("</div>");
      }
      writer.write("<ul class=\"nav nav-tabs no-border-bottom\">");

      /* Gestione Help INI */
      writer.write(
          "<li role=\"presentation\" style=\"border-bottom:1px solid #337ab7\">");
      writer.write("<a href=\"#\" onClick=\"toggleHelp('" + cu
          + "');return false;\"  style=\"background-color:#FFFFFF;border:0px\" class=\"toggle_help\" title=\"Visualizza Help\">");
      writer.write(
          "<span style=\"text-decoration: none; font-size:18px\" id=\"icona_help\" class=\"icon icon-info\"></span>");
      writer.write("</a></li>");
      /* Gestione Help FINE */

      writer.write("<li role=\"presentation\" "
          + (TABS.DATI_IDENTIFICATIVI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.DATI_IDENTIFICATIVI.toString().equals(activeTab)
              ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + "  href=\""
          + (TABS.DATI_IDENTIFICATIVI.toString().equals(activeTab) ? "#"
              : "datiidentificativi_" + bando.getIdBando() + ".do")
          + "\" >Dati Identificativi</a></li>");
      //Gestione Comuni Fogli
      if ("true".equals(mostraQuadroComuni))
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.COMUNI.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a "
            + (TABS.COMUNI.toString().equals(activeTab) ? "class=\"noLink\""
                : "class=\"noUnderline\"")
            + " href=\"" + (TABS.COMUNI.toString().equals(activeTab) ? "#"
                : "scelta_comuni.do")
            + "\" >Comuni</a></li>");
      }
      
      writer.write("<li role=\"presentation\" "
          + (TABS.ATTI_PUBBLICATI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.ATTI_PUBBLICATI.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.ATTI_PUBBLICATI.toString().equals(activeTab) ? "#"
              : "attiPubblicati.do")
          + "\" >Atti<br>da pubblicare</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.FILTRO_BENEFICIARIO.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.FILTRO_BENEFICIARIO.toString().equals(activeTab)
              ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\""
          + (TABS.FILTRO_BENEFICIARIO.toString().equals(activeTab) ? "#"
              : "filtrobeneficiari.do")
          + "\" >Beneficiari</a></li>");

      if ("true".equals(mostraQuadroInterventi))
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.INTERVENTI.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a "
            + (TABS.INTERVENTI.toString().equals(activeTab) ? "class=\"noLink\""
                : "class=\"noUnderline\"")
            + " href=\"" + (TABS.INTERVENTI.toString().equals(activeTab) ? "#"
                : "interventi.do")
            + "\" >Interventi</a></li>");
      }
      

      if (bando.getCodiceTipoBando() != null
          && bando.getCodiceTipoBando().compareTo("G") == 0)
        writer.write("<li role=\"presentation\" "
            + (TABS.FILIERE.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a "
            + (TABS.FILIERE.toString().equals(activeTab) ? "class=\"noLink\""
                : "class=\"noUnderline\"")
            + " href=\""
            + (TABS.FILIERE.toString().equals(activeTab) ? "#" : "filiere.do")
            + "\" >Filiere/Reti</a></li>");

      writer.write("<li role=\"presentation\" "
          + (TABS.OGGETTI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.OGGETTI.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.OGGETTI.toString().equals(activeTab) ? "#" : "oggetti.do")
          + "\" >Oggetti<br>Istanze</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.QUADRI.toString().equals(activeTab) ? "class=\"active\"" : "")
          + "><a "
          + (TABS.QUADRI.toString().equals(activeTab) ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\""
          + (TABS.QUADRI.toString().equals(activeTab) ? "#" : "quadri.do")
          + "\" >Quadri</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.CONTROLLI_TECNICI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.CONTROLLI_TECNICI.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.CONTROLLI_TECNICI.toString().equals(activeTab) ? "#"
              : "controlliAmm.do")
          + "\" >Controlli Tecnico<br>Amministrativi</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.GRAFICI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.GRAFICI.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.GRAFICI.toString().equals(activeTab) ? "#" : "grafici.do")
          + "\" >Grafici<br>Reportistica</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.CONTROLLI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.CONTROLLI.toString().equals(activeTab) ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\""
          + (TABS.CONTROLLI.toString().equals(activeTab) ? "#" : "controlli.do")
          + "\" >Controlli SIGC<br/>(Informatici)</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.DICHIARAZIONI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.DICHIARAZIONI.toString().equals(activeTab)
              ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\"" + (TABS.DICHIARAZIONI.toString().equals(activeTab) ? "#"
              : "dichiarazioni.do")
          + "\" >Dichiarazioni</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.IMPEGNI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.IMPEGNI.toString().equals(activeTab) ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\""
          + (TABS.IMPEGNI.toString().equals(activeTab) ? "#" : "impegni.do")
          + "\" >Impegni</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.ALLEGATI.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.ALLEGATI.toString().equals(activeTab) ? "class=\"noLink\""
              : "class=\"noUnderline\"")
          + " href=\""
          + (TABS.ALLEGATI.toString().equals(activeTab) ? "#" : "allegati.do")
          + "\" >Allegati</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.RICEVUTA.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.RICEVUTA.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.RICEVUTA.toString().equals(activeTab) ? "#" : "ricevuta.do")
          + "\" >Testi PEC / Mail</a></li>");
      
      writer.write("<li role=\"presentation\" "
              + (TABS.DOCUMENTI_RICHIESTI.toString().equals(activeTab) ? "class=\"active\""
                  : "")
              + "><a "
              + (TABS.DOCUMENTI_RICHIESTI.toString().equals(activeTab)
                  ? "class=\"noLink center\""
                  : "class=\"noUnderline center\"")
              + " href=\""
              + (TABS.DOCUMENTI_RICHIESTI.toString().equals(activeTab) ? "#" : "documentirichiesti.do")
              + "\" >Documenti<br/>richiesti</a></li>");
      
      writer.write("<li role=\"presentation\" "
          + (TABS.CRITERI_SELEZIONE.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.CRITERI_SELEZIONE.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\""
          + (TABS.CRITERI_SELEZIONE.toString().equals(activeTab) ? "#"
              : "criteriSelezione.do")
          + "\" >Criteri Selezione</a></li>");
      writer.write("<li role=\"presentation\" "
          + (TABS.TESTO_VERBALI.toString().equals(activeTab)
              ? "class=\"active\""
              : "")
          + "><a "
          + (TABS.TESTO_VERBALI.toString().equals(activeTab)
              ? "class=\"noLink center\""
              : "class=\"noUnderline center\"")
          + " href=\"" + (TABS.TESTO_VERBALI.toString().equals(activeTab) ? "#"
              : "testo_verbali.do")
          + "\" >Testo verbali</a></li>");

      writer.write("<li role=\"presentation\" "
          + (TABS.FONDI.toString().equals(activeTab) ? "class=\"active\"" : "")
          + ">"
//FIXME: hide tab Gestione Fondi in Cruscotto Bandi
//          + "<a href=\""
//          + (TABS.FONDI.toString().equals(activeTab) ? "#" : "gestfondi.do")
//          + "\" >Gestione fondi</a>"
          + "</li>");
      String codiceTipoBando = bando.getCodiceTipoBando();
      if (NemboConstants.TIPO_BANDO.INVESTIMENTO.equals(codiceTipoBando)
          || NemboConstants.TIPO_BANDO.GAL.equals(codiceTipoBando))
      {
        writer.write("<li role=\"presentation\" "
            + (TABS.CONTRIBUTO.toString().equals(activeTab) ? "class=\"active\""
                : "")
            + "><a href=\""
            + (TABS.CONTRIBUTO.toString().equals(activeTab) ? "#"
                : "contributo.do")
            + "\" >Contributo</a></li>");
      }

      writer.write("<li role=\"presentation\" "
          + (TABS.ATTIVAZIONE.toString().equals(activeTab) ? "class=\"active\""
              : "")
          + " ><a style=\"color:green\" title=\"Gestione attivazione oggetti\" "
          + (TABS.ATTIVAZIONE.toString().equals(activeTab) ? "class=\"noLink\""
              : "class=\"noUnderline center\"")
          + " href=\"" + (TABS.ATTIVAZIONE.toString().equals(activeTab) ? "#"
              : "attivazione.do")
          + "\" >Attivazione<br>oggetti</a></li>");
      writer.write("</ul>");
      writer.write("</div>");

      /* Gestione Help */
      writer.write(
          "<div id=\"help_container\" class=\"container-fluid\" style=\"display:none;margin-top:1em\"><blockquote id=\"help_text\"><span></span></blockquote></div>");
      String helpSession = (String) this.pageContext.getSession()
          .getAttribute(NemboConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE);

      if (helpSession != null
          && NemboConstants.FLAGS.SI.equals(helpSession))
      {
        writer.write(
            "<script>document.addEventListener('DOMContentLoaded', function() {toggleHelp('"
                + cu + "');}, false);</script>");
      }

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

  public String getCu()
  {
    return cu;
  }

  public void setCu(String cu)
  {
    this.cu = cu;
  }

}
