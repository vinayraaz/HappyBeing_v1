package com.azuyo.happybeing.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Admin on 17-01-2017.
 */

public class FontOverride {

    public static void setDefault(Context context, String staticTypefaceName, String customeFontAssetName) {

        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), customeFontAssetName);
        replaceFont(staticTypefaceName, typeface);
    }

    protected static void replaceFont(String staticTypefaceName, final Typeface typeface) {

        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceName);
            staticField.setAccessible(true);
            staticField.set(null, typeface);

        } catch (NoSuchFieldException e){
            Log.e("NoSuchFieldException", e.toString());
        } catch (IllegalAccessException e) {
            Log.e("illegalAccesException", e.toString());
        }
    }
}
