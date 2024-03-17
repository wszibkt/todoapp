<script setup>
import { ref } from 'vue'
import { useCategoriesStore } from '@/stores/categories'

defineProps(['categories'])

const isUpdating = ref(false)
const isDeleting = ref(false)

const categoryStore = useCategoriesStore()

async function deleteCategory(categoryId) {
  if (isDeleting.value) {
    return
  }

  if (confirm('Are you sure you want to delete a category?') == true) {
    isDeleting.value = true
    try {
      await categoryStore.deleteCategory(categoryId)
    } catch (err) {
      console.log('error occured while trying to delete category...', err)
    } finally {
      isDeleting.value = false
    }
  }
}

async function updateCategory(categoryId, categoryName) {
  if (isUpdating.value) {
    return
  }

  isUpdating.value = true

  try {
    let newName = prompt('Category name', categoryName)
    if (newName == null) {
      return
    }

    if (confirm('Are you sure you want to change the category name?') == true) {
      await categoryStore.updateCategory(categoryId, newName)
    }
  } catch (err) {
    console.log('error occured while trying to change category name...', err)
  } finally {
    isUpdating.value = false
  }
}
</script>

<template>
  <div class="categories-table" v-if="categories">
    <table>
      <tr>
        <th>#</th>
        <th>Name</th>
        <th></th>
      </tr>
      <tr v-for="category in categories" :key="category.id">
        <td>{{ category.id }}</td>
        <td>{{ category.name }}</td>
        <td class="right-align">
          <i class="icon edit-icon" @click="updateCategory(category.id, category.name)"></i>
          <i class="icon delete-icon" @click="deleteCategory(category.id)"></i>
        </td>
      </tr>
    </table>
  </div>
  <div v-else></div>
</template>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
}
.right-align {
  text-align: right;
}

td i {
  margin-left: 10px;
}

td,
th {
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #696969;
}

.icon:hover {
  cursor: pointer;
}
</style>
