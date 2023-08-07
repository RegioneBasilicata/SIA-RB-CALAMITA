package it.csi.nembo.nemboconf.util.stampa;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.modolxp.modolxppdfgensrv.dto.pdfstatic.PdfStaticInputRequest;
import it.csi.modolxp.modolxpsrv.dto.Applicazione;
import it.csi.modolxp.modolxpsrv.dto.Modello;
import it.csi.modolxp.modolxpsrv.dto.Modulo;
import it.csi.modolxp.modolxpsrv.dto.RendererModality;
import it.csi.modolxp.modolxpsrv.dto.RiferimentoAdobe;
import it.csi.modolxp.modolxpsrv.dto.XmlModel;
import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.stampa.fragment.AmmissioneFinanziamentoPremio_SezioniTesto;
import it.csi.nembo.nemboconf.util.stampa.fragment.AmmissioneFinanziamento_SezioniTesto;
import it.csi.nembo.nemboconf.util.stampa.fragment.FirmaLetteraAmmissioneFinanziamento;
import it.csi.nembo.nemboconf.util.stampa.fragment.FirmaLetteraAmmissionePremio;
import it.csi.nembo.nemboconf.util.stampa.fragment.Fragment;
import it.csi.nembo.nemboconf.util.stampa.fragment.Global;
import it.csi.nembo.nemboconf.util.stampa.fragment.Header;
import it.csi.nembo.nemboconf.util.stampa.fragment.HeaderLetteraAmmissioneFinanziamento;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroAllegati;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroAmbitiTematici;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroCaniDaGuardiania;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroCaratteristicheDelGAL;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroColtureAziendali;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroComuniIndicatori;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroContiCorrenti;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDanni;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDatiAnticipo;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDatiIdentificativi;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDatiPascolamento;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDichiarazioni;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroDocumentiRichiesti;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroEntiCdA;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroImpegni;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroInterventi;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroInterventiDanni;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroInterventiInfrastrutture;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroInterventiLetteraAmmissioneFinanziamento;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroOperazioniImpegni;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroOperazioniInCampo;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroPLVVegetale;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroPLVZootecnica;
//import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroPLVZootecnica;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroParticelleImpegno;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroPianoFinanziarioGAL;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroRazzeProtette;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroRendicontazioneSpese;
//import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroScorte;
import it.csi.nembo.nemboconf.util.stampa.fragment.QuadroScorte;

public abstract class Stampa
{
  public static final Map<String, Fragment> MAP_FRAGMENTS = new HashMap<String, Fragment>();
  public static final String                BASE_RIF_ADOBE = "Applications/Nembo/1.0/Forms/";

  public static IStampeEJB                  stampeEJB     = null;
  static
  {
    MAP_FRAGMENTS.put("GLOBAL", new Global());
    MAP_FRAGMENTS.put("HEADER", new Header());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DATI_IDENTIFICATIVI,
        new QuadroDatiIdentificativi());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DICHIARAZIONI,
        new QuadroDichiarazioni());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.IMPEGNI,
        new QuadroImpegni());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.ALLEGATI,
        new QuadroAllegati());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.CONTI_CORRENTI,
        new QuadroContiCorrenti());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.CARATTERISTICHE_GAL,
        new QuadroCaratteristicheDelGAL());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.ENTI_CDA,
        new QuadroEntiCdA());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.AMBITI_TEMATICI,
        new QuadroAmbitiTematici());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.INTERVENTI,
        new QuadroInterventi());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.COMUNI_INDICATORI,
        new QuadroComuniIndicatori());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.PIANO_FINANZIARIO_LEADER,
        new QuadroPianoFinanziarioGAL());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.OPERAZIONI_IN_CAMPO,
        new QuadroOperazioniInCampo());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.OPERAZIONI_IMPEGNI,
        new QuadroOperazioniImpegni());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.RAZZE_MINACCIATE,
        new QuadroRazzeProtette());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.CANI_GUARDIANIA,
        new QuadroCaniDaGuardiania());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DATI_PASCOLAMENTO,
        new QuadroDatiPascolamento());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.PARTICELLE_IMPEGNO,
        new QuadroParticelleImpegno());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DATI_ANTICIPO,
        new QuadroDatiAnticipo());

    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.RENDICONTAZIONE_SPESE,
        new QuadroRendicontazioneSpese());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.RENDICONTAZIONE_SALDO,
        new QuadroRendicontazioneSpese());

    MAP_FRAGMENTS.put("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO",
        new AmmissioneFinanziamento_SezioniTesto());
    MAP_FRAGMENTS.put("AMMISSIONE_FINANZIAMENTO_INTERVENTI",
        new QuadroInterventiLetteraAmmissioneFinanziamento());
    MAP_FRAGMENTS.put("HEADER_AMMISSIONE_FINANZIAMENTO",
        new HeaderLetteraAmmissioneFinanziamento());
    MAP_FRAGMENTS.put("FIRMA_AMMISSIONE_FINANZIAMENTO",
        new FirmaLetteraAmmissioneFinanziamento());

    MAP_FRAGMENTS.put("MISURE_PREMIO_SEZIONI_TESTO",
        new AmmissioneFinanziamentoPremio_SezioniTesto());
    MAP_FRAGMENTS.put("FIRMA_AMMISSIONE_FINANZIAMENTO_PREMIO",
        new FirmaLetteraAmmissionePremio());
    
    //Quadri Nembo
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.SUPERFICIE_COLTURA, new QuadroPLVVegetale());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.ALLEVAMENTI, new QuadroPLVZootecnica());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.SCORTE, new QuadroScorte());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DANNI, new QuadroDanni());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.INTERVENTI_INFRASTRUTTURE, new QuadroInterventiInfrastrutture());
    MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.INTERVENTI,   new QuadroInterventiDanni());
	MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.COLTURE_AZIENDALI, new QuadroColtureAziendali());
	MAP_FRAGMENTS.put(NemboConstants.QUADRO.CODICE.DOCUMENTI_RICHIESTI, new QuadroDocumentiRichiesti());

    try
    {
      InitialContext ic = new InitialContext();
      stampeEJB = (IStampeEJB) ic.lookup("java:app/Stampe");
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static final String DEFAULT_ENCODING = "UTF-8";

  public abstract byte[] genera(long idBandoOggetto) throws Exception;

  public byte[] genera(long id, String cuName) throws Exception
  {
    return null;
  };

  protected XMLStreamWriter getXMLStreamWriter(
      ByteArrayOutputStream xmlOutputStream) throws XMLStreamException
  {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    return factory.createXMLStreamWriter(xmlOutputStream, DEFAULT_ENCODING);
  }

  public byte[] callModol(byte[] xmlInput) throws Exception
  {
    /*
     * imposto la modalità di rendering da utilizzare per la restituzione dei
     * dati
     */
    RendererModality rm = new RendererModality();
    rm.setIdRendererModality(new Integer(3)); // PDF
    rm.setSelezionataPerRendering(true);

    /*
     * imposto il percorso di memorizzazione del template interno al server
     * LiveCycle
     */
    RiferimentoAdobe rifAdobe = new RiferimentoAdobe();
    rifAdobe.setXdpURI(getRifAdobe());

    /* definisco il Modello da utilizzare */
    Modello modello = new Modello();
    modello.setCodiceModello(getCodiceModello());
    modello.setRendererModality(new RendererModality[]
    { rm });
    modello.setRiferimentoAdobe(rifAdobe);

    /* definisco il Modulo da utilizzare */
    Modulo modulo = new Modulo();
    modulo.setCodiceModulo(getCodiceModulo());
    modulo.setModello(modello);

    /* definisco l'Applicazione da utilizzare */
    Applicazione applicazione = new Applicazione();
    applicazione.setCodiceApplicazione(getCodiceApplicazione());
    applicazione.setDescrizioneApplicazione(getDescrizioneApplicazione());

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(xmlInput);

    /*
     * finalmente invoco il servizio tramite la PD già istanziata in precedenza
     */
    Modulo moduloMerged = NemboUtils.WS.getModolServClient()
        .mergeModulo(applicazione, null, modulo, xml);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    byte[] ba = moduloMerged.getDataContent();
    ba = trasformStaticPDF(ba, applicazione);
    return ba;
  }

  private byte[] trasformStaticPDF(byte[] pdfBytes, Applicazione applicazione)
      throws Exception
  {
    byte[] bXmlModol = null;

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(pdfBytes);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    PdfStaticInputRequest pdfStatic = new PdfStaticInputRequest();
    pdfStatic.setPdfInput(pdfBytes);
   

    it.csi.modolxp.modolxppdfgensrv.dto.Applicazione applicazione2 = new it.csi.modolxp.modolxppdfgensrv.dto.Applicazione();
    applicazione.setCodiceApplicazione(applicazione.getCodiceApplicazione());
    applicazione
        .setDescrizioneApplicazione(applicazione.getDescrizioneApplicazione());

    bXmlModol = NemboUtils.WS.getModolPDFGenServClient()
        .toStaticPdf(applicazione2, null, pdfStatic);

    return bXmlModol;
  }

  protected String getDescrizioneApplicazione()
  {
    return "Gestione Pratiche NEMBO";
  }

  protected String getCodiceApplicazione()
  {
    return "NEMBOPRATICHE";
  }

  protected void generaStampaQuadri(XMLStreamWriter writer, long idBandoOggetto)
      throws Exception
  {
    List<String> codiciQuadro = stampeEJB
        .getElencoCodiciQuadroInOggetto(idBandoOggetto);
    if (codiciQuadro != null && !codiciQuadro.isEmpty())
    {
      for (String codice : codiciQuadro)
      {
        Fragment f = MAP_FRAGMENTS.get(codice);
        if (f != null)
        {
          f.writeFragment(writer, idBandoOggetto, stampeEJB);
        }
      }
    }
  }

  public void writeFragment(String codFragment, XMLStreamWriter writer,
      long idBandoOggetto, String cuName)
      throws Exception
  {
    Fragment f = MAP_FRAGMENTS.get(codFragment);
    if (f != null)
    {
      f.writeFragment(writer, idBandoOggetto, stampeEJB);
    }
  }

  public void writeFragmentTesti(String codFragment, XMLStreamWriter writer,
      Long idBandoOggetto, String cuName)
      throws Exception
  {
    Fragment f = MAP_FRAGMENTS.get(codFragment);
    if (f != null)
    {
      f.writeFragmentTesti(writer, stampeEJB, idBandoOggetto, cuName);
    }
  }

  protected abstract String getCodiceModulo();

  protected abstract String getCodiceModello();

  protected abstract String getRifAdobe();

  public abstract String getDefaultFileName();

}
