package com.vv10.util;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by apredoiu on 4/23/15.
 */
public class TestSSHShell extends TestCase {
    private String user = "vagrant";
    private String passwd = "vagrant";
    private int port = 2222;

    public void testCmd() throws IOException, InterruptedException {
        InetSocketAddress address = new InetSocketAddress("localhost",port);
        SSHShell ssh = new SSHShell(user, passwd, address, address.getPort(), "whoami");
        ssh.execute();
        System.out.println(ssh.getExitCode() + " = " + ssh.getOutput());
    }
}
