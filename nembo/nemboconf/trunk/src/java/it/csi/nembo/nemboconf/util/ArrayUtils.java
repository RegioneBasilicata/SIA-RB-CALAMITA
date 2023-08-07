package it.csi.nembo.nemboconf.util;

import java.util.ArrayList;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

public class ArrayUtils
{
  public long[] toLong(String[] array) throws InternalUnexpectedException
  {
    return toLong(array, false);
  }

  public long[] toLong(String[] array, boolean zeroOnError)
      throws InternalUnexpectedException
  {
    if (array == null)
    {
      return null;
    }
    int len = array == null ? 0 : array.length;
    long newArray[] = new long[len];
    for (int i = 0; i < len; ++i)
    {
      try
      {
        newArray[i] = new Long(array[i]);
      }
      catch (Exception e)
      {
        // Se il parametro zeroOnError è true allora non faccio nulla in caso di
        // errore, il valore i-esimo dell'array sarà quello di
        // default ossia 0
        if (!zeroOnError)
        {
          // altrimenti genero un'eccezione
          throw new InternalUnexpectedException(e, new LogParameter[]
          { new LogParameter("array", array) });
        }
      }
    }
    return newArray;
  }
  
  public List<Integer> toListOfInteger(String[] array)
  {
    int len = array == null ? 0 : array.length;
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < len; ++i)
    {
      String value = array[i];
      if (value == null)
      {
        list.add(null);
      }
      else
      {
        list.add(new Integer(value));
      }
    }
    return list;
  }

  public boolean contains(Object[] array, Object value)
  {
    boolean found = false;
    if (array != null)
    {
      for (Object obj : array)
      {
        if (value == obj) // Principalmente null == null
        {
          found = true;
          break;
        }
        if (obj != null && obj.equals(value))
        {
          found = true;
          break;
        }
      }
    }
    return found;
  }
}