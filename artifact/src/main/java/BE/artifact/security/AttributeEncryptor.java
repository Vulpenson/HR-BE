package BE.artifact.security;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final Logger logger = Logger.getLogger(AttributeEncryptor.class.getName());

    @Value("${codingshadows.app.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() throws Exception {
        logger.info("Initializing key with secret: " + secret);
        this.key = new SecretKeySpec(secret.getBytes(), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            logger.severe("Error while encrypting attribute: " + e.getMessage());
            throw new IllegalStateException("Error while encrypting attribute: " + e.getMessage());
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            logger.severe("Error while decrypting attribute: " + e.getMessage());
            throw new IllegalStateException("Error while decrypting attribute: " + e.getMessage());
        }
    }
}
