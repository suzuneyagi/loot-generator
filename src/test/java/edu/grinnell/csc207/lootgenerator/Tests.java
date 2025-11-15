package edu.grinnell.csc207.lootgenerator;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

public class Tests {
    @Test
    public void testLoot() throws Exception {
        String monsterName = LootGenerator.pickMonster();
        String treasureClass = LootGenerator.fetchTreasureClass(monsterName);

        assertEquals(monsterName, "Hell Bovine");
        assertEquals(treasureClass, "Cow (H)");
       
    }
}
