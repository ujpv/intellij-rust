package org.rust.ide.intentions

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.util.parentOfType

class WrapLambdaExprIntention : PsiElementBaseIntentionAction() {
    override fun getText() = "Add braces to lambda expression"
    override fun getFamilyName() = text
    override fun startInWriteAction() = true

    private data class Context(
        val lambdaExprBody: RustExprElement
    )

    private fun findContext(element: PsiElement): Context? {
        if (!element.isWritable) return null

        val lambdaExpr = element.parentOfType<RustLambdaExprElement>() ?: return null
        val body = lambdaExpr.expr ?: return null
        return if (body !is RustBlockExprElement) Context(body) else null
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val ctx = findContext(element) ?: return
        val lambdaBody = ctx.lambdaExprBody
        val relativeCaretPosition = editor.caretModel.offset - lambdaBody.textOffset

        val bodyStr = "\n${lambdaBody.text}\n"
        val blockExpr = RustPsiFactory(project).createBlockExpr(bodyStr)

        val offset = ((lambdaBody.replace(blockExpr)) as RustBlockExprElement).block?.expr?.textOffset ?: return
        editor.caretModel.moveToOffset(offset + relativeCaretPosition)
    }

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean =
        findContext(element) != null
}
