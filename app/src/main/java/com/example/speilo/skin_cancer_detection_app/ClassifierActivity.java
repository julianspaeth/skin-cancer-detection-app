package com.example.speilo.skin_cancer_detection_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;




/**
 * Created by spaethju on 13.03.18.
 */

public class ClassifierActivity extends Activity{
    static {
        System.loadLibrary("tensorflow_inference");
    }

    private static final String MODEL_FILE = "file:///android_asset/frozen_model_opt.pb";
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "InceptionV3/Predictions/Reshape_1";
    private TensorFlowInferenceInterface inferenceInterface;
    private Bitmap bitmap, preprocessedBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        this.bitmap = (Bitmap) getIntent().getExtras().get("data");
        preprocessedBitmap = preprocessImage(bitmap);
        float[] rgb = rgbValuesFromBitmap(preprocessedBitmap);
        System.out.println(rgb.length);
        classify(rgb);
    }

    public void classify(float[] rgb) {
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
        int numClasses = (int) inferenceInterface.graph().operation(OUTPUT_NAME).output(0).shape().size(1);
        float[] outputs = new float[numClasses];
        inferenceInterface.feed(INPUT_NAME, rgb, rgb.length);
        System.out.println(inferenceInterface.getStatString().toString());

        String[] outputNames = new String[] {OUTPUT_NAME};
        // Run the inference call.
        inferenceInterface.run(outputNames);

        // Copy the output Tensor back into the output array.
        inferenceInterface.fetch(OUTPUT_NAME, outputs);

        // Find the best classifications.
        System.out.println(outputs.length);
        for (int i = 0; i < outputs.length; ++i) {

        }


        Intent myIntent = new Intent(this, ResultsActivity.class);
        finish();
        this.startActivity(myIntent);
    }

    private float[] rgbValuesFromBitmap(Bitmap bitmap)
    {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorFilter colorFilter = new ColorMatrixColorFilter(
                colorMatrix);
        Bitmap argbBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(argbBitmap);

        Paint paint = new Paint();

        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int componentsPerPixel = 3;
        int totalPixels = width * height;
        int totalBytes = totalPixels * componentsPerPixel;

        float[] rgbValues = new float[totalBytes];
        @ColorInt int[] argbPixels = new int[totalPixels];
        argbBitmap.getPixels(argbPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < totalPixels; i++) {
            @ColorInt int argbPixel = argbPixels[i];
            int red = Color.red(argbPixel);
            int green = Color.green(argbPixel);
            int blue = Color.blue(argbPixel);
            rgbValues[i * componentsPerPixel + 0] = (float) red;
            rgbValues[i * componentsPerPixel + 1] = (float) green;
            rgbValues[i * componentsPerPixel + 2] = (float) blue;
        }

        return rgbValues;
    }


    public Bitmap preprocessImage(Bitmap imageBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap imageBitmap_rotated = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
        Bitmap imageBitmap_resized = Bitmap.createScaledBitmap(imageBitmap_rotated, 542, 718, false);
        return imageBitmap_resized;
    }


}
