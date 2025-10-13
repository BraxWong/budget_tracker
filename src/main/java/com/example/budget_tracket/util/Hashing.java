package com.example.budget_tracket.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Component;

@Component
public class Hashing {
	
	public ArrayList<byte[]> generatePasswordHash(String password){
		ArrayList<byte[]> passwordInfo = new ArrayList<byte[]>();
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		passwordInfo.add(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			passwordInfo.add(factory.generateSecret(spec).getEncoded());
			return passwordInfo;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}
	
	public boolean verifyPassword(String password, byte[] passwordSalt, byte[] encryptedPassword) {
		boolean correctPassword = false;
		KeySpec spec = new PBEKeySpec(password.toCharArray(), passwordSalt, 65536, 128);
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			correctPassword = Arrays.equals(encryptedPassword, factory.generateSecret(spec).getEncoded());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException("Error verifying password", e);
		}
		return correctPassword;
	}
}