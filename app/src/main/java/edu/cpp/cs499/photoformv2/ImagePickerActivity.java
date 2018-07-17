package edu.cpp.cs499.photoformv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.provider.MediaStore.Images;
//import com.squareup.picasso.Picasso;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.content.Context;
import android.content.ContentValues;
import android.os.Environment;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.UUID;
import java.io.IOException;
import android.util.Log;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.media.MediaScannerConnection;

public class ImagePickerActivity extends Activity {

    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private LinearLayout lLayout;
/**
    //bitmap section - IF USABLE
    public Bitmap createBitmapForView(View view)
    {
        int width = view.getWidth();
        int height = view.getHeight();
        // create a bitmap the size of the view
        Bitmap screenShot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // create a canvas with the bitmap
        Canvas canvas = new Canvas(screenShot);
        // draw the view to the canvas
        view.draw(canvas);
        //bitmap contains the view
        return screenShot;
    }
*/
    //bitmap2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);



        final Button pickImage1 = (Button) findViewById(R.id.btn_pick1);
        pickImage1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageView = (ImageView)findViewById(R.id.imageView1);
                Intent photoPickerIntent1 = new Intent(Intent.ACTION_PICK);
                photoPickerIntent1.setType("image/*");
                startActivityForResult(photoPickerIntent1, SELECT_PHOTO);

                
            }
        });



        Button pickImage2 = (Button) findViewById(R.id.btn_pick2);
        pickImage2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageView = (ImageView)findViewById(R.id.imageView2);
                Intent photoPickerIntent2 = new Intent(Intent.ACTION_PICK);
                photoPickerIntent2.setType("image/*");
                startActivityForResult(photoPickerIntent2, SELECT_PHOTO);
            }
        });



        Button pickImage3 = (Button) findViewById(R.id.btn_pick3);
        pickImage3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageView = (ImageView)findViewById(R.id.imageView3);
                Intent photoPickerIntent3 = new Intent(Intent.ACTION_PICK);
                photoPickerIntent3.setType("image/*");
                startActivityForResult(photoPickerIntent3, SELECT_PHOTO);
            }
        });



        Button pickImage4 = (Button) findViewById(R.id.btn_pick4);
        pickImage4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageView = (ImageView)findViewById(R.id.imageView4);
                Intent photoPickerIntent4 = new Intent(Intent.ACTION_PICK);
                photoPickerIntent4.setType("image/*");
                startActivityForResult(photoPickerIntent4, SELECT_PHOTO);
            }
        });
        //save feature button
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //imageView = (ImageView)findViewById(R.id.saveButton);
                        //pass view as parameter
                        File file = saveBitMap(ImagePickerActivity.this, lLayout);
                        if (file != null) {
                            Log.i("TAG", "Drawing saved to the gallery!");
                        } else {
                            Log.i("TAG", "Image could not be saved.");
                        }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);

                        //Picasso.get().load(friendList.get(position).getImageUrl())
                        //.resize(92, 92).into(imageView);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
    //////
    private File saveBitMap(Context context, View drawView){
        File pictureFileDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/folder name/");
        if (!pictureFileDirectory.exists()) {
            boolean isDirectoryCreated = pictureFileDirectory.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDirectory.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }
    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    //\scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /////


/*
    //add image to gallery?
    public static void addImageToGallery(final String filePath, final Context context){
        ContentValues values = new ContentValues();

        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Images.Media.MIME_TYPE, "Image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);


    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }




}
