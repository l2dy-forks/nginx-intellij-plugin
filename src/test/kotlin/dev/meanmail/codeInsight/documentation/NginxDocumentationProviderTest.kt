package dev.meanmail.codeInsight.documentation

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Assert.*

class NginxDocumentationProviderTest : BasePlatformTestCase() {

    fun testGenerateDocForListenDirective() {
        val file = myFixture.configureByText(
            "nginx.conf",
            """
            server {
                listen<caret> 80;
            }
            """.trimIndent()
        )

        val element = file.findElementAt(myFixture.caretOffset)!!
        val provider = NginxDocumentationProvider()

        val doc = provider.generateDoc(element, element)

        assertNotNull(doc)
        val documentation = doc!!
        assertTrue(documentation.contains("<b>listen</b>"))
        assertTrue(documentation.contains("Configures the IP address"))
        assertTrue(documentation.contains("<b>Context:</b>"))
        assertTrue(documentation.contains("server"))
        assertTrue(documentation.contains("<b>Parameters:</b>"))
        assertTrue(documentation.contains("<b>address</b>"))
    }

    fun testQuickNavigateInfoForListenDirective() {
        val file = myFixture.configureByText(
            "nginx.conf",
            """
            server {
                listen<caret> 80;
            }
            """.trimIndent()
        )

        val element = file.findElementAt(myFixture.caretOffset)!!
        val provider = NginxDocumentationProvider()

        val quickInfo = provider.getQuickNavigateInfo(element, element)

        assertNotNull(quickInfo)
        val info = quickInfo!!
        assertTrue(info.startsWith("<b>listen</b>"))
        assertTrue(info.contains("Configures the IP address"))
        assertFalse(info.contains("Parameters"))
    }
}
