<script setup>
import { ref, onMounted } from 'vue'
import CategoriesTableComponent from '@/components/CategoriesTableComponent.vue'
import { useCategoriesStore } from '@/stores/categories'

const categoriesStore = useCategoriesStore()
const showForm = ref(false)
const newCategory = ref('')
const error = ref([])

function addCategoryClicked() {
  showForm.value = !showForm.value
}

async function submitForm() {
  error.value = []

  try {
    await categoriesStore.addCategory(newCategory.value)
  } catch (ex) {
    if (ex.response.data?.name) {
      error.value = ex.response.data.name
    } else {
      error.value.push(ex.response.data)
    }
  }
}

onMounted(async () => {
  if (categoriesStore.categories?.length == 0) {
    await categoriesStore.fetchCategories()
  }
})
</script>

<template>
  <div class="box">
    <div class="box-heading">
      <h1>Categories</h1>
    </div>

    <button @click="addCategoryClicked">Add category</button>

    <form v-if="showForm" id="add-category-form" @submit.prevent="submitForm">
      <h3>Add category</h3>

      <div class="form-group">
        <input type="text" placeholder="category name" v-model="newCategory" required />
        <ul v-if="error" class="error-list">
          <li v-for="(error, index) in error" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <button type="submit" :disabled="categoriesStore.isAddingCategory">Save</button>
    </form>

    <div class="center" v-if="categoriesStore.isFetchingData">Loading...</div>
    <div class="center" v-else-if="categoriesStore.categories?.length <= 0">
      <h2>No categories</h2>
    </div>
    <CategoriesTableComponent :categories="categoriesStore.categories" v-else />
  </div>
</template>

<style scoped>
.center {
  text-align: center;
}
#add-category-form {
  margin-top: 1rem;
  margin-bottom: 1rem;
  button {
    font-size: smaller;
    margin-left: 5px;
  }
}
</style>
