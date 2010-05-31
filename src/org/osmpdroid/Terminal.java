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
  public int cash;
  public String lastActivity;
  public String lastPayment;
  public int bondsCount;
  public String balance;
  public int signalLevel;
  public String softVersion;
  public String printerModel;
  public String cashbinModel;
  public int bonds10count;
  public int bonds50count;
  public int bonds100count;
  public int bonds500count;
  public int bonds1000count;
  public int bonds5000count;
  public int bonds10000count;
  public String paysPerHour;
  public String agentId;
  public String agentName;
  
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
  
  public String id()
  {
    return(tid);
  }
}
