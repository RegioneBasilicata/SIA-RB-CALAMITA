<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />
	<p:utente />
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li class="active">Monitoraggio</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container-fluid" id="build">
		<table summary="Informazioni sulla build corrente" class="table table-hover table-striped table-bordered tableBlueTh">
			<tbody>
				<tr>
					<th>Nome componente</th>
					<td>${componentName}</td>
					<th>Numero di versione</th>
					<td>${componentVersion}</td>
					<th>Data del build</th>
					<td>${builtDate}</td>
        </tr>
        <tr>
					<th>Autore del build</th>
					<td>${builtBy}</td>
					<th>Ambiente</th>
					<td>${target}</td>
					<th>Build effettuato con</th>
					<td>${antVersion} e JDK ${createdBy}</td>
				</tr>
			</tbody>
		</table>
    <c:choose>
      <c:when test="${applicativoOK}">
        <c:choose>
          <c:when test="${serviziOK}">
            <div class="alert alert-success">
              <img src="../img/success_big.png" style="width: 32px; height: 32px" /> L'applicativo ${componentName} funziona normalmente, l'accesso al database è attivo.
              Non ci sono problemi nell'accesso ai servizi applicativi
            </div>
          </c:when>
          <c:otherwise>
            <div class="alert alert-warning">
              <img src="../img/warning_big.png" style="width: 32px; height: 32px" /> L'applicativo ${componentName} funziona normalmente, l'accesso al database è attivo.
              SONO STATI RILEVATI PROBLEMI NELL'ACCESSO AI SERVIZI APPLICATIVI
            </div>
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${serviziOK}">
            <div class="alert alert-danger">
              <img src="../img/fail_big.png" style="width: 32px; height: 32px" /> L'applicativo ${componentName} NON FUNZIONA CORRETTAMENTE E NON HA ACCESSO AL DATABASE.
              Non ci sono problemi nell'accesso ai servizi applicativi
            </div>
          </c:when>
          <c:otherwise>
            <div class="alert alert-danger">
              <img src="../img/fail_big.png" style="width: 32px; height: 32px" /> L'applicativo ${componentName} NON FUNZIONA CORRETTAMENTE E NON HA ACCESSO AL
              DATABASE. SONO STATI RILEVATI PROBLEMI NELL'ACCESSO AI SERVIZI APPLICATIVI
            </div>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
		<m:panel id="servizi" title="Verifica dei servizi esterni utilizzati">
			<table summary="Verifica dei servizi esterni utilizzati" class="table table-hover table-striped table-bordered tableBlueTh">
				<thead>
					<tr>
						<th>Servizio</th>
						<th>Disponibilit&agrave;</th>
						<th>Errore</th>
					</tr>
				</thead>
				<c:forEach items="${servizi}" var="servizio">
					<tr>
						<th>${servizio[0]}</th>
						<td class="center"><img src="../img/${servizio[1]}" style="width: 32px; height: 32px" /></td>
						<td>${servizio[2]}</td>
					</tr>
				</c:forEach>
			</table>
		</m:panel>
		<m:panel id="database" title="Stato del Database" visible="${statoDB!=null}">
			<small><em><strong>N.B.</strong> L'elenco seguente contiene tutte le procedure/funzioni definite sullo schema Oracle, anche se non effettivamente utilizzate da ${componentName}, quindi
					un'eventuale presenza di errori non indica necessariamente un malfunzionamento applicativo</em><br /> <br /></small>
			<table summary="Esiti verifiche sull'applicativo" class="table table-hover table-striped table-bordered tableBlueTh">
				<thead>
					<tr>
						<th>Oggetto del database</th>
						<th>Stato</th>
					</tr>
				</thead>
				<c:forEach items="${statoDB}" var="stato">
					<tr>
						<td>${stato[0]}</td>
						<td class="center"><img src="../img/${stato[2]}" style="width: 32px; height: 32px" /></td>
					</tr>
				</c:forEach>
			</table>
		</m:panel>
		<c:if test="${pulsanteBack}">
			<a class="btn btn-primary" href="../cunembo204/index.do">Indietro</a>
			<br class="clear" />
			<br />
		</c:if>
	</div>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />