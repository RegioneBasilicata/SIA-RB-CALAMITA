<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-toggle/css/bootstrap-toggle.min.css">
<style>
.stepwizard-step {
	width: 33%;
}
</style>
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />

	<p:utente />

	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="../cruscottobandi/index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Duplica bando</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container-fluid" id="content">
		<m:panel id="panelDuplicaBando" collapsible="true" title="Duplica/Esporta bando">
			<div class="col-sm-12">
				<m:stepWizard numberOfSteps="3" activeStep="2" labels="Tipo operazione/Selezione bando&&&Selezione oggetti&&&Configurazione oggetti"></m:stepWizard>


				<div style="padding-top: 3em;">
					<form name="inserisciForm" id="elencoForm" method="post" action="" class="form-horizontal" enctype="multipart/form-data">
						<div style="padding-top: 5em;">
							<m:panel title="Bandi selezionati" collapsible="false" id="info">
								<m:textarea id="denominazioneBandoSelezionato" disabled="true" label="Copia da bando: " name="denominazioneBandoSelezionato">${denominazioneBandoSelezionato}</m:textarea>
								<c:if test="${!isNuovoBando}">
									<m:textarea id="denominazioneBandoObiettivo" disabled="true" label="a bando: " name="denominazioneBandoObiettivo">${denominazioneBandoObiettivo}</m:textarea>
								</c:if>
								<c:if test="${isNuovoBando}">
									<m:textfield id="denominazioneBandoObiettivo" disabled="true" label="a nuovo bando: " name="denominazioneBandoObiettivo"
										value="${denominazioneBandoObiettivo}"></m:textfield>
								</c:if>
							</m:panel>
						</div>
						<input type="hidden" value="${idBandoSelezionato}" name="idBandoSelezionato">

						<c:choose>
							<c:when test="${isCopiaSuStessoBando}">
								<m:select id="idOggettoPartenza" selectedValue="${idOggettoPartenza}" valueProperty="id" textProperty="descrizione" list="${elencoOggetti}"
									name="idOggettoPartenza" label="Oggetto di partenza: *" preferRequestValues="${prfReqValues}"></m:select>
								<m:select id="idOggettoDestinazione" selectedValue="${idOggettoDestinazione}" valueProperty="id" textProperty="descrizione"
									list="${elencoOggetti}" name="idOggettoDestinazione" label="Oggetto destinazione: *" preferRequestValues="${prfReqValues}"></m:select>

									<div class="col-sm-12 ">
									<div class="puls-group" style="margin-top: 2em">
										<div class="pull-left">
											<button type="button" onclick="forwardToPage('duplica_${idBandoSelezionato}.do');" class="btn btn-default">indietro</button>
										</div>
										<div class="pull-right">
											<button type="button" name="conferma" id="conferma" onclick="conferma2()" class="btn btn-primary">avanti</button>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="stdMessagePanel" id="divErrore" class="" style="display: none;">
									<div class="alert alert-danger">
										<strong>Attenzione!</strong><br /> Selezionare almeno un oggetto.
									</div>
								</div>
								<br>
								<br>

								<%@include file="/WEB-INF/jsp/cruscottobandi/duplicabando/dualListOggetti.jsp"%>
								<div class="col-sm-12 ">
									<c:if test="${!isCopiaSuStessoBando}">
										<em>* per gli oggetti contrassegnati con asterisco verranno copiate solo le configurazioni mancanti</em>
									</c:if>
									<div class="puls-group" style="margin-top: 2em">
										<div class="pull-left">
											<button type="button" onclick="forwardToPage('duplica_${idBandoSelezionato}.do');" class="btn btn-default">indietro</button>
										</div>
										<div class="pull-right">
											<button type="button" name="conferma" id="conferma" onclick="conferma()" class="btn btn-primary">avanti</button>
										</div>
									</div>
								</div>
							</c:otherwise>
						</c:choose>

					</form>
				</div>
			</div>
		</m:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script src="/${sessionScope.webContext}/bootstrap-toggle/js/bootstrap-toggle.js"></script>
	<script src="../js/dual-list-box.js"></script>
	<script type="text/javascript">
		$('#oggettiDualList').DualListBox();

		//CAMBIO LABEL DUAL-LIST
		$('body')
				.on(
						'Nemboconfpratiche.dualistbox.loaded',
						function() {
							$(".unselected-title")
									.html(
											"<span class='unselected-title'>Oggetti bando selezionato</span>")
							$(".selected-title")
									.html(
											"<span class='unselected-title'>Oggetti bando obiettivo</span>")
						});

		$('input[data-toggle="bs-toggle"]').bootstrapToggle();

		function conferma() {
			var formData = {
				idOggetti : new Array(),
				prosegui : 'true'
			};
			$('#dual-list-box-Oggetti #selectedListHidden option').each(
					function(index) {
						formData.idOggetti[index] = $(this).val();
					});
			$.ajax({
				type : "POST",
				url : 'selezionaOggetti.do',
				data : $.param(formData, true),
				dataType : "text",
				async : false,
				success : function(data) {
					if (data.indexOf("error") != -1)
						$("#divErrore").show();
					else {
						$('#elencoForm').attr('action', 'goToStep3.do')
								.submit();
					}
				}
			});
		}


		function conferma2() {

			$('#elencoForm').attr('action', 'goToStep3CopiaOggettiStessoBando.do')
			.submit();
			}
	</script>
	<script src="../js/textSearch.js"></script>

	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />