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
		<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
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
		
		<table id="elencoOperazioni" class="bootstrap-table table table-hover table-striped table-bordered tableBlueTh ">
			<colgroup>
				<col width="8%">
				<col width="32%">
				<col width="20%">
				<col width="20%">
				<col width="20%">
			</colgroup>
			<thead>
	        	<tr>
					
					<th title="Codice Operazione">Codice</th>
	        		<th title="Descrizione Operazione">Descrizione</th>
	        		<th title="Percentuale minima">% minima</th>
	        		<th title="Percentuale massimo">% massima</th>
	        		<th title="Massimale di spesa">Massimale di spesa</th>
	        	</tr>
	        </thead>
	        <tbody>
	        	<c:forEach items="${elenco}" var="item">
	        		<tr>
	        			<td><c:out value="${item.codLivello}"></c:out> </td>
	        			<td><c:out value="${item.descrLivello}"></c:out> </td>
	        			<td><m:textfield id="percMinima_${item.idLivello}" name="percMinima_${item.idLivello}" value="${item.percMinimaStr}" preferRequestValues="${prefRqValues}"><m:input-addon>%</m:input-addon> </m:textfield> </td>
	        			<td><m:textfield id="percMassima_${item.idLivello}" name="percMassima_${item.idLivello}" value="${item.percMassimaStr}" preferRequestValues="${prefRqValues}"><m:input-addon>%</m:input-addon> </m:textfield> </td>
	        			<td><m:textfield id="massimaleSpesa_${item.idLivello}" name="massimaleSpesa_${item.idLivello}" value="${item.massimaleSpesaStr}" preferRequestValues="${prefRqValues}"><m:input-addon>&euro;</m:input-addon> </m:textfield> </td>
	        		</tr>	
	        	</c:forEach>
	        </tbody>
	      </table>  	
	      <div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="forwardToPage('contributo.do');" class="btn btn-default">indietro</button>
						<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
					</div>
				</div>
				</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>

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
   
  }
  else
  {
    alert("Selezionare almeno un operazione");
  }
  return false;
}

</script>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>