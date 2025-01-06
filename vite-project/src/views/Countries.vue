<template>
    <div>
      <h1>Countries</h1>
      <ErrorMessage :message="errorMessage" />
      <ul>
        <li v-for="country in countries" :key="country">{{ country }}</li>
      </ul>
    </div>

    <router-link to="/">Products</router-link>
    <br />
    <router-link to="/management">Add Product</router-link>
    <br />
    <router-link to="/measures">Units of Measure</router-link>
    <br />
    <router-link to="/colors">Colors</router-link>
    <br />
    <router-link to="/countries">Countries</router-link>
    <br />
    <router-link to="/delete-by-price">Delete products by price</router-link>
    <br />
    <router-link to="/min-creation-date">Product with min creation date</router-link>
    <br />
    <router-link to="/unique-units">Unique units</router-link>
    <br />
    <router-link to="/ebay">Ebay products</router-link>
    <br />
  </template>
  
  <script>
  import axios from 'axios';
  import ErrorMessage from '../components/ErrorMessage.vue';
  
  export default {
    components: {
      ErrorMessage
    },
    data() {
      return {
        countries: [],
        errorMessage: ''
      };
    },
    methods: {
      fetchCountries() {
        axios.get('https://localhost:8080/first-service/api/v1/products/country')
          .then(response => {
            this.countries = response.data;
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
      this.fetchCountries();
    }
  };
  </script>
  
  <style>
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    margin: 5px 0;
  }
  </style>
  