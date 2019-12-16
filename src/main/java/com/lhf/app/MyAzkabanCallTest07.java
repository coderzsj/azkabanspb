package com.lhf.app;

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

import java.time.LocalDateTime;

/**
 * @Author:95780
 * @Date: 19:26 2019/11/29
 * @Description:
 */
public class MyAzkabanCallTest07 {
    private static Logger logger = LogManager.getLogger(MyAzkabanCallTest07.class);


    public static void main(String[] args) throws Exception {
        try {
            ConnectionUtil.loadConf("application.properties");
        } catch (Exception e) {
            logger.error("===============FAILURE==============");
            logger.error("获取配置文件失败：" + e);
            throw new Exception("获取配置文件失败：" + e);
        }
        logger.info("++++++++++++++++ BEGIN " + DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA) + " +++++++++++++++++");
        // 获取到连接以及mq通道
        JobStep jobStep = new JobStep();
        jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME7);
        jobStep.setJobstatus("0");
        jobStep.setJobcreatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
        jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
        JobStep jobStep1 = ConnectionUtil.getJobStep(ConnectionUtil.QUEUE_SEND_NAME7, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
        if (jobStep1.getJobstatus() != null && jobStep1.getJobstatus().equals("2")) {
            logger.error("===============FAILURE==============");
            logger.error("任务已存在：" + ConnectionUtil.QUEUE_SEND_NAME7);
            throw new Exception("任务已存在：" + ConnectionUtil.QUEUE_SEND_NAME7);
        }
        int insert = ConnectionUtil.insert(jobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
        logger.info("创建任务：" + jobStep + " " + insert);
        Connection connection = ConnectionUtil.getConnectionRabbitMq(ConnectionUtil.rabbitmqaddresses, Integer.parseInt(ConnectionUtil.rabbitmqport), ConnectionUtil.rabbitmqvirtualhost, ConnectionUtil.rabbitmqusername, ConnectionUtil.rabbitmqpassword);
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        try {
            // 声明（创建）队列
            //queue 队列的名称
            //durable: 设置是否持久化。为 true 则设置队列为持久化。持久化的队列会存盘，在 服务器重启的时候可以保证不丢失相关信息
            //exclusive 设置是否排他。为 true 则设置队列为排他的。如果一个队列被声明为排 他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。这里需要注意 三点:排他队列是基于连接( Connection) 可见的，同 个连接的不同信道 (Channel) 是可以同时访问同一连接创建的排他队列; "首次"是指如果 个连接己经声明了 排他队列，其他连接是不允许建立同名的排他队列的，这个与普通队列不同:即使该队 列是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除，这种队列 适用于一个客户端同时发送和读取消息的应用场景。
            //autoDelete: 设置是否自动删除。为 true 则设置队列为自动删除。自动删除的前提是: 至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会 自动删除。不能把这个参数错误地理解为: "当连接到此队列的所有客户端断开时，这 个队列自动删除"，因为生产者客户端创建这个队列，或者没有消费者客户端与这个队 列连接时，都不会自动删除这个队列。
            //argurnents: 设置队列的其他一些参数，如 x-rnessage-ttl 、x-expires 、x-rnax-length 、x-rnax-length-bytes、 x-dead-letter-exchange、 x-deadletter-routing-key 、 x-rnax-priority 等。
            //Queue.DeclareOk queueDeclare (String queue,boolean durable,boolean exclusive,boolean autoDelete,Map arguments) throws IOException;
//            channel.queueDeclare(ConnectionUtil.QUEUE_SEND_NAME7, false, false, true, null);
//            String message1 = "";
            JSONObject object1 = new JSONObject();
            object1.put("status", "0000");
            object1.put("data", "jobstep准备执行");
            object1.put("name", "jobstep01");
            object1.put("date", "");
            object1.put("createtim", DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_NO_SECOND));
            // 消息内容
            if (args.length != 0) {
                object1.put("date", args[0]);
//                message1 = "start job begin" + QUEUE_SEND_NAME7 + args[0];
            }
            //exchange -- 交换机名称
            // routingKey -- 路由关键字
            //BasicProperties props -- 消息的基本属性，例如路由头等
            //byte[] body -- 消息体
            //basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
            channel.basicPublish("", ConnectionUtil.QUEUE_SEND_NAME7, null, object1.toJSONString().getBytes());
            logger.info("发送的消息，通道是：" + ConnectionUtil.QUEUE_SEND_NAME7 + " sends：" + object1.toJSONString());
            // 声明接收队列（创建）队列
            channel.queueDeclare(ConnectionUtil.QUEUE_REV_NAME7, false, false, true, null);
            // 定义队列的消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            // 监听队列
            channel.basicConsume(ConnectionUtil.QUEUE_REV_NAME7, true, consumer);
            // 获取消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message2 = new String(delivery.getBody());
            logger.info("接收的消息，通道是：" + ConnectionUtil.QUEUE_REV_NAME7 + " Received：" + message2);
            JSONObject json = JSON.parseObject(message2);
            String name = json.getString("name");
            String status = json.getString("status");
            String data = json.getString("data");
            if (!status.equals("0000")) {
                JobStep upJobStep = new JobStep();
                jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME7);
                jobStep.setJobstatus("3");
                jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
                int update = ConnectionUtil.update(upJobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
                logger.error("===============FAILURE==============");
                throw new Exception(name + "执行失败 " + "具体情况： " + data);
            }
//            if (message2.equals(""))
//            JobStep upJobStep = new JobStep();
//            upJobStep.setJobname(QUEUE_REV_NAME7);
//            upJobStep.setJobstatus("2");
//            upJobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
//            int update = ConnectionUtil.update(upJobStep,sqlforname, sqladdress, sqlusername, sqlpassword);
//            logger.info("更新完成 JobStep: " + QUEUE_REV_NAME7 + " 条数：" + update);
            logger.info(name + "执行成功 " + "具体情况： " + data);
            logger.info("===============SUCCEED==============");
        } catch (Exception e) {
            JobStep upJobStep = new JobStep();
            jobStep.setJobname(ConnectionUtil.QUEUE_SEND_NAME7);
            jobStep.setJobstatus("3");
            jobStep.setJobupdatetime(DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA));
            int update = ConnectionUtil.update(upJobStep, ConnectionUtil.sqlforname, ConnectionUtil.sqladdress, ConnectionUtil.sqlusername, ConnectionUtil.sqlpassword);
            logger.error("===============FAILURE==============");
            logger.error(e);
            throw new Exception(e);
        } finally {
            logger.info("++++++++++++++++ END " + DateUtils.parseDateTime(LocalDateTime.now(), DateUtils.PATTERN_DEFAULT_DATA) + " +++++++++++++++++");
            //关闭通道和连接
            try {
                channel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
