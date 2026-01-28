package org.mdafftfa;


import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import org.mdafftfa.entities.SlapperEntity;

public class Slapper extends PluginBase {

    @Override
    public void onEnable() {

        Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                for (Level world : Server.getInstance().getLevels().values()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity.namedTag.getBoolean("PursuitMinecart") && entity instanceof SlapperEntity) {
                            entity.close();
                        }
                    }
                }
            }
        }, 20);

        Server.getInstance().getCommandMap().register("slapper", new SlapperCommand(this));
        super.onEnable();
    }

}