package arena.listenery;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static arena.tovarny.TovarnaNaZbrane.FROZEN_SWORD;
import static arena.tovarny.TovarnaNaZbrane.HEALING_SWORD;


public class ZbraneListener implements Listener {

    @EventHandler
    public static void zbraneEvent(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;
        var player = (Player) event.getDamager();
        var entita = event.getEntity();
        if (!(entita instanceof LivingEntity)) return;
        LivingEntity zijciEntita = (LivingEntity) entita;
        var vecVPraveRuce = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

        //zmrazici mec
        if (FROZEN_SWORD.equals(vecVPraveRuce)) {
            zijciEntita.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 2, true));
        }

        //lecici mec
        if (HEALING_SWORD.equals(vecVPraveRuce)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1, true));
        }
    }
}