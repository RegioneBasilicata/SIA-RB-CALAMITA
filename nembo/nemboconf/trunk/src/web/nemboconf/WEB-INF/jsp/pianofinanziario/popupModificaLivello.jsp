<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<div>
  <b:error />
  <form id="modificaLivelloForm" class="form-horizontal" action="modifica_${idPianoFinanziario}_${idLivello}.do">
    <m:textfield id="budget" name="budget" label="Budget *" controlSize="7" maxlength="13" value="${importo}" preferRequestValues="${preferRequest}">
      <m:input-addon left="false">&euro;</m:input-addon>
    </m:textfield>
    <m:textfield id="trascinato" name="trascinato" label="Trascinato *" controlSize="7" maxlength="13" value="${trascinato}" preferRequestValues="${preferRequest}">
      <m:input-addon left="false">&euro;</m:input-addon>
    </m:textfield>
    <m:textarea name="motivazioni" id="motivazioni" preferRequestValues="${preferRequest}" label="Motivazioni: *" controlSize="7" placeholder="Inserire le motivazioni (max 4000 caratteri)"></m:textarea>
    <div class="pull-left">
      <button type="button" role="button" class="btn btn-default" data-dismiss="modal">annulla</button>
    </div>
    <div class="pull-right">
      <button type="button" role="button" class="btn btn-primary" onclick="onConfermaModifica()">conferma</button>
    </div>
    <br style="clear: left" /> <br />
  </form>
</div>
<script type="text/javascript">
  doErrorTooltip();
  function onConfermaModifica()
  {
    $form=$('#modificaLivelloForm');
    $.ajax(
    {
      type : "POST",
      url : $form.attr('action'),
      data : $form.serialize(),
      dataType : "html",
      async : false,
      success : function(data)
      {
        $('#popupModifica .modal-body').html(data);
      },
      fail : function(jqXHR, textStatus)
      {
        alert('Si è verificato un errore di sistema nell\'aggiornamento dei dati');
      }
    });
  }
</script>