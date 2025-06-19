package com.modelcontroller;

import com.modelcontroller.customer.Customer;
import com.modelcontroller.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.*;

@SpringBootApplication
public class Main {


//    private CustomerService customerService;
//    private static List<Customer> customers;
//
//    static{
//        customers = Arrays.asList(
//                new Customer(1,"Alex","Alex@gmail.com",30),
//                new Customer(2,"Morad","Morad@gmail.com",25)
//        );
//
//    };


    public static void main(String[] args) {
//        System.out.println(customers);
        ConfigurableApplicationContext applicationContext =  SpringApplication.run(Main.class, args);

        /// Afficher tous les Bean(instances) creer par Spring Boot
        printBean(applicationContext);
    }


    /*
        si vous remaquer ici... customerRepository a ete injecter sans meme d'utiliser un constructeur ou
        un @Autowired. Cela fonctionne car :
            * Spring Boot permet l’injection par paramètre de méthode dans
        les méthodes annotées avec @Bean
            * C’est un mécanisme qui ne repose pas sur les constructeurs mais sur la capacité de Spring à
        injecter des dépendances dans les méthodes de configuration.
     */
    @Bean("COMMANDElINE BEEEEEAN")
    CommandLineRunner runner (CustomerRepository customerRepository, Foo foo) {
//        return args -> {
//            List<Customer> customers = Arrays.asList(
//                    new Customer(NameGenerator.getName(),NameGenerator.getName()+"@gmail.com",30),
//                    new Customer(NameGenerator.getName(),NameGenerator.getName()+"@gmail.com",25));
//            customerRepository.saveAll(customers);
//
//        };
        Random random = new Random();
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            Customer customer = new Customer(firstName+" "+lastName,firstName+'.'+lastName+"@gmail.com", random.nextInt(100-20+1)+20);
            customerRepository.save(customer);
        };
    }


    /*
     * Avec l'annotaion @Bean je viens de creer une instance de la class Foo avec name=bar.
      * je peux dorenavant utiliser ce Bean(intance) n'importe ou(voir la class FooService ou j'ai pu utiliser ce Bean)
      * je peux aussi nome ce bean en utilisant cette syntaxe :
            @Bean("Foo")

     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // pour dire que c'est un bean de tyoe Singleton. voir la documentation Fichier Spring Boot Doc.
    public Foo getFoo() {
        return new Foo("bar");
    }

    record Foo(String name){}

    public static void printBean(ConfigurableApplicationContext ctx){
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for(String bdef : beanDefinitionNames){
            System.out.println(bdef);
        }
    }
//
//    static class Customer{
//        private Integer id;
//        private String name;
//        private String email;
//        private Integer age;
//
//        public Customer(Integer id, String name, String email, Integer age) {
//            this.id = id;
//            this.name = name;
//            this.email = email;
//            this.age = age;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public void setAge(Integer age) {
//            this.age = age;
//        }
//
//        public Integer getId() {
//            return id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public Integer getAge() {
//            return age;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (o == null || getClass() != o.getClass()) return false;
//            Customer personne = (Customer) o;
//            return Objects.equals(id, personne.id) && Objects.equals(name, personne.name) && Objects.equals(email, personne.email) && Objects.equals(age, personne.age);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(id, name, email, age);
//        }
//
//        @Override
//        public String toString() {
//            return "Customer{" +
//                    "id=" + id +
//                    ", name='" + name + '\'' +
//                    ", email='" + email + '\'' +
//                    ", age=" + age +
//                    '}';
//        }
//    }
//
//    @Autowired
//    public Main(CustomerService customerService) {
//        this.customerService = customerService;
//    }
//
//    /*
//    @RequestMapping(
//            value = "api/v1/customer",
//            method = GetMapping
//    )*/
//    //Equivalent de ...
//    @GetMapping("api/v1/customers")
//    public List<Customer> getAllCustomers() {
//        return customers;
//    }
//
//    @GetMapping("api/v1/customers/{customerId}")
//    public Customer getCustomerById(@PathVariable("customerId") Integer id)
//    {
//        return customers.stream().
//                filter(customer -> customer.getId().equals(id)).
//                findFirst().
//                orElseThrow(() -> new IllegalArgumentException("Customer not found"));
//    }









 /* Partie Une 1 ,
  de la video 0 a video 41
  */
////    @GetMapping("/greet")
////    public String greet(){
////        return "Hello";
////    }
////
////    @GetMapping("/record")
////    public GreeResponse1 greetRrspons(){
////        return new GreeResponse1("Hello");
////    }
////
////    record GreeResponse1(String name){}
////
////    record Personne(String name, int age){}
////
////    record GreetRespons(String greet,Personne personne, List<String> langage){}
////
////    @GetMapping("/greet2")
////    public GreetRespons greet2(@RequestParam(value = "name", required = false) String name){
////        String nameGreet = name==null || name.isBlank() ? "false" : "hello "+name;
////        return new GreetRespons(nameGreet,
////                                new Personne("faycal",28),
////                                Arrays.asList("Java", "js"));

}
