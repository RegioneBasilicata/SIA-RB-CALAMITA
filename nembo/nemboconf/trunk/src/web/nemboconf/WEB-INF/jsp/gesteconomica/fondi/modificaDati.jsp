<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.NavTabsEconomiaTag"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
  <script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>
  	<p:utente/>
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Gestione economica</li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="FONDI" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
	 	<c:if test="${msgErrore != null}">
				<div class="stdMessagePanel" style="margin-top:1em">
					<div class="alert alert-danger">
						<p>
							<strong>Attenzione!</strong><br />
							<c:out value="${msgErrore}" escapeXml="false"></c:out>
						</p>
					</div>
				</div>
				
		</c:if>
		<h4>Tipo importo: ${tipoImporto.descrizione}</h4>
		<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
			<input type="hidden" id="maxRowIndex" name="maxRowIndex" /> 
			<table id="tRisorse" class="table table-hover table-striped table-bordered tableBlueTh">
				<colgroup>
					<col width="4%"/>
					<col width="12%"/>
					<col width="12%"/>
					<col width="12%"/>
					<col width="10%"/>
					<col width="10%"/>
					<col width="10%"/>
					<col width="21%"/>
					<col width="9%"/>
				</colgroup>
				<thead>
					<tr>
						<th><a href="inseriscirisorsa.do" onclick="aggiungiRisorsa();return false;"  style="text-decoration: none;"><i class="ico24 ico_add" title="Inserisci Risorse"></i></a></th>
						<th>Descrizione importo</th>
						<th>Risorse attivate</th>
						<th>Data inizio validit&agrave;</th>
						<th>Data fine validit&agrave;</th>
						<th>Tipo operazione</th>
						<th>Ulteriore specializzazione</th>
						<th>Organismi delegati</th>
						<th>Fondo bloccato</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tipoImporto.risorseAttivateList}" var="b">
						<tr class="nhRow" data-index="${b.pageIndex}">
							<td>
								<c:if test="${b.risorsaEliminabile}">
									<a href="eliminarisorsa_${b.pageIndex}.do" onclick="deleteRisorsa(this);return false;" style="text-decoration: none;"><i class="ico24 ico_trash" title="Elimina Risorsa"></i></a>
								</c:if>
								<input type="hidden" id="risEliminabile_${b.pageIndex}" name="risEliminabile_${b.pageIndex}" value="${b.risorsaEliminabile}">
								<input type="hidden" id="testRow_${b.pageIndex}" name="testRow_${b.pageIndex}" value="OK">
								<input type="hidden" id="idRisorseLivelloBando_${b.pageIndex}" name="idRisorseLivelloBando_${b.pageIndex}" value="${b.idRisorseLivelloBando}">
							</td>
							<td><m:textfield id="descrizione_${b.pageIndex}" name="descrizione_${b.pageIndex}" value="${b.descrizione}" preferRequestValues="${prfRequestValues}"></m:textfield> </td>
							<td><m:textfield id="risAttivate_${b.pageIndex}" name="risAttivate_${b.pageIndex}" value="${b.risorseAttivate}" preferRequestValues="${prfRequestValues}"></m:textfield></td>
							<td><m:textfield type="date" dataType="dataPers" id="dataInizio_${b.pageIndex}" name="dataInizio_${b.pageIndex}" value="${b.dataInizioStr}" preferRequestValues="${prfRequestValues}"></m:textfield></td>
							<td><m:textfield type="date" dataType="dataPers" id="dataFine_${b.pageIndex}" name="dataFine_${b.pageIndex}" value="${b.dataFineStr}" preferRequestValues="${prfRequestValues}"></m:textfield></td>
							<td><m:select viewOptionsTitle="true" viewTitle="true" id="operazione_${b.pageIndex}" list="${elencoOperazioniRisorsa}" textProperty="codice" valueProperty="idLivello" selectedValue="${b.idLivello}" name="operazione_${b.pageIndex}" preferRequestValues="${prfRequestValues}"></m:select> </td>
							<td><m:textfield id="raggruppamento_${b.pageIndex}" name="raggruppamento_${b.pageIndex}" value="${b.raggruppamento}" preferRequestValues="${prfRequestValues}"></m:textfield> </td>
							<td>
								<div class="row">
									<div class="col-md-3">
										<m:checkBox  cssClass="chkAmmCompetenza" name="chkAmmCompetenza_${b.pageIndex}" value="S" checked="${(b.flagAmmCompetenza == 'N') ? 'false' : 'true'}"></m:checkBox>
								    	<div id="folderAmm_${b.pageIndex}">
								    	<c:if test="${b.flagAmmCompetenza != 'S'}">
									    	<a data-toggle="modal" data-target="#amministrazioniModal" href="#" onclick="openPopupAmministrazioni('${b.pageIndex}','${b.idRisorseLivelloBando}');">
									    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
									    	</a>
								    	</c:if>
								    	</div>
									</div>
									<div class="col-md-9">
										<m:static-text id="labelAmmComp_${b.pageIndex}" visible="${b.flagAmmCompetenza == 'S'}">Tutti</m:static-text>
										<m:select visible="${b.flagAmmCompetenza != 'S'}" multiple="true"  readonly="true" viewOptionsTitle="true" cssClass="selectAmmCompetenza" header="false" id="lAmmCompetenza_${b.pageIndex}" list="${b.elencoAmmCompetenza}" valueProperty="idAmmCompetenza" textProperty="descrizioneEstesa"  name="lAmmCompetenza_${b.pageIndex}"></m:select>
									</div>
								</div>
							</td>
							<td><m:checkBox id="fondoBloccato_${b.pageIndex}" value="S" name="fondoBloccato_${b.pageIndex}" checked="${(b.flagBloccato == 'N') ? 'false' : 'true'}" preferRequestValues="${prfRequestValues}"></m:checkBox> </td>
						</tr>
					</c:forEach>
						<tr class="hRow"  style="display:none">
							<td>
								<a href="eliminarisorsa_$$index.do" onclick="deleteRisorsa(this);return false;"  style="text-decoration: none;"><i class="ico24 ico_trash" title="Elimina Risorsa"></i></a>
								<input type="hidden" id="testRow_$$index" name="testRow_$$index" value="OK">
								<input type="hidden" id="idRisorseLivelloBando_$$index" name="idRisorseLivelloBando_$$index" value="0">
								<input type="hidden" id="risEliminabile_$$index" name="risEliminabile_$$index" value="true">
							</td>
							<td><m:textfield id="descrizione_$$index" name="descrizione_$$index" ></m:textfield> </td>
							<td><m:textfield id="risAttivate_$$index" name="risAttivate_$$index" ></m:textfield></td>
							<td><m:textfield type="date" dataType="dataPers" id="dataInizio_$$index" name="dataInizio_$$index" ></m:textfield></td>
							<td><m:textfield type="date" dataType="dataPers" id="dataFine_$$index" name="dataFine_$$index" ></m:textfield></td>
							<td><m:select viewOptionsTitle="true" viewTitle="true" id="operazione_$$index" list="${elencoOperazioniRisorsa}" textProperty="codice" valueProperty="idLivello" name="operazione_$$index"></m:select> </td>
							<td><m:textfield id="raggruppamento_$$index" name="raggruppamento_$$index"></m:textfield> </td>
							<td>
								<div class="row">
									<div class="col-md-3">
										<m:checkBox  cssClass="chkAmmCompetenza" name="chkAmmCompetenza_$$index" value="S" checked="true"></m:checkBox>
								    	<div id="folderAmm_$$index">
								    	</div>
									</div>
									<div class="col-md-9">
										<m:static-text id="labelAmmComp_$$index" visible="true">Tutti</m:static-text>
										<m:select visible="false" list="null" multiple="true" disabled="true" readonly="true" viewOptionsTitle="true" cssClass="selectAmmCompetenza" header="false" id="lAmmCompetenza_$$index"  name="lAmmCompetenza_$$index"></m:select>
									</div>
								</div>	
							</td>
							<td><m:checkBox id="fondoBloccato_$$index" value="S" name="fondoBloccato_$$index"></m:checkBox></td>
						</tr>
				</tbody>
			</table>
			
			<div class="form-group puls-group" style="margin-top: 2em">
				<div class="col-sm-12">
					<button type="button" onclick="forwardToPage('gestfondi.do');"
						class="btn btn-default">indietro</button>
					<button type="submit" name="conferma" id="conferma"  onclick="serializeAmministrazioni()"
						class="btn btn-primary pull-right">conferma</button>
				</div>
			</div>
			
			 <!-- POPUP AMMINISTRAZIONI INI -->
			    <div class="modal fade" id="amministrazioniModal" tabindex="-1" role="dialog" aria-labelledby="amministrazioniModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Scelta organismi delegati</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<div id="dual-list-box1" class="form-group row">
					            <select style="display: none" id="amministrazioniDualList"  multiple="multiple" data-title="Amministrazioni" 
					            	data-source="" data-value="IdAmmCompetenza" 
					            	data-sourceselected=""
					            	data-text="descrizioneEstesa"
					            	data-addcombo="true" data-labelcombo="Tipologia amministrazione" data-labelfilter="Filtra Amministrazione"></select>
					        </div>	                                                                                                                  					
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" id="modalConfirmAmministrazioni" onclick="" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP AMMINISTRAZIONI FINE -->
			
			<!-- POPUP CONFERMA ELIMINA INI -->
			    <div class="modal fade" id="eliminaModal" tabindex="-1" role="dialog" aria-labelledby="eliminaModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Elimina risorsa</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
						  <div class="stdMessagePanel">
						    <div id="messaggio" class="alert alert-warning">
						    	<input type="hidden" name="idRisorsaDaAnnullare" id="idRisorsaDaAnnullare" value="">
						    	<input type="hidden" name="idIndex" id="idIndex" value="">
						      	<p>Sei sicuro di voler eliminare questo elemento?</p>
						    </div>
						  </div>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="confermaElimina();" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP CONFERMA ELIMINA FINE -->
			    
			    <!-- POPUP CONFERMA ELIMINA INI -->
			    <div class="modal fade" id="erroreModal" tabindex="-1" role="dialog" aria-labelledby="erroreModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Elimina risorsa</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
						  <div class="stdMessagePanel">
						    <div id="messaggio" class="alert alert-danger">
						    	Impossibile eliminare la risorsa selezionata.
						    </div>
						  </div>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-right">  
						        <button type="button" data-dismiss="modal" class="btn btn-primary">Chiudi</button>
						      </div>
					    	</div>                                                                                                  				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP CONFERMA ELIMINA FINE -->
		</form:form>	
			
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>

<script type="text/javascript">
$( document ).ready(function() {
	$('#maxRowIndex').val($('.nhRow').last().data('index') + 1);
	//$("input[data-type='dataPers']").datepicker({ dateFormat: 'dd/mm/yy' });
	refreshBindEvent();

	$('body').on('focus',"input[data-type='dataPers']", function(){
	    $(this).datepicker({ dateFormat: 'dd/mm/yy' });
	});
});

function refreshBindEvent()
{
	$('.chkAmmCompetenza').change(function(){
		changeViewOrganismi(this);
	});
}

function changeViewOrganismi(obg)
{
	var pageIndex = $(obg).attr('name').split('_')[1];
	var idRisorseLivelloBando = $('#idRisorseLivelloBando_'+pageIndex).val();
	$('#folderAmm_'+pageIndex).html('');
	$('#lAmmCompetenza_'+pageIndex).html('');
	if(obg.checked){
		$('#labelAmmComp_'+pageIndex).show();
		$('#lAmmCompetenza_'+pageIndex).hide();
	}
	else{
		$('#labelAmmComp_'+pageIndex).hide();
		$('#lAmmCompetenza_'+pageIndex).show();
		$('#folderAmm_'+pageIndex).html('<a data-toggle="modal" data-target="#amministrazioniModal" href="#" onclick="openPopupAmministrazioni(\''+pageIndex+'\',\''+idRisorseLivelloBando+'\');"><span style="text-decoration: none;" class="icon-large icon-folder-open link"></span></a>');
	}	
}

function serializeAmministrazioni()
{
	$('.selectAmmCompetenza').each(function(index) {
		var pageIndex = $(this).attr('name').split('_')[1];
		if(pageIndex != '$$index')
		{
			var htmlSelected = "<select style='display:hidden' multiple id='selectedAmmCompetenza_"+pageIndex+"' name='selectedAmmCompetenza_"+pageIndex+"' >";
	
				$("#"+$(this).attr('id')+" option").each(function(index) {
					htmlSelected = htmlSelected + "<option selected='selected' value='"+$(this).val()+"'>"+$(this).text()+" </option>";
			    });
	
			htmlSelected = htmlSelected + " </select>";
	    	$(this).before(htmlSelected);
		}
    });
}

function serializeAmministrazioniGET()
{ 
	var htmlSelected = "";
	$('.selectAmmCompetenza').each(function(index) {
		var pageIndex = $(this).attr('name').split('_')[1];
		if(pageIndex != '$$index')
		{
				var count = 0;
				$("#"+$(this).attr('id')+" option").each(function(index) {
					if(count == 0)
						htmlSelected = "?selectedAmmCompetenza_"+count+"="+$(this).val();
					else
						htmlSelected = htmlSelected + "&selectedAmmCompetenza_"+count+"="+$(this).val();
						
					count++;
			    });
		}
    });
	return htmlSelected;
}

function filtraAmministrazioni(pageIndex)
{
	//pulisco select principale
	$('#lAmmCompetenza_'+pageIndex).find('option').remove();

	//reperisco value selezionati nella popup
	var arraySelectedValue = [];
    $('#dual-list-box-Amministrazioni #selectedListHidden option').each(function(index) {
    	arraySelectedValue[$(this).val()] = $(this).text();
    });
    
	//popolo select principale
    for (index = 0; index < arraySelectedValue.length; ++index) {
	    if(arraySelectedValue[index] != null)
		 {
	    	$('#lAmmCompetenza_'+pageIndex).append($('<option>', { value: index, text: arraySelectedValue[index] }).attr("title",arraySelectedValue[index]));
		 }
	}
    reorderSelectOptions('#lAmmCompetenza_'+pageIndex);
    //Chiudo popup
    $('#amministrazioniModal').modal('hide');
}

function openPopupAmministrazioni(pageIndex, idRisorseLivelloBando)
{
	resetDualList();
	
	$('#modalConfirmAmministrazioni').attr('onclick',"filtraAmministrazioni('"+pageIndex+"');");
	$('#amministrazioniDualList').data('source','loadAmministrazioniFondiDisponibili.json');
	$('#amministrazioniDualList').data('sourceselected','loadAmministrazioniFondiSelezionate_'+pageIndex+'_'+idRisorseLivelloBando+'.json'+serializeAmministrazioniGET());
	$('#amministrazioniDualList').DualListBox();
}

function resetDualList()
{
	$('#amministrazioniModal .modal-body').html('<div id="dual-list-box1" class="form-group row">'+
													'<select style="display: none" id="amministrazioniDualList"  multiple="multiple" data-title="Amministrazioni" '+ 
														'data-source="" data-value="IdAmmCompetenza"  '+
														'data-text="descrizioneEstesa" data-sourceselected="" '+
														'data-addcombo="true" data-labelcombo="Tipologia amministrazione" data-labelfilter="Filtra Amministrazione"></select>'+
												'</div>');
}

function deleteRisorsa(trashCell)
{
	var index = $(trashCell).closest('tr').data('index');
	var idRisorseLivelloBando = $('#idRisorseLivelloBando_'+index).val();
	if(idRisorseLivelloBando == 0)
	{
		$(trashCell).closest('tr').remove();
	}
	else
	{
		//Eseguo una chiamata ajax per cancellare il record se viene data conferma da parte dell'utente
		$('#idRisorsaDaAnnullare').val(idRisorseLivelloBando);
		$('#idIndex').val(index);
		$('#eliminaModal').modal('show');
	}
	return false;
}

function confermaElimina()
{
	var idRisorseLivelloBando = $('#idRisorsaDaAnnullare').val();
	$.ajax({
        type: "POST",
        url: "confermaeliminarisorsa.do",
        data: "idRisorseLivelloBando="+idRisorseLivelloBando,
        dataType: "text",
        async:false,
        success: function(data) {
        	$('#eliminaModal').modal('hide');
            if(data == 'OK')
            {
        		$("#idRisorseLivelloBando_"+$('#idIndex').val()).closest('tr').remove();
            }
            else
            {
            	$('#erroreModal').modal('show');
            }
        }
    });
}

function aggiungiRisorsa()
{
	var html = $('.hRow').clone().html();
	var indexNew =$('.nhRow').last().data('index') + 1;
	if(indexNew == undefined || indexNew == 'undefined' || isNaN(indexNew)){
		indexNew = 1;
	}
	$('#maxRowIndex').val(indexNew);
	html = html.replace(/\\$\\$index/g, indexNew);
	$('.hRow').before('<tr class="nhRow" data-index="'+indexNew+'">'+html+'</tr>');
	refreshBindEvent();
	return false;
}
</script>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>