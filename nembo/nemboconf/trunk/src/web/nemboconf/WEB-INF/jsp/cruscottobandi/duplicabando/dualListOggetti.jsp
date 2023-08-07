<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>

<div>
<form name="formPopupOggetti" id="formPopupOggetti" method="post">
<div id="dual-list-box1" class="form-group row">
			<select style="display: none" id="oggettiDualList" multiple="multiple" data-title="Oggetti" data-source="load_elenco_oggetti.json"
				data-value="idLegameGruppoOggetto" data-text="descrizioneOggetto" data-addcombo="true" data-labelcombo="Gruppo oggetto" data-labelfilter="Filtro"
				data-horizontal="false" data-toggle="true"></select>
		</div>
</form>
<br>
</div>

