package org.mdafftfa;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.mdafftfa.entities.SlapperEntity;

public class SlapperCommand extends Command {

    Slapper plugin;

    public SlapperCommand(Slapper plugin) {
        super("slapper", "Slapper command", "Usage: /slapper <spawn:remove:id>", new String[] {""});
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        switch (args[0]) {
            case "spawn":
                sender.sendMessage("SlapperEntity spawned successfully!");
                Location location = sender.getLocation();

                CompoundTag nbt = Entity.getDefaultNBT(location);

                IChunk chunk = sender.getLocation().getChunk();
                SlapperEntity entity = new SlapperEntity(chunk, nbt);
                entity.spawnToAll();
                break;
            default:
                break;
        }
        return true;
    }
}
