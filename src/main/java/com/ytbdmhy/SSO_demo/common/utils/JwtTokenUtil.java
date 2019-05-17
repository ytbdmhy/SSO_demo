package com.ytbdmhy.SSO_demo.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @description: jwt生成token
 */
public class JwtTokenUtil {

    // 寻找证书文件
    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    // 将证书文件里的私钥公钥拿出来
    static {
        try {
            // java key store 固定常量
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "123456".toCharArray());
            // jwt 为命令生成整数文件时的别名
            privateKey = (PrivateKey) keyStore.getKey("jwt", "123456".toCharArray());
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成token
     * @param subject （主体信息）
     * @param expirationSeconds 过期时间（秒）
     * @param claims 自定义身份信息
     * @return
     */
    public static String generateToken(String subject, int expirationSeconds, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
//                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000)) // 设置token超时时间
//                .signWith(SignatureAlgorithm.HS512, salt) // 不使用公钥私钥
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * @description: 解析token,获得subject中的信息
     * @param token
     * @param salt
     * @return
     */
    public static String parseToken(String token, String salt) {
        String subject = null;
        try {
//            Claims claims = Jwts.parser() // 不使用公钥私钥
//                    .setSigningKey(salt)
//                    .setSigningKey(publicKey)
//                    .parseClaimsJws(token)
//                    .getBody();
            subject = getTokenBody(token).getSubject();
        } catch (Exception e) {
        }
        return subject;
    }

    /**
     * @description: 获取token自定义属性
     * @param token
     * @return
     */
    public static Map<String, Object> getClaims(String token) {
        Map<String, Object> claims = null;
        try {
            claims = getTokenBody(token);
        } catch (Exception e) {

        }
        return claims;
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private static Object getTokenDetail(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parse(token)
                .getBody();
    }

    /**
     * @description: 是否已过期
     * @param expirationTime
     * @return
     */
    public static boolean isExpiration(String expirationTime) {
//        return getTokenBody(token).getExpiration().before(new Date());

        // 通过redis中的失效时间进行判断
        String currentTime = DateUtil.getTime();
        return DateUtil.compareDate(currentTime, expirationTime);
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ5dGJkbWh5IiwiaXAiOiIwOjA6MDowOjA6MDowOjEifQ.lNN5x1dvVnz62yN6xcUcGHaVdQYjonrsD769fi2izwKjDaYknDFD_1hyzvhFANjmp_qrDE5cEGXT6vXgrvSR9HmDw3TUOT0OgWyofJE_VES7bIMo-0Vr5EnhX6lPCcwTEuIJPAcd0fTxBLyteSJJIa068J9qmS3vybja3GaeXF0";

        String string = parseToken(token, "");
        System.out.println(string);

        System.out.println(getTokenDetail(token).toString());
    }
}
