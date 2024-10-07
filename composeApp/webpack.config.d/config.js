const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');

;(function(config) {
    config.mode = 'production';  // Set production mode for optimization

    // Webpack production optimizations
    config.optimization = {
        minimize: true,  // Enable code minification
        minimizer: [
            new TerserPlugin({
                terserOptions: {
                    mangle: true,  // Shorten variable and function names
                    compress: true,  // Compress the code
                    format: {
                        comments: false,  // Remove all comments
                    },
                },
                extractComments: false,  // Avoid creating separate comment files
            }),
        ],
        // Enable tree-shaking (removes unused code)
        usedExports: true,
        sideEffects: false,
    };

    // Modify devServer settings for Single Page Application
    config.devServer = {
        ...config.devServer,  // Keep existing config options (if any)
        port: 9000,  // Set the port (or change this to whatever port you prefer)
        static: [
            // Serve the directory containing your index.html
            path.resolve(__dirname, 'C:\\Users\\jacob\\AndroidStudioProjects\\website-brand\\composeApp\\src\\wasmJsMain\\resources'),
            // Add the directory where your .wasm file is located
            path.resolve(__dirname, 'C:\\Users\\jacob\\AndroidStudioProjects\\website-brand\\build\\js\\packages\\composeApp\\kotlin')
        ],
        historyApiFallback: {
            index: '/index.html',  // Serve index.html for all undefined routes
            rewrites: [
                // Ensure requests for .wasm files do not fallback to index.html
                { from: /^\/skiko\.wasm$/, to: '/skiko.wasm' } // Adjust the name if necessary
            ]
        },
        // CORS Configuration
        headers: {
            'Access-Control-Allow-Origin': '*',  // Allow all origins; adjust if needed for security
            'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',  // Allowed HTTP methods
            'Access-Control-Allow-Headers': 'Content-Type, Authorization',  // Allowed headers
        },
        // Enable the option to handle CORS preflight requests
        allowedHosts: 'all',  // Allow all hosts (useful for development)
    };
})(config);
