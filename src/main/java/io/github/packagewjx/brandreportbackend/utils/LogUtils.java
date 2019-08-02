package io.github.packagewjx.brandreportbackend.utils;

import io.github.packagewjx.brandreportbackend.domain.Constants;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public class LogUtils {
    public static String getLogTime(int year, String period, Integer periodTime) {
        switch (period) {
            case Constants.PERIOD_ANNUAL:
                return year + "年";
            case Constants.PERIOD_MONTHLY:
                return year + "年" + periodTime + "月";
            case Constants.PERIOD_QUARTERLY:
                return year + "年第" + periodTime + "季度";
            default:
                throw new IllegalArgumentException(period + "值不合法");
        }
    }
}
