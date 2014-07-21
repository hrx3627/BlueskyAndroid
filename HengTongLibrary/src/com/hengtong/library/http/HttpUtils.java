package com.hengtong.library.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.hengtong.library.utils.FileUtils;
import com.hengtong.library.utils.LogControl;
import com.hengtong.library.utils.StringUtils;

/**
 * 功能：Http工具类,主要负责相关请求
 * @ahthor：黄荣星
 * @date:2013-11-11
 * @version::V1.0
 */
public class HttpUtils {
	private static final String TAG = "HttpUtils";
	private static final int CONN_TIMEOUT = 8 * 1000;
	private static final int SO_TIMEOUT = 20 * 1000;
	private static final int MIN_BUFFER = 1024;

	public static final String NET_ERROR = "HttpUtils_Net_Error";
	public static final String NET_ERROR_RESULT_NULL = "HttpUtils_Net_Error_Result_Null";// 服务返回空

	private static DefaultHttpClient createHttpClient(String url) {
		return createHttpClient(url, CONN_TIMEOUT, SO_TIMEOUT);
	}

	private static synchronized DefaultHttpClient createHttpClient(String url, int connTimeOut, int soTimeOut) {
		HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(httpParams, false);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(httpParams, connTimeOut);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpParams, soTimeOut);
		try {
			int port = 80;
			if (!StringUtils.isEmpty(url)) {
				url = url.replace("http://", "");
				int index = url.indexOf(':');
				int indexSeparator = url.indexOf('/');
				if (index > 0 && indexSeparator > 0) {
					port = Integer.valueOf(url.substring(index + 1, indexSeparator));
				}
			}
			LogControl.d(TAG, "port = ", port);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), port));
			ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
			return new DefaultHttpClient(clientConnectionManager, httpParams);
		} catch (Exception e) {
			LogControl.e(TAG, "createHttpClient===>Exception = ", e.getMessage());
			return new DefaultHttpClient(httpParams);
		}
	}

	/**
	 * 执行post操作
	 * @param url
	 * @param params
	 * @param charSet
	 * @return
	 */
	public static String doPost(String url, List<HttpHeader> headers, Object jsonObject, String charSet) {
		String result = null;
		HttpPost mHttpPost = null;
		DefaultHttpClient client = null;
		HttpResponse mHttpResponse = null;
		try {

			mHttpPost = new HttpPost(url);
			mHttpPost.addHeader("Accept-Encoding", "gzip");

			if (headers != null && !headers.isEmpty()) {
				for (HttpHeader lHeader : headers) {
					mHttpPost.addHeader(lHeader.getKey(), lHeader.getValue());
				}
			}

			if (charSet != null) {
				mHttpPost.setEntity(new StringEntity(jsonObject.toString(), charSet));
			} else {
				mHttpPost.setEntity(new StringEntity(jsonObject.toString()));
			}
			client = createHttpClient(url);
			LogControl.e("hrx", "url:" + url);
			mHttpResponse = client.execute(mHttpPost);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = mHttpResponse.getEntity().getContent();
				Header header = mHttpResponse.getEntity().getContentEncoding();
				if (header != null && header.getValue().contains("gzip")) {
					GZIPInputStream unGzip = new GZIPInputStream(is);
					result = StringUtils.getStringByStream(unGzip);
					// result = StringUtils.getStringByStream(unGzip, "UTF-8");
				} else {
					result = StringUtils.getStringByStream(is);
					// result = StringUtils.getStringByStream(is, "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogControl.e(TAG, "Exception:doPost() -> e = ", e.getMessage());
			result = NET_ERROR;
		} finally {
			if (mHttpResponse != null) {
				mHttpResponse = null;
			}
			if (client != null) {
				client = null;
			}
			if (mHttpPost != null) {
				mHttpPost.abort();
				mHttpPost = null;
			}
			if (StringUtils.isNullOrEmpty(result)) {
				result = NET_ERROR_RESULT_NULL;
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private static String decode(String str) {
		if (str != null && str.indexOf("%") < 6) {
			return URLDecoder.decode(str);
		}
		return str;
	}

	/**
	 * 执行post操作
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Object params, String charSet) {
		return doPost(url, null, params, charSet);
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * 执行get操作
	 * @param url
	 * @return
	 */
	public static String doGet(String url, List<HttpHeader> headers) {
		String result = null;
		HttpGet mHttpGet = null;
		DefaultHttpClient client = null;
		HttpResponse mHttpResponse = null;
		try {
			LogControl.i(TAG, "url:" + url);

			client = createHttpClient(url);
			mHttpGet = new HttpGet(URLEncoder.encode(url, "UTF-8"));
			mHttpGet.addHeader("Accept-Encoding", "gzip");
			mHttpGet.addHeader("Content-Type", "application/json;charset=utf-8");

			if (headers != null && !headers.isEmpty()) {
				for (HttpHeader lHeader : headers) {
					mHttpGet.addHeader(lHeader.getKey(), lHeader.getValue());
				}
			}

			mHttpResponse = client.execute(mHttpGet);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = mHttpResponse.getEntity().getContent();
				Header header = mHttpResponse.getEntity().getContentEncoding();
				if (header != null && header.getValue().contains("gzip")) {
					GZIPInputStream unGzip = new GZIPInputStream(is);
					result = StringUtils.getStringByStream(unGzip);
				} else {
					result = StringUtils.getStringByStream(is);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogControl.e(TAG, "doGet() -> e = ", e.getMessage());
			result = NET_ERROR;
		} finally {
			if (mHttpResponse != null) {
				mHttpResponse = null;
			}
			if (client != null) {
				client = null;
			}
			if (mHttpGet != null) {
				mHttpGet.abort();
				mHttpGet = null;
			}
		}
		return decode(result);
	}

	/**
	 * 执行下载操作
	 * @param url
	 * @param savePath
	 * @return
	 */
	public static boolean downLoad(String url, String savePath) {
		return downLoad(url, savePath, null);
	}

	/**
	 * 执行下载操作
	 * @param url
	 * @param savePath
	 * @param i 下载信息接口
	 * @return
	 */
	public static boolean downLoad(String url, String savePath, OnDownloadListener i) {
		InputStream is = null;
		FileOutputStream fos = null;
		HttpURLConnection conn = null;
		int totalLen = -1;
		int progressLen = 0;
		boolean error = false;
		try {
			conn = connection(url);
			conn.connect();
			int count = 0;
			while (count < 3 && (conn == null || (conn != null && conn.getResponseCode() != 200))) {
				conn = connection(url);
				conn.connect();
				LogControl.i(TAG, "downLoad() -> 连接错误第", count, "次");
				count++;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			if (conn == null || conn.getResponseCode() != 200) {
				if (i != null) {
					i.onError(HttpErrorType.CONN_ERROR);
				}
				return false;
			}

			is = conn.getInputStream();
			totalLen = conn.getContentLength();
			if (totalLen < MIN_BUFFER) {
				LogControl.e(TAG, "downLoad() -> 文件太小...");
				if (i != null) {
					i.onError(HttpErrorType.FILE_ERROR);
				}
				return false;
			}
			FileUtils.mkdir(savePath);
			fos = new FileOutputStream(savePath);
			int readLength = 0;
			byte[] buffer = new byte[MIN_BUFFER];

			while (readLength != -1) {
				readLength = is.read(buffer);
				if (i != null) {
					i.onProgressUpdate(totalLen, progressLen, progressLen * 100 / totalLen);
				}
				if (readLength > 0) {
					progressLen += readLength;
					fos.write(buffer, 0, readLength);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = true;
			if (i != null) {
				i.onError(HttpErrorType.NET_ERROR);
			}
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogControl.e(TAG, "downLoad() -> 关闭流错误,错误信息:" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (error) {
					File lTemp = new File(savePath);
					if (lTemp != null && lTemp.exists() && lTemp.isFile()) {
						lTemp.delete();
					}
					lTemp = null;
				}
			}
		}
		if (i != null) {
			if (progressLen == totalLen) {
				i.onEnd();
			} else if (progressLen != totalLen) {
				i.onError(HttpErrorType.FILE_ERROR);
			}
		}
		return progressLen == totalLen;
	}

	private static HttpURLConnection connection(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(SO_TIMEOUT);
			conn.setRequestProperty("Accept-Encoding", "identity");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public interface OnDownloadListener {
		public void onProgressUpdate(int aTotalLen, int aCurLen, int aPercent);

		public void onError(HttpErrorType aError);

		public void onEnd();
	}

	public enum HttpErrorType {
		NET_ERROR, FILE_ERROR, CONN_ERROR
	}

	public static String uploadFile(String url, String filePath) {
		return uploadFile(url, filePath, null);
	}

	public static String uploadFile(String url, String filePath, Class<? extends FileEntity> cls) {
		StringBuffer resultSB = new StringBuffer();
		boolean uploadSuccessed = postFile(url, filePath, null, resultSB, cls);
		if (uploadSuccessed) {
			return decode(resultSB.toString());
		} else {
			return null;
		}
	}

	public static String uploadFile(String url, String filePath, List<HttpHeader> headers, Class<? extends FileEntity> cls) {
		StringBuffer resultSB = new StringBuffer();
		boolean uploadSuccessed = postFile(url, filePath, headers, resultSB, cls);
		if (uploadSuccessed) {
			return decode(resultSB.toString());
		} else {
			return null;
		}
	}

	private static boolean postFile(String url, String filePath, List<HttpHeader> headers, StringBuffer resultStringBuf, Class<? extends FileEntity> cls) {
		if (StringUtils.isEmpty(url) || StringUtils.isEmpty(filePath)) {
			return false;
		}
		HttpClient mHttpClient = null;
		HttpPost mHttpPost = null;
		HttpResponse mHttpResponse = null;
		try {
			mHttpClient = createHttpClient(url, CONN_TIMEOUT, 10 * 1000);
			mHttpPost = new HttpPost(url);
			mHttpPost.addHeader("Accept-Encoding", "gzip");
			if (headers != null && !headers.isEmpty()) {
				for (HttpHeader lHeader : headers) {
					mHttpPost.addHeader(lHeader.getKey(), lHeader.getValue());
				}
			}
			FileEntity entity = null;
			if (cls == null) {
				entity = new FileEntity(new File(filePath), "binary/octet-stream");
			} else {
				Constructor<? extends FileEntity> con = cls.getConstructor(new Class[] { File.class, String.class });
				entity = con.newInstance(new File(filePath), "binary/octet-stream");
			}
			mHttpPost.setEntity(entity);

			mHttpResponse = mHttpClient.execute(mHttpPost);

			if (mHttpResponse != null && mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (resultStringBuf != null) {
					InputStream is = mHttpResponse.getEntity().getContent();
					Header header = mHttpResponse.getEntity().getContentEncoding();
					if (header != null && header.getValue().contains("gzip")) {
						GZIPInputStream unGzip = new GZIPInputStream(is);
						resultStringBuf.append(StringUtils.getStringByStream(unGzip));
					} else {
						resultStringBuf.append(StringUtils.getStringByStream(is));
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
