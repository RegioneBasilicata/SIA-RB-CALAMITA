<form action="confermaEliminaTipoFiliera_${idTipoFiliera}.do" method="post" class="form-horizontal" id="confermaEliminaForm" style="margin-top: 1em" >
	${message}
	<input type="hidden" id="idTipoFiliera" value="${idTipoFiliera}">

	<div class="form-group puls-group" style="margin-top: 1.5em">
		<div class="col-sm-12">
			<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
			<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
		</div>
	</div>
</form>