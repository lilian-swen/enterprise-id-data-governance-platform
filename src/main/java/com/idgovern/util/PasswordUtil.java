package com.idgovern.util;

import java.util.Date;
import java.util.Random;

/**
 * Utility class for generating random passwords.
 *
 * <p>
 * This class provides a method to generate passwords consisting of alternating
 * letters and numbers. Password length is randomized between 8 and 10 characters.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Generated passwords contain letters (excluding easily confusable letters like 'i', 'l', 'o') and numbers (excluding '0' and '1').</li>
 *     <li>Letters and numbers alternate in the generated password.</li>
 *     <li>Password length is between 8 and 10 characters.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-15 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
public class PasswordUtil {

    /** Array of letters used for password generation (uppercase & lowercase, excluding confusing letters). */
    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    /** Array of digits used for password generation (excluding '0' and '1'). */
    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };


    /**
     * Generates a random password.
     *
     * <p>
     * The password consists of alternating letters and numbers. Length is
     * randomly selected between 8 and 10 characters.
     * </p>
     *
     * @return a randomly generated password
     */
    public static String randomPassword() {

        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag = false;

        int length = random.nextInt(3) + 8; // Random length: 8, 9, or 10
        for (int i = 0; i < length; i++) {
            if (flag) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            } else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
    }
}
