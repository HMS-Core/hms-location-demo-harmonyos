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
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.location.harmony.GeocoderProviderClient;
import com.huawei.hms.location.harmony.GetFromLocationNameRequest;
import com.huawei.hms.location.harmony.GetFromLocationRequest;
import com.huawei.hms.location.harmony.HWLocation;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.hiviewdfx.HiLog;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeocoderAbilitySlice extends BaseAbilitySlice implements Component.ClickedListener {
    private static final String TAG = "GeocoderAbilitySlice";
    private Text showLocation;

    private DirectionalLayout reverseGeocoderLayout;
    private TextField latitudeTextField;
    private TextField longitudeTextField;
    private TextField maxResults;
    private TextField language;
    private TextField country;

    private DirectionalLayout geocoderLayout;
    private TextField geocoderLocationName;
    private TextField geocoderMaxResults;
    private TextField geocoderLanguage;
    private TextField geocoderCountry;
    private TextField geocoderLowerLeftLatitude;
    private TextField geocoderLowerLeftLongitude;
    private TextField geocoderUpperRightLatitude;
    private TextField geocoderUpperRightLongitude;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_geocoder);
        checkSelfPermission();

        findComponentById(ResourceTable.Id_btn_switch_geocoder).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_get_from_location).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_get_from_location_name).setClickedListener(this);

        reverseGeocoderLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_reverseGeoCode_layout);
        latitudeTextField = (TextField) findComponentById(ResourceTable.Id_latitude_field);
        longitudeTextField = (TextField) findComponentById(ResourceTable.Id_longitude_field);
        maxResults = (TextField) findComponentById(ResourceTable.Id_maxResults_field);
        language = (TextField) findComponentById(ResourceTable.Id_language_field);
        country = (TextField) findComponentById(ResourceTable.Id_country_field);

        geocoderLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_geocoder_layout);
        geocoderLocationName = (TextField) findComponentById(ResourceTable.Id_geocoder_location_name_field);
        geocoderMaxResults = (TextField) findComponentById(ResourceTable.Id_geocoder_maxResults_field);
        geocoderLanguage = (TextField) findComponentById(ResourceTable.Id_geocoder_language_field);
        geocoderCountry = (TextField) findComponentById(ResourceTable.Id_geocoder_country_field);
        geocoderLowerLeftLatitude = (TextField) findComponentById(ResourceTable.Id_lower_left_latitude_field);
        geocoderLowerLeftLongitude = (TextField) findComponentById(ResourceTable.Id_lower_left_longitude_field);
        geocoderUpperRightLatitude = (TextField) findComponentById(ResourceTable.Id_upper_right_latitude_field);
        geocoderUpperRightLongitude = (TextField) findComponentById(ResourceTable.Id_upper_right_longitude_field);
        showLocation = (Text) findComponentById(ResourceTable.Id_show_location);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_switch_geocoder:
                switchMode();
                break;
            case ResourceTable.Id_btn_get_from_location:
                getFromLocation();
                break;
            case ResourceTable.Id_btn_get_from_location_name:
                getFromLocationName();
                break;
            default:
                break;
        }
    }

    private void getFromLocationName() {
        printLog(HiLog.INFO, TAG, "getFromLocationName begin");
        String lowerLeftLatitudeText = geocoderLowerLeftLatitude.getText();
        String lowerLeftLongitudeText = geocoderLowerLeftLongitude.getText();
        String upperRightLatitudeText = geocoderUpperRightLatitude.getText();
        String upperRightLongitudeText = geocoderUpperRightLongitude.getText();
        String maxResultsText = geocoderMaxResults.getText();
        if (!checkLatValid(lowerLeftLatitudeText)) {
            printGeocoderLog(HiLog.INFO, "lowerLeftLatitudeText is illegal");
            return;
        }
        if (!checkLogValid(lowerLeftLongitudeText)) {
            printGeocoderLog(HiLog.INFO, "LowerLeftLongitudeText is illegal");
            return;
        }
        if (!checkLogValid(upperRightLatitudeText)) {
            printGeocoderLog(HiLog.INFO, "UpperRightLatitudeText is illegal");
            return;
        }
        if (!checkLogValid(upperRightLongitudeText)) {
            printGeocoderLog(HiLog.INFO, "UpperRightLongitudeText is illegal");
            return;
        }
        if (!isInt(maxResultsText)) {
            printGeocoderLog(HiLog.INFO, "maxResults is illegal");
            return;
        }

        final int maxResult;
        try {
            maxResult = Integer.parseInt(maxResultsText);
        } catch (NumberFormatException e) {
            printGeocoderLog(HiLog.INFO, "maxResults is illegal");
            return;
        }

        final double lowerLeftLatitude = Double.parseDouble(lowerLeftLatitudeText);
        final double lowerLeftLongitude = Double.parseDouble(lowerLeftLongitudeText);
        final double upperRightLatitude = Double.parseDouble(upperRightLatitudeText);
        final double upperRightLongitude = Double.parseDouble(upperRightLongitudeText);
        final String addressName = geocoderLocationName.getText();

        final String languageText = geocoderLanguage.getText();
        final String countryText = geocoderCountry.getText();
        GetFromLocationNameRequest getFromLocationNameRequest =
                new GetFromLocationNameRequest(addressName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude, maxResult);
        Locale locale = new Locale(languageText, countryText);
        GeocoderProviderClient geocoderService = new GeocoderProviderClient(this, locale);
        // Enter the correct location information. Otherwise, the geographic information cannot be parsed. For a
        // non-China region, transfer the location information of the non-China region.
        geocoderService.getFromLocationName(getFromLocationNameRequest)
                .addOnSuccessListener((OnSuccessListener<List<HWLocation>>)
                        geocoderLocationList -> {
                            printGeocoderResult(geocoderLocationList);
                        })
                .addOnFailureListener(e -> {
                    String result = "getFromLocation onFailure:" + e.getMessage();
                    printGeocoderLog(HiLog.INFO, result);
                });
    }

    private void getFromLocation() {
        printLog(HiLog.INFO, TAG, "getFromLocation begin");
        String latitudeText = latitudeTextField.getText();
        String longitudeText = longitudeTextField.getText();
        String maxResultsText = maxResults.getText();
        if (!checkLatValid(latitudeText)) {
            printGeocoderLog(HiLog.INFO, "Latitude is illegal");
            return;
        }
        if (!checkLogValid(longitudeText)) {
            printGeocoderLog(HiLog.INFO, "Longitude is illegal");
            return;
        }
        if (!isInt(maxResultsText)) {
            printGeocoderLog(HiLog.INFO, "maxResults is illegal");
            return;
        }
        final double latitude = Double.parseDouble(latitudeText);
        final double longitude = Double.parseDouble(longitudeText);
        final int maxResult;
        try {
            maxResult = Integer.parseInt(maxResultsText);
        } catch (NumberFormatException e) {
            printGeocoderLog(HiLog.INFO, "maxResults is illegal");
            return;
        }
        final String languageText = language.getText();
        final String countryText = country.getText();
        // Enter a proper region longitude and latitude. Otherwise, no geographic information is returned. If a
        // non-China region is used, transfer the longitude and latitude of the non-China region and ensure that
        // the longitude and latitude are correct.
        GetFromLocationRequest getFromLocationRequest = new GetFromLocationRequest(latitude, longitude, maxResult);
        Locale locale = new Locale(languageText, countryText);
        GeocoderProviderClient geocoderProviderClient = new GeocoderProviderClient(this, locale);
        geocoderProviderClient.getFromLocation(getFromLocationRequest)
                .addOnSuccessListener((OnSuccessListener<List<HWLocation>>)
                        hwLocationList -> {
                            printGeocoderResult(hwLocationList);
                        })
                .addOnFailureListener(e -> {
                    String result = "getFromLocation onFailure:" + e.getMessage();
                    printGeocoderLog(HiLog.INFO, result);
                });
    }

    private void printGeocoderLog(int level,String logInfo) {
        printLog(level, TAG, logInfo);
        printScreenLog(showLocation, logInfo);
    }

    private boolean isDouble(String input) {
        if (isInt(input)) {
            return true;
        }
        Matcher mer = Pattern.compile("^[+-]?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$").matcher(input);
        return mer.matches();
    }

    public static boolean isInt(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.matches();
    }

    private boolean checkLatValid(String latitude) {
        if (!isDouble(latitude)) {
            return false;
        }
        return true;
    }

    private boolean checkLogValid(String longitude) {
        if (!isDouble(longitude)) {
            return false;
        }
        return true;
    }

    private void printGeocoderResult(List<HWLocation> geocoderResult) {
        if (geocoderResult == null || geocoderResult.isEmpty()) {
            printLog(HiLog.INFO, TAG, "geocoderLocationList is null or geocoderLocationList.isEmpty");
        } else {
            printLog(HiLog.INFO, TAG, "geocoderLocationList.size = " + geocoderResult.size());
            for (HWLocation hwLocation : geocoderResult) {
                StringBuilder builder = new StringBuilder();
                builder.append("Address:{countryName=")
                        .append(hwLocation.getCountryName())
                        .append(",countryCode=")
                        .append(hwLocation.getCountryCode())
                        .append(",state=")
                        .append(hwLocation.getState())
                        .append(",city=")
                        .append(hwLocation.getCity())
                        .append(",county=")
                        .append(hwLocation.getCounty())
                        .append(",street=")
                        .append(hwLocation.getStreet())
                        .append(",featureName=")
                        .append(hwLocation.getFeatureName())
                        .append(",postalCode=")
                        .append(hwLocation.getPostalCode())
                        .append(",phone=")
                        .append(hwLocation.getPhone())
                        .append(",url=")
                        .append(hwLocation.getUrl())
                        .append(",extraInfo=")
                        .append(hwLocation.getExtraInfo().size())
                        .append("}" + System.lineSeparator());

                Map<String, Object> map = hwLocation.getExtraInfo();
                for (Map.Entry entry : map.entrySet()) {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    builder.append("Location:{key=" + key);
                    builder.append("Location:value = " + value + "}");
                }
                builder.append("Location:{latitude=")
                        .append(hwLocation.getLatitude())
                        .append(",longitude=")
                        .append(hwLocation.getLongitude())
                        .append("}" + System.lineSeparator());
                printGeocoderLog(HiLog.INFO, builder.toString());
            }
        }
    }

    private void switchMode() {
        if (reverseGeocoderLayout.getVisibility() == Component.VISIBLE) {
            reverseGeocoderLayout.setVisibility(Component.HIDE);
            geocoderLayout.setVisibility(Component.VISIBLE);
            showLocation.setText("");
        } else {
            geocoderLayout.setVisibility(Component.HIDE);
            reverseGeocoderLayout.setVisibility(Component.VISIBLE);
            showLocation.setText("");
        }
    }
}
