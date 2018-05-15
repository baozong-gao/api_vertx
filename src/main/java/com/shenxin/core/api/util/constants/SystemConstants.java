package com.shenxin.core.api.util.constants;


/**
 * 常量类，包括配置文件的常量和程序定义的常量
 *
 */
public final class SystemConstants extends ConfigurableContants {

	static {
		init("/system.properties");
	}

    public static final int port =  getIntProperty("server.port",8088);

    public static final int vertx_work_pool =  getIntProperty("vertx.work.pool",30);

	public static final int service_thread_count =  getIntProperty("service.thread.count",30);

	public static final int web_thread_count =  getIntProperty("service.thread.count",10);

	public static final int asyn_thread_count =  getIntProperty("asyn.thread.count",3);

	public static final int dubbo_thread_count =  getIntProperty("asyn.thread.count",5);


	//mongodb
	public static final String mongo_ip =  getProperty("mongo.ip","172.16.1.24");
	public static final int mongo_port =  getIntProperty("mongo.port",10001);

}
