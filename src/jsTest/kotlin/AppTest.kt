import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.browser.document
import org.w3c.dom.HTMLTextAreaElement
import react.dom.client.createRoot
import react.create
import components.Chat
import components.Editor
import utils.executeScript
import utils.executePythonScript
import utils.executeNodeJsScript
import utils.generateNodeJsCode

class AppTest {

    @Test
    fun testChatComponent() {
        val container = document.createElement("div")
        document.body!!.appendChild(container)
        val root = createRoot(container)
        val initialMessages = listOf(Message(role = "user", content = "Hello"))
        root.render(Chat.create {
            this.initialMessages = initialMessages
            this.storeMessageHistory = { messages -> }
            this.importChat = { description, messages -> }
            this.exportChat = { }
            this.description = "Test Chat"
        })

        val textarea = document.querySelector("textarea") as HTMLTextAreaElement
        assertEquals("How can Bolt help you today?", textarea.placeholder)
    }

    @Test
    fun testEditorComponent() {
        val container = document.createElement("div")
        document.body!!.appendChild(container)
        val root = createRoot(container)
        root.render(Editor.create {
            this.initialValue = "Initial text"
            this.onChange = { newValue -> }
        })

        val textarea = document.querySelector("textarea") as HTMLTextAreaElement
        assertEquals("Initial text", textarea.value)
    }

    @Test
    fun testExecuteScript() = runBlocking {
        val result = executeScript("echo Hello, World!").await()
        assertEquals("Hello, World!\n", result)
    }

    @Test
    fun testExecutePythonScript() = runBlocking {
        val result = executePythonScript("path/to/script.py", listOf("arg1", "arg2")).await()
        assertTrue(result.contains("Expected output"))
    }

    @Test
    fun testExecuteNodeJsScript() = runBlocking {
        val result = executeNodeJsScript("path/to/script.js", listOf("arg1", "arg2")).await()
        assertTrue(result.contains("Expected output"))
    }

    @Test
    fun testGenerateNodeJsCode() {
        val kotlinJsCode = "/* Your Kotlin/JS code here */"
        val nodeJsCode = generateNodeJsCode(kotlinJsCode)
        assertTrue(nodeJsCode.contains("Expected Node.js code"))
    }
}
