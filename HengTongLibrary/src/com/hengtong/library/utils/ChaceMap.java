package com.hengtong.library.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * 功能：缓存图
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version:V1.0
 */
public class ChaceMap<M> {

	private Map<M, SoftReference<Object>> map = null;
	private List<Manager> list = null;
	private int maxSize = 0;

	/**
	 * Constructor
	 * @param maxSize 最大数量
	 */
	public ChaceMap(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * 将键名为key的object对象放在缓存图中
	 * @param key
	 * @param object
	 */
	public void put(M key, Object object) {
		if (object == null) {
			return;
		}
		if (map == null || list == null) {
			if (map == null)
				map = new HashMap<M, SoftReference<Object>>();
			if (list == null)
				list = new ArrayList<Manager>();
		}
		remove();
		SoftReference<Object> mSoftReference = new SoftReference<Object>(object);
		map.put(key, mSoftReference);
		list.add(new Manager(key, System.currentTimeMillis()));
	}

	private class ComparatorManager implements Comparator<Manager> {
		@Override
		public int compare(Manager m1, Manager m2) {
			// TODO Auto-generated method stub
			return (m1.getInsertTime() < m2.getInsertTime() ? -1 : (m1.getInsertTime() == m2.getInsertTime() ? 0 : 1));
		}
	}

	private void remove() {
		if (map.size() > maxSize) {
			List<Manager> tempList = new ArrayList<Manager>();
			Collections.sort(tempList, new ComparatorManager());
			for (int i = 0; i < list.size() && map.size() > maxSize; i++) {
				map.remove(list.get(i).getKey());
				list.remove(i);
			}
		}
	}

	/**
	 * 获取键名为key的对象
	 * @param key
	 * @return 如果没有以key为键名的对象返回null
	 */
	public Object get(M key) {
		if (map == null) {
			return null;
		}

		SoftReference<Object> mSoftReference = map.get(key);

		if (mSoftReference == null) {
			map.remove(key);
			return null;
		}

		return mSoftReference.get();
	}

	/**
	 * 缓存图当前以缓存对象数量
	 * @return
	 */
	public int size() {
		if (map != null)
			return map.size();
		return 0;
	}

	private class Manager {

		private M key;
		private long insertTime;

		public Manager(M key, long insertTime) {
			this.key = key;
			this.insertTime = insertTime;
		}

		public M getKey() {
			return key;
		}

		public long getInsertTime() {
			return insertTime;
		}

		@Override
		public String toString() {
			return "Manager [key=" + key + ", insertTime=" + insertTime + "]";
		}

	}

	/**
	 * 释放资源
	 */
	public void release() {
		try {
			if (map != null) {
				for (int i = 0; i < map.size(); i++) {
					SoftReference<Object> softReferenceObj = map.get(i);
					if (softReferenceObj != null) {
						Object obj = softReferenceObj.get();
						if (obj instanceof Bitmap) {
							((Bitmap) obj).recycle();
						}
						obj = null;
					}
				}
				map.clear();
			}
			map = null;
			if (list != null) {
				list.clear();
			}
			list = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
