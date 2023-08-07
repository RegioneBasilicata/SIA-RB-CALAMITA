package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroDanni extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_DANNI 					= "QuadroDanni";
  private static final String SEZIONE_VUOTA 									= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_DANNI 					= "TitoloSezioneDanni";
  private static final String TAG_NAME_DANNI 									= "Danni";
  private static final String TAG_NAME_TAB_DANNI 								= "TabDanni";
  private static final String TAG_NAME_RIGA_DANNI								= "RigaDanni";
  private static final String TAG_NAME_RIGA_TOTALE								= "RigaTotale";
  private static final String TAG_NAME_TOTALE_IMPORTO							= "TotImporto";
  private static final String TAG_NAME_PART_DANNI								= "PartDanni";
  private static final String TAG_NAME_VISIBILITY_PARTICELLARE					= "VisibilityParticellare";
  private static final String TAG_NAME_TAB_PART_DANNI							= "TabPartDanni";
  private static final String TAG_NAME_RIGA_PART_DANNI							= "RigaPartDanni";
  


	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception 
	{

		writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_DANNI);
		writeTag(writer, SEZIONE_VUOTA, "false");
		writeVisibility(writer, true);
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_DANNI, "Quadro - Danni");
		writer.writeStartElement(TAG_NAME_DANNI);
		writer.writeStartElement(TAG_NAME_TAB_DANNI);
		writer.writeStartElement(TAG_NAME_RIGA_DANNI);
		writeTag(writer, "Progressivo", "");
		writeTag(writer, "TipoDanno", "");
		writeTag(writer, "Denominazione", "");
		writeTag(writer, "DescEntitaDanneggiata", "");
		writeTag(writer, "DescDanno", "");
		writeTag(writer, "Quantita", "");
		writeTag(writer, "Importo", "");
		writer.writeEndElement(); // TAG_NAME_RIGA_PLV
		String sommaFormatted = "0,00 €";
		writer.writeStartElement(TAG_NAME_RIGA_TOTALE); // TAG_NAME_RIGA_TOTALE
		writeTag(writer, TAG_NAME_TOTALE_IMPORTO, sommaFormatted);
		writer.writeEndElement(); // TAG_NAME_RIGA_TOTALE

		writer.writeEndElement(); // TAG_NAME_TAB_DANNI
		writer.writeEndElement(); // TAG_NAME_DANNI

		writer.writeStartElement(TAG_NAME_PART_DANNI);
		writeTag(writer, TAG_NAME_VISIBILITY_PARTICELLARE, "true");
		writer.writeStartElement(TAG_NAME_TAB_PART_DANNI);
		writer.writeStartElement(TAG_NAME_RIGA_PART_DANNI);
		writeTag(writer, "Progressivo", "");
		writeTag(writer, "Provincia", "");
		writeTag(writer, "Comune", "");
		writeTag(writer, "Sezione", "");
		writeTag(writer, "Foglio", "");
		writeTag(writer, "Particella", "");
		writeTag(writer, "Subalterno", "");
		writeTag(writer, "SupCatastale", "");
		writeTag(writer, "Superficie", "");
		writeTag(writer, "Utilizzo", "");
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndElement(); // TAG_NAME_PART_DANNI
		writer.writeEndElement(); // TAG_NAME_FRAGMENT_QUADRO_DANNI
	}
}
