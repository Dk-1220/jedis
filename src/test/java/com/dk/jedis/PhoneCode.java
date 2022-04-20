package com.dk.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * Jedis 实现手机验证码测试
 */
public class PhoneCode {
    public static void main(String[] args) {
//        String code = getCode();
//        System.out.println(code);
        verifyCode("13313345678");
        getRedisCode("13313345678","495363");
    }

    // 1. 生成6位数字验证码
    public static String getCode() {
        Random random = new Random();
        String code = "";

        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

    // 2. 每个手机每天只能发送三次，验证码放在redis中，设置过期时间
    public static void verifyCode(String phone) {
        // 连接redis
        Jedis jedis = new Jedis("192.168.95.128", 6379);

        // 拼接key
        // 手机发送次数key
        String countKey = "VerifyCode" + phone + ":count";
        // 验证码key
        String codeKey = "VerifyCode" + phone + ":code";

        // 每个手机每天只能发送三次
        String count = jedis.get(countKey);
        if (count == null) {
            // 没有发送过
            // 设置发送次数为1
            jedis.setex(countKey, 24 * 60 * 60, "1");
        } else if (Integer.parseInt(count) <= 2) {
            jedis.incr(countKey);
        } else if (Integer.parseInt(count) > 2) {
            // 发送三次，不能再次发送
            System.out.println("今天发送次数已经超过三次");
            jedis.close();
            return;
        }

        // 发送验证码
        String code = getCode();
        jedis.setex(codeKey, 120, code);
        jedis.close();
    }

    // 3. 验证码校验
    public static void getRedisCode(String phone, String code) {
        // 从redis获取验证码
        Jedis jedis = new Jedis("192.168.95.128", 6379);
        // 验证码key
        String codeKey = "VerifyCode" + phone + ":code";
        String redisCode = jedis.get(codeKey);
        // 判断
        if (redisCode != null && redisCode.equals(code)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        jedis.close();
    }
}
