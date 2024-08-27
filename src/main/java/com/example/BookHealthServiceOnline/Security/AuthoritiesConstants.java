package com.example.BookHealthServiceOnline.Security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String HOSPITALADMIN = "ROLE_HOSPITAL_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String PATIENT = "ROLE_PATIENT";

    public static final String TECHNICIAN = "ROLE_TECHNICIAN";

    public static final String DOCTOR = "ROLE_DOCTOR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
