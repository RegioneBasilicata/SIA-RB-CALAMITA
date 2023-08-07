package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroDatiIdentificativi extends Fragment
{
  public static final String TAG_NAME_FRAGMENT           = "QuadroDatiIdentificativi";
  public static final String TAG_NAME_SEZIONE_ANAGRAFICA = "SezioneAnagrafica";
  public static final String TAG_NAME_SEZIONE_TITOLARE   = "SezioneTitolare";
  public static final String TAG_NAME_SEZIONE_FIRMATARIO = "SezioneFirmatario";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writer.writeEndElement();
  }
}
