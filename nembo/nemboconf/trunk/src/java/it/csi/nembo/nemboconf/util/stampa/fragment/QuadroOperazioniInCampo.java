package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroOperazioniInCampo extends Fragment
{
  public static final String TAG_NAME_FRAGMENT          = "QuadroOperazioniInCampo";
  public static final String TAG_NAME_ELENCO_OPERAZIONI = "ElencoOperazioniInCampo";
  public static final String TAG_NAME_OPERAZIONE        = "DettaglioOperazioneInCampo";
  public static final String TAG_ELENCO_PARTICELLE      = "ElencoParticelleCampo";
  public static final String TAG_PARTICELLA             = "DatiParticellaCampo";
  public static final String TAG_ELENCO_LEGENDA         = "LegendaUsiDelSuolo";
  public static final String TAG_DETTAGLIO_LEGENDA      = "DettaglioLegendaSuolo";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writer.writeEndElement(); // TAG_NAME_FRAGMENT
  }
}