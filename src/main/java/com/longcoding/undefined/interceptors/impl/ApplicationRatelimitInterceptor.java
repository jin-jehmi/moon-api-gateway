package com.longcoding.undefined.interceptors.impl;

import com.google.common.collect.Maps;
import com.longcoding.undefined.helpers.Const;
import com.longcoding.undefined.interceptors.RedisBaseValidationInterceptor;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.Map;

/**
 * Created by longcoding on 16. 4. 11..
 */
public class ApplicationRatelimitInterceptor extends RedisBaseValidationInterceptor<Map<String, Response<Long>>> {

    @Override
    public boolean setCondition(Map<String, Response<Long>> storedValue) {
        logger.info(storedValue.get(Const.REDIS_APP_RATELIMIT_MINUTELY).get() + "   "
        + storedValue.get(Const.REDIS_APP_RATELIMIT_DAILY).get());
        if ( storedValue.get(Const.REDIS_APP_RATELIMIT_MINUTELY).get() < 0 ) return false;
        if ( storedValue.get(Const.REDIS_APP_RATELIMIT_DAILY).get() < 0 ) return false;

        return true;
    }

    @Override
    public Map<String, Response<Long>> setJedisMultiCommand(Transaction jedisMulti) {
        Response<Long> applicationDailyRateLimit =
                jedisMulti.hincrBy(Const.REDIS_APP_RATELIMIT_DAILY, "app", -1);
        Response<Long> applicationMinutelyRateLimit =
                jedisMulti.hincrBy(Const.REDIS_APP_RATELIMIT_MINUTELY, "app", -1);


        Map<String, Response<Long>> appRatelimit = Maps.newHashMap();
        appRatelimit.put(Const.REDIS_APP_RATELIMIT_DAILY, applicationDailyRateLimit);
        appRatelimit.put(Const.REDIS_APP_RATELIMIT_MINUTELY, applicationMinutelyRateLimit);

        return appRatelimit;
    }

    @Override
    protected boolean onFailure(Transaction jedisMulti) {
        jedisMulti.hincrBy(Const.REDIS_SERVICE_CAPACITY_DAILY, "undefined", 1);
        jedisMulti.hincrBy(Const.REDIS_APP_RATELIMIT_DAILY, "app", 1);
        jedisMulti.hincrBy(Const.REDIS_APP_RATELIMIT_MINUTELY, "app", 1);
        return false;
    }
}
