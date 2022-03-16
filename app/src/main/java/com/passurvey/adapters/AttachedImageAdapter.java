package com.passurvey.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.passurvey.R;
import com.passurvey.activities.AttachedImagesActivity;
import com.passurvey.database.DBHelper;
import com.passurvey.model.PhotoModel;
import com.passurvey.utility.Utils;

import java.util.ArrayList;

/**
 * Created by iadmin on 28/9/16.
 */
public class AttachedImageAdapter extends RecyclerView.Adapter<AttachedImageAdapter.MyViewHolder> {
    ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<PhotoModel>imageList;
    private Context context;
  //  DisplayImageOptions defaultOptions;
    public static DBHelper dbHelper;
    String  ProfileId,UserID,SurveyID;
    private DisplayImageOptions options;


    public AttachedImageAdapter(ArrayList<PhotoModel> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
     /*   defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .writeDebugLogs()
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 320, null)
                .build();
        imageLoader.getInstance().init(config);*/

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_image_pic)
                .showImageForEmptyUri(R.drawable.add_image_pic)
                .showImageOnFail(R.drawable.add_image_pic)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        dbHelper = DBHelper.getInstance(context);
        ProfileId = Utils.getSharedPreString(context,Utils.PREFS_PROFILEID);
        UserID=Utils.getSharedPreString(context,Utils.PREFS_USERID);
        SurveyID=Utils.getSharedPreString(context,Utils.PREFS_SURVEYID);



    }




    public void delete(int position) {
        imageList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attached_image_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        PhotoModel current = imageList.get(position);
       // holder.attachedImg.setImageResource(current.getQuestionGroupId());


       imageLoader.displayImage("file://"+imageList.get(position).getPhotoPath(),holder.attachedImg,options);
       /*File imgFile = new  File(imageList.get(position).getPhotoPath());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile("file://"+imgFile.getAbsolutePath());
            holder.attachedImg.setImageBitmap(myBitmap);
        }*/

        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

       /* dbHelper.DeleteAttachedImage(UserID,SurveyID,ProfileId,imageList.get(position).getPhotoComment(),
        imageList.get(position).getPhotoPath());
                delete(position);*/
                closeDialog(position);


            }
        });

        holder.attachedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog(imageList.get(position).getPhotoComment(),imageList.get(position).getPhotoPath());
            }
        });
       // btnMoreView();


    }

    @Override
    public int getItemCount() {

        return imageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView closeImg,attachedImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            closeImg = (ImageView) itemView.findViewById(R.id.closeImg);
            attachedImg=(ImageView)itemView.findViewById(R.id.attachedImg);




        }
    }



    private  void  closeDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to Delete this image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dbHelper.DeleteAttachedImage(UserID, SurveyID, ProfileId, imageList.get(position).getPhotoComment(),
                                    imageList.get(position).getPhotoPath());
                            delete(position);
                        }
                        catch (Exception ex)
                        {

                        }
                     //   btnMoreView();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private  void btnMoreView(){
        if (imageList.size() < Utils.PHOTOLIMIT){
            AttachedImagesActivity.btnAddmore.setEnabled(true);
            AttachedImagesActivity.btnAddmore.setAlpha(1);
        }else {
            AttachedImagesActivity.btnAddmore.setEnabled(false);
            AttachedImagesActivity.btnAddmore.setAlpha(0.5F);
        }
    }
    public void showImageDialog(String photocomment,String photopath)
    {
        final Dialog dialog;
        ImageView attachedimg,dialogcloseImg;
        TextView txtcomment;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.attached_image_dialog_layout);
        dialogcloseImg=(ImageView)dialog.findViewById(R.id.dialogcloseImg);
        attachedimg=(ImageView)dialog.findViewById(R.id.dialogImg);

        txtcomment=(TextView)dialog.findViewById(R.id.txtcommentdialog);
        if(photocomment.equalsIgnoreCase("")){
            txtcomment.setText("");

        }else {
            txtcomment.setText(photocomment.toString());

        }
        imageLoader.displayImage("file://"+photopath,attachedimg,options);

        dialog.show();
        dialogcloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}

