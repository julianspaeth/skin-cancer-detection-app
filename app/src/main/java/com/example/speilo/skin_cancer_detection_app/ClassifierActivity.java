package com.example.speilo.skin_cancer_detection_app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by spaethju on 13.03.18.
 */

public class ClassifierActivity extends Activity{

    private static final String MODEL_FILE = "file:///android_asset/optimized_tfdroid.pb";
    private static final String LABEL_FILE = "file:///android_asset/graph_label_strings.txt";
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "label";
    private static final int INPUT_SIZE = 299;
   // private TensorFlowInferenceInterface c = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
  //  private int numClasses = (int) c.graph().operation(OUTPUT_NAME).output(0).shape().size(1);

    private Bitmap bitmap, preprocessedBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        this.bitmap = (Bitmap) getIntent().getExtras().get("data");
        preprocessedBitmap = preprocessImage(bitmap);
        System.out.println(preprocessedBitmap.getWidth());

    }


    public Bitmap preprocessImage(Bitmap imageBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap imageBitmap_rotated = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
        Bitmap imageBitmap_resized = Bitmap.createScaledBitmap(imageBitmap_rotated, 542, 718, false);
        return imageBitmap_resized;
    }


}
