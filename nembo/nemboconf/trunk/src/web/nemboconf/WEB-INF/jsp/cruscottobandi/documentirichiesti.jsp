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
	<p:CruscottoBandiHeader activeTab="DOCUMENTI_RICHIESTI" cu="CU-NEMBO-015-Q"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				
				<div style="margin-left:2em;margin-top:1em">
					<div class="form-group">
					    <label for="descrGruppoSelezionato" class="col-sm-4 control-label">
					    	Selezionare il gruppo oggetto / istanza da configurare
				    	   	<a data-toggle="modal" data-target="#gruppiModal" href="#">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
					    </label>
					    <div class="col-sm-8"> 
					    	<div class="well well-sm"><p id="descrGruppoSelezionato"><c:out value="${descrOggettoSelezionato}"></c:out></p></div>
					    </div>
				 	</div>
				</div>
				
				<c:if test="${errProcOggPresenti}">
					<div class="alert alert-danger alert-error">
						Attenzione!<br/>Non &egrave; stato possibile apportare modifiche ai documenti richiesti perch&egrave; in alcuni procedimenti sono gi&agrave; stati inseriti dei documenti che si sta tendando di rimuovere. 
					</div>
				</c:if>
				
				<c:if test="${errBandoAttivo}">
					<div class="alert alert-danger alert-error">
						Attenzione!<br/>Non &egrave; stato possibile apportare modifiche ai documenti richiesti in quanto l'oggetto risulta essere attivo. 
					</div>
				</c:if>
				
				<c:if test="${withDocri != null && !withDocri}">
					<div class="alert alert-danger alert-error">
						Attenzione!<br/>Il quadro "Documenti Richiesti" non &egrave; stato abilitato sull'oggetto corrente. 
					</div>
				</c:if>
				
				<c:if test="${canUpdate == false && modificabile == true}">
					<div class="alert alert-danger alert-error">
						Attenzione!<br/>L'operazione non può essere eseguita dal tipo di utente connesso.
					</div>
				</c:if>
				
				<c:if test="${documenti != null && withDocri}">
				<table class="table table-hover table-striped table-bordered tableBlueTh">
					<thead>
						<tr>
							<th>Tipo oggetto/istanza</th>
							<th>Sezione</th>
							<th>Nome documento</th>
							<th>Previsto <input type="checkbox" name="chkSelectAll" id="chkSelectAll" onclick="selectAllCheckboxes('idTipoDocRicOggetto',$('#selectAll').prop('checked'));"/></th>
						</tr>
					</thead>
					<tbody> 

						<c:forEach items="${documenti}" var="documento" varStatus="idx">
							<tr>
								<td>${documento.descOggetto}</td>
								<td>${documento.sezione}</td>
								<td>${documento.descTipoDocRichiesti}</td>
								<td><input type="checkbox" ${documento.idTipoDocRicBandoOgg != null ? "checked" : ""} name="idTipoDocRicOggetto" value="${documento.idTipoDocRicOggetto}" ${!canUpdate ? "disabled" : ""}/></td>
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
						<button type="button" onclick="forwardToPage('ricevuta.do');" class="btn btn-default">indietro</button>
						<button type="button" onclick="forwardToPage('criteriSelezione.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
						<input type="hidden" id="idGruppoOggettoSelezionato" name="idGruppoOggettoSelezionato" value="${idGruppoOggettoSelezionato}">
						<c:if test="${documenti!=null}">
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
	$('#mainForm').attr("action", "inserisci.do");
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
		$('#mainForm').attr("action", "visualizza_documenti_richiesti.do");
		$('#mainForm').submit().attr("action", act);
	}
}

function getImmagineJson(idQuadro)
{
    openPageInPopup('./getImmagine_'+idQuadro+'.do','visualizzaImmagine','Immagine', 'modal-large');
}

</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>