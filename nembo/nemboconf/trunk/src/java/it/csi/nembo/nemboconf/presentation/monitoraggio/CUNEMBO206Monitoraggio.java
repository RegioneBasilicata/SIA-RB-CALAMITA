package it.csi.nembo.nemboconf.presentation.monitoraggio;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.MONITORAGGIO)
@RequestMapping(value = "/monitoraggio")
public class CUNEMBO206Monitoraggio extends BaseController
{
  @Autowired
  private ICruscottoBandiEJB    cruscottoBandiEJB;                  // Va bene
                                                                    // un
                                                                    // qualsiasi
                                                                    // ejb
  protected static final String IMAGE_SUCCESS   = "success_big.png";
  protected static final String IMAGE_ERROR     = "fail_big.png";
  protected static final String DB_OBJECT_VALID = "VALID";

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session)
  {
    List<String[]> servizi = new ArrayList<String[]>();
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    servizi.add(checkWSProfilazione(utenteAbilitazioni));
    boolean serviziOK = true;
    for (String[] servizio : servizi)
    {
      if (IMAGE_ERROR.equals(servizio[1]))
      {
        serviziOK = false;
        break;
      }
    }
    if (serviziOK)
    {
      model.addAttribute("serviziOK", Boolean.valueOf(serviziOK));
    }
    model.addAttribute("servizi", servizi);
    boolean utenteServiziAgri = utenteAbilitazioni.getRuolo()
        .isUtenteServiziAgri();
    if (utenteServiziAgri)
    {
      List<String[]> statoDB = listDatabaseStatus();
      model.addAttribute("statoDB", statoDB);
      model.addAttribute("pulsanteBack", Boolean.TRUE);
    }
    else
    {
      model.addAttribute("pulsanteBack", Boolean.FALSE);
    }
    model.addAttribute("applicativoOK", checkDatabase());
    readVersionPropertyFile(model);
    return "monitoraggio/monitoraggio";
  }

  public String readVersionPropertyFile(Model model)
  {
    try
    {
      ResourceBundle version = ResourceBundle.getBundle("version");
      final String componentName = version.getString("ComponentName");
      if (componentName != null)
      {
        model.addAttribute("componentName", componentName.toUpperCase());
        model.addAttribute("componentVersion",
            version.getString("ComponentVersion"));
        model.addAttribute("builtBy", version.getString("Built-By"));
        model.addAttribute("builtDate",
            convertPropertyBuiltDate(version.getString("Built-Date")));
        String target = version.getString("Target");
        model.addAttribute("target", target);
        model.addAttribute("antVersion", version.getString("Ant-Version"));
        model.addAttribute("createdBy", version.getString("Created-By"));
        return target;
      }
    }
    catch (Exception E)
    {
      // handle
    }
    return null;
  }

  public void readManifestInfo(Model model)
  {
    Enumeration<URL> resources;
    try
    {
      resources = getClass().getClassLoader()
          .getResources("META-INF/MANIFEST.MF");
      while (resources.hasMoreElements())
      {
        try
        {
          Manifest manifest = new Manifest(
              resources.nextElement().openStream());
          Attributes attr = manifest.getMainAttributes();
          final String componentName = attr.getValue("ComponentName");
          if (componentName != null)
          {
            model.addAttribute("componentName", componentName.toUpperCase());
            model.addAttribute("componentVersion",
                attr.getValue("ComponentVersion"));
            model.addAttribute("builtBy", attr.getValue("Built-By"));
            model.addAttribute("builtDate",
                convertBuiltDate(attr.getValue("Built-Date")));
            model.addAttribute("target", attr.getValue("Target"));
            model.addAttribute("antVersion", attr.getValue("Ant-Version"));
            model.addAttribute("createdBy", attr.getValue("Created-By"));
            return;
          }
        }
        catch (IOException E)
        {
          // handle
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private String convertPropertyBuiltDate(String value)
  {
    SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    try
    {
      return NemboUtils.DATE.formatDateTime(sd.parse(value));
    }
    catch (ParseException e)
    {
      return value;
    }
  }

  private String convertBuiltDate(String value)
  {
    SimpleDateFormat sd = new SimpleDateFormat("yyyymmdd");
    try
    {
      return NemboUtils.DATE.formatDate(sd.parse(value));
    }
    catch (ParseException e)
    {
      return value;
    }
  }

  private Boolean checkDatabase()
  {
    try
    {
      // TEST del Database
      cruscottoBandiEJB.getSysDate(); // Ignoro il valore, mi interessa che
                                      // esegua la query e non vada in eccezione
      // Se sono arrivato qui non ci sono state eccezioni, quindi test eseguito
      // con successo ==> DATABASE OK
      return Boolean.TRUE;
    }
    catch (Exception e)
    {
      return Boolean.FALSE;
    }
  }

  private List<String[]> listDatabaseStatus()
  {
    try
    {
      // TEST dei package/function
      List<String[]> list = cruscottoBandiEJB.getStatoDatabase();
      if (list != null)
      {
        for (String[] dbObject : list)
        {
          if (DB_OBJECT_VALID.equalsIgnoreCase(dbObject[1]))
          {
            dbObject[2] = IMAGE_SUCCESS;
          }
          else
          {
            dbObject[2] = IMAGE_ERROR;
          }
        }
      }
      // Se sono arrivato qui non ci sono state eccezioni, quindi test eseguito
      // con successo
      return list;
    }
    catch (Exception e)
    {
      return null;
    }
  }

  private String[] checkWSProfilazione(UtenteAbilitazioni utenteAbilitazioni)
  {
    String[] result =
    { "Profilazione", null, null, PapuaservProfilazioneServiceFactory
        .getRestServiceClient().getRestServiceUrl() };
    try
    {
      PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .findAttoriForRuoloInApplicazione(
              utenteAbilitazioni.getRuolo().getCodice(), 15,
              NemboConstants.NEMBOCONF.ID);
      result[1] = IMAGE_SUCCESS;
      return result;
    }
    catch (Exception e)
    {
      result[1] = IMAGE_ERROR;
      result[2] = e.toString();
      return result;
    }
  }

}
