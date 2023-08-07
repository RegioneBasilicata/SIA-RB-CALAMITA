<form action="eliminaPrincipio_${idLivello}_${idPrincipioSelezione}.do" method="post" class="form-horizontal" id="confermaEliminaForm" style="margin-top: 1em">
	<input type="hidden" id="idPrincipioSelezione" value="${idPrincipioSelezione}">
	<input type="hidden" id="idLivello" value="${idLivello}">
	<div>
		${message}
		<div class="form-group puls-group" style="margin-top: 1.5em">
			<div class="col-sm-12">
				<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
				<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
			</div>
		</div>
	</div>
	
</form>