<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css"> 
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">
<style>
.ammComp {width: 20em;}
</style>
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
	  
	<p:utente/>
	
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li class="active">Gestione eventi</li>
				</ul>
			</div>
		</div>
	</div>
	
	<form:form action="" modelAttribute="filtroBandiForm" method="post">
	<input type="hidden" id="filtroBandi" value='${filtroAziende.get("elencoBandi")}' >
	
	
	</form:form> 
	  
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<h3>Elenco eventi calamitosi</h3>	
		<div id="filter-bar" style="margin-bottom:1em"> </div>
		<table id="elencoEventi" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh "
			  data-toggle="table" 
			  data-url="get_list_eventi_disponibili.do"
			  data-undefined-text = ''
			  data-pagination = "true"
			  data-only-info-pagination = "true"
			  data-page-size = "70"
			  data-show-pagination-switch="true" 
			  data-page-list ="[10, 25, 50, ,70, 100]"
			  data-pagination-v-align="top" 
			  data-show-columns="true"
			  >
			  
			<thead>
	        	<tr>
					<th class="center col-md-1" data-field="idEventoCalamitoso" data-formatter="azioniFormatter" data-switchable="false" >
						<p:abilitazione dirittoAccessoMinimo="W">
							<a href="inserisci.do"><i class="ico24 ico_add" title="Nuovo evento calamitoso"></i></a>
						</p:abilitazione>
					</th>
	        		<th class="col-md-5" data-field="descEvento" data-visible="true" data-switchable="false" data-sortable="true" >Descrizione<br/>evento</th>
	        		<th class="col-md-2" data-field="dataEventoStr" data-visible="true" data-switchable="false" data-sortable="true" data-sorter="dateSorterddmmyyyy">Data<br/>Evento</th>
	        		<th class="col-md-4" data-field="descCategoriaEvento" data-visible="true" data-switchable="false" data-sortable="true">Categoria<br/>Evento</th>
	        	</tr>
		    </thead>
		</table>
		<form><input type="hidden" value="0" id="numberOfDateFilter"></form>
		
	</div>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script type="text/javascript">

function allegatiFormatter($value, row, index)
{
  var allegati = row['allegati'];
  
  var ret='<ul style=\"padding-left: 2em;\">';
  var i =0;

  if(allegati!=null)
  {
      for (i=0;i<allegati.length;i++)
      	ret = ret.concat('<li><a href=\"download_'+allegati[i].idAllegatiBando+'.do\" title = '+allegati[i].descrizione+'>'+allegati[i].nomeFile+'</a></li><br>');
  }
  ret = ret.concat('</ul>');
  return ret;

}

	$( document ).ready(function() {
		
		$('#elencoEventi').bootstrapTable();
		var filterJSON = $('#filtroBandi').val();
		if(filterJSON)
			$('#filter-bar').bootstrapTableFilter("setupFilterFromJSON",filterJSON);
	
		if("${showErroreBandi}" != null && "${showErroreBandi}" !='')
		{
			openPageInPopup('elimina_' + "${idEventoCalamitoso}" + '.do','dlgMessaggio','Eliminazione evento calamitoso','modal-lg',false) 
		}
	});

	function azioniFormatter($value, row, index)
    {
      var azioni = [];
      var iconVisualizza 	= "<a href=\"datiidentificativi_"+row['idBando']+".do\" style=\"text-decoration: none;\"><i class=\"ico24 ico_magnify\" title=\"Visualizza bando / Attiva oggetti\"></i></a>";
      var iconModifica 		= "<a href=\"modifica_"+row['idEventoCalamitoso']+".do\" style=\"text-decoration: none;\"><i class=\"icon-list icon-large\" title=\"Modifica evento calamitoso\"></i></a>";
      var iconElimina 		= "<a href=\"elimina_"+row['idEventoCalamitoso']+".do\" onclick=\"return openPageInPopup('elimina_" + row['idEventoCalamitoso'] + ".do','dlgMessaggio','Eliminazione evento calamitoso','modal-lg',false) \"><i class=\"ico24 ico_trash\" title=\"Elimina evento calamitoso\"></i></a>";
      
	  azioni.push("<div style=\"width:130px\">");
      azioni.push(iconModifica);
   	  azioni.push(iconElimina);
      azioni.push("</div>");
      return azioni.join("&nbsp;"); 
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