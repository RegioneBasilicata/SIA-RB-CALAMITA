var typewatch = function(){
    var timer = 0;
    return function(callback, ms){
        clearTimeout (timer);
        timer = setTimeout(callback, ms);
    }  
}();

function forwardToPage(page)
{
	window.location.href = page;
}

function expandAllPageArrows()
{
	$('.espandi a').each(function(){
		$(this).click();
	});
}

function submitFormTo($form, page)
{
  $oldAction=$form.attr('action');
  $form.attr('action',page);
  $form.submit();
  $form.attr('action',$oldAction);
  return false;
}

function toggleRow(id, hide)
{
	var children=$('.toggle_'+id);
	if (hide===true)
	{
		children.hide();
		var tmp=$('#freccia_'+id);
		tmp.addClass("freccia");
		tmp.removeClass("freccia_giu");
		$('#'+id).attr("data-is-exploded", "false");
	}
	else
	{
		children.toggle();
		var tmp=$('#freccia_'+id);
		if (children.is(':visible'))
		{
	    $('#'+id).attr("data-is-exploded", "true");
			tmp.addClass("freccia_giu");
			tmp.removeClass("freccia");
		}
		else
		{
	    $('#'+id).attr("data-is-exploded", "false");
			tmp.addClass("freccia");
			tmp.removeClass("freccia_giu");
		}
	}
	
	if( children.is(':visible') ) 
	{
		var tmp=$('.toggle_'+id+" .freccia");
		tmp.addClass("freccia");
		tmp.removeClass("freccia_giu");
	}
	else 
	{
		var tmp=$('.toggle_'+id+" .freccia_giu");
		tmp.addClass("freccia_giu");
		tmp.removeClass("freccia");
		children.each(function(index, element)
				{
					toggleRow(element.id, true);
				})
	}
	return false;
}

var currentDialog = null;
function closeDialog()
{
	if (currentDialog)
	{
	  currentDialog.dialog('close');
	}
	return false;
}

function closeModal()
{
  if (_lastModalID)
  {
    $('#'+_lastModalID).modal('hide');
  }
  return false;
}

var lastShowDialogStatusOK = false;

function showDialog(url, title)
{
	lastShowDialogStatusOK = false;
	$('#currentDialog').remove();
	$('body').append("<div id='currentDialog' style='display:none'></div>");
	var html='';
	$.ajax({
		url: url,
		context: document.body,
		async:false
		}).done(function(data) 
	    {
		  html=data;
		  $('#currentDialog').html(data);
		});
	if (html.indexOf('dialogPanel')>-1)
	{
		currentDialog=$('#currentDialog').dialog({
	        width: 600, 
	        modal: true, 
	        bgiframe: false, 
	        resizable: false, 
	        draggable: false,
	        title: title,
	        dialogClass: "no-close-dialog"
	    });
		currentDialog.show();
		lastShowDialogStatusOK=true;
	}
	else
	{
		if (html.indexOf('paginaSessioneScaduta')>-1)
		{
			alert("Sessione scaduta! Si prega di rieseguire il login");
		}
		else
		{
			if (html.indexOf("<body")>-1)
			{
				alert("Errore di sistema");
			}
			else
			{
				alert(html);
			}
		}
	}
	return false;
	
}

function changeInPleaseWait(messaggio)
{
	$('#messaggio').removeClass("feedWarning");
	$('#messaggio').addClass("feedLoad");
	if (messaggio)
	{
		$('#messaggio').html("<p>"+messaggio+"</p>");
	}
	return false; // Per il return sull'onclick
}

function replaceWithPleaseWait(idTag, messaggio)
{
  $('#'+idTag).html
  (
      '<div class="dialogPanel">'
      +'<div class="stdMessagePanel">'
      +'<div id="messaggio" class="feedWarning">'
      +'<p>'+messaggio+'</p>'
      +'</div>'
      +'</div>'
      +'</div>'      
  );
  return false; // Per il return sull'onclick
}

function postDialogToPage($form, $page)
{
	var datastring = $($form).serialize();
	var htmlData = null;
	$.ajax({
	            type: "POST",
	            url: $page,
	            data: datastring,
	            dataType: "text",
	            async:false,
	            success: function(data) {
	            	htmlData=data;
	            }
	        });
	return htmlData;
}


function switchLabelToTextBox(idCell, confirmHref, cancellHref)
{
	if( $( "#"+idCell ).html().indexOf("<input") < 0 ) 
	{
		$( "#"+idCell ).html(
		    "<input type=\"text\" id=\"txt_"+idCell+"\" value=\"" + $( "#"+idCell ).html().trim() + "\" />" +
		    "<span class=\"cancella\"><a href=\"#\" onclick=\"forwardToPage('"+cancellHref+"')\" title=\"Annulla\" ><span class=\"nascosto\">elimina</span></a></span> " +
		    "<span class=\"ok\"><a href=\"#\" title=\"Salva modifica\" onclick=\"forwardToSavePage('"+idCell+"','"+confirmHref+"')\"><span class=\"nascosto\">consolida</span></a></span>"
		);
	}
}

function forwardToSavePage(idCell, confirmHref)
{
	forwardToPage(confirmHref+"?txt="+$("#txt_"+idCell).val());
}

function formatCurrency(value)
{
  value=value*100;
  var remainder=value%100;
  value=(value-remainder)/100;
  var frmDecimal=""+remainder;
  if (frmDecimal.length==1)
  {
    frmDecimal+="0";
  }
  var result="";
  while (value>0)
  {
    remainder=value%1000;
    value=parseInt(""+value/1000);
    if (result!="")
    {
      if (value>0)
      {
        if (remainder<10)
        {
          remainder="00"+remainder;
        }
        else
        {
          if (remainder<100)
          {
            remainder="0"+remainder;
          }
        }
      }
      result=""+remainder+"."+result;
    }
    else
    {
      if (value>0)
      {
        if (remainder<10)
        {
          remainder="00"+remainder;
        }
        else
        {
          if (remainder<100)
          {
            remainder="0"+remainder;
          }
        }
      }
      result=""+remainder;
    }
  }
  if (result=="")
  {
    result+="0";
  }
  result+=","+frmDecimal;
  return result;
}

function openPageInPopup(page, id, title, modalClass)
{
	var html=
	   "<div class=\"modal fade :modalClass\" id=\":id\" tabindex=\"-1\" data-backdrop=\"static\" data-keyboard=\"false\" role=\"dialog\" aria-labelledby=\"dlgTitle_:id\" aria-hidden=\"true\">"
	 + "		<div class=\"modal-dialog\">"
	 + "			<div class=\"modal-content\">"
	 + "				<div class=\"modal-header\">"
	 + "					<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Chiudi\">"
	 + "						<span aria-hidden=\"true\">&times;</span>"
	 + "					</button>"
	 + "					<h4 class=\"modal-title\" id=\"dlgTitle_:id\">:title</h4>"
	 + "				</div>"
	 + "				<div class=\"modal-body\"></div>"
	 + "			</div>"
	 + "		</div>"
	 + "	</div>";
	html=html.replace(':id',id);
	html=html.replace(':title',title);
	if (modalClass==null || modalClass==undefined)
	{
		modalClass="";
	}
	html=html.replace(':modalClass',modalClass);
	if ($('#'+id).length!=0)
	{
	  $('#'+id).replaceWith(html);
	}
	else
	{
    	$('body').append(html);
	}
	$.ajax({
		type : "GET",
		url : page,
		dataType : "html",
		async : false,
		success : function(data) {
			$('#'+id+' .modal-body').html(data);
		}
	});	
	$('#'+id).modal();
	return false;
}

function openPageInPopup(page, id, title, modalClass, modalDialogClass)
{
	var html=
	   "<div class=\"modal fade :modalClass\" id=\":id\"  tabindex=\"-1\" data-backdrop=\"static\" data-keyboard=\"false\" role=\"dialog\" aria-labelledby=\"dlgTitle_:id\" aria-hidden=\"true\">"
	 + "		<div class=\"modal-dialog  "+modalDialogClass+"\">"
	 + "			<div class=\"modal-content\">"
	 + "				<div class=\"modal-header\">"
	 + "					<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Chiudi\">"
	 + "						<span aria-hidden=\"true\">&times;</span>"
	 + "					</button>"
	 + "					<h4 class=\"modal-title\" id=\"dlgTitle_:id\">:title</h4>"
	 + "				</div>"
	 + "				<div class=\"modal-body\"></div>"
	 + "			</div>"
	 + "		</div>"
	 + "	</div>";
	html=html.replace(':id',id);
	html=html.replace(':title',title);
	if (modalClass==null || modalClass==undefined)
	{
		modalClass="";
	}
	html=html.replace(':modalClass',modalClass);
	if ($('#'+id).length!=0)
	{
	  $('#'+id).replaceWith(html);
	}
	else
	{
    	$('body').append(html);
	}
	$.ajax({
		type : "GET",
		url : page,
		dataType : "html",
		async : false,
		success : function(data) {
			$('#'+id+' .modal-body').html(data);
		}
	});	
	$('#'+id).modal();
	return false;
}

function doErrorTooltip()
{
	$('[data-toggle="error-tooltip"]').tooltip({
		container : 'body'
	});
	$('[data-toggle="tooltip"]').tooltip({
		container : 'body'
	});
	
	$('[data-toggle="txt-tooltip"]').tooltip({
		container : 'body'
	});
	
	$('[data-toggle="txt-tooltip"]').hover(function() {
		$('.tooltip').addClass('txt-tooltip')
	}, function() {
		$('.tooltip').removeClass('tooltipPhoto')
	});
	
	$('[data-toggle="error-tooltip"]').hover(function() {
		$('.tooltip').addClass('red-tooltip')
	}, function() {
		$('.tooltip').removeClass('tooltipPhoto')
	});
}

function initializeDatePicker()
{
  //$("input[data-date-picker='true']").datepicker();
}

function serializeSelectField(idSelect)
{
	var ser = '';
	var name = idSelect.split('-')[0];
	$('#'+idSelect+'  option').each(function(index) {
    	ser = ser + '&' + name + '=' + $(this).val();
    });
	
	return ser;
}

function getSelectValueString(idSelect)
{
	var ser = '';
	$('#'+idSelect+'  option').each(function(index) {
    	ser = ser + '&' + $(this).val() + '=' + $(this).html();
    });
	
	return ser;
}

function reorderSelectOptions(idSelect)
{
	var options = $(idSelect+' option');
    var arr = options.map(function(_, o) { return { t: $(o).text(), v: o.value }; }).get();
    arr.sort(function(o1, o2) { return o1.t > o2.t ? 1 : o1.t < o2.t ? -1 : 0; });
    options.each(function(i, o) {
      o.value = arr[i].v;
      $(o).text(arr[i].t);
    });
}    

function submitForm(formName, action)
{
	var act = $('form[name="'+formName+'"]').attr('action');
	$('form[name="'+formName+'"]').attr('action',action);
	$('form[name="'+formName+'"]').submit();
	$('form[name="'+formName+'"]').attr('action',act);
	return false;
}

function fileUploadChangeName(fileTag, textTag) {
	var fileName = $(fileTag).val();
	if (fileName) {
		fileName = fileName.replace(/^.*[\\\/]/, '');
	}
	$(textTag).val(fileName);
}


$('document').ready(function(){
	doErrorTooltip();
	$('.custom-collapsible-panel').on('hidden.bs.collapse', function (e) {
	    $('#' + e.currentTarget.id+' .panel-title i').removeClass('icon-collapse').addClass('icon-expand');
	    return false;
	});
	$('.custom-collapsible-panel').on('shown.bs.collapse', function (e) {
	    $('#' + e.currentTarget.id+' .panel-title i').removeClass('icon-expand').addClass('icon-collapse');
	    return false;
	});
	initializeDatePicker();
	
	$(".showTitleSelect").each(function(){
		$(this).attr('title', $(this).find('option:selected').attr('title'));
	});
	
	$(".showTitleSelect").change(function(){
		$(this).attr('title', $(this).find('option:selected').attr('title'));
	});
});


function toggleHelp(codCDU)
{
	  if($("#help_container").is(":visible"))
	  {
		  $.ajax({
	            type: "POST",
	            url: "../help/cleanhelp_"+codCDU+".do",
	            dataType: "html",
	            async:false,
	            success: function(data) {
	            	 $("#icona_help").css("background-color","#FFFFFF");
	       		  	 $("#help_container").hide();
	            }
	        }); 
	  }
	  else
	  {
		  $.ajax({
	            type: "POST",
	            url: "../help/gethelp_"+codCDU+".do",
	            dataType: "html",
	            async:false,
	            success: function(data) {
	            	$("#icona_help").css("background-color","yellow");
	            	
	            	//cerco se il primo figlio di #content è un panel panel-primary e lo aggancio li dentro
	            	if($( "#content div:first-child" ).hasClass( "panel-primary" ))
	            	{
	            		if($( "#content div:first-child #help_container" ).length <=0 ){ 
	            			$("#help_container").prependTo($( "#content div:first-child" ));
	            		}
	            	}
	            	
	            	$("#help_text").html(data);
	            	$("#help_container").show();
	            }
	        }); 
	  }
	  
	  
}

function submitFormViaAjax(formId, callback)
{
  $form=$('#'+formId);
  $.ajax({
    type : "POST",
    url : $form.attr('action'),
    data: $form.serialize(),
    dataType : "html",
    async : false,
    success : function(data) 
    {
      callback(data, true);
    },
    fail : function( jqXHR, textStatus )
    {
      callback(jqXHR, false); 
      }
    }); 
}

$(function()
    {
      try
      {
        $messaggiTestata = $('#messaggi-testata');
        if ($messaggiTestata.length > 0)
        {
          startMessaggistica($messaggiTestata);
        }
      }
      catch(e)
      {
        //Ignoro tutto
      }
    });
    var _messaggistica_rolling=true;
    var _messaggistica_end_time = null;
    function startMessaggistica($messaggiTestata)
    {
      var testoMessaggio=$messaggiTestata.html();
      var regex=/http[s]*:\/\/[-a-zA-Z0-9@:%._\+~#=]+[^.]/;
      var match=testoMessaggio.match(regex);
      var time=$('#banner_messaggistica').data('time');
      if (time)
      {
        _messaggistica_end_time=new Date(new Date().getTime()+time).getTime();
      }
      else
      {
        // Nessun limite di tempo
        _messaggistica_end_time=new Date(new Date().getTime()+14400000).getTime();
      }
      if (match!=null && match.length>0)
      {
        var hash=Array();
        for(_i=0;_i<match.length;_i++)
        {
          hash[match[_i]]=match[_i];
        }
        for(url in hash)
        {
          testoMessaggio = testoMessaggio.replace(url, '<a href="'+url+'" target="_blank">'+url+'</a>');
        }
      }
      
      var offset=$messaggiTestata.offset();
      var pos={left:$('#banner_messaggistica').width(),top:offset.top};
      $messaggiTestata.html(testoMessaggio);
      $messaggiTestata.offset({left:pos.left,top:pos.top});
      setTimeout(runMessaggistica,20);
    }
    
    function doneMessaggistica()
    {
    }
    function turnOffMessaggistica()
    {
      $('#container-messaggistica').remove();
      $.ajax('../cunembo203/turnoff.do');
    }
    
    function runMessaggistica()
    {
      try
      {
        if (new Date().getTime()>_messaggistica_end_time)
        {
          $('#container-messaggistica').remove();
          return;
        }
        if (_messaggistica_rolling)
        {
            var $messaggiTestata=$('#messaggi-testata');
            var offset=$messaggiTestata.offset();
            var pos={left:offset.left-1,top:offset.top};
            if (pos.left+$messaggiTestata.width()<-1)
            {
              pos.left=$('#banner_messaggistica').width();
            }
            $messaggiTestata.offset({left:pos.left,top:pos.top});
        }
        setTimeout(runMessaggistica,20);
      }
      catch(e)
      {
        //Ignoro tutto
      }
    }
    
	function isAllCheckboxUnchecked(checkboxesName) 
	{
		var allUnchecked = true;
		$("input[name="+checkboxesName+"]").each(function() 
		{
			if ($(this).prop("checked") == true) 
			{
				allUnchecked = false;
			}
		});
		return(allUnchecked);
	}
	
	function selectAllCheckboxes(checkboxesName, selected) 
	{
		$("input[name="+checkboxesName+"]").each(function() 
		{
			$(this).prop('checked',selected); 
		});
	}
	
	  function writeModalBodyError(errorMessage, modalID)
	  {
	     if (!modalID)
	     {
	       modalID=_lastModalID;
	     }
	     var html='<span class="fail_big" style="vertical-align: middle; float:left"></span><h4>'+errorMessage+
	     '</h4><br /><br/><button type="button" class="btn btn-primary pull-right" data-dismiss="modal">Chiudi</button><br style="clear:left"/>';
	     $('#'+modalID+' .modal-body').html(html);   
	  }
	  
	  function escapeHTML(text) {
	        if (typeof text === 'string') {
	            return text
	                .replace(/&/g, "&amp;")
	                .replace(/</g, "&lt;")
	                .replace(/>/g, "&gt;")
	                .replace(/"/g, "&quot;")
	                .replace(/'/g, "&#039;");
	        }
	        return text;
	    }
    
  