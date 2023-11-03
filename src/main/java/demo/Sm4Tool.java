package demo;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class Sm4Tool {

  private static final String ALGORITHM_NAME = "SM4";
  private static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
  private static final String ALGORITHM_NAME_ECB_NOPADDING = "SM4/ECB/NoPadding";
  private static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
  private static final String ALGORITHM_NAME_CBC_NOPADDING = "SM4/CBC/NoPadding";

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  public static String generateKey() {
    try {
      KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
      kg.init(128, new SecureRandom()); // 目前只支持16字符长度的Key
      return Hex.toHexString(kg.generateKey().getEncoded());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(String data, String key) {
    try {
      Cipher cipher = generateECBCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, Hex.decode(key));
      return Hex.toHexString(cipher.doFinal(data.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String decrypt(String cipherText, String key) {
    try {
      Cipher cipher = generateECBCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, Hex.decode(key));
      return new String(cipher.doFinal(Hex.decode(cipherText)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Cipher generateECBCipher(String algorithmName, int mode, byte[] key) {
    try {
      Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
      Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
      cipher.init(mode, sm4Key);
      return cipher;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
