package lineProcessor;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

class Line{
    String line;
    Character c;

    public Line(String line, Character c) {
        this.line = line;
        this.c = c;
    }
    public long getCharacterNumber(char c){

        return line.chars().filter(i->Character.toLowerCase(c)==Character.toLowerCase(i)).count();

    }

    @Override
    public String toString() {
        return line;
    }
}
class LineProcessor{


    public void readLines(InputStream in, OutputStream out, char c) {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));

        List<Line> lines=bufferedReader.lines().map(line->new Line(line,c)).collect(Collectors.toList());

        long maxNumber=lines.stream().mapToLong(i->i.getCharacterNumber(c)).max().orElse(0);
        Line theLine=lines.stream().filter(i->i.getCharacterNumber(c)==maxNumber).collect(Collectors.toList()).getLast();

        PrintWriter printWriter=new PrintWriter(out);

        printWriter.println(theLine);
        printWriter.println();

        printWriter.flush();

    }
}

public class LineProcessorTest {
    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        lineProcessor.readLines(System.in, System.out, 'a');
    }

}
