import javax.json.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;
//import java.io.StringReader;

public class Optimizer {
    public static void main(String[] args) {
        try{
            //String data = "[6, 15, 28, 45, 66, 91, 120, 153, 190]"; //2x^2 + 7x + 6
            //JsonArray JsonData = Json.createReader(new StringReader(data)).readArray();
            String data = args[0];
            JsonArray JsonData = Json.createReader(new BufferedReader(new FileReader(data))).readArray();
            Optimizer optimizer = new Optimizer();
            Calculator calculator = new Calculator();
            JsonValue function = optimizer.optimize(JsonData);
            System.out.println("\n\nBest Scoring Function:\t" + function);
            System.out.println("Sum of Squared Errors:\t" + calculator.computeError(JsonData, function));
            System.out.println("Fitness:\t\t\t\t\t" + calculator.computeFitness(JsonData, function));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private int NumTree = 10000;
    private int NumGen = 1000;
    private Crossoverer crossoverer = new Crossoverer();
    private Initializer initializer = new Initializer();
    private Calculator calculator = new Calculator();
    private Mutator mutator = new Mutator();
    private Cleaner cleaner = new Cleaner();
    private Random random = new Random();

    private class ExpTree implements Comparable<ExpTree> {
        JsonValue exp;
        double fitness;

        public ExpTree(JsonValue JsonExp, JsonArray JsonData){
            exp = cleaner.cleanUp(JsonExp);
            fitness = calculator.computeFitness(JsonData, exp);
        }

        public int compareTo(ExpTree t){
            return Double.compare(fitness, t.fitness);
        }
    }

    private JsonValue optimize(JsonArray data){
        ExpTree[] currGen = new ExpTree[NumTree];
        ExpTree[] nextGen = new ExpTree[NumTree];
        ExpTree[] temp;
        JsonValue children[];
        int i1, i2;

        for(int i = 0; i < NumTree; i++)
            currGen[i] = new ExpTree(initializer.initialize(), data);

        for(int gen = 0; gen < NumGen; gen++){
            Arrays.sort(currGen);

            if(gen % 10 == 0) {
                System.out.println("\n" + gen + "th Generation:");
                System.out.println("\tBest Scoring Function\t" + currGen[0].exp);
                System.out.println("\tSum of Squared Errors\t" + calculator.computeError(data, currGen[0].exp));
                System.out.println("\tFitness:\t" + currGen[0].fitness);
            }

            for(int i = 0; i < NumTree; i += 2) {
                i1 = (int) (random.nextDouble() * NumTree % NumTree / 2);
                i2 = (int) (random.nextDouble() * NumTree % NumTree / 2);
                children = crossoverer.crossover(currGen[i1].exp, currGen[i2].exp);
                if(random.nextInt(100) < 5)
                    children[0] = mutator.mutate(children[0]);
                if(random.nextInt(100) < 5)
                    children[1] = mutator.mutate(children[1]);
                nextGen[i] = new ExpTree(children[0], data);
                nextGen[i + 1] = new ExpTree(children[1], data);
            }
            temp = currGen;
            currGen = nextGen;
            nextGen = temp;
        }
        Arrays.sort(currGen);
        return currGen[0].exp;
    }
}
