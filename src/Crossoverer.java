import javax.json.*;
import java.io.StringReader;
import java.util.Random;

public class Crossoverer {
    public static void main(String[] args){
        JsonReader jsonReader;
        jsonReader = Json.createReader(new StringReader(args[0]));
        JsonValue exp1 = jsonReader.readValue();
        jsonReader = Json.createReader(new StringReader(args[1]));
        JsonValue exp2 = jsonReader.readValue();
        jsonReader.close();
        JsonValue[] children = new Crossoverer().crossover(exp1, exp2);
        System.out.println("Before Crossover :\n\t" + args[0] + "\n\t" + args[1]);
        System.out.println("\nAfter Crossover :\n\t" + children[0] + "\n\t" + children[1]);
    }

    boolean flag1, flag2;
    int indexTemp;

    protected JsonValue[] crossover(JsonValue exp1, JsonValue exp2){
        flag1 = false;
        flag2 = false;
        return crossHelper(exp1, exp2);
    }

    protected JsonValue[] crossHelper(JsonValue exp1, JsonValue exp2) {
        if (!flag1 && (exp1 instanceof JsonNumber || exp1 instanceof JsonString || new Random().nextInt(100) < 10))
                flag1 = true;
        if (!flag2 && (exp2 instanceof JsonNumber || exp2 instanceof JsonString || new Random().nextInt(100) < 10))
                flag2 = true;

        if (flag1 && flag2)
            return new JsonValue[]{exp2, exp1};
        else if (!flag1 && !flag2) {
            JsonArrayBuilder builder1 = Json.createArrayBuilder((JsonArray) exp1);
            JsonArrayBuilder builder2 = Json.createArrayBuilder((JsonArray) exp2);
            exp1 = selectChild((JsonArray) exp1);
            int i1 = indexTemp;
            exp2 = selectChild((JsonArray) exp2);
            int i2 = indexTemp;
            JsonValue[] subExpArray = crossHelper(exp1, exp2);
            exp1 = builder1.set(i1, subExpArray[0]).build();
            exp2 = builder2.set(i2, subExpArray[1]).build();
            return new JsonValue[]{exp1, exp2};
        } else if (!flag1) {
            JsonArrayBuilder builder1 = Json.createArrayBuilder((JsonArray) exp1);
            exp1 = selectChild((JsonArray) exp1);
            int i1 = indexTemp;
            JsonValue[] subExpArray = crossHelper(exp1, exp2);
            exp1 = builder1.set(i1, subExpArray[0]).build();
            exp2 = subExpArray[1];
            return new JsonValue[]{exp1, exp2};
        } else {
            JsonArrayBuilder builder2 = Json.createArrayBuilder((JsonArray) exp2);
            exp2 = selectChild((JsonArray) exp2);
            int i2 = indexTemp;
            JsonValue[] subExpArray = crossHelper(exp1, exp2);
            exp1 = subExpArray[0];
            exp2 = builder2.set(i2, subExpArray[1]).build();
            return new JsonValue[]{exp1, exp2};
        }
    }

    private JsonValue selectChild(JsonArray exp){
        JsonValue child;
        String operator = exp.getString(0);

        if(operator.equals("e") || operator.equals("sin")
                || operator.equals("cos") || new Random().nextBoolean()){
            indexTemp = 1;
            child = exp.get(1);
        } else {
            indexTemp = 2;
            child = exp.get(2);
        }
        return child;
    }
}
