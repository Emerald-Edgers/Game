package dk.ee.zg.common.item;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ServiceLoader;

public final class ItemManager {
    /**
     * Set of all loaded items.
     */
    private Set<Item> loadedItems;

    /**
     * List all the players items.
     */
    private List<Item> equippedItems;

    /**
     * {@link ItemManager} instance of weapon manager class,
     * used for making it a singleton pattern.
     */
    private static ItemManager instance;

    private ItemManager() {
        this.loadedItems = findLoadedItems();
        this.equippedItems = new ArrayList<>();
    }

    /**
     * Used for generating a list of random items from the loadedItems Set.
     * @param amount Amount of items to generate.
     * @return A random list of n amount of items.
     */
    public List<Item> createItemSelection(final int amount) {
        List<Item> itemSelection = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            int randomIndex = random.nextInt(loadedItems.size());
            Item randomItem = (Item) loadedItems.toArray()[randomIndex];
            itemSelection.add(randomItem);
        }

        return itemSelection;
    }

    /**
     * @return - returns an instance of ItemManager
     */
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    /**
     * Gets all instances of classes which implements {@link Item}.
     * @return  A List populated with found {@link Item}'s.
     * List can be empty if no {@link Item} implementations are found.
     */
    private Set<Item> findLoadedItems() {
        Set<Item> items = new HashSet<>();
        for (Item item : ServiceLoader.load(Item.class)) {
            items.add(item);
        }

        return items;
    }

    /**
     * Add an item to the list of equippeditems.
     * @param item item to be equipped.
     */
    public void equipItem(final Item item) {
        equippedItems.add(item);
    }

}
