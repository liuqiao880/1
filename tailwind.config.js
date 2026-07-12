/** @type {import('tailwindcss').Config} */

export default {
  darkMode: "class",
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    container: {
      center: true,
    },
    extend: {
      colors: {
        newspaper: {
          red: '#C41E3A',
          'red-dark': '#8B0000',
          'red-light': '#E63946',
        },
        ink: {
          black: '#1A1A1A',
          gray: '#4A4A4A',
          light: '#8A8A8A',
        },
        paper: {
          white: '#FAFAFA',
          cream: '#FDFBF7',
        },
        line: {
          separator: '#E0E0E0',
          thin: '#F0F0F0',
        },
        priority: {
          high: '#C41E3A',
          medium: '#B8860B',
          low: '#4682B4',
        },
      },
      fontFamily: {
        serif: ['Georgia', 'Noto Serif CJK SC', 'Source Han Serif SC', 'STSong', 'SimSun', 'serif'],
        sans: ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Noto Sans CJK SC', 'Helvetica', 'Arial', 'sans-serif'],
      },
    },
  },
  plugins: [],
};
