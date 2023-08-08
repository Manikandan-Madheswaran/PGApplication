package com.merchant.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.merchant.demo.webservice.utils.ConfigFileUtil;

@RestController
public class PropertyRefreshController
{
	@RequestMapping(value="/Refresh")
	public ModelAndView reloadproperties(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		System.out.println("###########Reload properties###########");
		int status = 0;
		ModelAndView mav = null;
		try
		{
			mav = new ModelAndView();
			mav.setViewName("propertyrefreshstatus");
			status = ConfigFileUtil.refreshProperty();
			if(status==1)
			{
				mav.addObject("message", "Property File loaded succesfully");
			}
			else if(status==0)
			{
				mav.addObject("message", "property File loading failed");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("###########Reload properties completed###########");
		}
		return mav;
	}
}
