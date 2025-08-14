//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.date;

import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String[] DATE_FORMAT = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmmssSSS", "HH:mm", "HH:mm:ss"};
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DateUtils() {
    }

    public static String formatTime(Date date) {
        return (new SimpleDateFormat(DEFAULT_TIME_FORMAT)).format(date);
    }

    public static String formatDate(Date date) {
        return (new SimpleDateFormat(DEFAULT_DATE_FORMAT)).format(date);
    }

    public static String formatDatetime(Date date) {
        return (new SimpleDateFormat(DEFAULT_DATETIME_FORMAT)).format(date);
    }

    public static Date getDayBegin(Date date) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);
        return c.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 23, 59, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date getNextDayBegin(Date date) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 24, 0, 0);
        return c.getTime();
    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar c = get(new Date());
        c.set(year, month, dayOfMonth, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getDate(Date date, int hour, int minute, int seconds) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), hour, minute, seconds);
        return c.getTime();
    }

    public static Calendar get(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null!");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.MILLISECOND, 0);
            return c;
        }
    }

    public static long duration(Date start, Date end) {
        if (start != null && end != null) {
            return Math.abs(start.getTime() - end.getTime());
        } else {
            throw new IllegalArgumentException("开始时间和结束时间不能为空!");
        }
    }

    public static Date parse(String str) {
        return parse(str, DATE_FORMAT);
    }

    public static Date parse(String str, String... patterns) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT, Locale.US);
        if (str.matches("\\d+") && str.length() == 13) {
            return new Date(Long.parseLong(str));
        } else {
            if (patterns == null || patterns.length == 0) {
                patterns = DATE_FORMAT;
            }

            String[] var3 = patterns;
            int var4 = patterns.length;
            int var5 = 0;

            while (var5 < var4) {
                String pattern = var3[var5];
                sdf.applyPattern(pattern);

                try {
                    Date d = sdf.parse(str);
                    return d;
                } catch (ParseException var8) {
                    ++var5;
                }
            }

            return null;
        }
    }

    public static Date add(Date date, int count, String type) {
        Assert.hasText(type, "时间类型不能为空！");
        Assert.isTrue("yMdHmsS".contains(type), "时间类型错误！");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        byte var5 = -1;
        switch (type.hashCode()) {
            case 72:
                if (type.equals("H")) {
                    var5 = 3;
                }
                break;
            case 77:
                if (type.equals("M")) {
                    var5 = 1;
                }
                break;
            case 83:
                if (type.equals("S")) {
                    var5 = 6;
                }
                break;
            case 100:
                if (type.equals("d")) {
                    var5 = 2;
                }
                break;
            case 109:
                if (type.equals("m")) {
                    var5 = 4;
                }
                break;
            case 115:
                if (type.equals("s")) {
                    var5 = 5;
                }
                break;
            case 119:
                if (type.equals("w")) {
                    var5 = 7;
                }
                break;
            case 121:
                if (type.equals("y")) {
                    var5 = 0;
                }
        }

        switch (var5) {
            case 0:
                calendar.add(Calendar.YEAR, count);
                break;
            case 1:
                calendar.add(Calendar.MONTH, count);
                break;
            case 2:
                calendar.add(Calendar.DATE, count);
                break;
            case 3:
                calendar.add(Calendar.HOUR_OF_DAY, count);
                break;
            case 4:
                calendar.add(Calendar.MINUTE, count);
                break;
            case 5:
                calendar.add(Calendar.SECOND, count);
                break;
            case 6:
                calendar.add(Calendar.MILLISECOND, count);
                break;
            case 7:
                calendar.add(Calendar.WEEK_OF_MONTH, count);
                break;
            default:
                Assert.isTrue(false, "错误的时间类型：" + type);
        }

        return calendar.getTime();
    }

    public static Date addHour(Date date, int hours) {
        return add(date, hours, "H");
    }

    public static Date addMinute(Date date, int minutes) {
        return add(date, minutes, "m");
    }

    public static Date addDay(Date date, int days) {
        return add(date, days, "d");
    }

    public static Date addMonth(Date date, int month) {
        return add(date, month, "M");
    }

    public static Date addWeek(Date date, int weeks) {
        return add(date, weeks, "w");
    }

    public static Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int compare(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.compareTo(date2);
        } else {
            throw new IllegalArgumentException("两者比较时间不能为空!");
        }
    }

}
