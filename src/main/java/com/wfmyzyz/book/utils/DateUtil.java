package com.wfmyzyz.book.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 */
public class DateUtil {
    /**
     * yyyy-MM
     */
    public static final String MONTH = "yyyy-MM";
    /**
     * yyyy-MM-dd
     */
    public static final String DATE = "yyyy-MM-dd";
    /**
     * yyyyMMdd
     */
    public static final String DATE_SIMPLE = "yyyyMMdd";
    /**
     * MM-dd HH:mm
     */
    public static final String DATE_MM_DD = "MM-dd HH:mm";
    /**
     * MM年dd日 HH:mm
     */
    public static final String DATE_MM_DD_CN = "M月d日 HH:mm";

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 含有T的时间
     */
    public static final String BLOCK_CHAIN_TIMESTAMP = "yyyy-MM-ddTHH:mm:ss";
    /**
     *
     */
    public static final String FULL_DATETIME = "yyyyMMddHHmmssSSS";
    /**
     *
     */
    public static final String MSEC = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 无间隔日期格式 yyyyMMddHHmmss
     */
    public static final String NOSPLIT_MDHM_DATETIME = "yyyyMMddHHmmss";

    /**
     * 常规日期时间格式，24小时制yyyy年MM月dd日 HH时mm分
     */
    public static final String NORMAL_DATETIME_FORMAT_CN = "yyyy年MM月dd日 HH时mm分";

    public static final String NORMAL_DATETIME_FORMAT_SIMPLE_CN = "yyyy年MM月dd日 HH:mm";

    public static final String NORMAL_DATETIME_FORMAT_SECOND_CN = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * 常规日期时间格式，24小时制yyyy年MM月dd日
     */
    public static final String NORMAL_DATE_FORMAT_CN = "yyyy年MM月dd日";
    /**
     * 常规日期时间格式，24小时制yyyy年M月d日 不要0
     */
    public static final String NORMAL_DATE_FORMAT_CN_SIMPLE = "yyyy年M月d日";
    /**
     * 常规日期时间格式，24小时制MM月dd日 HH时mm分
     */
    public static final String NORMAL_MONTHDATETIME_FORMAT_CN = "MM月dd日 HH时mm分";
    /**
     * 常规日期时间格式，MM月dd日
     */
    public static final String NORMAL_MONTHDATE_FORMAT_CN = "MM月dd日";
    /**
     * 常规日期时间格式，M月d日
     */
    public static final String NORMAL_MONTHDATE_FORMAT_CN_SIMPLE = "M月d日";

    /**
     * 常规日期时间格式，24小时制yyyy-MM-dd HH:mm 没有秒
     */
    public static final String NORMAL_DATETIME_FORMAT_NO_SECOND = "yyyy-MM-dd HH:mm";
    /**
     * 常规日期时间格式，24小时制MM-dd HH:mm:ss 没有年
     */
    public static final String NORMAL_DATETIME_FORMAT_NO_YEAR = "MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH
     */
    public static final String NORMAL_DATETIME_FORMAT_HOUR = "yyyy-MM-dd HH";

    /**
     * 24小时制格式
     */
    public static final String TIME_FORMAT = "HH:mm";

    /**
     * 截取时分秒
     */
    public static final String TIME_MINUTE_SECOND_FORMAT = "HH:mm:ss";

    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> SDFMAP = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /** 锁对象 */
    private static final Object LOCKOBJ = new Object();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = SDFMAP.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (LOCKOBJ) {
                tl = SDFMAP.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    //System.out.println("put new sdf of pattern " + pattern + " to map");
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            //System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    SDFMAP.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    /**
     * @Title: formatNew
     * @Description: 返回格式化时间
     * @author: lilsh  lilsh@corp.21cn.com
     * @date: 2014-9-15 下午4:45:35
     */
    public static String format(Date date, String dateFormat) {
        return getDateFormat(dateFormat).format(date);
    }
    /**
     * @Description: 格式化当前时间
     */
    public static String formatNow(String dateFormat) {
        return getDateFormat(dateFormat).format(new Date());
    }

    /**
     * @Title: parse
     * @Description: 返回解析时间
     * @author: lilsh  lilsh@corp.21cn.com
     * @date: 2014-9-15 下午4:45:35
     */
    public static Date parse(String strDate, String dateFormat) throws ParseException {
        return getDateFormat(dateFormat).parse(strDate);
    }

    public static Date getTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int getYear(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.YEAR);
    }

    // 返回的月数是 自然月-1 也就是说返回的月是从 0--11
    public static int getMonth(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.MONTH);
    }

    public static int getDate(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMin(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.MINUTE);
    }

    public static Date addDate(Date d, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    /**
     * 获取date下一天0点的时间
     * @param date
     * @return
     */
    public static Date addDateDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return  cal.getTime();
    }

    /**
     * 获取date下一天0点的时间
     * @param date
     * @return
     */
    public static Date addDateDay(String date, int day) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtil.parse(date, DateUtil.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return  cal.getTime();
    }


    // 计算两个日期之间的天数
    public static int getDays(Date date1, Date date2) {
        int days = 0;
        days = (int) (Math.abs((date2.getTime() - date1.getTime())) / (24 * 60 * 60 * 1000));
        return days;
    }

    public static int getSecond(Date date1, Date date2) {
        int second = 0;
        second = (int) (Math.abs((date1.getTime() - date2.getTime()) / 1000));
        return second;
    }

    public static int getMinute(Date date1, Date date2) {
        int minute = 0;
        minute = (int) (Math.abs((date1.getTime() - date2.getTime())
                / (60 * 1000)));
        return minute;
    }

    /**
     * 功能描述：获取前day天的日期
     *
     * @param day
     * @return
     * @version 1.0.0
     * @since 1.0.0 create on: 2012-8-15
     */
    public static Date getDayBefore(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
        return calendar.getTime();
    }

    /**
     * 功能描述：获取昨天的当前
     *
     * @return
     * @version 1.0.0
     * @since 1.0.0 create on: 2012-8-15
     */
    public static Date getYesterday() {
        return getDayBefore(1);
    }

//    /**
//     * 日期相减
//     *
//     * @param date
//     * 日期
//     * @param date1
//     * 日期
//     * @return 返回相减后的日期
//     */
//    public static int diffDate(Date date, Date date1) {
//        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
//    }

    /**
     * 计算两个日期相差多少天  d2 - d1
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //不同一年
        {
            int timeDistance = 0;
            if (year1 > year2) {
                for (int i = year2; i < year1; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                    {
                        timeDistance += 366;
                    } else    //不是闰年
                    {
                        timeDistance += 365;
                    }
                }
            } else {
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                    {
                        timeDistance -= 366;
                    } else    //不是闰年
                    {
                        timeDistance -= 365;
                    }
                }
            }
            return day2 - day1 - timeDistance;
        } else    //同年
        {
            return day2 - day1;
        }
    }

    /**
     * 返回毫秒
     *
     * @param date
     * 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    public static long getMillis(String time) {
        try {
            Date date = DateUtil.parse(time, DateUtil.DATETIME);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取今天某个时分秒的时间戳
     * @param hhmmss "08:30:00"
     * @return
     */
    public static long getTodayHMSMillis(String hhmmss) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);

        String[] times = hhmmss.split(":");
        int hour = CommonUtils.stringToNum(times[0]);
        int min = CommonUtils.stringToNum(times[1]);
        c.set(year, month, date, hour, min);

        return c.getTimeInMillis();
    }

    /**
     * 获取相隔n天的某个时分秒的时间戳,
     * @param hhmmss
     * @param n
     * @return
     */
    public static long getHMSMillis(String hhmmss, int n) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        String[] times = hhmmss.split(":");
        int hour = CommonUtils.stringToNum(times[0]);
        int min = CommonUtils.stringToNum(times[1]);
        c.set(year, month, date, hour, min, 0);
        c.add(Calendar.DAY_OF_YEAR, n);
        return c.getTimeInMillis();
    }

    /**
     * 获取今天剩余的秒数
     * @return
     */
    public static int getTodayLeftMiao(){
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    }

    /**
     * 判断是否同一天
     *
     * @param date1
     * @param date2
     * @return isSameYear && isSameMonth && isSameDate
     */
    public static boolean isSameDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 判断是否在活动时间内
     * @param nowDate
     * @return
     */
    public static boolean isActivityTime(Date nowDate, Date startDate, Date endDate) {
        if(!DateUtil.isSameDate(startDate, nowDate) && !DateUtil.isSameDate(endDate, nowDate)) {
            long today = nowDate.getTime();
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();
            if(today < startTime  //活动开始之前
                    || today > endTime) //活动已经结束
                return false;
        }
        //在活动开始当天、结束当天
        return true;
    }

    /**
     * 判断当前时间是否在区间内（24小时限制）
     * @return
     */
    public static boolean isCurrentInTimeInterval(String startTime, String endTime) {
        int h1, h2, m1, m2;
        String[] timeArr1, timeArr2;

        timeArr1 = startTime.split(":");
        timeArr2 = endTime.split(":");
        h1 = CommonUtils.stringToNum(timeArr1[0]);
        m1 = CommonUtils.stringToNum(timeArr1[1]);
        h2 = CommonUtils.stringToNum(timeArr2[0]);
        m2 = CommonUtils.stringToNum(timeArr2[1]);

        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        int hour = cld.get(Calendar.HOUR_OF_DAY);
        int minutes = cld.get(Calendar.MINUTE);
        if(hour == h1 && hour == h2 && h1 == h2) { //小时内
            if(minutes < m1 || minutes > m2) {
                return false;
            }
        } else {
            if(hour == h1 && minutes < m1) {
                return false;
            }

            if(hour == h2 && minutes > m2) {
                return false;
            }

            //hour
            if(hour < h1 || hour > h2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据在时间范围内，根据时间间隔获取时间集合
     * @param startTime
     * @param closeTime
     * @param kaipanTime
     * @return
     */
    public static Set<String> getTimeIntervalSet(String startTime, String closeTime, String kaipanTime) {
        int h1, h2, m1, m2;
        String[] timeArr1, timeArr2;

        int timeInterval = Integer.parseInt(kaipanTime);

        timeArr1 = startTime.split(":");
        timeArr2 = closeTime.split(":");
        h1 = CommonUtils.stringToNum(timeArr1[0]);
        m1 = CommonUtils.stringToNum(timeArr1[1]);
        h2 = CommonUtils.stringToNum(timeArr2[0]);
        m2 = CommonUtils.stringToNum(timeArr2[1]);

        Calendar cal1 = Calendar.getInstance(); // 开始时间
        cal1.setTime(new Date());
        cal1.set(Calendar.HOUR_OF_DAY, h1);
        cal1.set(Calendar.MINUTE, m1);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance(); //结束时间
        cal2.setTime(new Date());
        cal2.set(Calendar.HOUR_OF_DAY, h2);
        cal2.set(Calendar.MINUTE, m2);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        Set<String> timeSet = new HashSet<>();

        while (cal1.getTimeInMillis() <= cal2.getTimeInMillis()) { //直到大于结束时间，循环结束
            String format = DateUtil.format(cal1.getTime(), DateUtil.TIME_FORMAT);
            timeSet.add(format);

            cal1.add(Calendar.SECOND, timeInterval);
        }

        return timeSet;
    }

    /**
     * 根据在时间范围内，根据时间间隔获取时间集合
     * @param startTime
     * @param closeTime
     * @param kaipanTime
     * @return
     */
    public static Set<String> getSecondIntervalSet(String startTime, String closeTime, String kaipanTime) {
        int h1, h2, m1, m2;
        String[] timeArr1, timeArr2;

        int timeInterval = Integer.parseInt(kaipanTime);

        timeArr1 = startTime.split(":");
        timeArr2 = closeTime.split(":");
        h1 = CommonUtils.stringToNum(timeArr1[0]);
        m1 = CommonUtils.stringToNum(timeArr1[1]);
        h2 = CommonUtils.stringToNum(timeArr2[0]);
        m2 = CommonUtils.stringToNum(timeArr2[1]);

        int s1 = 0, s2 = 0;
        if (null != timeArr1[2]) {
            s1 = CommonUtils.stringToNum(timeArr1[2]);
        }

        if (null != timeArr2[2]) {
            s2 = CommonUtils.stringToNum(timeArr2[2]);
        }

        Calendar cal1 = Calendar.getInstance(); // 开始时间
        cal1.setTime(new Date());
        cal1.set(Calendar.HOUR_OF_DAY, h1);
        cal1.set(Calendar.MINUTE, m1);
        cal1.set(Calendar.SECOND, s1);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance(); //结束时间
        cal2.setTime(new Date());
        cal2.set(Calendar.HOUR_OF_DAY, h2);
        cal2.set(Calendar.MINUTE, m2);
        cal2.set(Calendar.SECOND, s2);
        cal2.set(Calendar.MILLISECOND, 0);

        Set<String> timeSet = new HashSet<>();

        while (cal1.getTimeInMillis() <= cal2.getTimeInMillis()) { //直到大于结束时间，循环结束
            String format = DateUtil.format(cal1.getTime(), DateUtil.TIME_MINUTE_SECOND_FORMAT);
            timeSet.add(format);

            cal1.add(Calendar.SECOND, timeInterval);
        }

        return timeSet;
    }

    /**
     * HH:mm 增加x秒,x要以60为一个单位
     * @param startTime
     * @param second
     * @return
     */
    public static String addTime(String startTime, String second) {

        int h1, m1;
        String[] timeArr1 = startTime.split(":");
        h1 = CommonUtils.stringToNum(timeArr1[0]);
        m1 = CommonUtils.stringToNum(timeArr1[1]);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, h1);
        cal.set(Calendar.MINUTE, m1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.SECOND, Integer.parseInt(second));

        return DateUtil.format(cal.getTime(), DateUtil.TIME_FORMAT);
    }

    /**
     * HH:mm:ss 增加x秒
     * @param startTime
     * @param second
     * @return
     */
    public static String addSecond(String startTime, String second) {

        int h1, m1;
        String[] timeArr1 = startTime.split(":");
        h1 = CommonUtils.stringToNum(timeArr1[0]);
        m1 = CommonUtils.stringToNum(timeArr1[1]);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, h1);
        cal.set(Calendar.MINUTE, m1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.SECOND, Integer.parseInt(second));

        return DateUtil.format(cal.getTime(), DateUtil.TIME_MINUTE_SECOND_FORMAT);
    }

    /**
     * 获取当天开始时间、结束时间
     *
     * @param date
     * @return
     */
    public static Map<String, String> getTodayStartTimeAndEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime = DateUtil.addDateDay(date, 0);

        Map<String, String> map = new HashMap<>();
        map.put("startTime", DateUtil.format(calendar.getTime(), DateUtil.DATETIME));

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.SECOND, -1);
        map.put("endTime", DateUtil.format(calendar.getTime(), DateUtil.DATETIME));
        return map;
    }

    public static Map<String, String> getTodayStartTimeAndEndTime(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtil.parse(date, DateUtil.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime = DateUtil.addDateDay(date, 0);

        Map<String, String> map = new HashMap<>();
        map.put("startTime", DateUtil.format(calendar.getTime(), DateUtil.DATETIME));

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.SECOND, -1);
        map.put("endTime", DateUtil.format(calendar.getTime(), DateUtil.DATETIME));
        return map;
    }


    /**
     * 时间戳类型转换成字符串
     * @param time
     * @param format
     * @return
     */
    public static String formatMillis(long time, String format) {
        return DateUtil.format(new Date(time), format);
    }


    /**
     * 在活动时间内，根据周期duringDay获取当前周期的开始时间和结束时间
     * @return
     */
    public static Map<String, Object> getStartTimeAndEndTimeWithDuringDay(Date date, String activityStartTime, String activityEndTime, int duringDay) throws ParseException {
        Map<String, Object> timeMap = new HashMap<>();
        Date activityStartTimeDate = null;
        activityStartTimeDate = DateUtil.parse(activityStartTime, DateUtil.DATE);
        // Date activityEndTimeDate = DateUtil.parse(activityEndTime, DateUtil.DATE);
        //活动过了n天
        int n = DateUtil.differentDays(activityStartTimeDate, date);
        //n/duringDay=第几期
        Date startTimeDate = DateUtil.addDateDay(activityStartTimeDate, (n / duringDay) * duringDay);
        Date endTimeDate = DateUtil.addDateDay(startTimeDate, duringDay);
        String startTime = DateUtil.format(startTimeDate, DateUtil.DATE);
        String endTime = DateUtil.format(endTimeDate, DateUtil.DATE);
        timeMap.put("startTime", startTime);
        timeMap.put("endTime", endTime);
        return timeMap;
    }

    public static void main(String[] args) throws ParseException {
//        DateUtil.getTimeIntervalSet(DateUtil.addTime("07:30", "1200"), DateUtil.addTime("20:10","1200"),"1200");
//        System.out.println(DateUtil.getTodayStartTimeAndEndTime(new Date()));
//        System.out.println(System.currentTimeMillis() + 1200 * 1000);


//        Date d1 = DateUtil.parse("2019-07-11", DateUtil.DATE);
//        Date d2 = DateUtil.parse("2019-07-12", DateUtil.DATE);
//        System.out.println(DateUtil.differentDays(d1, d2));
        System.out.println(DateUtil.checkDate("2019-09-17 00:00:00","yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 格式化当前时间
     */
    public static String formatNow() {
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return formatter.format(localDateTime);
    }

    /**
     * 获取过去一个月时的时间
     */
    public static String getMonthAgoDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return format.format(m);
    }

    /**
     * 验证字符串是否是时间格式
     */
    public static boolean checkDate(String dateStr,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            format.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return dateStr.length()==pattern.length();
    }
    /**
     * 转换时间格式
     * @param time
     * @param sourceFormat
     * @param targetFormat
     * @return
     */
    public static String transferFormat(String time, String sourceFormat, String targetFormat) {
        try {
            Date parse = DateUtil.parse(time, sourceFormat);
            String format = DateUtil.format(parse, targetFormat);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
