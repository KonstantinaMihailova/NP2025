package canvas;


import java.util.*;
import java.util.stream.Collectors;

enum Color {
    RED, GREEN, BLUE
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.println(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.println(canvas);
            }

        }
    }
}

interface Scalable{
    void scale(float scaleFactor);
}

interface Stackable{
    float weight();
}

abstract class Shape implements Scalable,Stackable{
    String id;
    Color color;
    String type;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public void scale(float scaleFactor) {

    }

    @Override
    public float weight() {
        return 0;
    }

    public String getId() {
        return id;
    }


}

class Circle extends Shape {
    float radius;


    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
        this.type = "Circle";
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius = radius * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f", id, color, weight());
    }


}

class Rectangle extends Shape{
    float width;
    float height;


    public Rectangle(String id, Color color, float width,float height) {
        super(id, color);
        this.width = width;
        this.height=height;
        this.type="Rectangle";
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.height=height*scaleFactor;
        this.width=width*scaleFactor;

    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",id,color,weight());
    }


}



class Canvas {
    List<Shape> shapes;

    public Canvas(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public Canvas() {
        this.shapes=new ArrayList<>();
    }


    public void scale(String id, float scaleFactor) {



        Shape s=shapes.stream().filter(i->i.id.equals(id)).collect(Collectors.toList()).get(0);

        shapes.remove(s);
        s.scale(scaleFactor);


        if(s.type.equals("Circle")){
            Circle circle=(Circle) s;
            this.add(circle.id,circle.color,circle.radius);
        }else{
            Rectangle rectangle=(Rectangle) s;
            this.add(rectangle.id,rectangle.color,rectangle.width,rectangle.height);
        }


    }




    void add(String id, Color color, float radius){
        Circle circle=new Circle(id,color,radius);
        for (int i = 0; i < shapes.size(); i++) {
            if(shapes.get(i).weight()< circle.weight()){
                shapes.add(i,circle);
                return;
            }
        }
        shapes.add(circle);
    }

    void add(String id, Color color, float width, float height){
        Rectangle rectangle=new Rectangle(id,color,width,height);
        for (int i = 0; i < shapes.size(); i++) {
            if(shapes.get(i).weight()< rectangle.weight()){
                shapes.add(i,rectangle);
                return;
            }
        }

        shapes.add(rectangle);
    }


    @Override
    public String toString() {
        return shapes.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}