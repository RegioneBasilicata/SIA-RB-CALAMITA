package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.stampa.InfoHeader;

public class HeaderLetteraAmmissioneFinanziamento extends Fragment
{
  public static final String TAG_NAME_HEADER = "Header";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {

    InfoHeader infoHeader = stampeEJB.getInfoHeader(idBandoOggetto);

    writer.writeStartElement(TAG_NAME_HEADER);

    writeTag(writer, "Titolo1", "REGIONE TOBECONFIG");
    writeTag(writer, "Titolo2", infoHeader.getListaAmmCompetenza());
    writeTag(writer, "Titolo3", "");
    writer.writeStartElement("DatiAzienda");

    writeTag(writer, "Denominazione", "");
    writeTag(writer, "Indirizzo", "");
    writeTag(writer, "CUAA", "");
    writeTag(writer, "PEC", "");
    writer.writeEndElement();
    writer.writeStartElement("RiferimentiDomanda");
    writeTag(writer, "NumeroDomanda", "");

    writer.writeEndElement();
    writer.writeStartElement("Oggetto");
    writeTag(writer, "Bando", infoHeader.getDescBando());
    writer.writeEndElement();
    writer.writeEndElement();
  }
}
