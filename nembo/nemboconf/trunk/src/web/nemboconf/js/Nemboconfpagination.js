   function preventUndefined(id)  																																	
   {																																									
	 var el = $('#'+id).val(); 																																		
		if(el == undefined || el == '' || el == 'null' || el == null)																																	
			return 0;      																																				
		return el;																																						
   } 

   function preventUndefinedString(str)  																																	
   {																																								
		if(str == 'undefined' || str == '')																																	
			return 0;      																																				
		return str;																																						
   } 

   
   function getNextPageSup()  																																		
   {																																									
	 var gotoPage = preventUndefined('numeroParziale1'); 																											
	 var pagcorrente = preventUndefined('paginaCorrente'); 																											
		if(gotoPage > 0) 																																				
			return gotoPage; 																																			
		return pagcorrente;																																				
   } 																																								
   function getNextPageInf()  																																		
   {																																									
	 var gotoPage = preventUndefined('numeroParziale2'); 																											
	 var pagcorrente = preventUndefined('paginaCorrente'); 																											
		if(gotoPage > 0) 																																				
			return gotoPage; 																																			
		return pagcorrente;																																				
   } 																																								

   function nextPage()																																				
   {																																									
	 $.getJSON("refresh_"+preventUndefined('numsub1OLD')+"_"+preventUndefined('numsub1')+"_"+(parseInt(preventUndefined('paginaCorrente'))+1)+"_"+preventUndefined('refID')+"_"+preventUndefined('visibleColumns')+".json", function(data) 							
	 { 																																								
	   elaboraDati(data); 																																			
	 }); 																																							
	  } 																																								

   function prevPage()																																				
   {																																									
	 $.getJSON("refresh_"+preventUndefined('numsub1OLD')+"_"+preventUndefined('numsub1')+"_"+(parseInt(preventUndefined('paginaCorrente'))-1)+"_"+preventUndefined('refID')+"_"+preventUndefined('visibleColumns')+".json", function(data) 							
	 {	 																																							
	   elaboraDati(data); 																																			
	 }); 																																							
	  } 																																								

   function refrehDataPaginazioneSuperiore()																															
   {																																									
	 $.getJSON("refresh_"+preventUndefined('numsub1OLD')+"_"+preventUndefined('numsub1')+"_"+getNextPageSup()+"_"+preventUndefined('refID')+"_"+preventUndefined('visibleColumns')+".json", function(data) 															
	 { 																																								
	   elaboraDati(data); 																																			
	 }); 																																							
	}

   function refrehDataPaginazioneSuperiore(rowPages)																															
   {																																									
	 $.getJSON("refresh_"+preventUndefined('numsub1OLD')+"_"+preventUndefinedString(String(rowPages))+"_"+getNextPageSup()+"_"+preventUndefined('refID')+"_"+preventUndefined('visibleColumns')+".json", function(data) 															
	 { 																																								
	   elaboraDati(data); 																																			
	 }); 																																							
	}
   
   function refrehDataPaginazioneInferiore()																															
   {																																									
	 $.getJSON("refresh_"+preventUndefined('numsub1OLD')+"_"+preventUndefined('numsub1')+"_"+getNextPageInf()+"_"+preventUndefined('refID')+"_"+preventUndefined('visibleColumns')+".json", function(data) 															
	 { 																																								
	   elaboraDati(data); 																																			
	 }); 																																							
	  } 																																								
   function elaboraDati(data)																																		
   {		
	   var elemntiPerPagina;
	   if (data["isValidJSON"]=="true") 																															
	   { 
		 $('#numeroParziale1').val('');
		 $('#numeroParziale2').val('');
		 
		 for(key in data) 																																			
		 { 																																							
			var value=data[key];   																																	
			if (key.indexOf('content')==0)
			{ 
				$('#paginationTable tbody').html('');			
				 for (var count in value) {																																
					appendRow(value[count]); 																																						
				} 																																						
			}else if (key.indexOf('newPage')==0){ 																													
				 $('.currentElement').html(value); 																														
				 $('#paginaCorrente').val(value);
				 $('#numeroParziale1').val(value);
				 $('#numeroParziale2').val(value);
			   }
			else if (key.indexOf('numeroPagine')==0){ 																													
				 $('#totPage').val(value); 																															
				 $('.totPage').html(value); 
			   }	
			else if (key.indexOf('countElementiPagina')==0){ 																													
				 $('.countRowPage').html(value); 
				 $('.page-size').html(value); 
				 elemntiPerPagina = value;
			   }
		    else if (key.indexOf('totaleElementi')==0){ 																													
				$('.totaleElementi').html(value); 																															
		    }
		    else if (key.indexOf('firstPageRow')==0){ 																													
				$('.firstPageRow').html(value); 																															
		    }
		    else if (key.indexOf('lastPageRow')==0){ 																													
				$('.lastPageRow').html(value); 																															
		    }
			else if (key.indexOf('listRisultatiPagina')==0){ 																													
				$('#numsub1').html('');			
				 for (var count in value) {																																
					//$('#numsub1').append('<option value=\"'+value[count]+'\">'+value[count]+'</option>') 																																						
					 $('#numsub1').append('<li class="optall opt'+value[count]+'" ><a href="javascript:refrehDataPaginazioneSuperiore('+value[count]+')">'+value[count]+'</a></li>')
				}					
				$('#numsub1').val(elemntiPerPagina);
				$('#numsub1OLD').val(elemntiPerPagina);
		    }
			else if (key.indexOf('visibleColumns')==0 && value.indexOf("&")!=-1)
			{
			 	var aHCols = value.split("&");
			 	$('td[data-table="paginationTable"').hide();
			 	$('th[data-table="paginationTable"').hide();
			 	$('th[data-property=\"azione\"').show();
			 	$("input[name='showCol']").each( function () {
					   $(this).prop('checked', false);
					});
		 		$.each(aHCols,function(number){
		 			 if(number > 0)
		 			 {
		 	          $('#showCol_'+aHCols[number]).prop('checked', true);
		 	          $('td[data-property=\"'+aHCols[number]+'\"').show();
		 	 	      $('th[data-property=\"'+aHCols[number]+'\"').show();
		 	 	      salvaColonneNascoste();
		 			 }
		 	    });
			}
		 } 																																							
			reloadArrow(); 																																				
	   } 																																							
	}
   
   function elaboraDatiPostOrdine(data)																																		
   {		
	   var elemntiPerPagina;
	   if (data["isValidJSON"]=="true") 																															
	   { 
		 for(key in data) 																																			
		 { 																																							
			var value=data[key];   																																	
			if (key.indexOf('content')==0)
			{ 
				$('#paginationTable tbody').html('');			
				 for (var count in value) {																																
					appendRow(value[count]); 																																						
				} 																																						
			}
			else if (key.indexOf('firstPageRow')==0){ 																													
				$('.firstPageRow').html(value); 																															
		    }
		    else if (key.indexOf('lastPageRow')==0){ 																													
				$('.lastPageRow').html(value); 																															
		    }
		    else if (key.indexOf('newPage')==0){ 																													
				 $('.currentElement').html(value); 																														
				 $('#paginaCorrente').val(value);
				 $('#numeroParziale1').val(value);
				 $('#numeroParziale2').val(value);
			   }
		 } 																																							
		 reloadArrow(); 																																				
	   } 																																							
	}

   function appendRow(obj)
   {
		var tr = '<tr> \n';
		$('#paginationTable thead th').each(function() {
			var colProperty = $(this).attr('data-property');
			var idTh = obj[colProperty];
			if(idTh == undefined)
				idTh = ' ';
			
			if($(this).attr('data-property') == 'azione')
			{
				tr = tr + '<th scope="row" data-table=\"paginationTable\" data-property=\"'+colProperty+'\" style=\"border: 1px solid #91A2B2;padding: 5px;\"><a href="'+obj['azioneHref']+'" style="text-decoration: none;"><i class="'+idTh+'" title="'+obj['titleHref']+'"></i></a></th>  \n';
			}
			else
			{
				tr = tr + '<td data-table=\"paginationTable\" data-property=\"'+colProperty+'\" style=\"border: 1px solid #91A2B2;padding: 5px;\">'+idTh+'</td>  \n';
			}
		});
		tr = tr + '</tr> \n';
		$('#paginationTable tbody').append(tr);
   }

   function reloadArrow()																																			
   {																																									
	 var currPage = $('#paginaCorrente').val();																														
	 var totPage =  $('#totPage').val();																																
	 
	 if(totPage == 1){
			$('.previousPage').addClass('pageDisPrev').removeClass('previousPage').html('<button type=\"button\" onclick=\"prevPage()\" class=\"btn btn-default disabled\">&lt;</button>');								
			$('.nextPage').addClass('pageDisNext').removeClass('nextPage').html('<button type=\"button\" onclick=\"nextPage()\" class=\"btn btn-default disabled\">&gt;</button>');										
	 }else if(currPage<=1){																																				
			$('.previousPage').addClass('pageDisPrev').removeClass('previousPage').html('<button type=\"button\" onclick=\"prevPage()\" class=\"btn btn-default disabled\">&lt;</button>');								
			$('.pageDisNext').addClass('nextPage').removeClass('pageDisNext').html('<button type=\"button\" onclick=\"nextPage()\" class=\"btn btn-default\">&gt;</button>');								
	}else if(currPage==totPage){																																	
			$('.pageDisPrev').addClass('previousPage').removeClass('pageDisPrev').html('<button type=\"button\" onclick=\"prevPage()\" class=\"btn btn-default\">&lt;</button>');								
			$('.nextPage').addClass('pageDisNext').removeClass('nextPage').html('<button type=\"button\" onclick=\"nextPage()\" class=\"btn btn-default disabled\">&gt;</button>');										
	 }else{ 																																							
		$('.pageDisPrev').addClass('previousPage').removeClass('pageDisPrev').html('<button type=\"button\" onclick=\"prevPage()\" class=\"btn btn-default\">&lt;</button>');								
		$('.pageDisNext').addClass('nextPage').removeClass('pageDisNext').html('<button type=\"button\" onclick=\"nextPage()\" class=\"btn btn-default\">&gt;</button>');								
			} 
   } 	
   
   function switchColumn( colProperty)
   {
	   $('td[data-property="'+colProperty+'"').toggle();
	   $('th[data-property="'+colProperty+'"').toggle();
	   salvaColonneNascoste();
	   refrehDataPaginazioneSuperiore();
   }
   
   function salvaColonneNascoste()
   {
	   var cols = "";
	   $("input[name='showCol']:checked").each( function () {
		   cols = cols+"&"+$(this).val();
		});
	   $('#visibleColumns').val(cols);
   }
   
   function restoreDataFromSession(paramName)																															
   {																																									
	 $.getJSON("restoreFromSession_"+paramName+".json", function(data) 															
	 {
	   if(data != null)
	   { 
		   elaboraDati(data);
		   //Oltre recuperare tutti i dati, devo anche preimpostare la freccia di ordinamento
		   var propertyName = data["orderProperty"];
		   var orderType = data["orderType"]; 
		   var arrowDown = "<span class=\"order\"><span class=\"caret\" style=\"margin: 10px 5px;\"></span></span>";
		   var arrowUp   = "<span class=\"order dropup\"><span class=\"caret\" style=\"margin: 10px 5px;\"></span></span>";
		 	  
		   //pulisco tutti i th
	 	   $('th').each(function(){ 
	 		  $(this).html($(this).html().replace(arrowUp,''));
	 		  $(this).html($(this).html().replace(arrowDown,''));
	 	   });
	 	   
	 	   if(orderType == 'ASC'){
	 		   $('th[data-property=\"'+propertyName+'\"]').append(arrowUp);
	 	   }else{
	 		  $('th[data-property=\"'+propertyName+'\"]').append(arrowDown);
	 	   }
	   }else{
		   refrehDataPaginazioneSuperiore();
	   }
	 }); 																																							
   }
   
   $( document ).ready(function() {
	   
	    //Controllo se devo recuperare i dati dalla sessione
	    if($('#mantainSession').length){
	    	restoreDataFromSession($('#mantainSession').val());
	    }else{
	    	refrehDataPaginazioneSuperiore();
	    }
	   
	    $('.sortable').click(function(){
	    	manageOrder($(this).attr('data-property'));
	    });
	    
	    function manageOrder(propertyName)
	    {
	       var orderType = "ASC";
		   var arrowDown = "<span class=\"order\"><span class=\"caret\" style=\"margin: 10px 5px;\"></span></span>";
	 	   var arrowUp   = "<span class=\"order dropup\"><span class=\"caret\" style=\"margin: 10px 5px;\"></span></span>";

	 	   //pulisco eventuali altre frecce in altre colonne
	 	   $('th[data-property!=\"'+propertyName+'\"]').each(function(){ 
	 		  $(this).html($(this).html().replace(arrowUp,''));
	 		  $(this).html($(this).html().replace(arrowDown,''));
	 	   });
	 	   
	 	   //Verifico la presenza della freccia di ordinamento legata alla property nella tabella
	 	   if($('th[data-property=\"'+propertyName+'\"] .order').html())
	 	   {
	 		 //modifico freccia per la colonna scelta
	 		 if($('th[data-property=\"'+propertyName+'\"] .dropup').html())
	 		 {
	 			 //Se la freccia è asc allora la modifico in desc e ordino
	 			$('th[data-property=\"'+propertyName+'\"]').html($('th[data-property=\"'+propertyName+'\"]').html().replace(arrowUp,'')); 
	 			$('th[data-property=\"'+propertyName+'\"]').append(arrowDown);
	 			orderType = "DESC";
	 		 }
	 		 else
	 		 {
	 			//Se la freccia è desc allora la modifico in asc e ordino
	 			$('th[data-property=\"'+propertyName+'\"]').html($('th[data-property=\"'+propertyName+'\"]').html().replace(arrowDown,''));
	 			$('th[data-property=\"'+propertyName+'\"]').append(arrowUp);
	 		 }
	 	   }else
	 	   {
	 		 //Se non c'è allora inserisco la freccia asc. , elimino tutte le altre presenti e ordino
	 		  $('th[data-property=\"'+propertyName+'\"]').append(arrowUp);
	 	   }
	 	   
	 	  $.getJSON("ordercolumn_"+propertyName+"_"+orderType+"_"+preventUndefined('numsub1')+".json", function(data) 							
	 	  {	 																																							
	 		  elaboraDatiPostOrdine(data); 																																			
	 	  }); 
	    }
	});
   