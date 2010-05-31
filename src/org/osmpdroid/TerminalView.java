package org.osmpdroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TerminalView extends TableRow implements View.OnClickListener
{
  private Terminal terminal;
  
  public TerminalView(Context context, Terminal terminal)
  {
    super(context);
    this.terminal = terminal;
    
    int icon;
    switch(terminal.state)
    {
      case Terminal.STATE_OK:
        icon = R.drawable.ok;
        break;
      case Terminal.STATE_WARRNING:
        icon = R.drawable.warning;
        break;
      default:
        icon = R.drawable.error;
    }
    
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon);
    ImageView image = new ImageView(context);
    image.setImageBitmap(bitmap);
    image.setPadding(5, 0, 10, 0);

    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    TextView text = new TextView(context);
    text.setTextSize(16);
    text.setText(terminal.Address());
    layout.addView(text);
    
    StringBuilder strings = new StringBuilder();
    TextView cosum = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.cosum));
    strings.append(": ");
    strings.append(terminal.cash);
    cosum.setText(strings.toString());
    layout.addView(cosum,LayoutParams.FILL_PARENT);
    
    TextView printer_state = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.printer_state));
    strings.append(": ");
    strings.append(terminal.printer_state);
    printer_state.setTextSize(9);
    printer_state.setText(strings.toString());
    layout.addView(printer_state,LayoutParams.FILL_PARENT);
    
    TextView cashbin_state = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.cashbin_state));
    strings.append(": ");
    strings.append(terminal.cashbin_state);
    cashbin_state.setTextSize(9);
    cashbin_state.setText(strings.toString());
    layout.addView(cashbin_state,LayoutParams.FILL_PARENT);
    
    this.addView(image);
    this.addView(layout,LayoutParams.FILL_PARENT);

    this.setPadding(0, 5, 0, 5);
    this.setOnClickListener(this);
  }

  @Override
  public void onClick(View arg0)
  {
    Intent intent = new Intent(getContext(),FullInfo.class);
    intent.putExtra("tid", terminal.tid);
    intent.putExtra("addr", terminal.Address());
    intent.putExtra("aid", terminal.agentId);
    intent.putExtra("an", terminal.agentName);
    intent.putExtra("cash", terminal.cash);
    intent.putExtra("bondsCount", terminal.bondsCount);
    intent.putExtra("lastActivity", terminal.lastActivity);
    intent.putExtra("lastPayment", terminal.lastPayment);
    intent.putExtra("signalLevel", terminal.signalLevel);
    intent.putExtra("balance", terminal.balance);
    intent.putExtra("softVersion", terminal.softVersion);
    intent.putExtra("printerModel", terminal.printerModel);
    intent.putExtra("cashbinModel", terminal.cashbinModel);
    intent.putExtra("b_co_10", terminal.bonds10count);
    intent.putExtra("b_co_50", terminal.bonds50count);
    intent.putExtra("b_co_100", terminal.bonds100count);
    intent.putExtra("b_co_500", terminal.bonds500count);
    intent.putExtra("b_co_1000", terminal.bonds1000count);
    intent.putExtra("b_co_5000", terminal.bonds5000count);
    intent.putExtra("paysPerHour", terminal.paysPerHour);    
    getContext().startActivity(intent);
  }

}
