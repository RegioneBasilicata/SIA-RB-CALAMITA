<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>

<div class="container-fluid" id="content" style="margin-bottom:3em">
<!-- testata ini -->
		
			<div class="panel-group" id="accordion">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<span><b>Bando:</b> <c:out value="${descrBando}"></c:out></span><br>
						<span><b>Descrizione importo:</b> <c:out value="${descrTipoImporto}"></c:out></span><br>
					</div>
				</div>
			</div>
		
		<!-- testata fine -->

	<table id="tParametro" class="table table-hover table-striped table-bordered tableBlueTh" >
		<thead>
			<tr> 
				<th>Descrizione economia</th>  
				<th>Importo economia</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${elencoEconomie}" var="a">
				<tr> 
					<td>${a.descrizione}</td>  
					<td align="right">${a.importoEconomiaFormatted} &euro;</td> 
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td align="right"><b>Totale:</b></td>
				<td align="right"><b>${totaleEconomie} &euro;</b></td>
			</tr>
		</tfoot>
	</table>
	<div class="form-group puls-group" style="margin-top: 2em">
		<div class="col-sm-12">
			<button type="button" onclick="$('#dlgEconomie').modal('hide');" class="btn btn-default">indietro</button>
		</div>
	</div>
	
</div>	
<script type="text/javascript">

</script>