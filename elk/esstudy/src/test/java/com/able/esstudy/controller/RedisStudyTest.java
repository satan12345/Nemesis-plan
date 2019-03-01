package com.able.esstudy.controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;

/**
 * @author jipeng
 * @date 2019-02-27 11:21
 * @description
 */
public class RedisStudyTest {


    public void delBigHash(String host, int port, String password, String bigHashKey) {
        Jedis jedis = new Jedis(host, port);
        if (password != null && !"".equals(password)) {
            jedis.auth(password);
        }
        ScanParams scanParams = new ScanParams().count(100);
        String cursor = "0";
        do {

            ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
            List<Map.Entry<String, String>> entryList = scanResult.getResult();
            if (entryList != null && !entryList.isEmpty()) {
                for (Map.Entry<String, String> entry : entryList) {
                    jedis.hdel(bigHashKey, entry.getKey());
                }
            }
            cursor = scanResult.getStringCursor();
        } while (!"0".equals(cursor));

        //删除bigkey
        jedis.del(bigHashKey);
    }


}

