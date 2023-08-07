package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class FirmaLetteraAmmissioneFinanziamento extends Fragment
{
  public static final String TAG_NAME_FIRMA = "RiquadroFirma";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FIRMA);
    writeTag(writer, "DataFirma", "");
    writeTag(writer, "FunzionarioIstruttore", "");
    writeTag(writer, "FunzionarioGradoSuperiore", "");
    writer.writeEndElement();
  }

  @Override
  public void writeFragmentTesti(XMLStreamWriter writer, IStampeEJB stampeEJB,
      Long idBandoOggetto, String cuName) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FIRMA);
    writeTag(writer, "DataFirma", "");
    writeTag(writer, "FunzionarioIstruttore", "");
    writeTag(writer, "FunzionarioGradoSuperiore", "");
    writer.writeEndElement();
  }
}
