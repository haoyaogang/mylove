package jb.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES Coder<br/>
 * secret key length:	56 bit, default:	56 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 * @author Aub
 * 
 */
/*Java 加密解密之对称加密算法DES
数据加密算法（Data Encryption Algorithm，DEA）是一种对称加密算法，很可能是使用最广泛的密钥系统，特别是在保护金融数据的安全中，最初开发的DEA是嵌入硬件中的。通常，自动取款机（Automated Teller Machine，ATM）都使用DEA。它出自IBM的研究工作，IBM也曾对它拥有几年的专利权，但是在1983年已到期后，处于公有范围中，允许在特定条件下可以免除专利使用费而使用。1977年被美国政府正式采纳。
 
1998年后实用化DES破译机的出现彻底宣告DES算法已不具备安全性，1999年NIST颁布新标准，规定DES算法只能用于遗留加密系统，但不限制使用DESede算法。当今DES算法正是推出历史舞台，AES算法称为他的替代者。(详见： Java 加密解密之对称加密算法AES )
 
加密原理
DES 使用一个 56 位的密钥以及附加的 8 位奇偶校验位，产生最大 64 位的分组大小。这是一个迭代的分组密码，使用称为 Feistel 的技术，其中将加密的文本块分成两半。使用子密钥对其中一半应用循环功能，然后将输出与另一半进行“异或”运算；接着交换这两半，这一过程会继续下去，但最后一个循环不交换。DES 使用 16 个循环，使用异或，置换，代换，移位操作四种基本运算。
 
JDK对DES算法的支持
密钥长度：56位 
工作模式：ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128
填充方式：Nopadding/PKCS5Padding/ISO10126Padding/*/
public class DESCoder {
	
	/**
	 * 密钥算法
	*/
	private static final String KEY_ALGORITHM = "DES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
//	private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/ISO10126Padding";
	
	
	/**
	 * 初始化密钥
	 * 
	 * @return byte[] 密钥 
	 * @throws Exception
	 */
	public static byte[] initSecretKey() throws Exception{
		//返回生成指定算法的秘密密钥的 KeyGenerator 对象
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		//初始化此密钥生成器，使其具有确定的密钥大小
		kg.init(56);
		//生成一个密钥
		SecretKey  secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 转换密钥
	 * 
	 * @param key	二进制密钥
	 * @return Key	密钥
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception{
		//实例化DES密钥规则
		DESKeySpec dks = new DESKeySpec(key);
		//实例化密钥工厂
		SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		//生成密钥
		SecretKey  secretKey = skf.generateSecret(dks);
		return secretKey;
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	密钥
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	二进制密钥
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	二进制密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//使用密钥初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//执行操作
		return cipher.doFinal(data);
	}
	
	
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	二进制密钥
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	密钥
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	二进制密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//还原密钥
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		//执行操作
		return cipher.doFinal(data);
	}
	
	private static String  showByteArray(byte[] data){
		if(null == data){
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for(byte b:data){
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
//		byte[] key = initSecretKey();
		byte[] key = "12345678".getBytes();
		System.out.println("key："+ showByteArray(key));
		
		Key k = toKey(key);
		
		String data ="DES数据";
		System.out.println("加密前数据: string:"+data);
		System.out.println("加密前数据: byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("加密后数据: byte[]:"+showByteArray(encryptData));
		System.out.println("加密后数据: hexStr:"+Hex.encodeHexStr(encryptData));
		System.out.println();
		byte[] decryptData = decrypt(Hex.decodeHex(Hex.encodeHexStr(encryptData).toCharArray()), k);
		System.out.println("解密后数据: byte[]:"+showByteArray(decryptData));
		System.out.println("解密后数据: string:"+new String(decryptData));
		
	}
}


