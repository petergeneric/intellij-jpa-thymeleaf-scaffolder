package com.peterphi.scaf.jpathymeleaf.velocity;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VelocityTemplater
{
	final VelocityEngine ve;
	final Class<?> clazz;


	public VelocityTemplater()
	{
		this(VelocityTemplater.class);
	}


	public VelocityTemplater(Class<?> clazz)
	{
		ve = new VelocityEngine();
		ve.init();

		this.clazz = clazz;
	}


	public VelocityCall template(String name)
	{
		final String template = loadTemplate(name);
		final VelocityContext context = new VelocityContext();

		return new VelocityCall(ve, template, context);
	}


	private String loadTemplate(String name)
	{
		try
		{
			final InputStream stream = getClass().getResourceAsStream(name);

			if (stream == null)
				throw new IllegalArgumentException("Could not find template in classpath: " + name);
			else
				return StringUtil.convertLineSeparators(FileUtil.loadTextAndClose(new InputStreamReader(new BufferedInputStream(stream),
				                                                                                        "UTF-8")));
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not load template " + name, e);
		}
	}
}
