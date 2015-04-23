package com.vv10.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * Created by apredoiu on 4/23/15.
 */
public class SSHShell implements Shell.CommandExecutor{
    static final Log LOG = LogFactory.getLog(SSHShell.class);
    private static final int CONF_CONNECT_TIMEOUT_DEFAULT = 30*1000;

    private String cmd;
    private InetSocketAddress serviceAddr;
    private int sshPort;
    private String user;
    private String passwd;

    private int exitStatus = -1;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    public SSHShell( String user, String passwd, InetSocketAddress serviceAddr , int sshPort, String cmd){
        this.serviceAddr = serviceAddr;
        this.user = user;
        this.passwd = passwd;
        this.sshPort = sshPort;
        this.cmd = cmd;
    }
    @Override
    public void execute() throws IOException, InterruptedException {
        String host = serviceAddr.getHostName();
        Session session;
        try {
            session = createSession(serviceAddr.getHostName(), user, sshPort);
        } catch (JSchException e) {
            LOG.warn("Unable to create SSH session", e);
            return;
        }
        session.setPassword(passwd);
        // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
        // sesConnection.setConfig("StrictHostKeyChecking", "no");
        LOG.info("Connecting to " + host + "...");
        try {
            session.connect(CONF_CONNECT_TIMEOUT_DEFAULT);
        } catch (JSchException e) {
            LOG.warn("Unable to connect to " + host + " as user " + user, e);
            return;
        }
        LOG.info("Connected to " + host);

        try {
            exitStatus = execCommand(session, this.cmd);
        } catch (JSchException e) {
            LOG.warn("Unable to achieve fencing on remote host", e);
            return;
        } finally {
            session.disconnect();
        }
    }

    /**
     * Execute a command through the ssh session, pumping its
     * stderr and stdout to our own logs.
     */
    private int execCommand(Session session, String cmd) throws JSchException, InterruptedException, IOException {
        LOG.debug("Running cmd: " + cmd);
        ChannelExec exec = null;
        try {
            exec = (ChannelExec)session.openChannel("exec");
            exec.setCommand(cmd);
            exec.setInputStream(null);
            exec.connect();

            // Pump stdout of the command to our WARN logs
            StreamPumper outPumper = new StreamPumper(out, cmd + " via ssh", exec.getInputStream());
            outPumper.start();

            // Pump stderr of the command to our WARN logs
            StreamPumper errPumper = new StreamPumper(err, cmd + " via ssh", exec.getErrStream());
            errPumper.start();

            outPumper.join();
            errPumper.join();
            return exec.getExitStatus();
        } finally {
            cleanup(exec);
        }
    }

    private static void cleanup(ChannelExec exec) {
        if (exec != null) {
            try {
                exec.disconnect();
            } catch (Throwable t) {
                LOG.warn("Couldn't disconnect ssh channel", t);
            }
        }
    }
    @Override
    public int getExitCode() throws IOException {
        return exitStatus;
    }

    @Override
    public String getOutput() throws IOException {
        return out.toString("UTF-8");
    }

    @Override
    public void close() {

    }

    private Session createSession(String host, String user, int sshPort) throws JSchException {
        JSch jsch = new JSch();
//        for (String keyFile : getKeyFiles()) {
//            jsch.addIdentity(keyFile);
//        }
//        JSch.setLogger(new LogAdapter());
        Session session = jsch.getSession(user, host, sshPort);
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    class StreamPumper {
        private final OutputStream out;

        final Thread thread;
        final String logPrefix;
        private final InputStream stream;
        private boolean started = false;

        StreamPumper(final OutputStream out, final String logPrefix, final InputStream stream) {
            this.out = out;
            this.logPrefix = logPrefix;
            this.stream = stream;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pump();
                    } catch (Throwable t) {
                        LOG.warn(logPrefix + ": Unable to pump output from ", t);
                    }
                }
            }, logPrefix + ": StreamPumper ");
            thread.setDaemon(true);
        }

        void join() throws InterruptedException {
            assert started;
            thread.join();
        }

        void start() {
            assert !started;
            thread.start();
            started = true;
        }

        protected void pump() throws IOException {
            InputStreamReader inputStreamReader = new InputStreamReader( stream, "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                out.write(line.getBytes());
            }
        }
    }
}
