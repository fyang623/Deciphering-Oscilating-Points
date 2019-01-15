import javax.json.*;
import java.util.Random;

public class Initializer {
    public static void main(String[] args){
        JsonValue exp = new Initializer().initialize();
        System.out.println(new Cleaner().cleanUp(exp));
    }

    static int StandardDeviation = 100;
    static int MaxHeight = 5;

    protected JsonValue initialize(){
        return generateExp(MaxHeight);
    }

    protected JsonValue generateExp(int maxHeight){
        if(maxHeight == 0){
            Random random = new Random();
            return random.nextBoolean() == true ? 
                   Json.createValue(random.nextGaussian() * StandardDeviation) :
                   Json.createValue("x");
        }

        JsonValue node = generateNode();
        if(node instanceof JsonNumber) return node;

        String content = ((JsonString) node).getString();
        if(content.equals("x")) return node;

        JsonArrayBuilder builder = Json.createArrayBuilder();
        if(content.equals("+") || content.equals("-") || content.equals("*")) {
            builder.add(node)
                   .add(generateExp(maxHeight - 1))
                   .add(generateExp(maxHeight - 1));
            return builder.build();
        } else {
            builder.add(node)
                   .add(generateExp(maxHeight - 1));
            return builder.build();
        }
    }

    private JsonValue generateNode(){
        Random random = new Random();
        switch (new Random().nextInt(8)) {
            case 0: return Json.createValue(random.nextGaussian() * StandardDeviation);
            case 1: return Json.createValue("x");
            case 2: return Json.createValue("+");
            case 3: return Json.createValue("-");
            case 4: return Json.createValue("*");
            case 5: return Json.createValue("e");
            case 6: return Json.createValue("sin");
            case 7: return Json.createValue("cos");
        }
        return null;
    }
}
