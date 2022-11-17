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

import com.huawei.agconnect.common.TextUtils;
import com.huawei.harmony.location.ResourceTable;
import com.huawei.hms.location.harmony.LocationUtils;
import com.huawei.hms.location.harmony.LonLat;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.hiviewdfx.HiLog;

public class CoordinateAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "CoordinateAbilitySlice";

    private TextField latitude;
    private TextField longitude;
    private TextField coordinateType;
    private Text showLocation;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_coordinate);
        findComponentById(ResourceTable.Id_btn_change_coordinate).setClickedListener(this);
        latitude = (TextField) findComponentById(ResourceTable.Id_coordinate_latitude_field);
        longitude = (TextField) findComponentById(ResourceTable.Id_coordinate_longitude_field);
        coordinateType = (TextField) findComponentById(ResourceTable.Id_coordinateType_field);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_change_coordinate:
                changeCoordinate();
                break;
            default:
                break;
        }
    }

    private void changeCoordinate() {
        printCoordinateLog(HiLog.INFO, "changeCoordinate begin");
        double coordinateLatitude = 0.0d;
        double coordinateLongitude = 0.0d;
        int type = 0;

        try {
            if (!TextUtils.isEmpty(latitude.getText().trim())) {
                coordinateLatitude = Double.valueOf(latitude.getText().trim());
            }
            if (!TextUtils.isEmpty(longitude.getText().trim())) {
                coordinateLongitude = Double.valueOf(longitude.getText().trim());
            }
            if (!TextUtils.isEmpty(coordinateType.getText().trim())) {
                type = Integer.valueOf(coordinateType.getText().trim());
            }
            LonLat lonLat = LocationUtils.convertCoord(coordinateLatitude, coordinateLongitude, type);
            if (lonLat == null) {
                printCoordinateLog(HiLog.INFO, "converted LatLon is null");
                return;
            }

            printCoordinateLog(HiLog.INFO, "before change latitude: " + coordinateLatitude + ",longitude:" + coordinateLongitude);
            printCoordinateLog(HiLog.INFO, "after change latitude: " + lonLat.getLatitude() + ",longitude:" + lonLat.getLongitude());
        } catch (NumberFormatException e) {
            printCoordinateLog(HiLog.INFO, "Replace the parameter with the correct one.");
        }
    }

    private void printCoordinateLog(int level, String logInfo) {
        printLog(level, TAG, logInfo);
        printScreenLog(showLocation, logInfo);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
