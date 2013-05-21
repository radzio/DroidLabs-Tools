package net.droidlabs.security;

/*
 Copyright (c) 2010, Sungjin Han <meinside@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
 this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of meinside nor the names of its contributors may be
 used to endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

//package org.andlib.helpers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author meinside@gmail.com
 * @since 09.11.24.
 * 
 *        last update 10.04.13.
 * 
 */
final class StringCodec
{
	/**
	 * 
	 * @param original
	 * @return null if fails
	 */
	public static String urlencode(String original)
	{
		try
		{
			// return URLEncoder.encode(original, "utf-8");
			// fixed: to comply with RFC-3986
			return URLEncoder.encode(original, "utf-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		}
		catch (UnsupportedEncodingException e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param encoded
	 * @return null if fails
	 */
	public static String urldecode(String encoded)
	{
		try
		{
			return URLDecoder.decode(encoded, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param original
	 * @param key
	 * @return null if fails
	 */
	public static String hmacSha1Digest(String original, String key)
	{
		return hmacSha1Digest(original.getBytes(), key.getBytes());
	}

	/**
	 * 
	 * @param original
	 * @param key
	 * @return null if fails
	 */
	public static String hmacSha1Digest(byte[] original, byte[] key)
	{
		try
		{
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key, "HmacSHA1"));
			byte[] rawHmac = mac.doFinal(original);
			return new String(Base64Coder.encode(rawHmac));
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param original
	 * @return null if fails
	 */
	public static String md5sum(byte[] original)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(original, 0, original.length);
			StringBuffer md5sum = new StringBuffer(new BigInteger(1, md.digest()).toString(16));
			while (md5sum.length() < 32)
				md5sum.insert(0, "0");
			return md5sum.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param original
	 * @return null if fails
	 */
	public static String md5sum(String original)
	{
		return md5sum(original.getBytes());
	}

	/**
	 * AES encrypt function
	 * 
	 * @param original
	 * @param key
	 *            16, 24, 32 bytes available
	 * @param iv
	 *            initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesEncrypt(byte[] original, byte[] key, byte[] iv)
	{
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32))
		{
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16)
		{
			// Logger.e("iv's bit length is not 16");
			return null;
		}

		try
		{
			SecretKeySpec keySpec = null;
			Cipher cipher = null;
			if (iv != null)
			{
				keySpec = new SecretKeySpec(key, "AES/CBC/PKCS7Padding");
				cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
			}
			else
			// if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			}

			return cipher.doFinal(original);
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * AES decrypt function
	 * 
	 * @param encrypted
	 * @param key
	 *            16, 24, 32 bytes available
	 * @param iv
	 *            initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesDecrypt(byte[] encrypted, byte[] key, byte[] iv)
	{
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32))
		{
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16)
		{
			// Logger.e("iv's bit length is not 16");
			return null;
		}

		try
		{
			SecretKeySpec keySpec = null;
			Cipher cipher = null;
			if (iv != null)
			{
				keySpec = new SecretKeySpec(key, "AES/CBC/PKCS7Padding");
				cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
			}
			else
			// if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
			}

			return cipher.doFinal(encrypted);
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * generates RSA key pair
	 * 
	 * @param keySize
	 * @param publicExponent
	 *            public exponent value (can be RSAKeyGenParameterSpec.F0 or F4)
	 * @return
	 */
	public static KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent)
	{
		KeyPair keys = null;
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
			keyGen.initialize(spec);
			keys = keyGen.generateKeyPair();
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return keys;
	}

	/**
	 * generates a RSA public key with given modulus and public exponent
	 * 
	 * @param modulus
	 *            (must be positive? don't know exactly)
	 * @param publicExponent
	 * @return
	 */
	public static PublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent)
	{
		try
		{
			return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * generates a RSA private key with given modulus and private exponent
	 * 
	 * @param modulus
	 *            (must be positive? don't know exactly)
	 * @param privateExponent
	 * @return
	 */
	public static PrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger privateExponent)
	{
		try
		{
			return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * RSA encrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param original
	 * @param key
	 * @return
	 */
	public static byte[] rsaEncrypt(byte[] original, PublicKey key)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(original);
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * RSA decrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param encrypted
	 * @param key
	 * @return
	 */
	public static byte[] rsaDecrypt(byte[] encrypted, PrivateKey key)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
		}
		catch (Exception e)
		{
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * converts given byte array to a hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes)
	{
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			if (((int) bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}

	/**
	 * converts given hex string to a byte array (ex: "0D0A" => {0x0D, 0x0A,})
	 * 
	 * @param str
	 * @return
	 */
	public static final byte[] hexStringToByteArray(String str)
	{
		int i = 0;
		byte[] results = new byte[str.length() / 2];
		for (int k = 0; k < str.length();)
		{
			results[i] = (byte) (Character.digit(str.charAt(k++), 16) << 4);
			results[i] += (byte) (Character.digit(str.charAt(k++), 16));
			i++;
		}
		return results;
	}
}

// Copyright 2003-2009 Christian d'Heureuse, Inventec Informatik AG, Zurich,
// Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
// EPL, Eclipse Public License, http://www.eclipse.org/legal
// LGPL, GNU Lesser General Public License,
// http://www.gnu.org/licenses/lgpl.html
// AL, Apache License, http://www.apache.org/licenses
// BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

/**
 * A Base64 Encoder/Decoder.
 * 
 * <p>
 * This class is used to encode and decode data in Base64 format as described in
 * RFC 1521.
 * 
 * <p>
 * Home page: <a href="http://www.source-code.biz">www.source-code.biz</a><br>
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
 * Multi-licensed: EPL/LGPL/AL/BSD.
 * 
 * <p>
 * Version history:<br>
 * 2003-07-22 Christian d'Heureuse (chdh): Module created.<br>
 * 2005-08-11 chdh: Lincense changed from GPL to LGPL.<br>
 * 2006-11-21 chdh:<br>
 * &nbsp; Method encode(String) renamed to encodeString(String).<br>
 * &nbsp; Method decode(String) renamed to decodeString(String).<br>
 * &nbsp; New method encode(byte[],int) added.<br>
 * &nbsp; New method decode(String) added.<br>
 * 2009-07-16: Additional licenses (EPL/AL) added.<br>
 * 2009-09-16: Additional license (BSD) added.<br>
 */
class Base64CoderOld
{

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];
	static
	{
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			map1[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			map1[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			map1[i++] = c;
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static byte[] map2 = new byte[128];
	static
	{
		for (int i = 0; i < map2.length; i++)
			map2[i] = -1;
		for (int i = 0; i < 64; i++)
			map2[map1[i]] = (byte) i;
	}

	/**
	 * Encodes a string into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param s
	 *            a String to be encoded.
	 * @return A String with the Base64 encoded data.
	 */
	public static String encodeString(String s)
	{
		return new String(encode(s.getBytes()));
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @return A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in)
	{
		return encode(in, in.length);
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @param iLen
	 *            number of bytes to process in <code>in</code>.
	 * @return A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in, int iLen)
	{
		int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
		int oLen = ((iLen + 2) / 3) * 4; // output length including padding
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen)
		{
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}
		return out;
	}

	/**
	 * Decodes a string from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return A String containing the decoded data.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static String decodeString(String s)
	{
		return new String(decode(s));
	}

	/**
	 * Decodes a byte array from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(String s)
	{
		return decode(s.toCharArray());
	}

	/**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded data.
	 * 
	 * @param in
	 *            a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in)
	{
		int iLen = in.length;
		if (iLen % 4 != 0)
			throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
		while (iLen > 0 && in[iLen - 1] == '=')
			iLen--;
		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen)
		{
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < oLen)
				out[op++] = (byte) o1;
			if (op < oLen)
				out[op++] = (byte) o2;
		}
		return out;
	}

	// Dummy constructor.
	private Base64CoderOld()
	{
	}

}