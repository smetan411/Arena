package arena.zarizeni.dvere_areny;

import arena.zarizeni.uloziste_dat.Uloziste;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import java.util.List;
import java.util.Set;

public final class DvereAreny {
    public static final String JMENO_DVERI_DO_ARENY = "Dvere_do_areny";
    private final Set<Block> dvere = Sets.newHashSet();
    private final Plugin plugin;
    private final Uloziste uloziste;

    public DvereAreny(Plugin plugin, Uloziste uloziste) {
        this.plugin = plugin;
        this.uloziste = uloziste;
    }

    private void zmenStavDveri(boolean stav) {
        for (Block block : dvere) {
            Door dvere = (Door) block.getBlockData();
            dvere.setOpen(stav);
            block.setBlockData(dvere);
        }
    }

    public void otevriDvere() {
        zmenStavDveri(true);
    }

    public void zavriDvere() {
        zmenStavDveri(false);
    }

    public void pridejDvere(Block block) {
        block.setMetadata(JMENO_DVERI_DO_ARENY, new FixedMetadataValue(plugin, true));
        dvere.add(block);
        uloziste.pridej(JMENO_DVERI_DO_ARENY, block.getLocation());
    }

    public void odeberDvere(Block block) {
        uloziste.odeber(JMENO_DVERI_DO_ARENY, block.getLocation());
        dvere.remove(block);
        block.removeMetadata(JMENO_DVERI_DO_ARENY, plugin);
    }

    public void clear() {
        dvere.clear();
    }
}
