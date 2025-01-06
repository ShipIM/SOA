<template>
    <div>
      <h1>Delete Products By Price</h1>
      <ErrorMessage :message="errorMessage" />
      <form @submit.prevent="deleteProductsByPrice">
        <input v-model="price" placeholder="Price" type="number" required />
        <button type="submit">Delete Products</button>
      </form>
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
        price: null,
        errorMessage: ''
      };
    },
    methods: {
      deleteProductsByPrice() {
        axios.delete(`https://localhost:8080/first-service/api/v1/products/price/${this.price}`)
          .then(() => {
            alert('Products deleted successfully!');
            this.price = null;
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
    }
  };
  </script>
  
  <style>
  form {
    display: flex;
    flex-direction: column;
  }
  input {
    margin-bottom: 10px;
  }
  </style>
  