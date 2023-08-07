<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
#popupStorico .modalContent{
	width:150em;
}
#importoCorrente{
	text-align:right;
}
#importoTrascinatoCorrente{
	text-align:right;
}
</style>
<div>
  <form id="storicoForm" class="form-horizontal" action="modifica_${idPianoFinanziario}_${idLivello}.do">
		<div>
			<m:panel id="info">
				<m:textfield id="importoCorrente" disabled="true" label="Importo corrente: " name="importoCorrente" value="${importoFocusAreaDTOCorrente.importoStr}"><m:input-addon left="false">&euro;</m:input-addon></m:textfield>
				<m:textfield id="importoTrascinatoCorrente" disabled="true" label="Importo trascinato corrente: " name="importoTrascinatoCorrente" value="${importoFocusAreaDTOCorrente.importoTrascinatoStr }"><m:input-addon left="false">&euro;</m:input-addon></m:textfield>
			</m:panel>
		</div>
		<table class="table table-hover table-bordered tableBlueTh">
		<thead><tr><th>Importo</th><th>Importo trascinato</th><th>Data effettuazione<br>modifica</th><th>Motivazioni</th></tr></thead>
		<tbody>
		<c:forEach items="${storicoImporti }" var="a">
			<tr><td><div class="pull-right">${a.importoStr }&nbsp;&euro;</div> </td><td><div class="pull-right">${a.importoTrascinatoStr}&nbsp;&euro;</div></td><td>${a.infoModifica}</td><td>${a.motivazioni}</td></tr>
		</c:forEach>
		</tbody>
		</table>

 <div class="pull-right">
      <button type="button" role="button" class="btn btn-default" data-dismiss="modal">chiudi</button>
    </div>
    <br style="clear: left" /> <br />
  </form>
</div>
