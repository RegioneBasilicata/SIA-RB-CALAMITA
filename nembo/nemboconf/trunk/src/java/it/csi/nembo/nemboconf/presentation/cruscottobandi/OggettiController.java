package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class OggettiController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "oggetti", method = RequestMethod.GET)
  public String oggettiGet(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> elenco = cruscottoEJB
        .getElencoGruppiOggetti(bando.getIdBandoMaster());
    session.removeAttribute("descrGruppoOggSelezionato");
    model.addAttribute("elencoGruppiOggetto", elenco);
    if (!model.containsKey("fromRequest"))
      model.addAttribute("fromRequest", false);
    model.addAttribute("idBando", idBando);

    if (bando.getCodiceTipoBando() != null
        && bando.getCodiceTipoBando().compareTo("G") == 0)
      model.addAttribute("indietro", "filiere.do");
    else
    {
      String mostraQuadroInterventi = (String) session
          .getAttribute("mostraQuadroInterventi");
      if (mostraQuadroInterventi != null
          && mostraQuadroInterventi.compareTo("true") == 0)
        model.addAttribute("indietro", "interventi.do");
      else
        model.addAttribute("indietro", "filtrobeneficiari.do");
    }

    return "cruscottobandi/oggetti";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "visualizzaoggetti", method = RequestMethod.POST)
  public String visualizzaquadri(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "selGruppoOggetto") long idGruppoOggetto)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> oggettiMaster = cruscottoEJB
        .getElencoGruppiOggettiBandoMaster(idBando, idGruppoOggetto);
    model.addAttribute("elenco", oggettiMaster);
    model.addAttribute("idBando", idBando);

    if (bando.getCodiceTipoBando() != null
        && bando.getCodiceTipoBando().compareTo("G") == 0)
      model.addAttribute("indietro", "filiere.do");
    else
    {
      String mostraQuadroInterventi = (String) session
          .getAttribute("mostraQuadroInterventi");
      if (mostraQuadroInterventi != null
          && mostraQuadroInterventi.compareTo("true") == 0)
        model.addAttribute("indietro", "interventi.do");
      else
        model.addAttribute("indietro", "filtrobeneficiari.do");
    }

    List<GruppoOggettoDTO> gruppiDisponibili = cruscottoEJB
        .getElencoGruppiOggetti(bando.getIdBandoMaster());

    for (GruppoOggettoDTO item : gruppiDisponibili)
    {
      if (item.getIdGruppoOggetto() == idGruppoOggetto)
      {
        model.addAttribute("descrGruppoOggSelezionato", item.getDescrGruppo());
        session.setAttribute("descrGruppoOggSelezionato",
            item.getDescrGruppo());
        break;
      }
    }

    model.addAttribute("elencoGruppiOggetto", gruppiDisponibili);
    model.addAttribute("selGruppoOggetto", idGruppoOggetto);
    model.addAttribute("idGruppoOggettoSelezionato", idGruppoOggetto);

    return "cruscottobandi/oggetti";
  }

  @RequestMapping(value = "confermaoggetti", method = RequestMethod.POST)
  public String oggettiPost(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "idGruppoOggettoSelezionato") long idGruppoOggetto)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    model.addAttribute("fromRequest", true);
    model.addAttribute("idGruppoOggettoSelezionato", idGruppoOggetto);
    long idBando = bando.getIdBando();
    String dataApertura;
    String dataChiusura = "";
    String dataProroga = "";
    String dataProrogaPrecedente = "";
    String oggettoSelezionato;

    List<GruppoOggettoDTO> oggettiMaster = cruscottoEJB
        .getElencoGruppiOggettiBandoMaster(idBando, idGruppoOggetto);
    List<OggettiIstanzeDTO> elencoDefinitivo = new ArrayList<OggettiIstanzeDTO>();

    boolean tuttoAttivo = true;

    for (GruppoOggettoDTO gruppo : oggettiMaster)
    {
      for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
      {
        oggetto.setDataInizioPrec(oggetto.getDataInizio());
        oggetto.setDataFinePrec(oggetto.getDataFine());
        oggetto.setDataRitardoPrec(oggetto.getDataRitardo());

        oggettoSelezionato = request
            .getParameter("chkOggetto_" + oggetto.getIdLegameGruppoOggetto());
        dataApertura = request
            .getParameter("dataApertura_" + oggetto.getIdLegameGruppoOggetto());
        dataChiusura = request
            .getParameter("dataChiusura_" + oggetto.getIdLegameGruppoOggetto());
        dataProrogaPrecedente = request.getParameter(
            "dataProrogaPrecedente_" + oggetto.getIdLegameGruppoOggetto());
        dataProroga = request
            .getParameter("dataProroga_" + oggetto.getIdLegameGruppoOggetto());

        if ((!GenericValidator.isBlankOrNull(dataApertura)
            || !GenericValidator.isBlankOrNull(dataChiusura)
            || !GenericValidator.isBlankOrNull(dataProroga))
            && GenericValidator.isBlankOrNull(oggettoSelezionato))
        {
          errors.addError("dataApertura_" + oggetto.getIdLegameGruppoOggetto(),
              "Hai valorizzato alcune date senza selezionare l'oggetto!");
        }

        // Se l'oggetto non è attivo ed è stato selezionato oppure era già stato
        // inserito su DB ed è stato deselezionato allora procedo con i
        // controlli
        if ("N".equals(oggetto.getFlagAttivo())
            && (!GenericValidator.isBlankOrNull(oggettoSelezionato)
                || oggetto.getIdLegameGruppoOggetto() > 0))
        {
          tuttoAttivo = false;
        }

        if ("N".equals(oggetto.getFlagAttivo())
            && !GenericValidator.isBlankOrNull(oggettoSelezionato))
        {
          Date dtApertura = strToDateTime(dataApertura);
          Date dtChiusura = strToDateTime(dataChiusura);
          Date dtProroga = strToDateTime(dataProroga);

          errors.validateMandatoryDateTime(dataApertura,
              "dataApertura_" + oggetto.getIdLegameGruppoOggetto(), true);

          if (dtApertura == null)
            errors.addError(
                "dataApertura_" + oggetto.getIdLegameGruppoOggetto(),
                "Inserire la data nel formato corretto dd/MM/yyyy HH:mm:ss");

          if ("S".equals(oggetto.getFlagIstanza()))
          {
            errors.validateMandatoryDateTime(dataChiusura,
                "dataChiusura_" + oggetto.getIdLegameGruppoOggetto(), true);

            if (dtChiusura == null)
              errors.addError(
                  "dataChiusura_" + oggetto.getIdLegameGruppoOggetto(),
                  "Inserire la data nel formato corretto dd/MM/yyyy HH:mm:ss");
            if (!GenericValidator.isBlankOrNull(dataProroga)
                && dtProroga == null)
              errors.addError(
                  "dataProroga_" + oggetto.getIdLegameGruppoOggetto(),
                  "Inserire la data nel formato corretto dd/MM/yyyy HH:mm:ss");
          }

          if (errors.isEmpty() && !GenericValidator.isBlankOrNull(dataApertura))
          {
            if (!GenericValidator.isBlankOrNull(dataChiusura))
            {
              if (dtApertura.compareTo(dtChiusura) > 0)
              {
                errors.addError(
                    "dataApertura_" + oggetto.getIdLegameGruppoOggetto(),
                    "La data apertura deve essere inferiore alla data chiusura!");
              }
            }
            if (!GenericValidator.isBlankOrNull(dataProroga))
            {
              if (dtApertura.compareTo(dtProroga) > 0)
              {
                errors.addError(
                    "dataApertura_" + oggetto.getIdLegameGruppoOggetto(),
                    "La data apertura deve essere inferiore alla data proroga!");
              }
              if (dtChiusura.compareTo(dtProroga) > 0)
              {
                errors.addError(
                    "dataProroga_" + oggetto.getIdLegameGruppoOggetto(),
                    "La data proroga deve essere successiva alla data chiusura!");
              }
              if (!GenericValidator.isBlankOrNull(dataProrogaPrecedente)
                  && cruscottoEJB
                      .canLogAttivitaBandoOggetto(oggetto.getIdBandoOggetto()))
              {
                Date dtProrogaPrec = strToDateTime(dataProrogaPrecedente);
                if (dtProrogaPrec.compareTo(dtProroga) > 0)
                {
                  errors.addError(
                      "dataProroga_" + oggetto.getIdLegameGruppoOggetto(),
                      "La data proroga deve essere successiva al "
                          + dataProrogaPrecedente + " !");
                }
              }
            }

            oggetto.setDataInizio(dtApertura);
            oggetto.setDataFine(dtChiusura);
            oggetto.setDataRitardo(dtProroga);
          }

          oggetto.setStatoPrecedente(
              (oggetto.getIdBandoOggetto() > 0) ? "S" : "N");
          oggetto.setStatoAttuale(
              GenericValidator.isBlankOrNull(oggettoSelezionato) ? "N" : "S");
          elencoDefinitivo.add(oggetto);
        }
        else
          if ("N".equals(oggetto.getFlagAttivo())
              && (oggetto.getIdBandoOggetto() > 0))
          {
            oggetto.setStatoPrecedente("S");
            oggetto.setStatoAttuale(
                GenericValidator.isBlankOrNull(oggettoSelezionato) ? "N" : "S");
            elencoDefinitivo.add(oggetto);
          }

      }
    }

    if (tuttoAttivo)
    {
      return "redirect:quadri.do";
    }
    else
      if (elencoDefinitivo.size() <= 0)
      {
        model.addAttribute("msgErrore",
            "E' necessario selezionare almeno un oggetto!");
      }
      else
        if (errors.isEmpty())
        {
          try
          {
            int statoAggiornamento = cruscottoEJB.aggiornaSelezioneOggettiBando(idBando,
                elencoDefinitivo);
		    if(statoAggiornamento != 0)
		    {
		      String msgErrore ="Operazione non consentita (errore generico).";
		      if(statoAggiornamento == -1)
		      {
		        msgErrore = "Non è stato possibile escludere l'oggetto in quanto l'oggetto è già stato attivato in precedenza (sono presenti record sulla tabella NEMBO_T_ATTIVITA_BANDO_OGGETTO) ";
		      }
		      model.addAttribute("msgErrore", msgErrore);
		      return visualizzaquadri(model, request, session,idGruppoOggetto);
		   }
          }
          catch (InternalUnexpectedException e) // perché c'è un problema di
                                                // violazione di integrità
                                                // quando si fanno le delete.
          {

            model.addAttribute("msgErrore", "Operazione non consentita");
            return visualizzaquadri(model, request, session, idGruppoOggetto);

          }

          UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
              .getAttribute("utenteAbilitazioni");
          Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
          String oggetto = "";
          List<GruppoOggettoDTO> gruppiDisponibili = cruscottoEJB
              .getElencoGruppiOggetti(bando.getIdBandoMaster());

          for (GruppoOggettoDTO item : gruppiDisponibili)
          {
            if (item.getIdGruppoOggetto() == idGruppoOggetto)
            {
              oggetto = item.getDescrGruppo();
              break;
            }
          }
          for (OggettiIstanzeDTO o : elencoDefinitivo)
          {
            String msg = "";
            if (o.getDataInizioPrec() != null && o.getDataInizio() != null)
            {
              if (o.getDataInizio().compareTo(o.getDataInizioPrec()) != 0)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat("Data inizio precedente: \""
                    + NemboUtils.DATE.formatDateTime(o.getDataInizioPrec())
                    + "\" Data inizio successiva: \""
                    + NemboUtils.DATE.formatDateTime(o.getDataInizio())
                    + "\"\n");
              }
            }
            else
            {
              if (o.getDataInizioPrec() == null)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat(
                    "Data inizio precedente: non impostata - data inizio successiva: \""
                        + NemboUtils.DATE.formatDateTime(o.getDataInizio())
                        + "\"\n");
              }

            }
            if (o.getDataFinePrec() != null && o.getDataFine() != null)
            {
              if (o.getDataFine().compareTo(o.getDataFinePrec()) != 0)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg
                    .concat("Data fine precedente: \""
                        + NemboUtils.DATE.formatDateTime(
                            o.getDataFinePrec())
                        + "\" Data fine successiva: \""
                        + NemboUtils.DATE.formatDateTime(o.getDataFine())
                        + "\"\n");
              }
            }
            else
            {
              if (o.getDataFinePrec() == null)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat(
                    "Data fine precedente: non impostata - data fine successiva: \""
                        + NemboUtils.DATE.formatDateTime(o.getDataFine())
                        + "\"\n");
              }
            }

            if (o.getDataRitardoPrec() != null && o.getDataRitardo() != null)
            {
              if (o.getDataRitardo().compareTo(o.getDataRitardoPrec()) != 0)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat("Data ritardo precedente: \""
                    + NemboUtils.DATE.formatDateTime(o.getDataRitardoPrec())
                    + "\" Data ritardo successiva: \""
                    + NemboUtils.DATE.formatDateTime(o.getDataRitardo())
                    + "\"\n");
              }
            }
            else
            {
              if (o.getDataRitardoPrec() == null && o.getDataRitardo() != null)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat(
                    "Data ritardo precedente: non impostata - data ritardo successiva: \""
                        + NemboUtils.DATE.formatDateTime(o.getDataRitardo())
                        + "\"\n");
              }
              if (o.getDataRitardo() == null && o.getDataRitardoPrec() != null)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat("Data ritardo precedente: \""
                    + NemboUtils.DATE.formatDateTime(o.getDataRitardoPrec())
                    + "\" Data ritardo successiva: non impostata\n");
              }
            }

            if (o.getStatoPrecedente() != null)
            {
              if (o.getStatoAttuale().compareTo(o.getStatoPrecedente()) != 0)
              {
                if (msg == "")
                  msg = o.getDescrOggetto() + "\n";
                msg = msg.concat("Stato precedente: \"" + o.getStatoPrecedente()
                    + "\" Stato successivo: \"" + o.getStatoAttuale() + "\"\n");

              }

            }

            if (msg != "")
              cruscottoEJB.logAttivitaBandoOggetto(o.getIdBandoOggetto(),
                  idUtente,
                  NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.OGGETTI_ISTANZE,
                  "Oggetto: \"" + oggetto + "\" \n" + msg);
          }
          return visualizzaquadri(model, request, session, idGruppoOggetto);
        }

    model.addAttribute("errors", errors);
    return visualizzaquadri(model, request, session, idGruppoOggetto);
  }

  private Date strToDateTime(String date)
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        "dd/MM/yyyy HH:mm:ss");
    try
    {
      if (GenericValidator.isBlankOrNull(date))
        return null;
      return simpleDateFormat.parse(date);
    }
    catch (ParseException ex)
    {
      return null;
    }
  }

}
