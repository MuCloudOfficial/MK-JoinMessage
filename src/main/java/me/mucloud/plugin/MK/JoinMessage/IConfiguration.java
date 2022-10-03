package me.mucloud.plugin.MK.JoinMessage;

import java.io.File;
import java.util.List;

public interface IConfiguration {

    String getVersion();

    File getConfigFile();

    void setJoinMessage(String message);

    void setExitMessage(String message);

    String getJoinMessage();

    String getExitMessage();

    List<PermissionMessage> getPermissionMessageMap();

    PermissionMessage comparePriority(List<PermissionMessage> pml);

    String messageChange(String message);

}
