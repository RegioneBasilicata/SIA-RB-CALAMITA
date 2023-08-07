<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div style="margin-bottom:4em">
	<div class="container-fluid">
		<div class="alert alert-info" role="alert">
		Gestione Oggetto: <b>${descrOggetto}</b>
		</div>
	</div>
	<div class="container-fluid">
		<table class="table table-hover table-striped table-bordered tableBlueTh" >
			<thead>
				<tr>
					<th>Ordine</th>
					<th>Codice Quadro</th>
					<th>Descrizione Quadro</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${elenco}" var="a" varStatus="al">
					<tr>
						<td class="tdModificaOrdine">
	    					<c:choose>
	    						<c:when test="${al.index == 0}">
	    							<span  class="pull-right"><a href="#" class="ico24 ico_down" title="Sposta gi&ugrave;" onclick="return modificaOrdine('${idOggetto}','${a.idQuadro}','${elenco[al.index+1].idQuadro}')"></a></span> 
				    			</c:when>
	    						<c:when test="${al.index == (fn:length(elenco) -1) }">
	    							<span  class="pull-left" style="margin-left:1em"><a href="#" class="ico24 ico_up" title="Sposta su" onclick="return modificaOrdine('${idOggetto}','${a.idQuadro}', '${elenco[al.index-1].idQuadro}')"></a></span> 
				    			</c:when>
				    			<c:otherwise>
				    				<div>
		    							<span  class="pull-right"><a href="#" class="ico24 ico_down" title="Sposta gi&ugrave;" onclick="return modificaOrdine('${idOggetto}','${a.idQuadro}','${elenco[al.index+1].idQuadro}')"></a></span> 
		    							<span  class="pull-right"><a href="#" class="ico24 ico_up" title="Sposta su" onclick="return modificaOrdine('${idOggetto}','${a.idQuadro}', '${elenco[al.index-1].idQuadro}')"></a></span>
	    							</div> 
				    			</c:otherwise>
	    					</c:choose>
		    			</td>
						<td>${a.codice}</td>
						<td>${a.descrizione}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>      
	<div class="col-sm-12">
	    <button type="button" onclick="$('#dlgParametri').modal('hide');" class="btn btn-default">indietro</button>
	</div>
</div>		
					
		
