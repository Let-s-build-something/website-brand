export async function loadIndex() {
    //await loadComposeApp()

    const script = document.createElement('script');
    script.src = 'composeApp.js';  // Ensure this path is correct
    script.type = 'application/javascript';

    script.onerror = function() {
        document.getElementById('loader-container').innerHTML = '<p>Error loading the app. Safari browser is unfortunately not support yet.</p>';
    };

    document.body.appendChild(script);
}

// Fallback message if loading takes too long
setTimeout(() => {
    if (document.getElementById('app').style.display === 'none') {
        document.getElementById('loader-container').innerHTML = '<p>Failed to load the app. Please refresh the page.</p>';
    }
}, 15000);
