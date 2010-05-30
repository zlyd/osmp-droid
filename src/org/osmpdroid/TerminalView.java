package org.osmpdroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TerminalView extends TableRow
{
  public TerminalView(Context context, Terminal terminal)
  {
    super(context);
    
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
    strings.append(terminal.cosum);
    cosum.setText(strings.toString());
    layout.addView(cosum,LayoutParams.FILL_PARENT);
    
    TextView printer_state = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.printer_state));
    strings.append(": ");
    strings.append(terminal.printer_state);
    printer_state.setText(strings.toString());
    layout.addView(printer_state,LayoutParams.FILL_PARENT);
    
    TextView cashbin_state = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.cashbin_state));
    strings.append(": ");
    strings.append(terminal.cashbin_state);
    cashbin_state.setText(strings.toString());
    layout.addView(cashbin_state,LayoutParams.FILL_PARENT);
    
    TextView last_payment = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.last_activity));
    strings.append(": ");
    strings.append(terminal.lastActivity);
    last_payment.setText(strings.toString());
    layout.addView(last_payment,LayoutParams.FILL_PARENT);
    
    TextView last_activity = new TextView(context);
    strings.delete(0, strings.length());
    strings.append(context.getString(R.string.last_payment));
    strings.append(": ");
    strings.append(terminal.lastPayment);
    last_activity.setText(strings.toString());
    layout.addView(last_activity,LayoutParams.FILL_PARENT);
    
    this.addView(image);
    this.addView(layout,LayoutParams.FILL_PARENT);
    
    this.setPadding(0, 5, 0, 5);    
  }

}
