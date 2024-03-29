package cn.ksafe.ssrsub;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SSRParser {
    private final static Log log = LogFactory.getLog(SSRParser.class);
    private static Pattern pattern_ssr = Pattern.compile("(?i)ssr://([A-Za-z0-9_=-]+)");
    private static Pattern decodePattern_ssr = Pattern.compile("(?i)^(.+):(\\d+?):(.*):(.+):(.*):([^/]+)");
    private static Pattern decodePattern_ssr_obfsparam = Pattern.compile("(?i)[?&]obfsparam=([A-Za-z0-9_=-]*)");
    private static Pattern decodePattern_ssr_remark = Pattern.compile("(?i)[?&]remarks=([A-Za-z0-9_=-]*)");
    private static Pattern decodePattern_ssr_protoparam = Pattern.compile("(?i)[?&]protoparam=([A-Za-z0-9_=-]*)");
    private static Pattern decodePattern_ssr_group = Pattern.compile("(?i)[?&]group=([A-Za-z0-9_=-]*)");
    private static Pattern pattern_ip = Pattern.compile("(?i)^(\\d{1,3}\\.){3}\\d{1,3}$");

    private static Base64.Decoder _decode = Base64.getUrlDecoder();

    private static String decode(String content) {
        return new String(_decode.decode(content.replaceAll("=", ""))).trim();
    }

    private static String encRemarks(String remarks) {
        remarks = remarks.replaceAll(" ", "_");
        remarks = remarks.replaceAll("-", "_");
        remarks = remarks.replaceAll("线路", "_");

        remarks = remarks.replaceAll("香港", "HK_");
        remarks = remarks.replaceAll("韩国", "KR_");
        remarks = remarks.replaceAll("日本", "JP_");
        remarks = remarks.replaceAll("美国", "US_");
        remarks = remarks.replaceAll("新加坡", "SG_");
        remarks = remarks.replaceAll("台湾", "TW_");
        remarks = remarks.replaceAll("澳门", "MO_");
        remarks = remarks.replaceAll("英国", "GB_");
        remarks = remarks.replaceAll("俄国", "RU_");
        remarks = remarks.replaceAll("俄罗斯", "RU_");
        remarks = remarks.replaceAll("欧洲", "EU_");


        remarks = remarks.replaceAll("国内", "CN_");

        remarks = remarks.replaceAll("游戏", "GANE_");
        remarks = remarks.replaceAll("优化", "OP_");

        remarks = remarks.replaceAll("高速", "HI_");

        remarks = remarks.replaceAll("中继", "TR_");
        remarks = remarks.replaceAll("中转", "TR_");
        remarks = remarks.replaceAll("标准", "ST_");

        remarks = remarks.replaceAll("__", "_");

        remarks = remarks.replaceAll("^_+", "");
        remarks = remarks.replaceAll("_+$", "");
        return remarks;
    }

    static Map<String, ShadowsocksRConfig> parser(String content, String group, boolean useRemarks, boolean noPrefix) {
        Map<String, ShadowsocksRConfig> map = new Hashtable<>();
        Matcher matcher = pattern_ssr.matcher(decode(content));
        while (matcher.find()) {
            ShadowsocksRConfig ssr = new ShadowsocksRConfig();
            String info = decode(matcher.group(1));
            log.debug("info: " + info);
            Matcher m = decodePattern_ssr.matcher(info);
            if (m.find()) {
                Matcher m2 = pattern_ip.matcher(m.group(1));
                ssr.setServer_name(m.group(1));
                if (m2.matches()) {
                    ssr.setServer(m.group(1));
                } else {
                    InetAddress giriAddress;
                    try {
                        giriAddress = InetAddress.getByName(m.group(1));
                        ssr.setServer(giriAddress.getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        ssr.setServer(m.group(1));
                    }
                }
                ssr.setServer_port(Integer.parseInt(m.group(2)));
                ssr.setProtocol(m.group(3));
                ssr.setMethod(m.group(4));
                ssr.setObfs(m.group(5));
                ssr.setPassword(decode(m.group(6)));
            }

            m = decodePattern_ssr_obfsparam.matcher(info);
            if (m.find()) {
                ssr.setObfs_param(decode(m.group(1)));
            }

            m = decodePattern_ssr_protoparam.matcher(info);
            if (m.find()) {
                ssr.setProtocol_param(decode(m.group(1)));
            }

            m = decodePattern_ssr_remark.matcher(info);
            if (m.find()) {
                ssr.setRemarks(decode(m.group(1)));
            }

            if (StringUtils.isEmpty(group)) {
                m = decodePattern_ssr_group.matcher(info);
                if (m.find()) {
                    ssr.setGroup(decode(m.group(1)));
                }
            } else {
                ssr.setGroup(group);
            }

            String key = ssr.getServer_name().substring(0, ssr.getServer().indexOf("."));
            if (useRemarks) {
                key = encRemarks(ssr.getRemarks());
            }

            if (!noPrefix) {
                key = ssr.getGroup() + "-" + key;
            }

            if (!map.containsKey(key)) {
                map.put(key, ssr);
            }
        }
        return map;
    }
}
