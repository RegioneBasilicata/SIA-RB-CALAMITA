package it.csi.nembo.nemboconf.dto.reportistica;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class RowsReportVO extends HashMap<String, List<CellReportVO>>
    implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  public void addRowReport(List<CellReportVO> rows)
  {
    this.put("c", rows);
  }
}
