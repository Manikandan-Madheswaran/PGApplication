package com.merchant.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MerchantDataController
{
	@RequestMapping(value="/Home")
	public ModelAndView homePage(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("index");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mav;
	}
}