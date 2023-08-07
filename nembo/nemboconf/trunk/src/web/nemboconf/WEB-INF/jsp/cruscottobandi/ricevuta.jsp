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
					<li class="active">Gestisci testo PEC </li>
				</ul>
			</div> 
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="RICEVUTA" cu="CU-NEMBO-015-M"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
		<b:error />
		<c:if test="${msgErrore != null}">
             	<div class="stdMessagePanel">	
                         <div class="alert alert-danger ">
                             <p><strong>Attenzione!</strong><br>
                            <c:out value="${msgErrore}"  escapeXml="false"></c:out>
                         </div>
                     </div>
             </c:if>
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<input type="hidden" value="${idBandoOggetto}" id="idBandoOggetto" name="idBandoOggetto">
				<div class="form-group">
				    <label for="tipologia" class="col-sm-4 control-label">
				    	Selezionare il gruppo oggetto / istanza da configurare
			    	   	<a data-toggle="modal" data-target="#oggettiModal" href="#">
				    		<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
				    	</a>
				    </label>
				    <div class="col-sm-8"> 
				    	<div class="well well-sm"><p id="oggettoSelezionato"><c:out value="${descrOggettoSelezionato}"></c:out></p></div>
				    </div>
				 </div>
				 
				 <c:if test="${ricevuta!=null}">
				 	<c:choose>
				 		<c:when test="${flagIstanza == 'S' && flagIstanzaPag == 'N'}"><div class="alert alert-success" role="alert">Oggetto di tipo ISTANZA. L'oggetto ed il testo della mail si riferisconto alle RICEVUTE di RICEZIONE ISTANZE</div></c:when>
				 		<c:when test="${flagIstanza == 'S' && flagIstanzaPag == 'S'}"><div class="alert alert-success" role="alert">Oggetto di tipo DOMANDA DI PAGAMENTO. L'oggetto ed il testo della mail si riferisconto alle RICEVUTE di RICEZIONE ISTANZE - DOMANDE DI PAGAMENTO</div></c:when>
				 		<c:when test="${flagIstanza == 'N'}"><div class="alert alert-success" role="alert">Oggetto di tipo ISTRUTTORIA. L'oggetto ed il testo della mail si riferiscono alle PEC inviate al beneficiario dalla PA come comunicazione dell'Esito dell'istruttoria</div></c:when>
				 	</c:choose>
				 	
					 <c:if test="${ricevutaDefault}">
						 <div class="alert alert-warning" role="alert">
							N.b: I dati proposti sono Oggetto e testo standard.
						</div>
					 </c:if>
					 
					 <c:if test="${flagAttivoRicevuta == 'S'}">
						<m:select labelSize="2"  id="placeholder" list="${elencoPlaceholder}" name="placeholder" disabled="true" disabledOptions="true"
						textProperty="descrizione" valueProperty="codice"
						label="Seleziona codice segnaposto" onchange="insertPlaceholder();"></m:select> 
							
					 	<m:textarea disabled="true"  labelSize="2"  name="oggettoMail" id="oggettoMail" label="Oggetto ricevuta" preferRequestValues="${fromRequest}">${ricevuta.oggettoMail}</m:textarea> 
					 	<m:textarea  disabled="true"  labelSize="2" name="corpoMail" id="corpoMail" label="Testo ricevuta" rows="20" preferRequestValues="${fromRequest}">${ricevuta.corpoMail}</m:textarea>
				 	</c:if>
				 	
				 	<c:if test="${flagAttivoRicevuta == 'N'}">
						<m:select labelSize="2"  id="placeholder" list="${elencoPlaceholder}" name="placeholder"
						textProperty="descrizione" valueProperty="codice"
						label="Seleziona codice segnaposto" onchange="insertPlaceholder();"></m:select> 
							
					 	<m:textarea labelSize="2"  name="oggettoMail" id="oggettoMail" label="Oggetto ricevuta" preferRequestValues="${fromRequest}">${ricevuta.oggettoMail}</m:textarea> 
					 	<m:textarea labelSize="2" name="corpoMail" id="corpoMail" label="Testo ricevuta" rows="20" preferRequestValues="${fromRequest}">${ricevuta.corpoMail}</m:textarea>
				 	</c:if>
				 	
				 	
				 </c:if>
				 
				 <div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('allegati.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
						<button type="button" onclick="submitAvanti();" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
						<c:if test="${flagAttivoRicevuta == 'N'}">
							<button type="button" onclick="submitConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right submitBtn">conferma</button>
						</c:if>
						<c:if test="${flagAttivoRicevuta == 'S'}">
							<button type="button"  name="conferma" id="conferma" disabled="disabled" class="btn btn-primary pull-right">conferma</button>
						</c:if>
						</p:abilitazione>
					</div>
				</div>
				
				<!-- POPUP SCELTA GRUPPO INI -->
			    <div class="modal fade" id="oggettiModal" tabindex="-1" role="dialog" aria-labelledby="oggettiModalLabel" aria-hidden="true">  		
					<div class="modal-dialog" style="width: 850px">                                                                                                         			
					  <div class="modal-content">                                                                                                      			
					    <div class="modal-header">                                                                                                     			
					      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 	
					      <h4 class="modal-title" id="myModalLabel">Selezionare il gruppo oggetto / istanza da configurare</h4>                                                               		
					    </div>                                                                                                                         				
					    <div class="modal-body">   
					    	<c:forEach items="${elenco}" var="a">
								<m:radio-list name="oggettiDisponibili" id="oggettiDisponibili" list="${a.oggetti}" textProperty="descrGruppoOggetto" valueProperty="idBandoOggetto"></m:radio-list>
							</c:forEach>	
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
/*
$(document).ready( function() {
	$("#corpoMail").Editor();  
	$("#corpoMail").Editor("setText", $('#corpoMail').val());              
});


function getCaretPosition(editableDiv) {
	  var caretPos = 0,
	    sel, range;
	  if (window.getSelection) {
	    sel = window.getSelection();
	    if (sel.rangeCount) {
	      range = sel.getRangeAt(0);
	      if (range.commonAncestorContainer.parentNode == editableDiv) {
	        caretPos = range.endOffset;
	      }
	    }
	  } else if (document.selection && document.selection.createRange) {
	    range = document.selection.createRange();
	    if (range.parentElement() == editableDiv) {
	      var tempEl = document.createElement("span");
	      editableDiv.insertBefore(tempEl, editableDiv.firstChild);
	      var tempRange = range.duplicate();
	      tempRange.moveToElementText(tempEl);
	      tempRange.setEndPoint("EndToEnd", range);
	      caretPos = tempRange.text.length;
	    }
	  }
	  return caretPos;
	}




	function insertPlaceholder()
	{
		var position = getCaretPosition($('.Editor-editor').get(0));
		var content = $("#corpoMail").Editor("getText");
		var newContent = content.substr(0, position) +" "+ $("#placeholder option:selected").val() + " "+ content.substr(position);
		$("#corpoMail").Editor("setText", newContent); 
		$("#placeholder").val('');
	}*/


	$(function () {
	    var tb = $("#corpoMail").get(0);
	    $("#corpoMail").keydown(function (event) {
	        var start = tb.selectionStart;
	        var end = tb.selectionEnd;
	        var reg = new RegExp("(\\\$\\\$[a-zA-z0-9_]+)", "g");
	        var amatch = null; 
	        while ((amatch = reg.exec(tb.value)) != null) {
	            var thisMatchStart = amatch.index;
	            var thisMatchEnd = amatch.index + amatch[0].length;
	            if (start <= thisMatchStart && end > thisMatchStart) {
	                event.preventDefault();
	                return false;
	            }
	            else if (start > thisMatchStart && start < thisMatchEnd) {
	                event.preventDefault();
	                return false;
	            }
	        }
	    });
	});
	
	function getCaret(el) { 
		  if (el.selectionStart) { 
		    return el.selectionStart; 
		  } else if (document.selection) { 
		    el.focus(); 

		    var r = document.selection.createRange(); 
		    if (r == null) { 
		      return 0; 
		    } 

		    var re = el.createTextRange(), 
		        rc = re.duplicate(); 
		    re.moveToBookmark(r.getBookmark()); 
		    rc.setEndPoint('EndToStart', re); 

		    return rc.text.length; 
		  }  
		  return 0; 
		}
	
	function insertPlaceholder()
	{
		var position = getCaret($('#corpoMail').get(0));
		var content = $("#corpoMail").val();
		var newContent = content.substr(0, position) +" "+ $("#placeholder option:selected").val() + " "+ content.substr(position);
		$("#corpoMail").val(newContent); 
		$("#placeholder").val('');
	}
	


	function confermaGruppo()
	{
		var scelta = $('input[name=oggettiDisponibili]:checked').val();
		if(!scelta)
		{
			return ;
		}
		$('#oggettoSelezionato').html($('input[name=oggettiDisponibili]:checked').closest('label').text());
		$('#idBandoOggetto').val($('input[name=oggettiDisponibili]:checked').val());
		$('#oggettiModal').modal('hide');
		$('#mainForm').attr('action','visualizzaricevuta.do').submit();
	}
	function submitConferma()
	{
		//$('#corpoMail').val($("#corpoMail").Editor("getText"));
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "confermaricevuta.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
	function submitAvanti()
	{
		var act = $('#mainForm').attr("action");
		$('#mainForm').attr("action", "avantiricevuta.do");
		$('#mainForm').submit();
		$('#mainForm').attr("action", act);
	}
</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>