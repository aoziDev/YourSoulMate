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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kakao.APIErrorResult;
import com.kakao.BasicKakaoStoryPostParamBuilder;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoStoryHttpResponseHandler;
import com.kakao.KakaoStoryLinkInfo;
import com.kakao.KakaoStoryService;
import com.kakao.KakaoStoryService.StoryType;
import com.kakao.LinkKakaoStoryPostParamBuilder;
import com.kakao.MyStoryInfo;
import com.kakao.helper.Logger;
import com.kakao.template.loginbase.SampleSignupActivity;
import com.pread.yoursoulmate.GlobalData;

public class KakaoStorySignupActivity extends SampleSignupActivity {

//	private final String execParam = "place=1111";
//  private final String marketParam = "referrer=kakaostory";
    private final String scrapUrl = "http://www.google.com";

    protected void redirectMainActivity() {
       GlobalData gd = (GlobalData)getApplicationContext();
       getLinkInfo(gd.getResultStr());
    }
    
	private void getLinkInfo(final String content) {
        KakaoStoryService.requestGetLinkInfo(new MyKakaoStoryHttpResponseHandler<KakaoStoryLinkInfo>() {
            @Override
            protected void onHttpSuccess(final KakaoStoryLinkInfo kakaoStoryLinkInfo) {
            	Log.e("onHttpSuccess", "Success");
                if (kakaoStoryLinkInfo != null && kakaoStoryLinkInfo.isValidResult()) {
                    Toast.makeText(getApplicationContext(), "succeeded to get link info.\n" + kakaoStoryLinkInfo, Toast.LENGTH_SHORT).show();
                    requestPostLink(kakaoStoryLinkInfo, content);
                } else {
                    Toast.makeText(getApplicationContext(), "failed to get link info.\nkakaoStoryLinkInfo=null", Toast.LENGTH_SHORT).show();
                }
            }
        }, scrapUrl);
    }
	
	private void requestPostLink(final KakaoStoryLinkInfo kakaoStoryLinkInfo, String content) {
        final LinkKakaoStoryPostParamBuilder postParamBuilder = new LinkKakaoStoryPostParamBuilder(kakaoStoryLinkInfo);
        postParamBuilder.setContent(content);
        requestPost(StoryType.LINK, postParamBuilder);
    }
	
    private void requestPost(final StoryType storyType, final BasicKakaoStoryPostParamBuilder postParamBuilder) {
        //postParamBuilder.setAndroidExecuteParam(execParam).setIOSExecuteParam(execParam).setAndroidMarketParam(marketParam).setIOSMarketParam(marketParam);
        try {
            final Bundle parameters = postParamBuilder.build();
            KakaoStoryService.requestPost(storyType, new MyKakaoStoryHttpResponseHandler<MyStoryInfo>() {
                @Override
                protected void onHttpSuccess(final MyStoryInfo myStoryInfo) {
                    if(myStoryInfo.getId() != null) {
                        Toast.makeText(getApplicationContext(), "succeeded to post " + storyType, Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "failed to post " + storyType + " on KakaoStory.\nmyStoryId=null", Toast.LENGTH_SHORT).show();
                    }
                }
            }, parameters);
        } catch (KakaoParameterException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @SuppressLint("HandlerLeak") 
    private abstract class MyKakaoStoryHttpResponseHandler<T> extends KakaoStoryHttpResponseHandler<T> {

        @Override
        protected void onHttpSessionClosedFailure(final APIErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        protected void onNotKakaoStoryUser() {
            Toast.makeText(getApplicationContext(), "not KakaoStory user", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onFailure(final APIErrorResult errorResult) {
            final String message = "MyKakaoStoryHttpResponseHandler : failure : " + errorResult;
            Logger.getInstance().d(message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    protected void redirectLoginActivity() {
        Intent intent = new Intent(this, KakaoStoryLoginActivity.class);
        startActivity(intent);
        finish();
    }
}