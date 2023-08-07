package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public class QuadroColtureAziendali extends Fragment
{
  private static final String TAG_NAME_SEZIONE_VUOTA = "SezioneVuota";
  public static final String TAG_NAME_DOMANDA = "Domanda";
  public static final String TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI = "QuadroColtureAziendali";
  public static final String TAG_NAME_TITOLO_SEZIONE_COLTURE_AZIENDALI = "TitoloSezioneColtureAziendali";
  public static final String TAG_NAME_SEZ_DATI_COLTURE = "SezDatiColture";
  public static final String TAG_NAME_VISIBILITY_DATI = "VisibilityDati";
  
  public static final String TAG_NAME_SEZ_RIEPILOGO = "SezRiepilogo"; 
  public static final String TAG_NAME_TOT_SUP = "TotSup";
  public static final String TAG_NAME_PLV_ORDINARIA = "PLVOrdinaria";
  public static final String TAG_NAME_PLV_EFFETTIVA = "PLVEffettiva";
  public static final String TAG_NAME_PERC_DANNO = "PercDanno";
  
  public static final String TAG_NAME_SEZ_ELENCO_COLTURE = "SezElencoColture";
  public static final String TAG_NAME_TAB_COLTURE_AZIENDALI = "TabColtureAziendali";
  
  public static final String TAG_NAME_RIGA_COLTURE_AZIENDALI = "RigaColtureAziendali";
  public static final String TAG_NAME_UBICAZIONETERRENO = "UbicazioneTerreno";
  public static final String TAG_NAME_UTILIZZO = "Utilizzo";
  public static final String TAG_NAME_SUPERFICIE = "Superficie";
  public static final String TAG_NAME_QUINTALIHA = "QuintaliHa";
  public static final String TAG_NAME_TOTQLIORD = "TotQliOrd";
  public static final String TAG_NAME_PREZZOQLEORD = "PrezzoQleOrd";
  public static final String TAG_NAME_TOTEUROORD = "TotEuroOrd";
  public static final String TAG_NAME_TOTQLIEFF = "TotQliEff";
  public static final String TAG_NAME_PREZZOQLEEFF = "PrezzoQleEff";
  public static final String TAG_NAME_TOTEUROEFF = "TotEuroEff";
  public static final String TAG_NAME_DANNO = "Danno";
  
  
  
  public static final String TAG_NAME_SEZ_NULLA = "SezNulla";
  public static final String TAG_NAME_VISIBILITY_SEZ_NULLA             = "VisibilitySezNulla";

  @Override
	public void writeFragment(XMLStreamWriter writer, long idBandoOggetto, IStampeEJB stampeEJB) throws Exception
	{
		
		writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI);		//TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI
			writeTag(writer, TAG_NAME_SEZIONE_VUOTA, "false");
			writeVisibility(writer, true);
			writeTag(writer, TAG_NAME_TITOLO_SEZIONE_COLTURE_AZIENDALI, "Quadro - Colture Aziendali");
			
			writer.writeStartElement(TAG_NAME_SEZ_DATI_COLTURE);						//TAG_NAME_SEZ_DATI_COLTURE
				writeTag(writer, TAG_NAME_VISIBILITY_DATI,"true");				
				writer.writeStartElement(TAG_NAME_SEZ_RIEPILOGO);						//TAG_NAME_SEZ_RIEPILOGO
					writeTag(writer,TAG_NAME_TOT_SUP,"");
					writeTag(writer,TAG_NAME_PLV_ORDINARIA,"");
					writeTag(writer,TAG_NAME_PLV_EFFETTIVA,"");
					writeTag(writer,TAG_NAME_PERC_DANNO,"");
				writer.writeEndElement();												//TAG_NAME_SEZ_RIEPILOGO
				
				writer.writeStartElement(TAG_NAME_SEZ_ELENCO_COLTURE);						//TAG_NAME_SEZ_ELENCO_COLTURE
					writer.writeStartElement(TAG_NAME_TAB_COLTURE_AZIENDALI);					//TAG_NAME_SEZ_ELENCO_COLTURE
						writer.writeStartElement(TAG_NAME_RIGA_COLTURE_AZIENDALI);			//TAG_NAME_RIGA_COLTURE_AZIENDALI
							writeTag(writer, TAG_NAME_UBICAZIONETERRENO, "");
							writeTag(writer, TAG_NAME_UTILIZZO, "");
							writeTag(writer, TAG_NAME_SUPERFICIE, "");
							writeTag(writer, TAG_NAME_QUINTALIHA, "");
							writeTag(writer, TAG_NAME_TOTQLIORD, "");
							writeTag(writer, TAG_NAME_PREZZOQLEORD, "");
							writeTag(writer, TAG_NAME_TOTEUROORD, "");
							writeTag(writer, TAG_NAME_TOTQLIEFF, "");
							writeTag(writer, TAG_NAME_PREZZOQLEEFF, "");
							writeTag(writer, TAG_NAME_TOTEUROEFF, "");
							writeTag(writer, TAG_NAME_DANNO, "");
						writer.writeEndElement();											//TAG_NAME_RIGA_COLTURE_AZIENDALI
					writer.writeEndElement();													//TAG_NAME_TAB_COLTURE_AZIENDALI
				writer.writeEndElement();												//TAG_NAME_SEZ_ELENCO_COLTURE
				writer.writeStartElement(TAG_NAME_SEZ_NULLA);								//TAG_NAME_SEZ_NULLA
					writeTag(writer,TAG_NAME_VISIBILITY_SEZ_NULLA,"false");
				writer.writeEndElement();													//TAG_NAME_SEZ_NULLA
			
			writer.writeEndElement();													//TAG_NAME_SEZ_DATI_COLTURE
		writer.writeEndElement(); 													//TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI
	}
}
