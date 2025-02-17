package raceMarathon_vlezna2024;

import java.io.*;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Ucesnik implements Comparable<Ucesnik> {
    Integer id;
    LocalTime start;
    LocalTime end;

    public Ucesnik(Integer id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public LocalTime getTotalTime() {

        Duration duration=Duration.between(start,end);

        return LocalTime.of(duration.toHoursPart(),duration.toMinutesPart(),duration.toSecondsPart());
    }

    @Override
    public int compareTo(Ucesnik o) {
        return this.getTotalTime().compareTo(o.getTotalTime());
    }

    @Override
    public String toString() {
        return id + " " + getTotalTime();
    }
}

class TeamRace {
    static List<Ucesnik> ucesnikList;

    public static void findBestTeam(InputStream in, PrintStream out) throws IOException {

        ucesnikList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        PrintWriter printWriter = new PrintWriter(out);


        List<String> lines = bufferedReader.lines().collect(Collectors.toList());

        for (String line : lines) {
            String[] parts = line.split("\\s+");

            Integer id = Integer.parseInt(parts[0]);
            LocalTime start = LocalTime.parse(parts[1]);
            LocalTime end = LocalTime.parse(parts[2]);

            Ucesnik ucesnik = new Ucesnik(id, start, end);
            ucesnikList.add(ucesnik);

        }

        List<Ucesnik> best4 = ucesnikList.stream().sorted().limit(4).collect(Collectors.toList());
        best4.forEach(printWriter::println);

        long totalSeconds=best4.stream().mapToLong(i->i.getTotalTime().toSecondOfDay()).sum();

        LocalTime totalTime = LocalTime.ofSecondOfDay(totalSeconds);
        printWriter.println(totalTime);
        printWriter.flush();

    }
}

public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
