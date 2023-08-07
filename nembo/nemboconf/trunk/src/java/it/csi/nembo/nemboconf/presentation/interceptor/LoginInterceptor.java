package it.csi.nembo.nemboconf.presentation.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import it.csi.nembo.nemboconf.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.NoLoginRequired;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.iride2.policy.entity.Identita;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class LoginInterceptor implements HandlerInterceptor
{
  MessaggisticaManager messaggisticaManager = new MessaggisticaManager();

  public LoginInterceptor()
  {
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
      HttpServletResponse response, Object handler, Exception ex)
      throws Exception
  {
  }

  @Override
  public void postHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler, ModelAndView modelAndView)
      throws Exception
  {
  }

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception
  {
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
                                                                                // 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0); // Proxies.
    HandlerMethod h = (HandlerMethod) handler;
    if (h.getMethod().isAnnotationPresent(NoLoginRequired.class)
        || h.getBeanType().isAnnotationPresent(NoLoginRequired.class))
    {
      return true;
    }
    HttpSession session = request.getSession();
    Identita identita = (Identita) session.getAttribute("identita");
    UtenteAbilitazioni utenteAbilitazioni = null;
    if (identita == null)
    {
        request.setAttribute("webContext", NemboConstants.NEMBOCONF.WEB_CONTEXT);
    	request
          .getRequestDispatcher(
              NemboConstants.JSP.VIEW.ERROR.SESSION_EXPIRED)
          .forward(request, response);
      return false;
    }
    else
    {
      utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      if (utenteAbilitazioni == null)
      {
    	 request.setAttribute("webContext", NemboConstants.NEMBOCONF.WEB_CONTEXT);
    	 request
            .getRequestDispatcher(
                NemboConstants.JSP.VIEW.ERROR.SESSION_EXPIRED)
            .forward(request, response);
        return false;
      }
      if (h.getMethod().isAnnotationPresent(NoLoginRequired.class)
          || h.getBeanType().isAnnotationPresent(NoLoginRequired.class))
      {
        return true;
      }
      Security security = h.getMethod().getAnnotation(Security.class);
      if (security == null)
      {
        security = h.getBeanType().getAnnotation(Security.class);
      }
      if (security == null)
      {
        String pagina = h.getMethod().getName();
        NemboConstants.LOGGIN.DEFAULT_LOGGER
            .error("ERRORE GRAVE: La pagina " + pagina
                + " non presenta l'annotazione @Security per la gestione dei diritti di accesso. Impossibile proseguire!");
        sendErrorPage(
            request,
            response,
            "Si è verificato un errore grave: non è stato trovato il diritto di accesso della pagina, per motivi di sicurezza l'accesso alla funzionalità è stato disabilitato. Si prega di riportare il problema all'assistenza tecnica",
            security);
        return false;
      }
      String macroCU = security.macroCDU();
      Security.DIRITTO_ACCESSO dirittoAccesso = security.dirittoAccessoMinimo();
      if (dirittoAccesso == Security.DIRITTO_ACCESSO.READ_WRITE
          && !NemboUtils.PAPUASERV.isUtenteReadWrite(utenteAbilitazioni))
      {
        sendErrorPage(request, response,
            " Accesso negato: l'utente non è abilitato alle funzioni di modifica dell'applicativo",
            security);
        return false;
      }
      if (!GenericValidator.isBlankOrNull(macroCU) && !NemboUtils.PAPUASERV
          .isMacroCUAbilitato(utenteAbilitazioni, macroCU))
      {
        sendErrorPage(request, response,
            "Accesso negato: Il ruolo scelto dall'utente non è abilitato a questa funzionalità",
            security);
        return false;
      }
      long idLivello = security.idLivello();
      if (idLivello != Long.MIN_VALUE)
      {
        if (!NemboUtils.PAPUASERV.isLivelloAbilitato(utenteAbilitazioni,
            idLivello))
        {
          sendErrorPage(request, response,
              "Accesso negato: L'utente non è stato abilitato a questa funzionalità",
              security);
          return false;
        }
      }
    }
    session.setAttribute("webContext", NemboConstants.NEMBOCONF.WEB_CONTEXT);
    return messaggisticaManager.validate(request, response, handler);
  }

  private void sendErrorPage(HttpServletRequest request,
      HttpServletResponse response, String messaggio, Security security)
      throws ServletException,
      IOException
  {
    request.setAttribute("titolo", "Errore");
    request.setAttribute("messaggio", messaggio);
    String errorPage = "/WEB-INF/jsp/errore/sicurezza.jsp";
    if (security != null)
    {
      errorPage = security.errorPage();
      if (GenericValidator.isBlankOrNull(errorPage))
      {
        if (Boolean.TRUE.equals(request.getAttribute("isPopup")))
        {
          errorPage = "/WEB-INF/jsp/dialog/soloErrore.jsp";
        }
        else
        {
          errorPage = "/WEB-INF/jsp/errore/sicurezza.jsp";
        }
      }
    }
    request.getRequestDispatcher(errorPage).forward(request, response);
  }

}
