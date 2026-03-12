package org.mdafftfa;

import cn.nukkit.entity.Entity;
import java.util.List;

public interface SlapperInterface {

    public void setDataFromEntity(Entity entity);

    public List<String> getCommands();

    public void addCommand(String command);

    public boolean hasCommand(String command);

    public void removeCommand(String command);

}
