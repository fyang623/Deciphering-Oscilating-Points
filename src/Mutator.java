import javax.json.*;
import java.io.StringReader;
import java.util.Random;

public class Mutator {
    public static void main(String[] args){
        JsonReader jsonReader = Json.createReader(new StringReader(args[0]));
        JsonValue JsonExp = jsonReader.readValue();
        jsonReader.close();
        System.out.println("Before Mutation :\t" + JsonExp);
        System.out.println("After Mutation :\t" + new Cleaner().cleanUp(new Mutator().mutate(JsonExp)));
    }

    protected JsonValue mutate(JsonValue JsonExp){
        return mutate(JsonExp, Initializer.MaxHeight);
    }

    protected JsonValue mutate(JsonValue exp, int maxHeight){
        Initializer initializer = new Initializer();
        Random random = new Random();

        if(exp instanceof JsonNumber || exp instanceof JsonString || maxHeight == 0 ||random.nextInt(100) < 10)
            //System.out.println("old subtree :\t" + exp);
            return initializer.generateExp(maxHeight);
            //System.out.println("new subtree :\t" + exp);

        JsonArrayBuilder builder = Json.createArrayBuilder((JsonArray) exp);
        String operator = ((JsonArray) exp).getString(0);

        if(operator.equals("e") || operator.equals("sin") || operator.equals("cos") || random.nextBoolean()){
            JsonValue child = mutate(((JsonArray) exp).get(1), maxHeight - 1);
            return builder.set(1, child).build();
        } else {
            JsonValue child = mutate(((JsonArray) exp).get(2), maxHeight - 1);
            return builder.set(2, child).build();
        }
    }
}
