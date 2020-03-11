package groovycalamari.exchangeratesbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.MessageComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class AmountAtDateHandler extends AmountHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AmountAtDateHandler.class);

    private static final Pattern pattern;

    static {
        String patternString = "^(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])) (\\d+(\\.)?(\\d{0,2})?)$";
        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    public AmountAtDateHandler(MessageComposer messageComposer,
                               ChatBotMessageParser messageParser,
                               ObjectMapper objectMapper,
                               UserRepository userRepository) {
        super(messageComposer, messageParser, objectMapper, userRepository);
    }

    @Override
    public boolean matches(@NonNull @NotBlank String text) {
        return pattern.matcher(text).matches();
    }

    protected Optional<BigDecimal> parseAmount(String text) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        if (matcher.groupCount() >= 4) {
            String grp = null;
            try {
                grp = matcher.group(4);
                return Optional.of(new BigDecimal(grp));
            } catch(NumberFormatException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("number format exception for {}", grp);
                }
            }
        }
        return Optional.empty();
    }

    protected Optional<String> parseDateStr(String text) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        if (matcher.groupCount() >= 1) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    protected Optional<String> parseUriAtSettings(Settings settings, String text) {
        return parseDateStr(text).map(datStr -> atDateUri(datStr, settings.getBase(), settings.getTarget()));

    }
}