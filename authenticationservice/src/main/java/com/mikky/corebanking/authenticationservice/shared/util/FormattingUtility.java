package com.mikky.corebanking.authenticationservice.shared.util;

public class FormattingUtility {

    private FormattingUtility() {}

    public static String formatRoleName(String roleName) {
        return "ROLE_" + roleName.toUpperCase();
    }

    public static String formatPermissionName(String permissionName) {
        return "CAN_" + permissionName.toUpperCase();
    }
}
