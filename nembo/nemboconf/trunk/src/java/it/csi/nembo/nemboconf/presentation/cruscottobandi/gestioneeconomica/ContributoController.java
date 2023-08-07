package it.csi.nembo.nemboconf.presentation.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.IEconomieEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.ContributoDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class ContributoController extends BaseController
{

  @Autowired
  private IEconomieEJB economieEJB = null;

  @RequestMapping(value = "contributo")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "gesteconomica/contributo/visualizzacontributo";
  }

  @RequestMapping(value = "getElencoContributi", produces = "application/json")
  @ResponseBody
  public List<ContributoDTO> getElencoContributi(HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    return economieEJB.getElencoContributi(bando.getIdBando(), null);
  }

  @RequestMapping(value = "/modifica", method = RequestMethod.GET)
  public String modifica(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    List<Long> idLivelli = NemboUtils.LIST
        .toListOfLong(request.getParameterValues("idLivello"));
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    List<ContributoDTO> lContributi = economieEJB
        .getElencoContributi(bando.getIdBando(), idLivelli);
    model.addAttribute("elenco", lContributi);
    session.setAttribute("elencoContributoDTO", lContributi);
    return "gesteconomica/contributo/modificacontributo";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String modificaPost(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    List<ContributoDTO> lContributi = (List<ContributoDTO>) session
        .getAttribute("elencoContributoDTO");

    Errors errors = new Errors();
    String pMinima;
    String pMassima;
    String massimale;

    for (ContributoDTO item : lContributi)
    {
      pMinima = request.getParameter("percMinima_" + item.getIdLivello());
      pMassima = request.getParameter("percMassima_" + item.getIdLivello());
      massimale = request.getParameter("massimaleSpesa_" + item.getIdLivello());

      errors.validateMandatoryBigDecimalInRange(pMinima,
          "percMinima_" + item.getIdLivello(), 2, BigDecimal.ZERO,
          new BigDecimal("100"));
      errors.validateMandatoryBigDecimalInRange(pMassima,
          "percMassima_" + item.getIdLivello(), 2, BigDecimal.ZERO,
          new BigDecimal("100"));

      if (!GenericValidator.isBlankOrNull(massimale))
      {
        errors.validateBigDecimalInRange(massimale,
            "massimaleSpesa_" + item.getIdLivello(), 2, BigDecimal.ZERO,
            new BigDecimal("9999999999.99"));
      }

      if (errors.isEmpty())
      {
        BigDecimal bdPercMinima = new BigDecimal(
            pMinima.trim().replace(',', '.'));
        BigDecimal bdPercMassima = new BigDecimal(
            pMassima.trim().replace(',', '.'));

        if (bdPercMinima.compareTo(bdPercMassima) == 1)
        {
          errors.addError("percMinima_" + item.getIdLivello(),
              "La percentuale minima non può superare la percentuale massima");
          model.addAttribute("prefRqValues", Boolean.TRUE);
          model.addAttribute("errors", errors);
          return modifica(model, request, session);
        }

        if (GenericValidator.isBlankOrNull(massimale))
        {
          item.setMassimaleSpesa(null);
        }
        else
        {
          item.setMassimaleSpesa(
              new BigDecimal(massimale.trim().replace(',', '.')));
        }
        item.setPercMinima(bdPercMinima);
        item.setPercMassima(bdPercMassima);
      }
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("prefRqValues", Boolean.TRUE);
      model.addAttribute("errors", errors);
      return modifica(model, request, session);
    }
    else
    {
      economieEJB.updateContributoLivelli(lContributi);
      return index(model, session);
    }
  }

}
