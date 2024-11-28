<template>
  <div>
    <h1>Product List</h1>
    <div>
      <label>Filter:
        <input v-model="filter" @input="fetchProducts" placeholder="field=value" />
      </label>
      <label>Page:
        <input type="number" v-model="page" @change="fetchProducts" min="1" />
      </label>
      <label>Size:
        <input type="number" v-model="size" @change="fetchProducts" min="1" />
      </label>
    </div>
    <ErrorMessage :message="errorMessage" />
    <table>
      <thead>
        <tr>
          <th @click="setSort('id')">ID <span v-if="isSorted('id')">{{ sortDirection('id') }}</span></th>
          <th @click="setSort('name')">Name <span v-if="isSorted('name')">{{ sortDirection('name') }}</span></th>
          <th @click="setSort('price')">Price <span v-if="isSorted('price')">{{ sortDirection('price') }}</span></th>
          <th @click="setSort('creation_date')">Creation Date <span v-if="isSorted('creation_date')">{{ sortDirection('creation_date') }}</span></th>
          <th>Coordinates</th>
          <th @click="setSort('unit_of_measure')">Unit of Measure <span v-if="isSorted('unit_of_measure')">{{ sortDirection('unit_of_measure') }}</span></th>
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
      <button @click="prevPage" v-if="page > 1">Previous</button>
      <span>Page {{ page }} of {{ totalPages }}</span>
      <button @click="nextPage" v-if="page < totalPages">Next</button>
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
      products: [],
      errorMessage: '',
      page: 1,
      size: 10,
      totalPages: 1,
      selectedSort: [], // Keep sort criteria as an array
      filter: ''
    };
  },
  methods: {
    fetchProducts() {
      const params = {
        page: this.page,
        size: this.size,
      };

      const sort =  this.selectedSort.length ? 
              this.selectedSort.map(s => `${s.field}:${s.direction}`).join("&sort=") : 
              null

      var endpoint = ""
      if (sort == null) {
        endpoint = `http://localhost:8080/first-service/api/v1/products/`
      } else {
        endpoint = `http://localhost:8080/first-service/api/v1/products/?sort=${sort}`
      }
      axios.get(endpoint, { params })
        .then(response => {
          this.products = response.data.data;
          this.totalPages = response.data.meta.total_pages;
          this.errorMessage = '';
        })
        .catch(error => {
          this.errorMessage = error.response ? error.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ') : 'Server Error';
        });
    },
    viewProduct(id) {
      this.$router.push({ path: `/product/${id}` });
    },
    deleteProduct(id) {
      axios.delete(`http://localhost:8080/first-service/api/v1/products/${id}`)
        .then(() => {
          this.fetchProducts();
        })
        .catch(error => {
          this.errorMessage = error.response ? error.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ') : 'Server Error';
        });
    },
    nextPage() {
      if (this.page < this.totalPages) {
        this.page++;
        this.fetchProducts();
      }
    },
    prevPage() {
      if (this.page > 1) {
        this.page--;
        this.fetchProducts();
      }
    },
    setSort(field) {
      const existingSortIndex = this.selectedSort.findIndex(s => s.field === field);

      if (existingSortIndex !== -1) {
        const currentSort = this.selectedSort[existingSortIndex];
        if (currentSort.direction === 'asc') {
          currentSort.direction = 'desc';
        } else {
          this.selectedSort.splice(existingSortIndex, 1);
        }
      } else {
        this.selectedSort.push({ field, direction: 'asc' });
      }
      this.fetchProducts();
    },
    isSorted(field) {
      return this.selectedSort.some(s => s.field === field);
    },
    sortDirection(field) {
      const sortItem = this.selectedSort.find(s => s.field === field);
      return sortItem ? (sortItem.direction === 'asc' ? '▲' : '▼') : '';
    }
  },
  created() {
    this.fetchProducts();
  }
};
</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
  cursor: pointer;
}
th:hover {
  background-color: #f0f0f0;
}
</style>
