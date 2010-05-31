package org.osmpdroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FullInfo extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.full_info);
    
    Bundle extra = getIntent().getExtras();
    setText(extra.getString("addr"),R.id.fullinfo_addr,0);
    setText(extra.getString("tid"),R.id.fullinfo_tid,0);
    setText(extra.getString("aid"),R.id.fullinfo_aid,0);
    setText(extra.getString("an"),R.id.fullinfo_an,0);
    setText(extra.getInt("cash"),R.id.fullinfo_cash,0);
    setText(extra.getInt("bondsCount"),R.id.fullinfo_bonds,0);
    setText(extra.getString("lastActivity"),R.id.fullinfo_last_activity,0);
    setText(extra.getString("lastPayment"),R.id.fullinfo_last_payment,0);
    setText(extra.getInt("signalLevel"),R.id.fullinfo_signal_level,0);
    setText(extra.getString("balance"),R.id.fullinfo_balance,0);
    setText(extra.getString("softVersion"),R.id.fullinfo_soft_version,0);
    setText(extra.getString("printerModel"),R.id.fullinfo_printer,0);
    setText(extra.getString("cashbinModel"),R.id.fullinfo_cashbin,0);
    setText(extra.getInt("b_co_10"),R.id.fullinfo_bonds10,0);
    setText(extra.getInt("b_co_50"),R.id.fullinfo_bonds50,0);
    setText(extra.getInt("b_co_100"),R.id.fullinfo_bonds100,0);
    setText(extra.getInt("b_co_500"),R.id.fullinfo_bonds500,0);
    setText(extra.getInt("b_co_1000"),R.id.fullinfo_bonds1000,0);
    setText(extra.getInt("b_co_5000"),R.id.fullinfo_bonds5000,0);
    setText(extra.getString("paysPerHour"),R.id.fullinfo_pays_per_hour,0);
  }
  
  private void setText(String value, int textId, int rowId)
  {
    TextView text = (TextView) findViewById(textId);
    text.setText(value);
  }
  
  private void setText(int value, int textId, int rowId)
  {
    setText(String.format("%d",value), textId, rowId);
  }
}
