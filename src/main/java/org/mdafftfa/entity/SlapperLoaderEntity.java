package org.mdafftfa.entity;

import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.powernukkitx.Server;
import org.powernukkitx.entity.Entity;
import org.powernukkitx.entity.data.human.Skin;
import org.powernukkitx.event.entity.EntityDamageEvent;
import org.powernukkitx.item.Item;
import org.powernukkitx.level.format.IChunk;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.mdafftfa.Slapper;
import org.powernukkitx.utils.ItemHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SlapperLoaderEntity extends Entity {

    public static String TAG = "SlapperLoaderEntity";
    public static final String IDENTIFIER = Entity.VILLAGER;

    private Skin skin;
    private Map<Integer, Item> inventory;
    private Item offHandInventory;

    public SlapperLoaderEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected String getEntityType() {
        return "Loader";
    }

    @Override
    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
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

        for (Map.Entry<Integer, Item> contents : this.inventory.entrySet()) {
            if (!(contents.getValue().isNull())) {
                inventory.putCompound(contents.getKey().toString(), ItemHelper.write(contents.getValue()));
            }
        }

        slapperInventory.putCompound("Inventory", inventory);
        slapperInventory.putCompound("OffHandInventory", ItemHelper.write(this.offHandInventory));

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

        this.inventory.putAll(contentsInventory);
        this.offHandInventory = ItemHelper.read(offHandInventory);
    }

    private byte[] forceTypeSafeByteArray(CompoundTag tag, String key, int defaultLen) {
        if (tag.contains(key) && tag.get(key) instanceof org.powernukkitx.nbt.tag.ByteArrayTag) {
            return tag.getByteArray(key);
        }

        return new byte[defaultLen];
    }

    public boolean isSlapperEntity() {
        if (Slapper.getInstance().ENTITY_TYPES.containsKey(getNbt().getString("SlapperType")) && !getNbt().getString("SlapperType").equalsIgnoreCase("Human")) return true;
        return false;
    }

    public boolean isSlapperHumanEntity() {
        if (Slapper.getInstance().ENTITY_TYPES.containsKey(getNbt().getString("SlapperType")) && getNbt().getString("SlapperType").equalsIgnoreCase("Human")) return true;
        return false;
    }

    public void writeSlapperDataFromEntity(Entity entity) {
        getNbt().putString("NameTag", entity.getNbt().getString("NameTag"));
        getNbt().putString("SlapperType", entity.getNbt().getString("SlapperType"));
        getNbt().putList("Commands", entity.getNbt().getList("Commands"));

        if (entity instanceof SlapperHumanEntity) {
            this.setSkin(this.readSkinFromNBT(entity.getNbt().getCompound("SlapperSkin")));
            this.writeSkinToNBT();
            this.readInventoryFromNBT(entity.getNbt().getCompound("SlapperInventory"));
            this.writeInventoryToNBT();
        }
    }

    @Override
    public boolean canBeSavedWithChunk() {
        return true;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (inventory == null) {
            inventory = new ConcurrentHashMap<>();
        }

        this.getNbt().putBoolean(TAG, true);
    }


    @Override
    public boolean attack(EntityDamageEvent source) {
        Server.getInstance().getLogger().info(this.getNbt().toString());
        this.close();
        return super.attack(source);
    }
}