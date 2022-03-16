package com.passurvey.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.passurvey.R;
import com.passurvey.activities.SplashActivity;
import com.passurvey.database.DBHelper;
import com.passurvey.database.DBUtility;
import com.passurvey.model.PhotoModel;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.webservices.WebServiceCaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class NetworkChangeReceiverOld extends BroadcastReceiver {
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	public static int IMAGE_NOTIFICATION_ID=247;
	private  boolean IsInternetPresent=false;
	 DBHelper dbHelper;
	 Cursor cursor,cursordata;
	 String Profileid="";
	 Context context;
	String SurveyId,GroupId,UserId,ProfileId;
	int synccount=0,synccount1=0,totalSynced=0,totalSyncedImage=0;
	int firstime=0,totalImageCount=0;
	private ArrayList<PhotoModel> phtolist = new ArrayList<>();
	Map<String, RequestBody> map = new HashMap<>();
	Map<String, RequestBody> mapcomment = new HashMap<>();
	// public static String Status = "";
	int groupbyphotototal=0,groupbyphotocount=0;
	String oldprofile="";

	@Override
	public void onReceive(Context mcontext, final Intent intent) {
	//	db = new DatabaseHelper(context);
		context =mcontext;
		dbHelper =new DBHelper(context);
		IsInternetPresent = getConnectivityStatus(context);
		if (IsInternetPresent){
			/*Write your logic for internet connections*/
			//senddatatoserver();
			/*amitk 31-1-17*/
			senddatatoservernew();
			sendPendingImageToServer();
		}
			Utils.isImageSyncing=false;
		//	cancelNotification();
		}

	public static boolean getConnectivityStatus(Context context) {
		boolean network_flag = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
					|| activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				network_flag = true;
			} else {
				network_flag = false;
			}
		}
		return network_flag;
	}

//	public  void senddatatoserver()
//	{
//		JSONObject Jsonrequest;
//		Jsonrequest =new JSONObject();
//		cursor =dbHelper.getallcompleteSurvey();
//
//		cursor.moveToFirst();
//		try {
//			if (cursor != null && cursor.getCount() > 0) {
//				synccount = cursor.getCount();
//				try {
//					JSONArray jsonArrayprofiledata;
//					JSONArray jsonArrayanswerdata;
//					do {
//						jsonArrayprofiledata = new JSONArray();
//						JSONObject profileuserdata = new JSONObject();
//
//						jsonArrayanswerdata = new JSONArray();
//
//
//						Profileid = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
//						UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
//						SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
//						profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
//						profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
//						//Manisha commented 13-1-20
////						profileuserdata.put("fund", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));
//						profileuserdata.put("managing_agent", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
//						profileuserdata.put("managment_surveyor", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
//						profileuserdata.put("facility_manager", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));
//						//profileuserdata.put("site_address",Utils.ReplaceAsciChar( cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
//					 /*18-10 */
//
//						//profileuserdata.put("site_address",Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
//
//						profileuserdata.put("site_name", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
//						profileuserdata.put("address1", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
//						profileuserdata.put("address2", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
//						profileuserdata.put("address3", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
//						profileuserdata.put("pincode", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));
//
//						profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
//						profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
//						//Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd");
//						//Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)),"dd-mm-yyyy","yyyy-mm-dd");
//						profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
//						profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
//						profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
//						profileuserdata.put("contractor_performance", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE))));
//						profileuserdata.put("statutory_audit_score", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE))));
//						profileuserdata.put("contractor_improvement", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE))));
//						profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
//						profileuserdata.put("consultants_comments_client", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS))));
//						profileuserdata.put("statutory_certification_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS))));
//						profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
//						jsonArrayprofiledata.put(profileuserdata);
//
//						cursordata = dbHelper.getallcompleteSurveyAnsProfileid(Profileid);
//						try {
//							if (cursordata != null && cursordata.getCount() > 0) {
//								do {
//									JSONObject useranswer = new JSONObject();
//									useranswer.put("id", cursordata.getString(cursordata.getColumnIndex(DBUtility.QUESTION_ID)));
//									useranswer.put("answer", Utils.ReplaceAsciChar(cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ANSWER))));
//									useranswer.put("priority", cursordata.getString(cursordata.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
//									useranswer.put("user_id", cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ID)));
//									//useranswer.put("dateoflastcertificate",cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
//									if (!cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
//										//Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
//										useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
//									} else {
//										useranswer.put("dateoflastcertificate", "");
//									}
//									jsonArrayanswerdata.put(useranswer);
//
//									//data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
//									//data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
//								} while (cursordata.moveToNext());
//
//							}
//						}
//						finally {
//							cursordata.close();
//						}
//					/*JSONObject useranswer = new JSONObject();
//					useranswer.put("id",cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
//					useranswer.put("answer",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER)));
//					useranswer.put("priority",cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
//					useranswer.put("user_id",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
//					jsonArrayanswerdata .put(useranswer);*/
//
//						//data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
//						//data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
//
//						Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
//						Jsonrequest.put("data", jsonArrayanswerdata);
//						Log.d("JsonRequest", Jsonrequest.toString());
//						senddatatoserver(Jsonrequest, Profileid);
//					} while (cursor.moveToNext());
//				/*Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
//				Jsonrequest.put("data", jsonArrayanswerdata);
//				Log.d("JsonRequest", Jsonrequest.toString());
//				senddatatoserver(Jsonrequest);*/
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//
//			} else {
//				if (synccount > 0) {
//					shownotification(synccount);
//					synccount = 0;
//				}
//				Log.d("No Data", "No Data for sync" + synccount);
//			}
//		}
//		finally {
//			cursor.close();
//		}
//	}

//	public  void senddatatoserver(JSONObject jsonObject, final String profid) {
//		try {
//			if (!Utils.isNetworkAvailable(c)) {
//				//Utils.showToast(c, c.getResources().getString(R.string.no_internet_connection));
//			}  else
//
//			{
//				//  Utils.showProgress(SplashActivity.this);
//				WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
//				RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
//				Call<ErrorResponse> call = service.UserAnswer(body);
//				call.enqueue(new Callback<ErrorResponse>() {
//
//					@Override
//					public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
//
//
//						if (response.isSuccessful())
//						{
//
//							ErrorResponse result = response.body();
//							if (result.getSuccess()) {
//								dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,profid);
//
//								Log.d("Data synced","Data Synchronized with surver");
//								//senddatatoserver();
//								//Utils.showToast(SplashActivity.this,response.body().getMessage());
//								phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, profid, UserId);
//								if (phtolist!=null&&phtolist.size()>0)
//								{
//									/*amitk 31-1-17 background image*/
//									dbHelper.updatePhotosflagCompleteSurveyData(profid,UserId);
//									uploadimage(SurveyId,profid,UserId);
//									Log.d("Proflid for image",profid);
//
//								}
//								else {
//									//dbHelper.DeleteSyncUserPhotos();
//									//dbHelper.DeleteSyncUserAnswer();
//
//									synccount1++;
//									Log.d("Sync count","No Data for sync"+synccount);
//									if(synccount==synccount1){
//										if (synccount>0)
//										{
//											shownotification(synccount);
//											Log.d("Sync count","No Data for sync"+synccount);
//											synccount=0;
//										}
//										Log.d("No Data","No Data for sync"+synccount);
//									}
//									new ExportDatabaseCSVTask().execute();
//
//								//	senddatatoserver();
//                                /*    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
//                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
//                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
//                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
//                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
//                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
//                                    finish();*/
//								}
//
//							} else {
//								//Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
//								Log.d("Data synced","Data Synchronized error");
//							}
//						}
//						else {
//							//  Utils.hideProgress();
//							Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//
//							try {
//								ErrorResponse errors = converter.convert(response.errorBody());
//								Log.d("Data synced","Data Synchronized error");
//								Toast.makeText(c, errors.getMessage(), Toast.LENGTH_SHORT).show();
//							} catch (Exception e) {
//								//Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
//								Log.d("Data synced","Data Synchronized error");
//							}
//						}
//
//
//					}
//
//					@Override
//					public void onFailure(Call<ErrorResponse> call, Throwable t) {
//						Log.v("onFailure", "onFailure");
//						//Utils.showToast(c, getResources().getString(R.string.server_error));
//						//Toast.makeText(c, R.string.server_error, Toast.LENGTH_SHORT).show();
//
//						//Utils.hideProgress();
//					}
//				});
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
///*Image sync code*/
//
//	public void uploadimage(String surveyid,String profileId,String Userid)
//	{
//
//		//ProfileId=profileId;
//		//UserId=Userid;
//		//SurveyId=surveyid;
//
//		phtolist = dbHelper.getimagepathAndcommentfinal(surveyid, profileId, Userid);
//		Log.d("photolist size",phtolist.size()+" survey profile userid"+surveyid+" : "+profileId+" : "+Userid);;
//		for (int i = 0; i < phtolist.size(); i++) {
//			if (Utils.checkfile(phtolist.get(i).getPhotoPath())) {
//				File file = new File(phtolist.get(i).getPhotoPath());
//				RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//				map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
//				//  map.put("image["+i+"]", fileBody);
//				//mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment());
//				mapcomment.put("comment[" + i + "]", Utils.getRequestBody(phtolist.get(i).getPhotoComment()));
//			}
//
//		}
//
//		InsertPhoto(profileId,Userid);
//	}
//
//	private void InsertPhoto(final String Profileid1, final String Userid1) {
//		try {
//
//			// Utils.showProgress(CompleteSurveyAndSync.this);
//			WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
//			RequestBody profileidbody =Utils.getRequestBody(Profileid1);
//
//			Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);
//
//			//Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);
//			call.enqueue(new Callback<ErrorResponse>() {
//
//				@Override
//				public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
//					//Utils.hideProgress();
//
//					if (response.isSuccessful()) {
//
//						ErrorResponse result = response.body();
//						if (result.getSuccess()) {
//							// Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();
//							long dbstatus=dbHelper.updatePhotosflag(Profileid1,Userid1);
//							if (dbstatus>0)
//							{ //Utils.hideProgress();
//								Log.d("image synced","Successfully");
//
//							}
//							else
//							{
//								// Utils.hideProgress();
//
//								Log.d("image synced","not successfully");
//							}
//							//dbHelper.DeleteSyncUserPhotos();
//							//dbHelper.DeleteSyncUserAnswer();
//							synccount1++;
//							Log.d("Sync count","No Data for sync"+synccount);
//							if(synccount==synccount1){
//								if (synccount>0)
//								{
//									shownotification(synccount);
//									synccount=0;
//								}
//								Log.d("No Data","No Data for sync"+synccount);
//							}
//							new ExportDatabaseCSVTask().execute();
//							//senddatatoserver();
//							//synccount++;
//                           /* Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
//                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
//                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
//                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
//                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
//                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
//                            finish();*/
//						}
//					} else {
//						// Utils.hideProgress();
//						Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//					}
//				}
//				@Override
//				public void onFailure(Call<ErrorResponse> call, Throwable t) {
//					Log.v("onFailure", "onFailure");
//					// Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
//					// Utils.hideProgress();
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void shownotification(Integer synccount) {
		Intent notificationIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SplashActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
				.setContentText(synccount+" Audit Sync successfully")
				//.setContentText(" Survey Sync successfully")
				.setTicker(context.getString(R.string.app_name))
				.setSmallIcon(getNotificationIcon())
				.setAutoCancel(true)
				.setContentIntent(pendingIntent).build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Random random = new Random();
		int m = random.nextInt(9999 - 1000) + 1000;
		notificationManager.notify(m, notification);
	}

	/* export sqlite to csv */
	public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
		//private final ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
		@Override
		protected void onPreExecute() {
            /*this.dialog.setMessage("Exporting database...");
            this.dialog.show();*/
		}

		protected Boolean doInBackground(final String... args) {
			File dbFile=context.getDatabasePath("PASSURVEYDB");
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
				} else{
					System.out.println("File already exists.");
				}

				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
				// SQLiteDatabase db = dbhelper.getWritableDatabase();
				Cursor curCSV=dbHelper.getalluserans();
				csvWrite.writeNext(curCSV.getColumnNames());
				while(curCSV.moveToNext()) {
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
			catch(SQLException sqlEx) {
				Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
				return false;
			}
			catch (IOException e) {
				Log.e("MainActivity", e.getMessage(), e);
				return false;
			}
		}

		protected void onPostExecute(final Boolean success) {
           /* if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }*/
			if (success) {
				//Toast.makeText(SplashActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
			} else {
				//   Toast.makeText(SplashActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
			}
		}}

	/*amitk send data to server 31-1-17*/
	public  void senddatatoservernew() {
		JSONObject Jsonrequest;
		Jsonrequest =new JSONObject();
		cursor = dbHelper.getallcompleteSurvey();
		cursor.moveToFirst();
		try {
			if (cursor != null && cursor.getCount() > 0) {
				synccount = cursor.getCount();
				try {
					JSONArray jsonArrayprofiledata;
					JSONArray jsonArrayanswerdata;
					jsonArrayprofiledata = new JSONArray();
					JSONObject profileuserdata = new JSONObject();
					jsonArrayanswerdata = new JSONArray();
					Profileid = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
					UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
					SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
					profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
					profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
					//Manisha commented 13-1-20
//					profileuserdata.put("fund", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));
					profileuserdata.put("fund","");
					profileuserdata.put("managing_agent", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
					profileuserdata.put("managment_surveyor", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
					profileuserdata.put("facility_manager", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));
					//profileuserdata.put("site_address",Utils.ReplaceAsciChar( cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
					 /*18-10 */
					//profileuserdata.put("site_address",Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
					profileuserdata.put("site_name", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
					profileuserdata.put("address1", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
					profileuserdata.put("address2", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
					profileuserdata.put("address3", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
					profileuserdata.put("pincode", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));
					profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
					profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
					//Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd");
					//Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)),"dd-mm-yyyy","yyyy-mm-dd");
					profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
					profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
					profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
					profileuserdata.put("contractor_performance", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE))));
					profileuserdata.put("statutory_audit_score", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE))));
					profileuserdata.put("contractor_improvement", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE))));
					profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
					profileuserdata.put("consultants_comments_client", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS))));
					profileuserdata.put("statutory_certification_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS))));
					profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
					jsonArrayprofiledata.put(profileuserdata);

					cursordata = dbHelper.getallcompleteSurveyAnsProfileid(Profileid);
					try {
						if (cursordata != null && cursordata.getCount() > 0) {
							do {
								JSONObject useranswer = new JSONObject();
								useranswer.put("id", cursordata.getString(cursordata.getColumnIndex(DBUtility.QUESTION_ID)));
								useranswer.put("answer", Utils.ReplaceAsciChar(cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ANSWER))));
								useranswer.put("priority", cursordata.getString(cursordata.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
								useranswer.put("user_id", cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ID)));
								// useranswer.put("dateoflastcertificate",cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
								if (!cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
									// Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
									useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
								} else {
									useranswer.put("dateoflastcertificate", "");
								}
								jsonArrayanswerdata.put(useranswer);
								// data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
								// data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
							} while (cursordata.moveToNext());
						}
					} finally {
						cursordata.close();
					}
					/*JSONObject useranswer = new JSONObject();
					useranswer.put("id",cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
					useranswer.put("answer",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER)));
					useranswer.put("priority",cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
					useranswer.put("user_id",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
					jsonArrayanswerdata .put(useranswer);*/
					//data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
					//data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
					Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
					Jsonrequest.put("data", jsonArrayanswerdata);
					Log.d("JsonRequest", Jsonrequest.toString());
					senddatatoservernew(Jsonrequest, Profileid);
				/*Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
				Jsonrequest.put("data", jsonArrayanswerdata);
				Log.d("JsonRequest", Jsonrequest.toString());
				senddatatoserver(Jsonrequest);*/
				} catch (JSONException e) {
					Log.e("",""+e);
					e.printStackTrace();
				}
			} else {
				if (totalSynced > 0) {
					totalImageCount = dbHelper.getonebyoneimagePendingcount();
					shownotification(totalSynced);
					totalSynced = 0;
				}
				Log.d("No Data", "No Data for sync" + totalSynced);
			}
		} finally {
			cursor.close();
		}
	}

	/*new amitk 31-1-17*/
	public  void senddatatoservernew(JSONObject jsonObject, final String profid) {
		try {
			if (!Utils.isNetworkAvailable(context)) {
				// Utils.showToast(c, c.getResources().getString(R.string.no_internet_connection));
			}  else {
				// Utils.showProgress(SplashActivity.this);
				WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
				RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
				Call<ErrorResponse> call = service.UserAnswer(body);
				call.enqueue(new Callback<ErrorResponse>() {
					@Override
					public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
						if (response.isSuccessful()) {
							ErrorResponse result = response.body();
							if (result.getSuccess()) {
								dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,profid);
								Log.d("Data synced","Data Synchronized with surver");
								totalSynced++;
								// senddatatoserver();
								// Utils.showToast(SplashActivity.this,response.body().getMessage());
								phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, profid, UserId);
								if (phtolist!=null&&phtolist.size()>0) {
									/*amitk 31-1-17 background image*/
									dbHelper.updatePhotosflagCompleteSurveyData(profid,UserId);
									if (firstime==0) {
										sendPendingImageToServer();
										Log.d("send pending call","send pending call true");
										firstime=1;
									}
								//	uploadimage(SurveyId,profid,UserId);
								//	Log.d("Proflid for image",profid);
								} else {
									//dbHelper.DeleteSyncUserPhotos();
									//dbHelper.DeleteSyncUserAnswer();

									/*synccount1++;
									Log.d("Sync count","No Data for sync"+synccount);
									if(synccount==synccount1){
										if (synccount>0)
										{
											shownotification(synccount);
											Log.d("Sync count","No Data for sync"+synccount);
											synccount=0;
										}
										Log.d("No Data","No Data for sync"+synccount);
									}*/
									/*new ExportDatabaseCSVTask().execute();

									senddatatoservernew();*/
									//	senddatatoserver();
                                /*    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                    finish();*/
								}
								new ExportDatabaseCSVTask().execute();
								// Manisha commented 18-04-20
								// senddatatoservernew();
							} else {
								//Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
								Log.d("Data synced","Data Synchronized error");
							}
						}
						else {
							//  Utils.hideProgress();
							Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

							try {
								ErrorResponse errors = converter.convert(response.errorBody());
								Log.d("Data synced","Data Synchronized error");
								Toast.makeText(context, errors.getMessage(), Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								//Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
								Log.d("Data synced","Data Synchronized error");
							}
						}
					}

					@Override
					public void onFailure(Call<ErrorResponse> call, Throwable t) {
						Log.v("onFailure", "onFailure");
						//Utils.showToast(c, getResources().getString(R.string.server_error));
						//Toast.makeText(c, R.string.server_error, Toast.LENGTH_SHORT).show();
						//Utils.hideProgress();
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /* send pending image to server amitk 31-1-17*/
	private void sendPendingImageToServer() {
		PhotoModel photo = dbHelper.getonebyoneimagePending();
		int toaalpendingcount = dbHelper.getonebyoneimagePendingcount();

		if (!Utils.isImageSyncing) {
			totalImageCount= dbHelper.getonebyoneimagePendingcount();
		}

		Log.d("total pending count","=> "+toaalpendingcount+"");
		if(photo!=null) {
			if (oldprofile.equalsIgnoreCase("")) {
				oldprofile = photo.getProfileId();
				groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(photo.getProfileId());
			}
			if (!oldprofile.equalsIgnoreCase(photo.getProfileId())) {
				oldprofile = photo.getProfileId();
				groupbyphotocount = 0;
				groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(photo.getProfileId());
			}

			if (map!=null)
			{
				map.clear();
			}
			if (mapcomment!=null)
			{
				mapcomment.clear();
			}
			if (Utils.checkfile(photo.getPhotoPath())) {
				Utils.isImageSyncing=true;
				File file = new File(photo.getPhotoPath());
				RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
				map.put("image[" + 0 + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
				//  map.put("image["+i+"]", fileBody);
				//mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment());
				mapcomment.put("comment[" + 0 + "]", Utils.getRequestBody(photo.getPhotoComment()));
				InsertPhotonew(photo.getProfileId(),photo.getUserId(),photo.getPhotoPath(),photo.getPhotoComment());
			}
		}
		else
		{
			Utils.isImageSyncing=false;
			if (totalSyncedImage>0)
			{
				cancelNotification();
				shownotificationImage(totalSyncedImage);
				Log.d("Total synced image "," => "+totalSyncedImage+"");
			}
		}

	}


	/*amitk send photo service call android*/

	private void InsertPhotonew(final String Profileid1, final String Userid1,final String photoPath,final String photoComment) {
		try {
			totalSyncedImage++;
			groupbyphotocount++;
			if (groupbyphotocount>groupbyphotototal) {
				groupbyphotocount=1;
				groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(Profileid1);
			}
			shownotificationImagewithcount(groupbyphotocount,Profileid1);
			// Utils.showProgress(CompleteSurveyAndSync.this);
			WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
			RequestBody profileidbody =Utils.getRequestBody(Profileid1);

			Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);
			//Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);
			call.enqueue(new Callback<ErrorResponse>() {

				@Override
				public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
					//Utils.hideProgress();
					if (response.isSuccessful()) {
						ErrorResponse result = response.body();
						if (result.getSuccess()) {
							// Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();
							long dbstatus=dbHelper.updatePhotosSyncFlag(Profileid1,Userid1,photoPath,photoComment);
							if (dbstatus>0) {
								//popupWindow.dismiss();
								Log.d("image synced","Successfully");
							}
							else {
								// popupWindow.dismiss();
								Log.d("image synced","not successfully");
							}

							//dbHelper.DeleteSyncUserPhotos();
							//dbHelper.DeleteSyncUserAnswer();
							/*synccount1++;
							Log.d("Sync count","No Data for sync"+synccount);
							if(synccount==synccount1){
								if (synccount>0)
								{
									shownotification(synccount);
									synccount=0;
								}
								Log.d("No Data","No Data for sync"+synccount);
							}*/
							new ExportDatabaseCSVTask().execute();
							sendPendingImageToServer();
							//senddatatoserver();
							//synccount++;
                           /* Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                            finish();*/
						}
					} else {
						// Utils.hideProgress();
						Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
					}
				}

				@Override
				public void onFailure(Call<ErrorResponse> call, Throwable t) {
					Log.v("onFailure", "onFailure");
					// Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
					// Utils.hideProgress();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shownotificationImage(Integer synccount) {
		Intent notificationIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SplashActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
				.setContentText(synccount+" Image Sync successfully")
				 //.setContentText(" Survey Sync successfully")
				.setTicker(context.getString(R.string.app_name))
				.setAutoCancel(true)
				.setSmallIcon(getNotificationIcon())
				.setContentIntent(pendingIntent).build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		/*Random random = new Random();
		int m = random.nextInt(9999 - 1000) + 1000;*/
		notificationManager.notify(IMAGE_NOTIFICATION_ID, notification);
	}

	private int getNotificationIcon() {
		boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return useWhiteIcon ? R.drawable.app_icon_white : R.mipmap.ic_launcher;
	}

	/*amitk 15-2-17*/
	public void shownotificationImagewithcount(Integer synccount,String Profileid) {
		Intent notificationIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SplashActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		String message ="";

		/*if (synccount==totalImageCount) {
			message=synccount+" Image Sync successfully";
		}
		else
		{
		}
		*/
		try {
			//message = dbHelper.getSurveyName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";
			message = dbHelper.getSurveyNameSiteName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";
		} catch (Exception ex) {
		}
		Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message)
				//.setContentText(" Survey Sync successfully")
				.setTicker(context.getString(R.string.app_name))
				.setAutoCancel(true)
				.setSmallIcon(getNotificationIcon())
				.setContentIntent(pendingIntent).build();

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		/*Random random = new Random();
		int m = random.nextInt(9999 - 1000) + 1000;*/
		notificationManager.notify(IMAGE_NOTIFICATION_ID, notification);
	}

	public  void cancelNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
		nMgr.cancel(IMAGE_NOTIFICATION_ID);
	}
}
