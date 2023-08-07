<div style="margin-bottom:4em">
	<div id="dual-list-box1" class="form-group row" >
	          <select style="display: none" id="livelliDualList"  multiple="multiple" data-title="Livelli" 
	          	data-source="loadLivelliDisponibili_${idBandoOggetto}_${idQuadroOggControlloAmm}.json" 
	          	data-sourceselected="loadLivelliSelezionati_${idBandoOggetto}_${idQuadroOggControlloAmm}.json" 
	          	data-value="idLivello" 
	          	data-text="codiceDescrizione"
	          	data-labelfilter="Filtra Livello"></select>
	      </div>	  
	      
	<div class="col-sm-12">
	    <button type="button" onclick="$('#dlgLivelli').modal('hide');" class="btn btn-default">indietro</button>
		<button type="button" onclick="serializzaConfermaSelectedList(${idBandoOggetto}, ${idQuadroOggControlloAmm});" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
	</div>
</div>							
<script>initMyDualList();</script>