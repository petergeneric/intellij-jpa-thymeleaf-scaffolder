package com.peterphi.scaf.jpathymeleaf.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.io.Writer;

public class VelocityCall
{
	private final VelocityEngine engine;
	private final String template;
	private final VelocityContext context;


	public VelocityCall(final VelocityEngine engine, final String template, final VelocityContext context)
	{
		this.engine = engine;
		this.template = template;
		this.context = context;
	}


	/**
	 * Sets a variable which is then exposed to the view layer
	 *
	 * @param name
	 * @param value
	 *
	 * @return
	 */
	public VelocityCall set(String name, Object value)
	{
		context.put(name, value);

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
		engine.evaluate(context, writer, getClass().getName(), template);
	}
}
