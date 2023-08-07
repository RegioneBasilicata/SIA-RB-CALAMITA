<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:error/>
<script type="text/javascript">
	function ottieniComuni()
	{
		var n=$('input[name="chkIdBandoComune"]:checked').length;
		if(n>0)
		{
			$('#n').html('Sei sicuro di voler eliminare i ' + n + '  comuni selezionati ed i relativi fogli?');
		}
		else
		{
			$('#n').html('Selezionare almeno un comune da eliminare');
			$('#btnConferma').hide();
		}
	}
</script>
<form name="formEliminazioneComuni" id="formEliminazioneComuni" method="post">
	<br/> 
	<div id="dual-div">
		<div id="divErrore"></div>
		<div style="display:inline-block; width:100%;">
			<c:choose>
			<c:when test="${eliminazioneMultipla != null && eliminazioneMultipla == true }">
				<span id="n"></span>
				<script type="text/javascript">ottieniComuni()</script>
			</c:when>
			<c:otherwise>
				Sei sicuro di voler eliminare il comune ed i relativi fogli?
				<input type="hidden" value="${idBandoComune}" name="chkIdBandoComune"/>
			</c:otherwise>
			</c:choose>
			<br/>
			<br/>
			<input type="button" class="btn btn-default" data-dismiss="modal" value="Annulla" /> 
			<input type="button" class="btn btn-primary pull-right" onclick="eliminaComuni()" value="Conferma" id="btnConferma"/> 
			
		</div>
	</div>
</form>

<script type="text/javascript">

	function eliminaComuni()
	{
		 var formName="";
		
		 if("${eliminazioneMultipla}" == null || "${eliminazioneMultipla}" == '')
		 {
		 	formName = 'formEliminazioneComuni';
		 }
		 else
		 {
		 	formName = 'mainForm';
		 }
		 _lastModalID="dlgEliminaComuni";
		 console.log(formName);
	     $.ajax({
	          type: "POST",
	          url: 'elimina_comuni.do',
	          data: $('#'+formName).serialize(),
	          dataType: "html",
	          async:false,
	          success: function(html) 
	          {
	              var COMMENT = '<success';
	              if (html != null && html.indexOf(COMMENT) >= 0) 
	              {
	                window.location.reload();
	                closeModal();          
	              } 
	              else 
	              {
	                $('#divErrore').html(html);
	                doErrorTooltip();
	              } 
	               
	          },
	          error: function(jqXHR, html, errorThrown) 
	           {
	              writeModalBodyError("Si è verificato un errore grave nell'accesso alla funzionalità di eliminazione. Se il problema persistesse si prega di contattare l'assistenza tecnica");
	           }  
	      });
		return false;
	}

</script> 