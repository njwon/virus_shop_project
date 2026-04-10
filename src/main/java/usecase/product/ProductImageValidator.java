package usecase.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProductImageValidator {
    public static final Set<String> ALLOWED_EXTENSIONS = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp"))
    );
    public static final Set<String> ALLOWED_MIME_TYPES = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp"))
    );
    private static final byte[][] IMAGE_SIGNATURES = {
        {(byte)0xFF, (byte)0xD8, (byte)0xFF},
        {(byte)0x89, 0x50, 0x4E, 0x47},
        {0x47, 0x49, 0x46, 0x38},
        {0x52, 0x49, 0x46, 0x46},
    };

    public static boolean isValidSignature(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[8];
            if (fis.read(header) < 4) return false;
            for (byte[] sig : IMAGE_SIGNATURES) {
                boolean match = true;
                for (int i = 0; i < sig.length; i++) {
                    if (header[i] != sig[i]) { match = false; break; }
                }
                if (match) return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
