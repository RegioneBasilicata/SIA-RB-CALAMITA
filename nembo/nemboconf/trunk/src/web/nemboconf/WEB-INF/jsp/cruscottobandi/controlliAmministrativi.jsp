<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
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
					<li class="active">Inserisci (Controlli Tecnico Amministrativi) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="CONTROLLI_TECNICI" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">

		<b:error />
				 <c:if test="${msgErrore != null}">
	             	<div class="stdMessagePanel">	
	                         <div class="alert alert-danger ">
	                             <p><strong>Attenzione!</strong><br>
	                            <c:out value="${msgErrore}"></c:out>
	                         </div>
	                     </div>
	             </c:if>
	        <form action="visualizzaControlliAmm.do" id="visualizzaControlliAmmForm" method="POST"><input type="hidden" value="${idQuadroOggetto}" id="idQuadroOggettoH" name="idQuadroOggetto"><input type="hidden" value="${idQuadroOggettoEBandoOggetto}" id="idQuadroOggettoEBandoOggettoH" name="idQuadroOggettoEBandoOggetto"></form>     
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<input type="hidden" value="${idQuadroOggetto}" id="idQuadroOggetto" name="idQuadroOggetto">
				<input type="hidden" value="${idQuadroOggettoEBandoOggetto}" id="idQuadroOggettoEBandoOggetto" name="idQuadroOggettoEBandoOggetto">		
				<div style="margin-left:2em;margin-top:1em">
					<div class="form-group">
					    <label for="descrGruppoSelezionato" class="col-sm-4 control-label">
					    	Selezionare il gruppo oggetto / istanza da configurare
				    	   	<a data-toggle="modal" data-target="#gruppiModal" href="#">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
					    </label>
					    <div class="col-sm-8"> 
					    	<div class="well well-sm"><p id="descrGruppoSelezionato"><c:out value="${descrGruppoContrSelezionato}"></c:out></p></div>
					    </div>
				 	</div>
				</div>
				
				<c:if test="${elencoControlliTecnici == null && ricercaeffettuata}">
					<div class="stdMessagePanel">	
	                         <div class="alert alert-warning">
	                             <p><strong>Attenzione!</strong><br>
	                            Non è stato trovato nessun Controllo Tecnico Amministrativo configurato per questo oggetto/istanza
	                         </div>
	                     </div>
				</c:if>
				
				<c:if test="${elencoControlliTecnici != null}">
				<c:set var="rowIndex" value="0"></c:set>
				<input type="hidden" name="indexNewDetail" id="indexNewDetail" value="-1"/>
				<table summary="Elenco Interventi" class="table table-hover table-bordered table-condensed tableBlueTh" data-show-columns="true">
					<colgroup>
						<col width="3%">
						<col width="2%">
						<col width="10%">
						<col width="45%">
						<col width="40%">
					</colgroup>
					<thead>
						<tr>
							<th class="center"> 
								<m:checkBox name="chkSelectAllControlli" id="chkSelectAllControlli" value="" onclick="selectAll(this,'idQuadroOggControlloAmm')" ></m:checkBox>
							</th>
							<th></th>
							<th class="center">Codice</th>
							<th class="center">Descrizione</th>
							<th class="center">Misure</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${elencoControlliTecnici}" var="c">
							<c:set var="rowIndex" value="${rowIndex + 1}"></c:set>
							<tr class="trseparator"><td colspan="6" style="height:2em"></td></tr>
							<c:set var="rowIndex" value="-1"></c:set>
							<tr>
								<td class="center">
									<m:checkBox name="idQuadroOggControlloAmm" disabled="${c.flagAttivo == 'S'}"  value="${c.idQuadroOggControlloAmm}" checked="false" onclick="selectAllByClass(this,'idControlloPadre_${c.idControlloAmministrativo}')"></m:checkBox>
								</td>
								<td class="center"> 
									<c:if test="${(bandoOggettoAttivo==null || bandoOggettoAttivo=='N')}">
										<a style="margin-left:1em" id="add_${c.idQuadroOggControlloAmm}" onclick="addNewControllo(this, '${c.idControlloAmministrativo}');" href="#add_${c.idQuadroOggControlloAmm}" class="ico16 ico_add"></a>
									</c:if>
								</td>
								<td colspan="3" style="background-color:#DDD"><strong>${c.descrizione}</strong></td>
							</tr>
							<c:forEach items="${c.controlliFigli}" var="f">
								<c:choose>
									<c:when test="${f.idQuadroOggControlloAmm < 0}">
										<c:set var="rowIndex" value="${rowIndex + 1}"></c:set>
										<tr  class="rowIndex_${rowIndex}">
											<td class="center">
												<input type="hidden" name="idQuadroOggControlloAmm" value="${f.idQuadroOggControlloAmm}&&${f.idControlloAmministratPadre}">
												<m:checkBox name="idQuadroOggControlloAmm"   cssClass="idControlloPadre_${f.idControlloAmministratPadre}" value="${f.idQuadroOggControlloAmm}&&${f.idControlloAmministratPadre}" disabled="true" checked="true" readonly="true"></m:checkBox>
											</td>
											<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_controllo.do"></a></td>
											<td><m:textfield id="codice_${f.idQuadroOggControlloAmm}" name="codice_${f.idQuadroOggControlloAmm}" value="${f.codice}" maxlength="20" preferRequestValues="${preferRqParam}"></m:textfield> </td>
											<td><m:textarea name="descrizione_${f.idQuadroOggControlloAmm}" id="descrizione_${f.idQuadroOggControlloAmm}" rows="2"  preferRequestValues="${preferRqParam}">${f.descrizione}</m:textarea> </td>
											<td>
												
											</td>
										</tr> 
									</c:when>
									<c:otherwise>
										<c:set var="rowIndex" value="${rowIndex + 1}"></c:set>
										<tr  class="rowIndex_${rowIndex}">
											<td class="center">
												<m:checkBox name="idQuadroOggControlloAmm"  cssClass="idControlloPadre_${f.idControlloAmministratPadre}" value="${f.idQuadroOggControlloAmm}&&${f.idControlloAmministratPadre}" checked="${f.flagSelezionato == 'S'}" readonly="${f.flagObbligatorio == 'S'}" disabled="${f.flagObbligatorio == 'S' || f.flagAttivo == 'S'}"></m:checkBox>
											</td>
											<td></td>
											<td>${f.codice}</td>
											<td>${f.descrizione}</td>
											<td>
												<div class="form-group">
													<div class="col-sm-9">
														<m:select id="livelli_${f.idQuadroOggControlloAmm}&&${f.idControlloAmministratPadre}" 
														list="${f.elencoLivelli}" 
														name="livelli_${f.idQuadroOggControlloAmm}&&${f.idControlloAmministratPadre}" multiple="true" header="false" 
														preferRequestValues="${preferRqParam}"
														textProperty="codiceDescrizione"
														valueProperty="idLivello"
														disabled="true"
														readonly="true"
														viewOptionsTitle="true"
														></m:select>
													</div>
													<div class="col-sm-3">
														<c:if test="${(bandoOggettoAttivo==null || bandoOggettoAttivo=='N') && (f.flagSelezionato == 'S')}">
															<a title="Gestisci Misure" onclick="openPageInPopup('gestione_livelli_${f.idBandoOggetto}_${f.idQuadroOggControlloAmm}.do','dlgLivelli','Abbina Misure',  'modal-large');return false;" data-toggle="modal" data-target="#livelliModal" href="#" >
														    	<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
														    </a>
													    </c:if>
											    	</div>
											    </div>
											</td>
										</tr> 
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>
						<tr class="trseparator"><td colspan="5" style="height:2em"></td></tr>
						<tr style="display: none" class="hrow">
							<td class="center">
								<input type="hidden" name="idQuadroOggControlloAmm" value="$$idUnivoco">
								<m:checkBox name="idQuadroOggControlloAmm" cssClass="idControlloPadre_$$idcontrollopadre" value="$$idUnivoco" disabled="true" checked="true" readonly="true"></m:checkBox>
							</td>
							<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_controllo.do"></a></td>
							<td><m:textfield id="codice_$$index" name="codice_$$index" maxlength="20" preferRequestValues="${preferRqParam}"></m:textfield> </td>
							<td><m:textarea name="descrizione_$$index" id="descrizione_$$index" rows="2"  preferRequestValues="${preferRqParam}"></m:textarea> </td>
						</tr>
					</tbody>
				</table>
				
				</c:if>
							 		    
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('quadri.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
							<button type="button" onclick="forwardToPage('grafici.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
							<c:if test="${elencoControlliTecnici != null && (bandoOggettoAttivo==null || bandoOggettoAttivo=='N')}">
								<button type="button" onclick="submitConferma();"name="conferma" id="conferma" class="btn btn-primary pull-right submitBtn">conferma</button>
							</c:if>
						</p:abilitazione>
					</div>
				</div>
				
				
				<!-- POPUP SCELTA GRUPPO INI -->
			    <div class="modal fade" id="gruppiModal" tabindex="-1" role="dialog" aria-labelledby="gruppiModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Selezionare il gruppo oggetto / istanza da configurare</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
								<m:radio-list name="gruppiDisponibili" id="gruppiDisponibili" list="${gruppiDisponibili}" textProperty="descrizioneEstesa" valueProperty="id"></m:radio-list>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      
							      <div class="pull-right">  
							        <button type="button" onclick="confermaGruppo();"  class="btn btn-primary">Conferma</button>
							      </div>
						      
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP SCELTA GRUPPO FINE -->
			    
				
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
function confermaGruppo()
{
	var scelta = $('input[name=gruppiDisponibili]:checked').val();
	if(!scelta)
	{
		return;
	}
	
	$('#descrGruppoSelezionato').html($('input[name=gruppiDisponibili]:checked').closest('label').text());
	$('#idQuadroOggettoEBandoOggetto').val(scelta);
	$('#gruppiModal').modal('hide');
	$('#mainForm').attr('action','visualizzaControlliAmm.do').submit();
}

function selectAll(self, name)
{
  $("input[name='"+name+"']:not(:disabled)").prop("checked", $(self).prop('checked'));
}

function addNewControllo(self, idcontrollopadre)
{
	var indexNew = Number($('#indexNewDetail').val()) - 1;
	$('#indexNewDetail').val(indexNew);
	var hrow = $('.hrow').clone().html();
	hrow = hrow.replace(/\\$\\$index/g, indexNew);
	hrow = hrow.replace(/\\$\\$idcontrollopadre/g, idcontrollopadre);
	hrow = hrow.replace(/\\$\\$idUnivoco/g, indexNew+"&&"+idcontrollopadre);
	
	$(self).closest('tr').nextAll( ".trseparator" ).first().before('<tr>'+hrow+'</tr>');
	return false;
}

function selectAllByClass(self, name)
{
  $("."+name+":not(:disabled)").prop("checked", $(self).prop('checked'));
}
function submitConferma()
{
	var act = $('#mainForm').attr("action");
	$('#mainForm').attr("action", "confermacontrollitecnici.do");
	$('#mainForm').submit();
	$('#mainForm').attr("action", act);
}


function initMyDualList()
{
	$('#livelliDualList').DualListBox();
}

function serializzaConfermaSelectedList(idBandoOggetto, idQuadroOggControlloAmm)
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
		  url: 'conferma_livelli_'+idBandoOggetto+'_'+idQuadroOggControlloAmm+'.do',
		  data: ser,
		  success: function(data, textStatus) {
	    	  $('#dlgLivelli').modal('hide');
	    	  $('#visualizzaControlliAmmForm').submit();
	      },
		  async:false
		});
}

</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>