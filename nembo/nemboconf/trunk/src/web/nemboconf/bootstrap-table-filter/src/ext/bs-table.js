!function($) {

    'use strict';

    var filterData = {};
    var bootstrapTableFilter;
    var serverUrl;

    var getTypeByValues = function(filter) {
        var typeFloat = true, typeInt = true;
        $.each(filter.values, function(i, val) {
            if (typeInt && (parseInt(val) != val)) {
                typeInt = false;
            }
            if (typeFloat && (parseFloat(val) != val)) {
                typeFloat = false;
            }
        });
        if (typeInt || typeFloat) {
            return {type: 'range'};
        }
        if (serverUrl) {
            var delimiter = serverUrl.indexOf('?') < 0 ? '?' : '&';
            return {
                type: 'search',
                source: serverUrl + delimiter + 'resourceFor=' + filter.field
            };
        }
        return {type: 'select'};
    };
    var getCols = function(cols, data, useAjax) {
        var ret = {};
        $.each(cols, function(i, col) {
            if (col.filterable)
            {
                ret[col.field] = {
                    field: col.field,
                    label: col.title,
                    values: []
                };
            }
        });
        $.each(data, function(i, row) {
            $.each(ret, function(field, filter) {
                if (ret[field].values.indexOf(row[field]) < 0) {
                    ret[field].values.push(row[field]);
                }
            });
        });
        $.each(ret, function(field, def) {
            ret[field] = $.extend(ret[field], getTypeByValues(def));
        });
        return ret;
    };
    var rowFilter = function(item, i) {
        var filterType;
        var filter;
        var ret = true;
        $.each(item, function(field, value) {
            filterType = false;
            try {
                filterType = bootstrapTableFilter.getFilterType(field);
                filter = bootstrapTableFilter.getFilter(field);
                if (typeof filter.values !== 'undefined') {
                    var oldVal = value;
                	value = filter.values.indexOf(value);
                    if(value == -1)
                    {
                    	 //Gestione filtri di tipo select
                    	 ret =  ret && $.inArray(oldVal, filterData[field]._values) >= 0;
                    	 return ret;
                    }
                }
                if (filterType && typeof filterData[field] !== 'undefined') {
                    ret = ret && bootstrapTableFilter.checkFilterTypeValue(filterType, filterData[field], value);
                }
            }
            catch (e) {}
        });
        return ret;
    };

    $.fn.bootstrapTableFilter.externals.push(function() {
        if (this.options.connectTo) {
            bootstrapTableFilter = this;
            var $bootstrapTable = $(this.options.connectTo);
            var data = $bootstrapTable.bootstrapTable('getData');
            var cols = $bootstrapTable.bootstrapTable('getColumns');
            serverUrl = $bootstrapTable.bootstrapTable('getServerUrl');
            var dataSourceServer = false;
            var filters = this.options.filters.length ? [] : getCols(cols, data, dataSourceServer);

            $.each(filters, function(field, filter) {
                bootstrapTableFilter.addFilter(filter);
            });
            if (serverUrl) {
                this.$el.on('submit.bs.table.filter', function() {
                    filterData = bootstrapTableFilter.getData();
                    var delimiter = serverUrl.indexOf('?') < 0 ? '?' : '&';
                    var url = serverUrl + delimiter + 'filter=' + encodeURIComponent(JSON.stringify(filterData));
                    $bootstrapTable.bootstrapTable('refresh', {url: url});
                });
            }
            else {
                $bootstrapTable.bootstrapTable('registerSearchCallback', rowFilter);
                this.$el.on('submit.bs.table.filter', function() {
                    filterData = bootstrapTableFilter.getData();
                    $bootstrapTable.bootstrapTable('updateSearch');
                });
            }
        }
    });

}(jQuery);

$('body').on('load-success.bs.table', function()
{
	aggiornaTotali();
});
$('body').on('page-change.bs.table', function()
{
	aggiornaTotali();
});
$('body').on('column-search.bs.table', function()
{
	aggiornaTotali();
});


function aggiornaTotali()
{
	//Gestione riga totali
	//verifico se tabella ha data-totali="true"
	$(".show-totali").each(function()
	{
		
		var tableID = $(this).attr('id');
		$('#'+tableID+' tfoot').remove();
		//aggiungo riga separatore
		var colCount = 0;
		var row = '<tr>';
		$('#'+tableID+' tbody tr:nth-child(1) td').each(function () {
	        if ($(this).attr('colspan')) {
	            colCount += +$(this).attr('colspan');
	        } else {
	            colCount++;
	        }
	        row = row + ' <td class="alignRight" data-field="'+$(this).data("field")+'"></td>';
	    });
	    
	    row = row + '</tr>';
		$('<tfoot><tr><td colspan="'+colCount+'"></td></tr>'+row+'</tfoot>').appendTo('#'+tableID);
		
		//calcolo totali per ogni colonna con data-totale="true"
		$('#'+tableID+' th[data-totale="true"]').each(function () {
			var field = $(this).data("field");
			
			var sum = 0;
			// iterate through each td based on class and add the values
			$('#'+tableID+' tbody tr td[data-field="'+field+'"]').each(function() {
			    var value = $(this).text();
			    if(!isNaN(value) && value.length != 0) {
			        sum += parseFloat(value);
			    }
			});
			//leggo tutte le celle all'interno del tbody che hanno questo data-field e ne faccio la somma
			if(sum % 1 != 0)
			{
				//arrotondo a 2 decimali se il numero è appunto decimale
				sum = parseFloat(sum).toFixed(2);
			}
			$('#'+tableID+' tfoot td[data-field="'+field+'"]').html("<b><i>"+sum+"</i></b>");
		});
	});
}	

