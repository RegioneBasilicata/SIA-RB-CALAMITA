package it.csi.nembo.nemboconf.presentation.taglib.mybootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class MyCheckBoxList extends MyInputGroup
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5602058023177228254L;
  private Object            list;
  private String            valueProperty;
  private String            textProperty;
  private String            checkedProperty;
  private Boolean           inline;

  @Override
  protected void writeCustomTag(StringBuilder sb, String errorMessage,
      boolean wrappedInAGroup) throws Exception
  {
    writeElements(sb);
  }

  @Override
  protected void addCssClassAttribute(StringBuilder sb, boolean errror)
  {
    addAttribute(sb, "class",
        NemboUtils.STRING.concat(" ", cssClass, "form-control"));
  }

  protected void writeElements(StringBuilder sb) throws SecurityException,
      IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException
  {
    String valueMethod = "getValue";
    String textMethod = "getLabel";
    String checkedMethod = "isChecked";
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
    if (!GenericValidator.isBlankOrNull(checkedProperty))
    {
      textMethod = "is" + Character.toUpperCase(checkedProperty.charAt(0))
          + textProperty.substring(1);
    }
    if (list != null)
    {
      if (list instanceof Iterable)
      {
        Iterator<?> iterator = ((Iterable<?>) list).iterator();
        while (iterator.hasNext())
        {
          processElement(sb, iterator.next(), valueMethod, textMethod,
              checkedMethod);
        }
      }
      else
      {
        if (list.getClass().isArray())
        {
          Object[] array = (Object[]) list;
          for (Object element : array)
          {
            processElement(sb, element, valueMethod, textMethod, checkedMethod);
          }
        }
      }
    }
  }

  private void processElement(StringBuilder sb, Object element,
      String valueMethod, String textMethod, String checkedMethod)
      throws SecurityException,
      IllegalArgumentException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    String value = getProperty(element, valueMethod);
    String text = getProperty(element, textMethod);
    boolean isChecked = false;
    if (!GenericValidator.isBlankOrNull(checkedMethod))
    {
      String checked = getProperty(element, checkedMethod);
      isChecked = checked != null && Boolean.valueOf(checked);
    }
    writeElement(sb, value, text, isChecked);
  }

  private String getProperty(Object Element, String methodName)
      throws SecurityException, NoSuchMethodException, IllegalArgumentException,
      IllegalAccessException, InvocationTargetException
  {
    Method method = Element.getClass().getMethod(methodName);
    Object value = method.invoke(Element);
    return value == null ? "" : value.toString();
  }

  public void writeElement(StringBuilder sb, String value, String text,
      boolean checked)
  {
    if (isInlineElement())
    {
      writeInlineElement(sb, value, text, checked);
    }
    else
    {
      writeNonInlineElement(sb, value, text, checked);
    }
  }

  public void writeInlineElement(StringBuilder sb, String value, String text,
      boolean checked)
  {
    sb.append("<label");
    addAttribute(sb, "class", "checkbox-inline");
    addDisableAttributeIfSet(sb, false);
    sb.append("><input");
    addAttribute(sb, "type", "checkbox");
    addAttribute(sb, "value", escapeHtml(value));
    if (checked)
    {
      addAttribute(sb, "checked", "checked");
    }
    addDisableAttributeIfSet(sb, false);
    addOnClickAttribute(sb, false);
    sb.append(">");
    sb.append(escapeHtml(text));
    sb.append("</label>");
  }

  public void writeNonInlineElement(StringBuilder sb, String value, String text,
      boolean checked)
  {
    sb.append("<div");
    addAttribute(sb, "class", "checkbox");
    addDisableAttributeIfSet(sb, false);
    sb.append(">");
    sb.append("<label><input");
    addAttribute(sb, "type", "checkbox");
    addAttribute(sb, "value", escapeHtml(value));
    if (checked)
    {
      addAttribute(sb, "checked", "checked");
    }
    addDisableAttributeIfSet(sb, false);
    addOnClickAttribute(sb, false);
    sb.append(">");
    sb.append(escapeHtml(text));
    sb.append("</label></div>");
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

  public String getCheckedProperty()
  {
    return checkedProperty;
  }

  public void setCheckedProperty(String checkedProperty)
  {
    this.checkedProperty = checkedProperty;
  }

  public Boolean getInline()
  {
    return inline;
  }

  public void setInline(Boolean inline)
  {
    this.inline = inline;
  }

  public boolean isInlineElement()
  {
    return inline != null && inline.booleanValue();
  }

}
