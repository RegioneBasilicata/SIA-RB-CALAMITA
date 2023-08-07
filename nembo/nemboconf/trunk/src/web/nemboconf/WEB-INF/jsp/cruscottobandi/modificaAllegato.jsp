<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<b:error />
<div id="mainAllegatoPaopup">
<form action="modifica_allegato_${idAllegatiBando}.do" method="post" class="form-horizontal" id="modificaForm" style="margin-top: 1em">
	<b:textfield label="Descrizione *" name="descrAllegato" value="${descrAllegato}" />
	<div class="form-group puls-group" style="margin-top: 1.5em">
		<div class="col-sm-12">
			<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
			<button type="button"  onclick="ajaxConferma('${idAllegatiBando}');" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
		</div>
	</div>
</form>
</div>
<script type="text/javascript">
	function ajaxConferma(idAllegato)
	{
		$.ajax({
			type : "POST",
			url : 'modifica_allegato_'+idAllegato+'.do',
			data: $('#modificaForm').serialize(),
			dataType : "text",
			async : false,
			success : function(data) {
				if(data == '<refresh>true</refresh>')
				{
				 	window.location.href='attiPubblicati.do';
				}
				else
				{
				  $("#mainAllegatoPaopup").html(data);	
				  doErrorTooltip();	
				}
			}
		});
	}
</script>	