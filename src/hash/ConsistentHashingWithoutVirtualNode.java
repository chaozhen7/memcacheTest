package hash;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 不带虚拟节点的一致性Hash算法
 * */
public class ConsistentHashingWithoutVirtualNode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] nodes = { "127.0.0.1:1111", "221.226.0.1:2222",
				"10.211.0.1:3333" };
		for (int i = 0; i < nodes.length; i++) {
			System.out.println("[" + nodes[i] + "]的hash值为" + getHash(nodes[i])
					+ " ,对应路由节点为[" + getServer(nodes[i]) + "]");
		}
	}

	/**
	 * 待添加入Hash环的服务器列表
	 * */
	private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

	/**
	 * key表示服务器的hash值，value表示服务器的名称
	 * */
	private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

	/**
	 * 程序初始化，将所有的服务器放入sortedMap中
	 * */
	static {
		for (int i = 0; i < servers.length; i++) {
			int hash = getHash(servers[i]);
			System.out.println("[" + servers[i] + "]加入集合中，其hash值为" + hash);
			sortedMap.put(hash, servers[i]);
		}
		System.out.println();
	}

	/**
	 * 获取对应路由节点
	 * */
	private static String getServer(String node) {
		// 计算得到对应的hash
		int hash = getHash(node);

		// 获取大于改hash的所有子map
		SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);

		// 第一个就是对应离node最近的节点
		Integer i = subMap.firstKey();

		// 返回对应的服务器名
		return subMap.get(i);
	}

	/**
	 * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
	 * */
	private static int getHash(String str) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash ^ str.charAt(i)) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;

		// 如果为负数则取绝对值
		if (hash < 0) {
			hash = Math.abs(hash);
		}
		return hash;
	}
}
