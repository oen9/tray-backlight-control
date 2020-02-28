# tray-backlight-control

Tray backlight control for linux

[![Build Status](https://travis-ci.org/oen9/tray-backlight-control.svg?branch=master)](https://travis-ci.org/oen9/tray-backlight-control)
[![CircleCI](https://circleci.com/gh/oen9/tray-backlight-control.svg?style=svg)](https://circleci.com/gh/oen9/tray-backlight-control)

## how to use

Add `tray-backlight-control` to autostart and just use it.\
tray icon: ![alt text](https://raw.githubusercontent.com/oen9/tray-backlight-control/master/src/main/resources/bulb14.png "tray icon")\
![alt text](https://raw.githubusercontent.com/oen9/tray-backlight-control/master/img/popup.png "popup")

## system dependencies

- `xbacklight` (or replacement like [acpilight](https://gitlab.com/wavexx/acpilight/ "acpilight"))
- `java` (tested with `java version "11.0.2"`)

## sources

### howto compile

1. compile: `sbt stage`
1. run: `./target/universal/stage/bin/tray-backlight-control`

### libraries

1. ZIO
