package it.csi.nembo.nemboconf.presentation.catalogomisure;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("catalogomisure/focusarea/")
public class FocusAreaController
{
  @Autowired
  private ICatalogoMisureEJB catMisureEJB = null;

  private void setTestataDetails(long idLivello, ModelMap model)
      throws InternalUnexpectedException
  {
    TipoOperazioneDTO tipoOp = catMisureEJB.getAlberaturaOperazione(idLivello);
    model.addAttribute("dettagliTestata", tipoOp);
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "dettaglio_{idLivello}", method = RequestMethod.GET)
  public String getIndex(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("elenco", catMisureEJB.getFocusArea(idLivello));
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/elencoFocusArea";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "modifica_{idLivello}", method = RequestMethod.GET)
  public String modificaGet(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("elenco", catMisureEJB.getElencoFocusArea(idLivello));
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/modificaFocusArea";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "modifica_{idLivello}", method = RequestMethod.POST)
  public String modificaPost(@PathVariable(value = "idLivello") long idLivello,
      ModelMap model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    String idFaPrincipale = request.getParameter("faPrimaria");
    String[] idFaSecondari = request.getParameterValues("faSecondaria");

    if (GenericValidator.isBlankOrNull(idFaPrincipale)
        && (idFaSecondari != null && idFaSecondari.length > 0))
    {
      model.addAttribute("msgErrore",
          "Non è possibile selezionare solamente Focus Area secondarie!");
    }
    else
    {
      if (idFaPrincipale != null && idFaSecondari != null)
      {
        for (String id : idFaSecondari)
        {
          if (idFaPrincipale.equals(id))
          {
            model.addAttribute("msgErrore",
                "Hai selezionato la Focus Area sia come primaria che come secondaria!");
            model.addAttribute("idLivello", String.valueOf(idLivello));
            addElencoToModel(idFaPrincipale, idFaSecondari, idLivello, model);
            setTestataDetails(idLivello, model);
            return "catalogomisure/modificaFocusArea";
          }
        }
      }
      String message = catMisureEJB.abbinaFocusArea(idLivello, idFaPrincipale,
          idFaSecondari);

      if (message != null && message.trim().length() > 0)
      {
        errors.addError("faPrimaria", message);
      }
      else
      {
        return "redirect: dettaglio_" + idLivello + ".do";
      }
    }

    setTestataDetails(idLivello, model);
    addElencoToModel(idFaPrincipale, idFaSecondari, idLivello, model);
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/modificaFocusArea";
  }

  private void addElencoToModel(String idFaPrincipale, String[] idFaSecondari,
      long idLivello, ModelMap model) throws InternalUnexpectedException
  {
    List<FocusAreaDTO> elenco = catMisureEJB.getElencoFocusArea(idLivello);
    for (FocusAreaDTO fa : elenco)
    {
      fa.setPrimaria(false);
      fa.setSecondaria(false);
      if (idFaPrincipale != null
          && (fa.getIdFocusArea() == Long.parseLong(idFaPrincipale)))
      {
        fa.setPrimaria(true);
      }

      if (idFaSecondari != null)
      {
        for (String id : idFaSecondari)
        {
          if (fa.getIdFocusArea() == Long.parseLong(id))
          {
            fa.setSecondaria(true);
          }
        }
      }
    }
    model.addAttribute("elenco", elenco);
  }

}
