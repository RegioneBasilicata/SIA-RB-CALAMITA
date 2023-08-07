<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css"> 
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">
<style>
.ammComp {width: 20em;}
</style>
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
	  
	<p:utente/>
	
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li class="active">Gestione eventi</li>
				</ul>
			</div>
		</div>
	</div>
	
	<form action="${azione}" method="post" >
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<h3>Inserisci evento calamitoso</h3>	
		<div id="filter-bar" style="margin-bottom:1em"> </div>
		<table id="elencoEventi" class="table table-hover table-striped table-bordered tableBlueTh "
			>
			  
			<thead>
	        	<tr>
	        		<th>Descrizione<br/>Evento</th>
	        		<th>Data<br/>Evento</th>
	        		<th>Categoria<br/>evento</th>
	        	</tr>
		    </thead>
		    <tbody>
		    	<c:if test="${isModifica != null && isModifica==true}">
			    	<tr>
		        		<td><m:textfield id="txtDescrizioneEvento" name="txtDescrizioneEvento" preferRequestValues="${preferRequest}" value="${evento.descEvento}"></m:textfield></td>
		        		<td>
		        			<m:textfield name="txtDataEvento" id="txtDataEvento" type="date" preferRequestValues="${preferRequest}" value="${evento.dataEventoStr}"></m:textfield>
		        		</td>
		        		<td><m:select id="slcCategoriaEvento" name="slcCategoriaEvento" list="${listCategoriaEvento}" selectedValue="${evento.idCategoriaEvento}" disabled="true"></m:select></td>
		        	</tr>
		    	</c:if>
		    	<c:if test="${isModifica == null}">
		    	
			    	<tr>
		        		<td><m:textfield id="txtDescrizioneEvento" name="txtDescrizioneEvento" preferRequestValues="${preferRequest}" ></m:textfield></td>
		        		<td>
		        			<m:textfield name="txtDataEvento" id="txtDataEvento" type="DATE" preferRequestValues="${preferRequest}"></m:textfield>
		        		</td>
		        		<td><m:select id="slcCategoriaEvento" name="slcCategoriaEvento" list="${listCategoriaEvento}" preferRequestValues="${preferRequest}"></m:select></td>
		        	</tr>
		    	</c:if>
		    </tbody>
		</table>
		<input type="submit" class="btn btn-primary pull-right" value="Conferma"></input>
		<a href="./index.do" class="btn btn-default" >Indietro</a>
	</div>
	</form>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>

<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>	
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>	
<script type="text/javascript">
$( document ).ready(function() {
		$("input[name='txtDataEvento']").datepicker({ dateFormat: 'dd/mm/yy' });
});
</script>
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>