package it.csi.nembo.nemboconf.presentation;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.dto.MapColonneNascosteVO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping(value = "/session")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
public class GestioneSessioneController extends BaseController
{
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/salvaFiltri", method = RequestMethod.POST)
  @ResponseBody
  public String salvaFiltri(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String key = request.getParameter("key");
    String filtro = request.getParameter("filtro");

    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(NemboConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA);
    if (mapFilters == null)
    {
      mapFilters = new HashMap<String, String>();
    }

    if (mapFilters.containsKey(key))
    {
      mapFilters.remove(key);
      mapFilters.put(key, filtro);
    }
    else
    {
      mapFilters.put(key, filtro);
    }
    session.setAttribute(NemboConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA,
        mapFilters);
    return "OK";
  }

  @RequestMapping(value = "/salvaColonna", method = RequestMethod.POST)
  @ResponseBody
  public String salvaColonna(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String key = request.getParameter("key");
    String field = request.getParameter("field");
    String value = request.getParameter("value");

    MapColonneNascosteVO hColumns = (MapColonneNascosteVO) session.getAttribute(
        NemboConstants.GENERIC.SESSION_VAR_AZIENDE_COLONNE_NASCOSTE);
    if (hColumns == null)
    {
      hColumns = new MapColonneNascosteVO();
    }

    if ("false".equalsIgnoreCase(value))
    {
      if (hColumns.containsKey(key))
      {
        hColumns.get(key).put(field, true);
      }
      else
      {
        HashMap<String, Boolean> vCols = new HashMap<String, Boolean>();
        vCols.put(field, true);
        hColumns.put(key, vCols);
      }
    }
    else
      if ("true".equalsIgnoreCase(value))
      {
        if (hColumns.containsKey(key))
        {
          hColumns.get(key).put(field, false);
        }
        else
        {
          HashMap<String, Boolean> vCols = new HashMap<String, Boolean>();
          vCols.put(field, false);
          hColumns.put(key, vCols);
        }
      }
    session.setAttribute(
        NemboConstants.GENERIC.SESSION_VAR_AZIENDE_COLONNE_NASCOSTE,
        hColumns);
    return "OK";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/salvaNumeroPagina", method = RequestMethod.POST)
  @ResponseBody
  public String salvaNumeroPagina(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String key = request.getParameter("key");
    String page = request.getParameter("page");

    HashMap<String, String> mapFilters = (HashMap<String, String>) session
        .getAttribute(NemboConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA);
    if (mapFilters == null)
    {
      mapFilters = new HashMap<String, String>();
    }

    if (mapFilters.containsKey(key))
    {
      mapFilters.remove(key);
      mapFilters.put(key, page);
    }
    else
    {
      mapFilters.put(key, page);
    }
    session.setAttribute(NemboConstants.GENERIC.SESSION_VAR_NUMERO_PAGINA,
        mapFilters);
    return "OK";
  }
}
