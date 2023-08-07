<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<m:error/>
<form name="formPopupSelezionaComuni" id="formPopupSelezionaComuni" method="post" action="inserisci_comuni.do">
	<br/> 
	<div id="dual-div">
		<div id="dual-list-box1" class="form-group row dualListMultipli">
		<select style="display: none" id="comuniDualList" multiple="multiple" 
			name="comuniDualList"
			data-title="Comuni" 
			data-value="id" 
			data-source="load_list_comuni.do"
			data-text="descrizione" 
			data-horizontal="false" 
			data-addcombo="true" 
			data-labelcombo="Provincia" 
			data-labelfilter="Filtro" 
			data-toggle="true"
			class="dualListMultipli">
		</select>
		</div>
		<!-- pull-right in popup -->
		<div style="display:inline-block; width:100%;">
			
			<input type="button" class="btn btn-default" data-dismiss="modal" value="Annulla" onclick="return false;"/> 
			<input type="submit" class="btn btn-primary pull-right" value="Conferma"/> 
		</div>
	</div>
</form>

<script type="text/javascript">
$('#comuniDualList').DualListBox();

function inserisciComuniSelezionati()
{
	var ser = serializeSelectField('selectedList');
	console.log(ser);
	 _lastModalID="dlgInserisci";
     $.ajax({
          type: "POST",
          url: 'inserisci_comuni.do',
          data: ser,
          dataType: "html",
          async:false,
          success: function(html) 
          {
              var COMMENT = '<success';
              if (html != null && html.indexOf(COMMENT) >= 0) 
              {
                window.location.reload();
              } 
              else 
              {
                var dlgName = "${dlgName}"
                $('#' + dlgName + ' .modal-body').html(html);
                doErrorTooltip();
              }            
          },
          error: function(jqXHR, html, errorThrown) 
           {
              writeModalBodyError("Si è verificato un errore grave nell'accesso alla funzionalità di eliminazione. Se il problema persistesse si prega di contattare l'assistenza tecnica");
           }  
      });
	closeModal();
	return false;
}

</script> 