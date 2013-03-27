package com.peterphi.scaf.jpathymeleaf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.peterphi.scaf.jpathymeleaf.codegen.ThymeleafCodeGenerator;
import com.peterphi.scaf.jpathymeleaf.util.JPAAnnotation;

/**
 * Action to generate a Thymeleaf Template .html for a given javax.persistence.Entity
 */
public class GenerateThymeleafEntityTemplate extends AnAction
{
	public void actionPerformed(AnActionEvent e)
	{
		final PsiClass psiClass = getPsiClassFromContext(e);

		generate(psiClass);
	}


	public void generate(final PsiClass clazz)
	{

		new WriteCommandAction.Simple(clazz.getProject())
		{
			@Override
			protected void run() throws Throwable
			{
				final PsiDocumentManager manager = PsiDocumentManager.getInstance(clazz.getProject());

				final String entityName = JPAAnnotation.ENTITY.read(clazz, "name");

				// Generate a new Document
				final PsiDirectory directory = clazz.getContainingFile().getContainingDirectory();
				final PsiFile file = directory.createFile(entityName + "_template.html");
				final Document document = manager.getDocument(file);

				final String text = new ThymeleafCodeGenerator().generate(clazz);

				document.setText(text);

				manager.commitDocument(document);
			}
		}.execute();
	}


	@Override
	public void update(final AnActionEvent e)
	{

		final PsiClass clazz = getPsiClassFromContext(e);

		if (clazz == null)
		{
			e.getPresentation().setEnabled(false);
			return;
		}

		PsiAnnotation annotation = JPAAnnotation.ENTITY.find(clazz);

		e.getPresentation().setEnabled(annotation != null);
	}


	private PsiClass getPsiClassFromContext(final AnActionEvent e)
	{
		final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
		final Editor editor = e.getData(PlatformDataKeys.EDITOR);

		if (psiFile == null || editor == null)
		{
			return null;
		}
		else
		{
			final PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());

			return PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
		}
	}
}
