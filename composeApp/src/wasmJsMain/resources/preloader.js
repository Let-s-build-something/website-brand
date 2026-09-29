const API_HOST = "__HTTPS_HOST_NAME__";
const AD_TRACKING_BEARER_TOKEN = "__AD_TRACKING_BEARER_TOKEN__";
const APPLE_PROVIDER_TOKEN = "__APPLE_PROVIDER_TOKEN__";

function detectRedirectType() {
    const userAgent = navigator.userAgent.toLowerCase();

    const isIPadOS =
        navigator.platform === "MacIntel" &&
        navigator.maxTouchPoints > 1;

    if (userAgent.includes("android")) {
        return "googleplay";
    }

    if (
        userAgent.includes("iphone") ||
        userAgent.includes("ipad") ||
        userAgent.includes("ipod") ||
        isIPadOS
    ) {
        return "appstore";
    }

    return null;
}

function addGoogleReferral(url, clickId) {
    if (!clickId) {
        return url;
    }

    const storeUrl = new URL(url);

    const referrer = new URLSearchParams(
        storeUrl.searchParams.get("referrer") || ""
    );

    referrer.set("click_id", clickId);

    storeUrl.searchParams.set(
        "referrer",
        referrer.toString()
    );

    return storeUrl.toString();
}

function addAppleCampaign(url, campaign) {
    if (!APPLE_PROVIDER_TOKEN || !campaign) {
        return url;
    }

    const storeUrl = new URL(url);

    storeUrl.searchParams.set("pt", APPLE_PROVIDER_TOKEN);
    storeUrl.searchParams.set("ct", campaign);
    storeUrl.searchParams.set("mt", "8");

    return storeUrl.toString();
}

async function handleAdRedirect() {
    const params = new URLSearchParams(window.location.search);

    const requestedRedirect = params.get("redirect");

    const redirectType =
        requestedRedirect === "googleplay" ||
        requestedRedirect === "appstore"
            ? requestedRedirect
            : detectRedirectType();

    if (!redirectType) {
        window.history.replaceState(null, "", "/");
        return false;
    }

    const request = {
        source: params.get("source"),
        utm_source: params.get("utm_source"),
        utm_medium: params.get("utm_medium"),
        utm_campaign: params.get("utm_campaign"),
        utm_content: params.get("utm_content"),
        ad_id: params.get("ad_id"),
        redirect: redirectType
    };

    try {
        const response = await fetch(
            `https://${API_HOST}/api/v1/ad-tracking`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization":
                        `Bearer ${AD_TRACKING_BEARER_TOKEN}`
                },
                body: JSON.stringify(request)
            }
        );

        if (!response.ok) {
            throw new Error(
                `Ad tracking failed: ${response.status}`
            );
        }

        const tracking = await response.json();

        if (!tracking.redirect_url) {
            throw new Error("Missing redirect URL");
        }

        let redirectUrl = tracking.redirect_url;

        if (redirectType === "googleplay") {
            redirectUrl = addGoogleReferral(
                redirectUrl,
                tracking.click_id
            );
        }

        if (redirectType === "appstore") {
            redirectUrl = addAppleCampaign(
                redirectUrl,
                request.utm_campaign
            );
        }

        window.location.replace(redirectUrl);
        return true;

    } catch (error) {
        if (redirectType === "googleplay") {
            window.location.replace(
                "https://play.google.com/store/apps/details?id=augmy.interactive.com"
            );
            return true;
        }

        if (redirectType === "appstore") {
            window.location.replace(
                "https://apps.apple.com/us/app/augmy-nudge-vibes-connect/id6737480584"
            );
            return true;
        }

        return false;
    }
}

export async function loadIndex() {
    const loader = document.getElementById('loader-container');
    const app = document.getElementById('app');

    if (!loader || !app) {
        return;
    }

     const path = window.location.pathname.replace(/\/+$/, '') || '/';

     if (path === '/r') {
        const redirected = await handleAdRedirect();
        if (redirected) {
            return;
        }
     }


    const script = document.createElement('script');
    script.src = 'composeApp.js';
    script.type = 'application/javascript';

    script.onerror = function () {
        loader.innerHTML = '<p>Error loading the app. Safari browser is unfortunately not supported yet.</p>';
    };

    document.body.appendChild(script);

    setTimeout(() => {
        const app = document.getElementById('app');
        const loader = document.getElementById('loader-container');

        if (!loader) return;

        if (
            !app ||
            getComputedStyle(app).display === 'none' ||
            app.childElementCount === 0
        ) {
            loader.innerHTML =
                '<p>Failed to load the app. Please refresh the page.</p>';
        }
    }, 15000);
}