<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />

	<p:utente />
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="../dettaglio_${idLivello}.do">catalogo misure</a> <span class="divider">/</span></li>
					<li class="active">criteri di selezione</li>

				</ul>
			</div>
		</div>
	</div>

	<!-- testata ini 
	<p:utente />-->
	<div class="container-fluid">
		<div class="panel-group" id="accordion">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<span><b>Misura <c:out value="${dettagliTestata.codMisura}"></c:out>:
					</b> <c:out value="${dettagliTestata.descrMisura}"></c:out></span><br> <span><b>Sottomisura <c:out value="${dettagliTestata.codSottomisura}"></c:out>:
					</b> <c:out value="${dettagliTestata.descrSottomisura}"></c:out></span><br> <span><b>Tipo operazione <c:out
								value="${dettagliTestata.codTipoOperazione}"></c:out>:
					</b> <c:out value="${dettagliTestata.descrTipoOperazione}"></c:out></span><br>
				</div>
			</div>
		</div>
	</div>
	<!-- testata fine -->

	<div class="container-fluid">
		<ul class="nav nav-tabs">
			<li role="presentation"><a href="../beneficiari/dettaglio_${idLivello}.do">Beneficiari</a></li>
			<c:if test="${addInterventiToQuadri}">
				<li role="presentation"><a href="../interventi/dettaglio_${idLivello}.do">Interventi</a></li>
			</c:if>
			<li role="presentation"><a href="../focusarea/dettaglio_${idLivello}.do">Focus Area</a></li>
			<li role="presentation"><a href="../settoridiproduzione/dettaglio_${idLivello}.do">Settori di produzione</a></li>
			<li role="presentation" class="active"><a href="#" onclick="return false;">Criteri di selezione</a></li>

		</ul>
	</div>

	<p:messaggistica />
	<div class="container-fluid" id="content" style="margin-bottom: 3em">
	<form:form id="myForm">
		<m:panel id="panelBeneficiari">
			<c:if test="${msgErrore != null}">
				<div class="stdMessagePanel" style="margin-top: 1em">
					<div class="alert alert-danger">
						<p>
							<strong>Attenzione!</strong><br />
							<c:out value="${msgErrore}" escapeXml="false"></c:out>
						</p>
					</div>
				</div>
			</c:if>

			<div class="container-fluid">
				<h4>Modifica principio di selezione</h4>
				<div>
					<m:textarea name="principioDiSelezione" id="principioDiSelezione" label="Principio di selezione: *" preferRequestValues="${preferRequest}">${principioSelezionato.descrizione}</m:textarea>
					<c:if test="${principioSelezionato!=null}">
						<div class="pull-right" style="padding-top: 2em;">
							<a type="button" onclick="eliminaPrincipio(${principioSelezionato.idPrincipioSelezione},${idLivello});return false;"
								id="eliminaPrincipio" class="btn btn-primary">elimina principio</a>
						</div>
					</c:if>					.
				</div>
			</div>
			<div class="container-fluid table-responsive" style="padding-top: 2em;">
								
					<table id="tParametro" class="table table-hover table-striped table-bordered tableBlueTh">
						<thead>
							<tr>
								<th><a class="ico24 ico_add" title="Aggiungi" onclick="aggiungiRiga(${idLivello});return false;" href="#"></a></th>
								<th>Codice</th>
								<th>Criterio selezione</th>
								<th>Specifiche</th>
								<th>Punteggio minimo</th>
								<th>Punteggio massimo</th>
								<th>Tipo di controllo</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${principioSelezionato.criteri}" var="c" varStatus="st">
								<tr class="nhRow" data-index="${st.index + 1}" id="nhRow_${st.index + 1}">
									<td><a id="trash_${st.index + 1}" onclick="$(this).closest('tr').remove();rimuoviRiga(${st.index + 1});return false;"
										class="ico24 ico_trash" href="#"></a></td>

									<td>
									 <m:textfield preferRequestValues="${preferRequest}"  disabled="true" id="codice_${st.index + 1}" name="codice_${st.index + 1}" value="${c.codice}"></m:textfield>
									 <input type="hidden" id="rowIndex_${st.index + 1}" name="rowindex" value="${st.index + 1}">
									</td>
									<td>
									<m:textarea name="criterio_${st.index + 1}" id="criterio_${st.index + 1}" preferRequestValues="${preferRequest}" cols="40"><c:out value="${fn:trim(c.criterioDiSelezione)}"></c:out></m:textarea></td>
									<td><m:textarea name="specifiche_${st.index + 1}" id="specifiche_${st.index + 1}" preferRequestValues="${preferRequest}" cols="40"><c:out value="${c.specifiche}"></c:out></m:textarea></td>
									<td><m:textfield id="punteggioMin_${st.index + 1}" name="punteggioMin_${st.index + 1}" value="${c.punteggioMin}"></m:textfield></td>
									<td><m:textfield id="punteggioMax_${st.index + 1}" name="punteggioMax_${st.index + 1}" value="${c.punteggioMax}"></m:textfield></td>
									<td><m:select id="flagElaborazione_${st.index + 1}" list="${tipiElaborazione}" name="flagElaborazione_${st.index + 1}"
											preferRequestValues="${preferRequest}" valueProperty="id" textProperty="descrizione" selectedValue="${c.idFlagElaborazione}"></m:select></td>
								</tr>
							</c:forEach>
							<tr class="hRow" style="display: none">
								<td><a id="trash_$$index" onclick="$(this).closest('tr').remove();rimuoviRiga($$index);return false;" class="ico24 ico_trash" href="#"></a></td>
								<td>
								<m:textfield preferRequestValues="${preferRequest}" disabled="true" type="hidden" id="codice_$$index" name="codice_$$index" value="newCodice"></m:textfield>
							    <input type="hidden" id="rowIndex_$$index" name="rowindex" value="$$index"></td>
								<td><m:textarea name="criterio_$$index" id="criterio_$$index" preferRequestValues="${preferRequest}" cols="30"></m:textarea></td>
								<td><m:textarea name="specifiche_$$index" id="specifiche_$$index" preferRequestValues="${preferRequest}" cols="30"></m:textarea></td>
								<td><m:textfield id="punteggioMin_$$index" name="punteggioMin_$$index"></m:textfield></td>
								<td><m:textfield id="punteggioMax_$$index" name="punteggioMax_$$index"></m:textfield></td>
								<td><m:select id="flagElaborazione_$$index" list="${tipiElaborazione}" name="flagElaborazione_$$index"
										preferRequestValues="${preferRequest}" valueProperty="id" textProperty="descrizione"></m:select></td>
							</tr>
						</tbody>
					</table>

					<div class="puls-group" style="margin-top: 2em">
						<div class="pull-left">
							<button type="button" onclick="forwardToPage('dettaglio_${idLivello}.do');" class="btn btn-default">indietro</button>
						</div>
						<div class="pull-right">
							<button type="button" onclick="onSubmitFunction()" name="conferma" id="conferma" class="btn btn-primary">conferma</button>
						</div>
					</div>
				</div>
		</m:panel>
		</form:form>
	</div>
 	<input type="hidden" id="maxCodicePerLivello" name="maxCodicePerLivello" value="${maxCodicePerLivello}">
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script type="text/javascript">
	$('.document').ready(function(){
		$('input[id^="codice_"]').attr("disabled", true);	
	});
	
	function onSubmitFunction(idLivello)
	{
		$('input[id^="codice_"]').attr("disabled", false);	
		$("form#myForm").submit();
	}

	var cnt=0;
	function aggiungiRiga(idLivello)
	{
		var html = $('.hRow').clone().html();
		var indexNew =$('.nhRow').size() + 1;
		if(indexNew == undefined || indexNew == 'undefined' || isNaN(indexNew)){
			indexNew = 1;
		}
		html = html.replace(/\\$\\$index/g, indexNew);

		var nextCodice = "";
		$.ajax({
			  type: 'GET',
			  url: 'getNextCodice_'+idLivello+'_'+cnt+'.do',
			  success: function(data) {
		    	  nextCodice = data; 
		      },
			  async:false
			});
		
		$('#tParametro tbody').append('<tr class="nhRow" data-index="'+indexNew+'" id="nhRow_'+indexNew+'" >'+html+'</tr>');
		$("#codice_"+indexNew).val(nextCodice);
		cnt+=1;

		return false;
	}

	function rimuoviRiga(id)
	{
		//cambio valore di tutti gli indici presenti nelle righe successive a quella
		//che viene eliminata. In questo modo ho una sequenza di indici consecutivi completa (senza buchi) e non ho problemi
		//con i prefer request values.
		
		var indexNew =$('.nhRow').last().data('index') + 1;
		for(var i=id+1; i<indexNew ;i++)
			{
				var x = i-1;
				$("#criterio_"+i).attr("name", "criterio_"+x);
				$("#specifiche_"+i).attr("name", "specifiche_"+x);
				$("#punteggioMin_"+i).attr("name", "punteggioMin_"+x);
				$("#punteggioMax_"+i).attr("name", "punteggioMax_"+x);
				$("#flagElaborazione_"+i).attr("name", "flagElaborazione_"+x);
				$("#codice_"+i).attr("name", "codice_"+x);
				
				$("#trash_"+i).attr("onclick", "$(this).closest('tr').remove();rimuoviRiga("+x+");return false;");
			
				$("#criterio_"+i).attr("id", "criterio_"+x);
				$("#specifiche_"+i).attr("id", "specifiche_"+x);
				$("#punteggioMin_"+i).attr("id", "punteggioMin_"+x);
				$("#punteggioMax_"+i).attr("id", "punteggioMax_"+x);
				$("#flagElaborazione_"+i).attr("id", "flagElaborazione_"+x);	
				$("#codice_"+i).attr("id", "codice_"+x);
				
				$("#trash_"+i).attr("id", "trash_"+x);	

				$("#rowIndex_"+i).val(x);	
				$("#rowIndex_"+i).attr("id", "rowIndex_"+x);		

				$("#nhRow_"+i).attr("data-index", x);
				$("#nhRow_"+i).attr("id", "nhRow_"+x);									
			}		
	}

	function eliminaPrincipio(idPrincipio, idLivello)
    {
        return openPageInPopup("confermaEliminaPrincipio_"+idLivello+"_"+idPrincipio+".do",'dlgEliminaPricipio','Elimina Principio di Selezione', 'modal-large');
    }
	</script>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />