package com.cosc.bandfanapp.network.model.bandmember;

import com.cosc.bandfanapp.model.BandMember;
import com.cosc.bandfanapp.network.model.Response;

import java.util.List;

/**
 * @author William Moffitt
 * @version 1.0 11/19/15
 */
public class ReadBandMembersResponse extends Response {

    public List<BandMember> band_members;

}
