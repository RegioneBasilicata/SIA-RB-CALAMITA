package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class QuadroInterventiLetteraAmmissioneFinanziamento extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_INTERVENTI = "QuadroInterventi";
  public static final String TAG_NAME_ELENCO_INTERVENTI   = "Interventi";
  public static final String TAG_NAME_LOCALIZZAZIONE      = "Localizzazione";
  public static final String TAG_NAME_ELENCO_PARTICELLE   = "ElencoParticelleInt";
  public static final String TAG_NAME_DATI_PARTICELLA     = "DatiParticellaInt";
  public static final String TAG_NAME_RIBASSO             = "Ribasso";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {

  }

  @Override
  public void writeFragmentTesti(XMLStreamWriter writer, IStampeEJB stampeEJB,
      Long idBandoOggetto, String cuName) throws Exception
  {

    // Questi elementi servono per far sì che vengano visualizzate le seguenti
    // tabelle nei doc
    // di anteprima delle domande di sostegno (acconto, saldo, anticipo)
    // per ora tutte le altre stampe non hanno questi tag, quindi vengono
    // ignorati. Nel caso fare classe apposta.

    if (cuName.compareTo(
        NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_1) == 0
        || cuName.compareTo(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_1) == 0)
    {
      writer.writeStartElement("QuadroInterventiPag");
      writeVisibility(writer, true);
      writer.writeStartElement("ElencoInterventi");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeStartElement("ElencoContributi");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeStartElement("ElencoProspetto");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeStartElement("TotaliProspetti");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeEndElement();
    }

    if (cuName.compareTo(
        NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_1) == 0
        || cuName.compareTo(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_1) == 0)
    {
      writer.writeStartElement("QuadroInterventiPag");
      writeVisibility(writer, true);
      writer.writeStartElement("ElencoInterventi");
      writeVisibility(writer, false);
      writer.writeEndElement();
      writer.writeStartElement("ElencoContributi");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeStartElement("ElencoProspetto");
      writeVisibility(writer, false);
      writer.writeEndElement();
      writer.writeStartElement("TotaliProspetti");
      writeVisibility(writer, false);
      writer.writeEndElement();
      writer.writeEndElement();
    }

    if (cuName.compareTo(
        NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_1) == 0
        || cuName.compareTo(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_1) == 0)
    {
      writer.writeStartElement("QuadroAnticipo");
      writer.writeStartElement("SezioneTitoloAnticipo");
      writeVisibility(writer, true);
      writer.writeEndElement();
      writer.writeEndElement();
    }

  }

}
