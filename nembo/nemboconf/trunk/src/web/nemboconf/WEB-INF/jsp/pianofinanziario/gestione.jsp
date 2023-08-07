<%@page import="it.csi.nembo.nemboconf.util.NemboConstants"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css"> 
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">
<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />
  <p:utente />
  <div class="container-fluid">
    <div class="row">
      <div class="moduletable">
        <ul class="breadcrumb">
          <li><a href="../index.do">Home</a> <span class="divider">/</span></li>
          <li><a href="./storico.do">Storico</a> <span class="divider">/</span></li>
          <li class="active">Piano finanziario</li>
        </ul>
      </div>
    </div>
  </div>
  <p:messaggistica/>
  <div class="container-fluid" id="content" style="margin-bottom: 3em">
    <form:form action="" id="dati" method="post">
      <div id="filter-bar" style="margin-bottom:1em"> </div>
      <table id="tblPianoFinanziario" class="table table-hover table-bordered tableBlueTh">
        <thead>
          <tr>
            <th data-field="azione" style="width: 48px;">
            
            <a onclick="toggleShowAll();" href="#" class="link" style="color:white;">Espandi</a>&nbsp;/<br/>
            <a onclick="toggleHideAll();" href="#" class="link" style="color:white;">Raggruppa</a>
             
            </th>
            <th data-field="misura" class="center" style="width: 60px" title="Misura">Mis.</th>
            <th data-field="sottomisura" class="center" style="width: 40px" title="Sottomisura">Sott.</th>
            <th data-field="operazione" class="center" style="width: 40px" title="Operazione">Op.</th>
            <th data-field="codiceSottomisura" data-visible="false"></th>
            <th data-field="codiceOperazione" data-visible="false"></th>
            <th data-field="settore" class="center" style="width: 40px" title="Settore">Sett.</th>
            <th data-field="focusarea" class="center">Focus Area</th>
            <th data-field="impbudget"  class="center numero importo-misura">Importo budget</th>
            <th data-field="imptrascinato"  class="center numero importo-misura">Importo trascinato</th>
            <th data-field="risattivate"  class="center numero importo-misura">Risorse attivate</th>
            <th data-field="risdisponibili"  class="center numero importo-misura">Risorse disponibili</th>
            <th data-field="economie"  class="center numero importo-misura">Economie</th>
            <th data-field="pagato"  class="center numero importo-misura">Liquidato</th>       
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
              <td>&nbsp;</td>
              <td >&nbsp;</td>
              <td id="importo_${misura.idLivello}" class="numero importo-misura tot-misura importo_class importo_riga_${misura.idLivello}">${misura.totaleEuro}</td>
              <td id="trascinato_${misura.idLivello}" class="numero importo-misura tot-misura trascinato_class importo_riga_${misura.idLivello}">${misura.totaleTrascinatoEuro}</td>
              <td id="risorseAttivate_${misura.idLivello}" class="numero importo-misura tot-misura risAttivate_class">${misura.totaleRisorseAttivateEuro}</td>
              <td id="risorseDisponibili_${misura.idLivello}" class="numero importo-misura tot-misura risDisponibili_class">${misura.risorseDisponibiliEuro}</td>
              <td id="economia_${misura.idLivello}" class="numero importo-misura tot-misura economia_class">${misura.totaleEconomieEuro}</td>
              <td id="pagato_${misura.idLivello}" class="numero importo-misura tot-misura pagato_class">${misura.totalePagatoEuro}</td>

            </tr>
            <c:forEach var="sottomisura" items="${misura.elenco}">
              <tr class="toggle_id_${misura.idLivello} sottomisura" id="id_${sottomisura.idLivello}"  title="<c:out value='${sottomisura.descrizione}'/>"
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
                <td>&nbsp;</td>
                <td >&nbsp;</td>
                <td id="importo_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura importo_class">${sottomisura.totaleEuro}</td>
                <td id="trascinato_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura trascinato_class">${sottomisura.totaleTrascinatoEuro}</td>
                <td id="risorseAttivate_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura risAttivate_class">${sottomisura.totaleRisorseAttivateEuro}</td>
                <td id="risorseDisponibili_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura risDisponibili_class">${sottomisura.risorseDisponibiliEuro}</td>
                <td id="economia_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura economia_class">${sottomisura.totaleEconomieEuro}</td>
                <td id="pagato_${sottomisura.idLivello}" class="numero importo-misura tot-sottomisura pagato_class">${sottomisura.totalePagatoEuro}</td>
              </tr>
              <c:forEach var="operazione" items="${sottomisura.elenco}">
                <c:forEach var="focusArea" items="${operazione.listFocusArea}">
                  <tr class="toggle_id_${sottomisura.idLivello}" id="id_${operazione.idLivello}" style="display: none"  title="<c:out value='${operazione.descrizione}'/>">
                   
                      <td> 
	                      <c:if test="${modifica}">
	                      	<p:abilitazione dirittoAccessoMinimo="R" idLivello="<%=NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO %>">
	                      		<a href="javascript:void(0);" onclick="return modificaLinea(${operazione.idLivello})" class="ico24 ico_modify" title="modifica"></a>
	                      	</p:abilitazione>
	                      </c:if>
	                      <c:if test="${focusArea.numeroRecordStorico > 0}">
	                      	<a id="storico_${operazione.idLivello}" href="javascript:void(0);" onclick="return visualizzaStorico(${operazione.idLivello})" class="ico24 ico_magnify" title="storico"></a>
	                      </c:if>
	                      <c:if test="${focusArea.numeroRecordStorico == 0}">
	                      	<a id="storico_${operazione.idLivello}" href="javascript:void(0);" style="display:none;" onclick="return visualizzaStorico(${operazione.idLivello})" class="ico24 ico_magnify" title="storico"></a>
	                      </c:if>
                      </td>
                    <td>${operazione.numeroMisura}</td>
                    <td>${operazione.numeroSottoMisura}</td>
                    <td>${operazione.numeroTipoOperazione}</td>
                    <td>${sottomisura.codice}</td>
                    <td>${operazione.numeroOperazione}</td>
                    <td>${operazione.codiceSettore}</td>
                    <td >${focusArea.codice}</td>
                    <td id="importo_${operazione.idLivello}" class="numero numero tot-focusarea importo_class">${focusArea.importoEuro}</td>
                    <td id="trascinato_${operazione.idLivello}" class="numero numero tot-focusarea trascinato_class">${focusArea.importoTrascinatoEuro}</td>
                    <td id="risorseAttivate_${operazione.idLivello}" class="numero numero tot-focusarea risAttivate_class">${operazione.totaleRisorseAttivateEuro}</td>
                    <td id="risorseDisponibili_${operazione.idLivello}" class="numero numero tot-focusarea risDisponibili_class">${operazione.risorseDisponibiliEuro}</td>
                    <td id="economia_${operazione.idLivello}" class="numero numero tot-focusarea economia_class">${operazione.totaleEconomieEuro}</td>
                    <td id="pagato_${operazione.idLivello}" class="numero numero tot-focusarea pagato_class">${focusArea.importoPagatoEuro}</td>                
                  </tr>
                </c:forEach>
              </c:forEach>
            </c:forEach>
          </c:forEach>
          
        </tbody>
        <tfoot>
        <tr>
            <td colspan="6"><strong class="pull-right">Totale</strong></td>
            <th id="importo_generale" class="numero importo-misura">${totali.importoEuro}</th>
            <th id="trascinato_generale" class="numero importo-misura">${totali.trascinatoEuro}</th>
            <th id="risorseAttivate_attivate" class="numero importo-misura">${totali.risorseAttivateEuro}</th>
            <th id="risorseDisponibili_generale" class="numero importo-misura">${totali.risorseDisponibiliEuro}</th>
            <th id="economia_generale" class="numero importo-misura">${totali.economieEuro}</th>
            <th id="pagato_generale" class="numero importo-misura">${totali.pagatoEuro}</th>
          </tr>
        </tfoot>
      </table>
    </form:form>
  </div>
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
  <script type="text/javascript">

   function ricalcolaTotali(){

	   var livelloMax = "" ;
	   var val = $(".tot-misura")[0];
	  	if( val!==undefined && val!=null && val!="")
		  	{
				livelloMax = ".tot-misura";
		  	}
	  	else{
	  			val = $(".tot-sottomisura")[0];
	  			if( val!==undefined && val!=null && val!="")
			  	{
					livelloMax = ".tot-sottomisura";
			  	}
	  			else
		  			livelloMax = ".tot-focusarea";
		  	} 

	  	var importoGenerale = 0;
	  	var trascinatoGenerale = 0;
	  	var risorseAttivate = 0;
	  	var risorseDisponibili = 0;
	  	var economiaGenerale = 0;
	  	var pagatoGenerale = 0;

	  	$.each($(livelloMax + ".importo_class"), function(value, key) {
			importoGenerale += parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});
		$.each($(livelloMax + ".trascinato_class"), function(value, key) {
			trascinatoGenerale +=  parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});
		$.each($(livelloMax + ".risAttivate_class"), function(value, key) {
			risorseAttivate +=  parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});
		$.each($(livelloMax + ".risDisponibili_class"), function(value, key) {
			risorseDisponibili +=  parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});
		$.each($(livelloMax + ".economia_class"),function(value, key) {
			economiaGenerale +=  parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});
		$.each($(livelloMax + ".pagato_class"),function(value, key) {
			pagatoGenerale +=  parseFloat(key.innerText.replace(/\./g, '').replace(',','.'));
		});	

		$("#importo_generale")[0].innerText = importoGenerale.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });
		$("#trascinato_generale")[0].innerText	= trascinatoGenerale.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });
		$("#risorseAttivate_attivate")[0].innerText	= risorseAttivate.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });
		$("#risorseDisponibili_generale")[0].innerText	= risorseDisponibili.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });
		$("#economia_generale")[0].innerText	= economiaGenerale.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });
		$("#pagato_generale")[0].innerText	= pagatoGenerale.toLocaleString(['ban', 'id'], { minimumFractionDigits: 2 });

	  }

   function visualizzaStorico(id)
   {
     openPageInPopup("visualizzaStorico_${idPianoFinanziario}_"+id+".do", "popupStorico", "Storico importo", "modal-large");
   }
    function modificaLinea(id)
    {
      openPageInPopup("modifica_${idPianoFinanziario}_"+id+".do", "popupModifica", "Modifica", null);
    }
    $( document ).ready(function() {

    	$('body').on('load-success.bs.table page-change.bs.table', function()
    		    {
    		      ricalcolaTotali();
    		    });
	    
   	 $('#tblPianoFinanziario').bootstrapTable(
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
                    source: '../catalogomisure/getElencoMisureJson.do'
				},
                {
                    field: 'codiceSottomisura',   
                    label: 'Sottomisura',  
                    type: 'ajaxSelect',   
                    source: '../catalogomisure/getElencoCodiciLivelliSottoMisureJson.do'
                },
                {
                    field: 'codiceOperazione',   
                    label: 'Operazione',  
                    type: 'ajaxSelect',   
                    source: '../catalogomisure/getElencoCodiciOperazioneJson.do'
                },
				{
				    field: 'focusarea',    
				    label: 'Focus Area',   
				    type: 'ajaxSelect',   
                    source: '../catalogomisure/getElencoFocusAreaJson.do'
				},
				{
				    field: 'settore',    
				    label: 'Settore',   
				    type: 'ajaxSelect',   
                    source: '../catalogomisure/getElencoSettoriJson.do'
				}
            ],
            connectTo: '#tblPianoFinanziario',
            onResetView: function() {
       	        ricalcolaTotali();
            }
        });

          
    });
    var visualizzaRisultatiFiltri = false;
    $('#tblPianoFinanziario').on('post-body.bs.table', function()
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
    	$('#tblPianoFinanziario tbody tr:not([class^="misura"])').show();
        }

    function toggleHideAll(){
    	$('#tblPianoFinanziario tbody tr:not([class^="misura"])').hide();
        }
    
  </script>
  <script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />