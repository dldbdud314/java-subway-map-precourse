package subway.controller;

import static subway.InputReader.getUserInput;
import static subway.ui.ErrorMessages.INVALID_MAIN_CHOICE;

import java.util.HashMap;
import java.util.Map;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.ui.InputView;
import subway.ui.OutputView;

public class SectionController implements Controller {
    private static final String SECTION = "구간";
    private static final String INSERT = "1";
    private static final String DELETE = "2";
    private static final String BACK = "B";

    private final InputView inputView;
    private final OutputView outputView;
    private final Map<String, Runnable> commandMap = new HashMap<>();

    public SectionController() {
        inputView = new InputView();
        outputView = new OutputView();
        mapInit();
    }

    private void mapInit() {
        commandMap.put(INSERT, this::insertStationToLine);
        commandMap.put(DELETE, this::deleteStationFromLine);
        commandMap.put(BACK, this::goBack);
    }

    @Override
    public void execute() {
        inputView.printSectionsMenu(SECTION);
        runCommand();
    }

    private void runCommand() {
        inputView.printUsersChoiceOpening();
        try {
            String choice = getUserInput();
            commandMap.getOrDefault(choice, this::throwException).run();
        } catch (IllegalArgumentException exception) {
            outputView.printErrorMessage(exception.getMessage());
            runCommand();
        }
    }

    private void insertStationToLine() {
        Line line = getLineInput();
        Station station = getStationInput(line);
    }

    private Line getLineInput() {
        inputView.printSectionsInputOpening("노선");
        try {
            String line = getUserInput();
            return LineRepository.getExistingLine(line);
        } catch (IllegalArgumentException exception) {
            outputView.printErrorMessage(exception.getMessage());
            return getLineInput();
        }
    }

    private Station getStationInput(Line line){
        inputView.printSectionsInputOpening("역");
        try {
            String station = getUserInput();
            Station existingStation = StationRepository.validateStation(new Station(station));
            LineRepository.validateIfStationExistsInLine(line, existingStation); // 해당 노선에 역이 존재하는 경우 exception 터트려야
            return existingStation;
        } catch (Exception exception) {
            outputView.printErrorMessage(exception.getMessage());
            return getStationInput(line);
        }
    }

    private void deleteStationFromLine() {

    }

    private void goBack() {
        return;
    }

    private void throwException() {
        throw new IllegalArgumentException(INVALID_MAIN_CHOICE.getMessage());
    }
}
