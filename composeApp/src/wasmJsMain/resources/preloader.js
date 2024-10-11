// Import BrotliDecode function from decode.min.js
import { BrotliDecode } from './decode.min.js';

// Decompress function
async function decompressBrotli(response) {
    // Read the compressed data as an ArrayBuffer
    const compressedData = await response.arrayBuffer();

    // Decompress the data using BrotliDecode
    const decompressedData = BrotliDecode(new Uint8Array(compressedData));

    // Return the decompressed data as an ArrayBuffer
    return decompressedData.buffer;
}

async function loadWasmFile(wasmUrl) {
    try {
        const response = await fetch(wasmUrl);
        if (!response.ok) {
            throw new Error(`Failed to load ${wasmUrl}`);
        }

        // Use the decompressBrotli function to decompress the WASM file
        const decompressedWasm = await decompressBrotli(response);
        return decompressedWasm;
    } catch (error) {
        console.error("Error loading WASM file:", error);
        throw error;
    }
}

export async function loadComposeApp() {
    try {
        // Fetch and decompress both WASM files
        const decompressedWasm1 = await loadWasmFile('composeApp.wasm.br');
        const decompressedWasm2 = await loadWasmFile('2eaba8643e2ccdf352b4.wasm.br');

        // Create Blob URLs for the decompressed WASM files
        const blob1 = new Blob([decompressedWasm1], { type: 'application/octet-stream' });
        const blob2 = new Blob([decompressedWasm2], { type: 'application/octet-stream' });

        // Create downloadable links for the blobs
        const url1 = URL.createObjectURL(blob1);
        const url2 = URL.createObjectURL(blob2);

        // Create links to download the files
        const link1 = document.createElement('wasm');
        link1.href = url1;
        link1.download = 'composeApp.wasm'; // Name of the downloaded file

        const link2 = document.createElement('wasm2');
        link2.href = url2;
        link2.download = '2eaba8643e2ccdf352b4.wasm'; // Name of the downloaded file

        // Append the links to the document (optional, depending on your use case)
        document.body.appendChild(link1);
        document.body.appendChild(link2);

        console.log("WASM files downloaded successfully!");
    } catch (error) {
        console.error("Error loading the app:", error);
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