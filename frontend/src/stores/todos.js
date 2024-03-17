import { defineStore } from 'pinia'
import { ref } from 'vue'
import apiClient from '@/apiClient'

export const useTodoStore = defineStore('todo', () => {
  const todos = ref([])
  const isLoading = ref(false)
  const isFetchingData = ref(false)
  const isAddingTodo = ref(false)
  const updatingIds = ref(new Set())
  const deletingIds = ref(new Set())

  const fetchTodo = async () => {
    isLoading.value = true
    isFetchingData.value = true

    try {
      const response = await apiClient.get('/api/todo')
      todos.value = response.data
    } finally {
      isLoading.value = false
      isFetchingData.value = false
    }
  }

  const addTodo = async (newTodo) => {
    isLoading.value = true
    isAddingTodo.value = true

    try {
      const response = await apiClient.post('/api/todo', newTodo)
      todos.value.unshift(response.data)
    } finally {
      isLoading.value = false
      isAddingTodo.value = false
    }
  }

  const updateTodo = async (todoId, todo) => {
    if (updatingIds.value.has(todoId)) {
      return
    }

    updatingIds.value.add(todoId)

    try {
      const response = await apiClient.put(`/api/todo/${todoId}`, todo)
      const index = todos.value.findIndex((todo) => todo.id === todoId)

      if (index !== -1) {
        todos.value[index] = { ...todos.value[index], ...response.data }
      }
    } finally {
      updatingIds.value.delete(todoId)
    }
  }

  const deleteTodo = async (todoId) => {
    if (deletingIds.value.has(todoId)) {
      return
    }

    deletingIds.value.add(todoId)

    try {
      await apiClient.delete(`/api/todo/${todoId}`)
      todos.value = todos.value.filter((todo) => todo.id !== todoId)
    } finally {
      deletingIds.value.delete(todoId)
    }
  }

  const clearState = () => {
    todos.value = []
    isLoading.value = false
    isFetchingData.value = false
    isAddingTodo.value = false
    updatingIds.value = new Set()
    deletingIds.value = new Set()
  }

  return {
    todos,
    isFetchingData,
    isAddingTodo,
    isLoading,
    fetchTodo,
    addTodo,
    updateTodo,
    deleteTodo,
    clearState
  }
})
