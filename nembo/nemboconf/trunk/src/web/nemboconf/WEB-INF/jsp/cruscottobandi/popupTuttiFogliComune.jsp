<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:error/>
<form name="formPopupTuttiFogliComune" id="formPopupTuttiFogliComune" method="post" action="tutti_fogli_comune_${idBandoComune}.do">
	<br/> 
	<div id="dual-div">

		<div id="divErrore"></div>
		<div style="display:inline-block; width:100%;">
			<b>Attenzione.</b> Continuando gli eventuali fogli che sono stati selezionati per il comune verranno eliminati.
			<br/>Continuare?
			<br/>
			<br/>
			<input type="button" class="btn btn-default" data-dismiss="modal" value="Annulla" onclick="uncheck(${idBandoComune});"/> 
			<input type="button" class="btn btn-primary pull-right" onclick="inserisciTuttiFogliComune()" value="Conferma"/> 
		</div>
	</div>
</form>

<script type="text/javascript">

function inserisciTuttiFogliComune()
{
	 _lastModalID="dlgTuttiFogliComune";
     $.ajax({
          type: "POST",
          url: 'tutti_fogli_comune_${idBandoComune}.do',
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

function uncheck(idBandoComune)
{
	$('#chkTuttiFogli_'+idBandoComune).prop('checked',false);
}

</script> 