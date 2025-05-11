package augmy.interactive.com.data

/** base URL for the brand storage */
private const val BASE_URL = "https://augmy.org/storage/"

sealed class Asset {

    protected open val appendix: String = ""
    open val url: String = ""

    sealed class Image(name: String): Asset() {

        final override val appendix: String = "img/"
        override val url = BASE_URL + appendix + name
        open val thumbnail = BASE_URL + appendix + "thumbnail/" + "tn_$name"

        data object NaturePalette: Image("7b270e82-4ceb-43dc-bbc1-b5d47ff1cfc0.jpg")
            data object NoRobots: Image("e9ba4583-1760-4c57-96dc-20bf28ff8a2a.png")
        data object SolutionIntegration: Image("bc1cedb8-e9b9-48c1-8555-e5f10d5b287a.png")
        data object ChatIntegration: Image("b91e606f-0689-4049-8742-b110e7c4129b.png")
        data object SentimentAnalysis: Image("69ab0efd-bbff-4bd7-a9fd-1f21b72b8404.png")
        data object CustomerSupport: Image("91fd6168-0ef7-42b7-8857-37840ae6f875.png")
        data object DataAssurance: Image("cd32c349-3104-419f-9e76-4c92c1990a3b.png")
        data object IAmJustAFight: Image("imjustafish.jpg")
        data object EarHelp: Image("e66f02af-b274-4acc-b25a-7bbcdd8e0131.jpg")
        data object Cooperation: Image("e4d1017e-d7d1-442c-ab1f-8a588d67b5b0.jpg")
        data object CustomDesign: Image("5802c63e-45fc-4687-8c72-d52066ef45e8.jpg")
        data object DesignConstruction: Image("90c610ff-e2e1-48e5-82c9-3c9440b765c3.jpg")
        data object Experiment: Image("95b14a4e-5182-4375-b78f-d0f6fd504926.jpg")

        data object HeiderSimmelPreview: Image("heider-simmel-preview.jpg")
    }

    sealed class Logo(name: String): Asset() {
        final override val appendix: String = "logo/"
        override val url = BASE_URL + appendix + name

        data object Twitter: Logo("twitter.svg")
        data object Instagram: Logo("instagram.svg")
        data object LinkedIn: Logo("linkedin.svg")
        data object Youtube: Logo("youtube.svg")
        data object Bluesky: Logo("bluesky.svg")
        data object Discord: Logo("discord.svg")
        data object Kofi: Logo("bluesky.svg") {
            override val url: String = "https://storage.ko-fi.com/cdn/brandasset/v2/kofi_symbol.png"
        }
    }
}