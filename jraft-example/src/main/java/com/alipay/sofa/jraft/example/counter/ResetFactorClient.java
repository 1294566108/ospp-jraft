package com.alipay.sofa.jraft.example.counter;

import com.alipay.sofa.jraft.CliService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.CliServiceImpl;
import com.alipay.sofa.jraft.option.CliOptions;

/**
 * @author Akai
 */
public class ResetFactorClient {
    public static void main(String[] args) {
        CliService cliService = new CliServiceImpl();
        cliService.init(new CliOptions());
        //w=2,r=4  writeFactor=4,readFactor=6
        //w=4,r=2  writeFactor=8,readFactor=2
        int writeFactor = 8;
        int readFactor = 2;
        String groupId = "counter";
        String confStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083,127.0.0.1:8084,127.0.0.1:8085";
        final Configuration conf = new Configuration();
        if (!conf.parse(confStr)) {
            throw new IllegalArgumentException("Fail to parse conf:" + confStr);
        }
        cliService.resetFactor(groupId, conf, readFactor, writeFactor);
    }
}
