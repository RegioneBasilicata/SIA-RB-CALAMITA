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
	<p:CruscottoBandiHeader activeTab="CONTRIBUTO" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
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
		<form:form action="" modelAttribute="" id="mainForm" method="get" class="form-horizontal" style="margin-top:2em">
		<table id="elencoOperazioni" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh "
			  data-toggle="table"
			  data-show-switch="true"   
			  data-url="getElencoContributi.json"
			  data-undefined-text = ''>
			<thead>
	        	<tr>
					<th >
						<a href="modifica.do"  onclick="return modificaMultiplo()" style="text-decoration: none;"><i class="ico24 ico_modify" title="Modifica le operazioni selezionate"></i></a>
					</th>  
					<th data-formatter="selezioneMultiplaCheckboxFormatter">
						<input type="checkbox" name="chkSelectAll" id="chkSelectAll" onclick="selectAll()" />
					</th>
					<th data-field="codLivello" title="Codice Operazione">Codice</th>
	        		<th data-field="descrLivello" title="Descrizione Operazione" >Descrizione</th>
	        		<th data-field="percMinima" title="Percentuale di contributo minima"  data-formatter="numberFormatter2NoDecimal">% minima</th>
	        		<th data-field="percMassima" title="Percentuale di contributo massimo" data-formatter="numberFormatter2NoDecimal">% massima</th> 
	        		<th data-field="massimaleSpesa" title="Massimale di spesa" data-formatter="numberFormatter2" >&euro; Massimale di spesa</th>
	        	</tr>
	        </thead>
	      </table>  
	      </form:form>	
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
function selezioneMultiplaCheckboxFormatter($value, row, index)
{
  return '<input type="checkbox" name="idLivello" value="'+row['idLivello']+'" />';
}

function selectAll()
{
  $("input[name='idLivello']").prop("checked", $('#chkSelectAll').prop('checked'));
}

function modificaMultiplo()
{
 if ($("input[name='idLivello']:checked").length > 0)
   {
     submitFormTo($('#mainForm'), 'modifica.do');
   }
 else
  {
    alert("Selezionare almeno un operazione");
    
  }
 return false;
}

</script>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>