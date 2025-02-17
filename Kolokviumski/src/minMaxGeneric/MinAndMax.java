package minMaxGeneric;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    T max;
    T min;
    private int count;
    private boolean flag;

    public MinMax() {

        this.count = 0;
        this.flag=true;
    }

    void update(T element) {
        if (flag) {
            max = element;
            min = element;
            flag=false;
        }
        if (max.compareTo(element) < 0 ) {
            max = element;
            count++;
        }
         if(min.compareTo(element)>0){
            min=element;
            count++;
        }

    }

    T max() {
        return max;
    }

    T min() {
        return min;
    }

    @Override
    public String toString() {
        return min+" "+max+" "+count+"\n";
        // не разбирам која е улогата на count, што треба да брои??
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
