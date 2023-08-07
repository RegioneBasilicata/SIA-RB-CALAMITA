package it.csi.nembo.nemboconf.util.stampa.fragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class QuadroAllegati extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_ALLEGATI = "QuadroAllegati";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    List<GruppoInfoDTO> dichiarazioni = stampeEJB.getDichiarazioni(
        idBandoOggetto, NemboConstants.QUADRO.CODICE.ALLEGATI);

    writer.writeStartElement(TAG_NAME_FRAGMENT_ALLEGATI);
    writeVisibility(writer, true);
    writer.writeStartElement("GruppiAllegati");
    for (GruppoInfoDTO gruppo : dichiarazioni)
    {
      writer.writeStartElement("GruppoAllegati");
      writeTag(writer, "TitoloGruppoAllegati", gruppo.getDescrizione());
      writer.writeStartElement("Allegati");
      for (DettaglioInfoDTO info : gruppo.getElencoDettagliInfo())
      {
        writer.writeStartElement("Allegato");
        writeTag(writer, "FlagObbligatorio", String.valueOf(
            NemboConstants.FLAGS.SI.equals(info.getFlagObbligatorio())));
        writeTag(writer, "FlagSelezionato", String.valueOf(false));
        writeTag(writer, "TestoAllegato", getTestoDichiarazione(info));
        writer.writeEndElement(); // Allegato
      }
      writer.writeEndElement(); // Allegati
      writer.writeEndElement(); // GruppoAllegati
    }
    writer.writeEndElement(); // GruppiAllegati
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_ALLEGATI
  }

  protected String getTestoDichiarazione(DettaglioInfoDTO info)
  {
    String testoAllegati = info.getDescrizione();
    if (testoAllegati.indexOf("$$") >= 0)
    {
      Matcher m = Pattern
          .compile("(\\$\\$INTEGER|\\$\\$STRING|\\$\\$NUMBER|\\$\\$DATE)")
          .matcher(testoAllegati);
      StringBuilder sb = new StringBuilder(testoAllegati);
      int start = 0;
      while (m.find())
      {
        String placeHolder = m.group(1);
        start = sb.indexOf(placeHolder, start);
        sb.replace(start, start + placeHolder.length(), "_______");
      }
      testoAllegati = sb.toString();
    }
    return testoAllegati;
  }

}
