package dev.meanmail.codeInsight.formatter

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NginxAutoIndentTest : BasePlatformTestCase() {

    private fun doTest(before: String, after: String) {
        myFixture.configureByText("nginx.conf", before)
        myFixture.type('\n')
        myFixture.checkResult(after)
    }

    fun testEnterInsideBlock() {
        doTest(
            "server {\n<caret>\n}",
            "server {\n\n    <caret>\n}"
        )
    }

    fun testEnterAfterDirectiveInBlock() {
        doTest(
            "server {\n    listen 80;<caret>\n}",
            "server {\n    listen 80;\n    <caret>\n}"
        )
    }

    fun testEnterAtTopLevel() {
        doTest(
            "<caret>",
            "\n<caret>"
        )
    }

    fun testEnterInsideNestedBlock() {
        doTest(
            "http {\n    server {\n<caret>\n    }\n}",
            "http {\n    server {\n\n        <caret>\n    }\n}"
        )
    }
}
