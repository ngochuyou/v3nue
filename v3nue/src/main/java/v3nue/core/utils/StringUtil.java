package v3nue.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils {

	public static String hash(String a) {
		MessageDigest md;

		try {
			// Select the message digest for the hash computation -> SHA-256
			md = MessageDigest.getInstance("SHA-256");
			// Generate the random salt
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			// Passing the salt to the digest for the computation
			md.update(salt);
			// Generate the salted hash
			byte[] hashedPassword = md.digest(a.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();

			for (byte b : hashedPassword)
				sb.append(String.format("%02x", b));

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "shjreisSnaIo";
		}
	}

	public static String random(int length) {
		String hash = hash("shjreisSnaIo");

		return (length < hash.length() ? hash.substring(0, length) : "shjreisSnaIo");
	}

	public static boolean isBCrypt(String encoded) {

		return Pattern.compile(Constants.BCRYPT_REGEX).matcher(encoded).matches();
	}

	public static boolean isEmail(String email) {

		return Pattern.compile(Constants.EMAIL_REGEX).matcher(email).matches();
	}
	
	public static boolean isDigits(String number) {
		
		return Pattern.compile("\\d+").matcher(number).matches();
	}
}
