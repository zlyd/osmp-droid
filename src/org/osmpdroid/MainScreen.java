package org.osmpdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainScreen extends Activity implements ITerminalDataReceiver
{
  public static final int SETTINGS_ID = Menu.FIRST;
  public static final int EXIT_ID = Menu.FIRST+1;
  public static final int REQUEST_ID = Menu.FIRST+2;
  
  public static final int ACTIVITY_CONFIGURE = 0;
  
  public static final String PREFS_ALL = "osmp-monitor-prefs";
 
  private TerminalStates terminals;
  private String login;
  private String password;
  private String terminal;
  
  public static boolean isEmptyString(String text)
  {
    return(text==null || text.length()==0);
  }
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    terminals = new TerminalStates();
    setContentView(R.layout.main);
    
    SharedPreferences prefs = getSharedPreferences(PREFS_ALL,MODE_PRIVATE);
    login = prefs.getString("login", null);
    password = prefs.getString("password", null);
    terminal = prefs.getString("terminal", null);
    
    if(isEmptyString(login))
    {
      AlertDialog alert = new AlertDialog.Builder(this).create();
      alert.setTitle(getString(R.string.configure_title));
      alert.setMessage(getString(R.string.configure_app));
      alert.show();
    }
    else
    {
      Request();
    }
  }
  
  protected void Draw()
  {
    if(terminals.status!=0)
    {
      AlertDialog alert = new AlertDialog.Builder(this).create();
      alert.setTitle(getString(R.string.request_error_title));
      alert.setMessage(String.format("%s (%d)", getString(R.string.request_error_text),terminals.status));
      alert.show();
      return;
    }
    
    TextView balance = (TextView) findViewById(R.id.balance_value);
    balance.setText(terminals.Balance());
    TextView overdraft = (TextView) findViewById(R.id.overdraft_value);
    overdraft.setText(terminals.Overdraft());
    
    TableLayout layout = new TableLayout (this);
    int length = terminals.Count();

    for(int j=0;j<1;++j)
    for (int i=0;i<length;++i)
    {
      Terminal terminal = terminals.Terminal(i);
      TerminalView view = new TerminalView(this, terminal);

      layout.addView(view,LayoutParams.FILL_PARENT);
    }
    ScrollView view = (ScrollView)findViewById(R.id.main_scroll);
    if(view.getChildCount()!=0)
      view.removeViewAt(0);
    view.addView(layout);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    boolean result = super.onCreateOptionsMenu(menu);
    menu.add(0, REQUEST_ID, 0, R.string.request);
    menu.add(0, SETTINGS_ID, 0, R.string.settings);
    return (result);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch(item.getItemId())
    {
      case REQUEST_ID:
        Request();
        return (true);
      case EXIT_ID:
        this.finish();
        return(true);
      case SETTINGS_ID:
        Intent intent = new Intent(this,Configure.class);
        intent.putExtra("login", login);
        intent.putExtra("terminal", terminal);
        startActivityForResult(intent, ACTIVITY_CONFIGURE);
        return(true);
    }
    return super.onOptionsItemSelected(item); 
  }
  
  public void onActivityResult(int requestCode,int resultCode, Intent intent)
  {
    super.onActivityResult(requestCode, resultCode, intent);
    //---
    if(requestCode==ACTIVITY_CONFIGURE)
    {
      if(resultCode==RESULT_OK)
      {
        Bundle extras = intent.getExtras();
        login = extras.getString("login");
        terminal = extras.getString("terminal");
        String local_password = extras.getString("password");
        if (local_password!="")
        {
          password = local_password;
        }
        
        SharedPreferences prefs = getSharedPreferences(PREFS_ALL,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("login",login);
        editor.putString("password",password);
        editor.putString("terminal",terminal);
        editor.commit();
        
        if(!isEmptyString(login))
          Request();
      }
    }
  }

  public void Request()
  {
    Bundle params = new Bundle();
    new TerminalStatesReceiver(login, password, terminal, terminals, this).execute(params);
  }
  
  
  @Override
  public void dataReceived()
  {
    Draw();
  }

  @Override
  public void dataReceiveError()
  {
    AlertDialog alert = new AlertDialog.Builder(this).create();
    alert.setTitle(getString(R.string.network_error_title));
    alert.setMessage(getString(R.string.network_error_text));
    alert.show();
  }
}