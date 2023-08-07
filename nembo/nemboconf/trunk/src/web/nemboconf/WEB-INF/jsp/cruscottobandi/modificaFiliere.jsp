<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<li class="active">Modifica (Filiere)</li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica />

	<p:CruscottoBandiHeader activeTab="FILIERE" cu="CU-NEMBO-015-F"></p:CruscottoBandiHeader>

	<div class="container-fluid" id="content" style="margin-bottom: 3em">
		<br />
		<b:panel title="Gestisci Filiere" collapsible="false">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<div class="stdMessageLoad">
					<div class="alert alert-info">
						<p>Caricamento elenco filiere in corso...</p>
					</div>
				</div>
				<c:if test="${msgErrore != null}">
					<div class="stdMessagePanel">
						<div class="alert alert-danger">
							<p>
								<strong>Attenzione!</strong><br />
								<c:out value="${msgErrore}"></c:out>
							</p>
						</div>
					</div>
				</c:if>
				<input type="hidden" name="selectedValues" id="selectedValues" value="">
				<div id="dual-list-box" class="form-group row">
					<select style="display: none" multiple="multiple" data-title="Filiere" data-source="loadFiliere.json" data-value="idTipoFiliera"
						data-text="descrizione" data-sourceselected="loadFiliereSelezionate.json" data-addcombo="false" 
						data-labelfilter="Tipo filiera"></select>
				</div>

				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('filiere.do');" class="btn btn-default">indietro</button>
						<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right" onclick="postData();return false;">conferma</button>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<script src="../js/dual-list-box.js"></script>
	<script type="text/javascript">
		$('select').DualListBox();

		function postData() {
			var ser = '';
			$('#selectedListHidden option').each(function(index) {
				if (index > 0)
					ser = ser + '&' + $(this).val();
				else
					ser = $(this).val();
			});
			$("#selectedValues").val(ser);
			$("#mainForm").submit();
		}
	</script>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />