<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form action="" modelAttribute="" id="mainForm" method="post" class="form-horizontal" style="margin-top:2em">
	<div style="margin-bottom:4em">
		<div class="container-fluid">
			<div class="alert alert-info" role="alert">
			Oggetto: <b>${descrOggetto}</b><br>
			Quadro:  <b>${descrQuadro}</b>
			</div>
		</div>
		<div class="container-fluid">
			<c:choose>
				<c:when test="${elencoControlli != null}">
					<table class="table table-hover table-striped table-bordered tableBlueTh" >
						<colgroup>
							<col width="10%"></col>
							<col width="50%"></col>
							<col width="20%"></col>
							<col width="20%"></col>
						</colgroup>
						<thead>
							<tr>
								<th>Codice</th>
								<th>Decsrizione</th>
								<th>Obbligatorio</th>
								<th>Gravit&agrave;</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${elencoControlli}" var="a" varStatus="al">
								<tr>								
									<td><input type="hidden" name="idQuadroOggettoControllo" value="${a.idQuadroOggettoControllo}" /> ${a.codice}</td>
									<td>${a.descrizione}</td>
									<td><m:select header="false" selectedValue="${a.flagObbligatorio}"  id="flagObbligatorio_${a.idQuadroOggettoControllo}" list="${listFlagObbligatorio}" valueProperty="id" textProperty="descrizione"  name="flagObbligatorio_${a.idQuadroOggettoControllo}"></m:select></td>
									<td><m:select header="false" selectedValue="${a.gravita}"  id="gravita_${a.idQuadroOggettoControllo}" list="${listgravita}" valueProperty="id" textProperty="descrizione"  name="gravita_${a.idQuadroOggettoControllo}"></m:select></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="stdMessagePanel">	
	                    <div class="alert alert-warning ">
	                        <p><strong>Attenzione!</strong><br>
	                        Non &egrave; stato associato alcun controllo a questa relazione Quadro - Oggetto
	                    </div>
	                </div>
				</c:otherwise>
			</c:choose>
		</div>      
		<div class="col-sm-12">
		    <button type="button" onclick="$('#dlgParametri').modal('hide');" class="btn btn-default">indietro</button>
		    <c:if test="${elencoControlli != null}">
   				<button type="submit" name="conferma" id="conferma" class="btn btn-primary pull-right">conferma</button>
   			</c:if>
		</div>
	</div>		
</form:form>					
		
