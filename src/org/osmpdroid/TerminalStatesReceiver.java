package org.osmpdroid;

import org.xml.sax.helpers.DefaultHandler;

import android.os.AsyncTask;
import android.os.Bundle;

public class TerminalStatesReceiver extends AsyncTask<Bundle, Integer, Boolean>
{
  private DefaultHandler dataHandler;
  private ITerminalDataReceiver callback;
  private String login;
  private String password;
  private String terminal;
  
  public TerminalStatesReceiver(String login,
                                String password,
                                String terminal,
                                DefaultHandler handler,
                                ITerminalDataReceiver callback)
  {
    dataHandler = handler;
    this.callback = callback;
    
    this.login = login;
    this.password = password;
    this.terminal = terminal;
  }
  
  @Override
  protected Boolean doInBackground(Bundle... arg0)
  {
    return(Request.Get("http://xml1.osmp.ru/term2/xml.jsp",login,password,terminal,dataHandler));
  }

  @Override
  protected void onPostExecute(Boolean result)
  {
    if(result)
      callback.dataReceived();
    else
      callback.dataReceiveError();
  }
}
