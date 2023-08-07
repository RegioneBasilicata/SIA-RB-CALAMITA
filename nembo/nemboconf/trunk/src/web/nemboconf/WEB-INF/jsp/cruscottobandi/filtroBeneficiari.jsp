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
					<li class="active">Inserisci (Filtro Beneficiari) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="FILTRO_BENEFICIARIO" cu="CU-NEMBO-015-B"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<br>
				<c:if test="${!formeDisponibili}">
					<div class="alert alert-warning" role="alert">
						N.b: Non sono presenti forme giuridiche disponibili per le misure selezionate per questo bando
					</div>
				</c:if>
				
				
				<div class="form-group">
				     <label for="formeGiuridiche" class="col-sm-2 control-label"> 
				    	Forme Giuridiche Aziende ammissibili
				    	<c:if test="${modificaAbilitata && utenteAbilitazioni.dirittoAccessoPrincipale=='W' && formeDisponibili}">
					    	<a data-toggle="modal" data-target="#formeGiuridicheModal" href="#"  onclick="openPopupFormeGiuridiche();">
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
				    	</c:if>
					</label> 
				    <div class="col-sm-10"> 
				    	<b:select name="formeGiuridiche" readonly="true"  header="false" id="formeGiuridiche"  size="5" list="${formeGiuridiche}" valueProperty="idFgTipologia" textProperty="descFormaGiuridica" multiple="true" ></b:select>
				    </div>
				 </div>
				 <div class="form-group">
				    <label for="ulterioreFiltro" class="col-sm-2 control-label"> 
				    	Ulteriore filtro 
				    	<c:if test="${modificaAbilitata && utenteAbilitazioni.dirittoAccessoPrincipale=='W' }">
					    	<a data-toggle="modal" data-target="#ulterioreFiltroModal" href="#" >
					    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
					    	</a>
				    	</c:if>
					</label>
				    <div class="col-sm-10"> 
				    	<input type="hidden" name="idDescrizioneFiltro" value="" id="idDescrizioneFiltro">
				    	<b:textarea rows="10" readonly="true" id="ulterioreFiltro" name="ulterioreFiltro" ><c:out value="${descrizioneFiltro}" escapeXml="false"></c:out></b:textarea>
				    </div>
				 </div>
				 
			
				 
				
				<div class="modal fade" id="ulterioreFiltroModal" tabindex="-1" role="dialog" aria-labelledby="ulterioreFiltroModal" aria-hidden="true">
					<div class="modal-dialog" style="width:700px">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title">Filtri di selezione aziende</h4>
							</div>
							<div class="modal-body">
								<table class="table table-hover table-striped table-bordered tableBlueTh">
								<colgroup>
					           	<col width="2%" />
					           	<col width="98%" />
					           </colgroup>
									<tbody>
										<c:forEach items="${elencoFiltriAziende}" var="a" varStatus="b">
											<tr>
												<td><b:radio name="filtroAzienda" value="${a.id}"></b:radio></td>
												<td id="filtro_${a.id}"><c:out  value="${a.descrizione}"  escapeXml="false"></c:out> </td>
											</tr>
										</c:forEach>									
									</tbody>
								</table>
								<div class="form-group puls-group" style="margin-top: 1.5em">
									<div class="col-sm-12">
										<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
										<button type="button" data-dismiss="modal" class="btn btn-primary pull-right" onclick="confermaFiltroAzienda();">conferma</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				
				 <!-- POPUP AMMINISTRAZIONI INI -->
			    <div class="modal fade" id="formeGiuridicheModal" tabindex="-1" role="dialog" aria-labelledby="formeGiuridicheModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Forme Giuridiche Aziende ammissibili</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
							<div id="dual-list-box1" class="form-group row">
					            <select style="display: none" id="formeGiuridicheDualList"  multiple="multiple" data-title="Forme" 
					            	data-source="loadFormeDisponibili_${idBando}.json" data-value="idFgTipologia" 
					            	data-sourceselected="loadFormeSelezionate_${idBando}.json"
					            	data-text="descFormaGiuridica"
					            	data-labelfilter="Filtra Forme Giuridiche"></select>
					        </div>	                                                                                                                  					
					    </div>                                                                                                                         				
					    <div class="modal-footer"> 
						    <div class="puls-group" style="margin-top:1em">
						      <div class="pull-left">  
						        <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						      </div>
						      <div class="pull-right">  
						        <button type="button" onclick="salvaFormeGiuridiche('${idBando}');" class="btn btn-primary">Conferma</button>
						      </div>
					    	</div>                                                                                                  				
					                                            				
					    </div>                                                                                                                         				
					  </div>                                                                                                                           				
					</div>                                                                                                                             				
		        </div> 
			    <!-- POPUP AMMINISTRAZIONI FINE -->
				
				
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('attiPubblicati.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
						<c:choose>
							<c:when test="${modificaAbilitata}">
								<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma e prosegui</button>
							</c:when>
							<c:otherwise>
									<c:if test="${mostraQuadroInterventi}">
										<button type="button" name="conferma" onclick="window.location='interventi.do';" id="conferma" class="btn btn-primary pull-right">prosegui</button>
									</c:if>
									<c:if test="${!mostraQuadroInterventi}">
										 	<button type="button" name="conferma" onclick="window.location='oggetti.do';" id="conferma" class="btn btn-primary pull-right">prosegui</button>
									</c:if>
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

		function confermaFiltroAzienda() 
		{
			var id = $('input[name=filtroAzienda]:checked').val();
			$('#idDescrizioneFiltro').val(id);
			$('#ulterioreFiltro').html($('#filtro_'+id).html().replace(/<br ?\/?>/g,'\n'));
		}

		function openPopupFormeGiuridiche()
		{
			resetDualList();
			$('#formeGiuridicheDualList').DualListBox();
		}

		function resetDualList()
		{
			//Funzione necessaria per eseguire il reset delle dual list e cancellare eventuali dual list presenti. IN questo modo viene poi ricreata solo quella che serve e le funzioni interne al 
			//widget non vanno a scrivere sull'eventuale altra list che trova nel DOM
			
			$('#formeGiuridicheDualList .modal-body').html('<select style="display: none" id="formeGiuridicheDualList"  multiple="multiple" data-title="Forme Giuridiche" '
	            		+' data-source="loadFormeDisponibili_${idBando}.json" data-value="idFgTipologia" '
	            		+' data-sourceselected="loadFormeSelezionate_${idBando}.json" '
		            	+ 'data-text="descFormaGiuridica" '
		            	+ 'data-labelfilter="Filtra Forme Giuridiche"></select>');
		}


		function salvaFormeGiuridiche(idBando)
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
				  url: 'salva_forme_giuridiche_'+idBando+'.do',
				  data: ser,
				  success: function(data, textStatus) {
					  location.reload();
			      },
				  async:false
				});
		}
	</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>