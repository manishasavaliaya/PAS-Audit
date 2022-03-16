package com.passurvey.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.passurvey.R;
import com.passurvey.database.DBHelper;
import com.passurvey.fragments.FragmentDrawer;
import com.passurvey.fragments.ProfileFragment;
import com.passurvey.fragments.SelectSurveyFragment;
import com.passurvey.utility.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import au.com.bytecode.opencsv.CSVWriter;
import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    View view;
    private TextView tvHeaderTitle;
    public static DBHelper dbHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private static Dialog popupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHelper = DBHelper.getInstance(MainActivity.this);
        tvHeaderTitle=(TextView)mToolbar.findViewById(R.id.tvHeaderTitle);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
    }

    @Override
    public void setLayoutView() {

    }

    @Override
    public void initialization() {

    }

    @Override
    public void setupData() {

    }

    @Override
    public void setListeners() {

    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  hideTopBar();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvHeaderTitle=(TextView)findViewById(R.id.tvHeaderTitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        dbHelper = DBHelper.getInstance(MainActivity.this);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerFragment.setMenuVisibility(false);
        // display the first navigation drawer view on app launch
        displayView(0);
    }*/


   /* @Override
    public void setLayoutView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_main, lnrContainer);
        hideTopBar();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvHeaderTitle=(TextView)findViewById(R.id.tvHeaderTitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerFragment.setMenuVisibility(false);
        // display the first navigation drawer view on app launch
        displayView(0);
    }*/

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                title = getString(R.string.profile);
              //  startActivity(new Intent(MainActivity.this,ProfileActivity.class));
              //  finish();
                break;

            case 1:
                fragment = new SelectSurveyFragment();
               // Utils.showToast(MainActivity.this,"SelectSurveyFragment Click");
                break;

            case 2:
                /*fragment = new MessagesFragment();
                title = getString(R.string.title_messages);*/
               // Utils.showToast(MainActivity.this,"MessagesFragment Click");
                break;

            default:
                break;
        }

        /*if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }*/
    }

    /*To set title call this method*/
    public void setHeaderTitle(String title)
    {
        tvHeaderTitle.setText(title);
    }

    /*Adding and replacing fragment*/
    public void addFragment(@NonNull Fragment fragment,
                            @NonNull String fragmentTag,Bundle data) {
        if (data!=null){
            fragment.setArguments(data);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_body,fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .commit();
        /*.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_right_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit)*/
    }

    public void ReplaceFragment(@NonNull Fragment fragment, @NonNull String fragmentTag,Bundle data) {
        if (data!=null){
            fragment.setArguments(data);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_body,fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            // return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        /*Snackbar snackbar = Snackbar
                .make(drawerFragment, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
        snackbar.show();*/
        Utils.showToast(MainActivity.this,getString(R.string.backtoexit));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static void showProgress(final Context context) {
        try {
            if (!((Activity) context).isFinishing()) {
                View layout = LayoutInflater.from(context).inflate(R.layout.popup_loading, null);
                popupWindow = new Dialog(context, android.R.style.Theme_Translucent);
                popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupWindow.setContentView(layout);
                popupWindow.setCancelable(false);
                if (!((Activity) context).isFinishing()) {
                    popupWindow.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showprogressnew()
{
    showProgress(MainActivity.this);
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

    public void showkeyboard(EditText edittext) {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                edittext.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public void showkeyboard(RichEditor edittext) {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                edittext.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public void hidekeybooard(View view) {
        //View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class DisplayKey implements Runnable {
        private final Context mContext;
        private final EditText mEditText;

        public DisplayKey(Context mContext,EditText meditText) {
            this.mContext = mContext;
            this.mEditText = meditText;
        }

        public void run() {
            //Toast.makeText(mContext, "Show code keyboard", Toast.LENGTH_SHORT).show();
            mEditText.setSelection(mEditText.getText().length());
            hidekeybooard(mEditText);
            showkeyboard(mEditText);
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void ExecsvCreation() {
        new ExportDatabaseCSVTask().execute();
    }
    /* create csv file */
    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        //private final ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
        @Override
        protected void onPreExecute() {
            /*this.dialog.setMessage("Exporting database...");
            this.dialog.show();*/
        }

        protected Boolean doInBackground(final String... args) {
            File dbFile=getDatabasePath("PASSURVEYDB");
            //AABDatabaseManager dbhelper = new AABDatabaseManager(getApplicationContext());
            // AABDatabaseManager dbhelper = new AABDatabaseManager(DatabaseExampleActivity.this) ;
            System.out.println(dbFile);  // displays the data base path in your logcat
            File exportDir = new File(Environment.getExternalStorageDirectory()+File.separator+"PASAuditData", "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "PasAudit.csv");
            try {
                if (file.createNewFile()){
                    System.out.println("File is created!");
                    System.out.println("myfile.csv "+file.getAbsolutePath());
                }else{
                    System.out.println("File already exists.");
                }

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                // SQLiteDatabase db = dbhelper.getWritableDatabase();

                Cursor curCSV=dbHelper.getalluserans();

                csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())

                {

                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9),curCSV.getString(10),curCSV.getString(11),curCSV.getString(12),curCSV.getString(13),curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),curCSV.getString(17),curCSV.getString(18)};

         /*curCSV.getString(3),curCSV.getString(4)};*/

                    csvWrite.writeNext(arrStr);


                }

                csvWrite.close();
                curCSV.close();
        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

                return true;

            }

            catch(SQLException sqlEx)

            {

                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);

                return false;

            }

            catch (IOException e)

            {

                Log.e("MainActivity", e.getMessage(), e);

                return false;

            }

        }

        protected void onPostExecute(final Boolean success)

        {

           /* if (this.dialog.isShowing())

            {

                this.dialog.dismiss();

            }*/

            if (success)

            {

                //Toast.makeText(SplashActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();

            }

            else

            {

                //   Toast.makeText(SplashActivity.this, "Export failed", Toast.LENGTH_SHORT).show();

            }
        }}
}