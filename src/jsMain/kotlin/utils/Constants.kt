package utils

import kotlinx.browser.window

const val WORK_DIR_NAME = "project"
const val WORK_DIR = "/home/$WORK_DIR_NAME"
const val MODIFICATIONS_TAG_NAME = "bolt_file_modifications"
val MODEL_REGEX = Regex("""^\[Model: (.*?)\]\n\n""")
val PROVIDER_REGEX = Regex("""\[Provider: (.*?)\]\n\n""")
const val DEFAULT_MODEL = "claude-3-5-sonnet-latest"
const val PROMPT_COOKIE_KEY = "cachedPrompt"

data class ModelInfo(
    val name: String,
    val label: String,
    val provider: String,
    val maxTokenAllowed: Int
)

data class ProviderInfo(
    val name: String,
    val staticModels: List<ModelInfo>,
    val getDynamicModels: (suspend () -> List<ModelInfo>)? = null,
    val getApiKeyLink: String? = null,
    val labelForGetApiKey: String? = null,
    val icon: String? = null
)

val PROVIDER_LIST = listOf(
    ProviderInfo(
        name = "Anthropic",
        staticModels = listOf(
            ModelInfo("claude-3-5-sonnet-latest", "Claude 3.5 Sonnet (new)", "Anthropic", 8000),
            ModelInfo("claude-3-5-sonnet-20240620", "Claude 3.5 Sonnet (old)", "Anthropic", 8000),
            ModelInfo("claude-3-5-haiku-latest", "Claude 3.5 Haiku (new)", "Anthropic", 8000),
            ModelInfo("claude-3-opus-latest", "Claude 3 Opus", "Anthropic", 8000),
            ModelInfo("claude-3-sonnet-20240229", "Claude 3 Sonnet", "Anthropic", 8000),
            ModelInfo("claude-3-haiku-20240307", "Claude 3 Haiku", "Anthropic", 8000)
        ),
        getApiKeyLink = "https://console.anthropic.com/settings/keys"
    ),
    ProviderInfo(
        name = "Ollama",
        staticModels = emptyList(),
        getDynamicModels = { getOllamaModels() },
        getApiKeyLink = "https://ollama.com/download",
        labelForGetApiKey = "Download Ollama",
        icon = "i-ph:cloud-arrow-down"
    ),
    ProviderInfo(
        name = "OpenAILike",
        staticModels = emptyList(),
        getDynamicModels = { getOpenAILikeModels() }
    ),
    ProviderInfo(
        name = "Cohere",
        staticModels = listOf(
            ModelInfo("command-r-plus-08-2024", "Command R plus Latest", "Cohere", 4096),
            ModelInfo("command-r-08-2024", "Command R Latest", "Cohere", 4096),
            ModelInfo("command-r-plus", "Command R plus", "Cohere", 4096),
            ModelInfo("command-r", "Command R", "Cohere", 4096),
            ModelInfo("command", "Command", "Cohere", 4096),
            ModelInfo("command-nightly", "Command Nightly", "Cohere", 4096),
            ModelInfo("command-light", "Command Light", "Cohere", 4096),
            ModelInfo("command-light-nightly", "Command Light Nightly", "Cohere", 4096),
            ModelInfo("c4ai-aya-expanse-8b", "c4AI Aya Expanse 8b", "Cohere", 4096),
            ModelInfo("c4ai-aya-expanse-32b", "c4AI Aya Expanse 32b", "Cohere", 4096)
        ),
        getApiKeyLink = "https://dashboard.cohere.com/api-keys"
    ),
    ProviderInfo(
        name = "OpenRouter",
        staticModels = listOf(
            ModelInfo("gpt-4o", "GPT-4o", "OpenAI", 8000),
            ModelInfo("anthropic/claude-3.5-sonnet", "Anthropic: Claude 3.5 Sonnet (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("anthropic/claude-3-haiku", "Anthropic: Claude 3 Haiku (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("deepseek/deepseek-coder", "Deepseek-Coder V2 236B (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("google/gemini-flash-1.5", "Google Gemini Flash 1.5 (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("google/gemini-pro-1.5", "Google Gemini Pro 1.5 (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("x-ai/grok-beta", "xAI Grok Beta (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("mistralai/mistral-nemo", "OpenRouter Mistral Nemo (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("qwen/qwen-110b-chat", "OpenRouter Qwen 110b Chat (OpenRouter)", "OpenRouter", 8000),
            ModelInfo("cohere/command", "Cohere Command (OpenRouter)", "OpenRouter", 4096)
        ),
        getDynamicModels = { getOpenRouterModels() },
        getApiKeyLink = "https://openrouter.ai/settings/keys"
    ),
    ProviderInfo(
        name = "Google",
        staticModels = listOf(
            ModelInfo("gemini-1.5-flash-latest", "Gemini 1.5 Flash", "Google", 8192),
            ModelInfo("gemini-2.0-flash-exp", "Gemini 2.0 Flash", "Google", 8192),
            ModelInfo("gemini-1.5-flash-002", "Gemini 1.5 Flash-002", "Google", 8192),
            ModelInfo("gemini-1.5-flash-8b", "Gemini 1.5 Flash-8b", "Google", 8192),
            ModelInfo("gemini-1.5-pro-latest", "Gemini 1.5 Pro", "Google", 8192),
            ModelInfo("gemini-1.5-pro-002", "Gemini 1.5 Pro-002", "Google", 8192),
            ModelInfo("gemini-exp-1206", "Gemini exp-1206", "Google", 8192)
        ),
        getApiKeyLink = "https://aistudio.google.com/app/apikey"
    ),
    ProviderInfo(
        name = "Groq",
        staticModels = listOf(
            ModelInfo("llama-3.1-8b-instant", "Llama 3.1 8b (Groq)", "Groq", 8000),
            ModelInfo("llama-3.2-11b-vision-preview", "Llama 3.2 11b (Groq)", "Groq", 8000),
            ModelInfo("llama-3.2-90b-vision-preview", "Llama 3.2 90b (Groq)", "Groq", 8000),
            ModelInfo("llama-3.2-3b-preview", "Llama 3.2 3b (Groq)", "Groq", 8000),
            ModelInfo("llama-3.2-1b-preview", "Llama 3.2 1b (Groq)", "Groq", 8000),
            ModelInfo("llama-3.3-70b-versatile", "Llama 3.3 70b (Groq)", "Groq", 8000)
        ),
        getApiKeyLink = "https://console.groq.com/keys"
    ),
    ProviderInfo(
        name = "HuggingFace",
        staticModels = listOf(
            ModelInfo("Qwen/Qwen2.5-Coder-32B-Instruct", "Qwen2.5-Coder-32B-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("01-ai/Yi-1.5-34B-Chat", "Yi-1.5-34B-Chat (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("codellama/CodeLlama-34b-Instruct-hf", "CodeLlama-34b-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("NousResearch/Hermes-3-Llama-3.1-8B", "Hermes-3-Llama-3.1-8B (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("Qwen/Qwen2.5-Coder-32B-Instruct", "Qwen2.5-Coder-32B-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("Qwen/Qwen2.5-72B-Instruct", "Qwen2.5-72B-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("meta-llama/Llama-3.1-70B-Instruct", "Llama-3.1-70B-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("meta-llama/Llama-3.1-405B", "Llama-3.1-405B (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("01-ai/Yi-1.5-34B-Chat", "Yi-1.5-34B-Chat (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("codellama/CodeLlama-34b-Instruct-hf", "CodeLlama-34b-Instruct (HuggingFace)", "HuggingFace", 8000),
            ModelInfo("NousResearch/Hermes-3-Llama-3.1-8B", "Hermes-3-Llama-3.1-8B (HuggingFace)", "HuggingFace", 8000)
        ),
        getApiKeyLink = "https://huggingface.co/settings/tokens"
    ),
    ProviderInfo(
        name = "OpenAI",
        staticModels = listOf(
            ModelInfo("gpt-4o-mini", "GPT-4o Mini", "OpenAI", 8000),
            ModelInfo("gpt-4-turbo", "GPT-4 Turbo", "OpenAI", 8000),
            ModelInfo("gpt-4", "GPT-4", "OpenAI", 8000),
            ModelInfo("gpt-3.5-turbo", "GPT-3.5 Turbo", "OpenAI", 8000)
        ),
        getApiKeyLink = "https://platform.openai.com/api-keys"
    ),
    ProviderInfo(
        name = "xAI",
        staticModels = listOf(
            ModelInfo("grok-beta", "xAI Grok Beta", "xAI", 8000)
        ),
        getApiKeyLink = "https://docs.x.ai/docs/quickstart#creating-an-api-key"
    ),
    ProviderInfo(
        name = "Deepseek",
        staticModels = listOf(
            ModelInfo("deepseek-coder", "Deepseek-Coder", "Deepseek", 8000),
            ModelInfo("deepseek-chat", "Deepseek-Chat", "Deepseek", 8000)
        ),
        getApiKeyLink = "https://platform.deepseek.com/apiKeys"
    ),
    ProviderInfo(
        name = "Mistral",
        staticModels = listOf(
            ModelInfo("open-mistral-7b", "Mistral 7B", "Mistral", 8000),
            ModelInfo("open-mixtral-8x7b", "Mistral 8x7B", "Mistral", 8000),
            ModelInfo("open-mixtral-8x22b", "Mistral 8x22B", "Mistral", 8000),
            ModelInfo("open-codestral-mamba", "Codestral Mamba", "Mistral", 8000),
            ModelInfo("open-mistral-nemo", "Mistral Nemo", "Mistral", 8000),
            ModelInfo("ministral-8b-latest", "Mistral 8B", "Mistral", 8000),
            ModelInfo("mistral-small-latest", "Mistral Small", "Mistral", 8000),
            ModelInfo("codestral-latest", "Codestral", "Mistral", 8000),
            ModelInfo("mistral-large-latest", "Mistral Large Latest", "Mistral", 8000)
        ),
        getApiKeyLink = "https://console.mistral.ai/api-keys/"
    ),
    ProviderInfo(
        name = "LMStudio",
        staticModels = emptyList(),
        getDynamicModels = { getLMStudioModels() },
        getApiKeyLink = "https://lmstudio.ai/",
        labelForGetApiKey = "Get LMStudio",
        icon = "i-ph:cloud-arrow-down"
    ),
    ProviderInfo(
        name = "Together",
        staticModels = listOf(
            ModelInfo("Qwen/Qwen2.5-Coder-32B-Instruct", "Qwen/Qwen2.5-Coder-32B-Instruct", "Together", 8000),
            ModelInfo("meta-llama/Llama-3.2-90B-Vision-Instruct-Turbo", "meta-llama/Llama-3.2-90B-Vision-Instruct-Turbo", "Together", 8000),
            ModelInfo("mistralai/Mixtral-8x7B-Instruct-v0.1", "Mixtral 8x7B Instruct", "Together", 8192)
        ),
        getDynamicModels = { getTogetherModels() },
        getApiKeyLink = "https://api.together.xyz/settings/api-keys"
    ),
    ProviderInfo(
        name = "Perplexity",
        staticModels = listOf(
            ModelInfo("llama-3.1-sonar-small-128k-online", "Sonar Small Online", "Perplexity", 8192),
            ModelInfo("llama-3.1-sonar-large-128k-online", "Sonar Large Online", "Perplexity", 8192),
            ModelInfo("llama-3.1-sonar-huge-128k-online", "Sonar Huge Online", "Perplexity", 8192)
        ),
        getApiKeyLink = "https://www.perplexity.ai/settings/api"
    )
)

val DEFAULT_PROVIDER = PROVIDER_LIST[0]

val staticModels: List<ModelInfo> = PROVIDER_LIST.flatMap { it.staticModels }

var MODEL_LIST: List<ModelInfo> = staticModels

suspend fun getModelList(apiKeys: Map<String, String>, providerSettings: Map<String, ProviderInfo>? = null): List<ModelInfo> {
    MODEL_LIST = PROVIDER_LIST.filter { it.getDynamicModels != null }
        .flatMap { it.getDynamicModels?.invoke() ?: emptyList() } + staticModels
    return MODEL_LIST
}

suspend fun getTogetherModels(): List<ModelInfo> {
    // Implement the logic to fetch Together models
    return emptyList()
}

suspend fun getOllamaModels(): List<ModelInfo> {
    // Implement the logic to fetch Ollama models
    return emptyList()
}

suspend fun getOpenAILikeModels(): List<ModelInfo> {
    // Implement the logic to fetch OpenAILike models
    return emptyList()
}

suspend fun getOpenRouterModels(): List<ModelInfo> {
    // Implement the logic to fetch OpenRouter models
    return emptyList()
}

suspend fun getLMStudioModels(): List<ModelInfo> {
    // Implement the logic to fetch LMStudio models
    return emptyList()
}

suspend fun initializeModelList(providerSettings: Map<String, ProviderInfo>? = null): List<ModelInfo> {
    val apiKeys = mutableMapOf<String, String>()
    val storedApiKeys = window.localStorage.getItem("apiKeys")
    if (storedApiKeys != null) {
        apiKeys.putAll(JSON.parse<Map<String, String>>(storedApiKeys))
    }
    MODEL_LIST = PROVIDER_LIST.filter { it.getDynamicModels != null }
        .flatMap { it.getDynamicModels?.invoke() ?: emptyList() } + staticModels
    return MODEL_LIST
}
