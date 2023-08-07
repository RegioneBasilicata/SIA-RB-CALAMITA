
package it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getListaMessaggiResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getListaMessaggiResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listaMessaggi" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}listaMessaggi" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getListaMessaggiResponse", propOrder =
{
    "listaMessaggi"
})
public class GetListaMessaggiResponse
    implements Serializable
{

  private final static long serialVersionUID = 1L;
  protected ListaMessaggi   listaMessaggi;

  /**
   * Recupera il valore della propriet� listaMessaggi.
   * 
   * @return possible object is {@link ListaMessaggi }
   * 
   */
  public ListaMessaggi getListaMessaggi()
  {
    return listaMessaggi;
  }

  /**
   * Imposta il valore della propriet� listaMessaggi.
   * 
   * @param value
   *          allowed object is {@link ListaMessaggi }
   * 
   */
  public void setListaMessaggi(ListaMessaggi value)
  {
    this.listaMessaggi = value;
  }

}
