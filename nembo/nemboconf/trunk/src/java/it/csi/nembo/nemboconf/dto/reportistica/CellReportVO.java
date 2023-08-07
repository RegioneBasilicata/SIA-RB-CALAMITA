package it.csi.nembo.nemboconf.dto.reportistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/*
 * Each cell in the table is described by an object with the following properties:

v [Optional] The cell value. The data type should match the column data type. If the cell is null, 
  the v property should be null, though it can still have f and p properties.
f [Optional] A string version of the v value, formatted for display. Typically the values will match, though they do not need to, 
   so if you specify Date(2008, 0, 1) for v, you should specify "January 1, 2008" or some such string for this property. This value is 
   not checked against the v value. The visualization will not use this value for calculation, only as a label for display. If omitted, a 
   string version of v will be automatically generated using the default formatter. The f values can be modified using your own formatter, or 
   set with setFormattedValue() or setCell(), or retrieved with getFormattedValue().
p [Optional] An object that is a map of custom values applied to the cell. These values can be of any JavaScript type. If your visualization 
  supports any cell-level properties, it will describe them. These properties can be retrieved by the getProperty() and getProperties() methods. 
  Example: p:{style: 'border: 1px solid green;'}.
Cells in the row array should be in the same order as their column descriptions in cols. To indicate a null cell, you can specify null, leave a 
blank for a cell in an array, or omit trailing array members. So, to indicate a row with null for the first two cells, you could specify
[ , , {cell_val}] or [null, null, {cell_val}].
*/

import it.csi.nembo.nemboconf.util.NemboUtils;

public class CellReportVO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private Object            v;
  private String            f;
  private String            p;

  public Object getValueFormatted()
  {
    if (v instanceof BigDecimal)
    {
      BigDecimal tmp = (BigDecimal) v;
      return NemboUtils.FORMAT.formatGenericNumber(tmp, tmp.scale(),
          tmp.scale() > 0);
    }
    else
      if (v instanceof Date)
      {
        return NemboUtils.DATE.formatDate((Date) v);
      }

    return v;
  }

  public Object getV()
  {
    return v;
  }

  public void setV(Object v)
  {
    this.v = v;
  }

  public String getF()
  {
    return f;
  }

  public void setF(String f)
  {
    this.f = f;
  }

  public String getP()
  {
    return p;
  }

  public void setP(String p)
  {
    this.p = p;
  }

}
