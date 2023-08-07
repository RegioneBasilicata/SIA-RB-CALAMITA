<%@page import="it.csi.nembo.nemboconf.util.NemboConstants"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />

<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />
  <p:utente />
  <div class="container-fluid">
    <div class="row">
      <div class="moduletable">
        <ul class="breadcrumb">
          <li><a href="../index.do">Home</a> <span class="divider">/</span></li>
          <li class="active">Piano finanziario</li>
        </ul>
      </div>
    </div>
  </div>
  <p:messaggistica/>
  <div class="container-fluid" id="content" style="margin-bottom: 3em">
    <b:error />
    <form:form action="" id="dati" method="post">
      <table class="table table-hover table-bordered tableBlueTh">
        <thead>
          <tr>
            <th style="text-align: center; width: 128px"></th>
            <th colspan="2">Versione</th>
            <th>Stato</th>
            <th>Validit&agrave; dal</th>
            <th>Creata da</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="piano" items="${elenco}">
            <tr class="riga_${piano.iter[0].idStatoPianoFinanziario}"> 
              <th>
                <form>
                  <c:if test="${piano.iter[0].idStatoPianoFinanziario==1}">
                  <span class="detail"> 
                    <a class="ico24 ico_magnify"
                    href="gestione_${piano.idPianoFinanziario}.do"></a></span>
                     
                     <p:abilitazione dirittoAccessoMinimo="R" idLivello="<%=NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO %>"> 
                    <a title="Storicizza le modifiche ed esporta in excel" href="" class="glyphicon glyphicon-floppy-save"  style ="font-size: 1.6em;text-decoration: none;"
                      onclick="return openPageInPopup('confermaConsolidamento_${piano.idPianoFinanziario}.do','consolidaPianoFinanziario', 'STORICIZZAZIONE PIANO FINANZIARIO');"></a>
                      </p:abilitazione>
                  <span class="esporta_xls" style="padding-right: 2px">
                  <a href="excel_${piano.idPianoFinanziario}.do" class="ico24 ico_excel"
                    title="esporta in excel"></a>
                <!--    <a href="ods_${piano.idPianoFinanziario}.do" class="ico24 ico_ods"
                    title="esporta in libreOffice"></a>--> 
					</span> 
                  </c:if>
                  <c:if test="${piano.iter[0].idStatoPianoFinanziario==2}">
	                  <span class="esporta_xls" style="padding-right: 2px"><a href="download_${piano.idPianoFinanziario}.do" class="ico24 ico_excel"
    	                title="visualizza excel"></a></span>
                  </c:if>
                </form>
              </th>
              <td style="border-right: 0px">${piano.descrizione}</td>
              <td style="border-left: 0px; width: 16px">
              <c:if test="${piano.iter[0].idStatoPianoFinanziario==1}">
                  <span class="freccia" id="freccia_idrif"><a href="#"
                    title="espandi/chiudi" onclick="return toggleRowpf();"></a></span>
                </c:if></td>
              <td>${piano.iter[0].descStato}</td>
              <td><fmt:formatDate value="${piano.iter[0].dataInizio}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
              <td>${piano.iter[0].descUtente}</td>
            </tr>
          </c:forEach>
          <c:if test="${empty elenco}">
            <tr>
              <th colspan="6" class="error">Nessun piano finanziaro attualmente disponibile</th>
            </tr>
          </c:if>
        </tbody>
      </table>
    </form:form>
  </div>
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
  <script type="text/javascript">
  function toggleRowpf()
  {
	$('.riga_2').toggle();
	var tmp=$('#freccia_idrif');
		
	if($('.riga_2').is(':visible'))
	{
		tmp.addClass("freccia_giu");
		tmp.removeClass("freccia");
	}else{
  		tmp.addClass("freccia");
  		tmp.removeClass("freccia_giu");

	}
  	
  	return false;
  }

  </script>
  
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />