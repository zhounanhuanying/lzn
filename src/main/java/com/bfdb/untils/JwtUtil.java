package com.bfdb.untils;

import com.bfdb.config.Constant;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JwtUtil {
    //30秒
    private static final long tokenRefreshInterval = 30000;

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = Constant.JWT_SECRET;
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64( stringKey );
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec( encodedKey, 0, encodedKey.length, "AES" );
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id        当前用户ID或者生成的UUID
     *                  //     * @param issuer    该JWT的签发者，是否使用是可选的
     * @param userName  用户姓名
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String userName) throws Exception {
        // 指定签名的时候使用的签名算法，也就是header那部分，jwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setHeaderParam( "typ", "JWT" ) //jwt类型
                .claim( "userName", userName )
                .setId( id )  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .signWith( signatureAlgorithm, key )//设置签名使用的签名算法和签名使用的秘钥
                .setIssuedAt( new Date(System .currentTimeMillis() ) );//令牌签发时间
//        // 生成JWT的时间
//        long nowMillis = System.currentTimeMillis();
//        // 设置过期时间
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date( expMillis );
//            System.out.println( "令牌过期时间：" + sdf.format( exp ) );
//            builder.setExpiration( exp ).setNotBefore( new Date() ); //设置过期时间
//        } else {
//            Date exp = new Date( ttlMillis );
//            builder.setExpiration( exp );
//        }

        return builder.compact();
    }


    /**
     * 刷新token
     *
     * @param token
     * @param tokenValidityTime
     * @return
     */
//    public static String refreshToken(String token,String tokenValidityTime) {
////        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
//        String refreshedToken = null;
//        try {
//           String  parseJWTuserName=parseJWTuserName(token);
//            refreshedToken=createJWT(Constant.JWT_ID,parseJWTuserName,Long.valueOf(tokenValidityTime));
////            Claims jwtClaims = Jwts.parser().//得到DefaultJwtParser
////                    setSigningKey(generalKey())//设置签名的秘钥
////                    .parseClaimsJws(token).getBody();//设置需要解析的jwt
////           String  sign=token.split( "\\." )[2];
////            Date d1 = jwtClaims.getIssuedAt();
////            Date d2 = jwtClaims.getExpiration();
//
////            System.out.println("sign：" + sign);
////            System.out.println("令牌签发时间：" + sdf.format(d1));
////            System.out.println("令牌过期时间：" + sdf.format(d2));
//
////            refreshedToken = generateToken(jwtClaims);
//
//            //令牌签发时间
////            Date d3 = Jwts.parser().//得到DefaultJwtParser
////                    setSigningKey(generalKey())//设置签名的秘钥
////                    .parseClaimsJws(refreshedToken).getBody().getIssuedAt();
////            //令牌过期时间
////            Date d4 = Jwts.parser().//得到DefaultJwtParser
////                    setSigningKey( generalKey() )//设置签名的秘钥
////                    .parseClaimsJws( refreshedToken ).getBody().getExpiration();
////            System.out.println("令牌签发时间：" + sdf.format(d3));
////            System.out.println("令牌过期时间：" + sdf.format(d4));
//        } catch (Exception e) {
//            refreshedToken = null;
//        }
//        return refreshedToken;
//    }

    /**
     * 定义token还有30秒过期，才刷新
     * @return
     */
//    public static long refreshTokenTime(long ttlMillis, long tokenValidityTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//        long nowMilliTime = 0;
//        try {
//            // 过期时间向前推30秒
//            long nowMillis = ttlMillis - tokenRefreshInterval;
//            //获取当前时间
//            long nowMilli = System.currentTimeMillis();
//            if (nowMillis <= nowMilli) {
//                nowMilliTime=nowMilli+tokenValidityTime;
//            }
//            Date datenowMilli1 = new Date( nowMilliTime );
//            System.out.println( "转换过期时间" + sdf.format( datenowMilli1 ) );
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return nowMilliTime;
//    }

    /**
     * 根据jwt获取用户名
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static String parseJWTuserName(String jwt) throws Exception {
        String verifiedToken = tokenVerification( jwt );
        if (Constant.NONE_TOKEN.equals( verifiedToken ) && Constant.ERROR_TOKEN.equals( verifiedToken )) {
            return null;
        }
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey( key )                 //设置签名的秘钥
                .parseClaimsJws( verifiedToken ).getBody();     //设置需要解析的jwt
        return claims.get( "userName" ).toString();
    }

    /**
     * 验证jwt
     *
     * @param userToken
     * @return
     * @throws Exception
     */
    public static String tokenVerification(String userToken) throws Exception {
        String sign = null;
        try {
            //获取签名
            sign = userToken.split( "\\." )[2];
            Claims jwtClaims = Jwts.parser().//得到DefaultJwtParser
                    setSigningKey( generalKey() )//设置签名的秘钥
                    .parseClaimsJws( userToken ).getBody();//设置需要解析的jwt
            ObjectMapper objectMapper = new ObjectMapper();
            String payload = objectMapper.writeValueAsString( jwtClaims );
            //下面就是在为payload添加各种标准声明和私有声明了
            JwtBuilder jwtBuilder = Jwts.builder(). //这里其实就是new一个JwtBuilder，设置jwt的body
                    setHeaderParam( "typ", "JWT" )
                    .setPayload( payload )
                    .signWith( SignatureAlgorithm.HS256, generalKey() );//设置签名使用的签名算法和签名使用的秘钥
            String compact = jwtBuilder.compact();
            String newSign = compact.split( "\\." )[2];
            //验证新的签名和旧签名是否正确
            if (!sign.equals( newSign )) {
                return Constant.NONE_TOKEN;
            }
        }  catch (SignatureException | MalformedJwtException e) {
            //token错误
            return Constant.ERROR_TOKEN;
        }
        return userToken;
    }

}
