<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>

<form method="post" name="mainForm"  id="mainForm" action="${action}">
<div class="container-fluid">
<b:error />
  <div class="stdMessagePanel">
    <div id="messaggio" class="alert alert-info">
      <p>${messaggio}</p>
    </div>
  </div>
  <div class="stdMessagePanel" id="msgError" style="display: none">
    <div id="messaggioErrore" class="alert alert-danger">
      
    </div>
  </div>
  <m:textfield id="nomepiano" name="nomepiano" label="Nome versione storicizzata" ></m:textfield>
  <br>
  
  <div class="form-group puls-group" style="margin-top: 1.5em">
    <div class="col-sm-12">
      <button type="button" data-dismiss="modal" class="btn btn-default" onclick="return closeDialog()">Chiudi</button>
      <button type="button"  onclick="ajaxConferma();" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
    </div>
  </div>
</div>  
</form>
<script type="text/javascript">
function ajaxConferma()
{
	$("#conferma").attr("disabled", "disabled");
	$.ajax({
		type : "POST",
		url : $('#mainForm').attr('action'),
		data: $('#mainForm').serialize(),
		dataType : "html",
		async : false,
		success : function(html) {
			if(html == 'SUCCESS')
			{
				window.location.href="index.do";
			}
			else
			{
		        $('#messaggioErrore').html("<p>"+html+"</p>");
		        $('#msgError').show();
		    	$("#conferma").removeAttr("disabled");
		        
			}
		}
	
	});
}
</script>