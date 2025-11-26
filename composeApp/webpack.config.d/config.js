const path = require("path");
const TerserPlugin = require("terser-webpack-plugin");

config.optimization = config.optimization || {};
config.optimization.minimize = true;
config.optimization.minimizer = [
    new TerserPlugin({
        terserOptions: {
            mangle: true,
            compress: true,
            output: { beautify: false },
        },
    }),
];

;(function (config) {
    config.devServer = {
        ...config.devServer,

        port: 9000,
        allowedHosts: "all",

        headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
            "Access-Control-Allow-Headers": "Content-Type, Authorization",
        },

        historyApiFallback: {
            index: "/index.html",
            rewrites: [
                { from: /^\/skiko\.wasm$/, to: "/skiko.wasm" },
                { from: /^\/composeApp\.wasm\.br$/, to: "/composeApp.wasm.br" },
            ],
        },

        // ⛔ Webpack 4: onBeforeSetupMiddleware
        // ✔ Webpack 5: setupMiddlewares
        setupMiddlewares: (middlewares, devServer) => {
            if (!devServer) {
                throw new Error("webpack-dev-server is not defined");
            }

            devServer.app.get("*.wasm.br", (req, res, next) => {
                res.setHeader("Content-Encoding", "br");
                res.setHeader("Content-Type", "application/wasm");
                next();
            });

            devServer.app.get("*.wasm", (req, res, next) => {
                res.setHeader("Content-Type", "application/wasm");
                next();
            });

            return middlewares;
        },

        // Webpack 5 requires proxy to be an array, not an object
        proxy: [
            {
                context: ["/downloads"],
                target: "https://augmy.org",
                changeOrigin: true,
                secure: false,
                logLevel: "debug",
            },
        ],
    };
})(config);
