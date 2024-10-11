async function decompressBrotli(response) {
    const compressedData = await response.arrayBuffer();

    // Use fflate's Brotli decompression function (fflate automatically detects Brotli format)
    const decompressedData = fflate.decompress(new Uint8Array(compressedData));

    return decompressedData.buffer; // Return the ArrayBuffer for WebAssembly instantiation
}


async function loadWasmFile(wasmUrl) {
    try {
        const response = await fetch(wasmUrl);
        if (!response.ok) {
            throw new Error(`Failed to load ${wasmUrl}`);
        }
        
        const decompressedWasm = await decompressBrotli(response);
        return WebAssembly.instantiate(decompressedWasm);
    } catch (error) {
        console.error("Error loading WASM file:", error);
        throw error;
    }
}

export async function loadComposeApp() {
    try {
        // Fetch and decompress both WASM files
        const wasm1 = loadWasmFile('composeApp.wasm.br');
        const wasm2 = loadWasmFile('2eaba8643e2ccdf352b4.wasm.br');
        
        // Wait for both WASM files to be ready
        const [result1, result2] = await Promise.all([wasm1, wasm2]);

        // Store the results (exported instances) in window for later use
        window.composeAppWasm = result1.instance;
        window.secondWasmModule = result2.instance;

        // Invoke the app readiness check
        waitForWasmAppReady();
    } catch (error) {
        document.getElementById('splash').innerHTML = '<p>Error loading the app. Please try again.</p>';
    }
}

// WASM app ready check
function onWasmAppReady() {
    const splash = document.getElementById('splash');
    const app = document.getElementById('app');
    splash.style.display = 'none'; // Hide splash screen
    app.style.display = 'block'; // Show app
    console.log("WASM app is ready!");
}

function waitForWasmAppReady() {
    if (typeof window['composeAppWasm'] !== 'undefined') {
        onWasmAppReady();
    } else {
        setTimeout(waitForWasmAppReady, 100);
    }
}

// Fallback message if loading takes too long
setTimeout(() => {
    if (document.getElementById('app').style.display === 'none') {
        document.getElementById('splash').innerHTML = '<p>Failed to load the app. Please refresh the page.</p>';
    }
}, 15000);
