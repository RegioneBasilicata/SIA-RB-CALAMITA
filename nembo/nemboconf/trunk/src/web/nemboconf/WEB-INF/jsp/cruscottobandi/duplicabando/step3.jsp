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
			<m:stepWizard numberOfSteps="3" activeStep="3" labels="Tipo operazione/Selezione bando&&&Selezione oggetti&&&Configurazione oggetti"></m:stepWizard>

				<div style="padding-top: 3em;">
					<form name="inserisciForm" id="elencoForm" method="post" action="confermaDuplicazioneBando.do" class="form-horizontal" enctype="multipart/form-data">
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
								
								<c:if test="${isCopiaSuStessoBando}">
								<m:textfield id="denomOggettoPartenza" disabled="true" label="Da oggetto: " name="denomOggettoPartenza"
										value="${denomOggettoPartenza}"></m:textfield>
								</c:if>
							</m:panel>
						</div>
						<input type="hidden" value="${idBandoSelezionato}" name="idBandoSelezionato">
						<div class="stdMessagePanel" id="divErrore" class="" style="display: none;">
							<div class="alert alert-danger">
								<strong>Attenzione!</strong><br /> Selezionare almeno un oggetto.
							</div>
						</div>
						<br> <br>

						<div class="col-sm-12 ">
						<c:if test="${isCopiaSuStessoBando}"><c:set var="colSpan" value="3"></c:set></c:if>
						<c:if test="${!isCopiaSuStessoBando}"><c:set var="colSpan" value="4"></c:set></c:if>
						<div class="alert alert-info">
							<c:if test="${!isCopiaSuStessoBando}"><strong>I seguenti oggetti verranno copiati nel bando selezionato. Di seguito è possibile selezionare/deselezionare (dove possibile) le configurazioni facoltative da importare oltre a quelle di base.</strong></c:if>
							<c:if test="${isCopiaSuStessoBando}"><strong>Selezionare gli elementi di configurazione aggiuntivi da copiare sull'oggetto selezionato: </strong></c:if>
						</div>
						<table id="tableOggetti" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh ">
						<thead>
							<tr>
								<th>Oggetti da importare</th>
								<th>Impegni</th>
								<th>Dichiarazioni</th>
								<th>Allegati</th>
								<c:if test="${!isCopiaSuStessoBando}"><th>Testi verbali</th></c:if>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${elencoGruppiOggetti }" var="g">
								<c:if test="${g.daImportare}">
									<tr class="info"><td colspan="${colSpan}">${g.descrGruppo}<td></tr>
									<c:forEach items="${g.oggetti}" var="o">
											<c:if test="${o.daImportare }">
												<tr>
													<td>${o.descrOggetto}</td>
													<td><input class="control-label col-sm-3 pull left" <c:if test="${!o.hasImpegni}">disabled</c:if> <c:if test="${o.hasImpegni}">checked</c:if> name="flagImportaImpegni_${o.idLegameGruppoOggetto}" id="flagImportaImpegni" title="Importa impegni" type="checkbox" data-toggle="bs-toggle" /></td>
													<td><input class="control-label col-sm-3 pull left" <c:if test="${!o.hasDichiarazioni}">disabled</c:if><c:if test="${o.hasDichiarazioni}">checked</c:if> name="flagImportaDichiarazioni_${o.idLegameGruppoOggetto}" id="flagImportaDichiarazioni" title="Importa dichiarazioni" type="checkbox" data-toggle="bs-toggle" /></td>
													<td><input class="control-label col-sm-3 pull left" <c:if test="${!o.hasAllegati}">disabled</c:if> <c:if test="${o.hasAllegati}">checked</c:if> name="flagImportaAllegati_${o.idLegameGruppoOggetto}" id="flagImportaAllegati" title="Importa allegati" type="checkbox" data-toggle="bs-toggle"  /></td>
													<c:if test="${!isCopiaSuStessoBando}"><td><input class="control-label col-sm-3 pull left" <c:if test="${!o.hasTestiVerbali}">disabled</c:if><c:if test="${o.hasTestiVerbali}">checked</c:if>  name="flagImportaTesti_${o.idLegameGruppoOggetto}" id="flagImportaTesti" title="Importa testi verbali" type="checkbox" data-toggle="bs-toggle"  /></td></c:if>
												</tr>
											</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</tbody>
						</table>
							  <em>* per gli oggetti contrassegnati con asterisco verranno copiate solo le configurazioni mancanti</em>
							<div class="puls-group" style="margin-top: 2em">
								<div class="pull-left">
									<button type="button" onclick="forwardToPage('goToStep2.do');" class="btn btn-default">indietro</button>
								</div>
								<div class="pull-right">
									<button type="submit" name="conferma" id="conferma" class="btn btn-primary">conferma</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</m:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script src="/${sessionScope.webContext}/bootstrap-toggle/js/bootstrap-toggle.js"></script>
	<script src="../js/dual-list-box.js"></script>
	<script type="text/javascript">

		$('input[data-toggle="bs-toggle"]').bootstrapToggle();

	</script>

	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />