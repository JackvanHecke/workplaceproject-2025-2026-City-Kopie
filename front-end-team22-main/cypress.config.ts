// cypress.config.ts
import { defineConfig } from "cypress";
import createBundler from "@bahmutov/cypress-esbuild-preprocessor";
import { addCucumberPreprocessorPlugin } from "@badeball/cypress-cucumber-preprocessor";
// @ts-ignore: ESM subpath entrypoint - allow TS to compile until tsconfig is updated
import { createEsbuildPlugin } from "@badeball/cypress-cucumber-preprocessor/esbuild";

import dotenv from "dotenv";
// @ts-ignore: node-fetch may be ESM; ignore TS errors here if types are not installed yet
import fetch from "node-fetch";
import path from "path";

// Load the correct .env
const envFile =
  process.env.NODE_ENV === "production" ? ".env.production" : ".env.development";
dotenv.config({ path: path.resolve(__dirname, envFile) });

const baseUrl = process.env.CYPRESS_BASE_URL || "http://localhost:8080";

export default defineConfig({
  e2e: {
    baseUrl,
    // you used these patterns previously — keep them
    // specPattern: ['cypress/e2e/features/test.feature', 'cypress/e2e/features/lecturers.overview.feature'],
    specPattern: ["cypress/e2e/features/**/*.feature",
       "cypress/e2e/tests/**/*.cy.{js,jsx,ts,tsx}"
    ],
    supportFile: "cypress/support/e2e.ts",

    // reporter removed as requested (keep the rest of the file intact for others)
    // reporter: "mochawesome",
    // reporterOptions: { ... } // intentionally removed

    async setupNodeEvents(on, config) {
      // 1. Cucumber plugin (compiles .feature → JS)
      await addCucumberPreprocessorPlugin(on, config);

      // 2. Wire up esbuild so that Cucumber gets the .feature files
      on(
        "file:preprocessor",
        createBundler({
          plugins: [createEsbuildPlugin(config)],
        })
      );

      // 3. Your reset‐database task (uses NEXT_PUBLIC_API_URL and optional API_TEST_TOKEN)
      on("task", {
        // inside the existing on("task", { ... }) block in cypress.config.ts
// (keep resetDatabase as-is; add the following performLogin task)

    async performLogin({ email, password }: { email: string; password: string }) {
      // this code runs in Node (plugin) context
      if (!process.env.NEXT_PUBLIC_API_URL) {
        throw new Error("NEXT_PUBLIC_API_URL not set in environment");
      }
      const apiBase = process.env.NEXT_PUBLIC_API_URL.replace(/\/$/, "");

      // 1) request the salt
      const saltUrl = `${apiBase}/profile/salt?${new URLSearchParams({ email })}`;
      console.log(`🛠 [plugin] performLogin → getting salt from ${saltUrl}`);
      const saltResp = await fetch(saltUrl, { method: "GET", headers: { "Content-Type": "application/json" } });
      const saltText = await saltResp.text();
      if (!saltResp.ok) {
        throw new Error(`Failed to get salt: ${saltResp.status} ${saltResp.statusText} - ${saltText}`);
      }
      if (saltText === "Unknown Profile!") {
        throw new Error("Unknown Profile!");
      }

      // 2) hash the password using bcryptjs (synchronous is ok here)
      // eslint-disable-next-line @typescript-eslint/no-var-requires
      const bcrypt = require("bcryptjs");
      const hashedPassword = bcrypt.hashSync(password, saltText);

      // 3) call login endpoint
      const loginUrl = `${apiBase}/profile/login`;
      console.log(`🛠 [plugin] performLogin → POST ${loginUrl}`);
      const loginResp = await fetch(loginUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, hashedPassword }),
      });
      const loginText = await loginResp.text();
      if (!loginResp.ok) {
        throw new Error(`Login failed: ${loginResp.status} ${loginResp.statusText} - ${loginText}`);
      }
      // loginText is the token your app sets into cookie in the browser code
      console.log("🛠 [plugin] performLogin ← got token (length):", loginText ? loginText.length : 0);
      return loginText; // resolves to token string for the test to set cookie with
    }

      });

      return config;
    },
  },
});
