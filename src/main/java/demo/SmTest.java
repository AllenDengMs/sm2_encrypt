package demo;

import demo.Sm2Tool.Keypair;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmTest {

  private static final String performpanceTestData = "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";

  /**
   * 配合JS版使用：https://github.com/JuneAndGreen/sm-crypto
   */
  private static String publicKeyFromJS = "04c7a0c75f752a3a65498f7d3b6cab912d0cbe72aa6807ee675a1dd14f3149fe416e9a9c31e4d032a6cc9585b62f1d2a98f2090187ea83b24e8a4ab881a5424383";
  private static String privateKeyFromJS = "8c30bed6088fa995e10db01700b5fb22591757aca1dea9fc20b8ecf89bb68938";

  public static void main(String[] args) {
//    testSm2Encrypt(); // 加密、解密
//    testSm2Sign(); // 签名、验签
//
//    testSm3(); // sm3加密
//    testSm4Encrypt(); // sm4加密

    testSm2SignPerformance(); // 签名速度测试
    testSm2EncryptPerformance(); // 加密速度测试
    testSm4Performance(); // 加密速度测试
  }

  /**
   * 测试sm3加密，对标MD5
   */
  private static void testSm3() {
    Sm3Tool.hash("data");
  }

  /**
   * 测试sm2加密/解密
   */
  private static void testSm2Encrypt() {
    System.out.println("=========== 测试sm2加密/解密 ===========");
    // 生成公钥私钥
    Keypair keypair = Sm2Tool.generateKeyPair();
    String serverPrivateKey = keypair.getPrivateKey();
    String serverPublicKey = keypair.getPublicKey();

    // 加密、解密
    String data = "Test Data";
    String encryptData = Sm2Tool.encrypt(data, serverPublicKey);
    System.out.println("encryptData: " + encryptData);
    System.out.println("decryptData: " + Sm2Tool.decrypt(encryptData, serverPrivateKey));

    // 使用js生成公钥密码进行加密解密
    System.out.println(
        Sm2Tool.decrypt(Sm2Tool.encrypt("Test Js Key", publicKeyFromJS),
            privateKeyFromJS));
  }

  /**
   * <pre>
   * 测试Sm2签名、验签
   * 注意：如果使用使用这个版本的JS库，https://github.com/JuneAndGreen/sm-crypto
   * 前端JS代码要这样写：
   *    let sigValueHex = sm2.doSignature(msg, privateKey,  { hash:true, der:true, userId: id }) // 签名
   *    let verifyResult = sm2.doVerifySignature(msg, sigValueHex, publicKey,  { hash:true, der:true, userId: id}) // 验签结果
   * </pre>
   */
  private static void testSm2Sign() {
    System.out.println("=========== 测试Sm2签名、验签 ===========");
    String data = "Hello";
    String sign = Sm2Tool.sign(data, privateKeyFromJS);
    System.out.println("签名结果：" + sign);
    boolean b = Sm2Tool.verifySign(data, sign, publicKeyFromJS);
    System.out.println("验签结果：" + b);
  }

  /**
   * 测试Sm2加密性能
   */
  private static void testSm2EncryptPerformance() {
    System.out.println("=========== 测试Sm2加密性能 ===========");
    int count = 100; // 测试时间
    long sm2EncrypttimeCount = 0;
    long sm2DtimeCount = 0;
    Keypair keys = Sm2Tool.generateKeyPair();
    String publicKey = keys.getPublicKey();
    String privateKey = keys.getPrivateKey();
    Sm2Tool.decrypt(Sm2Tool.encrypt(performpanceTestData, publicKey), privateKey); // 初始化
    for (int i = 0; i < count; i++) {
      long startTime1 = System.currentTimeMillis();
      String encrypt = Sm2Tool.encrypt(performpanceTestData, publicKey);
      long eTime = System.currentTimeMillis() - startTime1;
      sm2EncrypttimeCount += eTime;

      long startTime = System.currentTimeMillis();
      Sm2Tool.decrypt(encrypt, privateKey);
      long dTime = System.currentTimeMillis() - startTime;
      sm2DtimeCount += dTime;
    }
    int length = performpanceTestData.length();
    System.out.println("字符长度" + length + "，SM2加密时间（毫秒）: " + new BigDecimal(sm2EncrypttimeCount).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
    System.out.println("字符长度" + length + "，SM2解密密时间（毫秒）: " + new BigDecimal(sm2DtimeCount).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
  }

  /** 测试Sm2签名/验签性能 */
  private static void testSm2SignPerformance() {
    System.out.println("=========== 测试Sm2签名/验签性能  ===========");
    String id = null;
    int count = 100; // 测试时间
    long sm2SignTimeCount = 0;
    long sm2VerifyTimeCount = 0;
    Keypair keys = Sm2Tool.generateKeyPair();
    String publicKey = keys.getPublicKey();
    String privateKey = keys.getPrivateKey();
    Sm2Tool.verifySign(performpanceTestData, Sm2Tool.sign(performpanceTestData, privateKey, id), publicKey, id); // 初始化
    for (int i = 0; i < count; i++) {
      long startTime1 = System.currentTimeMillis();
      String sign = Sm2Tool.sign(performpanceTestData, privateKey, id);
      long eTime = System.currentTimeMillis() - startTime1;
      sm2SignTimeCount += eTime;

      long startTime = System.currentTimeMillis();
      Sm2Tool.verifySign(performpanceTestData, sign, publicKey, id);
      long dTime = System.currentTimeMillis() - startTime;
      sm2VerifyTimeCount += dTime;
    }
    int length = performpanceTestData.length();
    System.out.println("字符长度" + length + "，SM2签名时间（毫秒）: " + new BigDecimal(sm2SignTimeCount).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
    System.out.println("字符长度" + length + "，SM2验签密时间（毫秒）: " + new BigDecimal(sm2VerifyTimeCount).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
  }

  /**
   * 测试SM4对称加密，对标AES加密
   */
  private static void testSm4Encrypt() {
    System.out.println("=========== 测试sm4加密/解密 ===========");
    String sm4Key = "79e5011c2b4f49d85cf0c5466b934d53";
    String data = "abc";
    String encrypt = Sm4Tool.encrypt(data, sm4Key);
    System.out.println(encrypt);
    System.out.println(Sm4Tool.decrypt(encrypt, sm4Key));
  }

  private static void testSm4Performance() {
    System.out.println("=========== 测试sm4加密性能 ===========");
    String sm4Key = "79e5011c2b4f49d85cf0c5466b934d53";
    Sm4Tool.decrypt(Sm4Tool.encrypt(performpanceTestData, sm4Key), sm4Key);
    int count = 100; // 测试时间
    long encryptTime = 0;
    long decryptTime = 0;
    for (int i = 0; i < count; i++) {

      long startTime = System.currentTimeMillis();
      String encrypt = Sm4Tool.encrypt(performpanceTestData, sm4Key);
      encryptTime += (System.currentTimeMillis() - startTime);

      long startTime2 = System.currentTimeMillis();
      Sm4Tool.decrypt(encrypt, sm4Key);
      decryptTime += (System.currentTimeMillis() - startTime2);
    }
    int length = performpanceTestData.length();
    System.out.println("字符长度" + length + "，SM4加密时间（毫秒）: " + new BigDecimal(encryptTime).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
    System.out.println("字符长度" + length + "，SM4解密时间（毫秒）: " + new BigDecimal(decryptTime).divide(new BigDecimal(count), 2, RoundingMode.CEILING));
  }

}
