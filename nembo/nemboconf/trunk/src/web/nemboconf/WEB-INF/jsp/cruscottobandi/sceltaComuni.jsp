<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.CruscottoBandiHeader"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<li class="active">Inserisci (Dati identificativi) </li>
				</ul>
			</div>
		</div>
	</div>
  <p:messaggistica/>
	<p:CruscottoBandiHeader activeTab="COMUNI" cu="CU-NEMBO-015-T"></p:CruscottoBandiHeader>
	<div class="container-fluid" id="content">
	<b:panel type="DEFAULT">
			<b:error />
			<form:form action="" modelAttribute=""  id="mainForm" name="mainForm" method="post" class="form-horizontal" >
			<table id="elencoComuni" class=" table table-hover table-striped table-bordered tableBlueTh ">
				<thead>
		        	<tr>
						<th class="col-md-1">
							<p:abilitazione dirittoAccessoMinimo="W">
								<c:if test="${modificaAbilitata == true}">
									<a href="inserisci.do" onclick="return openPageInPopup('./popup_seleziona_comuni.do','dlgInserisci','Selezione comuni','modal-large',false)" class="ico24 ico_add" title="Inserisci"></a>
									<a class="ico24 ico_trash" onclick="eliminazioneMultipla();"></a>
								</c:if>
							</p:abilitazione>
						</th>
						<th class="checkbox-field"><m:checkBox name="chkSelectAll" id="chkSelectAll" value="" onclick="selectAllCheckboxes('chkIdBandoComune',$('#chkSelectAll').prop('checked'));"></m:checkBox></th>
		        		<th class="col-md-6">Comune</th>
		        		<th class="col-md-1">Seleziona<br/>tutti i fogli</th>
		        		<th class="col-md-1"></th>
		        		<th class="col-md-3">Fogli</th>
		        	</tr>
			    </thead>
			    <tbody>
			    	<c:forEach items="${comuni}" var="comune">
				    	<tr>
				    		<td class="col-md-1">
				    			<input type="hidden" name="hiddenComune_${comune.idBandoComune}" />
				    			<c:if test="${modificaAbilitata == true}">
				    			<a class="ico24 ico_trash" onclick="eliminaComune(${comune.idBandoComune});"></a>
				    			</c:if> 
				    		</td>
				    		<td class="checkbox-field"><m:checkBox name="chkIdBandoComune" value="${comune.idBandoComune}"></m:checkBox></td>
				    		<td class="col-md-6">${comune.comuneProvincia}</td>
				    		<td class="col-md-1">
				    			<m:checkBox id="chkTuttiFogli_${comune.idBandoComune}" name="chkTuttiFogli_${comune.idBandoComune}" value="${comune.idBandoComune}" checked="${comune.tuttiFogli}" onclick="onChangeChkTuttiFogli(${comune.idBandoComune});"></m:checkBox>
				    		<td class="col-md-1">
				    			<input type="button" id="btnTuttiFogli_${comune.idBandoComune}" class="btn btn-default" onclick="inserisciFogli(${comune.idBandoComune});" value="Scelta Fogli" 
				    			<c:if test="${comune.tuttiFogli}">style="display:none;"</c:if> />
				    		</td>
				    		<td class="col-md-3"><m:select id="slcFogli_${comune.idBandoComune}" name="slcFogli_${comune.idBandoComune}" list="${comune.decodificaFogli}" multiple="true" readonly="true" header="false" visible="${!comune.tuttiFogli}" > </m:select></td>
				    	</tr>
			    	</c:forEach>
			    </tbody>
			</table>
			</form:form>
			
			<a class="btn btn-default" href="./datiidentificativi_${idBando}.do">Indietro</a>
			<a class="btn btn-primary pull-right" href="./attiPubblicati.do">Conferma e Prosegui</a>
		</b:panel>
	</div>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html"/>
<script src="../js/dual-list-box.js"></script>
<script src="../js/Nemboconfcruscottobandi.js"></script>
<c:if test="${modificaAbilitata == true}">

<script type="text/javascript">
	var idBando = "${idBando}";
	
	function onChangeChkTuttiFogli(idBandoComune)
	{	
		var unchecked = isAllCheckboxUnchecked('chkTuttiFogli_'+idBandoComune);
		var btnTuttiFogli = 'btnTuttiFogli_' + idBandoComune;
		var slcFogli = 'slcFogli_' + idBandoComune;
		if(unchecked)
		{
			openPageInPopup('./popup_seleziona_fogli_comune_' + idBandoComune + '_true.do','dlgInserisciFogli','Inserisci fogli','modal-large',false);
		}
		else
		{
			openPageInPopup('./popup_tutti_fogli_comune_' + idBandoComune + '.do','dlgTuttiFogliComune','Seleziona tutti i fogli','modal-large',false);
		}
	}
	
	function inserisciFogli(idBandoComune)
	{
		openPageInPopup('./popup_seleziona_fogli_comune_' + idBandoComune + '_false.do','dlgInserisciFogli','Inserisci fogli','modal-large',false);
	}
	
	function eliminaComune(idBandoComune)
	{
		openPageInPopup('./popup_eliminazione_comune_' + idBandoComune + '.do','dlgEliminaComuni','Elimina comuni','modal-large',false);	
	}
	
	function eliminazioneMultipla()
	{
		openPageInPopup('./popup_eliminazione_comuni.do', 'dlgEliminaComuni', 'Elimina comuni', 'modal-large', false);
	}

</script>	
</c:if>	
<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html"/>