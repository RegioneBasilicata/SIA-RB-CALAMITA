<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
					<li class="active">Inserisci (quadri) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="QUADRI" cu="CU-NEMBO-015-Q"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<input type="hidden" id="idGruppoOggettoSelezionato" name="idGruppoOggettoSelezionato" value="${idGruppoOggettoSelezionato}">
				<div style="margin-left:2em;margin-top:1em">
					<div class="form-group">
					    <label for="descrGruppoSelezionato" class="col-sm-4 control-label">
					    	Selezionare il gruppo oggetto / istanza da configurare
				    	   	<a data-toggle="modal" data-target="#gruppiModal" href="#">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
					    </label>
					    <div class="col-sm-8"> 
					    	<div class="well well-sm"><p id="descrGruppoSelezionato"><c:out value="${descrGruppoQuadrSelezionato}"></c:out></p></div>
					    </div>
				 	</div>
				</div>
				<c:if test="${elenco != null}">
				<table class="table table-hover table-striped table-bordered tableBlueTh">
					<thead>
						<tr>
							<th>Tipo oggetto/istanza</th>
							<th>Nome quadro</th>
							<th>Obbligatorio</th>
							<th>Previsto</th>
						</tr>
					</thead>
					<tbody> 
						<c:forEach items="${elenco}" var="quadro" varStatus="idx">
						<c:if test="${(idx.index == 0) || (quadro.descrGruppo != elenco[idx.index-1].descrGruppo)}">
							<tr>
								<td colspan="4" align="center">
									<i><b><c:out value="${quadro.descrGruppo}"></c:out></b></i>
								</td>
							</tr>
						</c:if>	
						
							<tr>
								<td>
									<c:if test="${(idx.index == 0) || (quadro.descrOggetto != elenco[idx.index-1].descrOggetto)}">
										<c:out value="${quadro.descrOggetto}"></c:out>
									</c:if>
								</td>
								<td><c:out value="${quadro.descrizione}"></c:out>
								<c:if test="${not empty quadro.descrizioneEstesa}" >
								<a style="margin-left:0.5em;" href="javascript:void(0);" data-toggle="txt-tooltip" title="${quadro.descrizioneEstesa}"><span class="icon icon-list"></span></a>
								</c:if>
								<c:if test="${quadro.dimensioneImmagine>0}" >	
								<a style="margin-left:0.5em;" onclick= "getImmagineJson(${quadro.idQuadro})"  data-toggle="tooltip" title="Click per visualizzare l'immagine"><span class="glyphicon glyphicon-picture"></span></a></c:if>
								</td>
								
								
								<c:choose>
									<c:when test="${quadro.flagAttivo == 'N' && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
										<td>
											<input type="hidden" name="chkObbligatorioHidden_${quadro.idQuadroOggettoMaster}" value="${quadro.flagObbligatorio == 'S'}">
											<b:checkBox  name="chkObbligatorio_${quadro.idQuadroOggettoMaster}" preferRequestValues="${fromRequest}" value="${quadro.idQuadro}" disabled="true"  checked="${quadro.flagObbligatorio == 'S'}"></b:checkBox> 
										</td>
										<td><b:checkBox  name="chkPrevisto_${quadro.idQuadroOggettoMaster}" preferRequestValues="${fromRequest}" value="${quadro.idQuadroOggettoMaster}" disabled="${quadro.flagObbligatorio == 'S'}"  checked="${quadro.quadroEsistente}"></b:checkBox> </td>
									</c:when>
									<c:otherwise>
										<td><b:checkBox  name="chkObbligatorio_${quadro.idQuadroOggettoMaster}" preferRequestValues="${fromRequest}" value="${quadro.idQuadroOggettoMaster}" disabled="true"  checked="${quadro.flagObbligatorio == 'S'}"></b:checkBox> </td>
										<td><b:checkBox  name="chkPrevisto_${quadro.idQuadroOggettoMaster}" preferRequestValues="${fromRequest}" value="${quadro.idQuadroOggettoMaster}" disabled="true"  checked="${quadro.quadroEsistente}"></b:checkBox> </td>
									</c:otherwise>
								</c:choose>
							</tr>
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
					      <h4 class="modal-title" id="myModalLabel">Selezionare il gruppo oggetto / istanza da configurare</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
								<m:radio-list name="selGruppoOggetto" id="selGruppoOggetto" list="${elencoGruppiOggetto}" textProperty="descrizioneEstesa" valueProperty="idUnivoco"></m:radio-list>
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
						<button type="button" onclick="forwardToPage('oggetti.do');" class="btn btn-default">indietro</button>
						<button type="button" onclick="forwardToPage('controlliAmm.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
						<c:if test="${elenco!=null}">
							<p:abilitazione dirittoAccessoMinimo="W">
								<button type="button" onclick="submitConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
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
	var act = $('#mainForm').attr("action");
	$('#mainForm').attr("action", "quadri.do");
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
	
	if($('#selGruppoOggetto').val()==0)
	{
		$('#tableControlli').remove();
	}else{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "visualizzaquadri.do");
		$('#mainForm').submit().attr("action", act);
	}
}

function getImmagineJson(idQuadro)
{
    openPageInPopup('./getImmagine_'+idQuadro+'.do','visualizzaImmagine','Immagine', 'modal-large');
}

</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>