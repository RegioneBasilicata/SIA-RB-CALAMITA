<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.CruscottoBandiHeader"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<div class="container-fluid">
	<form:form action="" modelAttribute="" id="mainFormPopup" name="mainForm" method="post" class="form-horizontal">
		<input type="hidden" id="idBandoOggettoPopup" name="idBandoOggettoPopup" value="${idBandoOggetto}" />
		<input type="hidden" id="idGruppoTestiVerbali" name="idGruppoTestiVerbali" value="${idGruppoTestiVerbali}" />		
		<input type="hidden" id="idElencoCduPopup" name="idElencoCduPopup" value="${idElencoCdu}" />
		<input type="hidden" id="operationPopup" name="operationPopup" value="" />

		<div class="form-group">
			<label for="tipologia" class="col-sm-4 control-label">Selezionare il gruppo oggetto / istanza da cui copiare i testi <a data-toggle="modal"
				data-target="#oggettiModalPopup" href="#"> <span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
			</a>
			</label>
			<div class="col-sm-8">
				<div class="well well-sm">
					<p id="descrOggettoSelezionatoPopup">
						<c:out value="${descrOggettoSelezionato}"></c:out>
					</p>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="tipologia" class="col-sm-4 control-label"> Tipo documento da cui copiare i testi<a data-toggle="modal" data-target="#verbaliModalPopup" href="#">
					<span style="text-decoration: none;" class="icon-large icon-folder-open link"></span>
			</a>
			</label>
			<div class="col-sm-8">
				<div class="well well-sm">
					<p id="oggettoSelezionatoPopup">
						<c:out value="${descrTipoDocumentoSelezionato}"></c:out>
					</p>
				</div>
			</div>
		</div>
	</form:form>
</div>

<script type="text/javascript">
	
</script>