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

package com.huawei.sample.harmony.location.slice;

import com.huawei.hms.location.harmony.FusedLocationClient;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseAbilitySlice extends AbilitySlice {
    private static final String TAG = "BaseAbilitySlice";

    private static final int DOMAIN = 0xD001100;

    private static final int REQUEST_CODE = 0x1001;

    private static Map<String, HiLogLabel> logLabelMap = new HashMap<>();

    public FusedLocationClient fusedLocationClient;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        fusedLocationClient = new FusedLocationClient(this);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    public void checkSelfPermission() {
        if (verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED) {
            printLog(HiLog.INFO, TAG, "Self: LOCATION permission not granted!");
            if (canRequestPermission("ohos.permission.LOCATION")) {
                printLog(HiLog.INFO, TAG, "Self: can request permission here");
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.LOCATION"}, REQUEST_CODE);
            } else {
                printLog(HiLog.WARN, TAG, "Self: enter settings to set permission");
            }
        } else {
            printLog(HiLog.INFO, TAG, "Self: LOCATION permission granted!");
        }

        if (verifySelfPermission("ohos.permission.LOCATION_IN_BACKGROUND") != IBundleManager.PERMISSION_GRANTED) {
            printLog(HiLog.INFO, TAG, "Self: LOCATION_IN_BACKGROUND permission not granted!");
            if (canRequestPermission("ohos.permission.LOCATION_IN_BACKGROUND")) {
                printLog(HiLog.INFO, TAG, "Self: can request permission here");
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.LOCATION_IN_BACKGROUND"}, REQUEST_CODE);
            } else {
                printLog(HiLog.WARN, TAG, "Self: enter settings to set permission");
            }
        } else {
            printLog(HiLog.INFO, TAG, "Self: LOCATION_IN_BACKGROUND permission granted!");
        }
    }

    public void printLog(int level, String tag, String message) {
        HiLogLabel hiLogLabel = getLogLabel(tag);
        switch (level) {
            case HiLog.INFO:
                HiLog.info(hiLogLabel, message);
                break;
            case HiLog.WARN:
                HiLog.warn(hiLogLabel, message);
                break;
            case HiLog.ERROR:
                HiLog.error(hiLogLabel, message);
                break;
            default:
                HiLog.debug(hiLogLabel, message);
                break;
        }
    }

    private HiLogLabel getLogLabel(String tag) {
        HiLogLabel label;
        String tagStr;

        if (tag == null || tag.isEmpty()) {
            tagStr = "TAG";
        } else {
            tagStr = tag;
        }
        if (logLabelMap.containsKey(tagStr)) {
            label = logLabelMap.getOrDefault(tagStr, new HiLogLabel(HiLog.LOG_APP, DOMAIN, "TAG"));
        } else {
            label = new HiLogLabel(HiLog.LOG_APP, DOMAIN, tagStr);
            logLabelMap.put(tagStr, label);
        }
        return label;
    }

    public void printScreenLog(Text showLocation, String msg) {
        StringBuilder outputBuilder = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(curDate);
        outputBuilder.append(dateStr);
        outputBuilder.append(" ");
        outputBuilder.append(msg);
        outputBuilder.append(System.lineSeparator());
        showLocation.setMultipleLine(true);
        showLocation.append(outputBuilder.toString());
    }
}
