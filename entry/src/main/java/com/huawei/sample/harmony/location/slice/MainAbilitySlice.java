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

import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.bundle.ElementName;
import ohos.hiviewdfx.HiLog;

import com.huawei.harmony.location.ResourceTable;

public class MainAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "MainAbilitySlice";

    private String bundleName = "com.huawei.sample.harmony.location";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        checkSelfPermission();

        findComponentById(ResourceTable.Id_btn_requestLocationCallback).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_getLastLocation).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_getLocationAvailability).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_checkSetting).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_geocoder).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_coordinate).setClickedListener(this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_requestLocationCallback:
                startLocationCallbackAbility();
                break;
            case ResourceTable.Id_btn_getLastLocation:
                startLastLocationAbility();
                break;
            case ResourceTable.Id_btn_getLocationAvailability:
                startLocationAvailabilityAbility();
                break;
            case ResourceTable.Id_btn_checkSetting:
                startCheckSettingAbility();
                break;
            case ResourceTable.Id_btn_geocoder:
                startGeocoderAbility();
                break;
            case ResourceTable.Id_btn_coordinate:
                startCoordinateAbility();
                break;
            default:
                break;
        }
    }

    private void startGeocoderAbility() {
        String abilityName = bundleName + ".ability.GeocoderAbility";
        startAbility("", bundleName, abilityName);
    }

    /**
     * Opening the location request page
     */
    private void startLocationCallbackAbility() {
        String abilityName = bundleName + ".ability.RequestLocationCallbackAbility";
        startAbility("", bundleName, abilityName);
    }

    /**
     * Enable the latest location page.
     */
    private void startLastLocationAbility() {
        String abilityName = bundleName + ".ability.GetLastLocationAbility";
        startAbility("", bundleName, abilityName);
    }

    /**
     * The page for obtaining location status is displayed.
     */
    private void startLocationAvailabilityAbility() {
        String abilityName = bundleName + ".ability.GetLocationAvailabilityAbility";
        startAbility("", bundleName, abilityName);
    }

    /**
     * Open the Get Settings page
     */
    private void startCheckSettingAbility() {
        String abilityName = bundleName + ".ability.CheckSettingAbility";
        startAbility("", bundleName, abilityName);
    }

    private void startCoordinateAbility() {
        String abilityName = bundleName + ".ability.CoordinateAbility";
        startAbility("", bundleName, abilityName);
    }


    private void startAbility(String deviceId, String bundleName, String abilityName) {
        Intent intent = new Intent();
        // Specify the target through ElementName.
        // The three parameters of the constructor are: device ID (an empty string indicates the current device), application package name, and mobility fully qualified name.
        ElementName name = new ElementName(deviceId, bundleName, abilityName);
        intent.setElement(name);
        printLog(HiLog.INFO, TAG, "abilityName = " + abilityName);
        startAbility(intent);
    }
}
