package demo;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.engines.SM2Engine.Mode;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;

public class Sm2Tool {

  // 加密模式
  private static final Mode CIPHER_MODE = Mode.C1C3C2;
  // 椭圆曲线ECParameters ASN.1 结构
  private static final X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
  // 椭圆曲线公钥或私钥的基本域参数。
  private static final ECParameterSpec ecParameterSpec = new ECParameterSpec(
      x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  private Sm2Tool() {
  }

  /**
   * <pre>
   * 生成密钥对
   * 注意公钥格式："04" + xHex + yHex
   *    这个公钥格式是为了兼容Js版本的公钥。BC库默认公钥不带04开头
   *
   * <a href="https://github.com/JuneAndGreen/sm-crypto">JS版本</a>
   *  JS代码：
   *    const sm2 = require('sm-crypto').sm2
   *    let keypair = sm2.generateKeyPairHex()
   *    const publicKey = keypair.publicKey
   *    const privateKey = keypair.privateKey
   * </pre>
   */
  public static Keypair generateKeyPair() {
    try {
      KeyPairGenerator kpGen = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
      kpGen.initialize(ecParameterSpec, new SecureRandom());
      KeyPair kp = kpGen.generateKeyPair();

      ECPrivateKeyParameters priKey = getPrivateKeyParameters((BCECPrivateKey) kp.getPrivate());
      ECPublicKeyParameters pubKey = getPublicKeyParameters((BCECPublicKey) kp.getPublic());

      String xHex = ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded());
      String yHex = ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded());

      Keypair keys = new Keypair();
      keys.setPrivateKey(ByteUtils.toHexString(priKey.getD().toByteArray()));
      keys.setPublicKey("04" + xHex + yHex); // 追加“04”开头。BC库的publicKey是不带04开头。这里追加04，是为了配合前端JS库
      return keys;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解析Js库生成的privateKey
   * @param privateKeyHex 私钥，Hex编码
   * @return BC库的PrivateKey
   */
  private static BCECPrivateKey getPrivateKey(String privateKeyHex) {
    BigInteger d = new BigInteger(privateKeyHex, 16);
    ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecParameterSpec);
    return new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
  }

  /**
   * 解析Js库生成的publicKey
   * @param publicKeyHex 公钥，Hex编码。格式："04" + xHex + yHex
   * @return BC库的PublicKey
   */
  private static BCECPublicKey getPublicKey(String publicKeyHex) {
    int halfLength =
        (publicKeyHex.length() - 2) / 2; // 去掉“04”开头。BC库的publicKey是不带04开头。这里去掉04，是为了配合前端JS库
    String xHex = publicKeyHex.substring(2, halfLength + 2);
    String yHex = publicKeyHex.substring(halfLength + 2);
    ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(
        x9ECParameters.getCurve().createPoint(new BigInteger(xHex, 16), new BigInteger(yHex, 16)),
        ecParameterSpec);
    return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
  }

  /**
   * 解析公钥参数
   * @param publicKey BC库的PublicKey
   * @return 公钥参数
   */
  private static ECPublicKeyParameters getPublicKeyParameters(BCECPublicKey publicKey) {
    ECParameterSpec ecParameterSpec = publicKey.getParameters();
    ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
        ecParameterSpec.getG(), ecParameterSpec.getN());
    return new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
  }

  /**
   * 解析私钥参数
   * @param privateKey BC库的PublicKey
   * @return 私钥参数
   */
  private static ECPrivateKeyParameters getPrivateKeyParameters(BCECPrivateKey privateKey) {
    ECParameterSpec ecParameterSpec = privateKey.getParameters();
    ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
        ecParameterSpec.getG(), ecParameterSpec.getN());
    return new ECPrivateKeyParameters(privateKey.getD(), ecDomainParameters);
  }

  public static String decrypt(String cipherData, String privateKey) {
    return decrypt(getPrivateKey(privateKey), cipherData);
  }

  /**
   * SM2解密算法
   * @param privateKey  私钥
   * @param cipherData  密文数据
   */
  private static String decrypt(PrivateKey privateKey, String cipherData) {
    // 固定04开头,js版因为省略了04开头，后台补上
    byte[] cipherDataByte = Hex.decode("04" + cipherData);

    BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
    ECPrivateKeyParameters ecPrivateKeyParameters = getPrivateKeyParameters(bcecPrivateKey);
    SM2Engine sm2Engine = new SM2Engine(CIPHER_MODE);
    sm2Engine.init(false, ecPrivateKeyParameters);

    try {
      return new String(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length),
          StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 加密
   * @param publicKey 公钥
   * @param data 原文
   */
  private static String encrypt(PublicKey publicKey, String data) {
    ECPublicKeyParameters ecPublicKeyParameters = getPublicKeyParameters(
        (BCECPublicKey) publicKey);

    SM2Engine sm2Engine = new SM2Engine(CIPHER_MODE);
    sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));

    try {
      byte[] in = data.getBytes(StandardCharsets.UTF_8);
      // substring(2), 去掉04开头
      return Hex.toHexString(sm2Engine.processBlock(in, 0, in.length)).substring(2);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(String data, String publicKey) {
    return encrypt(getPublicKey(publicKey), data);
  }

  /**
   * 签名
   * @param data 原文
   * @param privateKeyHex 私钥
   * @param id 附加参数，非必填，例如：userId
   * @return 签名值：对签名后得到的DER编码的Byte 进行Hex编码后的值
   */
  public static String sign(String data, String privateKeyHex, String id) {
    try {
      // 生成SM2sign with sm3 签名验签算法实例
      SM2Signer signer = new SM2Signer();
      CipherParameters cipherParameters = null;
      ParametersWithRandom pwr = new ParametersWithRandom(
          getPrivateKeyParameters(getPrivateKey(privateKeyHex)), new SecureRandom());
      if (id != null) {
        cipherParameters = new ParametersWithID(pwr, id.getBytes());
      } else {
        cipherParameters = pwr;
      }
      // 签名需要使用私钥，使用私钥 初始化签名实例
      signer.init(true, cipherParameters);
      // 写入签名原文到算法中
      byte[] srcDataByte = data.getBytes(StandardCharsets.UTF_8);
      signer.update(srcDataByte, 0, srcDataByte.length);
      // 签名原文
      return Hex.toHexString(signer.generateSignature());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String sign(String data, String privateKeyHex) {
    return sign(data, privateKeyHex, null);
  }

  /**
   * 验签
   * @param data 原文
   * @param signText 签名值
   * @param publicKeyHex 公钥
   * @param id 附加参数，非必填，例如：userId
   * @return ture or false
   */
  public static boolean verifySign(String data, String signText, String publicKeyHex, String id) {
    try {
      // 签名需要使用公钥，使用公钥 初始化签名实例
      SM2Signer signer = new SM2Signer();
      CipherParameters param = null;
      ECPublicKeyParameters pubKeyParameters = getPublicKeyParameters(
          getPublicKey(publicKeyHex));
      if (id != null) {
        param = new ParametersWithID(pubKeyParameters, id.getBytes());
      } else {
        param = pubKeyParameters;
      }
      signer.init(false, param);
      // 写入待验签的签名原文到算法中
      byte[] srcDataByte = data.getBytes(StandardCharsets.UTF_8);
      signer.update(srcDataByte, 0, srcDataByte.length);
      return signer.verifySignature(Hex.decode(signText));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verifySign(String data, String signText, String publicKeyHex) {
    return verifySign(data, signText, publicKeyHex, null);
  }

  public static class Keypair {

    private String privateKey;
    private String publicKey;

    public Keypair() {
    }

    public String getPrivateKey() {
      return privateKey;
    }

    public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
    }

    public String getPublicKey() {
      return publicKey;
    }

    public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
    }
  }
}