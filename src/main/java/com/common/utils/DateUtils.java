package com.common.utils;

import com.common.enums.DateTypeEnum;
import com.common.exception.InvalidDateTypeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Pattern;

public class DateUtils {

    private static LocalDate parseLocalDate(String date, String dateType) {
        try {
            String dateFormat = DateTypeEnum.getFormat(dateType);
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException e) {
            throw new InvalidDateTypeException("잘못된 날짜 형식입니다.");
        }
    }

    private static LocalDateTime parseLocalDateTime(String date, String dateType) {
        try {
            String dateFormat = DateTypeEnum.getFormat(dateType);
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException e) {
            throw new InvalidDateTypeException("잘못된 날짜 형식입니다.");
        }
    }

    public static LocalDate toLocalDate(String dateStr) {
        String dateType = DateTypeEnum.DAY.getType();
        Pattern pattern = DateTypeEnum.getPatternByType(dateType);

        return Optional.ofNullable(dateStr)
                .filter(date -> pattern.matcher(date).matches())
                .map(date -> parseLocalDate(date, dateType))
                .orElseThrow(() -> new InvalidDateTypeException("잘못된 날짜 형식입니다."));
    }

    public static LocalDateTime toLocalDateTime(String dateTimeStr) {
        String dateType = DateTypeEnum.DAY_WITH_TIME.getType();
        Pattern pattern = DateTypeEnum.getPatternByType(dateType);

        return Optional.ofNullable(dateTimeStr)
                .filter(date -> pattern.matcher(date).matches())
                .map(date -> parseLocalDateTime(date, dateType))
                .orElseThrow(() -> new InvalidDateTypeException("잘못된 날짜 형식입니다."));
    }
}
