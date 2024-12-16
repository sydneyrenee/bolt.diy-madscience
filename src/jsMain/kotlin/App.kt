package com.example

import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import com.example.components.Chat
import utils.generateNodeJsCode

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    val root = createRoot(container)
    root.render(Chat.create())
}

fun handleGenerateCode() {
    val kotlinJsCode = "/* Your Kotlin/JS code here */"
    val nodeJsCode = generateNodeJsCode(kotlinJsCode)
    println("Generated Node.js code: $nodeJsCode")
}
