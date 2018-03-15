package com.example.speilo.skin_cancer_detection_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by spaethju on 13.03.18.
 */

public class CameraActivity extends Activity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private File photoFile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("IO ERROR!");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            System.out.println("Width: " + bitmap.getWidth());
            Intent myIntent = new Intent(this, ClassifierActivity.class);
            myIntent.putExtras(extras); //Put your id to your next Intent
            finish();
            this.startActivity(myIntent);
        }
    }

    //Matrix matrix = new Matrix();
    //matrix.postRotate(90);
    //Bitmap imageBitmap_rotated = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
    //Bitmap imageBitmap_resized = Bitmap.createScaledBitmap(imageBitmap_rotated, 542, 718, false);

//    @Override
//    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE) {
//            if (resultcode == Activity.RESULT_OK) {
//                System.out.println("Debug:: " + mCurrentPhotoPath);
////                storageDir = new File(
////                        Environment.getExternalStoragePublicDirectory(
////                                Environment.DIRECTORY_PICTURES
////                        ),
////                        getAlbumName()
////                );
////                MediaStore.Images.Media.insertImage(getContentResolver(), yourBitmap, yourTitle, yourDescription);
////                File file = new File(mCurrentPhotoPath);
////
////                Uri uri = Uri.fromFile(file);
//
//                galleryAddPic();
//            }
//        }
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
////
//            galleryAddPic();
//        }
//    }
    //    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        switch(requestCode) {
//            case 0:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    //file path of captured image
//                    String filePath = cursor.getString(columnIndex);
//                    //file path of captured image
//                    File f = new File(filePath);
//                    String filename = f.getName();
//
////                    Toast.makeText(SiteViewFieldCreate.this, "Your Path:"+filePath, 2000).show();
////                    Toast.makeText(SiteViewFieldCreate.this, "Your Filename:"+filename, 2000).show();
//                    cursor.close();
//
//                    //Convert file path into bitmap image using below line.
//                    // yourSelectedImage = BitmapFactory.decodeFile(filePath);
//
//
//                    //put bitmapimage in your imageview
//                    //yourimgView.setImageBitmap(yourSelectedImage);
//                }
//                break;
//        }
//    }


////    Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//    Bitmap imageBitmap_rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//    Bitmap imageBitmap_resized = Bitmap.createScaledBitmap(imageBitmap_rotated, 542, 718, false);

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        System.out.println("Debug:: " + mCurrentPhotoPath);

        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
