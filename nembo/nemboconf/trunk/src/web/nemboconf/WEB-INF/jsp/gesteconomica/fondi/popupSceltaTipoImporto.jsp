<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form  method="post" class="form-horizontal" id="sceltaimporto" style="margin-top: 1em">
	<div class="form-group puls-group" style="margin-top: 1.5em;margin-left:0em;margin-right:1em">
		<input type="hidden" name="idBando" id="idBando" value="">
		<c:if test="${msgErrore != null}">
				<div class="stdMessagePanel" style="margin-top:1em">
					<div class="alert alert-danger">
						<p>
							<strong>Attenzione!</strong><br />
							<c:out value="${msgErrore}" escapeXml="false"></c:out>
						</p>
					</div>
				</div>
				
		</c:if>
		<c:if test="${elenco != null}">
			<table class="myovertable table table-hover table-condensed table-bordered">
			<tbody>
				<c:forEach items="${elenco}" var="a">
					<tr>
						<td><a class="icon-circle-arrow-right icon-large" 
							   href="confermasceltatipoimporto_${a.idTipoImporto}.do"></a>
						</td>
						<td><c:out value="${a.descrizione}"></c:out> </td>
					</tr>			
				</c:forEach>
			</tbody>
			</table>
		</c:if>
		<c:if test="${msgErrore != null}">
		<div class="modal-footer"> 
		    <div class="puls-group" style="margin-top:1em">
		      <div class="pull-right">  
		        <button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
		      </div>
	    	</div>                                                                                                  				
	                                            				
	    </div> 
		</c:if>
	</div>
</form>

<script type="text/javascript">

</script>