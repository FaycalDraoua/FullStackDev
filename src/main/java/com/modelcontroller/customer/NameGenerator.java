package com.modelcontroller.customer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    private static List<String> names;

    public static List<String> generateNames() {
        if(names == null) {
            names = new ArrayList<String>();
            try (InputStream inputStreamReader = NameGenerator.class.getResourceAsStream("/first-names.txt");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamReader))) {

                if(inputStreamReader == null)
                    throw new RuntimeException("Unable to find first names.txt file");

                String line;
                while ((line = reader.readLine()) != null) {
                    names.add(line.trim());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return names;
    }

    public static String getName(){
        generateNames();
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }

    
    public static void main(String[] args) {
        System.out.println(NameGenerator.getName());
        System.out.println(NameGenerator.getName());

    }

}
