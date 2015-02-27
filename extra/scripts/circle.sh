#!/bin/bash
#
# this file is used by circleci.com to setup the CI env
#


# Fix the CircleCI path
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
#  sudo apt-get update > /dev/null 2> /dev/null 
#  echo y | sudo apt-get upgrade > /dev/null 2> /dev/null 
#  echo y | sudo apt-get install --no-install-recommends  inkscape sox &&
#  echo y | sudo apt-get install --no-install-recommends  imagemagick optipng &&
  cp -r /usr/local/android-sdk-linux $ANDROID_HOME &&
  echo y | android update sdk -u -a -t android-19 > /dev/null &&
  echo y | android update sdk -u -a -t platform-tools > /dev/null &&
  echo y | android update sdk -u -a -t build-tools-21.0.0 > /dev/null &&
  touch $DEPS
fi
