package com.merchant.demo.controller;

import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.merchant.demo.model.TransactionRequest;
import com.merchant.demo.model.TransactionResponse;
import com.merchant.demo.service.MerchantService;
import com.merchant.demo.webservice.utils.ConfigFileUtil;

@RestController
public class MerchantHostedSupportingController
{
	@Autowired
	private MerchantService merchantService;

	public MerchantService getmerchantService() {
		return merchantService;
	}

	public void setmerchantService(MerchantService merchantService) {
		this.merchantService = merchantService;
	}
	
	@RequestMapping(value = "/Supporting")
	public ModelAndView SupportingRequest(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("Supporting");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/SupportingTransaction")
	public ModelAndView performSupportingTransaction(HttpServletRequest request,HttpServletResponse response,TransactionRequest tranData)
	{
		ModelAndView mav = null;
		TransactionResponse tranResponse = null;
		String type = "Tranportal";
		try
		{
			mav = new ModelAndView();
			
			SecureRandom random = new SecureRandom();
	        StringBuilder resultTrackId = new StringBuilder();
	        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(dic.length());
	            resultTrackId.append(dic.charAt(index));
	        }

	        tranData.setId(ConfigFileUtil.getProperty("MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_ID"));
			tranData.setPassword(ConfigFileUtil.getProperty("MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_PWD"));
			tranData.setResourceKey(ConfigFileUtil.getProperty("MERCHANT_HOSTED_SUPPORTING_RESOURCE_KEY"));
			tranData.setTrackID(resultTrackId.toString());
			tranData.setResponseURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"MerchantHostedResponse");
			tranData.setErrorURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"MerchantHostedResponse");
			tranResponse = merchantService.performTranportalTransaction(tranData,type);
			if(tranResponse==null)
			{
				mav.setViewName("error");
				mav.addObject("Error", "Problem occured while parsing the data received from Payment Gateway");
			}
			else if(tranResponse.getError()!=null)
			{
				mav.setViewName("error");
				mav.addObject("Error",tranResponse.getError());
			}
			else if(tranResponse!=null)
			{
				mav.setViewName("success");
				mav.addObject("response",tranResponse);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	/*@RequestMapping(value = "/MOFSupportingBillPay")
	public ModelAndView MOFBillPaySupportingRequest(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("BillPaySupporting");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/MOFSupportingBillPay")
	public ModelAndView performMOFBillPaySupportingTransaction(HttpServletRequest request,HttpServletResponse response,TransactionRequest tranData)
	{
		ModelAndView mav = null;
		TransactionResponse tranResponse = null;
		try
		{
			mav = new ModelAndView();
			
			SecureRandom random = new SecureRandom();
	        StringBuilder resultTrackId = new StringBuilder();
	        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(dic.length());
	            resultTrackId.append(dic.charAt(index));
	        }

	        tranData.setId(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_ID"));
			tranData.setPassword(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_PWD"));
			tranData.setResourceKey(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_RESOURCE_KEY"));
			tranData.setTrackID(resultTrackId.toString());
			tranResponse = merchantService.performMOFBillPayTranportalTransaction(tranData);
			if(tranResponse==null)
			{
				mav.setViewName("error");
				mav.addObject("Error", "Problem occured while parsing the data received from Payment Gateway");
			}
			else if(tranResponse.getError()!=null)
			{
				mav.setViewName("error");
				mav.addObject("Error",tranResponse.getError());
			}
			else if(tranResponse!=null)
			{
				mav.setViewName("success");
				mav.addObject("response",tranResponse);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/MOFSupportingAbsher")
	public ModelAndView MOFAbsherSupportingRequest(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("AbsherSupporting");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/MOFSupportingTransactionAbsher")
	public ModelAndView performMOFAbsherSupportingTransaction(HttpServletRequest request,HttpServletResponse response,TransactionRequest tranData)
	{
		ModelAndView mav = null;
		TransactionResponse tranResponse = null;
		try
		{
			mav = new ModelAndView();
			
			SecureRandom random = new SecureRandom();
	        StringBuilder resultTrackId = new StringBuilder();
	        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(dic.length());
	            resultTrackId.append(dic.charAt(index));
	        }

	        tranData.setId(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_ID"));
			tranData.setPassword(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_TRANPORTAL_PWD"));
			tranData.setResourceKey(ConfigFileUtil.getProperty("MOF_MERCHANT_HOSTED_SUPPORTING_RESOURCE_KEY"));
			tranData.setTrackID(resultTrackId.toString());
			tranResponse = merchantService.performMOFAbsherTranportalTransaction(tranData);
			if(tranResponse==null)
			{
				mav.setViewName("error");
				mav.addObject("Error", "Problem occured while parsing the data received from Payment Gateway");
			}
			else if(tranResponse.getError()!=null)
			{
				mav.setViewName("error");
				mav.addObject("Error",tranResponse.getError());
			}
			else if(tranResponse!=null)
			{
				mav.setViewName("success");
				mav.addObject("response",tranResponse);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}*/

}
