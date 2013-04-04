package com.peterphi.scaf.jpathymeleaf.codegen;

import com.intellij.psi.PsiClass;
import com.peterphi.scaf.jpathymeleaf.velocity.VelocityCall;
import com.peterphi.scaf.jpathymeleaf.velocity.VelocityTemplater;

public class ThymeleafCodeGenerator
{
	private final VelocityTemplater templater;


	public ThymeleafCodeGenerator()
	{
		// Set up a Velocity instance that loads from this .jar
		this.templater = new VelocityTemplater();
	}


	public String generate(PsiClass entity)
	{
		VelocityCall call = templater.template("/com/peterphi/scaf/jpathymeleaf/codegen/forms.vm");

		call.set("entity", new PersistentMember(entity));

		return call.process();
	}
}
