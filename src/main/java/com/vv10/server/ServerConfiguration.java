package com.vv10.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by predoiua on 4/21/15.
 */
@Service
@Component("serverConfiguration")
public class ServerConfiguration {

    private List<Server> servers;
    @Autowired
    public ServerConfiguration(@Value("${servers}") Resource res) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            servers = objectMapper.readValue(res.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, Server.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for (Server s: servers) {
            sb.append(s.toString());
        }
        return  sb.toString();
    }

}
