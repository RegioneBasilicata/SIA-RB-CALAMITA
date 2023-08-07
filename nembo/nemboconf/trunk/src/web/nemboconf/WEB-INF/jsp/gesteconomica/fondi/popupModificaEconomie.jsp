<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>

<div class="container-fluid" id="content" style="margin-bottom:3em">
<b:error></b:error>
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
<form:form action="" modelAttribute="" id="mainFormParametri" method="post" class="form-horizontal" style="margin-top:2em">
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
	<table id="tParametro" class="table table-hover table-striped table-bordered tableBlueTh" >
		<thead>
			<tr> 
				<th><a onclick="aggiungiEconomia();return false;" class="ico24 ico_add" href="#"></a></th>
				<th>Descrizione economia</th>  
				<th>Importo economia</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${elencoEconomie}" var="a" varStatus="st">
				<tr class="nhRow" data-index="${st.index + 1}"> 
					<td>
						<input type="hidden" name="rowIndex" value="${st.index + 1}">
						<a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="elimina_economia.do"></a>
					</td>
					<td><m:textfield id="descr_${st.index + 1}" name="descr_${st.index + 1}" value="${a.descrizione}" preferRequestValues="${prfEconomieRqValues}"></m:textfield></td>  
					<td align="right"><m:textfield id="val_${st.index + 1}" name="val_${st.index + 1}" value="${a.importoEconomiaStr}" preferRequestValues="${prfEconomieRqValues}"></m:textfield></td> 
				</tr>
			</c:forEach>
			<tr class="hRow"  style="display: none">
			  	<td>
			  		<input type="hidden" name="rowIndex" value="$$index">
			  		<a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="#"></a>
			  	</td>
				<td><m:textfield id="descr_$$index" name="descr_$$index" ></m:textfield></td>  
				<td align="right"><m:textfield id="val_$$index" name="val_$$index" ></m:textfield></td> 
		  	</tr>
		</tbody>
	</table>
	<div class="form-group puls-group" style="margin-top: 2em">
		<div class="col-sm-12">
			<button type="button" onclick="$('#dlgEconomie').modal('hide');" class="btn btn-default">indietro</button>
			<p:abilitazione dirittoAccessoMinimo="W">
				<button type="button"  onclick="ajaxConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
			</p:abilitazione>
		</div>
	</div>
</form:form>	
	
</div>	
<script type="text/javascript">
function aggiungiEconomia()
{
	var html = $('.hRow').clone().html();
	var indexNew =$('.nhRow').last().data('index') + 1;
	if(indexNew == undefined || indexNew == 'undefined' || isNaN(indexNew)){
		indexNew = 1;
	}
	html = html.replace(/\\$\\$index/g, indexNew);
	$('#tParametro tbody').append('<tr class="nhRow" data-index="'+indexNew+'">'+html+'</tr>');
	return false;
}

function ajaxConferma()
{
	$.ajax({
		type : "POST",
		url : 'modificaEconomie_'+${idRisorseLivelloBando}+'.do',
		data: $('#mainFormParametri').serialize(),
		dataType : "html",
		async : false,
		success : function(html) {
			if(html == 'SUCCESS')
			{
				window.location.href="gestfondi.do";
			}
			else
			{
				$('#dlgEconomie .modal-body').html(html);
		        doErrorTooltip();
			}
		}
	});
}


</script>