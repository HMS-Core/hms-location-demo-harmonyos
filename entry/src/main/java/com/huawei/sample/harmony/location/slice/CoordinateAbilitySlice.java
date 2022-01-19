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
import com.huawei.hms.location.harmony.LatLon;
import com.huawei.hms.location.harmony.LocationUtils;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.hiviewdfx.HiLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String latitudeText = latitude.getText();
        String longitudeText = longitude.getText();
        String coordinateTypeText = coordinateType.getText();
        if (!checkLatitudeValid(latitudeText)) {
            printCoordinateLog(HiLog.INFO, "latitudeText is illegal");
            return;
        }
        if (!checkLongitudeValid(longitudeText)) {
            printCoordinateLog(HiLog.INFO, "longitudeText is illegal");
            return;
        }

        if (!isInt(coordinateTypeText)) {
            printCoordinateLog(HiLog.INFO, "coordinateTypeText is illegal");
            return;
        }

        final int lastCoordinateType;
        try {
            lastCoordinateType = Integer.parseInt(coordinateTypeText);
        } catch (NumberFormatException e) {
            printCoordinateLog(HiLog.INFO, "coordinateTypeText is illegal");
            return;
        }

        final double coordinateLatitude = Double.parseDouble(latitudeText);
        final double coordinateLongitude = Double.parseDouble(longitudeText);

        LatLon latLon = LocationUtils.convertCoord(coordinateLatitude, coordinateLongitude, lastCoordinateType);
        if (latLon == null) {
            printCoordinateLog(HiLog.INFO, "latLon is null");
            return;
        }

        printCoordinateLog(HiLog.INFO, "before change latitude: " + coordinateLatitude + ",longitude:" + coordinateLongitude);
        printCoordinateLog(HiLog.INFO, "after change latitude: " + latLon.getLatitude() + ",longitude:" + latLon.getLongitude());
    }

    private void printCoordinateLog(int level, String logInfo) {
        printLog(level, TAG, logInfo);
        printScreenLog(showLocation, logInfo);
    }

    public static boolean isInt(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.matches();
    }

    private boolean isDouble(String input) {
        if (isInt(input)) {
            return true;
        }
        Matcher mer = Pattern.compile("^[+-]?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$").matcher(input);
        return mer.matches();
    }

    private boolean checkLongitudeValid(String longitude) {
        if (!isDouble(longitude)) {
            return false;
        }
        return true;
    }

    private boolean checkLatitudeValid(String latitude) {
        if (!isDouble(latitude)) {
            return false;
        }
        return true;
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
