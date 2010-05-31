package org.osmpdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
  private HashMap<String,TerminalView> views;
  private String login;
  private String password;
  private String terminal;
  
  public MainScreen()
  {
    terminals = new TerminalStates();
    views  = new HashMap<String, TerminalView>();
  }
  
  public static boolean isEmptyString(String text)
  {
    return(text==null || text.length()==0);
  }
  
  private static String moneyString(double money)
  {
    String result = String.format("%(.2f", money);
    int i = result.length()-1;
    while(result.charAt(i)=='0')
      --i;
    return(result.substring(0, i+1));
  }
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    SharedPreferences prefs = getSharedPreferences(PREFS_ALL,MODE_PRIVATE);
    login = prefs.getString("login", null);
    password = prefs.getString("password", null);
    terminal = prefs.getString("terminal", null);
    
    if(isEmptyString(login))
    {
      AlertDialog alert = new AlertDialog.Builder(this)
                                         .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) 
                                           {
                                             callSettings();
                                           }
                                         }).create();
      alert.setTitle(getString(R.string.configure_title));
      alert.setMessage(getString(R.string.configure_app));
      alert.show();
    }
    else
    {
      if(terminals.isEmpty())
        Request();
      else
        Draw();
    }
  }
  
  protected void Draw()
  {
    TextView balance = (TextView) findViewById(R.id.balance_value);
    double value = terminals.Balance();
    String text = moneyString(value);
    balance.setText(text);
    
    TableLayout layout = (TableLayout) findViewById(R.id.terminals_table);
    layout.removeAllViews();
    int sum = 0;
    Iterator<String> iter = terminals.iterator();
    ArrayList<TerminalView> red_list = new ArrayList<TerminalView>();
    ArrayList<TerminalView> yellow_list = new ArrayList<TerminalView>();
    ArrayList<TerminalView> green_list = new ArrayList<TerminalView>();

    while(iter.hasNext())
    {
      Terminal terminal = terminals.at(iter.next());
      if(terminal!=null)
      {
        TerminalView view = views.get(terminal.id()); 
        if(view==null)
        {
          view = new TerminalView(this, terminal);
          views.put(terminal.id(), view);
        }
        sum+=terminal.cash;
        switch(terminal.state)
        {
          case Terminal.STATE_OK:
            green_list.add(view);
            break;
          case Terminal.STATE_WARRNING:
            yellow_list.add(view);
            break;
          default:
            red_list.add(view);
        }
      }
    }
    // Красный лист
    Iterator<TerminalView> view_iter = red_list.iterator();
    while(view_iter.hasNext())
    {
      layout.addView(view_iter.next());
    }
    // Желтый лист
    view_iter = yellow_list.iterator();
    while(view_iter.hasNext())
    {
      layout.addView(view_iter.next());
    }
    // Зеленый лист
    view_iter = green_list.iterator();
    while(view_iter.hasNext())
    {
      layout.addView(view_iter.next());
    }
    TextView cash = (TextView) findViewById(R.id.cash_value);
    cash.setText(String.format("%s", sum));
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
        callSettings();
        return(true);
    }
    return super.onOptionsItemSelected(item); 
  }
  
  private void callSettings()
  {
    Intent intent = new Intent(this,Configure.class);
    intent.putExtra("login", login);
    intent.putExtra("terminal", terminal);
    startActivityForResult(intent, ACTIVITY_CONFIGURE); 
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
    if(terminals.status!=0)
    {
      AlertDialog alert = new AlertDialog.Builder(this)
                          .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
                            {
                              public void onClick(DialogInterface dialog, int id) 
                              {
                               dialog.cancel();
                              }
                            })
                          .create();
      alert.setTitle(getString(R.string.request_error_title));
      alert.setMessage(String.format("%s (%d)", getString(R.string.request_error_text),terminals.status));
      alert.show();
      return;
    }
    else
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