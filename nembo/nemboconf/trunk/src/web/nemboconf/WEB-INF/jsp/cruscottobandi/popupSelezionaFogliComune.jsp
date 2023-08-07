<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:error/>
<form name="formPopupSelezionaFogliComune" id="formPopupSelezionaFogliComune" method="post" action="inserisci_bandi_comune_${idBandoComune}.do">
	<br/> 
	<div id="dual-div">

		<div id="divErrore"></div>
		<div style="display:inline-block; width:100%;">
			<m:textarea name="txtFogli" id="txtFogli">${fogli}</m:textarea>
			<br/>
				<b>Note</b>: inserire fogli separati da "a capo", spazi o ;
			<br/>
			<input type="button" class="btn btn-default" data-dismiss="modal" value="Annulla" onclick="check(${idBandoComune});"/> 
			<input type="button" class="btn btn-primary pull-right" onclick="inserisciFogliSelezionati()" value="Conferma"/> 
		</div>
	</div>
</form>

<script type="text/javascript">
$('#comuniDualList').DualListBox();

function inserisciFogliSelezionati()
{
	 _lastModalID="dlgInserisciFogli";
     $.ajax({
          type: "POST",
          url: 'inserisci_fogli_comune_${idBandoComune}.do',
          data: $('#formPopupSelezionaFogliComune').serialize(),
          dataType: "html",
          async:false,
          success: function(html) 
          {
              debugger;
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

function check(idBandoComune)
{
	/**
	<c:if test="${fromCheckbox == true}">
	*/
		$('#chkTuttiFogli_'+idBandoComune).prop('checked',true);
	/**
	</c:if>
	*/
}

</script> 