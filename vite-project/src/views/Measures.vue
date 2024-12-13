<template>
  <div>
    <h1>Units of Measure</h1>
    <ErrorMessage :message="errorMessage" />
    
    <ul v-if="units.length">
      <li v-for="unit in units" :key="unit">{{ unit }}</li>
    </ul>
    <p v-else>No units found.</p>
    
    <br />
    
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
  </div>
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
      units: [],
      errorMessage: ''
    };
  },
  methods: {
    fetchMeasures() {
      axios.get('https://localhost:8080/first-service/api/v1/products/measure')
        .then(response => {
          console.log(response);
          this.units = response.data;
          this.errorMessage = '';
        })
        .catch(error => {
          if (error.response) {
            this.errorMessage = error.response.data.errors
              .map(e => `${e.field}: ${e.message}`).join(', ');
          } else {
            this.errorMessage = 'Server Error';
          }
          console.error('Fetch error:', error);
        });
    }
  },
  created() {
    this.fetchMeasures();
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
