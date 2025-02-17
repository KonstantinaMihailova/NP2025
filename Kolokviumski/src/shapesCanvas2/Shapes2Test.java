package shapesCanvas2;


import java.io.*;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

enum Type{
    S,C
}

abstract class Shape implements Comparable<Shape>{
    Type type;
    int size;

    public Shape(int size) {
        this.size = size;
    }

    public abstract double area();
}

class Square extends Shape{

    public Square(int size) {
        super(size);
        this.type=Type.S;
    }

    @Override
    public double area() {
        return this.size*this.size;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(this.area(),o.area());
    }
}

class Circle extends Shape{

    public Circle(int size) {
        super(size);
        this.type=Type.C;
    }

    @Override
    public double area() {
        return this.size*this.size*Math.PI;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(this.area(),o.area());
    }
}

class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String id,double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f",id,maxArea));
    }
}

class Canvas implements Comparable<Canvas>{
    private String id;
    private List<Shape> shapes;

    public Canvas(String id,List<Shape> shapes) {
        this.id = id;
        this.shapes=shapes;
    }

    public void add(int size){
        shapes.add(new Square(size));
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public static Canvas createCanvas(String line,double maxArea) throws IrregularCanvasException {

        String[] parts=line.split("\\s+");

        String id=parts[0];
        List<Shape> shapes=new ArrayList<>();

        for (int i = 1; i <parts.length ; i+=2) {
            Type type=Type.valueOf(parts[i]);
            int size=Integer.parseInt(parts[i+1]);
            Shape shape;

            if(type.equals(Type.C))
                shape=new  Circle(size);
            else
                shape= new Square(size);

            if(shape.area()>maxArea){
                throw new IrregularCanvasException(id,maxArea);
            }

            shapes.add(shape);
        }
        return new Canvas(id,shapes);

    }

    public int getTotalCircles(){
        return (int) shapes.stream().filter(i->i.type.equals(Type.C)).count();
    }
    public int getTotalSquares(){
        return (int) shapes.stream().filter(i->i.type.equals(Type.S)).count();
    }

    public double sumOfSquaresParametars(){
        return shapes.stream().mapToDouble(i->i.area()).sum();
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(o.sumOfSquaresParametars(),this.sumOfSquaresParametars());
    }



    @Override
    public String toString() {

        DoubleSummaryStatistics doubleSummaryStatistics=shapes.stream().mapToDouble(Shape::area).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",id,shapes.size(),getTotalCircles(),getTotalSquares(),doubleSummaryStatistics.getMin(),doubleSummaryStatistics.getMax(),doubleSummaryStatistics.getAverage());
    }
}

class ShapesApplication{

    private double maxArea;
    private List<Canvas> canvasList;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    public ShapesApplication() {
        canvasList=new ArrayList<>();
    }

    void readCanvases (InputStream inputStream){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

        this.canvasList=bufferedReader.lines()
                .map(i-> {
                    try {
                        return Canvas.createCanvas(i,this.maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).
                filter(Objects::nonNull).
                collect(Collectors.toList());

    }


    public void printCanvases(PrintStream out) {
        PrintWriter printWriter=new PrintWriter(out);

        this.canvasList.stream().sorted().forEach(printWriter::println);

        printWriter.flush();

    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);

    }
}
