package canvas2Kolokvium;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super(message);
    }
}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}

class User  {

    final static Comparator<User> USER_COMPARATOR=Comparator.comparing(User::getShapesListSize).reversed()
            .thenComparing(User::getSumAreaOfShapes).reversed();

    String id;
    List<Shape> shapesCreatedByMe;
    int flag = 0;

    public User(String id) {
        this.id = id;
        shapesCreatedByMe = new ArrayList<>();
    }

    public int getShapesListSize(){
        return shapesCreatedByMe.size();
    }

    public void addShape(Shape shape) {
        shapesCreatedByMe.add(shape);
    }

    public double getSumAreaOfShapes() {
        return shapesCreatedByMe.stream().mapToDouble(Shape::getArea).sum();
    }


//    @Override
//    public int compareTo(User o) {
//        int res = Integer.compare(this.shapesCreatedByMe.size(), o.shapesCreatedByMe.size());
//
//        if (res == 0)
//            return Double.compare(this.getSumAreaOfShapes(), o.getSumAreaOfShapes());
//
//        return res;
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        shapesCreatedByMe.stream().sorted(Shape.PERIMETAR_COMPARATOR)
                .forEach(i -> stringBuilder.append(i).append("\n"));

        return stringBuilder.toString().trim();
    }
}

interface Shape extends Comparable<Shape> {

    final static Comparator<Shape> PERIMETAR_COMPARATOR = Comparator.comparing(Shape::getPerimeter);


    public double getArea();

    public double getPerimeter();

    public void scale(double v);


    @Override
    public default int compareTo(Shape o) {
        return Double.compare(this.getArea(), o.getArea());
    }


}

class Circle implements Shape {
    User user;
    double size;


    public Circle(User user, double size) {
        this.user = user;
        this.size = size;
    }

    @Override
    public double getArea() {
        return size * size * Math.PI;
    }


    @Override
    public int hashCode() {
        return Objects.hash(user, size);
    }

    @Override
    public double getPerimeter() {
        return 2 * size * Math.PI;
    }

    @Override
    public void scale(double v) {
        size = size * v;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", size, getArea(), getPerimeter());
    }

}

class Rectangle implements Shape {
    User user;
    double side1;
    double side2;

    public Rectangle(User user, double side1, double side2) {
        this.user = user;
        this.side1 = side1;
        this.side2 = side2;
    }


    @Override
    public int hashCode() {
        return Objects.hash(user, side1, side2);
    }

    @Override
    public double getArea() {
        return side1 * side2;
    }

    @Override
    public double getPerimeter() {
        return 2 * side1 + 2 * side2;
    }

    @Override
    public void scale(double v) {
        side1 = side1 * v;
        side2 = side2 * v;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", side1, side2, getArea(), getPerimeter());
    }
}

class Square implements Shape {
    User user;
    double size;


    @Override
    public int hashCode() {
        return Objects.hash(user, size);
    }

    public Square(User user, double size) {
        this.user = user;
        this.size = size;
    }

    @Override
    public double getArea() {
        return size * size;
    }

    @Override
    public double getPerimeter() {
        return 4 * size;
    }

    @Override
    public void scale(double v) {
        size = size * v;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", size, getArea(), getPerimeter());
    }
}


class Canvas {
    Set<Shape> shapes;
    List<User> allUsers;


    public Canvas() {
        shapes = new TreeSet<>();
        allUsers = new ArrayList<>();
    }

    void readShapes(InputStream is) throws InvalidDimensionException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        List<String> lines = bufferedReader.lines().collect(Collectors.toList());

        for (String line : lines) {
            try {

                String[] parts = line.split("\\s+");

                double size = Double.parseDouble(parts[2]);

                if (size == 0.0)
                    throw new InvalidDimensionException("Dimension 0 is not allowed!");


                String id = parts[1];
                if (!id.chars().allMatch(Character::isLetterOrDigit) || id.length() != 6)
                    throw new InvalidIDException("ID " + id + " is not valid");

                User user = allUsers.stream().filter(i -> i.id.equals(id))
                        .findFirst()
                        .orElse(null);

                if (user == null) {
                    user = new User(id);
                    allUsers.add(user);
                }


                if (line.startsWith("1")) {

                    Circle circle = new Circle(user, size);
                    shapes.add(circle);
                    user.addShape(circle);

                }

                if (line.startsWith("2")) {

                    Square square = new Square(user, size);
                    shapes.add(square);
                    user.addShape(square);


                }

                if (line.startsWith("3")) {

                    double side2 = Double.parseDouble(parts[3]);

                    if (side2 == 0.0)
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");


                    Rectangle rectangle = new Rectangle(user, size, side2);
                    shapes.add(rectangle);
                    user.addShape(rectangle);

                }
            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
            }

        }
    }


    public void scaleShapes(String number, double v) {
        User theUser = allUsers.stream().filter(i -> i.id.equals(number)).findFirst().orElse(null);

        if (theUser != null) {

            shapes.stream()
                    .filter(shape -> theUser.shapesCreatedByMe.contains(shape))
                    .forEach(shape -> shape.scale(v));
        }

    }

    public void printAllShapes(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        shapes.forEach(printWriter::println);
        printWriter.flush();
    }

    public void printByUserId(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        allUsers.stream().sorted(User.USER_COMPARATOR.reversed()).forEach(i -> {
            printWriter.println("Shapes of user: " + i.id);
            printWriter.println(i);

        });
        printWriter.flush();
    }

    public void statistics(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);

        DoubleSummaryStatistics doubleSummaryStatistics = shapes.stream()
                .mapToDouble(i -> i.getArea())
                .summaryStatistics();

        printWriter.println(String.format("count: %d", doubleSummaryStatistics.getCount()));
        printWriter.println(String.format("sum: %.2f", doubleSummaryStatistics.getSum()));
        printWriter.println(String.format("min: %.2f", doubleSummaryStatistics.getMin()));
        printWriter.println(String.format("average: %.2f", doubleSummaryStatistics.getAverage()));
        printWriter.println(String.format("max: %.2f", doubleSummaryStatistics.getMax()));

        printWriter.flush();

    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidDimensionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}


