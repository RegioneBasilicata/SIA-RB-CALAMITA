<%@page import="it.csi.nembo.nembopratiche.util.NemboConstants"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nembopratiche.tld"%>
<%@taglib prefix="b" uri="/WEB-INF/bootstrap.tld"%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/head.html"/>

<body>
  <span class="success_big" style="vertical-align:middle;float:left"></span><br /><br />Il documento � stato generato, &egrave; ora disponibile per il download
  <br /><a id="conferma" style="margin-right:8px" role="button" class="btn btn-primary pull-right" href="${pdf}">scarica</a>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/footer.html"/>

  <script type="text/javascript">

  $(document).ready(function() {
		window.opener.location.reload();
		  });


</script>
