<form action="confermaeliminarow_${quadroCatalogo}_${id}.do" method="post" class="form-horizontal" id="confermaEliminaForm" style="margin-top: 1em" >
	${message}
	<input type="hidden" id="quadroCatalogo" value="${quadroCatalogo}">
	<input type="hidden" id="id" value="${id}">
	<div class="form-group puls-group" style="margin-top: 1.5em">
		<div class="col-sm-12">
			<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
			<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
		</div>
	</div>
</form>