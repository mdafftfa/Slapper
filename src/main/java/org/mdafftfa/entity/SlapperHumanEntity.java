package org.mdafftfa.entity;

import org.cloudburstmc.protocol.bedrock.data.actor.ActorFlags;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.command.ConsoleCommandSender;
import org.powernukkitx.entity.Entity;
import org.powernukkitx.entity.EntityHuman;
import org.powernukkitx.entity.IHuman;
import org.powernukkitx.entity.custom.CustomEntity;
import org.powernukkitx.entity.custom.CustomEntityDefinition;
import org.powernukkitx.entity.data.human.Skin;
import org.powernukkitx.event.entity.EntityDamageByEntityEvent;
import org.powernukkitx.event.entity.EntityDamageEvent;
import org.powernukkitx.inventory.*;
import org.powernukkitx.item.Item;
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
import org.powernukkitx.utils.ItemHelper;

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
                .physics(false, false, false)
                .pushable(false, false)
                .isPersistent(true)
                .build();
    }

    public void writeSkinToNBT() {
        CompoundTag skinTag = new CompoundTag();
        byte[] sData = (skin != null && skin.getSkin().getSkinData() != null) ? skin.getSkin().getSkinData().getImage() : new byte[16384];
        if (sData == null || sData.length == 0) sData = new byte[16384];

        skinTag.putByteArray("SkinData", sData);
        if (skin.getSkin().getSkinId() != null) skinTag.putString("SkinId", skin.getSkin().getSkinId());
        skinTag.putString("AnimationData", skin.getSkin().getAnimationData() != null ? skin.getSkin().getAnimationData() : "");
        skinTag.putByteArray("CapeData", skin.getSkin().getCapeData() != null && skin.getSkin().getCapeData().getImage() != null ? skin.getSkin().getCapeData().getImage() : new byte[0]);

        skinTag.putString("ArmSize", skin.getSkin().getArmSize() != null ? skin.getSkin().getArmSize() : "wide");
        skinTag.putString("CapeId", skin.getSkin().getCapeId() != null ? skin.getSkin().getCapeId() : "");
        skinTag.putBoolean("CapeOnClassic", skin.getSkin().isCapeOnClassic());
        skinTag.putString("FullSkinId", skin.getSkin().getFullSkinId() != null ? skin.getSkin().getFullSkinId() : UUID.randomUUID().toString());
        skinTag.putString("GeometryData", skin.getSkin().getGeometryData() != null ? skin.getSkin().getGeometryData() : "");

        skinTag.putBoolean("OverridingPlayerAppearance", skin.getSkin().isOverridingPlayerAppearance());
        skinTag.putString("GeometryName", UUID.randomUUID().toString());

        skinTag.putString("GeometryDataEngineVersion", skin.getSkin().getGeometryDataEngineVersion() != null ? skin.getSkin().getGeometryDataEngineVersion() : "");
        skinTag.putBoolean("Persona", skin.getSkin().isPersona());
        skinTag.putString("PlayFabId", skin.getSkin().getPlayFabId() != null ? skin.getSkin().getPlayFabId() : "");
        skinTag.putBoolean("Premium", skin.getSkin().isPremium());
        skinTag.putBoolean("PrimaryUser", skin.getSkin().isPrimaryUser());
        skinTag.putString("SkinColor", skin.getSkin().getSkinColor() != null ? skin.getSkin().getSkinColor() : "");
        skinTag.putString("SkinResourcePatch", skin.getSkin().getSkinResourcePatch() != null ? skin.getSkin().getSkinResourcePatch() : "");
        skinTag.putBoolean("Trusted", skin.getSkin().isValid());

        this.getNbt().putCompound("SlapperSkin", skinTag);
    }

    public Skin readSkinFromNBT(CompoundTag nbt) {
        Skin skin = new Skin(org.cloudburstmc.protocol.bedrock.data.skin.Skin.builder()
                .skinData(ImageData.of(forceTypeSafeByteArray(nbt, "SkinData", 64 * 64 * 4)))
                .capeData(ImageData.of(forceTypeSafeByteArray(nbt, "CapeData", 0)))
                .animationData(nbt.getString("AnimationData"))
                .armSize(nbt.getString("ArmSize"))
                .capeId(nbt.getString("CapeId"))
                .capeOnClassic(nbt.getBoolean("CapeOnClassic"))
                .fullSkinId(nbt.getString("FullSkinId"))
                .geometryData(nbt.getString("GeometryData"))
                .overridingPlayerAppearance(nbt.getBoolean("OverridingPlayerAppearance"))
                .geometryName(nbt.getString("GeometryName"))
                .geometryDataEngineVersion(nbt.getString("GeometryDataEngineVersion"))
                .persona(nbt.getBoolean("Persona"))
                .playFabId(nbt.getString("PlayFabId"))
                .premium(nbt.getBoolean("Premium"))
                .primaryUser(nbt.getBoolean("PrimaryUser"))
                .skinColor(nbt.getString("SkinColor"))
                .skinId(nbt.getString("SkinId"))
                .skinResourcePatch(nbt.getString("SkinResourcePatch"))
                .build()
        , nbt.getBoolean("Trusted"));
        return skin;
    }

    public void writeInventoryToNBT() {
        CompoundTag slapperInventory = new CompoundTag();

        CompoundTag inventory = new CompoundTag();

        for (Map.Entry<Integer, Item> contents : this.getInventory().getContents().entrySet()) {
            if (!(contents.getValue().isNull())) {
                inventory.putCompound(contents.getKey().toString(), ItemHelper.write(contents.getValue()));
            }
        }

        slapperInventory.putCompound("Inventory", inventory);
        slapperInventory.putCompound("OffHandInventory", ItemHelper.write(this.getOffhandInventory().getItem(0)));

        this.getNbt().putCompound("SlapperInventory", slapperInventory);
    }

    public void readInventoryFromNBT(CompoundTag nbt) {
        CompoundTag inventory = nbt.getCompound("Inventory");
        CompoundTag offHandInventory = nbt.getCompound("OffHandInventory");

        Map<Integer, Item> contentsInventory = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            String slot = String.valueOf(i);
            if (inventory.containsCompound(slot)) {
                Item item = ItemHelper.read(inventory.getCompound(slot));
                contentsInventory.put(Integer.parseInt(slot), item);
            }
        }

        Item item = ItemHelper.read(offHandInventory);

        this.getInventory().setContents(contentsInventory);
        this.getOffhandInventory().setItem(item);
    }

    private byte[] forceTypeSafeByteArray(CompoundTag tag, String key, int defaultLen) {
        if (tag.contains(key) && tag.get(key) instanceof org.powernukkitx.nbt.tag.ByteArrayTag) {
            return tag.getByteArray(key);
        }

        return new byte[defaultLen];
    }

    @Override @SuppressWarnings("unchecked")
    public void setDataFromEntity(Entity entity) {
        this.setSkin(readSkinFromNBT(entity.getNbt().getCompound("SlapperSkin")));
        this.readInventoryFromNBT(entity.getNbt().getCompound("SlapperInventory"));

        ListTag<StringTag> commandsTag = entity.getNbt().getList("Commands", StringTag.class);
        this.commands.setAll(commandsTag.getAll());

        this.setNameTag(entity.getNbt().getString("NameTag"));
    }

    @Override
    protected void initEntity() {
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
        this.getNbt().putBoolean(TAG, true);
        this.getNbt().putString("SlapperType", getEntityType());
        this.getNbt().putString("NameTag", this.getNameTag());
        this.getNbt().putList("Commands", this.commands);
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