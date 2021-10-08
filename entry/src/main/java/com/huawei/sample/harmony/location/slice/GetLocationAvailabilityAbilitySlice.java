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

import com.huawei.harmony.location.ResourceTable;
import com.huawei.hms.location.harmony.LocationAvailability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;

/**
 * GetLocationAvailability Example
 */
public class GetLocationAvailabilityAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "GetLocationAvailabilityAbilitySlice";

    private Text showLocation;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_get_location_availability);
        checkSelfPermission();
        findComponentById(ResourceTable.Id_btn_getLocationAvailability).setClickedListener(this);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_getLocationAvailability:
                getLocationAvailability();
                break;
            default:
                break;
        }
    }

    private void getLocationAvailability() {
        fusedLocationClient.getLocationAvailability()
                .addOnCompleteListener(harmonyTask -> {
                    if (harmonyTask.isSuccessful()) {
                        LocationAvailability locationAvailability = harmonyTask.getResult();
                        if (locationAvailability != null) {
                            String result = "getLocationAvailability onComplete:" +
                                    harmonyTask.getResult().isLocationAvailable();
                            printLog(HiLog.INFO, TAG, result);
                            printScreenLog(showLocation, result);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    String result = "getLocationAvailability onFailure: errorMessage:" + e.getMessage();
                    printLog(HiLog.INFO, TAG, result);
                    printScreenLog(showLocation, result);
                });
    }
}
