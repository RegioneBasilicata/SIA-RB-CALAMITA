package it.csi.nembo.nemboconf.util;

import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.exception.InternalException;

public class ExceptionUtils
{

  public ExceptionUtils()
  {
  }

  public InternalException processDAOException(Throwable t, String query,
      LogParameter parameters[], LogVariable variables)
  {
    return null;
  }

}
