package com.meishe.send;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpSendForMS {

	public static void main(String[] args) throws Exception 
	{
		System.out.println("返回结果:" + connect( "D:/work/code_svn/MEISHE/src/com/meishe/B01_02_11_shuilu/见费投保报文100267.xml"));
	}










	public static String connect(String datafile) throws Exception {

		BufferedReader in = null;
		String PAIS_HTTPS_URI = "https://10.36.192.51:8107";

		String keyStore = "D:/work/code_svn/MEISHE/src/com/meishe/send/EXV_BIS_IFRONT_PCIS_MEISHE_001_PRD.pfx"; // 证书的路径，pfx格式

		String trustStore = keyStore;
		String keyPass = "paic1234"; // pfx文件的密码
		String trustPass = keyPass;//"123456"; // jks文件的密码f

		FileInputStream keystoreInstream = new FileInputStream(new File(keyStore));
		FileInputStream trustStoreInstream = new FileInputStream(new File(trustStore));
		SSLSocketFactory socketFactory = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			// 加载pfx文件
			ks.load(keystoreInstream, keyPass.toCharArray());

			KeyStore ts = KeyStore.getInstance("PKCS12");
			// 加载jks文件
			ts.load(trustStoreInstream, trustPass.toCharArray()); 

			socketFactory = new SSLSocketFactory(SSLSocketFactory.SSL, ks, keyPass,
					ts, null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				keystoreInstream.close();
				trustStoreInstream.close();}
			catch(Exception e){}
		}
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			Scheme sch = new Scheme("https", 8007, socketFactory);
			httpclient.getConnectionManager().getSchemeRegistry().register(sch);
			HttpPost httpPost = null;
			httpPost = new HttpPost(PAIS_HTTPS_URI);


			// 创建UrlEncodedFormEntity对象
			String BK_SERIAL = String.valueOf(System.currentTimeMillis());
			System.out.println("BK_SERIAL:" + BK_SERIAL);
			String bodyText = FileUtils.readFileToString(new File(datafile),"GB2312").replaceAll("#BK_SERIAL#",BK_SERIAL);
			System.out.println(bodyText);
			StringEntity entity = new StringEntity(bodyText, "text/html", "GB2312");
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpclient.execute(httpPost);
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"GB2312"));
			StringBuffer sb = new StringBuffer("");
			String valueString = null;
			while (!StringUtils.isEmpty(valueString=in.readLine())){
				sb.append(valueString);
			}
			result = sb.toString();
			httpclient.getConnectionManager().shutdown();
			//result = URLDecoder.decode(result.toStrin-g(), "GBK");
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}