
package it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getAllegatoResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getAllegatoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allegato" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllegatoResponse", propOrder =
{
    "allegato"
})
public class GetAllegatoResponse
    implements Serializable
{

  private final static long serialVersionUID = 1L;
  protected byte[]          allegato;

  /**
   * Recupera il valore della proprietÓ allegato.
   * 
   * @return possible object is byte[]
   */
  public byte[] getAllegato()
  {
    return allegato;
  }

  /**
   * Imposta il valore della proprietÓ allegato.
   * 
   * @param value
   *          allowed object is byte[]
   */
  public void setAllegato(byte[] value)
  {
    this.allegato = value;
  }

}
