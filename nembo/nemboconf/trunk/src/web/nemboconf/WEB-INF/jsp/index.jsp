<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<li class="active">Home</li>
				</ul>
			</div>
		</div>
	</div>
	
	
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
		<c:if test='${catalogoMisure}'>
                    <h2 class="primo">
                      <a href="catalogomisure/dettaglio.do">Catalogo misure</a>
                    </h2>
                    <p>Funzionalità per la definizione del legame tra misure/sottomisure, operazioni e focus area.</p>
                  </c:if>
                  <c:if test='${pianoRegionale}'>
                    <h2 class="primo">
                      <a href="pianofinanziario/storico.do">Piano finanziario</a>
                    </h2>
                    <p>Funzionalità per la consultazione e la modifica del piano finanziario, mediante l'indicazione
                      dei budget assegnati alle operazioni in riferimento a ciascuna focus area associata.</p>
                  </c:if>
                  <c:if test="${gestioneeventi}">
                  	 <h2 class="primo">
                  	 	<a href="gestioneeventi/index.do">Gestione eventi</a>
                  	 </h2>
                  	 <p>Funzionalit&agrave; per la consultazione, creazione e modifica degli eventi calamitosi per i quali 
                  	 potranno essere aperti dei bandi.</p>
                  </c:if>
                  <c:if test='${cruscottoBandi}'>
	                  <h2 class="primo">
	                    <a href="cruscottobandi/index.do">Cruscotto bandi</a>
	                  </h2>
	                  <p>Funzionalità per la gestione della configurazione di un bando.</p>
                  </c:if>
                  <c:if test='${ambitiTematici}'>
                    <h2 class="primo">
                      <a href="ambititematici/storico.do">Ambiti Tematici</a>
                    </h2>
                    <p>Funzionalità per la consultazione e la modifica degli Ambiti Tematici e per le associazioni
                      con le relative Misure/Sotto Misure/Operazioni e la Focus Area.</p>
                  </c:if>
                  <c:if test='${monitoraggio}'>
	                  <h2 class="primo">
	                    <a href="monitoraggio/index.do">Monitoraggio del sistema</a>
	                  </h2>
	                  <p>Informazioni sullo stato del sistema, sulla versione rilasciata, sullo stato del database e dei principali servizi richiamati</p>
                  </c:if>	
                  <c:if test='${messaggistica}'>
	                  <h2 class="primo">
	                    <a href="cunembo202/index.do">Gestione messaggistica</a>
	                  </h2>
	                  <p>Visualizzazione di tutti i messaggi inviati dal gestore del sistema informativo agli utenti. In base al ruolo dell'utente connesso vengono visualizzati i messaggi a lettura obbligatoria e/o facoltativa.</p>
                  </c:if>	
                  
                  <c:if test='${configurazioneCataloghi}'>
	                  <h2 class="primo">
	                    <a href="configurazionecataloghi/index.do">Configurazione Cataloghi</a>
	                  </h2>
	                  <p>Funzionalità per la gestione dei cataloghi relativi al Nemboconf</p>
                  </c:if>	
                    <c:if test='${reportistica}'>
	                  <h2 class="primo">
	                    <a href="reportisticaIndex/index.do">Reportistica</a>
	                  </h2>
	                  <p>Funzionalità per la consultazione dei dati relativi alla reportistica</p>
                  </c:if>	
                  
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
 
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>