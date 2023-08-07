package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroDocumentiRichiesti extends Fragment
{
	
	
	private static final String CONTENUTO_TITOLO_1 					= "Direzione Agricoltura - Settore A1711B - Attuazione programmi relativi alle strutture delle aziende agricole e alle avversità atmosferiche";
	private static final String CONTENUTO_TITOLO_2 					= "D.Lgs. 102/2004 e s.m.i. art.5 c.3";
	private static final String CONTENUTO_TITOLO_3 					= "ELENCO DOCUMENTI RIPRISTINO STRUTTURE";
	
	private static final String ID_SEZIONE_A						= "A";
	private static final String ID_SEZIONE_B						= "B";
	private static final String ID_SEZIONE_C						= "C";
	private static final String ID_SEZIONE_D						= "D";
	private static final String ID_SEZIONE_E						= "E";
	private static final String ID_SEZIONE_F						= "F";
	private static final String ID_SEZIONE_G						= "G";
	private static final String ID_SEZIONE_H						= "H";
	
	private static final String TRUE								= "true";
	
	private static final String TAG_F_LETTERA_DOCUMENTI_RICHIESTI	= "FLetteraDocumentiRichiesti";
	
	private static final String TAG_NAME_HEADER_SEZIONI				= "HeaderSezioni";
		
	private static final String TAG_NAME_TITOLO_1 					= "Titolo1";
	private static final String TAG_NAME_TITOLO_2 					= "Titolo2";
	private static final String TAG_NAME_TITOLO_3 					= "Titolo3";
	
	private static final String TAG_NAME_SEZ_A 						= "SezA";
	private static final String TAG_NAME_VISIBILITY_A 				= "VisibilityA";
	private static final String TAG_NAME_HEADER_ROW_A 				= "HeaderRowA";
	private static final String TAG_NAME_CODICE_SEZ_A 				= "CodiceSezA";
	private static final String TAG_NAME_DESC_SEZ_A 				= "DescSezA";
	private static final String TAG_NAME_TAB_SEZ_A 					= "TabSezA";
	private static final String TAG_NAME_RIGA_SEZ_A 				= "RigaSezA";
	private static final String TAG_NAME_DOC_A 						= "DocA";
	
	private static final String TAG_NAME_SEZ_B 						= "SezB";
	private static final String TAG_NAME_VISIBILITY_B 				= "VisibilityB";
	private static final String TAG_NAME_HEADER_ROW_B 				= "HeaderRowB";
	private static final String TAG_NAME_CODICE_SEZ_B 				= "CodiceSezB";
	private static final String TAG_NAME_DESC_SEZ_B					= "DescSezB";
	private static final String TAG_NAME_TAB_SEZ_B 					= "TabSezB";
	private static final String TAG_NAME_RIGA_SEZ_B 				= "RigaSezB";
	private static final String TAG_NAME_DOC_B 						= "DocB";
	
	private static final String TAG_NAME_SEZ_C 						= "SezC";
	private static final String TAG_NAME_VISIBILITY_C 				= "VisibilityC";
	private static final String TAG_NAME_HEADER_ROW_C 				= "HeaderRowC";
	private static final String TAG_NAME_CODICE_SEZ_C 				= "CodiceSezC";
	private static final String TAG_NAME_DESC_SEZ_C 				= "DescSezC";
	private static final String TAG_NAME_TAB_SEZ_C 					= "TabSezC";
	private static final String TAG_NAME_RIGA_SEZ_C 				= "RigaSezC";
	private static final String TAG_NAME_DOC_C 						= "DocC";
	
	private static final String TAG_NAME_SEZ_D 						= "SezD";
	private static final String TAG_NAME_VISIBILITY_D 				= "VisibilityD";
	private static final String TAG_NAME_HEADER_ROW_D 				= "HeaderRowD";
	private static final String TAG_NAME_CODICE_SEZ_D 				= "CodiceSezD";
	private static final String TAG_NAME_DESC_SEZ_D 				= "DescSezD";
	private static final String TAG_NAME_TAB_SEZ_D 					= "TabSezD";
	private static final String TAG_NAME_RIGA_SEZ_D 				= "RigaSezD";
	private static final String TAG_NAME_DOC_D 						= "DocD";
	
	private static final String TAG_NAME_SEZ_E 						= "SezE";
	private static final String TAG_NAME_VISIBILITY_E 				= "VisibilityE";
	private static final String TAG_NAME_HEADER_ROW_E 				= "HeaderRowE";
	private static final String TAG_NAME_CODICE_SEZ_E 				= "CodiceSezE";
	private static final String TAG_NAME_DESC_SEZ_E 				= "DescSezE";
	private static final String TAG_NAME_TAB_SEZ_E 					= "TabSezE";
	private static final String TAG_NAME_RIGA_SEZ_E 				= "RigaSezE";
	private static final String TAG_NAME_DOC_E 						= "DocE";
	
	private static final String TAG_NAME_SEZ_F 						= "SezF";
	private static final String TAG_NAME_VISIBILITY_F 				= "VisibilityF";
	private static final String TAG_NAME_HEADER_ROW_F				= "HeaderRowF";
	private static final String TAG_NAME_CODICE_SEZ_F 				= "CodiceSezF";
	private static final String TAG_NAME_DESC_SEZ_F 				= "DescSezF";
	private static final String TAG_NAME_TAB_SEZ_F 					= "TabSezF";
	private static final String TAG_NAME_RIGA_SEZ_F 				= "RigaSezF";
	private static final String TAG_NAME_DOC_F 						= "DocF";
	
	private static final String TAG_NAME_SEZ_G 						= "SezG";
	private static final String TAG_NAME_VISIBILITY_G 				= "VisibilityG";
	private static final String TAG_NAME_HEADER_ROW_G 				= "HeaderRowG";
	private static final String TAG_NAME_CODICE_SEZ_G 				= "CodiceSezG";
	private static final String TAG_NAME_DESC_SEZ_G 				= "DescSezG";
	private static final String TAG_NAME_TAB_SEZ_G 					= "TabSezG";
	private static final String TAG_NAME_RIGA_SEZ_G 				= "RigaSezG";
	private static final String TAG_NAME_DOC_G 						= "DocG";
	
	private static final String TAG_NAME_SEZ_H 						= "SezH";
	private static final String TAG_NAME_VISIBILITY_H 				= "VisibilityH";
	private static final String TAG_NAME_HEADER_ROW_H 				= "HeaderRowH";
	private static final String TAG_NAME_CODICE_SEZ_H 				= "CodiceSezH";
	private static final String TAG_NAME_DESC_SEZ_H 				= "DescSezH";
	private static final String TAG_NAME_TAB_SEZ_H 					= "TabSezH";
	private static final String TAG_NAME_RIGA_SEZ_H 				= "RigaSezH";
	private static final String TAG_NAME_DOC_H 						= "DocH";
	
	private static final String VISIBILITY		 					= "Visibility";
	private static final String VISIBILITY_VALUE		 			= "true";
  
	@Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception
	{
		writer.writeStartElement(TAG_F_LETTERA_DOCUMENTI_RICHIESTI);					//FLetteraDocumentiRichiesti
		    writer.writeStartElement(TAG_NAME_HEADER_SEZIONI);								//HeaderSezioni
		    writeTag(writer,TAG_NAME_TITOLO_1,CONTENUTO_TITOLO_1);							//Titolo1
		    writeTag(writer,TAG_NAME_TITOLO_2,CONTENUTO_TITOLO_2);							//Titolo2
		    writeTag(writer,TAG_NAME_TITOLO_3,CONTENUTO_TITOLO_3);							//Titolo3
		    
		    // SEZIONE A
		    writer.writeStartElement(TAG_NAME_SEZ_A); 										//TAG SEZA
		    	writeTag(writer,TAG_NAME_VISIBILITY_A, TRUE);								//TAG VISIBILITYA
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_A); 								//TAG TABSEZA
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_A);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_A, ID_SEZIONE_A);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_A, "");
			    	writer.writeEndElement();
			    	writer.writeStartElement(TAG_NAME_RIGA_SEZ_A); 							//TAG RIGASEZA
		    			writeTag(writer, TAG_NAME_DOC_A, ""); 				//TAG docA
		    		writer.writeEndElement();	    	
		    	writer.writeEndElement(); 													//TAG SEZA
		    writer.writeEndElement();	
		    // FINE SEZIONE A
		    	
		    // SEZIONE B
		    writer.writeStartElement(TAG_NAME_SEZ_B); 										//TAG SEZB
		    	writeTag(writer,TAG_NAME_VISIBILITY_B, TRUE);								//TAG VISIBILITYB
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_B); 								//TAG TABSEZB		    	
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_B);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_B,ID_SEZIONE_B);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_B,"");
			    	writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_B); 							//TAG RIGASEZB
			    			writeTag(writer, TAG_NAME_DOC_B, ""); 				//TAG docB
			    		writer.writeEndElement();	    	
		    	writer.writeEndElement(); 													//TAG SEZB
		  
		    writer.writeEndElement();	
		    // FINE SEZIONE B
		    

		    // SEZIONE C
		    writer.writeStartElement(TAG_NAME_SEZ_C); 										//TAG SEZC
		    	writeTag(writer,TAG_NAME_VISIBILITY_C, TRUE);								//TAG VISIBILITYC
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_C); 								//TAG TABSEZC		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_C);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_C,ID_SEZIONE_C);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_C,"");
		    		writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_C); 							//TAG RIGASEZC
			    			writeTag(writer, TAG_NAME_DOC_C, ""); 				//TAG docC
			    		writer.writeEndElement();
		    	writer.writeEndElement(); 													//TAG SEZC
		    
		    writer.writeEndElement();	
		    // FINE SEZIONE C
		    
		    // SEZIONE D
		    writer.writeStartElement(TAG_NAME_SEZ_D); 										//TAG SEZD
		    	writeTag(writer,TAG_NAME_VISIBILITY_D, TRUE);								//TAG VISIBILITYD
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_D); 								//TAG TABSEZD		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_D);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_D,ID_SEZIONE_D);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_D,"");
		    		writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_D); 							//TAG RIGASEZD
			    			writeTag(writer, TAG_NAME_DOC_D, ""); 				//TAG docD
			    		writer.writeEndElement();
		    	writer.writeEndElement(); 													//TAG SEZD
		    
		    writer.writeEndElement();	
		    // FINE SEZIONE D

		    // SEZIONE E
		    writer.writeStartElement(TAG_NAME_SEZ_E); 										//TAG SEZE
		    	writeTag(writer,TAG_NAME_VISIBILITY_E, TRUE);								//TAG VISIBILITYE
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_E); 								//TAG TABSEZE		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_E);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_E,ID_SEZIONE_E);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_E,"");
		    		writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_E); 							//TAG RIGASEZE
			    			writeTag(writer, TAG_NAME_DOC_E, ""); 				//TAG docE
			    		writer.writeEndElement();
			    			    	
		    	writer.writeEndElement(); 													//TAG SEZE
		    
		    writer.writeEndElement();	
		    // FINE SEZIONE E
		    
		    // SEZIONE F
		    writer.writeStartElement(TAG_NAME_SEZ_F); 										//TAG SEZF
		    	writeTag(writer,TAG_NAME_VISIBILITY_F, TRUE);								//TAG VISIBILITYF
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_F); 								//TAG TABSEZF		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_F);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_F,ID_SEZIONE_F);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_F,"");
		    		writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_F); 							//TAG RIGASEZF
			    			writeTag(writer, TAG_NAME_DOC_F, ""); 				//TAG docF
			    		writer.writeEndElement();
			    			    	
		    	writer.writeEndElement(); 													//TAG SEZF
		    
		    writer.writeEndElement();	
		    // FINE SEZIONE F

		    // SEZIONE G
		    writer.writeStartElement(TAG_NAME_SEZ_G); 										//TAG SEZG
		    	writeTag(writer,TAG_NAME_VISIBILITY_G, TRUE);								//TAG VISIBILITYG
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_G); 								//TAG TABSEG		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_G);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_G,ID_SEZIONE_G);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_G,"");
		    		writer.writeEndElement();
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_G); 							//TAG RIGASEZG
			    			writeTag(writer, TAG_NAME_DOC_G, ""); 				//TAG docG
			    		writer.writeEndElement();
			    			    	
		    	writer.writeEndElement(); 													//TAG SEZG
		    
		    writer.writeEndElement();	
		    // FINE SEZIONE G
		    
		    // SEZIONE H
		    writer.writeStartElement(TAG_NAME_SEZ_H); 										//TAG SEZH
		    String altroDoc = "";
		    	writeTag(writer,TAG_NAME_VISIBILITY_H, TRUE);								//TAG VISIBILITYH
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_H); 								//TAG TABSEZH		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_H);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_H,ID_SEZIONE_H);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_H,"");
		    		writer.writeEndElement();
		    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_H); 							//TAG RIGASEZH
		    			writeTag(writer, TAG_NAME_DOC_H, altroDoc); 				//TAG docH
		    		writer.writeEndElement();
			    			    	
		    	writer.writeEndElement(); 													//TAG SEZH
		   
		    writer.writeEndElement();	
		    // FINE SEZIONE H
		    
		    writeTag(writer,VISIBILITY,VISIBILITY_VALUE);							//Visibility
		    
		    writer.writeEndElement();	//header sezioni
	    writer.writeEndElement();	//f_lettera

	  }
	

	
	
}
