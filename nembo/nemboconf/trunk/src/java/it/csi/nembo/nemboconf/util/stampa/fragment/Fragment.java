package it.csi.nembo.nemboconf.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.business.IStampeEJB;

public abstract class Fragment
{
  public abstract void writeFragment(XMLStreamWriter writer,
      long idBandoOggetto, IStampeEJB stampeEJB) throws Exception;

  protected void writeTag(XMLStreamWriter writer, String name, String value)
      throws XMLStreamException
  {
    writeTag(writer, name, value, false);
  }

  public void writeFragmentTesti(XMLStreamWriter writer, IStampeEJB stampeEJB,
      Long idBandoOggetto, String cuName) throws Exception
  {
  }

  protected void writeVisibility(XMLStreamWriter writer, boolean visibility)
      throws XMLStreamException
  {
    writeTag(writer, "Visibility", String.valueOf(visibility));
  }

  protected void writeTag(XMLStreamWriter writer, String name, String value,
      boolean blankAsNull) throws XMLStreamException
  {
    if (value == null)
    {
      if (blankAsNull)
      {
        value = "";
      }
      else
      {
        return;
      }
    }
    writer.writeStartElement(name);
    try
    {
      if (value != null)
      {
        value = value.replace(new Character((char) 128).toString(), "\u20ac") // rimpiazzo
                                                                              // euro
                                                                              // con
                                                                              // €";
            .replace(new Character((char) 191).toString(), "\u20ac"); // rimpiazzo
                                                                      // euro
                                                                      // con €";
      }
      writer.writeCharacters(value);
    }
    catch (Exception e)
    {
      throw new XMLStreamException(e);
    }
    writer.writeEndElement();
  }

  protected void writeTagIfNotEmpty(XMLStreamWriter writer, String name,
      String value) throws XMLStreamException
  {
    if (GenericValidator.isBlankOrNull(value))
    {
      return;
    }
    writer.writeStartElement(name);
    try
    {
      writer.writeCharacters(value);
    }
    catch (Exception e)
    {
      throw new XMLStreamException(e);
    }
    writer.writeEndElement();
  }

  protected void writeCDataTag(XMLStreamWriter writer, String name,
      String value) throws XMLStreamException
  {
    writeCDataTag(writer, name, value, false);
  }

  protected void writeCDataTag(XMLStreamWriter writer, String name,
      String value, boolean blankAsNull) throws XMLStreamException
  {
    if (value == null)
    {
      if (blankAsNull)
      {
        value = "";
      }
      else
      {
        return;
      }
    }
    writer.writeStartElement(name);
    writer.writeCData(value);
    writer.writeEndElement();
  }

}
