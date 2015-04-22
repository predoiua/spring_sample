package com.vv10.server;

/**
 * Created by predoiua on 4/21/15.
 */
public class Server {

    private String name;
    private String ip;
    private Integer port;

    public Server(){
    }
    @Override
    public String toString(){
        return "Name: " + name + " ip :" + ip + " Port:" + port.toString();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
