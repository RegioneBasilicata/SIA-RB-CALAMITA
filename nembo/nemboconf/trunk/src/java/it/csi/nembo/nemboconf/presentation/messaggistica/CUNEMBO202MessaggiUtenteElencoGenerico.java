package it.csi.nembo.nemboconf.presentation.messaggistica;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.dto.messaggistica.InfoMessaggio;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.DettagliMessaggio;
import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.InternalException_Exception;
import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.ListaMessaggi;
import it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica.LogoutException_Exception;
import it.csi.nembo.nemboconf.presentation.interceptor.logout.MessaggisticaManager;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping(value = "/cunembo202")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY)
public class CUNEMBO202MessaggiUtenteElencoGenerico
    extends MessaggisticaBaseController
{
  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session,
      HttpServletResponse response)
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) getUtenteAbilitazioni(
        session);
    try
    {
      ListaMessaggi listaMessaggi = NemboUtils.WS.getMessaggistica()
          .getListaMessaggi(NemboConstants.NEMBOCONF.ID,
              utenteAbilitazioni.getRuolo().getCodice(),
              utenteAbilitazioni.getCodiceFiscale(),
              MessaggisticaManager.FLAG_GENERICO, null, null, Boolean.TRUE);
      List<InfoMessaggio> list = MessaggisticaManager
          .convertiMessaggi(listaMessaggi.getMessaggi());
      dividiMessaggi(model, list);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (InternalException_Exception e)
    {
      e.printStackTrace();
    }
    catch (LogoutException_Exception e)
    {
      // E' in corso un logout ==> butto fuori l'utente dall'applicativo
      try
      {
        String messaggioLogout = MessaggisticaManager.performLogout(session, e);
        MessaggisticaManager.redirectToLoggedOutPage(response, messaggioLogout);
      }
      catch (Exception e1)
      {
      }
    }
    return "messaggistica/elenco/lista";
  }

  protected void dividiMessaggi(Model model, List<InfoMessaggio> list)
  {
    List<InfoMessaggio> messaggiLetti = new ArrayList<InfoMessaggio>();
    List<InfoMessaggio> messaggiDaLeggere = new ArrayList<InfoMessaggio>();
    for (InfoMessaggio info : list)
    {
      if (info.isLetto())
      {
        messaggiLetti.add(info);
      }
      else
      {
        messaggiDaLeggere.add(info);
      }
    }
    if (!messaggiLetti.isEmpty())
    {
      model.addAttribute("messaggiLetti", messaggiLetti);
    }
    if (!messaggiDaLeggere.isEmpty())
    {
      model.addAttribute("messaggiDaLeggere", messaggiDaLeggere);
    }
  }

  @RequestMapping(value = "/dettaglio_{idElencoMessaggi}", method = RequestMethod.GET)
  @IsPopup
  public String dettaglio(Model model, HttpServletRequest request,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi)
  {
    DettagliMessaggio dm = null;
    try
    {
      dm = NemboUtils.WS.getMessaggistica().getDettagliMessaggio(
          idElencoMessaggi,
          getUtenteAbilitazioni(request.getSession()).getCodiceFiscale());
      model.addAttribute("dataInserimento",
          NemboUtils.DATE.formatDateTime(NemboUtils.DATE
              .fromXMLGregorianCalendar(dm.getDataInizioValidita())));
      if (dm != null)
      {
        String messaggio = dm.getTestoMessaggio();
        if (messaggio != null)
        {
          dm.setTestoMessaggio(messaggio.replace("\n", "<br />"));
        }
      }
      model.addAttribute("messaggio", dm);
      model.addAttribute("idElencoMessaggi", idElencoMessaggi);
    }
    catch (Exception e)
    {

    }
    return "messaggistica/elenco/dettaglio";
  }

  @RequestMapping(value = "/confermo_lettura_{idElencoMessaggi}", method = RequestMethod.POST)
  @IsPopup
  public String dettaglioPost(Model model, HttpServletRequest request,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi)
      throws MalformedURLException, InternalException_Exception
  {
    NemboUtils.WS.getMessaggistica().confermaLetturaMessaggio(
        idElencoMessaggi,
        getUtenteAbilitazioni(request.getSession()).getCodiceFiscale());
    forceRefreshMessaggistica(request.getSession());
    return "redirect:../cunembo202/index.do";
  }

  @RequestMapping(value = "/download_allegato_{idElencoMessaggi}_{idAllegato}", method = RequestMethod.GET)
  @IsPopup
  public ResponseEntity<byte[]> downloadAllegato(HttpSession session,
      @PathVariable("idElencoMessaggi") long idElencoMessaggi,
      @PathVariable("idAllegato") long idAllegato)
      throws InternalUnexpectedException
  {
    return super.downloadAllegato(session, idElencoMessaggi, idAllegato);
  }
}
