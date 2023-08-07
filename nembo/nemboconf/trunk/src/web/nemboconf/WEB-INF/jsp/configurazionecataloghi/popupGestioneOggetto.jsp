<div style="margin-bottom:4em">
	<div class="container-fluid">
		<div class="alert alert-info" role="alert">
		Oggetto: <b>${descrOggetto}</b>
		</div>
	</div>
	<div id="dual-list-box1" class="form-group row" >
	          <select style="display: none" id="oggQuadroDualList"  multiple="multiple" data-title="Quadri" 
	          	data-source="loadQuadriDisponibili_${idOggetto}.json" 
	          	data-sourceselected="loadQuadriSelezionati_${idOggetto}.json" 
	          	data-value="idQuadro" 
	          	data-text="descrizione"
	          	data-labelfilter="Filtra Quadro"></select>
	      </div>	  
	      
	<div class="col-sm-12">
	    <button type="button" onclick="$('#dlgParametri').modal('hide');" class="btn btn-default">indietro</button>
		<button type="button" onclick="serializzaConfermaSelectedList(${idOggetto});" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
	</div>
</div>							
		
<script>initMyDualList();</script>