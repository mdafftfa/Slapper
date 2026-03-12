package org.mdafftfa;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.registry.Registries;
import cn.nukkit.scheduler.Task;

import cn.nukkit.utils.TextFormat;
import org.mdafftfa.commands.RcaCommand;
import org.mdafftfa.commands.SlapperCommand;
import org.mdafftfa.entity.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Slapper extends PluginBase {

    public Map<String, Constructor<? extends SlapperInterface>> ENTITY_TYPES = new HashMap<>();
    public String prefix = TextFormat.YELLOW + "[Slapper] " + TextFormat.GREEN;

    public Map<String, Integer> hitSessions = new HashMap<>();

    public void registerEntities() {
        register("Chicken", SlapperChicken.class);
        register("Pig", SlapperPig.class);
        register("Sheep", SlapperSheep.class);
        register("Cow", SlapperCow.class);
        register("MushroomCow", SlapperMushroomCow.class);
        register("Wolf", SlapperWolf.class);
        register("Enderman", SlapperEnderman.class);
        register("Spider", SlapperSpider.class);
        register("Skeleton", SlapperSkeleton.class);
        register("PigZombie", SlapperPigZombie.class);
        register("Creeper", SlapperCreeper.class);
        register("Slime", SlapperSlime.class);
        register("Silverfish", SlapperSilverfish.class);
        register("Villager", SlapperVillager.class);
        register("Zombie", SlapperZombie.class);
        register("Human", SlapperHumanEntity.class);
        register("Bat", SlapperBat.class);
        register("CaveSpider", SlapperCaveSpider.class);
        register("LavaSlime", SlapperLavaSlime.class);
        register("Ghast", SlapperGhast.class);
        register("Ocelot", SlapperOcelot.class);
        register("Blaze", SlapperBlaze.class);
        register("ZombieVillager", SlapperZombieVillager.class);
        register("Snowman", SlapperSnowman.class);
        register("Minecart", SlapperMinecart.class);
        register("Boat", SlapperBoat.class);
        register("PrimedTNT", SlapperPrimedTNT.class);
        register("Horse", SlapperHorse.class);
        register("Donkey", SlapperDonkey.class);
        register("Mule", SlapperMule.class);
        register("SkeletonHorse", SlapperSkeletonHorse.class);
        register("ZombieHorse", SlapperZombieHorse.class);
        register("Witch", SlapperWitch.class);
        register("Rabbit", SlapperRabbit.class);
        register("Stray", SlapperStray.class);
        register("Husk", SlapperHusk.class);
        register("WitherSkeleton", SlapperWitherSkeleton.class);
        register("IronGolem", SlapperIronGolem.class);
        register("Squid", SlapperSquid.class);
        register("ElderGuardian", SlapperElderGuardian.class);
        register("Endermite", SlapperEndermite.class);
        register("Evoker", SlapperEvoker.class);
        register("Guardian", SlapperGuardian.class);
        register("PolarBear", SlapperPolarBear.class);
        register("Shulker", SlapperShulker.class);
        register("Vex", SlapperVex.class);
        register("Vindicator", SlapperVindicator.class);
        register("Wither", SlapperWither.class);
        register("Llama", SlapperLlama.class);
        register("EndCrystal", SlapperEndCrystal.class);
    }

    private void register(String name, Class<? extends SlapperInterface> clazz) {
        try {
            Constructor<? extends SlapperInterface> constructor =
                    clazz.getDeclaredConstructor(IChunk.class, CompoundTag.class);

            constructor.setAccessible(true);
            ENTITY_TYPES.put(name, constructor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Slapper instance;

    public static Slapper getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.registerEntities();
        instance = this;

        try {
            Registries.ENTITY.registerCustomEntity(this, SlapperHumanEntity.class);
            Registries.ENTITY.rebuildTag();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                for (Level level : Server.getInstance().getLevels().values()) {
                    if (level.getPlayers().isEmpty()) continue;
                    if (!Server.getInstance().isLevelLoaded(level.getName())) continue;

                    for (Entity entity : level.getEntities()) {

                        if (entity.namedTag.getBoolean(SlapperLoaderEntity.TAG) && !(entity instanceof SlapperLoaderEntity)) {
                            SlapperLoaderEntity newEntity = new SlapperLoaderEntity(entity.getChunk(), entity.namedTag.copy());
                            newEntity.writeSlapperDataFromEntity(entity);
                            entity.close();
                            newEntity.spawnToAll();
                        }

                        if (entity.namedTag.getBoolean(SlapperLoaderEntity.TAG) && entity instanceof SlapperLoaderEntity) {
                            if (((SlapperLoaderEntity) entity).isSlapperHumanEntity()) {
                                SlapperHumanEntity slapper = new SlapperHumanEntity(entity.chunk, Entity.getDefaultNBT(entity.getLocation()));
                                slapper.setDataFromEntity(entity);
                                slapper.spawnToAll();
                                entity.close();
                            }

                            if (((SlapperLoaderEntity) entity).isSlapperEntity()) {
                                try {
                                    Location location = entity.getLocation();
                                    IChunk chunk = entity.getChunk();
                                    CompoundTag nbt = Entity.getDefaultNBT(location);

                                    Constructor<? extends SlapperInterface> constructor =
                                            ENTITY_TYPES.get(entity.namedTag.getString("SlapperType"));

                                    if (constructor == null) {
                                        continue;
                                    }
                                    SlapperInterface slapper = constructor.newInstance(chunk, nbt);
                                    slapper.setDataFromEntity(entity);
                                    ((SlapperEntity) slapper).spawnToAll();
                                    entity.close();
                                } catch (Exception e) {
                                    Server.getInstance().getLogger().info(e.toString());
                                }
                            }
                        }

                        if (
                                (entity.namedTag.getBoolean(SlapperEntity.TAG) && !(entity instanceof SlapperEntity)) ||
                                        (entity.namedTag.getBoolean(SlapperHumanEntity.TAG) && !(entity instanceof SlapperHumanEntity))
                        ) {

                            try {
                                Location location = entity.getLocation();
                                IChunk chunk = entity.getChunk();
                                CompoundTag nbt = Entity.getDefaultNBT(location);

                                Constructor<? extends SlapperInterface> constructor =
                                        ENTITY_TYPES.get(entity.namedTag.getString("SlapperType"));

                                if (constructor == null) {
                                    continue;
                                }

                                SlapperInterface newEntity = constructor.newInstance(chunk, nbt);

                                if (newEntity instanceof SlapperEntity) {
                                    ((SlapperEntity) newEntity).setDataFromEntity(entity);
                                    ((SlapperEntity) newEntity).spawnToAll();
                                }

                                if (newEntity instanceof SlapperHumanEntity) {
                                    ((SlapperHumanEntity) newEntity).setDataFromEntity(entity);
                                    ((SlapperHumanEntity) newEntity).spawnToAll();
                                }

                                entity.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }, 20);

        Server.getInstance().getPluginManager().registerEvents(new SlapperListener(this), this);
        Server.getInstance().getCommandMap().register("slapper", new SlapperCommand(this));
        Server.getInstance().getCommandMap().register("rca", new RcaCommand(this));
        super.onEnable();
    }

}