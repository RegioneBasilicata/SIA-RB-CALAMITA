<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<link rel="stylesheet" href="/${sessionScope.webContext}/bootstrap-toggle/css/bootstrap-toggle.min.css">
<div class="container-fluid">
	<div class="stdMessagePanel">
		<div id="messaggio" class="alert alert-warning">
			<strong>Sei sicuro di voler eseguire la disconnessione dall' applicativo?</strong> <br />
			<ul>
				<li>Disconnetti anche da TOBECONFIG <input id="logoutSispie" data-toggle="toggle" type="checkbox" value="S" /></li>
			</ul>
		</div>
	</div>
	<div class="form-group puls-group">
		<div class="col-sm-12" id='btn-group1'>
			<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
			<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right" onclick="logout()">
				Esci <i class="icon-signout"></i>
			</button>
		</div>
		<div id='btn-group2' style="display:none">
			<a role="button" class="btn btn-primary" href="/">Homepage TOBECONFIG</a>
			<a role="button" class="btn btn-default" href="/${webContext}/login/wrup/seleziona_ruolo.do">Riaccedi</a>
		</div>
	</div>
</div>
<script type="text/javascript">
  if (typeof toggle_loaded == 'undefined')
  {
    $.getScript("/${webContext}/bootstrap-toggle/js/bootstrap-toggle.js");
  }
  else
  {
    $('input[data-toggle="toggle"]').bootstrapToggle();
  }
  function logout()
  {
    var $messaggio = $('#messaggio');
    var logoutSispie = $('#logoutSispie').prop('checked');
    $('#btn-group1').remove();
    $messaggio.html('');
    $messaggio.append("Disconnessione dall'applicativo in corso... ");
    $.ajax(
    {
      url : '/${webContext}/logout/esegui.do',
      async : false
    }).success(function()
    {
      $messaggio.append("Eseguita <i class='ico32 ico_success'></i>");
    }).error(function()
    {
      $messaggio.append("Fallita <i class='ico32 ico_fail'></i>");
    });

    if (logoutSispie)
    {
      $messaggio.append("<br />Disconnessione dal portale TOBECONFIG in corso... ");
      window.location.href='${shibbolethLogoutUrl}';
    }
    $('#btn-group2').toggle();
  }
</script>
