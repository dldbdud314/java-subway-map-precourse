package subway.domain;

import static subway.domain.Line.validateName;
import static subway.ui.ErrorMessages.DUPLICATED_LINE_NAME;
import static subway.ui.ErrorMessages.DUPLICATED_STATION_NAME;
import static subway.ui.ErrorMessages.NON_EXISTING_LINE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LineRepository {
    private static final List<Line> lines = new ArrayList<>();

    public static List<Domain> lines() {
        return Collections.unmodifiableList(lines);
    }

    public static void addLine(Line line) {
        lines.add(line);
    }

    public static void deleteLineByName(String name) {
        if (isDeletable(name)){
            lines.removeIf(line -> Objects.equals(line.getName(), name));
            return;
        }
        throw new IllegalStateException(NON_EXISTING_LINE.getMessage());
    }

    private static boolean isDeletable(String name) {
        return lines.stream()
                .anyMatch(line -> Objects.equals(line.getName(), name));
    }

    public static String validateLineToAdd(String lineInput){
        validateName(lineInput);
        if (isDuplicatedLine(lineInput)){
            throw new IllegalArgumentException(DUPLICATED_LINE_NAME.getMessage());
        }
        return lineInput;
    }

    public static String validateIfLineExists(String lineInput){
        validateName(lineInput);
        if (!isDuplicatedLine(lineInput)){
            throw new IllegalArgumentException(NON_EXISTING_LINE.getMessage());
        }
        return lineInput;
    }

    private static boolean isDuplicatedLine(String lineInput) {
        return lines.stream()
                .anyMatch(line -> lineInput.equals(line.getName()));
    }

}
