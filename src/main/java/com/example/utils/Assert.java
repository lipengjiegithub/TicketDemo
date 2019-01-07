package com.example.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Assert {

    private static Log log = LogFactory.getLog(Assert.class);

    public static boolean flag(boolean flag, String success, String fail) {
        if(flag == true) {
            log.debug(success);
        }else {
            log.error(fail);
        }
        return flag;
    }
}
