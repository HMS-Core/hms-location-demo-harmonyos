/*
*       Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.huawei.sample.harmony.location.ability;

import com.huawei.sample.harmony.location.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Arrays;

public class MainAbility extends Ability {
    private static final int REQUEST_CODE = 0x1000;

    private static final int DOMAIN = 0xD001100;

    private HiLogLabel hiLogLabel = new HiLogLabel(HiLog.LOG_APP, DOMAIN, "TAG");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        String[] strings = {"ohos.permission.LOCATION", "ohos.permission.LOCATION_IN_BACKGROUND"};
        requestPermissionsFromUser(strings, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        HiLog.info(hiLogLabel, "MainAbility onRequestPermissionsFromUserResult :" + Arrays.toString(grantResults));
        if (requestCode == REQUEST_CODE) {
            HiLog.info(hiLogLabel, "MainAbility request permission success.");
        }
    }
}
