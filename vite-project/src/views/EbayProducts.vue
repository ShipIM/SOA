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

    <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div> <!-- Success message display -->

    <h2>Filtered Products:</h2>
    <label>Page:
        <input type="number" v-model="page" @change="filterByPrice" min="1" />
      </label>
      <label>Size:
        <input type="number" v-model="pageSize" @change="filterByPrice" min="1" />
      </label>
    <table>
      <thead>
        <tr>
          <th rowspan="2">ID</th>
          <th rowspan="2">Name</th>
          <th rowspan="2">Price</th>
          <th rowspan="2">Creation Date</th>
          <th rowspan="2">Unit of Measure</th>
          <th colspan="2">Coordinates</th>
          <th colspan="5">Owner</th>
          <th rowspan="2">Actions</th>
        </tr>
        <tr>
          <th>X</th>
          <th>Y</th>
          <th>Name</th>
          <th>Height</th>
          <th>Birthday</th>
          <th>Eye Color</th>
          <th>Nationality</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price }}</td>
          <td>{{ new Date(product.creation_date).toLocaleDateString() }}</td>
          <td>{{ product.unit_of_measure }}</td>
          <td>{{ product.coordinates.x }}</td>
          <td>{{ product.coordinates.y }}</td>
          <td>{{ product.owner ? product.owner.name : 'N/A' }}</td>
          <td>{{ product.owner ? product.owner.height : 'N/A' }}</td>
          <td>{{ product.owner.birthday ? new Date(product.owner.birthday).toLocaleDateString() : 'N/A' }}</td>
          <td>{{ product.owner ? product.owner.eye_color : 'N/A' }}</td>
          <td>{{ product.owner ? product.owner.nationality : 'N/A' }}</td>
          <td>
            <button @click="viewProduct(product.id)">View</button>
            <button @click="deleteProduct(product.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div>
      <button @click="prevPage" v-if="page > 1">Previous</button>
      <span>Page {{ page }} of {{ totalPages }}</span>
      <button @click="nextPage" v-if="page < totalPages">Next</button>
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
      successMessage: '', // Add this line for success messages
      page: 1,
      pageSize: 10,
      totalPages: 1
    };
  },
  methods: {
    filterByPrice() {
      axios.get(`http://localhost:8080/second-service/api/v1/ebay/filter/price/${this.priceFrom}/${this.priceTo}?page=${this.page}&size=${this.pageSize}`)
        .then(response => {
          this.products = response.data.data;
          this.page = response.data.meta.current_page;
          this.totalPages = response.data.meta.total_pages;
          this.errorMessage = '';
          this.successMessage = ''; // Clear any previous success message
        })
        .catch(error => {
          this.errorMessage = '';
          if (error.response) {
            this.errorMessage = error.response.data.errors
              .map(e => `${e.field}: ${e.message}`).join(', ');
          }
        });
    },
    nextPage() {
      if (this.page < this.totalPages) {
        this.page++;
        this.filterByPrice();
      }
    },
    prevPage() {
      if (this.page > 1) {
        this.page--;
        this.filterByPrice();
      }
    },
    increasePrices() {
      axios.post(`http://localhost:8080/second-service/api/v1/ebay/price/increase/${this.increasePercent}`)
        .then(() => {
          this.successMessage = 'Prices increased successfully!'; // Set success message
          setTimeout(() => {
            this.successMessage = ''; // Clear message after a few seconds
          }, 3000);
          this.filterByPrice(); // Refresh the product list after increasing prices
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
    viewProduct(productId) {
      this.$router.push({ path: `/product/${productId}` }); // Navigate to product details page
    },
    deleteProduct(productId) {
      if (confirm('Are you sure you want to delete this product?')) {
        axios.delete(`http://localhost:8080/first-service/api/v1/products/${productId}`)
          .then(() => {
            this.successMessage = 'Product deleted successfully!';
            setTimeout(() => {
              this.successMessage = ''; // Clear message after a short period
            }, 3000);
            this.filterByPrice(); // Refresh the product list
          })
          .catch(error => {
            this.errorMessage = error.response ?
              error.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ') : 'Server Error';
          });
      }
    }
  }
};
</script>

<style>
.alert {
  color: green; /* Customize your success message color */
  margin-bottom: 10px;
}
</style>
