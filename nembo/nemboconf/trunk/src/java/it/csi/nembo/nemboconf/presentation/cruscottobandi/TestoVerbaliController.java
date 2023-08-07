package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoTestoVerbaleDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.TestoVerbaleDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.IsPopup;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class TestoVerbaliController extends BaseController
{

  private static final String FLAG_NON_ISTANZA = "N";
  @Autowired
  private ICruscottoBandiEJB  cruscottoEJB     = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "testo_verbali")
  public String datiIdentificativiGet(Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> oggettiBando = cruscottoEJB
        .getElencoGruppiOggetti(idBando, FLAG_NON_ISTANZA);
    model.addAttribute("elencoIstruttorie", oggettiBando);
    String sIdBandoOggetto = request.getParameter("idBandoOggetto");
    GruppoOggettoDTO gruppoOggettoSelezionato = null;
    OggettiIstanzeDTO oggettoSelezionato = null;
    String msgLog = "";

    if (sIdBandoOggetto != null)
    {
      Long lIdBandoOggetto = NemboUtils.NUMBERS.checkLong(sIdBandoOggetto);
      if (lIdBandoOggetto != null)
      {
        long idBandoOggetto = lIdBandoOggetto.longValue();
        if (oggettiBando != null)
        {
          for (GruppoOggettoDTO g : oggettiBando)
          {
            List<OggettiIstanzeDTO> oggetti = g.getOggetti();
            if (oggetti != null)
            {
              for (OggettiIstanzeDTO o : oggetti)
                if (o.getIdBandoOggetto() == idBandoOggetto)
                {
                  oggettoSelezionato = o;
                  gruppoOggettoSelezionato = g;
                  model.addAttribute("gruppoOggetto", gruppoOggettoSelezionato);
                  model.addAttribute("oggetto", oggettoSelezionato);
                  model.addAttribute("idBandoOggetto",
                      oggettoSelezionato.getIdBandoOggetto());
                  model.addAttribute("descrOggettoSelezionato",
                      gruppoOggettoSelezionato.getDescrGruppo() + " - "
                          + o.getDescrOggetto());
                  boolean bandoOggettoModificabile = NemboConstants.FLAGS.NO
                      .equals(o.getFlagAttivo());
                  boolean isReadWrite = getUtenteAbilitazioni(session)
                      .isReadWrite();
                  model.addAttribute("canUpdate",
                      bandoOggettoModificabile && isReadWrite);
                  model.addAttribute("modificabile",
                      Boolean.valueOf(bandoOggettoModificabile));

                  List<DecodificaDTO<Long>> cuDocumenti = cruscottoEJB
                      .getElencoDocumentiBandoOggetto(idBando,
                          bando.getIdBandoMaster(),
                          oggettoSelezionato.getIdBandoOggetto(), true);
                  model.addAttribute("cuDocumenti", cuDocumenti);
                  String sIdElencoCdu = request.getParameter("idElencoCdu");
                  Long lIdElencoCdu = NemboUtils.NUMBERS
                      .checkLong(sIdElencoCdu);
                  if (lIdElencoCdu != null)
                  {
                    DecodificaDTO<Long> documentoSelezionato = NemboUtils.LIST
                        .findDecodificaById(cuDocumenti, lIdElencoCdu);
                    if (documentoSelezionato != null)
                    {
                      boolean conferma = "conferma"
                          .equals(request.getParameter("operation"));
                      String flagCatalogo = conferma
                          ? NemboConstants.FLAGS.SI
                          : null;
                      model.addAttribute("descrTipoDocumentoSelezionato",
                          documentoSelezionato.getDescrizione());
                      model.addAttribute("idElencoCdu",
                          documentoSelezionato.getId());
                      List<GruppoTestoVerbaleDTO> gruppi = cruscottoEJB
                          .getGruppiTestoVerbale(
                              idBandoOggetto, lIdElencoCdu, null, flagCatalogo);

                      if (gruppi == null || gruppi.isEmpty())
                      {
                        // se non esistono gruppi per il bandoOggetto e doc
                        // selezionati
                        // ne inserisco quanti ce ne sono legati al bando
                        // oggetto master ed al CDU selezionato.
                        long idBandoMaster = bando.getIdBandoMaster();
                        cruscottoEJB.creaGruppiTestoVerbali(idBando,
                            idBandoOggetto, lIdElencoCdu, idBandoMaster);
                        gruppi = cruscottoEJB.getGruppiTestoVerbale(
                            idBandoOggetto, lIdElencoCdu, null, flagCatalogo);
                      }
                      if (bandoOggettoModificabile && isReadWrite)
                      {
                        if (conferma)
                        {
                          Errors errors = new Errors();

                          if (gruppi != null)
                          {
                            for (GruppoTestoVerbaleDTO gruppo : gruppi)
                            {
                              /*
                               * if (msgLog == "") msgLog = "Gruppo oggetto: \""
                               * + o.getDescrGruppoOggetto() +
                               * "\"\n Documento: \"" +
                               * documentoSelezionato.getDescrizione() + "\"\n";
                               */

                              /*
                               * msgLog =
                               * logTestiNonInCatalogoPrimaDellaModifica(msgLog,
                               * idBandoOggetto, lIdElencoCdu,
                               * gruppo.getIdGruppoTestoVerbale());
                               */

                              String[] testi = request
                                  .getParameterValues("testo_verbale_"
                                      + gruppo.getIdGruppoTestoVerbale());

                              if (testi != null)
                                for (int j = 0; j < testi.length; j++)
                                  testi[j] = NemboUtils.STRING
                                      .escapeSpecialsChar(testi[j]);

                              gruppo.addTestiNonInCatalogo(testi);

                              // msgLog =
                              // logTestiNonInCatalogoDopoLaModifica(msgLog,
                              // testi);
                              msgLog = "Modificati testi verbali.";
                              validateTesti(errors, gruppo);
                            }
                          }
                          if (!errors.addToModelIfNotEmpty(model))
                          {
                            List<DecodificaDTO<Long>> testi = filterGruppiForInsert(
                                gruppi);
                            try
                            {
                              cruscottoEJB.updateTestiVerbali(idBandoOggetto,
                                  lIdElencoCdu, testi,
                                  getUtenteAbilitazioni(session)
                                      .getIdUtenteLogin(),
                                  msgLog);
                              // rileggo per
                              // averli tutti,
                              // anche quelli
                              // appena inseriti
                              gruppi = cruscottoEJB.getGruppiTestoVerbale(
                                  idBandoOggetto,
                                  lIdElencoCdu, null, null);

                              for (GruppoTestoVerbaleDTO gr : gruppi)
                              {

                                // azzero ordini per metterli nuovi
                                for (TestoVerbaleDTO tv : gr
                                    .getTestoVerbale())
                                  tv.setOrdine(-1);

                                int ordine = 10;

                                String tstHidden = request
                                    .getParameter("testo_verbale_hidden_"
                                        + gr.getIdGruppoTestoVerbale() + "_"
                                        + ordine);
                                long startTime = System.currentTimeMillis(); // fetch
                                                                             // starting
                                                                             // time
                                // se entro un min non trova il match (problema
                                // dovuto a chissa quali caratteri speciali)
                                // esce
                                while ((tstHidden != null
                                    && tstHidden.compareTo("") != 0)
                                    && (System.currentTimeMillis()
                                        - startTime) < 30000)
                                {
                                  tstHidden = request
                                      .getParameter("testo_verbale_hidden_"
                                          + gr.getIdGruppoTestoVerbale() + "_"
                                          + ordine);
                                  if (tstHidden != null)
                                  {
                                    for (TestoVerbaleDTO tv : gr
                                        .getTestoVerbale())
                                    {
                                      if (tv.getOrdine() == -1)
                                        if (NemboUtils.STRING
                                            .escapeSpecialsChar(tv
                                                .getDescrizione()
                                                .replace("\r", "")
                                                .replace("\n",
                                                    "")
                                                .replace("\t", ""))
                                            .compareTo(
                                                NemboUtils.STRING
                                                    .escapeSpecialsChar(
                                                        tstHidden
                                                            .replace("\r", "")
                                                            .replace("\n", "")
                                                            .replace("\t",
                                                                ""))) == 0)
                                        {
                                          tv.setOrdine(ordine);
                                          ordine += 10;
                                          break;
                                        }
                                    }
                                  }

                                }
                                if ((System.currentTimeMillis()
                                    - startTime) < 30000)
                                  cruscottoEJB.updateOrdineTestiGruppo(gr);
                                else
                                {
                                  errors.addError("error",
                                      "Impossibile effettuare la conferma. Controllare che non siano stati inseriti dei caratteri speciali.");
                                  model.addAttribute("errors", errors);
                                }

                              }

                              /*
                               * for (GruppoTestoVerbaleDTO gr : gruppi) {
                               * cruscottoEJB.updateOrdineTestiGruppo(gr); }
                               */

                            }
                            catch (ApplicationException e)
                            {
                              errors.addError("error", e.getMessage());
                            }

                          }
                        }
                      }
                      model.addAttribute("segnaposto",
                          cruscottoEJB.getElencoPlaceholderNemboconf());
                      model.addAttribute("btnConfermaAttivo",
                          Boolean.valueOf(bandoOggettoModificabile));

                      gruppi = cruscottoEJB.getGruppiTestoVerbale(
                          idBandoOggetto,
                          lIdElencoCdu, null, null);
                      model.addAttribute("gruppi", gruppi);
                    }
                  }
                }
            }
          }
        }
      }
    }

    return "cruscottobandi/testoVerbali";
  }

  /*
   * private String logTestiNonInCatalogoDopoLaModifica(String msgLog, String[]
   * testi) {
   * 
   * if (testi != null) { msgLog += "Testi dopo la modifica: \n"; for (String s
   * : testi) { msgLog += "\"" + s + "\"\n"; } } return msgLog; }
   * 
   * private String logTestiNonInCatalogoPrimaDellaModifica(String msgLog, long
   * idBandoOggetto, Long lIdElencoCdu, long idGruppoTestoVerbale) throws
   * InternalUnexpectedException {
   * 
   * // mi servono i testi NON in catalogo prima della modifica
   * List<GruppoTestoVerbaleDTO> gruppiPerLog =
   * cruscottoEJB.getGruppiTestoVerbale(idBandoOggetto, lIdElencoCdu,
   * NemboConstants.FLAGS.SI, null); // leggo i testi non in catalogo prima
   * della modifica if (gruppiPerLog != null) { for (GruppoTestoVerbaleDTO
   * gruppo : gruppiPerLog) { if (gruppo.getIdGruppoTestoVerbale() ==
   * idGruppoTestoVerbale) {
   * 
   * msgLog += "\nGruppo testo verbale: \"" + gruppo.getDescGruppoTestoVerbale()
   * + "\"\n"; msgLog += "Testi editabili prima della modifica: \n"; for
   * (TestoVerbaleDTO t : gruppo.getTestoVerbale()) { if
   * (t.getFlagCatalogo().compareTo("N") == 0) msgLog += "\"" +
   * t.getDescrizione() + "\"\n"; } } } } return msgLog; }
   */

  private List<DecodificaDTO<Long>> filterGruppiForInsert(
      List<GruppoTestoVerbaleDTO> gruppi)
  {
    List<DecodificaDTO<Long>> list = new ArrayList<DecodificaDTO<Long>>();
    for (GruppoTestoVerbaleDTO gruppo : gruppi)
    {
      gruppo.addTestiToUpdateList(list);
    }
    return list;
  }

  private void validateTesti(Errors errors, GruppoTestoVerbaleDTO gruppo)
  {
    long idGruppoTestoVerbale = gruppo.getIdGruppoTestoVerbale();
    List<TestoVerbaleDTO> testi = gruppo.getTestoVerbale();
    if (testi != null)
    {
      for (TestoVerbaleDTO testo : testi)
      {
        errors.validateMandatoryFieldLength(testo.getDescrizione(), 1, 4000,
            "testo_verbale_" + idGruppoTestoVerbale + "_"
                + (testo.getOrdine() - 10));
      }
    }
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "duplicaTesti_{idGruppoTestoVerbali}")
  @IsPopup
  public String duplicaTesti(
      @PathVariable("idGruppoTestoVerbali") long idGruppoTestoVerbali,
      Model model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("idGruppoTestiVerbali", idGruppoTestoVerbali);
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> oggettiBando = cruscottoEJB
        .getElencoGruppiOggetti(idBando, FLAG_NON_ISTANZA);
    model.addAttribute("elencoIstruttorie", oggettiBando);

    return "cruscottobandi/selezioneBandoOggettoAndElencoCduPopup";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "selezionaOggetto_{idBandoOggetto}")
  @ResponseBody
  @IsPopup
  public String selezionaOggetto(
      @PathVariable("idBandoOggetto") long idBandoOggetto, Model model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    List<GruppoOggettoDTO> oggettiBando = cruscottoEJB
        .getElencoGruppiOggetti(idBando, FLAG_NON_ISTANZA);
    GruppoOggettoDTO gruppoOggettoSelezionato = null;
    OggettiIstanzeDTO oggettoSelezionato = null;
    String descrOgg = "";
    String docs = "";

    if (oggettiBando != null)
    {
      for (GruppoOggettoDTO g : oggettiBando)
      {
        List<OggettiIstanzeDTO> oggetti = g.getOggetti();
        if (oggetti != null)
        {
          for (OggettiIstanzeDTO o : oggetti)
            if (o.getIdBandoOggetto() == idBandoOggetto)
            {
              oggettoSelezionato = o;
              gruppoOggettoSelezionato = g;
              model.addAttribute("idBandoOggetto",
                  oggettoSelezionato.getIdBandoOggetto());
              descrOgg = gruppoOggettoSelezionato.getDescrGruppo() + " - "
                  + o.getDescrOggetto();
              model.addAttribute("descrOggettoSelezionato",
                  gruppoOggettoSelezionato.getDescrGruppo() + " - "
                      + o.getDescrOggetto());

              List<DecodificaDTO<Long>> cuDocumenti = cruscottoEJB
                  .getElencoDocumentiBandoOggetto(idBando,
                      bando.getIdBandoMaster(),
                      oggettoSelezionato.getIdBandoOggetto(), false);
              for (DecodificaDTO<Long> d : cuDocumenti)
                docs = docs + d.getId() + "%%%" + d.getDescrizione() + "___";
            }
        }
      }
    }
    return descrOgg + "&&&" + docs;
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "selezionaIdElencoCdu_{idElencoCdu}_{idBandoOggetto}_{idGruppoTestoVerbali}")
  @ResponseBody
  @IsPopup
  public String selezionaIdElencoCdu(
      @PathVariable("idElencoCdu") long idElencoCdu,
      @PathVariable("idBandoOggetto") long idBandoOggetto,
      @PathVariable("idGruppoTestoVerbali") long idGruppoTestoVerbali,
      Model model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    // LEGGO I TESTI PER l'ID OGG BANDO E ID ELENCOCDU E LI COPIO PER QUESTO ID
    // GRUPPO OGGETTO (stesso idTipoCollocazione)
    List<TestoVerbaleDTO> testiNonInCatalogo = cruscottoEJB
        .getTestoVerbali(idBandoOggetto, idElencoCdu, idGruppoTestoVerbali);
    String msgLog = "Modificati testi verbali: duplicazione.";
    cruscottoEJB.duplicaTestoVerbali(idBandoOggetto, idElencoCdu,
        testiNonInCatalogo, idGruppoTestoVerbali, msgLog,
        utenteAbilitazioni.getIdUtenteLogin());

    return "ok";
  }

  @RequestMapping(value = "confermaImportazioneTesti_{idBandoOggetto}_{idElencoCdu}")
  @IsPopup
  public String confermaImporta(Model model,
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBandoOggetto") long idBandoOggetto,
      @PathVariable("idElencoCdu") long idElencoCdu)
      throws InternalUnexpectedException
  {

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    long idBandoMaster = bando.getIdBandoMaster();

    boolean hasGruppiDaImportare = cruscottoEJB.hasGruppiDaImportare(
        idBandoMaster, idBando, idBandoOggetto, idElencoCdu);

    if (hasGruppiDaImportare)
    {
      model.addAttribute("message",
          "L'importazione dei testi causerà l'eliminazione dei testi presenti in catalogo. Continuare?");
      model.addAttribute("showConferma", "true");
    }
    else
    {
      model.addAttribute("message",
          "Attenzione! Non è stato configurato un catalogo per il documento selezionato.");
    }
    return "cruscottobandi/confermaImportazioneTesti";
  }

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "importaTestiDaCatalogo_{idBandoOggetto}_{idElencoCdu}")
  @ResponseBody
  @IsPopup
  public String importaTestiDaCatalogo(
      @PathVariable("idBandoOggetto") long idBandoOggetto,
      @PathVariable("idElencoCdu") long idElencoCdu, Model model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);

    BandoDTO bando = (BandoDTO) session.getAttribute("bando");
    long idBando = bando.getIdBando();
    long idBandoMaster = bando.getIdBandoMaster();

    String msgLog = "Modificati testi verbali: importazione da catalogo.";
    cruscottoEJB.importaTestiDaCatalogo(idBando, idBandoMaster, idBandoOggetto,
        idElencoCdu, msgLog, utenteAbilitazioni.getIdUtenteLogin());

    return "SUCCESS";
  }
}
