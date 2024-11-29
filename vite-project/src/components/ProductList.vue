<template>
  <div>
    <h1>Product List</h1>
    <div>
      <label>Page:
        <input type="number" v-model="page" @change="fetchProducts" min="1" />
      </label>
      <label>Size:
        <input type="number" v-model="size" @change="fetchProducts" min="1" />
      </label>
      <ErrorMessage :message="errorMessage" />
      <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
    </div>
    
    <table>
      <thead>
        <tr>
          <th rowspan="2" @click="setSort('id')">ID <span v-if="isSorted('id')">{{ sortDirection('id') }}</span></th>
          <th rowspan="2" @click="setSort('name')">Name <span v-if="isSorted('name')">{{ sortDirection('name') }}</span></th>
          <th rowspan="2" @click="setSort('price')">Price <span v-if="isSorted('price')">{{ sortDirection('price') }}</span></th>
          <th rowspan="2" @click="setSort('creationDate')">Creation Date <span v-if="isSorted('creationDate')">{{ sortDirection('creationDate') }}</span></th>
          <th rowspan="2" @click="setSort('unitOfMeasure')">Unit of Measure <span v-if="isSorted('unitOfMeasure')">{{ sortDirection('unitOfMeasure') }}</span></th>
          <th colspan="2">Coordinates</th>
          <th colspan="5">Owner <span v-if="isSorted('owner')">{{ sortDirection('owner') }}</span></th>
          <th rowspan="3">Actions</th>
        </tr>
        <tr>
          <th @click="setSort('coordinates.x')">X <span v-if="isSorted('coordinates.x')">{{ sortDirection('coordinates.x') }}</span></th>
          <th @click="setSort('coordinates.y')">Y <span v-if="isSorted('coordinates.y')">{{ sortDirection('coordinates.y') }}</span></th>
          <th @click="setSort('owner.name')">Name <span v-if="isSorted('owner.name')">{{ sortDirection('owner.name') }}</span></th>
          <th @click="setSort('owner.height')">Height <span v-if="isSorted('owner.height')">{{ sortDirection('owner.height') }}</span></th>
          <th @click="setSort('owner.birthday')">Birthday <span v-if="isSorted('owner.birthday')">{{ sortDirection('owner.birthday') }}</span></th>
          <th @click="setSort('owner.eyeColor')">Eye Color <span v-if="isSorted('owner.eyeColor')">{{ sortDirection('owner.eyeColor') }}</span></th>
          <th @click="setSort('owner.nationality')">Nationality <span v-if="isSorted('owner.nationality')">{{ sortDirection('owner.nationality') }}</span></th>
        </tr>
        <tr>
          <th>
            <select v-model="filters.id.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
              <option value="<="><=</option>
              <option value=">=">>=</option>
            </select>
            <input v-model="filters.id.value" @input="fetchProducts" placeholder="Filter ID" />
          </th>
          <th>
            <select v-model="filters.name.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
            </select>
            <input v-model="filters.name.value" @input="fetchProducts" placeholder="Filter Name" />
          </th>
          <th>
            <select v-model="filters.price.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
              <option value="<="><=</option>
              <option value=">=">>=</option>
            </select>
            <input v-model="filters.price.value" @input="fetchProducts" placeholder="Filter Price" />
          </th>
          <th>
            <select v-model="filters.creationDate.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
            </select>
            <input v-model="filters.creationDate.value" @input="fetchProducts" placeholder="Filter Date" />
          </th>
          <th>
            <select v-model="filters.unitOfMeasure.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
            </select>
            <input v-model="filters.unitOfMeasure.value" @input="fetchProducts" placeholder="Filter Unit" />
          </th>
          <th>
            <select v-model="filters.coordinates_x.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
              <option value="<="><=</option>
              <option value=">=">>=</option>
            </select>
            <input v-model="filters.coordinates_x.value" @input="fetchProducts" placeholder="Filter X" />
          </th>
          <th>
            <select v-model="filters.coordinates_y.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
              <option value="<="><=</option>
              <option value=">=">>=</option>
            </select>
            <input v-model="filters.coordinates_y.value" @input="fetchProducts" placeholder="Filter Y" />
          </th>
          <th>
            <select v-model="filters.owner_name.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
            </select>
            <input v-model="filters.owner_name.value" @input="fetchProducts" placeholder="Filter Name" />
          </th>
          <th>
            <select v-model="filters.owner_height.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
              <option value="<="><=</option>
              <option value=">=">>=</option>
            </select>
            <input v-model="filters.owner_height.value" @input="fetchProducts" placeholder="Filter Height" />
          </th>
          <th>
            <select v-model="filters.owner_birthday.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
              <option value="<"><</option>
              <option value=">">></option>
            </select>
            <input v-model="filters.owner_birthday.value" @input="fetchProducts" placeholder="Filter Birthday" />
          </th>
          <th>
            <select v-model="filters.owner_eyeColor.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
            </select>
            <input v-model="filters.owner_eyeColor.value" @input="fetchProducts" placeholder="Filter Eye Color" />
          </th>
          <th>
            <select v-model="filters.owner_nationality.operator" @change="fetchProducts">
              <option value="=">=</option>
              <option value="!=">!=</option>
            </select>
            <input v-model="filters.owner_nationality.value" @input="fetchProducts" placeholder="Filter Nationality" />
          </th>
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
      successMessage: '',
      page: 1,
      size: 10,
      totalPages: 1,
      selectedSort: [],
      filters: {
        id: { operator: '=', value: '' },
        name: { operator: '=', value: '' },
        price: { operator: '=', value: '' },
        creationDate: { operator: '=', value: '' },
        unitOfMeasure: { operator: '=', value: '' },
        coordinates_x: { operator: '=', value: '' },
        coordinates_y: { operator: '=', value: '' },
        owner_name: { operator: '=', value: '' },
        owner_height: { operator: '=', value: '' },
        owner_birthday: { operator: '=', value: '' },
        owner_eyeColor: { operator: '=', value: '' },
        owner_nationality: { operator: '=', value: '' },
      }
    };
  },
  methods: {
    fetchProducts() {
      var filters = "";

      Object.keys(this.filters).forEach((key) => {
        const filter = this.filters[key];
        if (filter.value) {
          var replKey = key
          replKey = replKey.replaceAll("_", ".")
          filters += `&filter=${replKey}[${filter.operator}]=${filter.value}`
        }
      });

      const sort = this.selectedSort.length ?
        this.selectedSort.map(s => `${s.field}:${s.direction}`).join("&sort=") :
        null;

      const endpoint = sort === null
        ? `http://localhost:8080/first-service/api/v1/products/?page=${this.page}&size=${this.size}${filters}`
        : `http://localhost:8080/first-service/api/v1/products/?page=${this.page}&size=${this.size}&sort=${sort}${filters}`;

      axios.get(endpoint)
        .then(response => {
          this.products = response.data.data;
          this.totalPages = response.data.meta.total_pages;
          this.errorMessage = '';
        })
        .catch(error => {
          this.errorMessage = error.response ?
            error.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ') :
            'Server Error';
        });
    },
    viewProduct(id) {
      this.$router.push({ path: `/product/${id}` });
    },
    deleteProduct(id) {
      if (confirm('Are you sure you want to delete this product?')) {
        axios.delete(`http://localhost:8080/first-service/api/v1/products/${id}`)
          .then(() => {
            this.successMessage = 'Product deleted successfully!';
            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
            this.fetchProducts();
          })
          .catch(error => {
            this.errorMessage = error.response ?
              error.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ') :
              'Server Error';
          });
      }
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
  text-align: center;
  cursor: pointer;
}
th:hover {
  background-color: #f0f0f0;
}
.alert {
  color: green;
  margin-bottom: 10px;
}
tr input, tr select {
  width: 100%;
  box-sizing: border-box;
}
</style>
