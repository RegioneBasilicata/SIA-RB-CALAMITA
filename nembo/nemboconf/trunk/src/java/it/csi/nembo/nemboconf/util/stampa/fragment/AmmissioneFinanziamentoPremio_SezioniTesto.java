package it.csi.nembo.nemboconf.util.stampa.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class AmmissioneFinanziamentoPremio_SezioniTesto extends Fragment
{
  public static final String TAG_NAME_HEADER_TESTO_CONFIGURATO = "SezioneTestoConfigurato";
  public static final String TAG_NAME_CASTELLETTO              = "Castelletto";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    Map<String, List<String>> mapTesti = stampeEJB.getTestiStampeIstruttoria(
        NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_COMUNICAZIONE_PROVVEDIMENTO_FINALE,
        idBandoOggetto);
    Map<String, Object> cache = new HashMap<String, Object>();
    if (mapTesti != null && !mapTesti.isEmpty())
    {
      for (String tipoCollocazioneTesto : mapTesti.keySet())
      {
        writeSezioneTestoConfigurato(writer, tipoCollocazioneTesto,
            mapTesti.get(tipoCollocazioneTesto), cache);
      }
    }
  }

  @Override
  public void writeFragmentTesti(XMLStreamWriter writer, IStampeEJB stampeEJB,
      Long idBandoOggetto, String cuName) throws Exception
  {
    Map<String, List<String>> mapTesti = stampeEJB
        .getTestiStampeIstruttoria(cuName, idBandoOggetto);
    Map<String, Object> cache = new HashMap<String, Object>();
    if (mapTesti != null && !mapTesti.isEmpty())
    {
      for (String tipoCollocazioneTesto : mapTesti.keySet())
      {
        writeSezioneTestoConfigurato(writer, tipoCollocazioneTesto,
            mapTesti.get(tipoCollocazioneTesto), cache);
      }
    }
  }

  private void writeSezioneTestoConfigurato(XMLStreamWriter writer,
      String tipoCollocazioneTesto, List<String> list,
      Map<String, Object> cache)
      throws Exception
  {
    if (list == null || list.isEmpty())
    {
      return;
    }
    writer.writeStartElement(
        TAG_NAME_HEADER_TESTO_CONFIGURATO + tipoCollocazioneTesto);
    for (String testo : list)
    {
      writeTestoConfigurato(writer, tipoCollocazioneTesto, testo);
    }
    writer.writeEndElement();

  }

  private void writeTestoConfigurato(XMLStreamWriter writer,
      String tipoCollocazioneTesto, String testo) throws XMLStreamException
  {
    writer.writeStartElement("RigaTestoConfigurato" + tipoCollocazioneTesto);
    writeTag(writer, "TestoConfigurato" + tipoCollocazioneTesto, testo);
    writer.writeEndElement();
  }
}