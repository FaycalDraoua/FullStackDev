package Training;

import jakarta.el.LambdaExpression;

public class Main {

    public static void main(String[] args) {

        ClassAnglobante ca = new ClassAnglobante();

        /*

        Si c'etait une class interne non static voici la syntaxe :

            ClassAnglobante.ClassInterne ci = ca.new ClassInterne("did it", 10);

         */

        /*

        Si c'etait une class interne static voici la syntaxe :

            ClassAnglobante.ClassInterne ci = new ClassAnglobante.ClassInterne("did it", 12);

         */


    }
}
