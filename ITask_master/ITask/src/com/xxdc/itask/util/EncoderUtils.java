package com.xxdc.itask.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author Bruce.Zhang
 * @data 2014年10月6日
 * 加密工具
 */
public final class EncoderUtils {
	/**
	 * MD5加密
	 */
	public static String md5(String string) {
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (Exception e) {

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 返回可逆算法DES的密钥
	 * 
	 * @param key
	 *            前8字节将被用来生成密钥。
	 * @return 生成的密钥
	 * @throws Exception
	 */
	public static Key getDESKey(byte[] key) throws Exception {
		DESKeySpec des = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return keyFactory.generateSecret(des);
	}

	/**
	 * 根据指定的密钥及算法，将字符串进行解密。
	 * 
	 * @param data
	 *            要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
	 * @param key
	 *            密钥。
	 * @param algorithm
	 *            算法。
	 * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
	 * @throws Exception
	 */
	public static String decrypt(String data, Key key, String algorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);
		String result = new String(cipher.doFinal(StringUtils.hexStringToByteArray(data)), "utf8");
		return result;
	}

	/**
	 * 根据指定的密钥及算法对指定字符串进行可逆加密。
	 * 
	 * @param data
	 *            要进行加密的字符串。
	 * @param key
	 *            密钥。
	 * @param algorithm
	 *            算法。
	 * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
	 */
	public static String encrypt(String data, Key key, String algorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return StringUtils.byteArrayToHexString(cipher.doFinal(data.getBytes("utf8")));
	}
	
	/**
     * 签名生成算法
     * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
     * @param String secret 签名密钥
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(HashMap<String,String> params, String secret) throws IOException
    {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Entry<String, String>> entrys = sortedParams.entrySet();
     
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        basestring.append(secret);
     
        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
     
        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }
}
