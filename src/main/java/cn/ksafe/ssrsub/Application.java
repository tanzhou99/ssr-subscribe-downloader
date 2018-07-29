package cn.ksafe.ssrsub;

import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Application {
    private final static Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        Options options = new Options();

        Option urlOpt = new Option("u", "url", true, "input subscribe url");
        urlOpt.setRequired(true);
        options.addOption(urlOpt);

        Option groupOpt = new Option("g", "group", true, "input shadowsocksr group");
        groupOpt.setRequired(true);
        options.addOption(groupOpt);

        Option savePathOpt = new Option("s", "save-path", true, "shadowsocksr config save path");
        savePathOpt.setRequired(false);
        options.addOption(savePathOpt);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String url = cmd.getOptionValue("url");
        String group = cmd.getOptionValue("group");
        String savePath = cmd.getOptionValue("save-path","/etc/shadowsocksr");


        String content = HttpRequest.sendGetRequest(url);
        Map<String, ShadowsocksRConfig> map = SSRParser.parser(content, group);
        Gson gson = new Gson();
        for (String key : map.keySet()) {
            log.info(key + ": " + gson.toJson(map.get(key)));
            File file = new File(savePath.replaceAll("/$", "") + "/" + key + ".json");
            try {
                FileUtils.writeStringToFile(file, gson.toJson(map.get(key)),"utf-8");
            } catch (IOException e) {
                log.warn(e);
            }
        }
    }
}
