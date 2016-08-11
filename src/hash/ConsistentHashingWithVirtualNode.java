package hash;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author LCZ
 * @date 创建时间：2016年2月15日 下午4:04:16
 * @description 带虚拟节点的一致性Hash算法
 * @version 1.0
 */
public class ConsistentHashingWithVirtualNode {

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
	 * 真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
	 * */
	private static List<String> realNodes = new LinkedList<String>();

	/**
	 * 虚拟节点 ，key表示虚拟节点的hash值，value表示虚拟节点的名称
	 * */
	private static SortedMap<Integer, String> virtualNodes = new TreeMap<Integer, String>();

	/**
	 * 虚拟节点的数目，测试时指定为5个虚拟节点
	 * */
	private static final int VIRTUAL_NODES = 5;

	/**
	 * 程序初始化，将所有的服务器放入sortedMap中
	 * */
	static {
		// 添加服务器到真实节点列表
		for (int i = 0; i < servers.length; i++) {
			realNodes.add(servers[i]);
		}

		// 再添加虚拟节点
		for (String str : realNodes) {
			for (int i = 0; i < VIRTUAL_NODES; i++) {
				String virtualNodeName = str + "&&VN" + String.valueOf(i);
				int hash = getHash(virtualNodeName);
				System.out.println("虚拟节点[" + virtualNodeName + "]被添加，hash值为"
						+ hash);
				virtualNodes.put(hash, virtualNodeName);
			}
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
		SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);

		// 第一个就是对应离node最近的节点
		Integer i = subMap.firstKey();

		// 返回对应的服务器名
		String virtualNode = subMap.get(i);
		return virtualNode.substring(0, virtualNode.indexOf("&&"));
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
