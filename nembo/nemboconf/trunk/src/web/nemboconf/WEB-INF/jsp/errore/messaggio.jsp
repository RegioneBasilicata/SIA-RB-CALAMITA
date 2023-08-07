<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--INIZIO HEAD-->
<r:include resourceProvider="portal"
	url="/ris/resources/application/nemboconf/include/head.html" />
<!--FINE HEAD-->
</head>
<body>
	<!-- PAGE (OBBLIGATORIO) -->
	<div id="page">
		<!-- 2 -->
		<r:include resourceProvider="portal"
			url="/ris/resources/global/include/portal_header.html" />

		<!-- APPICATION AREA (OBBLIGATORIO) -->
		<div id="applicationArea">

			<!--area userInfoPanel (dati utente + esci)-->
			<!-- 3 -->
			<r:include resourceProvider="portal"
				url="/ris/resources/application/nemboconf/include/application_header.html" />

			<!-- FINE HEADER (parte comune a tutto l'applicativo) -->

			<!-- PANNELLO DEI CONTENUTI -->

			<div id="contentPanel">

				<!--area menu verticale-->

				<div id="northPanel">
					<div class="wrapper">
						<!-- INIZIO CONTENUTO NORTH PANEL -->

						<!--area filo arianna + link help e contatti-->
						<div id="contextPanel">
							<div id="breadCrumbPanel">
								<span class="element">login</span> <span class="separator">
									&raquo; </span> <span id="currentElement" class="active">selezione
									del ruolo</span>
							</div>
							<!--area searchBox-->

							<!--area menu orizzontale-->

							<!-- FINE CONTENUTO NORTH PANEL -->
						</div>
					</div>
					<!--/northPanel-->
					<div id="CenterWrapper" class="floatWrapper">
						<div id="centerPanel">
							<div class="wrapper">
								<!-- INZIO CONTENUTO CENTER PANEL -->
								<h3>${titolo}</h3>
								<div class="pageContainer">
									<h2>${messaggio}</h2>
									<div class="commandPanel">
										<div class="button left">
											<span><input type="submit" value="conferma" /></span>
										</div>
										<div class="button right">
											<span><input type="button" value="annulla"
												onclick="window.location.href='/'" /></span>
										</div>
									</div>
								</div>
								<!-- FINE CONTENUTO CENTER PANEL -->
							</div>
						</div>
					</div>
				</div>

				<!-- FINE PANNELLO DEI CONTENUTI -->


				<!-- footer (con remincl) (parte comune a tutto l'applicativo)  -->

				<r:include resourceProvider="portal"
					url="/ris/resources/global/include/portal_footer.html" />

			</div>
			<!-- FINE APPICATION AREA (OBBLIGATORIO) -->

		</div>
		<!-- FINE PAGE (OBBLIGATORIO) -->
</body>
</html>