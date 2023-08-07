<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<li><a href="../dettaglio_${idLivello}.do">catalogo misure</a> <span class="divider">/</span></li>
					<li class="active">dettaglio settori di produzione</li>
				</ul>
			</div>
		</div>
	</div> 
	
	<!-- testata ini <p:utente/>-->
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
	
	<div class="container-fluid">
		<ul class="nav nav-tabs">
			<li role="presentation"><a href="../beneficiari/dettaglio_${idLivello}.do">Beneficiari</a></li>
			<c:if test="${addInterventiToQuadri}"><li role="presentation"><a href="../interventi/dettaglio_${idLivello}.do">Interventi</a></li></c:if>
			<li role="presentation"><a href="../focusarea/dettaglio_${idLivello}.do">Focus Area</a></li>
			<li role="presentation" class="active"><a href="#"  onclick="return false;">Settori di produzione</a></li>
			<li role="presentation"><a href="../criteriDiSelezione/dettaglio_${idLivello}.do">Criteri di selezione</a></li>			
		</ul>	
	</div>
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<m:panel id="panelSettProduzione">
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
		<table class="table table-hover table-striped table-bordered tableBlueTh">
			<thead>
				<tr> 
					<th><a class="ico24 ico_modify" href="inserisci_${idLivello}.do"></a></th>  
					<th>Descrizione</th> 
					<th>Data inizio</th>
					<th>Data fine</th> 
				</tr>
			</thead>
			<tbody>
			  <c:forEach items="${elenco}" var="e" >
			  	<tr>
				  	<td><a onclick="return eliminaSettore('${e.idSettore}','${idLivello}')" class="ico24 ico_trash" href="elimina_settore_${e.idSettore}.do"></a></td>
				  	<td><c:out value="${e.descrizione}"></c:out></td>	
				  	<td><c:out value="${e.dataInizioStr}"></c:out></td>				  	
				    <td><c:out value="${e.dataFineStr}"></c:out></td>				  	
			  	</tr>
			  </c:forEach>
			</tbody>
		</table>
		</m:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script type="text/javascript">
	function eliminaSettore(idSettore, idLivello)
    {
        return openPageInPopup('eliminaSettore_'+idSettore+'_'+idLivello+'.do','dlgEliminaSettore','Elimina Settore', 'modal-large');
    }
	</script>
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>