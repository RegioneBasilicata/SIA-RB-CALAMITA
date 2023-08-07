<%@page import="it.csi.nembo.nemboconf.presentation.taglib.nemboconf.CruscottoBandiHeader"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nemboconf.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="../js/commons.js"></script>

<r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/head.html" />

<body>
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/header.html" />
  <p:utente />
  <div class="container-fluid">
    <div class="row">
      <div class="moduletable">
        <ul class="breadcrumb">
          <li><a href="../index.do">Home</a> <span class="divider">/</span></li>
          <li><a href="index.do">Cruscotto bandi</a> <span class="divider">/</span></li>
          <li class="active">Testo verbali</li>
        </ul>
      </div>
    </div>
  </div>
  <p:messaggistica />
  <p:CruscottoBandiHeader activeTab="TESTO_VERBALI" cu="CU-NEMBO-015-V"></p:CruscottoBandiHeader>
  <div class="container-fluid" id="content">
    <b:panel type="DEFAULT">
      <b:error />
      <form:form action="" modelAttribute="" id="mainForm" name="mainForm" method="post" class="form-horizontal">
        <input type="hidden" id="idBandoOggetto" name="idBandoOggetto" value="${idBandoOggetto}" />
        <input type="hidden" id="idElencoCdu" name="idElencoCdu" value="${idElencoCdu}" />
        <input type="hidden" id="operation" name="operation" value="" />
        <div class="form-group">
          <label for="tipologia" class="col-sm-4 control-label">Selezionare il gruppo oggetto / istanza da configurare <a
            data-toggle="modal" data-target="#oggettiModal" href="#"> <span style="text-decoration: none;"
              class="icon-large icon-folder-open link"></span>
          </a>
          </label>
          <div class="col-sm-8">
            <div class="well well-sm">
              <p id="oggettoSelezionato">
                <c:out value="${descrOggettoSelezionato}"></c:out>
              </p>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="tipologia" class="col-sm-4 control-label"> Tipo documento <a data-toggle="modal"
            data-target="#verbaliModal" href="#"> <span style="text-decoration: none;"
              class="icon-large icon-folder-open link"></span>
          </a>
          </label>
          <div class="col-sm-8">
            <div class="well well-sm">
              <p id="oggettoSelezionato">
                <c:out value="${descrTipoDocumentoSelezionato}"></c:out>
              </p>
            </div>
          </div>
        </div>
        
        <c:if test="${canUpdate && !empty idBandoOggetto && !empty idElencoCdu}">
        	<div style="padding-bottom: 4em;">
        		<input class="pull-right btn btn-primary" type="button"  onclick="confermaImportazioneTesti(${idBandoOggetto},${idElencoCdu});" value="Importa testi dal catalogo">
        		</div>
        </c:if>
        
        <c:forEach items="${gruppi}" var="g">
          <table id="tGruppo" class="table table-hover table-striped table-bordered tableBlueTh">
            <colgroup>
              <col width="60">
            </colgroup>
            <thead>
              <tr>
                <th style="width:6em;"><c:if test="${canUpdate && g.visibile}">
                <a href="#" onclick="return addTestoVerbale('${g.idGruppoTestoVerbale}')" class="ico24 ico_add"></a>
                <a style="font-size: 24px;width: 32px;color:white;" onclick="return openPageInPopup('duplicaTesti_${g.idGruppoTestoVerbale}.do','dlgDuplica','Duplica Testi','modal-large', true)"   title="duplica testi" class="glyphicon glyphicon-paste" href="javascript:void(0);"></a>
                </c:if></th>
                <th><c:out value="${g.descGruppoTestoVerbale}" />              
                <input type="hidden" id="lenGruppo_${g.idGruppoTestoVerbale }" value="${fn:length(g.testoVerbale)-1 }">
                </th>
              </tr>
            </thead>
            <tbody id="tbody_gruppo_testo_${g.idGruppoTestoVerbale}">
            
              <c:forEach items="${g.testoVerbale}" var="tv" varStatus="pos">
                <tr <c:if test="${!tv.disabled && canUpdate}">class="deletable"</c:if> id="rowId_${g.idGruppoTestoVerbale}_${pos.index}" >
                  <td style="text-align: center; vertical-align: center;" ><c:if test="${!tv.disabled && canUpdate}">
                      <a href="javascript:void(0);" onclick="return deleteTesto(this,'${g.idGruppoTestoVerbale}','${(pos.index+1)*10}','${pos.index}')" class="ico24 ico_trash"></a>
                    </c:if>
                    <c:if test="${canUpdate}">
                    <div class="row" id="row_${pos.index}">					
						<a <c:if test="${pos.index==fn:length(g.testoVerbale)-1 }"> style="display:none"</c:if> class="ico24 ico_down inline" onclick="spostaGiu('${pos.index}','${g.idGruppoTestoVerbale}','${(pos.index+1)*10}')" href="javascript:void(0);" title="Sposta giu"></a>
						<a <c:if test="${pos.index==0 }"> style="display:none"</c:if> class="ico24 ico_up inline" onclick="spostaSu('${pos.index}','${g.idGruppoTestoVerbale}','${(pos.index+1)*10}')" href="javascript:void(0);" title="Sposta su"></a>
                  	</div>
                  	</c:if></td>
                  <td><c:set var="errorId" value="testo_verbale_${g.idGruppoTestoVerbale}_${(pos.index+1)*10}" /> <m:textarea
                      onfocus="callPlaceHolderBar(this)" id="testo_verbale_${g.idGruppoTestoVerbale}_${(pos.index+1)*10}"                 
                      name="testo_verbale_${g.idGruppoTestoVerbale}" style="width:100%" disabled="${tv.disabled || !canUpdate}"
                      error="${errors[errorId]}" escapeHtml="false"><c:out value="${tv.descrizione}"/></m:textarea>
                      <input type="hidden" name="testo_verbale_hidden_${g.idGruppoTestoVerbale}_${(pos.index+1)*10}" id="testo_verbale_hidden_${g.idGruppoTestoVerbale}_${(pos.index+1)*10}" value='<c:out value="${tv.descrizione}"/>'></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:forEach>
        <div class="form-group puls-group" style="margin-top: 2em">
          <div class="col-sm-12">
            <button type="button" onclick="forwardToPage('criteriSelezione.do');" class="btn btn-default">indietro</button>
            <a role="button" href="attivazione.do" id="avanti" class="btn btn-primary">avanti</a>
            <c:if test="${gruppi!=null}">
         	<div class = "pull-right">
	         		<button type="button" onclick="anteprima();" class="btn btn-primary ">Anteprima</button> 
	         		         	<a role="button" hidden="true" style="padding-right:0.5em;" id="anteprima" href=""> </a>	  
	            <p:abilitazione dirittoAccessoMinimo="W">
	            
	              <c:if test="${btnConfermaAttivo}">
	              
	                <button type="button" name="conferma" id="conferma" class="btn btn-primary "
	                  onclick="confermaDati()">conferma</button>
	              </c:if>
	              <c:if test="${!btnConfermaAttivo}">
	                <button type="button" name="conferma" id="conferma" disabled="disabled"
	                  class="btn btn-primary pull-right">conferma</button>
	              </c:if>
	            </p:abilitazione>
            </div>
            </c:if>
          </div>
        </div>
      </form:form>
      <!-- POPUP SCELTA GRUPPO INI -->
      <div class="modal fade" id="oggettiModal" tabindex="-1" role="dialog" aria-labelledby="oggettiModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" style="width: 850px">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h4 class="modal-title" id="myModalLabel">Seleziona istruttoria</h4>
            </div>
            <div class="modal-body">
              <c:forEach items="${elencoIstruttorie}" var="a">
                <m:radio-list name="oggettiDisponibili" id="oggettiDisponibili" list="${a.oggetti}"
                  textProperty="descrGruppoOggetto" valueProperty="idBandoOggetto"></m:radio-list>
              </c:forEach>
            </div>
            <div class="modal-footer">
              <div class="puls-group" style="margin-top: 1em">
                <div class="pull-left">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
                </div>
                <div class="pull-right">
                  <button type="button" onclick="confermaIstanza();" class="btn btn-primary">Conferma</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- POPUP SCELTA GRUPPO FINE -->
      <!-- POPUP SCELTA GRUPPO INI -->
      <div class="modal fade" id="verbaliModal" tabindex="-1" role="dialog" aria-labelledby="verbaliModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" style="width: 850px">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h4 class="modal-title" id="myModalLabel">Seleziona tipo documento</h4>
            </div>
            <div class="modal-body">
              <m:radio-list name="documentiDisponibili" id="documentiDisponibili" list="${cuDocumenti}"
                textProperty="descrizione" valueProperty="id" ></m:radio-list>
                <c:forEach items="${cuDocumenti}" var="d"><input type="hidden" id="codiceCU_${d.id}" value="${d.codice}"></c:forEach>
            </div>
            <div class="modal-footer">
              <div class="puls-group" style="margin-top: 1em">
                <div class="pull-left">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
                </div>
                <div class="pull-right">
                  <button type="button" onclick="confermaDocumento();" class="btn btn-primary">Conferma</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- POPUP SCELTA GRUPPO FINE -->
      


    </b:panel>
    <div id="segnaposto" style="display: none">
      <m:select id="segnaposto_list" onchange="insertSegnaposto(this)" valueProperty="codice" list="${segnaposto}"
        name="segnaposto_list" label="Selezionare il segnaposto"></m:select>
    </div>
  </div>
  
  
		<!-- POPUP PER DUPLICAZIONE TESTI -->
		<!-- POPUP SCELTA GRUPPO INI -->
	<div class="modal fade" id="oggettiModalPopup" style="z-index: 2000;" tabindex="-2" role="dialog" aria-labelledby="oggettiModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 850px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Seleziona istanza</h4>
				</div>
				<div class="modal-body">
					<c:forEach items="${elencoIstruttorie}" var="a">
						<m:radio-list name="oggettiDisponibili" id="oggettiDisponibili" list="${a.oggetti}" textProperty="descrGruppoOggetto"
							valueProperty="idBandoOggetto"></m:radio-list>
					</c:forEach>
				</div>
				<div class="modal-footer">
					<div class="puls-group" style="margin-top: 1em">
						<div class="pull-left">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						</div>
						<div class="pull-right">
							<button type="button" onclick="confermaIstanzaPopup();" class="btn btn-primary">Conferma</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- POPUP SCELTA GRUPPO FINE -->
	<!-- POPUP SCELTA TIPO DOC INI -->
	<div class="modal fade" id="verbaliModalPopup" style="z-index: 2000;" tabindex="2" role="dialog" aria-labelledby="verbaliModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 850px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Seleziona tipo documento da cui copiare i testi</h4>
				</div>
				<div class="modal-body" id="modalBodyVerbaliPopup">

				</div>
				<div class="modal-footer">
					<div class="puls-group" style="margin-top: 1em">
						<div class="pull-left">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
						</div>
						<div class="pull-right">
							<button type="button" onclick="confermaDocumentoPopup();" class="btn btn-primary">Conferma</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- POPUP SCELTA TIPO DOC FINE -->
  <r:include resourceProvider="portal" url="/staticresources/assets/application/nemboconf/include/footer.html" />
  <script src="../js/dual-list-box.js"></script>
  <script src="../js/Nemboconfcruscottobandi.js"></script>
  
  <script type="text/javascript">

  $(document).ready(function(){
	  	addOnClickForTextAreas();
	  });


   function addOnClickForTextAreas() {
		for(var i = 0; i< $("textarea:enabled").length; i++)
		 {
			var id = $("textarea:enabled")[i].id;
			var idGruppo = id.split("_")[2];
		    var ordine = id.split("_")[3];
		    $($("textarea:enabled")[i]).attr('onchange', "copyValue(this,'"+idGruppo+ "','"+ordine+"');");
		  }
	  }
	  
	  function anteprima()
	  {
	        var oggettoSelezionato = $('#idBandoOggetto');
	        var documento = $('#idElencoCdu');
	  		var idOggetto = oggettoSelezionato.val();
	  		var idDoc = documento.val();

		    var codiceCU=$('#codiceCU_'+idDoc).val();

	     	 $("#anteprima").attr("href", "stampa_"+idOggetto+"_"+codiceCU+"/indexTesti.do");
	      	 $("#anteprima")[0].click();
		  	 return false;
		  }

  
      function confermaIstanza()
      {
        var oggettoSelezionato = $('input[name="oggettiDisponibili"]:checked');
        if (oggettoSelezionato.length)
        {
          $('#idBandoOggetto').val(oggettoSelezionato.val());
          $('#idElencoCdu').val("");
          $('#mainForm').submit();

        } else
        {
          alert('Selezionare una istanza');
        }
      }
      function confermaDocumento()
      {
        var documento = $('input[name="documentiDisponibili"]:checked');
        if (documento.length)
        {
          $('#idElencoCdu').val(documento.val());
          $('#mainForm').submit();

        } else
        {
          alert('Selezionare un documento');
        }
      }

      function addTestoVerbale(id)
      {
        var len = parseInt($("#lenGruppo_"+id).val())+1;
        var newOrdine = (len+1)*10;
        $("#lenGruppo_"+id).val(parseInt($("#lenGruppo_"+id).val())+1);
        var defaultText='<tr class="deletable" id="rowId_'+id+'_'+len+'"><td style="text-align:center;vertical-align:center;">'+
        '<input type="hidden" id="lenGruppo_'+id+'" value="'+len+'"><a href="javascript:void(0);" onclick="return deleteTesto(this,\''+id+'\',\''+newOrdine+'\',\''+len+'\')" class="ico24 ico_trash"></a>'+
        '<div class="row" id="row_'+len+'"><a class="ico24 ico_down inline" onclick="spostaGiu(\''+len+'\',\''+id+'\',\''+newOrdine+'\')" href="javascript:void(0);" title="Sposta giu"></a>'+
		'<a class="ico24 ico_up inline" onclick="spostaSu(\''+len+'\',\''+id+'\',\''+newOrdine+'\')" href="javascript:void(0);" title="Sposta su"></a>'+
      	'</div></td><td><textarea onfocus="callPlaceHolderBar(this)" name="testo_verbale_'+id+'" style="width:100%" onchange="copyValue(this,'+id+','+newOrdine+');" ></textarea>'+
        '<input type="hidden" name="testo_verbale_hidden_'+id+'_'+newOrdine+'" id="testo_verbale_hidden_'+id+'_'+newOrdine+'" value=""></td>'+
      	'</td></tr>';
        //defaultText=defaultText.replace(':ID',id);
        var newElement=$(defaultText);
        $('#tbody_gruppo_testo_'+id).append(defaultText);
        $('html, body').animate({
          scrollTop: $('#tbody_gruppo_testo_'+id).offset().top
      }, 500);
        newElement.find('textarea').keydown(textarea_keydown);
        newElement.focus();

        updateArrows(id);
		//debugger;
        //$("ico_down").hide();
        //$("ico_up").hide();
        
        return false;
      }

      function copyValue(self,id,ordine){
    	  var $this=$(self);
				$("#testo_verbale_hidden_"+id+"_"+ordine).val($this.val());
          }
      
      function deleteTesto(self, idGruppo, ordine, pos)
      {
        $('#segnaposto').hide();
        $('body').append($('#segnaposto'));
        $('.hasPlaceHolderBar').removeClass('hasPlaceHolderBar');
        $(self).closest('.deletable').remove();

        //aggionrare pos di tutti quellli con pos > di questo pos
        updatePosIds(idGruppo, ordine, pos);
        $("#lenGruppo_"+idGruppo).val(parseInt($("#lenGruppo_"+idGruppo).val())-1);
        //aggiornare frecce
        updateArrows(idGruppo);
        
        return false;
      }

      function confermaDati()
      {

    	  var j=0;
		for(j=0; j<$('textarea').length; j++)
			{
				if($('textarea')[j].value===undefined || $('textarea')[j].value=="")
					{
						showMessageBox("Attenzione", "Sono presenti dei testi vuoi, riempirli o eliminarli per continuare.", "modal-large");
						return; 
					}
			}

          
        $('#operation').val('conferma');
        $('#mainForm').submit();
      }
      function callPlaceHolderBar(self)
      {
        var $this=$(self);
        if ($('.hasPlaceHolderBar').length>0)
        {
          if (!$this.hasClass('hasPlaceHolderBar'))
          {
            $('.hasPlaceHolderBar').removeClass('hasPlaceHolderBar');
            $('#segnaposto').fadeOut(200, function(){$this.before($('#segnaposto'));$('#segnaposto').fadeIn() });
            $this.addClass('hasPlaceHolderBar');
          }
        }
        else
        {
          $this.before($('#segnaposto'));
          $this.addClass('hasPlaceHolderBar');
          $('#segnaposto').fadeIn(200);
        };
      }


      function getCaret(el) { 
          if (el.selectionStart) { 
            return el.selectionStart; 
          } else if (document.selection) { 
            el.focus(); 

            var r = document.selection.createRange(); 
            if (r == null) { 
              return 0; 
            } 

            var re = el.createTextRange(), 
                rc = re.duplicate(); 
            re.moveToBookmark(r.getBookmark()); 
            rc.setEndPoint('EndToStart', re); 

            return rc.text.length; 
          }  
          return 0; 
        }

      function insertSegnaposto(self)
      {
        var $this=$(self);
        var $selected=$this.find("option:selected");
        if ($selected.length==1)
        {
          var segnaposto=$selected.val();
          var $textarea=$('#segnaposto').next();
          if ($textarea.length==1)
          {
            var start = $textarea[0].selectionStart;
            var end = $textarea[0].selectionEnd;
            if (!end)
            {
              end=start;
            }
            var textAreaTxt = $textarea.val();
            $textarea.val(textAreaTxt.substring(0, start) + segnaposto + textAreaTxt.substring(end) );
            var caretPos=start+segnaposto.length;
            $textarea[0].selectionStart = caretPos;
            $textarea[0].selectionEnd = caretPos;
            $this.val("");
            $textarea.focus();
            $textarea.trigger('change');
            
          }
        }
      }
      
      function textarea_keydown(event) 
      {
        if (event.keyCode<=40)
        {
          return true;
        }
        var tb = event.target;
        var start = tb.selectionStart;
        var end = tb.selectionEnd;
        var reg = new RegExp("(\\\$\\\$[a-zA-z0-9_]+)", "g");
        var amatch = null; 
        while ((amatch = reg.exec(tb.value)) != null) 
        {
          var thisMatchStart = amatch.index;
          var thisMatchEnd = amatch.index + amatch[0].length;
          if ((start <= thisMatchStart) && (end > thisMatchStart)) 
          {
            event.preventDefault();
            return false;
          }
          else if (start > thisMatchStart && start < thisMatchEnd) 
          {
            event.preventDefault();
            return false;
          }
        }
      };
      $('textarea').keydown(textarea_keydown);




    function spostaSu(rowIndex, idGruppo, ordine)
  	{ 		
      	var nTesti = $("#lenGruppo_"+idGruppo).val()
  		var selected = $("#rowId_"+idGruppo + "_" +rowIndex);
  		var prevId = parseInt(rowIndex) - 1; 	
  		var prev = $("#rowId_"+idGruppo + "_" +prevId);

  		var prevOrd = prevId+1;
  		var rowOrd = parseInt(rowIndex)+1;
  		selected.find("a.ico_down").attr('onclick', "spostaGiu('"+prevId+"','"+idGruppo+"','"+prevOrd+0+"')");
  		selected.find("a.ico_up").attr('onclick', "spostaSu('"+prevId+"','"+idGruppo+"','"+prevOrd+0+"')");
  		prev.find("a.ico_down").attr('onclick', "spostaGiu('"+rowIndex+"','"+idGruppo+"','"+rowOrd+0+"')");
  		prev.find("a.ico_up").attr('onclick', "spostaSu('"+rowIndex+"','"+idGruppo+"','"+rowOrd+0+"')");

  		if(selected.find("a.ico_trash").length!=0)
  			{
  				selected.find("a.ico_trash").attr('onclick', "deleteTesto(this,"+idGruppo+","+prevOrd+0+","+prevId+")");
  				selected.find("textarea").attr('onchange',"copyValue(this,"+idGruppo+","+prevOrd+0+")");
  			}
  		if(prev.find("a.ico_trash").length!=0)
  			{
  				prev.find("a.ico_trash").attr('onclick', "deleteTesto(this,"+idGruppo+","+rowOrd+0+","+rowIndex+")");	
  				prev.find("textarea").attr('onchange',"copyValue(this,"+idGruppo+","+rowOrd+0+")");			
  			}

  		selected.attr('id', "rowId_"+idGruppo+'_'+prevId);
  		prev.attr('id', "rowId_"+idGruppo+'_'+rowIndex);

  		selected.after(prev);
  		
	  	updateArrows(idGruppo);	

		//change ordine testo
		var newOrd = parseInt(ordine)-10;
		$("#testo_verbale_"+idGruppo + "_" +ordine).attr('id',"testo_verbale_"+idGruppo + "_aaa");
		$("#testo_verbale_"+idGruppo + "_" +newOrd).attr('id',"testo_verbale_"+idGruppo + "_" +ordine);
		$("#testo_verbale_"+idGruppo + "_aaa").attr('id',"testo_verbale_"+idGruppo + "_"+ newOrd);
		//change ordine testo (nascosto)
		var newOrd = parseInt(ordine)-10;
		$("#testo_verbale_hidden_"+idGruppo + "_" +newOrd).attr('name',"testo_verbale_hidden_"+idGruppo + "_" +ordine);
		$("#testo_verbale_hidden_"+idGruppo + "_" +ordine).attr('name',"testo_verbale_hidden_"+idGruppo + "_"+ newOrd);
		$("#testo_verbale_hidden_"+idGruppo + "_" +ordine).attr('id',"testo_verbale_hidden_"+idGruppo + "_aaa");
		$("#testo_verbale_hidden_"+idGruppo + "_" +newOrd).attr('id',"testo_verbale_hidden_"+idGruppo + "_" +ordine);
		$("#testo_verbale_hidden_"+idGruppo + "_aaa").attr('id',"testo_verbale_hidden_"+idGruppo + "_"+ newOrd);
		
  		return false;
  	}

  	function spostaGiu(rowIndex, idGruppo, ordine)
  	{
      	var nTesti = $("#lenGruppo_"+idGruppo).val();
  	  	
  		var selected = $("#rowId_"+idGruppo + "_" +rowIndex);
  		var nextId = parseInt(rowIndex) + 1; 	
  		var next = $("#rowId_"+idGruppo + "_" +nextId);
  		var nextOrd = nextId+1;
  		var rowOrd = parseInt(rowIndex)+1;
  		

  		selected.find("a.ico_down").attr('onclick', "spostaGiu('"+nextId+"','"+idGruppo+"','"+nextOrd+0+"')");
  		selected.find("a.ico_up").attr('onclick', "spostaSu('"+nextId+"','"+idGruppo+"','"+nextOrd+0+"')");
  		next.find("a.ico_down").attr('onclick', "spostaGiu('"+rowIndex+"','"+idGruppo+"','"+rowOrd+0+"')");
  		next.find("a.ico_up").attr('onclick', "spostaSu('"+rowIndex+"','"+idGruppo+"','"+rowOrd+0+"')");

  		if(selected.find("a.ico_trash").length!=0)
  			{
  				selected.find("a.ico_trash").attr('onclick', "deleteTesto(this,'"+idGruppo+"','"+nextOrd+0+"','"+nextId+"')");
  				selected.find("textarea").attr('onchange',"copyValue(this,"+idGruppo+","+nextOrd+0+")");							
  			}
  		if(next.find("a.ico_trash").length!=0)
  			{
  				next.find("a.ico_trash").attr('onclick', "deleteTesto(this,'"+idGruppo+"','"+rowOrd+0+"','"+rowIndex+"')");
  				next.find("textarea").attr('onchange',"copyValue(this,"+idGruppo+","+rowOrd+0+")");			
  					
  			}

  		selected.attr('id', "rowId_"+idGruppo+'_'+nextId);
  		next.attr('id', "rowId_"+idGruppo+'_'+rowIndex);
  		
		updateArrows(idGruppo);	

	  	next.after(selected);

			//change ordine testo
			var newOrd = parseInt(ordine)+10;
  			$("#testo_verbale_"+idGruppo + "_" +ordine).attr('id',"testo_verbale_"+idGruppo + "_aaa");
			$("#testo_verbale_"+idGruppo + "_" +newOrd).attr('id',"testo_verbale_"+idGruppo + "_" +ordine);
			$("#testo_verbale_"+idGruppo + "_aaa").attr('id',"testo_verbale_"+idGruppo + "_"+ newOrd);
			//change ordine testo (nascosti per post)
			var newOrd = parseInt(ordine)+10;
			$("#testo_verbale_hidden_"+idGruppo + "_" +newOrd).attr('name',"testo_verbale_hidden_"+idGruppo + "_" +ordine);
			$("#testo_verbale_hidden_"+idGruppo + "_" +ordine).attr('name',"testo_verbale_hidden_"+idGruppo + "_"+ newOrd);
  			$("#testo_verbale_hidden_"+idGruppo + "_" +ordine).attr('id',"testo_verbale_hidden_"+idGruppo + "_aaa");
			$("#testo_verbale_hidden_"+idGruppo + "_" +newOrd).attr('id',"testo_verbale_hidden_"+idGruppo + "_" +ordine);
			$("#testo_verbale_hidden_"+idGruppo + "_aaa").attr('id',"testo_verbale_hidden_"+idGruppo + "_"+ newOrd);
				  	
  		return false;
  	}


  	function updatePosIds(idGruppo, ordine, pos){

  		var nextPos = parseInt(pos)+1;
  		var newPos =  parseInt(pos);
  		while(nextPos<=$("#lenGruppo_"+idGruppo).val()+1)
  			{
  				$("#rowId_"+idGruppo + "_" +nextPos).find("a.ico_down").attr('onclick', "spostaGiu('"+newPos+"','"+idGruppo+"','"+newPos+0+"')");
  				$("#rowId_"+idGruppo + "_" +nextPos).find("a.ico_up").attr('onclick', "spostaSu('"+newPos+"','"+idGruppo+"','"+newPos+0+"')");
  		  		if($("#rowId_"+idGruppo + "_" +nextPos).find("a.ico_trash")!==undefined)
  		  			$("#rowId_"+idGruppo + "_" +nextPos).find("a.ico_trash").attr('onclick', "deleteTesto(this,'"+idGruppo+"','"+newPos+0+"','"+newPos+"')");
  		  		if($("#rowId_"+idGruppo + "_" +nextPos).find("textarea")!==undefined)
		  			$("#rowId_"+idGruppo + "_" +nextPos).find("textarea").attr('onchange', "copyValue(this,'"+idGruppo+"','"+nextPos+0+"')");

			$("#rowId_"+idGruppo + "_" +nextPos).attr('id', "rowId_"+idGruppo + "_" +newPos);

  				$("#testo_verbale_hidden_"+idGruppo + "_" +nextPos+0).attr('name',"testo_verbale_hidden_"+idGruppo + "_" +newPos+0);		
  				$("#testo_verbale_hidden_"+idGruppo + "_" +nextPos+0).attr('id',"testo_verbale_hidden_"+idGruppo + "_" +newPos+0);
  	  			//$("#testo_verbale_"+idGruppo + "_" +nextPos+0).attr('name',"testo_verbale_"+idGruppo + "_" +newPos+0);
  	  			$("#testo_verbale_"+idGruppo + "_" +nextPos+0).attr('id',"testo_verbale_"+idGruppo + "_" +newPos+0);
  				
  	  				
  				nextPos= nextPos+1;
  				newPos = newPos+1;	

  			}		
  	  	}

  	function updateArrows(idGruppo){

  		var len = $("#lenGruppo_"+idGruppo).val(); 
  		for (var i = 0 ; i<= len; i++)
  	  		{
  				$("#rowId_"+idGruppo + "_" +i).find("a.ico_down").show();
  				$("#rowId_"+idGruppo + "_" +i).find("a.ico_up").show();			
  	  		}
  			$("#rowId_"+idGruppo + "_" +0).find("a.ico_up").hide();	
  			$("#rowId_"+idGruppo + "_" +$("#lenGruppo_"+idGruppo).val()).find("a.ico_down").hide();
  			
  	  	}


  	function confermaIstanzaPopup() {
		var oggettoSelezionato = $('input[name="oggettiDisponibili"]:checked');
		if (oggettoSelezionato.length) {
			$('#idBandoOggettoPopup').val(oggettoSelezionato.val());
			var idBandoOggetto = $('#idBandoOggettoPopup').val();
			$.ajax({
				type : "POST",
				url : 'selezionaOggetto_'+idBandoOggetto+'.do',
				async : false,
				success : function(data, textStatus) {

					var val = data.split("&&&");
					var descrOggettoSelezionato = val[0];
					var documenti = val[1];
					
					$('#descrOggettoSelezionatoPopup').html(descrOggettoSelezionato);
					
					//create radio buttons
					var radio="";
					var docs = documenti.split("___");
					var i = 0; 
					if(docs!==undefined && docs.length>0)
					for(i=0;i<docs.length-1 ; i++)
						{
							var d = docs[i];
							var id = d.split("%%%")[0];
							var desc = d.split("%%%")[1];
							radio = radio + '<div class="radio"><label><input type="radio" name="documentiDisponibili" id="documentiDisponibili_'+i+'" value="'+id+'">'+desc+'</label></div>';
						}
					$('#oggettiModalPopup').modal('hide');
					$('#modalBodyVerbaliPopup').html(radio);
					
				}
			});		

		} else {
			alert('Selezionare una istanza');
		}
	}
	function confermaDocumentoPopup() {
		var documento = $('input[name="documentiDisponibili"]:checked');
		if (documento.length) {
			$('#idElencoCduPopup').val(documento.val());
			var idBandoOggetto = $('#idBandoOggettoPopup').val();			
			var idElencoCdu = $('#idElencoCduPopup').val();
			var idGruppoTestiVerbali = $('#idGruppoTestiVerbali').val();
			$.ajax({
				type : "POST",
				url : 'selezionaIdElencoCdu_'+idElencoCdu+'_'+idBandoOggetto+'_'+idGruppoTestiVerbali+'.do',
				dataType : "html",
				async : false,
				success : function(data, textStatus) {

					
					$('#oggettiModalPopup').modal('hide');
			        $('#mainForm').submit();
					
				}
			});		
			
		} else {
			alert('Selezionare un documento');
		}
	}


	  function showMessageBox(title, message, modalClass)
	    {
	      var id='__std_messagebox__';
	      var html=
	         "<div class=\"modal fade :modalClass\" id=\":id\" tabindex=\"-1\" data-backdrop=\"static\" data-keyboard=\"false\" role=\"dialog\" aria-labelledby=\"dlgTitle_:id\" aria-hidden=\"true\">"
	       + "    <div class=\"modal-dialog\">"
	       + "      <div class=\"modal-content\">"
	       + "        <div class=\"modal-header\">"
	       + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Chiudi\">"
	       + "            <span aria-hidden=\"true\">&times;</span>"
	       + "          </button>"
	       + "          <h4 class=\"modal-title\" id=\"dlgTitle_:id\">:title</h4>"
	       + "        </div>"
	       + "        <div class=\"modal-body\">:message</div>"
	       +"         <div class=\"modal-footer\">                                                                                                           "
	       + "         <button type=\"button\" class=\"btn btn-default btn-primary\" data-dismiss=\"modal\">Chiudi</button>                                             "
	       + "        </div> "
	       + "      </div>"
	       + "    </div>"
	       + "  </div>";
	      _lastModalID=id;
	      html=html.replace(':id',id);
	      html=html.replace(':title',title);
	      html=html.replace(':message',message);
	      if (modalClass==null || modalClass==undefined)
	      {
	        modalClass="";
	      }
	      html=html.replace(':modalClass',modalClass);
	      if ($('#'+id).length!=0)
	      {
	        $('#'+id).replaceWith(html);
	      }
	      else
	      {
	          $('body').append(html);
	      }
	      $('#'+id).modal();
	      return false;
	    }


	  function confermaImportazioneTesti(idBandoOggetto,idElencoCdu){
		  debugger;
		 	return openPageInPopup('confermaImportazioneTesti_'+idBandoOggetto+'_'+idElencoCdu+'.do','dlgElimina','Importa testi da catalogo.','modal-large','false');

		  }


		function importaTestiDaCatalogo(){

			var idBandoOggetto = $("#idBandoOggetto").val();
			var idElencoCdu = $("#idElencoCdu").val();

			if(idBandoOggetto === undefined || idBandoOggetto==null || idBandoOggetto==""  || idElencoCdu === undefined || idElencoCdu==null ||  idElencoCdu=="")
				{
					showMessageBox("Attenzione", "Per importare i testi, selezionare prima un documento.", "modal-large");
					return; 
				}
			else

				$.ajax({
					type : "GET",
					url : 'importaTestiDaCatalogo_'+idBandoOggetto+'_'+idElencoCdu+'.do',
					dataType : "html",
					async : false,
					success : function(data, textStatus) {
						        $('#mainForm').submit();
						
					}
				});		
			}
		
    </script>
    
  <r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />