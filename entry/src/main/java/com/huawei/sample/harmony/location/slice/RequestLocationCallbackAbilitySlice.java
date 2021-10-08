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

import com.huawei.hms.location.harmony.HWLocation;
import com.huawei.hms.location.harmony.Location;
import com.huawei.hms.location.harmony.LocationCallback;
import com.huawei.hms.location.harmony.LocationResult;
import com.huawei.hms.location.harmony.LocationAvailability;
import com.huawei.hms.location.harmony.LocationRequest;

import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;

import com.huawei.harmony.location.ResourceTable;

import java.util.List;

/**
 * RequestLocationCallback Example
 */
public class RequestLocationCallbackAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "RequestLocationCallbackSlice";

    private Text showLocation;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                List<Location> locations = locationResult.getLocations();
                if (!locations.isEmpty()) {
                    printLog(HiLog.INFO, TAG, "onLocationResult location is not empty");
                    List<HWLocation> hwLocationList = locationResult.getHWLocationList();
                    if (!hwLocationList.isEmpty()) {
                        printLog(HiLog.INFO, TAG, "onLocationResult hwLocationList is not empty");
                    }
                    for (HWLocation hwLocation : hwLocationList) {
                        String result = "[new]onLocationResult location[Longitude,Latitude,Accuracy,"
                                + "CountryName,State,City,County,FeatureName,Provider]:"
                                + hwLocation.getLongitude() + "," + hwLocation.getLatitude() + ","
                                + hwLocation.getAccuracy() + "," + hwLocation.getCountryName() + ","
                                + hwLocation.getState() + "," + hwLocation.getCity() + ","
                                + hwLocation.getCounty() + "," + hwLocation.getFeatureName() + ","
                                + hwLocation.getProvider();
                        printLog(HiLog.INFO, TAG, result);
                        printScreenLog(showLocation, result);
                    }
                }
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            if (locationAvailability != null) {
                String result = "onLocationAvailability isLocationAvailable:"
                        + locationAvailability.isLocationAvailable();
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            }
        }
    };

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_request_location_callback);
        checkSelfPermission();

        findComponentById(ResourceTable.Id_btn_request).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_remove).setClickedListener(this);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_request:
                requestLocationUpdates();
                break;
            case ResourceTable.Id_btn_remove:
                removeLocationUpdates();
                break;
            default:
                break;
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = buildLocationRequest();
        printLog(HiLog.INFO, TAG, "requestLocationUpdates !! ");
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback)
            .addOnSuccessListener(var -> {
                String result = "requestLocationUpdatesWithCallback onSuccess";
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            })
            .addOnFailureListener(e -> {
                String result = "requestLocationUpdatesWithCallback onFailure:" + e.getMessage();
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            });
    }

    private void removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
            .addOnSuccessListener(var -> {
                String result = "removeLocationUpdatesWithCallback onSuccess";
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            })
            .addOnFailureListener(e -> {
                String result = "removeLocationUpdatesWithCallback onFailure:" + e.getMessage();
                printLog(HiLog.INFO, TAG, result);
                printScreenLog(showLocation, result);
            });
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setNeedAddress(true);
        locationRequest.setLanguage("zh");
        return locationRequest;
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
