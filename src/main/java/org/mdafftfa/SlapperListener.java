package org.mdafftfa;

import org.powernukkitx.entity.Entity;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.level.LevelSaveEvent;
import org.powernukkitx.level.Level;

import org.mdafftfa.entity.SlapperEntity;
import org.mdafftfa.entity.SlapperHumanEntity;
import org.mdafftfa.entity.SlapperLoaderEntity;

public class SlapperListener implements Listener {

    Slapper plugin;
    String prefix;

    public SlapperListener(Slapper plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLevelSafe(LevelSaveEvent event) {
        Level level = event.getLevel();
        for (Entity entity : level.getEntities()) {
            if (entity.getNbt().getBoolean(SlapperHumanEntity.TAG) && entity instanceof SlapperHumanEntity) {
                SlapperLoaderEntity slapper = new SlapperLoaderEntity(entity.getChunk(), Entity.getDefaultNBT(entity));
                slapper.writeSlapperDataFromEntity(entity);
                entity.close();
                slapper.spawnToAll();
            }

            if (entity.getNbt().getBoolean(SlapperEntity.TAG) && entity instanceof SlapperEntity) {
                SlapperLoaderEntity slapper = new SlapperLoaderEntity(entity.getChunk(), Entity.getDefaultNBT(entity));
                slapper.writeSlapperDataFromEntity(entity);
                entity.close();
                slapper.spawnToAll();
            }

        }
    }

}
