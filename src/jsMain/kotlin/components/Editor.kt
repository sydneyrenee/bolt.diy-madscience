package components

import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.textarea
import utils.generateNodeJsCode

external interface EditorProps : Props {
    var initialValue: String
    var onChange: (String) -> Unit
}

val Editor = FC<EditorProps> { props ->
    val (value, setValue) = useState(props.initialValue)
    val editorRef = useRef<HTMLTextAreaElement?>(null)

    useEffectOnce {
        val editorElement = editorRef.current
        editorElement?.value = props.initialValue

        val handleInput = {
            val newValue = editorElement?.value ?: ""
            setValue(newValue)
            props.onChange(newValue)
        }

        editorElement?.addEventListener("input", { handleInput() })

        cleanup {
            editorElement?.removeEventListener("input", { handleInput() })
        }
    }

    val handleSave = {
        val kotlinJsCode = editorRef.current?.value ?: ""
        val nodeJsCode = generateNodeJsCode(kotlinJsCode)
        println("Generated Node.js code: $nodeJsCode")
    }

    div {
        className = "editor-container"
        textarea {
            ref = editorRef
            className = "editor-textarea"
            value = value
            onChange = {
                val newValue = it.target.value
                setValue(newValue)
                props.onChange(newValue)
            }
        }
        button {
            +"Save"
            onClick = { handleSave() }
        }
    }
}

fun main() {
    val root = document.getElementById("root") as HTMLDivElement
    render(root) {
        Editor {
            initialValue = "Type here..."
            onChange = { newValue -> println("Editor value changed: $newValue") }
        }
    }
}
