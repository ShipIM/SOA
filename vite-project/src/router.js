import { createMemoryHistory, createRouter } from 'vue-router'

import Products from './views/Products.vue'
import ProductManagement from './views/ProductManagement.vue'
import ProductDetail from './components/ProductDetail.vue';
import ProductForm from './components/ProductForm.vue';
import Measures from './views/Measures.vue';
import Colors from './views/Colors.vue';
import Countries from './views/Countries.vue';
import DeleteProductsByPrice from './views/DeleteProducts.vue';
import ProductWithMinCreationDate from './views/MinDate.vue';
import UniqueUnits from './views/Unique.vue';
import EbayProducts from './views/EbayProducts.vue';


const routes = [
  {
    path: '/',
    name: 'Products',
    component: Products
  },
  {
    path: '/management',
    name: 'ProductManagement',
    component: ProductManagement
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail
  },
  {
    path: '/product/update/:id',
    name: 'ProductUpdate',
    component: ProductForm
  },
  {
    path: '/measures',
    name: 'Measures',
    component: Measures
  },
  {
    path: '/colors',
    name: 'Colors',
    component: Colors
  },
  {
    path: '/countries',
    name: 'Countries',
    component: Countries
  },
  {
    path: '/delete-by-price',
    name: 'DeleteProductsByPrice',
    component: DeleteProductsByPrice
  },
  {
    path: '/min-creation-date',
    name: 'ProductWithMinCreationDate',
    component: ProductWithMinCreationDate
  },
  {
    path: '/unique-units',
    name: 'UniqueUnits',
    component: UniqueUnits
  },
  {
    path: '/ebay',
    name: 'EbayProducts',
    component: EbayProducts
  }
]
  
const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router
