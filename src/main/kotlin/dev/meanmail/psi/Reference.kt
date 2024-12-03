package dev.meanmail.psi

import com.intellij.openapi.paths.WebReference
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import dev.meanmail.resolveFile

interface ReferenceElement : NamedElement {
    val ref: String
}

class Reference(element: ReferenceElement) :
    PsiReferenceBase<ReferenceElement>(element, null, false) {
    override fun resolve(): PsiElement? {
        return resolveFile(element.ref)
    }

    private fun resolveFile(ref: String): PsiElement? {
        if (ref.startsWith("http")) {
            return WebReference(element, ref).resolve()
        }
        if (ref.isEmpty()) return null
        var file: VirtualFile? = null
        val directory = element.containingFile
            ?.containingDirectory?.virtualFile ?: return null
        file = resolveFile(ref, directory) ?: return null

        return PsiManager.getInstance(element.project).findFile(file)
    }

    override fun getRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val newElement = element.setName(newElementName)
        element.parent.node.replaceChild(element.node, newElement.node)
        return newElement
    }
}
