<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<b:error />
<form method="post" class="form-horizontal" id="deleteForm" style="margin-top: 1em">
			<h4>Sei sicuro di voler eliminare questa giustificazione?</h4>
	<div class="form-group puls-group" style="margin-top: 1.5em; margin-right: 0px">

			<input type="hidden" name="idSoluzioneAnomalia" value="${idSoluzioneAnomalia}" />			
						
		<button type="button" data-dismiss="modal" class="btn btn-default">annulla</button>
		<button type="button" name="conferma" id="conferma" class="btn btn-primary pull-right" onclick="confermaElimina()">conferma</button>
	</div>
	<script type="text/javascript">
    function confermaElimina()
    {
      $
          .ajax(
          {
            type : "POST",
            url : '../cunembo234/elimina_giustificazione_${idSoluzioneAnomalia}.do',
            data : $('#deleteForm').serialize(),
            dataType : "html",
            async : false,
            success : function(html)
            {
              var COMMENT = '<success';
              if (html != null && html.indexOf(COMMENT) >= 0)
              {
                window.location.reload();
              }
              else
              {
                $('#EliminaVisita .modal-body').html(html);
                doErrorTooltip();
              }
            },
            error : function(jqXHR, html, errorThrown)
            {
              writeModalBodyError("Si � verificato un errore grave nell'accesso alla funzionalit� di eliminazione. Se il problema persistesse si prega di contattare l'assistenza tecnica");
            }
          });
    }
  </script>
</form>