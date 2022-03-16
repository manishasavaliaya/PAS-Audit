package com.passurvey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.passurvey.expandablelistModel.Ingredient;
import com.passurvey.expandablelistModel.Recipe;
import com.passurvey.model.CommentsModel;
import com.passurvey.model.DefaultAnswerModel;
import com.passurvey.model.PhotoModel;
import com.passurvey.model.ProfileModel;
import com.passurvey.model.QuestionAnswerModel;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.model.QuestionNameModel;
import com.passurvey.model.managingAuditorModel;
import com.passurvey.requestresponse.DefaultansRequestResponse;
import com.passurvey.requestresponse.GetCommentsRequestResponse;
import com.passurvey.requestresponse.ManagementAuditorRequestResponse;
import com.passurvey.requestresponse.SurveydataRequestResponse;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static String TAG = DBHelper.class.getSimpleName();
    public static DBHelper dbHelper;

    public DBHelper(Context context) {
        super(context, DBUtility.DATABASE_NAME, null, 7);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBUtility.CREATE_TABLE_QUESTION);
        db.execSQL(DBUtility.CREATE_TABLE_USER_ANSWER);
        db.execSQL(DBUtility.CREATE_TABLE_DEFAULT_ANSWER);
        db.execSQL(DBUtility.CREATE_TABLE_SURVEY_PROFILE);
        db.execSQL(DBUtility.CREATE_TABLE_SURVEY_PHOTOS);
        db.execSQL(DBUtility.CREATE_TABLE_MANAGEMENT_AUDITOR);
        db.execSQL(DBUtility.CREATE_TABLE_COMMENTS);
        db.execSQL(DBUtility.CREATE_TABLE_PERFOMANCE_COMMENTS);
        db.execSQL(DBUtility.CREATE_TABLE_CLIENT_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_USER_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_SURVEY_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_SURVEY_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_PERFOMANCE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_CLIENT_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_DEFAULT_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS " + DBUtility.TABLE_MANAGEMENT_AUDITOR);

        onCreate(db);
    }

    // truncate table
    public void truncateUserAnswer(String SurveyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBUtility.TABLE_USER_ANSWER,
                DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(SurveyId)});
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_USER_ANSWER +" where "+DBUtility.PROFILE_ID+"='"+SurveyId+"'" , null);
        db.close();*/
    }

    public void truncateSurveyProfile(String SurveyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBUtility.TABLE_SURVEY_PROFILE,
                DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(SurveyId)});
       /* SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_SURVEY_PROFILE +" where "+DBUtility.PROFILE_ID+"='"+SurveyId+"'" , null);
        db.close();*/
    }

    public void truncateSurveyPhotos(String SurveyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBUtility.TABLE_SURVEY_PHOTOS,
                DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(SurveyId)});
        /*SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.PROFILE_ID+"='"+SurveyId+"'" , null);
        db.close();*/
    }

    public void truncateQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_QUESTION);
        db.close();
    }

    public void truncateUserAnswer() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_USER_ANSWER);
        db.close();
    }

    public void truncateDefaultAns() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_DEFAULT_ANSWER);
        db.close();
    }

    public void truncateManagementAuditor() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_MANAGEMENT_AUDITOR);
        db.close();
    }

    /*truncate comments*/
    public void truncateComments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_COMMENTS);
        db.close();
    }

    /*truncate perfomancecomments*/
    public void truncatePerfomanceComments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_PERFOMANCE_COMMENTS);
        db.close();
    }

    /*truncate perfomancecomments*/
    public void truncateClientsComments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_CLIENT_COMMENTS);
        db.close();
    }

    /* survey profile*/
    public void truncateSurveyProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_SURVEY_PROFILE);
        db.close();
    }

    /*Survey Photo*/
    public void truncateSurveyPhotos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBUtility.TABLE_SURVEY_PHOTOS);
        db.close();
    }

    public int getQuestionCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION, null);
        Log.e("db size", cursor.getCount() + "");
        return cursor.getCount();
    }

    public int getUserAnsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER, null);
        Log.e("db size", cursor.getCount() + "");
        return cursor.getCount();
    }

   /* public int getCartItemCount(int userID, int RestaurantID, int FoodItemID, int mealTYPE) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_CART +" where " +DBUtility.USER_ID +"="+userID +" AND " +DBUtility.RESTAURANT_ID +"="+RestaurantID +" AND "+DBUtility.FOODITEM_ID +"="+FoodItemID +" AND "+DBUtility.MEAL_TYPE +"="+mealTYPE, null);
        return cursor.getCount();
    }
    public int getISRemember() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_LOGIN, null);
        cursor.moveToFirst();
        int i = cursor.getInt(cursor.getColumnIndex(DBUtility.ISREMEMBER));
        cursor.close();

        return i;
    }
    public int getRestairantId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_CART, null);
        cursor.moveToFirst();
        int i = cursor.getInt(cursor.getColumnIndex(DBUtility.RESTAURANT_ID));
        cursor.close();

        return i;
    }
    public int getMaxCoockingTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select MAX("+DBUtility.COOKING_TIME+") from " + DBUtility.TABLE_CART, null);
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                i = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return i;
    }
    public float getSubOrderTotal() {
        float i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select SUM("+DBUtility.ORDER_TOTAL+") from " + DBUtility.TABLE_CART, null);
        if (cursor.moveToFirst()) {
            do {
               i = Float.parseFloat(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return i;
    }*/

    /*+ QUESTION_ID + " INTEGER, "
            + SURVEY_ID + " INTEGER, "
            + GROUP_ID + " INTEGER, "
            + SURVEY_NAME + " TEXT, "
            + GROUP_NAME + " TEXT, "
            + QUESTION_NAME + " TEXT " + ")";*/

    public void insertQuestion(SurveydataRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        // Log.i(TAG, "USER DATA INSERT");
        insertDashBoard.put(DBUtility.QUESTION_ID, data.getId());
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        insertDashBoard.put(DBUtility.GROUP_ID, data.getGroupId());
        insertDashBoard.put(DBUtility.SURVEY_NAME, data.getSurveyName());
        insertDashBoard.put(DBUtility.GROUP_NAME, data.getGroupName());
        insertDashBoard.put(DBUtility.QUESTION_NAME, data.getQuestionName());
        insertDashBoard.put(DBUtility.GROUP_SRNO, data.getQustionGroupSrno());
        insertDashBoard.put(DBUtility.QUESTION_SRNO, data.getQustionSrno());
        insertDashBoard.put(DBUtility.HAS_CERTIFICATE, data.getDateoflastcertificate());
        db.insert(DBUtility.TABLE_QUESTION, null, insertDashBoard);
        db.close();
    }

    public void insertQuestionAnswer(String QuestinId, String SurveyId, String GroupId, String SurveyName, String GroupName, String QuestionName, String UserId, String UserAnswer, String AnsPriority, String CreatedDateTime, String CompleteFlag, String CompleteSurveyFlag, String SyncFlag, String ProfileId, String QuestionSrno, String GroupSrno, String HasCertificate, String CertificateDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //   Log.i(TAG, "USER DATA INSERT");
        insertDashBoard.put(DBUtility.USER_ID, UserId);
        insertDashBoard.put(DBUtility.PROFILE_ID, ProfileId);
        insertDashBoard.put(DBUtility.QUESTION_ID, QuestinId);
        insertDashBoard.put(DBUtility.SURVEY_ID, SurveyId);
        insertDashBoard.put(DBUtility.GROUP_ID, GroupId);
        insertDashBoard.put(DBUtility.SURVEY_NAME, SurveyName);
        insertDashBoard.put(DBUtility.GROUP_NAME, GroupName);
        insertDashBoard.put(DBUtility.QUESTION_SRNO, QuestionSrno);
        insertDashBoard.put(DBUtility.GROUP_SRNO, GroupSrno);
        insertDashBoard.put(DBUtility.QUESTION_NAME, QuestionName);
        insertDashBoard.put(DBUtility.USER_ANSWER, UserAnswer);
        insertDashBoard.put(DBUtility.ANSWER_PRIORITY, AnsPriority);
        insertDashBoard.put(DBUtility.CREATED_DATE_TIME, CreatedDateTime);
        insertDashBoard.put(DBUtility.COMPLETE_FLAG, CompleteFlag);
        insertDashBoard.put(DBUtility.COMPLETE_SURVEYFLAG, CompleteSurveyFlag);
        insertDashBoard.put(DBUtility.SYNC_FLAG, SyncFlag);
        insertDashBoard.put(DBUtility.HAS_CERTIFICATE, HasCertificate);
        insertDashBoard.put(DBUtility.CERTIFICATE_DATE, CertificateDate);
        db.insert(DBUtility.TABLE_USER_ANSWER, null, insertDashBoard);
        // db.close();
    }

    /* Insert Profile */
    public void insertProfile(ProfileModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileID());
        insertDashBoard.put(DBUtility.USER_ID, data.getUserID());
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyID());
//        insertDashBoard.put(DBUtility.PROFILE_FUND, data.getFund());
        insertDashBoard.put(DBUtility.PROFILE_COMANAGINGAGENT, data.getCOManagingAgent());
        insertDashBoard.put(DBUtility.PROFILE_MANAGEMENTSURVEOR, data.getManagementSurveor());
        insertDashBoard.put(DBUtility.PROFILE_FACILITIESMANAGER, data.getFacilitiesManager());
        /*18-10*/
        insertDashBoard.put(DBUtility.PROFILE_SITENAME, data.getSiteName());
        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS1, data.getSiteAddress1());
        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS2, data.getSiteAddress2());
        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS3, data.getSiteAddress3());
        insertDashBoard.put(DBUtility.PROFILE_SITEPOSTALCODE, data.getSitePostalCode());
        insertDashBoard.put(DBUtility.PROFILE_REPORTPREPAREDBY, data.getReportPreparedby());
        insertDashBoard.put(DBUtility.PROFILE_REPORTCHECKEDBY, data.getReportCheckedby());
        insertDashBoard.put(DBUtility.PROFILE_AUDITDATE, data.getAuditDate());
        insertDashBoard.put(DBUtility.PROFILE_DATEOFISSUE, data.getDateofIssue());
        insertDashBoard.put(DBUtility.PROFILE_AUDITVISIT, data.getAuditVisit());
        insertDashBoard.put(DBUtility.PROFILE_CONTRACTORSPERFOMANCE, data.getContractorsPerfomance());
        insertDashBoard.put(DBUtility.PROFILE_STATUTOORYAUDITSCORE, data.getStatutoryAuditScore());
        insertDashBoard.put(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE, data.getContractorImprovePerfomance());
        insertDashBoard.put(DBUtility.PROFILE_CONSULTANTSCOMMENTS, data.getConsultantsComments());
        insertDashBoard.put(DBUtility.PROFILE_CLIENTCOMMENTS, data.getClientsComments());
        insertDashBoard.put(DBUtility.PROFILE_STATUTORYCOMMENTS, data.getStatutoryComments());
        insertDashBoard.put(DBUtility.PROFILE_CREATEDDATE, data.getCreatedDate());
        insertDashBoard.put(DBUtility.PROFILE_CREATEDTIME, data.getCreatedTime());

        db.insert(DBUtility.TABLE_SURVEY_PROFILE, null, insertDashBoard);
        db.close();
    }

    /*insert photos*/
    public void insertPhotos(PhotoModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");
        insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.SYNC_FLAG, data.getSynFlag());
        insertDashBoard.put(DBUtility.SYNC_FLAG_IMAGE, data.getSYNC_FLAG_IMAGE());
        insertDashBoard.put(DBUtility.SYNC_FLAG_SURVEYCOMPLETE, data.getSYNC_FLAG_SURVEYCOMPLETE());
        insertDashBoard.put(DBUtility.CREATED_DATE_TIME, data.getCreatedDateTime());
        db.insert(DBUtility.TABLE_SURVEY_PHOTOS, null, insertDashBoard);
        db.close();
    }

    public String copyandstoreuserans(String SurveyId, String UserId, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    /*(String QuestinId,String SurveyId, String GroupId,String SurveyName,                                              String GroupName,String                                         QuestionName,String UserId,String UserAnswer,String AnsPriority,String CreatedDateTime,String CompleteFlag,String SyncFlag)*/
                    //insertQuestionAnswer(cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)),cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)),cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME)),cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME),cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME)),UserId,"","","","0","0");
                    insertQuestionAnswer(cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME)), cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME)), UserId, "", "", "", "0", "0", "0", ProfileId, cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_SRNO)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_SRNO)), cursor.getString(cursor.getColumnIndex(DBUtility.HAS_CERTIFICATE)), "");
                } while (cursor.moveToNext());
                data = "inserted";

            } else {

                data = "not inserted";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public void insertDefaultAnswer(DefaultansRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");
        insertDashBoard.put(DBUtility.ID, data.getId());
        insertDashBoard.put(DBUtility.DEFAULT_ANSWER_PRIORITY, data.getDefaultAnswerPriority());
        insertDashBoard.put(DBUtility.DEFAULT_ANSWER, data.getDefaultAnswer());
        insertDashBoard.put(DBUtility.QUESTION_ID, data.getQuestion_id());
        insertDashBoard.put(DBUtility.GROUP_ID, data.getQuestion_group_id());
        insertDashBoard.put(DBUtility.DEFAULT_ANS_SR_NO, data.getSrno());

        db.insert(DBUtility.TABLE_DEFAULT_ANSWER, null, insertDashBoard);
        db.close();
    }
    //insert managing auditor

    public void insertManagementAuditor(ManagementAuditorRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        // Log.i(TAG, "USER DATA INSERT");

        insertDashBoard.put(DBUtility.ID, data.getId());
        insertDashBoard.put(DBUtility.PROFILE_MANAGEMENT_AUDITOR_NAME, data.getName());


        db.insert(DBUtility.TABLE_MANAGEMENT_AUDITOR, null, insertDashBoard);
        db.close();
    }

    /*Update managing auditor*/
    public void UpdateManagementAuditor(ManagementAuditorRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        insertDashBoard.put(DBUtility.ID, data.getId());
        insertDashBoard.put(DBUtility.PROFILE_MANAGEMENT_AUDITOR_NAME, data.getName());
        db.update(DBUtility.TABLE_MANAGEMENT_AUDITOR, insertDashBoard, DBUtility.ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }

    public String[] getSurveyList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct(" + DBUtility.SURVEY_NAME + ") from " + DBUtility.TABLE_QUESTION, null);
        Log.e("db size", cursor.getCount() + "");
        String[] data = null;
        int i = 0;

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                data = new String[cursor.getCount() + 1];
                data[i] = "Select Survey";
                i++;
                do {
                    data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    i++;
                } while (cursor.moveToNext());
            } else {
                data = new String[1];
                data[i] = "No Survey Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public String getSurveyId(String SurveyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBUtility.SURVEY_ID + " from " + DBUtility.TABLE_QUESTION + " where surveyname='" + SurveyName + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                data = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
            } else {
                data = "No Survey Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public String getGroupId(String SurveyId, String GroupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBUtility.GROUP_ID + " from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.GROUP_NAME + " ='" + GroupName + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                data = cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID));
            } else {
                data = "No group id Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    /*group id from question id*/

    public String getGroupIdfromquestionid(String SurveyId, String QuestionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBUtility.GROUP_ID + " from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.QUESTION_ID + " ='" + QuestionId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {


                data = cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID));


            } else {

                data = "No group id Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;

    }

    /*group name from question id*/

    public String getGroupNamefromquestionid(String SurveyId, String QuestionId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBUtility.GROUP_NAME + " from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.QUESTION_ID + " ='" + QuestionId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {


                data = cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME));


            } else {

                data = "No group id Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;

    }

    /*group name from question id*/

    public String getGroupNamefromGroupid(String SurveyId, String GroupId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBUtility.GROUP_NAME + " from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {


                data = cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME));


            } else {

                data = "No group id Found";
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;

    }

    /*get incomplete groupid*/
    public String[] getIncompleteGroupId(String SurveyId, String UserId) {

        SQLiteDatabase db = this.getReadableDatabase();
        // select groupid,* from useranswer where surveyid=2 and CompleteFlag = 0  and user_id =1 limit 1
        Cursor cursor = db.rawQuery("select " + DBUtility.GROUP_ID + ", " + DBUtility.QUESTION_ID + " from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.COMPLETE_FLAG + " =0 limit 1", null);
        Log.e("db size", cursor.getCount() + "");
        String[] data = new String[2];

        try {

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {


                data[0] = cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID));
                data[1] = cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID));


            } else {

                data = null;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public ArrayList<QuestionGroupModel> getQuestionGroupModelArrayList(String SurveyID, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct(" + DBUtility.GROUP_NAME + ")," + DBUtility.GROUP_ID + " from " + DBUtility.TABLE_USER_ANSWER + " where surveyid=" + SurveyID + " and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' order by " + DBUtility.GROUP_SRNO, null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<QuestionGroupModel> data = new ArrayList<>();
        int i = 0;
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                do {


                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new QuestionGroupModel(cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME)), "1"));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }


        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return data;
    }


    public Cursor getQuestion(String SurveId, String GroupId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "'" + " order by " + DBUtility.QUESTION_ID + " asc  limit 1", null);
        return cursor;

    }

    /*getquestion from userans table*/
    public Cursor getQuestionUserAns(String SurveId, String GroupId, String UserId, String ProfileId) {

        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.COMPLETE_SURVEYFLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);

        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.SYNC_FLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        return cursor;


    }

    /*get checking any question is reming in question group 19-11-2016*/

    public Cursor getQuestionUserAnsRemaining(String SurveId, String GroupId, String UserId, String ProfileId) {

        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.COMPLETE_SURVEYFLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.COMPLETE_FLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        return cursor;

    }

    public Cursor getNextQuestion(String SurveId, String GroupId, String QuestionId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + " and " + DBUtility.QUESTION_ID + " > " + QuestionId + " order by " + DBUtility.QUESTION_ID + " asc  limit 1";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.QUESTION_ID + " > " + QuestionId + " order by " + DBUtility.QUESTION_ID + " asc  limit 1", null);
        return cursor;

    }

    /*19-11-2016*/
    public Cursor getDrawerQuestionUserAns(String SurveId, String GroupId, String QuestionId, String UserId, String ProfileId, String QuestionSrno) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + " and " + DBUtility.QUESTION_ID + " = '" + QuestionId + "' order by " + DBUtility.QUESTION_ID + " asc  limit 1";
        Log.d("Query", query);
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND "  +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.COMPLETE_SURVEYFLAG+" = 0 and " + DBUtility.GROUP_ID + " ='"+GroupId+ "' and "+DBUtility.PROFILE_ID+ " ='"+ProfileId+"' and "+DBUtility.QUESTION_SRNO+" > "+ QuestionSrno+" order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.SYNC_FLAG + " = 0 and " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.QUESTION_SRNO + " = '" + QuestionSrno + "' order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        return cursor;
    }

    public Cursor getNextQuestionUserAns(String SurveId, String GroupId, String QuestionId, String UserId, String ProfileId, String QuestionSrno) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + " and " + DBUtility.QUESTION_ID + " > " + QuestionId + " order by " + DBUtility.QUESTION_ID + " asc  limit 1";
        Log.d("Query", query);
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND "  +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.COMPLETE_SURVEYFLAG+" = 0 and " + DBUtility.GROUP_ID + " ='"+GroupId+ "' and "+DBUtility.PROFILE_ID+ " ='"+ProfileId+"' and "+DBUtility.QUESTION_SRNO+" > "+ QuestionSrno+" order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.SYNC_FLAG + " = 0 and " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.QUESTION_SRNO + " > " + QuestionSrno + " order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
        return cursor;
    }

    public Cursor getPreviousQuestion(String SurveId, String GroupId, String QuestionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.QUESTION_ID + " < " + QuestionId + " order by " + DBUtility.QUESTION_ID + " desc  limit 1";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.QUESTION_ID + " < " + QuestionId + " order by " + DBUtility.QUESTION_ID + " desc  limit 1", null);
        return cursor;
    }

    /* User Answer*/
    public Cursor getPreviousQuestionUserAns(String SurveId, String GroupId, String QuestionId, String UserId, String ProfileId, String QuestionSrno) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.QUESTION_ID + " < " + QuestionId + " order by " + DBUtility.QUESTION_ID + " desc  limit 1";
        Log.d("Query", query);
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND "  +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.COMPLETE_SURVEYFLAG+" =0 and "  + DBUtility.GROUP_ID + " ='"+GroupId+ "' and "+DBUtility.QUESTION_SRNO+" < "+ QuestionSrno+" order by " + DBUtility.QUESTION_SRNO + " desc  limit 1", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.SYNC_FLAG + " =0 and " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.QUESTION_SRNO + " < " + QuestionSrno + " order by " + DBUtility.QUESTION_SRNO + " desc  limit 1", null);
        return cursor;
    }

    public Cursor checkIncompleteSurvey(String UserId, String SurveyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.COMPLETE_FLAG + " =0 " , null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "'" + " AND " + DBUtility.SYNC_FLAG + " =0 ", null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
              if(cursor != null) {
                cursor.close();
            }
        }*/
    }

    public Cursor checkIncompleteSurveyLogin(String UserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.COMPLETE_SURVEYFLAG + " =0 " , null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'" + " AND " + DBUtility.SYNC_FLAG + " =0 ", null);

        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.SYNC_FLAG + " =0 " , null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    public Cursor checkIncompleteSurveyLogin1(String UserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'" + " AND " + DBUtility.COMPLETE_FLAG + " =0 ", null);
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.COMPLETE_SURVEYFLAG + " =0 " , null);

        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.SYNC_FLAG + " =0 " , null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    /*Get Userinfo*/
    public Cursor getUserInfo(String SurveId, String UserId, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DBUtility.TABLE_SURVEY_PROFILE + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "'";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    public Cursor getsurveyProfiledata(String UserId, String SurveyId, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PROFILE + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "'" + " AND " + DBUtility.PROFILE_ID + " ='" + ProfileId + "'", null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    /*getComplete Survey data profile id wise*/
    public Cursor getcompleteSurvey(String UserId, String SurveyId, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile" +
                "      ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 and useranswer.USER_ID ='" + UserId + "' and useranswer.SurveyID ='" + SurveyId + "' and useranswer.ProfileID ='" + ProfileId + "'";
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' AND " + DBUtility.USER_ID + " ='" + UserId + "'"+ " AND " + DBUtility.PROFILE_ID+" ='"+"' AND "+DBUtility.COMPLETE_FLAG + " =1 " , null);
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    /*getComplete all Survey data group by profile total no of survey*/
    public Cursor getallcompleteSurvey() {
        //"SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 group by useranswer.ProfileIDSELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 group by useranswer.ProfileID"
        String Query = "SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 group by useranswer.ProfileID";
        /*String Query="SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile" +
                "      ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 ";
*/
        Log.d("qry", Query + "");
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " +DBUtility.COMPLETE_FLAG + " =1 " , null);
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    /*getComplete all Survey data profile wise use answery*/
    public Cursor getallcompleteSurveyAnsProfileid(String ProfileId) {
        //"SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 and useranswer.ProfileID='165f5e88a6c2b2cd_03/10/2016 02:38:20'"
        String Query = "SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 and useranswer.ProfileID='" + ProfileId + "'";
        /*String Query="SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile" +
                "      ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 ";
*/
        Log.d("qry", Query + "");
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " +DBUtility.COMPLETE_FLAG + " =1 " , null);
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        return cursor;
        /*try {
            return cursor;
        }
        finally {
            cursor.close();
        }*/
    }

    /*Default Answer */
    public ArrayList<DefaultAnswerModel> getDefaultAnswerModelArrayList(int GroupId, int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_DEFAULT_ANSWER+ " where " + DBUtility.GROUP_ID + " =" + GroupId + " and " + DBUtility.QUESTION_ID + " =" + questionId, null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<DefaultAnswerModel> data = new ArrayList<>();
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    // data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)), cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                    Log.e("DEFAULT::",""+data.size());
                } while (cursor.moveToNext());
            } else {
                data = null;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    /* auditor name */
    public ArrayList<managingAuditorModel> getmanagingAuditorModelArrayList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_MANAGEMENT_AUDITOR, null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<managingAuditorModel> data = new ArrayList<>();
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = crsor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new managingAuditorModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)), cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENT_AUDITOR_NAME))));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    //question name
    public ArrayList<QuestionNameModel> getQuestionNameModelArrayList(String SurveId, String GroupId, String UserId, String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " + DBUtility.USER_ID + " ='" + UserId + "' and " + DBUtility.PROFILE_ID + " ='" + ProfileId + "' and " + DBUtility.SYNC_FLAG + " = 0 ", null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<QuestionNameModel> data = new ArrayList<>();

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new QuestionNameModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)), cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME))));
                } while (cursor.moveToNext());
            } else {
                data = null;
            }
        } finally {
            cursor.close();
        }
        return data;
    }

    /*Default Answer Conditional */
    //Manisha added groupId,questionId 7-2-19
    public ArrayList<DefaultAnswerModel> getDefaultAnswerModelArrayList(String Priority,int groupId,int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<DefaultAnswerModel> data = new ArrayList<>();
        try {
            cursor = db.rawQuery("select * from " + DBUtility.TABLE_DEFAULT_ANSWER + " where " + DBUtility.DEFAULT_ANSWER_PRIORITY + "='" + Priority + "'" + " and " + DBUtility.GROUP_ID + " =" + groupId + " and " + DBUtility.QUESTION_ID + " =" + questionId+" order by "+DBUtility.DEFAULT_ANS_SR_NO, null);
            Log.e("db size", cursor.getCount() + "");

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)), cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                } while (cursor.moveToNext());
            } else {
                data = null;
            }
        }catch (Exception e){
            e.printStackTrace();

        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public ArrayList<DefaultAnswerModel> getArraylistSurveyList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct("+DBUtility.SURVEY_NAME+"), " +DBUtility.SURVEY_ID+" from " + DBUtility.TABLE_QUESTION   , null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<DefaultAnswerModel> data = new ArrayList<>();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                do {

                    data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME))));


                } while (cursor.moveToNext());
            } else {

                data = null;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }

    /*Update User Answer*/

    public long updateUserAns(QuestionAnswerModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateUserAns = new ContentValues();
        Log.i(TAG, "User Cart Data Update");
        //updateUserAns.put(DBUtility.USER_ID, data.getUserId());
        // updateUserAns.put(DBUtility.QUESTION_ID, data.getQuestionId());
        // updateUserAns.put(DBUtility.SURVEY_ID, data.getSurveyId());
        // updateUserAns.put(DBUtility.GROUP_ID, data.getGroupId());
        //updateUserAns.put(DBUtility.SURVEY_NAME, data.getSurveyName());
        // updateUserAns.put(DBUtility.GROUP_NAME, data.getGroupName());
        //updateUserAns.put(DBUtility.QUESTION_NAME, data.getQuestionName());

        updateUserAns.put(DBUtility.USER_ANSWER, data.getUserAnswer());
        updateUserAns.put(DBUtility.ANSWER_PRIORITY, data.getAnswerPriority());
        updateUserAns.put(DBUtility.CREATED_DATE_TIME, data.getCreateDateTime());
        updateUserAns.put(DBUtility.COMPLETE_FLAG, data.getCompleteFlag());
        updateUserAns.put(DBUtility.SYNC_FLAG, data.getSyncFlag());
        updateUserAns.put(DBUtility.CERTIFICATE_DATE, data.getCertificateDate());

        long a = db.update(DBUtility.TABLE_USER_ANSWER,
                updateUserAns,
                DBUtility.USER_ID + " = ? AND " + DBUtility.SURVEY_ID + " = ? AND " + DBUtility.GROUP_ID + " = ? AND " + DBUtility.QUESTION_ID + " = ? AND "+ DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(data.getUserId()),String.valueOf(data.getSurveyId()),String.valueOf(data.getGroupId()),String.valueOf(data.getQuestionId()),String.valueOf(data.getProfileId())});
        //  int a =db.rawQuery("UPDATE "+ DBUtility.TABLE_CART+ "  SET "+DBUtility.QUANTITY +"="+ quantitiy +" WHERE "+ some_column=some_value ");
        Log.v("update>>","update>>"+a);
        return a;
        // int a = db.update(DBUtility.TABLE_CART, updateCart, DBUtility.QUANTITY + "=" + quantitiy, null);
    }

    // khush 13/08/2019 group prority not applicable
    public long groupPriorityNotApplicable(QuestionAnswerModel data) {
        Log.d("find", "groupPriorityNotApplicable: "+data.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateUserAns = new ContentValues();
        Log.i(TAG, "User Cart Data Update");

      //  updateUserAns.put(DBUtility.QUESTION_ID, data.getQuestionId());
      //  updateUserAns.put(DBUtility.SURVEY_NAME, data.getSurveyName());
      //  updateUserAns.put(DBUtility.GROUP_NAME, data.getGroupName());
      //  updateUserAns.put(DBUtility.QUESTION_NAME, data.getQuestionName());

        updateUserAns.put(DBUtility.ANSWER_PRIORITY, "Not Applicable");
        updateUserAns.put(DBUtility.CREATED_DATE_TIME, data.getCreateDateTime());
        updateUserAns.put(DBUtility.COMPLETE_FLAG, data.getCompleteFlag());
        updateUserAns.put(DBUtility.SYNC_FLAG, data.getSyncFlag());
        updateUserAns.put(DBUtility.USER_ANSWER, "");
        updateUserAns.put(DBUtility.CERTIFICATE_DATE, "");

        //  updateUserAns.put(DBUtility.CERTIFICATE_DATE, data.getCertificateDate());
        long a = db.update(DBUtility.TABLE_USER_ANSWER,
                updateUserAns,
                DBUtility.USER_ID + " = ? AND " + DBUtility.SURVEY_ID + " = ? AND " + DBUtility.GROUP_ID + " = ? AND " + DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(data.getUserId()),String.valueOf(data.getSurveyId()),String.valueOf(data.getGroupId()),String.valueOf(data.getProfileId())});
        //  int a =db.rawQuery("UPDATE "+ DBUtility.TABLE_CART+ "  SET "+DBUtility.QUANTITY +"="+ quantitiy +" WHERE "+ some_column=some_value ");
        Log.v("update>>","update>>"+updateUserAns.toString());
        return a;
        // int a = db.update(DBUtility.TABLE_CART, updateCart, DBUtility.QUANTITY + "=" + quantitiy, null);
    }

    /* Update surveycomplete flag after filling all the group questions */
    public long updateUserAnsSurveyFlag(String SurveyId,String UserId,String ProfileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateUserAns = new ContentValues();
        Log.i(TAG, "User Cart Data Update");

        updateUserAns.put(DBUtility.COMPLETE_SURVEYFLAG,"1");
        long a = db.update(DBUtility.TABLE_USER_ANSWER,
                updateUserAns,
                DBUtility.USER_ID + " = ? AND " + DBUtility.SURVEY_ID + " = ? AND " + DBUtility.PROFILE_ID + " = ? ",
                new String[]{String.valueOf(UserId),String.valueOf(SurveyId),String.valueOf(ProfileId)});
        //  int a =db.rawQuery("UPDATE "+ DBUtility.TABLE_CART+ "  SET "+DBUtility.QUANTITY +"="+ quantitiy +" WHERE "+ some_column=some_value ");
        Log.v("update>>","update>>"+a);
        return a;
        // int a = db.update(DBUtility.TABLE_CART, updateCart, DBUtility.QUANTITY + "=" + quantitiy, null);
    }

    /*Update syncflag flag after sync withserver */
    public long updateUserAnsSyncFlag(String UserId,String SurveyId,String ProfileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateUserAns = new ContentValues();
        Log.i(TAG, "User Cart Data Update");
        updateUserAns.put(DBUtility.SYNC_FLAG,"1");
        long a = db.update(DBUtility.TABLE_USER_ANSWER,
                updateUserAns,
                DBUtility.USER_ID + " = ? AND " + DBUtility.SURVEY_ID + " = ? AND " +DBUtility.PROFILE_ID+ " = ? AND "+ DBUtility.COMPLETE_SURVEYFLAG + " = ?  AND " + DBUtility.SYNC_FLAG + " = ? ",
                new String[]{String.valueOf(UserId),String.valueOf(SurveyId),String.valueOf(ProfileId),String.valueOf("1"),String.valueOf("0")});
        //  int a =db.rawQuery("UPDATE "+ DBUtility.TABLE_CART+ "  SET "+DBUtility.QUANTITY +"="+ quantitiy +" WHERE "+ some_column=some_value ");
        Log.v("update>>","update>>"+a);
        return a;
        // int a = db.update(DBUtility.TABLE_CART, updateCart, DBUtility.QUANTITY + "=" + quantitiy, null);
    }



    /*Update Question Table*/
    public void UpdateQuestion(SurveydataRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        insertDashBoard.put(DBUtility.QUESTION_ID, data.getId());
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        insertDashBoard.put(DBUtility.GROUP_ID, data.getGroupId());
        insertDashBoard.put(DBUtility.SURVEY_NAME, data.getSurveyName());
        insertDashBoard.put(DBUtility.GROUP_NAME, data.getGroupName());
        insertDashBoard.put(DBUtility.QUESTION_NAME, data.getQuestionName());

        db.update(DBUtility.TABLE_QUESTION,insertDashBoard,DBUtility.QUESTION_ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }


    /*Update default ans*/
    public void UpdateDefaultAnswer(DefaultansRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //Log.i(TAG, "USER DATA INSERT");

        insertDashBoard.put(DBUtility.ID, data.getId());
        insertDashBoard.put(DBUtility.DEFAULT_ANSWER, data.getDefaultAnswer());
        insertDashBoard.put(DBUtility.DEFAULT_ANSWER_PRIORITY, data.getDefaultAnswerPriority());
        insertDashBoard.put(DBUtility.GROUP_ID, data.getQuestion_group_id());
        insertDashBoard.put(DBUtility.QUESTION_ID, data.getQuestion_id());
        insertDashBoard.put(DBUtility.DEFAULT_ANS_SR_NO, data.getSrno());

        db.update(DBUtility.TABLE_DEFAULT_ANSWER,  insertDashBoard,DBUtility.ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }


    /*Update User Profile*/

    public void UpdateProfile(ProfileModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");




        // insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileID());
        // insertDashBoard.put(DBUtility.USER_ID, data.getUserID());
        // insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyID());
//        insertDashBoard.put(DBUtility.PROFILE_FUND, data.getFund());
        insertDashBoard.put(DBUtility.PROFILE_COMANAGINGAGENT, data.getCOManagingAgent());
        insertDashBoard.put(DBUtility.PROFILE_MANAGEMENTSURVEOR, data.getManagementSurveor());
        insertDashBoard.put(DBUtility.PROFILE_FACILITIESMANAGER, data.getFacilitiesManager());
        /*new update 18-10*/

        insertDashBoard.put(DBUtility.PROFILE_SITENAME, data.getSiteName());
        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS1, data.getSiteAddress1());

        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS2, data.getSiteAddress2());
        insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS3, data.getSiteAddress3());
        insertDashBoard.put(DBUtility.PROFILE_SITEPOSTALCODE, data.getSitePostalCode());

        insertDashBoard.put(DBUtility.PROFILE_REPORTPREPAREDBY, data.getReportPreparedby());
        insertDashBoard.put(DBUtility.PROFILE_REPORTCHECKEDBY, data.getReportCheckedby());
        insertDashBoard.put(DBUtility.PROFILE_AUDITDATE, data.getAuditDate());
        insertDashBoard.put(DBUtility.PROFILE_DATEOFISSUE, data.getDateofIssue());
        insertDashBoard.put(DBUtility.PROFILE_AUDITVISIT, data.getAuditVisit());
        // insertDashBoard.put(DBUtility.PROFILE_CONTRACTORSPERFOMANCE, data.getContractorsPerfomance());
        // insertDashBoard.put(DBUtility.PROFILE_STATUTOORYAUDITSCORE, data.getStatutoryAuditScore());
        //  insertDashBoard.put(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE, data.getContractorImprovePerfomance());
        // insertDashBoard.put(DBUtility.PROFILE_CONSULTANTSCOMMENTS, data.getConsultantsComments());
        // insertDashBoard.put(DBUtility.PROFILE_CREATEDDATE, data.getCreatedDate());
        // insertDashBoard.put(DBUtility.PROFILE_CREATEDTIME, data.getCreatedTime());


        db.update(DBUtility.TABLE_SURVEY_PROFILE, insertDashBoard,DBUtility.PROFILE_ID + " = ? AND "+DBUtility.USER_ID+" = ? AND "+DBUtility.SURVEY_ID+" = ?",
                new String[]{String.valueOf(data.getProfileID()),String.valueOf(data.getUserID()),String.valueOf(data.getSurveyID())});
        db.close();
    }

    /*Update Summary*/
    public void UpdateSummary(ProfileModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");
        // insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileID());
        // insertDashBoard.put(DBUtility.USER_ID, data.getUserID());
        // insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyID());
        // insertDashBoard.put(DBUtility.PROFILE_FUND, data.getFund());
        //insertDashBoard.put(DBUtility.PROFILE_COMANAGINGAGENT, data.getCOManagingAgent());
        // insertDashBoard.put(DBUtility.PROFILE_MANAGEMENTSURVEOR, data.getManagementSurveor());
        // insertDashBoard.put(DBUtility.PROFILE_FACILITIESMANAGER, data.getFacilitiesManager());
        // insertDashBoard.put(DBUtility.PROFILE_SITEADDRESS, data.getSiteAddress());
        //insertDashBoard.put(DBUtility.PROFILE_REPORTPREPAREDBY, data.getReportPreparedby());
        // insertDashBoard.put(DBUtility.PROFILE_REPORTCHECKEDBY, data.getReportCheckedby());
        //insertDashBoard.put(DBUtility.PROFILE_AUDITDATE, data.getAuditDate());
        // insertDashBoard.put(DBUtility.PROFILE_DATEOFISSUE, data.getDateofIssue());
        // insertDashBoard.put(DBUtility.PROFILE_AUDITVISIT, data.getAuditVisit());

        insertDashBoard.put(DBUtility.PROFILE_CONTRACTORSPERFOMANCE, data.getContractorsPerfomance());
        insertDashBoard.put(DBUtility.PROFILE_STATUTOORYAUDITSCORE, data.getStatutoryAuditScore());
        insertDashBoard.put(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE, data.getContractorImprovePerfomance());
        insertDashBoard.put(DBUtility.PROFILE_CONSULTANTSCOMMENTS, data.getConsultantsComments());
        insertDashBoard.put(DBUtility.PROFILE_CLIENTCOMMENTS,data.getClientsComments());
        insertDashBoard.put(DBUtility.PROFILE_STATUTORYCOMMENTS, data.getStatutoryComments());
        // insertDashBoard.put(DBUtility.PROFILE_CREATEDDATE, data.getCreatedDate());
        // insertDashBoard.put(DBUtility.PROFILE_CREATEDTIME, data.getCreatedTime());

        db.update(DBUtility.TABLE_SURVEY_PROFILE, insertDashBoard,DBUtility.PROFILE_ID + " = ? AND "+DBUtility.USER_ID+" = ? AND "+DBUtility.SURVEY_ID+" = ?",
                new String[]{String.valueOf(data.getProfileID()),String.valueOf(data.getUserID()),String.valueOf(data.getSurveyID())});
        db.close();
    }

    /*For navigation Drawer*/
    public List<QuestionGroupModel> getQuestionGroupModelArrayListDrawer(String SurveyID,String UserId,String ProfileId) {
        /*select groupid,groupname , count(1) as TotalGroupCount
        from useranswer where surveyid=1 and completeflag=0
        group by groupname*/
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Log.e("db size", cursor.getCount() + "");
        Log.e("query", cursor.getCount() + "");
        List<QuestionGroupModel> data = new ArrayList<>();
        int i=0;
        cursor.moveToFirst();

        try {
            if (cursor.getCount() > 0) {

                do {


                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new QuestionGroupModel(cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME)), cursor.getString(cursor.getColumnIndex("TotalCount"))));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }

    /*Check survey has question or not*/
    public boolean checkSurveyHasQuestion(String SurveyId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_QUESTION + " where " + DBUtility.SURVEY_ID + "='" + SurveyId + "' " , null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() > 0) {
                if (cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)) != null && cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME)) != null && cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME)) != null) {
                    return true;
                } else {
                    return false;
                }


            } else {
                return false;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /*Mena*/
//write by mena
    public ArrayList<PhotoModel> getimagepathAndcomment(String SurveyID,String ProfileId,String user_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =0 and "+DBUtility.PHOTO_COMMENT+" !='ProfileImage_"+ProfileId+"'", null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<PhotoModel> data = new ArrayList<>();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new PhotoModel(cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_PATH)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_COMMENT)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.CREATED_DATE_TIME)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_SURVEYCOMPLETE))));
                } while (cursor.moveToNext());
            } else {
                data = new ArrayList<>();

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public ArrayList<PhotoModel> getimagepathAndcommentfinal(String SurveyID,String ProfileId,String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");

        ArrayList<PhotoModel> data = new ArrayList<>();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new PhotoModel(cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_PATH)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_COMMENT)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.CREATED_DATE_TIME)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_SURVEYCOMPLETE))));
                } while (cursor.moveToNext());
            } else {
                data = null;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    /*amitk get one by one image from db*/
    public PhotoModel getonebyoneimage(String SurveyID,String ProfileId,String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");
        PhotoModel data = new PhotoModel();
        int i=0;
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                data = new PhotoModel(cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_PATH)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_COMMENT)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.CREATED_DATE_TIME)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_SURVEYCOMPLETE)));

            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    /*amtik total count pending image*/
    public int getonebyoneimagecount(String SurveyID,String ProfileId,String user_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");

        int count=0;
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                count = cursor.getCount();

                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));


            } else {
                count = 0;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }



        return count;
    }



    /*amitk get one by one image from db for all pending image to be synced*/
    public PhotoModel getonebyoneimagePending()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");

        PhotoModel data = new PhotoModel();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                data = new PhotoModel(cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_PATH)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_COMMENT)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.CREATED_DATE_TIME)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(DBUtility.SYNC_FLAG_SURVEYCOMPLETE)));

            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }



        return data;
    }

    public int getonebyoneimagePendingcount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");

        int data =0;
        int i=0;


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                data = cursor.getCount();

            } else {
                data = 0;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }



        return data;
    }

    /*amitk 15-2-17 by group pending image*/

    public int getonebyoneimagePendingcountbyGroup(String SurveyId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.PROFILE_ID+" ='"+SurveyId+"' "+" and "+DBUtility.SYNC_FLAG+" =1 "+" and "+DBUtility.SYNC_FLAG_SURVEYCOMPLETE+" =1 "+" and "+DBUtility.SYNC_FLAG_IMAGE+" =0 ", null);
        Log.e("db size", cursor.getCount() + "");

        int data =0;
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                data = cursor.getCount();

            } else {
                data = 0;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }


    public String getSurveyName(String ProfileId)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select "+DBUtility.SURVEY_NAME+" from " + DBUtility.TABLE_USER_ANSWER +" where "+DBUtility.PROFILE_ID+"='"+ProfileId+"' ", null);
        Log.e("db size", cursor.getCount() + "");
        String data = "";


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                data = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));


            } else {

                data = "SurveyName";
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;

    }


    public String getSurveyNameSiteName(String ProfileId)
    {
        //"SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 and useranswer.ProfileID='165f5e88a6c2b2cd_03/10/2016 02:38:20'"
        String data = "";
        String Query=       "SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=1 and useranswer.ProfileID='"+ProfileId+"'";
        /*String Query="SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile" +
                "      ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1  and useranswer.SyncFlag=0 ";
*/
        Log.d("qry",Query+"");
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " +DBUtility.COMPLETE_FLAG + " =1 " , null);
        Cursor cursor = db.rawQuery(Query , null);
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {


                data = "Site Name: " + cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME)) + " \n" + cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));


            } else {

                data = "SurveyName";
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return data;
    }

//write by mena

//write by mena

    public long DeleteAttachedImage(String  user_id,String survey_id,String profile_id,String photo_comment,String photopath) {
        SQLiteDatabase db = this.getWritableDatabase();




        long a = db.delete(DBUtility.TABLE_SURVEY_PHOTOS,
                DBUtility.USER_ID + " = ? AND " + DBUtility.SURVEY_ID + " = ? AND " + DBUtility.PROFILE_ID + " = ? AND " + DBUtility.PHOTO_COMMENT + " = ? AND " + DBUtility.PHOTO_PATH + " = ?",
                new String[]{String.valueOf(user_id),String.valueOf(survey_id),String.valueOf(profile_id),String.valueOf(photo_comment),String.valueOf(photopath)});
        db.close();

        return  a;
    }
//write by mena


    /*upadate  photos flag*/

    public long updatePhotosflag(String Proileid,String Userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        //insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        //insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        //insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        //insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        //insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.SYNC_FLAG,"1");

        long a =db.update(DBUtility.TABLE_SURVEY_PHOTOS, insertDashBoard,DBUtility.PROFILE_ID + " = ? and "+DBUtility.USER_ID+" = ? ",new String[]{String.valueOf(Proileid),String.valueOf(Userid)});
        db.close();
        return a;
    }

    /*upadate  photos survery flag 31-1-17 amitk*/
    public long updatePhotosflagComplete(String Proileid,String Userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //    Log.i(TAG, "USER DATA INSERT");
        //insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        //insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        //insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        //insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        //insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.SYNC_FLAG_SURVEYCOMPLETE,"1");

        long a =db.update(DBUtility.TABLE_SURVEY_PHOTOS, insertDashBoard,DBUtility.PROFILE_ID + " = ? and "+DBUtility.USER_ID+" = ? ",new String[]{String.valueOf(Proileid),String.valueOf(Userid)});
        db.close();
        return a;
    }

    /*upadate  photos survery flag 31-1-17 amitk*/

    public long updatePhotosflagCompleteSurveyData(String Proileid,String Userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //   Log.i(TAG, "USER DATA INSERT");

        //insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        //insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        //insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        //insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        //insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.SYNC_FLAG,"1");

        long a =db.update(DBUtility.TABLE_SURVEY_PHOTOS, insertDashBoard,DBUtility.PROFILE_ID + " = ? and "+DBUtility.USER_ID+" = ? ",new String[]{String.valueOf(Proileid),String.valueOf(Userid)});
        db.close();
        return a;
    }

    /*upadate  photos sync flag 31-1-17 amitk*/

    public long updatePhotosSyncFlag(String Proileid,String Userid,String filepath,String Comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        //insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        //insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        //insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        //insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        //insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.SYNC_FLAG_IMAGE,"1");

        long a =db.update(DBUtility.TABLE_SURVEY_PHOTOS, insertDashBoard,DBUtility.PROFILE_ID + " = ? and "+DBUtility.USER_ID+" = ? and "+DBUtility.PHOTO_PATH+" = ? and "+DBUtility.PHOTO_COMMENT+" = ? ",new String[]{String.valueOf(Proileid),String.valueOf(Userid),String.valueOf(filepath),String.valueOf(Comment)});
        db.close();
        return a;
    }



    /*Delete Sync data from local userans*/
   /* public long DeleteSyncUserAnswer() {
        SQLiteDatabase db = this.getWritableDatabase();




        long a = db.delete(DBUtility.TABLE_USER_ANSWER,
                DBUtility.SYNC_FLAG + " = ? ",
                new String[]{String.valueOf("1")});
        db.close();
        if (a>0) {
            Log.d("Delete Sync Answer", a + "Sync UserAns deleted");
        }
        else
        {
            Log.d("Delete Sync Answer", a + "Not deleted UserAnswer");
        }

        return  a;
    }

    *//*Delete Sync Photos from local userans*//*
    public long DeleteSyncUserPhotos() {
        SQLiteDatabase db = this.getWritableDatabase();




        long a = db.delete(DBUtility.TABLE_SURVEY_PHOTOS,
                DBUtility.SYNC_FLAG + " = ? ",
                new String[]{String.valueOf("1")});
        db.close();


        if (a>0) {
            Log.d("Delete Sync Photo", a + "Sync Photo deleted");
        }
        else
        {
            Log.d("Delete Sync Photo", a + "Not deleted ");
        }
        return  a;
    }*/


    public boolean checkprofilecomplete(String ProfileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

       /* String query ="SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"'  and fund is not null and fund !=''  and COManagingAgent is not null and COManagingAgent !='' and ManagementSurveor is not null and ManagementSurveor !='' \n" +
                "and SiteAddress is not null and SiteAddress !=''  and ReportPreparedby is not null and ReportPreparedby !=''\n" +
                " and ReportCheckedby is not null and ReportCheckedby !='' and AuditDate is not null and AuditDate !='' and DateofIssue is not null and DateofIssue !=''   and  AuditVisit is not null and AuditVisit !='';";*/

      /*  String query =  "SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"'  and fund is not null and fund !=''  and COManagingAgent is not null and COManagingAgent !='' and ManagementSurveor is not null and ManagementSurveor !='' \n" +
        "and SiteName is not null and SiteName !=''  and SiteAddress1 is not null and SiteAddress1 !='' and SitePostalCode is not null and SitePostalCode !=''   and ReportPreparedby is not null and ReportPreparedby !=''\n" +
                " and ReportCheckedby is not null and ReportCheckedby !='' and AuditDate is not null and AuditDate !='' and DateofIssue is not null and DateofIssue !=''   and  AuditVisit is not null and AuditVisit !='';";

      */

      /*  String query =  "SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"' " +



                "and SiteName is not null and SiteName !=''  " +


                "and ReportPreparedby is not null and ReportPreparedby !=''\n" +

                " and AuditDate is not null and AuditDate !=''" +
                " and DateofIssue is not null and DateofIssue !='';";*/



        String query =  "SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"' " +



                "and SiteName is not null and SiteName !=''  " +


                "and ReportPreparedby is not null and ReportPreparedby !=''\n" +

                " and AuditDate is not null and AuditDate !='';";



        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() > 0) {
            /*if (cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID))!=null&&cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME))!=null&&cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME))!=null)
            {
                return true;
            }
            else
            {
                return false;
            }
*/
                return true;

            } else {
                return false;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }


    public boolean checksummary(String ProfileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        /*String query ="SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"'  and ContractorsPerfomance is not null and ContractorsPerfomance !=''  and StatutoryAuditScore is not null and StatutoryAuditScore !='' and ContractorImprovePerfomance is not null and ContractorImprovePerfomance !='' \n" +
                "and ConsultantsComments is not null and ConsultantsComments !='' ";*/
        /*3-1-17*/
        String query ="SELECT * FROM  surveyprofile where ProfileID='"+ProfileId+"'  and ContractorsPerfomance is not null and ContractorsPerfomance !='' and" + " ConsultantsComments is not null and ConsultantsComments !='' ";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
            /*if (cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID))!=null&&cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME))!=null&&cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME))!=null)
            {
                return true;
            }
            else
            {
                return false;
            }
*/
                return true;

            } else {
                return false;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }



    public boolean getProfileimagepathAndcomment(String SurveyID,String ProfileId,String user_id,String Photocomment)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PHOTO_COMMENT+"='"+Photocomment+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =0", null);
        Log.e("db size", cursor.getCount() + "");
        boolean hasimage= false;


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                hasimage = true;
            } else {
                hasimage = false;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return hasimage;



    }
    /*Get profile photo*/
    public String getprofilephoto(String SurveyID,String ProfileId,String user_id,String Photocomment)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PHOTOS +" where "+DBUtility.SURVEY_ID+" = '"+SurveyID+"'"+" and "+DBUtility.USER_ID+" ='"+user_id+"' and "+DBUtility.PHOTO_COMMENT+"='"+Photocomment+"' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG+" =0", null);
        Log.e("db size", cursor.getCount() + "");
        String  hasimage= "";


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                hasimage = cursor.getString(cursor.getColumnIndex(DBUtility.PHOTO_PATH));
            } else {
                hasimage = "";

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return hasimage;



    }

    /* Update Profile Image*/

    public long updateProfilephoto(String Proileid,String Userid,String PhotoPath,String PhotoComment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        //  Log.i(TAG, "USER DATA INSERT");

        //insertDashBoard.put(DBUtility.PROFILE_ID, data.getProfileId());
        //insertDashBoard.put(DBUtility.USER_ID, data.getUserId());
        //insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurveyId());
        //insertDashBoard.put(DBUtility.PHOTO_PATH, data.getPhotoPath());
        //insertDashBoard.put(DBUtility.PHOTO_COMMENT, data.getPhotoComment());
        insertDashBoard.put(DBUtility.PHOTO_PATH,PhotoPath);

        long a =db.update(DBUtility.TABLE_SURVEY_PHOTOS, insertDashBoard,DBUtility.PROFILE_ID + " = ? and "+DBUtility.USER_ID+" = ? and "+DBUtility.PHOTO_COMMENT+" = ?",new String[]{String.valueOf(Proileid),String.valueOf(Userid),String.valueOf(PhotoComment)});
        db.close();
        Log.d("Updated Profile photo",a+ "updated");
        return a;
    }



    /*amitk 18-11-2016*/


    public List<Recipe> getQuestionGroupModelArrayListDrawernew(String SurveyID, String UserId, String ProfileId)
    {
        /*select groupid,groupname , count(1) as TotalGroupCount
        from useranswer where surveyid=1 and completeflag=0
        group by groupname*/
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' AND "+DBUtility.SYNC_FLAG + " = 0 "+" group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Log.e("db size", cursor.getCount() + "");
        Log.e("query", cursor.getCount() + "");
        List<Recipe> data = new ArrayList<>();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                do {


                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    List<Ingredient> questionNameModels = dbHelper.getQuestionNameModelArrayListingrediant(SurveyID, cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)), UserId, ProfileId);
                    data.add(new Recipe(cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.GROUP_NAME)), cursor.getString(cursor.getColumnIndex("TotalCount")), questionNameModels));
                } while (cursor.moveToNext());
            } else {
                data = new ArrayList<>();

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }


    //question name
    public ArrayList<Ingredient> getQuestionNameModelArrayListingrediant(String SurveId , String GroupId, String UserId, String ProfileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.SYNC_FLAG + " = 0 ", null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<Ingredient> data = new ArrayList<>();


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                do {

                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new Ingredient(cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_NAME)), cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_SRNO)), cursor.getString(cursor.getColumnIndex(DBUtility.COMPLETE_FLAG))));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }



        return data;
    }

    /* get question group question completed count */

    public int getQuestionGroupCount(String SurveyID,String UserId,String ProfileId,String GroupId)
    {
        /*select groupid,groupname , count(1) as TotalGroupCount
        from useranswer where surveyid=1 and completeflag=0
        group by groupname*/
        int count =0;
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' AND "+ DBUtility.GROUP_ID+"='"+GroupId+"' group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Log.e("db size", cursor.getCount() + "");
        Log.e("query", cursor.getCount() + "");
        //List<QuestionGroupModel> data = new ArrayList<>();
        int i=0;

        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("TotalCount")));

            } else {
                count = 0;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return count;
    }

    /*19-11-2016*/
    /*Delete Sync data from local userans after one month*/
    public long DeleteSyncUserAnswerMonth(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.delete(DBUtility.TABLE_USER_ANSWER,
                DBUtility.SYNC_FLAG + " = ? and "+DBUtility.CREATED_DATE_TIME+ "  <= Datetime('"+Date+"') ",
                new String[]{String.valueOf("1")});
        db.close();
        if (a>0) {
            Log.d("Delete Sync Answer", a + "Sync UserAns deleted");
        }
        else
        {
            Log.d("Delete Sync Answer", a + "Not deleted UserAnswer");
        }

        return  a;
    }
    /*19-11-2016*/
    /*Delete Sync Photos from local userans after one month*/
    public long DeleteSyncUserPhotosMonth(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.delete(DBUtility.TABLE_SURVEY_PHOTOS,
                DBUtility.SYNC_FLAG + " = ? and "+DBUtility.CREATED_DATE_TIME+ "  <= Datetime('"+Date+"') ",
                new String[]{String.valueOf("1")});
        db.close();
        if (a>0) {
            Log.d("Delete Sync Photo", a + "Sync Photo deleted");
        }
        else
        {
            Log.d("Delete Sync Photo", a + "Not deleted ");
        }
        return  a;
    }

    /*21-11-2016*/
    public Cursor getalluserans()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select "+DBUtility.GROUP_ID+","+DBUtility.GROUP_NAME+", sum(case when CompleteFlag = 1 then 0 else 1 end) as TotalCount from " + DBUtility.TABLE_USER_ANSWER +" where surveyid="+SurveyID +" and "+DBUtility.USER_ID+" ='"+UserId+"' AND "+DBUtility.PROFILE_ID+"='"+ProfileId+"' group by "+DBUtility.GROUP_NAME+" order by "+DBUtility.GROUP_SRNO , null);
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER,null);
        return cursor;
    }

    public boolean checkcopyedornot(String ProfileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.PROFILE_ID + "='" + ProfileId + "' " , null);
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean checkprofileexixt(String ProfileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_SURVEY_PROFILE+ " where " + DBUtility.PROFILE_ID + "='" + ProfileId + "' " , null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }


    //insert comments
    public void insertComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment INSERT");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        //Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.insert(DBUtility.TABLE_COMMENTS, null, insertDashBoard);
        db.close();
    }

    /*Update comments*/
    public void UpdateComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment update");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        //Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.update(DBUtility.TABLE_COMMENTS,  insertDashBoard,DBUtility.COMMENTS_ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }

    /*getcomments*/
    public ArrayList<CommentsModel> getCommentsModelArrayList(int surveyId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_COMMENTS + " where " + DBUtility.SURVEY_ID + "='" + surveyId + "'" , null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<CommentsModel> data = new ArrayList<>();


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                do {

                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new CommentsModel(cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS))));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }

    /*10-8*/

    //insert comments

    public void insertPerfomanceComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment INSERT");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        //Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.insert(DBUtility.TABLE_PERFOMANCE_COMMENTS, null, insertDashBoard);
        db.close();
    }

    /*Update comments*/
    public void UpdatePerfomanceComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment INSERT");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        //Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.update(DBUtility.TABLE_PERFOMANCE_COMMENTS,  insertDashBoard,DBUtility.COMMENTS_ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }

    /*getcomments*/
    public ArrayList<CommentsModel> getPerfomanceCommentsModelArrayList(int surveyId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_PERFOMANCE_COMMENTS + " where " + DBUtility.SURVEY_ID + "='" + surveyId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<CommentsModel> data = new ArrayList<>();
        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                do {
                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new CommentsModel(cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS))));
                } while (cursor.moveToNext());
            } else {
                data = null;
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return data;
    }


    //Manisha added 27/02/20
    //insert client comments
    public void insertClientComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment INSERT");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        //Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.insert(DBUtility.TABLE_CLIENT_COMMENTS, null, insertDashBoard);
        db.close();
    }

    //Manisha added 27/02/20
    /* Update client comments*/
    public void UpdateClientComments(GetCommentsRequestResponse.Datum data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertDashBoard = new ContentValues();
        Log.i(TAG, "USER Comment update");

        insertDashBoard.put(DBUtility.COMMENTS_ID, data.getId());
        insertDashBoard.put(DBUtility.COMMENTS, data.getComments());
        // Manisha added 18-04-20
        insertDashBoard.put(DBUtility.SURVEY_ID, data.getSurvey_id());

        db.update(DBUtility.TABLE_CLIENT_COMMENTS,  insertDashBoard,DBUtility.COMMENTS_ID + " = ? ",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }
    //Manisha added 27/02/20
    /*get client comments*/
    public ArrayList<CommentsModel> getClientCommentsModelArrayList(int surveyId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_CLIENT_COMMENTS + " where " + DBUtility.SURVEY_ID + "='" + surveyId + "'", null);
        Log.e("db size", cursor.getCount() + "");
        ArrayList<CommentsModel> data = new ArrayList<>();


        cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {

                do {

                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    data.add(new CommentsModel(cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS_ID)), cursor.getString(cursor.getColumnIndex(DBUtility.COMMENTS))));
                } while (cursor.moveToNext());
            } else {
                data = null;

            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }


        return data;
    }

    // Manisha added 4-2-19
    public Cursor getDefaultAns(int GroupId, int questionId) {

        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.COMPLETE_SURVEYFLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);

        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_DEFAULT_ANSWER + " where " + DBUtility.GROUP_ID + " =" + GroupId + " and " + DBUtility.QUESTION_ID + " =" + questionId+" order by "+DBUtility.DEFAULT_ANS_SR_NO, null);
        return cursor;


    }

//    public Cursor getDefaultAns(String GroupId, String QuestionId) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        //Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_USER_ANSWER + " where " + DBUtility.SURVEY_ID + "='" + SurveId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId + "' and " +DBUtility.USER_ID + " ='" + UserId + "' and "+DBUtility.PROFILE_ID+" ='"+ProfileId+"' and "+DBUtility.COMPLETE_SURVEYFLAG + " = 0 order by " + DBUtility.QUESTION_SRNO + " asc  limit 1", null);
//
//        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_DEFAULT_ANSWER + " where " + DBUtility.QUESTION_ID + "='" + QuestionId + "' AND " + DBUtility.GROUP_ID + " ='" + GroupId  + "'", null);
//        return cursor;
//    }

    /*querys*/
    /*select  distinct(surveyname),surveyid  from question questionID  ;
    select * from question;
    select surveyid from question where surveyname='Survey 1' limit 1;
    select groupname from question where surveyid=5;
    select groupid from question where groupname='Site Documentation – Health and Safety Logbook' limit 1;
    select questionname from question where surveyid=5 and groupid =4;
    select * from question where surveyid=1 and groupid =1  order by questionid asc  limit 1
    select * from question where surveyid=1 and groupid =1 and questionid > 1 order by questionid asc  limit 1
    select * from question where surveyid=1 and groupid =1 and questionid < 1 order by questionid asc  limit 1
    */

//    public void insertCartData(MenuRequestResponse.MenuData menuData, int userID, int quantity, int mealType, float v) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues insertDashBoard = new ContentValues();
//        insertDashBoard.put(DBUtility.USER_ID, userID);
//        insertDashBoard.put(DBUtility.RESTAURANT_ID, menuData.getRestaurant_id());
//        insertDashBoard.put(DBUtility.FOODITEM_ID, menuData.getFood_item_id());
//        insertDashBoard.put(DBUtility.FOODITEM_NAME, menuData.getFood_item_name());
//        insertDashBoard.put(DBUtility.FOODITEM_IMAGE, menuData.getFood_item_image());
//        insertDashBoard.put(DBUtility.FOODITEM_DESCRIPTION, menuData.getFood_item_description());
//        insertDashBoard.put(DBUtility.IS_BREAKFAST, menuData.getIs_breakfast());
//        insertDashBoard.put(DBUtility.BREAKFAST_PRICE, menuData.getFood_item_breakfast_price());
//        insertDashBoard.put(DBUtility.IS_lUNCH, menuData.getIs_lunch());
//        insertDashBoard.put(DBUtility.LUNCH_PRICE, menuData.getFood_item_lunch_price());
//        insertDashBoard.put(DBUtility.IS_DINNER, menuData.getIs_dinner());
//        insertDashBoard.put(DBUtility.DINNER_PRICE, menuData.getFood_item_dinner_price());
//        insertDashBoard.put(DBUtility.COOKING_TIME, menuData.getApprox_cooking_time());
//        insertDashBoard.put(DBUtility.QUANTITY, quantity);
//        insertDashBoard.put(DBUtility.MEAL_TYPE, mealType);
//        insertDashBoard.put(DBUtility.ORDER_TOTAL, v);
//        db.insert(DBUtility.TABLE_CART, null, insertDashBoard);
//        db.close();
//    }
//
//    public MenuRequestResponse.MenuData getCartData(int userID, int RestaurantID, int FoodItemID, int mealTYPE) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_CART  +" where " +DBUtility.USER_ID +"="+userID +" AND " +DBUtility.RESTAURANT_ID +"="+RestaurantID +" AND "+DBUtility.FOODITEM_ID +"="+FoodItemID +" AND "+DBUtility.MEAL_TYPE +"="+mealTYPE, null);
//        Log.e("db size", cursor.getCount() + "");
//        MenuRequestResponse.MenuData cartData = new MenuRequestResponse().new MenuData();
//        if (cursor.moveToFirst()) {
//
//            cartData.setUserID(cursor.getInt(cursor.getColumnIndex(DBUtility.USER_ID)));
//            cartData.setRestaurant_id(cursor.getInt(cursor.getColumnIndex(DBUtility.RESTAURANT_ID)));
//            cartData.setFood_item_id(cursor.getInt(cursor.getColumnIndex(DBUtility.FOODITEM_ID)));
//            cartData.setFood_item_name(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_NAME)));
//            cartData.setFood_item_image(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_IMAGE)));
//            cartData.setFood_item_description(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_DESCRIPTION)));
//            cartData.setIs_breakfast(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_BREAKFAST)));
//            cartData.setFood_item_breakfast_price(cursor.getFloat(cursor.getColumnIndex(DBUtility.BREAKFAST_PRICE)));
//            cartData.setIs_lunch(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_lUNCH)));
//            cartData.setFood_item_lunch_price(cursor.getInt(cursor.getColumnIndex(DBUtility.LUNCH_PRICE)));
//            cartData.setIs_dinner(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_DINNER)));
//            cartData.setFood_item_dinner_price(cursor.getInt(cursor.getColumnIndex(DBUtility.DINNER_PRICE)));
//            cartData.setApprox_cooking_time(cursor.getFloat(cursor.getColumnIndex(DBUtility.COOKING_TIME)));
//            cartData.setQuantity(cursor.getInt(cursor.getColumnIndex(DBUtility.QUANTITY)));
//            cartData.setMealType(cursor.getInt(cursor.getColumnIndex(DBUtility.MEAL_TYPE)));
//
//
//        }
//        return cartData;
//    }
//    public ArrayList<MenuRequestResponse.MenuData> getCartData(int userID, int RestaurantID) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_CART  +" where " +DBUtility.USER_ID +"="+userID +" AND " +DBUtility.RESTAURANT_ID +"="+RestaurantID , null);
//        Log.e("db size", cursor.getCount() + "");
//        ArrayList<MenuRequestResponse.MenuData> data =new ArrayList<>();
//        MenuRequestResponse.MenuData cartData = new MenuRequestResponse().new MenuData();
//        if (cursor.moveToFirst()) {
//
//            cartData.setUserID(cursor.getInt(cursor.getColumnIndex(DBUtility.USER_ID)));
//            cartData.setRestaurant_id(cursor.getInt(cursor.getColumnIndex(DBUtility.RESTAURANT_ID)));
//            cartData.setFood_item_id(cursor.getInt(cursor.getColumnIndex(DBUtility.FOODITEM_ID)));
//            cartData.setFood_item_name(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_NAME)));
//            cartData.setFood_item_image(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_IMAGE)));
//            cartData.setFood_item_description(cursor.getString(cursor.getColumnIndex(DBUtility.FOODITEM_DESCRIPTION)));
//            cartData.setIs_breakfast(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_BREAKFAST)));
//            cartData.setFood_item_breakfast_price(cursor.getFloat(cursor.getColumnIndex(DBUtility.BREAKFAST_PRICE)));
//            cartData.setIs_lunch(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_lUNCH)));
//            cartData.setFood_item_lunch_price(cursor.getInt(cursor.getColumnIndex(DBUtility.LUNCH_PRICE)));
//            cartData.setIs_dinner(cursor.getInt(cursor.getColumnIndex(DBUtility.IS_DINNER)));
//            cartData.setFood_item_dinner_price(cursor.getInt(cursor.getColumnIndex(DBUtility.DINNER_PRICE)));
//            cartData.setApprox_cooking_time(cursor.getFloat(cursor.getColumnIndex(DBUtility.COOKING_TIME)));
//            cartData.setQuantity(cursor.getInt(cursor.getColumnIndex(DBUtility.QUANTITY)));
//            cartData.setMealType(cursor.getInt(cursor.getColumnIndex(DBUtility.MEAL_TYPE)));
//
//            data.add(cartData);
//        }
//        return data;
//    }
//    public LoginRequestResponse.LoginData getUserLoginData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + DBUtility.TABLE_LOGIN, null);
//        Log.e("db size", cursor.getCount() + "");
//        LoginRequestResponse.LoginData userLoginData = new LoginRequestResponse().new LoginData();
//        if (cursor.moveToFirst()) {
//
//            userLoginData.setUser_id(cursor.getInt(cursor.getColumnIndex(DBUtility.USER_ID)));
//            userLoginData.setFirst_name(cursor.getString(cursor.getColumnIndex(DBUtility.USER_FirstNAME)));
//            userLoginData.setLast_name(cursor.getString(cursor.getColumnIndex(DBUtility.USER_LastNAME)));
//            userLoginData.setEmail(cursor.getString(cursor.getColumnIndex(DBUtility.USER_EMAIL)));
//            userLoginData.setPhone(cursor.getString(cursor.getColumnIndex(DBUtility.USER_PHONE)));
//            userLoginData.setAddress(cursor.getString(cursor.getColumnIndex(DBUtility.USER_ADDRESS)));
//            userLoginData.setGender(cursor.getString(cursor.getColumnIndex(DBUtility.GENDER)));
//            userLoginData.setUser_latitude(cursor.getString(cursor.getColumnIndex(DBUtility.Latitude)));
//            userLoginData.setUser_longitude(cursor.getString(cursor.getColumnIndex(DBUtility.Longitude)));
//            userLoginData.setEnable_notification(cursor.getInt(cursor.getColumnIndex(DBUtility.NOTIFICATION)));
//            userLoginData.setWallet_amount(cursor.getInt(cursor.getColumnIndex(DBUtility.WALLET)));
//            userLoginData.setApp_rating(cursor.getInt(cursor.getColumnIndex(DBUtility.APPRATING)));
//            userLoginData.setDevice_token(cursor.getString(cursor.getColumnIndex(DBUtility.DEVICE_TOKEN)));
//
//        }
//        return userLoginData;
//    }
//
//    public void updateCartData(MenuRequestResponse.MenuData menuData,int quantitiy,int userId,int mealtype,float total) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues updateCart = new ContentValues();
//        Log.i(TAG, "User Cart Data Update");
//       // updateCart.put(DBUtility.USER_ID, menuData.getUserId());
//        updateCart.put(DBUtility.QUANTITY, quantitiy);
//        updateCart.put(DBUtility.ORDER_TOTAL, total);
//
//        int a = db.update(DBUtility.TABLE_CART,
//                updateCart,
//                 DBUtility.USER_ID + " = ? AND " + DBUtility.RESTAURANT_ID + " = ? AND " + DBUtility.FOODITEM_ID + " = ? AND " + DBUtility.MEAL_TYPE + " = ?",
//                new String[]{String.valueOf(userId),String.valueOf(menuData.getRestaurant_id()),String.valueOf(menuData.getFood_item_id()),String.valueOf(mealtype)});
//  //  int a =db.rawQuery("UPDATE "+ DBUtility.TABLE_CART+ "  SET "+DBUtility.QUANTITY +"="+ quantitiy +" WHERE "+ some_column=some_value ");
//     Log.v("update>>","update>>"+a);
//       // int a = db.update(DBUtility.TABLE_CART, updateCart, DBUtility.QUANTITY + "=" + quantitiy, null);
//    }
////
////    public boolean updateLoginUserData(LoginUserRequestResponse.NewUserData userData) {
////        SQLiteDatabase db = this.getWritableDatabase();
////        ContentValues updateDashBoard = new ContentValues();
////        Log.i(TAG, "User Login Data Update");
////        updateDashBoard.put(DBUtility.USER_ID, userData.getUserId());
////        updateDashBoard.put(DBUtility.USER_NAME, userData.getName());
////        updateDashBoard.put(DBUtility.USER_EMAIL_ADDRESS, userData.getEmail());
////        updateDashBoard.put(DBUtility.USER_PHONE, userData.getPhone());
////        updateDashBoard.put(DBUtility.USER_DESCRIPTION, userData.getDescription());
////        updateDashBoard.put(DBUtility.USER_PROFILE_PICTURE, userData.getProfilePicture());
////        updateDashBoard.put(DBUtility.FB_ID, userData.getFacebookId());
////        updateDashBoard.put(DBUtility.TWITTER_ID, userData.getTwitterId());
////        updateDashBoard.put(DBUtility.DEVICE_TOKEN, userData.getDeviceToken());
////        updateDashBoard.put(DBUtility.DEVICE_TYPE, userData.getDeviceType());
////        updateDashBoard.put(DBUtility.LIKE_COUNT, userData.getLikeNotification());
////        updateDashBoard.put(DBUtility.COMMENT_COUNT, userData.getCommentNotification());
////        updateDashBoard.put(DBUtility.SHARE_COUNT, userData.getShareNotification());
////        updateDashBoard.put(DBUtility.CHAT_COUNT, userData.getChatNotification());
////        updateDashBoard.put(DBUtility.IS_PASSWORD_SET, userData.getPasswordSet());
////        updateDashBoard.put(DBUtility.UNREAD_NOTIFICATION_COUNT, userData.getUnread_notifications_count());
////        int a = db.update(DBUtility.TABLE_LOGIN, updateDashBoard, DBUtility.USER_ID + "=" + userData.getUserId(), null);
////
////        // int a = db.update(DBUtility.TABLE_LOGIN, updateDashBoard, DBUtility.USER_ID + "=?" + userData.getUserId(), null);
////        Log.e("aaaaa", a + "");
////        return true;
////    }
////

    /*Querys new*/
 /* for getting incomplere answer
 select groupname,count(1),
    sum(case when CompleteFlag = 1 then 1 else 0 end) as CompleteFlag1
    ,sum(case when CompleteFlag = 1 then 0 else 1 end) as CompleteFlag0
    from useranswer  group by groupname
    select * from useranswer

       to insert multiple data at a time from one table to another
    Insert into tablename (columnname, columnname,columnname)
    select Columnname,columnname, 12345,@variableName
    from TableName where columname = @newVariableName*/

/*    SELECT * FROM useranswer  CROSS JOIN surveyprofile

    SELECT * FROM useranswer LEFT OUTER JOIN surveyprofile
    ON useranswer.profileid = surveyprofile.profileid where useranswer.CompleteSurveyFlag=1 and useranswer.SyncFlag=0;*/

}