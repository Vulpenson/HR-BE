package BE.artifact.security;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;
import java.util.logging.Logger;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

//    private static final String AES = "AES/ECB/PKCS5Padding";

    Logger logger = Logger.getLogger(AttributeEncryptor.class.getName());

    @Value("${codingshadows.app.secret}")
    private String secret;

    private Key key;
    private Cipher cipher;

    @PostConstruct
    public void init() throws Exception {
        logger.info("Initializing key and cipher with secret: " + secret);
        this.key = new SecretKeySpec(secret.getBytes(), "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}
