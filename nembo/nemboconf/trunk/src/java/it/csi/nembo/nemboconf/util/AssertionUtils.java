package it.csi.nembo.nemboconf.util;

import java.util.List;

import it.csi.nembo.nemboconf.exception.NemboAssertionException;

/**
 * Classe astratta per le funzioni di utilit� sulle asserzioni (gestione
 * custom). La classe � abstract perch� non deve essere usata direttamente ma
 * solo dalla sua implementazione nella costante Utils.STRING
 * 
 * @author Stefano Einaudi (Matr. 70399)
 * 
 */
public class AssertionUtils
{
  public void notNull(Object obj, String name)
      throws NemboAssertionException
  {
    if (obj == null)
    {
      throw new NemboAssertionException("L'oggetto " + name + " � null");
    }
  }

  public void notNull(List<?> obj, String name)
      throws NemboAssertionException
  {
    if (obj == null || obj.size() == 0)
    {
      throw new NemboAssertionException(
          "La lista di oggetti " + name + " � vuota o null");
    }
  }
}
