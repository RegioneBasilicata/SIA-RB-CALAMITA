<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<style>
.backGroudYellow{
background-color :#FFFCE6;
}
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
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Inserisci (Controlli) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="CONTROLLI" cu="CU-NEMBO-015-C"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">

		<b:error />
				 <c:if test="${msgErrore != null}">
	             	<div class="stdMessagePanel">	
	                         <div class="alert alert-danger ">
	                             <p><strong>Attenzione!</strong><br>
	                            ${msgErrore}
	                         </div>
	                     </div>
	             </c:if>
	             
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
					    	<div class="well well-sm"><p id="descrGruppoSelezionato"><c:out value="${descrGruppoContrSelezionato}"></c:out></p></div>
					    </div>
				 	</div>
				</div>
				<c:if test="${msgWarning != null}">
	             	<div class="stdMessagePanel">	
	                         <div class="alert alert-warning ">
	                             <p><strong>Attenzione!</strong><br>
	                            <c:out value="${msgWarning}"></c:out>
	                         </div>
	                     </div>
	             </c:if>
				<c:if test="${controlli != null}">
					<table id="tableControlli" class="table table-hover table-striped table-bordered tableBlueTh">
						<colgroup>
							<col width="25%">
							<col width="5%">
							<col width="55%">
							<col width="3%">
							<col width="3%">
							<col width="3%">
							<col width="3%">
							<col width="3%">
							<col width="3%">
							<col width="3%">						
						</colgroup>
						<thead>
							<tr>
								<th>Quadro / Gruppo controllo</th>
								<th>Controllo</th>
								<th>Descrizione</th>
								<th>Obbligatorio</th>
								<th>Gravit&agrave; (default)</th>
						
								<th>Giustificabile (default)</th>
								<th>Attivo</th>
								<th>Gravità (previsto)</th>
								
								<th>Giustificabile (previsto)</th>								
								<th>Parametri aggiuntivi</th>								
							</tr>
						</thead> 
						<tbody>
							<c:forEach items="${controlli}" var="a" varStatus="st">
								<c:if test="${(st.index == 0) || (a.descrOggetto != controlli[st.index-1].descrOggetto)}">
									<tr>
										
										<td id="oggetto_${a.idUnivoco}" colspan="10" align="center">
										
											<i><b><c:out value="${a.descrOggetto}"></c:out></b></i>
										</td>
									</tr>
								</c:if>
								
								<tr id="ancorOggetto_${a.idOggetto}" >
								  	<td>
								  		<c:if test="${(st.index == 0) || (a.descrQuadro != controlli[st.index-1].descrQuadro)|| (a.descrOggetto != controlli[st.index-1].descrOggetto)}">
									  		<c:out value="${a.descrQuadro}"></c:out>
								  		</c:if>
								  	</td>
								  	<td>
										<input type="hidden" id="gravitaInserita_${a.idUnivoco}" name="gravitaInserita_${a.idUnivoco}" value="${a.gravitaInserita}">
										<input type="hidden" id="gravita_${a.idUnivoco}" name="gravita_${a.idUnivoco}" value="${a.gravita}">
								  		<c:out value="${a.codice}"></c:out></td>
								  	<td>
								  		<c:out value="${a.descrizione}"></c:out>
								  		<c:if test="${a.descrEstesa != null}">
											<span class="icon icon-list" data-toggle="txt-tooltip"  data-placement="top" title="<c:out value='${a.descrEstesa}'></c:out>"></span>
										</c:if>
								  	</td>
								  	<td class="backGroudYellow"><b:checkBox disabled="true" name="chkObbligatorio_${a.idUnivoco}" value="chkObbligatorio_${a.idUnivoco}" checked="${a.flagObbligatorio == 'S'}"></b:checkBox> </td>
								  	<td class="backGroudYellow">
								  		<c:choose>
								  			<c:when test="${a.gravita == 'W'}">
									  			<span>
									    			<a href="#" onclick="return false;" title="Warning" id="icoDefault_${a.idUnivoco}" class="ico24 ico_warning noLink" ></a>
									    		</span>
								  			</c:when>
								  			<c:when test="${a.gravita == 'B'}">
									  			<span>
									    			<a href="#" onclick="return false;" title="Bloccante" id="icoDefault_${a.idUnivoco}"  class="ico24 ico_error noLink" ></a>
									    		</span>
								  			</c:when>
								  			<c:when test="${a.gravita == 'G'}">
									  			<span>
									    			<a href="#" onclick="return false;" title="Grave" id="icoDefault_${a.idUnivoco}" class="ico24 ico_fatal noLink"></a>
									    		</span>
								  			</c:when>
								  		</c:choose>
								  	</td>
								  	<td class="backGroudYellow">
								  	<div class="form-inline">
								  		<b:checkBox disabled="true" name="chkGiustificabileDefault_${a.idUnivoco}" value="chkGiustificabileDefault_${a.idUnivoco}" checked="${a.flagGiustificabileQuadro == 'S' && a.numeroSoluzioniAnomalie> 0}"></b:checkBox>
								  		<!--<c:if test="${a.numeroSoluzioniAnomalie > 0}">
								  			<a data-toggle="tooltip" title="Visualizza dettaglio risoluzioni" style="cursor:pointer;padding-left:0.8em;" onclick="openPageInPopup('getDettaglioRisoluzioni_${a.idControllo}.do', 'dlgSoluzioni', 'Elenco giustificazioni legate al controllo','modal-large', false);"> <span class="glyphicon glyphicon-search"></span></a>
								  		</c:if>-->
								  		</div>
								  	</td>
								  	<td><b:checkBox  onClick="chkPrevistoClick(${a.idUnivoco}, ${a.numeroSoluzioniAnomalie });" disabled="${a.flagObbligatorio == 'S' || a.flagAttivo == 'S' || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}"  name="chkPrevisto_${a.idUnivoco}" value="chkPrevisto_${a.idUnivoco}" checked="${a.idControlloInserito != 0}"></b:checkBox></td>
								  	
								  	<td id="tdGravitaInserita_${a.idUnivoco}">
									  	<c:choose>
									  		
									  		<c:when test="${a.gravitaInserita != null && a.flagAttivo == 'S'}">
									  			<c:choose>
										  			<c:when test="${a.gravitaInserita == 'W'}">
											  			<span >
											    			<a href="#" id="ico_${a.idUnivoco}" title="Warning" class="ico24 ico_warning noLink" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravitaInserita == 'B'}">
											  			<span >
											    			<a href="#" id="ico_${a.idUnivoco}" title="Bloccante" class="ico24 ico_error noLink" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravitaInserita == 'G'}">
											  			<span >
											    			<a href="#"  id="ico_${a.idUnivoco}" title="Grave" class="ico24 ico_fatal noLink" ></a>
											    		</span>
										  			</c:when>
									  			</c:choose>
									  		</c:when>
									  		<c:when test="${a.gravitaInserita != null && a.flagAttivo != 'S'}">
									  			<c:choose>
										  			<c:when test="${a.gravitaInserita == 'W'}">
											  			<span >
											    			<a href="#" onclick="changeImage('${a.idUnivoco}');return false;" id="ico_${a.idUnivoco}" title="Warning" class="ico24 ico_warning" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravitaInserita == 'B'}">
											  			<span >
											    			<a href="#" onclick="changeImage('${a.idUnivoco}');return false;" id="ico_${a.idUnivoco}" title="Bloccante" class="ico24 ico_error" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravitaInserita == 'G'}">
											  			<span >
											    			<a href="#" onclick="changeImage('${a.idUnivoco}');return false;" id="ico_${a.idUnivoco}" title="Grave" class="ico24 ico_fatal" ></a>
											    		</span>
										  			</c:when>
									  			</c:choose>
									  		</c:when>
									  		<c:when test="${a.flagObbligatorio == 'S'}">
									  			<c:choose>
										  			<c:when test="${a.gravita == 'W'}">
											  			<span>
											    			<a href="#" onclick="return false;" title="Warning" class="ico24 ico_warning noLink" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravita == 'B'}">
											  			<span>
											    			<a href="#" onclick="return false;" title="Bloccante" class="ico24 ico_error noLink" ></a>
											    		</span>
										  			</c:when>
										  			<c:when test="${a.gravita == 'G'}">
											  			<span>
											    			<a href="#" onclick="return false;" title="Grave" class="ico24 ico_fatal noLink" ></a>
											    		</span>
										  			</c:when>
									  			</c:choose>
									  		</c:when>
									  	</c:choose>
								  	</td>
								  	
								  	<td>
								  	<div class="form-inline">
								  		<b:checkBox  disabled="${a.flagGiustificabileBando==null || a.flagAttivo == 'S' || utenteAbilitazioni.dirittoAccessoPrincipale=='R' || a.numeroSoluzioniAnomalie <= 0}"  name="chkGiustificabilePrevisto_${a.idUnivoco}" value="chkGiustificabilePrevisto_${a.idUnivoco}" checked="${a.flagGiustificabileBando == 'S' && a.numeroSoluzioniAnomalie>0}"></b:checkBox>
								  		<c:if test="${a.numeroSoluzioniAnomalie > 0}">
								  			<a data-toggle="tooltip" title="Visualizza dettaglio risoluzioni" style="cursor:pointer;padding-left:0.8em;" onclick="openPageInPopup('getDettaglioRisoluzioni_${a.idControllo}.do', 'dlgSoluzioni', 'Elenco giustificazioni legate al controllo','modal-large', false);"> <span class="glyphicon glyphicon-search"></span></a>
								  		</c:if>
								  		</div>
								  	</td>
								  	
								  	<td>
								  		<c:if test="${a.descrParametro != null}">
								  			<a id="parametri_${a.idUnivoco}" href="#" onClick="openPageParametri('${a.idBandoOggetto}','${a.idControllo}','${a.codice}');">
								  			Parametri aggiuntivi</a>
								  		</c:if>
								  	</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>				 		    
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('grafici.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
							<button type="button" onclick="submitAvanti();" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
							<c:if test="${controlli != null}">
								<button type="button" onclick="submitConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right submitBtn">conferma</button>
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
								<m:radio-list name="selGruppoOggetto" id="selGruppoOggetto" list="${elenco}" textProperty="descrizioneEstesa" valueProperty="idUnivoco"></m:radio-list>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="visualizzaControlli();"  class="btn btn-primary">Conferma</button>
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

	
	function chkPrevistoClick(idControllo, numeroSoluzioniAnomalie)
	{
		if($('input[name=chkPrevisto_'+idControllo+']').attr('checked') == 'checked')
		{
			//se sto ceccando allora riporto l'immagine
			var defClass = $('#icoDefault_'+idControllo).attr('class');
			defClass= defClass.replace('noLink','');
			$('#tdGravitaInserita_'+idControllo).html('<span >'+
	    			'<a href="#" onclick="changeImage('+idControllo+');return false;" id="ico_'+idControllo+'" title="'+$('#icoDefault_'+idControllo).attr('title')+'" class="'+defClass+'" ></a>'+
		    		'</span>');
			$('#gravitaInserita_'+idControllo).val($('#gravita_'+idControllo).val());


			//replico il flag Giustificabile (default) a Giustificabile (previsto)
			if(numeroSoluzioniAnomalie!=0)
			{
				$("input[name=chkGiustificabilePrevisto_"+idControllo+']').attr('checked',$("input[name=chkGiustificabileDefault_"+idControllo+']').attr('checked'));
				$("input[name=chkGiustificabilePrevisto_"+idControllo+']').removeAttr('disabled');
			}
		}else
		{
			//se non sto ceccando allora elimino l'immagine
			$('#tdGravitaInserita_'+idControllo).html(' ');
			$('#gravitaInserita_'+idControllo).val(' ');

			//rimuovo il flag e disabilito checkbox
			$("input[name=chkGiustificabilePrevisto_"+idControllo+']').attr('checked',false);
			$("input[name=chkGiustificabilePrevisto_"+idControllo+']').attr('disabled','disabled');
		}
	}
	
	function changeImage(idControllo)
	{
		var currCss = $('#ico_'+idControllo).attr('class').trim();
		if(currCss == 'ico24 ico_warning'){
			$('#ico_'+idControllo).attr('class', 'ico24 ico_error');
			$('#ico_'+idControllo).attr('title', 'Bloccante');
			$('#gravitaInserita_'+idControllo).val('B');
		}else if(currCss == 'ico24 ico_error'){
			$('#ico_'+idControllo).attr('class', 'ico24 ico_fatal');
			$('#ico_'+idControllo).attr('title', 'Grave');
			$('#gravitaInserita_'+idControllo).val('G');
		}else if(currCss == 'ico24 ico_fatal'){
			$('#ico_'+idControllo).attr('class', 'ico24 ico_warning');
			$('#ico_'+idControllo).attr('title', 'Warning');
			$('#gravitaInserita_'+idControllo).val('W');
		}
	}
	
	function openPageParametri(idBandoOggetto, idControllo, codice)
	{
		$('<input>').attr({
		    type: 'hidden',
		    id: 'idControllo',
		    name: 'idControllo',
		    value: idControllo
		}).appendTo('#mainForm');
		
		$('<input>').attr({
		    type: 'hidden',
		    id: 'idBandoOggetto',
		    name: 'idBandoOggetto',
		    value: idBandoOggetto
		}).appendTo('#mainForm');
		
		openPageInPopup('parametri_'+idControllo+'_'+idBandoOggetto+'.do','dlgParametri','PARAMETRI CONTROLLO '+codice,  'modal-large');
		/*
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "parametri.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);*/
	}
	
	function submitAvanti()
	{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "avanti.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
	
	function submitConferma()
	{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "conferma.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
	
	function visualizzaControlli()
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
			$('#mainForm').attr("action", "visualizzaControlli.do");
			$('#mainForm').submit().attr("action", act);
		}
	}
</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>