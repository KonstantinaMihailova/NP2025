package tripleWithList;

import java.util.*;
import java.util.stream.Collectors;


public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple

class Triple<T extends Number>{

    List<T> numbers;

    public Triple(T n1,T n2,T n3 ) {
        this.numbers=new ArrayList<>();
        numbers.add(n1);
        numbers.add(n2);
        numbers.add(n3);
    }

    double max(){
        return numbers.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
    }

    double avarage(){
        DoubleSummaryStatistics doubleSummaryStatistics=numbers.stream().mapToDouble(Number::doubleValue).summaryStatistics();

        return doubleSummaryStatistics.getAverage();
    }

    void sort(){
        numbers=numbers.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",numbers.get(0).doubleValue(),numbers.get(1).doubleValue(),numbers.get(2).doubleValue());
    }
}



