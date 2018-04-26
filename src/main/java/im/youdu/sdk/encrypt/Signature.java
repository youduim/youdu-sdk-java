package im.youdu.sdk.encrypt;

import im.youdu.sdk.exception.SignatureException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// 计算回调消息签名接口
public class Signature {

    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     回调用Token
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     * @throws SignatureException 生成失败
     */
    public static String generateSignature(String token, String timestamp, String nonce, String encrypt) throws SignatureException {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer buffer = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                buffer.append(array[i]);
            }
            String str = buffer.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            buffer = new StringBuffer();
            String shaHex;
            for (byte aDigest : digest) {
                shaHex = Integer.toHexString(aDigest & 0xFF);
                if (shaHex.length() < 2) {
                    buffer.append(0);
                }
                buffer.append(shaHex);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException(e.getMessage(), e);
        }
    }
}
