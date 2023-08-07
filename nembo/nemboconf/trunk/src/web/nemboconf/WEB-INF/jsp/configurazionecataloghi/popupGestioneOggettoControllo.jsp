<div style="margin-bottom:4em">
	<div class="container-fluid">
		<div class="alert alert-info" role="alert">
		Oggetto: <b>${descrOggetto}</b><br>
		Quadro:  <b>${descrQuadro}</b>
		</div>
	</div>
	<div id="dual-list-box1" class="form-group row" >
	          <select style="display: none" id="oggQuadroControlloDualList"  multiple="multiple" data-title="Controlli" 
	          	data-source="loadQuadriControlliDisponibili_${idQuadroOggetto}.json" 
	          	data-sourceselected="loadQuadriControlliSelezionati_${idQuadroOggetto}.json" 
	          	data-value="idControllo" 
	          	data-text="descrEstesa"
	          	data-labelfilter="Filtra Controlli"></select>
	      </div>	  
	      
	<div class="col-sm-12">
	    <button type="button" onclick="$('#dlgParametri').modal('hide');" class="btn btn-default">indietro</button>
		<button type="button" onclick="serializzaConfermaSelectedList(${idQuadroOggetto});" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
	</div>
</div>							
		
<script>initMyDualList();</script>