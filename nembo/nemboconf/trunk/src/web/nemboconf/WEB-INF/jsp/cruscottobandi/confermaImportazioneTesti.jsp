<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form action=""  method="post" class="form-horizontal" id="confermaImportazioneTestiForm" style="margin-top: 1em" >
	${message}

	<div class="form-group puls-group" style="margin-top: 1.5em">
		<div class="col-sm-12">
			<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
			<c:if test="${!empty showConferma}">
			<button type="button" onclick="importaTestiDaCatalogo()" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
			</c:if>
		</div>
	</div>
</form>