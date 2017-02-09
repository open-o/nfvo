#!/bin/bash
ps auxww | grep 'manage.py runserver 127.0.0.1:8484' | awk '{print $2}' | xargs kill -9
