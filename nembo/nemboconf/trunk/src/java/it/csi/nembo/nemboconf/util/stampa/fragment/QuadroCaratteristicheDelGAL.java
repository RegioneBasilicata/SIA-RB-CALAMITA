package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroCaratteristicheDelGAL extends Fragment
{
  public static final String TAG_NAME_FRAGMENT                              = "QuadroCaratteristicheGAL";
  public static final String TAG_NAME_CARATTERISTICHE_GAL                   = "CaratteristicheGAL";
  public static final String TAG_NAME_SEZIONE_COMPAGINE_SOCIETARIA          = "CompagineSocietariaGAL";
  public static final String TAG_NAME_SEZIONE_ELENCO_COMPETENZE             = "ElencoCompetenzeGAL";
  public static final String TAG_NAME_SEZIONE_COMPETENZA_GAL                = "CompetenzaGAL";

  public static final String TAG_NAME_SEZIONE_ELENCO_MODALITA_CONCERTAZIONE = "ElencoModalitaConcertazionePSL";
  public static final String TAG_NAME_SEZIONE_MODALITA_CONCERTAZIONE        = "ModalitaConcertazionePSL";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writer.writeEndElement();
  }
}
