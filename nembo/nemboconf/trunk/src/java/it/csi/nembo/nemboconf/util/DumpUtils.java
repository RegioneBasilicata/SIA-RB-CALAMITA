package it.csi.nembo.nemboconf.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.dto.pianofinanziario.IterPianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;

public abstract class DumpUtils
{
  public static final int MAX_RECURSION = 5;

  public static String dump(Object obj, String name)
  {
    try
    {
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
      // XMLStreamWriter writer = new
      // IndentingXMLStreamWriter(factory.createXMLStreamWriter(xmlOutputStream));
      XMLStreamWriter writer = factory.createXMLStreamWriter(xmlOutputStream);
      writer.writeStartDocument();
      dump(0, writer, obj, name);
      writer.writeEndDocument();
      writer.close();
      return xmlOutputStream.toString();
    }
    catch (Exception e)
    {
      return "<" + name
          + ">Eccezione nella generazione dell'xml di dump dell'oggetto " + name
          + "<" + name + ">";
    }
  }

  public static void dump(int recursionDepth, XMLStreamWriter writer,
      Object obj, String name) throws XMLStreamException
  {
    if (recursionDepth > MAX_RECURSION)
    {
      writeMaxRecursionException(writer);
      return;
    }
    if (obj == null || obj instanceof ILoggable)
    {
      // Non incremento il recursionDepth in quanto sto scrivendo l'oggetto
      // corrente (forzato ad un altro tipo)
      dump(recursionDepth, writer, (ILoggable) obj, name);
    }
    else
    {
      Class<?> objClass = obj.getClass();
      if (isPrimitive(obj))
      {
        writePrimitive(writer, obj, name);
      }
      else
      {
        if (objClass.isArray())
        {
          int index = 0;
          Object[] array = (Object[]) obj;
          writer.writeStartElement(name);
          for (Object element : array)
          {
            dump(recursionDepth + 1, writer, element, name + (index++));
          }
          writer.writeEndElement();
        }
        else
        {
          if (obj instanceof Iterable)
          {
            int index = 0;
            Iterable<?> iterable = (Iterable<?>) obj;
            Iterator<?> iterator = iterable.iterator();
            writer.writeStartElement(name);
            while (iterator.hasNext())
            {
              dump(recursionDepth + 1, writer, iterator.next(),
                  name + (index++));
            }
            writer.writeEndElement();
          }
          else
          {
            String value = null;
            try
            {
              value = obj.toString();
            }
            catch (Exception e)
            {
              value = "toString() Error! Stacktrace:\n" + e.toString();
            }
            writePrimitive(writer, value, name);
          }
        }
      }
    }
  }

  public static String getStackTraceAsString(Throwable e)
  {

    String result = null;
    try
    {
      if (e != null)
      {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.flush();
        result = stringWriter.toString();
      }
    }
    catch (Exception t)
    {
      result = "ERRORE getStackTraceAsString()";
    }
    return result;
  }

  private static boolean isPrimitive(Object obj)
  {
    return obj instanceof Number || obj instanceof CharSequence
        || obj instanceof java.util.Date || obj instanceof Boolean;
  }

  private static void writeMaxRecursionException(XMLStreamWriter writer)
      throws XMLStreamException
  {
    writer.writeStartElement("MaxRecursionException");
    writer.writeEndElement();
  }

  public static void dump(int recursionDepth, XMLStreamWriter writer,
      ILoggable obj, String name) throws XMLStreamException
  {
    if (recursionDepth > MAX_RECURSION)
    {
      writeMaxRecursionException(writer);
      return;
    }
    writer.writeStartElement(name);
    if (obj == null)
    {
      writer.writeAttribute("null", "true");
    }
    else
    {
      for (Method method : getMethods(obj.getClass()))
      {
        Object value = getMethodResult(method, obj);
        dump(recursionDepth + 1, writer, value, method.getName().substring(3));
      }
    }
    writer.writeEndElement();
  }

  private static ArrayList<Method> getMethods(Class<?> objClass)
  {
    ArrayList<Method> list = new ArrayList<Method>();
    Method[] methods = objClass.getMethods();
    if (methods != null)
    {
      Class<?> parameters[] = null;
      for (Method method : methods)
      {
        parameters = method.getParameterTypes();
        int size = parameters == null ? 0 : parameters.length;
        if (size == 0)
        {
          String name = method.getName();
          if (name.startsWith("get") && name.length() > 3)
          {
            list.add(method);
          }
        }
      }
    }
    return list;
  }

  private static void writePrimitive(XMLStreamWriter writer, Object value,
      String name) throws XMLStreamException
  {
    writer.writeStartElement(name);
    try
    {
      if (value == null)
      {
        writer.writeCharacters("null");
      }
      else
      {
        writer.writeCharacters(value.toString());
      }
    }
    catch (Exception e)
    {
      writer.writeCharacters(e.toString());
    }
    writer.writeEndElement();
  }

  public static Object getMethodResult(Method m, Object obj)
  {
    try
    {
      return m.invoke(obj);
    }
    catch (Throwable e)
    {
      return "Reflection Error! Stacktrace:\n" + e.toString();
    }
  }

  public static String getExceptionStackTrace(Throwable cause)
  {
    if (cause == null)
    {
      return "ERROR: No StackTrace Available! See logs for detail";
    }
    StringBuilder sbStackTrace = new StringBuilder();
    int countRecursion = 0;
    while (cause != null && countRecursion <= MAX_RECURSION)
    {
      if (sbStackTrace.length() > 0)
      {
        sbStackTrace.append("\n\n Caused by:\n\n");
      }
      sbStackTrace.append(getStackTraceAsString(cause));
      countRecursion++;
      cause = cause.getCause();
    }

    return sbStackTrace.toString();
  }

  public static final void main(String[] args)
  {
    PianoFinanziarioDTO p = new PianoFinanziarioDTO();
    p.setIter(new ArrayList<IterPianoFinanziarioDTO>());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    p.getIter().add(new IterPianoFinanziarioDTO());
    System.err.println(dump(p, "p"));
  }
}
