<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.css"> 
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.css">

<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html"/>
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html"/>
	  
	<p:utente/>
	
	<div class="container-fluid">
		<div class="row">
			<div class="moduletable">
				<ul class="breadcrumb">
					<li><a href="../index.do">Home</a> <span class="divider">/</span></li>
					<li class="active">Configurazione cataloghi</li>
				</ul>
			</div>
		</div>
	</div>
	
	<p:NavTabsConfigurazioneCataloghi activeTab="${quadroCatalogo}"></p:NavTabsConfigurazioneCataloghi>
	
	<div class="container-fluid"  id="content">
	<b:panel type="DEFAULT" cssClass="navpanel">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" name="mainForm" method="post" class="form-horizontal" >

				<c:forEach items="${tabella.jsonData['cols']}" var="col" varStatus="idx">
					<c:if test="${idx.index > 0 }"> <!-- il primo elemento è l'id della tabella -->
						<div class="form-group">
							<c:set var="isObbl" value="${col.nullable}"></c:set>
							<c:if test="${col.nullable}"><c:set var="txtObbl" value=""></c:set></c:if>
							<c:if test="${!col.nullable}"><c:set var="txtObbl" value=" *"></c:set></c:if>
							<c:choose>
								<c:when test="${col.type == 'datetime'}"> 
									<m:textfield type="date"  dataType="campodata" name="${col.id}" id="${col.id}" preferRequestValues="${preferRequestValuesTabella}" label="${commento} ${col.label}${txtObbl}"></m:textfield>
								</c:when>
								<c:when test="${col.type == 'number'}">
									<m:textfield name="${col.id}" id="${col.id}" preferRequestValues="${preferRequestValuesTabella}" label="${commento} ${col.label}${txtObbl}"></m:textfield>
								</c:when>
								<c:when test="${col.type == 'string' && col.maxSize<=300}">
									<m:textfield name="${col.id}" id="${col.id}" preferRequestValues="${preferRequestValuesTabella}" label="${commento} ${col.label}${txtObbl}"></m:textfield>
								</c:when>
								<c:when test="${col.type == 'string' && col.maxSize>300}">
									<m:textarea name="${col.id}" id="${col.id}" preferRequestValues="${preferRequestValuesTabella}" label="${commento} ${col.label}${txtObbl}" rows="3"></m:textarea>
								</c:when>
								<c:otherwise>
									<!-- tipo non gestito, tipo blob -->
								</c:otherwise>
							</c:choose>
						 </div>
					 </c:if>
				</c:forEach>
				<em>I campi contrassegnati con * sono obbligatori</em>
		
				<div class="form-group puls-group" style="margin-top:2em;margin-bottom:5em;margin-right:0.5em">
					<div class="pull-left">
						<button type="button"  onclick="history.go(-1);" class="btn btn-default">Indietro</button>
					</div>
					<div class="pull-right">  
			          <button type="submit" class="btn btn-primary">Conferma</button>
			        </div>
			    </div>
	    	</form:form>
	    </b:panel>
	</div>
		
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>

<script src="/${sessionScope.webContext}/js/Nembotableformatter.js"></script>	
<script src="/${sessionScope.webContext}/bootstrap-table/src/bootstrap-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/dist/extensions/filter/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/bootstrap-table-filter.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/ext/bs-table.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table/src/locale/bootstrap-table-it-IT.js"></script>
<script src="/${sessionScope.webContext}/bootstrap-table-filter/src/locale/bootstrap-table-filter.it-IT.js"></script>

<script>

$( document ).ready(function() {
	$('body').on('focus',"input[data-type='campodata']", function(){
	    $(this).datepicker({ dateFormat: 'dd/mm/yy' });
	});
});

$('#dettEstrazioneTable').on("post-body.bs.table", function (data) {
	$('.fixed-table-loading').remove();
	$('#dettEstrazioneCnt').show();
	$('#pleaseWait').hide();
	
});

</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>