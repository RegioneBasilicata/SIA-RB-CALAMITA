package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroScorte extends Fragment
{
  private static final String TAG_NAME_DESCRIZIONE_SCORTE = "DescrizioneScorte";
private static final String TAG_NAME_QUANTITA_SCORTE = "QuantitaScorte";
private static final String TAG_NAME_TIPO_SCORTE = "TipoScorte";
private static final String TAG_NAME_FRAGMENT_SCORTE 		= "QuadroScorte";
  private static final String SEZIONE_VUOTA 				= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE 		= "TitoloSezioneScorte";
  private static final String TAG_NAME_SCORTE 				= "Scorte";
  private static final String TAG_NAME_TAB_SCORTE 			= "TabScorte";
  private static final String TAG_NAME_RIGA_SCORTE			= "RigaScorte";
  private static final String TAG_NAME_VISIBILITY_SCORTE	= "VisibilityScorte";
  private static final String TAG_NAME_SEZIONE_NULLA		= "SezioneNulla";
  private static final String TAG_NAME_VISIBILITY_TESTO_NULLO	= "VisibilityTestoNullo";

	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception 
	{
	  	String visibilityTestoNullo = "false";
	
	    writer.writeStartElement(TAG_NAME_FRAGMENT_SCORTE);
	    writeTag(writer,SEZIONE_VUOTA,"false");
	    writeVisibility(writer, true);
	    writeTag(writer, TAG_NAME_TITOLO_SEZIONE, "Quadro - Scorte");
	    writer.writeStartElement(TAG_NAME_SCORTE);
		
	    	writeTag(writer,TAG_NAME_VISIBILITY_SCORTE,"true");
	    	writer.writeStartElement(TAG_NAME_TAB_SCORTE);
	    	
	    		writer.writeStartElement(TAG_NAME_RIGA_SCORTE);
		    		writeTag(writer, TAG_NAME_TIPO_SCORTE, "");
		    		writeTag(writer, TAG_NAME_QUANTITA_SCORTE, "");
		    		writeTag(writer, TAG_NAME_DESCRIZIONE_SCORTE, "");
	    		writer.writeEndElement(); 		//TAG_NAME_RIGA_PLV
	    	writer.writeEndElement(); 			//TAG_NAME_TAB_SCORTE
	    	visibilityTestoNullo = "false";
	    writer.writeEndElement(); 								//TAG_NAME_SCORTE
	    writer.writeStartElement(TAG_NAME_SEZIONE_NULLA); 		//TAG_NAME_SEZIONE_NULLA
	    writeTag(writer, TAG_NAME_VISIBILITY_TESTO_NULLO, visibilityTestoNullo);
	    writer.writeEndElement(); 								//TAG_NAME_SEZIONE_NULLA
		writer.writeEndElement(); 								//TAG_NAME_FRAGMENT
	}
}
