package arena.commands;

import arena.zarizeni.dvere_areny.DvereAreny;
import arena.zarizeni.uloziste_dat.Uloziste;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static arena.zarizeni.dvere_areny.DvereAreny.JMENO_DVERI_DO_ARENY;
import static arena.zarizeni.monster_switch.MonsterSwitchCommand.MONSTER_SWITCH_NAME;

public class ResetHry implements CommandExecutor {

    private final DvereAreny dvereAreny;
    private final Uloziste uloziste;

    public ResetHry(DvereAreny dvereAreny, Uloziste uloziste) {
        this.dvereAreny = dvereAreny;
        this.uloziste = uloziste;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        var player = (Player) commandSender;
        if (player.isOp()) {
            dvereAreny.clear();
            uloziste.smazCeleUloziste();
        }
        return true;
    }
}