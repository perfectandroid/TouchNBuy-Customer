package com.perfect.easyshopplus.Utility;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.DecimalFormat;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    static Random random = new Random();
    private static final String ASCII = "ASCII";

    public static float dipToPx(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dipValue * density;
    }


    //Generate random number(Prize)
    private static String generateCodePart(int min, int max) {
        int minNumber = 1;
        int maxNumber = 100;
        return String.valueOf((random.nextInt((maxNumber - minNumber) + 1) + minNumber));
    }

    public static String generateNewCode() {
        String firstCodePart = generateCodePart(1000, 9999);
        return "You Won\nRs." + firstCodePart;
    }

    public static void hideKeyboard(Activity activity)
    {
        try
        {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Exception e)
        {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }

    public static String encryptMessage(final String message)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {


        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        byte[] clean = message.getBytes();
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        String key = /*getEncryptString()*/"fenerbachemonaco";

        byte[] keyBytes = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);
        return new String(Base64.encode(encryptedIVAndText, Base64.DEFAULT), ASCII);

    }

    public static String encryptStart(String encypt) throws Exception {
        String te = encypt;

        String encrypted = encrypt(te);
        return encrypted;
    }

    private static String encrypt(String inputText) throws Exception {
        String s = "Agentscr";
        byte[] keyValue = s.getBytes("US-ASCII");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyValue);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec(keyValue);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            bout.write(cipher.doFinal(inputText.getBytes("ASCII")));
        } catch (Exception e) {
            System.out.println("Exception .. " + e.getMessage());
        }

        return new String(Base64.encode(bout.toByteArray(), Base64.DEFAULT), "ASCII");

    }

    public static String getDecimelFormate(double amount) {
        DecimalFormat fmt = new DecimalFormat("#,##,##,##,###.00");
        String amt = fmt.format(amount);
        if (amt.substring(0,1).equals(".")){
            amt = "0"+amt;
        }
        return amt;
    }

}
