##  harmony-location-demo


## 目录

 * [介绍](#介绍)
 * [开始](#开始)
 * [安装](#安装)
 * [支持环境](#支持环境)
 * [样例代码](#样例代码)
 * [结果](#结果)
 * [证书](#证书)


## 介绍
华为定位示例代码封装了华为定位的API。它提供了许多示例程序供您参考或使用。

## 开始
我们还提供了一个示例来演示用于HarmonyOS的LocationKit SDK的使用。
此示例使用Gradle构建系统。
首先，通过克隆此代码仓库或下载压缩包来演示。
在DevEco Studio中，使用“打开现有的DevEco Studio项目”，选择“harmony-location-demo”的目录。
您可以使用“gradlew signReleaseHap”命令直接构建项目。

## 安装

1.克隆或下载此项目，并在DevEco Studio中打开下载的文件夹。  
2.使用DevEco Studio的功能将配置的项目安装到设备上。

## 支持环境
华为智能手表已安装HMS Core (APK) 6.0.0及以上版本。

## 样例代码
1. 调用FusedLocationProviderClient的requestLocationUpdates添加定位请求。代码位置在src/main/java/com/huawei/sample/harmony/location/slice/RequestLocationCallbackAbilitySlice.java中

## 结果
<img src="images/home.png" width = 20% height = 20%>
<img src="images/get_last_location.png" width = 20% height = 20%>
<img src="images/request_location.png" width = 20% height = 20%>


## 证书
harmony-location-demo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

