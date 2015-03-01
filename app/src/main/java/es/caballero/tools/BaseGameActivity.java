package es.caballero.tools;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rcaballeron on 01.03.2015.
 */
public abstract class BaseGameActivity extends Activity {
    private Map<String, Typeface> typefaces = new HashMap<String, Typeface>();

    protected void addTypeface(String name) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), name + ".ttf");
        typefaces.put(name, typeface);
    }

    protected void setTypeface(TextView v, String typefaceName) {
        Typeface t = typefaces.get(typefaceName);
        if (t != null) {
            v.setTypeface(t);
        }
    }
}
