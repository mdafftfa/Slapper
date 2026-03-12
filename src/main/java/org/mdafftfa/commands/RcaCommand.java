package org.mdafftfa.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import org.mdafftfa.Slapper;

import java.util.Arrays;

public class RcaCommand extends Command {

    Slapper plugin;
    String prefix;

    public String MSG_NO_PERM = TextFormat.GREEN + "[" + TextFormat.YELLOW + "Slapper" + TextFormat.GREEN + "]" + TextFormat.RED + " You don't have permission+";

    public RcaCommand(Slapper plugin) {
        super("rca", plugin.prefix + "Execute a command as someone else!", "", new String[0]);
        this.plugin = plugin;
        this.prefix = plugin.prefix;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender.hasPermission("rca.command"))) {
            sender.sendMessage(MSG_NO_PERM);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(prefix + " Please enter a player and a command.");
            return false;
        }

        Player player = Server.getInstance().getPlayerExact(args[0]);
        if (player != null) {
            String[] shiftedArgsCommand = Arrays.copyOfRange(args, 1, args.length);
            String command = String.join(" ", shiftedArgsCommand).trim();

            Server.getInstance().executeCommand(player, command);
        } else {
            sender.sendMessage(prefix + " Player not found.");
        }

        return true;
    }
}
