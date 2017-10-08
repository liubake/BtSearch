package com.erola.btsearch.util.log4j;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * 自定义 DailyRollingFileAppender，限定日志严格按照配置中定义类别进行记录
 * Created by Erola on 2017/10/3.
 */
public class StrictDailyRollingFileAppender extends DailyRollingFileAppender {
    /**
     * 重写 isAsSevereAsThreshold 方法
     * @param priority
     * @return
     */
    @Override
    public boolean isAsSevereAsThreshold(Priority priority)  {
        return threshold == null || priority.equals(threshold);
    }
}