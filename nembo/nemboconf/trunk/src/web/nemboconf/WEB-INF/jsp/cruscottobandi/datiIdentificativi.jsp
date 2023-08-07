<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.CruscottoBandiHeader"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
	  
	<p:utente/>
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Inserisci (Dati identificativi) </li>
				</ul>
			</div>
		</div>
	</div>
  <p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="DATI_IDENTIFICATIVI" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	<div class="container-fluid" id="content">
	<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" name="mainForm" method="post" class="form-horizontal" >
				<input  type="hidden" name="amministrazioniHidden" id="amministrazioniHidden" value="">
				<input  type="hidden" name="tipiOpHidden" id="tipiOpHidden" value="">
				<input  type="hidden" name="ambitiTematiciHidden" id="ambitiTematiciHidden" value="">
				
				<br>
				
				<div class="form-group">
				    <label for="denominazione" class="col-sm-2 control-label">Denominazione *</label>
				    <div class="col-sm-10"> 
				    	<b:textarea disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" name="denominazione" >${denominazione}</b:textarea>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="tipologia" class="col-sm-2 control-label">Tipologia</label>
				    <div class="col-sm-10">
					    <b:textfield name=" tipologiaH" value="${tipologia}" disabled="true"></b:textfield>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="dataApertura" class="col-sm-2 control-label">Data apertura *</label>
				    <div class="col-sm-10"> 
				    	<b:textfield name="dataApertura" id="dataApertura" type="DATE" value="${dataApertura}" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}"></b:textfield>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="dataChiusura" class="col-sm-2 control-label">Data scadenza presentazione domande di adesione *</label>
				    <div class="col-sm-10"> 
				    	<b:textfield name="dataChiusura" id="dataChiusura" type="DATE" value="${dataChiusura}" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}"></b:textfield>
				    </div>
				 </div>
				 
				 <c:if test="${isAvversitaAtmosferica=='true' }">
					 <div class="form-group">
					    <label for="idCategoriaEvento" class="col-sm-2 control-label">Categoria Evento Calamitoso</label>
					    <div class="col-sm-10"> 
						    	<b:select name="idCategoriaEvento" id="idCategoriaEvento" list="${listCategoriaEvento}" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" selectedValue="${idCategoriaEvento}"></b:select>
					    </div>
					 </div>
					 
					 <div class="form-group">
					    <label for="idEventoCalamitoso" class="col-sm-2 control-label">Descrizione evento calamitoso</label>
					    <div class="col-sm-10"> 
					    	<b:select name="idEventoCalamitoso" id="idEventoCalamitoso" list="${listEventoCalamitoso}" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" selectedValue="${idEventoCalamitoso}"></b:select>
					    </div>
					 </div>
				 </c:if>
				 
				 
				<div class="form-group">
				    <label for="amministrazione" class="col-sm-2 control-label"> 
				    	Amministrazione (Organismo Delegato) *
				    	<c:if test="${modificaAbilitata && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
					    	<a data-toggle="modal" data-target="#amministrazioniModal" href="#" onclick="openPopupAmministrazioni();">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
				    	</c:if>
					</label>
				    <div class="col-sm-10">
				    	<b:select name="amministrazione" readonly="true"  header="false" id="amministrazione"  size="5" list="${ammCompetenzaAssociate}" valueProperty="idAmmCompetenza" textProperty="descrizioneEstesa" multiple="true" ></b:select>
				    </div>
				 </div>
				<div class="form-group">
				    <label for="annoRif" class="col-sm-2 control-label">Anno campagna / Anno di riferimento *</label>
				    <div class="col-sm-10"> 
				    	<b:textfield name="annoRif" maxlength="4" labelSize="4"  value="${annoRif}" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}"></b:textfield>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="cuaa" class="col-sm-2 control-label">Il bando consente più domande per la stessa Azienda / Beneficiario *</label>
				    <div class="col-sm-7">
				    	<b:radiolist  name="bandoMultiplo" disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" valueProperty="codice" list="${bandoMultiplo}" value="${vBandoMultiplo}" labelProperty="descrizione" ></b:radiolist>
				    </div>
				 </div>
				 
				 
				 <div class="form-group">
				    <label for="referenteBando" class="col-sm-2 control-label">Referente del bando *</label>
				    <div class="col-sm-10"> 
				    	<b:textfield disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" name="referenteBando" value="${referenteBando}"></b:textfield>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="emailReferenteBando" class="col-sm-2 control-label">Email del referente del bando *</label>
				    <div class="col-sm-10"> 
				    	<b:textfield disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" name="emailReferenteBando" value="${emailReferenteBando}"></b:textfield>
				    </div>
				 </div>
				 
					 <div class="form-group">
					 
					    <label for="tipiOperazioni" class="col-sm-2 control-label"> 
					    	Tipi di operazioni previste dal Bando *
					    	<c:if test="${modificaAbilitata && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
						    	<a data-toggle="modal" data-target="#tipiOperazioniModal" href="#" onclick="openPopupTipiOperazioni();">
						    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
						    	</a>
					    	</c:if>
						</label>
					    <div class="col-sm-10"> 
					    	<b:select  name="tipiOperazioni" readonly="true"  header="false" id="tipiOperazioni"  size="5" list="${tipiOperazioneAssociati}" valueProperty="idLivello" textProperty="descrizioneEstesa" multiple="true" ></b:select>
					    </div>
					 </div>		
		 
				 
				 <em>I campi contrassegnati con * sono obbligatori</em>
				 
				 <div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('index.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
							<c:choose>
								<c:when test="${modificaAbilitata}">
									<button type="submit" name="conferma" onclick="prepareSubmit();" id="conferma" class="btn btn-primary pull-right">conferma e prosegui</button>
								</c:when>
								<c:otherwise>
									<c:if test="${isAvversitaAtmosferica=='true' }">
										<button type="button" name="conferma" onclick="window.location='scelta_comuni.do';"  id="conferma" class="btn btn-primary pull-right">prosegui</button>
									</c:if>
									<c:if test="${isAvversitaAtmosferica=='false' }">
										<button type="button" name="conferma" onclick="window.location='attiPubblicati.do';"  id="conferma" class="btn btn-primary pull-right">prosegui</button>
									</c:if>		
								</c:otherwise>
							</c:choose>
						</p:abilitazione>
						<c:if test="${utenteAbilitazioni.dirittoAccessoPrincipale=='R'}">
							<button type="button" name="conferma" onclick="window.location='filtrobeneficiari.do';"  id="conferma" class="btn btn-primary pull-right">prosegui</button>
						</c:if>
					</div>
				 </div>
				
				 <!-- POPUP AMMINISTRAZIONI INI -->
			    <div class="modal fade" id="amministrazioniModal" tabindex="-1" role="dialog" aria-labelledby="amministrazioniModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 1000px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Amministrazioni previste per il bando</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<div id="dual-list-box1" class="form-group row">
					            <select style="display: none" id="amministrazioniDualList"  multiple="multiple" data-title="Amministrazioni" 
					            	data-source="loadAmministrazioniDisponibili_${idBando}.json" data-value="IdAmmCompetenza" 
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
						        <button type="button" onclick="filtraAmministrazioni();" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP AMMINISTRAZIONI FINE -->
				
				 <!-- POPUP TIPI OPERAZIONE INI -->
			    <div class="modal fade" id="tipiOperazioniModal" tabindex="-1" role="dialog" aria-labelledby="tipiOperazioniModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Tipi di operazioni previste dal Bando</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<div id="dual-list-box2" class="form-group row">
					            <select style="display: none" id="tipiOperazioniDualList"  multiple="multiple" data-title="Elementi" 
					            	data-sort-text="false" data-source="loadTipiOperazioniDisponibili_${idBando}.json" data-value="idLivello" data-text="descrizioneEstesa"></select>
					        </div>	                                                                                                                  					
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="filtraTipiOperazioni();" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP TIPI OPERAZIONE FINE -->
			    
			    <!-- POPUP AMBITO TEMATICO INI -->
			    <div class="modal fade" id="ambitiTematiciModal" tabindex="-1" role="dialog" aria-labelledby="ambitiTematiciModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Ambiti tematici - Operazioni previsti dal Bando</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<div id="dual-list-box3" class="form-group row">
					            <select style="display: none" id="ambitiTematiciDualList"  multiple="multiple" data-title="AmbitiTematici"  data-horizontal="true"
					            	data-sort-text="false" data-source="loadAmbitiTematiciDisponibili_${idBando}.json" data-value="idAmbitoTematico" data-text="descrizioneEstesa"></select>
					        </div>	                                                                                                                  					
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="filtraAmbitiTematici();" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP AMBITO TEMATICO FINE -->
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
	$( document ).ready(function() {
		$("input[name='dataApertura']").datepicker({ dateFormat: 'dd/mm/yy 00:00:00' });
		$("input[name='dataChiusura']").datepicker({ dateFormat: 'dd/mm/yy 23:59:59' });
		$("input[name='dataEvento']").datepicker({ dateFormat: 'dd/mm/yy 00:00:00' });
		
		$('#idCategoriaEvento').change(function(){
			loadIdEventoCalamitoso();
		});

	});
	
	function openPopupAmministrazioni()
	{
		resetDualList();
		$('#amministrazioniDualList').DualListBox();
		$("#dual-list-box-Amministrazioni #selectedList").html(' ');
		$('#dual-list-box-Amministrazioni #selectedListHidden').html(' ');
		var ser = serializeSelectField('amministrazione');
		$('#amministrazione option').remove();
		
		$.getJSON('loadAmministrazioniSelezionate_'+${idBando}+'.json',ser,  function(json) {
			setTimeout(function() {
				$.each(json, function(key, item) {
	                $('<option>', {
	                    value: item['IdAmmCompetenza'],
	                    text: item['descrizioneEstesa']
	                }).attr("data-group",item['gruppo']).appendTo($('#dual-list-box-Amministrazioni #selectedList'));
	                
	                $('<option>', {
	                    value: item['IdAmmCompetenza'],
	                    text: item['descrizioneEstesa']
	                }).attr("data-group",item['gruppo']).appendTo($('#dual-list-box-Amministrazioni #selectedListHidden'));

	                $('<option>', {
	                    value: item['IdAmmCompetenza'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#amministrazione'));
	            });
	            
				var options = [];

	            $("#dual-list-box-Amministrazioni #selectedList").find('option').each(function() {
	                options.push({value: $(this).val(), text: $(this).text()});
	            });

	            $("#dual-list-box-Amministrazioni #selectedList").data('options', options);
	            $("#dual-list-box-Amministrazioni .selected-count").text(options.length);
	            
	            //rimuovo dall'elenco di sx quelle presenti nella select principale
				$('#amministrazione option').each(function() {
					$('#dual-list-box-Amministrazioni #unselectedList option[value="'+$(this).val()+'"]').remove();
					$('#dual-list-box-Amministrazioni #unselectedListHidden option[value="'+$(this).val()+'"]').remove();
				});
	            
	            reorderSelectOptions('#dual-list-box-Amministrazioni #selectedList');

	            
			}, 1000);
		});
		$('.stdMessageLoad').hide();
	}
	
	function openPopupTipiOperazioni()
	{
		resetDualList();
		$('#tipiOperazioniDualList').DualListBox();
		$("#dual-list-box-Elementi #selectedList").html(' ');
		$('#dual-list-box-Elementi #selectedListHidden').html(' ');
		var ser = serializeSelectField('tipiOperazioni');
		$('#tipiOperazioni option').remove();
		
		$.getJSON('loadTipiOperazioniSelezionati_'+${idBando}+'.json',ser,  function(json) {
			setTimeout(function() {
				$.each(json, function(key, item) {
	                $('<option>', {
	                    value: item['idLivello'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#dual-list-box-Elementi #selectedList'));
	                
	                $('<option>', {
	                    value: item['idLivello'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#dual-list-box-Elementi #selectedListHidden'));

	                $('<option>', {
	                    value: item['IdAmbitoTematico'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#tipiOperazioni'));
	            });
	            
				var options = [];

	            $("#dual-list-box-Elementi #selectedList").find('option').each(function() {
	                options.push({value: $(this).val(), text: $(this).text()});
	            });

	            $("#dual-list-box-Elementi #selectedList").data('options', options);
	            $("#dual-list-box-Elementi .selected-count").text(options.length);
	            
	            //rimuovo dall'elenco di sx quelle presenti nella select principale
				$('#tipiOperazioni option').each(function() {
					$('#dual-list-box-Elementi #unselectedList option[value="'+$(this).val()+'"]').remove();
					$('#dual-list-box-Elementi #unselectedListHidden option[value="'+$(this).val()+'"]').remove();
				});
	            
	            //reorderSelectOptions('selectedList');
	            
			}, 1000);
		});
		$('.stdMessageLoad').hide();
	}

	function openPopupAmbitiTematici()
	{
		resetDualList();
		$('#ambitiTematiciDualList').DualListBox();
		$("#dual-list-box-AmbitiTematici #selectedList").html(' ');
		$('#dual-list-box-AmbitiTematici #selectedListHidden').html(' ');
		var ser = serializeSelectField('ambitiTematici');
		$('#ambitiTematici option').remove();
		$.getJSON('loadAmbitiTematiciSelezionati_'+${idBando}+'.json',ser,  function(json) {
			setTimeout(function() {
				$.each(json, function(key, item) {
	                $('<option>', {
	                    value: item['IdAmbitoTematico'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#dual-list-box-AmbitiTematici #selectedList'));
	                
	                $('<option>', {
	                    value: item['IdAmbitoTematico'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#dual-list-box-AmbitiTematici #selectedListHidden'));

	                $('<option>', {
	                    value: item['IdAmbitoTematico'],
	                    text: item['descrizioneEstesa']
	                }).appendTo($('#ambitiTematici'));
	            });
	            
				var options = [];

	            $("#dual-list-box-AmbitiTematici #selectedList").find('option').each(function() {
	                options.push({value: $(this).val(), text: $(this).text()});
	            });

	            $("#dual-list-box-AmbitiTematici #selectedList").data('options', options);
	            $("#dual-list-box-AmbitiTematici .selected-count").text(options.length);
	            
	            //rimuovo dall'elenco di sx quelle presenti nella select principale
				$('#ambitiTematici option').each(function() {
					$('#dual-list-box-AmbitiTematici #unselectedList option[value="'+$(this).val()+'"]').remove();
					$('#dual-list-box-AmbitiTematici #unselectedListHidden option[value="'+$(this).val()+'"]').remove();
				});
				var xxx = $('#ambitiTematici option');
	            if($('#ambitiTematici option').length==0)
		            {
		            	$("#dual-list-box-AmbitiTematici #selectedList").html(' ');
		        		$('#dual-list-box-AmbitiTematici #selectedListHidden').html(' ');
		            }
	            
	            //reorderSelectOptions('selectedList');	
	            
			}, 1000);
		});
		
		$('.stdMessageLoad').hide();
	}
	
	function filtraAmministrazioni()
	{
		//pulisco select principale
		$('#amministrazione').find('option').remove();

		//reperisco value selezionati nella popup
		var arraySelectedValue = [];
	    $('#dual-list-box-Amministrazioni #selectedListHidden option').each(function(index) {
	    	arraySelectedValue[$(this).val()] = $(this).text();
	    });
	    
		//popolo select principale
	    for (index = 0; index < arraySelectedValue.length; ++index) {
		    if(arraySelectedValue[index] != null)
			 {
		    	$('#amministrazione').append($('<option>', { value: index, text: arraySelectedValue[index] }));
			 }
		}
	    reorderSelectOptions('#amministrazione');
	
	    $.ajax({
			  type: 'GET',
			  url: 'salvaAmministrazione_'+getSelectValueString('amministrazione')+'.do',
			  success: function(data, textStatus) {
		      },
			  async:false
			});


        $('#ambitiTematiciHidden').val("");
        $("#ambitiTematici option").remove();

	    //Chiudo popup
	    $('#amministrazioniModal').modal('hide');
	}
	
	function prepareSubmit()
	{
		$('#amministrazioniHidden').val(getSelectValueString('amministrazione'));
		$('#tipiOpHidden').val(getSelectValueString('tipiOperazioni'));
	}
	
	function filtraTipiOperazioni()
	{
		//pulisco select principale
		$('#tipiOperazioni').find('option').remove();

		//reperisco value selezionati nella popup
		var arraySelectedValue = [];
	    $('#dual-list-box-Elementi #selectedListHidden option').each(function(index) {
	    	arraySelectedValue[$(this).val()] = $(this).text();
	    });
	    
		//popolo select principale
	    for (index = 0; index < arraySelectedValue.length; ++index) {
		    if(arraySelectedValue[index] != null)
			 {
		    	$('#tipiOperazioni').append($('<option>', { value: index, text: arraySelectedValue[index] }));
			 }
		}

		var operazioni = getSelectValueString('tipiOperazioni');
		var ids = []; 
		var j=0;
		if(operazioni!=null && operazioni!="")
		   {
			   var s = operazioni.split("=");
			   for(var i =0; i<s.length;i++)			   
			   {
				   var id = s[i].split("&")[1];
				   ids[j]=id;
				   j++;
			   }
		   }
		
		//Chiudo popup
	    $('#tipiOperazioniModal').modal('hide');
	}

	function filtraAmbitiTematici()
	{
		//pulisco select principale
		$('#ambitiTematici').find('option').remove();

		//reperisco value selezionati nella popup
		var arraySelectedValue = [];
		var ids = [];
		var i =0; 
	    $('#dual-list-box-AmbitiTematici #selectedListHidden option').each(function(index) {
	    	arraySelectedValue[$(this).val()] = $(this).text();
	    	ids[i++]=$(this).val();
	    });
	    
		//popolo select principale
	    for (index = 0; index < ids.length; ++index) {
		    if(arraySelectedValue[ids[index]] != null)
			 {
		    	$('#ambitiTematici').append($('<option>', { value: ids[index], text: arraySelectedValue[ids[index]] }));
			 }
		}

		//Chiudo popup
	    $('#ambitiTematiciModal').modal('hide');
	}
	
	function resetDualList()
	{
		//Funzione necessaria per eseguire il reset delle dual list e cancellare eventuali dual list presenti. IN questo modo viene poi ricreata solo quella che serve e le funzioni interne al 
		//widget non vanno a scrivere sull'eventuale altra list che trova nel DOM
		
		$('#tipiOperazioniModal .modal-body').html('<div id="dual-list-box2" class="form-group row"> '+
												     '<select style="display: none" id="tipiOperazioniDualList"  multiple="multiple" data-title="Elementi"'+ 
					                                      'data-sorttext="false" data-source="loadTipiOperazioniDisponibili_${idBando}.json" data-value="idLivello" data-text="descrizioneEstesa"></select>'+
			                                       '</div>	');
		
		$('#amministrazioniModal .modal-body').html('<div id="dual-list-box1" class="form-group row">'+
														'<select style="display: none" id="amministrazioniDualList"  multiple="multiple" data-title="Amministrazioni" '+ 
															'data-source="loadAmministrazioniDisponibili_${idBando}.json" data-value="IdAmmCompetenza"  '+
															'data-text="descrizioneEstesa" '+
															'data-addcombo="true" data-labelcombo="Tipologia amministrazione" data-labelfilter="Filtra Amministrazione"></select>'+
													'</div>');

		$('#ambitiTematiciModal .modal-body').html('<div id="dual-list-box3" class="form-group row"> '+
			     '<select style="display: none" id="ambitiTematiciDualList"  multiple="multiple" data-title="AmbitiTematici" data-horizontal="true" data-toggle="true"'+ 
                     'data-sorttext="false" data-source="loadAmbitiTematiciDisponibili_${idBando}.json" data-value="IdAmbitoTematico" data-text="descrizioneEstesa"></select>'+
              '</div>	');
	}
	
	
	function loadIdEventoCalamitoso()
	{
		var idCategoriaEvento = $('#idCategoriaEvento').val();
		var option = '<option value=":ID">:DESC</option>';
		if(idCategoriaEvento != null && idCategoriaEvento !=='')
		{
			$.ajax(
		    {
		      type : "GET",
		      url : "load_eventi_calamitosi_" + idCategoriaEvento + ".do",
		      dataType : "json",
		      async : false,
		      success : function(data)
		      {
				 clearIdEventoCalamitoso();
				 $.each(data, function(key,value){
 					 	//console.log('id: ' + value.id);
					 	//console.log('desc: ' + value.descrizione);
					 	var myOption = option;
					 	myOption = myOption.replace(":ID",value.id);
					 	myOption = myOption.replace(":DESC",value.descrizione);
					 	$('#idEventoCalamitoso').append(myOption);
					 	console.log(myOption);
					 	
				 });
		      },
		      fail : function(jqXHR, textStatus)
		      {
		        alert('Si è verificato un errore di sistema nell\'aggiornamento dei dati');
		      }
		    });
		}
		else
		{
			clearIdEventoCalamitoso();
		}
	}
	
	function clearIdEventoCalamitoso()
	{
		$('#idEventoCalamitoso').find('option').remove().end().append('<option value="">-- selezionare --</option>').val('');
	}
</script>		
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>