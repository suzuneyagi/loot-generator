package edu.grinnell.csc207.lootgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootGenerator {
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";

    public static ArrayList<Monster> MonsterParse (String filename) throws IOException {
        //parses the monster file and returns a list of Monster objects
        //Array list of Monster objects
        ArrayList<Monster> monsters = new ArrayList<>();
        Scanner text = new Scanner(new File(filename));

        while (text.hasNextLine()) {
            String line = text.nextLine();
            String[] parts = line.split("\t");
            String monsterClass = parts[0];
            String type = parts[1];
            int level = Integer.parseInt(parts[2]);
            String treasureClass = parts[3];
            Monster monster = new Monster(monsterClass, level, type, treasureClass);
            monsters.add(monster);
        }

        text.close();
        return monsters;
    }

    public static class Monster {
        //holds the class, level, type, and treasure class of a monster declared in the file

        String monsterClass;
        String type;
        int level;
        String treasureClass;

        public Monster(String monsterClass, int level, String type, String treasureClass) {
            this.monsterClass = monsterClass;
            this.level = level;
            this.type = type;
            this.treasureClass = treasureClass;
        }
    }

    public static String pickMonster() throws IOException{
        //picks a random monster from the list of monsters
        ArrayList<Monster> monsters = MonsterParse(DATA_SET + "/monstats.txt");
        String monsterName;
        Random rnd = new Random();
        int index = rnd.nextInt(monsters.size()); 
        monsterName = monsters.get(index).monsterClass;
        return monsterName;
    }

    public static String fetchTreasureClass(String monsterName) throws IOException{
        ArrayList<Monster> monsters = MonsterParse(DATA_SET + "/monstats.txt");
        String treasureClass;
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).monsterClass.equals(monsterName)) {
                treasureClass = monsters.get(i).treasureClass;
                return treasureClass;
            }
        }
        return null;
    }

    public static Map<String, TC> TCParse (String filename) throws IOException {
        //parses the monster file and returns a list of Monster objects
        //Map (key: treasureClass, value: TC object)
        Map<String, TC> tc = new HashMap<>();
        Scanner text = new Scanner(new File(filename));

        while (text.hasNextLine()) {
            String line = text.nextLine();
            String[] parts = line.split("\t");
            String treasureClass = parts[0];
            String item1 = parts[1];
            String item2 = parts[2];
            String item3 = parts[3];
            TC tcInstance = new TC (treasureClass, item1, item2, item3);
            tc.put(treasureClass, tcInstance);
        }

        text.close();
        return tc;
    }

    public static class TC {
        String treasureClass;
        String item1;
        String item2;
        String item3;

        public TC(String treasureClass, String item1, String item2, String item3) {
            this.treasureClass = treasureClass;
            this.item1 = item1;
            this.item2 = item2;
            this.item3 = item3;
        }
    }
   
    public static String generateBaseItem(String treasureClass) throws IOException {
        Map<String, TC> tc = TCParse(DATA_SET + "/TreasureClassEx.txt");
        String item = treasureClass;

        //look up the monster's TC in TreasureClassEx.txt.
        while (tc.containsKey(item)) {
            TC currentTC = tc.get(item);
            Random rnd = new Random();
            int itemIndex = rnd.nextInt(3) + 1; // Generate random number (1-3)
            switch (itemIndex) {
                case 1:
                    item = currentTC.item1;
                    break;
                case 2:
                    item = currentTC.item2;
                    break;
                case 3:
                    item = currentTC.item3;
                    break;
            }

        }
        return item;
    }
    
    public static void main(String[] args) throws IOException{
        System.out.println("This program kills monsters and generates loot!");
        // TOOD: Implement me!
        String monsterName = pickMonster();
        String treasureClass = fetchTreasureClass(monsterName);
        System.out.println("Monster name is : " + monsterName);
        System.out.println("Monster tc is : " + treasureClass);
        System.out.println("Dropped item is : " + generateBaseItem(treasureClass));
    }
    }

