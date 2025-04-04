package arena.monstra;


import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TovarnaNaZombiky {

    public final static String ZOMBIE_NAME = "Zombie";
    public final static String ZOMBIE_CONFIG_FILE_NAME = "zombies.json";

    //mapujeme level zombie na definici zombie
    private MonsterDefinition[]  definiceZombiku;

//  dataAdresar je nazev souboru, ke kteremu chceme cestu
    public TovarnaNaZombiky(File dataAdresar) {        //nahrajeme zombie z jsonu, v serveru, plugins, Arena
        Path pathZombieJson = Path.of(dataAdresar.getPath(), ZOMBIE_CONFIG_FILE_NAME);
        try {
            if (Files.notExists(pathZombieJson)) {
                copyZombieConfigFromJar(pathZombieJson);
            }
            // co ty tri radky presne delaji ?
            String definiceZombikuJson = Files.readString(pathZombieJson);
            Gson gson = new Gson();
            definiceZombiku = gson.fromJson(definiceZombikuJson, MonsterDefinition[].class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyZombieConfigFromJar(Path targetPath) throws IOException
    {

        // co presne to dela ?
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(ZOMBIE_CONFIG_FILE_NAME)) {
            Files.createDirectory(targetPath.getParent());
            Files.copy(is, targetPath);
        }
    }

    public void createZombie(Location location, int level, int pocet) {
        for (int i = 0; i < pocet; i++) {
            Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
            var definiceZombika = definiceZombiku[level - 1];
            var zbran = definiceZombika.weapon;
            var helma = definiceZombika.helmet;
            var brneni = definiceZombika.chestPlate;
            var kalhoty = definiceZombika.legins;
            var boty = definiceZombika.boots;
            var regenerace = definiceZombika.regeneration;
            var speed = definiceZombika.speed;
            var sila = definiceZombika.strength;
            var health = definiceZombika.health;

            if (zbran != null) {
                zombie.getEquipment().setItemInMainHand(new ItemStack(Material.valueOf(zbran)));
            }
            if (helma != null) {
                zombie.getEquipment().setHelmet(new ItemStack(Material.valueOf(helma)));
            }
            if (brneni != null) {
                zombie.getEquipment().setChestplate(new ItemStack(Material.valueOf(brneni)));
            }
            if (kalhoty != null) {
                zombie.getEquipment().setLeggings(new ItemStack(Material.valueOf(kalhoty)));
            }
            if (boty != null) {
                zombie.getEquipment().setBoots(new ItemStack(Material.valueOf(boty)));
            }
            if (regenerace != null) {
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000000, regenerace));
            }
            if (sila != null) {
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1000000000, sila));
            }

            if (speed != null) {
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000000, speed));
            }

            if (health != null) {
                AttributeInstance ai = zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                ai.setBaseValue(health);
                zombie.setHealth(health);
            }
            zombie.setCustomName(ZOMBIE_NAME + " LVL " + level);

        }
    }
}
