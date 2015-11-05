package com.cosc.bandfanapp.network.model.login;

import com.cosc.bandfanapp.network.model.Response;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public class LoginResponse extends Response {

    public int id;
    public String first_name;
    public String last_name;
    public String username;
    public String dob;
    public String email;
    public int is_moderator;

}
