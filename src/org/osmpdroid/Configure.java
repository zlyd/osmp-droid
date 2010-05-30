package org.osmpdroid;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configure extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.configure);
    
    Bundle extra = getIntent().getExtras();
    EditText login = (EditText)findViewById(R.id.login);
    login.setText(extra.getString("login"));
    EditText terminal = (EditText)findViewById(R.id.terminal);
    terminal.setText(extra.getString("terminal"));
    
    Button button = (Button) findViewById(R.id.save_bitton);
    button.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Bundle data = new Bundle();
        
        EditText login = (EditText)findViewById(R.id.login);
        EditText password = (EditText)findViewById(R.id.password);
        EditText terminal = (EditText)findViewById(R.id.terminal);
        String password_value = password.getText().toString(); 
        
        if(!MainScreen.isEmptyString(password_value))
        {
          try
          {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.reset();
            m.update(password_value.getBytes(),0,password_value.length());
            BigInteger i = new BigInteger(1,m.digest());
            password_value = String.format("%1$032X", i).toLowerCase();
          }
          catch (NoSuchAlgorithmException e)
          {
            // TODO Показать сообщение об ошибке
            password_value = null;
          }
        }
        else
          password_value = null;
        
        data.putString("login", login.getText().toString());
        data.putString("password", password_value);        
        data.putString("terminal", terminal.getText().toString());
        
        Intent result = new Intent();
        result.putExtras(data);
        setResult(RESULT_OK, result);
        finish();
      }
    });
  } 
}
