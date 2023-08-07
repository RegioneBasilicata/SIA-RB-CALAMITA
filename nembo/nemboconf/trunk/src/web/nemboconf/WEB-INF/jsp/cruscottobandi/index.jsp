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
					<li class="active">Cruscotto bandi</li>
				</ul>
			</div>
		</div>
	</div>
	
	<form:form action="" modelAttribute="filtroBandiForm" method="post">
	<input type="hidden" id="filtroBandi" value='${filtroAziende.get("elencoBandi")}' >
	
	
	</form:form> 
	  
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<h3>Elenco bandi disponibili</h3>	
		<div id="filter-bar" style="margin-bottom:1em"> </div>
		<table id="elencoBandi" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh "
			  data-toggle="table" 
			  data-url="getElencoBandiCruscotto.json"
			  data-undefined-text = ''
			  data-pagination = true
			  data-only-info-pagination = true
			  data-page-size = 70
			  data-show-pagination-switch="true" 
			  data-page-list ="[10, 25, 50, ,70, 100]"
			  data-pagination-v-align="top" 
			  data-show-columns="true"
			  data-escape-table = "true"
			  >
			  
			<thead>
	        	<tr>
					<th class="center" data-field="azione" data-formatter="azioniFormatter" data-switchable="false" >
						<p:abilitazione dirittoAccessoMinimo="W">
							<a href="configura_nuovo_bando.do" onClick="return configuraNuovoBando();" style="text-decoration: none;"><i class="ico24 ico_add" title="Configura nuovo bando"></i></a>
						</p:abilitazione>
					</th>
	        		<th data-field="procesistente" data-visible="false" data-switchable="false"></th>
	        		<th data-field="haveReport" data-visible="false" data-switchable="false"  ></th>
	        		<th data-field="haveChart" data-visible="false" data-switchable="false" ></th>
	        		<th data-field="idBando" data-visible="false" data-switchable="false">  </th>
	        		<th data-field="denominazione" data-sortable="true">Denominazione<br/>del bando</th>
	        		<th data-field="annoCampagna" data-sortable="true" title="Anno campagna / Anno di riferimento">Anno<br/>di rif.</th>
	        		<th data-field="dataInizioStr" data-sortable="true" data-sorter="dateSorterddmmyyyyHHmmss">Data<br/>apertura</th>
	        		<th data-field="dataFineStr" data-sortable="true" data-sorter="dateSorterddmmyyyyHHmmss"> Data<br/>chiusura</th>
	        		<th data-field="descEventoCalamitoso" data-sortable="true"> Desc. evento<br/>calamitoso</th>
	        		<th data-field="descCatEvento" data-sortable="true"> Categoria<br/>evento</th>
	        		<th data-field="dataEventoStr" data-sortable="true" data-sorter="dateSorterddmmyyyyHHmmss"> Data<br/>evento</th>
	        		<th data-field="descrTipoBando" data-sortable="true">Tipo<br/>bando</th>
	        		<th data-field="elencoCodiciLivelliMisureHtml" data-sortable="false">Operazione</th>	   	
	        		<th data-field="elencoCodiciLivelliMisure" data-visible="false" data-switchable="false" ></th>
	        		<th data-field="elencoCodiciLivelliSottoMisure" data-visible="false" data-switchable="false" ></th>
	        		<th data-field="elencoCodiciOperazione" data-visible="false" data-switchable="false" ></th>
	        		<th data-field="elencoSettori" data-visible="false" data-switchable="false" ></th>
	        		<th data-field="elencoFocusArea" data-visible="false" data-switchable="false" ></th>
					<th data-field="elencoDescrizioniLivelli" data-visible="false" data-switchable="false" ></th>	
					<th data-field="allegati" data-escape-field="false" data-formatter="allegatiFormatter">Elenco<br/>allegati</th>
					<th data-field="amministrazioniCompetenzaHtml" data-visible="false" data-switchable="true" class="ammComp">Organismo delegato (Amm. Competenza)</th>	
					<th data-field="idsAmministrazioniCompetenza" data-visible="false" data-switchable="false"></th>														
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
      	ret = ret.concat('<li><a href=\"download_'+allegati[i].idAllegatiBando+'.do\" title = '+ escapeHTML(allegati[i].nomeFile) +'>'+ escapeHTML(allegati[i].descrizione)+'</a></li><br>');
  }
  ret = ret.concat('</ul>');
  return ret;

}

	$( document ).ready(function() {
		
		$('#elencoBandi').bootstrapTable(
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
					field: 'annoCampagna',    
				    label: 'Anno campagna',   
				    type: 'range'
				},
				{
				    field: 'denominazione',    
				    label: 'Denominazione',   
				    type: 'search'
				},
				
                {
                    field: 'dataInizioStr',   
                    label: 'Data apertura',  
                    type: 'date'   
                },
                {
                    field: 'dataFineStr',   
                    label: 'Data chiusura',  
                    type: 'date'   
                },
                {
                    field: 'dataEventoStr',   
                    label: 'Data evento',  
                    type: 'date'   
                },
				{
				    field: 'descrTipoBando',    
				    label: 'Tipo bando',   
				    type: 'search'
				},
				{
				    field: 'procesistente',    
				    label: 'Situazione Bando',   
				    type: 'select',
				    values: [
				             {id: 'S', label: 'Attivo'},
				             {id: 'N', label: 'Non attivo'}
				         ],
				},
				{
				    field: 'flagTitolaritaRegionale',    
				    label: 'Titolarità regionale',   
				    type: 'select',
				    values: [
				             {id: 'Si', label: 'Si'},
				             {id: 'No', label: 'No'}
				         ],
				},
                {
                    field: 'elencoCodiciLivelliMisure',   
                    label: 'Misura',  
                    type: 'ajaxSelectList',   
                    source: 'getElencoCodiciLivelliMisureJson.do'
                }
                ,
                {
                    field: 'elencoCodiciLivelliSottoMisure',   
                    label: 'Sottomisura',  
                    type: 'ajaxSelectList',   
                    source: 'getElencoCodiciLivelliSottoMisureJson.do'
                },
                {
                    field: 'elencoCodiciOperazione',   
                    label: 'Operazione',  
                    type: 'ajaxSelectList',   
                    source: 'getElencoCodiciOperazioneJson.do'
                },
				{
				    field: 'elencoFocusArea',    
				    label: 'Focus Area',   
				    type: 'ajaxSelectList',   
                    source: 'getElencoFocusAreaJson.do'
				},
				{
				    field: 'elencoSettori',    
				    label: 'Settore',   
				    type: 'ajaxSelectList',   
                    source: 'getElencoSettoriJson.do'
				},
				{
				    field: 'idsAmministrazioniCompetenza',    
				    label: 'Amm. Competenza',   
				    type: 'ajaxSelectList',   
                    source: 'getElencoAmministrazioniCompetenzaJson.do'
				}
          ],
          connectTo: '#elencoBandi',
          onSubmit: function() {

          	var data = $('#filter-bar').bootstrapTableFilter('getData');
              var elabFilter = JSON.stringify(data);
              $.ajax({
              	  type: "POST",
              	  url: '../session/salvaFiltri.do',
              	  data: "key=elencoBandi&filtro="+elabFilter,
              	  success: function( data ){}
              	});
              console.log(data);
          }
      });

		var filterJSON = $('#filtroBandi').val();
		if(filterJSON)
			$('#filter-bar').bootstrapTableFilter("setupFilterFromJSON",filterJSON);
	});

	function azioniFormatter($value, row, index)
    {
      var azioni = [];

      

      //var iconDuplicaBando 	= "<a style=\"font-size: 24px;width: 32px;\" onclick=\"return openPageInPopup('duplicabando_"+row['idBando']+".do','dlgDuplica','Duplica Bando','modal-lg',false)\"   title=\"duplica bando\"class=\"glyphicon glyphicon-paste\" href=\"duplica_"+row['idBando']+".do\"></a>";
     
      var iconDuplicaBando 	= "<a style=\"font-size: 24px;width: 32px;\"   title=\"duplica bando\"class=\"glyphicon glyphicon-paste\" href=\"duplica_"+row['idBando']+".do\"></a>";
      var iconVisualizza 	= "<a href=\"datiidentificativi_"+row['idBando']+".do\" style=\"text-decoration: none;\"><i class=\"ico24 ico_magnify\" title=\"Visualizza bando / Attiva oggetti\"></i></a>";
      var iconModifica 		= "<a href=\"datiidentificativi_"+row['idBando']+".do\" style=\"text-decoration: none;\"><i class=\"icon-list icon-large\" title=\"Modifica bando\"></i></a>";
      var iconGestEconomica = "<a href=\"gesteconomica_"+row['idBando']+".do\" style=\"text-decoration: none;\"><i class=\"ico24 ico_graphic\" title=\"Gestione economica e reportistica\"></i></a>";
      
	  //row['procesistente'] == 'S' --> bando con almeno un oggetto attivo
	  azioni.push("<div style=\"width:130px\">");
      if(row['procesistente'] == 'S')
      {
    	  azioni.push(iconVisualizza);
    	  azioni.push(iconGestEconomica); 
      }
      else
      {
    	  azioni.push(iconModifica);
      }   
      azioni.push(iconDuplicaBando);
      azioni.push("</div>");
      return azioni.join("&nbsp;"); 
    }
	
	function configuraNuovoBando()
    {
        return openPageInPopup('sceltabandomaster.do','dlgConfiguraBando','Configura nuovo bando - scegli tipo misura', 'modal-large');
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