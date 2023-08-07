
package it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getAllegato complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getAllegato">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAllegato" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllegato", propOrder =
{
    "idAllegato"
})
public class GetAllegato
    implements Serializable
{

  private final static long serialVersionUID = 1L;
  protected long            idAllegato;

  /**
   * Recupera il valore della propriet� idAllegato.
   * 
   */
  public long getIdAllegato()
  {
    return idAllegato;
  }

  /**
   * Imposta il valore della propriet� idAllegato.
   * 
   */
  public void setIdAllegato(long value)
  {
    this.idAllegato = value;
  }

}
