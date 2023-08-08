package com.merchant.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.merchant.demo.model.TransactionRequest;
import com.merchant.demo.model.TransactionResponse;
import com.merchant.demo.service.MerchantService;
import com.merchant.demo.util.MerchantTrackIDGenerator;
import com.merchant.demo.webservice.utils.ConfigFileUtil;

@RestController
public class BankBillPayHostedController
{
	@Autowired
	private MerchantService merchantService;

	public MerchantService getmerchantService() {
		return merchantService;
	}

	public void setmerchantService(MerchantService merchantService) {
		this.merchantService = merchantService;
	}

	@RequestMapping(value = "/BankHostedBillPayment")
	public ModelAndView HostedRequest(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("BankHostedBillPayment");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/BankHostedBillPaymentTransaction")
	public ModelAndView performHostedTransaction(HttpServletRequest request,HttpServletResponse response,TransactionRequest tranData)
	{
		ModelAndView mav = null;
		TransactionRequest transactionData = null;
		String mrchtrackID = null;
		String type = "Hosted";
		try
		{
			mav = new ModelAndView();
			mrchtrackID = MerchantTrackIDGenerator.getMerchantTrackID();

	        tranData.setId(ConfigFileUtil.getProperty("BANK_HOSTED_TRANPORTAL_ID"));
			tranData.setPassword(ConfigFileUtil.getProperty("BANK_HOSTED_TRANPORTAL_PWD"));
			tranData.setResourceKey(ConfigFileUtil.getProperty("BANK_HOSTED_RESOURCE_KEY"));
			tranData.setTrackID(mrchtrackID);
			tranData.setResponseURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"BankHostedBillPaymentResponse");
			tranData.setErrorURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"BankHostedBillPaymentResponse");
			transactionData = merchantService.performHostedTransaction(tranData,type);
			System.out.println("Payment Page : "+transactionData.getPaymentPage());
			if(StringUtils.hasText(transactionData.getPaymentPage()))
			{
				return new ModelAndView("redirect:"+transactionData.getPaymentPage());
			}
			else
			{
				mav.setViewName("error");
				mav.addObject("Error", transactionData.getError());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			transactionData = null;
			mrchtrackID = null;
		}
		return mav;
	}
	
	@RequestMapping(value = "/BankHostedBillPaymentResponse")
	public ModelAndView performHostedResponse(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		String encryptedResponse = null;
		TransactionResponse transactionResponse = null;
		try
		{
			mav = new ModelAndView();
			transactionResponse = new TransactionResponse();
			encryptedResponse = request.getParameter("trandata");
			if(encryptedResponse==null)
			{
				mav.setViewName("error");
				mav.addObject("Error", "Trandata is null");
			}
			
			transactionResponse.setResourceKey(ConfigFileUtil.getProperty("BANK_HOSTED_RESOURCE_KEY"));
			transactionResponse = merchantService.parseAPIEncryptedResult(encryptedResponse,transactionResponse);
			if(transactionResponse==null)
			{
				mav.setViewName("error");
				mav.addObject("Error", "Problem occured while parsing the data received from Payment Gateway");
			}

			else if(transactionResponse.getError()!=null)
			{
				mav.setViewName("error");
				mav.addObject("Error",transactionResponse.getError());
			}
			else if(transactionResponse!=null)
			{
				mav.setViewName("success");
				mav.addObject("response",transactionResponse);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			encryptedResponse = null;
			transactionResponse = null;
		}
		return mav;
	}
	
	
}