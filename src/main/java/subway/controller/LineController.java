package subway.controller;

import static subway.InputReader.getUserInput;
import static subway.ui.ErrorMessages.INVALID_MAIN_CHOICE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import subway.domain.Domain;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.ui.InputView;
import subway.ui.OutputView;

public class LineController implements Controller {
    private static final String LINE = "노선";
    private static final String INBOUND = "상행";
    private static final String OUTBOUND = "하행";
    private static final String CREATE = "1";
    private static final String DELETE = "2";
    private static final String GET_LIST = "3";
    private static final String BACK = "B";

    private final InputView inputView;
    private final OutputView outputView;
    private final Map<String, Runnable> commandMap = new HashMap<>();


    public LineController() {
        inputView = new InputView();
        outputView = new OutputView();
        mapInit();
    }

    private void mapInit() {
        commandMap.put(CREATE, this::createLine);
        commandMap.put(DELETE, this::deleteLine);
        commandMap.put(GET_LIST, this::getLinesList);
        commandMap.put(BACK, this::goBack);
    }

    @Override
    public void execute() {
        inputView.printFeaturesMenu(LINE);
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

    private void createLine() {
        String lineName = getLineInput();
        Station inboundLastStation = getInboundLastStationInput();
        Station outboundLastStation = getOutboundLastStationInput();
        LineRepository.addLine(new Line(lineName, inboundLastStation, outboundLastStation));
        outputView.printCreationResult(LINE);
    }

    private String getLineInput() {
        inputView.printCreationChoiceOpening(LINE);
        try {
            String line = getUserInput();
            return LineRepository.validateLineToAdd(line);
        } catch (IllegalArgumentException exception) {
            outputView.printErrorMessage(exception.getMessage());
            return getLineInput();
        }
    }

    private Station getInboundLastStationInput() {
        inputView.printLastStationChoiceOpening(INBOUND);
        try {
            String inboundLast = getUserInput();
            return StationRepository.validateStation(new Station(inboundLast));
        } catch (Exception exception) {
            outputView.printErrorMessage(exception.getMessage());
            return getInboundLastStationInput();
        }
    }

    private Station getOutboundLastStationInput() {
        inputView.printLastStationChoiceOpening(OUTBOUND);
        try {
            String outboundLast = getUserInput();
            return StationRepository.validateStation(new Station(outboundLast));
        } catch (Exception exception) {
            outputView.printErrorMessage(exception.getMessage());
            return getOutboundLastStationInput();
        }
    }

    private void deleteLine() {
        inputView.printDeleteChoiceOpening(LINE);
        try {
            String lineName = getUserInput();
            LineRepository.deleteLineByName(lineName);
            outputView.printDeletionResult(LINE);
        } catch (IllegalStateException exception) {
            outputView.printErrorMessage(exception.getMessage());
            deleteLine();
        }
    }

    private void getLinesList() {
        inputView.printListOpening(LINE);
        List<Domain> lines = LineRepository.lines();
        outputView.printDomainList(lines);
    }

    private void goBack() {
        return;
    }

    private void throwException() {
        throw new IllegalArgumentException(INVALID_MAIN_CHOICE.getMessage());
    }

}
