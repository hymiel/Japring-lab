package com.common.enums;

import com.common.exception.InvalidDateTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum DateTypeEnum {

    DAY("day", "yyyy-MM-dd", Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$")),
    MONTH("month", "yyyy-MM", Pattern.compile("^\\d{4}-\\d{2}$")),
    YEAR("year", "yyyy", Pattern.compile("^\\d{4}$")),
    DAY_WITH_TIME("dayWithTime", "yyyy-MM-dd HH:mm:ss", Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")),
    LOCAL_DATETIME_MILLIS("localDateTimeMillis", "yyyy-MM-dd HH:mm:ss.SSS", Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?$"));

    private final String type;
    private final String format;
    private final Pattern pattern;


    public static String getFormat(String type) {
        return Arrays.stream(DateTypeEnum.values())
                .filter(dateTypeEnum -> dateTypeEnum.getType().equals(type))
                .map(DateTypeEnum::getFormat)
                .findFirst()
                .orElseThrow(() -> new InvalidDateTypeException("올바른 데이트 타입형식이 아닙니다."));
    }

    public static Pattern getPatternByType(String type) {
        return Arrays.stream(DateTypeEnum.values())
                .filter(dateTypeEnum -> dateTypeEnum.getType().equalsIgnoreCase(type))
                .map(DateTypeEnum::getPattern)
                .findFirst()
                .orElseThrow(() -> new InvalidDateTypeException("패턴이 등록되지 않은 데이트 타입입니다."));
    }
}
