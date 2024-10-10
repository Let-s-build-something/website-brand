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
        static: [
            // Serve the directory containing your index.html
            path.resolve(__dirname, 'C:\\Users\\jacob\\AndroidStudioProjects\\website-brand\\composeApp\\src\\wasmJsMain\\resources'),
            // Add the directory where your .wasm and .wasm.br files are located
            path.resolve(__dirname, 'C:\\Users\\jacob\\AndroidStudioProjects\\website-brand\\build\\js\\packages\\composeApp\\kotlin')
        ],
        historyApiFallback: {
            index: '/index.html',  // Serve index.html for all undefined routes
            rewrites: [
                // Ensure requests for .wasm files do not fallback to index.html
                { from: /^\/skiko\.wasm$/, to: '/skiko.wasm' },
                { from: /^\/composeApp\.wasm\.br$/, to: '/composeApp.wasm.br' } // Ensure the .wasm.br is served
            ]
        },
        // CORS Configuration
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
            'Access-Control-Allow-Headers': 'Content-Type, Authorization',
        },
        // Enable the option to handle CORS preflight requests
        allowedHosts: 'all',
        // Set the appropriate Content-Encoding and Content-Type for Brotli-compressed WASM
        onBeforeSetupMiddleware(devServer) {
            devServer.app.get('*.wasm.br', (req, res, next) => {
                res.setHeader('Content-Encoding', 'br');
                res.setHeader('Content-Type', 'application/wasm');
                next();
            });
        },
    };
})(config);