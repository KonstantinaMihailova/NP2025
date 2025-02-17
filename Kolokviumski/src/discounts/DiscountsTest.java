package discounts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



class Product implements Comparable<Product>{
    Integer regularPrice;
    Integer discountedPrice;

    public Product(Integer discountedPrice, Integer regularPrice) {
        this.regularPrice = regularPrice;
        this.discountedPrice = discountedPrice;
    }

    public int getDiscountPercent(){
        return (int)(100-((float) discountedPrice/regularPrice*100));
    }

    public int getAbsoluteDiscount(){
        return regularPrice - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("\n%2d%% %d/%d", getDiscountPercent(), discountedPrice, regularPrice);
    }

    @Override
    public int compareTo(Product o) {
        int res = Integer.compare(o.getDiscountPercent(), this.getDiscountPercent());
        if(res == 0)
            return Integer.compare(o.getAbsoluteDiscount(), this.getAbsoluteDiscount());
        return res;
    }
}

class Store{
    static final Comparator<Store> DISCOUNT_COMPARATOR = Comparator.comparing(Store::getTotalAverageDiscount).reversed()
            .thenComparing(s -> s.name);
    static final Comparator<Store> ABSOLUTE_DISCOUNT_COMPARATOR = Comparator.comparing(Store::getTotalAbsoluteDiscount)
            .thenComparing(s -> s.name);

    String name;
    List<Product> products;

    public Store(String name) {
        this.name = name;
        products = new ArrayList<>();
    }

    public void addProduct(Product p){
        products.add(p);
    }

    public double getTotalAverageDiscount(){
        return  products.stream().mapToInt(Product::getDiscountPercent).average().orElse(0.0);
    }

    public int getTotalAbsoluteDiscount(){
        return products.stream().mapToInt(Product::getAbsoluteDiscount).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d", name, getTotalAverageDiscount(), getTotalAbsoluteDiscount()));
        products.stream().sorted().forEach(sb::append);
        return sb.toString();
    }
}

class Discounts{
    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());

        for(String line : lines){
            String[] parts = line.split("\\s+");
            String name = parts[0];
            Store store = new Store(name);
            stores.add(store);

            List<String> allProducts = Arrays.stream(parts).skip(1).collect(Collectors.toList());
            for(String product : allProducts){
                String[] prices = product.split(":");
                Product product1 = new Product(Integer.parseInt(prices[0]), Integer.parseInt(prices[1]));
                store.addProduct(product1);
            }
        }
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream().sorted(Store.DISCOUNT_COMPARATOR).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream().sorted(Store.ABSOLUTE_DISCOUNT_COMPARATOR).limit(3).collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
