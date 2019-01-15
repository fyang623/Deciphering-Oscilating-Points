import javax.json.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class Calculator {
    public static void main(String[] args){
        try {
            JsonArray data = Json.createReader(new BufferedReader(new FileReader(args[0]))).readArray();
            JsonArray exp = Json.createReader(new StringReader(args[1])).readArray();
            Calculator calculator = new Calculator();
            double error = calculator.computeError(data, exp);
            System.out.println(error);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static double computeFitness(JsonArray data, JsonValue exp){
        int height = getHeight(exp);
        double error = computeError(data, exp);
        return Initializer.MaxHeight > height ? error : error * Math.pow(2.0, height - Initializer.MaxHeight);
    }

    protected static double computeError(JsonArray data, JsonValue exp){
        double error = 0;
        for(int i = 0; i < data.size(); i++)
            error += Math.pow(eval(exp, i) - ((JsonNumber) data.get(i)).doubleValue(), 2);
        return error;
    }

    public static int getHeight(JsonValue exp){
        if(exp instanceof JsonNumber || exp instanceof JsonString)
            return 0;

        String operator = ((JsonString) ((JsonArray) exp).get(0)).getString();
        switch(operator){
            case "+": case "-": case "*":
                return 1 + Math.max(getHeight(((JsonArray) exp).get(1)), getHeight(((JsonArray) exp).get(2)));
            default: return 1 + getHeight(((JsonArray) exp).get(1));
        }
    }

    private static double eval(JsonValue exp, int x){
        if(exp instanceof JsonNumber)
            return ((JsonNumber) exp).doubleValue();
        else if(exp instanceof JsonString)
            return x;

        String operator = ((JsonArray) exp).getString(0);

        if(operator.equals("e") || operator.equals("sin") || operator.equals("cos")) {
            double child = eval(((JsonArray) exp).get(1), x);
            switch (operator) {
                case "e": return Math.exp(child);
                case "sin": return Math.sin(child);
                case "cos": return Math.cos(child);
            }
        } else {
            double leftChild = eval(((JsonArray) exp).get(1), x);
            double rightChild = eval(((JsonArray) exp).get(2), x);
            switch (operator){
                case "+": return leftChild + rightChild;
                case "-": return leftChild - rightChild;
                case "*": return leftChild * rightChild;
            }
        }
        return 0;
    }
}
