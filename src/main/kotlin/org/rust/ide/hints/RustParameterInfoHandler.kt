package org.rust.ide.hints

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.lang.parameterInfo.*
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.util.parentOfType

/**
 * Provides functions/methods arguments hint.
 */
class RustParameterInfoHandler : ParameterInfoHandler<PsiElement, RustArgumentsDescription> {

    var hintText: String = ""

    override fun couldShowInLookup() = true

    override fun tracksParameterIndex() = true

    override fun getParameterCloseChars() = ",)"

    override fun getParametersForLookup(item: LookupElement, context: ParameterInfoContext?): Array<out Any> {
        val el = item.`object` as PsiElement
        val p = el.parent?.parent
        return if (p is RustCallExprElement && p.declaration != null || p is RustMethodCallExprElement && p.declaration != null) arrayOf(p) else emptyArray()
    }

    override fun getParametersForDocumentation(p: RustArgumentsDescription, context: ParameterInfoContext?) =
        p.arguments

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiElement? {
        val contextElement = context.file.findElementAt(context.editor.caretModel.offset) ?: return null
        return findElementForParameterInfo(contextElement)
    }

    fun findElementForParameterInfo(contextElement: PsiElement) =
        contextElement.parentOfType<RustArgListElement>()

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext) =
        context.file.findElementAt(context.editor.caretModel.offset)

    override fun showParameterInfo(element: PsiElement, context: CreateParameterInfoContext) {
        if (element !is RustArgListElement) return
        val argsDescr = RustArgumentsDescription.findDescription(element) ?: return
        context.itemsToShow = arrayOf(argsDescr)
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun updateParameterInfo(place: PsiElement, context: UpdateParameterInfoContext) {
        val argIndex = findArgumentIndex(place)
        if (argIndex == INVALID_INDEX) {
            context.removeHint()
            return
        }
        context.setCurrentParameter(argIndex)
        when {
            context.parameterOwner == null -> context.parameterOwner = place
            context.parameterOwner != findElementForParameterInfo(place) -> {
                context.removeHint()
                return
            }
        }
        context.objectsToView.mapIndexed { i, o -> context.setUIComponentEnabled(i, true) }
    }

    override fun updateUI(p: RustArgumentsDescription?, context: ParameterInfoUIContext) {
        if (p == null) {
            context.isUIComponentEnabled = false
            return
        }
        val range = p.getArgumentRange(context.currentParameterIndex)
        hintText = p.presentText
        context.setupUIComponentPresentation(
            hintText,
            range.startOffset,
            range.endOffset,
            !context.isUIComponentEnabled,
            false,
            false,
            context.defaultParameterColor)
    }

    /**
     * Finds index of the argument in the given place
     */
    private fun findArgumentIndex(place: PsiElement): Int {
        val callArgs = place.parentOfType<RustArgListElement>() ?: return INVALID_INDEX
        val descr = RustArgumentsDescription.findDescription(callArgs) ?: return INVALID_INDEX
        var index = -1
        if (descr.arguments.isNotEmpty()) {
            index += generateSequence(callArgs.firstChild, { c -> c.nextSibling})
                .filter { it.text == "," }
                .count({ it.textRange.startOffset < place.textRange.startOffset }) + 1
            if (index >= descr.arguments.size) {
                index = -1
            }
        }
        return index
    }

    private companion object {
        val INVALID_INDEX: Int = -2
    }
}

/**
 * Holds information about arguments from func/method declaration
 */
class RustArgumentsDescription(
    val arguments: Array<String>
) {
    fun getArgumentRange(index: Int): TextRange {
        if (index < 0 || index >= arguments.size) return TextRange.EMPTY_RANGE
        val start = arguments.take(index).sumBy { it.length + 2 }
        val range = TextRange(start, start + arguments[index].length)
        return range
    }

    val presentText = if (arguments.isEmpty()) "<no arguments>" else arguments.joinToString(", ")

    companion object {
        /**
         * Finds declaration of the func/method and creates description of its arguments
         */
        fun findDescription(args: RustArgListElement): RustArgumentsDescription? {
            val call = args.parent
            val paramsList = when (call) {
                is RustCallExprElement -> call.declaration?.parameters
                is RustMethodCallExprElement -> call.declaration?.parameters
                else -> null
            }?.parameterList ?: return null
            val params = paramsList
                .map { "${it.pat?.text ?: "?"}: ${it.type?.text ?: "?"}" }
                .toTypedArray()
            return RustArgumentsDescription(params)
        }
    }
}

private val RustCallExprElement.declaration: RustFnElement?
    get() = (expr as? RustPathExprElement)?.path?.reference?.resolve() as? RustFnElement

private val RustMethodCallExprElement.declaration: RustImplMethodMemberElement?
    get() = reference.resolve() as? RustImplMethodMemberElement
