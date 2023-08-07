package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;


public class QuadroInterventiInfrastrutture extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_INTERVENTI 					= "QuadroInterInfrastrutture";
  private static final String SEZIONE_VUOTA 										= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_INTERVENTI 					= "TitoloSezioneInterInfrastrutture";
  private static final String TAG_NAME_INTERVENTI 									= "InterInfrastrutture";
  private static final String TAG_NAME_TAB_INTERVENTI 								= "TabInterInfrastrutture";
  private static final String TAG_NAME_RIGA_INTERVENTI								= "RigaInterInfrastrutture";
  
  private static final String TAG_NAME_PROGRESSIVO_INTERVENTO 							= "ProgressivoIntervento";
  private static final String TAG_NAME_INTERVENTO 										= "Intervento";
  private static final String TAG_NAME_DESC_INTERVENTO 									= "DescIntervento";
  private static final String TAG_NAME_IMPORTO_INTERVENTO 								= "ImportoIntervento";
  
  private static final String TAG_NAME_RIGA_TOTALE										= "RigaTotale";
  private static final String TAG_NAME_TOTALE_IMPORTO									= "TotImporto";
  
  private static final String TAG_NAME_LOCALIZZAZIONE									= "Localizzazione";
  private static final String TAG_NAME_VISIBILITY_LOC									= "VisibilityLoc";
  private static final String TAG_NAME_TAB_LOC											= "TabLoc";
  private static final String TAG_NAME_RIGA_LOC											= "RigaLoc";
  
  private static final String TAG_NAME_COMUNE											= "Comune";

	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception
	{
		writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_INTERVENTI);
		writeTag(writer, SEZIONE_VUOTA, "false");
		writeVisibility(writer, true);
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_INTERVENTI, "Quadro - Interventi Richiesti");
		writer.writeStartElement(TAG_NAME_INTERVENTI);
		writer.writeStartElement(TAG_NAME_TAB_INTERVENTI);
		writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);			// TAG_NAME_RIGA_INTERVENTI
		
		writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
		writeTag(writer, TAG_NAME_INTERVENTO, "");
		writeTag(writer, TAG_NAME_DESC_INTERVENTO, "");
		writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, "");
		writer.writeEndElement(); 									// TAG_NAME_RIGA_INTERVENTI
		String sommaFormatted = "";
		
		writer.writeStartElement(TAG_NAME_RIGA_TOTALE);					//TAG_NAME_RIGA_TOTALE
		writeTag(writer,TAG_NAME_TOTALE_IMPORTO,sommaFormatted);
		writer.writeEndElement(); 										// TAG_NAME_RIGA_TOTALE
		writer.writeEndElement(); 										// TAG_NAME_TAB_INTERVENTI
		writer.writeEndElement(); 										// TAG_NAME_INTERVENTI
		
		writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE); 		//TAG_NAME_LOCALIZZAZIONE
		writeTag(writer,TAG_NAME_VISIBILITY_LOC,"false");
		writer.writeStartElement(TAG_NAME_TAB_LOC);			//TAG_NAME_TAB_LOC
		writer.writeStartElement(TAG_NAME_RIGA_LOC);		//TAG_NAME_RIGA_LOC
		writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
		writeTag(writer, TAG_NAME_COMUNE, "");
		writer.writeEndElement();							//TAG_NAME_RIGA_LOC
		writer.writeEndElement();							//TAG_NAME_TAB_LOC
		writer.writeEndElement();								// TAG_NAME_LOCALIZZAZIONE 
		writer.writeEndElement(); 								// TAG_NAME_FRAGMENT_QUADRO_INTERVENTI
	}

}
