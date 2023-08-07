package it.csi.nembo.nembopratiche.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nembopratiche.business.IQuadroEJB;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.nembo.nembopratiche.util.NemboConstants;
import it.csi.nembo.nembopratiche.util.NemboUtils;

public class PreavvisoDiRigettoTotale extends Stampa
{
  public static final String ROOT_TAG = "Domanda";
  
  private String cuNameRef = null;
  
  public PreavvisoDiRigettoTotale() {
		super();
	  }
  
  public PreavvisoDiRigettoTotale(String cuNameRef) {
	super();
	this.cuNameRef = cuNameRef;
  }

@Override
  public byte[] genera(long idProcedimentoOggetto, String cuName) throws Exception
  {
	if(cuNameRef != null)
	{
		cuName = cuNameRef;
	}
	else if (!cuName.endsWith("-"+NemboConstants.ESITO.TIPO.NEGATIVO))
    {
      cuName+="-"+NemboConstants.ESITO.TIPO.NEGATIVO;
    }
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    IQuadroEJB quadroEJB = NemboUtils.APPLICATION.getEjbQuadro();
    generaXML(writer, idProcedimentoOggetto, quadroEJB, cuName);
    //return callModol(xmlOutputStream.toByteArray());
    return null;
  }

  protected void generaXML(XMLStreamWriter writer, long idProcedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception
  {
	if(cuNameRef != null)
	{
		cuName = cuNameRef;
	}
    ProcedimentoOggetto procedimentoOggetto = quadroEJB.getProcedimentoOggetto(idProcedimentoOggetto);
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    writeFragment("HEADER_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("GLOBAL", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("AMMISSIONE_FINANZIAMENTO_INTERVENTI", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("FIRMA_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB, procedimentoOggetto, cuName);
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "preavvisoRigettoTotale";
  }

  protected String getCodiceModello()
  {
    return "preavvisoRigettoTotale";
  }

  protected String getRifAdobe()
  {
    return "/psr20/rp-01/psrpratiche/templates/preavvisoRigettoTotale.xdp";
  }

  @Override
  public String getDefaultFileName()
  {
    return "Preavviso rigetto.pdf";
  }
}
