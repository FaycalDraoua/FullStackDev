package Training;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImplLambda {


    public static void testLambda(LambdaExp lambda) {
        lambda.run("Alice",10);
    }

    public static int ExecuteOperation(int a, int b,MathOperat mathOperat) {
        return mathOperat.operation(a,b);
    }

    public static void main(String[] args) {
        // Implémentation de LambdaExp avec une expression lambda
        LambdaExp lambdaExpression = (name, age) -> {
            System.out.println("Nom : " + name);
            System.out.println("Âge : " + age);
        };

        // Appel de testLambda en passant l'expression lambda
        testLambda(lambdaExpression);

        // Ou directement en ligne sans variable intermédiaire
        testLambda((name, age) -> {
            System.out.println("Nom : " + name);
            System.out.println("Âge : " + age);
        });

        System.out.println(ExecuteOperation(5,6,(a,b)-> a+b));

        Consumer<String> consumer;


    }
}
