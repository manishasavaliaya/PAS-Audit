package com.passurvey.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.passurvey.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public final class Utils {
     /*Local Link*/
     // public static final String BASE_URL = "http://10.20.1.13:8080/api/";
     /*Live Link*/
     // public static final String BASE_URL = "http://103.244.121.90:8080/api/";
     // public static final String BASE_URL = "http://3.137.27.69/PAS_Survey/public/index.php/api/";
     // public static final String BASE_URL = "http://103.240.34.162:8080/api/"; //old
     // public static final String BASE_URL = "http://103.244.121.90:8080/api/"; // new
     // public static final String BASE_URL = "http://office.thepasltd.co.uk/api/"; // live
     // public static final String BASE_URL = "https://app.thepasltd.co.uk/api/"; //live
    public static final String BASE_URL = "http://pas.ipathsolutions.co.in:8080/public/api/"; //live
    public static final String PREFS_NAME = "MyPrefs";
    public static final String PREFS_SURVEYID = "SurveyId";
    public static final String PREFS_SURVEYNAME = "SurveyName";
    public static final String PREFS_GROUPID = "GroupId";
    public static final String PREFS_GROUPNAME = "GroupName";
    public static final String PREFS_USERID = "UserId";
    public static final String PREFS_DISPLAYNAME = "display_name";
    public static final String PREFS_PROFILECOMPLETED = "profile_completed";
    public static final String PREFS_PROFILEID = "ProfileId";
    public static final String PREFS_PROFILE_AUDIT_DATE = "prefs_profile_audit_date";
    public static final String PREFS_DRAWEREXPANDPOSITION = "DrawerExpandPosition";
    public static final String PREFS_DRAWEREXPANDNAME = "DrawerExpandName";
    public static final String PREFS_DRAWER = "Drawer";
    public static final int PHOTOLIMIT= 20;
    public static boolean isImageSyncing=false;
    private static Dialog popupWindow;

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
             activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception ex) {

        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showToast(Context context, String message) {
        try{
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, getPixelsFromDp(context, 70));
        toast.show();
        } catch (Exception ex) {

        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
         float scale = 0;
        try{
       scale = context.getResources().getDisplayMetrics().density;
        } catch (Exception ex) {
        }
        return (int) (dp * scale + 0.5f);
    }

    public static void showProgress(final Context context) {
        try {
            if(popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
            if (!((Activity) context).isFinishing()) {
                View layout = LayoutInflater.from(context).inflate(R.layout.popup_loading, null);
                popupWindow = new Dialog(context, android.R.style.Theme_Translucent);
                popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupWindow.setContentView(layout);
                popupWindow.setCancelable(false);
               if(popupWindow != null && !popupWindow.isShowing())
                    popupWindow.show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setSharedPreString(Context context, String key, String text) {
        try{
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(key, text);
        editor.commit();
        } catch (Exception ex) {

        }
    }

    public static String getSharedPreString(Context context, String key) {
        String text = null;
        try{
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, "");
        } catch (Exception ex) {

        }
        return text;
    }

    public static boolean isExistKeyinPref(Context context, String key) {
        SharedPreferences pref;
        boolean isexist = false;
        try{
        pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isexist = pref.contains(key);
        } catch (Exception ex) {
        }
        return isexist;
    }

    public static void removeSharedPref(Context context) {
        try{
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
            editor.clear();
        editor.commit();
        } catch (Exception ex) {

        }
    }

    @SuppressLint("NewApi")
    public static boolean validateString(String object) {
        boolean flag = false;
        if (object != null && !object.equalsIgnoreCase("null") && !object.equalsIgnoreCase("(null)") &&
                !object.isEmpty() && !object.equalsIgnoreCase("(null)")) {
            flag = true;
        }
        return flag;
    }

    public static RequestBody imageToBody(String text) {
        RequestBody requestBody = null;
        try{
        if (text != null && text.length() > 0) {
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            File file = new File(text);
            requestBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        } else {
            requestBody = null;
        }
        } catch (Exception ex) {

        }
        return requestBody;
    }

    public static RequestBody textToBody(String text) {
        RequestBody requestBody = null;
        try{
         requestBody = RequestBody.create(MediaType.parse("text/plain"), text);
        } catch (Exception ex) {

        }
        return requestBody;
    }

    public final static boolean isValidEmail(String email) {
        if (email.trim().length() == 0)
            return false;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
    public static int pxToDp(int px,Context ctx) {
        int dp = 0;
        try{
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
         dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        } catch (Exception ex) {

        }
        return dp;
    }
    public static void shareText(Context context, String shareText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);

    }

    public static String getFormatedTime(String time, String input, String outputF) {
        SimpleDateFormat inputFormate = new SimpleDateFormat(input);
        SimpleDateFormat outputFormate = new SimpleDateFormat(outputF);
        String output = null;
        try {
            Date date = inputFormate.parse(time);
            output = outputFormate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }
    public final static boolean isValidPhone(String mobile) {
        String PHONE_PATTERN = "(\\+[0-9]+[\\- \\.]*)?"
                + "(\\([0-9]+\\)[\\- \\.]*)?"
                + "([0-9][0-9\\- \\.]+[0-9])";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();

    }
    private String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd/MM/yyyy   HH:mm a", cal).toString();
        return date;
    }
    public static String getDateTime()
    {
        java.text.DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }

    public static String getDateTimeNew()
    {
        java.text.DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }

    public static String getDate()
    {
        java.text.DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }

    public static String getTime()
    {
        java.text.DateFormat dateFormatter = new SimpleDateFormat("hh:mm: a");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }
    public static String getDeviceid(Context context)
    {
         String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return android_id;
    }
    public static boolean checkfile(String  filepath)
    {
        if (filepath!=null) {
            File file;

            file = new File(filepath);
            if (file.exists()) {
//Do something
                return true;
            } else {
                return false;
            }
        }
        else
        {
            return false;
        }
// Do something else.
    }

    public static void showkeyboard(Context ctx,View v)
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                v.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        }

    public static String ReplaceAsciChar(String Status) {

        //Status = Status.replace("&", "amp").replace("~", "-").replace("<", "lt").replace("\"", "\"").replace("\r\n", "\n");
        Status = Status.replace("\"", "\"").replace("\r\n", "\n");
        return Status;
    }

   /* public class DisplayKey implements Runnable {
        private final Context mContext;
        private final EditText mEditText;

        public DisplayKey(Context mContext,EditText meditText) {
            this.mContext = mContext;
            this.mEditText =meditText;
        }

        public void run() {
            //Toast.makeText(mContext, "Show code keyboard", Toast.LENGTH_SHORT).show();
            mEditText.setSelection(mEditText.getText().length());
            showkeyboard(mContext,mEditText);
        }
    }*/

    public static RequestBody getRequestBody(String string)
    {
        RequestBody requestBody = null;
        try{
            requestBody = RequestBody.create(MediaType.parse("text/plain"), string);
        } catch (Exception ex) {

        }
        return requestBody;
    }

    public static String getmonthbeforedate()
    {
        //java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        Calendar calReturn = Calendar.getInstance();
        calReturn.add(Calendar.DATE, -30);
        return   dateFormat.format(calReturn.getTime());
    }

    //Manisha 12-8-19
    public static Date convertStringToDate(String strDate,String inputFormat,String outputFormate) {
        java.text.DateFormat iFormatter = new SimpleDateFormat(inputFormat);
        java.text.DateFormat oFormatter = new SimpleDateFormat(outputFormate);
        String strDateTime = null;

        try {
            strDateTime = oFormatter.format(iFormatter.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;

        try {
            date = oFormatter.parse(strDateTime);
            // String returndate = new SimpleDateFormat(oFormatter).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        date = format2.format(date);
//        System.out.println(format2.format(date));
        return date;
    }
    public static String getTodayDate(String formet) {
        String datevalue = "";
        try {
            SimpleDateFormat df;
            Calendar c = Calendar.getInstance();
//            "dd/MM/yyyy"
            df = new SimpleDateFormat(formet);

            final Date date = c.getTime();
            datevalue = df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return datevalue;
    }
//    public static void msgDialog() {
//
//        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        dialog.setContentView(R.layout.dialog_not_applicable);
//
//        dialog.getWindow().setDimAmount(0.6f);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//
//        Button dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
//        // if button is clicked, close the custom dialog
//        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//
//            }
//        });
//        Button dialogButtonno = (Button) dialog.findViewById(R.id.btnno);
//        // if button is clicked, close the custom dialog
//        dialogButtonno.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//
//        dialog.show();
//    }
}
