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

    /**
     * Parses monstats.txt and returns an ArrayList of Monster objects
     * @return ArrayList of Monster objects
     * @throws IOException if the file cannot be read
     */
    public static ArrayList<Monster> MonsterParse () throws IOException {
        //Array list of Monster objects
        ArrayList<Monster> monsters = new ArrayList<>();
        Scanner text = new Scanner(new File(DATA_SET + "/monstats.txt"));

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

    /**
     * Monster class that holds the class, level, type, and treasure class of a monster declared in the file
     */
    public static class Monster {
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

    /**
     * Picks one of the monsters at random from monstats.txt file
     * @return a random monster's name
     * @throws IOException if the file cannot be read
     */
    public static String pickMonster() throws IOException{
        //picks a random monster from the list of monsters
        ArrayList<Monster> monsters = MonsterParse();
        String monsterName;
        Random rnd = new Random();
        int index = rnd.nextInt(monsters.size()); 
        monsterName = monsters.get(index).monsterClass;
        return monsterName;
    }

    /**
     * Fetches the treasure calss of the monster pickd randomly
     * @param monsterName the name of the monster picked by pickMonster() method
     * @return trasure class of the given monster
     * @throws IOException if the file cannot be read
     */
    public static String fetchTreasureClass(String monsterName) throws IOException{
        ArrayList<Monster> monsters = MonsterParse();
        String treasureClass;
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).monsterClass.equals(monsterName)) {
                treasureClass = monsters.get(i).treasureClass;
                return treasureClass;
            }
        }
        return null;
    }

    /**
     * Parses the TreasureClassEx.txt file and returns a map of TC objects
     * @return Map<String, TC> (key: treasureClass, value: TC object)
     * @throws IOException if the file cannot be read
     */
    public static Map<String, TC> TCParse() throws IOException {
        //parses the monster file and returns a list of Monster objects
        //Map (key: treasureClass, value: TC object)
        Map<String, TC> tc = new HashMap<>();
        Scanner text = new Scanner(new File(DATA_SET + "/TreasureClassEx.txt"));

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

    /**
     * TC class that holds the treasure class and its three items declared in the file
     */
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
   
    /**
     * Find the base item from the treasure class by looking up the treasure class in TreasureClassEx.txt
     * @param treasureClass the treasure class of the monster
     * @return the base item dropped by the monster
     * @throws IOException  if the file cannot be read
     */
    public static String generateBaseItem(String treasureClass) throws IOException {
        Map<String, TC> tc = TCParse();
        String item = treasureClass;

        //look up the monster's TC in TreasureClassEx.txt until a base item is found
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

    /**
     * Armor class to hold armor data read from armor.txt
     */
    public static class Armor {
        String name;
        int minac;
        int maxac;

        public Armor(String name, int minac, int maxac) {
            this.name = name;
            this.minac = minac;
            this.maxac = maxac;
        }
    }

    /**
     * Parses the armor file and returns a map of Armor objects 
     * @param filename the path to the armor file 
     * @return Map<String, Armor> (key: armor name, value: Armor object)
     * @throws IOException if the file cannot be read
     */
    public static Map<String, Armor> armorParse(String filename) throws IOException {
        Map<String, Armor> armors = new HashMap<>();
        Scanner text = new Scanner(new File(filename));

        while (text.hasNextLine()) {
            String line = text.nextLine();
            String[] parts = line.split("\t");
            String name = parts[0];
            int minac = Integer.parseInt(parts[1]);
            int maxac = Integer.parseInt(parts[2]);
            Armor armor = new Armor(name, minac, maxac);
            armors.put(name, armor);
        }

        text.close();
        return armors;
    }

    /**
     * Generates defense value ranging from minac to maxac for the given base item
     * @param baseItem the name of the base item 
     * @return a random defense value between minac and maxac
     * @throws IOException if the armor file cannot be read
     */
    public static int generateBaseStats(String baseItem) throws IOException{
        Map<String, Armor> armors = armorParse(DATA_SET + "/armor.txt");
        Armor stats = armors.get(baseItem);
        Random rnd = new Random();
        int defenseValue = rnd.nextInt(stats.maxac - stats.minac + 1) + stats.minac;
        return defenseValue;
    }

    /**
     * Affix class to hold affix data read from MagicPrefix.txt and MagicSuffix.txt
     */
    public static class Affix {
        String name;
        String mod1code;
        int mod1min;
        int mod1max;

        public Affix(String name, String mod1code, int mod1min, int mod1max) {
            this.name = name;
            this.mod1code = mod1code;
            this.mod1min = mod1min;
            this.mod1max = mod1max;
        }
    }

    /**
     * Parses the MagicPrefix.txt and MagicSuffix.txt file and returns an ArrayList of Affix objects
     * @param filename
     * @return
     * @throws IOException
     */
    public static ArrayList<Affix> affixParse(String filename) throws IOException {
        ArrayList<Affix> affixes = new ArrayList<>();
        Scanner text = new Scanner(new File(filename));

        while (text.hasNextLine()) {
            String line = text.nextLine();
            String[] parts = line.split("\t");
            String name = parts[0];
            String mod1code = parts[1];
            int mod1min = Integer.parseInt(parts[2]);
            int mod1max = Integer.parseInt(parts[3]);
            Affix affix = new Affix(name, mod1code, mod1min, mod1max);
            affixes.add(affix);
        }

        text.close();
        return affixes;
    }

    /**
     * AffixSelected class to hold the selected affix data
     */
    public static class AffixSelected {
        boolean isAffix;
        String affixName;
        String affixStats;
        int affixValue;

        public AffixSelected(boolean isAffix, String affixName, String affixStats, int affixValue) {
            this.isAffix = isAffix;
            this.affixName = affixName;
            this.affixStats = affixStats;
            this.affixValue = affixValue;
        }
    }

    /**
     * Helper method for generateAffix to select an affix randomly
     * @param baseItem baseItem found in generateBaseItem method
     * @param affix ArrayList of Affix objects (prefix or suffix) read from the files
     * @param affixName name of the Selected affix
     * @param affixStats stats of the Selected affix
     * @param affixValue value of the Selected affix
     * @return AffixSelected object
     */
    public static AffixSelected affixHelper(String baseItem, ArrayList<Affix> affix, String affixName, String affixStats, int affixValue) {
        Random affixBoolean = new Random();
        boolean isAffix = affixBoolean.nextBoolean();

        if (isAffix) {
            Random affixInt = new Random();
            int affixIndex = affixInt.nextInt(affix.size());
            Affix selectedPrefix = affix.get(affixIndex);
            affixName = selectedPrefix.name;
            affixStats = selectedPrefix.mod1code;

            Random preStats = new Random();
            affixValue = preStats.nextInt(selectedPrefix.mod1max - selectedPrefix.mod1min + 1) + selectedPrefix.mod1min;
        }

        AffixSelected affixSelected = new AffixSelected(isAffix, affixName, affixStats, affixValue);
        return affixSelected;
    }

    /**
     * Generates affixes
     * @param baseItem name of the base item found in generateBaseItem method
     * @throws IOException if the affix files cannot be read
     */
    public static void generateAffix(String baseItem) throws IOException{
        String prefixName = "";
        String suffixName = "";
        String prefixStats = "";
        String suffixStats = "";
        int prefixValue = 0;
        int suffixValue = 0;

        ArrayList<Affix> prefix = affixParse(DATA_SET + "/MagicPrefix.txt");
        ArrayList<Affix> suffix = affixParse(DATA_SET + "/MagicSuffix.txt");

        AffixSelected prefixSelected = affixHelper(baseItem, prefix, prefixName, prefixStats, prefixValue);
        AffixSelected suffixSelected = affixHelper(baseItem, suffix, suffixName, suffixStats, suffixValue);

        String defenseValue = Integer.toString(generateBaseStats(baseItem));

        if (prefixSelected.isAffix) {
            System.out.println(prefixSelected.affixName + " " + baseItem + " " + suffixSelected.affixName);
            System.out.println("Defense: " + defenseValue);
            System.out.println(prefixSelected.affixValue + " " + prefixSelected.affixStats);
            if (suffixSelected.isAffix) {
                System.out.println(suffixSelected.affixValue + " " + suffixSelected.affixStats);
            }
        } else if (suffixSelected.isAffix) {
            System.out.println(baseItem + " " + suffixSelected.affixName);
            System.out.println("Defense: " + defenseValue);
            System.out.println(suffixSelected.affixValue + " " + suffixSelected.affixStats);
        } else {
            System.out.println(baseItem);
            System.out.println("Defense: " + defenseValue);
        }

    }
    

    /** 
     * Main method to run the loot generator program
     * @param args command line arguments
     * @throws IOException if any of the files cannot be read
     */
    public static void main(String[] args) throws IOException{

        boolean playing = true;

        while (playing) {
            String monsterName = pickMonster();
            String treasureClass = fetchTreasureClass(monsterName);
            String baseItem = generateBaseItem(treasureClass);
           
            System.out.println("This program kills monsters and generates loot!");
            System.out.println("Fighting " + monsterName);
            System.out.println("You have slain " + monsterName + "!");
            System.out.println(monsterName + " dropped: ");
            System.out.println();
            generateAffix(baseItem);
            System.out.println();

            System.out.println("Fight again [y/n]?");
            Scanner scanner = new Scanner (System.in);
            String input = scanner.nextLine();
            while (!input.equals("Y") && !input.equals("y") && !input.equals("N") && !input.equals("n")) {
                System.out.println("Fight again [y/n]?");
                input = scanner.nextLine();
            }
            if (input.equals("Y") || input.equals("y")) {
                playing = true;
                System.out.println();
            } else {
                playing = false;
                System.out.println();
                scanner.close();
            }
        }
        
    }
    }

