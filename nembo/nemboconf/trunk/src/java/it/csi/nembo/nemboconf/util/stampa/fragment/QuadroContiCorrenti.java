package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroContiCorrenti extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_CONTI_CORRENTI = "QuadroContoCorrente";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT_CONTI_CORRENTI);
    writeVisibility(writer, true);
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_CONTI_CORRENTI
  }
}
