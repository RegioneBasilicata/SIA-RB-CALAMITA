<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
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
					<li class="active">Gestione grafici e reportistica </li>
				</ul>
			</div> 
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="GRAFICI" cu="CU-NEMBO-015-R"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
	<b:panel type="DEFAULT">
		<b:error />
		<c:if test="${msgErrore != null}">
             	<div class="stdMessagePanel">	
                         <div class="alert alert-danger ">
                             <p><strong>Attenzione!</strong><br>
                            <c:out value="${msgErrore}"  escapeXml="false"></c:out>
                         </div>
                     </div>
             </c:if>
             
             <c:if test="${msgSuccess != null}">
             	<div class="stdMessagePanel">	
                         <div class="alert alert-success ">
                            <c:out value="${msgSuccess}"  escapeXml="false"></c:out>
                         </div>
                     </div>
             </c:if>
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<m:panel id="pgrafici" title="Grafici previsti per il bando selezionato" collapsible="false">
					<c:choose>
						<c:when test="${elencografici != null}">
							<table id="tableGrafici" class="table table-hover table-striped table-bordered tableBlueTh">
								<thead>
									<tr>
										<th>Attivo</th>
										<th>Descrizione</th>
										<th>Visibilit&agrave;</th>
										<th>Tipo visualizzazione</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${elencografici}" var="a">
										<tr>
											<td><m:checkBox name="chkattivo" value="${a.idElencoQuery}" checked="${a.flagVisibile == 'S'}" preferRequestValues="${preferReqValues}" ></m:checkBox> </td>
											<td>
												<c:out value="${a.descrBreve}"></c:out>
												<c:if test="${a.descrEstesa != null}">
													<span class="icon icon-list" data-toggle="txt-tooltip"  data-placement="top" title="<c:out value='${a.descrEstesa}'></c:out>"></span>
												</c:if>
											</td>
											<td><c:out value="${a.descrAttori}"></c:out></td>
											<td><c:out value="${a.descrTipoVisualizzazione}"></c:out></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:when>
						<c:otherwise>
							<div class="stdMessagePanel">	
		                         <div class="alert alert-warning">		                             
		                            Non sono presenti grafici per questo bando
		                         </div>
		                     </div>	
						</c:otherwise>
					</c:choose>
				</m:panel>	
				<m:panel id="preport" title="Reportistica prevista per il bando selezionato" collapsible="false">
					<c:choose>
						<c:when test="${elencoreport != null}">
							<table id="tableGrafici" class="table table-hover table-striped table-bordered tableBlueTh">
								<thead>
									<tr>
										<th>Attivo</th>
										<th>Descrizione</th>
										<th>Visibilit&agrave;</th>
										<th>Tipo visualizzazione</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${elencoreport}" var="a">
										<tr>
											<td><m:checkBox name="chkattivo" value="${a.idElencoQuery}" checked="${a.flagVisibile == 'S'}" preferRequestValues="${preferReqValues}" ></m:checkBox> </td>
											<td>
												<c:out value="${a.descrBreve}"></c:out>
												<c:if test="${a.descrEstesa != null}">
													<span class="icon icon-list" data-toggle="txt-tooltip"  data-placement="top" title="<c:out value='${a.descrEstesa}'></c:out>"></span>
												</c:if>
											</td>
											<td><c:out value="${a.descrAttori}"></c:out></td>
											<td><c:out value="${a.descrTipoVisualizzazione}"></c:out></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:when>
						<c:otherwise>
							<div class="stdMessagePanel">	
		                         <div class="alert alert-warning">		                             
		                            Non sono presenti report per questo bando
		                         </div>
		                     </div>	
						</c:otherwise>
					</c:choose>
				</m:panel>	

			    <div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('controlliAmm.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
							<button type="button" onclick="forwardToPage('controlli.do');" name="avanti" id="avanti" class="btn btn-primary">avanti</button>
							<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right submitBtn">conferma</button>
						</p:abilitazione>
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