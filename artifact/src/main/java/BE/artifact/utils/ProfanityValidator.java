package BE.artifact.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ProfanityValidator implements ConstraintValidator<ProfanityCheck, String> {

    private Pattern pattern;
    private final List<String> badWords = Arrays.stream(SwearWordsEnum.values())
            .map(Enum::name)
            .map(String::toLowerCase)
            .toList();

    @Override
    public void initialize(ProfanityCheck constraintAnnotation) {
        String badWordsRegex = Arrays.stream(SwearWordsEnum.values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .map(word -> "\\b" + Pattern.quote(word) + "\\b")  // Use \b for word boundaries and quote to escape regex characters in words
                .reduce((first, second) -> first + "|" + second)
                .orElse("");
        pattern = Pattern.compile(badWordsRegex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // empty fields are OK; use @NotNull for null checks
        }
        return !pattern.matcher(value).find();
    }
}
