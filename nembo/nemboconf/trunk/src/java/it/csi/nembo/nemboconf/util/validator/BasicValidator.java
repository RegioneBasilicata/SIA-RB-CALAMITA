package it.csi.nembo.nemboconf.util.validator;

import java.util.HashMap;

public class BasicValidator extends HashMap<String, String>
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3770166489356520835L;

  public void addError(String errorName, String errorMessage)
  {
    put(errorName, errorMessage);
  }

  public String removeError(String errorName)
  {
    return remove(errorName);
  }
}
