<template>
    <div>
      <h1>Product with Minimum Creation Date</h1>
      <ErrorMessage :message="errorMessage" />
      <div v-if="product">
        <h2>Product Details</h2>
        <p><strong>ID:</strong> {{ product.id }}</p>
        <p><strong>Name:</strong> {{ product.name }}</p>
        <p><strong>Price:</strong> {{ product.price }}</p>
        <p><strong>Creation Date:</strong> {{ new Date(product.creation_date).toLocaleDateString() }}</p>
        <p><strong>Coordinates:</strong> X: {{ product.coordinates.x }}, Y: {{ product.coordinates.y }}</p>
        <p><strong>Unit of Measure:</strong> {{ product.unit_of_measure || 'N/A' }}</p>
        <p><strong>Owner:</strong> {{ product.owner.name }}</p>
      </div>
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
        product: null,
        errorMessage: ''
      };
    },
    methods: {
      fetchProduct() {
        axios.get(`http://localhost:8080/first-service/api/v1/products/dates/min`)
          .then(response => {
            this.product = response.data;
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
      this.fetchProduct()
    }
  };
  </script>
  
  <style>
  p {
    margin: 5px 0;
  }
  </style>
  