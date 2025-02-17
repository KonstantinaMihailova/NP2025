package blockContainer;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>{

    Map<Integer,Block<T>> blocks;
    int maxCapacity;
    int numInMap;

    public BlockContainer(int n) {
        blocks=new HashMap<>();
        this.maxCapacity=n;
        this.numInMap=0;
    }


    public void add(T element) {
        if(numInMap==0){
            blocks.put(++numInMap,new Block<>(maxCapacity));
        }

        if(blocks.get(numInMap).getNumOfElements()==maxCapacity){
            blocks.put(++numInMap,new Block<>(maxCapacity));
        }



        blocks.get(numInMap).addElement(element);


    }


    public void remove(T lastInteger) {

        if(blocks.get(numInMap).size==1)
            blocks.remove(numInMap);
        else{

            blocks.get(numInMap).removeLast(lastInteger);
        }
    }

    public void sort() {

        List<T> allElements=new ArrayList<>();

        for(Block<T> block: blocks.values()){
            allElements.addAll(block.elements);
        }

        Collections.sort(allElements);


        blocks.clear();
        numInMap=0;

        for (T element:allElements)
            add(element);
    }

    @Override
    public String toString() {
        return blocks.values().stream().map(Block::toString).collect(Collectors.joining(","));
    }
}

class Block<T extends Comparable<T>>{
    int size;
    Set<T> elements;

    public Block(int size) {
        elements=new TreeSet<>();
        this.size=size;
    }

    public int getNumOfElements(){
        return elements.size();
    }

    public void addElement(T element){
        elements.add(element);
    }

    public void removeLast(T element){
        elements.remove(element);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();

        sb.append("[");
        elements.forEach(i->sb.append(i).append(", "));
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");



        return sb.toString();
    }
}



public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



