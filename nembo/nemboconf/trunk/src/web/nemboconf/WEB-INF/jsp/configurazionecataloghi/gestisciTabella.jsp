<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css"> 
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">

<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
	  
	<p:utente/>
	
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li class="active">Configurazione cataloghi</li>
				</ul>
			</div>
		</div>
	</div>
	
	<p:NavTabsConfigurazioneCataloghi activeTab="${quadroCatalogo}"></p:NavTabsConfigurazioneCataloghi>
	
	<div class="container-fluid"  id="content" style="margin-bottom:4em">
	<b:panel type="DEFAULT" cssClass="navpanel">
		<div id="filter-bar" style="margin-bottom:1em"> </div>
			<table id="dettEstrazioneTable" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh" 
				  data-toggle="table" 
				  data-undefined-text = ''
				  data-pagination="true"  
				  data-show-pagination-switch="true" 
				  data-show-toggle="true"
				  data-pagination-v-align="top" 
				  data-show-columns="true"
				  data-show-filter="true">
				<thead>
		        	<tr>
		        		<th data-switchable="false" data-width="40px"><a href="#" title="aggiungi record" onclick="forwardToPage('addrow_${quadroCatalogo}.do')" class="ico24 ico_add"></a></th>
						<c:forEach items="${tabella.jsonData['cols']}" var="col" varStatus="idx">
							<th data-field="${col.id}" data-sortable="true"><c:out value="${col.label}"></c:out></th>
						</c:forEach>
		        	</tr>
			    </thead>
			    <tbody>
			    	<c:forEach items="${tabella.jsonData['rows']}" var="row" >
				    	<tr>
			    		<c:forEach items="${row['c']}" var="cell" varStatus="idx">
			    			<c:if test="${idx.index == 0}">
								<td data-switchable="false" data-width="40px"><a href="#" title="elimina record" onclick="return eliminaRecord('${quadroCatalogo}',${cell.valueFormatted})"  class="ico24 ico_trash"></a></td>
							</c:if>
							<td>${cell.valueFormatted}</td>
			    		</c:forEach>
				    	</tr>
			    	</c:forEach>
			    </tbody>
			</table>
	    </b:panel>
	</div>
		
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>



<script>

$( document ).ready(function() {
	
	$('#dettEstrazioneTable').bootstrapTable(
		    {
		   queryParams: function(params) 
		     {
		            var values = $('#filter-bar').bootstrapTableFilter('getData');
		            if ( !jQuery.isEmptyObject(values)) {
		            	var elabFilter = JSON.stringify(values);
    			        return elabFilter;
		            }
		            return params;
		        }
		    }
		);

	${filtriTabella}
});


$('#dettEstrazioneTable').on("post-body.bs.table", function (data) {
	$('.fixed-table-loading').remove();
	$('#dettEstrazioneCnt').show();
	$('#pleaseWait').hide();
	
});

function eliminaRecord(quadroCatalogo, id)
{
    return openPageInPopup('deleterow_'+quadroCatalogo+'_'+id+'.do','dlgEliminaRecord','Elimina', 'modal-large');
}

</script>	
<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>	
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>