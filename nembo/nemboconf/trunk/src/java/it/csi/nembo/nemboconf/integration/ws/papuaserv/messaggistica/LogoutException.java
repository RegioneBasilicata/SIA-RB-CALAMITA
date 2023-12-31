
package it.csi.nembo.nemboconf.integration.ws.papuaserv.messaggistica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per LogoutException complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="LogoutException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nestedExcClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stackTraceMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nestedExcMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogoutException", propOrder =
{
    "nestedExcClassName",
    "stackTraceMessage",
    "nestedExcMsg"
})
public class LogoutException
    implements Serializable
{

  private final static long serialVersionUID = 1L;
  @XmlElement(required = true, nillable = true)
  protected String          nestedExcClassName;
  @XmlElement(required = true, nillable = true)
  protected String          stackTraceMessage;
  @XmlElement(required = true, nillable = true)
  protected String          nestedExcMsg;

  /**
   * Recupera il valore della proprietÓ nestedExcClassName.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNestedExcClassName()
  {
    return nestedExcClassName;
  }

  /**
   * Imposta il valore della proprietÓ nestedExcClassName.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNestedExcClassName(String value)
  {
    this.nestedExcClassName = value;
  }

  /**
   * Recupera il valore della proprietÓ stackTraceMessage.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getStackTraceMessage()
  {
    return stackTraceMessage;
  }

  /**
   * Imposta il valore della proprietÓ stackTraceMessage.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setStackTraceMessage(String value)
  {
    this.stackTraceMessage = value;
  }

  /**
   * Recupera il valore della proprietÓ nestedExcMsg.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getNestedExcMsg()
  {
    return nestedExcMsg;
  }

  /**
   * Imposta il valore della proprietÓ nestedExcMsg.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setNestedExcMsg(String value)
  {
    this.nestedExcMsg = value;
  }

}
