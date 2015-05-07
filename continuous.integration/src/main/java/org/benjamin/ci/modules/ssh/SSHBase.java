package org.benjamin.ci.modules.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHBase {

    /** Default listen port for SSH daemon */
    private static final int SSH_PORT = 22;

    private String host;
    private String knownHosts;
    private int port = SSH_PORT;
    private boolean failOnError = true;
    private boolean verbose;
    private final SSHUserInfo userInfo;

    /**
     * Constructor for SSHBase.
     */
    public SSHBase() {
        super();
        userInfo = new SSHUserInfo();
    }

    /**
     * Remote host, either DNS name or IP.
     *
     * @param host  The new host value
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Get the host.
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the failonerror flag.
     * Default is true
     * @param failure if true throw a build exception when a failure occuries,
     *                otherwise just log the failure and continue
     */
    public void setFailonerror(final boolean failure) {
        failOnError = failure;
    }

    /**
     * Get the failonerror flag.
     * @return the failonerror flag
     */
    public boolean getFailonerror() {
        return failOnError;
    }

    /**
     * Set the verbose flag.
     * @param verbose if true output more verbose logging
     * @since Ant 1.6.2
     */
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Get the verbose flag.
     * @return the verbose flag
     * @since Ant 1.6.2
     */
    public boolean getVerbose() {
        return verbose;
    }

    /**
     * Username known to remote host.
     *
     * @param username  The new username value
     */
    public void setUsername(final String username) {
        userInfo.setName(username);
    }


    /**
     * Sets the password for the user.
     *
     * @param password  The new password value
     */
    public void setPassword(final String password) {
        userInfo.setPassword(password);
    }

    /**
     * Sets the keyfile for the user.
     *
     * @param keyfile  The new keyfile value
     */
    public void setKeyfile(final String keyfile) {
        userInfo.setKeyfile(keyfile);
    }

    /**
     * Sets the passphrase for the users key.
     *
     * @param passphrase  The new passphrase value
     */
    public void setPassphrase(final String passphrase) {
        userInfo.setPassphrase(passphrase);
    }

    /**
     * Sets the path to the file that has the identities of
     * all known hosts.  This is used by SSH protocol to validate
     * the identity of the host.  The default is
     * <i>${user.home}/.ssh/known_hosts</i>.
     *
     * @param knownHosts a path to the known hosts file.
     */
    public void setKnownhosts(final String knownHosts) {
        this.knownHosts = knownHosts;
    }

    /**
     * Setting this to true trusts hosts whose identity is unknown.
     *
     * @param yesOrNo if true trust the identity of unknown hosts.
     */
    public void setTrust(final boolean yesOrNo) {
        userInfo.setTrust(yesOrNo);
    }

    /**
     * Changes the port used to connect to the remote host.
     *
     * @param port port number of remote host.
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Get the port attribute.
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Initialize the task.
     * This initializizs the known hosts and sets the default port.
     * @throws BuildException on error
     */
    public void init()
//    		throws BuildException 
    {
//        super.init();
        this.knownHosts = System.getProperty("user.home") + "/.ssh/known_hosts";
        this.port = SSH_PORT;
    }

    /**
     * Open an ssh session.
     * @return the opened session
     * @throws JSchException on error
     */
    protected Session openSession() throws JSchException {
        final JSch jsch = new JSch();
        final SSHBase base = this;
        if (verbose) {
            JSch.setLogger(new com.jcraft.jsch.Logger(){
                    public boolean isEnabled(final int level){
                        return true;
                    }
                    public void log(final int level, final String message){
//                        base.log(message, Project.MSG_INFO);
                    }
                });
        }
        if (null != userInfo.getKeyfile()) {
            jsch.addIdentity(userInfo.getKeyfile());
        }

        if (!userInfo.getTrust() && knownHosts != null) {
//            log("Using known hosts: " + knownHosts, Project.MSG_DEBUG);
            jsch.setKnownHosts(knownHosts);
        }

        final Session session = jsch.getSession(userInfo.getName(), host, port);
        session.setConfig("PreferredAuthentications",
                "publickey,keyboard-interactive,password");
        session.setUserInfo(userInfo);
//        log("Connecting to " + host + ":" + port);
        session.connect();
        return session;
    }

    /**
     * Get the user information.
     * @return the user information
     */
    protected SSHUserInfo getUserInfo() {
        return userInfo;
    }
}