package com.lhf.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author:95780
 * @Date: 10:03 2019/9/19
 * @Description:jdk1.8日期处理
 */
public class DateUtils {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DEFAULT_DATA = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String PATTERN_NO_SPLIT_CHAR = "yyyyMMddHHmmss";
    public static final String PATTERN_NO_SECOND = "yyyyMMddHHmm";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_NO_SPLIT_CHAR = "yyyyMMdd";

    /**
     * 转换日期类为字符串
     *
     * @param localDate
     * @param pattern
     * @return
     */
    public static String parseDate(LocalDate localDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(dateTimeFormatter);
    }

    /**
     * 转换日期时间为字符串
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String parseDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * Date转换为格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String parseDateformat(Date date, String pattern) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return parseDateTime(localDateTime, pattern);
    }


}
