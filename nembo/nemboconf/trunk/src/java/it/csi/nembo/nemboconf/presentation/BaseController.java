package it.csi.nembo.nemboconf.presentation;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
public class BaseController
{
  protected static final Logger logger = Logger
      .getLogger(NemboConstants.LOGGIN.LOGGER_NAME + ".presentation");

  public BaseController()
  {
  }

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  protected UtenteAbilitazioni getUtenteAbilitazioni(HttpSession session)
  {

    return (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni");
  }

  protected long getIdUtenteLogin(HttpSession session)
  {
    return getUtenteAbilitazioni(session).getIdUtenteLogin();
  }

  public boolean isUtenteAbilitatoMacroCdU(HttpSession session, String macroCdU)
  {
    return NemboUtils.PAPUASERV
        .isMacroCUAbilitato(getUtenteAbilitazioni(session), macroCdU);
  }

  public boolean isUtenteAbilitatoModificaMacroCdU(HttpSession session,
      String macroCdU)
  {
    return isUtenteAbilitatoModificaMacroCdU(session, macroCdU, null);
  }

  public boolean isUtenteAbilitatoModificaMacroCdU(HttpSession session,
      String macroCdU, Long idLivello)
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    boolean isAbilitato = NemboUtils.PAPUASERV
        .isUtenteReadWrite(utenteAbilitazioni);
    if (isAbilitato)
    {
      isAbilitato = NemboUtils.PAPUASERV
          .isMacroCUAbilitato(utenteAbilitazioni, macroCdU);
    }
    if (isAbilitato && idLivello != null)
    {
      isAbilitato = NemboUtils.PAPUASERV
          .isLivelloAbilitato(utenteAbilitazioni, idLivello);
    }
    return isAbilitato;
  }

  protected List<UtenteLogin> loadRuoloDescr(List<Long> lIdUtente)
      throws InternalUnexpectedException
  {
    try
    {
      int size = lIdUtente.size();
      long[] aIdUtente = new long[size];
      for (int i = 0; i < size; ++i)
      {
        aIdUtente[i] = lIdUtente.get(i);
      }
      UtenteLogin[] utenti = PapuaservProfilazioneServiceFactory
          .getRestServiceClient().findUtentiLoginByIdList(aIdUtente);
      return Arrays.asList(utenti);
    }
    catch (Exception e)
    {
      throw new InternalUnexpectedException(e, new LogVariable[]
      { new LogVariable("lIdUtente", lIdUtente) });
    }
  }

  protected String getUtenteDescrizione(Long idUtente, List<UtenteLogin> utenti)
  {
    if (utenti != null)
    {
      for (UtenteLogin utente : utenti)
      {
        if (utente.getIdUtenteLogin().longValue() == idUtente.longValue())
          return utente.getCognome() + " " + utente.getNome() + " ("
              + utente.getEnte().getDenominazioneEnte()
              + ")";
      }
    }
    return "";
  }

  protected void setModelDialogWarning(Model model, String messaggio)
  {
    setModelDialogWarning(model, null, messaggio, null);
  }

  protected void setModelDialogWarning(Model model, String messaggio,
      String action)
  {
    setModelDialogWarning(model, null, messaggio, action);
  }

  protected void setModelDialogWarning(Model model, String messaggioAttesa,
      String messaggio, String action)
  {
    model.addAttribute("chiudi", "no, chiudi");
    model.addAttribute("prosegui", "si, prosegui");
    model.addAttribute("messaggio", messaggio);
    if (messaggioAttesa != null)
    {
      model.addAttribute("messaggioAttendere", messaggioAttesa);
    }
    else
    {
      model.addAttribute("messaggioAttendere",
          "Attendere prego, operazione in corso...");
    }
    if (action != null)
    {
      model.addAttribute("action", action);
    }
  }

  public void writePlainText(HttpServletResponse response, String text)
      throws IOException
  {
    response.setContentType("text/plain");
    response.getWriter().write(text);
  }

  public void logOperationGruppoInfoDTO(String type, long idBandoOggetto,
      String nomeQuadro,
      List<GruppoInfoDTO> listaPrecedente, List<GruppoInfoDTO> listaSuccessiva,
      HttpSession session)
      throws InternalUnexpectedException
  {
    String msgLog = "";
    String msgId = nomeQuadro;
    String msgDescr = "";
    boolean modified = false;
    boolean printAll = false;

    if (listaPrecedente.size() != listaSuccessiva.size())
    {
      // se ne sono stati aggiunti o tolti stampo tutto ciò che c'era
      // prima e dopo la modifica
      modified = true;
      printAll = true;

    }
    else
    {
      // se invece modifico quelli esistenti riporto solo le modifiche
      // effettuate
      Iterator<GruppoInfoDTO> itPrec = listaSuccessiva.iterator();
      Iterator<GruppoInfoDTO> itSucc = listaPrecedente.iterator();
      while (itPrec.hasNext() && itSucc.hasNext())
      {
        GruppoInfoDTO p = itPrec.next();
        GruppoInfoDTO s = itSucc.next();

        if (p.getDescrizione().compareTo(s.getDescrizione()) != 0)
        {
          modified = true;
          msgDescr += " \nModifica titolo " + type + " da: \""
              + p.getDescrizione() + "\" a: \"" + s.getDescrizione() + "\"";
        }

        if (p.getElencoDettagliInfo().size() != s.getElencoDettagliInfo()
            .size())
        {
          modified = true;
          printAll = true;
        }
        else
        {
          Iterator<DettaglioInfoDTO> itDetPrec = p.getElencoDettagliInfo()
              .iterator();
          Iterator<DettaglioInfoDTO> itDetSucc = s.getElencoDettagliInfo()
              .iterator();
          while (itDetPrec.hasNext() && itDetSucc.hasNext())
          {
            DettaglioInfoDTO dp = itDetPrec.next();
            DettaglioInfoDTO ds = itDetSucc.next();

            if (dp.getDescrizione().compareTo(ds.getDescrizione()) != 0)
            {
              modified = true;
              msgLog += " \nTitolo " + type + ": \"" + s.getDescrizione()
                  + "\"";
              msgLog += " - Modifica dettaglio " + type + " da: \""
                  + dp.getDescrizione() + "\" a: \""
                  + ds.getDescrizione() + "\"";
            }
            if (dp.getFlagObbligatorio()
                .compareTo(ds.getFlagObbligatorio()) != 0)
            {
              modified = true;
              msgLog += " \nTitolo " + type + ": \"" + s.getDescrizione()
                  + "\"";
              msgLog += "  - Modifica flag obbligatorietà del dettaglio: \""
                  + ds.getDescrizione() + "\" da: \"" + dp.getFlagObbligatorio()
                  + "\" a: \""
                  + ds.getFlagObbligatorio() + "\"";
            }

            if (dp.getIdVincoloDichiarazione() != ds
                .getIdVincoloDichiarazione())
            {
              modified = true;
              msgLog += " \nTitolo " + type + ": \"" + s.getDescrizione()
                  + "\"";
              msgLog += " - Modifica tipo vincolo del dettaglio: \""
                  + ds.getDescrizione() + "\" da: \""
                  + dp.getIdVincoloDichiarazione() + "\" a: \""
                  + ds.getIdVincoloDichiarazione() + "\"";

            }

            if (type.compareTo("allegato") == 0)
            {
              if (dp.getFlagGestioneFile()
                  .compareTo(ds.getFlagGestioneFile()) != 0)
              {
                modified = true;
                msgLog += " \nTitolo " + type + ": \"" + s.getDescrizione()
                    + "\"";
                msgLog += " - Modifica flag gestione file da: \""
                    + dp.getDescrizione() + "\" a: \""
                    + ds.getDescrizione() + "\"";
              }

              if (ds.getExtIdTipoDocumento() != null
                  && dp.getExtIdTipoDocumento() != null)
              {
                if (dp.getExtIdTipoDocumento().longValue() != ds
                    .getExtIdTipoDocumento().longValue())
                {
                  modified = true;
                  msgLog += " \nTitolo " + type + ": \"" + s.getDescrizione()
                      + "\"";
                  msgLog += "  - Modifica tipo file da: \""
                      + dp.getExtIdTipoDocumento() + "\" a: \""
                      + ds.getExtIdTipoDocumento() + "\"";
                }
              }
            }
          }
        }
      }
    }

    if (printAll)
    {
      msgLog += " \nDati prima della modifica: ";
      for (GruppoInfoDTO gi : listaSuccessiva)
      {
        msgLog += " \nTitolo " + type + ": " + gi.getDescrizione() + " ";
        for (DettaglioInfoDTO d : gi.getElencoDettagliInfo())
        {
          msgLog += " \nDettaglio: \"" + d.getDescrizione()
              + "\" \nFlag obbligatorio: \"" + d.getFlagObbligatorio()
              + "\" \nVincolo: \"" + d.getTipoVincolo() + "\"";
          if (type.compareTo("allegato") == 0)
            msgLog += " \nFile da allegare: \"" + d.getFlagGestioneFile()
                + "\" Tipo file: \""
                + d.getExtIdTipoDocumento() + "\"";
        }
        msgLog += "\n";
      }
      msgLog += " \nDati dopo della modifica: ";
      for (GruppoInfoDTO gi : listaPrecedente)
      {
        msgLog += " \nTitolo " + type + ": \"" + gi.getDescrizione() + "\" ";
        for (DettaglioInfoDTO d : gi.getElencoDettagliInfo())
        {
          msgLog += " \nDettaglio: \"" + d.getDescrizione()
              + "\" \nFlag obbligatorio: \"" + d.getFlagObbligatorio()
              + "\" \nVincolo: \"" + d.getTipoVincolo() + "\"";
          if (type.compareTo("allegato") == 0)
            msgLog += " \nFile da allegare: \"" + d.getFlagGestioneFile()
                + "\" \nTipo file: \""
                + d.getExtIdTipoDocumento() + "\"";
        }
        msgLog += "\n";
      }
    }
    else
    {
      msgDescr += msgLog;
      msgLog = msgDescr;
    }

    if (modified)
    {
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
          .getAttribute("utenteAbilitazioni");
      Long idUtente = utenteAbilitazioni.getIdUtenteLogin();
      String quadro = "";
      if (type.compareTo("allegato") == 0)
        quadro = NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ALLEGATI;
      else
        if (type.compareTo("dichiarazione") == 0)
          quadro = NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.DICHIARAZIONI;
        else
          if (type.compareTo("impegno") == 0)
            quadro = NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.IMPEGNI;

      String oggetto = cruscottoEJB.getDescrizioneOggetto(idBandoOggetto);
      cruscottoEJB.logAttivitaBandoOggetto(idBandoOggetto, idUtente, quadro,
          msgId + " - " + oggetto + " " + msgLog);
    }

  }

  public Map<String, Object> getCommonFromSession(String id,
      HttpSession session, boolean removeIfDifferentOwner)
  {
    @SuppressWarnings("unchecked")
    Map<String, Object> common = (Map<String, Object>) session
        .getAttribute("common");
    if (common != null)
    {
      String ownerID = (String) common.get("ID");
      if (ownerID == null)
      {
        common = new HashMap<String, Object>();
        common.put("ID", id);
      }
      else
      {
        if (!ownerID.equals(id))
        {
          common = new HashMap<String, Object>();
          common.put("ID", id);
          if (removeIfDifferentOwner)
          {
            session.removeAttribute("common");
          }
        }
      }
    }
    else
    {
      common = new HashMap<String, Object>();
      common.put("ID", id);
    }
    return common;
  }

  public void saveCommonInSession(Map<String, Object> common,
      HttpSession session)
  {
    session.setAttribute("common", common);
  }

  public void clearCommonInSession(HttpSession session)
  {
    session.removeAttribute("common");
  }
}
