package it.csi.nembo.nemboconf.presentation.configurazionecataloghi;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.reportistica.ColReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.dto.reportistica.ReportVO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.presentation.taglib.nemboconf.NavTabsConfigurazioneCataloghiTag;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("/configurazionecataloghi")
public class ConfigurazioneCataloghiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "index", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return visualizzaTabella(
        NavTabsConfigurazioneCataloghiTag.TABS.QUADRI.toString(), model,
        session);
  }

  @RequestMapping(value = "visualizza_tabella_{quadroCatalogo}", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String visualizzaTabella(
      @PathVariable(value = "quadroCatalogo") String quadroCatalogo,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    prepareCommonData(quadroCatalogo, model);
    return "configurazionecataloghi/gestisciTabella";
  }

  @RequestMapping(value = "addrow_{quadroCatalogo}", method = RequestMethod.GET)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String addRow(
      @PathVariable(value = "quadroCatalogo") String quadroCatalogo,
      Model model, HttpSession session) throws InternalUnexpectedException
  {
    prepareCommonData(quadroCatalogo, model);
    return "configurazionecataloghi/aggiungiRigaTabella";
  }

  @RequestMapping(value = "addrow_{quadroCatalogo}", method = RequestMethod.POST)
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  public String addRowPost(
      @PathVariable(value = "quadroCatalogo") String quadroCatalogo,
      Model model, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("preferRequestValuesTabella", Boolean.TRUE);
    LinkedHashMap<String, String> mapValues = new LinkedHashMap<String, String>();
    GraficoVO descTabella = prepareCommonData(quadroCatalogo, model);
    Errors errors = validaForm(descTabella.getReportVO(), request, mapValues);

    if (errors.isEmpty())
    {
      cruscottoEJB.insertRow(descTabella.getNomeTabella(), mapValues);
      return visualizzaTabella(quadroCatalogo, model, session);
    }

    model.addAttribute("errors", errors);
    return "configurazionecataloghi/aggiungiRigaTabella";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "deleterow_{quadroCatalogo}_{id}", method = RequestMethod.GET)
  public String eliminaintervento(
      @PathVariable(value = "quadroCatalogo") String quadroCatalogo,
      @PathVariable(value = "id") long id,
      Model model) throws InternalUnexpectedException
  {
    model.addAttribute("message",
        "Stai per eliminare il record scelto, vuoi continuare? ");
    model.addAttribute("id", id);
    model.addAttribute("quadroCatalogo", quadroCatalogo);
    return "configurazionecataloghi/confermaEliminaRecord";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "confermaeliminarow_{quadroCatalogo}_{id}", method = RequestMethod.POST)
  public String eliminainterventoPost(
      @PathVariable(value = "quadroCatalogo") String quadroCatalogo,
      @PathVariable(value = "id") long id,
      Model model) throws InternalUnexpectedException
  {
    GraficoVO tabella = prepareCommonData(quadroCatalogo, model);
    cruscottoEJB.deleteRowByID(tabella.getNomeTabella(),
        tabella.getReportVO().getColsDefinitions().get(0).getLabel(), id);
    return "redirect:visualizza_tabella_" + quadroCatalogo + ".do";
  }

  private GraficoVO prepareCommonData(String quadroCatalogo, Model model)
      throws InternalUnexpectedException
  {
    String nomeTabella = null;

    if (quadroCatalogo
        .equals(NavTabsConfigurazioneCataloghiTag.TABS.QUADRI.toString()))
    {
      nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_QUADRO;
    }
    else
      if (quadroCatalogo
          .equals(NavTabsConfigurazioneCataloghiTag.TABS.OGGETTI.toString()))
      {
        nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_OGGETTO;
      }
      else
        if (quadroCatalogo.equals(
            NavTabsConfigurazioneCataloghiTag.TABS.CONTROLLI.toString()))
        {
          nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_CONTROLLO;
        }
        else
          if (quadroCatalogo
              .equals(NavTabsConfigurazioneCataloghiTag.TABS.AZIONI.toString()))
          {
            nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_AZIONE;
          }
          else
            if (quadroCatalogo.equals(
                NavTabsConfigurazioneCataloghiTag.TABS.ELENCO_CDU.toString()))
            {
              nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_ELENCO_CDU;
            }
            else
              if (quadroCatalogo.equals(
                  NavTabsConfigurazioneCataloghiTag.TABS.ICONE.toString()))
              {
                nomeTabella = NemboConstants.NOMI_TABELLE.NEMBO_D_ICONA;
              }
              else
              {
                throw new InternalUnexpectedException("Quadro non trovato!",
                    null);
              }

    GraficoVO descTabella = cruscottoEJB.getDescTabella(nomeTabella);
    descTabella.setNomeTabella(nomeTabella);
    model.addAttribute("quadroCatalogo", quadroCatalogo);
    model.addAttribute("tabella", descTabella);

    StringBuffer filtri = new StringBuffer(
        " $('#filter-bar').bootstrapTableFilter({ "
            + " filters:[ ");

    if (descTabella.getReportVO().getColsDefinitions() != null)
    {
      for (int i = 0; i <= descTabella.getReportVO().getColsDefinitions().size()
          - 1; i++)
      {
        ColReportVO col = descTabella.getReportVO().getColsDefinitions().get(i);

        filtri.append(" {  "
            + "	  field: '" + col.getId() + "' ,	"
            + "	  label: '" + col.getLabel() + "',	"
            + "	  type: '"
            + (("datetime".equals(col.getType())) ? "date" : "search")
            + "'				"
            + "	} ");
        if (i < descTabella.getReportVO().getColsDefinitions().size() - 1)
        {
          filtri.append(" , ");
        }
      }
    }
    filtri.append(" ], connectTo: '#dettEstrazioneTable', "
        + " onSubmit: function() { "
        + " } "
        + " });");
    model.addAttribute("filtriTabella", filtri.toString());

    return descTabella;
  }

  private Errors validaForm(ReportVO reportVO, HttpServletRequest request,
      LinkedHashMap<String, String> mapValues)
  {
    Errors errors = new Errors();
    int count = 0;
    for (ColReportVO colonna : reportVO.getColsDefinitions())
    {
      String inputValue = (String) request.getParameter(colonna.getId());
      mapValues.put(colonna.getId(), inputValue);

      count++;
      if (count == 1)
      {
        continue;
      }

      if (!colonna.isNullable())
      {
        errors.validateMandatory(inputValue, colonna.getId());
      }
      if (!GenericValidator.isBlankOrNull(inputValue)
          && colonna.getMaxSize() > 0
          && (ColReportVO.TYPE_NUMBER.equals(colonna.getType())
              || ColReportVO.TYPE_STRING.equals(colonna.getType())))
      {
        errors.validateFieldMaxLength(inputValue, colonna.getId(),
            colonna.getMaxSize());
      }
      if (!GenericValidator.isBlankOrNull(inputValue)
          && ColReportVO.TYPE_NUMBER.equals(colonna.getType()))
      {
        errors.validateBigDecimal(inputValue, colonna.getId(), 2);
      }
      if (!GenericValidator.isBlankOrNull(inputValue)
          && ColReportVO.TYPE_DATE.equals(colonna.getType()))
      {
        errors.validateDate(inputValue, colonna.getId(), true);
      }
      if (!GenericValidator.isBlankOrNull(inputValue)
          && ColReportVO.TYPE_DATETIME.equals(colonna.getType()))
      {
        errors.validateDateTime(inputValue, colonna.getId(), true);
      }
    }

    return errors;
  }

}
