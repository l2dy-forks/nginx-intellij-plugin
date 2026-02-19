package dev.meanmail.codeInsight.formatter

import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import dev.meanmail.NginxLanguage
import dev.meanmail.psi.Types

fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
    val keepBlankLines = settings.getCommonSettings(NginxLanguage).KEEP_BLANK_LINES_IN_CODE
    return SpacingBuilder(settings, NginxLanguage)
        .afterInside(Types.LBRACE, BRACE_BLOCKS)
        .parentDependentLFSpacing(1, 1, true, keepBlankLines)
        .beforeInside(Types.RBRACE, BRACE_BLOCKS)
        .parentDependentLFSpacing(1, 1, true, keepBlankLines)
        .before(Types.SEMICOLON).spaceIf(false)
        .before(BRACE_BLOCKS).spaces(1)
}
