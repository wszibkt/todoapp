<script setup>
import { ref, onMounted, computed } from 'vue'
import TodoItemComponent from '@/components/TodoItemComponent.vue'
import { useTodoStore } from '@/stores/todos'
import { useCategoriesStore } from '@/stores/categories'

const showForm = ref(false)
const todoStore = useTodoStore()
const categoriesStore = useCategoriesStore()
const selectedCategories = ref('All')

let todoTask = ref()
let todoCategory = ref()

const todoTaskError = ref('')
const categoryError = ref('')

function addTaskClicked() {
  showForm.value = !showForm.value
}

function validateInput() {
  todoTaskError.value = ''
  categoryError.value = ''

  let isValid = true

  if (todoTask.value.length < 3) {
    todoTaskError.value = 'Task must be at least 3 characters'
    isValid = false
  }

  if (todoCategory.value <= 0) {
    categoryError.value = 'Invalid category'
    isValid = false
  }

  return isValid
}

async function addTodo() {
  if (validateInput()) {
    try {
      await todoStore.addTodo({
        categoryId: todoCategory.value,
        title: todoTask.value
      })
      showForm.value = false
    } catch (ex) {
      console.log(ex)
    }
  }
}

const filteredTodos = computed(() => {
  if (selectedCategories.value === 'All') {
    return todoStore.todos
  } else {
    return todoStore.todos.filter((todo) => todo.category.id === selectedCategories.value)
  }
})

onMounted(async () => {
  if (todoStore.todos?.length == 0) {
    await todoStore.fetchTodo()
  }
  if (categoriesStore.categories?.length == 0) {
    await categoriesStore.fetchCategories()
  }
})
</script>

<template>
  <div class="box">
    <div class="box-heading">
      <h1>TODO LIST</h1>
    </div>

    <div class="todolist-grid-2">
      <button
        @click="addTaskClicked"
        :disabled="categoriesStore.categories?.length == 0"
        :title="categoriesStore.categories?.length == 0 ? 'You must add a category first!' : ''"
      >
        Add task
      </button>
      <select v-model="selectedCategories">
        <option>All</option>
        <option
          v-for="category in categoriesStore.categories"
          :key="category.id"
          :value="category.id"
        >
          {{ category.name }}
        </option>
      </select>
    </div>

    <div v-if="showForm" id="addTodoContent">
      <h2>Add new TODO</h2>
      <hr />
      <form @submit.prevent="addTodo">
        <div class="form-group">
          <label for="todoTask">What you want todo</label>
          <input type="text" id="todoTask" v-model="todoTask" required />
          <span v-show="todoTaskError" class="error-message">{{ todoTaskError }}</span>
        </div>
        <div class="form-group">
          <label for="todoCategory">Category</label>
          <select id="todoCategory" v-model="todoCategory" required>
            <option
              v-for="category in categoriesStore.categories"
              :key="category.id"
              :value="category.id"
            >
              {{ category.name }}
            </option>
          </select>
          <span v-show="categoryError" class="error-message">{{ categoryError }}</span>
        </div>
        <div class="todoadd-button-container">
          <button id="cancelButton" type="submit" @click="addTaskClicked">Cancel</button>
          <button type="submit" :disabled="todoStore.isAddingTodo">Save</button>
        </div>
      </form>
    </div>

    <div class="center" v-if="todoStore.isFetchingData">Loading...</div>
    <div class="center" v-else-if="todoStore.todos?.length == 0">
      <h2>Nothing to do yet...</h2>
    </div>
    <div v-else class="todo-items-container">
      <TodoItemComponent v-for="todo in filteredTodos" :key="todo.id" :todo="todo" />
    </div>
  </div>
</template>

<style scoped>
#addTodoContent {
  padding: 20px;
  background-color: rgba(34, 33, 33, 0.76);
  border-radius: 20px;
  margin-bottom: 20px;

  hr {
    border: 0;
    height: 0;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    border-bottom: 1px solid rgba(255, 255, 255, 0.3);
    margin-bottom: 10px;
  }

  .todoadd-button-container {
    display: flex;
    justify-content: space-between;
  }

  #cancelButton {
    background-color: rgba(65, 64, 64, 0.83);
  }
}

.todo-items-container {
  border-radius: 15px;
  padding: 1rem;
  background-color: rgba(34, 33, 33, 0.76);
}

.todolist-grid-2 {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 1rem;
}
.center {
  text-align: center;
}
</style>
