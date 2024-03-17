import { defineStore } from 'pinia'
import { ref } from 'vue'
import apiClient from '@/apiClient'
import { useTodoStore } from '@/stores/todos'

export const useCategoriesStore = defineStore('categories', () => {
  const categories = ref([])
  const isLoading = ref(false)
  const isFetchingData = ref(false)
  const isAddingCategory = ref(false)
  const updatingIds = ref(new Set())
  const deletingIds = ref(new Set())

  const fetchCategories = async () => {
    isLoading.value = true
    isFetchingData.value = true

    try {
      const response = await apiClient.get('/api/category')
      categories.value = response.data
    } finally {
      isLoading.value = false
      isFetchingData.value = false
    }
  }

  const addCategory = async (newCategory) => {
    if (newCategory.trim() === '') return

    isLoading.value = true
    isAddingCategory.value = true

    try {
      const response = await apiClient.post('/api/category', {
        name: newCategory
      })

      categories.value.push(response.data)
    } finally {
      isLoading.value = false
      isAddingCategory.value = false
    }
  }

  const updateCategory = async (categoryId, newName) => {
    if (updatingIds.value.has(categoryId)) {
      return
    }

    updatingIds.value.add(categoryId)

    try {
      const response = await apiClient.put(`/api/category/${categoryId}`, {
        name: newName
      })

      const index = categories.value.findIndex((category) => category.id === categoryId)

      if (index !== -1) {
        const todoStore = useTodoStore()
        todoStore.todos
          .filter((todo) => todo.category.id === categoryId)
          .forEach((todo) => {
            todo.category.name = newName
          })
        categories.value[index] = { ...categories.value[index], ...response.data }
      }
    } finally {
      updatingIds.value.delete(categoryId)
    }
  }

  const deleteCategory = async (categoryId) => {
    if (deletingIds.value.has(categoryId)) {
      return
    }

    deletingIds.value.add(categoryId)

    try {
      await apiClient.delete(`/api/category/${categoryId}`)
      categories.value = categories.value.filter((category) => category.id !== categoryId)

      const todoStore = useTodoStore()
      todoStore.todos = todoStore.todos.filter((todo) => todo.category.id !== categoryId)
    } finally {
      deletingIds.value.delete(categoryId)
    }
  }

  const clearState = () => {
    categories.value = []
    isLoading.value = false
    isFetchingData.value = false
    isAddingCategory.value = false
    updatingIds.value = new Set()
    deletingIds.value = new Set()
  }

  return {
    categories,
    isFetchingData,
    isAddingCategory,
    isLoading,
    fetchCategories,
    addCategory,
    updateCategory,
    deleteCategory,
    clearState
  }
})
