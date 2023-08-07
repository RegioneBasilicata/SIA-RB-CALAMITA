package it.csi.nembo.nemboconf.presentation.taglib.bootstrap;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringEscapeUtils;

public class Radio extends SimpleBoostrapTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 1080560085783747016L;
  private boolean           checked          = false;
  private boolean           readonly         = false;

  @Override
  protected void writeCustomTagElement(StringBuilder sb)
      throws SecurityException, IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException,
      InvocationTargetException
  {
    setFormControl(false);
    writeCheckBox(sb);
  }

  public void writeCheckBox(StringBuilder sb)
  {
    String error = getError(getErrors());
    if (error != null)
    {
      String tmp = sb.toString().replace("has-error",
          "has-error has-error-checkbox");
      sb.delete(0, sb.toString().length());
      sb.append(tmp);
    }

    sb.append(" <div class=\"checkbox\"> ");
    sb.append(
        "<label style=\"margin-right:1em\" ><input type=\"radio\" value=\"")
        .append(StringEscapeUtils.escapeHtml4(value)).append("\"");
    writeDefaultAttributes(sb);

    if (checked)
      sb.append(" checked=\"checked\" ");

    if (disabled)
      sb.append(" disabled=\"disabled\" ");

    if (readonly)
      sb.append(" readonly=\"readonly\" ");

    sb.append(" />");

    sb.append("</label>");
    sb.append(" </div> ");
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

}
