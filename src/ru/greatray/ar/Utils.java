package ru.greatray.ar;

import java.util.Calendar;

class Utils {

    static String getRemindMinutesMessageFormatted(long millis, String msg) {
        return msg.replaceAll("%m", String.valueOf(Math.round(millis / 60000.0D)));
    }

    static String getRemindSecondsMessageFormatted(long millis, String msg) {
        return msg.replaceAll("%s", String.valueOf(Math.round(millis / 1000.0D)));
    }

    static String getRestartTimeMessageFormatted(long time, String msg) {
        long restart = (time - Calendar.getInstance().getTimeInMillis()) / 1000;
        long hours = restart / 3600;
        restart = restart % 3600;
        long minutes = restart / 60;
        restart = restart % 60;
        long seconds = restart;
        return msg.replaceAll("%h", String.valueOf(hours)).replaceAll("%m", String.valueOf(minutes)).replaceAll("%s", String.valueOf(seconds));
    }

    static long parseTimeStamp(String[] args) {
        long result = 0;
        int hour = 0;
        int minutes = 0;
        int seconds = 0;
        int param = 0;
        for (int i = 1; i < args.length; i++) {
            if (i % 2 == 1) {
                param = Integer.parseInt(args[i]);
            } else {
                if (args[i].equalsIgnoreCase("H")) {
                    if (hour > 0 || param <= 0)
                        return result;
                    hour += param;
                }
                if (args[i].equalsIgnoreCase("M")) {
                    if (minutes > 0 || param <= 0)
                        return result;
                    minutes += param;
                }
                if (args[i].equalsIgnoreCase("S")) {
                    if (seconds > 0 || param <= 0)
                        return result;
                    seconds += param;
                }
            }
        }
        result = (hour * 3600 + minutes * 60 + seconds) * 1000;
        if (result > 0)
            return result + Calendar.getInstance().getTimeInMillis();
        return 0;
    }


    static long parseTimeStamp(String timestamp) {
        String[] stringVars = timestamp.split(":");
        int hour;
        int minute;

        if (stringVars.length != 2) {
            return 0;
        }

        try {
            hour = Integer.parseInt(stringVars[0]);
            minute = Integer.parseInt(stringVars[1]);
        } catch (Exception e) {
            return 0;
        }

        if (hour < 0 || hour > 23) {
            return 0;
        }
        if (minute < 0 || minute > 59) {
            return 0;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        if (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < 0) {
            cal.setTimeInMillis(cal.getTimeInMillis() + 86400000);
        }

        return cal.getTimeInMillis();
    }
}
