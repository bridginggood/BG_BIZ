package com.bridginggoodbiz.DB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public class BgHttpHelper {
	private static final int RETRY_LIMIT = 3;

	private static final int KEY = 0;
	private static final int VALUE = 1;

	/**
	 * Returns JSON STRING
	 * @param requestURL	URL to send GET/POST request
	 * @param requestParam	Parameter data
	 * @param method		"GET" | "POST"
	 * @return				JSON string
	 * @throws Exception
	 */
	public static String requestHttpRequest(String requestURL, String requestParam, String method) throws Exception{
		int responseCode=0, retryAttempt=0;
		StringBuilder jsonStringBuilder = new StringBuilder();

		// To resolve returning -1 problem
		System.setProperty("http.keepAlive", "false");
		
		if(method.equals("GET"))
			requestURL = requestURL+"?"+requestParam;

		//loop is there to make retry-connection upon responseCode == -1
		while(retryAttempt < RETRY_LIMIT && responseCode<=0){
			HttpURLConnection httpURLConnection = null;
			URL targetURL = new URL(requestURL);
			BufferedReader postRes = null;
			String line="";

			//Check if the server is HTTPS or HTTP
			if (targetURL.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) targetURL.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				httpURLConnection = https;
			} else {
				httpURLConnection = (HttpURLConnection) targetURL.openConnection();
			}


			//Create httpURLConnection header
			byte[] bytes = requestParam.getBytes("UTF-8");
			httpURLConnection.setRequestMethod(method);
			if(method.equals("POST")){
				httpURLConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
				httpURLConnection.setRequestProperty("Content-Language", "UTF-8");
				//httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				//httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
				httpURLConnection.setDoOutput(true);
				httpURLConnection.connect();

				//Send POST data
				OutputStream outputStream = httpURLConnection.getOutputStream();
				outputStream.write(bytes);
				outputStream.close();
			}
			Log.d("BgDB", "requestURL:"+requestURL);
			//Check responseCode to determine if the call to server was successful.
			responseCode = httpURLConnection.getResponseCode();
			Log.d("BgDB", "ResponseCode: "+responseCode);
			if(responseCode > 0 && responseCode < 400){
				postRes = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((line = postRes.readLine()) != null){
					jsonStringBuilder.append(line);
				}
				postRes.close();
				Log.d("BgDB", "JSON Received: "+jsonStringBuilder.toString());
				httpURLConnection.disconnect();
				break;
			}
			else{
				Log.d("BgDB", "ResponseCode Error!");
			}
			retryAttempt++;
			Log.d("BgDB","Connection Retry: "+retryAttempt);
			httpURLConnection.disconnect();
		}
		return jsonStringBuilder.toString();
	}

	/**
	 * 
	 * @param data	Param data
	 * @return		param data in string format
	 */
	public static String generateParamData(String[][] data){
		String rtn = "";
		for(String[] cell:data){
			rtn += URLEncoder.encode(cell[KEY]) + "=" + URLEncoder.encode(cell[VALUE]) + "&";
		}
		return rtn;
	}

	// always verify the host - Don't check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - Don't check for any SSL certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
}
