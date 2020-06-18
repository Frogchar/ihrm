package com.ihrm.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/18 14:51
 * @Email songchao_ss@163.com
 */
public class JWTTest {
    public static void main(String[] args) {
        String jwt = Jwts.builder().setId("12").setIssuer("宋超").setSubject("微服务").signWith(SignatureAlgorithm.HS256, "frogchar").compact();
        System.out.println(jwt);
    }
}
