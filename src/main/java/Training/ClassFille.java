package Training;

public class ClassFille extends ClassMere{

    /**
     * Methode Redefini
     */
    public void test(){
        System.out.println("test ClassFille");
    }

    /**
     * Methode Surchargee.
     */
    public void test(int a){

    }

    public void useMethodes(){
        ClassMere.staticMethod(); // ou simplement staticMethod()
        this.test(); // ou simplement test()
    }

    public static void main(String[] args) {
        ClassFille cf = new ClassFille();
        cf.test();

        cf.useMethodes();
    }
}
