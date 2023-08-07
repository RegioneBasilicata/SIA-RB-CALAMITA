package it.csi.nembo.nemboconf.presentation.catalogomisure;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.PrincipioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;

@Controller
@RequestMapping("catalogomisure/criteriDiSelezione/")
public class CriteriDiSelezioneController
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
      ModelMap model, HttpSession session)
      throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    DecodificaDTO<String> tipoLivello = catMisureEJB
        .getInfoTipoLivello(idLivello);

    String codice = tipoLivello.getCodice();
    if ("I".equals(codice) || NemboConstants.TIPO_BANDO.GAL.equals(codice))
    {
      session.setAttribute("addInterventiToQuadri", Boolean.TRUE);
    }

    model.addAttribute("elenco", catMisureEJB.getCriteriDiSelezione(idLivello));
    model.addAttribute("idLivello", String.valueOf(idLivello));
    return "catalogomisure/criteridiselezione/elencoCriteriDiSelezione";
  }

  private List<DecodificaDTO<String>> getTipiElaborazione()
  {
    List<DecodificaDTO<String>> tipiElaborazione = new ArrayList<>();
    tipiElaborazione.add(new DecodificaDTO<String>("A", "Automatico"));
    tipiElaborazione.add(new DecodificaDTO<String>("M", "Manuale"));
    return tipiElaborazione;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "modificaCriteri_{idLivello}_{idPrincipioSelezione}", method = RequestMethod.GET)
  public String modificaPrincipio(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idPrincipioSelezione") long idPrincipioSelezione,
      ModelMap model,
      HttpSession session) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);

    model.addAttribute("tipiElaborazione", getTipiElaborazione());
    model.addAttribute("idLivello", String.valueOf(idLivello));
    model.addAttribute("idPrincipioSelezione",
        String.valueOf(idPrincipioSelezione));

    if (idPrincipioSelezione != -1)
    {
      PrincipioDiSelezioneDTO principioSelezionato = catMisureEJB
          .getPrincipioDiSelezioneById(idLivello,
              idPrincipioSelezione);
      model.addAttribute("principioSelezionato", principioSelezionato);
      model.addAttribute("modifica", true);
    }

    return "catalogomisure/criteridiselezione/gestisciPrincipio";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "confermaEliminaPrincipio_{idLivello}_{idPrincipioSelezione}", method = RequestMethod.GET)
  public String confermaEliminaPrincipio(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idPrincipioSelezione") long idPrincipioSelezione,
      ModelMap model,
      HttpSession session) throws InternalUnexpectedException
  {

    model.addAttribute("idLivello", idLivello);
    model.addAttribute("idPrincipioSelezione", idPrincipioSelezione);
    model.addAttribute("message",
        "Eliminare il principio selezionato ed i relativi criteri?");
    return "catalogomisure/criteridiselezione/confermaEliminaPrincipio";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "eliminaPrincipio_{idLivello}_{idPrincipioSelezione}", method = RequestMethod.POST)
  public String eliminaPrincipio(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idPrincipioSelezione") long idPrincipioSelezione,
      ModelMap model,
      HttpSession session) throws InternalUnexpectedException
  {
    String res = catMisureEJB.eliminaPrincipioDiSelezione(idPrincipioSelezione);
    if (res.compareTo("ERROR") == 0)
    {
      model.addAttribute("msgErrore",
          "Il criterio è già stato associato ad uno o più bandi. Impossibile eliminare.");
      prepareModel(idLivello, model, idPrincipioSelezione);

      return "catalogomisure/criteridiselezione/gestisciPrincipio";
    }
    return "redirect:dettaglio_" + idLivello + ".do";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.VISUALIZZA_CATALOGO_MISURE)
  @RequestMapping(value = "modificaCriteri_{idLivello}_{idPrincipioSelezione}", method = RequestMethod.POST)
  public String modificaPrincipioPost(
      @PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "idPrincipioSelezione") long idPrincipioSelezione,
      ModelMap model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);
    model.addAttribute("tipiElaborazione", getTipiElaborazione());
    if (idPrincipioSelezione != -1)
      model.addAttribute("modifica", true);
    model.addAttribute("idLivello", String.valueOf(idLivello));
    model.addAttribute("idPrincipioSelezione",
        String.valueOf(idPrincipioSelezione));

    Errors error = new Errors();
    PrincipioDiSelezioneDTO principio = validateForm(idPrincipioSelezione,
        idLivello, error, model, request);

    if (error.isEmpty())
    {
      String res = catMisureEJB.updatePrincipioDiSelezione(principio,
          idLivello);
      if (res.compareTo("ERROR") == 0)
      {
        model.addAttribute("msgErrore",
            "Il criterio è già stato associato ad uno o più bandi. Impossibile modificare.");
        prepareModel(idLivello, model, idPrincipioSelezione);
        return "catalogomisure/criteridiselezione/gestisciPrincipio";
      }

      return "redirect:dettaglio_" + idLivello + ".do";
    }
    else
    {
      model.addAttribute("errors", error);
      model.addAttribute("preferRequest", true);
    }

    model.addAttribute("principioSelezionato", principio);

    return "catalogomisure/criteridiselezione/gestisciPrincipio";
  }

  private void prepareModel(long idLivello, ModelMap model,
      long idPrincipioSelezione) throws InternalUnexpectedException
  {
    setTestataDetails(idLivello, model);

    model.addAttribute("tipiElaborazione", getTipiElaborazione());
    model.addAttribute("idLivello", String.valueOf(idLivello));
    model.addAttribute("idPrincipioSelezione",
        String.valueOf(idPrincipioSelezione));
    PrincipioDiSelezioneDTO principioSelezionato = catMisureEJB
        .getPrincipioDiSelezioneById(idLivello,
            idPrincipioSelezione);
    model.addAttribute("principioSelezionato", principioSelezionato);
    model.addAttribute("modifica", true);
  }

  private PrincipioDiSelezioneDTO validateForm(long idPrincipioSelezione,
      long idLivello, Errors error, ModelMap model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<CriterioDiSelezioneDTO> criteri = new ArrayList<CriterioDiSelezioneDTO>();
    PrincipioDiSelezioneDTO principio = new PrincipioDiSelezioneDTO();
    principio.setIdPrincipioSelezione(idPrincipioSelezione);

    String descrizionePrincipio = request.getParameter("principioDiSelezione");
    String codice;
    String descrizioneCriterio;
    String specifiche;
    String punteggioMin;
    String punteggioMax;
    String flagElaborazione;
    String[] righe = request.getParameterValues("rowindex");
    error.validateMandatoryFieldLength(descrizionePrincipio, 0, 4000,
        "principioDiSelezione");
    principio.setDescrizione(descrizionePrincipio);
    for (String riga : righe)
    {
      if ("$$index".equals(riga))
      {
        continue;
      }

      CriterioDiSelezioneDTO criterio = new CriterioDiSelezioneDTO();

      codice = request.getParameter("codice_" + riga);
      descrizioneCriterio = request.getParameter("criterio_" + riga);
      specifiche = request.getParameter("specifiche_" + riga);
      punteggioMin = request.getParameter("punteggioMin_" + riga);
      punteggioMax = request.getParameter("punteggioMax_" + riga);
      flagElaborazione = request.getParameter("flagElaborazione_" + riga);

      error.validateMandatoryFieldLength(descrizioneCriterio, 0, 4000,
          "criterio_" + riga);
      error.validateMandatoryFieldLength(specifiche, 0, 4000,
          "specifiche_" + riga);
      Long puntMax = error.validateMandatoryLong(punteggioMax,
          "punteggioMax_" + riga);
      Long puntMin = error.validateMandatoryLong(punteggioMin,
          "punteggioMin_" + riga);
      error.validateMandatory(flagElaborazione, "flagElaborazione_" + riga);
      if (puntMax != null && puntMin != null)
        if (puntMax.longValue() < puntMin.longValue())
          error.addError("punteggioMax_" + riga,
              "Il punteggio massimo non può essere minore del punteggio minimo.");

      criterio.setCriterioDiSelezione(descrizioneCriterio);
      criterio.setSpecifiche(specifiche);
      criterio.setPunteggioMax(puntMax);
      criterio.setPunteggioMin(puntMin);
      criterio.setFlagElaborazione(flagElaborazione);
      criterio.setCodice(codice);

      criteri.add(criterio);
    }

    if (criteri.isEmpty())
      error.addError("principioDiSelezione", "Inserire almeno un criterio.");
    principio.setCriteri(criteri);
    return principio;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CATALOGO_MISURE)
  @RequestMapping(value = "getNextCodice_{idLivello}_{cnt}", method = RequestMethod.GET)
  @ResponseBody
  public String getNextCodice(@PathVariable(value = "idLivello") long idLivello,
      @PathVariable(value = "cnt") long cnt, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String cod = catMisureEJB.getMaxCodiceCriterio(idLivello);
    String before = cod.substring(0, 9); // prima parte della stringa
    String after = "";

    if (cod.length() == 12) // vuol dire che ho due cifre (es. non 9 ma 09)
    {
      after = cod.substring(11, cod.length()); // ultima parte del codice
      cod = cod.substring(9, 11); // numero progressivo
    }
    else
    {
      after = cod.substring(10, cod.length()); // ultima parte del codice
      cod = cod.substring(9, 10); // numero progressivo
    }

    Long number = Long.parseLong(cod) + cnt; // aumento il numero
    String newNumber = "";
    if (number.longValue() < 10) // se è minore di 10 ci metto lo 0 davanti
      newNumber = "0" + String.valueOf(number);
    else
      newNumber = String.valueOf(number);

    String ret = before + newNumber + after; // ricostruisco il codice finale

    return ret;
  }
}
