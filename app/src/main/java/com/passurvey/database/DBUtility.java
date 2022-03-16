package com.passurvey.database;

public class DBUtility {
    public static final String DATABASE_NAME = "PASSURVEYDB";
    /* public static final String TABLE_LOGIN = "LOGIN";
    public static final String TABLE_CART = "CART";*/

    public static final String TABLE_QUESTION = "Question";
    public static final String TABLE_USER_ANSWER = "UserAnswer";
    public static final String TABLE_DEFAULT_ANSWER = "DefaultAnswer";
    public static final String TABLE_SURVEY_PROFILE = "SurveyProfile";
    public static final String TABLE_SURVEY_PHOTOS = "SurveyPhotos";
    public static final String TABLE_MANAGEMENT_AUDITOR = "managementAuditor";

    /*getcomments*/
    public static final String TABLE_COMMENTS = "Commnets";
    public static final String TABLE_PERFOMANCE_COMMENTS = "PerfomanceCommnets";
    public static final String TABLE_CLIENT_COMMENTS = "ClientCommnets";

    /*      "id": 6,
            "survey_id": 5,
            "group_id": 4,
            "survey_name": "Survey 1",
            "group_name": "Site Documentation – Health and Safety Logbook",
            "question_name": "Is the Contractors Health & Safety policy document in place and up to date."*/

    public static final String ID = "ID";
    public static final String DEFAULT_ANS_SR_NO = "default_ans_sr_no";
    public static final String USER_ID = "USER_ID";
    public static final String QUESTION_ID = "QuestionID";
    public static final String SURVEY_ID = "SurveyID";
    public static final String GROUP_ID = "GroupID";
    public static final String GROUP_SRNO = "GroupSrNo";
    public static final String QUESTION_SRNO = "QuestionSrNo";
    public static final String SURVEY_NAME = "SurveyName";
    public static final String GROUP_NAME = "GroupName";
    public static final String QUESTION_NAME = "QuestionName";
    public static final String USER_ANSWER = "UserAnswer";
    public static final String ANSWER_PRIORITY = "AnswerPriority";
    public static final String CREATED_DATE_TIME = "CreatedDateTime";
    public static final String COMPLETE_FLAG = "CompleteFlag";
    public static final String COMPLETE_SURVEYFLAG = "CompleteSurveyFlag";
    public static final String SYNC_FLAG = "SyncFlag";

   // photo table
   public static final String SYNC_FLAG_IMAGE = "SyncFlag_img";
    public static final String SYNC_FLAG_SURVEYCOMPLETE = "SyncFlag_survey_complete";
    public static final String HAS_CERTIFICATE = "HasCertificate";
    public static final String CERTIFICATE_DATE = "CertificateDate";
    public static final String DEFAULT_ANSWER = "DefaultAnswer";
    public static final String DEFAULT_ANSWER_PRIORITY= "DefaultAnswerPriority";

/*Survey Profile*/

    public static final String PROFILE_ID = "ProfileID";
    /*UserID
     SurveyID*/
//    public static final String PROFILE_FUND = "Fund";
    public static final String PROFILE_COMANAGINGAGENT = "COManagingAgent";
    public static final String PROFILE_MANAGEMENTSURVEOR = "ManagementSurveor";
    public static final String PROFILE_FACILITIESMANAGER = "FacilitiesManager";
    public static final String PROFILE_SITEADDRESS = "SiteAddress";
    public static final String PROFILE_SITENAME = "SiteName";
    public static final String PROFILE_SITEADDRESS1 = "SiteAddress1";
    public static final String PROFILE_SITEADDRESS2 = "SiteAddress2";
    public static final String PROFILE_SITEADDRESS3 = "SiteAddress3";
    public static final String PROFILE_SITEPOSTALCODE = "SitePostalCode";

    /*update new 18-10*/
    public static final String PROFILE_REPORTPREPAREDBY = "ReportPreparedby";
    public static final String PROFILE_REPORTCHECKEDBY = "ReportCheckedby";
    public static final String PROFILE_AUDITDATE = "AuditDate";
    public static final String PROFILE_DATEOFISSUE = "DateofIssue";
    public static final String PROFILE_AUDITVISIT = "AuditVisit";
    public static final String PROFILE_CONTRACTORSPERFOMANCE = "ContractorsPerfomance";
    public static final String PROFILE_STATUTOORYAUDITSCORE = "StatutoryAuditScore";
    public static final String PROFILE_CONTRACTORIMPROVEPERFOMANCE = "ContractorImprovePerfomance";
    public static final String PROFILE_CONSULTANTSCOMMENTS = "ConsultantsComments";
    public static final String PROFILE_STATUTORYCOMMENTS = "StatutoryComments";
    public static final String PROFILE_CLIENTCOMMENTS = "ClientComments";
    public static final String PROFILE_CREATEDDATE = "CreatedDate";
    public static final String PROFILE_CREATEDTIME = "CreatedTime";
    public static final String PROFILE_MANAGEMENT_AUDITOR_NAME = "CreatedTime";
    public static final String COMMENTS_ID = "ID";
    public static final String COMMENTS = "Comments";

    /*iMAGE*/
   /* ProfileID
            UserID
    SurveyID*/

    public static final String PHOTO_PATH =  "PhotoPath";

    public static final String PHOTO_COMMENT =  "PhotoComment";

    public static final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_QUESTION + "("
            + QUESTION_ID + " INTEGER PRIMARY KEY, "
            + SURVEY_ID + " INTEGER, "
            + GROUP_ID + " INTEGER, "
            + SURVEY_NAME + " TEXT, "
            + GROUP_NAME + " TEXT, "
            + QUESTION_NAME + " TEXT, "
            +  QUESTION_SRNO + " DOUBLE, "
            +  HAS_CERTIFICATE + " TEXT, "
            + GROUP_SRNO + " INTEGER " + ")";

    public static final String CREATE_TABLE_USER_ANSWER = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_USER_ANSWER + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER  , "
            + PROFILE_ID + " TEXT, "
            + QUESTION_ID + " INTEGER , "
            + SURVEY_ID + " INTEGER, "
            + GROUP_ID + " INTEGER, "
            + QUESTION_SRNO + " DOUBLE, "
            + GROUP_SRNO + " INTEGER, "
            + SURVEY_NAME + " TEXT, "
            + GROUP_NAME + " TEXT, "
            + QUESTION_NAME + " TEXT, "
            + USER_ANSWER + " TEXT, "
            + ANSWER_PRIORITY + " TEXT, "
            + CREATED_DATE_TIME + " TEXT, "
            + HAS_CERTIFICATE + " TEXT, "
            + CERTIFICATE_DATE + " TEXT, "
            + COMPLETE_FLAG + " INTEGER, "
            + COMPLETE_SURVEYFLAG + " INTEGER, "
            + SYNC_FLAG + " INTEGER " + ")";

    //Manisha added groupId & question id
    //Manisha change DEFAULT_ANS_SR_NO int to Double
    public static final String CREATE_TABLE_DEFAULT_ANSWER = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_DEFAULT_ANSWER + "("
            + ID + " INTEGER PRIMARY KEY, "
            + DEFAULT_ANS_SR_NO + " DOUBLE, "
            + DEFAULT_ANSWER_PRIORITY + " TEXT, "
            + GROUP_ID + " INTEGER, "
            + QUESTION_ID + " INTEGER, "
            + DEFAULT_ANSWER + " TEXT " + ")";

    public static final String CREATE_TABLE_MANAGEMENT_AUDITOR = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_MANAGEMENT_AUDITOR + "("
            + ID + " INTEGER PRIMARY KEY, "
            + PROFILE_MANAGEMENT_AUDITOR_NAME + " TEXT " + ")";

    /*SURVEY PROFILE*/
    public static final String CREATE_TABLE_SURVEY_PROFILE = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_SURVEY_PROFILE + "("
            + PROFILE_ID + " TEXT, "
            + USER_ID + " INTEGER  , "
            + SURVEY_ID + " INTEGER, "
//            + PROFILE_FUND + " TEXT, "
            + PROFILE_COMANAGINGAGENT + " TEXT, "
            + PROFILE_MANAGEMENTSURVEOR + " TEXT, "
            + PROFILE_FACILITIESMANAGER + " TEXT, "
            + PROFILE_SITENAME + " TEXT, "
            + PROFILE_SITEADDRESS1 + " TEXT, "
            + PROFILE_SITEADDRESS2 + " TEXT, "
            + PROFILE_SITEADDRESS3 + " TEXT, "
            + PROFILE_SITEPOSTALCODE + " TEXT, "
            + PROFILE_REPORTPREPAREDBY + " TEXT, "
            + PROFILE_REPORTCHECKEDBY + " TEXT, "
            + PROFILE_AUDITDATE + " TEXT, "
            + PROFILE_DATEOFISSUE + " TEXT, "
            + PROFILE_AUDITVISIT + " TEXT, "
            + PROFILE_CONTRACTORSPERFOMANCE + " TEXT, "
            + PROFILE_STATUTOORYAUDITSCORE + " TEXT, "
            + PROFILE_CONTRACTORIMPROVEPERFOMANCE + " TEXT, "
            + PROFILE_CONSULTANTSCOMMENTS + " TEXT, "
            + PROFILE_CLIENTCOMMENTS + " TEXT, "
            + PROFILE_STATUTORYCOMMENTS + " TEXT, "
            + PROFILE_CREATEDDATE + " TEXT, "
            + PROFILE_CREATEDTIME + " TEXT " + ")";

    /*SURVEY PHOTO*/
    public static final String CREATE_TABLE_SURVEY_PHOTOS = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_SURVEY_PHOTOS + "("
            + PROFILE_ID + " TEXT, "
            + USER_ID + " INTEGER, "
            + SURVEY_ID + " INTEGER, "
            + PHOTO_PATH + " TEXT, "
            + PHOTO_COMMENT + " TEXT, "
            + SYNC_FLAG + " INTEGER, "
            + SYNC_FLAG_IMAGE + " INTEGER, "
            + SYNC_FLAG_SURVEYCOMPLETE + " INTEGER, "
            + CREATED_DATE_TIME + " TEXT "+ ")";
    /*comments create table */

    public static final String CREATE_TABLE_COMMENTS = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_COMMENTS + "("
            + COMMENTS_ID + " INTEGER KEY, "
            + SURVEY_ID + " INTEGER, "
            + COMMENTS + " TEXT " + ")";

    public static final String CREATE_TABLE_PERFOMANCE_COMMENTS = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_PERFOMANCE_COMMENTS + "("
            + COMMENTS_ID + " INTEGER KEY, "
            + SURVEY_ID + " INTEGER, "
            + COMMENTS + " TEXT " + ")";

    //Manisha added 27/02/20
    public static final String CREATE_TABLE_CLIENT_COMMENTS = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_CLIENT_COMMENTS + "("
            + COMMENTS_ID + " INTEGER KEY, "
            + SURVEY_ID + " INTEGER, "
            + COMMENTS + " TEXT " + ")";

    /*
    public static final String USER_ID = "USER_ID";
    public static final String USER_FirstNAME = "USER_FirstNAME";
    public static final String USER_LastNAME = "USER_LastNAME";
    public static final String USER_EMAIL = "USER_EMAIL_ADDRESS";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String GENDER = "GENDER";
    public static final String Latitude = "Latitude";
    public static final String Longitude = "Longitude";
    public static final String ISREMEMBER = "ISREMEMBER";
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String WALLET = "WALLET";
    public static final String APPRATING = "APPRATING";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";

   ///Cart Fields
    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final String FOODITEM_ID = "FOODITEM_ID";
    public static final String FOODITEM_NAME = "FOODITEMNAME";
    public static final String FOODITEM_IMAGE = "FOODITEMIMAGE";
    public static final String FOODITEM_DESCRIPTION = "FOODITEMDESCRIPTION";
    public static final String IS_BREAKFAST = "ISBREAKFAST";
    public static final String BREAKFAST_PRICE = "BREAKFAST_PRICE";
    public static final String IS_lUNCH = "IS_lUNCH";
    public static final String LUNCH_PRICE = "LUNCH_PRICE";
    public static final String IS_DINNER = "IS_DINNER";
    public static final String DINNER_PRICE = "DINNER_PRICE";
    public static final String COOKING_TIME = "COOKING_TIME";
    public static final String MEAL_TYPE = "MEAL_TYPE";
    public static final String QUANTITY = "QUANTITY";
    public static final String _ID = "_ID";
    public static final String ORDER_TOTAL = "ORDER_TOTAL";

    public static final String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_CART + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER  , "
            + RESTAURANT_ID + " INTEGER NOT NULL, "
            + FOODITEM_ID + " INTEGER, "
            + FOODITEM_NAME + " TEXT, "
            + FOODITEM_IMAGE + " TEXT, "
            + FOODITEM_DESCRIPTION + " TEXT, "
            + IS_BREAKFAST + " INTEGER, "
            + BREAKFAST_PRICE + " TEXT, "
            + IS_lUNCH + " INTEGER, "
            + LUNCH_PRICE + " TEXT, "
            + IS_DINNER + " INTEGER, "
            + DINNER_PRICE + " TEXT, "
            + COOKING_TIME + " INTEGER , "
            + QUANTITY + " INTEGER , "
            + MEAL_TYPE + " INTEGER ,"
            + ORDER_TOTAL + " INTEGER " + ")";


    public static final String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + DBUtility.TABLE_LOGIN + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_FirstNAME + " TEXT NOT NULL, "
            + USER_LastNAME + " TEXT, "
            + USER_EMAIL + " TEXT, "
            + USER_PHONE + " TEXT, "
            + USER_ADDRESS + " TEXT, "
            + GENDER + " TEXT, "
            + Latitude + " TEXT, "
            + Longitude + " TEXT, "
            + ISREMEMBER + " INTEGER, "
            + NOTIFICATION + " INTEGER, "
            + WALLET + " INTEGER, "
            + APPRATING + " INTEGER , "
            + DEVICE_TOKEN + " TEXT " + ")";*/
}
