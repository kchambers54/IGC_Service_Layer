package com.common;

import com.beust.jcommander.Parameter;

/**
 * JCommander argument initialization.
 */
public class Args {
    @Parameter(names = {"--help", "-h"} ,help = true)
    private boolean help = false;

    @Parameter(names = {"--debug", "-d"}, description = "enables verbose debugging")
    private boolean debug = false;

    @Parameter(names = "-url", description = "URL to base all requests from. Default is IGC API", order=0)
    private String url = "https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/";

    @Parameter(names = "-ssl", description = "Disable SSL Cert Verification", order=1)
    private boolean disableSslVerification = true; //TODO - Currently disabled by default

    @Parameter(names = {"-user", "-username"}, description = "Username for all HTTP requests", order=2)
    private String username = "igc.api.user";

    @Parameter(names = {"-pw", "-password"}, description = "Password for all HTTP requests", password = true, order=3)
    private String password = "igc.api.user"; //TODO Remove default password.

    public boolean isHelp() {
        return help;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getUrl() {
        return url;
    }

    public boolean isDisableSslVerification() {
        return disableSslVerification;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
