<template>
    <div v-if="message" class="error-message">
      <p>{{ message }}</p>
    </div>
  </template>
  
  <script>
  export default {
    props: ['message']
  }
  </script>
  
  <style>
  .error-message {
    color: red;
    border: 1px solid red;
    padding: 10px;
    margin: 10px 0;
  }
  </style>
  