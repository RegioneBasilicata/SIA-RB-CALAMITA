package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import it.csi.nembo.nemboconf.util.NemboUtils;

public abstract class MyInputGroup extends MyBodyTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5602058023177228254L;
  private String            leftAddOnHtml    = null;
  private String            rightAddOnHtml   = null;
  private String            groupCssClass    = null;

  public MyInputGroup()
  {
    super();
  }

  public MyInputGroup(String additionalCssClass)
  {
    super(additionalCssClass);
  }

  public void addAddOn(boolean left, String html)
  {
    if (left)
    {
      leftAddOnHtml = html;
    }
    else
    {
      rightAddOnHtml = html;
    }
  }

  @Override
  protected void writeCustomTag(StringBuilder sb, String errorMessage)
      throws Exception
  {
    boolean isInputGroup = leftAddOnHtml != null || rightAddOnHtml != null;
    if (isInputGroup)
    {
      sb.append("<div");
      if (groupCssClass != null)
      {
        if (errorMessage != null)
        {
          TAG_UTIL.addAttribute(sb, "class", NemboUtils.STRING.concat(" ",
              CSS_ERROR_CLASSES + " input-group", groupCssClass));
          addErrorAttributes(sb, errorMessage);
        }
        else
        {
          TAG_UTIL.addAttribute(sb, "class",
              NemboUtils.STRING.concat(" ", "input-group", groupCssClass));
        }
      }
      else
      {
        if (errorMessage != null)
        {
          TAG_UTIL.addAttribute(sb, "class",
              CSS_ERROR_CLASSES + " input-group");
          addErrorAttributes(sb, errorMessage);
        }
        else
        {
          TAG_UTIL.addAttribute(sb, "class", "input-group");
        }
      }
      sb.append(">");
      if (leftAddOnHtml != null)
      {
        sb.append(leftAddOnHtml);
      }
    }
    else
    {
      if (errorMessage != null)
      {
        sb.append("<div");
        TAG_UTIL.addAttribute(sb, "class", CSS_ERROR_CLASSES);
        addErrorAttributes(sb, errorMessage);
        sb.append(">");
      }
    }
    writeCustomTag(sb, errorMessage, isInputGroup);
    if (isInputGroup)
    {
      if (rightAddOnHtml != null)
      {
        sb.append(rightAddOnHtml);
      }
      sb.append("</div>");
    }
    else
    {
      if (errorMessage != null)
      {
        sb.append("</div>");
      }
    }
    leftAddOnHtml = null;
    rightAddOnHtml = null;
  }

  protected abstract void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception;

  public String getGroupCssClass()
  {
    return groupCssClass;
  }

  public void setGroupCssClass(String groupCssClass)
  {
    this.groupCssClass = groupCssClass;
  }
}
