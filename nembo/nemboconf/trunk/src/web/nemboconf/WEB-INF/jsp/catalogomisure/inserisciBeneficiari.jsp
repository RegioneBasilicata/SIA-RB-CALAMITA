<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<li><a href="dettaglio_${idLivello}.do">dettaglio beneficiari</a> <span class="divider">/</span></li>
					<li class="active">inserisci beneficiari</li>
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

	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
			<h3>Inserisci beneficiari</h3>
			<div class="stdMessageLoad">			
		        <div class="alert alert-info">
		         <p>Caricamento elenco beneficiari in corso...</p> 
		        </div>
		    </div>
		    <c:if test="${msgErrore != null}">
				<div class="stdMessagePanel">
					<div class="alert alert-danger">
						<p>
							<strong>Attenzione!</strong><br />
							<c:out value="${msgErrore}" escapeXml="false"></c:out>
						</p>
					</div>
				</div>
			</c:if>
		    <input type="hidden" name="idLivello" id="idLivello" value="${idLivello}" >
		    <input type="hidden" name="selectedValues" id="selectedValues" value="" >
			<div id="dual-list-box" class="form-group row">
		    	<select style="display: none" multiple="multiple" data-title="Beneficiari"  data-source="loadBeneficiari_${idLivello}.json"
		                data-value="idFgTipologia" data-text="descrizione" data-sourceselected="loadBeneficiariSelezionati_${idLivello}.json"
		                data-addcombo="true" data-labelcombo="Tipologia azienda" data-labelfilter="Forma giuridica"
		         ></select>
		    </div>
	    
		    <div class="form-group puls-group" style="margin-top: 2em">
				<div class="col-sm-12">
					<button type="button" onclick="forwardToPage('dettaglio_${idLivello}.do');"
						class="btn btn-default">indietro</button>
					<button type="submit" name="conferma" id="conferma"
						class="btn btn-primary pull-right" onclick="postData();return false;">conferma</button>
				</div>
			</div>
		</form:form>	
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../../js/dual-list-box.js"></script>
<script type="text/javascript">
	$('select').DualListBox();

	function postData()
	{
		var ser = '';
		$('#selectedListHidden option').each(function(index) {
			if(index>0)
	    		ser = ser + '&' + $(this).val();
			else
				ser = $(this).val();
	    });
		$( "#selectedValues" ).val(ser);
		$( "#mainForm" ).submit();
	}
	

</script>		
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>