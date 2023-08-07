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
					<li class="active">Criteri selezione (modifica) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="CRITERI_SELEZIONE" cu="CU-NEMBO-015-CS"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
			<b:error />
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
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
			<input type="hidden" name="selectedValues" id="selectedValues" value="" >
				<div id="dual-list-box1" class="form-group row">
		            	<select id="criteriDualList" style="display: none" multiple="multiple" data-title="Criteri"  data-source="loadCriteriDisponibili.json"
		                data-toggle = "true"
		                data-value="idLivelloCriterio" data-text="criterioDiSelEsteso" data-sourceselected="loadCriteriSelezionati.json"
		                data-addcombo="true" data-labelcombo="Principio di selezione" data-labelfilter="Filtra Criteri Selezione"
		         ></select>
		        </div>	
				
				
				<div class="form-group puls-group" style="margin-top: 2em">
				<div class="col-sm-12">
					<button type="button" onclick="forwardToPage('criteriSelezione.do');"
						class="btn btn-default">indietro</button>
					<button type="submit" name="conferma" id="conferma"
						class="btn btn-primary pull-right" onclick="postData();return false;">conferma</button>
				</div>
			</div>
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
$('#criteriDualList').DualListBox();

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