import type { NextConfig } from "next";
import path from "node:path";

const nextConfig: NextConfig = {
  outputFileTracingRoot: path.join(__dirname, "../.."),
  webpack: (config) => {
    config.resolve.alias = {
      ...config.resolve.alias,
      "@aulms/api-client": path.join(__dirname, "../../generated/frontend/index.ts"),
      axios: path.join(__dirname, "node_modules/axios"),
    };
    return config;
  },
};

export default nextConfig;
