<%@page import="it.csi.nembo.nembopratiche.util.NemboConstants"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nembopratiche.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/head.html" />
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/${sessionScope.headerProcedimento}" />
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li class="active">Home</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container-fluid" id="content" style="margin-bottom: 3em">
		<m:panel id="panelMessaggi" title="Elenco messaggi con lettura obbligatoria rivolti agli utenti del Sistema Informativo Agricoltura del TOBECONFIG">
			<h3>Sono presenti alcuni messaggi di cui bisogna necessariamente prendere visione prima di accedere all'applicativo</h3>
			<table class="table table-hover table-striped table-bordered tableBlueTh">
				<thead>
					<tr>
						<th class="center">Titolo</th>
						<th class="center">Data inserimento</th>
						<th class="center">Allegati</th>
					</tr>
				</thead>
				<c:forEach items="${messaggi}" var="m">
					<tr>
						<td><a href="dettaglio_${m.idElencoMessaggi}.do" onclick="return openPageInPopup('dettaglio_${m.idElencoMessaggi}.do','dlgMessaggio','Visualizzazione messaggio','modal-lg',false)">${m.titolo}</a></td>
						<td class="center"><fmt:formatDate value="${m.dataInizioValidita}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
						<td class="center">${m.conAllegati?"Si":"No"}</td>
					</tr>
				</c:forEach>
			</table>
		</m:panel>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/footer.html" />

	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />