package com.peterphi.scaf.jpathymeleaf.plugin.thymeleaf.formgen;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.peterphi.scaf.jpathymeleaf.testing.AnnotationConstant;

import java.beans.Introspector;
import java.util.Collection;

public class MemberReflection
{
	private final PsiModifierListOwner el;
	private final PsiType type;

	// only one o the following
	private final PsiMethod method;
	private final PsiField field;
	private final PsiClass clazz;

	public MemberReflection(PsiClass clazz)
	{
		this(clazz, JavaPsiFacade.getInstance(clazz.getProject()).getElementFactory().createType(clazz), null, null, clazz);
	}

	public MemberReflection(PsiMethod method)
	{
		this(method, method.getReturnType(), method, null, null);
	}

	public MemberReflection(PsiField field)
	{
		this(field, field.getType(), null, field, null);
	}

	private MemberReflection(PsiModifierListOwner el,
	                         PsiType type,
	                         final PsiMethod method,
	                         final PsiField field,
	                         final PsiClass clazz)
	{
		if (countNotNull(method, field, clazz) != 1)
			throw new IllegalArgumentException("Only one of method,field,clazz may be specified");

		this.el = el;
		this.type = type;

		this.method = method;
		this.field = field;
		this.clazz = clazz;
	}


	public String getTypeName()
	{
		if (clazz != null)
			return clazz.getQualifiedName();
		else if (type instanceof PsiPrimitiveType)
			return ((PsiPrimitiveType) type).getBoxedTypeName();
		else if (type instanceof PsiClassType)
			return ((PsiClassType) type).resolve().getQualifiedName(); // TODO is this right?
		else
			throw new IllegalArgumentException("Don't know how to find type name for " + type);
	}

	/**
	 * Returns true if this member is generated (i.e. should not be set)
	 *
	 * @return
	 */
	public boolean isGenerated()
	{
		return AnnotationConstant.GENERATED_VALUE.has(el);
	}

	public String getName()
	{
		if (field != null)
			return field.getName();
		else if (method != null)
		{
			final String name = method.getName();
			final String withoutPrefix;

			if (name.startsWith("is"))
				withoutPrefix = name.substring(2);
			else if (name.startsWith("get") || name.startsWith("set"))
				withoutPrefix = name.substring(3);
			else
				withoutPrefix = name;

			return Introspector.decapitalize(withoutPrefix);
		}
		else
		{
			return clazz.getName();
		}
	}

	public boolean isPersistent()
	{
		return hasOne(el,
		              AnnotationConstant.COLUMN,
		              AnnotationConstant.JOIN_COLUMN,
		              AnnotationConstant.ID,
		              AnnotationConstant.VERSION,
		              AnnotationConstant.ENTITY);
	}

	public boolean isId()
	{
		return AnnotationConstant.ID.has(el);
	}

	public boolean isVersion()
	{
		return AnnotationConstant.VERSION.has(el);
	}

	public boolean isNullable()
	{
		final String colNullable = AnnotationConstant.COLUMN.read(el, "nullable");
		final String jcolNullable = AnnotationConstant.JOIN_COLUMN.read(el, "nullable");

		if (colNullable != null)
			return Boolean.parseBoolean(colNullable); // prefer @Column.nullable
		else if (jcolNullable != null)
			return Boolean.parseBoolean(jcolNullable);// next @JoinColumn.nullable
		else
			return false; // by default not nullable
	}

	public boolean isCollection()
	{
		// Get java.util.Collection
		PsiType collectionType = getType(Collection.class.getName());

		return (type instanceof PsiArrayType) || type.isAssignableFrom(collectionType);
	}

	private PsiType getType(String fqname)
	{
		return JavaPsiFacade.getInstance(el.getProject()).getElementFactory().createTypeByFQClassName(fqname);
	}

	public boolean isEnum()
	{
		final PsiType enumType = getType(Enum.class.getName());

		return enumType.isAssignableFrom(type);
	}

	public boolean isBoolean()
	{
		return PsiType.BOOLEAN.isAssignableFrom(type);
	}

	public boolean isNumber()
	{
		return PsiType.LONG.isAssignableFrom(type) || PsiType.INT.isAssignableFrom(type) || PsiType.SHORT.isAssignableFrom(type);
	}

	public boolean isString()
	{
		final PsiType stringType = getType(String.class.getName());

		return type.isAssignableFrom(stringType);
	}

	public boolean isSimpleColumn()
	{
		return isNumber() || isBoolean() || isEnum() || isString();
	}


	private static int countNotNull(Object... objs)
	{
		int total = 0;
		for (Object obj : objs)
			if (obj != null)
				total++;

		return total;
	}

	private static boolean hasOne(PsiModifierListOwner element, AnnotationConstant... constants)
	{
		for (AnnotationConstant constant : constants)
			if (constant.has(element))
				return true;

		return false;
	}

	public PsiField[] getFields()
	{
		return ((PsiClassType) type).resolve().getAllFields();
	}

	public PsiMethod[] getMethods()
	{
		return ((PsiClassType) type).resolve().getAllMethods();
	}
}
