package com.pread.yoursoulmate.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

public class FacebookPostingActivity extends Activity {
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private LoginButton authButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		init();
		setCloseButton();
		setLoginButton();
	}

	private void init() {
		authButton = (LoginButton)findViewById(R.id.authButton);
	}
	
	private void setCloseButton() {
		findViewById(R.id.tv_facebook_posting_close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setLoginButton() {
		authButton.setOnErrorListener(new OnErrorListener() {      
			@Override
			public void onError(FacebookException error) {
				// Login error
			}
		});

		publishStory();     
		
		// session status가 바뀔 때 -> 로그인하고 액티비티 복귀한다음 호출
		authButton.setSessionStatusCallback(new Session.StatusCallback() {
			@Override
			@SuppressWarnings("deprecation")
			public void call(Session session, SessionState state, Exception exception) { 
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,Response response) {    
							if (user != null) { 
								// Login success (user에 정보가 들어있음.)
								// Log.i(TAG,"User ID "+ user.getId());
								publishStory();     
							}
						}
					});
				}
			}
		});

	}

	private boolean isLogined(){
		Session session = Session.getActiveSession();
		if(session == null)
			return false;

		if(!session.isOpened())
			return false;

		return true;
	}

	private ProgressDialog progDialog;
	private void publishStory() {
		if (!isLogined()) {
			return;
		}
		
		authButton.setVisibility(View.GONE);
		
		Session session = Session.getActiveSession();
		if (session != null) {

			// Check for publish permissions    
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);

				progDialog.dismiss();
				Toast.makeText(FacebookPostingActivity.this, "등록 실패(Permission error)", Toast.LENGTH_LONG).show();
				finish();
				return;
			}

			progDialog = ProgressDialog.show(FacebookPostingActivity.this, null, "등록중..", true);        

			GlobalData gd = (GlobalData)getApplication();

			Bundle postParams = new Bundle();
			/* 테스트용 임시 market주소 */
			postParams.putString("link", "https://play.google.com/store/apps/details?id=com.macropinch.pearl");
			/* Release시 아래주소로 변경 */
//			postParams.putString("link", "https://play.google.com/store/apps/details?id=com.pread.yoursoulmate");
			
			/* 링크주소가 Market주소가 아닐 때 링크 정보를 아래와 같이 수정할 수 있음 */
//			postParams.putString("name", getString(R.string.your_past_life));
//			postParams.putString("description", gd.getResultStr() + " " + getString(R.string.was));
//			postParams.putString("caption", "당신의 전생을 확인해보세요!!");
//			postParams.putString("picture", "http://imgtest.monkey3.co.kr/get_image.php?type=album&id=185582&w=100");

			String resultMessage = String.format("%s\n%s\n%s", getString(R.string.your_past_life), gd.getResultStr(), getString(R.string.was));
			postParams.putString("message", resultMessage);

			
			Request.Callback callback= new Request.Callback() {
				public void onCompleted(Response response) {
					progDialog.dismiss();
					Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_LONG).show();
					finish();
					/*
                	JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                        Toast.makeText(
                                FacebookLoginActivity.this,
                                "등록성공",
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        //Log.i(TAG, "JSON error "+ e.getMessage());
                    }

                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        //Toast.makeText(FacebookLoginActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(FacebookLoginActivity.this, postId, Toast.LENGTH_LONG).show();
                    }
					 */
				}
			};

			Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}


	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}


	// 액티비티로 복귀할떄 (로그인 후에..)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
