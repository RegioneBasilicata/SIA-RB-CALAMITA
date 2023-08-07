package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroInterventi extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_INTERVENTI = "QuadroInterventi";
  public static final String TAG_NAME_LOCALIZZAZIONE      = "Localizzazione";
  public static final String TAG_NAME_ELENCO_PARTICELLE   = "ElencoParticelleInt";
  public static final String TAG_NAME_DATI_PARTICELLA     = "DatiParticellaInt";
  public static final String TAG_NAME_RIBASSO             = "Ribasso";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT_INTERVENTI);
    writeVisibility(writer, true);

    writer.writeEndElement(); // TAG_NAME_FRAGMENT_INTERVENTI
  }
}
