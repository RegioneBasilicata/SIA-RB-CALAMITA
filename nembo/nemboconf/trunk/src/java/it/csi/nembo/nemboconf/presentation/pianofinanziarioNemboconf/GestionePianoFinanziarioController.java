package it.csi.nembo.nemboconf.presentation.pianofinanziarioNemboconf;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.IPianoFinanziarioNemboEJB;
import it.csi.nembo.nemboconf.dto.Totali;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.SottoMisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE) // ,
                                                                                                                                // idLivello
                                                                                                                                // =
                                                                                                                                // 60000)
@RequestMapping(value = "/pianofinanziario")
public class GestionePianoFinanziarioController extends BaseController
{

  private static final BigDecimal       MAX_IMPORTO = new BigDecimal(
      "9999999999.99");
  @Autowired
  private IPianoFinanziarioNemboEJB pianoFinanziarioNemboconf;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_PIANO_FINANZIARIO_REGIONALE)
  @RequestMapping(value = "gestione_{idPianoFinanziario}")
  public String storico(HttpSession session, Model model,
      @ModelAttribute("idPianoFinanziario") @PathVariable("idPianoFinanziario") long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    List<MisuraDTO> elencoLivelli = pianoFinanziarioNemboconf
        .getElencoLivelli(idPianoFinanziario);
    model.addAttribute("misure", elencoLivelli);
    model.addAttribute("totali", Totali.calcolaTotali(elencoLivelli));
    model.addAttribute("modificaUtente",
        isModificabile(idPianoFinanziario, session));
    model.addAttribute("modifica", Boolean.TRUE);
    return "pianofinanziario/gestione";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE, idLivello = NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO)
  @RequestMapping(value = "modifica_{idPianoFinanziario}_{idLivello}", method = RequestMethod.GET)
  public String visualizzaModificaFinanziario(HttpSession session, Model model,
      @ModelAttribute("idPianoFinanziario") @PathVariable("idPianoFinanziario") long idPianoFinanziario,
      @ModelAttribute("idLivello") @PathVariable("idLivello") long idLivello)
      throws InternalUnexpectedException
  {
    ImportoFocusAreaDTO importoFocusAreaDTO = pianoFinanziarioNemboconf
        .getImportoPianoFinanziario(idPianoFinanziario, idLivello);
    if (importoFocusAreaDTO != null)
    {
      model.addAttribute("importo", NemboUtils.FORMAT
          .formatGenericNumber(importoFocusAreaDTO.getImporto(), 2, true));
      model.addAttribute("trascinato",
          NemboUtils.FORMAT.formatGenericNumber(
              importoFocusAreaDTO.getImportoTrascinato(), 2, true));
    }
    return "pianofinanziario/popupModificaLivello";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE)
  @RequestMapping(value = "visualizzaStorico_{idPianoFinanziario}_{idLivello}", method = RequestMethod.GET)
  public String visualizzaStorico(HttpSession session, Model model,
      @ModelAttribute("idPianoFinanziario") @PathVariable("idPianoFinanziario") long idPianoFinanziario,
      @ModelAttribute("idLivello") @PathVariable("idLivello") long idLivello)
      throws InternalUnexpectedException
  {
    ImportoFocusAreaDTO importoFocusAreaDTOCorrente = pianoFinanziarioNemboconf
        .getImportoPianoFinanziario(idPianoFinanziario, idLivello);
    List<ImportoFocusAreaDTO> storicoImporti = pianoFinanziarioNemboconf
        .getStoricoImportiPianoFinanziario(idPianoFinanziario, idLivello);

    // servizio di papua per la decodifica di tutti gli idUtenti
    // quelli che trovo li metto in una mappa, cosi faccio meno chiamate (stesso
    // id= stessa descrizione
    Map<Long, String> mapUtenti = new HashMap<>();
    for (ImportoFocusAreaDTO i : storicoImporti)
    {
      if (!mapUtenti.containsKey(i.getExtIdUtenteAggiornamento()))
      {
        List<Long> idUtenti = new ArrayList<Long>();
        idUtenti.add(i.getExtIdUtenteAggiornamento());
        List<UtenteLogin> utentiList = loadRuoloDescr(idUtenti);
        String descr = super.getUtenteDescrizione(
            i.getExtIdUtenteAggiornamento(), utentiList);
        i.setInfoModifica(i.getDataInserimentoStr() + " - " + descr);
        mapUtenti.put(i.getExtIdUtenteAggiornamento(), descr);
      }
      else
      {
        i.setInfoModifica(i.getDataInserimentoStr() + " - "
            + mapUtenti.get(i.getExtIdUtenteAggiornamento()));
      }
    }

    model.addAttribute("storicoImporti", storicoImporti);
    model.addAttribute("importoFocusAreaDTOCorrente",
        importoFocusAreaDTOCorrente);
    return "pianofinanziario/storicoImporti";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE, idLivello = NemboConstants.LIVELLI_ABILITAZIONI.MODIFICA_PIANO_FINANZIARIO)
  @RequestMapping(value = "modifica_{idPianoFinanziario}_{idLivello}", method = RequestMethod.POST)
  public String modificaPianoFinanziario(HttpServletRequest request,
      Model model,
      @ModelAttribute("idPianoFinanziario") @PathVariable("idPianoFinanziario") long idPianoFinanziario,
      @ModelAttribute("idLivello") @PathVariable("idLivello") long idLivello)
      throws InternalUnexpectedException
  {

    ImportoFocusAreaDTO importoFocusAreaDTOCorrente = pianoFinanziarioNemboconf
        .getImportoPianoFinanziario(idPianoFinanziario, idLivello);
    model.addAttribute("preferRequest", Boolean.TRUE);
    String sTmpBudget = request.getParameter("budget");
    String sTmpTrascinato = request.getParameter("trascinato");
    String motivazioni = request.getParameter("motivazioni");

    Errors errors = new Errors();
    BigDecimal importo = errors.validateMandatoryBigDecimalInRange(sTmpBudget,
        "budget", 2, BigDecimal.ZERO, MAX_IMPORTO);
    BigDecimal maxImportoTrascinato = importo != null ? importo
        : BigDecimal.ZERO;
    BigDecimal importoTrascinato = errors.validateOptionalBigDecimalInRange(
        sTmpTrascinato, "trascinato", 2, BigDecimal.ZERO, maxImportoTrascinato);
    errors.validateMandatoryFieldLength(motivazioni, 5, 4000, "motivazioni");

    if (errors.isEmpty())
    {
      boolean valoriCambiatiImporto = true;
      boolean valoriCambiatiImportoTrascinato = true;

      if (importoFocusAreaDTOCorrente.getImporto() != null
          && importoFocusAreaDTOCorrente.getImporto().compareTo(importo) == 0)
        valoriCambiatiImporto = false;

      if (importoFocusAreaDTOCorrente.getImportoTrascinato() == null)
        importoFocusAreaDTOCorrente.setImportoTrascinato(new BigDecimal(0));

      if (importoTrascinato == null)
        importoTrascinato = new BigDecimal(0);

      if (importoFocusAreaDTOCorrente.getImportoTrascinato()
          .compareTo(importoTrascinato) == 0)
        valoriCambiatiImportoTrascinato = false;

      if (!valoriCambiatiImporto && !valoriCambiatiImportoTrascinato)
      {
        errors.addError("trascinato",
            "I valori inseriti sono uguali a quelli precedenti. Impossibile procedere.");
        errors.addError("budget",
            "I valori inseriti sono uguali a quelli precedenti. Impossibile procedere.");
      }
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      return "pianofinanziario/popupModificaLivello";
    }
    else
    {
      try
      {
        UtenteAbilitazioni u = getUtenteAbilitazioni(request.getSession());
        pianoFinanziarioNemboconf.updateRigaPianoFinanziario(idPianoFinanziario,
            idLivello, importo, importoTrascinato, motivazioni,
            u.getIdUtenteLogin(), importoFocusAreaDTOCorrente.getImporto(),
            importoFocusAreaDTOCorrente.getImportoTrascinato());
      }
      catch (ApplicationException e)
      {
        errors.addError("error", e.getMessage());
        model.addAttribute("errors", errors);
        return "pianofinanziario/popupModificaLivello";
      }

      model.addAttribute("idFocusAreaModificata", idLivello);
      return "pianofinanziario/reload";
    }
  }

  private boolean isModificabile(long idPianoFinanziario, HttpSession session)
      throws InternalUnexpectedException
  {
    boolean modificaUtente = NemboUtils.PAPUASERV
        .isUtenteReadWrite(getUtenteAbilitazioni(session));
    if (modificaUtente)
    {
      modificaUtente = isUtenteAbilitatoModificaMacroCdU(session,
          Security.GESTISCI_PIANO_FINANZIARIO_REGIONALE, 60000l);
    }
    if (modificaUtente)
    {
      PianoFinanziarioDTO pianoCorrente = pianoFinanziarioNemboconf
          .getPianoFinanziarioCorrente(
              NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
      modificaUtente = pianoCorrente != null
          && pianoCorrente.getIdPianoFinanziario() == idPianoFinanziario
          && pianoCorrente.isModificabile();
    }
    return modificaUtente;
  }

  @RequestMapping(value = "elimina_{idPianoFinanziario}_{idLivello}_{idFocusArea}", method = RequestMethod.GET)
  public String elimina(Model model,
      @ModelAttribute @PathVariable("idPianoFinanziario") @NumberFormat(style = NumberFormat.Style.NUMBER) long idPianoFinanziario,
      @PathVariable("idLivello") @NumberFormat(style = NumberFormat.Style.NUMBER) long idLivello,
      @PathVariable("idFocusArea") @NumberFormat(style = NumberFormat.Style.NUMBER) long idFocusArea,
      HttpServletResponse response)
      throws InternalUnexpectedException, ApplicationException
  {
    try
    {
      model.addAttribute("pageName", "elimina_" + idPianoFinanziario + "_"
          + idLivello + "_" + idFocusArea + ".do");
      PianoFinanziarioDTO pianoCorrente = pianoFinanziarioNemboconf
          .getPianoFinanziarioCorrente(
              NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
      if (pianoCorrente == null)
      {
        writePlainText(response, "Nessun piano finanziario trovato!");
        return null;
      }
      else
      {
        if (pianoCorrente.getIdPianoFinanziario() != idPianoFinanziario)
        {
          writePlainText(response,
              "Il piano finanziario non è quello corrente, impossibile modificarne i dati");
          return null;
        }
        else
        {
          if (pianoCorrente.getCurrentIter()
              .getIdStatoPianoFinanziario() != NemboConstants.PIANO_FINANZIARIO.STATO.ID.BOZZA)
          {
            writePlainText(response,
                "Il piano finanziario non è in BOZZA, impossibile modificarne i dati");
            return null;
          }
        }
      }
    }
    catch (IOException e)
    {
      throw new InternalUnexpectedException(e, new LogParameter[]
      {
          new LogParameter("idPianoFinanziario", idPianoFinanziario),
          new LogParameter("idLivello", idLivello),
          new LogParameter("idFocusArea", idFocusArea) });
    }
    setModelDialogWarning(model,
        "Attenzione, si desidera eliminare la risorsa indicata?");
    return "dialog/pianofinanziario/elimina";
  }

  @RequestMapping(value = "elimina_{idPianoFinanziario}_{idLivello}_{idFocusArea}", method = RequestMethod.POST)
  public String eliminaPost(Model model,
      @ModelAttribute @PathVariable("idPianoFinanziario") @NumberFormat(style = NumberFormat.Style.NUMBER) long idPianoFinanziario,
      @PathVariable("idLivello") @NumberFormat(style = NumberFormat.Style.NUMBER) long idLivello,
      @PathVariable("idFocusArea") @NumberFormat(style = NumberFormat.Style.NUMBER) long idFocusArea)
      throws InternalUnexpectedException, ApplicationException
  {
    try
    {
      TipoOperazioneDTO operazione = pianoFinanziarioNemboconf
          .deleteRisorsa(idPianoFinanziario, idLivello, idFocusArea);
      model.addAttribute("operazione", operazione);
      // loadPrioritaFocusArea(model, idPianoFinanziario);
      return "dialog/pianofinanziario/aggiornamentoOK";
    }
    catch (ApplicationException e)
    {
      e.printStackTrace();
      model.addAttribute("errore", e.getMessage());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    model.addAttribute("pageName", "elimina_" + idPianoFinanziario + "_"
        + idLivello + "_" + idFocusArea + ".do");

    return "dialog/pianofinanziario/elimina";
  }

  @RequestMapping(value = "load_{idPianoFinanziario}", produces = "application/json")
  @ResponseBody
  public Map<String, String> json(
      @PathVariable("idPianoFinanziario") @NumberFormat(style = NumberFormat.Style.NUMBER) long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    HashMap<String, String> values = new HashMap<String, String>();
    List<MisuraDTO> elencoLivelli = pianoFinanziarioNemboconf
        .getElencoLivelli(idPianoFinanziario);
    for (MisuraDTO misura : elencoLivelli)
    {
      for (SottoMisuraDTO sottomisura : misura.getElenco())
      {
        for (TipoOperazioneDTO operazione : sottomisura.getElenco())
        {
          values.put("importo_" + operazione.getIdLivello(),
              operazione.getTotaleEuro());
          values.put("trascinato_" + operazione.getIdLivello(),
              operazione.getTotaleTrascinatoEuro());
          values.put("risorseAttivate_" + operazione.getIdLivello(),
              operazione.getTotaleRisorseAttivateEuro());
          values.put("risorseDisponibili_" + operazione.getIdLivello(),
              operazione.getRisorseDisponibiliEuro());
          values.put("economia_" + operazione.getIdLivello(),
              operazione.getTotaleEconomieEuro());
        }
        values.put("importo_" + sottomisura.getIdLivello(),
            sottomisura.getTotaleEuro());
        values.put("trascinato_" + sottomisura.getIdLivello(),
            sottomisura.getTotaleTrascinatoEuro());
        values.put("risorseAttivate_" + sottomisura.getIdLivello(),
            sottomisura.getTotaleRisorseAttivateEuro());
        values.put("risorseDisponibili_" + sottomisura.getIdLivello(),
            sottomisura.getRisorseDisponibiliEuro());
        values.put("economia_" + sottomisura.getIdLivello(),
            sottomisura.getTotaleEconomieEuro());
      }
      values.put("trascinato_" + misura.getIdLivello(),
          misura.getTotaleTrascinatoEuro());
      values.put("importo_" + misura.getIdLivello(), misura.getTotaleEuro());
      values.put("risorseAttivate_" + misura.getIdLivello(),
          misura.getTotaleRisorseAttivateEuro());
      values.put("risorseDisponibili_" + misura.getIdLivello(),
          misura.getRisorseDisponibiliEuro());
      values.put("economia_" + misura.getIdLivello(),
          misura.getTotaleEconomieEuro());
    }
    // Calcolo i totali, anche se il metodo calcolaTotali riscorre l'elenco dei
    // livelli non è uno spreco di tempo/cpu in quanto scorre solo le misure i
    // cui
    // totali sono già stati calcolati nel ciclo precedente
    Totali totali = Totali.calcolaTotali(elencoLivelli);
    values.put("importo_generale",
        NemboUtils.FORMAT.formatCurrency(totali.importo));
    values.put("trascinato_generale",
        NemboUtils.FORMAT.formatCurrency(totali.trascinato));
    values.put("risorseAttivate_generale",
        NemboUtils.FORMAT.formatCurrency(totali.risorseAttivate));
    values.put("economia_generale",
        NemboUtils.FORMAT.formatCurrency(totali.economie));
    values.put("risorseDisponibili_generale",
        NemboUtils.FORMAT.formatCurrency(totali.risorseDisponibili));
    values.put("isValidJSON", "true"); // Serve al javascript per capire che è
                                       // non ci sono stati errori nella
                                       // creazione del file json
    return values;
  }

}