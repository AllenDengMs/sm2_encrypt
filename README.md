# Java版SM2 SM3 SM4


简单封装BC库的，实现SM2、SM3、SM4常用的加密、解密。和JS版本通用。

Js版：https://github.com/JuneAndGreen/sm-crypto


```java
// SM2
// 生成公钥私钥
Keypair keypair = Sm2Tool.generateKeyPair();
String serverPrivateKey = keypair.getPrivateKey();
String serverPublicKey = keypair.getPublicKey();

// 加密、解密
String data = "Test Data";
String encryptData = Sm2Tool.encrypt(data, serverPublicKey); // 加密
Sm2Tool.decrypt(encryptData, serverPrivateKey) // 解密

// 签名验签
String data = "Hello";
String sign = Sm2Tool.sign(data, privateKeyFromJS); // 签名
boolean isCorrect = Sm2Tool.verifySign(data, sign, publicKeyFromJS); // 验签

// Sm3
Sm3Tool.hash("data");

// Sm4
String sm4Key = Sm4Tool.generateKey();
String data = "abc";
String encrypt = Sm4Tool.encrypt(data, sm4Key); // 加密
Sm4Tool.decrypt(encrypt, sm4Key); // 解密
```
