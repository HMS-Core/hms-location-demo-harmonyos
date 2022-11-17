/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2022. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.sample.harmony.location.slice;

import com.huawei.hms.location.harmony.LocationRequest;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
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
     * Obtain the latest location information.
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
     * Obtains location information that carries address semantics.
     */
    private void getLastLocationWithAddress() {
        LocationRequest locationRequest = buildLocationRequest();
        if (locationRequest != null) {
            fusedLocationClient.getLastLocationWithAddress(locationRequest)
                    .addOnSuccessListener(location -> {
                        if (null == location) {
                            printScreenLog(showLocation, "[new]getLastLocationWithAddress onSuccess: location is null");
                            printLog(HiLog.INFO, TAG, "[new]getLastLocationWithAddress onSuccess: hwLocation is null");
                            return;
                        }
                        String result = "[new]getLastLocationWithAddress onSuccess location" +
                                "[Longitude,Latitude,Accuracy,CountryName,State,City,County,FeatureName,CoordinateType]:"
                                + location.getLongitude() + "," + location.getLatitude() + ","
                                + location.getAccuracy() + "," + location.getCountryName() + ","
                                + location.getState() + "," + location.getCity() + ","
                                + location.getCounty() + "," + location.getFeatureName() + ","
                                + location.getCoordinateType();
                        printLog(HiLog.INFO, TAG, result);
                        printScreenLog(showLocation, result);
                    })
                    .addOnFailureListener(e -> {
                        printLog(HiLog.INFO, TAG, "getLastLocationWithAddress onFailure:" + e.getMessage());
                        printScreenLog(showLocation, "getLastLocationWithAddress onFailure:" + e.getMessage());
                    });
        }
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setNeedAddress(true);
        locationRequest.setLanguage("zh");
        locationRequest.setCountryCode("CN");

        // 设置84转02坐标系转换标志，默认为0.默认返回84坐标系。
        String coordinateType = getText(ResourceTable.Id_et_coordinateType);
        if (coordinateType != null && !coordinateType.isEmpty()) {
            try {
                // 此处主要用于解决鸿蒙的bug,number键盘可以输入"@"和"."。因此做以下处理。
                locationRequest.setCoordinateType(Integer.valueOf(coordinateType));
            } catch (Exception e) {
                printLog(HiLog.INFO, TAG, "Replace the parameter with the correct one.");
                printScreenLog(showLocation, "Replace the parameter with the correct one.");
                return null;
            }
        } else {
            // 当coordinateType数值为空，传入默认值。
            locationRequest.setCoordinateType(LocationRequest.COORDINATE_TYPE_WGS84);
        }
        return locationRequest;
    }

    public String getText(int resID) {
        TextField textField = (TextField) findComponentById(resID);
        return textField.getText();
    }
}
