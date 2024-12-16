package components

import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.textarea
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.svg
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h5
import react.dom.html.ReactHTML.h6
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.textarea
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.svg
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h5
import react.dom.html.ReactHTML.h6
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.option
import utils.generateNodeJsCode

external interface ChatProps : Props {
    var initialMessages: List<Message>
    var storeMessageHistory: (List<Message>) -> Unit
    var importChat: (String, List<Message>) -> Unit
    var exportChat: () -> Unit
    var description: String?
}

val Chat = FC<ChatProps> { props ->
    val (messages, setMessages) = useState(props.initialMessages)
    val (input, setInput) = useState("")
    val (isStreaming, setIsStreaming) = useState(false)
    val (chatStarted, setChatStarted) = useState(props.initialMessages.isNotEmpty())
    val textareaRef = useRef<HTMLTextAreaElement>(null)

    val handleInputChange: (React.ChangeEvent<HTMLTextAreaElement>) -> Unit = { event ->
        setInput(event.target.value)
    }

    val sendMessage: (React.UIEvent) -> Unit = { event ->
        event.preventDefault()
        if (input.isNotEmpty()) {
            setIsStreaming(true)
            MainScope().launch {
                // Simulate sending message to server and receiving response
                val newMessage = Message(role = "user", content = input)
                setMessages(messages + newMessage)
                setInput("")
                setIsStreaming(false)
                props.storeMessageHistory(messages + newMessage)

                // Generate Node.js code from Kotlin/JS code
                val nodeJsCode = generateNodeJsCode(input)
                println("Generated Node.js code: $nodeJsCode")
            }
        }
    }

    div {
        if (!chatStarted) {
            div {
                h1 { +"Where ideas begin" }
                p { +"Bring ideas to life in seconds or get help on existing projects." }
            }
        }
        div {
            messages.forEach { message ->
                div {
                    +message.content
                }
            }
            textarea {
                ref = textareaRef
                value = input
                onChange = handleInputChange
                placeholder = "How can Bolt help you today?"
            }
            button {
                +"Send"
                onClick = sendMessage
            }
        }
    }
}
