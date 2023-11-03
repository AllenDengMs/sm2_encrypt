package demo;

import java.security.Security;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * 对标MD5
 */
public class Sm3Tool {

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /**
   * 获取hash值
   * @param data 原文
   * @return hex编码值
   */
  public static String hash(String data) {
    SM3Digest digest = new SM3Digest();
    byte[] dataBytes = data.getBytes();
    digest.update(dataBytes, 0, dataBytes.length);
    byte[] hash = new byte[digest.getDigestSize()]; // 摘要值，对于SM3算法来说是32字节
    digest.doFinal(hash, 0);
    return Hex.toHexString(hash);
  }
}
