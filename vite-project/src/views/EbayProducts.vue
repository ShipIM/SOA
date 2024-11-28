<template>
  <div>
    <h1>Ebay Product Management</h1>
    <ErrorMessage :message="errorMessage" />

    <div>
      <h2>Filter Products by Price</h2>
      <input v-model.number="priceFrom" placeholder="Price From" type="number" />
      <input v-model.number="priceTo" placeholder="Price To" type="number" />
      <button @click="filterByPrice">Filter</button>
    </div>

    <div>
      <h2>Increase Prices</h2>
      <input v-model.number="increasePercent" placeholder="Increase Percent" type="number" />
      <button @click="increasePrices">Increase Prices</button>
    </div>

    <h2>Filtered Products:</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Price</th>
          <th>Creation Date</th>
          <th>Coordinates</th>
          <th>Unit of Measure</th>
          <th>Owner</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price }}</td>
          <td>{{ new Date(product.creation_date).toLocaleDateString() }}</td>
          <td>X: {{ product.coordinates.x }}, Y: {{ product.coordinates.y }}</td>
          <td>{{ product.unit_of_measure || 'N/A' }}</td>
          <td>{{ product.owner ? product.owner.name : 'N/A' }}</td>
          <td>
            <button @click="viewProduct(product.id)">View</button>
            <button @click="deleteProduct(product.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div>
      <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)">Previous</button>
      <span>Page {{ currentPage }} of {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">Next</button>
    </div>
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
      products: [],
      priceFrom: null,
      priceTo: null,
      increasePercent: null,
      errorMessage: '',
      currentPage: 1,
      pageSize: 10,
      totalPages: 1
    };
  },
  methods: {
    filterByPrice() {
      axios.get(`http://localhost:8080/second-service/api/v1/ebay/filter/price/${this.priceFrom}/${this.priceTo}?page=${this.currentPage}&size=${this.pageSize}`)
        .then(response => {
          this.products = response.data;
          this.currentPage = response.data.currentPage;
          this.totalPages = response.data.totalPages;
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
    },
    increasePrices() {
      axios.post(`http://localhost:8080/second-service/api/v1/ebay/price/increase/${this.increasePercent}`)
        .then(() => {
          alert('Prices increased successfully!');
          this.filterByPrice(this.currentPage);
        })
        .catch(error => {
          if (error.response) {
            this.errorMessage = error.response.data.errors
              .map(e => `${e.field}: ${e.message}`).join(', ');
          } else {
            this.errorMessage = 'Server Error';
          }
        });
    },
    changePage(page) {
      this.currentPage = page;
      this.filterByPrice(page);
    },
    viewProduct(productId) {
    },
    deleteProduct(productId) {
    }
  },
};
</script>

<style>
</style>
