package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroPLVZootecnica extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_PLV_ZOOTECNICA 	= "QuadroPLVZootecnica";
  private static final String SEZIONE_VUOTA 					= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_PLV_ZOOTECNICA 			= "TitoloSezionePLVZootecnica";
  private static final String TAG_NAME_PLV_ZOOTECNICA 			= "PLVZootecnica";
  private static final String TAG_NAME_TAB_PLV_ZOOTECNICA 		= "TabPLVZootecnica";
  private static final String TAG_NAME_RIGA_PLV_ZOOTECNICA		= "RigaPLVZootecnica";
  private static final String TAG_NAME_VISIBILITY_PLV_ZOOTECNICA		= "VisibilityPLVZootenica";
  private static final String TAG_NAME_SEZIONE_NULLA			= "SezioneNulla";
  private static final String TAG_NAME_VISIBILITY_TESTO_NULLO	= "VisibilityTestoNullo";
  

	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception 
	{
		String visibilityTestoNullo;

		writer.writeStartElement(TAG_NAME_FRAGMENT_PLV_ZOOTECNICA);
		writeTag(writer, SEZIONE_VUOTA, "false");
		writeVisibility(writer, true);
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_PLV_ZOOTECNICA, "Quadro - P.L.V. Zootecnica");
		writer.writeStartElement(TAG_NAME_PLV_ZOOTECNICA);
		writeTag(writer,TAG_NAME_VISIBILITY_PLV_ZOOTECNICA,"true");
			writer.writeStartElement(TAG_NAME_TAB_PLV_ZOOTECNICA);
			writer.writeStartElement(TAG_NAME_RIGA_PLV_ZOOTECNICA);
			writeTag(writer, "CodAzZootecnica", "");
			writeTag(writer, "Specie", "");
			writeTag(writer, "Categoria", "");
			writeTag(writer, "TipoProd", "");
			writeTag(writer, "NCapi", "");
			writeTag(writer, "QProdAnnua", "");
			writeTag(writer, "ProdLorda", "");
			writeTag(writer, "Reimpieghi", "");
			writeTag(writer, "ProdNetta", "");
			writeTag(writer, "UDM", "");
			writeTag(writer, "PrezzoUnitario", "");
			writeTag(writer, "PLVZoo", "");
		writer.writeEndElement(); // TAG_NAME_RIGA_PLV
		writer.writeEndElement(); 							// TAG_NAME_PLV_ZOOTECNICA
		visibilityTestoNullo = "false";
		writer.writeEndElement(); 							// TAG_NAME_TAB_PLV
		writer.writeStartElement(TAG_NAME_SEZIONE_NULLA);	//TAG_NAME_VISIBILITY_TESTO_NULLO
		writeTag(writer,TAG_NAME_VISIBILITY_TESTO_NULLO,visibilityTestoNullo);
		writer.writeEndElement(); 							// TAG_NAME_VISIBILITY_TESTO_NULLO
		writer.writeEndElement(); 							// TAG_NAME_FRAGMENT_PLV_ZOOTECNICA	
	}
}
