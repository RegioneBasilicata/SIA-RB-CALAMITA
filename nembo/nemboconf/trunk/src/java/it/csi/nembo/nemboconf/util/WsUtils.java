package it.csi.nembo.nemboconf.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.IMessaggisticaWS;
import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.Messaggistica;

import it.csi.modolxp.modolxppdfgensrv.business.session.facade.ModolPdfGeneratorSrvFacade;
import it.csi.modolxp.modolxppdfgensrv.client.ModolxppdfgensrvServiceClient;
import it.csi.modolxp.modolxppdfgensrv.dto.publish.ModolxppdfgensrvServiceDto;
import it.csi.modolxp.modolxpsrv.business.session.facade.ModolSrvFacade;
import it.csi.modolxp.modolxpsrv.client.ModolxpsrvServiceClient;
import it.csi.modolxp.modolxpsrv.dto.publish.ModolxpsrvServiceDto;


public class WsUtils
{
  public static String[]     PAPUASERV_MESSAGGISTICA_WSDL = null;
  public static final Random RANDOM                       = new Random(
      System.currentTimeMillis());
  public static final int    SHORT_CONNECT_TIMEOUT        = 1000;
  public static final int    SHORT_REQUEST_TIMEOUT        = 5000;
  /* 10 secondi */
  public static final int    MAX_CONNECT_TIMEOUT          = 10 * 1000;
  /* 280 secondi */
  public static final int    MAX_REQUEST_TIMEOUT          = 280 * 1000;
  public static final int[]  SHORT_TIMEOUT                = new int[]
  { SHORT_CONNECT_TIMEOUT, SHORT_REQUEST_TIMEOUT };
  public static final int[]  MAX_TIMEOUT                  = new int[]
  { MAX_CONNECT_TIMEOUT, MAX_REQUEST_TIMEOUT };

  private static final String MODOL_ENDPOINT;
  private static final int    MODOL_PORT;
  private static final String CONTEXT_MODOLSRV             = "/modolxp/modolxpsrv/";
  private static final String CONTEXT_MODOLGENSRV          = "/modolxp/modolxppdfgen/";

  
  static
  {
    ResourceBundle res = ResourceBundle.getBundle("config");
    PAPUASERV_MESSAGGISTICA_WSDL = new String[2];
    PAPUASERV_MESSAGGISTICA_WSDL[0] = res
        .getString("papuaserv.messaggistica.wsdl.server1");
    PAPUASERV_MESSAGGISTICA_WSDL[1] = res
        .getString("papuaserv.messaggistica.wsdl.server2");
    
    MODOL_ENDPOINT = res.getString("modolxp.endpoint");
    MODOL_PORT = getIntFromResource(res, "modolxp.port", 80);
  }

  
  private static int getIntFromResource(ResourceBundle res, String property, int defaultValue)
  {
    try
    {
      return new Integer(res.getString(property));
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  public IMessaggisticaWS getMessaggistica() throws MalformedURLException
  {
    return getMessaggisticaWithTimeout(MAX_TIMEOUT);
  }

  public IMessaggisticaWS getMessaggisticaWithTimeout(int timeouts[])
      throws MalformedURLException
  {
    final IMessaggisticaWS messaggisticaPort = new Messaggistica(
        new URL(PAPUASERV_MESSAGGISTICA_WSDL[RANDOM
            .nextInt(PAPUASERV_MESSAGGISTICA_WSDL.length)]),
        new QName(
            "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
            "messaggistica")).getMessaggisticaPort();
    if (timeouts != null)
    {
      setTimeout((BindingProvider) messaggisticaPort, timeouts);
    }
    return messaggisticaPort;
  }

  public void setTimeout(BindingProvider provider, int[] timeouts)
      throws MalformedURLException
  {
    Map<String, Object> requestContext = provider.getRequestContext();
    requestContext.put("javax.xml.ws.client.connectionTimeout", timeouts[0]);
    requestContext.put("javax.xml.ws.client.receiveTimeout", timeouts[1]);
  }
  public ModolPdfGeneratorSrvFacade getModolPDFGenServClient() throws Exception
  {
    ModolxppdfgensrvServiceDto modolxpsrvService = new ModolxppdfgensrvServiceDto();
    modolxpsrvService.setServer(MODOL_ENDPOINT);
    modolxpsrvService.setContext(CONTEXT_MODOLGENSRV);
    modolxpsrvService.setPort(MODOL_PORT);
    ModolPdfGeneratorSrvFacade modolxpsrv = ModolxppdfgensrvServiceClient.getModolxppdfgensrvService(modolxpsrvService);

    return modolxpsrv;
  }
  
  public ModolSrvFacade getModolServClient() throws Exception
  {

    ModolxpsrvServiceDto modolxpsrvService = new ModolxpsrvServiceDto();

    modolxpsrvService.setServer(MODOL_ENDPOINT);
    modolxpsrvService.setContext(CONTEXT_MODOLSRV);
    modolxpsrvService.setPort(MODOL_PORT);
    ModolSrvFacade modolxpsrv = ModolxpsrvServiceClient.getModolxpsrvService(modolxpsrvService);

    return modolxpsrv;
  }
  
  public String getModolXPServClientURL()
  {
    return MODOL_ENDPOINT + ":" + MODOL_PORT + CONTEXT_MODOLSRV;
  }

  public String getModolXPPDFGenServClientURL()
  {
    return MODOL_ENDPOINT + ":" + MODOL_PORT +  CONTEXT_MODOLGENSRV;
  }
  
}