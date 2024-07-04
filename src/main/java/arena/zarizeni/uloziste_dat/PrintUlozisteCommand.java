package arena.zarizeni.uloziste_dat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrintUlozisteCommand implements CommandExecutor {


    private final Uloziste uloziste;

    public PrintUlozisteCommand(Uloziste uloziste) {
        this.uloziste = uloziste;
    }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        uloziste.print();
        return true;
    }
}
