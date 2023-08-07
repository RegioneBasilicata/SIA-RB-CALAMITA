package it.csi.nembo.nemboconf.util.stampa.anticipo;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.stampa.Stampa;

public class LetteraIstruttoriaAnticipoNegativo extends Stampa
{
  public static final String ROOT_TAG  = "Domanda";
  private String             cuNameRef = null;

  public LetteraIstruttoriaAnticipoNegativo()
  {
    super();
  }

  public LetteraIstruttoriaAnticipoNegativo(String cuNameRef)
  {
    super();
    this.cuNameRef = cuNameRef;
  }

  @Override
  public byte[] genera(long idBandoOggetto, String cuName) throws Exception
  {
    if (cuNameRef != null)
    {
      cuName = cuNameRef;
    }
    else
      if (!cuName.endsWith("-" + NemboConstants.ESITO.TIPO.NEGATIVO))
      {
        cuName += "-" + NemboConstants.ESITO.TIPO.NEGATIVO;
      }
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    generaXML(writer, idBandoOggetto, stampeEJB, cuName);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB quadroEJB, String cuName) throws Exception
  {
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    writeFragmentTesti("GLOBAL", writer, idBandoOggetto, cuName);
    writeFragment("HEADER_AMMISSIONE_FINANZIAMENTO", writer, idBandoOggetto,
        cuName);
    writeFragmentTesti("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO", writer,
        idBandoOggetto, cuName);
    writeFragment(NemboConstants.QUADRO.CODICE.DATI_ANTICIPO, writer,
        idBandoOggetto, cuName);
    writeFragment("FIRMA_VERBALE_AMMISSIONE_FINANZIAMENTO", writer,
        idBandoOggetto, cuName);

    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {

    return "nembo_comunicazioneAnticipo-N";
  }

  protected String getCodiceModello()
  {

    return "nembo_comunicazioneAnticipo-N";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "comunicazioneAnticipo-N.xdp";
  }

  @Override
  public String getDefaultFileName()
  {
    return "Lettera istruttoria anticipo.pdf";
  }

  @Override
  public byte[] genera(long idBandoOggetto) throws Exception
  {
    return null;
  }
}
