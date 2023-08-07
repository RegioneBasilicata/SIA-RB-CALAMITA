package it.csi.nembo.nemboconf.presentation.messaggistica;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping(value = "/cunembo203")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY)
public class CUNEMBO203MessaggisticaInTestata
    extends MessaggisticaBaseController
{
  @RequestMapping(value = "/turnoff")
  @SuppressWarnings("unchecked")
  @ResponseBody
  public Map<String, Object> turnoff(HttpSession session)
  {
    final String THIS_METHOD = "[MessaggisticaController::turnoff]";
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " BEGIN.");
      }
      /*
       * La mappa "messaggistica" è mantenuta aggiornata in sessione dal
       * NemboconfInterceptor (nello specifico dal LogoutManager richiamato dal
       * NemboconfInterceptor) ogni qual volta si accede a una pagina
       * (ovviamente le chiamate ai servizi vengono eseguite soltanto ogni X
       * minuti per non sovraccaricare il sistema)
       */
      Map<String, Object> mapMessaggistica = (Map<String, Object>) session
          .getAttribute("messaggistica");
      mapMessaggistica.put(MessaggisticaManager.KEY_DISABLED_UNTIL_NEXT_REFRESH,
          Boolean.TRUE);
      return mapMessaggistica;
    }
    finally
    {
      logger.debug(THIS_METHOD + " END.");
    }
  }
}
