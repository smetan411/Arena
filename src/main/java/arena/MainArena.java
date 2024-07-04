package arena;


import arena.commands.*;
import arena.listenery.*;
import arena.monstra.MonstraStav;
import arena.monstra.TovarnaNaZombiky;
import arena.monstra.VlnyMonster;
import arena.zarizeni.dvere_areny.DvereAreny;
import arena.zarizeni.dvere_areny.DvereArenyCommand;
import arena.zarizeni.dvere_areny.DvereArenyListener;
import arena.zarizeni.dvere_areny.DvereArenyInicializace;
import arena.zarizeni.monster_switch.InicializaceMonsterSwitche;
import arena.zarizeni.monster_switch.MonsterSwitchCommand;
import arena.zarizeni.monster_switch.MonsterSwitchListener;
import arena.zarizeni.monster_switch.ResetSwitchCommand;
import arena.zarizeni.uloziste_dat.Uloziste;
import arena.zarizeni.uloziste_dat.PrintUlozisteCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class MainArena extends JavaPlugin {

    @Override
    public void onEnable() {

        var world = getServer().getWorlds().get(0);
        var uloziste = new Uloziste(world, this);
        var monstraStav = new MonstraStav(world);

        var tovarnaNaZombiky = new TovarnaNaZombiky(getDataFolder());
        var tovarnaNaVlny = new VlnyMonster(tovarnaNaZombiky);

        var dvereAreny = new DvereAreny(this, uloziste);
        var dvereArenyListener = new DvereArenyListener(dvereAreny);
        new DvereArenyInicializace(dvereAreny, uloziste).inicializace();

        var monsterSwitch = new MonsterSwitchListener(this, dvereAreny, tovarnaNaVlny, uloziste, monstraStav);
        new InicializaceMonsterSwitche(uloziste, world, this).inicializace();


        //listenery
        getServer().getPluginManager().registerEvents(monsterSwitch, this);
        getServer().getPluginManager().registerEvents(new OdmenaZaZabitiMonstra(monstraStav, tovarnaNaZombiky), this);
        getServer().getPluginManager().registerEvents(new SmrtMonstra(dvereAreny, monstraStav), this);
        getServer().getPluginManager().registerEvents(new PripojeniRespawn(), this);
        getServer().getPluginManager().registerEvents(dvereArenyListener, this);
        getServer().getPluginManager().registerEvents(new ObchodnikNesmrtelnost(), this);
        getServer().getPluginManager().registerEvents(new ZbraneListener(), this);

        //commandy
        getCommand("+obchodnikZbrane").setExecutor(new ObchodnikZbrane());
        getCommand("+obchodnikJidlo").setExecutor(new ObchodnikJidlo());
        getCommand("+monsterSwitch").setExecutor(new MonsterSwitchCommand());
        getCommand("+resetMonsterSwitch").setExecutor(new ResetSwitchCommand(tovarnaNaVlny));
        getCommand("+dvere").setExecutor(new DvereArenyCommand());
        getCommand("+znicMonstra").setExecutor(new ZabijVsechnaMonstra(monstraStav));
        getCommand("+mecNaObchodnika").setExecutor(new MecNaObchodnika());
        getCommand("+resetHry").setExecutor(new ResetHry(dvereAreny, uloziste));
        getCommand("+printUloziste").setExecutor(new PrintUlozisteCommand(uloziste));
    }
}