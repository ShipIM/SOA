<template>
    <UniqueUnits />
    
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
  import UniqueUnits from '../components/UniqueUnits.vue'
  
  export default {
    components: {
        UniqueUnits
    }
  }
  </script>
  