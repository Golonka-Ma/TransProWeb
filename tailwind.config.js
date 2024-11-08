/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode:'class',
  content: [
    './src/main/resources/templates/**/*.html',
    './src/main/resources/static/js/**/*.js',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Poppins', 'ui-sans-serif', 'system-ui'],
      },
      colors: {
        primary: '#1F3E2D',
        secondary: '#1D4B3D',
        darkPrimary: '#0F1E16',
        darkSecondary: '#0E261D',
        gold: '#6b5c0f',
        field_gray: '#C3C3C3',

      },
    },
  },
  plugins: [
    require('tailwindcss-filters'),
  ],
};
