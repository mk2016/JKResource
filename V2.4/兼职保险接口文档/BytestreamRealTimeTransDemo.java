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
 * ��ȡ���ӱ���ʾ������
 * ע�⣺��ʾ�������ο���ʾ���������������������б�д
 * @author HXS 2012-12-23
 *
 */

public class BytestreamRealTimeTransDemo {

	/**
	 * Ϊ��������������Ϣ��Ϊ��̬���������е������ɽ����޸�
	 */
	private static final String ENCODING="gbk";//�ַ���GBK

	//�����������õ����ݲ���
/*	
	private static final String PINGANURL="http://localhost:7001/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//ƽ���ķ����ַ
	private static String KEYSTORE_FILENAME="./config/EXV_BIS_IFRONT_PCIS_SUNING_001_PRD.pfx";
	private static String KEYSTORE_PASSWORD="paic1234";//��Կ�������
	private static String KEYSTORE_ALIAS="1";//��Կ��ı���
	private static final String UMCODE="99870000";//�����˺�����;99870000������99720000
	private static final String POLICYNO="10404011900000095845";//������
	private static final String VALIDATECODE="ntZyZcXEEcAstXiQlt";//"LZnaYreVfeYfUPGBwt";//������֤��
	private static final String ISSEPERATED="";//����Ǹ�������д�գ�������ŵ��Ŵ���дgroup��������ŵ���������дsingle
*/	
//	private static final String TransID="00000000000000000100";//"22345678901234567891";  ces//22345678901234567920
//	private static final String requestId="Ctrip";
	
	
	
	//���Ի������ò���
//	private static final String PINGANURL="http://localhost:7001/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//ƽ���ķ����ַ,�Ͻӿ�
//���´�ӡ	
	//private static final String PINGANURL="http://epcis-ptp-dmzstg2.pingan.com.cn/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//ƽ���ķ����ַ
	private static final String PINGANURL="http://epcis-ptp-dmzstg2.pingan.com.cn/epcis.ptp.partner.getAhsEPolicyPDFWithCert.do";//ƽ���ķ����ַ,�Ͻӿ�

	private static String KEYSTORE_FILENAME="D:/share/print/EXV_BIS_IFRONT_PCIS_HZWBYT_001_PRD.pfx";//EXV_BIS_IFRONT_PCIS_BJDTW_001_PRD  EXV_BIS_IFRONT_PCIS_HZWBYT_001_PRD
//	private static String KEYSTORE_PASSWORD="yangmingsong";//��Կ�������
	private static String KEYSTORE_PASSWORD="paic1234";//��Կ�������
	private static String KEYSTORE_ALIAS="1";//��Կ��ı���
	private static final String UMCODE="79200000";//�����˺� //99750000  
	private static final String POLICYNO="10457551900111086371";//������10457551900111086371
	private static final String VALIDATECODE="WpHVsFFaXDCjScmaxn";//WpHVsFFaXDCjScmaxn
	private static final String ISSEPERATED="group";//����Ǹ�������д�գ�������ŵ��Ŵ���дgroup��������ŵ���������дsingle
	//���´�ӡ
	private static final String IDENTIFY="79200000";//��֤��ʶ
	private static final String personInfoListStr="insured_023:450922199005108723,insured_024:450922199005108724";
	private static final String comminuteFlag ="";
	
	
	private static final String FILE_PATH="d:\\716��ӡ\\";//���PDF��·��
	//������ӡ
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
		
		System.out.println("���͵�ƽ�������������ݣ�");
		Map requestParam=new HashMap();
		requestParam.put("umCode",UMCODE);//�����˺�
		requestParam.put("policyNo",POLICYNO);//������
		requestParam.put("validateCode",VALIDATECODE);//����������
		requestParam.put("isSeperated",ISSEPERATED);//����Ǹ�������д�գ�������ŵ��Ŵ���дgroup��������ŵ���������дsingle
		//���´�ӡ	
		requestParam.put("identify",IDENTIFY);//��֤��ʶ
		//������ӡ
		requestParam.put("electronicPolicy",electronicPolicy);
		requestParam.put("comminuteFlag", comminuteFlag);
		requestParam.put("personInfoListStr", personInfoListStr);
		
//		requestParam.put("TransID",TransID);//�����˺�
//		requestParam.put("requestId",requestId);
		
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime=sdf.format(calendar.getTime());
		String data=UMCODE+POLICYNO+VALIDATECODE+ISSEPERATED+curTime;//��ǩ������˳���ܱ仯
		System.out.println("��ǩ�����ݣ�"+data);
		String cipherText=signData(data,KEYSTORE_FILENAME,KEYSTORE_PASSWORD,KEYSTORE_ALIAS);//ǩ��
		System.out.println("ǩ�������"+cipherText);//ǩ�����
		requestParam.put("curTime",curTime);//ʱ���
		requestParam.put("cipherText",cipherText);
		
		sendMsgToPingAn(requestParam); //���ͱ��ĵ�ƽ��
		System.out.println("����ƽ�����ش���������ļ�·����"+FILE_PATH);
		
	}
	/**
	 * ���������ĵ�ƽ��������ƽ���ķ��ؽ��
	 * @author HXS
	 * @date 2012-12-19
	 * @todo TODO
	 * @param requestMsg
	 * @return ���ؽ��
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
			//����
			paramList.add(new BasicNameValuePair("identify",(String)requestMap.get("identify")));
			//����
			paramList.add(new BasicNameValuePair("electronicPolicy",(String)requestMap.get("electronicPolicy")));
			 
				//�����ƽ���ڲ�������ԣ���Ҫʹ�ô��������󵽲��Ի���
//			((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(new AuthScope("10.36.232.18",80), new UsernamePasswordCredentials("", ""));    
//			HttpHost proxy = new HttpHost("10.36.232.18", 8080);     
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
			
	       
			HttpPost httpPost = new HttpPost(PINGANURL);
			StringEntity entity;
			entity = new UrlEncodedFormEntity(paramList,  ENCODING);
			httpPost.setEntity(entity);
			System.out.println("���ڷ��ͽ���...");
			HttpResponse httpResponse=httpClient.execute(httpPost);//��������ƽ��
			HttpEntity httpEntity=httpResponse.getEntity();//��ȡ��������
			Header[] headers=httpResponse.getHeaders("Content-type");//��ȡ��������
			
			for(int i=0;i<headers.length;i++)
			{
				String typeValue=headers[i].getValue();
				if(typeValue!=null&&typeValue.toUpperCase().indexOf("PDF")>=0)//�����pdf���ͣ����غ���ļ���pdf����
				{
					type="PDF";
					break;
				}
					
			}
			
			if(httpEntity!=null)
			{
				pdfStream=EntityUtils.toByteArray(httpEntity);//��ȡ��������
				String fileName="";
				String curTime=(String)requestMap.get("curTime");
				curTime=curTime.replaceAll(":", "");//ð�Ų�����Ϊ�ļ���
				if(type.equals("PDF"))
				{
					fileName=FILE_PATH+curTime+".pdf";
				}
				else
				{
					fileName=FILE_PATH+curTime+".html";
				}

				OutputStream os=new FileOutputStream(new File(fileName));//������ݵ��ļ�
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
	 * ǩ���㷨
	 * @author HXS
	 * @date 2012-7-2
	 * @todo TODO
	 * @param data  ��Ҫǩ��������
	 * @param keyStoreFileName  ��˽Կ���ļ�
	 * @param keyStorePassword  ��˽Կ�ļ�������
	 * @param keyStoreAlias  ����
	 * @return
	 */
	public static String signData(String data,String keyStoreFileName,String keyStorePassword,String keyStoreAlias)
	{
		KeyStore keyStore;
		byte[]  signRstByte=null;
		String signValue="";
		String keystoreType="";
		try {
			if(keyStoreFileName.toUpperCase().indexOf("PFX")>=0)//�ж�֤���ļ��ĸ�ʽ
			{
				keystoreType="PKCS12";
			}
			else
			{
				keystoreType="JKS";
			}
			keyStore = KeyStore.getInstance(keystoreType);//��ȡJKS֤��ʵ��
			FileInputStream in=new FileInputStream(keyStoreFileName);//��ȡ֤���ļ���
			char[]pwdChar=keyStorePassword.toCharArray();//֤������
			keyStore.load(in, pwdChar);//����֤�鵽keystore��
			PrivateKey privateKey=(PrivateKey)keyStore.getKey(keyStoreAlias, pwdChar);//��֤���л�ȡ˽Կ
			Signature sign=Signature.getInstance("SHA1WithRSA");//SHA1WithRSAǩ���㷨
			sign.initSign(privateKey);//����˽Կ
			sign.update(data.getBytes());//��������
			signRstByte=sign.sign();//����
			BASE64Encoder encoder=new BASE64Encoder();
			signValue=encoder.encodeBuffer(signRstByte);//BASE64����
			//System.out.println("ǩ��������������signValue=="+signValue);
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
