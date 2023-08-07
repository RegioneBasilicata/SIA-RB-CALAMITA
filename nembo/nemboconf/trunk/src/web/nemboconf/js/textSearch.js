
function loadDataTextSearch(id, addr, value, valueProperty, textProperty) {

	$(".searchRow").remove();
	$(".warningSearch").remove();
	var html="";
	if (value === undefined || value == null || value == "") {
		html += "<div class=\"searchRow\" style=\"padding-top:2em\">"
				+ "<div class=\"stdMessagePanel warningSearch\ style=\"padding-top:2em;\">"
				+ " <div class=\"alert alert-warning\">"
				+ "  <p><strong>Attenzione!</strong><br>Inserire un parametro di ricerca.</p>"
				+ " </div></div></div>";
		$("#" + id).closest('.form-group').after(html);
		return;
	} else
		$
				.ajax({
					url : addr,
					async : false,
					type : "POST",
					data : "value=" + value,
					success : function(data) {
						var html = "";
						if (data !== undefined && data != null && data != "") {
							var i = 0;
							html += "<div class=\"searchRow\" style=\"padding-top:2em\">"
							$(data)
									.each(

											function(index, row) {
												debugger;
												html += "<div class=\"row\">"
														+ "<div class=\"col-lg-12\">"
														+ "<div class=\"input-group\">"
														+ "<span class=\"input-group-addon\"><input type=\"radio\" value=\""
														+ row[valueProperty]
														+ "\" name=\""
														+ valueProperty
														+ "\"></input></span>"
														+ "<input type=\"text\" style=\"background-color:#FFF;max-width:200em;\" class=\"form-control\" value=\""
														+ row[textProperty]
														+ "\" readonly=\"readonly\">"
														+ " </div><!-- /input-group -->"
														+ "</div><!-- /.col-lg-9 -->"
														+ " </div>";
											});

						} else {
							html += "<div class=\"searchRow\" style=\"padding-top:2em\">"
							html += "<div class=\"stdMessagePanel warningSearch\ style=\"padding-top:2em;\">"
									+ " <div class=\"alert alert-warning\">"
									+ "  <p><strong>Attenzione!</strong><br>Non &egrave; stato trovato nessun risultato.</p>"
									+ " </div></div>";
						}
						html += "</div>"
						$("#" + id).closest('.form-group').after(html);
					}

				});
}