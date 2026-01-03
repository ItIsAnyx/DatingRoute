<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>

</script>

<style>

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  line-height: 1.6;
  min-height: 100vh;
}

#app {
  width: 100%;
  min-height: 100vh;
}


html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: #191919; 
  color: white;
}

#app {
  height: 100%;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

*, *::before, *::after {
  box-sizing: border-box;
}
</style>