
package it.csi.nembo.nemboconf.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class ComunicazioneProvvedimentoFinale extends Stampa
{
  public static final String ROOT_TAG  = "Domanda";
  @SuppressWarnings("unused")
  private String             cuNameRef = null;
  private String             cuaaBeneficiario;
  private String             identificativoProcedimento;

  public ComunicazioneProvvedimentoFinale()
  {
    super();
  }

  public ComunicazioneProvvedimentoFinale(String cuNameRef)
  {
    super();
    this.cuNameRef = cuNameRef;
  }

  @Override
  public byte[] genera(long idBandoOggetto, String cuName) throws Exception
  {
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    generaXML(writer, idBandoOggetto, stampeEJB, cuName);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB, String cuName) throws Exception
  {
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    writeFragment("HEADER_AMMISSIONE_FINANZIAMENTO", writer, idBandoOggetto,
        cuName);
    writeFragmentTesti("GLOBAL", writer, idBandoOggetto, cuName);
    writeFragment("MISURE_PREMIO_SEZIONI_TESTO", writer, idBandoOggetto,
        cuName);
    writeFragment("FIRMA_AMMISSIONE_FINANZIAMENTO_PREMIO", writer,
        idBandoOggetto, cuName);
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "nembo_ComunicazioneISTPF";
  }

  protected String getCodiceModello()
  {
    return "nembo_ComunicazioneISTPF";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "ComunicazioneISTPF.xdp";
  }

  @Override
  public String getDefaultFileName()
  {
    String ret = "";
    if (cuaaBeneficiario != null)
      ret += cuaaBeneficiario;

    if (identificativoProcedimento != null)
      ret += "_" + identificativoProcedimento;

    return ret + "_ProvvedFinale.pdf";
  }

  @Override
  public byte[] genera(long idBandoOggetto) throws Exception
  {
    return null;
  }
}
