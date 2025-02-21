<template>
    <div>
      <h1>Product Detail</h1>
      <ErrorMessage :message="errorMessage" />
      <div v-if="product">
        <p><strong>ID:</strong> {{ product.id }}</p>
        <p><strong>Name:</strong> {{ product.name }}</p>
        <p><strong>Price:</strong> {{ product.price }}</p>
        <p><strong>Creation Date:</strong> {{ new Date(product.creation_date).toLocaleString() }}</p>
        <p><strong>Coordinates:</strong> X: {{ product.coordinates.x }}, Y: {{ product.coordinates.y }}</p>
        <p><strong>Unit of Measure:</strong> {{ product.unit_of_measure || 'N/A' }}</p>
        
        <h2>Owner Details</h2>
        <p><strong>Name:</strong> {{ product.owner.name }}</p>
        <p><strong>Height:</strong> {{ product.owner.height }}</p>
        <p><strong>Birthday:</strong> {{ product.owner.birthday ? new Date(product.owner.birthday).toLocaleDateString() : 'N/A' }}</p>
        <p><strong>Eye Color:</strong> {{ product.owner.eye_color }}</p>
        <p><strong>Nationality:</strong> {{ product.owner.nationality }}</p>
        
        <div>
          <router-link :to="{ name: 'ProductUpdate', params: { id: product.id } }">
            <button>Edit Product</button>
          </router-link>
          <button @click="deleteProduct(product.id)">Delete Product</button>
        </div>
      </div>
      <button @click="goBack">Back</button>
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
        const id = this.$route.params.id;
        axios.get(`https://localhost:8090/api/v1/products/${id}`)
          .then(response => {
            this.product = response.data;
            this.errorMessage = '';
          })
          .catch(error => {
            if (error.response) {
              this.errorMessage = error.response.data.errors
                  .map(e => e.field ? `${e.field}: ${e.message}` : e.message).join(', ');
            } else {
              this.errorMessage = 'Server Error';
            }
          });
      },
      deleteProduct(id) {
        if (confirm('Are you sure you want to delete this product?')) {
          axios.delete(`https://localhost:8090/api/v1/products/${id}`)
            .then(() => {
              this.$router.push({ name: 'Products' });
            })
            .catch(error => {
              if (error.response) {
                this.errorMessage = error.response.data.errors
                    .map(e => e.field ? `${e.field}: ${e.message}` : e.message).join(', ');
              } else {
                this.errorMessage = 'Server Error';
              }
            });
        }
      },
      goBack() {
        this.$router.push({ name: 'Products' });
      }
    },
    created() {
      this.fetchProduct();
    }
  };
  </script>
  
  <style>
  p {
    margin: 5px 0;
  }
  </style>
  