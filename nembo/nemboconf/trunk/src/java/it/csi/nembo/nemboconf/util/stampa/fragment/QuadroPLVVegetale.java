package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroPLVVegetale extends Fragment
{
	private static final String TAG_VISIBILITY_PLV_VEGETALE = "VisibilityPLVVegetale";
	private static final String TAG_VISIBILITY_TESTO_NULLO = "VisibilityTestoNullo";
	private static final String TAG_SEZIONE_NULLA = "SezioneNulla";
	public static final String TAG_NAME_DOMANDA = "Domanda";
	public static final String TAG_NAME_FRAGMENT_PLV_VEGETALE = "QuadroPLVVegetale";
	public static final String TAG_NAME_PLV_VEGETALE = "PLVVegetale";
	public static final String TAG_NAME_TAB_PLV = "TabPLV";
	public static final String TAG_NAME_RIGA_PLV = "RigaPLV";


	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception 
	{
		writer.writeStartElement(TAG_NAME_FRAGMENT_PLV_VEGETALE);
		writeVisibility(writer, true);
		writeTag(writer, "TitoloSezionePLVVegetale", "Quadro - P.L.V. Vegetale");

		writer.writeStartElement(TAG_NAME_PLV_VEGETALE);
		writeTag(writer, TAG_VISIBILITY_PLV_VEGETALE, "true");
		writer.writeStartElement(TAG_NAME_TAB_PLV);
		writer.writeStartElement(TAG_NAME_RIGA_PLV);
		writeTag(writer, "Utilizzo", "");
		writeTag(writer, "Superficie", "");
		writeTag(writer, "Produzione", "");
		writeTag(writer, "GGLavorative", "");
		writeTag(writer, "UF", "");
		writeTag(writer, "ReimpieghiQ", "");
		writeTag(writer, "ReimpieghiUF", "");
		writeTag(writer, "PLV", "");
		writer.writeEndElement(); // TAG_NAME_RIGA_PLV
		writer.writeEndElement(); // TAG_NAME_TAB_PLV
		writer.writeEndElement(); // TAG_NAME_PLV_VEGETALE

		writer.writeStartElement(TAG_SEZIONE_NULLA);
		writeTag(writer, TAG_VISIBILITY_TESTO_NULLO, "false");
		writer.writeEndElement();
		writer.writeEndElement(); // TAG_NAME_FRAGMENT_PLV_VEGETALE
	}
}
