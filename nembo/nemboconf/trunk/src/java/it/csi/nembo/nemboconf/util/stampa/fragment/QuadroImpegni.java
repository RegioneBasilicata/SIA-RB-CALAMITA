package it.csi.nembo.nemboconf.util.stampa.fragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class QuadroImpegni extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_IMPEGNI = "QuadroImpegni";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    List<GruppoInfoDTO> dichiarazioni = stampeEJB.getDichiarazioni(
        idBandoOggetto, NemboConstants.QUADRO.CODICE.IMPEGNI);

    writer.writeStartElement(TAG_NAME_FRAGMENT_IMPEGNI);
    writeVisibility(writer, true);

    if (dichiarazioni != null && dichiarazioni.size() > 0)
    {
      writer.writeStartElement("GruppiImpegni");
      for (GruppoInfoDTO gruppo : dichiarazioni)
      {
        writer.writeStartElement("GruppoImpegni");
        writeTag(writer, "TitoloGruppoImpegni", gruppo.getDescrizione());
        writer.writeStartElement("Impegni");
        for (DettaglioInfoDTO info : gruppo.getElencoDettagliInfo())
        {
          writer.writeStartElement("Impegno");
          writeTag(writer, "FlagObbligatorio", String.valueOf(
              NemboConstants.FLAGS.SI.equals(info.getFlagObbligatorio())));
          writeTag(writer, "FlagSelezionato", String.valueOf(false));
          writeCDataTag(writer, "TestoImpegno", getTestoDichiarazione(info));
          writer.writeEndElement(); // Dichiarazione
        }
        writer.writeEndElement(); // Dichiarazioni
        writer.writeEndElement(); // GruppoDichiarazioni
      }
      writer.writeEndElement(); // GruppiDichiarazioni
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_DICHIARAZIONI
  }

  protected String getTestoDichiarazione(DettaglioInfoDTO info)
  {
    String testoDichiarazioni = info.getDescrizione();
    if (testoDichiarazioni.indexOf("$$") >= 0)
    {
      Matcher m = Pattern
          .compile("(\\$\\$INTEGER|\\$\\$STRING|\\$\\$NUMBER|\\$\\$DATE)")
          .matcher(testoDichiarazioni);
      StringBuilder sb = new StringBuilder(testoDichiarazioni);
      int start = 0;
      while (m.find())
      {
        String placeHolder = m.group(1);
        start = sb.indexOf(placeHolder, start);
        sb.replace(start, start + placeHolder.length(), "_______");
      }
      testoDichiarazioni = sb.toString();
    }
    return testoDichiarazioni;
  }

}
