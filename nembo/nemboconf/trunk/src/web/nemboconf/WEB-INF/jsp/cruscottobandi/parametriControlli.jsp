	<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
	<p:messaggistica/><div class="container-fluid" id="content" style="margin-bottom:3em">
	<br/>
	<b:error />
			 <c:if test="${msgErrore != null}">
             	<div class="stdMessagePanel">	
                         <div class="alert alert-danger ">
                             <p><strong>Attenzione!</strong><br>
                            <c:out value="${msgErrore}"></c:out>
                         </div>
                     </div>
             </c:if>
		
		<!-- testata ini -->
		<div class="container-fluid">
			<div class="panel-group" id="accordion">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<span><b>Parametri aggiuntivi relativi al controllo:</b> <c:out value="${descrizione}"></c:out></span><br>
						<span><b>Dati tecnici del parametro:</b> <c:out value="${datiTecnici}"></c:out></span><br>
					</div>
				</div>
			</div>
		</div>
		<!-- testata fine -->
			<c:if test="${bandoAttivo == null}">
			<div class="container-fluid" >
				<m:panel id="importaMultiplo" title="Importa"  startOpened="false">
					<div class="container-fluid" id="importa">
			    		<div class="col-sm-12">
			    			<p>Importa i valori incollandoli in questa sezione.<br>I valori devono essere separati da: <strong>a capo</strong>, <strong>virgola</strong> oppure <strong>punto e virgola</strong>.</p>
			    		</div>
			    		<div class="col-sm-12" style="margin-top:1em">
				    		<m:textarea name="testoValoriParametri" id="testoValoriParametri" rows="15"></m:textarea>
				    	</div>
			    		<div class="col-sm-12 puls-group" style="margin-top:1em">
					      <div class="pull-right">  
					        <button type="button" onclick="popolaValori();$('#content_importaMultiplo').removeClass('in');" class="btn btn-primary">Importa</button>
					      </div>
				    	</div> 
			    	</div>
				</m:panel>
				</div>
			</c:if>
			<form:form action="" modelAttribute="" id="mainFormParametri" method="post" class="form-horizontal" style="margin-top:2em">
				<div class="container-fluid" style="max-height:500px;overflow-y:auto">
				<table id="tParametro" class="table table-hover table-striped table-bordered tableBlueTh" >
					<colgroup>
						<col width="5%">
						<col width="95%">
					</colgroup>
					<thead>
						<tr> 
							<th>
							<c:if test="${bandoAttivo == null}">
								<a onclick="aggiungiParametro();return false;" class="ico24 ico_add" href="#"></a>
							</c:if>
							</th>  
							<th>Valore</th> 
						</tr>
					</thead>
					<tbody>
					  <c:forEach items="${parametri}" var="e" varStatus="st">
					  	<tr class="nhRow" data-index="${st.index + 1}">
						  	<td>
						  		<c:if test="${bandoAttivo == null}">
						  			<a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="elimina_parametro.do"></a>
						  		</c:if>
						  	</td>
						  	<td>
						  	
						  	<c:choose>
						  		<c:when test="${bandoAttivo == null}">
						  			<b:textfield id="parametro_${st.index + 1}" name="parametri" value="${e.descrizione}" controlSize="12"></b:textfield>
						  		</c:when>
						  		<c:otherwise>
						  			<c:out value="${e.descrizione}"></c:out>
						  		</c:otherwise>
						  	</c:choose>
						  	</td>
					  	</tr>
					  </c:forEach>
					  <tr class="hRow"  style="display: none">
						  	<td><a onclick="$(this).closest('tr').remove();return false;" class="ico24 ico_trash" href="#"></a></td>
						  	<td><b:textfield id="parametro_$$index" name="parametri" value="" controlSize="12"></b:textfield> </td>
					  	</tr>
					</tbody>
				</table>
				</div>	
						 		    
				<div class="form-group puls-group" style="margin-top: 2em">
					<div class="col-sm-12">
						<button type="button" onclick="$('#dlgParametri').modal('hide');" class="btn btn-default">indietro</button>
						<c:if test="${bandoAttivo == null}">
						<p:abilitazione dirittoAccessoMinimo="W">
							<button type="button"  onclick="ajaxConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
						</p:abilitazione>	
						</c:if>	
					</div>
				</div>
			</form:form>
	</div>

<script type="text/javascript">
	function popolaValori()
	{
		var testo = $('#testoValoriParametri').val();
		if( (testo != 'undefined') && (testo.trim().length > 0 ) )
		{
			if(testo.indexOf(',') >= 0) { elabTestoCodici(testo, ",");}
			else if(testo.indexOf(';') >= 0) { elabTestoCodici(testo, ";");}
			else { elabTestoCodici(testo, "\n");}
		}
		$('#testoValoriParametri').val('');
	}
	
	function elabTestoCodici(testo, separatore)
	{
		$.each(testo.split(separatore), function(index, value) {
			if( (value != 'undefined') && (value.trim().length > 0 ) )
			{
				aggiungiParametro();
				$('input[name="parametri"]').last().val(value);
			}
		});
	}	

	function aggiungiParametro()
	{
		var html = $('.hRow').clone().html();
		var indexNew =$('.nhRow').last().data('index') + 1;
		if(indexNew == undefined || indexNew == 'undefined' || isNaN(indexNew)){
			indexNew = 1;
		}
		html = html.replace(/\\$\\$index/g, indexNew);
		$('#tParametro tbody').append('<tr class="nhRow" data-index="'+indexNew+'">'+html+'</tr>');
		return false;
	}
	function ajaxConferma()
	{
		$.ajax({
			type : "POST",
			url : 'confermaparametri.do',
			data: $('#mainFormParametri').serialize(),
			dataType : "text",
			async : false,
			success : function(data, status) {
				$('#dlgParametri').modal('hide');
			}
		});
	}
</script>	