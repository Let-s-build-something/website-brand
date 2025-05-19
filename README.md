# Augmy Brand Website — Kotlin Multiplatform

Our brand website for the movement and start-up **Augmy** is a fully single-page application built using **Compose Kotlin Multiplatform**. The Kotlin code is compiled to **WebAssembly (WASM) JavaScript**, enabling a seamless multiplatform experience on the web.

![Screencastfrom2025-05-1910-39-05-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/92287247-a212-443b-8505-c4669d0a81b7)

## Architecture & Performance Strategy

We aim to keep the **landing page primarily HTML-based**, and lazily load the WASM file after the initial render. This approach significantly reduces the initial load time — which has been the biggest drawback when using Compose Multiplatform for web so far.

Beyond performance, the greatest advantage of this setup is the ease of building new component and UI development in general. Compose Multiplatform allows us to create native-like components and simulate native apps directly in the browser.

## Long-Term Vision

Our ultimate goal is to provide complex simulations and demos of our core functionalities — exactly as they appear in our native multiplatform Augmy app. Using the same Kotlin Multiplatform technology stack across native and web platforms saves a tremendous amount of development time and ensures consistency without discrepancies.

## Navigation & Responsiveness

- Multiple HTML files exist **only for preview purposes**, as many websites require different preview URLs, this is the approach to support it. Once a user visits any of the HTML pages, they are immediately routed back into the Compose Multiplatform navigation tree — effectively maintaining a single-app navigation model.

- The website dynamically updates the path and navigates to the desired destination, with the landing page as a fallback.

- Responsiveness is handled using **Material3 Window Size Classes**, mirroring Android’s approach by dividing screen sizes into:
  - **Compact** (phone)
  - **Medium** (tablet)
  - **Expanded** (desktop)

![Screencastfrom2025-05-1910-37-58-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/8a737f41-c6c9-48fc-967c-293da7518266)


## Building & Running

You can build and run the site using Gradle tasks:

```bash
# Development mode
./gradlew wasmJsBrowserDevelopmentRun

# Production mode
./gradlew wasmJsBrowserProductionRun
