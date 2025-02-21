<template>
  <div>
    <h1>{{ isEdit ? 'Edit' : 'Add' }} Product</h1>
    <ErrorMessage :message="errorMessage" />
    <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="product-name">Product Name</label>
        <input id="product-name" v-model="product.name" placeholder="Product Name" required />
      </div>
      <div class="form-group">
        <label for="product-price">Price</label>
        <input id="product-price" v-model="product.price" placeholder="Price" type="number" required />
      </div>
      <div class="form-group">
        <label for="coordinate-x">X Coordinate</label>
        <input id="coordinate-x" v-model="product.coordinates.x" placeholder="X Coordinate" type="number" required />
      </div>
      <div class="form-group">
        <label for="coordinate-y">Y Coordinate</label>
        <input id="coordinate-y" v-model="product.coordinates.y" placeholder="Y Coordinate" type="number" required />
      </div>
      <div class="form-group">
        <label for="unit-of-measure">Unit of Measure</label>
        <select id="unit-of-measure" v-model="product.unit_of_measure" required>
          <option value="">Select Unit of Measure</option>
          <option v-for="unit in units" :key="unit" :value="unit">{{ unit }}</option>
        </select>
      </div>
      <h3>Owner Information</h3>
      <div class="form-group">
        <label for="owner-name">Owner Name</label>
        <input id="owner-name" v-model="product.owner.name" placeholder="Owner Name" required />
      </div>
      <div class="form-group">
        <label for="owner-height">Owner Height (Greater than 0)</label>
        <input id="owner-height" v-model="product.owner.height" placeholder="Owner Height" type="number" required />
      </div>
      <div class="form-group">
        <label for="owner-birthday">Owner Birthday (Optional)</label>
        <input id="owner-birthday" type="date" v-model="product.owner.birthday" />
      </div>
      <div class="form-group">
        <label for="owner-eye-color">Eye Color</label>
        <select id="owner-eye-color" v-model="product.owner.eye_color" required>
          <option value="">Select Eye Color</option>
          <option v-for="color in eyeColors" :key="color" :value="color">{{ color }}</option>
        </select>
      </div>
      <div class="form-group">
        <label for="owner-nationality">Nationality</label>
        <select id="owner-nationality" v-model="product.owner.nationality" required>
          <option value="">Select Nationality</option>
          <option v-for="country in countries" :key="country" :value="country">{{ country }}</option>
        </select>
      </div>
      <button type="submit">{{ isEdit ? 'Update' : 'Add' }} Product</button>
    </form>
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
      product: {
        name: '',
        price: null,
        coordinates: { x: null, y: null },
        unit_of_measure: '',
        owner: {
          name: '',
          height: null,
          birthday: null,
          eye_color: '',
          nationality: ''
        }
      },
      isEdit: false,
      errorMessage: '',
      successMessage: '',
      units: [],
      eyeColors: [],
      countries: []
    }
  },
  methods: {
    fetchProduct() {
      const id = this.$route.params.id;
      axios.get(`https://localhost:8090/api/v1/products/${id}`)
        .then(response => {
          this.product = response.data;
          this.errorMessage = '';
        })
        .catch(err => {
          if (err.response) {
            this.errorMessage = err.response.data.errors
              .map(e => `${e.field}: ${e.message}`).join(', ');
          } else {
            this.errorMessage = 'Server Error';
          }
        });
    },
    fetchUnits() {
      axios.get('https://localhost:8090/api/v1/products/measure')
        .then(response => {
          this.units = response.data;
        })
        .catch(error => {
          console.error('Error fetching units:', error);
        });
    },
    fetchEyeColors() {
      axios.get('https://localhost:8090/api/v1/products/color')
        .then(response => {
          this.eyeColors = response.data;
        })
        .catch(error => {
          console.error('Error fetching eye colors:', error);
        });
    },
    fetchCountries() {
      axios.get('https://localhost:8090/api/v1/products/country')
        .then(response => {
          this.countries = response.data;
        })
        .catch(error => {
          console.error('Error fetching countries:', error);
        });
    },
    submitForm() {
      const method = this.isEdit ? 'put' : 'post';
      const url = this.isEdit ? `https://localhost:8090/api/v1/products/${this.product.id}` : 'https://localhost:8090/api/v1/products';

      const { id, creation_date, ...productWithoutIdAndDate } = this.product;

      axios[method](url, this.isEdit ? productWithoutIdAndDate : this.product)
        .then(() => {
          this.successMessage = this.isEdit ? 'Product updated successfully!' : 'Product added successfully!';
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
          this.isEdit ? this.$router.push({ name: 'ProductUpdate', params: { id: this.product.id } }) : this.$router.push({ name: 'Products' });
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
    goBack() {
      this.$router.push({ name: 'Products' });
    }
  },
  created() {
    this.fetchUnits();
    this.fetchEyeColors();
    this.fetchCountries();
    if (this.$route.params.id) {
      this.isEdit = true;
      this.fetchProduct();
    }
  }
}
</script>

<style>
form {
  display: flex;
  flex-direction: column;
}
.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
label {
  width: 200px;
  margin-right: 10px;
}
input, select {
  flex: 1;
}
button {
  margin-top: 10px;
}
.alert {
  color: green;
  margin-bottom: 10px;
}
</style>
