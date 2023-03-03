package com.digwex.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import androidx.annotation.NonNull;

public class TypefaceUtils {

    private static final String FONTS_FOLDER = "fonts/";
    private static final String FONT_CIRCE_BOLD = "Circe-bold.otf";
    private static final String FONT_CIRCE_REGULAR = "Circe-regular.otf";


    private static Typeface mCirceBoldTypeface;
    private static Typeface mCirceRegularTypeface;

    public static Typeface getCirceBoldTypeface(@NonNull AssetManager assetManager){
        if(mCirceBoldTypeface == null){
            mCirceBoldTypeface = Typeface.createFromAsset(assetManager, FONTS_FOLDER + FONT_CIRCE_BOLD);
        }

        return mCirceBoldTypeface;
    }

    public static Typeface getCirceRegularTypeface(@NonNull AssetManager assetManager){
        if(mCirceRegularTypeface == null){
            mCirceRegularTypeface = Typeface.createFromAsset(assetManager, FONTS_FOLDER + FONT_CIRCE_REGULAR);
        }

        return mCirceRegularTypeface;
    }

}
