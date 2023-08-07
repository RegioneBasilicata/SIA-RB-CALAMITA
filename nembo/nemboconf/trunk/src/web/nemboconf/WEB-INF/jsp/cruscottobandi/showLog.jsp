<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.CruscottoBandiHeader"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">

<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />

	<p:utente />
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Log</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container-fluid" id="content" style="padding-bottom: 3em">

		<div style="display: inline-block; vertical-align: middle;" class="alert alert-info col-lg-12" role="alert">
			<div class="col-lg-10">
				Configurazione bando: <b>${denominazioneBando}</b>
			</div>
			<div class="pull-right col-lg-2" style="padding-top: 2em;">
				<form action='datiidentificativi_${idBando}.do'>
					<button type='submit' style='margin-top: -1.6em' class='btn btn-default pull-right'>Indietro</button>
				</form>
			</div>
		</div>


		<b:error />
		<form:form action="" modelAttribute="filtroLogForm" method="post">
			<input type="hidden" id="filtroLog" value='${filtroAziende.get("logTable")}'>
		</form:form>

		<h3>Log attività</h3>

		<div id="filter-bar" style="margin-bottom: 1em"></div>

		<table id="logTable" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh " data-toggle="table"
			data-url="getLog_${idBando}.json" data-undefined-text='' data-pagination="true" data-show-pagination-switch="true" data-show-toggle="true"
			data-pagination-v-align="top" data-show-columns="true" data-show-filter="true">
			<thead>
				<tr>
					<th data-field="idBandoOggetto" data-sortable="true" data-visible="false" data-switchable="false">Id Bando Oggetto</th>
					<th data-field="descrizioneAttivita" data-sortable="true">Descrizione attività</th>
					<th data-field="descrOggetto" data-sortable="true" data-visible="true">Oggetto</th>
					<th data-field="dataUltimoAggiornamentoStr" data-sortable="true">Data ultimo aggiornamento</th>
					<th data-field="utente" data-sortable="true">Utente</th>
					<th data-field="note" data-sortable="false">Note</th>
				</tr>
			</thead>
		</table>

	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script src="../js/Nemboconfcruscottobandi.js"></script>
	<script type="text/javascript">
		$(document).ready(
				function() {

					$('#logTable')
							.bootstrapTable(
									{
										queryParams : function(params) {
											var values = $('#filter-bar')
													.bootstrapTableFilter(
															'getData');
											if (!jQuery.isEmptyObject(values)) {
												var elabFilter = JSON
														.stringify(values);
												return elabFilter;
											}
											return params;
										}
									});

					$('#filter-bar').bootstrapTableFilter(
							{
								filters : [ {
									field : 'descrizioneAttivita',
									label : 'Descrizione',
									type : 'search'
								}, {
									field : 'descrOggetto',
									label : 'Oggetto',
									type : 'search'
								}, {
									field : 'dataUltimoAggiornamentoStr',
									label : 'Data',
									type : 'date'
								}, {
									field : 'utente',
									label : 'Utente',
									type : 'search'
								}, {
									field : 'note',
									label : 'Note',
									type : 'search'
								} ],
								connectTo : '#logTable',
								onSubmit : function() {
									var data = $('#filter-bar')
											.bootstrapTableFilter('getData');
									var elabFilter = JSON.stringify(data);
									$.ajax({
										type : "POST",
										url : '../session/salvaFiltri.do',
										data : "key=logTable&filtro="
												+ elabFilter,
										success : function(data) {
										}
									});
									console.log(data);
								}
							});
					var filterJSON = $('#filtroLog').val();
					if (filterJSON !== undefined && filterJSON != null
							&& filterJSON != "")
						$('#filter-bar').bootstrapTableFilter(
								"setupFilterFromJSON", filterJSON);

				});
	</script>
	<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />