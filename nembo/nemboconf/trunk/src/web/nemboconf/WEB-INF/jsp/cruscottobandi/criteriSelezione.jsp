<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Criteri selezione </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="CRITERI_SELEZIONE" cu="CU-NEMBO-015-CS"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				
				<c:if test="${elenco == null}">
				<div class="stdMessagePanel">
					<div class="alert alert-warning">
						<p>
							Non &egrave; stato abbinato nessun criterio di selezione per il bando
						</p>
					</div>
				</div>
				</c:if>
				
				<table class="table table-hover table-striped table-bordered tableBlueTh">
					<thead>
						<tr>
							<th><a class="ico24 ico_modify" href="modificaCriteri.do" title="Gestisci Criteri slezione"></a></th>
							<th>Principio di selezione</th>
							<th>Operazione</th>
							<th>Codice</th>
							<th>Criterio selezione</th>
							<th>Specifiche</th>
							<th>Punteggio minimo</th>  
							<th>Punteggio massimo</th>
							<th>Tipo di controllo</th>
						</tr>
					</thead>
					<tbody> 
						<c:forEach items="${elenco}" var="item" varStatus="idx">
							<tr>
								<td></td>
								<td><c:out value="${item.descrPrincipiSelezione}"></c:out> </td>
								<td><c:out value="${item.codOperazione}"></c:out> </td>
								<td><c:out value="${item.codice}"></c:out> </td>
								<td><c:out value="${item.criterioDiSelezione}"></c:out> </td>
								<td><c:out value="${item.specifiche}"></c:out> </td>
								<td><c:out value="${item.punteggioMin}"></c:out> </td>
								<td><c:out value="${item.punteggioMax}"></c:out> </td>
								<td><c:out value="${item.tipoControllo}"></c:out> </td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('documentirichiesti.do');" class="btn btn-default">indietro</button>
						<button type="button" onclick="forwardToPage('testo_verbali.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">


</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>