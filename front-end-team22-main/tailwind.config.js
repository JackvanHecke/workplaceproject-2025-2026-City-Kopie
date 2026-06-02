/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}", // Note the addition of the `app` directory.
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",

    // Or if using `src` directory:
    "./src/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        "client-bg": "#ebf7fa",
        "client-primary": "#0f8d97",
        "darker-client-bg": "#098995",
        "darker-client-primary": "#dfeaed",
      },
      spacing: {
        "30pixspacing": "30px",
      },
    },
  },
  plugins: [],
};
