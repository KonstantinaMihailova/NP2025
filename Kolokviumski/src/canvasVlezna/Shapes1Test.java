package canvasVlezna;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class Square {
    int size;

    public Square(int size) {
        this.size = size;
    }

    public int getPerimeter(){
        return 4*size;
    }
}

class Canvas {
    String id;
    List<Square> squares;

    public Canvas(String id, List<Square> squares) {
        this.id = id;
        this.squares = squares;
    }

    public int getSquaresPerimeter(){
        return squares.stream().mapToInt(Square::getPerimeter).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,squares.size(),getSquaresPerimeter());
    }
}

class ShapesApplication {
    List<Canvas> canvasList;

    public ShapesApplication() {
        canvasList = new ArrayList<>();
    }

    public int readCanvases(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        bufferedReader.lines().forEach(line -> {
            String[] parts = line.split("\\s+");

            String id = parts[0];
            List<Square> squares = Arrays.stream(parts).skip(1).map(Integer::parseInt)
                    .map(Square::new)
                    .collect(Collectors.toList());

            Canvas canvas = new Canvas(id,squares);
            canvasList.add(canvas);
        });

        return canvasList.stream().mapToInt(i->i.squares.size()).sum();
    }


    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter printWriter=new PrintWriter(out);

        int maxPerimeter=canvasList.stream().mapToInt(Canvas::getSquaresPerimeter).max().getAsInt();
        Canvas theCanvas=canvasList.stream().filter(i->i.getSquaresPerimeter()==maxPerimeter).findFirst().orElse(null);
        if(theCanvas!=null)
            printWriter.println(theCanvas);

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