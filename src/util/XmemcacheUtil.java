package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * @author sina Email:chaozhen7@163.com
 * @date 2016年8月11日 上午10:42:32
 * @version 1.0
 */
public class XmemcacheUtil {
	private static final Logger LOGGER = Logger.getLogger(XmemcacheUtil.class);
	private static MemcachedClient xMemcachedClient;
	static{
//		初始化时配合Spring加载使用
//		if(appContext == null){
//			appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//			/*String [] name = appContext.getBeanDefinitionNames();
//			for(String s:name){
//				System.out.println(s);
//			}*/
//			xMemcachedClient = (MemcachedClient)appContext.getBean("XmemcachedClient");
//		}
	}
	private XmemcacheUtil() {

	}

	/**
	 * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expire
	 *            过期时间 New Date(1000*10)：十秒后过期
	 * @return
	 */
	public static boolean setExp(String key, Object value, int expire) {
		boolean flag = false;
		try {
			flag = xMemcachedClient.set(key, expire, value);

		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached set方法报错，key值：" + key + "\r\n"
					+ exceptionWrite(e));
		}

		return flag;
	}

	/**
	 * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expire
	 *            过期时间 New Date(1000*10)：十秒后过期
	 * @return
	 */
	public static boolean addExp(String key, Object value, int expire) {
		boolean flag = false;
		try {
			flag = xMemcachedClient.add(key, expire, value);
		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached add方法报错，key值：" + key + "\r\n"
					+ exceptionWrite(e));
		}
		return flag;
	}

	/**
	 * 仅当键已经存在时，replace 命令才会替换缓存中的键。
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expire
	 *            过期时间 New Date(1000*10)：十秒后过期
	 * @return
	 */
	public static boolean replaceExp(String key, Object value, int expire) {
		boolean flag = false;
		try {
			flag = xMemcachedClient.replace(key, expire, value);
		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached replace方法报错，key值：" + key + "\r\n"
					+ exceptionWrite(e));
		}
		return flag;
	}

	/**
	 * get 命令用于检索与之前添加的键值对相关的值。
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static Object get(String key) {
		Object value = null;
		try {
			value = xMemcachedClient.get(key);
		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached get方法报错，key值：" + key + "\r\n"
					+ exceptionWrite(e));
		}
		return value;
	}

	/**
	 * 删除 memcached 中的任何现有值。
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static boolean deleteExp(String key) {
		boolean flag = false;
		try {
			flag = xMemcachedClient.delete(key);
		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached delete方法报错，key值：" + key + "\r\n"
					+ exceptionWrite(e));
		}
		return flag;
	}

	/**
	 * 清理缓存中的所有键/值对
	 * 
	 * @return
	 */
	public static void flushAll() {

		try {
			xMemcachedClient.flushAll();
		} catch (Exception e) {
			// TODO: handle exception
			MemcachedLog.writeLog("XMemcached flushAll方法报错\r\n"
					+ exceptionWrite(e));
		}

	}

	/**
	 * 返回异常栈信息，String类型
	 * 
	 * @param e
	 * @return
	 */
	private static String exceptionWrite(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	/**
	 * 
	 * @ClassName: MemcachedLog
	 * @Description: Memcached日志记录
	 * 
	 */
	private static class MemcachedLog {
		private final static String MEMCACHED_LOG = "D:\\Xmemcached.log";
		private final static String LINUX_MEMCACHED_LOG = "/usr/local/logs/Xmemcached.log";
		private static FileWriter fileWriter;
		private static BufferedWriter logWrite;
		// 获取PID，可以找到对应的JVM进程
		private final static RuntimeMXBean runtime = ManagementFactory
				.getRuntimeMXBean();
		private final static String PID = runtime.getName();

		/**
		 * 初始化写入流
		 */
		static {
			try {
				String osName = System.getProperty("os.name");
				if (osName.indexOf("Windows") == -1) {
					fileWriter = new FileWriter(MEMCACHED_LOG, true);
				} else {
					fileWriter = new FileWriter(LINUX_MEMCACHED_LOG, true);
				}
				logWrite = new BufferedWriter(fileWriter);
			} catch (IOException e) {
				LOGGER.error("memcached 日志初始化失败", e);
				closeLogStream();
			}
		}

		/**
		 * 写入日志信息
		 * 
		 * @param content
		 *            日志内容
		 */
		public static void writeLog(String content) {
			try {
				logWrite.write("["
						+ PID
						+ "] "
						+ "- ["
						+ new SimpleDateFormat("yyyy年-MM月-dd日 hh时:mm分:ss秒")
								.format(new Date().getTime()) + "]\r\n"
						+ content);
				logWrite.newLine();
				logWrite.flush();
			} catch (IOException e) {
				LOGGER.error("memcached 写入日志信息失败", e);
			}
		}

		/**
		 * 关闭流
		 */
		private static void closeLogStream() {
			try {
				fileWriter.close();
				logWrite.close();
			} catch (IOException e) {
				LOGGER.error("memcached 日志对象关闭失败", e);
			}
		}
	}
}
