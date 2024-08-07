package arena.zarizeni.uloziste_dat;

import com.google.common.primitives.Ints;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public final class Uloziste {

    private final World world;
    // uloziste - druh bloku
    private TileState uloziste;
    private final Plugin plugin;

    public Uloziste(World world, Plugin plugin) {
        this.world = world;
        this.plugin = plugin;

        Block block0 = world.getBlockAt(0, 0, 0);

        if (!(block0.getState() instanceof Chest)) {
            block0.setType(Material.CHEST);
        }
        uloziste = (TileState) block0.getState();
    }

    // ulozime pro klic key seznam lokaci locations
    public void smazCeleUloziste() {
        Block block0 = world.getBlockAt(0, 0, 0);
        block0.setType(Material.AIR);
        block0.setType(Material.CHEST);
        uloziste = (TileState) block0.getState();
    }

     public void uloz(String key, Set<Location> locations) {
        PersistentDataContainer container = uloziste.getPersistentDataContainer();
        NamespacedKey namespaceKey = new NamespacedKey(plugin, key);
        List<Integer> seznam = new ArrayList<>();//Lists.newArrayList();
        for (Location location : locations) {
            seznam.add(location.getBlockX());
            seznam.add(location.getBlockY());
            seznam.add(location.getBlockZ());
        }
        container.set(namespaceKey, PersistentDataType.INTEGER_ARRAY, Ints.toArray(seznam));
        uloziste.update(true, false);
    }

    public Set<Location> nacti(String key) {
        PersistentDataContainer container = uloziste.getPersistentDataContainer();
        int[] souradnice = container.get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER_ARRAY);
        Set<Location> locations = new HashSet<>();
        if (souradnice == null) return locations;
        for (int i = 0; i < souradnice.length; i= i + 3) {
            locations.add(new Location(world, souradnice[i], souradnice[i+1], souradnice[i+2]));
        }
        return locations;
    }

    public void print() {
        PersistentDataContainer container = uloziste.getPersistentDataContainer();
        Set<NamespacedKey> keys = container.getKeys();
        for (var key : keys) {
            int[] souradnice = container.get(key, PersistentDataType.INTEGER_ARRAY);
            Set<Location> locations = new HashSet<>();
            if (souradnice == null) {
                plugin.getLogger().info("Key: " + key.getKey());
                plugin.getLogger().info("Empty");
                return;
            }
            for (int i = 0; i < souradnice.length; i = i + 3) {
                locations.add(new Location(world, souradnice[i], souradnice[i + 1], souradnice[i + 2]));
            }
            plugin.getLogger().info("Key: " + key);
            plugin.getLogger().info(locations.stream()
                    .map(loc -> "(" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ")")
                    .toList()
                    .toString()
            );
        }
    }

    public void pridej(String key, Location location) {
        Set<Location> locations = nacti(key);
        locations.add(location);
        uloz(key, locations);
    }

    public void odeber(String key, Location location) {
        Set<Location> locations = nacti(key);
        locations.remove(location);
        uloz(key, locations);
    }
}
