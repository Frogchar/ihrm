package com.ihrm.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/18 15:15
 * @Email songchao_ss@163.com
 */
public class CustomJwt {
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        long time = now + 1000 * 60;
        String jwt = Jwts.builder().setId("12").setIssuedAt(new Date())
                .setExpiration(new Date(time))
                .signWith(SignatureAlgorithm.HS256, "frogchar")
                .claim("key", "value")
                .claim("logo","logo.png")
                .compact();
        System.out.println(jwt);
    }
}
