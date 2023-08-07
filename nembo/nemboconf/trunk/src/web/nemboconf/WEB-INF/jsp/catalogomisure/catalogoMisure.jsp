<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<li class="active">catalogo misure</li>
				</ul>
			</div>
		</div>
	</div> 
	
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<h3>Catalogo misure</h3>
		<div id="filter-bar" style="margin-bottom:1em"> </div>
		<input type="hidden" id="idLivelloSelezionato" value="${idLivelloSelezionato}" >
		<table id="tblCatalogo" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh " 
		data-toggle="table" 
		data-url="">
        <thead>
          <tr>
            <th data-field="azione"  style="width: 48px" class="center">
            	<a onclick="toggleShowAll();" href="#" class="link" style="color:white;">Espandi</a>&nbsp;/<br/>
            <a onclick="toggleHideAll();" href="#" class="link" style="color:white;">Raggruppa</a>
              
            </th>
            <th data-field="misura" class="center" style="width: 60px" title="Misura">Mis.</th>
            <th data-field="sottomisura" class="center" style="width: 40px" title="Sottomisura">Sott.</th>
            <th data-field="operazione"  class="center" style="width: 40px" title="Operazione">Op.</th>
            <th data-field="codiceSottomisura" data-visible="false"></th>
            <th data-field="codiceOperazione" data-visible="false"></th>

            <th data-field="descrizione" >Descrizione</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="misura" items="${misure}">
            <tr id="id_${misura.idLivello}" class="misura" title="<c:out value='${misura.descrizione}'/>">
              <td>&nbsp;</td>
              <td>${misura.numeroMisura}<c:if test="${misura.espandibile}">
                  <span style="float: right" class="freccia" id="freccia_id_${misura.idLivello}"> <a href="#"
                    title="espandi/chiudi" onclick="toggleRow('id_${misura.idLivello}');return false"></a></span>
                </c:if></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>

             <td class=""><c:out value="${misura.descrizione}"/></td>
            </tr>
            <c:forEach var="sottomisura" items="${misura.elenco}">
              <tr class=" toggle_id_${misura.idLivello} sottomisura" id="id_${sottomisura.idLivello}"  title="<c:out value='${sottomisura.descrizione}'/>"
                style="display: none">
                <td>&nbsp;</td>
                <td>${sottomisura.numeroMisura}</td>
                <td>${sottomisura.numeroSottoMisura}<c:if test="${misura.espandibile}">
                    <span style="float: right" class="freccia" id="freccia_id_${sottomisura.idLivello}"> <a
                      href="#" title="espandi/chiudi" onclick="toggleRow('id_${sottomisura.idLivello}');return false"></a>
                    </span>
                  </c:if></td>
                <td>&nbsp;</td>
                <td>${sottomisura.codice}</td>
                <td>&nbsp;</td>

                <td class=""><c:out value="${sottomisura.descrizione}"/></td>
              </tr>
              <c:forEach var="operazione" items="${sottomisura.elenco}">
                <c:forEach var="focusArea" items="${operazione.listFocusArea}">
                  <tr class="toggle_id_${sottomisura.idLivello}" id="id_${operazione.idLivello}" style="display: none"  title="<c:out value='${operazione.descrizione}'/>">
                    <td><a href="beneficiari/dettaglio_${operazione.idLivello}.do" style="text-decoration: none;"><i class="icon-list icon-large" title="Gestisci Beneficiari"></i></a></td>
                    <td>${operazione.numeroMisura}</td>
                    <td>${operazione.numeroSottoMisura}</td>
                    <td>${operazione.numeroTipoOperazione}</td>
                    <td>${sottomisura.codice}</td>
                    <td>&nbsp;</td>
                    <td class=""><c:out value="${operazione.descrizione}"/></td>
                  </tr>
                </c:forEach>
              </c:forEach>
            </c:forEach>
          </c:forEach>
          
        </tbody>
      </table>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
 
 <script type="text/javascript">
 
 $( document ).ready(function() {

	 $('#tblCatalogo').bootstrapTable(
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

	 $('#filter-bar').bootstrapTableFilter({
         filters:[
				{
				    field: 'misura',    
				    label: 'Misura',   
				    type: 'ajaxSelect',   
                    source: 'getElencoMisureJson.do'
				},
                {
                    field: 'codiceSottomisura',   
                    label: 'Sottomisura',  
                    type: 'ajaxSelect',   
                    source: 'getElencoCodiciLivelliSottoMisureJson.do'
                },
                {
                    field: 'codiceOperazione',   
                    label: 'Operazione',  
                    type: 'ajaxSelect',   
                    source: 'getElencoCodiciOperazioneJson.do'
                },
				{
				    label: 'Focus Area',   
				    field: 'descrizione',    
				    label: 'Descrizione',   
				    type: 'search'
				}
         ],
         connectTo: '#tblCatalogo',
         onSubmit: function() {
         }
     });
 });
 var visualizzaRisultatiFiltri = false;
 $('#tblCatalogo').on('post-body.bs.table', function()
	      {
	        $(function()
	        {
		        if(!visualizzaRisultatiFiltri)
			    {
		        	$('tbody tr:not([class^="misura"])').hide();
			    }
		        visualizzaRisultatiFiltri = false;
	        });
	      });

 $('#filter-bar').on('submit.bs.table.filter', function()
	      {
	        $(function()
	        {
	        	visualizzaRisultatiFiltri = true;
	        	$('#tblCatalogo tbody tr:not([class^="misura"])').show();
	        	$('#tblCatalogo tbody tr[class^="sottomisura"]').hide();
	        });
	      });
 
 function toggleShowAll(){
 	$('#tblCatalogo tbody tr:not([class^="misura"])').show();
     }

 function toggleHideAll(){
 	$('#tblCatalogo tbody tr:not([class^="misura"])').hide();
     }

</script>
 <script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>