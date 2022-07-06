package com.rainman.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * 时间工具类
 *
 * @author yao hai tao
 */
@lombok.extern.log4j.Log4j2
public class DateUtils {
    /**
     * 常用时间格式化字符串
     */
    @lombok.Setter
    private static String[] parsePatterns = {
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss.SSSSSS",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH",
            "yyyy-MM-dd",
            "yyyy-MM",
            "yyyyMMdd HH:mm:ss.SSS",
            "yyyyMMdd HH:mm:ss",
            "yyyyMMdd HH:mm",
            "yyyyMMdd HH",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmm",
            "yyyyMMddHH",
            "yyyyMMdd",
            "yyyyMM",
            "yyyy"
    };

    private DateUtils() {
    }

    /**
     * 新时间
     *
     * @return 新时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 解析
     *
     * @param str           时间字符串
     * @param parsePatterns 时间格式化字符串
     * @return 時間
     * @throws ParseException
     */
    public static Date parseDate(final String str, final String... parsePatterns) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        String[] newParsePatterns = ArrayUtils.isNotEmpty(parsePatterns) ? parsePatterns : DateUtils.parsePatterns;

        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str, newParsePatterns);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static Date parseDate(final Object obj, final String... parsePatterns) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof Long) {
            return new Date((Long) obj);
        } else if (obj instanceof Double) {
            return new Date(((Double) obj).longValue());
        }

        return parseDate(Objects.toString(obj), parsePatterns);
    }

    public static Date defaultIfBlankParseDate(final String str, String defaultStr, final String... parsePatterns) {
        return parseDate(StringUtils.defaultIfBlank(str, defaultStr), parsePatterns);
    }

    public static Date defaultIfNullParseDate(final String str, Date defaultDate, final String... parsePatterns) {
        return ObjectUtils.defaultIfNull(parseDate(str, parsePatterns), defaultDate);
    }
}
