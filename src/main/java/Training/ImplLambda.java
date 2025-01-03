package Training;

public class ImplLambda {

    public static void main(String[] args) {
        LambdaExp lambdaExp = (name,age)->{
            System.out.println("je suis une lambda expression");
        };

        lambdaExp.run("",0);
    }
}
