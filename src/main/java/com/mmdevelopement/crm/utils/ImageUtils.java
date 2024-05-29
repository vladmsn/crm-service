package com.mmdevelopement.crm.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class ImageUtils {

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static byte[] decodeImage(String image) {
        return decoder.decode(image);
    }

    public static String encodeImage(byte[] image) {
        return encoder.encodeToString(image);
    }
}
