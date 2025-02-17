package triple;


import java.util.Scanner;


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

class Triple<T extends Number & Comparable<T>> {

T num1;
T num2;
T num3;

public Triple(T num1, T num2, T num3) {
    this.num1 = num1;
    this.num2 = num2;
    this.num3 = num3;
}

double max() {
    double tmp = Math.max(num1.doubleValue(), num2.doubleValue());
    return Math.max(tmp, num3.doubleValue());
}

double avarage() {
    double sum = num1.doubleValue() + num2.doubleValue() + num3.doubleValue();
    return sum / 3;
}

void sort() {
    double maximum = max();
    T tmp1 = null;
    T tmp2 = null;
    T tmp3 = null;

    if (num1.doubleValue() == maximum) {

        tmp3=num1;
        if (num3.compareTo(num2)>0){
            tmp2=num3;
            tmp1=num2;
        }else{
            tmp1=num3;
            tmp2=num2;
        }
    }

    if (num2.doubleValue() == maximum) {
        tmp3=num2;
        if (num1.compareTo(num3)>0){
            tmp2=num1;
            tmp1=num3;
        }else{
            tmp2=num3;
            tmp1=num1;
        }
    }

    if (num3.doubleValue() == maximum) {


        tmp3=num3;
        if (num1.compareTo(num2)>0){
            tmp2=num1;
            tmp1=num2;
        }else{
            tmp2=num2;
            tmp1=num1;
        }
    }

    num1=tmp1;
    num2=tmp2;
    num3=tmp3;

}

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",num1.doubleValue(),num2.doubleValue(),num3.doubleValue());
    }
}


