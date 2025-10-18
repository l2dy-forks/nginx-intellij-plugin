package dev.meanmail.codeInsight.inspections

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NginxDirectiveInspectionTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return "src/test/resources/dev/meanmail/inspections/directive"
    }

    private fun doTest(fileName: String) {
        myFixture.enableInspections(NginxDirectiveInspection())
        myFixture.configureByFile(fileName)
        myFixture.checkHighlighting(true, false, true)
    }

    fun testLuaDirectives() {
        doTest("luaDirectives.nginx")
    }
}
