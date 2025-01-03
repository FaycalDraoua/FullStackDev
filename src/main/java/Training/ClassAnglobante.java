package Training;

public class ClassAnglobante {

    private  int id;
    private  static String name;


    public int getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    // je peux meme utiliser la class interne dans la class anglobante
    public ClassInterne getClassInterne() {
        return new ClassInterne("f",10);
    }

    /// Une class static interne elle peux acceder uniquement au attribut et au methode static de la class Anglobante.

    public static class ClassInterne{
        private int idInterne;

        public ClassInterne(String namee, int idInterne) {
            name = namee;
            this.idInterne = idInterne;
        }
    }
}
