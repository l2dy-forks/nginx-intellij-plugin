package dev.meanmail.codeInsight.formatter

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NginxFormatterTest : BasePlatformTestCase() {

    private fun doTest(before: String, after: String) {
        myFixture.configureByText("nginx.conf", before)
        myFixture.performEditorAction(IdeActions.ACTION_EDITOR_REFORMAT)
        myFixture.checkResult(after)
    }

    fun testBasicBlockIndent() {
        doTest(
            "server {\nlisten 80;\nserver_name localhost;\n}",
            "server {\n    listen 80;\n    server_name localhost;\n}"
        )
    }

    fun testNestedBlocks() {
        doTest(
            "http {\nserver {\nlisten 80;\n}\n}",
            "http {\n    server {\n        listen 80;\n    }\n}"
        )
    }

    fun testMapBlockIndent() {
        doTest(
            "map \$uri \$new {\ndefault 0;\n/foo 1;\n}",
            "map \$uri \$new {\n    default 0;\n    /foo 1;\n}"
        )
    }

    fun testGeoBlockIndent() {
        doTest(
            "geo \$geo {\ndefault 0;\n10.0.0.0/8 1;\n}",
            "geo \$geo {\n    default 0;\n    10.0.0.0/8 1;\n}"
        )
    }

    fun testTypesBlockIndent() {
        doTest(
            "types {\ntext/html html;\napplication/json json;\n}",
            "types {\n    text/html html;\n    application/json json;\n}"
        )
    }

    fun testNoSpaceBeforeSemicolon() {
        doTest(
            "listen 80 ;",
            "listen 80;"
        )
    }

    fun testSpaceBeforeBraceBlock() {
        doTest(
            "server{\nlisten 80;\n}",
            "server {\n    listen 80;\n}"
        )
    }

    fun testIfDirective() {
        doTest(
            "if (\$var) {\nreturn 200;\n}",
            "if (\$var) {\n    return 200;\n}"
        )
    }

    fun testPreservesBlankLineAtStartOfBlock() {
        doTest(
            "server {\n\n    listen 80;\n}",
            "server {\n\n    listen 80;\n}"
        )
    }

    fun testPreservesMultiLineContinuation() {
        val text = """
            log_format combined '${'$'}remote_addr - ${'$'}remote_user [${'$'}time_local] '
                                '"${'$'}request" ${'$'}status ${'$'}body_bytes_sent '
                                '"${'$'}http_referer" "${'$'}http_user_agent"';
        """.trimIndent()
        doTest(text, text)
    }

    fun testDeeplyNested() {
        doTest(
            "http {\nserver {\nlocation / {\nreturn 200;\n}\n}\n}",
            "http {\n    server {\n        location / {\n            return 200;\n        }\n    }\n}"
        )
    }
}
