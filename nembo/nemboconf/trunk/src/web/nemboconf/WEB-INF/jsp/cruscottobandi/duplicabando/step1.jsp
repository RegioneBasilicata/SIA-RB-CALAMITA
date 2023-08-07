<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">
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
			<m:stepWizard numberOfSteps="3" activeStep="1" type="circle" labels="Tipo operazione/Selezione bando&&&Selezione oggetti&&&Configurazione oggetti"></m:stepWizard>

				<div style="padding-top: 3em;">
					<form name="inserisciForm" id="elencoForm" method="post" action="" class="form-horizontal" style="margin-top: 5em" enctype="multipart/form-data">
						<b:error />

						<input type="hidden" value="${idBandoSelezionato}" name="idBandoSelezionato">
						<m:textfield id="denominazioneBandoSelezionato" disabled="true" label="Bando selezionato: " name="denominazioneBandoSelezionato" value="${denominazioneBandoSelezionato}"></m:textfield>
						<m:select id="tipoOperazione" selectedValue="${tipoOperazione}" valueProperty="id" textProperty="descrizione" onchange="loadInputs()"
							list="${operazioni}" name="tipoOperazione" label="Tipo operazione: *" preferRequestValues="${prfReqValues}"></m:select>
						<div id="nomeBando" style="display: none;">
							<m:textarea id="denominazioneBando" name="denominazioneBando" label="Denominazione bando: *" preferRequestValues="${prfReqValues}">${denominazioneBando}</m:textarea>
						</div>
						<div id="selectBando" style="display: none;">
							<m:textSearch placeholder="cerca bando" label="Bando obiettivo: *" name="selezioneBando" id="selezioneBando" buttonText="cerca"
								actionUrl="cerca.do" valueProperty="idBando" textProperty="denominazione" preferRequestValues="${prfReqValues}"
								textValue="${denominazioneInserita}">
							</m:textSearch>
						</div>

						<div class="col-sm-12 ">
							<div class="puls-group" style="margin-top: 2em">
								<div class="pull-left">
									<a type="button" href="../cruscottobandi/index.do" class="btn btn-default">indietro</a>
								</div>
								<div class="pull-right">
									<button type="button" name="conferma" id="conferma" onclick="goToStep2();" class="btn btn-primary">avanti</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</m:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />

	<script type="text/javascript">
		$(document).ready(function() {
			loadInputs()
		});
		function loadInputs() {

			var idOperazione = $("#tipoOperazione").val();
			if (idOperazione !== undefined && idOperazione != null
					&& idOperazione != "")
				if (idOperazione == 0) //show input name bando
				{
					$("#nomeBando").show();
					$("#selectBando").hide();

				}
				else if (idOperazione == 2)
				{
					$("#nomeBando").hide();
					$("#selectBando").hide();
				}
				 else //show input select bando
				{
					$("#selectBando").show();
					$("#nomeBando").hide();
				}

		}
		function goToStep2() {
			$('#elencoForm').attr('action', 'goToStep2.do').submit();
		}
	</script>
	<script src="../js/textSearch.js"></script>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />