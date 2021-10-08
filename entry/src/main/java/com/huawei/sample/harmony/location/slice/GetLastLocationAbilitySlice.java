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

import com.huawei.hms.location.harmony.LocationRequest;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;

import com.huawei.harmony.location.ResourceTable;

/**
 * GetLastLocation Example
 */
public class GetLastLocationAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "GetLastLocationSlice";

    private Text showLocation;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_get_last_location);
        checkSelfPermission();
        findComponentById(ResourceTable.Id_btn_getLastLocation).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_getLastLocationWithAddress).setClickedListener(this);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_getLastLocation:
                getLastLocation();
                break;
            case ResourceTable.Id_btn_getLastLocationWithAddress:
                getLastLocationWithAddress();
                break;
            default:
                break;
        }
    }

    /**
     * 获取最新的location信息
     */
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if (null == location) {
                    printLog(HiLog.INFO, TAG, "[old]getLastLocation onSuccess: location is null");
                    return;
                }
                String result = "[old]getLastLocation onSuccess location[Longitude,Latitude]:"
                        + location.getLongitude() + "," + location.getLatitude();
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            })
            .addOnFailureListener(e -> {
                printLog(HiLog.INFO, TAG, "getLastLocation onFailure:" + e.getMessage());
                printScreenLog(showLocation, "getLastLocation onFailure:" + e.getMessage());
            });
    }

    /**
     * 获取携带地址语义的location信息
     */
    private void getLastLocationWithAddress() {
        fusedLocationClient.getLastLocationWithAddress(buildLocationRequest())
            .addOnSuccessListener(location -> {
                if (null == location) {
                    printLog(HiLog.INFO, TAG, "[new]getLastLocationWithAddress onSuccess: hwLocation is null");
                    return;
                }
                String result = "[new]getLastLocationWithAddress onSuccess location" +
                        "[Longitude,Latitude,Accuracy,CountryName,State,City,County,FeatureName]:"
                        + location.getLongitude() + "," + location.getLatitude() + ","
                        + location.getAccuracy() + "," + location.getCountryName() + ","
                        + location.getState() + "," + location.getCity() + ","
                        + location.getCounty() + "," + location.getFeatureName();
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            })
            .addOnFailureListener(e -> {
                printLog(HiLog.INFO, TAG, "getLastLocationWithAddress onFailure:" + e.getMessage());
                printScreenLog(showLocation, "getLastLocationWithAddress onFailure:" + e.getMessage());
            });
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setNeedAddress(true);
        locationRequest.setLanguage("zh");
        locationRequest.setCountryCode("CN");
        return locationRequest;
    }

}
