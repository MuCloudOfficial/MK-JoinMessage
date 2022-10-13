package me.mucloud.plugin.MK.JoinMessage;

public class PermissionMessage {

    private final String Permission;
    private final int Priority;
    private final String JoinMessage;
    private final String ExitMessage;

    protected PermissionMessage(String permission,int priority,String joinmessage,String exitmessage){
        Permission = permission;
        Priority = priority;
        JoinMessage = joinmessage;
        ExitMessage = exitmessage;
    }

    public String getJoinMessage(){
        return JoinMessage;
    }

    public String getExitMessage(){
        return ExitMessage;
    }

    public String getPermission(){
        return Permission;
    }

    public int getPriority(){
        return Priority;
    }

}
