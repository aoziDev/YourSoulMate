/**
 * Copyright 2014 Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
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
package com.kakao.sample.push;

import android.content.Intent;
import android.os.Bundle;
import com.kakao.template.loginbase.SampleLoginActivity;

/**
 * 카카오계정 로그인을 사용하는 앱에 한해서 카카오 SDK가 푸시 토큰 등록/해제를 대행해 준다.
 *
 * 세션을 오픈한 후에 {@link PushMainActivity}로 넘긴다.
 * (이 샘플은 가입 절차가 따로 필요 없는 자동가입 앱이다.)
 * @author MJ
 */
public class PushLoginActivity extends SampleLoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackground(getResources().getDrawable(R.drawable.push_sample_login_background));
    }

    /**
     * 세션이 오픈되었으면 가입 페이지로 이동한다.
     */
    @Override
    protected void onSessionOpened() {
        final Intent intent = new Intent(PushLoginActivity.this, PushSignupActivity.class);
        startActivity(intent);
        finish();
    }
}
