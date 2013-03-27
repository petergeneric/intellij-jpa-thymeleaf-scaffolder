package com.peterphi.scaf.jpathymeleaf.codegen;

import com.intellij.psi.PsiClass;
import com.peterphi.scaf.jpathymeleaf.freemarker.FreemarkerCall;
import com.peterphi.scaf.jpathymeleaf.freemarker.FreemarkerTemplater;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public class ThymeleafCodeGenerator
{
	private final FreemarkerTemplater templater;


	public ThymeleafCodeGenerator()
	{
		// Set up a Freemarker instance that loads from this .jar
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(getClass(), "/com/peterphi/scaf/jpathymeleaf/codegen");
		config.setObjectWrapper(new DefaultObjectWrapper());

		this.templater = new FreemarkerTemplater(config);
	}


	public String generate(PsiClass entity)
	{
		FreemarkerCall call = templater.template("forms");

		call.set("entity", new PersistentMember(entity));

		return call.process();
	}
}
