package leebeno.com.leebeno.Common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by advosoft on 7/26/2018.
 */

public class MySpinner extends Spinner {

    OnItemSelectedListener listener;

    public MySpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition())
        {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }
}