<%@page import="it.csi.nembo.nembopratiche.util.NemboUtils"%>
<%@page import="it.csi.nembo.nembopratiche.presentation.login.CtrlLogin"%>
<%@page import="it.csi.nembo.nembopratiche.exception.*"%>
<%@page import="it.csi.nembo.nembopratiche.dto.MapColonneNascosteVO"%>
<%@page import="it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni"%>
<%@page import="it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin"%>
<%@page import="it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory"%>
<%@page import="it.csi.nembo.nembopratiche.util.validator.Errors"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="it.csi.iride2.policy.entity.Identita"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.csi.nembo.nembopratiche.dto.DecodificaDTO"%>
<%@page import="it.csi.nembo.nembopratiche.util.NemboConstants"%>
<%@taglib uri="/WEB-INF/remincl.tld" prefix="r"%>
<%@taglib prefix="p" uri="/WEB-INF/nembopratiche.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="/WEB-INF/mybootstrap.tld"%>

<%!private static final String[]                         CODICI_FISCALI =
  { "AAAAAA00B77B000F", "AAAAAA00A11B000J", "AAAAAA00A11C000K", "AAAAAA00A11D000L", "AAAAAA00A11E000M",
      "AAAAAA00A11F000N", "AAAAAA00A11G000O", "AAAAAA00A11H000P", "AAAAAA00A11I000Q", "AAAAAA00A11J000R",
      "AAAAAA00A11K000S", "AAAAAA00A11L000T",
      "AAAAAA00A11M000U", "AAAAAA00A11N000V", "AAAAAA00A11O000W", "AAAAAA00A11R000Z", "AAAAAA00A11S000A",
      "AAAAAA00A11T000B", "AAAAAA00A11U000C",
      "AAAAAA00A11V000D", "AAAAAA00A11P000X"};

  private static final ArrayList<DecodificaDTO<String>> DEMO           = new ArrayList<DecodificaDTO<String>>();
  private static final ArrayList<DecodificaDTO<String>> PROVIDERS      = new ArrayList<DecodificaDTO<String>>();
  private static final ArrayList<DecodificaDTO<String>> LIVELLI        = new ArrayList<DecodificaDTO<String>>();
  static
  {
    PROVIDERS.add(new DecodificaDTO<String>("ipa", "Pubblica amministrazione"));
    PROVIDERS.add(new DecodificaDTO<String>("TOBECONFIG", "Privati"));

    LIVELLI.add(new DecodificaDTO<String>("1", "username e password di utenti auto-registrati"));
    LIVELLI.add(new DecodificaDTO<String>("2", "username e password di utenti con identità verificati"));
    LIVELLI.add(new DecodificaDTO<String>("4", "username, password e PIN di utenti con identità verificati"));
    LIVELLI.add(new DecodificaDTO<String>("8", "certificati X.509 di CA non qualificate"));
    LIVELLI.add(new DecodificaDTO<String>("16", "certificati X.509 di CA qualificate"));

    for (int i = 0; i < CODICI_FISCALI.length; ++i)
    {
      DEMO.add(new DecodificaDTO<String>(String.valueOf(i), "CSI.DEMO " + (i + 20) + " (" + CODICI_FISCALI[i] + ")"));
    }
  }

  public Identita creaIdentita(String codFiscale, String cognome, String nome, int livelloAutenticazione, String idProvider, String timestamp)
  {
    Identita identita = new Identita();

    identita.setCodFiscale(codFiscale);
    identita.setCognome(cognome);
    identita.setNome(nome);
    identita.setIdProvider(idProvider);
    identita.setLivelloAutenticazione(livelloAutenticazione);
    identita.setTimestamp(timestamp);
    identita.setMac("jI3S6AflG95C9sze/8/WKg==");
    return identita;
  }

  private int getLivelloAutenticazione(HttpServletRequest request)
  {
    String[] livelloAccesso = request.getParameterValues("livelloAccesso");
    int iLivelloAccesso = 0;
    if (livelloAccesso != null)
    {
      for (String livello : livelloAccesso)
      {
        iLivelloAccesso += new Integer(livello);
      }
    }
    return iLivelloAccesso;
  }

  private String getTimestamp()
  {
    Date date = new Date();
    return new SimpleDateFormat("yyyyddmmhhmmss").format(date);
  }

  public void addPortalCookie(HttpServletResponse response, String portal)
  {
    Cookie cookie = new Cookie(NemboConstants.PORTAL.NEMBOPRATICHE_LOGIN_PORTAL, portal);
    cookie.setPath("/nembopratiche");
    cookie.setMaxAge(8 * 60 * 60);
    response.addCookie(cookie);
  }%>
<%
  boolean isSubmit = request.getParameter("isSubmit") != null;
  if (!isSubmit)
  {
    request.setAttribute("idxCodiceFiscale", "8"); // CSI.DEMO 28
    request.setAttribute("livelloAccesso", new String[]
    { "1", "2", "4", "8", "16" }); // CSI.DEMO 28
  }
  else
  {
    request.setAttribute("preferRequest", Boolean.TRUE);
    Errors errors = new Errors();
    String idProvider = request.getParameter("idProvider");
    errors.validateMandatory(request.getParameterValues("livelloAccesso"), "livelloAccesso");
    Identita identita = null;
    if (errors.isEmpty())
    {
      if (request.getParameter("loginDemo") != null)
      {
        int idxCodiceFiscale = new Integer(request.getParameter("idxCodiceFiscale")).intValue();
        identita = creaIdentita(CODICI_FISCALI[idxCodiceFiscale], "TOBECONFIG", "DEMO " + (idxCodiceFiscale + 20), getLivelloAutenticazione(request),
            idProvider, getTimestamp());
      }
      else
      {
        if (request.getParameter("loginID") != null)
        {
          Long idUtenteLogin = errors.validateMandatoryLong(request.getParameter("idUtenteLogin"), "idUtenteLogin");
          if (errors.isEmpty())
          {
            UtenteLogin listUtenti[] = null;
            try
            {
              listUtenti = PapuaservProfilazioneServiceFactory.getRestServiceClient().findUtentiLoginByIdList(new long[]
              { idUtenteLogin });
            }
            catch (Exception e)
            {
              String message = NemboUtils.STRING.nvl(e.getMessage());
              if (message.indexOf("No content to map to Object due to end of input") < 0)
              {
                // E' un errore sconosciuto, rilancio l'eccezione
                throw e;
              }
              else
              {
                // L'errore è dovuto al bug di Jackson che non considera un response body vuoto come un oggetto null
                // Ignoro l'eccezione e lascio la listUtenti null in modo che sia gestita opportunamente
              }
            }
            if (listUtenti != null && listUtenti.length > 0)
            {
              UtenteLogin utenteLogin = listUtenti[0];
              if (utenteLogin.getIdProcedimento() == NemboConstants.NEMBOPRATICHE.ID)
              { 
                identita = creaIdentita(utenteLogin.getCodiceFiscale(), utenteLogin.getCognome(), utenteLogin.getNome(), getLivelloAutenticazione(request),
                    idProvider, getTimestamp());
                if ("ipa".equals(idProvider))
                {
                  addPortalCookie(response, NemboConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
                  session.setAttribute(NemboConstants.PORTAL.NEMBOPRATICHE_LOGIN_PORTAL, NemboConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
                }
                else
                {
                  addPortalCookie(response, NemboConstants.PORTAL.PRIVATI_SISPIE);
                  session.setAttribute(NemboConstants.PORTAL.NEMBOPRATICHE_LOGIN_PORTAL, NemboConstants.PORTAL.PRIVATI_SISPIE);
                }
                session.setAttribute("identita", identita);
                String nextPage = CtrlLogin.login(session, utenteLogin.getRuolo().getCodice(),1);
                nextPage = nextPage.replaceAll("redirect:(\\b)*(../)*", "/nembopratiche/");
                response.sendRedirect(nextPage);
                return;
              }
              else
              {
                errors.addError("error", "L'utente corrispondente a questo Id NON E' di nembopratiche");
                errors.addError("idUtenteLogin", "L'utente corrispondente a questo Id NON E' di nembopratiche");
              }
            }
            else
            {
              errors.addError("error", "Utente sconosciuto: l'id utente non è presente sulla tabella papua_t_utente_login.");
              errors.addError("idUtenteLogin", "Id Utente non presente sul db di papua");
            }
          }
        }
      }
    }
    if (!errors.isEmpty())
    {
      request.setAttribute("errors", errors);
    }
    if (identita != null)
    {
      session.setAttribute("identita", identita);
      if ("ipa".equals(idProvider))
      {
        response.sendRedirect("/nembopratiche/login/wrup/seleziona_ruolo.do");
      }
      else
      {
        response.sendRedirect("/nembopratiche/login/sisp/seleziona_ruolo.do");
      }
      return;
    }
  }
%>
<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/head.html" />
<body>
	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/portal_header-small.html" />
	<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/header.html" />

	<div class="container-fluid" id="content" style="margin-bottom: 3em">
		<h3>Selezionare l'utente con cui si vuole accedere</h3>

		<form:form action="" commandName="loginModel" modelAttribute="loginModel" method="post">
			<m:error />
			<input type="hidden" name="isSubmit" value="TRUE" />
			<m:panel id="selezionaUtente" title="Utente" cssClass="form-horizontal">
				<div class="col-md-9">
					<m:select id="idProvider" name="idProvider" list="<%=PROVIDERS%>" label="Accesso" header="" preferRequestValues="${preferRequest}" />
				</div>
				<div class="col-md-9">
					<m:select id="livelloAccesso" name="livelloAccesso" list="<%=LIVELLI%>" label="Livello accesso" header="" multiple="${true}" size="5"
						selectedValue="${livelloAccesso}" preferRequestValues="${preferRequest}" />
				</div>
				<!-- Demo -->
				<br style="clear: left" />
				<m:panel id="panelDemo" title="Login con utente demo">
					<div class="col-md-9">
						<m:select label="Demo" name="idxCodiceFiscale" list="<%=DEMO%>" id="idxCodiceFiscale" selectedValue="${idxCodiceFiscale}"
							preferRequestValues="${preferRequest}" />
					</div>
					<div class=" col-md-3">
						<div class="form-group">
							<button type="submit" style="margin-top: 0px !important; width: 100%" name="loginDemo" id="loginDemo" class="btn btn-primary">Login</button>
						</div>
					</div>
				</m:panel>
				<!-- IdUtenteLogin -->
				<br style="clear: left" />
				<m:panel id="panelID" title="Login con idUtenteLogin di papua">
					<div class="col-md-9">
						<m:textfield label="Id utente login" name="idUtenteLogin" id="idUtenteLogin" preferRequestValues="${preferRequest}" />
					</div>
					<div class=" col-md-3">
						<div class="form-group">
							<button type="submit" style="margin-top: 0px !important; width: 100%" name="loginID" id="loginID" class="btn btn-primary">Login</button>
						</div>
					</div>
				</m:panel>
				<br style="clear: left" />
			</m:panel>
		</form:form>
	</div>

	<r:include resourceProvider="portal" url="/staticresources/assets/application/nembopratiche/include/footer.html" />

	<r:include resourceProvider="portal" url="/staticresources/assets/global/include/footerSP07.html" />