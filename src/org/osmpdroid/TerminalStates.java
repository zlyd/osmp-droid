package org.osmpdroid;

import java.util.HashMap;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TerminalStates extends DefaultHandler
{
  private final int TAG_NONE   = 0;
  private final int TAG_EXTRA  = 1;
  private final int TAG_RESULT = 2;
  
  private final int EXTRA_UNKNOWN  = 0;
  private final int EXTRA_BALANCE  = 1;
  private final int EXTRA_OVERDRAFT  = 2;
  
  protected HashMap<String,Terminal> terminals;
  protected int status;
  private String balance;
  private String overdraft;
  
  private int tagState;
  private int extraState;
  private StringBuilder text;
  
  public TerminalStates()
  {
    terminals = new HashMap<String,Terminal>();
    text = new StringBuilder();
  }
  
  @Override
  public void startDocument()
  {
    tagState = TAG_NONE;
    text.delete(0, text.length());
  }
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if(localName.compareToIgnoreCase("term")==0)
    {
      String tid = attributes.getValue("tid");
      String address = attributes.getValue("addr");
      
      Terminal terminal;
      if(terminals.containsKey(tid))
        terminal = terminals.get(tid);
      else
      {
        terminal = new Terminal(tid,address);
        terminals.put(tid, terminal);
      }
      
      String state = attributes.getValue("rs");
      if (state!=null)
        terminal.state = Integer.parseInt(state);
      else
        terminal.state = Terminal.STATE_ERROR;
      // состояние принтера
      state = attributes.getValue("rp");
      if(state!=null)
        terminal.printer_state = state;
      else
        terminal.printer_state = "none";
      // состояние купироприемника
      state = attributes.getValue("rc");
      if(state!=null)
        terminal.cashbin_state = state;
      else
        terminal.cashbin_state = "none";
      // сумма
      String sum = attributes.getValue("cosum");
      if(sum!=null)
        terminal.cosum = sum;
      else
        terminal.cosum = "unknown";
      // последняя активность
      String lastActivity = attributes.getValue("lat");
      if(lastActivity!=null)
        terminal.lastActivity = lastActivity;
      else
        terminal.lastActivity = "unknown";
      // последний платеж
      String lastPayment = attributes.getValue("lpd");
      if(lastPayment!=null)
        terminal.lastPayment = lastPayment;
      else
        terminal.lastPayment = "unknown";
    }
    else if(localName.compareToIgnoreCase("result-code")==0)
      tagState = TAG_RESULT;
    else if(localName.compareToIgnoreCase("extra")==0)
    {
      tagState = TAG_EXTRA;
      String extra_name = attributes.getValue("name");
      if(extra_name.compareToIgnoreCase("balance")==0)
        extraState = EXTRA_BALANCE;
      else if(extra_name.compareToIgnoreCase("overdraft")==0)
        extraState = EXTRA_OVERDRAFT;
      else
        extraState = EXTRA_UNKNOWN;
    }
    else
      tagState = TAG_NONE;
    // не допускаем вложенности
    text.delete(0, text.length());
  }
  
  @Override
  public void characters(char[] ch, int start, int length)
  {
    text.append(ch,start,length);
  }
  
  @Override
  public void endElement(String uri, String localName, String qName)
  {
    switch(tagState)
    {
      case TAG_RESULT:
        status = Integer.parseInt(text.toString());
        break;
      case TAG_EXTRA:
        if(extraState == EXTRA_BALANCE)
          balance = text.toString();
        else if(extraState == EXTRA_OVERDRAFT)
          overdraft = text.toString();
        break;
    }
    tagState = TAG_NONE;
  }
  
  public int Count()
  {
    Set<String> keys = terminals.keySet();
    return (keys.toArray().length);
  }
  
  public String Balance()
  {
    return(balance);
  }
  
  public String Overdraft()
  {
    return(overdraft);
  }
  
  public Terminal Terminal(int index)
  {
    Set<String> keys = terminals.keySet();
    if (keys.size()<=index)
      return (null);
    return (terminals.get(keys.toArray()[index]));
  }
}
