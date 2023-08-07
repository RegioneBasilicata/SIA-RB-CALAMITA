package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;


public class QuadroInterventiDanni extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_INTERVENTI 					= "QuadroInterventi";
  private static final String SEZIONE_VUOTA 										= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_INTERVENTI 					= "TitoloSezioneInterventi";
  private static final String TAG_NAME_INTERVENTI 									= "Interventi";
  private static final String TAG_NAME_TAB_INTERVENTI 								= "TabInterventi";
  private static final String TAG_NAME_RIGA_INTERVENTI								= "RigaInterventi";
  
  private static final String TAG_NAME_PROGRESSIVO_DANNO	 								= "ProgressivoDanno";
  private static final String TAG_NAME_TIPO_DANNO 										= "TipoDanno";
  private static final String TAG_NAME_DANNO 											= "Danno";
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
  
  private static final String TAG_NAME_PROGRESSIVO										= "Progressivo";
  private static final String TAG_NAME_COMUNE											= "Comune";
  private static final String TAG_NAME_SEZIONE											= "Sezione";
  private static final String TAG_NAME_FOGLIO											= "Foglio";
  private static final String TAG_NAME_PARTICELLA										= "Particella";
  private static final String TAG_NAME_SUBALTERNO										= "Subalterno";
  private static final String TAG_NAME_SUPCATASTALE										= "SupCatastale";
  private static final String TAG_NAME_OCCUPAZIONE										= "Occupazione";
  private static final String TAG_NAME_DESTINAZIONE										= "Destinazione";
  private static final String TAG_NAME_SUPUTILIZZATA									= "SupUtilizzata";
  

	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception
	{
		writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_INTERVENTI);
			writeTag(writer, SEZIONE_VUOTA, "false");
			writeVisibility(writer, true);
			writeTag(writer, TAG_NAME_TITOLO_SEZIONE_INTERVENTI, "Quadro - Interventi");
			writer.writeStartElement(TAG_NAME_INTERVENTI);
				writer.writeStartElement(TAG_NAME_TAB_INTERVENTI);
				
					writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);
						writeTag(writer, TAG_NAME_PROGRESSIVO_DANNO, "");
						writeTag(writer, TAG_NAME_TIPO_DANNO, "");
						writeTag(writer, TAG_NAME_DANNO, "");
						writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
						writeTag(writer, TAG_NAME_INTERVENTO, "");
						writeTag(writer, TAG_NAME_DESC_INTERVENTO, "");
						writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, "");
					writer.writeEndElement(); // TAG_NAME_RIGA_PLV	
						
					writer.writeStartElement(TAG_NAME_RIGA_TOTALE);		//TAG_NAME_RIGA_TOTALE
						writeTag(writer,TAG_NAME_TOTALE_IMPORTO,"");
					writer.writeEndElement(); 							// TAG_NAME_RIGA_TOTALE
				writer.writeEndElement(); 							// TAG_NAME_TAB_INTERVENTI
			writer.writeEndElement(); 							// TAG_NAME_INTERVENTI
					
			writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE); 	//TAG_NAME_LOCALIZZAZIONE
				writeTag(writer, TAG_NAME_VISIBILITY_LOC, "true");
					writer.writeStartElement(TAG_NAME_TAB_LOC);			//TAG_NAME_TAB_LOC
						writer.writeStartElement(TAG_NAME_RIGA_LOC);	//TAG_NAME_RIGA_LOC
							writeTag(writer,TAG_NAME_PROGRESSIVO,"");
							writeTag(writer,TAG_NAME_COMUNE,"");
							writeTag(writer,TAG_NAME_SEZIONE,"");
							writeTag(writer,TAG_NAME_FOGLIO,"");
							writeTag(writer,TAG_NAME_PARTICELLA,"");
							writeTag(writer,TAG_NAME_SUBALTERNO,"");
							writeTag(writer,TAG_NAME_SUPCATASTALE,"");
							writeTag(writer,TAG_NAME_OCCUPAZIONE,"");
							writeTag(writer,TAG_NAME_DESTINAZIONE,"");
							writeTag(writer,TAG_NAME_SUPUTILIZZATA,"");
						writer.writeEndElement(); 						//TAG_NAME_RIGA_LOC					
					writer.writeEndElement(); 							//TAG_NAME_TAB_LOC			
			writer.writeEndElement();							// TAG_NAME_PART_INTERVENTI 
		writer.writeEndElement(); 							// TAG_NAME_FRAGMENT_QUADRO_INTERVENTI
	}


}
