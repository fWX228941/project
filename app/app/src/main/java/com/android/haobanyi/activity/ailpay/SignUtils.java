package com.android.haobanyi.activity.ailpay;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtils {
	/*开发者将私钥保留，将公钥提交给支付宝网关  */

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";
	/*支付宝签名*/
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/*微信签名*/
	public static String signByMD5(String source) {
		byte[] bytes = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(source.getBytes());  //更新摘要

			bytes = digest.digest(); //再通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		StringBuilder builder = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			/**
			 * 0xFF默认是整形，一个byte跟0xFF相与会先将那个byte转化成整形运算
			 */
			if ((b & 0xFF) < 0x10) {  //如果为1位 前面补个0
				builder.append("0");
			}

			builder.append(Integer.toHexString(b & 0xFF));
		}

		return builder.toString();
	}

}
