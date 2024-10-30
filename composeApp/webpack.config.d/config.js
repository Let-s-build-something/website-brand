const path = require('path');
const TerserPlugin = require("terser-webpack-plugin");

config.optimization = config.optimization || {};
config.optimization.minimize = true;
config.optimization.minimizer = [
    new TerserPlugin({
        terserOptions: {
            mangle: true,
            compress: true,
            output: {
                beautify: false,
            },
        },
    }),
];

;(function(config) {
    // Modify devServer settings for Single Page Application
    config.devServer = {
        ...config.devServer,
        port: 9000,
        historyApiFallback: {
            index: '/index.html',
            rewrites: [
                { from: /^\/skiko\.wasm$/, to: '/skiko.wasm' },
                { from: /^\/composeApp\.wasm\.br$/, to: '/composeApp.wasm.br' }
            ]
        },
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
            'Access-Control-Allow-Headers': 'Content-Type, Authorization',
        },
        allowedHosts: 'all',
        onBeforeSetupMiddleware(devServer) {
            devServer.app.get('*.wasm.br', (req, res, next) => {
                res.setHeader('Content-Encoding', 'br');
                res.setHeader('Content-Type', 'application/wasm');
                next();
            });
            devServer.app.get('*.wasm', (req, res, next) => {
                res.setHeader('Content-Type', 'application/wasm');
                next();
            });
        },
        proxy: {
            '/downloads': {
                target: 'https://augmy.org',
                changeOrigin: true,
                secure: false,  // If you're targeting an HTTPS URL and don't want to verify the SSL certificate.
                logLevel: 'debug',  // This will log the proxy requests to the terminal for debugging.
            }
        }
    };
})(config);
