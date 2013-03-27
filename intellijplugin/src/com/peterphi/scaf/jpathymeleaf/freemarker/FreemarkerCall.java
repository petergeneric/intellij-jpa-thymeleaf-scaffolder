package com.peterphi.scaf.jpathymeleaf.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerCall
{
	private final Map<String, Object> data = new HashMap<String, Object>();
	private final Template template;


	public FreemarkerCall(final Template template)
	{
		this.template = template;
	}


	public Map<String, Object> getData()
	{
		return data;
	}


	public String getName()
	{
		return template.getName();
	}


	/**
	 * Sets a variable which is then exposed to the view layer
	 *
	 * @param name
	 * @param value
	 *
	 * @return
	 */
	public FreemarkerCall set(String name, Object value)
	{
		data.put(name, value);

		return this;
	}


	public FreemarkerCall setAll(final Map<String, Object> data)
	{
		this.data.putAll(data);

		return this;
	}


	/**
	 * Render the template to a String
	 *
	 * @return
	 */
	public String process()
	{
		final StringWriter sw = new StringWriter();

		process(sw);

		return sw.toString();
	}


	/**
	 * Render the template to a Writer
	 */
	public void process(Writer writer)
	{
		try
		{
			template.process(data, writer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Error writing template to writer", e);
		}
		catch (TemplateException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
