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
		final PsiClass clazz = getPsiClassFromContext(e);

		// Perform the generate action
		new WriteCommandAction.Simple(clazz.getProject())
		{
			@Override
			protected void run() throws Throwable
			{
				generate(clazz);
			}
		}.execute();
	}


	/**
	 * Generate a .html file based on the entity. Should be called inside a write command.
	 *
	 * @param clazz
	 */
	public void generate(final PsiClass clazz)
	{
		// Generate the code we'll put into the file
		final String text = new ThymeleafCodeGenerator().generate(clazz);

		final PsiDirectory directory = clazz.getContainingFile().getContainingDirectory();
		final String fileName = getEntityName(clazz) + "_template.html";

		final PsiFile file = getOrCreate(directory, fileName);

		setText(file, text);
	}


	/**
	 * Write some text to a PsiFile
	 *
	 * @param file
	 * @param text
	 */
	protected void setText(PsiFile file, String text)
	{
		final PsiDocumentManager manager = PsiDocumentManager.getInstance(file.getProject());

		final Document document = manager.getDocument(file);

		document.setText(text);
	}


	protected String getEntityName(PsiClass clazz)
	{
		return JPAAnnotation.ENTITY.read(clazz, "name");
	}


	/**
	 * Get or create a file under a directory
	 *
	 * @param directory
	 * @param fileName
	 *
	 * @return
	 */
	protected PsiFile getOrCreate(PsiDirectory directory, final String fileName)
	{
		final PsiFile file = directory.findFile(fileName);

		// Create the file if necessary
		if (file != null)
			return file; // the file already existed
		else
			return directory.createFile(fileName); // the file didn't exist, create it
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
