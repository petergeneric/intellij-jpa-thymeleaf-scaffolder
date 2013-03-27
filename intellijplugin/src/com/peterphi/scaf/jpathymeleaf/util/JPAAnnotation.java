package com.peterphi.scaf.jpathymeleaf.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiLiteral;
import com.intellij.psi.PsiModifierListOwner;

/**
 * The JPA annotations we reference
 */
public enum JPAAnnotation
{
	ENTITY("javax.persistence.Entity"),
	COLUMN("javax.persistence.Column"),
	GENERATED_VALUE("javax.persistence.GeneratedValue"),
	ID("javax.persistence.Id"),
	JOIN_COLUMN("javax.persistence.JoinColumn"),
	VERSION("javax.persistence.Version");

	private final String className;


	JPAAnnotation(final String className)
	{
		this.className = className;
	}


	public boolean has(PsiModifierListOwner element)
	{
		return find(element) != null;
	}


	public PsiAnnotation find(PsiModifierListOwner element)
	{
		return element.getModifierList().findAnnotation(className);
	}


	/**
	 * Read an annotation attribute from an annotated element, returning the value as a literal
	 *
	 * @param element
	 * @param attribute
	 *
	 * @return
	 */
	public String read(PsiModifierListOwner element, String attribute)
	{
		final PsiAnnotationMemberValue val = readMemberValue(element, attribute);

		if (val != null && val instanceof PsiLiteral)
			return ((PsiLiteral) val).getValue().toString();
		else
			return null; // could not extract
	}


	public PsiAnnotationMemberValue readMemberValue(PsiModifierListOwner element, String attribute)
	{
		PsiAnnotation annotation = find(element);

		if (annotation != null)
			return annotation.findAttributeValue(attribute);
		else
			return null;
	}
}
