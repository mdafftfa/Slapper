package org.mdafftfa;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;

import org.mdafftfa.entity.*;

import java.util.HashMap;
import java.util.Map;

public class Slapper extends PluginBase {

    public Map<String, Class<? extends SlapperInterface>> ENTITY_TYPES = new HashMap<>();

    public void registerEntities() {
        ENTITY_TYPES.put("Chicken", SlapperChicken.class);
        ENTITY_TYPES.put("Pig", SlapperPig.class);
        ENTITY_TYPES.put("Sheep", SlapperSheep.class);
        ENTITY_TYPES.put("Cow", SlapperCow.class);
        ENTITY_TYPES.put("MushroomCow", SlapperMushroomCow.class);
        ENTITY_TYPES.put("Wolf", SlapperWolf.class);
        ENTITY_TYPES.put("Enderman", SlapperEnderman.class);
        ENTITY_TYPES.put("Spider", SlapperSpider.class);
        ENTITY_TYPES.put("Skeleton", SlapperSkeleton.class);
        ENTITY_TYPES.put("PigZombie", SlapperPigZombie.class);
        ENTITY_TYPES.put("Creeper", SlapperCreeper.class);
        ENTITY_TYPES.put("Slime", SlapperSlime.class);
        ENTITY_TYPES.put("Silverfish", SlapperSilverfish.class);
        ENTITY_TYPES.put("Villager", SlapperVillager.class);
        ENTITY_TYPES.put("Zombie", SlapperZombie.class);
        ENTITY_TYPES.put("Human", SlapperHuman.class);
        ENTITY_TYPES.put("Bat", SlapperBat.class);
        ENTITY_TYPES.put("CaveSpider", SlapperCaveSpider.class);
        ENTITY_TYPES.put("LavaSlime", SlapperLavaSlime.class);
        ENTITY_TYPES.put("Ghast", SlapperGhast.class);
        ENTITY_TYPES.put("Ocelot", SlapperOcelot.class);
        ENTITY_TYPES.put("Blaze", SlapperBlaze.class);
        ENTITY_TYPES.put("ZombieVillager", SlapperZombieVillager.class);
        ENTITY_TYPES.put("Snowman", SlapperSnowman.class);
        ENTITY_TYPES.put("Minecart", SlapperMinecart.class);
        ENTITY_TYPES.put("Boat", SlapperBoat.class);
        ENTITY_TYPES.put("PrimedTNT", SlapperPrimedTNT.class);
        ENTITY_TYPES.put("Horse", SlapperHorse.class);
        ENTITY_TYPES.put("Donkey", SlapperDonkey.class);
        ENTITY_TYPES.put("Mule", SlapperMule.class);
        ENTITY_TYPES.put("SkeletonHorse", SlapperSkeletonHorse.class);
        ENTITY_TYPES.put("ZombieHorse", SlapperZombieHorse.class);
        ENTITY_TYPES.put("Witch", SlapperWitch.class);
        ENTITY_TYPES.put("Rabbit", SlapperRabbit.class);
        ENTITY_TYPES.put("Stray", SlapperStray.class);
        ENTITY_TYPES.put("Husk", SlapperHusk.class);
        ENTITY_TYPES.put("WitherSkeleton", SlapperWitherSkeleton.class);
        ENTITY_TYPES.put("IronGolem", SlapperIronGolem.class);
        ENTITY_TYPES.put("Squid", SlapperSquid.class);
        ENTITY_TYPES.put("ElderGuardian", SlapperElderGuardian.class);
        ENTITY_TYPES.put("Endermite", SlapperEndermite.class);
        ENTITY_TYPES.put("Evoker", SlapperEvoker.class);
        ENTITY_TYPES.put("Guardian", SlapperGuardian.class);
        ENTITY_TYPES.put("PolarBear", SlapperPolarBear.class);
        ENTITY_TYPES.put("Shulker", SlapperShulker.class);
        ENTITY_TYPES.put("Vex", SlapperVex.class);
        ENTITY_TYPES.put("Vindicator", SlapperVindicator.class);
        ENTITY_TYPES.put("Wither", SlapperWither.class);
        ENTITY_TYPES.put("Llama", SlapperLlama.class);
        ENTITY_TYPES.put("EndCrystal", SlapperEndCrystal.class);
    }

    @Override
    public void onEnable() {
        this.registerEntities();

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                for (Level world : Server.getInstance().getLevels().values()) {

                    if (world.getPlayers().isEmpty()) continue;

                    for (Entity entity : world.getEntities()) {
                        if (
                                entity.namedTag.getBoolean(SlapperEntity.TAG) && !(entity instanceof SlapperEntity) ||
                                entity.namedTag.getBoolean(SlapperHuman.TAG) && !(entity instanceof SlapperHuman)
                        ) {

                            try {
                                Location location = entity.getLocation();
                                CompoundTag nbt = Entity.getDefaultNBT(location);
                                IChunk chunk = entity.getLocation().getChunk();

                                Class<? extends SlapperInterface> entityClass = ENTITY_TYPES.get(entity.namedTag.getString("EntityType"));
                                SlapperInterface newEntity = entityClass
                                        .getDeclaredConstructor(IChunk.class, CompoundTag.class)
                                        .newInstance(chunk, nbt);

                                entity.close();

                                if (newEntity instanceof SlapperEntity) {
                                    ((SlapperEntity) newEntity).setDataFromEntity(entity);
                                    ((SlapperEntity) newEntity).spawnToAll();
                                }

                                if (newEntity instanceof SlapperHuman) {
                                    ((SlapperHuman) newEntity).setDataFromEntity(entity);
                                    ((SlapperHuman) newEntity).spawnToAll();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }, 20);

        Server.getInstance().getCommandMap().register("slapper", new SlapperCommand(this));
        super.onEnable();
    }

}