<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
					<li class="active">Inserisci (Dichiarazioni) </li>
				</ul>
			</div> 
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="DICHIARAZIONI" cu="CU-NEMBO-015-D"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
		<b:error />
		<c:if test="${msgErrore != null}">
             	<div class="stdMessagePanel">	
                         <div class="alert alert-danger ">
                             <p><strong>Attenzione!</strong><br>
                            <c:out value="${msgErrore}" escapeXml="false"></c:out>
                         </div>
                     </div>
             </c:if>
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<input type="hidden" value="${idQuadroOggetto}" id="idQuadroOggetto" name="idQuadroOggetto">
				<input type="hidden" value="${idBandoOggetto}" id="idBandoOggetto" name="idBandoOggetto">
				<div class="form-group">
				    <label data-toggle="modal" data-target="#gruppiModal" for="tipologia" class="col-sm-4 control-label">
				    	Selezionare il gruppo oggetto / istanza da configurare
			    	   	<a href="#">
				    		<span style="text-decoration: none;" class="icon-large icon-folder-open link blueFolder"></span>
				    	</a>
				    </label>
				    <div class="col-sm-8"> 
				    	<div class="well well-sm"><p id="gruppoSelezionato"><c:out value="${descrGruppoSelezionato}"></c:out></p></div>
				    </div>
				 </div>
				 <p>Nel testo delle dichiarazioni è possibile aggiungere dei campi variabili che dovranno essere inseriti in fase di compilazione della dichiarazione: $$STRING (campo stringa di libera digitazione, accetta qlc carattere); $$NUMBER (numero che può essere anche inserito con valori decimali usando la virgola come carattere separatore); $$INTEGER (valore numerico intero); $$DATE (campo in formato data gg/mm/aaaa).</p>
				 
				 <c:if test="${elencoGruppiInfo != null || nuoveDichiarazioni != null}">
				 	<fieldset>
				 	<legend>Dichiarazioni importate da catalogo</legend>
					 <table id="tGruppo" class="table table-hover table-striped table-bordered tableBlueTh" >
						<colgroup>
							<col width="8%">
							<col width="92%">
							<col width="5%">
						</colgroup>
						<thead>
							<tr> 
								<th>
									<c:if test="${flagAttivoDichiarazione == 'N'}">
								 		<a data-toggle="modal" data-target="#gruppiCatalogoModal" href="#" class="ico24 ico_modify" href="#"></a>
								 	</c:if>
								</th>  
								<th>Titolo dichiarazione e Dettaglio dichiarazione</th>
								<th>Obbligatorio</th> 
							</tr>
						</thead>
						<tbody>
						  <c:forEach items="${elencoGruppiInfo}" var="e" varStatus="st">
						  	<c:if test="${e.flagInfoCatalogo == 'S'}">
						  		<tr class="row_${e.idGruppoInfo}">
								  	<td></td>
								  	<td colspan="2">
								  		<span class="freccia espandi" id="freccia_${e.idGruppoInfo}"><a  title="Espandi" id="${e.idGruppoInfo}" href="#" onclick="toggleRow('${e.idGruppoInfo}');return false;"></a></span>
								  		<c:out value="${e.descrizione}"></c:out>
								  	</td>
						  		</tr>
							  	<c:forEach items="${e.elencoDettagliInfo}" var="f" >
								  	<tr class="row_${e.idGruppoInfo} toggle_${e.idGruppoInfo}" style="display: none">
								  		<td></td>
								  		<td><c:out value="${f.descrizione}"></c:out></td>	
								  		<td><b:checkBox name="chkGruppo" value="${e.idGruppoInfo}" disabled="true" checked="${f.flagObbligatorio == 'S'}"></b:checkBox></td>
								  	</tr>
							  	</c:forEach>
							 </c:if>
						  </c:forEach>
						</tbody>
					 </table>
					 </fieldset>
					 
					<fieldset>
				 		<legend>Dichiarazioni aggiuntive</legend>
				 		<input type="hidden" id="maxid" value="-1">
				 		<table id="tNewGruppo" class="table table-hover table-striped table-bordered tableBlueTh" >
						<colgroup>
							<col width="8%">
							<col width="62%">
							<col width="5%">
							<col width="18%">
							<col width="7%">
						</colgroup>
							<thead>
								<tr> 
									<th>
										<c:if test="${flagAttivoDichiarazione == 'N'}">
											<a onclick="addNewGruppoInfo();return false;" href="#" class="ico24 ico_add" href="#"></a> 
											<a data-toggle="modal" data-target="#vincoliModal" href="#"  title="Gestisci vincoli"><span style="color:white" class="glyphicon glyphicon-random btn-lg" aria-hidden="true"></span></a>
										</c:if>
									</th>  
									<th>Titolo dichiarazione e Dettaglio dichiarazione</th>
									<th>Obbligatorio</th> 
									<th>Vincolo tra dichiarazioni</th>
									<th>Ordine</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${nuoveDichiarazioni != null}">
										<c:forEach items="${nuoveDichiarazioni}" var="a" varStatus="l">
											<tr class="newGroup row_${l.index}"><td colspan="5"><input type="hidden" id="idGruppoInfo_${l.index}" name="idGruppoInfo_${l.index}" value="${a.idGruppoInfo}"><i>Titolo dichiarazione *</i></td></tr>
											<tr class="newGroup row_${l.index}" >
												<td>
													<c:if test="${a.elencoDettagliInfo != null}">
														<input type="hidden" id="maxDetail_${l.index}" value="${fn:length(a.elencoDettagliInfo)-1}">
													</c:if>
													<c:if test="${a.elencoDettagliInfo == null}">
														<input type="hidden" id="maxDetail_${l.index}" value="-1">
													</c:if>
													<c:if test="${(flagAttivoDichiarazione == 'N' && !sessionProcedimentoOggettoQuadroInserito) || (requestScope[\"isNuovoInserimento_\".concat(l.index)])}">
														<a onclick="$('.row_${l.index}').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
													</c:if>	  
												</td>
												<td colspan="4"><m:textarea name="txtAreaGruppo_${l.index}" id="txtAreaGruppo_${l.index}" readonly="${flagAttivoDichiarazione == 'S'}" style="width:100%">${a.descrizione}</m:textarea></td>
											</tr>
											<tr class="newGroup newDetail row_${l.index} detRow_${l.index}">
												<td colspan="5">
													<i>Dettaglio dichiarazione *
														<c:if test="${flagAttivoDichiarazione == 'N'}">
															<a style="margin-left:1em" onclick="addNewDettaglioInfo(${l.index});return false;" href="#" class="ico16 ico_add"></a>&nbsp;
															<a style="margin-left:1em" onclick="reorderManager(${l.index});return false;" href="#" class="ico16 ico_up_down"></a>
														</c:if>
													</i>
												</td>
											</tr>
											<c:forEach items="${a.elencoDettagliInfo}" var="b" varStatus="c">
												<tr class="newGroup newDetail row_${l.index} detRow_${l.index} detRow_${c.index}_${l.index}">
													<td> 
														<input type="hidden" id="idDettaglioInfo_${c.index}_${l.index}" name="idDettaglioInfo_${c.index}_${l.index}" value="${b.idDettaglioInfo}">
														<c:if test="${(flagAttivoDichiarazione == 'N' && !sessionProcedimentoOggettoQuadroInserito) || (requestScope[\"isNuovoInserimento_\".concat(c.index).concat('_').concat(l.index)])}">
															<a onclick="$('.detRow_${c.index}_${l.index}').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
														</c:if>	
													</td>
													<td><m:textarea readonly="${flagAttivoDichiarazione == 'S'}" id="txtAreaGruppo_${c.index}_${l.index}" name="txtDettaglioGruppo_${c.index}_${l.index}" style="width:100%">${b.descrizione}</m:textarea></td>
													<td><b:checkBox disabled="${flagAttivoDichiarazione == 'S'}" name="flagObblDetailGruppo_${c.index}_${l.index}" value="${c.index}" checked="${b.flagObbligatorio == 'S'}"></b:checkBox></td>
													<td><m:select  viewTitle="true" viewOptionsTitle="true"selectedValue="${mapIdLegamiInfo[b.idLegameInfoStr]}" preferRequestValues="${preferRqParameter}"  disabled="${flagAttivoDichiarazione == 'S'}" id="selvincolo_${c.index}_${l.index}"  list="${elencoVincoli}" textProperty="descrizione" valueProperty="id" name="selvincolo_${c.index}_${l.index}"></m:select> </td>
													<td></td> 
												</tr>
											</c:forEach>
											<tr class="newGroup row_${l.index}"><td colspan="5">&nbsp;</td></tr>
										</c:forEach>
									</c:when>
									<c:when test="${elencoGruppiInfoNoCatalogo != null }">
										<c:forEach items="${elencoGruppiInfoNoCatalogo}" var="a" varStatus="l">
											<c:if test="${a.flagInfoCatalogo == 'N'}">
												<tr class="newGroup row_${l.index}"><td colspan="5"><input type="hidden" id="idGruppoInfo_${l.index}" name="idGruppoInfo_${l.index}" value="${a.idGruppoInfo}"><i>Titolo dichiarazione *</i></td></tr>
												<tr class="newGroup row_${l.index}" >
													<td>
														<c:if test="${a.elencoDettagliInfo != null}">
															<input type="hidden" id="maxDetail_${l.index}" value="${fn:length(a.elencoDettagliInfo)-1}">
														</c:if>
														<c:if test="${a.elencoDettagliInfo == null}">
															<input type="hidden" id="maxDetail_${l.index}" value="-1">
														</c:if>
														<c:if test="${flagAttivoDichiarazione == 'N' && !sessionProcedimentoOggettoQuadroInserito}">
															<a onclick="$('.row_${l.index}').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
														</c:if>
													</td>
													<td colspan="4"><m:textarea readonly="${flagAttivoDichiarazione == 'S'}"  name="txtAreaGruppo_${l.index}" id="txtAreaGruppo_${l.index}"  style="width:100%">${a.descrizione}</m:textarea></td>
 												</tr>
												<tr class="newGroup newDetail row_${l.index} detRow_${l.index} "><td colspan="5"><i>Dettaglio dichiarazione * <c:if test="${flagAttivoDichiarazione == 'N'}"><a onclick="addNewDettaglioInfo(${l.index});return false;" href="#" class="ico16 ico_add" style="margin-left:1em"></a><a style="margin-left:1em" onclick="reorderManager(${l.index});return false;" href="#" class="ico16 ico_up_down"></a></c:if></i></td></tr>
												<c:forEach items="${a.elencoDettagliInfo}" var="b" varStatus="c">
													<tr class="newGroup newDetail row_${l.index} detRow_${l.index} detRow_${c.index}_${l.index}">
														<td> 
															<input type="hidden" id="idDettaglioInfo_${c.index}_${l.index}" name="idDettaglioInfo_${c.index}_${l.index}" value="${b.idDettaglioInfo}">
															<c:if test="${flagAttivoDichiarazione == 'N' && !sessionProcedimentoOggettoQuadroInserito}">
																<a onclick="$('.detRow_${c.index}_${l.index}').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
															</c:if>	
														</td>
														<td><m:textarea readonly="${flagAttivoDichiarazione == 'S'}"  id="txtAreaGruppo_${c.index}_${l.index}" name="txtDettaglioGruppo_${c.index}_${l.index}" style="width:100%">${b.descrizione}</m:textarea></td>
														<td><b:checkBox disabled="${flagAttivoDichiarazione == 'S'}"  name="flagObblDetailGruppo_${c.index}_${l.index}" value="${c.index}" checked="${b.flagObbligatorio == 'S'}"></b:checkBox></td>
														<td><m:select  viewTitle="true" viewOptionsTitle="true" selectedValue="${mapIdLegamiInfo[b.idLegameInfoStr]}" preferRequestValues="${preferRqParameter}" disabled="${flagAttivoDichiarazione == 'S'}" id="selvincolo_${c.index}_${l.index}" list="${elencoVincoli}" textProperty="descrizione" valueProperty="id"  name="selvincolo_${c.index}_${l.index}"></m:select></td>
														<td></td> 
													</tr>
												</c:forEach>
												<tr class="newGroup row_${l.index}"><td colspan="5">&nbsp;</td></tr>
											</c:if>
										</c:forEach>
									</c:when> 
								</c:choose>
								
								<tr id="hNewGroup" style="display: none">
									<td>
										<input type="hidden" id="maxDetail_$$index" value="-1">
										<a onclick="$('.row_$$index').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
									</td>
									<td colspan="4"><m:textarea name="txtAreaGruppo_$$index" id="txtAreaGruppo_$$index" style="width:100%"></m:textarea></td>
								</tr>
								<tr id="hNewDetail" style="display: none">
									<td>		
										<a onclick="$('.detRow_$$idGruppo_$$index').remove();return false;" class="ico24 ico_trash pull-left" href="elimina_gruppo.do"></a>
									</td>
									<td><m:textarea name="txtDettaglioGruppo_$$index_$$idGruppo" id="txtAreaGruppo_$$index_$$idGruppo" style="width:100%"></m:textarea></td>
									<td><b:checkBox name="flagObblDetailGruppo_$$index_$$idGruppo" value="$$index" checked="true"></b:checkBox> </td>
									<td><m:select  viewTitle="true" viewOptionsTitle="true"  preferRequestValues="${preferRqParameter}" id="selvincolo_$$index_$$idGruppo" list="${elencoVincoli}" textProperty="descrizione" valueProperty="id" name="selvincolo_$$index_$$idGruppo"></m:select></td>
									<td></td>
								</tr>
								
							</tbody>
						</table>
				 	</fieldset>
				 </c:if>
				 <em>I campi contrassegnati con * sono obbligatori</em>
				 <div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('controlli.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
						<button type="button" onclick="submitAvanti();" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
						<c:if test="${flagAttivoDichiarazione == 'N'}">
							<button type="button" onclick="submitConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right submitBtn">conferma</button>
						</c:if>
						<c:if test="${flagAttivoDichiarazione == 'S'}">
							<button type="button"  name="conferma" id="conferma" disabled="disabled" class="btn btn-primary pull-right ">conferma</button>
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
			    
			    <!-- POPUP CATALOGO GRUPPO INI -->
			    <div class="modal fade" id="gruppiCatalogoModal" tabindex="-1" role="dialog" aria-labelledby="gruppiCatalogoModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Catalogo dichiarazioni Standard</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<table id="tGruppo" class="table table-hover table-striped table-bordered tableBlueTh" >
								<colgroup>
									<col width="5%">
									<col width="90%">
									<col width="5%">
								</colgroup>
								<tbody>
								  <c:forEach items="${elencoGruppiInfoCatalogo}" var="e" varStatus="st">
								  	<tr>
									  	<th class=""  style="border-left: 0px">	
									  		<span class="freccia espandi" id="freccia_${e.idGruppoInfo}"><a  title="Espandi" id="${e.idGruppoInfo}" href="#" onclick="toggleRow('${e.idGruppoInfo}');return false;"></a></span>
									  	</th>
									  	<th><c:out value="${e.descrizione}"></c:out></th>
									  	<th>
									  		<c:if test="${e.flagInfoObbligatoria == 'S'}"><input type="hidden" name="chkGruppoCatalogo" value="${e.idGruppoInfo}"> </c:if>
									  		<b:checkBox name="chkGruppoCatalogo" value="${e.idGruppoInfo}" disabled="${e.flagInfoObbligatoria == 'S'}" checked="${e.flagInfoObbligatoria == 'S'}"></b:checkBox> 
									  	</th>
									  	<c:forEach items="${e.elencoDettagliInfo}" var="f" >
										  	<tr class="toggle_${e.idGruppoInfo}" style="display: none">
										  		<td colspan="3"><c:out value="${f.descrizione}"></c:out></td>	
										  	</tr>
									  	</c:forEach>
								  	</tr>
								  </c:forEach>
								 
								</tbody>
							 </table>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="confermaGruppoCatalogo();"  class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP CATALOGO GRUPPO FINE -->
			    
			    
			    
			    <!-- POPUP VINCOLI INI -->
			    <div class="modal fade" id="vincoliModal" tabindex="-1" role="dialog" aria-labelledby="vincoliModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 950px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Gestione vincoli</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
					    	<p>Significato dei vincoli:<br>
							<strong>UNIVOCO</strong>: sulle dichiarazioni o impegni associati allo stesso vincolo deve essercene obbligatoriamente selezionata una e non più di una<br>
							<strong>OBBLIGATORIO</strong>: sulle dichiarazioni o impegni associati allo stesso vincolo deve essercene obbligatoriamente selezionata almeno una<br>
							<strong>OPZIONALE_UNIVOCO</strong>: sulle dichiarazioni o impegni associati allo stesso vincolo può non esssercene selezionata alcuna o in alternativa solamente una</p>
							<table id="tVincoli" class="table table-hover table-striped table-bordered tableBlueTh" >
								<thead>
									<tr>
										<th><a onclick="aggiungiVincolo();return false;" class="ico24 ico_add" href="#"></a></th>
										<th>Tipo</th>
										<th>Identificativo</th>
									</tr>
								</thead>	
								<tbody>
									<tr class="hRowVincoli" style="display:none">
									 	<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="elimina_vincolo.do"></a></td>
									 	<td><m:select  viewTitle="true" viewOptionsTitle="true" id="tipiVincoli_$$index" preferRequestValues="${preferRqParameter}" onchange="$('#idVincolo_$$index').val($('#tipiVincoli_$$index option:selected').text()+'_'+$$index);" list="${tipiVincoliDich}" name="tipiVincoli_$$index" valueProperty="id" textProperty="descrizione"></m:select></td>
									 	<td><m:textfield id="idVincolo_$$index" name="idVincolo_$$index" disabled="true"></m:textfield> </td>
									 </tr> 
									<c:if test="${vincoliTrovati != null}">
										<c:forEach items="${vincoliTrovati}" var="a" varStatus="idx">
											<tr class="rowVincoli"  data-index="${idx.index}"> 
										 		<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="elimina_vincolo.do"></a></td>
										 		<td><m:select  viewTitle="true" viewOptionsTitle="true" selectedValue="${a.codice}" id="tipiVincoli_${idx.index}" preferRequestValues="${preferRqParameter}" onchange="$('#idVincolo_${idx.index}').val($('#tipiVincoli_${idx.index} option:selected').text().substring(0,1)+'_${idx.index});" list="${tipiVincoliDich}" name="tipiVincoli_${idx.index}" valueProperty="id" textProperty="descrizione"></m:select></td>
										 		<td><m:textfield id="idVincolo_${idx.index}" value="${a.descrizione}_${idx.index}" name="idVincolo_${idx.index}" disabled="true"></m:textfield> </td>
										 	</tr>
									 	</c:forEach>
									</c:if>
									 
								</tbody>
							 </table>
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="confermaVincoli();"  class="btn btn-primary ">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP VINCOLI FINE -->
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
	$( document ).ready(function() {
		$('#maxid').val($('[name^=txtAreaGruppo_]').length-2);
	});
	
	function aggiungiVincolo()
	{
		var html = $('.hRowVincoli').clone().html();
		var indexNew =$('.rowVincoli').last().data('index') + 1;
		if(indexNew == undefined || indexNew == 'undefined' || isNaN(indexNew)){
			indexNew = 0;
		}
		html = html.replace(/\\$\\$index/g, indexNew);
		$('#tVincoli tbody').append('<tr class="rowVincoli" data-index="'+indexNew+'">'+html+'</tr>');
	}

	function confermaVincoli()
	{
		var optionsVincoli = "<option value=\"\">-- selezionare --</option>";
		$('.rowVincoli input[name^="idVincolo_"]').each(function(){
			optionsVincoli = optionsVincoli + " <option title=\""+$(this).val()+"\"  value=\""+$(this).val()+"\">"+$(this).val()+"</option>";  
		});
		$('select[name^="selvincolo_"]').each(function(){
			var selecteVal = $(this).val();
			$(this).html(optionsVincoli).val(selecteVal);
		});
		$('#vincoliModal').modal("hide");
	}

	function confermaGruppo()
	{
		var scelta = $('input[name=gruppiDisponibili]:checked').val();
		if(!scelta)
		{
			return ;
		}
		
		$('#gruppoSelezionato').html($('input[name=gruppiDisponibili]:checked').closest('label').text());
		$('#idQuadroOggetto').val($('input[name=gruppiDisponibili]:checked').val());
		$('#gruppiModal').modal('hide');
		$('#mainForm').attr('action','visualizzadichiarazioni.do').submit();
	}
	function confermaGruppoCatalogo()
	{
		$('#gruppiCatalogoModal').modal('hide');
		$('#mainForm').attr('action','refreshGruppoInfo.do').submit();
	}
	function addNewGruppoInfo()
	{
		var maxId = $('#maxid').val();
		var newId = parseInt(maxId) + 1 ;
		$('#maxid').val(newId);
		$('#tNewGruppo tbody').append('<tr class="newGroup row_'+newId+'"><td colspan="5"><i>Titolo dichiarazione *</i></td></tr>');
		$('#tNewGruppo tbody').append('<tr class="newGroup row_'+newId+'">'+$('#hNewGroup').html().replace(/\\$\\$index/g, newId)+'</tr>');
		
		maxId = $('#maxDetail_'+newId).val();
		var newIdDet = parseInt(maxId) + 1 ;
		$('#maxDetail_'+newId).val(newIdDet);
		var txt = $('#hNewDetail').html().replace(/\\$\\$index/g, newIdDet);
		
		txt = txt.replace(/\\$\\$idGruppo/g, newId);
		$('#tNewGruppo tbody').append('<tr class="newGroup newDetail row_'+newId+' detRow_'+newId+' "><td colspan="5"><i>Dettaglio dichiarazione * <a onclick="addNewDettaglioInfo('+newId+');return false;" href="#" class="ico16 ico_add" href="#" style="margin-left:1em"></a>&nbsp;<a onclick="reorderManager('+newId+');return false;" href="#" style="margin-left:1em" class="ico16 ico_up_down"></a></i></td></tr>');
		$('#tNewGruppo tbody').append('<tr class="newGroup newDetail row_'+newId+' detRow_'+newId+' detRow_'+newId+'_'+newIdDet+'">'+txt+'</tr>');
		$('#tNewGruppo tbody').append('<tr class="newGroup row_'+newId+'"><td colspan="5">&nbsp;</td></tr>');
	}

	function reorderManager(idGruppo)
	{
		var count =  $('tr.detRow_'+idGruppo+'').length;
		if(count>0){
		var htmlDown = "<div><a class=\"ico24 ico_down pull-right\" onclick=\"spostaGiu('WWrowIndex','"+idGruppo+"');return false;\" href=\"sposta_giu.do\" title=\"Sposta giu\"></a></div>";
		var htmlUp   = "<div><a class=\"ico24 ico_up pull-right\" href=\"sposta_su.do\" onclick=\"spostaSu('WWrowIndex','"+idGruppo+"');return false;\" title=\"Sposta su\"></a></div>";
		var iniCnt   = "<div class=\"reorderDiv\">";
		var fineCnt  = "</div>";
		
		$('tr.detRow_'+idGruppo+'').each(function(index){
			if(index > 0)
			{
				index = index-1;
				var _htmlDown = $(htmlDown).clone().html().replace("WWrowIndex",index);
				var _htmlUp = $(htmlUp).clone().html().replace("WWrowIndex",index);
				
				$cell = $(this).find( "td" ).last();
				if($cell.html().indexOf("reorderDiv") >= 0)
				{
					$cell.html($cell.remove(".reorderDiv"));
				}
				else
				{
					// inserisco le frecce su/giu
					if(index==0){
						$cell.html($cell.html()+iniCnt+_htmlDown+fineCnt);
					}
					else if(index == (count-2)){ // tolgo anche la riga di intestazione
						$cell.html($cell.html()+iniCnt+_htmlUp+fineCnt);
					}
					else
					{
						$cell.html($cell.html()+iniCnt+_htmlUp);
						$cell.html($cell.html()+_htmlDown+fineCnt);
					}
				}
			}
		});
		}
	}

	function spostaSu(rowIndex, idGruppo)
	{
		var thisTxt =$('#txtAreaGruppo_'+rowIndex+'_'+idGruppo).val();
		var newTxt =$('#txtAreaGruppo_'+(Number(rowIndex)-1)+'_'+idGruppo).val();
		if(newTxt!==undefined){
			var thisSelect =$('#selvincolo_'+rowIndex+'_'+idGruppo).val();
			var newSelect =$('#selvincolo_'+(Number(rowIndex)-1)+'_'+idGruppo).val();
			var thisFlag =$('input[name="flagObblDetailGruppo_'+rowIndex+'_'+idGruppo+'"]').prop( "checked" );
			var newFlag  =$('input[name="flagObblDetailGruppo_'+(Number(rowIndex)-1)+'_'+idGruppo+'"]').prop( "checked" );
			$('#txtAreaGruppo_'+rowIndex+'_'+idGruppo).val(newTxt);
			$('#txtAreaGruppo_'+(Number(rowIndex)-1)+'_'+idGruppo).val(thisTxt);
			$('#selvincolo_'+rowIndex+'_'+idGruppo).val(newSelect);
			$('#selvincolo_'+(Number(rowIndex)-1)+'_'+idGruppo).val(thisSelect);
			$('input[name="flagObblDetailGruppo_'+rowIndex+'_'+idGruppo+'"]').prop( "checked",newFlag );
			$('input[name="flagObblDetailGruppo_'+(Number(rowIndex)-1)+'_'+idGruppo+'"]').prop( "checked",thisFlag );
		}
		return false;
	}

	function spostaGiu(rowIndex, idGruppo)
	{
		var thisTxt =$('#txtAreaGruppo_'+rowIndex+'_'+idGruppo).val();
		var newTxt =$('#txtAreaGruppo_'+(Number(rowIndex)+1)+'_'+idGruppo).val();
		if(newTxt!==undefined){
		var thisSelect =$('#selvincolo_'+rowIndex+'_'+idGruppo).val();
		var newSelect =$('#selvincolo_'+(Number(rowIndex)+1)+'_'+idGruppo).val();
		var thisFlag =$('input[name="flagObblDetailGruppo_'+rowIndex+'_'+idGruppo+'"]').prop( "checked" );
		var newFlag  =$('input[name="flagObblDetailGruppo_'+(Number(rowIndex)+1)+'_'+idGruppo+'"]').prop( "checked" );
		
		$('#txtAreaGruppo_'+rowIndex+'_'+idGruppo).val(newTxt);
		$('#txtAreaGruppo_'+(Number(rowIndex)+1)+'_'+idGruppo).val(thisTxt);
		$('#selvincolo_'+rowIndex+'_'+idGruppo).val(newSelect);
		$('#selvincolo_'+(Number(rowIndex)+1)+'_'+idGruppo).val(thisSelect);
		$('input[name="flagObblDetailGruppo_'+rowIndex+'_'+idGruppo+'"]').prop( "checked",newFlag );
		$('input[name="flagObblDetailGruppo_'+(Number(rowIndex)+1)+'_'+idGruppo+'"]').prop( "checked",thisFlag );
		}
		
		return false;
	}

	function addNewDettaglioInfo(idGruppo)
	{
		$('.detRow_'+idGruppo+' .reorderDiv').remove();
		var maxId = $('#maxDetail_'+idGruppo).val();
		var newId = parseInt(maxId) + 1 ;
		$('#maxDetail_'+idGruppo).val(newId);
		var txt = $('#hNewDetail').html().replace(/\\$\\$index/g, newId);
		
		txt = txt.replace(/\\$\\$idGruppo/g, idGruppo);
		$('.detRow_'+idGruppo+'').last().after('<tr class="newGroup newDetail row_'+idGruppo+' detRow_'+idGruppo+' detRow_'+idGruppo+'_'+newId+'">'+txt+'</tr>');
	}
	function submitConferma()
	{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "confermadichiarazioni.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
	function submitAvanti()
	{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "avantidichiarazioni.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>