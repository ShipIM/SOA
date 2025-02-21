<template>
    <div>
      <h1>Unique Units of Measure</h1>
      <ErrorMessage :message="errorMessage" />
      <ul>
        <li v-for="unit in uniqueUnits" :key="unit">{{ unit }}</li>
      </ul>

    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import ErrorMessage from './ErrorMessage.vue';
  
  export default {
    components: {
      ErrorMessage
    },
    data() {
      return {
        uniqueUnits: [],
        errorMessage: ''
      };
    },
    methods: {
      fetchUniqueUnits() {
        axios.get('https://localhost:8090/api/v1/products/measurements/unique')
          .then(response => {
            this.uniqueUnits = response.data;
            this.errorMessage = '';
          })
          .catch(error => {
            if (error.response) {
              this.errorMessage = error.response.data.errors
                .map(e => `${e.field}: ${e.message}`).join(', ');
            } else {
              this.errorMessage = 'Server Error';
            }
          });
      }
    },
    created() {
      this.fetchUniqueUnits()
    }
  };
  </script>
  
  <style>
  li {
    margin: 5px 0;
  }
  </style>
  