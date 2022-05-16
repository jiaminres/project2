package com.jiamin;


import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import sun.security.util.AuthResources_it;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    //    5
//            0 1 1 3 4
//            1 0 1 3 1
    public static int res = 0;

    public static void main(String[] args) {
        String format1 = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
        System.out.println(format1);
        TemporalAccessor t = DateTimeFormatter.RFC_1123_DATE_TIME.parse("Sat, 7 May 2022 09:37:51 gmt");
        String format = DateTimeFormatter.RFC_1123_DATE_TIME.format(t);
        System.out.println(format);

        ZonedDateTime gmt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("GMT"));
        format = DateTimeFormatter.RFC_1123_DATE_TIME.format(gmt);
        System.out.println(format);

//        new Date

    }
}
