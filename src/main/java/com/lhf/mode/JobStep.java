package com.lhf.mode;

/**
 * @Author:95780
 * @Date: 17:09 2019/12/2
 * @Description:
 */
public class JobStep {
    private String id;
    private String jobname;
    /**
     * 0、创建
     * 1、执行
     * 2、执行完成
     * 3、失败
     */
    private String jobstatus;
    private String jobcreatetime;
    private String jobupdatetime;

    public JobStep() {
        super();
    }

    public JobStep(String id, String jobname, String jobstatus, String jobcreatetime, String jobupdatetime) {
        this.id = id;
        this.jobname = jobname;
        this.jobstatus = jobstatus;
        this.jobcreatetime = jobcreatetime;
        this.jobupdatetime = jobupdatetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getJobstatus() {
        return jobstatus;
    }

    public void setJobstatus(String jobstatus) {
        this.jobstatus = jobstatus;
    }

    public String getJobcreatetime() {
        return jobcreatetime;
    }

    public void setJobcreatetime(String jobcreatetime) {
        this.jobcreatetime = jobcreatetime;
    }

    public String getJobupdatetime() {
        return jobupdatetime;
    }

    public void setJobupdatetime(String jobupdatetime) {
        this.jobupdatetime = jobupdatetime;
    }
}
