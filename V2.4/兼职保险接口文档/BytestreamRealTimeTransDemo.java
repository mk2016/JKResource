package com.pingan.bytestream.realtime.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;

/**
 * 获取电子保单示例程序
 * 注意：本示例仅供参考演示，生产代码请合作伙伴另行编写
 * @author HXS 2012-12-23
 *
 */

public class BytestreamRealTimeTransDemo {

	/**
	 * 为简洁起见，配置信息作为静态变量，如有调整，可进行修改
	 */
	private static final String ENCODING="gbk";//字符集GBK

	//开发环境可用的数据参数
/*	
	private static final String PINGANURL="http://localhost:7001/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//平安的服务地址
	private static String KEYSTORE_FILENAME="./config/EXV_BIS_IFRONT_PCIS_SUNING_001_PRD.pfx";
	private static String KEYSTORE_PASSWORD="paic1234";//密钥库的密码
	private static String KEYSTORE_ALIAS="1";//密钥库的别名
	private static final String UMCODE="99870000";//出单账号苏宁;99870000；凯撒99720000
	private static final String POLICYNO="10404011900000095845";//保单号
	private static final String VALIDATECODE="ntZyZcXEEcAstXiQlt";//"LZnaYreVfeYfUPGBwt";//保单验证码
	private static final String ISSEPERATED="";//如果是个单，填写空；如果是团单团打，填写group；如果是团单个单，填写single
*/	
//	private static final String TransID="00000000000000000100";//"22345678901234567891";  ces//22345678901234567920
//	private static final String requestId="Ctrip";
	
	
	
	//测试环境可用参数
//	private static final String PINGANURL="http://localhost:7001/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//平安的服务地址,老接口
//极致打印	
	//private static final String PINGANURL="http://epcis-ptp-dmzstg2.pingan.com.cn/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//平安的服务地址
	private static final String PINGANURL="http://epcis-ptp-dmzstg2.pingan.com.cn/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//平安的服务地址,老接口

	private static String KEYSTORE_FILENAME="D:/share/print/EXV_BIS_IFRONT_PCIS_HZWBYT_001_PRD.pfx";//EXV_BIS_IFRONT_PCIS_BJDTW_001_PRD  EXV_BIS_IFRONT_PCIS_HZWBYT_001_PRD
//	private static String KEYSTORE_PASSWORD="yangmingsong";//密钥库的密码
	private static String KEYSTORE_PASSWORD="paic1234";//密钥库的密码
	private static String KEYSTORE_ALIAS="1";//密钥库的别名
	private static final String UMCODE="79200000";//出单账号 //99750000  
	private static final String POLICYNO="10457551900111086371";//保单号10457551900111086371
	private static final String VALIDATECODE="WpHVsFFaXDCjScmaxn";//WpHVsFFaXDCjScmaxn
	private static final String ISSEPERATED="group";//如果是个单，填写空；如果是团单团打，填写group；如果是团单个单，填写single
	//极致打印
	private static final String IDENTIFY="79200000";//验证标识
	private static final String personInfoListStr="insured_023:450922199005108723,insured_024:450922199005108724";
	private static final String comminuteFlag ="";
	
	
	private static final String FILE_PATH="d:\\716打印\\";//存放PDF的路径
	//共保打印
	private static final String electronicPolicy = "";
	

	/**
	 * @author HXS
	 * @date 2012-12-19
	 * @todo TODO
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		printDome();
		

	}

	
	public static void printDome(){
		
		System.out.println("发送到平安的请求报文内容：");
		Map requestParam=new HashMap();
		requestParam.put("umCode",UMCODE);//出单账号
		requestParam.put("policyNo",POLICYNO);//保单号
		requestParam.put("validateCode",VALIDATECODE);//保单验真码
		requestParam.put("isSeperated",ISSEPERATED);//如果是个单，填写空；如果是团单团打，填写group；如果是团单个单，填写single
		//极致打印	
		requestParam.put("identify",IDENTIFY);//验证标识
		//共保打印
		requestParam.put("electronicPolicy",electronicPolicy);
		requestParam.put("comminuteFlag", comminuteFlag);
		requestParam.put("personInfoListStr", personInfoListStr);
		
//		requestParam.put("TransID",TransID);//出单账号
//		requestParam.put("requestId",requestId);
		
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime=sdf.format(calendar.getTime());
		String data=UMCODE+POLICYNO+VALIDATECODE+ISSEPERATED+curTime;//待签名参数顺序不能变化
		System.out.println("待签名内容："+data);
		String cipherText=signData(data,KEYSTORE_FILENAME,KEYSTORE_PASSWORD,KEYSTORE_ALIAS);//签名
		System.out.println("签名结果："+cipherText);//签名结果
		requestParam.put("curTime",curTime);//时间戳
		requestParam.put("cipherText",cipherText);
		
		sendMsgToPingAn(requestParam); //发送报文到平安
		System.out.println("接收平安返回处理结束，文件路径："+FILE_PATH);
		
	}
	/**
	 * 发送请求报文到平安并接收平安的返回结果
	 * @author HXS
	 * @date 2012-12-19
	 * @todo TODO
	 * @param requestMsg
	 * @return 返回结果
	 */
	private static void sendMsgToPingAn(Map requestMap)
	{
		String responseMsg="";
		byte[] pdfStream=null;
		String type="HTML";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			List paramList=new ArrayList();
			
			paramList.add(new BasicNameValuePair("umCode",(String)requestMap.get("umCode")));
			paramList.add(new BasicNameValuePair("policyNo",(String)requestMap.get("policyNo")));
			paramList.add(new BasicNameValuePair("validateCode",(String)requestMap.get("validateCode")));
			paramList.add(new BasicNameValuePair("curTime",(String)requestMap.get("curTime")));
			paramList.add(new BasicNameValuePair("isSeperated",(String)requestMap.get("isSeperated")));
			paramList.add(new BasicNameValuePair("cipherText",(String)requestMap.get("cipherText")));
			paramList.add(new BasicNameValuePair("TransID",(String)requestMap.get("TransID")));
			paramList.add(new BasicNameValuePair("requestId",(String)requestMap.get("requestId")));
			paramList.add(new BasicNameValuePair("comminuteFlag",(String)requestMap.get("comminuteFlag")));
			paramList.add(new BasicNameValuePair("personInfoListStr",(String)requestMap.get("personInfoListStr")));
			//极致
			paramList.add(new BasicNameValuePair("identify",(String)requestMap.get("identify")));
			//共保
			paramList.add(new BasicNameValuePair("electronicPolicy",(String)requestMap.get("electronicPolicy")));
			 
				//如果是平安内部网络测试，需要使用代理方能请求到测试环境
//			((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(new AuthScope("10.36.232.18",80), new UsernamePasswordCredentials("", ""));    
//			HttpHost proxy = new HttpHost("10.36.232.18", 8080);     
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
			
	       
			HttpPost httpPost = new HttpPost(PINGANURL);
			StringEntity entity;
			entity = new UrlEncodedFormEntity(paramList,  ENCODING);
			httpPost.setEntity(entity);
			System.out.println("正在发送交易...");
			HttpResponse httpResponse=httpClient.execute(httpPost);//发送请求到平安
			HttpEntity httpEntity=httpResponse.getEntity();//获取返回内容
			Header[] headers=httpResponse.getHeaders("Content-type");//获取返回类型
			
			for(int i=0;i<headers.length;i++)
			{
				String typeValue=headers[i].getValue();
				if(typeValue!=null&&typeValue.toUpperCase().indexOf("PDF")>=0)//如果是pdf类型，下载后的文件是pdf类型
				{
					type="PDF";
					break;
				}
					
			}
			
			if(httpEntity!=null)
			{
				pdfStream=EntityUtils.toByteArray(httpEntity);//获取返回内容
				String fileName="";
				String curTime=(String)requestMap.get("curTime");
				curTime=curTime.replaceAll(":", "");//冒号不能作为文件名
				if(type.equals("PDF"))
				{
					fileName=FILE_PATH+curTime+".pdf";
				}
				else
				{
					fileName=FILE_PATH+curTime+".html";
				}

				OutputStream os=new FileOutputStream(new File(fileName));//输出内容到文件
				os.write(pdfStream);
				os.close();
			}
	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * 签名算法
	 * @author HXS
	 * @date 2012-7-2
	 * @todo TODO
	 * @param data  需要签名的内容
	 * @param keyStoreFileName  含私钥的文件
	 * @param keyStorePassword  含私钥文件的密码
	 * @param keyStoreAlias  别名
	 * @return
	 */
	public static String signData(String data,String keyStoreFileName,String keyStorePassword,String keyStoreAlias)
	{
		KeyStore keyStore;
		byte[]  signRstByte=null;
		String signValue="";
		String keystoreType="";
		try {
			if(keyStoreFileName.toUpperCase().indexOf("PFX")>=0)//判断证书文件的格式
			{
				keystoreType="PKCS12";
			}
			else
			{
				keystoreType="JKS";
			}
			keyStore = KeyStore.getInstance(keystoreType);//获取JKS证书实例
			FileInputStream in=new FileInputStream(keyStoreFileName);//获取证书文件流
			char[]pwdChar=keyStorePassword.toCharArray();//证书密码
			keyStore.load(in, pwdChar);//加载证书到keystore中
			PrivateKey privateKey=(PrivateKey)keyStore.getKey(keyStoreAlias, pwdChar);//从证书中获取私钥
			Signature sign=Signature.getInstance("SHA1WithRSA");//SHA1WithRSA签名算法
			sign.initSign(privateKey);//设置私钥
			sign.update(data.getBytes());//设置明文
			signRstByte=sign.sign();//加密
			BASE64Encoder encoder=new BASE64Encoder();
			signValue=encoder.encodeBuffer(signRstByte);//BASE64编码
			//System.out.println("签名并编码后的内容signValue=="+signValue);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signValue;

	}

}
