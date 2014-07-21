package com.hengtong.library.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.hengtong.library.utils.LogControl;

/**
 * 功能： Http网络请求中心
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HttpNetCenter extends Thread {
	private final String TAG = "HttpNetCenter";
	private Queue<HttpNetTask> mQueue;
	private Object mLock;
	private boolean mRun;
	private boolean mWait;
	private HttpNetTask mNetTask;
	private Queue<HttpNetTask> mAsyncQueue;
	private int mCurAsyncThread;
	private int mMaxAsyncThead;
	private List<String> mUrlList;
	private boolean mIgnoreSameTask;
	private Map<String, List<INetTask>> mForceInvokeMap;

	public static HttpNetCenter instance;

	private HttpNetCenter() {
		mQueue = new LinkedList<HttpNetTask>();
		mLock = new Object();
		mWait = false;
		mRun = true;
		mAsyncQueue = new LinkedList<HttpNetTask>();
		mCurAsyncThread = 0;
		mMaxAsyncThead = 3;
		mUrlList = new ArrayList<String>();
		mIgnoreSameTask = false;
		mForceInvokeMap = new HashMap<String, List<INetTask>>();
		this.start();
	}

	public static HttpNetCenter getInstance() {
		if (instance == null) {
			instance = new HttpNetCenter();
		}
		return instance;
	}

	private void waitThread() {
		if (!mWait) {
			synchronized (mLock) {
				try {
					mWait = true;
					mLock.wait();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 开始执行任务
	 */
	public void runThread() {
		if (mWait) {
			synchronized (mLock) {
				try {
					mWait = false;
					mLock.notify();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 是否忽略相同任务
	 * @return
	 */
	public boolean isIgnoreSameTask() {
		return mIgnoreSameTask;
	}

	/**
	 * 设置是否忽略相同任务
	 * @param aIgnoreSameTask
	 */
	public void setIgnoreSameTask(boolean aIgnoreSameTask) {
		this.mIgnoreSameTask = aIgnoreSameTask;
	}

	/**
	 * 添加Http网络任务
	 * @param aNetTask
	 */
	public void addTask(HttpNetTask aNetTask) {
		if (aNetTask == null) {
			return;
		}
		try {
			LogControl.e(TAG, "addTask ...");
			if (mIgnoreSameTask && mUrlList.contains(aNetTask.getUrl())) {
				LogControl.d(TAG, "Ignore same task:", aNetTask.getUrl());
				if (aNetTask.isForceToInvokeInterface()) {
					LogControl.d(TAG, "force to invoke interface ...");
					if (!mForceInvokeMap.containsKey(aNetTask.getUrl())) {
						mForceInvokeMap.put(aNetTask.getUrl(), new ArrayList<INetTask>());
					}
					mForceInvokeMap.get(aNetTask.getUrl()).add(aNetTask.getCallBack());
				}
				return;
			}
			if (mIgnoreSameTask)
				mUrlList.add(aNetTask.getUrl());
			mQueue.offer(aNetTask);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 添加Http网络任务并且运行线程
	 * @param aNetTask
	 */
	public void addTaskAndRun(HttpNetTask aNetTask) {
		if (aNetTask == null) {
			return;
		}
		try {
			if (mIgnoreSameTask && mUrlList.contains(aNetTask.getUrl())) {
				if (aNetTask.isForceToInvokeInterface()) {
					LogControl.d(TAG, "force to invoke interface ...");
					if (!mForceInvokeMap.containsKey(aNetTask.getUrl())) {
						mForceInvokeMap.put(aNetTask.getUrl(), new ArrayList<INetTask>());
					}
					mForceInvokeMap.get(aNetTask.getUrl()).add(aNetTask.getCallBack());
				}
				return;
			}
			if (mIgnoreSameTask)
				mUrlList.add(aNetTask.getUrl());
			mQueue.offer(aNetTask);
			runThread();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (mRun) {
			if (mQueue.isEmpty()) {
				waitThread();
			}
			String lUrl = "";
			try {
				mNetTask = mQueue.poll();
				if (mNetTask == null) {
					break;
				}
				lUrl = mNetTask.getUrl();
				String result = doGetResultFromNet(mNetTask);
				LogControl.d(TAG, "content:", result);
				mNetTask.getCallBack().onEnd(result);
				if (mIgnoreSameTask) {
					if (mNetTask.isForceToInvokeInterface() && mForceInvokeMap.containsKey(mNetTask.getUrl())) {
						for (INetTask lNetTask : mForceInvokeMap.get(mNetTask.getUrl())) {
							lNetTask.onEnd(result);
						}
						List<INetTask> lList = mForceInvokeMap.get(mNetTask.getUrl());
						lList.clear();
						lList = null;
						mForceInvokeMap.remove(mNetTask.getUrl());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				mUrlList.remove(lUrl);
				mNetTask = null;
			}
		}
	}

	/**
	 * 线程是否运行
	 * @return
	 */
	public boolean isRunning() {
		return mRun;
	}

	/**
	 * 停止线程
	 */
	public void stopThread() {
		mRun = false;
		runThread();
	}

	// 以上为单线控制
	// ******************* 华丽的分割线 ******************
	// 以下为多线程操作

	/**
	 * 设置最大异步线程数量
	 */
	public void setMaxAsyncThread(int aMaxAsyncThead) {
		mMaxAsyncThead = aMaxAsyncThead;
	}

	/**
	 * 添加异步线程任务
	 * @param aNetTask
	 */
	public void addAsyncTask(HttpNetTask aNetTask) {
		if (aNetTask == null) {
			return;
		}
		try {
			if (mIgnoreSameTask && mUrlList.contains(aNetTask.getUrl())) {
				LogControl.d(TAG, "Ignore same task:", aNetTask.getUrl());
				if (aNetTask.isForceToInvokeInterface()) {
					LogControl.d(TAG, "force to invoke interface ...");
					if (!mForceInvokeMap.containsKey(aNetTask.getUrl())) {
						mForceInvokeMap.put(aNetTask.getUrl(), new ArrayList<INetTask>());
					}
					mForceInvokeMap.get(aNetTask.getUrl()).add(aNetTask.getCallBack());
				}
				return;
			}
			if (mIgnoreSameTask)
				mUrlList.add(aNetTask.getUrl());
			mAsyncQueue.offer(aNetTask);
			checkAsyncThreads();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private HttpNetTask getAsyncTaskFromQueue() {
		synchronized (mAsyncQueue) {
			try {
				if (!mAsyncQueue.isEmpty()) {
					return mAsyncQueue.poll();
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private enum OpType {
		GET_SIZE, ADD, SUBTRACTION
	}

	@SuppressWarnings("incomplete-switch")
	private synchronized int opAsyncThreadNum(OpType opType) {
		switch (opType) {
		case ADD:
			++mCurAsyncThread;
			break;
		case SUBTRACTION:
			--mCurAsyncThread;
			break;
		}
		return mCurAsyncThread;
	}

	private void checkAsyncThreads() {
		if (opAsyncThreadNum(OpType.GET_SIZE) < mMaxAsyncThead) {
			new NetAsyncThread().start();
			opAsyncThreadNum(OpType.ADD);
		}
	}

	private String doGetResultFromNet(HttpNetTask lNetTask) throws Exception {
		String lUrl = lNetTask.getUrl();
		LogControl.d(TAG, "url=", lUrl);
		switch (lNetTask.getRequestType()) {
		case GET:
			return HttpUtils.doGet(lUrl);
		case POST:
			return HttpUtils.doPost(lUrl,lNetTask.getHeaders(),lNetTask.getPostParams(), "UTF-8");
		case UPLOAD:
			return HttpUtils.uploadFile(lUrl, lNetTask.getFilePath(), lNetTask.getHeaders(), null);
		case DOWNLOAD:
			return HttpUtils.downLoad(lUrl, lNetTask.getFilePath()) ? lNetTask.getFilePath() : null;
		}
		return null;
	}

	private class NetAsyncThread extends Thread {
		private boolean mAsyncRun;

		public NetAsyncThread() {
			mAsyncRun = true;
		}

		public void run() {
			while (mAsyncRun) {
				hasWork();
			}
		}

		private void hasWork() {
			HttpNetTask lNetTask = getAsyncTaskFromQueue();
			if (lNetTask != null) {
				String lUrl = lNetTask.getUrl();
				try {
					String result = doGetResultFromNet(lNetTask);
					LogControl.d(TAG, "content:", result);
					lNetTask.getCallBack().onEnd(result);
					if (mIgnoreSameTask) {
						if (lNetTask.isForceToInvokeInterface() && mForceInvokeMap.containsKey(lNetTask.getUrl())) {
							for (INetTask item : mForceInvokeMap.get(lNetTask.getUrl())) {
								item.onEnd(result);
							}
							List<INetTask> lList = mForceInvokeMap.get(lNetTask.getUrl());
							lList.clear();
							lList = null;
							mForceInvokeMap.remove(lNetTask.getUrl());
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					mUrlList.remove(lUrl);
				}
			} else {
				mAsyncRun = false;
				opAsyncThreadNum(OpType.SUBTRACTION);
			}
		}
	}

}
