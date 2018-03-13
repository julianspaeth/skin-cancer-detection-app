package com.example.speilo.skin_cancer_detection_app;

import android.app.Activity;
import android.media.Image;

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
    private TensorFlowInferenceInterface c = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
    private int numClasses = (int) c.graph().operation(OUTPUT_NAME).output(0).shape().size(1);

    private Image image, preprocessedImage;


    public ClassifierActivity(Image image) {
        this.image = image;
        preprocessedImage = preprocessImage(image);
    }

    public Image preprocessImage(Image image) {
        preprocessedImage = image;

        //Preprocess steps as in skin cancer detection python

        return preprocessedImage;
    }


}
