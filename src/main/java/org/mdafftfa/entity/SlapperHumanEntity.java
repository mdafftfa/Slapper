package org.mdafftfa.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.IHuman;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.entity.data.EntityFlag;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;

import org.jetbrains.annotations.NotNull;

import org.mdafftfa.HitSessionType;
import org.mdafftfa.Slapper;
import org.mdafftfa.SlapperInterface;
import org.mdafftfa.events.SlapperDeletionEvent;
import org.mdafftfa.events.SlapperHitEvent;

import java.util.*;

public class SlapperHumanEntity extends EntityHuman implements SlapperInterface, CustomEntity, IHuman, InventoryHolder, EntityHandItem {

    public static String TAG = "SlapperHumanEntity";
    public static final String IDENTIFIER = "mdafftfa:slapper_human";

    private ListTag<StringTag> commands;

    public SlapperHumanEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected String getEntityType() {
        return "Human";
    }

    @Override
    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    public static CustomEntityDefinition definition() {
        return CustomEntityDefinition.simpleBuilder(IDENTIFIER)
                .eid(IDENTIFIER)
                .hasSpawnEgg(false)
                .isSummonable(false)
                .maxHealth(20)
                .physics(false, false, false)
                .pushable(false, false)
                .isPersistent(true)
                .build();
    }

    public void writeSkinToNBT() {
        CompoundTag skinTag = new CompoundTag();
        byte[] sData = (skin != null && skin.getSkinData() != null) ? skin.getSkinData().data : new byte[16384];
        if (sData == null || sData.length == 0) sData = new byte[16384];

        skinTag.putByteArray("SkinData", sData);
        if (skin.getSkinId() != null) skinTag.putString("SkinId", skin.getSkinId());
        skinTag.putString("AnimationData", skin.getAnimationData() != null ? skin.getAnimationData() : "");
        skinTag.putByteArray("CapeData", skin.getCapeData() != null && skin.getCapeData().data != null ? skin.getCapeData().data : new byte[0]);

        skinTag.putString("ArmSize", skin.getArmSize() != null ? skin.getArmSize() : "wide");
        skinTag.putString("CapeId", skin.getCapeId() != null ? skin.getCapeId() : "");
        skinTag.putBoolean("CapeOnClassic", skin.isCapeOnClassic());
        skinTag.putString("FullSkinId", skin.getFullSkinId() != null ? skin.getFullSkinId() : UUID.randomUUID().toString());
        skinTag.putString("GeometryData", skin.getGeometryData() != null ? skin.getGeometryData() : "");

        skinTag.putBoolean("OverridingPlayerAppearance", skin.isOverridingPlayerAppearance());
        skinTag.putString("GeometryName", UUID.randomUUID().toString());

        skinTag.putString("GeometryDataEngineVersion", skin.getGeometryDataEngineVersion() != null ? skin.getGeometryDataEngineVersion() : "");
        skinTag.putBoolean("Persona", skin.isPersona());
        skinTag.putString("PlayFabId", skin.getPlayFabId() != null ? skin.getPlayFabId() : "");
        skinTag.putBoolean("Premium", skin.isPremium());
        skinTag.putBoolean("PrimaryUser", skin.isPrimaryUser());
        skinTag.putString("SkinColor", skin.getSkinColor() != null ? skin.getSkinColor() : "");
        skinTag.putString("SkinResourcePatch", skin.getSkinResourcePatch() != null ? skin.getSkinResourcePatch() : "");
        skinTag.putBoolean("Trusted", skin.isTrusted());

        this.namedTag.putCompound("SlapperSkin", skinTag);
    }

    public Skin readSkinFromNBT(CompoundTag nbt) {
        Skin skin = new Skin();

        skin.setSkinData(forceTypeSafeByteArray(nbt, "SkinData", 64 * 64 * 4));
        skin.setCapeData(forceTypeSafeByteArray(nbt, "CapeData", 0));

        skin.setAnimationData(nbt.getString("AnimationData"));
        skin.setArmSize(nbt.getString("ArmSize"));
        skin.setCapeId(nbt.getString("CapeId"));
        skin.setCapeOnClassic(nbt.getBoolean("CapeOnClassic"));
        skin.setFullSkinId(nbt.getString("FullSkinId"));
        skin.setGeometryData(nbt.getString("GeometryData"));

        skin.setOverridingPlayerAppearance(nbt.getBoolean("OverridingPlayerAppearance"));
        skin.setGeometryName(nbt.getString("GeometryName"));

        skin.setGeometryDataEngineVersion(nbt.getString("GeometryDataEngineVersion"));
        skin.setPersona(nbt.getBoolean("Persona"));
        skin.setPlayFabId(nbt.getString("PlayFabId"));
        skin.setPremium(nbt.getBoolean("Premium"));
        skin.setPrimaryUser(nbt.getBoolean("PrimaryUser"));
        skin.setSkinColor(nbt.getString("SkinColor"));
        skin.setSkinId(nbt.getString("SkinId"));
        skin.setSkinResourcePatch(nbt.getString("SkinResourcePatch"));
        skin.setTrusted(nbt.getBoolean("Trusted"));

        return skin;
    }

    public void writeInventoryToNBT() {
        CompoundTag slapperInventory = new CompoundTag();

        CompoundTag inventory = new CompoundTag();

        for (Map.Entry<Integer, Item> contents : this.getInventory().getContents().entrySet()) {
            if (!(contents.getValue().isNull())) {
                inventory.putCompound(contents.getKey().toString(), NBTIO.putItemHelper(contents.getValue()));
            }
        }

        slapperInventory.putCompound("Inventory", inventory);
        slapperInventory.putCompound("OffHandInventory", NBTIO.putItemHelper(this.getOffhandInventory().getItem(0)));

        this.namedTag.putCompound("SlapperInventory", slapperInventory);
    }

    public void readInventoryFromNBT(CompoundTag nbt) {
        CompoundTag inventory = nbt.getCompound("Inventory");
        CompoundTag offHandInventory = nbt.getCompound("OffHandInventory");

        Map<Integer, Item> contentsInventory = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            String slot = String.valueOf(i);
            if (inventory.containsCompound(slot)) {
                Item item = NBTIO.getItemHelper(inventory.getCompound(slot));
                contentsInventory.put(Integer.parseInt(slot), item);
            }
        }

        Item item = NBTIO.getItemHelper(offHandInventory);

        this.getInventory().setContents(contentsInventory);
        this.getOffhandInventory().setItem(item);
    }

    private byte[] forceTypeSafeByteArray(CompoundTag tag, String key, int defaultLen) {
        if (tag.contains(key) && tag.get(key) instanceof cn.nukkit.nbt.tag.ByteArrayTag) {
            return tag.getByteArray(key);
        }

        return new byte[defaultLen];
    }

    @Override @SuppressWarnings("unchecked")
    public void setDataFromEntity(Entity entity) {
        this.setSkin(readSkinFromNBT(entity.namedTag.getCompound("SlapperSkin")));
        this.readInventoryFromNBT(entity.namedTag.getCompound("SlapperInventory"));

        ListTag<StringTag> commandsTag = entity.namedTag.getList("Commands", StringTag.class);
        this.commands.setAll(commandsTag.getAll());

        this.setNameTag(entity.namedTag.getString("NameTag"));
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setCanBeSavedWithChunk(true);

        if (this.commands == null) {
            if (namedTag.containsList("Commands")) {
                this.commands = this.namedTag.getList("Commands", StringTag.class);
            } else {
                this.commands = new ListTag<>();
            }
        }

        this.setDataFlag(EntityFlag.CAN_SHOW_NAME, true);
        this.setDataFlag(EntityFlag.ALWAYS_SHOW_NAME, true);
        this.setNameTagAlwaysVisible(true);
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
    public void saveNBT() {
        super.saveNBT();

        this.writeSkinToNBT();
        this.writeInventoryToNBT();
        this.namedTag.putBoolean(TAG, true);
        this.namedTag.putString("SlapperType", getEntityType());
        this.namedTag.putString("NameTag", this.getNameTag());
        this.namedTag.putList("Commands", this.commands);
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