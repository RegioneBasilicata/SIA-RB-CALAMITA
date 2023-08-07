<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
  
  	<p:utente/>
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="../dettaglio.do">catalogo misure</a> <span class="divider">/</span></li>
					<li><a href="dettaglio_${idLivello}.do">dettaglio focus area</a> <span class="divider">/</span></li>
					<li class="active">modifica focus area</li>
				</ul>
			</div>
		</div>
	</div> 
	
	<!-- testata ini --><p:utente/>
	<div class="container-fluid">
		<div class="panel-group" id="accordion">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<span><b>Misura <c:out value="${dettagliTestata.codMisura}"></c:out>:</b> <c:out value="${dettagliTestata.descrMisura}"></c:out></span><br>
					<span><b>Sottomisura <c:out value="${dettagliTestata.codSottomisura}"></c:out>:</b> <c:out value="${dettagliTestata.descrSottomisura}"></c:out></span><br>
					<span><b>Tipo operazione <c:out value="${dettagliTestata.codTipoOperazione}"></c:out>:</b> <c:out value="${dettagliTestata.descrTipoOperazione}"></c:out></span><br>
				</div>
			</div>
		</div>
	</div>
	<!-- testata fine -->

	<c:if test="${msgErrore != null}">
		<div class="container-fluid">
			<div class="stdMessagePanel">
				<div class="alert alert-danger">
					<p>
						<strong>Attenzione!</strong><br />
						<c:out value="${msgErrore}"></c:out>
					</p>
				</div>
			</div>
		</div>
	</c:if>
	
	<div class="container-fluid">
		<ul class="nav nav-tabs">
			<li role="presentation"><a href="../beneficiari/dettaglio_${idLivello}.do">Beneficiari</a></li>
			<li role="presentation"><a href="../interventi/dettaglio_${idLivello}.do">Interventi</a></li>
			<li role="presentation" class="active"><a href="#" onclick="return false;">Focus Area</a></li>
		</ul>	
	</div>
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<m:panel id="panelModificaFA">
		<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal">
			<table class="table table-hover table-striped table-bordered tableBlueTh">
			   <colgroup>
			   	<col width="10%"/>
			   	<col width="10%"/>
			   	<col width="80%"/>
			   </colgroup>
				<thead>
					<tr> 
						<th> 
							Principale
							<a onclick="resetRadioList();return false;" class="ico24 ico_clean" href="#"></a>
						</th>
						<th>Secondaria</th> 
						<th>Focus area</th>
					</tr>
				</thead>
				<tbody>
				  <c:forEach items="${elenco}" var="e" >
				  	<tr>
					  	<td>
					  		<c:set var="ischecked" value="false"></c:set>
					  		<c:if test="${e.primaria}">
								<c:set var="ischecked" value="true"></c:set>				  						
					  		</c:if>
					  		<b:radio name="faPrimaria" value="${e.idFocusArea}" checked="${ischecked}"></b:radio>
					  	</td>
					  	<td>
					  		<c:set var="ischecked" value="false"></c:set>
					  		<c:if test="${e.secondaria}">
								<c:set var="ischecked" value="true"></c:set>				  						
					  		</c:if>
					  		<b:checkBox name="faSecondaria" value="${e.idFocusArea}" checked="${ischecked}"></b:checkBox>
					  	</td>
					  	<td><c:out value="${e.codice}"></c:out>&nbsp;-&nbsp;<c:out value="${e.descrizione}"></c:out></td>
				  	</tr>
				  </c:forEach>
				</tbody>
			</table>
			
			<div class="form-group puls-group" style="margin-top: 2em">
				<div class="col-sm-12">
					<button type="button" onclick="forwardToPage('dettaglio_${idLivello}.do');"
						class="btn btn-default">indietro</button>
					<button type="submit" name="conferma" id="conferma"
						class="btn btn-primary pull-right">conferma</button>
				</div>
			</div>
		</form:form>
		</m:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script type="text/javascript">
function resetRadioList()
{
	$('input[name="faPrimaria"]').attr('checked', false);
	return false;
}
</script>
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>