<div>
<span class="please_wait" style="vertical-align:middle"></span> Attendere prego, aggiornamento dati e refresh dell pagina in corso...
</div>
<input type="hidden" value="${idFocusAreaModificata}" id="idFocusAreaModificata">
<script type="text/javascript">
$.ajax(
    {
      type : "POST",
      url : "load_${idPianoFinanziario}.do",
      dataType : "json",
      async : false,
      success : function(data)
      {
        for(x in data)
        {
          if (x.indexOf('importo_')==0 || x.indexOf('trascinato_')==0 ||
           x.indexOf('risorse')==0 || x.indexOf('economia_')==0 )
          {
            $('#'+x).html(data[x]);
          }
        }
        $('#popupModifica').modal('hide');
        var idFocusArea = $("#idFocusAreaModificata").val();
        $("#storico_"+idFocusArea).css("display", "inline-table");
      },
      fail : function(jqXHR, textStatus)
      {
        alert('Si è verificato un errore di sistema nell\'aggiornamento dei dati');
        $('#popupModifica').modal('hide');
      }
    });
</script>