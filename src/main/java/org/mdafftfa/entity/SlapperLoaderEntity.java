package org.mdafftfa.entity;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.mdafftfa.Slapper;

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

    public Skin readSkinFromNBT(CompoundTag nbt) {
        this.skin = new Skin();

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

    public void writeSkinToNBT() {
        CompoundTag skinTag = new CompoundTag();
        // Gunakan helper function untuk mencegah NullPointerException
        //                .putString("GeometryName", skin.)
//                .putString("OverridingPlayerAppearance", skin.)

        skinTag.putByteArray("SkinData", skin.getSkinData().data);
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
        skinTag.putString("SkinId", skin.getSkinId());
        skinTag.putString("SkinResourcePatch", skin.getSkinResourcePatch() != null ? skin.getSkinResourcePatch() : "");
        skinTag.putBoolean("Trusted", skin.isTrusted());

        this.namedTag.putCompound("SlapperSkin", skinTag);
    }

    public void writeInventoryToNBT() {
        CompoundTag slapperInventory = new CompoundTag();

        CompoundTag inventory = new CompoundTag();

        for (Map.Entry<Integer, Item> contents : this.inventory.entrySet()) {
            if (!(contents.getValue().isNull())) {
                inventory.putCompound(contents.getKey().toString(), NBTIO.putItemHelper(contents.getValue()));
            }
        }

        slapperInventory.putCompound("Inventory", inventory);
        slapperInventory.putCompound("OffHandInventory", NBTIO.putItemHelper(this.offHandInventory));

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

        this.inventory.putAll(contentsInventory);
        this.offHandInventory = NBTIO.getItemHelper(offHandInventory);
    }

    private byte[] forceTypeSafeByteArray(CompoundTag tag, String key, int defaultLen) {
        if (tag.contains(key) && tag.get(key) instanceof cn.nukkit.nbt.tag.ByteArrayTag) {
            return tag.getByteArray(key);
        }

        return new byte[defaultLen];
    }

    public boolean isSlapperEntity() {
        if (Slapper.getInstance().ENTITY_TYPES.containsKey(namedTag.getString("SlapperType")) && !namedTag.getString("SlapperType").equalsIgnoreCase("Human")) return true;
        return false;
    }

    public boolean isSlapperHumanEntity() {
        if (Slapper.getInstance().ENTITY_TYPES.containsKey(namedTag.getString("SlapperType")) && namedTag.getString("SlapperType").equalsIgnoreCase("Human")) return true;
        return false;
    }

    public void writeSlapperDataFromEntity(Entity entity) {
        namedTag.putString("NameTag", entity.namedTag.getString("NameTag"));
        namedTag.putString("SlapperType", entity.namedTag.getString("SlapperType"));
        namedTag.putList("Commands", entity.namedTag.getList("Commands"));

        if (entity instanceof SlapperHumanEntity) {
            this.setSkin(this.readSkinFromNBT(entity.namedTag.getCompound("SlapperSkin")));
            this.writeSkinToNBT();
            this.readInventoryFromNBT(entity.namedTag.getCompound("SlapperInventory"));
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

        this.namedTag.putBoolean(TAG, true);
    }


    @Override
    public boolean attack(EntityDamageEvent source) {
        Server.getInstance().getLogger().info(this.namedTag.toString());
        this.close();
        return super.attack(source);
    }
}