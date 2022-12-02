package subway.domain;

public class Station {
    private String name;

    public Station(String name) { this.name = name;
    }

    public String getName() {
        return name;
    }

    // 추가 기능 구현
    public boolean nameMatches(Station station){
        return this.name.equals(station.getName());
    }
}
