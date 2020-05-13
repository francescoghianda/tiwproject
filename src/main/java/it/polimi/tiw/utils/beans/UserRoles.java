package it.polimi.tiw.utils.beans;

public class UserRoles
{
    private UserRoles(){}

    public static final String MANAGER = "MANAGER";
    public static final String WORKER = "WORKER";
    public static final String[] ROLES = {MANAGER, WORKER};

    public static boolean isValid(String role)
    {
        return role != null && (role.equals(MANAGER) || role.equals(WORKER));
    }
}
