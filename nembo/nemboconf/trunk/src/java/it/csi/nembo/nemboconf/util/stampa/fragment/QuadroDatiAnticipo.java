package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroDatiAnticipo extends Fragment
{
  public static final String TAG_NAME_FRAGMENT                = "QuadroAnticipo";
  public static final String TAG_NAME_SEZIONE_TITOLO_ANTICIPO = "SezioneTitoloAnticipo";
  public static final String TAG_NAME_SEZIONE_ANTICIPO        = "SezioneAnticipo";
  public static final String TAG_NAME_SEZIONE_FIDEIUSSIONE    = "SezioneFideiussione";
  public static final String TAG_NAME_SEZIONE_BANCA           = "SezioneBanca";
  public static final String TAG_NAME_SEZIONE_ALTRO_ISTITUTO  = "SezioneAltroIstituto";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeSezioneTitoloAnticipo(writer);
    writeDatiAnticipo(writer);
    writeDatiFideiussione(writer);
    writeDatiBanca(writer);
    writeAltroistituto(writer);
    writer.writeEndElement();
  }

  protected void writeSezioneTitoloAnticipo(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_TITOLO_ANTICIPO);
    writeVisibility(writer, true);
    writer.writeEndElement();
  }

  protected void writeDatiAnticipo(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_ANTICIPO);
    writeTag(writer, "TitolosezAnticipo", "DATI ANTICIPO");
    writer.writeEndElement();
  }

  protected void writeDatiFideiussione(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_FIDEIUSSIONE);
    writeTag(writer, "TitoloSezFideiussione", "DATI FIDEJIUSSIONE");
    writer.writeEndElement();
  }

  protected void writeDatiBanca(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_BANCA);
    writeTag(writer, "VisibilitaBanca", "true");
    writeTag(writer, "TitoloSezBanca", "ISTITUTO BANCARIO");
    writer.writeEndElement();
  }

  protected void writeAltroistituto(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_ALTRO_ISTITUTO);
    writeTag(writer, "VisibilitaIstituto", "false");
    writeTag(writer, "TitoloSezAltroIstituto", "ALTRO ISTITUTO");
    writer.writeEndElement();
  }
}
