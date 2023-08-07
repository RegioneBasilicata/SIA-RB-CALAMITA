<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form action="datiidentificativi.do" method="post" class="form-horizontal" id="datiidentificativi" style="margin-top: 1em">
	<div class="form-group puls-group" style="margin-top: 1.5em;margin-left:0em;margin-right:1em">
		<input type="hidden" name="idBando" id="idBando" value="">
		<table class="myovertable table table-hover table-condensed table-bordered">
		<tbody>
			<c:forEach items="${elenco}" var="a">
				<tr>
					<td><a class="icon-circle-arrow-right icon-large" 
						   href="datiidentificativi_${a.idBando}.do"></a>
					</td>
					<td><c:out value="${a.denominazione}"></c:out> </td>
				</tr>			
			</c:forEach>
		</tbody>
		</table>
	</div>
</form>

<script type="text/javascript">

</script>