// wasm-loader.js
export function onWasmAppReady() {
    const splash = document.getElementById('splash');
    const app = document.getElementById('app');
    splash.style.display = 'none'; // Hide splash screen
    app.style.display = 'block'; // Show app
    console.log("WASM app is ready!");
}

export function waitForWasmAppReady() {
    if (typeof window['kotlinApp'] !== 'undefined') {
        onWasmAppReady();
    } else {
        setTimeout(waitForWasmAppReady, 100);
    }
}

export async function loadCompressedWasm() {
    const brotli = await import("https://unpkg.com/brotli-wasm@3.0.1/index.web.js?module").then(m => m.default);

    // Load and decompress the Brotli-compressed WebAssembly file
    return fetch('/composeApp.wasm.br')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch .wasm.br file: ${response.statusText}`);
            }
            return response.arrayBuffer();
        })
        .then(compressedData => {
            // Decompress the data
            const decompressedData = brotli.decompress(new Uint8Array(compressedData));

            const blob = new Blob([decompressedData], { type: 'application/wasm' });
            const url = URL.createObjectURL(blob);

            // Create a new script element for the WASM module
            const script = document.createElement('script');
            script.src = url; // Use the Blob URL for the WASM
            script.type = 'application/javascript';

            document.body.appendChild(script);

            script.onload = () => {
                URL.revokeObjectURL(url);
                console.log("WASM module loaded successfully!");
                waitForWasmAppReady(); // Check if the app is ready after loading
            };

            script.onerror = () => {
                document.getElementById('splash').innerHTML = '<p>Failed to load the WASM module.</p>';
                console.error("Failed to load the WASM module.");
            };
        })
        .catch(error => {
            console.error("Error loading WASM:", error);
            document.getElementById('splash').innerHTML = '<p>Error loading the app. Please try again.</p>';
        });
}

export function loadComposeApp() {
    const script = document.createElement('script');
    script.src = 'composeApp.js';  // Ensure this path is correct
    script.type = 'application/javascript';

    script.onload = function() {
        loadCompressedWasm(); // Load the compressed WASM after the main app script is ready
    };

    script.onerror = function() {
        document.getElementById('splash').innerHTML = '<p>Error loading the app. Please try again.</p>';
    };

    document.body.appendChild(script);
}
