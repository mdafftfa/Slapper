package org.mdafftfa;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import org.mdafftfa.entity.SlapperEntity;
import org.mdafftfa.entity.SlapperHuman;
import org.mdafftfa.entity.SlapperVillager;

import java.util.*;

public class SlapperCommand extends Command {

    Slapper plugin;

    public SlapperCommand(Slapper plugin) {
        super("slapper", "Slapper command", "Usage: /slapper <spawn:remove:id>", new String[] {""});
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        switch (args[0]) {
            case "clear":
                for (Level world : Server.getInstance().getLevels().values()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof SlapperEntity || entity instanceof SlapperHuman) {
                            entity.close();
                        }
                    }
                }
                break;
            case "spawn":

                String selectedEntity = null;

                for (String entityType : plugin.ENTITY_TYPES.keySet()) {
                    if (entityType.toLowerCase(Locale.ROOT).equalsIgnoreCase(args[1].toLowerCase())) {
                        selectedEntity = entityType;
                    }
                }

                if (selectedEntity == null) {
                    sender.sendMessage("Invalid entity type!");
                    return true;
                }

                try {
                    Location location = sender.getLocation();
                    CompoundTag nbt = Entity.getDefaultNBT(location);
                    IChunk chunk = sender.getLocation().getChunk();

                    Class<? extends SlapperInterface> entityClass = plugin.ENTITY_TYPES.get(selectedEntity);
                    SlapperInterface entity = entityClass
                            .getDeclaredConstructor(IChunk.class, CompoundTag.class)
                            .newInstance(chunk, nbt);

                    if (entity instanceof SlapperEntity) {
                        ((SlapperEntity) entity).setNameTag(sender.getName());
                        ((SlapperEntity) entity).spawnToAll();
                    }

                    if (entity instanceof SlapperHuman) {
                        ((SlapperHuman) entity).setNameTag(sender.getName());
                        ((SlapperHuman) entity).setSkin(sender.asPlayer().getSkin());
                        ((SlapperHuman) entity).spawnToAll();
                    }

                    sender.sendMessage("SlapperEntity spawned successfully!");
                } catch (Exception e) {
                    sender.sendMessage(e.getMessage());
                }
                break;
            default:
                break;
        }
        return true;
    }
}
