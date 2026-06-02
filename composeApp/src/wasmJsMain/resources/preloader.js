export async function loadIndex() {
    const loader = document.getElementById('loader-container');
    const app = document.getElementById('app');

    if (!loader || !app) {
        return;
    }

    const script = document.createElement('script');
    script.src = 'composeApp.js';
    script.type = 'application/javascript';

    script.onerror = function () {
        loader.innerHTML = '<p>Error loading the app. Safari browser is unfortunately not supported yet.</p>';
    };

    document.body.appendChild(script);
}

setTimeout(() => {
    const app = document.getElementById('app');
    const loader = document.getElementById('loader-container');

    if (!loader) return;

    if (!app || getComputedStyle(app).display === 'none' || app.childElementCount === 0) {
        loader.innerHTML = '<p>Failed to load the app. Please refresh the page.</p>';
    }
}, 15000);