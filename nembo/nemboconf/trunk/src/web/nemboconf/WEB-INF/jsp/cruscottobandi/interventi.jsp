<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<li class="active">Inserisci (Interventi) </li>
				</ul>
			</div>
		</div>
	</div>
	<p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="INTERVENTI" cu="CU-NEMBO-015-I"></p:CruscottoBandiHeader>
	
	<div class="container-fluid" id="content" style="margin-bottom:3em">
		<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
				<table id="interventiTable" class="table table-hover table-striped table-bordered tableBlueTh">
				<colgroup>
				 <col width="8%">
				 <col width="15%">
				 <col width="15%">
				 <col width="10%">
				 <col width="10%">
				 <col width="14%">
				 <col width="14%">
				 <col width="14%">
				</colgroup>
					<thead>
						<tr>
							<th>
								<c:if test="${modificaAbilitata  && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
									<a class="ico24 ico_modify" href="modificainterventi.do" title="Gestisci interventi"></a>
								</c:if>
									<a href="#" title="esporta in excel" onclick="forwardToPage('downloadExcel.xls?id=${idBando}')" class="ico24 ico_excel"></a>
							</th>
							<th data-property="tipoIntervento" class="sortable">Tipo intervento</th>
							<th data-property="descrIntervento" class="sortable">Descrizione intervento</th>
							<th data-property="locIntervento" class="sortable">Localizzazione dell'intervento</th>
							<th data-property="operazione" class="sortable">Operazione</th>
							<th>Costo Unit Min</th>
							<th>Costo Unit Max</th>
							<th>Dato / UM</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${elenco}" var="e" varStatus="idx">
							<tr>
								<td >
									<c:if test="${modificaAbilitata  && utenteAbilitazioni.dirittoAccessoPrincipale=='W'}">
										<a onclick="return eliminaIntervento('${e.idDescrizioneIntervento}','${e.idLivello}')" class="ico24 ico_trash" href="elimina_Intervento_${e.idDescrizioneIntervento}.do"></a>
									</c:if>
								</td>
							  	<td data-property="tipoIntervento"  data-value="${e.tipoIntervento}"><c:out value="${e.tipoIntervento}"></c:out></td>
							  	<td data-property="descrIntervento"  data-value="${e.descrIntervento}"><c:out value="${e.descrIntervento}"></c:out></td>
							  	<td data-property="locIntervento" data-value="${e.localizzazione}"><c:out value="${e.localizzazione}"></c:out></td>
							  	<td data-property="operazione"  data-value="${e.operazione}"><c:out value="${e.operazione}"></c:out></td>
							  	
							  	<c:choose>
							  		<c:when test="${e.flagGestioneCostoUnitario == 'S'}">
  									  	<td><m:textfield disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" id="costoUnitMin_${e.idDescrizioneIntervento}" name="costoUnitMin_${e.idDescrizioneIntervento}" value="${e.costoUnitMinimoStr}" type="euro" preferRequestValues="${fromRequest}"></m:textfield></td>
										<td><m:textfield disabled="${!modificaAbilitata || utenteAbilitazioni.dirittoAccessoPrincipale=='R'}" id="costoUnitMax_${e.idDescrizioneIntervento}" name="costoUnitMax_${e.idDescrizioneIntervento}" value="${e.costoUnitMassimoStr}" type="euro" preferRequestValues="${fromRequest}"></m:textfield></td>
							  		</c:when>
							  		<c:otherwise>
  									  	<td></td>
										<td></td>
							  		</c:otherwise>
							  	</c:choose>
							  	<td>
									<c:forEach items="${e.infoMisurazioni}" var="a" varStatus="idx">
										<c:if test="${idx.index>0}"> <br> </c:if>
										<c:out value="${a.descMisurazione}"></c:out>
										<c:if test="${a.codiceUnitaMisura != null && a.codiceUnitaMisura != 'NO_MISURA'}">
											&nbsp;<c:out value="${a.codiceUnitaMisura}"></c:out>
										</c:if>
									</c:forEach>  	
						  	  </td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12"> 
						<button type="button" onclick="forwardToPage('filtrobeneficiari.do');" class="btn btn-default">indietro</button>
						<p:abilitazione dirittoAccessoMinimo="W">
						<c:choose>
							<c:when test="${modificaAbilitata}">
								<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma e prosegui</button>
							</c:when>
							<c:otherwise>
									<button type="button" name="conferma" onclick="window.location='oggetti.do';" id="conferma" class="btn btn-primary pull-right">prosegui</button>
							</c:otherwise>
						</c:choose>
						</p:abilitazione>
					</div>
				</div>
			</form:form>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfsort.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<script type="text/javascript">
	function eliminaIntervento(idDescrizioneIntervento, idLivello)
    {
        return openPageInPopup('eliminaintervento_'+idLivello+'_'+idDescrizioneIntervento+'.do','dlgEliminaIntervento','Elimina Intervento', 'modal-large');
    }
	</script>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>