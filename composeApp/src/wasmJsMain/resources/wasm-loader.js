// wasm-loader.js
function onWasmAppReady() {
    const splash = document.getElementById('splash');
    const app = document.getElementById('app');
    splash.style.display = 'none'; // Hide splash screen
    app.style.display = 'block'; // Show app
    console.log("WASM app is ready!");
}

function waitForWasmAppReady() {
    if (typeof window['kotlinApp'] !== 'undefined') {
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

export function loadComposeApp() {
    const script = document.createElement('script');
    script.src = 'composeApp.js';  // Ensure this path is correct
    script.type = 'application/javascript';
    script.onload = function() {
        waitForWasmAppReady();
    };
    script.onerror = function() {
        document.getElementById('splash').innerHTML = '<p>Error loading the app. Please try again.</p>';
    };
    document.body.appendChild(script);
}
