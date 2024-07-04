package arena.zarizeni.monster_switch;

import arena.monstra.MonstraStav;
import arena.monstra.VlnyMonster;
import arena.zarizeni.dvere_areny.DvereAreny;
import arena.zarizeni.uloziste_dat.Uloziste;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static arena.zarizeni.monster_switch.MonsterSwitchCommand.MONSTER_SWITCH_NAME;


public final class MonsterSwitchListener implements Listener {

    private final Plugin plugin;
    private final DvereAreny dvere;
    private final VlnyMonster vlnyMonster;
    private final Uloziste uloziste;
    private MonstraStav monstraStav;

    public MonsterSwitchListener(Plugin plugin, DvereAreny dvere, VlnyMonster vlnyMonster, Uloziste uloziste, MonstraStav monstraStav) {
        this.plugin = plugin;
        this.dvere = dvere;
        this.vlnyMonster = vlnyMonster;
        this.uloziste = uloziste;
        this.monstraStav = monstraStav;
    }

    @EventHandler
    public void spawnMonsters(PlayerInteractEvent e) {
        //udalost PlayerInteractEvent se pusti i pri zniceni
        // a polozeni bloku, timto zabranime vygenerovani monster po zniceni
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        // zabranime pokracovani pri polozeni blocku
        if (e.getClickedBlock().getType() != Material.LEVER) return;

        if (!monstraStav.jsouMonstraMrtva()) return;

        var block = e.getClickedBlock();
        if (block == null) return;

        List<MetadataValue> blockMetadata = block.getMetadata(MONSTER_SWITCH_NAME);
        if (!blockMetadata.isEmpty()) {
            dvere.zavriDvere();
            vlnyMonster.dalsiVlna(block.getLocation());
        }
    }

    @EventHandler
    public void placeMonsterSwitch(BlockPlaceEvent e) {
        if (MONSTER_SWITCH_NAME.equals(e.getItemInHand().getItemMeta().getDisplayName())) {
            var block = e.getBlockPlaced();
            block.setMetadata(MONSTER_SWITCH_NAME, new FixedMetadataValue(plugin, true));
            uloziste.pridej(MONSTER_SWITCH_NAME, block.getLocation());
        }
    }

    @EventHandler
    public void znicMonsterSwitch(BlockBreakEvent e) {
        var blockMetadata = e.getBlock().getMetadata(MONSTER_SWITCH_NAME);
        if (!blockMetadata.isEmpty()) {
            e.getBlock().removeMetadata(MONSTER_SWITCH_NAME, plugin);
            uloziste.odeber(MONSTER_SWITCH_NAME, e.getBlock().getLocation());
            opravUloziste();
        }
    }

    private void opravUloziste() {
        var lokaceSpinacu = Sets.newHashSet(uloziste.nacti(MONSTER_SWITCH_NAME));
        for (var lokaceSpinace : lokaceSpinacu) {
            var metadataSpinace = lokaceSpinace.getBlock().getMetadata(MONSTER_SWITCH_NAME);
            if (metadataSpinace.isEmpty()) {
                lokaceSpinacu.remove(lokaceSpinace);
            }
        }
        uloziste.uloz(MONSTER_SWITCH_NAME, lokaceSpinacu);
    }
}
