package com.lhf.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhf.mode.JobStep;
import com.lhf.utils.ConnectionUtil;
import com.lhf.utils.DateUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author:95780
 * @Date: 11:42 2019/12/3
 * @Description: 此中任务必须予以捕获，要向mq通知，该流程失败
 */
public class Test05 {
    private static Logger logger = LogManager.getLogger(Test05.class);


    public static void main(String[] args) {
        try {
            ConnectionUtil.loadConf("application.properties");
        } catch (Exception e) {
            logger.error("获取配置文件失败：" + e);
        }
        Connection connection = null;
        Channel channel = null;
        String name = null;
        try {
            connection = ConnectionUtil.getConnectionRabbitMq(ConnectionUtil.rabbitmqaddresses, Integer.parseInt(ConnectionUtil.rabbitmqport), ConnectionUtil.rabbitmqvirtualhost, ConnectionUtil.rabbitmqusername, ConnectionUtil.rabbitmqpassword);
            // 从连接中创建通道
            channel = connection.createChannel();
            channel.queueDeclare(ConnectionUtil.QUEUE_SEND_NAME5, false, false, true, null);
            // 定义队列的消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            // 监听队列
            channel.basicConsume(ConnectionUtil.QUEUE_SEND_NAME5, true, consumer);
            // 获取消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message2 = new String(delivery.getBody());
            logger.info("接收的消息，通道是：" + ConnectionUtil.QUEUE_SEND_NAME5 + " Received：" + message2);
            JSONObject json = JSON.parseObject(message2);
            name = json.getString("name");
            String status = json.getString("status");
            String data = json.getString("data");
            String date = json.getString("date");
            if (("").equals(date) && !date.equals(DateUtils.parseDate(LocalDate.now().plusDays(-1), DateUtils.PATTERN_DATE_NO_SPLIT_CHAR))) {
                logger.info("指定更新" + date + "任务" + " 任务名：" + name);
            } else {
            }

            /*
            手动制造异常
            String str1 = null;
            str1.equals("123");*/

            JobStep jobStep = new JobStep();
            jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME5);
            jobStep.setJobstatus("1");
            jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
//            JobStep jobStep1 = ConnectionUtil.getJobStep(QUEUE_SEND_NAME5, sqlforname, sqladdress, sqlusername, sqlpassword);
            int update = ConnectionUtil.update(jobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
            logger.info("执行任务：" + name + "更新为已执行，更新条数：" + update);
            logger.info("开始执行任务：" + name);
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(1000);
                logger.info("休眠，模拟正在执行任务。。。。。" + i);
            }
            jobStep.setJobstatus("2");
            jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
//            JobStep jobStep1 = ConnectionUtil.getJobStep(QUEUE_SEND_NAME5, sqlforname, sqladdress, sqlusername, sqlpassword);
            int update1 = ConnectionUtil.update(jobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
            JSONObject object1 = new JSONObject();
            object1.put("status", "0000");
            object1.put("data", "jobstep执行完成");
            object1.put("name", name);
            channel.basicPublish("", ConnectionUtil.QUEUE_REV_NAME5, null, object1.toJSONString().getBytes());

            logger.info("发送的消息，通道是：" + ConnectionUtil.QUEUE_REV_NAME5 + " sends：" + object1.toJSONString());
        } catch (Exception e) {
            try {
                JobStep jobStep = new JobStep();
                jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME5);
                jobStep.setJobstatus("3");
                jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
                int update = ConnectionUtil.update(jobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
                connection = ConnectionUtil.getConnectionRabbitMq(ConnectionUtil.rabbitmqaddresses, Integer.parseInt(ConnectionUtil.rabbitmqport), ConnectionUtil.rabbitmqvirtualhost, ConnectionUtil.rabbitmqusername, ConnectionUtil.rabbitmqpassword);
                // 从连接中创建通道
                channel = connection.createChannel();
                JSONObject object1 = new JSONObject();
                object1.put("status", "1111");
                object1.put("data", "jobstep执行失败");
                object1.put("name", name);
                object1.put("createtim", DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_NO_SECOND));
                channel.basicPublish("", ConnectionUtil.QUEUE_REV_NAME5, null, object1.toJSONString().getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                try {
                    channel.close();
                    connection.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
