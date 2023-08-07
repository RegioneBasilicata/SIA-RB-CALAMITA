package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

public class PaginationTag extends BaseTag
{

  private static final long        serialVersionUID = 6618030419988165566L;
  private ArrayList<InfoColonnaVO> colonne          = null;
  private Long                     refID            = new Long(0);
  private String                   mantainSession   = null;

  public int doStartTag() throws JspException
  {
    colonne = new ArrayList<InfoColonnaVO>();
    return super.doStartTag();
  }

  public int doEndTag() throws JspException
  {
    try
    {
      this.pageContext.getOut().write(writeTag().toString());
    }
    catch (IOException e)
    {
      e.printStackTrace();
      throw new JspException(e);
    }
    return super.doEndTag();
  }

  private StringBuilder writeTag()
  {
    StringBuilder buffer = new StringBuilder();

    if (!GenericValidator.isBlankOrNull(mantainSession))
    {
      buffer.append("<input type=\"hidden\" id=\"mantainSession\" value=\""
          + mantainSession + "\" > </input> \n");
    }

    buffer.append(
        " <!-- PAGINAZIONE SUPERIORE -->  																									\n"
            + " <input type=\"hidden\" id=\"paginaCorrente\" value=\"\" > </input> 																		\n"
            + " <input type=\"hidden\" id=\"totPage\" value=\"\" > </input> 																			\n"
            + " <input type=\"hidden\" id=\"refID\" value=\"" + refID
            + "\" > </input> 																		\n"
            + " <input type=\"hidden\" id=\"numsub1OLD\" value=\"\" > </input> 																			\n"
            + " <input type=\"hidden\" id=\"visibleColumns\" value=\"\" > </input> 																			\n"
            + " <div class=\"fixed-table-pagination well well-large\" style=\"height:5em\"> 															\n"

            // Show/Hide columns ini
            + "<div class=\"columns columns-right btn-group pull-right\">																\n"
            + "	<div class=\"keep-open\" title=\"Columns\"> 																			\n"
            + "		<button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\"> 						\n"
            + "			<i class=\"glyphicon glyphicon-th icon-th\"></i> <span class=\"caret\"></span> 									\n"
            + "		</button> 																											\n"
            + "		<ul class=\"dropdown-menu dropdown-menu-hideColumn\" role=\"menu\">   																		\n");
    for (InfoColonnaVO colonna : colonne)
    {
      if (!GenericValidator.isBlankOrNull(colonna.getTitle()))
        buffer.append(
            "	<li><label><input type=\"checkbox\" name=\"showCol\" class=\"showCol\" id=\"showCol_"
                + colonna.getPropertyName() + "\" onClick=\"switchColumn('"
                + colonna.getPropertyName() + "');\" value=\""
                + colonna.getPropertyName() + "\" checked=\"checked\"> "
                + colonna.getTitle() + "</label></li> \n");
    }
    buffer.append("	</ul> 	\n"
        + "	</div> 				\n"
        + "</div> 				\n"
        // Show/Hide columns fine

        + "		<div class=\"pull-left pagination-detail\"> \n"
        + "			<span class=\"pagination-info\"><span class=\"firstPageRow\"></span>-<span class=\"lastPageRow\"></span> di <span class=\"totaleElementi\"></span> risultati</span> \n"
        + "			<span class=\"page-list\">  \n"
        + "				<span class=\"btn-group dropup\">  \n"
        + "					<button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\">  \n"
        + "						<span id=\"page-size\" class=\"page-size\"></span>   \n"
        + "						<span class=\"caret\"></span>  \n"
        + "					</button>  \n"
        + "					<ul id=\"numsub1\" class=\"dropdown-menu\" role=\"menu\">  \n"
        + "					</ul>  \n"
        + "				</span> risultati per pagina  \n"
        + "			</span>  \n"
        + "		</div> \n"
        + "		<div class=\"pull-right pagination-detail\">  																																					\n"
        + " 		<span class=\"pagingRight\"> \n"
        + " 			<span class=\"pageValue\"> \n"
        + " 				Pagina \n"
        + " 				<input type=\"text\" size=\"3\" name=\"numeroParziale1\" value=\"\"  id=\"numeroParziale1\" class=\"pageValueNum idleField\">	\n"
        + " 				di <span class=\"totPage\"></span> 																												\n"
        + "					<div class=\"btn-group\" role=\"group\"> \n"
        + "						<button type=\"button\" onclick=\"refrehDataPaginazioneSuperiore()\" class=\"btn btn-default\">Vai</button> \n"
        + "					</div> \n"
        + " 	    	</span><!--/pageValue--> \n"
        + getButtonAvantiIndietro()
        + " 		</span><!--/pagingRight--> \n" + " </div>\n"
        + "</div>\n"
        + " <!-- FINE PAGINAZIONE SUPERIORE --> \n"
        + " <table summary=\"...\" id=\"paginationTable\" class=\"myovertable table table-hover table-condensed table-bordered  tableBlueTh\"> \n"
        + " 	<thead> \n" + " 	  <tr> \n");
    for (int i = 0; i < colonne.size(); i++)
    {
      InfoColonnaVO colonna = colonne.get(i);
      buffer.append(
          "	<th data-table=\"paginationTable\" scope=\"col\" data-property=\""
              + colonna.getPropertyName() + "\"");

      if (colonna.isSortable())
        buffer.append("   class=\"sortable\" data-sortable=\"true\" ");

      buffer.append(">" + colonna.getTitle());

      if (colonna.getIcons() != null)
      {
        for (TableIcon icon : colonna.getIcons())
        {
          if (icon.isHeaderIcon())
          {
            buffer.append("<a onclick=\"" + icon.getOnclick() + "\" class=\""
                + icon.getCssClass() + "\" href=\"" + icon.getHref()
                + "\"></a>");
          }
        }
      }
      buffer.append("</th> \n");
    }

    buffer.append("</tr> \n"
        + " 	</thead> \n"
        + " 	<tbody> \n"
        + " 	</tbody> \n"
        + " </table> \n"
        + " <!-- PAGINAZIONE INFERIORE --> \n"
        + " <div class=\"fixed-table-pagination well well-large\" style=\"height:5em\"> \n"
        + " <div class=\"pull-right pagination-detail\">  \n"
        + " 	<!--La pagingLeft nella paginazione inferiore NON CI DEVE ESSERE--> \n"
        + " 	<span class=\"pagingRight\"> \n"
        + " 		<span class=\"pageValue\"> \n"
        + " 			Pagina\n"
        + " 			<input type=\"text\" size=\"3\" name=\"numeroParziale2\" value=\"\" id=\"numeroParziale2\" class=\"pageValueNum idleField\"> \n"
        + " 			di <span class=\"totPage\"></span> \n"
        + "				<div class=\"btn-group\" role=\"group\"> \n"
        + "					<button type=\"button\" onclick=\"refrehDataPaginazioneInferiore()\" class=\"btn btn-default\">Vai</button> \n"
        + "				</div> \n"
        + " 		</span><!--/pageValue--> \n"
        + getButtonAvantiIndietro()
        + " 	</span><!--/pagingRight--> \n" + " </div> </div>\n");

    return buffer;
  }

  private String getButtonAvantiIndietro()
  {
    StringBuilder sb = new StringBuilder(" <span class=\"pageNumbers\">  \n");
    sb.append(
        " <!--inizio quando prec disattivo (o non fare vedere prec linkato)--> 																					\n"
            + "	<div class=\"btn-group pageDisPrev\" role=\"group\"> \n"
            + "		<button type=\"button\" onclick=\"prevPage()\" class=\"btn btn-default disabled\">&lt;</button> \n"
            + "	</div> \n"
            + " <!--fine quando prec disattivo--> 																														\n"
            + " pagina <span class=\"currentElement\"></span> di <span class=\"totPage\"><span class=\"totPage\"></span></span>	\n"
            + " <!--inizio succ attivo (da alternare a quello disattivo o non visualizzarlo quando disattivo)--> 														\n"
            + "	<div class=\"btn-group nextPage\" role=\"group\"> \n"
            + "		<button type=\"button\" onclick=\"nextPage()\" class=\"btn btn-default\">&gt;</button> \n"
            + "	</div> \n"
            + " <!--fine succ attivo--> 																																\n"
            + " </span><!--/pageNumbers--> \n ");
    return sb.toString();
  }

  public void setColonna(InfoColonnaVO infoColonnaVO)
  {
    colonne.add(infoColonnaVO);
  }

  public Long getRefID()
  {
    return refID;
  }

  public void setRefID(Long refID)
  {
    this.refID = refID;
  }

  public String getMantainSession()
  {
    return mantainSession;
  }

  public void setMantainSession(String mantainSession)
  {
    this.mantainSession = mantainSession;
  }

}