package arena.zarizeni.dvere_areny;

import arena.zarizeni.uloziste_dat.Uloziste;
import org.bukkit.Location;

import java.util.Set;

import static arena.zarizeni.dvere_areny.DvereAreny.JMENO_DVERI_DO_ARENY;


public class DvereArenyInicializace {
    private final Uloziste uloziste;
    private final DvereAreny dvereAreny;

    public DvereArenyInicializace(DvereAreny dvereAreny, Uloziste uloziste) {
        this.uloziste = uloziste;
        this.dvereAreny = dvereAreny;
    }

    public void inicializace() {
        Set<Location> lokaceBlokuDveri = uloziste.nacti(JMENO_DVERI_DO_ARENY);
        for (Location lokace : lokaceBlokuDveri) {
            dvereAreny.pridejDvere(lokace.getBlock());
        }
    }

}
