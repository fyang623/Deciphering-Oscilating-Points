import javax.json.*;

public class Cleaner {

    public JsonValue cleanUp(JsonValue exp) {
        if (exp instanceof JsonNumber || exp instanceof JsonString) return exp;

        JsonArrayBuilder builder = Json.createArrayBuilder((JsonArray) exp);
        String operator = ((JsonArray) exp).getString(0);

        if (operator.equals("e") || operator.equals("sin") || operator.equals("cos")) {
            JsonValue child = cleanUp(((JsonArray) exp).get(1));
            if (child instanceof JsonNumber) {
                switch (operator) {
                    case "e":
                        return Json.createValue(Math.exp(((JsonNumber) child).doubleValue() % 20));
                    case "sin":
                        return Json.createValue(Math.sin(((JsonNumber) child).doubleValue()));
                    case "cos":
                        return Json.createValue(Math.cos(((JsonNumber) child).doubleValue()));
                }
            } else return builder.set(1, child).build();
        } else {
            JsonValue leftChild = cleanUp(((JsonArray) exp).get(1));
            JsonValue rightChild = cleanUp(((JsonArray) exp).get(2));
            if (leftChild instanceof JsonNumber && rightChild instanceof JsonNumber) {
                switch (operator) {
                    case "+":
                        return Json.createValue(((JsonNumber) leftChild).doubleValue() + ((JsonNumber) rightChild).doubleValue());
                    case "-":
                        return Json.createValue(((JsonNumber) leftChild).doubleValue() - ((JsonNumber) rightChild).doubleValue());
                    case "*":
                        return Json.createValue(((JsonNumber) leftChild).doubleValue() * ((JsonNumber) rightChild).doubleValue());
                }
            } else return builder.set(1, leftChild).set(2, rightChild).build();
        }
        return null;
    }
}
