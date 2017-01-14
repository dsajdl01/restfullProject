package com.department.core.data;

import com.departments.dto.fault.exception.LoginStaffException;
import com.departments.dto.fault.exception.ValidationException;
import com.httpSession.core.HttpSessionCoreServlet;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.Base64.*;

/**
 * Created by david on 21/12/16.
 */
public class PasswordAuthentication {

    /**
     * Each token produced by this class uses this identifier as a prefix.
     */
    public static final String ID = "$31$";

    /**
     * The minimum recommended cost, used by default
     */
    public static final int DEFAULT_COST = 16;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final int SIZE = 128;

    private static final Pattern layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");

    private final SecureRandom random;

    private final int cost;

    private HttpSessionCoreServlet httpSessionCoreServlet;

/*    public static void main(String arg[]) {
        PasswordAuthentication pa = new PasswordAuthentication();

        System.out.println("dkjghdsk: " + pa.hashPassword("password"));

        System.out.println("commape true: " + pa.authenticate("password", "$31$16$jDvam4TS3HgpKUwxoWBmftGKYemTwS9xjfaytGILoS0"));

        System.out.println("commape false: " + pa.authenticate("david.sajdl@arkessa.co.uk", "$31$16$jDvam4TS3HgpKUwxoWBmftGKYemTwS9xjfaytGILoS0"));
    }*/

    public PasswordAuthentication(HttpSessionCoreServlet httpSessionCoreServlet)
    {
        this(DEFAULT_COST);
        this.httpSessionCoreServlet = httpSessionCoreServlet;
    }

    /**
     * Create a password manager with a specified cost
     *
     * @param cost the exponential computational cost of hashing a password, 0 to 30
     */
    public PasswordAuthentication(int cost)
    {
        iterations(cost); /* Validate cost */
        this.cost = cost;
        this.random = new SecureRandom();
    }

    private static int iterations(int cost)
    {
        if ((cost & ~0x1F) != 0)
            throw new IllegalArgumentException("cost: " + cost);
        return 1 << cost;
    }

    /**
     * Hash a password for storage.
     *
     * @return a secure authentication token to be stored for later authentication
     */
    protected String hash(char[] password)
    {
        byte[] salt = new byte[SIZE / 8];
        random.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);

        Encoder enc = getUrlEncoder().withoutPadding();
        return ID + cost + '$' + enc.encodeToString(hash);
    }

    /**
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    protected boolean authenticate(char[] password, String token)
    {
        Matcher m = layout.matcher(token);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid token format");
        int iterations = iterations(parseInt(m.group(1)));
        byte[] hash = getUrlDecoder().decode(m.group(2));
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
        byte[] check = pbkdf2(password, salt, iterations);
        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx)
            zero |= hash[salt.length + idx] ^ check[idx];
        return zero == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
    {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        }
        catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }

    public String hashPassword(String password)
    {
        return hash(password.toCharArray());
    }


    public boolean authenticate(String password, String token)
    {
        return authenticate(password.toCharArray(), token);
    }

    public void authorizedStaffId(int staffId, HttpServletRequest request) throws ValidationException, LoginStaffException {
        basicStaffIdValidation(staffId);
        httpSessionCoreServlet.anyStaffIsLogin(request);
        staffIdmatchWithLoginStaffId(staffId, request);
    }

    private void staffIdmatchWithLoginStaffId(int staffId, HttpServletRequest request) throws LoginStaffException {
        if (staffId != httpSessionCoreServlet.getStaffIdAttribute(request)) {
          throw new LoginStaffException("Current staff is not authorized");
       }
    }

    private void basicStaffIdValidation(int staffId) throws ValidationException {
        if(staffId <= 0 ) throw new ValidationException("Staff id cannot be zero or negative");
    }
}
