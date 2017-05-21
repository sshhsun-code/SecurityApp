package com.example.sunqi.securityking.global;

/**
 * Created by sshunsun on 2017/4/23.
 */
public class Constant {

    public static class Action {
        public static final String ACTION_NOR_FUN = "security.sunqi.normalfunction";
    }

    public static class State{
        public static final int NOTIFY_MANAGE = 0; //收起通知
        public static final int NOTIFY_SHOW = 2;   //显示通知
    }

    public static class AppSP{
        public static final String GLOBAL_CONFIG = "global_config";
        public static final String NOTIFY_READ_CONFIG = "notify_read_config";
    }

    public static class URI{
        public static final String NOTIFY_APP_URI = "content://app.security.sunqi/app_notify";
        public static final String NOTIFY_DATA_URI = "content://notify.security.sunqi/data_notify";
    }

    public static class Task{
        public static final int TASK_TO_GUIDE_NOTIFICATION_READ = 10;
        public static final int TASK_TO_GUIDE_APP_USAGE = 11;
        public static final int TASK_TO_GUIDE_AUTO_STRAT = 12;
    }

    public static class PATH{
        public static final String APKURL = "\thttp://fir.im/fn5j";
    }

    // 安全等级：低、中、高
    public enum Level{LOW, MID, HIGH}

    // 权限
    public enum Permission {
        AUTO_START, // 自启动
        NOTIFICATION_READ, // 通知读取
        USAGE_STATS, // 查看进程信息
    }
}
