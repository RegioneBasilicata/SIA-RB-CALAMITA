<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>

<m:error />
<c:choose>
	<c:when test="${nBandi>0}">
			<form method="post" class="form-horizontal" id="deleteForm" style="margin-top: 1em" action="./elimina_conferma_{idEventoCalamitoso}.do">
				
				<div class="stdMessagePanel stdMessageLoad">
					<div class="alert alert-danger">
						<p><strong>Attenzione!</strong><br/> Impossibile eliminare un evento calamitoso per il quale esistono dei bandi.</p>
					</div>
				</div>
				<button type="button" data-dismiss="modal" class="btn btn-default">Annulla</button>
				<br/>
			</form>
	</c:when>
	<c:otherwise>
		<form method="post" action="./elimina_conferma_${idEventoCalamitoso}.do" class="form-horizontal" id="eliminaForm" style="margin-top: 1em">
			  <h4>Sei sicuro di voler modificare questo evento calamitoso?</h4>
				  <div class="form-group puls-group" style="margin-top: 1.5em">
				    <div class="col-sm-12" style="padding-left:0px !important;">
				      <button type="button" data-dismiss="modal" class="btn btn-default">Annulla</button>
				      <button type="button" name="conferma" id="conferma" class="btn btn-primary pull-right" onclick="confermaElimina()">Conferma</button>
				    </div>
				    <input type="hidden" name="idEventoCalamitoso" value="${idEventoCalamitoso}" />
				  </div>
			  <script type="text/javascript">
			    function confermaElimina()
			    {
					data: $('#eliminaForm').submit();
			    }
			  </script>
			</form>
	</c:otherwise>
</c:choose>