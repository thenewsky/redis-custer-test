package me.zhujin.java.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;

public class RedisClusterTest {

	protected static Logger logger = Logger.getLogger(RedisClusterTest.class);
	private static JedisCluster redisCluster;
	private static String ip = "192.168.2.150";
	private static int port_begin = 6000;
	private static int port_end = 6010;
	static Random random = new Random();

	public RedisClusterTest() {
		
	}
	static  {
		
		init();
	}
	private static void init() {
		Set<HostAndPort> redisClusterNodes = new HashSet<HostAndPort>();
		redisClusterNodes.add(new HostAndPort(ip, port_begin));
		for (int i = port_begin; i <=port_end; i++) {
			redisClusterNodes.add(new HostAndPort(ip, i));
		}
		//
//		JedisPoolConfig poolConfig = new JedisPoolConfig();
//		poolConfig.setMaxIdle(30);
//		poolConfig.setMaxTotal(30);
//		poolConfig.setNumTestsPerEvictionRun(30);
//		poolConfig.setTestWhileIdle(true);
//		poolConfig.setTimeBetweenEvictionRunsMillis(60000);
//		poolConfig.setMinEvictableIdleTimeMillis(60000);
		redisCluster = new JedisCluster(redisClusterNodes, 3000, 1000);
	}

	/*
	 * 释放错误链接
	 */
	protected static void returnBrokenResource(JedisPool jedisPool, Jedis jedis, String cmdName, String key,
			Exception e) {
		logger.error(format("释放错误连接:{0}->{1}", cmdName, key), e);
		if (jedis != null) {
			try {
				jedisPool.returnBrokenResource(jedis);
			} catch (Exception e1) {
				logger.error(format("释放资源:{0}->{1}失败", cmdName, key), e1);
			}
		}
	}

	protected static void returnResource(JedisPool jedisPool, Jedis jedis, String cmdName, String key) {
		if (jedis != null) {
			try {
				jedisPool.returnResource(jedis);
			} catch (Exception e) {
				logger.error(format("释放资源:{0}->{1}失败", cmdName, key), e);
			}
		}
	}

	public static void testSet() {
		int failTimes = 0;
		long before = System.currentTimeMillis();
		long b = System.currentTimeMillis();
		for (long i = 0; i < 1000100000L; i++) {
			try {
				redisCluster.set(i + ":" + i, "1");
				long a = System.currentTimeMillis();
				long subTime = a - b;
//				logger.info(subTime);
				if (i > 0 && i % 100000 == 0) {
					logger.info(String.format("第%s个 10万条数据 耗时:%s", i /100000, subTime ));
					b = a;
				}
			} catch (Exception e) {
				long a = System.currentTimeMillis();
				logger.info(String.format("第%s次插入失败, 耗时:%s, Error:%s", i + 1, a - b, e.getMessage()));
				failTimes++;
			}

		}

		long after = System.currentTimeMillis();
		System.out.println("Cost time " + (after - before));
		System.out.println("Fail times " + failTimes);
		 
		
	}

	public static void testGet() {
		init();
		int found = 0;
		long before = System.currentTimeMillis();
		for (long i = 0; i < 1000000L; i++) {
			String key = i + ":" + i;
			try {
				String value = redisCluster.get(key);
				if (value != null && !value.isEmpty()) {
					System.out.println(key + " not found");
				} else {
					found++;
				}
			} catch (Exception e) {
				System.out.println(key + " not found");
			}

		}
		long after = System.currentTimeMillis();
		System.out.println("Cost time " + (after - before));
		System.out.println("Total founded " + found);
	}

	/**
	 * 自定义格式输出文本{0} {1}
	 * 
	 * @param s
	 * @param objects
	 * @return
	 */
	public static String format(String s, Object... objects) {
		if (objects != null && objects.length > 0) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			sb.append("{").append(i).append("}");
			int j = s.indexOf(sb.toString());
			while (j >= 0) {
				if (i < objects.length) {
					s = s.replace(sb.toString(), objects[i] == null ? "" : objects[i].toString());
				}
				i++;
				sb = new StringBuilder();
				sb.append("{").append(i).append("}");
				j = s.indexOf(sb.toString());
			}
		}
		return s;
	}

	public static void main(String[] args) {
		testSet();
		// testGet();
	}
}