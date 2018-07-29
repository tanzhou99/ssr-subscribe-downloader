package cn.ksafe.ssrsub;

public class ShadowsocksRConfig {

    private String server;
    private int server_port;
    private String protocal;
    private String method;
    private String obfs;
    private String password;
    private String obfs_param;
    private String protocal_param;
    private String remarks;
    private String group;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getServer_port() {
        return server_port;
    }

    public void setServer_port(int server_port) {
        this.server_port = server_port;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getObfs() {
        return obfs;
    }

    public void setObfs(String obfs) {
        this.obfs = obfs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getObfs_param() {
        return obfs_param;
    }

    public void setObfs_param(String obfs_param) {
        this.obfs_param = obfs_param;
    }

    public String getProtocal_param() {
        return protocal_param;
    }

    public void setProtocal_param(String protocal_param) {
        this.protocal_param = protocal_param;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
