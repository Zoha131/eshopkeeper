package converter;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateConverter extends StringConverter<LocalDate> {
    String pattern = "dd-MM-yyyy";
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);


    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    @Override public String toString(LocalDate date) {
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

    @Override public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }
}
