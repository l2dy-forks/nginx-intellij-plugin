package dev.meanmail.codeInsight.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import dev.meanmail.directives.catalog.Directive
import dev.meanmail.directives.catalog.DirectiveParameter
import dev.meanmail.directives.catalog.findDirectives
import dev.meanmail.psi.DirectiveStmt

class NginxDocumentationProvider : AbstractDocumentationProvider() {

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        val (directive, _) = resolveDirective(element, originalElement) ?: return null

        return buildString {
            append("<b>").append(directive.name).append("</b>")
            append(" <i>(").append(directive.module.name).append(")</i>")
            append(firstParagraph(directive.description))
        }
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val (directive, _) = resolveDirective(element, originalElement) ?: return null

        return buildString {
            append("<b>").append(directive.name).append("</b>")
            append(" <i>(").append(directive.module.name).append(")</i><br><br>")

            if (directive.description.isNotBlank()) {
                append(directive.description)
                append("<br><br>")
            }

            append("<b>Context:</b>")
            val contexts = directive.context
            if (contexts.isEmpty()) {
                append(" any")
            } else {
                append(contexts.joinToString(", ", prefix = " ") { it.name })
            }
            append("<br><br>")

            val parameterSets = parameterSets(directive)
            if (parameterSets.isNotEmpty()) {
                append("<b>Parameters:</b><br>")

                if (parameterSets.size > 1) {
                    parameterSets.forEachIndexed { index, parameters ->
                        if (parameters.isEmpty()) return@forEachIndexed

                        append("Alternative").append(index + 1).append(":<br>")
                        appendParameterBlock(parameters)

                        if (index < parameterSets.lastIndex) {
                            append("<hr>")
                        }
                    }
                } else {
                    appendParameterBlock(parameterSets.single())
                }
            }
        }
    }

    private fun parameterSets(directive: Directive): List<List<DirectiveParameter>> {
        return directive.parameters.takeIf { it.isNotEmpty() }?.let { listOf(it) } ?: emptyList()
    }

    private fun StringBuilder.appendParameterBlock(parameters: List<DirectiveParameter>) {
        parameters.forEach { parameter ->
            val name = parameter.name ?: "value"

            append("<b>").append(name).append("</b>")
            append(if (parameter.required) " (required)" else " (optional)")
            if (parameter.multiple) {
                append(" (multiple)")
            }
            parameter.defaultValue?.let {
                append(" default: ").append(it)
            }
            append("<br>")

            parameter.description?.let {
                append(it).append("<br>")
            }

            parameter.allowedValues?.takeIf { it.isNotEmpty() }?.let { values ->
                append("Allowed values: ")
                append(values.joinToString(", "))
                append("<br>")
            }

            when {
                parameter.minValue != null && parameter.maxValue != null -> {
                    append("Range: ")
                    append(parameter.minValue)
                    append(" .. ")
                    append(parameter.maxValue)
                    append("<br>")
                }

                parameter.minValue != null -> {
                    append("Minimum: ")
                    append(parameter.minValue)
                    append("<br>")
                }

                parameter.maxValue != null -> {
                    append("Maximum: ")
                    append(parameter.maxValue)
                    append("<br>")
                }
            }

            append("<br>")
        }
    }

    private fun resolveDirective(
        element: PsiElement?,
        originalElement: PsiElement?
    ): Pair<Directive, DirectiveStmt>? {
        val candidates = sequenceOf(element, originalElement)

        for (candidate in candidates) {
            if (candidate == null) continue

            val directiveStmt = when (candidate) {
                is DirectiveStmt -> candidate
                else -> PsiTreeUtil.getParentOfType(candidate, DirectiveStmt::class.java, false)
            } ?: continue

            val nameOwner = directiveStmt as? PsiNameIdentifierOwner ?: continue
            val name = nameOwner.name ?: continue

            val path = directiveStmt.path
            val contextPath = if (path.size > 1) path.subList(0, path.size - 1) else emptyList()

            val directive = findDirectives(name, contextPath).firstOrNull()
                ?: findDirectives(name).firstOrNull()

            if (directive != null) {
                return directive to directiveStmt
            }
        }

        return null
    }

    private fun firstParagraph(text: String): String {
        if (text.isBlank()) {
            return text
        }

        val closingParagraph = text.indexOf("</p>")
        if (closingParagraph != -1) {
            return text.take(closingParagraph + "</p>".length)
        }

        val openingParagraph = text.indexOf("<p")
        if (openingParagraph != -1) {
            val closing = text.indexOf("</p>", openingParagraph)
            if (closing != -1) {
                return text.substring(openingParagraph, closing + "</p>".length)
            }
        }

        val doubleBreak = text.indexOf("<br><br>")
        if (doubleBreak != -1) {
            return text.take(doubleBreak + "<br><br>".length)
        }

        return text
    }
}
