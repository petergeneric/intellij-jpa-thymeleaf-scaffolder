package com.peterphi.scaf.jpathymeleaf.codegen;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.peterphi.scaf.jpathymeleaf.util.MemberReflection;

import java.util.ArrayList;
import java.util.List;

public class PersistentMember
{
	private final MemberReflection reflection;


	public PersistentMember(PsiClass clazz)
	{
		this(new MemberReflection(clazz));
	}


	private PersistentMember(MemberReflection reflection)
	{
		this.reflection = reflection;
	}


	public String getName()
	{
		return reflection.getName();
	}


	public boolean isGenerated()
	{
		return reflection.isGenerated();
	}


	public boolean isNullable()
	{
		return reflection.isNullable();
	}


	public boolean isCollection()
	{
		return reflection.isCollection();
	}


	public boolean isEnum()
	{
		return reflection.isEnum();
	}


	public boolean isBoolean()
	{
		return reflection.isBoolean();
	}


	public boolean isNumber()
	{
		return reflection.isNumber();
	}


	public boolean isString()
	{
		return reflection.isString();
	}


	public boolean isSimpleColumn()
	{
		return reflection.isSimpleColumn();
	}


	public String getTypeName()
	{
		return reflection.getTypeName();
	}


	public List<PersistentMember> getFields()
	{
		List<PersistentMember> list = new ArrayList<PersistentMember>();

		for (MemberReflection member : getMembers())
		{
			// Discard non-persistent fields
			if (member.isPersistent())
				list.add(new PersistentMember(member));
		}

		return list;
	}


	private List<MemberReflection> getMembers()
	{
		List<MemberReflection> list = new ArrayList<MemberReflection>();

		for (PsiField field : reflection.getFields())
			list.add(new MemberReflection(field));

		for (PsiMethod method : reflection.getMethods())
			list.add(new MemberReflection(method));

		return list;
	}
}
