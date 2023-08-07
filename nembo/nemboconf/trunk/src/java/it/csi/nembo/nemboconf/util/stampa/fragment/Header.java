package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.stampa.InfoHeader;

public class Header extends Fragment
{
  public static final String TAG_NAME_HEADER      = "Header";
  public static final String TAG_NAME_CASTELLETTO = "Castelletto";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    InfoHeader infoHeader = stampeEJB.getInfoHeader(idBandoOggetto);

    writer.writeStartElement(TAG_NAME_HEADER);

    writeTag(writer, "Titolo1", "REGIONE TOBECONFIG");
    writeTag(writer, "Titolo2", infoHeader.getListaAmmCompetenza());// testata.getDescAmmCompetenza());
    writeTag(writer, "Titolo3", "");
    writer.writeStartElement(TAG_NAME_CASTELLETTO);
    // Al momento è vuoto
    writer.writeEndElement();
    writeTag(writer, "Bando", infoHeader.getDescBando());

    writeTag(writer, "Operazioni", infoHeader.getListaMisure());
    writeTag(writer, "Oggetto", infoHeader.getDescOggetto());
    writeTag(writer, "Specificita", "");
    writer.writeEndElement();
  }
}
