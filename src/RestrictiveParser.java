
import java.io.File;

public class RestrictiveParser extends LiberalParser {

    @Override
    public String parse(File file) {
        String content = super.parse(file);
        content = content.replaceAll("[\\p{Punct}&&[^-]]", "").toUpperCase();
        return content;
    }
}
