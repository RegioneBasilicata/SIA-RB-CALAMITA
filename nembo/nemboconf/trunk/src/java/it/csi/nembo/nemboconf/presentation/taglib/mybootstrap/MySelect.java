package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class MySelect extends MyInputGroup
{
  /** serialVersionUID */
  private static final long       serialVersionUID   = -5412221111769893273L;
  private HashMap<String, String> insertedValuesMap  = null;

  private String[]                selectedValues;
  protected int                   size               = 0;
  private Object                  list;
  private String                  valueProperty;
  private String                  textProperty;
  private Object                  headerKey          = "";
  private String                  headerValue        = " -- selezionare --";
  private boolean                 header             = true;
  private boolean                 multiple           = false;
  private boolean                 disabled           = false;
  private boolean                 disabledOptions    = false;
  private boolean                 readonly           = false;
  private boolean                 viewTitle          = false;
  private boolean                 viewOptionsTitle   = false;
  private boolean                 forceRequestValues = false;
  protected Boolean               preferRequestValues;
  private boolean                 visible            = true;

  public MySelect()
  {
    super("form-control");
  }

  protected void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    insertedValuesMap = new HashMap<String, String>();
    sb.append("<select");
    addBaseAttributes(sb, errorMessage != null);

    if (viewTitle)
    {
      String substring = "form-control";
      String replacement = "form-control showTitleSelect";
      int position = sb.lastIndexOf(substring);
      sb.replace(position, position + substring.length(), replacement);
    }

    if (multiple)
      sb.append(" multiple ");
    if (disabled)
      sb.append(" disabled=\"disabled\" ");
    if (readonly)
      sb.append(" readonly=\"readonly\" ");
    if (!visible)
      sb.append(" style=\"display:none\" ");
    if (size > 0)
      sb.append(" size=\"" + size + "\" ");
    sb.append(">");
    writeOptions(sb);
    sb.append("</select>");
  }

  protected void writeOptions(StringBuilder sb) throws SecurityException,
      IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException
  {
    String valueMethod = "getId";
    String textMethod = "getDescrizione";
    if (!GenericValidator.isBlankOrNull(valueProperty))
    {
      valueMethod = "get" + Character.toUpperCase(valueProperty.charAt(0))
          + valueProperty.substring(1);
    }
    if (!GenericValidator.isBlankOrNull(textProperty))
    {
      textMethod = "get" + Character.toUpperCase(textProperty.charAt(0))
          + textProperty.substring(1);
    }
    if (header)
    {
      writeOption(sb, NemboUtils.STRING.checkNull(headerKey), headerValue,
          null);
    }
    String[] finalSelectedValues = selectedValues;
    if (preferRequestValues != null && preferRequestValues.booleanValue())
    {
      finalSelectedValues = this.pageContext.getRequest()
          .getParameterValues(name);
    }

    if (list != null)
    {
      if (list instanceof Iterable)
      {
        Iterator<?> iterator = ((Iterable<?>) list).iterator();
        while (iterator.hasNext())
        {
          processOption(sb, iterator.next(), valueMethod, textMethod,
              finalSelectedValues);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          for (Object option : array)
          {
            processOption(sb, option, valueMethod, textMethod,
                finalSelectedValues);
          }
        }
      }
    }

    if (forceRequestValues)
    {
      // Forzo inserimento di options che magari non sono presenti nella mia
      // list appena elaborata
      if (finalSelectedValues != null)
      {
        for (String val : finalSelectedValues)
        {
          if (!insertedValuesMap.containsKey(val))
          {
            writeOption(sb, val, val, finalSelectedValues);
          }
        }
      }
    }
  }

  private void processOption(StringBuilder sb, Object option,
      String valueMethod, String textMethod, String[] finalSelectedValue)
      throws SecurityException,
      IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(option, valueMethod);
    String text = getProperty(option, textMethod);
    insertedValuesMap.put(value, text);
    writeOption(sb, value, text, finalSelectedValue);
  }

  private String getProperty(Object option, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = option.getClass().getMethod(methodName);
    Object value = method.invoke(option);
    return value == null ? "" : value.toString();
  }

  public void writeOption(StringBuilder sb, String value, String text,
      String[] finalSelectedValue)
  {

    sb.append("<option value=\"").append(StringEscapeUtils.escapeHtml4(value))
        .append("\"");
    if (finalSelectedValue != null
        && NemboUtils.ARRAY.contains(finalSelectedValue, value))
    {
      sb.append(" selected=\"selected\" ");
    }
    if (viewOptionsTitle)
    {
      sb.append(" title=\"" + StringEscapeUtils.escapeHtml4(text) + "\" ");
    }
    if (disabledOptions)
    {
      sb.append(" disabled ");
    }
    sb.append(">").append(StringEscapeUtils.escapeHtml4(text))
        .append("</option>");
  }

  public String[] getSelectedValues()
  {

    return selectedValues;
  }

  public void setSelectedValue(Object selectedValue)
  {
    if (selectedValue == null)
    {
      this.selectedValues = null;
    }
    else
    {
      if (selectedValue instanceof String[])
      {
        this.selectedValues = (String[]) selectedValue;
      }
      else
      {
        if (selectedValue instanceof String)
        {
          this.selectedValues = new String[]
          { (String) selectedValue };
        }
        else
        {
          if (selectedValue instanceof List)
          {
            List<?> list = (List<?>) selectedValue;
            int size = list.size();
            this.selectedValues = new String[size];
            for (int i = 0; i < size; ++i)
            {
              Object obj = list.get(i);
              selectedValues[i] = obj == null ? null : obj.toString();
            }
          }
          else
          {
            this.selectedValues = new String[]
            { selectedValue.toString() };
          }
        }
      }
    }
  }

  public int getSize()
  {
    return size;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

  public Object getList()
  {
    return list;
  }

  public void setList(Object list)
  {
    this.list = list;
  }

  public String getValueProperty()
  {
    return valueProperty;
  }

  public void setValueProperty(String valueProperty)
  {
    this.valueProperty = valueProperty;
  }

  public String getTextProperty()
  {
    return textProperty;
  }

  public void setTextProperty(String textProperty)
  {
    this.textProperty = textProperty;
  }

  public Object getHeaderKey()
  {
    return headerKey;
  }

  public void setHeaderKey(Object headerKey)
  {
    this.headerKey = headerKey;
  }

  public String getHeaderValue()
  {
    return headerValue;
  }

  public void setHeaderValue(String headerValue)
  {
    this.headerValue = headerValue;
  }

  public boolean isHeader()
  {
    return header;
  }

  public void setHeader(boolean header)
  {
    this.header = header;
  }

  public boolean isMultiple()
  {
    return multiple;
  }

  public void setMultiple(boolean multiple)
  {
    this.multiple = multiple;
  }

  public boolean isDisabled()
  {
    return disabled;
  }

  public void setDisabled(boolean disabled)
  {
    this.disabled = disabled;
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public Boolean getPreferRequestValues()
  {
    return preferRequestValues;
  }

  public void setPreferRequestValues(Boolean preferRequestValues)
  {
    this.preferRequestValues = preferRequestValues;
  }

  public boolean isDisabledOptions()
  {
    return disabledOptions;
  }

  public void setDisabledOptions(boolean disabledOptions)
  {
    this.disabledOptions = disabledOptions;
  }

  public boolean isForceRequestValues()
  {
    return forceRequestValues;
  }

  public void setForceRequestValues(boolean forceRequestValues)
  {
    this.forceRequestValues = forceRequestValues;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  public boolean isViewTitle()
  {
    return viewTitle;
  }

  public void setViewTitle(boolean viewTitle)
  {
    this.viewTitle = viewTitle;
  }

  public boolean isViewOptionsTitle()
  {
    return viewOptionsTitle;
  }

  public void setViewOptionsTitle(boolean viewOptionsTitle)
  {
    this.viewOptionsTitle = viewOptionsTitle;
  }

}
