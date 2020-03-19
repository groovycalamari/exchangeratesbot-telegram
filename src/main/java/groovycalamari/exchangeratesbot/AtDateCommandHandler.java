package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeRates;
import io.exchangeratesapi.ExchangeRatesApi;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MatcherCommandHandler;
import io.micronaut.bots.core.MessageComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class AtDateCommandHandler extends ExchangeRatesApiCommandHandler implements MatcherCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AtDateCommandHandler.class);

    private static final Pattern pattern;

    static {
        String patternString = "^(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])) ("+ String.join("|",Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+") ("+ String.join("|",Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+")$";
        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    protected AtDateCommandHandler(MessageComposer messageComposer,
                                   ChatBotMessageParser messageParser,
                                   ExchangeRatesApi exchangeRatesApi) {
        super(messageComposer, messageParser, exchangeRatesApi);
    }

    @Override
    public boolean matches(@NonNull @NotBlank String text) {
        return pattern.matcher(text).matches();
    }

    @Override
    protected Optional<ExchangeRates> exchangeRates(@NonNull ChatBotMessageReceive update, @NonNull String text) {
        Matcher matcher = pattern.matcher(text.toUpperCase());
        matcher.find();
        if (matcher.groupCount() >= 5) {
            String dateStr = matcher.group(1);
            Currency base = Currency.valueOf(matcher.group(4));
            Currency symbol = Currency.valueOf(matcher.group(5));
            return atDate(dateStr, base, symbol);

        }
        return Optional.empty();
    }
}
