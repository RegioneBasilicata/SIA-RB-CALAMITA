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
					<li class="active">Inserisci (atti pubblicati) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="ATTI_PUBBLICATI" cu="CU-NEMBO-015-U"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<br>
				
				 
				 <div class="form-group">
				    <label for="tipologia" class="col-sm-2 control-label">
				    	File allegati al Bando (DGR, D.D., Manuali, etc.)
				    	<c:if test="${utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
					    	<a data-toggle="modal" data-target="#dlgInserisci" href="#" onclick="return allegaFile()">
					    		<span style="text-decoration: none;" class="icon-large icon-paperclip link pull-right"></span>
					    	</a>
				    	</c:if>
				    </label>
				    <div class="col-sm-10">
				    	<div class="col-sm-9">
				    	<c:if test="${allegati != null}">
					    	<table id="allBando" class="table table-hover table-striped table-bordered tableBlueTh">
						    	<colgroup>
									<col width="12%">
									<col width="10%">
									<col width="5%">
									<col width="35%">
									<col width="38%">
								</colgroup>
								<thead>
									<tr>
										<th colspan="3"></th>
										<th>Descrizione</th>
										<th>Nome File</th>
									</tr>
								</thead>
						    	<tbody>
						    	<c:forEach items="${allegati}" var="a" varStatus="al">
						    		<tr>
						    			
						    			<td>
						    					<span id="icons_modify_${a.idAllegatiBando}">
									    			<a href="#" class="ico32 ico_modify" title="Modifica allegato" onclick="return modificaAllegato('${a.idAllegatiBando}')"></a>
									    		</span>
												<span id="icons_trash_${a.idAllegatiBando}">
									    			<a href="#" class="ico32 ico_trash" data-toggle="modal" data-target="#dlgElimina" title="Elimina allegato" onclick="return eliminaAllegato('${a.idAllegatiBando}')"></a>
									    		</span>
						    			</td>
						    			<td class="tdModificaOrdine">
						    				<c:if test="${fn:length(allegati) > 1}">
						    					<c:choose>
						    						<c:when test="${al.index == 0}">
						    							<span  class="pull-right"><a href="#" class="ico24 ico_down" title="Sposta gi&ugrave;" onclick="return modificaOrdine('${a.idAllegatiBando}','${allegati[al.index+1].idAllegatiBando}')"></a></span> 
									    			</c:when>
						    						<c:when test="${al.index == (fn:length(allegati) -1) }">
						    							<span  class="pull-left" style="margin-left:1em"><a href="#" class="ico24 ico_up" title="Sposta su" onclick="return modificaOrdine('${a.idAllegatiBando}', '${allegati[al.index-1].idAllegatiBando}')"></a></span> 
									    			</c:when>
									    			<c:otherwise>
									    				<div>
							    							<span  class="pull-right"><a href="#" class="ico24 ico_down" title="Sposta gi&ugrave;" onclick="return modificaOrdine('${a.idAllegatiBando}','${allegati[al.index+1].idAllegatiBando}')"></a></span> 
							    							<span  class="pull-right"><a href="#" class="ico24 ico_up" title="Sposta su" onclick="return modificaOrdine('${a.idAllegatiBando}', '${allegati[al.index-1].idAllegatiBando}')"></a></span>
						    							</div> 
									    			</c:otherwise>
						    					</c:choose>
						    				</c:if>
						    			</td>
						    			<td>
						    				<span >
								    			<a href="downloadAlleg_${a.idBando}_${a.idAllegatiBando}.do" class="ico32 ${a.cssClass}" title="Visualizza documento"></a>
								    		</span>
						    			</td>
						    			<td><c:out value="${a.descrizione}"></c:out></td>
						    			<td><c:out value="${a.nomeFile}"></c:out></td>
						    		</tr>
						    	</c:forEach>
						    	</tbody>
					    	</table>
				    	</c:if>
				    	</div>
				    </div>
				 </div>
				 
				 <!-- Modal -->
				<div class="modal fade" id="dlgInserisci" tabindex="-1" role="dialog" aria-labelledby="dlgTitle" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title" id="dlgTitle">Inserimento nuovo allegato</h4>
							</div>
							<div class="modal-body"></div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="dlgElimina" tabindex="-1" role="dialog" aria-labelledby="dlgTitle" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title" id="dlgTitle">Elimina allegato</h4>
							</div>
							<div class="modal-body"></div>
						</div>
					</div>
				</div>
				
				
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<c:if test="${isAvversitaAtmosferica=='true' }">
							<button type="button" onclick="forwardToPage('scelta_comuni.do');" class="btn btn-default">indietro</button>
						</c:if>
						<c:if test="${isAvversitaAtmosferica=='false' }">
							<button type="button" onclick="forwardToPage('./datiidentificativi_${idBando}.do');" class="btn btn-default">indietro</button>
						</c:if>						
						<p:abilitazione dirittoAccessoMinimo="W">
						<c:choose>
							<c:when test="${modificaAbilitata}">
								<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma e prosegui</button>
							</c:when>
							<c:otherwise>
								<button type="button" name="conferma" onclick="window.location='filtrobeneficiari.do';" id="conferma" class="btn btn-primary pull-right">prosegui</button>
							</c:otherwise>
						</c:choose>
						</p:abilitazione>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
		function allegaFile() {
			$.ajax({
				type : "GET",
				url : 'inserisciFile.do',
				dataType : "html",
				async : false,
				success : function(data, textStatus) {
					setDialogHtml(data);
				}
			});
		}
		
		function modificaOrdine(idAllegatoPartenza, idAllegatoArrivo)
		{
			$('.tdModificaOrdine').html("<div align=\"center\" title=\"Attendere prego...\"><span class=\"ico32 ico_pleasewait\" style=\"vertical-align: middle\"></span></div>");
			$.ajax({
				type : "GET",
				url : 'modificaOrdine_'+idAllegatoPartenza+'_'+idAllegatoArrivo+'.do',
				dataType : "html",
				async : false,
				success : function(data, textStatus) {
					window.location.href='reloadattiPubblicati.do';
				}
			});
		}
		
		function eliminaAllegato(id) {
			$.ajax({
				type : "GET",
				url : 'conferma_elimina_' + id + '.do',
				dataType : "html",
				async : false,
				success : function(data) {
					$('#dlgElimina .modal-body').html(data);
					doErrorTooltip();
				}
			});
			return false;
		}
		function setDialogHtml(data) 
		{
			$('#dlgInserisci .modal-body').html(data);
			doErrorTooltip();
		}
		
		function modificaAllegato(id) {
			openPageInPopup('modifica_allegato_'+id+'.do','dlgModifica','Modifica allegato','modal-large');
		}
	</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>