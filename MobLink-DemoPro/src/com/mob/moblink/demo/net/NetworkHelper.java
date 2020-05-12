package com.mob.moblink.demo.net;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by weishj on 2017/12/20.
 */

public class NetworkHelper extends AbstractHttp{
	private static final String TAG = "MobLink_NetworkHelper";

	public NetworkHelper() {
		super();
	}

	public NetworkHelper(int connectionTimeOut, int soTimeOut) {
		super(connectionTimeOut, soTimeOut);
	}

	private HttpURLConnection getHttpURLConnection(String url, HttpMethod httpMethod, Map<String,Object> params) throws IOException {

		if(HttpMethod.GET ==httpMethod){
			String paras = null;
			if(params!=null){
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for(String key : params.keySet()){
					list.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
				}
				paras = URLEncodedUtils.format(list, DEFAULT_ENCODING);
			}
			if (paras!=null) {
				url += URL_AND_PARA_SEPARATOR;
				url += paras;
			}
		}
		HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
		// Header
		httpURLConnection.setConnectTimeout(connectionTimeOut);
		httpURLConnection.setReadTimeout(soTimeOut);
		httpURLConnection.setUseCaches(false);

		String paramstr = null;
		if(HttpMethod.POST ==httpMethod){
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("POST");
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			paramstr = convertMapToParams(params);
			if(!TextUtils.isEmpty(paramstr))
				printWriter.write(paramstr);
				printWriter.flush();
		}else{
			httpURLConnection.setRequestMethod("GET");
		}
		Log.i(TAG, "=========== Http ==========");
		Log.i(TAG, "HEADER: "+ convertMaptoString(httpURLConnection.getHeaderFields()));
		Log.i(TAG, "PARAMS: " + paramstr);
		Log.i(TAG, "HTTP METHOD: "+ httpURLConnection.getRequestMethod());
		Log.i(TAG, "URL:"+ url);
		Log.i(TAG, "=========== Http ==========");
		return httpURLConnection;
	}

	@Override
	public void asyncConnect(String url, HttpCallBack<String> httpCallBack) {
		asyncConnect(url, null, httpCallBack);
	}

	@Override
	public void asyncConnect(String url, Map<String, Object> params,
							 HttpCallBack<String> httpCallBack) {
		asyncConnect(url, params, HttpMethod.POST, httpCallBack);

	}

	@Override
	public void asyncConnect(final String url, final Map<String, Object> params,
							 final HttpMethod httpMethod, final HttpCallBack<String> httpCallBack) {
		asyncThread(new Runnable() {
			@Override
			public void run() {
				syncConnect(url, params, httpMethod, httpCallBack);
			}
		});

	}

	@Override
	public String syncConnect(String url) {
		return syncConnect(url, null);
	}

	@Override
	public String syncConnect(String url, Map<String, Object> params) {
		return syncConnect(url, params, HttpMethod.POST);
	}

	@Override
	public String syncConnect(String url, Map<String, Object> params,
							  HttpMethod httpMethod) {
		return syncConnect(url, params, httpMethod, null);
	}

	@Override
	public String syncConnect(String url, Map<String, Object> params,
							  HttpCallBack<String> httpCallBack) {
		return syncConnect(url, params, HttpMethod.POST, httpCallBack);
	}

	@Override
	public String syncConnect(String url, Map<String, Object> params,
							  HttpMethod httpMethod, HttpCallBack<String> httpCallBack) {

		if(TextUtils.isEmpty(url)){
			return null;
		}

		BufferedReader reader = null;

		HttpURLConnection httpURLConnection = null;

		int statusCode = -1;
		try {
			Log.v(TAG, url);

			if(httpCallBack!=null){
				httpCallBack.onStart(url);
			}
			httpURLConnection = getHttpURLConnection(url,httpMethod,params);
			httpURLConnection.connect();
			statusCode = httpURLConnection.getResponseCode();
			if(statusCode==HttpURLConnection.HTTP_OK){

				reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

				StringBuffer buffer = new StringBuffer();
				String line = null;

				long progress = 0;

				long count = httpURLConnection.getContentLength();
				isCancel = false;
				if(httpCallBack != null && count!=-1)
					httpCallBack.onLoading(progress, count);
				while ((!isCancel) && (line = reader.readLine())!=null) {
					buffer.append(line);

					if(httpCallBack != null && count!=-1){
						progress+= line.getBytes().length;
						httpCallBack.onLoading(progress, count);
					}
				}


				if(httpCallBack != null){
					if(!isCancel){
						progress = count;
						httpCallBack.onLoading(progress, count);
					}else{
						reader.close();
						httpCallBack.onCancel();
						return null;
					}
				}
				reader.close();
				if(httpCallBack != null && !isCancel)
					httpCallBack.onSuccess(buffer.toString());

				if(httpURLConnection!=null)
					httpURLConnection.disconnect();
				System.out.println(">>>>>>>>>>> url >>>>" + url);
				System.out.println(">>>>>>>>>>> res >>>>>> " + buffer.toString());
				return buffer.toString();
			}else{
				reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
				StringBuffer buffer = new StringBuffer();
				String line;
				while ((line = reader.readLine())!=null) {
					buffer.append(line);
				}
				String errResponse = buffer.toString();
				if(httpCallBack != null) {
					httpCallBack.onFailure(statusCode, new Throwable(errResponse));
				}
				return errResponse;
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpCallBack != null)
				httpCallBack.onFailure(statusCode, e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpCallBack != null)
				httpCallBack.onFailure(statusCode, e);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpCallBack != null)
				httpCallBack.onFailure(statusCode, e);
		}finally{

			if(httpURLConnection!=null){
				httpURLConnection.disconnect();
			}
		}

		return null;
	}

	@Override
	public void asyncDownloadFile(final String url, final String fileName,
								  final HttpCallBack<File> httpDownloadCallBack) {
		asyncThread(new Runnable() {
			@Override
			public void run() {
				syncDownloadFile(url,fileName,httpDownloadCallBack);
			}
		});
	}

	@Override
	public File syncDownloadFile(String url, String fileName) {
		return syncDownloadFile(url, fileName, null);
	}

	@Override
	public File syncDownloadFile(String url, String fileName,
								 HttpCallBack<File> httpDownloadCallBack) {

		if(TextUtils.isEmpty(url)){
			return null;
		}

		File file = null;

		BufferedInputStream bis = null;

		FileOutputStream fos = null;

		HttpURLConnection httpURLConnection = null;

		int statusCode = -1;
		try {
			Log.v(TAG, url);

			if(TextUtils.isEmpty(fileName)){
				return null;
			}

			if(httpDownloadCallBack!=null)
				httpDownloadCallBack.onStart(url);

			httpURLConnection = getHttpURLConnection(url,HttpMethod.GET,null);
			httpURLConnection.connect();
			statusCode = httpURLConnection.getResponseCode();
			if(statusCode == HttpURLConnection.HTTP_OK){

				file = new File(fileName);
				fos = new FileOutputStream(file);

				long progress = 0;

				long count = httpURLConnection.getContentLength();

				bis = new BufferedInputStream(httpURLConnection.getInputStream());

				isCancel = false;
				byte[] buffer = new byte[DEFAULT_BYTE_LENGTH];
				int len = 0;
				if(httpDownloadCallBack!=null && count!=-1)
					httpDownloadCallBack.onLoading(progress, count);
				long time = System.currentTimeMillis();
				while((!isCancel) && (len = bis.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					long temp = System.currentTimeMillis();
					if(temp-time>=1000){
						time = temp;
						if(httpDownloadCallBack!=null && count!=-1){
							progress += len;
							httpDownloadCallBack.onLoading(progress, count);
						}
					}
				}

				if(httpDownloadCallBack!=null ){
					if(!isCancel){
						progress = count;
						httpDownloadCallBack.onLoading(progress, count);
					}else{
						bis.close();
						fos.close();
						httpDownloadCallBack.onCancel();

						if(httpURLConnection!=null)
							httpURLConnection.disconnect();

						return file;
					}
				}

				bis.close();
				fos.close();

				if(httpDownloadCallBack!=null && !isCancel)
					httpDownloadCallBack.onSuccess(file);

			}else{
				if(httpDownloadCallBack!=null)
					httpDownloadCallBack.onFailure(statusCode, null);
			}

		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpDownloadCallBack!=null)
				httpDownloadCallBack.onFailure(statusCode, e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpDownloadCallBack!=null)
				httpDownloadCallBack.onFailure(statusCode, e);
		}catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			if(httpDownloadCallBack!=null)
				httpDownloadCallBack.onFailure(statusCode, e);
		}finally{
			if(httpURLConnection!=null)
				httpURLConnection.disconnect();
		}

		return file;
	}

	/**
	 * Convert map to string.
	 * <p>
	 * This method will call the toString() method of K and V.
	 *
	 * @param map the map to be output.
	 * @return the string result.
	 */
	private String convertMaptoString(Map map) {
		if (map == null) {
			return "";
		}
		String str = "[";
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			str += " " + pair.getKey() + ":" + (pair.getValue() == null ? "" : pair.getValue());
		}
		return str + " ]";
	}

	private String convertMapToParams(Map map) {
		if (map == null) {
			return "";
		}
		String str = "";
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			if (!TextUtils.isEmpty(str)) {
				str += "&";
			}
			Map.Entry pair = (Map.Entry) it.next();
			str += pair.getKey() + "=" + (pair.getValue() == null ? "" : pair.getValue());
		}
		return str;
	}
}
