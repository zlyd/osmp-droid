package org.osmpdroid;

public class Terminal
{
  public final static int STATE_OK = 0;
  public final static int STATE_WARRNING = 2;
  public final static int STATE_ERROR = 1;
  
  public int state;
  public String printer_state;
  public String cashbin_state;
  public String mobile_bill;
  public String lpd;
  public String cosum;
  public String lastActivity;
  public String lastPayment;
  
  protected String address;
  protected String tid;
  
  public Terminal(String id, String address)
  {
    this.tid = id;
    this.address = address;
  }
  
  public String Address() 
  {
    return address;
  }
}
