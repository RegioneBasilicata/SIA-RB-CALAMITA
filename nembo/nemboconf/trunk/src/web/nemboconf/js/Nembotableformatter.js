Number.prototype.formatCurrency = function(c){
	  var n = this, 
	      c = isNaN(c = Math.abs(c)) ? 2 : c, 
	      d = "," , 
	      t = ".", 
	      s = n < 0 ? "-" : "", 
	      i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
	      j = (j = i.length) > 3 ? j % 3 : 0;
	     return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
	   };


function preferNumberFormatter($value)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
      return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency()+'</span>';
  }
  catch(e)
  {
	  return '<span class="pull-right">'+$value+'</span>';
  }
}

function numberFormatter($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
      return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(0)+'</span>';
  }
  catch(e)
  {
    
  }
}

function numberFormatter1($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
    	return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(1)+'</span>';
  }
  catch(e)
  {
    
  }
}

function numberFormatter2($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
    	return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(2)+'</span>';
  }
  catch(e)
  {
    
  }
}

function numberFormatter2NoDecimal($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
    	return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(2).replace(',00','')+'</span>';
  }
  catch(e)
  {
    
  }
}

function toggleButtonFormatter($value, row, index)
{
  try
  {
    return '<div class="center"><input data-toggle="bs-toggle" type="checkbox" value="'+$value+'" /></div>';
  }
  catch(e)
  {
    
  }
}

function totalFormattedAdder(rows, field)
{
	debugger;
  var __sum=null;
  $(rows).each(function(index, currentRow)
   {
    var value = currentRow[field];
    
    if(value != null)
    {
    	value = value.toString().replace(',','');
    }
    
    if(value!=null && !isNaN(value) && value.length != 0) 
    {
      __sum += parseFloat(value);
    }
  });
  return __sum;
}

function euroFormatter($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
      return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency()+' &euro;</span>';
  }
  catch(e)
  {
    
  }
}

function euroFormatter2($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
    	return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(2)+' &euro;</span>';
  }
  catch(e)
  {
    
  }
}

function numberFormatter4($value, row, index)
{
  try
  {
    $number=Number($value);
    if (isNaN($number))
    {
    	return '<span class="pull-right">'+$value+'</span>';
    }
    return '<span class="pull-right">'+$number.formatCurrency(4)+'</span>';
  }
  catch(e)
  {
    
  }
}

function numberFormatterAcceptNull($value, row, index)
{
	if($value == null)
	{
		return '<span class="pull-right"></span>';
	}
	else{
		return numberFormatter($value, row, index);
	}
}

function numberFormatter2AcceptNull($value, row, index)
{
	if($value == null)
	{
		return '<span class="pull-right"></span>';
	}
	else{
		return numberFormatter2($value, row, index);
	}
}

function numberFormatter4AcceptNull($value, row, index)
{
	if($value == null)
	{
		return '<span class="pull-right"></span>';
	}
	else{
		return numberFormatter4($value, row, index);
	}
}

function dateSorterddmmyyyy(a, b) {
	//date in formato dd/mm/yyyy
    a = a.split('/')[2]+a.split('/')[1]+a.split('/')[0];
    b = b.split('/')[2]+b.split('/')[1]+b.split('/')[0];
    if (a > b) return 1;
    if (a < b) return -1;
    return 0;
}

function dateSorterddmmyyyyHHmmss(a, b) {
	//date in formato dd/mm/yyyy hh:mm:ss
	
	if(a!=null && b!=null && a!="" && b!=""){

	values=a.split("/");
	day=values[0];
	month=values[1];
	
	//resto is yyyy hh:mm:ss
	resto = values[2];

	values=resto.split(' ');
	year = values[0];
	time = values[1].split(':');
	hour = time[0];
	minutes = time[1];
	seconds = time[2];
	
	a = year+month+day+hour+minutes+seconds;
    
	values=b.split('/');
	day=values[0];
	month=values[1];
	
	resto = values[2];

	values=resto.split(' ');
	year = values[0];
	time = values[1].split(':');
	hour = time[0];
	minutes = time[1];
	seconds = time[2];

	b = year+month+day+hour+minutes+seconds;

    if (a > b) return 1;
    if (a < b) return -1;
	}
    return 0;
}