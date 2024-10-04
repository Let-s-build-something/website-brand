const path = require('path');  // Ensure path module is imported

;(function(config) {
    config.mode = 'development';

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
    };
})(config);
