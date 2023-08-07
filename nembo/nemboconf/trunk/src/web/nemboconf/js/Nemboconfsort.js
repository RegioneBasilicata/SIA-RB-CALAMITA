$( document ).ready(function() {
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
	 	   
	 	   //$items=$('td[data-property=\"'+propertyName+'\"]');
	 	   $tableCnt = $('td[data-property=\"'+propertyName+'\"]').closest('table');
	 	   $items=$('#'+ $tableCnt.attr('id') + ' tbody tr');
	 	   $items.sort(function(a, b)
	 			  {
	 		        var c1=$(a).children('[data-property=\"'+propertyName+'\"]');
	 		        var c2=$(b).children('[data-property=\"'+propertyName+'\"]');
	 				var v1= c1.attr("data-value");
	 				var v2= c2.attr("data-value");
	 				
	 				var type1= c1.attr("data-type");
	 				var type2= c2.attr("data-type");
	 				
	 				if(type1 == type2 == 'DATE')
	 				{
	 					v1 = new Date(v1).getTime();
	 					v2 = new Date(v2).getTime();
	 				}
	 				else if(type1 == type2 == 'INT')
	 				{
	 					v1 = parseInt(v1);
	 					v2 = parseInt(v2);
	 				}
	 				
	 				if (v1<v2)
	 				{
	 				  if(orderType == 'ASC')	
	 					  return -1;
	 				  else if(orderType == 'DESC')
	 					 return 1;
	 				}
	 				if (v1>v2)
	 				{
	 					if(orderType == 'ASC')	
		 					  return 1;
		 				  else if(orderType == 'DESC')
		 					 return -1;
	 				}
	 				
	 				return 0;
	 				
	 			  });
	 	  
	 	  $items.detach().appendTo($tableCnt);
	 	  
	    }
	});