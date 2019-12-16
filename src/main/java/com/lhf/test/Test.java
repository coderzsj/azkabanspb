package com.lhf.test;

import com.lhf.app.MyAzkabanCall;
import com.lhf.mode.JobStep;
import com.lhf.utils.ConnectionUtil;
import com.lhf.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Properties;

/**
 * @Author:95780
 * @Date: 14:12 2019/12/3
 * @Description:
 */
public class Test {
    private static Logger logger = LogManager.getLogger(Test.class);

    public static void main(String[] args) throws Exception {
        try{
            ConnectionUtil.loadConf("application.properties");
        } catch (Exception e){
            logger.error("获取配置文件失败：" + e);
        }
        JobStep jobStep = new JobStep();
        jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME1);
        jobStep.setJobstatus("2");
        jobStep.setJobcreatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
        jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
        int insert = ConnectionUtil.insert(jobStep,ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
        logger.info(insert);
    }
}
