package com.rebiekong.tools.zk.entry;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Znode {

    public String text;
    public String path;
    public String lastModifyTime;
    public List<Znode> nodes;
    public String data = "";
    public List<ZnodeACL> acl;


    public static Znode createRootNode(ZooKeeper zk) {
        return createNodeByPath("/", zk);
    }

    public static Znode createNodeByPath(String path, ZooKeeper zk) {
        Znode znode = new Znode();
        znode.path = path;
        if (!path.equals("/")) {
            String[] c = path.split("/");
            znode.text = c[c.length - 1];
        } else {
            znode.text = path;
        }
        List<Znode> n = createChildrenNode(znode, zk);
        if (n.size() > 0) {
            znode.nodes = n;
        }
        return znode;
    }


    public static Znode getDetail(String path, ZooKeeper zk) {
        Znode znode = new Znode();
        znode.path = path;
        znode.text = path;
        try {
            Stat stat = zk.exists(path, false);
            znode.acl = zk.getACL(path, stat).stream().map(ZnodeACL::fromACL).collect(Collectors.toList());
            znode.lastModifyTime = new Date(stat.getMtime()).toString();
            byte[] b = zk.getData(path, false, stat);
            if (b != null) {
                znode.data = new String(b);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return znode;
    }

    private static List<Znode> createChildrenNode(Znode parent, ZooKeeper zk) {
        List<String> childrenPath = new ArrayList<>();
        if (parent != null) {
            try {
                childrenPath = zk.getChildren(parent.path, false)
                        .stream()
                        .map(s -> parent.path.equals("/") ? s : ("/" + s))
                        .map(s -> parent.path + s)
                        .collect(Collectors.toList());
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return childrenPath.stream().map(s -> createNodeByPath(s, zk)).collect(Collectors.toList());
    }
}
