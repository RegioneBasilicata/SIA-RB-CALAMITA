package it.csi.nembo.nemboconf.util.stampa.fragment;

import java.math.BigDecimal;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class QuadroRendicontazioneSpese extends Fragment
{
  public static final String TAG_NAME_FRAGMENT      = "QuadroSpese";
  public static final String TAG_NAME_TABELLA_SPESE = "TabellaSpese";
  public static final String TAG_NAME_RIGA_SPESE    = "RigaSpese";
  public static final String TAG_NAME_RIGA_TOTALI   = "TotaliSpese";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writeTag(writer, "TitoloSezioneSpese", "Quadro - Rendicontazione Spese");
    writer.writeStartElement(TAG_NAME_TABELLA_SPESE);
    BigDecimal totImportoAmmesso = new BigDecimal(0);
    BigDecimal totImportoContributo = new BigDecimal(0);
    BigDecimal totImportoSpesa = new BigDecimal(0);
    BigDecimal totContributoRichiesto = new BigDecimal(0);
    writeRiga(writer);
    writer.writeEndElement();
    // RIGA TOTALI
    writer.writeStartElement(TAG_NAME_RIGA_TOTALI);
    writeTag(writer, "TotImportoAmmesso",
        NemboUtils.FORMAT.formatGenericNumber(totImportoAmmesso, 2, false));
    writeTag(writer, "TotImportoContributo", NemboUtils.FORMAT
        .formatGenericNumber(totImportoContributo, 2, false));
    writeTag(writer, "TotImportoSpesa",
        NemboUtils.FORMAT.formatGenericNumber(totImportoSpesa, 2, false));
    writeTag(writer, "TotContributoRichiesto", NemboUtils.FORMAT
        .formatGenericNumber(totContributoRichiesto, 2, false));
    writer.writeEndElement();
    writer.writeEndElement();
  }

  protected void writeRiga(XMLStreamWriter writer) throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_RIGA_SPESE);
    writeTag(writer, "Progressivo", "1");
    writeTag(writer, "DescrizioneIntervento", "");
    writeTag(writer, "ImportoAmmesso", "0,00");
    writeTag(writer, "ImportoContributo", "0,00");
    writeTag(writer, "ImportoSpesa", "0,00");
    writeTag(writer, "ContributoRichiesto", "0,00");
    writer.writeEndElement();
  }
}
