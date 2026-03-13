package org.mdafftfa.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;

import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;

import cn.nukkit.nbt.tag.CompoundTag;

import cn.nukkit.utils.TextFormat;
import org.mdafftfa.HitSessionType;
import org.mdafftfa.Slapper;
import org.mdafftfa.SlapperInterface;
import org.mdafftfa.entity.SlapperEntity;
import org.mdafftfa.entity.SlapperHumanEntity;
import org.mdafftfa.events.SlapperCreationEvent;

import java.lang.reflect.Constructor;
import java.util.*;

public class SlapperCommand extends Command {

    Slapper plugin;
    String prefix;

    public String helpHeader = TextFormat.YELLOW + "---------- " +
            TextFormat.GREEN + "[" + TextFormat.YELLOW + "Slapper Help" + TextFormat.GREEN + "] " +
            TextFormat.YELLOW + "----------";

    public List<String> mainArgs = Arrays.asList(
            "help: /slapper help",
            "spawn: /slapper spawn <type> [name]",
            "edit: /slapper edit [id] [args...]",
            "id: /slapper id",
            "remove: /slapper remove [id]",
            "version: /slapper version",
            "cancel: /slapper cancel",
            "entities: /slapper entities"
    );

    public List<String> editArgs = Arrays.asList(
            "helmet: /slapper edit <eid> helmet <item_name>",
            "chestplate: /slapper edit <eid> chestplate <item_name>",
            "leggings: /slapper edit <eid> leggings <item_name>",
            "boots: /slapper edit <eid> boots <item_name>",
            "skin: /slapper edit <eid> skin",
            "name: /slapper edit <eid> name <name>",
            "addcommand: /slapper edit <eid> addcommand <command>",
            "delcommand: /slapper edit <eid> delcommand <command>",
            "listcommands: /slapper edit <eid> listcommands",
            "blockid: /slapper edit <eid> block <block_name>",
            "scale: /slapper edit <eid> scale <size>",
            "tphere: /slapper edit <eid> tphere",
            "tpto: /slapper edit <eid> tpto",
            "menuname: /slapper edit <eid> menuname <name/remove>"
    );
    public String MSG_NO_PERM = TextFormat.GREEN + "[" + TextFormat.YELLOW + "Slapper" + TextFormat.GREEN + "]" + TextFormat.RED + " You don't have permission+";

    public SlapperCommand(Slapper plugin) {
        super("slapper", "Slapper command", plugin.prefix + "Usage: /slapper <spawn:remove:id>", new String[] {""});
        this.plugin = plugin;
        this.prefix = plugin.prefix;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        switch (args[0]) {
            case "id": {
                if (!(sender.hasPermission("slapper.command.id"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (this.plugin.hitSessions.containsKey(sender.getName())) {
                    this.plugin.hitSessions.remove(sender.getName());
                    return false;
                }

                this.plugin.hitSessions.put(sender.getName(), HitSessionType.ID);
                sender.sendMessage(prefix + "Hit an entity to get its ID!");
                break;
            }

            case "cancel": {
                if (!(sender.hasPermission("slapper.command.cancel"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (!this.plugin.hitSessions.containsKey(sender.getName())) {
                    sender.sendMessage(prefix + "You are not in hit sessions!");
                    return false;
                }

                sender.sendMessage(prefix + "Cancelled hit sessions successfully!");
                break;
            }

            case "edit": {
                if (!(sender.hasPermission("slapper.command.edit"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (args.length < 3) {
                    sender.sendMessage(prefix + "Usage: /slapper edit <id> <action> [value]");
                    return false;
                }

                long id;
                try {
                    id = Long.parseLong(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix + "Invalid ID format.");
                    return false;
                }

                Entity entity = sender.getLocation().level.getEntity(id);

                if (entity instanceof SlapperEntity || entity instanceof SlapperHumanEntity) {
                    switch (args[2].toLowerCase()) {
                        case "help":
                        case "?":
                            for (String editArgs : editArgs) {
                                sender.sendMessage(TextFormat.GREEN + editArgs.replace("<eid>", args[1]));
                            }
                            break;

                        case "helm":
                        case "helmet":
                        case "head":
                        case "hat":
                        case "cap":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                if (hand.isHelmet()) {
                                    ((SlapperHumanEntity) entity).getInventory().setHelmet(hand);
                                    sender.sendMessage(prefix + "Helmet armor updated successfully!");
                                } else {
                                    sender.sendMessage(prefix + "Please hold a helmet Item!");
                                }
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can do this Command!");
                            }
                            break;

                        case "chest":
                        case "shirt":
                        case "chestplate":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                if (hand.isChestplate()) {
                                    ((SlapperHumanEntity) entity).getInventory().setChestplate(hand);
                                    sender.sendMessage(prefix + "Chestplate armor updated successfully!");
                                } else {
                                    sender.sendMessage(prefix + "Please hold a chestplate Item!");
                                }
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can do this Command!");
                            }
                            break;

                        case "pants":
                        case "legs":
                        case "leggings":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                if (hand.isLeggings()) {
                                    ((SlapperHumanEntity) entity).getInventory().setLeggings(hand);
                                    sender.sendMessage(prefix + "Legging armor updated successfully!");
                                } else {
                                    sender.sendMessage(prefix + "Please hold a legging Items!");
                                }
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can do this Command!");
                            }
                            break;

                        case "feet":
                        case "boots":
                        case "shoes":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                if (hand.isBoots()) {
                                    ((SlapperHumanEntity) entity).getInventory().setBoots(hand);
                                    sender.sendMessage(prefix + "Boot armor updated successfully!");
                                } else {
                                    sender.sendMessage(prefix + "Please hold a boot Items!");
                                }
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can do this Command!");
                            }
                            break;

                        case "hand":
                        case "item":
                        case "holding":
                        case "arm":
                        case "held":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                ((SlapperHumanEntity) entity).getInventory().setItemInHand(hand);
                                sender.sendMessage(prefix + "Item updated successfully!");
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can held an Item!");
                            }
                            break;
                        case "offhand":
                            if (entity instanceof SlapperHumanEntity) {
                                Item hand = sender.asPlayer().getInventory().getItemInHand();
                                ((SlapperHumanEntity) entity).getOffhandInventory().setItem(hand);
                                sender.sendMessage(prefix + "Item updated successfully!");
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can held an Item!");
                            }
                            break;

                        case "setskin":
                        case "changeskin":
                        case "editskin":
                        case "skin":
                            if (entity instanceof SlapperHumanEntity) {
                                ((SlapperHumanEntity) entity).setSkin(sender.asPlayer().getSkin());
                                sender.sendMessage(prefix + "Skin updated successfully!");
                            } else {
                                sender.sendMessage(prefix + "Only slapper human can change the Skin!");
                            }
                            break;

                        case "name":
                        case "customname":
                            if (args.length <= 3) {
                                sender.sendMessage(prefix + "Please enter a name!");
                                return false;
                            }

                            String[] shiftedArgsName = Arrays.copyOfRange(args, 3, args.length);
                            String name = String.join(" ", shiftedArgsName).trim();

                            entity.setNameTag(name);
                            sender.sendMessage(prefix + "Name updated successfully!");
                            break;

                        case "addc":
                        case "addcmd":
                        case "addcommand":
                            String[] shiftedArgsAdd = Arrays.copyOfRange(args, 3, args.length);
                            String addCommand = String.join(" ", shiftedArgsAdd).trim();

                            if (!(((SlapperInterface) entity).hasCommand(addCommand))) {
                                ((SlapperInterface) entity).addCommand(addCommand);
                                sender.sendMessage(prefix + "Command added!");
                            } else {
                                sender.sendMessage(prefix + "That command already exist!");
                            }
                            break;
                        case "delc":
                        case "delcmd":
                        case "delcommand":
                        case "removecommand":
                            if (((SlapperInterface) entity).getCommands().isEmpty()) {
                                sender.sendMessage(prefix + "That entity doesn't have any commands!");
                                return false;
                            }

                            String[] shiftedArgsRemove = Arrays.copyOfRange(args, 3, args.length);
                            String removeCommand = String.join(" ", shiftedArgsRemove).trim();

                            if (((SlapperInterface) entity).hasCommand(removeCommand)) {
                                ((SlapperInterface) entity).removeCommand(removeCommand);
                                sender.sendMessage(prefix + "Command removed!");
                            } else {
                                sender.sendMessage(prefix + "That command doesn't exist!");
                            }
                            break;
                        case "listcs":
                        case "listcmds":
                        case "listcommands":
                            if (((SlapperInterface) entity).getCommands().isEmpty()) {
                                sender.sendMessage(prefix + "That entity doesn't have any commands!");
                                return false;
                            }

                            sender.sendMessage(prefix + "List Command of Entity ID: " + id);
                            for (String command : ((SlapperInterface) entity).getCommands()) {
                                sender.sendMessage(TextFormat.GREEN + "- " + command + "\n");
                            }
                            break;

                        case "teleporthere":
                        case "movehere":
                        case "tphere":
                        case "bringhere":
                            entity.teleport(sender.getLocation());
                            sender.sendMessage(prefix + "Teleported slapper entity to you!");
                            break;

                        case "teleportto":
                        case "tpto":
                        case "goto":
                        case "teleport":
                        case "tp":
                            sender.asPlayer().teleport(entity.getLocation());
                            sender.sendMessage(prefix + "Teleported you to slapper entity!");
                            break;
                        case "scale":
                        case "size":
                            if (args.length >= 3 && args[3].matches("-?\\d+(\\.\\d+)?")) {
                                float scale = Float.parseFloat(args[3]);
                                entity.setScale(scale);
                                sender.sendMessage(prefix + "Updated scale!");
                            } else {
                                sender.sendMessage(prefix + "Please enter a value!");
                            }
                            break;
                        default:
                            for (String editArgs : editArgs) {
                                sender.sendMessage(TextFormat.GREEN + editArgs.replace("<eid>", args[1]));
                            }
                            break;
                    }
                } else {
                    sender.sendMessage(prefix + "That entity is not handled by Slapper.");
                    return false;
                }
                break;
            }

            case "help":
            case "?":
                if (!(sender.hasPermission("slapper.command.help"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                sender.sendMessage(this.helpHeader);
                for (String msgArg : mainArgs) {
                    sender.sendMessage(TextFormat.GREEN + " - " + msgArg + "\n");
                }
                break;
            case "list":
            case "entities":
                if (!(sender.hasPermission("slapper.command.list"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                sender.sendMessage(prefix + "List of entities: " + plugin.ENTITY_TYPES.keySet());
                break;
            case "remove": {
                if (!(sender.hasPermission("slapper.command.remove"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (!(sender.hasPermission("slapper.command.remove"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (args.length < 2) {
                    this.plugin.hitSessions.put(sender.getName(), HitSessionType.REMOVE);
                    sender.sendMessage(prefix + "Hit a slapper entity to remove it!");
                    return true;
                }

                try {
                    long id = Long.parseLong(args[1]);
                    Entity entity = sender.getLocation().level.getEntity(id);
                    if (entity != null) {
                        entity.close();
                        sender.sendMessage(prefix + "Removed slapper entity successfully!");
                    } else {
                        sender.sendMessage(prefix + "No entity found with that ID.");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix + "Invalid ID format.");
                }
                break;
            }

            case "add":
            case "make":
            case "create":
            case "spawn":
            case "apawn":
            case "spanw": {
                if (!(sender.hasPermission("slapper.command.spawn"))) {
                    sender.sendMessage(MSG_NO_PERM);
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(prefix + "Usage: /slapper spawn <type> [name]");
                    return false;
                }

                String selectedEntity = null;
                String name = "";

                if (args.length > 2) {
                    String[] nameParts = Arrays.copyOfRange(args, 2, args.length);
                    name = String.join(" ", nameParts).trim();
                }

                if (name.isEmpty()) {
                    name = sender.getName();
                }

                for (String entityType : plugin.ENTITY_TYPES.keySet()) {
                    if (entityType.toLowerCase(Locale.ROOT).equalsIgnoreCase(args[1].toLowerCase())) {
                        selectedEntity = entityType;
                        break;
                    }
                }

                if (selectedEntity == null) {
                    sender.sendMessage(prefix + "Invalid entity type!");
                    return false;
                }

                try {
                    Location location = sender.getLocation();
                    CompoundTag nbt = Entity.getDefaultNBT(location);
                    IChunk chunk = sender.getLocation().getChunk();

                    Constructor<? extends SlapperInterface> constructor =
                            plugin.ENTITY_TYPES.get(selectedEntity);
                    SlapperInterface entity = constructor.newInstance(chunk, nbt);

                    SlapperCreationEvent event = new SlapperCreationEvent((Entity) entity, selectedEntity, sender.asPlayer(), SlapperCreationEvent.CAUSE_COMMAND);
                    Server.getInstance().getPluginManager().callEvent(event);

                    if (entity instanceof SlapperEntity && !(event.isCancelled())) {
                        ((SlapperEntity) entity).setNameTag(name);
                        ((SlapperEntity) entity).spawnToAll();

                        sender.sendMessage(prefix + selectedEntity + " entity spawned with name " + name + " and entity ID " + ((Entity) entity).getId());
                    }

                    if (entity instanceof SlapperHumanEntity && !(event.isCancelled())) {
                        ((SlapperHumanEntity) entity).setNameTag(name);
                        ((SlapperHumanEntity) entity).setSkin(sender.asPlayer().getSkin());
                        ((SlapperHumanEntity) entity).getInventory().setItemInHand(sender.asPlayer().getInventory().getItemInHand());
                        ((SlapperHumanEntity) entity).getOffhandInventory().setItem(sender.asPlayer().getOffhandInventory().getItem(0));
                        ((SlapperHumanEntity) entity).getInventory().setArmorContents(sender.asPlayer().getInventory().getArmorContents());
                        ((SlapperHumanEntity) entity).spawnToAll();

                        sender.sendMessage(prefix + selectedEntity + " entity spawned with name " + name + " and entity ID " + ((Entity) entity).getId());
                    }
                } catch (Exception e) {
                    Server.getInstance().getLogger().error(e.getMessage());
                }
                break;
            }
            default:
                sender.sendMessage(prefix + "Unknown command. Type '/slapper help' for help.");
                break;
        }
        return true;
    }
}
