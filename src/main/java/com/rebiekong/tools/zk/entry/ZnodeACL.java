package com.rebiekong.tools.zk.entry;

import org.apache.zookeeper.data.ACL;

public class ZnodeACL {
    public String id;
    public String scheme;
    public String perms;

    public static ZnodeACL fromACL(ACL acl) {
        ZnodeACL znodeACL = new ZnodeACL();
        znodeACL.id = acl.getId().getId();
        znodeACL.scheme = acl.getId().getScheme();
        int f = acl.getPerms();
        String p = "";
        if ((f & 0b10000) != 0) {
            p += "C";
        }
        if ((f & 0b01000) != 0) {
            p += "D";
        }
        if ((f & 0b00100) != 0) {
            p += "R";
        }
        if ((f & 0b00010) != 0) {
            p += "W";
        }
        if ((f & 0b00001) != 0) {
            p += "A";
        }
        znodeACL.perms = p;
        return znodeACL;
    }
}
