package it.csi.nembo.nemboconf.presentation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/logout")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = "")
public class LogoutController extends BaseController
{
  @RequestMapping("conferma")
  public String conferma(Model model, HttpServletRequest request)
  {
    Cookie[] cookies = request.getCookies();
    if (cookies != null)
    {
      for (Cookie cookie : cookies)
      {
        if ("NEMBOCONF_LOGIN_PORTAL".equals(cookie.getName()))
        {
          if ("RUPAR".equals(cookie.getValue()))
          {
            model.addAttribute("shibbolethLogoutUrl",
                "/sp_liv1_WRUP/Shibboleth.sso/Logout");
          }
          else
          {
            model.addAttribute("shibbolethLogoutUrl",
                "/liv1/Shibboleth.sso/Logout");
          }
          break;
        }
      }
    }
    model.addAttribute("webContext", NemboConstants.NEMBOCONF.WEB_CONTEXT);
    return "logout/conferma";
  }

  @RequestMapping("esegui")
  @ResponseBody
  public String esegui(HttpSession session)
  {
    session.invalidate();
    return "<success>true</success>";
  }
}
