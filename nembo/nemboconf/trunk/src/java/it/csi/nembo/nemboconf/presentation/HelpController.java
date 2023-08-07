package it.csi.nembo.nemboconf.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping(value = "/help")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class HelpController extends BaseController
{
  @Autowired
  private ICruscottoBandiEJB cruscottoBandi = null;

  @RequestMapping(value = "gethelp_{codcdu}", method = RequestMethod.POST)
  @ResponseBody
  public String gethelp(@PathVariable("codcdu") String codcdu, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    session.setAttribute(NemboConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE,
        NemboConstants.FLAGS.SI);
    return cruscottoBandi.getHelpCdu(codcdu);
  }

  @RequestMapping(value = "cleanhelp_{codcdu}", method = RequestMethod.POST)
  @ResponseBody
  public String cleanhelp(@PathVariable("codcdu") String codcdu, Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    session
        .removeAttribute(NemboConstants.GENERIC.SESSION_VAR_HELP_IS_ACTIVE);
    return "OK";
  }
}
