package com.idgovern.util;

import lombok.extern.slf4j.Slf4j;
import java.security.MessageDigest;

/**
 * Utility class for generating MD5 hash digests.
 *
 * <p>
 * Provides a simple method to convert arbitrary strings into their
 * MD5 hexadecimal representation. Useful for password hashing,
 * data integrity checks, or creating unique identifiers.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Input string must not be null; otherwise, the result is null.</li>
 *     <li>Output is a 32-character uppercase hexadecimal string.</li>
 *     <li>Uses Java's built-in {@link MessageDigest} with the MD5 algorithm.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class MD5Util {

    /**
     * Generates the MD5 hash of the input string.
     *
     * <p>
     * Converts the resulting hash bytes into an uppercase hexadecimal string.
     * Returns null if any exception occurs during digest generation.
     * </p>
     *
     * @param s the input string to hash
     * @return 32-character uppercase hexadecimal MD5 hash, or null if error occurs
     */
    public final static String encrypt(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            // Obtain MD5 digest instance
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // Update digest with input bytes
            mdInst.update(btInput);
            // Compute the digest
            byte[] md = mdInst.digest();
            /// Convert digest bytes to hexadecimal string
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("generate md5 error for input: {}", s, e);
            return null;
        }
    }
}
