<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.NavTabsConfigurazioneCataloghiTag"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
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
	
	<p:NavTabsConfigurazioneCataloghi activeTab="<%=NavTabsConfigurazioneCataloghiTag.TABS.QUADRIOGGETTI.toString()%>"></p:NavTabsConfigurazioneCataloghi>
	
	<div class="container-fluid"  id="content">
	<b:panel type="DEFAULT" cssClass="navpanel">
			<b:error />
			
			<form:form action="" modelAttribute="" id="mainForm" name="mainForm" method="post" class="form-horizontal" >
				<div id="filter-bar" style="margin-bottom:1em"> </div>
				<table id="elencoBandi" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh "
					  data-toggle="table" 
					  data-url="getElencoOggetti.json"
					  data-undefined-text = ''
					  data-pagination = true
					  data-only-info-pagination = true
					  data-show-pagination-switch="true" 
					  data-pagination-v-align="top" 
					  data-show-columns="true"
					  >
					<thead>
			        	<tr>
			        		
			        		<th data-field="id_oggetto" data-formatter="gestisciOggettoFormatter" data-switchable="false"  ></th>
			        		<th data-field="codOggetto"  >Codice Oggetto</th>
			        		<th data-field="descrOggetto"  >Descrizione Oggetto</th>
			        		<th data-field="flagIstanza"  >Flag Istanza</th>
			        		
			        	</tr>
				    </thead>
				</table>
				<br><i>N.b: Seleziona il singolo oggetto per poterne gestire l'abbinamento con i quadri disponibili</i>
	    	</form:form>
	    </b:panel>
	</div>
		
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>

<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>	
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script>

$( document ).ready(function() {
	$('body').on('focus',"input[data-type='campodata']", function(){
	    $(this).datepicker({ dateFormat: 'dd/mm/yy' });
	});
});

function gestisciOggettoFormatter($value, row, index) 
{
  var azioni = [];
  var iconGestisci	= "<a href=\"gestisci_oggetto_"+row['idOggetto']+".do\" onClick=\"openPageInPopup('gestisci_oggetto_"+row['idOggetto']+".do','dlgParametri','Gestisci Oggetto',  'modal-large');return false;\" style=\"text-decoration: none;\"><i class=\"ico24 ico_add\" title=\"Inserisci o elimina relazioni oggetto-quadro\"></i></a>";
  var iconModify	= "<a href=\"visualizza_oggetto_"+row['idOggetto']+".do\" onClick=\"openPageInPopup('visualizza_oggetto_"+row['idOggetto']+".do','dlgParametri','Gestisci Oggetto',  'modal-large');return false;\" style=\"text-decoration: none;\"><i class=\"ico24 ico_magnify\" title=\"Visualizza relazioni oggetto-quadro\"></i></a>";
  azioni.push(iconGestisci);
  azioni.push(iconModify);
  return azioni.join("&nbsp;"); 
}

$('#dettEstrazioneTable').on("post-body.bs.table", function (data) {
	$('.fixed-table-loading').remove();
	$('#dettEstrazioneCnt').show();
	$('#pleaseWait').hide();
	
});

function serializzaConfermaSelectedList(idOggetto)
{
	//prelevo i codici istat dei comuni selezionati nella select e popolo la tabella
	var ser = '';
	$('#selectedList option').each(function(index) {
    	ser = ser + '&selectedList=' + $(this).val();
    });
	if($('#selectedList').find('option').length > 4000)
	{
		alert("Hai superato il limite massimo di 4000 elementi!");
		return;
	}
	$.ajax({
		  type: 'POST',
		  url: 'gestisci_oggetto_'+idOggetto+'.do',
		  data: ser,
		  success: function(data, textStatus) {
	    	  $('#dlgParametri').modal('hide');
	      },
		  async:false
		});
}

function initMyDualList()
{
	$('#oggQuadroDualList').DualListBox();
}

function modificaOrdine(idOggetto, idAllegatoPartenza, idAllegatoArrivo)
{
	$('.tdModificaOrdine').html("<div align=\"center\" title=\"Attendere prego...\"><span class=\"ico32 ico_pleasewait\" style=\"vertical-align: middle\"></span></div>");
	$.ajax({
		type : "GET",
		url : 'modificaOrdineQuadri_'+idOggetto+'_'+idAllegatoPartenza+'_'+idAllegatoArrivo+'.do',
		dataType : "html",
		async : false,
		success : function(data, textStatus) {
			$('#dlgParametri .modal-body').html(data);
		}
	});
}

</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>