package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroEntiCdA extends Fragment
{
  public static final String    TAG_NAME_FRAGMENT             = "QuadroEntiCda";
  public static final String    TAG_NAME_SEZIONE_QUOTE        = "Quote";
  public static final String    TAG_NAME_SEZIONE_TOTALI_QUOTE = "TotaliQuote";
  public static final String    TAG_NAME_SEZIONE_TAB_CDA      = "TabCda";
  public static final String    TAG_NAME_SEZIONE_RIGA_ENTI    = "RigaEnti";
  public static final String    TAG_NAME_SEZIONE_MEMBRI       = "TabCdaMembri";
  public static final String    TAG_NAME_SEZIONE_RIGA_MEMBRI  = "RigaCdaMembri";
  protected static final String ENTE_PUBBLICO                 = "EP";
  protected static final String DECODIFICA_ENTE_PUBBLICO      = "Ente Pubblico";
  protected static final String DECODIFICA_SOCIO_PRIVATO      = "Socio Privato";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writer.writeEndElement();
  }

}
