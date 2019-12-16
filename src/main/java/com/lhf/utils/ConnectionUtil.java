package com.lhf.utils;

import com.lhf.app.MyAzkabanCall;
import com.lhf.mode.JobStep;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @Author:95780
 * @Date: 19:24 2019/12/1
 * @Description: 工具类
 */
public class ConnectionUtil {
    public static String rabbitmqaddresses; //地址
    public static String rabbitmqusername; //用户名
    public static String rabbitmqpassword; //密码
    public static String rabbitmqport;
    public static String rabbitmqvirtualhost; //虚拟机主机
    public static String QUEUE_REV_NAME1;
    public static String QUEUE_SEND_NAME1;
    public static String QUEUE_REV_NAME2;
    public static String QUEUE_SEND_NAME2;
    public static String QUEUE_REV_NAME3;
    public static String QUEUE_SEND_NAME3;
    public static String QUEUE_REV_NAME4;
    public static String QUEUE_SEND_NAME4;
    public static String QUEUE_SEND_NAME5;
    public static String QUEUE_REV_NAME5;
    public static String QUEUE_SEND_NAME6;
    public static String QUEUE_REV_NAME6;
    public static String QUEUE_REV_NAME7;
    public static String QUEUE_SEND_NAME7;
    public static String QUEUE_REV_NAME8;
    public static String QUEUE_SEND_NAME8;
    public static String QUEUE_REV_NAME9;
    public static String QUEUE_SEND_NAME9;
    public static String QUEUE_REV_NAME10;
    public static String QUEUE_SEND_NAME10;
    public static String sqlforname;
    public static String sqladdress;
    public static String sqlusername;
    public static String sqlpassword;

    /**
     * @param host        服务地址
     * @param port        端口
     * @param virtualhost vhost
     * @param username    用户名
     * @param password    密码
     * @return com.rabbitmq.client.Connection
     * @throws Exception
     * @Description rabbitmq使用
     */
    public static Connection getConnectionRabbitMq(String host, int port, String virtualhost, String username, String password) throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost(host);
        //端口
        factory.setPort(port);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost(virtualhost);
        factory.setUsername(username);
        factory.setPassword(password
        );
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

    /**
     * @param forName  数据库驱动
     * @param address  数据库连接
     * @param username 用户名
     * @param password 密码
     * @param commit   是否自动提交
     * @return java.sql.Connection
     * @throws Exception
     * @Description 数据库连接Connection
     */
    public static java.sql.Connection getConnectionJdbc(String forName, String address, String username, String password, boolean commit) throws Exception {
        java.sql.Connection c = null;
        Class.forName(forName);
        c = DriverManager.getConnection(address, username, password);
        // 把自动提交
        c.setAutoCommit(commit);
        return c;
    }

    /**
     * @param jobStep
     * @return
     * @throws Exception
     */
    public static int insert(JobStep jobStep, String sqlforname, String sqladdress, String sqlusername, String sqlpassword) throws Exception {
        int i = 0;
        java.sql.Connection connectionJdbc = ConnectionUtil.getConnectionJdbc(sqlforname, sqladdress, sqlusername, sqlpassword, false);
        String insertsql = "insert into jobstep(jobname, jobstatus, jobcreatetime, jobupdatetime) values(?,?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) connectionJdbc.prepareStatement(insertsql);
            pstmt.setString(1, jobStep.getJobname());
            pstmt.setString(2, jobStep.getJobstatus());
            pstmt.setString(3, jobStep.getJobcreatetime());
            pstmt.setString(4, jobStep.getJobupdatetime());
            i = pstmt.executeUpdate();
            connectionJdbc.commit();
        } catch (Exception e) {
            throw new Exception("数据库insert操作异常： " + e);
        } finally {
            pstmt.close();
            connectionJdbc.close();
        }
        return i;
    }

    /**
     * @param jobname
     * @return
     */
    public static int delete(String jobname, String sqlforname, String sqladdress, String sqlusername, String sqlpassword) throws Exception {
        int i = 0;
        java.sql.Connection connectionJdbc = ConnectionUtil.getConnectionJdbc(sqlforname, sqladdress, sqlusername, sqlpassword, false);
        String sql = "delete from jobstep where jobname='" + jobname + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) connectionJdbc.prepareStatement(sql);
            i = pstmt.executeUpdate();
            connectionJdbc.commit();
        } catch (Exception e) {
            throw new Exception("数据库delete操作异常： " + e);
        } finally {
            pstmt.close();
            connectionJdbc.close();
        }
        return i;
    }

    /**
     * @param jobStep
     * @return
     * @throws Exception
     */
    public static int update(JobStep jobStep, String sqlforname, String sqladdress, String sqlusername, String sqlpassword) throws Exception {
        int i = 0;
        java.sql.Connection connectionJdbc = ConnectionUtil.getConnectionJdbc(sqlforname, sqladdress, sqlusername, sqlpassword, false);
        String sql = "update jobstep set jobstatus = '" + jobStep.getJobstatus() + "', jobupdatetime= '" + jobStep.getJobupdatetime() + "' where jobname='" + jobStep.getJobname() + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) connectionJdbc.prepareStatement(sql);
            i = pstmt.executeUpdate();
            connectionJdbc.commit();
        } catch (Exception e) {
            throw new Exception("数据库update操作异常： " + e);
        } finally {
            pstmt.close();
            connectionJdbc.close();
        }
        return i;
    }

    /**
     * @param jobname
     * @return
     * @throws Exception
     */
    public static JobStep getJobStep(String jobname, String sqlforname, String sqladdress, String sqlusername, String sqlpassword) throws Exception {
        java.sql.Connection connectionJdbc = ConnectionUtil.getConnectionJdbc(sqlforname, sqladdress, sqlusername, sqlpassword, false);
        JobStep jobStep = new JobStep();
        String sql = "select id,jobname,jobstatus,jobcreatetime,jobupdatetime from JobStep where  jobname='" + jobname + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) connectionJdbc.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobStep.setId(rs.getString("id"));
                jobStep.setJobname(rs.getString("jobname"));
                jobStep.setJobstatus(rs.getString("jobstatus"));
                jobStep.setJobcreatetime(rs.getString("jobcreatetime"));
                jobStep.setJobupdatetime(rs.getString("jobupdatetime"));
            }
        } catch (Exception e) {
            throw new Exception("数据库getJobStep操作异常： " + e);
        } finally {
            pstmt.close();
            connectionJdbc.close();
        }
        return jobStep;
    }
    /**
     * 初始化配置
     *
     * @param path
     * @throws Exception
     */
    public static void loadConf(String path) throws Exception {
        Properties props = new Properties();
        props.load(ConnectionUtil.class.getClassLoader().getResourceAsStream(path));
        rabbitmqaddresses = props.getProperty("rabbitmqaddresses");
        rabbitmqport = props.getProperty("rabbitmqport");
        rabbitmqusername = props.getProperty("rabbitmqusername");
        rabbitmqpassword = props.getProperty("rabbitmqpassword");
        rabbitmqvirtualhost = props.getProperty("rabbitmqvirtualhost");
        QUEUE_REV_NAME1 = props.getProperty("queue_rev_name1");
        QUEUE_SEND_NAME1 = props.getProperty("queue_send_name1");
        QUEUE_REV_NAME2 = props.getProperty("queue_rev_name2");
        QUEUE_SEND_NAME2 = props.getProperty("queue_send_name2");
        QUEUE_REV_NAME3 = props.getProperty("queue_rev_name3");
        QUEUE_SEND_NAME3 = props.getProperty("queue_send_name3");
        QUEUE_REV_NAME4 = props.getProperty("queue_rev_name4");
        QUEUE_SEND_NAME4 = props.getProperty("queue_send_name4");
        QUEUE_REV_NAME5 = props.getProperty("queue_rev_name5");
        QUEUE_SEND_NAME5 = props.getProperty("queue_send_name5");
        QUEUE_REV_NAME6 = props.getProperty("queue_rev_name6");
        QUEUE_SEND_NAME6 = props.getProperty("queue_send_name6");
        QUEUE_REV_NAME7 = props.getProperty("queue_rev_name7");
        QUEUE_SEND_NAME7 = props.getProperty("queue_send_name7");
        QUEUE_REV_NAME8 = props.getProperty("queue_rev_name8");
        QUEUE_SEND_NAME8 = props.getProperty("queue_send_name8");
        QUEUE_REV_NAME9 = props.getProperty("queue_rev_name9");
        QUEUE_SEND_NAME9 = props.getProperty("queue_send_name9");
        QUEUE_REV_NAME10 = props.getProperty("queue_rev_name10");
        QUEUE_SEND_NAME10 = props.getProperty("queue_send_name10");
        sqlforname = props.getProperty("sqlforname");
        sqladdress = props.getProperty("sqladdress");
        sqlusername = props.getProperty("sqlusername");
        sqlpassword = props.getProperty("sqlpassword");
    }
}
