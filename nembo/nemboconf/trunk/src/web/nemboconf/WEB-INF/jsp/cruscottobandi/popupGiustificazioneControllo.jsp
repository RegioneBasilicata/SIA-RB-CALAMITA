
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<style>.tooltip-inner {
    max-width: 350px;
    font-size:13px;
}</style>
<div style="margin-bottom: 4em">

	<table summary="Bando" style="margin-top: 4px" class="myovertable table table-hover table-condensed table-bordered">
		<colgroup>
			<col width="30%" />
			<col width="70%" />		
		</colgroup>
		<tbody>
			<tr class="toggle_target_altri">
				<th>Codice controllo</th>
				<td><c:out value="${controllo.codice}"></c:out></td>
			</tr>			
			<tr class="toggle_target_altri">
				<th>Descrizione controllo</th>
				<td><c:out value="${controllo.descrizione}"></c:out></td>
			</tr>			
		</tbody>
	</table>

	<table id="tableSoluzioni" class="table table-hover table-striped table-bordered tableBlueTh">
		<thead>
			<tr>
				<th>Descrizione</th>
				<th>Note obbligatorie</th>
				<th>Allegato obbligatorio</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${soluzioni}" var="s">
				<tr>
					<td>
					<c:if test="${s.codiceIdentificativo == 'AMF' }">
						<a rel="tooltip" title="Giutificazione di tipo Antimafia: per questa giustificazione è necessario indicare i seguenti campi aggiuntivi: competente, provincia prefettura, data documento ed eventuale numero e data protocollo del documento." style="cursor:pointer;padding-left:0.8em;"> <span class="glyphicon glyphicon-list"></span></a>
					</c:if>
						<c:out value="${s.descrizione}"></c:out>
						</td>
					<td><c:out value="${s.flagNoteObbligatorie}"></c:out></td>
					<td><c:out value="${s.flagFileObbligatorio}"></c:out></td>
				</tr>
			</c:forEach>
			<tr class="hRow" style="display: none">
				<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="#"></a></td>
				<td><b:textfield id="parametro_$$index" name="parametri" value="" controlSize="12"></b:textfield></td>
			</tr>
		</tbody>
	</table>

	<div class="form-group puls-group" style="margin-top: 2em">
		<div class="col-sm-12">
			<button type="button" onclick="$('#dlgSoluzioni').modal('hide');" class="btn btn-default">Chiudi</button>
		</div>
	</div>
</div>
<script type="text/javascript">
$( document ).ready(function() {
    $("[rel='tooltip']").tooltip();
});
</script>