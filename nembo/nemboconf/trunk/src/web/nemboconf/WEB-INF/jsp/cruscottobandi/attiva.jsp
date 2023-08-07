<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<li class="active">Inserisci (Attivazione oggetti) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="ATTIVAZIONE" cu="CU-NEMBO-015-ZZ"></p:CruscottoBandiHeader>
	
	
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
		<b:error />
		<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
			<table class="table table-hover table-striped table-bordered tableBlueTh">
				<thead>
					<tr>
						<th>Azione</th>
						<th>Stato Attivo / da Attivare</th>
						<th>Oggetti / Istanze previste per il Bando</th>
						<th>Utente</th>
						<th>Data attivazione</th>
						<th>Stampe</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${elenco}" var="e">
						<tr>
						  	<td colspan="6" class="bg-info"><strong><i><c:out value="${e.descrGruppo}"></c:out></i></strong></td>
						</tr>
						<c:forEach items="${e.oggetti}" var="f">
							<tr>
								<td>
									<c:choose>
										<c:when test="${f.flagAttivo == 'S'}">
											<b:checkBox name="chkOggetto_${f.idBandoOggetto}" value="${f.idBandoOggetto}" checked="true" disabled="true"></b:checkBox>
										<p:abilitazione dirittoAccessoMinimo="W">
											<a title="Disattiva oggetto" href="disattiva_${f.idBandoOggetto}.do" onclick="disattiva('${f.idBandoOggetto}');return false;">DISATTIVA</a>
											</p:abilitazione>	
										</c:when>
										<c:otherwise>
										<p:abilitazione dirittoAccessoMinimo="W">
											<a title="Attiva oggetto" href="attiva_${f.idBandoOggetto}.do" onclick="attiva('${f.idBandoOggetto}');return false;">ATTIVA</a>
											</p:abilitazione>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${f.flagAttivo == 'S'}">
											Bando attivo
										</c:when>
										<c:otherwise>
											Bando non attivo
										</c:otherwise>
									</c:choose>
								</td>
							  	<td><c:out value="${f.descrOggetto}"></c:out></td>
							  	<td></td>
							  	<td></td>
							  	<td><c:forEach items="${stampe[f.idBandoOggetto]}" var="stampa">
                    <a href="stampa_${f.idBandoOggetto}_${stampa.codiceCdu}/index.do.do" style="vertical-align:middle"><i class="${stampa.nomeIcona}" ></i> ${stampa.descTipoDocumento}</a><br />
                  </c:forEach></td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="form-group puls-group" style="margin-top: 2em">
				<div class="col-sm-12">
					<button type="button" onclick="forwardToPage('index.do');" class="btn btn-primary pull-right">fine</button>
				</div>
			</div>
		</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script type="text/javascript">
function attiva(idBandoOggetto)
{
	return openPageInPopup('attiva_'+idBandoOggetto+'.do','dlgAttivaOggetto','Attiva oggetto', 'modal-large');
}

function disattiva(idBandoOggetto)
{
	return openPageInPopup('disattiva_'+idBandoOggetto+'.do','dlgDisattivaOggetto','Disattiva oggetto', 'modal-large');
}
</script>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>