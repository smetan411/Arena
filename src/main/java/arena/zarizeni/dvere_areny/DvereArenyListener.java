package arena.zarizeni.dvere_areny;

import arena.zarizeni.uloziste_dat.Uloziste;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static arena.zarizeni.dvere_areny.DvereAreny.JMENO_DVERI_DO_ARENY;

public final class DvereArenyListener implements Listener {

    private DvereAreny dvereAreny;

    public DvereArenyListener(DvereAreny dvereAreny) {

        this.dvereAreny = dvereAreny;
    }

    /* Rusi otevirani dveri, aby se oteviraly pouze v zavislosti na monstrech v arene */
    @EventHandler
    public void neotevriDvere(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (block == null || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        var blockMetadata = block.getMetadata(JMENO_DVERI_DO_ARENY);
        if (!blockMetadata.isEmpty()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void polozDvere(BlockPlaceEvent event) {
        if (JMENO_DVERI_DO_ARENY.equals(event.getItemInHand().getItemMeta().getDisplayName())) {
            Block dolniCastDveri = event.getBlockPlaced();
            Block horniCastDveri = dolniCastDveri.getWorld().getBlockAt(dolniCastDveri.getLocation().add(0, 1, 0));
            dvereAreny.pridejDvere(dolniCastDveri);
            dvereAreny.pridejDvere(horniCastDveri);
            dvereAreny.otevriDvere();
        }
    }

    @EventHandler
    public void rozbijDvere(BlockBreakEvent event) {
        List<MetadataValue> meta = event.getBlock().getMetadata(JMENO_DVERI_DO_ARENY);
        if (!meta.isEmpty()) {
            var znicenaCast = event.getBlock();
            var blokNadZnicenime = znicenaCast.getWorld().getBlockAt(znicenaCast.getLocation().add(0, 1, 0));
            var blokPodZnicenime = znicenaCast.getWorld().getBlockAt(znicenaCast.getLocation().add(0, -1, 0));
            var zbylaCast = blokNadZnicenime.getMetadata(JMENO_DVERI_DO_ARENY).isEmpty() ? blokPodZnicenime : blokNadZnicenime;
            dvereAreny.odeberDvere(znicenaCast);
            dvereAreny.odeberDvere(zbylaCast);
        }
    }
}
