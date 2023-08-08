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
public class InvoicePaymentController
{
	@Autowired
	private MerchantService merchantService;

	public MerchantService getmerchantService() {
		return merchantService;
	}

	public void setmerchantService(MerchantService merchantService) {
		this.merchantService = merchantService;
	}
	
	@RequestMapping(value = "/InvoicePayment")
	public ModelAndView MerchantHosted(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("InvoicePayment");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/InvoicePaymentTransaction")
	public ModelAndView performMerchantHostedTransaction(HttpServletRequest request,HttpServletResponse response,TransactionRequest tranData)
	{
		ModelAndView mav = null;
		TransactionResponse tranResponse = null;
		String mrchtrackID = null;
		String type = "Invoice";
		try
		{
			mav = new ModelAndView();
			mrchtrackID = MerchantTrackIDGenerator.getMerchantTrackID();

	        tranData.setId(ConfigFileUtil.getProperty("MERCHANT_HOSTED_TRANPORTAL_ID"));
			tranData.setPassword(ConfigFileUtil.getProperty("MERCHANT_HOSTED_TRANPORTAL_PWD"));
			tranData.setResourceKey(ConfigFileUtil.getProperty("MERCHANT_HOSTED_RESOURCE_KEY"));
			tranData.setTrackID(mrchtrackID);
			tranData.setResponseURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"InvoicePaymentResponse");
			tranData.setErrorURL(ConfigFileUtil.getProperty("CONTEXT_PATH")+"InvoicePaymentResponse");
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
		finally
		{
			tranResponse = null;
			mrchtrackID = null;
		}
		return mav;
	}
	
	@RequestMapping(value = "/InvoicePaymentResponse")
	public ModelAndView performMerchantHostedResponse(HttpServletRequest request,HttpServletResponse response)
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
			
			transactionResponse.setResourceKey(ConfigFileUtil.getProperty("MERCHANT_HOSTED_RESOURCE_KEY"));
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
