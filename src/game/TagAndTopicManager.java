package game;

import game.characters.Attribute;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import shared.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagAndTopicManager {
    private static HashSet<String> loggedTags = new HashSet<>();
    private static Map<String, Set<Attribute>> attributesByCategory = new HashMap<>();
    private static Map<String, Attribute> attributesByName = new HashMap<>();

    private static String buildingAttributeCategory; // used during initialization to track the name of the attribute being initialized.

    public static void initTopicsAndAttributes() {
        Util.parseFileAndDoEachLine(GameMaster.getResourceFolder() + "attributes.txt",
                TagAndTopicManager::makeAttributeCategory);
    }

    private static void makeAttributeCategory(String line) {
        if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
            buildingAttributeCategory = line.substring(line.indexOf(' ') + 1);
            attributesByCategory.put(buildingAttributeCategory, new HashSet<>());
        } else { // no entry-start, so this line is an attribute in the above category
            String[] parts = line.split(" ", 3);
            Attribute toAdd = new Attribute(parts[0].toUpperCase(), parts[1], parts.length > 2 ? parts[2] : "");
            attributesByName.put(toAdd.name, toAdd);
            attributesByCategory.get(buildingAttributeCategory).add(toAdd);
        }
    }

    public static void logTag(String tag) {
        loggedTags.add(tag);
        System.out.println("Logged " + tag);
    }

    public static boolean hasLog(String tag) {
        return loggedTags.contains(tag);
    }

    public static String lookup(String reference) {
        System.out.println("Lookup request for " + reference);
        if (reference.contains(".")) {
            String[] sections = reference.split("\\.");
            for (String s : sections) System.out.println(s);

            if (CharacterManager.contains(sections[0])) { // We are looking up a field on a character, e.g. ~PRESTON.GENDER, ~MARIS.RACE
                GameCharacter referencedCharacter = CharacterManager.get(sections[0]);
                System.out.println(referencedCharacter);
                if (sections[1].equals("NAME")) {
                    return CharacterManager.getKnownName(referencedCharacter.label);
                }
                return getAttributeDescription(referencedCharacter.getAttributeForCategory(sections[1]));
            }
            throw new RuntimeException("Failed lookup for reference:" + reference);
        }
        return reference;
    }

    public static String getAttributeDescription(String attribute) {
        Attribute a = attributesByName.get(attribute);
        return a.hasDescription() ? a.description : a.adjective;
    }

    public static String getAttributeAdjective(String attribute) {
        return attributesByName.get(attribute).adjective;
    }
}
