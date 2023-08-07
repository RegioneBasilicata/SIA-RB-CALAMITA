/**
 * jQuery DualListBox plugin with Bootstrap styling v1.0
 * http://www.geodan.nl
 *
 * Copyright (c) 2014 Geodan B.V.
 * Created by: Alex van den Hoogen
 * Modify by: TOBECONFIG 			  
 *
 * Usage:
 *   Create a <select> and apply this script to that select via jQuery like so:
 *   $('select').DualListBox(); - the DualListBox will than be created for you.
 *
 *   Options and parameters can be provided through html5 data-* attributes or
 *   via a provided JavaScript object. Optionally already selected items can
 *   also be provided and this script can download the JSON to fill the
 *   DualListBox when a valid URI is provided.
 *
 *   See the default parameters (below) for a complete list of options.
 */

(function($) {
    /** Initializes the DualListBox code as jQuery plugin. */
    $.fn.DualListBox = function(paramOptions, selected) {
        return this.each(function () {
            var defaults = {
                element:    $(this).context,    // Select element which creates this dual list box.
                uri:        'local.json',       // JSON file that can be opened for the data.
                value:      'id',               // Value that is assigned to the value field in the option.
                text:       'name',             // Text that is assigned to the option field.
                title:      'Example',          // Title of the dual list box.
                json:       true,               // Whether to retrieve the data through JSON.
                timeout:    500,                // Timeout for when a filter search is started.
                horizontal: false,              // Whether to layout the dual list box as horizontal or vertical.
                addCombo : false,
                addComboMaster : false,
                sortText : true,
                textLength: 70,                 // Maximum text length that is displayed in the select.
                moveAllBtn: true,               // Whether the append all button is available.
                maxAllBtn:  4000,                // Maximum size of list in which the all button works without warning. See below
                toggle: false,
                labelfilter: '',
                labelcombo: '',
                labelcombomaster: '',
                warning:    'Hai superato il limite massimo di 4000 elementi!'
            };

            var htmlOptions = {
                element:    $(this).context,
                uri:        $(this).data('source'),
                uriSelected: $(this).data('sourceselected'),
                value:      $(this).data('value'),
                text:       $(this).data('text'),
                title:      $(this).data('title'),
                sortText:      $(this).data('sorttext'),
                json:       $(this).data('json'),
                timeout:    $(this).data('timeout'),
                horizontal: $(this).data('horizontal'),
                textLength: $(this).data('textlength'),
                moveAllBtn: $(this).data('moveallbtn'),
                maxAllBtn:  $(this).data('maxallbtn'),
                toggle:  $(this).data('toggle'),
                actionRefresh:	 $(this).data('refresh'),
                addCombo: $(this).data('addcombo'),
                addComboMaster: $(this).data('addcombomaster'),
                labelfilter: $(this).data('labelfilter'),
                labelcombo: $(this).data('labelcombo'),
                labelcombomaster: $(this).data('labelcombomaster')
            };

            var options = $.extend({}, defaults, htmlOptions, paramOptions);

            $.each(options, function(i, item) {
                if (item === undefined || item === null) { throw 'DualListBox: ' + i + ' is undefined.'; }
            });

            options['parent'] = 'dual-list-box-' + options.title;
            options['parentElement'] = '#' + options.parent;

            selected = $.extend([{}], selected);
           
            if (options.json) {
               addElementsViaJSON(options, selected);
              pleaseWait(options);
            } else {
                construct(options);
            }
            
            //gestione dell'elenco "selected" della dual list
            $('body').unbind('Nemboconfpratiche.dualistbox.loaded');
            $('body').on('Nemboconfpratiche.dualistbox.loaded', function() {
            	if(htmlOptions.uriSelected) {
            		$.getJSON(htmlOptions.uriSelected, function(json) {
           			   $("#selectedList").html('');
           			   $("#selectedListHidden").html('');
           			   $.each(json, function(key, item) {
           				   if(htmlOptions.addCombo){												//aggiungo elementi alla selectedList e selectedListHidden
           					   $('<option>', {
           	    	                 value: item[htmlOptions.value],
           	    	                 text: item[htmlOptions.text]
           	    	           }).attr("data-group",item['gruppo']).appendTo($('#selectedList'));
           					   $('<option>', {
               		                 value: item[htmlOptions.value],
               		                 text: item[htmlOptions.text]
               		            }).attr("data-group",item['gruppo']).appendTo($('#selectedListHidden'));
           				   } else {
           					   $('<option>', {
         	    	                 value: item[htmlOptions.value],
         	    	                 text: item[htmlOptions.text]
         	    	               }).appendTo($('#selectedList'));
           					   $('<option>', {
               		                 value: item[htmlOptions.value],
               		                 text: item[htmlOptions.text]
               		           }).appendTo($('#selectedListHidden'));
           				   }
           	           });
         	           		
           			   $("#selectedList").filterByText($('#input.filter.form-control filter-selected'),500,null,options,'unselectedComboBox');
           			   
           			   var options = [];													//elenco di valori in 'options'
           	           $("#selectedList").find('option').each(function() {
           	             options.push({value: $(this).val(), text: $(this).text()});
           	           });
           			
           	           $("#selectedList").data('options', options);							//aggiorno i valori di selezione di quelli selezionati			
           	           $(".selected-count").text(options.length);
           	           loadSelectedComboBox();
           	           $('#selectedComboBox').val("0");
           	        });
            	}
      			$('.stdMessageLoad').hide();
      		});
        })
    };
    
    function pleaseWait(options)
    {
      $(options.element).parent().append('<span id="'+options.element.id+'-wait-please" class="col-sm-12"><h4>Attendere prego, caricamento in corso...</h4></span>');
    }

    /** Retrieves all the option elements through a JSON request. */
    function addElementsViaJSON(options, selected) {
        var multipleTextFields = false;

        if (options.text.indexOf(':') > -1) {
            var textToUse = options.text.split(':');

            if (textToUse.length > 1) {
                multipleTextFields = true;
            }
        }

        $.getJSON(options.uri, function(json) {
            $.each(json, function(key, item) {
                var text = '';

                if (multipleTextFields) {
                    textToUse.forEach(function (entry) { text += item[entry] + ' '; });
                } else {
                    text = item[options.text];
                }

                var newOpt = $('<option>', { value: item[options.value], text: text, selected: (selected.some(function (e) { return e[options.value] === item[options.value] }) === true) }).attr("data-group",item['gruppo']);
                if(options.addComboMaster)
                {
                	$(newOpt).attr("data-group-master",item['gruppoMaster']).appendTo(options.element);
                }
                else
                {
                	$(newOpt).appendTo(options.element);
                }
               
            });

            construct(options);
            
            resetSelectedUnselected();
            
            $('body').trigger('Nemboconfpratiche.dualistbox.loaded', [$(options.element).attr('id')]);
            $('#'+options.element.id+"-wait-please").hide();
            
        });
    }
    
    function resetSelectedUnselected()
    {
        //BUGFIX firefox 
        $('#unselectedComboBox').val("0");
        $('#selectedComboBox').val("0");
        $('#unselectedComboBoxMaster').val("0");
        $('#selectedComboBoxMaster').val("0");
    }
    
    /** Adds the event listeners to the buttons and filters. */
    function addListeners(options) {
        var unselected = $(options.parentElement + ' .unselected');
        var selected = $(options.parentElement + ' .selected');

        $(options.parentElement).find('button').bind('click', function() {
            switch ($(this).data('type')) {
                case 'str': /* Selected to the right. */
                	if(unselected.find('option:selected').length >= options.maxAllBtn)
                		alert(options.warning);
                	else
                	{
                		unselected.find('option:selected').clone().appendTo($('#selectedListHidden'));
                		unselected.find('option:selected').each(function (){
                			$('#unselectedListHidden').find('option[value="'+$(this).val()+'"]').remove();
                		});
	                	unselected.find('option:selected').appendTo(selected);
	                    $(this).prop('disabled', true);
	                    eval(options.actionRefresh);
	                    if(options.addCombo)
	                    {
	                    	manageShift(options);
	                    }
	                    else
	                    {
	                    	initUnSelectedList(options.parentElement);
	                    }
	                    
	                    $('#selectedComboBox').val("0");
	                    $('#selectedComboBoxMaster').val("0");
                	}
                    break;
                case 'atr': /* All to the right. */
                	if(unselected.find('option').length >= options.maxAllBtn)
                		alert(options.warning);
                	else
                	{
	                    if (unselected.find('option').length >= options.maxAllBtn && confirm(options.warning) ||
	                        unselected.find('option').length < options.maxAllBtn) {
	                        unselected.find('option').each(function () {
	                            if ($(this).isVisible()) {
	                            	$(this).clone().appendTo($('#selectedListHidden'));
	                            	$('#unselectedListHidden').find('option[value="'+$(this).clone().val()+'"]').remove();
	                                $(this).remove().appendTo(selected);
	                            }
	                        });
	                        eval(options.actionRefresh);
	                        if(options.addCombo)
		                    {
	                        	manageShift(options);
		                    }
		                    else
		                    {
		                    	initUnSelectedList(options.parentElement);
		                    }
	                    }
	                    
	                    $('#selectedComboBox').val("0");
	                    $('#selectedComboBoxMaster').val("0");

                	}
                    break;
                case 'stl': /* Selected to the left. */
                	if(selected.find('option:selected').length >= options.maxAllBtn)
                		alert(options.warning);
                	else
                	{
                		selected.find('option:selected').each(function () {
                			$('#selectedListHidden').find('option[value="'+$(this).val()+'"]').remove();
                        });
                		
	                	selected.find('option:selected').clone().appendTo($('#unselectedListHidden'));
	                	selected.find('option:selected').remove().appendTo(unselected);
	                    $(this).prop('disabled', true);
	                    eval(options.actionRefresh);
	                    loadUnSelectedComboBox();
	                    $('#unselectedComboBox').val("0");
	                    $('#unselectedComboBoxMaster').val("0");
	                    
	                    if(options.addCombo){
	                    	manageShift(options);
	                    }
                	}
                    break;
                case 'atl': /* All to the left. */
                	
                	if(selected.find('option').length >= options.maxAllBtn)
                		alert(options.warning);
                	else
                	{
	                    if (selected.find('option').length >= options.maxAllBtn && confirm(options.warning) ||
	                        selected.find('option').length < options.maxAllBtn) {
	                        selected.find('option').each(function () {
	                            if ($(this).isVisible()) {
	                            	$('#selectedListHidden').find('option[value="'+$(this).val()+'"]').remove();
	                            	$(this).clone().appendTo($('#unselectedListHidden'));
	                            	$(this).remove().appendTo(unselected);
	                            }
	                        });
	                        eval(options.actionRefresh);
	                        loadUnSelectedComboBox();
	                    }
	                    $('#unselectedComboBox').val("0");
	                    $('#unselectedComboBoxMaster').val("0");
	                    
	                    if(options.addCombo){
	                    	manageShift(options);
	                    }
                	}
                    break;
                case 'tgl': /* Modifica visualizzazione orizzontale - verticale */
                	if(options.horizontal == false)
                	{
                		options.horizontal = true;
                	}
                	else
                	{
                		options.horizontal = false;
                	}
                	
                	var bckSelectedValues = $('#selectedList').html();
                	var bckUnSelectedValues = $('#unselectedList').html();
                	var bckUnselectedComboBox = $('#unselectedComboBox').html();
                	var bckValUnselectedComboBox = $('#unselectedComboBox').val();
                	
                	var bckSelectedComboBox = $('#selectedComboBox').html();
                	var bckValSelectedComboBox = $('#selectedComboBox').val();
                	var bckUnselectedListHidden = $('#unselectedListHidden').html();
                	var bckSelectedListHidden = $('#selectedListHidden').html();
                	
                	$(options['parentElement']).html('');
                    
                    if (options.json) {
                       addElementsViaJSON(options, [{}]);
                      pleaseWait(options);
                    } else {
                        construct(options);
                    }
                    
                    $('body').on('Nemboconfpratiche.dualistbox.loaded', function()
                    {
	                    $('#selectedList').html(bckSelectedValues);
	                    $('#unselectedList').html(bckUnSelectedValues);
	                    $('#unselectedComboBox').html(bckUnselectedComboBox);
	                    $('#unselectedComboBox').val(bckValUnselectedComboBox);
	                    $('#selectedComboBox').html(bckSelectedComboBox);
	                    $('#selectedComboBox').val(bckValSelectedComboBox);
	                    $('#unselectedListHidden').html(bckUnselectedListHidden);
	                    $('#selectedListHidden').html(bckSelectedListHidden);
                    });
                    break;  
                default: break;
            }
            
            if(options.sortText)
            {
	            unselected.filterByText($(options.parentElement + ' .filter-unselected'), options.timeout, options.parentElement, options, 'unselectedComboBox').scrollTop(0).sortOptions();
	            selected.filterByText($(options.parentElement + ' .filter-selected'), options.timeout, options.parentElement, options, 'selectedComboBox').scrollTop(0).sortOptions();
            }
            else
            {
	            unselected.filterByText($(options.parentElement + ' .filter-unselected'), options.timeout, options.parentElement, options, 'unselectedComboBox').scrollTop(0);
	            selected.filterByText($(options.parentElement + ' .filter-selected'), options.timeout, options.parentElement, options, 'selectedComboBox').scrollTop(0);
            }
            
            handleMovement(options);
        });

        $(options.parentElement).closest('form').submit(function() {
            selected.find('option').prop('selected', true);
        });

        $(options.parentElement).find('input[type="text"]').keypress(function(e) {
            if (e.which === 13) {
                event.preventDefault();
            }
        });

        if(options.sortText)
        {
            selected.filterByText($(options.parentElement + ' .filter-selected'), options.timeout, options.parentElement, options, 'selectedComboBox').scrollTop(0).sortOptions();
            unselected.filterByText($(options.parentElement + ' .filter-unselected'), options.timeout, options.parentElement, options, 'unselectedComboBox').scrollTop(0).sortOptions();
        }
        else
        {
            selected.filterByText($(options.parentElement + ' .filter-selected'), options.timeout, options.parentElement, options, 'selectedComboBox').scrollTop(0);
            unselected.filterByText($(options.parentElement + ' .filter-unselected'), options.timeout, options.parentElement, options, 'unselectedComboBox').scrollTop(0);
        }
    }

    /** Constructs the jQuery plugin after the elements have been retrieved. */
    function construct(options) {
        createDualListBox(options);
        parseStubListBox(options);
        addListeners(options);
        $("#unselectedList").find('option').each(function(){
    		$(this).clone().appendTo('#unselectedListHidden');
    	});
        loadUnSelectedComboBox();
    }

    

    /** Creates a new dual list box with the right buttons and filter. */
    function createDualListBox(options) {
        $(options.element).parent().attr('id', options.parent);

        $(options.parentElement).addClass('row').append(
        		(options.toggle == false ? ' ' : ('   <div class="col-md-12">'+
        			' <button type="button" title="Cambia tipo visualizzazione" class="btn btn-default pull-right" data-type="tgl" ><span class="glyphicon glyphicon-retweet"></span></button> </div> ' 
        			)) +
        		
                (options.horizontal == false ? '   <div class="col-md-5">' : '   <div class="col-md-12">') +
                '       <h4><span class="unselected-title"></span> <small>- mostrati <span class="unselected-count"></span></small></h4>' +
                
                ((options.labelcombomaster) ? '<label>'+options.labelcombomaster+'</label>' : '') +
                ((options.addComboMaster)
                		? ' <select class="unselectedComboBoxMaster form-control" style="margin-bottom: 5px;" id="unselectedComboBoxMaster"   name="unselectedComboBoxMaster" onchange="filterUnSelectedListMaster(\''+options.parentElement+'\')" style="width: 100%;"><option value="0" selected="true">--seleziona--</option></select>' : ''
                ) +
                
                ((options.labelcombo) ? '<label>'+options.labelcombo+'</label>' : '') +
                ((options.addCombo)
                		? ' <select class="unselectedComboBox form-control" style="margin-bottom: 5px;" id="unselectedComboBox"   name="unselectedComboBox" onchange="filterUnSelectedList(\''+options.parentElement+'\')" style="width: 100%;"><option value="0" selected="true">--seleziona--</option></select>' : ''
                ) +
                
                ((options.labelfilter) ? '<label>'+options.labelfilter+'</label>' : '') +
                '       <input class="filter form-control filter-unselected" type="text" placeholder="Filtra" style="margin-bottom: 5px;">' +
                //ORIGINALE (options.horizontal == false ? '' : createHorizontalButtons(1, options.moveAllBtn)) +
                '       <select class="unselected" id="unselectedList" style="height: 200px; width: 100%;" multiple></select>' +
                '       <select class="unselectedHidden" name="unselectedListHidden" id="unselectedListHidden" style="display:none"></select>' +
                '   </div>' +
                
                
                (options.horizontal == false ? createVerticalButtons(options.moveAllBtn) : '') + 
                 
                (options.horizontal == false ? '' : '<div class="col-md-12">') +
                (options.horizontal == false ? '' : createHorizontalButtons(1, options.moveAllBtn)) +
                (options.horizontal == false ? '' : createHorizontalButtons(2, options.moveAllBtn)) +
                (options.horizontal == false ? '' : '</div>') +
                
                (options.horizontal == false ? '   <div class="col-md-5">' : '  <div class="col-md-12">') +
                '       <h4><span class="selected-title"></span> <small>- mostrati <span class="selected-count"></span></small></h4>' +
                
                
                ((options.labelcombomaster) ? '<label>'+options.labelcombomaster+'</label>' : '') +
                ((options.addComboMaster)
                		? ' <select class="selectedComboBoxMaster form-control" style="margin-bottom: 5px;" id="selectedComboBoxMaster"   name="selectedComboBoxMaster" onchange="filterSelectedListMaster(\''+options.parentElement+'\')"  style="width: 100%;"><option value="0" selected="true">--seleziona--</option></select>' : ''
                ) +
                
                ((options.labelcombo) ? '<label>'+options.labelcombo+'</label>' : '') +
                ((options.addCombo)
                		? ' <select class="selectedComboBox form-control" style="margin-bottom: 5px;" name="selectedComboBox" id="selectedComboBox"   onchange="filterSelectedList(\''+options.parentElement+'\')" style="width: 100%;"><option value="0" selected="true">--seleziona--</option></select>' : ''
                ) +
                ((options.labelfilter) ? '<label>'+options.labelfilter+'</label>' : '') +
                '       <input class="filter form-control filter-selected" type="text" placeholder="Filtra" style="margin-bottom: 5px;">' +
                
                //ORIGINALE (options.horizontal == false ? '' : createHorizontalButtons(2, options.moveAllBtn)) +
                
                '       <select class="selected" name="selectedList" id="selectedList" style="height: 200px; width: 100%;" multiple></select>' +
                '       <select class="selectedHidden" name="selectedListHidden" id="selectedListHidden" style="display:none"></select>' +
                '   </div>');

        $(options.parentElement + ' .selected').prop('name', $(options.element).prop('name'));
        $(options.parentElement + ' .unselected-title').text(options.title+ ' disponibili');
        $(options.parentElement + ' .selected-title').text(options.title+ ' selezionati');
    }

    /** Creates the buttons when the dual list box is set in horizontal mode. */
    /* ORIGINALE
    function createHorizontalButtons(number, copyAllBtn) {
        if (number == 1) {
            return (copyAllBtn ? '       <button type="button" class="btn btn-default atr" data-type="atr" style="margin-bottom: 5px;"><span class="glyphicon glyphicon-list"></span> <span class="glyphicon glyphicon-chevron-right"></span></button>': '') +
                '       <button type="button" class="btn btn-default ' + (copyAllBtn ? 'pull-right col-md-6' : 'col-md-12') + ' str" data-type="str" style="margin-bottom: 5px;" disabled><span class="glyphicon glyphicon-chevron-right"></span></button>';
        } else {
            return '       <button type="button" class="btn btn-default ' + (copyAllBtn ? 'col-md-6' : 'col-md-12') + ' stl" data-type="stl" style="margin-bottom: 5px;" disabled><span class="glyphicon glyphicon-chevron-left"></span></button>' +
                (copyAllBtn ? '       <button type="button" class="btn btn-default col-md-6 pull-right atl" data-type="atl" style="margin-bottom: 5px;"><span class="glyphicon glyphicon-chevron-left"></span> <span class="glyphicon glyphicon-list"></span></button>' : '');
        }
    }
    */

    function createHorizontalButtons(number, copyAllBtn) {
        if (number == 1) {
            return '<div class="col-md-6" style="margin-top:1em;margin-bottom:1em">' + (copyAllBtn ? '       <button type="button" title="Sposta tutti gli elementi" class="btn btn-default  col-md-6 atr" data-type="atr" style="margin-bottom: 5px;"><span class="glyphicon glyphicon-list"></span> <span class="glyphicon glyphicon-chevron-down"></span></button>': '') +
                '       <button type="button" title="Sposta gli elementi selezionati" class="btn btn-default ' + (copyAllBtn ? 'pull-right col-md-6' : 'col-md-12') + ' str" data-type="str" style="margin-bottom: 5px;" disabled><span class="glyphicon glyphicon-chevron-down"></span></button></div>';
        } else {
            return '  <div class="col-md-6" style="margin-top:1em;margin-bottom:1em">     <button type="button" title="Elimina tutti gli elementi" class="btn btn-default ' + (copyAllBtn ? 'col-md-6' : 'col-md-12') + ' stl" data-type="stl" style="margin-bottom: 5px;" disabled><span class="glyphicon glyphicon-chevron-up"></span></button>' +
                (copyAllBtn ? '       <button type="button" title="Elimina gli elementi selezionati" class="btn btn-default col-md-6 pull-right atl" data-type="atl" style="margin-bottom: 5px;"><span class="glyphicon glyphicon-chevron-up"></span> <span class="glyphicon glyphicon-list"></span></button>' : '') +'</div>';
        }
    }

    
    /** Creates the buttons when the dual list box is set in vertical mode. */
    function createVerticalButtons(copyAllBtn) {
        return '   <div class="col-md-2 center-block" style="margin-top: ' + (copyAllBtn ? '80px' : '130px') +'">' +
            (copyAllBtn ? '       <button type="button" title="Sposta tutti gli elementi" class="btn btn-default col-md-8 col-md-offset-2 atr" data-type="atr" style="margin-bottom: 10px;"><span class="glyphicon glyphicon-list"></span> <span class="glyphicon glyphicon-chevron-right"></span></button>' : '') +
            '       <button type="button" title="Sposta gli elementi selezionati" class="btn btn-default col-md-8 col-md-offset-2 str" data-type="str" style="margin-bottom: 20px;" disabled><span class="glyphicon glyphicon-chevron-right"></span></button>' +
            '       <button type="button" title="Elimina gli elementi selezionati"  class="btn btn-default col-md-8 col-md-offset-2 stl" data-type="stl" style="margin-bottom: 10px;" disabled><span class="glyphicon glyphicon-chevron-left"></span></button>' +
            (copyAllBtn ? '       <button type="button" title="Elimina tutti gli elementi" class="btn btn-default col-md-8 col-md-offset-2 atl" data-type="atl" style="margin-bottom: 10px;"><span class="glyphicon glyphicon-chevron-left"></span> <span class="glyphicon glyphicon-list"></span></button>' : '') +
            '   </div>';
    }

    /** Specifically handles the movement when one or more elements are moved. */
    function handleMovement(options) {
        $(options.parentElement + ' .unselected').find('option:selected').prop('selected', false);
        $(options.parentElement + ' .selected').find('option:selected').prop('selected', false);

        $(options.parentElement + ' .filter').val('');
        $(options.parentElement + ' select').find('option').each(function() { $(this).show(); });

        countElements(options.parentElement);
    }

    /** Parses the stub select / list box that is first created. */
    function parseStubListBox(options) {
        var textIsTooLong = false;

        $(options.element).find('option').text(function (i, text) {
            $(this).data('title', text);
            
            if (text.length > options.textLength) {
                textIsTooLong = true;
                //return text.substr(0, options.textLength) + '...';
                return text;
            }
        }).each(function () {
            if (textIsTooLong) {
                $(this).prop('title', $(this).data('title'));
            }

            if ($(this).is(':selected')) {
                $(this).appendTo(options.parentElement + ' .selected');
            } else {
                $(this).appendTo(options.parentElement + ' .unselected');
            }
        });

        $(options.element).remove();
        handleMovement(options);
    }

    /**
     * Filters through the select boxes and hides when an element doesn't match.
     *
     * Original source: http://www.lessanvaezi.com/filter-select-list-options/
     */
    $.fn.filterByText = function(textBox, timeout, parentElement, myOptions, comboBox) {
    	
    	return this.each(function() {
            var select = this;
            var options = [];

            $(select).find('option').each(function() {
                options.push({value: $(this).val(), text: $(this).text()});
            });

            $(select).next().data('options', options);

            $(textBox).bind('keyup', function() {
                delay(function() 
                {
                	
                	var options = $(select).next().data('options');
                    if(options != '')
                    {
                    	$(select).html('');
                    }
                    var search = $.trim($(textBox).val());
                    var regex = new RegExp(search.replace('%',''),'gi');
                    $.each(options, function(i) {
                    	
                    	var filterCombo = $('#'+comboBox).val();
                    	if(search.indexOf('%') === 0) {
                    		if(options[i].text.match(regex) === null) {
                    			//$(select).next().find($('option[value="' + options[i].value + '"]')).hide();
                            } else {
                            	if(myOptions.addCombo && filterCombo != '0'){
	                    			var dataGroup = $(select).next().find($('option[value="' + options[i].value + '"]')).clone();
	                    			var dataGroupFilter = dataGroup[0]['dataset']['group'];
	                    			if(dataGroupFilter == filterCombo){
	                    				$(select).append($(select).next().find($('option[value="' + options[i].value + '"]')).clone());
                    				}else{
                    					//exclude
                    				}
                            	}else{
                            		$(select).append($(select).next().find($('option[value="' + options[i].value + '"]')).clone());
                            	}
                            }
                    	}
                    	else
                    	{
	                    	if(options[i].text.toUpperCase().indexOf(search.toUpperCase()) === 0) {
	                    		if(myOptions.addCombo && filterCombo != '0'){
	                    			var dataGroup = $(select).next().find($('option[value="' + options[i].value + '"]')).clone();
	                    			var dataGroupFilter = dataGroup[0]['dataset']['group'];
	                    			if(dataGroupFilter == filterCombo){
	                    				$(select).append($(select).next().find($('option[value="' + options[i].value + '"]')).clone());
                    				}else{
                    				//exclude	
                    				}
                            	}else{
                            		$(select).append($(select).next().find($('option[value="' + options[i].value + '"]')).clone());
                            	}
	                        } else {
	                            //$(select).next().find($('option[value="' + options[i].value + '"]')).hide();
	                        }
                    	}
                    });
                	
                    if(!$('option[value="' + options[0].value + '"]').data('group'))
                    {
                    	$(select).sortOptions();
                    }
                    
                    countElements(parentElement);
                }, timeout);
            });
        });
    };

    /** Checks whether or not an element is visible. The default jQuery implementation doesn't work. */
    $.fn.isVisible = function() {
        return !($(this).css('visibility') == 'hidden' || $(this).css('display') == 'none');
    };

    /** Sorts options in a select / list box. */
    $.fn.sortOptions = function() {
        return this.each(function() {
            $(this).append($(this).find('option').remove().sort(function(a, b) {
                var at = $(a).text(), bt = $(b).text();
                return (at > bt) ? 1 : ((at < bt) ? -1 : 0);
            }));
        });
    };

    /** Simple delay function that can wrap around an existing function and provides a callback. */
    var delay = (function() {
        var timer = 0;
        return function (callback, ms) {
            clearTimeout(timer);
            timer = setTimeout(callback, ms);
        };
    })();
})(jQuery);

function initUnSelectedList(parentElement)
{
	
	$("#unselectedList").html('');
	$('#unselectedListHidden option').each(function(){
		var $options = $(this).clone();
		$options.appendTo("#unselectedList");
	});
	countElements(parentElement);
}

function filterUnSelectedList(parentElement)
{
	
	//cerco nella hidden list tutte le option che abbiano questo gruppo e le copio nella lista visibile
	$("#unselectedList").html('');
	var val = $("#unselectedComboBox").val();
	
	if( val == '0')
	{
		if($('#unselectedComboBoxMaster') && $('#unselectedComboBoxMaster').val() != '0' && (typeof $('#unselectedComboBoxMaster').val() != "undefined"))
		{
			if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
				$('#unselectedListHidden option[data-group-master="'+$('#unselectedComboBoxMaster').val()+'"]').each(function(){
					var $options = $(this).clone();
					$options.appendTo("#unselectedList");
				});
			}
		}
		else
		{
			if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
				$('#unselectedListHidden option').each(function(){
					var $options = $(this).clone();
					$options.appendTo("#unselectedList");
				});
			}
		}
	}
	else
	{
		if($('#unselectedComboBoxMaster')  && $('#unselectedComboBoxMaster').val() != '0' && (typeof $('#unselectedComboBoxMaster').val() != "undefined"))
		{
			if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
				$('#unselectedListHidden option[data-group="'+val+'"][data-group-master="'+$('#unselectedComboBoxMaster').val()+'"]').each(function(){
					var $options = $(this).clone();
					$options.appendTo("#unselectedList");
					
				});
			}
		}
		else
		{
			if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
				$('#unselectedListHidden option[data-group="'+val+'"]').each(function(){
					var $options = $(this).clone();
					$options.appendTo("#unselectedList");
				});
			}
		}
	}
	$(parentElement +' .filter-unselected').val('');
	countElements(parentElement);
}

function filterSelectedListMaster(parentElement)
{
	
	$("#selectedList").html('');
	var val = $("#selectedComboBoxMaster").val();
	
	if( val == '0')
	{
		if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
			$("#selectedListHidden option").each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
			});
		}
	}
	else
	{
		if($('#selectedListHidden option[value="'+val+'"]').length <= 0){
			$('#selectedListHidden option[data-group-master="'+val+'"]').each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
			});
		}
	}
	
	refreshSelectedComboBox();
	countElements(parentElement);
	$('#selectedComboBox').val("0");
}

function filterUnSelectedListMaster(parentElement)
{
	
	//cerco nella hidden list tutte le option che abbiano questo gruppo e le copio nella lista visibile
	$("#unselectedList").html('');
	var val = $("#unselectedComboBoxMaster").val();
	
	if( val == '0')
	{
		if($('#unselectedListHidden option[value="'+val+'"]').length <= 0){
			$("#unselectedListHidden option").each(function(){
				var $options = $(this).clone();
				$options.appendTo("#unselectedList");
			});
		}
	}
	else
	{
		if($('#unselectedListHidden option[value="'+val+'"]').length <= 0){
			$('#unselectedListHidden option[data-group-master="'+val+'"]').each(function(){
				var $options = $(this).clone();
				$options.appendTo("#unselectedList");
			});
		}
	}
	
	refreshUnSelectedComboBox();
	countElements(parentElement);
	$('#unselectedComboBox').val("0");
	
}

/** Counts the elements per list box/select and shows it. */
function countElements(parentElement) {
    var countUnselected = 0, countSelected = 0;

    $(parentElement + ' .unselected').find('option').each(function() { if ($(this).isVisible()) { countUnselected++; } });
    $(parentElement + ' .selected').find('option').each(function() { if ($(this).isVisible()) { countSelected++ } });

    $(parentElement + ' .unselected-count').text(countUnselected);
    $(parentElement + ' .selected-count').text(countSelected);

    toggleButtons(parentElement);
}

/** Toggles the buttons based on the length of the selects. */
function toggleButtons(parentElement) {
    $(parentElement + ' .unselected').change(function() {
        $(parentElement + ' .str').prop('disabled', false);
    });

    $(parentElement + ' .selected').change(function() {
        $(parentElement + ' .stl').prop('disabled', false);
    });

    if ($(parentElement + ' .unselected').has('option').length == 0) {
        $(parentElement + ' .str').prop('disabled', true);
       	$(parentElement + ' .atr').prop('disabled', false);
    } else {
        $(parentElement + ' .atr').prop('disabled', false);
    }

    if ($(parentElement + ' .selected').has('option').length == 0) {
        $(parentElement + ' .stl').prop('disabled', true);
    } else {
        $(parentElement + ' .atl').prop('disabled', false);
    }
}

function filterSelectedList(parentElement)
{
	
	//cerco nella hidden list tutte le option che abbiano questo gruppo e le copio nella lista visibile
	$("#selectedList").html('');
	var val = $("#selectedComboBox").val();
	
	if( val == '0')
	{
		if($('#selectedComboBoxMaster') && $('#selectedComboBoxMaster').val() != '0'&& (typeof $('#selectedComboBoxMaster').val() != "undefined"))
		{
			$('#selectedListHidden option[data-group-master="'+$('#selectedComboBoxMaster').val()+'"]').each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
			});
		}
		else
		{
			$("#selectedListHidden option").each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
			});
		}
	}
	else
	{
		if($('#selectedComboBoxMaster') && $('#selectedComboBoxMaster').val() != '0'&& (typeof $('#selectedComboBoxMaster').val() != "undefined"))
		{
			$('#selectedListHidden option[data-group="'+val+'"][data-group-master="'+$('#selectedComboBoxMaster').val()+'"]').each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
				
			});
		}
		else
		{
			$('#selectedListHidden option[data-group="'+val+'"]').each(function(){
				var $options = $(this).clone();
				$options.appendTo("#selectedList");
			});
		}
	}
	$(parentElement +' .filter-selected').val('');
	countElements(parentElement);
}

function loadSelectedComboBox()
{
	
	$("#selectedComboBox").html('');
	//$('<option>', { value: '0', text: '--seleziona--', selected: true}).appendTo($("#selectedComboBox"));
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#selectedComboBox"));
	
	var tmp = "";
	
	$("#selectedListHidden option").each(function(){
		var gruppo = $(this).attr("data-group");
		if(tmp.indexOf("&&"+gruppo+"&_&") == -1)
		{
			tmp = tmp + "&&"+gruppo+"&_&";
			$('<option>', { value: gruppo, text: gruppo }).appendTo($("#selectedComboBox"));
		}
	});
	
	$("#selectedComboBox").sortOptions();
	$("#selectedList").sortOptions();
	if($("#unselectedComboBoxMaster"))
	{
		loadSelectedComboBoxMaster();
	}
}

function loadUnSelectedComboBox()
{
	
	$("#unselectedComboBox").html('');
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#unselectedComboBox"));
	
	var tmp = "";
	
	$("#unselectedListHidden option").each(function(){
		var gruppo = $(this).attr("data-group");
		if(tmp.indexOf("&&"+gruppo+"&_&") == -1)
		{
			tmp = tmp + "&&"+gruppo+"&_&";
			$('<option>', { value: gruppo, text: gruppo}).appendTo($("#unselectedComboBox"));
		}
	});
	
	$("#unselectedComboBox").sortOptions();
	if($("#unselectedComboBoxMaster"))
	{
		loadUnSelectedComboBoxMaster();
	}
}

function loadSelectedComboBoxMaster()
{
	
	$("#selectedComboBoxMaster").html('');
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#selectedComboBoxMaster"));
	
	var tmp = "&&";
	
	$("#selectedListHidden option").each(function(){
		var gruppo = $(this).attr("data-group-master");
		if(gruppo != undefined && tmp.indexOf("&&"+gruppo+"&&") == -1)
		{
			tmp = tmp + gruppo+"&&";
			$('<option>', { value: gruppo, text: gruppo}).appendTo($("#selectedComboBoxMaster"));
		}
	});
	
	$("#selectedComboBoxMaster").sortOptions();
}

function loadUnSelectedComboBoxMaster()
{
	
	$("#unselectedComboBoxMaster").html('');
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#unselectedComboBoxMaster"));
	
	var tmp = "&&";
	
	$("#unselectedListHidden option").each(function(){
		var gruppo = $(this).attr("data-group-master");
		if(gruppo != undefined && tmp.indexOf("&&"+gruppo+"&&") == -1)
		{
			tmp = tmp + gruppo+"&&";
			$('<option>', { value: gruppo, text: gruppo}).appendTo($("#unselectedComboBoxMaster"));
		}
	});
	
	$("#unselectedComboBoxMaster").sortOptions();
}

function refreshSelectedComboBox()
{
	
	$("#selectedComboBox").html('');
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#selectedComboBox"));
	
	var tmp = "&&";
	
	$("#selectedList option").each(function(){
		var gruppo = $(this).attr("data-group");
		if(tmp.indexOf("&&"+gruppo+"&&") == -1)
		{
			tmp = tmp + gruppo+"&&";
			$('<option>', { value: gruppo, text: gruppo}).appendTo($("#selectedComboBox"));
		}
	});
	
	$("#selectedComboBox").sortOptions();
}

function refreshUnSelectedComboBox()
{
	
	$("#unselectedComboBox").html('');
	$('<option value="0" selected="selected">--seleziona--</option>').appendTo($("#unselectedComboBox"));
	
	var tmp = "&&";
	
	$("#unselectedList option").each(function(){
		var gruppo = $(this).attr("data-group");
		if(tmp.indexOf("&&"+gruppo+"&&") == -1)
		{
			tmp = tmp + gruppo+"&&";
			$('<option>', { value: gruppo, text: gruppo}).appendTo($("#unselectedComboBox"));
		}
	});
	
	$("#unselectedComboBox").sortOptions();
}

function manageShift(options)
{
	$('#unselectedComboBox').val("0");
	$('#unselectedComboBoxMaster').val("0");
	loadUnSelectedComboBox();
	$('#unselectedComboBox').val("0");
	$('#unselectedComboBoxMaster').val("0");
	
	$('#selectedComboBox').val("0");
	$('#selectedComboBoxMaster').val("0");
	loadSelectedComboBox();
	$('#selectedComboBox').val("0");
	$('#selectedComboBoxMaster').val("0");
	filterUnSelectedList(options.parentElement);
	filterSelectedList(options.parentElement);
}
