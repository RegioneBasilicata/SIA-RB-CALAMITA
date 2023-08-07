
package it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per messaggio complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="messaggio">
 *   &lt;complexContent>
 *     &lt;extension base="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}messaggioBase">
 *       &lt;sequence>
 *         &lt;element name="conAllegati" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messaggio", propOrder =
{
    "conAllegati"
})
public class Messaggio
    extends MessaggioBase
    implements Serializable
{

  private final static long serialVersionUID = 1L;
  protected boolean         conAllegati;

  /**
   * Recupera il valore della proprietà conAllegati.
   * 
   */
  public boolean isConAllegati()
  {
    return conAllegati;
  }

  /**
   * Imposta il valore della proprietà conAllegati.
   * 
   */
  public void setConAllegati(boolean value)
  {
    this.conAllegati = value;
  }

}
