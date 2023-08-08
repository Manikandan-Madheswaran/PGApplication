package com.merchant.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.merchant.demo.model.TransactionRequest;
import com.merchant.demo.model.TransactionResponse;
import com.merchant.demo.webservice.utils.JsonArraytoMap;

@Service
public class MerchantService
{
	public static String AES_IV = "PGKEYENCDECIVSPC";
	private static final String HEX_DIGITS = "0123456789abcdef";
	
	public TransactionRequest performHostedTransaction(TransactionRequest trandata,String type)
	{
		TransactionRequest transactionData = null;
		JSONObject respData = null;
		JsonArraytoMap arrayToMap = null;
		JSONArray array = null;
		List<Object> objlist = null;
		Iterator<Object> itr = null;
		HashMap<Object, Object> map = null;
		String status = null;
		String result = null;
		String errorText = null;
		String paymentId = null;
		String paymentPage = null;
		String error = null;
		try
		{
			transactionData = new TransactionRequest();
			respData = apiReqData(trandata);
			if (respData != null)
			{
				String response = notifyMerch(respData,type);
				if (response != null)
				{
					System.out.println("*****************Response from PG***************** :" + response);
					if (isJson(response))
					{
						arrayToMap = new JsonArraytoMap();
						array = new JSONArray(response);
						objlist = arrayToMap.toList(array);
						itr = objlist.iterator();
						map = new HashMap<Object, Object>();

						while (itr.hasNext())
						{
							try
							{
								map = (HashMap<Object, Object>) itr.next();
								if (map.get("status") != null && !map.get("status").equals(""))
									status = map.get("status") + "";
								if (map.get("result") != null && !map.get("result").equals(""))
									result = map.get("result") + "";
								if (map.get("errorText") != null && !map.get("errorText").equals(""))
									errorText = map.get("errorText") + "";
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}

					if (status != null && status.equals("1") && result != null)
					{
						String[] rsp = result.split(":http");
						paymentId = rsp[0];
						paymentPage = "http" + rsp[1].replace("/btch/", "/misrpts/");
						transactionData.setPaymentPage(paymentPage+"?PaymentID="+paymentId);
					}
					else if (status != null && status.equals("2") && errorText != null)
					{
						error = errorText;
						transactionData.setError(error);
					}
				}
				else
				{
					System.out.println("Response from PG is empty.......");
					transactionData.setError("Problem occured while connecting to ARB PG");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			respData = null;
			arrayToMap = null;
			array = null;
			objlist = null;
			itr = null;
			map = null;
			status = null;
			result = null;
			errorText = null;
			paymentId = null;
			paymentPage = null;
			error = null;
		}
		return transactionData;
	}
	
	@SuppressWarnings("deprecation")
	public JSONObject apiReqData(TransactionRequest trandata) 
	{
		JSONObject req = null;
		String tranData = null;
		JSONObject restTranData = null;
		JSONArray restTranDataArr = null;
		try
		{
			restTranData = buildAPIHostRequest(trandata);
			if(restTranData != null && restTranData.length() > 0)
			{
				restTranDataArr = new JSONArray();
				restTranDataArr.put(restTranData);
				System.out.println("REST API Plain request: " + restTranDataArr.toString());
				tranData = encryptAES(URLEncoder.encode(restTranDataArr.toString()),trandata.getResourceKey());
				System.out.println("REST API Encrypted request: " + tranData);
				
				req = new JSONObject();
				req.put("id", trandata.getId());
				req.put("trandata", tranData);
				req.put("responseURL", trandata.getResponseURL());
				req.put("errorURL", trandata.getErrorURL());
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return req;
	}
	
	private synchronized JSONObject buildAPIHostRequest(TransactionRequest trandata) 
	{
		JSONObject pipeObject = new JSONObject();
		try
		{
			if (StringUtils.hasText(trandata.getId()))
			{
				pipeObject.put("id", trandata.getId());
			}
			if (StringUtils.hasText(trandata.getPassword()))
			{
				pipeObject.put("password", trandata.getPassword());
			}
			if (StringUtils.hasText(trandata.getCurrency()))
			{
				pipeObject.put("currencyCode", trandata.getCurrency());
			}
			if (StringUtils.hasText(trandata.getAction()))
			{
				pipeObject.put("action", trandata.getAction());
			}
			if (StringUtils.hasText(trandata.getAmount()))
			{
				pipeObject.put("amt", trandata.getAmount());
			}
			if (StringUtils.hasText(trandata.getResponseURL()))
			{
				pipeObject.put("responseURL", trandata.getResponseURL());
			}
			if (StringUtils.hasText(trandata.getErrorURL()))
			{
				pipeObject.put("errorURL", trandata.getErrorURL());
			}
			if (StringUtils.hasText(trandata.getTrackID()))
			{
				pipeObject.put("trackId", trandata.getTrackID());
			}
			if (StringUtils.hasText(trandata.getCardNumber()))
			{
				pipeObject.put("cardNo", trandata.getCardNumber());
			}
			if (StringUtils.hasText(trandata.getExpYear()))
			{
				pipeObject.put("expYear", trandata.getExpYear());
			}
			if (StringUtils.hasText(trandata.getExpMonth()))
			{
				pipeObject.put("expMonth", trandata.getExpMonth());
			}
			if (StringUtils.hasText(trandata.getCvv()))
			{
				pipeObject.put("cvv2", trandata.getCvv());
			}
			if (StringUtils.hasText(trandata.getCardHolderName()))
			{
				pipeObject.put("member", trandata.getCardHolderName());
			}
			if (StringUtils.hasText(trandata.getCustid()))
			{
				pipeObject.put("custid", trandata.getCustid());
			}
			if (StringUtils.hasText(trandata.getUdf1()))
			{
				pipeObject.put("udf1", trandata.getUdf1());
			}
			if (StringUtils.hasText(trandata.getUdf2()))
			{
				pipeObject.put("udf2", trandata.getUdf2());
			}
			if (StringUtils.hasText(trandata.getUdf3()))
			{
				pipeObject.put("udf3", trandata.getUdf3());
			}
			if (StringUtils.hasText(trandata.getUdf4()))
			{
				pipeObject.put("udf4", trandata.getUdf4());
			}
			if (StringUtils.hasText(trandata.getUdf5()))
			{
				pipeObject.put("udf5", trandata.getUdf5());
			}
			if (StringUtils.hasText(trandata.getUdf6()))
			{
				pipeObject.put("udf6", trandata.getUdf6());
			}
			if (StringUtils.hasText(trandata.getUdf7()))
			{
				pipeObject.put("udf7", trandata.getUdf7());
			}
			if (StringUtils.hasText(trandata.getUdf8()))
			{
				pipeObject.put("udf8", trandata.getUdf8());
			}
			if (StringUtils.hasText(trandata.getUdf9()))
			{
				pipeObject.put("udf9", trandata.getUdf9());
			}
			if (StringUtils.hasText(trandata.getUdf10()))
			{
				pipeObject.put("udf10", trandata.getUdf10());
			}
			if(StringUtils.hasText(trandata.getCardType()))
			{
				pipeObject.put("cardType", trandata.getCardType());
			}
			if(StringUtils.hasText(trandata.getTransId()))
			{
				pipeObject.put("transId", trandata.getTransId());
			}
			
			if(StringUtils.hasText(trandata.getMobileNumber()))
			{
				pipeObject.put("mobileNumber", trandata.getMobileNumber());
			}
			
			if(StringUtils.hasText(trandata.getCardonFileAction()))
			{
				pipeObject.put("cardOnFileAction", trandata.getCardonFileAction());
			}
			
			if(StringUtils.hasText(trandata.getMaskedCardNo()))
			{
				pipeObject.put("maskedCardNo", trandata.getMaskedCardNo());
			}
			
			if(StringUtils.hasText(trandata.getInvoiceType()))
			{
				pipeObject.put("invoiceType", trandata.getInvoiceType());
			}
			
			if(StringUtils.hasText(trandata.getInvoiceId()))
			{
				pipeObject.put("invoiceId", trandata.getInvoiceId());
			}
			
			if(StringUtils.hasText(trandata.getItemDesc()))
			{
				pipeObject.put("itemDesc", trandata.getItemDesc());
			}
			
			if(StringUtils.hasText(trandata.getBuyerName()))
			{
				pipeObject.put("buyerName", trandata.getBuyerName());
			}
			
			if(StringUtils.hasText(trandata.getMobile()))
			{
				pipeObject.put("mobile", trandata.getMobile());
			}
			
			if(StringUtils.hasText(trandata.getEmail()))
			{
				pipeObject.put("email", trandata.getEmail());
			}
			
			if(StringUtils.hasText(trandata.getExpiryDate()))
			{
				pipeObject.put("expiryDate", trandata.getExpiryDate());
			}
			
			if(StringUtils.hasText(trandata.getViolationCount())
					&& StringUtils.hasText(trandata.getViolationID())
					&& StringUtils.hasText(trandata.getViolationAmt()))
			{
				JSONObject absherObject = new JSONObject();
				JSONObject violationObject = new JSONObject();
				JSONObject violationList = new JSONObject();
				JSONArray violationArray = new JSONArray();
				
				if(StringUtils.hasText(trandata.getSectorID()))
				{
					absherObject.put("sectorID", trandata.getSectorID());
				}
				
				if(StringUtils.hasText(trandata.getServiceCode()))
				{
					absherObject.put("serviceCode", trandata.getServiceCode());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryIDType()))
				{
					absherObject.put("beneficiaryIDType", trandata.getBeneficiaryIDType());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryID()))
				{
					absherObject.put("beneficiaryID", trandata.getBeneficiaryID());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiayName()))
				{
					absherObject.put("beneficiaryName", trandata.getBeneficiayName());
				}
				
				if(StringUtils.hasText(trandata.getBranchCode()))
				{
					absherObject.put("branchCode", trandata.getBranchCode());
				}
				
				if(StringUtils.hasText(trandata.getViolationCount()))
				{
					violationObject.put("violationCount",trandata.getViolationCount());
				}
				
				if(StringUtils.hasText(trandata.getViolationID()))
				{
					violationList.put("violationID",trandata.getViolationID());
				}
				
				if(StringUtils.hasText(trandata.getViolationAmt()))
				{
					violationList.put("violationAmt",trandata.getViolationAmt());
				}
				
				violationArray.put(violationList);
				violationObject.put("violationList",violationArray);
				absherObject.put("violationDetails", violationObject);
				pipeObject.put("moiPymnt", absherObject);
				
				/*if(StringUtils.hasText(trandata.getViolationCount())
						&&StringUtils.hasText(trandata.getViolationID())
						&&StringUtils.hasText(trandata.getViolationAmt()))
				{
					violationObject.put("violationCount",trandata.getViolationCount());
					violationObject.put("violationID",trandata.getViolationID());
					violationObject.put("violationAmt",trandata.getViolationAmt());
				}*/
			}
			
			else if(StringUtils.hasText(trandata.getNumberOfSentences())
					&& StringUtils.hasText(trandata.getPaymentInquiryType())
					&& StringUtils.hasText(trandata.getSentenceNumber())
					&& StringUtils.hasText(trandata.getAmount())
					&& StringUtils.hasText(trandata.getInstallmentNumber()))
			{
				JSONObject absherObject = new JSONObject();
				JSONObject sentenceObject = new JSONObject();
				JSONObject sentenceList = new JSONObject();
				JSONArray sentenceArray = new JSONArray();
				
				if(StringUtils.hasText(trandata.getSectorID()))
				{
					absherObject.put("sectorID", trandata.getSectorID());
				}
				
				if(StringUtils.hasText(trandata.getServiceCode()))
				{
					absherObject.put("serviceCode", trandata.getServiceCode());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryIDType()))
				{
					absherObject.put("beneficiaryIDType", trandata.getBeneficiaryIDType());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryID()))
				{
					absherObject.put("beneficiaryID", trandata.getBeneficiaryID());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiayName()))
				{
					absherObject.put("beneficiaryName", trandata.getBeneficiayName());
				}
				
				if(StringUtils.hasText(trandata.getBranchCode()))
				{
					absherObject.put("branchCode", trandata.getBranchCode());
				}
				
				if(StringUtils.hasText(trandata.getNumberOfSentences()))
				{
					sentenceObject.put("numberOfSentences",trandata.getNumberOfSentences());
				}
				
				if(StringUtils.hasText(trandata.getPaymentInquiryType()))
				{
					sentenceObject.put("paymentInquiryType",trandata.getPaymentInquiryType());
				}
				
				if(StringUtils.hasText(trandata.getSentenceNumber()))
				{
					sentenceList.put("sentenceNumber",trandata.getSentenceNumber());
				}
				
				if(StringUtils.hasText(trandata.getAmount()))
				{
					sentenceList.put("sentenceAmt",trandata.getSentenceAmt());
				}
				
				if(StringUtils.hasText(trandata.getInstallmentNumber()))
				{
					sentenceList.put("installmentNumber",trandata.getInstallmentNumber());
				}
				
				sentenceArray.put(sentenceList);
				sentenceObject.put("sentences", sentenceArray);
				absherObject.put("sentenceDetails", sentenceObject);
				pipeObject.put("moiPymnt", absherObject);
				
				/*if(StringUtils.hasText(trandata.getNumberOfSentences())
						&& StringUtils.hasText(trandata.getPaymentInquiryType())
						&& StringUtils.hasText(trandata.getSentenceNumber())
						&& StringUtils.hasText(trandata.getAmount())
						&& StringUtils.hasText(trandata.getInstallmentNumber()))
				{
					sentenceObject.put("numberOfSentences",trandata.getNumberOfSentences());
					sentenceObject.put("paymentInquiryType",trandata.getPaymentInquiryType());
					sentenceObject.put("sentenceNumber",trandata.getSentenceNumber());
					sentenceObject.put("sentenceAmt",trandata.getAmount());
					sentenceObject.put("installmentNumber",trandata.getInstallmentNumber());
				}*/
			}
			else if(StringUtils.hasText(trandata.getSectorID())
					|| StringUtils.hasText(trandata.getServiceCode())
					|| StringUtils.hasText(trandata.getBeneficiaryIDType())
					|| StringUtils.hasText(trandata.getBeneficiaryID())
					|| StringUtils.hasText(trandata.getBeneficiayName())
					|| StringUtils.hasText(trandata.getBranchCode()))
			{
				JSONObject absherObject = new JSONObject();
				
				if(StringUtils.hasText(trandata.getSectorID()))
				{
					absherObject.put("sectorID", trandata.getSectorID());
				}
				
				if(StringUtils.hasText(trandata.getServiceCode()))
				{
					absherObject.put("serviceCode", trandata.getServiceCode());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryIDType()))
				{
					absherObject.put("beneficiaryIDType", trandata.getBeneficiaryIDType());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiaryID()))
				{
					absherObject.put("beneficiaryID", trandata.getBeneficiaryID());
				}
				
				if(StringUtils.hasText(trandata.getBeneficiayName()))
				{
					absherObject.put("beneficiaryName", trandata.getBeneficiayName());
				}
				
				if(StringUtils.hasText(trandata.getBranchCode()))
				{
					absherObject.put("branchCode", trandata.getBranchCode());
				}
				pipeObject.put("moiPymnt", absherObject);
			}
			
			if((StringUtils.hasText(trandata.getBankIdCode1())
					&& StringUtils.hasText(trandata.getiBanNum1())
					&& StringUtils.hasText(trandata.getServiceAmount1())
					&& StringUtils.hasText(trandata.getValueDate1())
					&& StringUtils.hasText(trandata.getBenificiaryName1()))
					|| (StringUtils.hasText(trandata.getBankIdCode2())
							&& StringUtils.hasText(trandata.getiBanNum2())
							&& StringUtils.hasText(trandata.getServiceAmount2())
							&& StringUtils.hasText(trandata.getValueDate2())
							&& StringUtils.hasText(trandata.getBenificiaryName2()))
					|| (StringUtils.hasText(trandata.getBankIdCode3())
							&& StringUtils.hasText(trandata.getiBanNum3())
							&& StringUtils.hasText(trandata.getServiceAmount3())
							&& StringUtils.hasText(trandata.getValueDate3())
							&& StringUtils.hasText(trandata.getBenificiaryName3())))
			{
				JSONObject payoutObject = null;
				JSONArray payoutArray = new JSONArray();
				
				if((StringUtils.hasText(trandata.getBankIdCode1())
						&& StringUtils.hasText(trandata.getiBanNum1())
						&& StringUtils.hasText(trandata.getServiceAmount1())
						&& StringUtils.hasText(trandata.getValueDate1())
						&& StringUtils.hasText(trandata.getBenificiaryName1())))
				{
					payoutObject = new JSONObject();
					payoutObject.put("bankIdCode", trandata.getBankIdCode1());
					payoutObject.put("iBanNum", trandata.getiBanNum1());
					payoutObject.put("serviceAmount", trandata.getServiceAmount1());
					payoutObject.put("valueDate", trandata.getValueDate1());
					payoutObject.put("benificiaryName", trandata.getBenificiaryName1());
					payoutArray.put(payoutObject);
				}
				
				if((StringUtils.hasText(trandata.getBankIdCode2())
						&& StringUtils.hasText(trandata.getiBanNum2())
						&& StringUtils.hasText(trandata.getServiceAmount2())
						&& StringUtils.hasText(trandata.getValueDate2())
						&& StringUtils.hasText(trandata.getBenificiaryName2())))
				{
					payoutObject = new JSONObject();
					payoutObject.put("bankIdCode", trandata.getBankIdCode2());
					payoutObject.put("iBanNum", trandata.getiBanNum2());
					payoutObject.put("serviceAmount", trandata.getServiceAmount2());
					payoutObject.put("valueDate", trandata.getValueDate2());
					payoutObject.put("benificiaryName", trandata.getBenificiaryName2());
					payoutArray.put(payoutObject);
				}
				
				if((StringUtils.hasText(trandata.getBankIdCode3())
						&& StringUtils.hasText(trandata.getiBanNum3())
						&& StringUtils.hasText(trandata.getServiceAmount3())
						&& StringUtils.hasText(trandata.getValueDate3())
						&& StringUtils.hasText(trandata.getBenificiaryName3())))
				{
					payoutObject = new JSONObject();
					payoutObject.put("bankIdCode", trandata.getBankIdCode3());
					payoutObject.put("iBanNum", trandata.getiBanNum3());
					payoutObject.put("serviceAmount", trandata.getServiceAmount3());
					payoutObject.put("valueDate", trandata.getValueDate3());
					payoutObject.put("benificiaryName", trandata.getBenificiaryName3());
					payoutArray.put(payoutObject);
				}
				
				if(payoutObject!=null && payoutArray!=null)
				{
					pipeObject.put("accountDetails", payoutArray);
				}
				
			}
			
			if(StringUtils.hasText(trandata.getTransactionType())
					&& StringUtils.hasText(trandata.getBillerID())
					&& trandata.getBillAmount()!=null
					&& StringUtils.hasText(trandata.getBillType())
					&& StringUtils.hasText(trandata.getBillNumber())
					&& StringUtils.hasText(trandata.getIDType())
					&& StringUtils.hasText(trandata.getIDNumber()))
			{
				JSONObject billerObject = new JSONObject();
				billerObject.put("transactionType", trandata.getTransactionType());
				billerObject.put("billerID", trandata.getBillerID());
				billerObject.put("billAmount", trandata.getBillAmount());
				billerObject.put("billType", trandata.getBillType());
				billerObject.put("billNumber", trandata.getBillNumber());
				billerObject.put("IDType", trandata.getIDType());
				billerObject.put("IDNumber", trandata.getIDNumber());
				pipeObject.put("billingDetails", billerObject+"");
			}
			
			if(StringUtils.hasText(trandata.getAgencyCode())
					&& StringUtils.hasText(trandata.getIssuerAgencyId())
					&& StringUtils.hasLength(trandata.getBillingAccountId())
					&& StringUtils.hasText(trandata.getBillingCycle())
					&& StringUtils.hasText(trandata.getDueAmount())
					&& StringUtils.hasText(trandata.getPaidAmount())
					&& StringUtils.hasText(trandata.getBillReferenceInfo()))
			{
				JSONObject agencyObject = new JSONObject();
				agencyObject.put("agencyCode", trandata.getAgencyCode());
				agencyObject.put("issuerAgencyId", trandata.getIssuerAgencyId());
				agencyObject.put("billingAccountId", trandata.getBillingAccountId());
				agencyObject.put("billingCycle", trandata.getBillingCycle());
				agencyObject.put("dueAmount", trandata.getDueAmount());
				agencyObject.put("paidAmount", trandata.getPaidAmount());
				agencyObject.put("billReferenceInfo", trandata.getBillReferenceInfo());
				pipeObject.put("billDetails", agencyObject);
			}
			
			return pipeObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}
	}
	
	 public static String encryptAES(final String encryptString, final String key)
	 {
	        byte[] encryptedText;
	        String encryptedData = null;
	        IvParameterSpec ivspec;
	        SecretKeySpec skeySpec;
	        Cipher cipher;
	        byte[] text;
	        String result;
	        try
	        {
	            ivspec = new IvParameterSpec(MerchantService.AES_IV.getBytes(StandardCharsets.UTF_8));
	            skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
	            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivspec);
				text = encryptString.getBytes("UTF-8");
				encryptedText = cipher.doFinal(text);
				result = byteArrayToHexString(encryptedText);
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            return encryptedData;
	        }
	        return result;
	    }

	    public static String decryptAES(final String key, final String encryptedString)
	    {
	        SecretKeySpec skeySpec;
	        IvParameterSpec ivspec;
	        Cipher cipher;
	        byte[] textDecrypted = null;
	        try
	        {
	            final int len = encryptedString.length();
	            final byte[] data = new byte[len / 2];
	            for (int i = 0; i < len; i += 2) {
	                data[i / 2] = (byte)((Character.digit(encryptedString.charAt(i), 16) << 4) + Character.digit(encryptedString.charAt(i + 1), 16));
	            }
	            skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
	            ivspec = new IvParameterSpec(MerchantService.AES_IV.getBytes(StandardCharsets.UTF_8));
	            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(2, skeySpec, ivspec);
	            textDecrypted = cipher.doFinal(data);
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            assert false;
	            return new String(textDecrypted);
	        }
	        return new String(textDecrypted);
	    }
	    
	    public String notifyMerch(JSONObject jsonObject,String type) throws Exception 
		{
			JSONArray jsonArray = null;
			String result = null;
			String url = null;
			try
			{
				jsonArray = new JSONArray();
				jsonArray.put(jsonObject);
				if("Hosted".equals(type))
				{
					url ="https://securepayments.alrajhibank.com.sa/misrpts/payment/hosted.htm";
				}    
				else if("Tranportal".equals(type))
				{
					url="https://securepayments.alrajhibank.com.sa/misrpts/payment/tranportal.htm";
				}
				else if("URPayment".equals(type))
				{
					url="https://securepayments.alrajhibank.com.sa/pg/payment/TranportalURPay.htm";
				}
				else if("Invoice".equals(type))
				{
					url="https://securepayments.alrajhibank.com.sa/pg/payment/invoice.htm";
				}
				else if("BillPay".equals(type))
				{
					url="https://securepayments.alrajhibank.com.sa/pg/payment/tranportalPlain.htm";
				}
				else if("Absher".equals(type))
				{
					url="https://securepayments.alrajhibank.com.sa/pg/payment/tranportalabsherPlain.htm";
				}
					
				result = sendWebServiceData(jsonArray.toString(), url);
				if (result != null)
				{
					return result;
				}
				return result;
			}
			finally
			{
				jsonArray = null;
				jsonObject = null;
				url = null;
			}
		}
	    
	    @SuppressWarnings({ "deprecation", "resource" })
		public String sendWebServiceData(String json, String url) throws Exception
	    {
		    HttpClient client = null;
		    //HttpParams httpParams = null;
		    HttpPost post = null;
		    HttpResponse response = null;
		    StringEntity input = null;
		    String result=null;
		    try
		    {
				//client = new DefaultHttpClient();
				//client = HttpClientBuilder.create().build();
				//httpParams = client.getParams();
				//httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,1000);
				
				RequestConfig.Builder requestBuilder = RequestConfig.custom();
				requestBuilder.setConnectTimeout(30*1000);
				requestBuilder.setConnectionRequestTimeout(30*1000);

				HttpClientBuilder builder = HttpClientBuilder.create();     
				builder.setDefaultRequestConfig(requestBuilder.build());
				client = builder.build();
				
				if(url == null)
				{
					System.out.println("URL is empty......."+url);
				}
				post = new HttpPost(url);
				input = new StringEntity(json.toString());
				input.setContentType("application/json");
				post.setEntity(input);
				post.setHeader("Authorization", "Authorization");
				response = client.execute(post);
		    	int resCode = response.getStatusLine().getStatusCode();
		    	if(resCode == 200)
		    	{
		    		String line = null;
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				 	while ((line = rd.readLine()) != null)
				 	{
				 		System.out.println("#######"+line);
				 		result = line ;
				 		return result;
				 	}	 	
				}
			}
		    catch(Exception e)
		    {
		    	e.printStackTrace();
			}
		    finally
		    {
			    client = null;
			    //httpParams = null;
			    post = null;
			    response = null;
			    input = null;
			}
		    return result;
		}
	    
	    public static boolean isJson(String Json)
	    {
			try {
				new JSONObject(Json);
			} catch (JSONException ex) {
				try {
					new JSONArray(Json);
				} catch (JSONException ex1) {
					return false;
				}
			}
			return true;
		}
	    
	    public static String byteArrayToHexString(byte[] data, int length)
	    {
			StringBuffer buf = new StringBuffer();

			for (int i = 0; i != length; i++) {
				int v = data[i] & 0xff;

				buf.append(HEX_DIGITS.charAt(v >> 4));
				buf.append(HEX_DIGITS.charAt(v & 0xf));
			}

			return buf.toString();
		}
	    
	 public static String byteArrayToHexString(byte[] data)
	 {
			return byteArrayToHexString(data, data.length);
	 }
	 
	 @SuppressWarnings({ "deprecation", "unchecked" })
	public TransactionResponse parseAPIEncryptedResult(String encryptedResponse, TransactionResponse tranResponse)
	{
			JsonArraytoMap arrayToMap = null;
			JSONArray array = null;
			List<Object> objlist = null;
			Iterator<Object> itr = null;
			Map<String, Object> map = null;
			String trandata = null;
			try
			{
				trandata = decryptAES(tranResponse.getResourceKey(), encryptedResponse);
				System.out.println("Response data URL Encoded : "+trandata);
				if (isJson(URLDecoder.decode(trandata)))
				{
					trandata = URLDecoder.decode(trandata);
					System.out.println("Response data : "+trandata);
					arrayToMap = new JsonArraytoMap();
					array = new JSONArray(trandata);
					objlist = arrayToMap.toList(array);
					itr = objlist.iterator();
					map = new HashMap<String, Object>();

					while (itr.hasNext())
					{
							map = (HashMap<String, Object>) itr.next();
							
							if(map!=null && map.get("errorText")!=null)
							{
								String error = (String) map.get("error");
								String errorText = (String) map.get("errorText");
								String status = (String)map.get("status");
								if (map.get("errorText") != null && !map.get("errorText").equals(""))
								{
									tranResponse.setError(errorText);
								}
								tranResponse.setStatus(status);
							}
							else
							{
								BeanUtils.populate(tranResponse, map);	
							}
					}
				}
				else
				{
					tranResponse.setError("Transaction response data is invalid");
				}
				return tranResponse;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				tranResponse.setError("Problem occured while parsing the response");
				return tranResponse;
			}
			finally
			{
				arrayToMap = null;
				array = null;
				objlist = null;
				itr = null;
				map = null;
			}
	}
	 
	 @SuppressWarnings("unchecked")
	public TransactionRequest performMerchantHostedTransaction(TransactionRequest trandata,String type)
		{
			TransactionRequest transactionData = null;
			JSONObject respData = null;
			JsonArraytoMap arrayToMap = null;
			JSONArray array = null;
			List<Object> objlist = null;
			Iterator<Object> itr = null;
			HashMap<Object, Object> map = null;
			String status = null;
			String result = null;
			String errorText = null;
			String paymentId = null;
			String paymentPage = null;
			String error = null;
			try
			{
				transactionData = new TransactionRequest();
				respData = apiReqData(trandata);
				if (respData != null)
				{
					String response = notifyMerch(respData,type);
					if (response != null)
					{
						System.out.println("*****************Response from PG***************** :" + response);
						if (isJson(response))
						{
							arrayToMap = new JsonArraytoMap();
							array = new JSONArray(response);
							objlist = arrayToMap.toList(array);
							itr = objlist.iterator();
							map = new HashMap<Object, Object>();

							while (itr.hasNext())
							{
								try
								{
									map = (HashMap<Object, Object>) itr.next();
									if (map.get("status") != null && !map.get("status").equals(""))
										status = map.get("status") + "";
									if (map.get("result") != null && !map.get("result").equals(""))
										result = map.get("result") + "";
									if (map.get("errorText") != null && !map.get("errorText").equals(""))
										errorText = map.get("errorText") + "";
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}

						if (status != null && status.equals("1") && result != null)
						{
							String[] rsp = result.split(":http");
							paymentId = rsp[0];
							paymentPage = "http" + rsp[1].replace("/btch/", "/misrpts/");
							//transactionData.setPaymentPage(paymentPage+"?PaymentID="+paymentId);
							transactionData.setPaymentPage(paymentPage);
						}
						else if (status != null && status.equals("2") && errorText != null)
						{
							error = errorText;
							transactionData.setError(error);
						}
					}
					else
					{
						System.out.println("Response from PG is empty.......");
						transactionData.setError("Problem occured while connecting to ARB PG");
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				respData = null;
				arrayToMap = null;
				array = null;
				objlist = null;
				itr = null;
				map = null;
				status = null;
				result = null;
				errorText = null;
				paymentId = null;
				paymentPage = null;
				error = null;
			}
			return transactionData;
		}
	 
	@SuppressWarnings({ "unchecked", "deprecation" })
	public TransactionResponse performTranportalTransaction(TransactionRequest trandata,String type)
	{
		JSONObject respData = null;
		JsonArraytoMap arrayToMap = null;
		JSONArray array = null;
		List<Object> objlist = null;
		Iterator<Object> itr = null;
		HashMap<String, Object> map = null;
		String status = null;
		String errorText = null;
		String error = null;
		TransactionResponse tranResponse = null;
		String transactionResponse = null;
		try
		{
			tranResponse = new TransactionResponse();
			respData = apiReqData(trandata);
			System.out.println("Plain Request Data : "+respData);
			if (respData != null)
			{
				String response = notifyMerch(respData,type);
				if (response != null)
				{
					System.out.println("*****************Encrypted response from PG***************** :" + response);
					if (isJson(response))
					{
						arrayToMap = new JsonArraytoMap();
						array = new JSONArray(response);
						objlist = arrayToMap.toList(array);
						itr = objlist.iterator();
						map = new HashMap<String, Object>();
						while (itr.hasNext())
						{
							map = (HashMap<String, Object>) itr.next();
							
							if (map.get("trandata") != null && !map.get("trandata").equals(""))
							{
								transactionResponse = decryptAES(trandata.getResourceKey(), map.get("trandata")+"");
								System.out.println("*****************Decrypted response from PG***************** :" + URLDecoder.decode(transactionResponse));
								array = new JSONArray(URLDecoder.decode(transactionResponse));
								objlist = arrayToMap.toList(array);
								itr = objlist.iterator();
								map = new HashMap<String, Object>();
								while (itr.hasNext())
								{
									map = (HashMap<String, Object>) itr.next();
									BeanUtils.populate(tranResponse, map);
								}
							}
							else
							{
								error = (String) map.get("error");
								errorText = (String) map.get("errorText");
								status = (String)map.get("status");
								if (map.get("errorText") != null && !map.get("errorText").equals(""))
								{
									tranResponse.setError(error+"-"+errorText);
								}
								tranResponse.setStatus(status);
							}
						}
					}
				}
				else
				{
					System.out.println("Response from PG is empty.......");
					tranResponse.setError("Problem occured while connecting to ARB PG");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			tranResponse.setError("Problem occured while connecting to ARB PG");
		}
		finally
		{
			respData = null;
			arrayToMap = null;
			array = null;
			objlist = null;
			itr = null;
			map = null;
			status = null;
			errorText = null;
			error = null;
		}
		return tranResponse;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public TransactionResponse performMOFBillPayTranportalTransaction(TransactionRequest trandata)
	{
		JSONObject respData = null;
		JsonArraytoMap arrayToMap = null;
		JSONArray array = null;
		List<Object> objlist = null;
		Iterator<Object> itr = null;
		HashMap<String, Object> map = null;
		String status = null;
		String errorText = null;
		String error = null;
		TransactionResponse tranResponse = null;
		String transactionResponse = null;
		String type = "Tranportal";
		try
		{
			tranResponse = new TransactionResponse();
			respData = apiMOFReqData(trandata);
			System.out.println("Plain Request Data : "+respData);
			if (respData != null)
			{
				String response = notifyMerch(respData,type);
				if (response != null)
				{
					System.out.println("*****************Encrypted response from PG***************** :" + response);
					if (isJson(response))
					{
						arrayToMap = new JsonArraytoMap();
						array = new JSONArray(response);
						objlist = arrayToMap.toList(array);
						itr = objlist.iterator();
						map = new HashMap<String, Object>();
						while (itr.hasNext())
						{
							map = (HashMap<String, Object>) itr.next();
							
							if (map.get("trandata") != null && !map.get("trandata").equals(""))
							{
								System.out.println("*****************Decrypted response from PG***************** :" + URLDecoder.decode(transactionResponse));
								array = new JSONArray(URLDecoder.decode(transactionResponse));
								objlist = arrayToMap.toList(array);
								itr = objlist.iterator();
								map = new HashMap<String, Object>();
								while (itr.hasNext())
								{
									map = (HashMap<String, Object>) itr.next();
									BeanUtils.populate(tranResponse, map);
								}
							}
							else
							{
								error = (String) map.get("error");
								errorText = (String) map.get("errorText");
								status = (String)map.get("status");
								if (map.get("errorText") != null && !map.get("errorText").equals(""))
								{
									tranResponse.setError(error+"-"+errorText);
								}
								tranResponse.setStatus(status);
							}
						}
					}
				}
				else
				{
					System.out.println("Response from PG is empty.......");
					tranResponse.setError("Problem occured while connecting to ARB PG");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			tranResponse.setError("Problem occured while connecting to ARB PG");
		}
		finally
		{
			respData = null;
			arrayToMap = null;
			array = null;
			objlist = null;
			itr = null;
			map = null;
			status = null;
			errorText = null;
			error = null;
		}
		return tranResponse;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public TransactionResponse performMOFAbsherTranportalTransaction(TransactionRequest trandata)
	{
		JSONObject respData = null;
		JsonArraytoMap arrayToMap = null;
		JSONArray array = null;
		List<Object> objlist = null;
		Iterator<Object> itr = null;
		HashMap<String, Object> map = null;
		String status = null;
		String errorText = null;
		String error = null;
		TransactionResponse tranResponse = null;
		String transactionResponse = null;
		String type = "Tranportal";
		try
		{
			tranResponse = new TransactionResponse();
			respData = apiMOFReqData(trandata);
			System.out.println("Plain Request Data : "+respData);
			if (respData != null)
			{
				String response = notifyMerch(respData,type);
				if (response != null)
				{
					System.out.println("*****************Encrypted response from PG***************** :" + response);
					if (isJson(response))
					{
						arrayToMap = new JsonArraytoMap();
						array = new JSONArray(response);
						objlist = arrayToMap.toList(array);
						itr = objlist.iterator();
						map = new HashMap<String, Object>();
						while (itr.hasNext())
						{
							map = (HashMap<String, Object>) itr.next();
							
							if (map.get("trandata") != null && !map.get("trandata").equals(""))
							{
								System.out.println("*****************Decrypted response from PG***************** :" + URLDecoder.decode(transactionResponse));
								array = new JSONArray(URLDecoder.decode(transactionResponse));
								objlist = arrayToMap.toList(array);
								itr = objlist.iterator();
								map = new HashMap<String, Object>();
								while (itr.hasNext())
								{
									map = (HashMap<String, Object>) itr.next();
									BeanUtils.populate(tranResponse, map);
								}
							}
							else
							{
								error = (String) map.get("error");
								errorText = (String) map.get("errorText");
								status = (String)map.get("status");
								if (map.get("errorText") != null && !map.get("errorText").equals(""))
								{
									tranResponse.setError(error+"-"+errorText);
								}
								tranResponse.setStatus(status);
							}
						}
					}
				}
				else
				{
					System.out.println("Response from PG is empty.......");
					tranResponse.setError("Problem occured while connecting to ARB PG");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			tranResponse.setError("Problem occured while connecting to ARB PG");
		}
		finally
		{
			respData = null;
			arrayToMap = null;
			array = null;
			objlist = null;
			itr = null;
			map = null;
			status = null;
			errorText = null;
			error = null;
		}
		return tranResponse;
	}
	
	@SuppressWarnings("deprecation")
	public JSONObject apiMOFReqData(TransactionRequest trandata) 
	{
		JSONObject req = null;
		String tranData = null;
		JSONObject restTranData = null;
		JSONArray restTranDataArr = null;
		try
		{
			restTranData = buildAPIHostRequest(trandata);
			if(restTranData != null && restTranData.length() > 0)
			{
				restTranDataArr = new JSONArray();
				restTranDataArr.put(restTranData);
				System.out.println("REST API Plain request: " + restTranDataArr.toString());
				tranData = URLEncoder.encode(restTranDataArr.toString());
				System.out.println("REST API Encrypted request: " + tranData);
				
				req = new JSONObject();
				req.put("id", trandata.getId());
				req.put("trandata", tranData);
				req.put("responseURL", trandata.getResponseURL());
				req.put("errorURL", trandata.getErrorURL());
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return req;
	}
	
public static String encryptAESMerchant(String encryptString,String key) throws Exception{
		
		String AES_IV = "PGKEYENCDECIVSPC";
		byte [] encryptedText=null;
		IvParameterSpec ivspec=null;
		SecretKeySpec skeySpec=null;
		Cipher cipher=null;
		
		byte [] text=null;
		
		String s=null;
		try {
			ivspec = new IvParameterSpec(AES_IV.getBytes("UTF-8"));
			skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivspec);
			
			text = encryptString.getBytes("UTF-8");
			
			encryptedText = cipher.doFinal(text);
			s = byteArrayToHexString(encryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			encryptedText=null;
			ivspec=null;
			skeySpec=null;
			cipher=null;
			text=null;
		}
		 return s.toUpperCase();
	}

public static String encryptMerchant(String plainText, String secretKey, String iv) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
	
    //IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
	//SecretKeySpec secretKeySpec = new SecretKeySpec(hexToBytes(secretKey), "AES");
    
	IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
	
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
    //return Base64.getEncoder().encodeToString(encryptedBytes);
	
	//return bytesToHex(encryptedBytes);
	return bytesToHexStr(encryptedBytes);

}

public static String decryptMerchant(String encryptedText, String secretKey, String iv) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
    //IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
	
	//SecretKeySpec secretKeySpec = new SecretKeySpec(hexToBytes(secretKey), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

    
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

    //byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
	byte[] encryptedBytes = hexToBytes(encryptedText);
	byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
	return new String(decryptedBytes, StandardCharsets.UTF_8);
}

private static byte[] hexToBytes(String hexString) {
	int length = hexString.length();
	byte[] bytes = new byte[length / 2];
	for (int i = 0; i < length; i += 2) {
		bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
				+ Character.digit(hexString.charAt(i + 1), 16));
	}
	return bytes;
}

private static String bytesToHexStr(byte[] data) {
	StringBuffer buf = new StringBuffer();
	
	for (int i =0; i != data.length; i++){
		int v = data[i] & 0xff;
		
		buf.append(HEX_DIGITS.charAt(v >> 4));
		buf.append(HEX_DIGITS.charAt(v & 0xf));
	}
	
	return buf.toString();
}
	
	public static void main(String[] args) throws Exception {
		String encryptedString = encryptAES("%5B%0D%0A%20%20%20%20%7B%0D%0A%20%20%20%20%20%20%20%20%22id%22%3A%20%2283z150VLpCCVcgh%22,%0D%0A%20%20%20%20%20%20%20%20%22amt%22%3A%20%221220%22,%0D%0A%20%20%20%20%20%20%20%20%22action%22%3A%20%221%22,%0D%0A%20%20%20%20%20%20%20%20%22currencyCode%22%3A%20%22682%22,%0D%0A%20%20%20%20%20%20%20%20%22langid%22%3A%20%22en%22,%0D%0A%20%20%20%20%20%20%20%20%22password%22%3A%20%22kj!k33QDAZj0%233%40%22,%0D%0A%20%20%20%20%20%20%20%20%22trackId%22%3A%20%22442%22,%0D%0A%20%20%20%20%20%20%20%20%22udf2%22%3A%20%22SI%22,%0D%0A%20%20%20%20%20%20%20%20%22custid%22%3A%20%22202225722673270%22,%0D%0A%20%20%20%20%20%20%20%20%22cardType%22%3A%20%22D%22,%0D%0A%20%20%20%20%20%20%20%20%22responseURL%22%3A%20%22https%3A%2F%2Fmerchantpage%2FPaymentResult.jsp%22,%0D%0A%20%20%20%20%20%20%20%20%22errorURL%22%3A%20%22https%3A%2F%2Fmerchantpage%2FPaymentResult.jsp%22%0D%0A%20%20%20%20%7D%0D%0A%5D","22577572524022577572524022577572");
		System.out.println(encryptedString);
		String decryptedString = decryptAES("22577572524022577572524022577572",encryptedString);
		System.out.println(decryptedString);
		String encryptedStringMerchant = encryptMerchant("%5B%0D%0A%20%20%20%20%7B%0D%0A%20%20%20%20%20%20%20%20%22id%22%3A%20%2283z150VLpCCVcgh%22,%0D%0A%20%20%20%20%20%20%20%20%22amt%22%3A%20%221220%22,%0D%0A%20%20%20%20%20%20%20%20%22action%22%3A%20%221%22,%0D%0A%20%20%20%20%20%20%20%20%22currencyCode%22%3A%20%22682%22,%0D%0A%20%20%20%20%20%20%20%20%22langid%22%3A%20%22en%22,%0D%0A%20%20%20%20%20%20%20%20%22password%22%3A%20%22kj!k33QDAZj0%233%40%22,%0D%0A%20%20%20%20%20%20%20%20%22trackId%22%3A%20%22442%22,%0D%0A%20%20%20%20%20%20%20%20%22udf2%22%3A%20%22SI%22,%0D%0A%20%20%20%20%20%20%20%20%22custid%22%3A%20%22202225722673270%22,%0D%0A%20%20%20%20%20%20%20%20%22cardType%22%3A%20%22D%22,%0D%0A%20%20%20%20%20%20%20%20%22responseURL%22%3A%20%22https%3A%2F%2Fmerchantpage%2FPaymentResult.jsp%22,%0D%0A%20%20%20%20%20%20%20%20%22errorURL%22%3A%20%22https%3A%2F%2Fmerchantpage%2FPaymentResult.jsp%22%0D%0A%20%20%20%20%7D%0D%0A%5D","22577572524022577572524022577572","PGKEYENCDECIVSPC");
		System.out.println(encryptedStringMerchant);
		String decryptedStringMerchant = decryptMerchant(encryptedStringMerchant,"22577572524022577572524022577572","PGKEYENCDECIVSPC");
		System.out.println(decryptedStringMerchant);
		String data ="{\"apiOperation\":\"PAY\",\"sourceOfFunds\":{\"type\":\"CARD\",\"provided\":{\"card\":{\"number\":\"4012001037141112\",\"expiry\":{\"month\":\"12\", \"year\":\"24\"},\"securityCode\":\"207\"}}},\"order\":{\"reference\":\"1234567890\",\"amount\":\"10.00\"},\"transaction\":{\"amount\":\"10.00\",\"currency\":\"SAR\",\"reference\":\"1234567890\",\"targetTransactionId\":\"600202314521452568\"},\"customer\":{\"ipAddress\":\"10.44.77.27\"}}";
	}
}
