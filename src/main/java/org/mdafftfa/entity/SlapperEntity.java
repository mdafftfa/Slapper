package org.mdafftfa.entity;

import org.cloudburstmc.protocol.bedrock.data.actor.ActorFlags;
import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.command.ConsoleCommandSender;
import org.powernukkitx.entity.Entity;
import org.powernukkitx.event.entity.EntityDamageByEntityEvent;
import org.powernukkitx.event.entity.EntityDamageEvent;
import org.powernukkitx.level.format.IChunk;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.nbt.tag.ListTag;

import org.powernukkitx.nbt.tag.StringTag;
import org.jetbrains.annotations.NotNull;

import org.mdafftfa.HitSessionType;
import org.mdafftfa.Slapper;
import org.mdafftfa.SlapperInterface;
import org.mdafftfa.events.SlapperDeletionEvent;
import org.mdafftfa.events.SlapperHitEvent;

import java.util.ArrayList;
import java.util.List;

public class SlapperEntity extends Entity implements SlapperInterface {

    public static String TAG = "SlapperEntity";

    private ListTag<StringTag> commands;

    public SlapperEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected String getEntityType() {
        return "";
    }

    @Override
    public @NotNull String getIdentifier() {
        return Entity.AGENT;
    }

    @Override @SuppressWarnings("unchecked")
    public void setDataFromEntity(Entity entity) {
        this.setNameTag(entity.getNbt().getString("NameTag"));
        ListTag<StringTag> commandsTag = entity.getNbt().getList("Commands", StringTag.class);
        this.commands.setAll(commandsTag.getAll());
    }

    @Override
    public void initEntity() {
        super.initEntity();
        setCanBeSavedWithChunk(true);

        if (this.commands == null) {
            if (getNbt().containsList("Commands")) {
                this.commands = this.getNbt().getList("Commands", StringTag.class);
            } else {
                this.commands = new ListTag<>();
            }
        }

        this.setDataFlag(ActorFlags.CAN_SHOW_NAME, true);
        this.setDataFlag(ActorFlags.ALWAYS_SHOW_NAME, true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.getNbt().putBoolean(TAG, true);
        this.getNbt().putString("SlapperType", getEntityType());
        this.getNbt().putString("NameTag", this.getNameTag());
        this.getNbt().putList("Commands", this.commands);
    }

    @Override
    public boolean canBeSavedWithChunk() {
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        saveNBT();
        return isAlive();
    }

    @Override
    public List<String> getCommands() {
        List<String> listString = new ArrayList<>();
        for (StringTag stringTag : commands.getAll()) {
            listString.add(stringTag.data);
        }
        return listString;
    }

    @Override
    public void addCommand(String command) {
        if (!this.commands.getAll().contains(new StringTag(command))) {
            commands.add(new StringTag(command));
        }
    }

    @Override
    public boolean hasCommand(String command) {
        return this.commands.getAll().contains(new StringTag(command));
    }

    @Override
    public void removeCommand(String command) {
        if (this.commands.getAll().contains(new StringTag(command))) {
            this.commands.remove(new StringTag(command));
        }
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (!(source instanceof EntityDamageByEntityEvent)) {
            return false;
        }

        Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
        if (!(damager instanceof Player)) {
            return false;
        }

        Player player = (Player) damager;
        Entity entity = source.getEntity();
        String prefix = Slapper.getInstance().prefix;

        if (Slapper.getInstance().hitSessions.containsKey(player.getName())) {
            int sessionType = Slapper.getInstance().hitSessions.get(player.getName());

            if (sessionType == HitSessionType.ID) {
                player.sendMessage(prefix + "Entity ID: " + entity.getId());
                Slapper.getInstance().hitSessions.remove(player.getName());
            } else if (sessionType == HitSessionType.REMOVE) {
                SlapperDeletionEvent event = new SlapperDeletionEvent(entity, this.getEntityType(), player, SlapperDeletionEvent.CAUSE_INTERACT);
                Server.getInstance().getPluginManager().callEvent(event);
                if (!(event.isCancelled())) {
                    player.sendMessage(prefix + "Entity removed!");
                    entity.close();
                    Slapper.getInstance().hitSessions.remove(player.getName());
                }
            }
        } else {
            SlapperHitEvent event = new SlapperHitEvent(entity, this.getEntityType(), player, SlapperHitEvent.CAUSE_INTERACT);
            Server.getInstance().getPluginManager().callEvent(event);

            if (!(event.isCancelled())) {
                for (String command : getCommands()) {
                    Server.getInstance().getCommandMap().executeCommand(new ConsoleCommandSender(), command.replace("{player}", "\"" + damager.getName() + "\""));
                }
            }
        }

        source.setCancelled();
        return super.attack(source);
    }
}