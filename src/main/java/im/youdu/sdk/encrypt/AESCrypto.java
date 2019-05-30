package im.youdu.sdk.encrypt;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import im.youdu.sdk.exception.AESCryptoException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Random;

/**
 * 提供设置企业应用通知加解密接口(UTF8编码的字符串).
 * 第三方调用开放接口设置企业应用通知，并请求消息进行加密。
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * 在官方网站下载JCE无限制权限策略文件（
 * 	    JDK6的下载地址：
 * 	    http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
 * 	    JDK7的下载地址：
 *      http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html，
 *      JDK8的下载地址：
 *      http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 *      ）
 * 	下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
 * 	如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件
 * 	如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件
 */
public class AESCrypto {
    private static Charset CHARSET = Charset.forName("utf-8");
    private String appId;
    private byte[] aesKey;

    // 生成4个字节的网络字节序
    private static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // 还原4个字节的网络字节序
    private static  int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    // 随机生成16位字符串
    private static String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            buffer.append(base.charAt(number));
        }
        return buffer.toString();
    }

    /**
     * 对明文进行加密.
     *
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     * @throws AESCryptoException aes加密失败
     */
    private static String encryptData(String randomStr, byte[] text, byte[] appId, byte[] aesKey) throws AESCryptoException {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(text.length);

        // randomStr + networkBytesOrder + text + appId
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(text);
        byteCollector.addBytes(appId);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            return Base64.encode(encrypted);

        } catch (GeneralSecurityException e) {
            throw new AESCryptoException(e.getMessage(), e);
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AESCryptoException aes解密失败
     */
    private static byte[] decryptData(String text, String appId, byte[] aesKey) throws AESCryptoException {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decode(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (GeneralSecurityException e) {
            throw new AESCryptoException(e.getMessage(), e);
        }

        // 去除补位字符
        byte[] bytes = PKCS7Encoder.decode(original);

        // 分离16位随机字符串,网络字节序和appId
        byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

        int length = recoverNetworkBytesOrder(networkOrder);

        byte[] backAppId = Arrays.copyOfRange(bytes, 20 + length, bytes.length);
        String backAppIdStr = new String(backAppId);
//        if (! appId.equals(backAppIdStr)) {
//            throw new AESCryptoException("AppId不匹配", null);
//        }
        return Arrays.copyOfRange(bytes, 20, 20 + length);
    }

    /**
     * 构造函数
     * @param appId 有度userportal管理后台上，拿到应用对应的appId
     * @param encodingAesKey 有度userpotal管理后台上，取得系统生成的EncodingAESKey
     */
    public AESCrypto(String appId, String encodingAesKey) {
        this.appId = appId;
        this.aesKey = Base64.decode(encodingAesKey);
    }

    /**
     * 将要调用设置企业应用通知的请求消息加密.
     * 对要发送的消息进行AES-CBC加密
     * @return 返回密文
     * @throws AESCryptoException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public String encrypt(byte[] content) throws AESCryptoException {
        return encryptData(getRandomStr(), content, this.appId.getBytes(), this.aesKey);
    }

    /**
     * 检验消息的真实性，并且获取解密后的明文.
     *
     * @param postData 密文，对应POST请求的数据
     *
     * @return 解密后的原文
     * @throws AESCryptoException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public byte[] decrypt(String postData) throws AESCryptoException {
        return decryptData(postData, this.appId, this.aesKey);
    }

    public byte[] decryptV2(String postData) throws AESCryptoException {
        return decryptDataV2(postData, this.appId, this.aesKey);
    }

    private static byte[] decryptDataV2(String text, String appId, byte[] aesKey) throws AESCryptoException {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = java.util.Base64.getUrlDecoder().decode(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (GeneralSecurityException e) {
            throw new AESCryptoException(e.getMessage(), e);
        }

        // 去除补位字符
        byte[] bytes = PKCS7Encoder.decode(original);

        // 分离16位随机字符串,网络字节序和appId
        byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

        int length = recoverNetworkBytesOrder(networkOrder);

        byte[] backAppId = Arrays.copyOfRange(bytes, 20 + length, bytes.length);
        String backAppIdStr = new String(backAppId);
        return Arrays.copyOfRange(bytes, 20, 20 + length);
    }

}
