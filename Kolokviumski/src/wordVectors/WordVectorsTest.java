package wordVectors;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//NE JA RAZBIRAM ZADACATA ZA DA JA RESAM :((

class WordVectors{
    String [] words;
    List<List<Integer>> vectors;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.words = words;
        this.vectors = vectors;
    }


    public void readWords(List<String> wordsList) {

    }

    public List<Integer> slidingWindow(int n) {
        return null;
    }
}

public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}




