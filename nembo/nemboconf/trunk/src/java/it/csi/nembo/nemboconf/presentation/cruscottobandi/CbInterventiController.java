package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.CruscottoInterventiDTO;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class CbInterventiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "interventi", method = RequestMethod.GET)
  public String interventiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    boolean modificaAbilitata = (bando.getFlagMaster().equals("S")) ? true
        : cruscottoEJB.isBandoModificabile(idBando);
    List<CruscottoInterventiDTO> elenco = cruscottoEJB.getInterventi(idBando);
    session.setAttribute("elencoInterventi", elenco);
    session.setAttribute("modificaInterventiAbilitata", modificaAbilitata);

    if (!model.containsKey("fromRequest"))
      model.addAttribute("fromRequest", false);
    model.addAttribute("elenco", elenco);
    model.addAttribute("idBando", idBando);
    model.addAttribute("modificaAbilitata", modificaAbilitata);

    if (bando.getCodiceTipoBando() != null
        && bando.getCodiceTipoBando().compareTo("G") == 0)
      model.addAttribute("isGal", true);

    return "cruscottobandi/interventi";
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "interventi", method = RequestMethod.POST)
  public String interventiPost(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @ModelAttribute("errors") Errors errors)
      throws InternalUnexpectedException
  {
    String costoUnitMin = "";
    String costoUnitMax = "";
    model.addAttribute("fromRequest", true);
    List<CruscottoInterventiDTO> elenco = (List<CruscottoInterventiDTO>) session
        .getAttribute("elencoInterventi");

    if (elenco != null)
    {
      for (CruscottoInterventiDTO row : elenco)
      {
        // guardo se l'utente ha inserito i costi unitari o se era obbligato a
        // farlo
        costoUnitMin = request
            .getParameter("costoUnitMin_" + row.getIdDescrizioneIntervento());
        costoUnitMax = request
            .getParameter("costoUnitMax_" + row.getIdDescrizioneIntervento());

        if (!GenericValidator.isBlankOrNull(costoUnitMin)
            || !GenericValidator.isBlankOrNull(costoUnitMax)
            || "S".equals(row.getFlagGestioneCostoUnitario()))
        {
          errors.validateMandatoryBigDecimal(costoUnitMin,
              "costoUnitMin_" + row.getIdDescrizioneIntervento(), 2);
          errors.validateMandatoryBigDecimal(costoUnitMax,
              "costoUnitMax_" + row.getIdDescrizioneIntervento(), 2);

          if (errors.isEmpty())
          {
            if (!GenericValidator.isBlankOrNull(costoUnitMin))
            {
              BigDecimal bdCostoUnitMin = new BigDecimal(
                  costoUnitMin.trim().replace(',', '.'));
              if (bdCostoUnitMin.longValue() == new Long("0"))
                errors.addError(
                    "costoUnitMin_" + row.getIdDescrizioneIntervento(),
                    "Il valore deve essere maggiore di zero!");
            }
            if (!GenericValidator.isBlankOrNull(costoUnitMax))
            {
              BigDecimal bdCostoUnitMax = new BigDecimal(
                  costoUnitMax.trim().replace(',', '.'));
              if (bdCostoUnitMax.longValue() == new Long("0"))
                errors.addError(
                    "costoUnitMin_" + row.getIdDescrizioneIntervento(),
                    "Il valore deve essere maggiore di zero!");
            }
          }
        }
        if (errors.isEmpty() && !GenericValidator.isBlankOrNull(costoUnitMin)
            && !GenericValidator.isBlankOrNull(costoUnitMax))
        {
          BigDecimal bdCostoUnitMin = new BigDecimal(
              costoUnitMin.trim().replace(',', '.'));
          BigDecimal bdCostoUnitMax = new BigDecimal(
              costoUnitMax.trim().replace(',', '.'));
          if (bdCostoUnitMax.compareTo(bdCostoUnitMin) == -1)
            errors.addError("costoUnitMax_" + row.getIdDescrizioneIntervento(),
                "Il costo unitario massimo deve essere superiore al costo unitario minimo!");

          if (errors.isEmpty())
          {
            long idBando = getIdBando(session);
            cruscottoEJB.updateIntervento(idBando,
                row.getIdDescrizioneIntervento(), row.getIdLivello(),
                bdCostoUnitMin, bdCostoUnitMax);

          }
        }

      }
    }

    if (errors.isEmpty())
    {

      List<CruscottoInterventiDTO> elencoAggiornato = (List<CruscottoInterventiDTO>) session
          .getAttribute("nuovoElenco");
      List<CruscottoInterventiDTO> elencoPrec = (List<CruscottoInterventiDTO>) session
          .getAttribute("vecchioElenco");

      if (elencoAggiornato != null && elencoPrec != null)
      {
        Iterator<CruscottoInterventiDTO> elAggIter = elencoAggiornato
            .iterator();
        Iterator<CruscottoInterventiDTO> elPrecIter = elencoPrec.iterator();
        boolean uguali = true;
        if (elencoAggiornato.size() == elencoPrec.size())
          while (elAggIter.hasNext() && elPrecIter.hasNext())
          {
            if (elAggIter.next().getIdDescrizioneIntervento() != elPrecIter
                .next().getIdDescrizioneIntervento())
              uguali = false;
          }
        else
          uguali = false;

        if (!uguali)
        {
          UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
              .getAttribute("utenteAbilitazioni");
          Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
          Long idBandoOggetto = cruscottoEJB
              .getFirstIdBandoOggetto(getIdBando(session));

          String oldList = "", newList = "";
          for (CruscottoInterventiDTO i : elencoPrec)
            oldList = oldList.concat(i.getDescrIntervento() + " - ");// + " " +
                                                                     // i.getTipoIntervento()
                                                                     // + " " +
                                                                     // i.getDescrIntervento()
                                                                     // + " -
                                                                     // ");
          for (CruscottoInterventiDTO i : elencoAggiornato)
            newList = newList.concat(i.getDescrIntervento() + " - ");// + " " +
                                                                     // i.getTipoIntervento()
                                                                     // + " " +
                                                                     // i.getDescrIntervento()
                                                                     // + " -
                                                                     // ");

          if (oldList != "")
            oldList = oldList.substring(0, oldList.length() - 3);
          if (newList != "")
            newList = newList.substring(0, newList.length() - 3);

          if (idBandoOggetto != null)
          {
            String s = "Lista prima della modifica: \"" + oldList
                + "\"  \nLista dopo la modifica: \"" + newList + "\"\n";
            if (s.length() > 3999)
              s = "Messaggio troppo lungo per il db. Come fare?";
            cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
                NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.INTERVENTI, s);
          }
        }

        if (session.getAttribute("nuovoElenco") != null)
          session.removeAttribute("nuovoElenco");
        if (session.getAttribute("vecchioElenco") != null)
          session.removeAttribute("vecchioElenco");

      }

      BandoDTO bando = cruscottoEJB.getInformazioniBando(getIdBando(session));
      if (bando.getCodiceTipoBando() != null
          && bando.getCodiceTipoBando().compareTo("G") == 0)
        return "redirect:filiere.do";

      return "redirect:oggetti.do";

    }

    return interventiGet(model, request, session);
  }

  @RequestMapping(value = "modificainterventi", method = RequestMethod.GET)
  public String modificainterventiGet(ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    model.addAttribute("idBando", idBando);
    return "cruscottobandi/modificaInterventi";
  }

  @RequestMapping(value = "modificainterventi", method = RequestMethod.POST)
  public String modificainterventiPost(ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    boolean modificaInterventiAbilitata = (Boolean) session
        .getAttribute("modificaInterventiAbilitata");
    if (!modificaInterventiAbilitata)
    {
      model.addAttribute("msgErrore",
          "Il bando è attivo! La modifica non è abilitata!");
      return "cruscottobandi/modificaInterventi";
    }
    // es: 64_4&65_4 --> idLivello_idDescrizioneIntervento
    String selectedValue = request.getParameter("selectedValues");
    if (selectedValue == null || selectedValue.trim().length() <= 0)
    {
      model.addAttribute("msgErrore", "Devi selezionare almeno un intervento!");
    }
    else
      if (isDoppioIntervento(selectedValue.split("&")))
      {
        model.addAttribute("msgErrore",
            "Hai selezionato più di una volta lo stesso intervento su livelli diversi!");
      }
      else
      {
        if (session.getAttribute("vecchioElenco") == null)
          session.setAttribute("vecchioElenco",
              cruscottoEJB.getInterventi(getIdBando(session)));
        cruscottoEJB.insertInterventi(getIdBando(session),
            strToVector(selectedValue));
        session.setAttribute("nuovoElenco",
            cruscottoEJB.getInterventi(getIdBando(session)));
        return "redirect:interventi.do";
      }

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    model.addAttribute("idBando", idBando);
    return "cruscottobandi/modificaInterventi";
  }

  @RequestMapping(value = "eliminaintervento_{idLivello}_{idDescrizioneIntervento}", method = RequestMethod.GET)
  public String eliminaintervento(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idDescrizioneIntervento") long idDescrizioneIntervento,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {
    boolean modificaInterventiAbilitata = (Boolean) session
        .getAttribute("modificaInterventiAbilitata");
    if (!modificaInterventiAbilitata)
    {
      model.addAttribute("msgErrore",
          "Il bando è attivo! La modifica non è abilitata!");
      return "cruscottobandi/modificaInterventi";
    }
    model.addAttribute("message",
        "Stai per eliminare l'intervento scelto, vuoi continuare? ");
    model.addAttribute("idDescrizioneIntervento", idDescrizioneIntervento);
    model.addAttribute("idLivello", idLivello);
    return "cruscottobandi/confermaEliminaInterventi";
  }

  @RequestMapping(value = "confermaeliminaintervento_{idLivello}_{idDescrizioneIntervento}", method = RequestMethod.POST)
  public String eliminainterventoPost(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idDescrizioneIntervento") long idDescrizioneIntervento,
      ModelMap model, HttpSession session) throws InternalUnexpectedException
  {

    if (session.getAttribute("vecchioElenco") == null)
      session.setAttribute("vecchioElenco",
          cruscottoEJB.getInterventi(getIdBando(session)));

    cruscottoEJB.eliminaIntervento(getIdBando(session), idLivello,
        idDescrizioneIntervento);
    session.setAttribute("nuovoElenco",
        cruscottoEJB.getInterventi(getIdBando(session)));

    return "redirect:interventi.do";
  }

  private Vector<DecodificaDTO<Long>> strToVector(String val)
  {
    // es: 64_4&65_4 --> idLivello_idDescrizioneIntervento
    Vector<DecodificaDTO<Long>> map = new Vector<DecodificaDTO<Long>>();

    String[] rows = val.split("&");
    for (String item : rows)
    {
      map.add(new DecodificaDTO<Long>(Long.parseLong(item.split("_")[1]),
          item.split("_")[0], ""));
    }

    return map;
  }

  private boolean isDoppioIntervento(String[] rows)
  {
    // es: array di 65_4 --> idLivello_idDescrizioneIntervento
    HashMap<String, String> map = new HashMap<String, String>();
    String idDescrizioneIntervento;
    for (String item : rows)
    {
      idDescrizioneIntervento = item.split("_")[1];

      if (map.containsKey(idDescrizioneIntervento))
      {
        return true;
      }
      else
      {
        map.put(idDescrizioneIntervento, idDescrizioneIntervento);
      }
    }

    return false;
  }

  @RequestMapping(value = "loadInterventi", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadInterventi(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    List<CruscottoInterventiDTO> elenco = cruscottoEJB
        .getElencoInterventiSelezionabili(getIdBando(session));
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (CruscottoInterventiDTO com : elenco)
      {
        String id = String.valueOf(com.getIdLivello()) + "_"
            + String.valueOf(com.getIdDescrizioneIntervento());
        parameters = new TreeMap<String, String>();
        parameters.put("idDescrizioneIntervento", id);
        parameters.put("descrizione",
            com.getOperazione() + " - " + com.getDescrIntervento());
        parameters.put("gruppo", com.getTipoIntervento());
        values.put(id, parameters);
      }

    return values;
  }

  @RequestMapping(value = "loadInterventiSelezionati", produces = "application/json")
  @ResponseBody
  public Map<String, Object> loadInterventiSelezionati(HttpSession session)
      throws InternalUnexpectedException
  {
    List<CruscottoInterventiDTO> elenco = cruscottoEJB
        .getElencoInterventiSelezionati(getIdBando(session));
    HashMap<String, Object> values = new HashMap<String, Object>();
    TreeMap<String, String> parameters = null;

    if (elenco != null && elenco.size() > 0)
      for (CruscottoInterventiDTO com : elenco)
      {
        String id = String.valueOf(com.getIdLivello()) + "_"
            + String.valueOf(com.getIdDescrizioneIntervento());
        parameters = new TreeMap<String, String>();
        parameters.put("idDescrizioneIntervento", id);
        parameters.put("descrizione",
            com.getOperazione() + " - " + com.getDescrIntervento());
        parameters.put("gruppo", com.getTipoIntervento());
        values.put(id, parameters);
      }

    return values;
  }

  @RequestMapping(value = "downloadExcel")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  public ModelAndView downloadExcel(
      @RequestParam(value = "id", required = true) long idBando, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    List<CruscottoInterventiDTO> elenco = cruscottoEJB.getInterventi(idBando);
    return new ModelAndView("excelElencoInterventiCruscottoBandiView", "elenco",
        elenco);
  }

  private long getIdBando(HttpSession session)
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    return bando.getIdBando();
  }

}
