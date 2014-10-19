/**
 * Copyright 2014 Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission.??
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pread.yoursoulmate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kakao.APIErrorResult;
import com.kakao.BasicKakaoStoryPostParamBuilder;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoStoryHttpResponseHandler;
import com.kakao.KakaoStoryLinkInfo;
import com.kakao.KakaoStoryService;
import com.kakao.KakaoStoryService.StoryType;
import com.kakao.LinkKakaoStoryPostParamBuilder;
import com.kakao.MeResponseCallback;
import com.kakao.MyStoryInfo;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.helper.Logger;
import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

public class KakaoStoryPostingActivity extends Activity {

//	private final String execParam = "place=1111";
//  private final String marketParam = "referrer=kakaostory";
	
	/* 테스트용 market주소 */
//	private final String scrapUrl = "https://play.google.com/store/apps/details?id=com.macropinch.pearl";
	
	/* Release시 아래 주소로 수정 */
    private final String scrapUrl = "https://play.google.com/store/apps/details?id=com.pread.yoursoulmate";
    
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 자동가입앱인 경우는 가입안된 유저가 나오는 것은 에러 상황.
     */
    protected void showSignup() {
        Logger.getInstance().d("not registered user");
        redirectLoginActivity();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            protected void onSuccess(final UserProfile userProfile) {
                Logger.getInstance().d("UserProfile : " + userProfile);
                userProfile.saveUserToCache();
                redirectMainActivity();
            }

            @Override
            protected void onNotSignedUp() {
                showSignup();
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.getInstance().d(message);
                redirectLoginActivity();
            }
        });
    }

    
    @SuppressLint("HandlerLeak") 
    private abstract class MyKakaoStoryHttpResponseHandler<T> extends KakaoStoryHttpResponseHandler<T> {
        @Override
        protected void onHttpSessionClosedFailure(final APIErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        protected void onNotKakaoStoryUser() {
            Toast.makeText(getApplicationContext(), "카카오스토리에 가입되지 않은 유저입니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onFailure(final APIErrorResult errorResult) {
            final String message = "HttpResponseHandler 실패(" + errorResult + ")";
            Logger.getInstance().d(message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
    
    protected void redirectLoginActivity() {
        Intent intent = new Intent(this, KakaoStoryLoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    protected void redirectMainActivity() {
       GlobalData gd = (GlobalData)getApplicationContext();
       
       final String content = String.format("%s\n%s\n%s", getString(R.string.your_past_life), gd.getResultStr(), getString(R.string.was));

       KakaoStoryService.requestGetLinkInfo(new MyKakaoStoryHttpResponseHandler<KakaoStoryLinkInfo>() {
           @Override
           protected void onHttpSuccess(final KakaoStoryLinkInfo kakaoStoryLinkInfo) {
               if (kakaoStoryLinkInfo != null && kakaoStoryLinkInfo.isValidResult()) {
                   //Toast.makeText(getApplicationContext(), "succeeded to get link info.\n" + kakaoStoryLinkInfo, Toast.LENGTH_SHORT).show();
                   requestPostLink(kakaoStoryLinkInfo, content);
               } else {
                   Toast.makeText(getApplicationContext(), "Link정보 가져오기 실패", Toast.LENGTH_LONG).show();
               }
           }
       }, scrapUrl);
    }
    
	private void requestPostLink(final KakaoStoryLinkInfo kakaoStoryLinkInfo, String content) {
        final LinkKakaoStoryPostParamBuilder postParamBuilder = new LinkKakaoStoryPostParamBuilder(kakaoStoryLinkInfo);
        postParamBuilder.setContent(content);
        requestPost(StoryType.LINK, postParamBuilder);
    }
	
	private ProgressDialog progDialog;
    private void requestPost(final StoryType storyType, final BasicKakaoStoryPostParamBuilder postParamBuilder) {
        //postParamBuilder.setAndroidExecuteParam(execParam).setIOSExecuteParam(execParam).setAndroidMarketParam(marketParam).setIOSMarketParam(marketParam);
		progDialog = ProgressDialog.show(KakaoStoryPostingActivity.this, null, "등록중..", true);        

    	try {
            final Bundle parameters = postParamBuilder.build();
            KakaoStoryService.requestPost(storyType, new MyKakaoStoryHttpResponseHandler<MyStoryInfo>() {
                @Override
                protected void onHttpSuccess(final MyStoryInfo myStoryInfo) {
                    if(myStoryInfo.getId() != null) {
                        Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "등록실패" + "(on KakaoStory)", Toast.LENGTH_LONG).show();
                    }
                    
                    progDialog.dismiss();
                    finish();
                }
            }, parameters);
        } catch (KakaoParameterException e) {
        	progDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
