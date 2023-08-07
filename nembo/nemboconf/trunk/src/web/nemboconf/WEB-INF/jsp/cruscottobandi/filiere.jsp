<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
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
					<li class="active">Inserisci (Interventi)</li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica />
	<p:CruscottoBandiHeader activeTab="FILIERE" cu="CU-NEMBO-015-F"></p:CruscottoBandiHeader>

	<div class="container-fluid" id="content" style="margin-bottom: 3em">
		<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<table id="FiliereTable" summary="Elenco Filiere" class="table table-hover table-bordered tableBlueTh" data-toggle="table"
					data-url="elencoFiliere.json" data-undefined-text=''>

					<thead>
						<tr>
							<th data-width="50" data-switchable="false" data-field="idTipoFiliera" data-formatter="deleteFormatter"><c:if
									test="${modificaAbilitata  && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
									<a class="ico24 ico_modify" href="modificaFiliere.do" title="Gestisci filiere"></a>
								</c:if></th>
							<th data-field="descrizioneTipoFiliera">Filiera</th>
							<th data-field="dataFineValiditaTipoFilieraStr">Data fine validità</th>
						</tr>
					</thead>

				</table>


				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('${goBack}');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
							<c:choose>
								<c:when test="${modificaAbilitata}">
									<button type="button" name="conferma" id="conferma" class="btn btn-primary pull-right" onclick="window.location='oggetti.do';">conferma e prosegui</button>
								</c:when>
								<c:otherwise>
									<button type="button" name="conferma" onclick="window.location='oggetti.do';" id="conferma" class="btn btn-primary pull-right">prosegui</button>
								</c:otherwise>
							</c:choose>
						</p:abilitazione>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
	<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>
	<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>
	<script src="../js/dual-list-box.js"></script>
	<script src="../js/Nemboconfsort.js"></script>
	<script src="../js/Nemboconfcruscottobandi.js"></script>
	<script type="text/javascript">
		function eliminaFiliera(idTipoFiliera) {
			return openPageInPopup('eliminaFiliera_' + idTipoFiliera + '.do',
					'dlgEliminaFiliera', 'Elimina Filiera', 'modal-large');
		}

		function deleteFormatter(rows, field) {

			var html = [];
			
			if('${modificaAbilitata}'=='true' && '${utenteAbilitazioni.dirittoAccessoPrincipale=="W"}'=='true')
			html.push('<a onclick="return eliminaFiliera('
					+ field['idTipoFiliera']
					+ ')" class="ico24 ico_trash"></a>');
			return html.join("");
		}
	</script>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />