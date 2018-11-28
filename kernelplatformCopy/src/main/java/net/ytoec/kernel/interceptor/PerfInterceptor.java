package net.ytoec.kernel.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import net.ytoec.kernel.service.impl.OrderServiceImpl;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerfInterceptor implements MethodInterceptor {

    //Logger                                                logger                 = Logger.getLogger(PerfInterceptor.class);
    private static Logger logger = LoggerFactory.getLogger(PerfInterceptor.class);
    private static ConcurrentHashMap<String, MethodStats> methodStats            = new ConcurrentHashMap<String, MethodStats>();

    private static long                                   statLogFrequency       = 50000;

    private static long                                   methodWarningThreshold = 350;

    public Object invoke(MethodInvocation method) throws Throwable {
        methodWarningThreshold=Long.valueOf(ConfigUtilSingle.getInstance().getPerformanceLimt());
        long start = System.currentTimeMillis();

        try {
            return method.proceed();

        }

        finally {

            updateStats(method.getMethod().getDeclaringClass().getCanonicalName(),method.getMethod().getName(), (System.currentTimeMillis() - start));

        }

    }

    private void updateStats(String calssName,String methodName, long elapsedTime) {

        MethodStats stats = methodStats.get(calssName+","+methodName);

        if (stats == null) {

            stats = new MethodStats(calssName,methodName);

            methodStats.put(calssName+","+methodName, stats);

        }

        stats.count++;

        stats.totalTime += elapsedTime;

        if (elapsedTime > stats.maxTime) {

            stats.maxTime = elapsedTime;

        }

        if (elapsedTime > methodWarningThreshold) {

            logger.error("method warning: " +calssName+","+ methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime
 + ", maxTime = " + stats.maxTime
					+ ",avgTime= " + stats.totalTime / stats.count);

        }


        if (stats.count % statLogFrequency == 0) {

            long avgTime = stats.totalTime / stats.count;

            long runningAvg = (stats.totalTime - stats.lastTotalTime) / statLogFrequency;

            logger.error("class:"+calssName+" method: " + methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime
                         + ", avgTime = " + avgTime + ", runningAvg = " + runningAvg + ", maxTime = " + stats.maxTime);

            // reset the last total time

            stats.lastTotalTime = stats.totalTime;

        }

    }

    class MethodStats {

        public String className;
        public String methodName;

        public long   count;

        public long   totalTime;

        public long   lastTotalTime;

        public long   maxTime;

        public MethodStats(String methodName,String calssName) {

            this.className=calssName;
            this.methodName = methodName;

        }

    }

}
