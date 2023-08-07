package it.csi.nembo.nembopratiche.integration.ws.smrcomms.smrcommsrvservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import it.csi.smrcomms.siapcommws.interfacews.smrcomm.SmrcommSrv;

/**
 * This class was generated by Apache CXF 3.1.6 2016-10-03T15:01:38.840+02:00
 * Generated source version: 3.1.6
 * 
 */
@WebServiceClient(name = "SmrcommSrvService", wsdlLocation = "file:siapcomm/SmrcommSrvService_1.wsdl", targetNamespace = "http://smrcomm.business.siapcommws.smrcomms.csi.it/")
public class SmrcommSrvService extends Service
{

  public final static URL   WSDL_LOCATION;

  public final static QName SERVICE        = new QName(
      "http://smrcomm.business.siapcommws.smrcomms.csi.it/",
      "SmrcommSrvService");
  public final static QName SmrcommSrvPort = new QName(
      "http://smrcomm.business.siapcommws.smrcomms.csi.it/", "SmrcommSrvPort");
  static
  {
    URL url = null;
    try
    {
      url = new URL("file:siapcomm/SmrcommSrvService_1.wsdl");
    }
    catch (MalformedURLException e)
    {
      java.util.logging.Logger.getLogger(SmrcommSrvService.class.getName())
          .log(java.util.logging.Level.INFO,
              "Can not initialize the default wsdl from {0}",
              "file:siapcomm/SmrcommSrvService_1.wsdl");
    }
    WSDL_LOCATION = url;
  }

  public SmrcommSrvService(URL wsdlLocation)
  {
    super(wsdlLocation, SERVICE);
  }

  public SmrcommSrvService(URL wsdlLocation, QName serviceName)
  {
    super(wsdlLocation, serviceName);
  }

  public SmrcommSrvService()
  {
    super(WSDL_LOCATION, SERVICE);
  }

  public SmrcommSrvService(WebServiceFeature... features)
  {
    super(WSDL_LOCATION, SERVICE, features);
  }

  public SmrcommSrvService(URL wsdlLocation, WebServiceFeature... features)
  {
    super(wsdlLocation, SERVICE, features);
  }

  public SmrcommSrvService(URL wsdlLocation, QName serviceName,
      WebServiceFeature... features)
  {
    super(wsdlLocation, serviceName, features);
  }

  @WebEndpoint(name = "SmrcommSrvPort")
  public SmrcommSrv getSmrcommSrvPort()
  {
    return super.getPort(SmrcommSrvPort, SmrcommSrv.class);
  }

}
