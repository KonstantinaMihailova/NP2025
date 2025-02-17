package mojDDV;


import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",amount));
    }
}
enum TaxType{
    A,
    B,
    V
}

class Item{
    private int price;
    private TaxType type;

    public Item(int price, TaxType type) {
        this.price = price;
        this.type = type;
    }

    public Item(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TaxType getType() {
        return type;
    }

    public void setType(TaxType type) {
        this.type = type;
    }

    public double getCalculatedTax(){
        if(type.equals(TaxType.A))
            return 0.18*price*0.15;
        else if(type.equals(TaxType.B))
            return 0.05*price*0.15;
        else return 0;
    }
}

class Receipt implements Comparable<Receipt>{

    private long id;
    private List<Item> items;

    public Receipt(long id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public Receipt(long id) {
        this.id = id;
        this.items=new ArrayList<>();
    }

    public static Receipt create(String line) throws AmountNotAllowedException {
        String[] parts=line.split("\\s+");
        long id=Long.parseLong(parts[0]);
        List<Item> items=new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i->{
                    if(Character.isDigit(i.charAt(0))){
                        items.add(new Item(Integer.parseInt(i)));
                    }else{
                        items.get(items.size()-1).setType(TaxType.valueOf(i));
                    }
                });

        if(totalAmount(items)>30000){
            throw new AmountNotAllowedException(totalAmount(items));
        }

        return new Receipt(id,items);
    }

    public static int totalAmount(List<Item> items){
        return items.stream().mapToInt(i->i.getPrice()).sum();
    }

    public  int totalAmount(){
        return items.stream().mapToInt(i->i.getPrice()).sum();
    }

    public double taxReturns(){
        return items.stream().mapToDouble(i->i.getCalculatedTax()).sum();
    }

    public long getId() {
        return id;
    }

    @Override
    public int compareTo(Receipt o) {
        return Comparator.comparing(Receipt::taxReturns)
                .thenComparing(Receipt::totalAmount)
                .compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f",id,totalAmount(),taxReturns());
    }
}

class MojDDV{

    private List<Receipt> receipts;

    public MojDDV() {
        this.receipts=new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        receipts=new BufferedReader(new InputStreamReader(in))
                .lines()
                .map(line-> {
                    try {
                        return Receipt.create(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList());


        receipts=receipts.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    public void printStatistics(PrintStream out) {
        PrintWriter printWriter=new PrintWriter(out);
        DoubleSummaryStatistics summaryStatistics=receipts.stream()
                .mapToDouble(Receipt::taxReturns).summaryStatistics();

        printWriter.print(String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f",summaryStatistics.getMin(),summaryStatistics.getMax(),summaryStatistics.getSum(),summaryStatistics.getCount(),summaryStatistics.getAverage()));

        printWriter.flush();
    }

    public void printSorted(PrintStream out) {

        PrintWriter printWriter=new PrintWriter(out);

        receipts.stream().forEach(i->out.println(i.toString()));

        printWriter.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {
        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printSorted(System.out);


        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}
