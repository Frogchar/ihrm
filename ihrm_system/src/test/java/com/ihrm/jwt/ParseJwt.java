package com.ihrm.jwt;

import io.jsonwebtoken.*;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/18 14:55
 * @Email songchao_ss@163.com
 */
public class ParseJwt {
    public static void main(String[] args) {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMiIsImlhdCI6MTU5MjQ2NDgwMiwiZXhwIjoxNTkyNDY0ODYyLCJrZXkiOiJ2YWx1ZSIsImxvZ28iOiJsb2dvLnBuZyJ9.B-d4mBLVWCr8lU7Wf2e-uUvb3gKZ_X446kfonqjW6R0";
        String frogchar = Jwts.parser().setSigningKey("frogchar").parseClaimsJws(jwt).getSignature();
        Claims claims = Jwts.parser().setSigningKey("frogchar").parseClaimsJws(jwt).getBody();
        JwsHeader header = Jwts.parser().setSigningKey("frogchar").parseClaimsJws(jwt).getHeader();
        System.out.println(frogchar);
        System.out.println(claims);
        System.out.println(header);
        Object o = claims.get("key");
        Object o1 = claims.get("logo");
        System.out.println(o);
        System.out.println(o1);
    }
}
