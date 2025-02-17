package shapesCanvas;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square{
    private int size;

    public Square(int size) {
        this.size = size;
    }

    public int parametar(){
        return 4*size;
    }
}

class Canvas implements Comparable<Canvas>{
    private String id;
    private List<Square> squares;

    public Canvas(String id,List<Square> squares) {
        this.id = id;
        this.squares=squares;
    }

    public void add(int size){
        squares.add(new Square(size));
    }

    public List<Square> getSquares() {
        return squares;
    }

    public static Canvas createCanvas(String line){
        String id=line.split("\\s+")[0];
        List<Square> squares1= Arrays.stream(line.split("\\s+")).skip(1)
                .mapToInt(Integer::parseInt)
                .mapToObj(Square::new)
                .collect(Collectors.toList());

        return new Canvas(id,squares1);
    }

    public int sumOfSquaresParametars(){
        return squares.stream().mapToInt(i->i.parametar()).sum();
    }

    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.sumOfSquaresParametars(),o.sumOfSquaresParametars());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,squares.size(),sumOfSquaresParametars());
    }
}

class ShapesApplication{

    private List<Canvas> canvasList;

    public ShapesApplication() {
        canvasList=new ArrayList<>();
    }

    int readCanvases (InputStream inputStream){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

        canvasList=bufferedReader.lines()
                .map(Canvas::createCanvas)
                .collect(Collectors.toList());

        return canvasList.stream().mapToInt(i->i.getSquares().size()).sum();
    }


    public void printLargestCanvasTo(PrintStream out) {
        PrintWriter printWriter=new PrintWriter(out);

        Canvas canvas=this.canvasList.stream().max(Comparator.naturalOrder()).get();
        printWriter.println(canvas);

        printWriter.flush();


    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
