<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.NavTabsEconomiaTag"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
  <script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>
  	<p:utente/>
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
					<li class="active">Gestione economica</li>
				</ul>
			</div>
		</div>
	</div>
  <p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="FONDI" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	<div class="container-fluid" id="content" >
		<b:panel type="DEFAULT">
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
		<div style="margin-bottom:1em">
			<button type="button" id="creatipoimporto" onClick="return creaTipoImporto();" class="btn btn-primary">Aggiungi Tipo Importo</button>
		</div>
	
			
		<c:if test="${elencoTipiImporti != null}">
			<div style="">
			<c:forEach items="${elencoTipiImporti}" var="a">
			<br>
					<div style="margin-bottom:1em">
					<span class="spanheader">Tipo importo: ${a.descrizione}</span> 
					<span>&nbsp;&nbsp; <b>Ultimo aggiornamento:</b> <fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${a.dataUltimoAggiornamento}" />&nbsp;${a.descrizioneUtenteAggiornamento}</span>
					</div>
					<table class="table table-hover table-striped table-bordered tableBlueTh">
						<colgroup>
							<col width="3%"/>
							<col width="10%"/>
							<col width="8%"/>
							<col width="9%"/>
							<col width="7%"/>
							<col width="10%"/>
							<col width="10%"/>
							<col width="10%"/>
							<col width="8%"/>
							<col width="19%"/>
							<col width="6%"/>
						</colgroup>
						<thead>
							<tr>
								<th><a href="modificaFondi_${a.idTipoImporto}.do"  style="text-decoration: none;"><i class="ico24 ico_modify" title="Gestisci le risorse"></i></a></th>
								<th>Descrizione importo</th>
								<th>Risorse attivate</th>
								<th>Economie</th>
								<th>Risorse disponibili</th>
								<th>Data inizio validit&agrave;</th>
								<th>Data fine validit&agrave;</th>
								<th>Tipo operazione</th>
								<th>Ulteriore specializzazione</th>
								<th>Organismi delegati</th>
								<th>Fondo bloccato</th>
							</tr>
						</thead> 
						<tbody>
							<c:forEach items="${a.risorseAttivateList}" var="b">
								<tr>
									<td></td>
									<td>${b.descrizione}</td>
									<td><script type="text/javascript">document.write(euroFormatter2(${b.risorseAttivate},'',''));</script></td>
									<td>
									<a href="#" onClick="openPageInPopup('dettaglioEconomie_${b.idRisorseLivelloBando}.do','dlgEconomie','Visualizza economie',  'modal-large');" style="text-decoration: none;"><i class="icon-list icon-large" title="dettaglio"></i></a>
									<script type="text/javascript">document.write(euroFormatter2(${b.totaleEconomie},'',''));</script>
									<a href="#" onClick="openPageInPopup('modificaEconomie_${b.idRisorseLivelloBando}.do','dlgEconomie','Gestisci economie',  'modal-large');" style="text-decoration: none;"><i class="ico24 ico_modify" title="modifica"></i></a>
									</td>
									<td><script type="text/javascript">document.write(euroFormatter2(${b.totaleRisorseDisponibili},'',''));</script></td>
									<td><fmt:formatDate pattern="dd/MM/yyyy" value="${b.dataInizio}" /></td>
									<td><fmt:formatDate pattern="dd/MM/yyyy" value="${b.dataFine}" /></td>
									<td>${b.codiceLivello}</td>
									<td>${b.raggruppamento}</td>
									<td>
										<div class="row">
											<div class="col-md-2">
											<m:checkBox name="chkAmmCompetenza_${idRisorseLivelloBando}" value="S" checked="${(b.flagAmmCompetenza == 'N') ? 'false' : 'true'}" disabled="true" readonly="true"></m:checkBox>
											</div>
											<div class="col-md-10">
											<c:choose>
												<c:when test="${b.flagAmmCompetenza == 'S'}">Tutti</c:when>
												<c:otherwise>
													<m:select multiple="true" viewOptionsTitle="true" viewTitle="true" header="false"  readonly="true" id="lAmmCompetenza_${idRisorseLivelloBando}" list="${b.elencoAmmCompetenza}" valueProperty="idAmmCompetenza" textProperty="descrizioneEstesa"  name="lAmmCompetenza_${idRisorseLivelloBando}"></m:select>
												</c:otherwise>
											</c:choose>
											</div>
										</div>
									</td>
									<td><m:checkBox id="fondoBloccato_${b.idRisorseLivelloBando}" value="S" name="fondoBloccato_${b.idRisorseLivelloBando}" checked="${(b.flagBloccato == 'N') ? 'false' : 'true'}" disabled="true" readonly="true"></m:checkBox>   </td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="2" align="right"><b>Totale</b></td>
								<td><b><script type="text/javascript">document.write(euroFormatter2(${a.totaleRisorseAttivate},'',''));</script></b></td>
								<td><b><script type="text/javascript">document.write(euroFormatter2(${a.totaleEconomie},'',''));</script></b></td>
								<td><b><script type="text/javascript">document.write(euroFormatter2(${a.totaleRisorseDisponibili},'',''));</script></b></td>
								<td colspan="5"></td> 
							</tr>
						</tfoot>
					</table>
				
			</c:forEach>
		</div>
		</c:if>	
			
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>

<script type="text/javascript">
function creaTipoImporto()
{
	return openPageInPopup('sceltatipoimporto.do','dlgTipoImporto','Scegli tipo importo', 'modal-large');
} 
</script>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>