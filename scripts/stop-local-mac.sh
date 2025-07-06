#!/usr/bin/env bash

ps aux | grep '[j]ava.*bootRun' | awk '{print $2}' | xargs -r kill -9