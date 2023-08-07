package it.csi.nembo.nemboconf.util;

import java.util.Properties;

import javax.naming.Context;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.nembo.nemboconf.exception.InternalServiceException;

public class PortaDelegataUtils
{
  public static final String FILE_PD_PAPUASERV     = "papuaserv-client-2.0.0.xml";
  public static final String FILE_PD_MODOL         = "modolsrv-client.xml";
  public static final String FILE_PD_PDF_GENERATOR = "pdf-generator-client.xml";
  private InfoPortaDelegata  pdModol;
  private InfoPortaDelegata  pdPdfGenerator;

  public String getProviderURLModol()
  {
    try
    {
      if (pdModol == null)
      {
        pdModol = PDConfigReader.read(PortaDelegataUtils.class.getClassLoader()
            .getResourceAsStream(FILE_PD_MODOL));
      }
      return extractProviderURL(pdModol);
    }
    catch (Exception e)
    {
      return "#ERROR# exception = " + e.getMessage();
    }
  }

  public String extractProviderURL(InfoPortaDelegata pdInfo)
  {
    try
    {
      Properties propFirstPD = pdInfo.getPlugins()[0].getProperties();
      return (String) propFirstPD.get(Context.PROVIDER_URL);
    }
    catch (Exception e)
    {
      return "#ERROR# exception = " + e.getMessage();
    }
  }

  public InfoPortaDelegata getInfoPortaDelegataModol()
      throws InternalServiceException
  {
    try
    {
      if (pdModol == null)
      {
        pdModol = PDConfigReader.read(PortaDelegataUtils.class.getClassLoader()
            .getResourceAsStream(FILE_PD_MODOL));
      }
      return pdModol;
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Errore di accesso al servizio MODOLSRV", e);
    }
  }
}
