package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class Global extends Fragment
{
  public static final String TAG_NAME_GLOBAL = "Global";

  @Override
  public void writeFragment(XMLStreamWriter writer, long idBandoOggetto,
      IStampeEJB stampeEJB) throws Exception
  {
    writer.writeStartElement(TAG_NAME_GLOBAL);
    writeTag(writer, "Bozza", String.valueOf(true));

    String codiceTipoBando = stampeEJB.getCodiceTipoBando(idBandoOggetto);
    if (codiceTipoBando != null && codiceTipoBando.compareTo("G") == 0)
    {
      writeTag(writer, "IsGAL", "true");
      writeTag(writer, "AmmGAL", "anteprima");
    }
    String codiceOggetto = stampeEJB.getCodiceOggetto(idBandoOggetto);
    if (codiceOggetto != null && (codiceOggetto.compareTo("ISABG") == 0
        || codiceOggetto.compareTo("ISAMB") == 0))
    {
      writeTag(writer, "IsAmmib", "true");
    }

    if (codiceOggetto != null && (codiceOggetto.compareTo("ISVAR") == 0))
    {
      writeTag(writer, "IsVariante", "true");
    }

    writer.writeEndElement();
  }

  @Override
  public void writeFragmentTesti(XMLStreamWriter writer, IStampeEJB stampeEJB,
      Long idBandoOggetto, String cuName) throws Exception
  {

    writer.writeStartElement(TAG_NAME_GLOBAL);
    writeTag(writer, "Bozza", String.valueOf(true));

    String codiceTipoBando = stampeEJB.getCodiceTipoBando(idBandoOggetto);
    String codiceOggetto = stampeEJB.getCodiceOggetto(idBandoOggetto);
    if (codiceOggetto != null && (codiceOggetto.compareTo("ISABG") == 0
        || codiceOggetto.compareTo("ISAMB") == 0))
    {
      writeTag(writer, "IsAmmib", "true");
    }

    if (codiceOggetto != null && (codiceOggetto.compareTo("ISVAR") == 0))
    {
      writeTag(writer, "IsVariante", "true");
    }

    writeTagSecondaIstruttoria(writer, cuName);

    writer.writeEndElement();
  }

  private void writeTagSecondaIstruttoria(XMLStreamWriter writer, String cuName)
      throws XMLStreamException
  {

    if (cuName.equals(
        NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_2)
        ||

        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_2)
        ||
        cuName.equals(
            NemboConstants.USECASE.STAMPA.CRUSCOTTO_BANDI.GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_2)

    )
      writeTag(writer, "IsSecondaIstruttoria", "true");
  }
}
