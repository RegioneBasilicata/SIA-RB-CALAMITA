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
					<li class="active">Inserisci (oggetti/istanze) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="OGGETTI" cu="CU-NEMBO-015-O"></p:CruscottoBandiHeader>
	
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
			
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<input type="hidden" id="idGruppoOggettoSelezionato" name="idGruppoOggettoSelezionato" value="${idGruppoOggettoSelezionato}">
				<div style="margin-left:2em;margin-top:1em">
					<div class="form-group">
					    <label for="descrGruppoSelezionato" class="col-sm-4 control-label">
					    	Gruppi Oggetti/Istanze previsti
				    	   	<a data-toggle="modal" data-target="#gruppiModal" href="#">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
					    </label>
					    <div class="col-sm-8"> 
					    	<div class="well well-sm"><p id="descrGruppoSelezionato"><c:out value="${descrGruppoOggSelezionato}"></c:out></p></div>
					    </div>
				 	</div>
				</div>
				<c:if test="${elenco != null}">
					<em>Selezionare gli oggetti previsti per il Bando; la "data apertura" è obbligatoria; per le "Istanze presentate dal beneficiario" la "data chiusura / scadenza" è obbligatoria, la "Data proroga o ritardo" è facoltativa</em>
					<table class="table table-hover table-striped table-bordered tableBlueTh" style="margin-top:1em">
						<colgroup>
							<col width="3%">
							<col width="42%">
							<col width="10%">
							<col width="15%">
							<col width="15%">
							<col width="15%">
						</colgroup>
						<thead>
							<tr>
								<th></th>
								<th>Tipo oggetto/istanza</th>
								<th>Istanza presentata dal beneficiario</th>
								<th>Data apertura</th>
								<th>Data chiusura / scadenza</th>
								<th>Data proroga o ritardo (consentito) </th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${elenco}" var="e">
								<tr>
									<td></td>
								  	<td colspan="5" align="center"><i><b><c:out value="${e.descrGruppo}"></c:out></b></i></td>
								</tr>
								<c:forEach items="${e.oggetti}" var="f">
									<c:choose>
										<c:when test="${f.flagAttivo == 'S' && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
											<c:set var="titleTxt" value="Tipo oggetto/istanza già attivo sul Bando e non più modificabile"></c:set>
											<tr>
												<td>
													<div class="has-error red-tooltip" data-original-title="${titleTxt}" data-toggle="error-tooltip">
														<input type="hidden" name="chkOggetto_${f.idLegameGruppoOggetto}" value="${f.idLegameGruppoOggetto}">
														<b:checkBox name="chkOggetto_${f.idLegameGruppoOggetto}" preferRequestValues="${fromRequest}" value="${f.idLegameGruppoOggetto}" disabled="true"  checked="${f.idBandoOggetto > 0}"></b:checkBox>
													</div> 
												</td>
											  	<td>
											  		<div class="has-error red-tooltip" data-original-title="${titleTxt}" data-toggle="error-tooltip">
											  			<c:out value="${f.descrOggetto}"></c:out>
											  		</div>
											  	</td>
											  	<td><c:out value="${f.flagIstanzaDescr}"></c:out></td>
											  	<td>
											  		<input type="hidden" name="dataApertura_${f.idLegameGruppoOggetto}" value="${f.dataInizioStr}">
											  		<m:textfield name="dataApertura_${f.idLegameGruppoOggetto}" value="${f.dataInizioStr}" id="dataApertura_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</td>
											  	<td>
											  		<input type="hidden" name="dataChiusura_${f.idLegameGruppoOggetto}" value="${f.dataFineStr}">
											  		<m:textfield name="dataChiusura_${f.idLegameGruppoOggetto}" value="${f.dataFineStr}" id="dataChiusura_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</td>
											  	<td>
											  	<c:if test="${f.flagIstanza == 'S'}">
											  		<input type="hidden" name="dataProroga_${f.idLegameGruppoOggetto}" value="${f.dataRitardoStr}">
											  		<input type="hidden" name="dataProrogaPrecedente_${f.idLegameGruppoOggetto}" value="${f.dataRitardoStr}">
											  		<m:textfield name="dataProroga_${f.idLegameGruppoOggetto}"  value="${f.dataRitardoStr}" id="dataProroga_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</c:if>
											  	</td>
											</tr>
										</c:when>
										<c:when test="${f.flagMasterScaduto == 'S' || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}">
											<c:set var="titleTxt" value="Tipo oggetto/istanza non più disponibile ed utilizzabile"></c:set>
											<tr>
												<td>
													<div class="has-error red-tooltip" data-original-title="${titleTxt}" data-toggle="error-tooltip">
														<b:checkBox name="chkOggetto_${f.idLegameGruppoOggetto}" preferRequestValues="${fromRequest}" value="${f.idLegameGruppoOggetto}" disabled="true"  checked="${f.idBandoOggetto > 0}"></b:checkBox>
													</div> 
												</td>
											  	<td>
											  		<div class="has-error red-tooltip" data-original-title="${titleTxt}" data-toggle="error-tooltip">
											  			<c:out value="${f.descrOggetto}"></c:out>
											  		</div>
											  	</td>
											  	<td><c:out value="${f.flagIstanzaDescr}"></c:out></td>
											  	<td>
											  		<input type="hidden" name="dataApertura_${f.idLegameGruppoOggetto}" value="${f.dataInizioStr}">
											  		<m:textfield name="dataApertura_${f.idLegameGruppoOggetto}" value="${f.dataInizioStr}" id="dataApertura_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</td>
											  	<td>
											  		<input type="hidden" name="dataChiusura_${f.idLegameGruppoOggetto}" value="${f.dataFineStr}">
											  		<m:textfield name="dataChiusura_${f.idLegameGruppoOggetto}" value="${f.dataFineStr}" id="dataChiusura_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</td>
											  	<td>
											  	<c:if test="${f.flagIstanza == 'S'}">
											  		<input type="hidden" name="dataProroga_${f.idLegameGruppoOggetto}" value="${f.dataRitardoStr}">
											  		<input type="hidden" name="dataProrogaPrecedente_${f.idLegameGruppoOggetto}" value="${f.dataRitardoStr}">
											  		<m:textfield name="dataProroga_${f.idLegameGruppoOggetto}"  value="${f.dataRitardoStr}" id="dataProroga_${f.idLegameGruppoOggetto}" type="date" disabled="true" preferRequestValues="${fromRequest}"></m:textfield>
											  	</c:if>
											  	</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td><b:checkBox name="chkOggetto_${f.idLegameGruppoOggetto}" preferRequestValues="${fromRequest}" value="${f.idLegameGruppoOggetto}" checked="${f.idBandoOggetto > 0}"></b:checkBox> </td>
											  	<td><c:out value="${f.descrOggetto}"></c:out></td>
											  	<td><c:out value="${f.flagIstanzaDescr}"></c:out></td>
											  	<td><m:textfield name="dataApertura_${f.idLegameGruppoOggetto}" dataType="dataPers" value="${f.dataInizioStr}" id="dataApertura_${f.idLegameGruppoOggetto}" type="date" preferRequestValues="${fromRequest}"></m:textfield></td>
											  	<td><m:textfield name="dataChiusura_${f.idLegameGruppoOggetto}" dataType="dataPers2" value="${f.dataFineStr}" id="dataChiusura_${f.idLegameGruppoOggetto}" type="date" preferRequestValues="${fromRequest}"></m:textfield></td>
											  	<td>
											  	<c:if test="${f.flagIstanza == 'S'}">
											  		<input type="hidden" name="dataProrogaPrecedente_${f.idLegameGruppoOggetto}" value="${f.dataRitardoStr}">
											  		<m:textfield name="dataProroga_${f.idLegameGruppoOggetto}" dataType="dataPers2"  value="${f.dataRitardoStr}" id="dataProroga_${f.idLegameGruppoOggetto}" type="date" preferRequestValues="${fromRequest}"></m:textfield>
											  	</c:if>
											  	</td>
											</tr>
										</c:otherwise>
									</c:choose>
								
								</c:forEach>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
				<!-- POPUP SCELTA GRUPPO INI -->
			    <div class="modal fade" id="gruppiModal" tabindex="-1" role="dialog" aria-labelledby="gruppiModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Seleziona gruppo oggetto di riferimento</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
								<m:radio-list name="selGruppoOggetto" id="selGruppoOggetto" list="${elencoGruppiOggetto}" textProperty="descrGruppo" valueProperty="idGruppoOggetto"></m:radio-list>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="visualizzaQuadri();"  class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP SCELTA GRUPPO FINE -->
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
					<c:choose>
						<c:when test="${mostraQuadroInterventi}">
							<button type="button" onclick="forwardToPage('${indietro}');" class="btn btn-default">indietro</button>
						</c:when>
						<c:otherwise>
							<button type="button" onclick="forwardToPage('${indietro}');" class="btn btn-default">indietro</button>
						</c:otherwise>
					</c:choose>
					<button type="button" onclick="forwardToPage('quadri.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
						<c:if test="${elenco!=null}">
							<p:abilitazione dirittoAccessoMinimo="W">
								<button type="button" name="conferma" onclick="submitConferma();" id="conferma" class="btn btn-primary pull-right">conferma</button>
							</p:abilitazione>
						</c:if>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
function submitConferma()
{
	$('#conferma').attr('disabled',true);
	var act = $('#mainForm').attr("action");
	$('#mainForm').attr("action", "confermaoggetti.do");
	$('#mainForm').submit();
	$('#mainForm').attr("action", act);
}
function visualizzaQuadri()
{
	var scelta = $('input[name=selGruppoOggetto]:checked').val();
	if(!scelta)
	{
		return ;
	}
	var act = $('#mainForm').attr("action");
	$('#mainForm').attr("action", "visualizzaoggetti.do");
	$('#mainForm').submit().attr("action", act);
}

$( document ).ready(function() {
	$("input[data-type='dataPers']").datepicker({ dateFormat: 'dd/mm/yy 00:00:00' });
	$("input[data-type='dataPers2']").datepicker({ dateFormat: 'dd/mm/yy 23:59:59' });
});
</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>