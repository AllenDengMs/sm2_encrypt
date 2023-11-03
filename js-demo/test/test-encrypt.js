describe('测试', () => {
  it('sm2', function () {
    const sm2 = require('sm-crypto').sm2
    const cipherMode = 1 // 1 - C1C3C2，0 - C1C2C3，默认为1

    let keypair = sm2.generateKeyPairHex()
    let publicKey = '04c7a0c75f752a3a65498f7d3b6cab912d0cbe72aa6807ee675a1dd14f3149fe416e9a9c31e4d032a6cc9585b62f1d2a98f2090187ea83b24e8a4ab881a5424383'
    let privateKey = '8c30bed6088fa995e10db01700b5fb22591757aca1dea9fc20b8ecf89bb68938'
    // publicKey = keypair.publicKey // 公钥
    // privateKey = keypair.privateKey // 私钥

    // 加密
    const msg = 'Hello'
    let encryptData = sm2.doEncrypt(msg, publicKey, cipherMode) // 加密结果
    console.log(encryptData)
    let decryptData = sm2.doDecrypt(encryptData, privateKey, cipherMode) // 解密结果
    console.log(decryptData)

    // 签名
    let sigValueHex = sm2.doSignature(msg, privateKey,  { hash:true, der:true }) // 签名
    // let sigValueHex = sm2.doSignature(msg, privateKey,  { hash:true, der:true, userId: '1308' }) // 签名
    console.log('sign: ', sigValueHex)
    let verifyResult = sm2.doVerifySignature(msg, sigValueHex, publicKey,  { hash:true, der:true}) // 验签结果
    // let verifyResult = sm2.doVerifySignature(msg, sigValueHex, publicKey,  { hash:true, der:true, userId: 1308}) // 验签结果
    console.log('verifySign: ', verifyResult)
  })
  
  it('sm3', function () {
    const sm3 = require('sm-crypto').sm3
    let hashData = sm3('abc')
    console.log(hashData)
    // hmac
    hashData = sm3('abc', {
      key: 'daac25c1512fe50f79b0e4526b93f5c0e1460cef40b6dd44af13caec62e8c60e0d885f3c6d6fb51e530889e6fd4ac743a6d332e68a0f2a3923f42585dceb93e9', // 要求为 16 进制串或字节数组
    })
    console.log(hashData)
  })

  it('sm4-encrypt', function () {
    const sm4 = require('sm-crypto').sm4
    const msg = 'hello world!' // 可以为 utf8 串或字节数组
    const key = '79e5011c2b4f49d85cf0c5466b934d53' // 可以为 16 进制串或字节数组，要求为 128 比特
    // 加密，默认输出 16 进制字符串，默认使用 pkcs#7 填充（传 pkcs#5 也会走 pkcs#7 填充）
    console.log(sm4.encrypt(msg, key))
  })

  it('sm4-decrypt', function () {
    const sm4 = require('sm-crypto').sm4
    const encryptData = 'b852c36a23f81fc6a74f15cc44ff761d' // 可以为 16 进制串或字节数组
    const key = '79e5011c2b4f49d85cf0c5466b934d53' // 可以为 16 进制串或字节数组，要求为 128 比特

    console.log(sm4.decrypt(encryptData, key)) // 解密，默认输出 utf8 字符串，默认使用 pkcs#7 填充（传 pkcs#5 也会走 pkcs#7 填充）
  })
})