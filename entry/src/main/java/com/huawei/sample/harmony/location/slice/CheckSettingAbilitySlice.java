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

import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.location.harmony.LocationSettingsStatusCodes;
import com.huawei.hms.location.harmony.LocationRequest;
import com.huawei.hms.location.harmony.LocationSettingsRequest;
import com.huawei.hms.location.harmony.LocationSettingsResponse;
import com.huawei.hms.location.harmony.LocationSettingsStates;
import com.huawei.hms.location.harmony.SettingsProviderClient;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;

import com.huawei.harmony.location.ResourceTable;
import com.huawei.hms.location.harmony.base.ApiException;

/**
 * CheckSetting Example
 */
public class CheckSettingAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "CheckSettingSlice";

    private SettingsProviderClient settingsProviderClient;

    private Text showLocation;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_check_setting);
        checkSelfPermission();
        settingsProviderClient = new SettingsProviderClient(this);
        findComponentById(ResourceTable.Id_btn_check_setting).setClickedListener(this);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_check_setting:
                checkLocationSettings();
                break;
            default:
                break;
        }
    }

    /**
     * 检查配置项参数
     */
    private void checkLocationSettings() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        LocationSettingsRequest request =
                builder.addLocationRequest(locationRequest).setAlwaysShow(false).setNeedBle(false).build();
        settingsProviderClient.checkLocationSettings(request)
            .addOnSuccessListener((OnSuccessListener<LocationSettingsResponse>) response -> {
                LocationSettingsStates locationSettingsStates = response.getLocationSettingsStates();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(System.lineSeparator())
                        .append("isBlePresent=").append(locationSettingsStates.isBlePresent())
                        .append(System.lineSeparator())
                        .append("isBleUsable=").append(locationSettingsStates.isBleUsable())
                        .append(System.lineSeparator())
                        .append("isGnssPresent=").append(locationSettingsStates.isGnssPresent())
                        .append(System.lineSeparator())
                        .append("isGnssUsable=").append(locationSettingsStates.isGnssUsable())
                        .append(System.lineSeparator())
                        .append("isLocationPresent=").append(locationSettingsStates.isLocationPresent())
                        .append(System.lineSeparator())
                        .append("isLocationUsable=").append(locationSettingsStates.isLocationUsable())
                        .append(System.lineSeparator())
                        .append("isNetworkLocationUsable=").append(locationSettingsStates.isNetworkLocationUsable())
                        .append(System.lineSeparator())
                        .append("isNetworkLocationPresent=").append(locationSettingsStates.isNetworkLocationPresent());

                printLog(HiLog.INFO, TAG, "checkLocationSettings onSuccess:" + stringBuilder.toString());
                printScreenLog(showLocation, "checkLocationSettings onSuccess:" + stringBuilder.toString());
            })
            .addOnFailureListener(exp -> {
                if (exp instanceof ApiException) {
                    int statusCode = ((ApiException) exp).getStatusCode();
                    String result = "checkLocationSettings onFailure:errorCode:" + statusCode
                            + " errorMessage:" + exp.getMessage();
                    printLog(HiLog.INFO, TAG, result);
                    printScreenLog(showLocation, result);
                    if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        printLog(HiLog.INFO, TAG, "Location settings are not satisfied. Attempting to upgrade "
                                + "location settings ");
                    }
                }
            });
    }
}
