package it.csi.nembo.nemboconf.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamWriter;

public class Pratica extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  @Override
  public byte[] genera(long idBandoOggetto) throws Exception
  {
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    generaXML(writer, idBandoOggetto);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, long idBandoOggetto)
      throws Exception
  {
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    MAP_FRAGMENTS.get("GLOBAL").writeFragment(writer, idBandoOggetto,
        stampeEJB);
    MAP_FRAGMENTS.get("HEADER").writeFragment(writer, idBandoOggetto,
        stampeEJB);
    generaStampaQuadri(writer, idBandoOggetto);
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "nembo_pratica";
  }

  protected String getCodiceModello()
  {
    return "nembo_pratica";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "pratica.xdp";
  }

  @Override
  public String getDefaultFileName()
  {
    return "pratica.pdf";
  }
}
