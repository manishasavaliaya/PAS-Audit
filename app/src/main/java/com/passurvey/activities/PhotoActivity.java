package com.passurvey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.passurvey.R;
import com.passurvey.model.PhotoModel;
import com.passurvey.utility.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.theartofdev.edmodo.cropper.CropImageActivity.progressbar1;

/**
 * Created by iadmin on 27/9/16.
 */
public class PhotoActivity extends MainActivity implements View.OnClickListener {

    private ImageView iconImg, addImage;
    private Button btnAddmore,btnSkip,btnfinish;
    private EditText edtcommit;
    private TextView txtAddImage;
    private PhotoModel photoModel;
    private String storagepath;
    String  ProfileId,UserID,SurveyID;
    private  boolean isbtnfinishclick=false;
    ImageLoader imageLoader = ImageLoader.getInstance();
  //  DisplayImageOptions defaultOptions;
    private DisplayImageOptions options;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.photo_activity_layout, contentFrameLayout);
          /*amitk 4-2-17*/
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(PhotoActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_image_pic)
                .showImageForEmptyUri(R.drawable.add_image_pic)
                .showImageOnFail(R.drawable.add_image_pic)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        bindid();
    }

    private void bindid() {
        setHeaderTitle(getString(R.string.title_photo));
       /* defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(PhotoActivity.this)
                .writeDebugLogs()
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 320, null)
                .build();*/
      //  imageLoader.getInstance().init(config);

        iconImg = (ImageView) findViewById(R.id.iconImg);
        addImage = (ImageView) findViewById(R.id.addImage);
        btnAddmore = (Button) findViewById(R.id.btnAddMore);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        txtAddImage=(TextView)findViewById(R.id.txtAddImage);
        edtcommit=(EditText)findViewById(R.id.edtcommit);
        btnfinish=(Button)findViewById(R.id.btnFinish);
        iconImg.setOnClickListener(this);
        btnAddmore.setOnClickListener(this);
        btnfinish.setVisibility(View.GONE);
        btnSkip.setOnClickListener(this);
        btnfinish.setOnClickListener(this);

        otherViewVisible();
        btnSkip.setVisibility(View.GONE);
        btnfinish.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddMore:
                onClickbtnAddmore();
                break;

            case  R.id.iconImg:
                selectImage();
                break;

            case R.id.btnFinish:
                isbtnfinishclick=true;
                onClickbtnfinished();
                break;

            case R.id.btnSkip:
                startActivity(new Intent(PhotoActivity.this, FillOutDetailsActivity.class));
                finish();
                break;
        }
    }

    private void selectImage() {
        startCropImageActivity(null);
       /* final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoActivity.this);
      //  builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(cameraIntent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();*/
    }
    private  void otherViewVisible(){
        iconImg.setVisibility(View.VISIBLE);
        txtAddImage.setVisibility(View.VISIBLE);

    }

    private  void otherViewInVisible(){
        iconImg.setVisibility(View.INVISIBLE);
        txtAddImage.setVisibility(View.INVISIBLE);

    }
    private  void clearview(){
        addImage.setImageBitmap(null);
        edtcommit.setText("");
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setInitialCropWindowPaddingRatio(0)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);

    }

    public  void onClickbtnAddmore(){
    if (dbHelper.getimagepathAndcomment(SurveyID,ProfileId,UserID)== null || dbHelper.getimagepathAndcomment(SurveyID,ProfileId,UserID).size() <Utils.PHOTOLIMIT){

    if(iconImg.getVisibility()==View.VISIBLE){
        Utils.showToast(PhotoActivity.this,getString(R.string.image_validation));
    }else if (edtcommit.getText().toString().equalsIgnoreCase("")){
        Utils.showToast(PhotoActivity.this,getString(R.string.comment_validation));
    }else {
        otherViewVisible();
        AddPhotoInDatabase(edtcommit.getText().toString(),storagepath);
        clearview();
        btnfinish.setVisibility(View.VISIBLE);
    }

}
    else {
    Utils.showToast(PhotoActivity.this,getString(R.string.image_select_validation));

}


    }

    public  void onClickbtnfinished(){

        if(iconImg.getVisibility()==View.VISIBLE && edtcommit.getText().toString().equalsIgnoreCase("") ){
            startActivity(new Intent(PhotoActivity.this, AttachedImagesActivity.class));
            finish();
        }else if(dbHelper.getimagepathAndcomment(SurveyID,ProfileId,UserID).size() < Utils.PHOTOLIMIT){
            AddPhotoInDatabase(edtcommit.getText().toString(),storagepath);
        }

        else {
            startActivity(new Intent(PhotoActivity.this, AttachedImagesActivity.class));
            finish();
        }
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;


        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    /*amitk 3-2-17*/

    private void copyFile(File input, File  outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist

            in = new FileInputStream(input);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;


        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                Log.d("path",result.getUri().getPath());
                Uri imageUri = result.getUri();
                try {
                   // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    try {
                        // progressbar.show();
                       //  Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(bitmap, 500, 500);
                       // addImage.setImageBitmap(resizedBitmap);
                        otherViewInVisible();
                        // progressbar.dismiss();

                        String camaraimagepath = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator;
                        File mFolder = new File(camaraimagepath);
                        File imgFile = new File(camaraimagepath, String.valueOf(System.currentTimeMillis()) + ".jpg");

                        if (!mFolder.exists()) {
                            mFolder.mkdir();
                        }
                        String copypath;
                        if (imageUri.toString().contains("content://")) {
                            storagepath = imgFile.getPath();

                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(imageUri, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            copypath= c.getString(columnIndex);
                            c.close();

                            //copyFile(new File(c.getString(columnIndex)),imgFile);
                        }
                        else
                        {
                            storagepath = imgFile.getPath();

                            copypath= imageUri.getPath();
                        }
                        copyFile(new File(copypath),imgFile);
                        imageLoader.displayImage("file://"+copypath,addImage,options);
                       /* FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(imgFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG,70, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            otherViewVisible();
                            progressbar1.dismiss();
                        }*/

                    }catch (Exception e) {
                        e.printStackTrace();
                        otherViewVisible();
                        progressbar1.dismiss();

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    otherViewVisible();
                    progressbar1.dismiss();
                }

                progressbar1.dismiss();
                //

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            try {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(photo, 500, 500);
                addImage.setImageBitmap(resizedBitmap);
                otherViewInVisible();

                //String camaraimagepath=Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+File.separator;
                String camaraimagepath = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator;

                File dir = new File(camaraimagepath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                //String path=Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+File.separator;
                String path = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator;


                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                file.getPath();
                storagepath = file.getPath();

                try {
                    outFile = new FileOutputStream(file);
                    //   bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    otherViewVisible();


                } catch (IOException e) {
                    e.printStackTrace();
                    otherViewVisible();


                } catch (Exception e) {
                    e.printStackTrace();
                    otherViewVisible();


                }

            }catch (Exception e) {
                e.printStackTrace();
                otherViewVisible();


            }
        }
             else if (requestCode == 2) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
                Log.w("image from gallery..", picturePath+"");

                otherViewInVisible();




           *//* Bitmap newrot = null;
            try {
                newrot = modifyOrientation(BitmapFactory.decodeFile(picturePath), picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap resizedBitmap=null;
            if (newrot != null) {
                resizedBitmap = ThumbnailUtils.extractThumbnail(newrot, 500, 500);
            } else
            {
                resizedBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath), 500, 500);
            }*//*
           // addImage.setImageBitmap(resizedBitmap);
             imageLoader.displayImage("file://"+picturePath,addImage);


            File f = new File(picturePath);
            Uri contentUri = Uri.fromFile(f);
            String inputfilepath=f.getName();
            storagepath=Environment.getExternalStorageDirectory()+File.separator+"PASAudit"+"/"+inputfilepath;
            copyFile(f.getParent()+File.separator, f.getName(), Environment.getExternalStorageDirectory()+File.separator+"PASAudit"+File.separator);
        }
        }
*/

    private void AddPhotoInDatabase(String photoComment,String photoPath){
         ProfileId = Utils.getSharedPreString(PhotoActivity.this,Utils.PREFS_PROFILEID);
        UserID=Utils.getSharedPreString(PhotoActivity.this,Utils.PREFS_USERID);
        SurveyID=Utils.getSharedPreString(PhotoActivity.this,Utils.PREFS_SURVEYID);

        photoModel=new PhotoModel();
        photoModel.setSurveyId(SurveyID);
        photoModel.setProfileId(ProfileId);
        photoModel.setUserId(UserID);
        photoModel.setPhotoComment(photoComment);
        photoModel.setPhotoPath(photoPath);
        photoModel.setSynFlag("0");

       // photoModel.setCreatedDateTime(Utils.getDateTime());
        photoModel.setCreatedDateTime(Utils.getDateTimeNew());
        photoModel.setSYNC_FLAG_IMAGE("0");
        photoModel.setSYNC_FLAG_SURVEYCOMPLETE("0");

        dbHelper.insertPhotos(photoModel);
        if (isbtnfinishclick){
            startActivity(new Intent(PhotoActivity.this, AttachedImagesActivity.class));
            finish();
            isbtnfinishclick=false;
        }
    }

   /* public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }*/

}
