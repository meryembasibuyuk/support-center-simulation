package com.alarm;

public class ConsoleAlarmNotifier implements AlarmNotifier {
    @Override
    public void notify(Alarm alarm) {
        System.out.println("🚨 ALARM: " + alarm);
    }
}