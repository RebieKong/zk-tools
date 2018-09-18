package com.rebiekong.tools.zk.controller;

import com.alibaba.fastjson.JSON;
import com.rebiekong.tools.zk.entry.Znode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexController {

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String list(
            @RequestParam(name = "ips", defaultValue = "127.0.0.1:2181") String ips,
            @RequestParam(name = "user_name", required = false) String userName,
            @RequestParam(name = "password", required = false) String password
    ) {
        Watcher watcher = event -> System.out.println(event.toString());
        try {
            ZooKeeper zk = new ZooKeeper(ips, 5000, watcher);
            if (userName != null && password != null && !userName.equals("") && !password.equals("")) {
                zk.addAuthInfo("digest", (userName + ":" + password).getBytes());
            }
            String rs = JSON.toJSONString(Znode.createRootNode(zk));
            zk.close();
            return rs;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @RequestMapping(path = "/detail", method = RequestMethod.GET)
    public String detail(
            @RequestParam("path") String path,
            @RequestParam(name = "ips", defaultValue = "127.0.0.1:2181") String ips,
            @RequestParam(name = "user_name", required = false) String userName,
            @RequestParam(name = "password", required = false) String password
    ) {
        Watcher watcher = event -> System.out.println(event.toString());
        try {
            ZooKeeper zk = new ZooKeeper(ips, 5000, watcher);
            if (userName != null && password != null && !userName.equals("") && !password.equals("")) {
                zk.addAuthInfo("digest", (userName + ":" + password).getBytes());
            }
            String rs = JSON.toJSONString(Znode.getDetail(path, zk));
            zk.close();
            return rs;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
