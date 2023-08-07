<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.NavTabsEconomiaTag"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script src="/${sessionScope.webContext}/js/Nemboconfcruscottobandi.js"></script>
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
					<li class="active">Reportistica </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:navTabsEconomiaTag activeTab="<%=NavTabsEconomiaTag.TABS.GRAFICI.toString() %>"></p:navTabsEconomiaTag>
	
		<div class="container-fluid" id="content" style="margin-bottom:2em">
			<m:panel id="panelGrafici" cssClass="navpanel">
				<div id="chartContainer" align="center"></div>
				<c:forEach items="${elencoQueryBando}" var="a">
					<script type="text/javascript"> createNewChart(${a.id},'singleChart','chartContainer',false,false);</script>
				</c:forEach>
			</m:panel>
		</div>
	
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>

<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>