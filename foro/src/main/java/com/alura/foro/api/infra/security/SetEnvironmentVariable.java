package com.alura.foro.api.infra.security;
/*
 * Tried to run the app after a while and it returned an error
 * saying environment variable wasn't set so I tried this
 */
public class SetEnvironmentVariable {
    public static void main(String[] args) {
        // Set API_SECURITY_SECRET as a system property
        System.setProperty("JWT_SECRET", "0303456");

        // Now you can access it in your application
        String apiSecuritySecret = System.getProperty("JWT_SECRET");
        System.out.println("JWT_SECRET is set to: " + apiSecuritySecret);
    }
}

// Remember that setting secrets in code is generally not recommended for production environments due to security considerations.
// Use environment variables or a secure configuration mechanism appropriate for your deployment environment.