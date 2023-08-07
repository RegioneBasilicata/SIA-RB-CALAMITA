package it.csi.nembo.nemboconf.presentation.login;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.dto.MapColonneNascosteVO;
import it.csi.nembo.nemboconf.exception.InternalException;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.NoLoginRequired;
import it.csi.iride2.policy.entity.Identita;
import it.csi.papua.papuaserv.dto.gestioneutenti.Ruolo;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@RequestMapping(value = "/login")
@NoLoginRequired
public class CtrlLogin
{
  private static final String LOGIN_WRUP = "RUPAR";
  private static final String LOGIN_SISP = "SISPIE";
  private static final String LOGIN_SPID = "SPID";

  @RequestMapping(value = "wrup/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloWRUP(HttpServletResponse response, HttpSession session)
      throws InternalException
  {
    addPortalCookie(response, LOGIN_WRUP);
    Identita identita = (Identita) session.getAttribute("identita");
    identita.setLivelloAutenticazione(identita.getLivelloAutenticazione() & 31);
    session.setAttribute("identita", identita);
    return "redirect:../seleziona_ruolo.do";
  }

  @RequestMapping(value = "sisp/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloSISP(HttpServletResponse response, HttpSession session)
      throws InternalException
  {
    addPortalCookie(response, LOGIN_SISP);
    Identita identita = (Identita) session.getAttribute("identita");
    identita.setLivelloAutenticazione(identita.getLivelloAutenticazione() & 31);
    /*
     * Imposto il 6 bit del livello di autenticazione a 1, come convenzione per
     * papua se questo bit è a 1 significa che l'utente ha fatto login tramite
     * SPID (Non crea problemi in quanto il livello di autenticazione di
     * shibboleth/iride2 usa solo i primi 5 bit.
     */
    session.setAttribute("identita", identita);
    return "redirect:../seleziona_ruolo.do";
  }

  @RequestMapping(value = "spid/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloSPID(HttpServletResponse response,
      HttpSession session) throws InternalException
  {
    addPortalCookie(response, LOGIN_SPID);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 1, come convenzione per
     * papua se questo bit è a 1 significa che l'utente ha fatto login tramite
     * SPID (Non crea problemi in quanto il livello di autenticazione di
     * shibboleth/iride2 usa solo i primi 5 bit.
     */
    identita.setLivelloAutenticazione(identita.getLivelloAutenticazione() | 32);
    session.setAttribute("identita", identita);
    return "redirect:../seleziona_ruolo.do";
  }

  public void addPortalCookie(HttpServletResponse response, String portal)
  {
    Cookie cookie = new Cookie("NEMBOCONF_LOGIN_PORTAL", portal);
    cookie.setPath("/nemboconf");
    cookie.setMaxAge(8 * 60 * 60);
    response.addCookie(cookie);
  }

  @RequestMapping(value = "seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloGet(
      @ModelAttribute("loginModel") ModelLogin loginModel, ModelMap model,
      HttpSession session) throws InternalException
  {
    model.addAttribute("ruoli", loadRuoli(session));
    return "login/selezionaRuolo";
  }

  @RequestMapping(value = "seleziona_ruolo", method = RequestMethod.POST)
  public String selezionaRuoloPost(ModelMap model, HttpSession session,
      @ModelAttribute("loginModel") @Valid ModelLogin loginModel,
      BindingResult bindingResult)
      throws InternalException
  {
    if (bindingResult.hasErrors())
    {
      return selezionaRuoloGet(loginModel, model, session);
    }
    return login(session, loginModel.getRuolo());
  }

  public static String login(HttpSession session, String ruolo)
      throws InternalServiceException
  {
    Identita identita = (Identita) session.getAttribute("identita");
    try
    {
      UtenteAbilitazioni utenteAbilitazioni = PapuaservProfilazioneServiceFactory
          .getRestServiceClient().login(identita.getCodFiscale(),
              identita.getCognome(), identita.getNome(),
              identita.getLivelloAutenticazione(),
              NemboConstants.NEMBOCONF.ID, ruolo);
      session.setAttribute("utenteAbilitazioni", utenteAbilitazioni);
      session.setAttribute(
          NemboConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA,
          new HashMap<String, String>());
      session.setAttribute(
          NemboConstants.GENERIC.SESSION_VAR_AZIENDE_COLONNE_NASCOSTE,
          new MapColonneNascosteVO());

    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Si è verificato un errore interno durante l'accesso ai servizi di autenticazione di PAPUA",
          InternalException.GENERIC_ERROR, e);
    }
    return "redirect:../cunembo201/index.do";
  }

  private Ruolo[] loadRuoli(HttpSession session) throws InternalException
  {
    Identita identita = (Identita) session.getAttribute("identita");
    try
    {
      Ruolo[] ruoli = PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .findRuoliForPersonaInApplicazione(identita.getCodFiscale(),
              identita.getLivelloAutenticazione(),
              NemboConstants.NEMBOCONF.ID);
      if (ruoli != null)
      {
        for (Ruolo ruolo : ruoli)
        {
          ruolo.setDescrizione(" " + ruolo.getDescrizione());
        }
      }
      return ruoli;
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Si è verificato un errore interno durante l'accesso ai servizi di autenticazione di PAPUA",
          InternalException.GENERIC_ERROR, e);
    }
  }

}
