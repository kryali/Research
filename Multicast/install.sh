#!/bin/bash
ACTIVITY_PATH=com.kryali.research/com.kryali.research.MainMenuActivity
BIN_PATH=bin/Multicast-debug.apk

function install_and_run {
  ID=$1
  adb -s $ID -d install -r $BIN_PATH
  adb -s $ID shell am start -n $ACTIVITY_PATH
}

ant debug
for device in $( adb devices | grep -w device | sed 's/\s*device//' );
  do install_and_run $device;
done;
