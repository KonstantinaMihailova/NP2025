package quizProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;


class NotValidInputException extends Exception{
    public NotValidInputException(String message) {
        super(message);
    }
}
class LineProcessor{
    String index;
    String[] correctAnswers;
    String[] studentAnswers;

    public LineProcessor(String line) {
        String[] parts= line.split(";");

        index=parts[0];


        String [] correctAnswersParts=parts[1].split(",");

        correctAnswers=new String[correctAnswersParts.length];

        IntStream.range(0, correctAnswersParts.length).forEach(i -> correctAnswers[i] = correctAnswersParts[i]);

        String [] studentsAnswersParts=parts[2].split(",");

        studentAnswers=new String[studentsAnswersParts.length];

        IntStream.range(0, studentsAnswersParts.length).forEach(i -> studentAnswers[i] = studentsAnswersParts[i]);

    }

    public double getResult() throws NotValidInputException {
        if(correctAnswers.length!= studentAnswers.length){
            throw new NotValidInputException("A quiz must have same number of correct and selected answers");
        }

        double result=0.0;

        for (int i = 0; i < correctAnswers.length; i++) {
            if(correctAnswers[i].equals(studentAnswers[i]))
                result+=1;
            else
                result-=0.25;
        }

        return result;
    }
}

class QuizProcessor{
    static Map<String, Double> processAnswers(InputStream is){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
        Map<String,Double> mapWithStudents=new TreeMap<>();

        bufferedReader.lines().map(LineProcessor::new)
                .forEach(i-> {
                    try {
                        mapWithStudents.put(i.index,i.getResult());
                    } catch (NotValidInputException e) {
                        System.out.println(e.getMessage());
                    }
                });

        return mapWithStudents;

    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));


    }
}
