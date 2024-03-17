<script setup>
import { ref } from 'vue'
import { useTodoStore } from '@/stores/todos'

const isUpdating = ref(false)
const isUpdatingTitle = ref(false)
const isDeleting = ref(false)
const props = defineProps({
  todo: {
    required: true
  }
})
const todoStore = useTodoStore()

async function deleteTodo() {
  if (isDeleting.value) {
    return
  }

  if (confirm('Are you sure you want to delete a todo?') == true) {
    isDeleting.value = true
    try {
      await todoStore.deleteTodo(props.todo.id)
    } catch (err) {
      console.log('error occured while trying to delete todo...', err)
    } finally {
      isDeleting.value = false
    }
  }
}

async function changeComplete() {
  if (isUpdating.value) {
    return
  }

  isUpdating.value = true
  try {
    await todoStore.updateTodo(props.todo.id, {
      title: props.todo.title,
      completed: !props.todo.completed,
      categoryId: props.todo.category.id
    })
  } catch (err) {
    console.log('error occured while trying to change completion...', err)
  } finally {
    isUpdating.value = false
  }
}

async function changeTitle() {
  if (isUpdatingTitle.value) {
    return
  }

  isUpdatingTitle.value = true
  try {
    let newTitle = prompt('What you want to do?', props.todo.title)
    if (newTitle == null) {
      return
    }

    if (confirm('Are you sure you want to change the todo?') == true) {
      await todoStore.updateTodo(props.todo.id, {
        title: newTitle,
        completed: props.todo.completed,
        categoryId: props.todo.category.id
      })
    }
  } catch (err) {
    console.log('error occured while trying to change title...', err)
  } finally {
    isUpdatingTitle.value = false
  }
}
</script>

<template>
  <div class="todo-item">
    <div class="todo-item-flex">
      <input
        type="checkbox"
        :checked="todo.completed"
        @click="changeComplete"
        :disabled="isUpdating"
      />
      <div class="todo-item-title">
        <div>
          <h3>
            <small>
              <span class="category-crumb">{{ todo.category.name ?? 'anything' }}</span>
            </small>
            {{ todo.title }}
          </h3>
        </div>
        <p>
          {{
            new Date(todo.createTime * 1000).toLocaleString('en-US', {
              hour: '2-digit',
              minute: '2-digit',
              day: '2-digit',
              month: '2-digit',
              year: 'numeric'
            })
          }}
        </p>
      </div>
    </div>
    <div class="todo-item-flex">
      <div :class="{ disabled: isUpdatingTitle, 'todo-item-icon': true }" @click="changeTitle">
        <i class="icon edit-icon"></i>
      </div>
      <div :class="{ disabled: isDeleting, 'todo-item-icon': true }" @click="deleteTodo">
        <i class="icon delete-icon"></i>
      </div>
    </div>
  </div>
</template>

<style scoped>
.category-crumb {
  background-color: rgba(5, 133, 126, 0.432);
  padding: 5px;
  border-radius: 10px;
  color: rgb(238, 238, 238);
  font-size: 0.75rem;
}

.todo-item {
  border-radius: 10px;
  padding: 0.25rem 1rem 0.25rem 1rem;
  background-color: rgba(63, 61, 61, 0.76);
  margin-bottom: 1rem;

  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-item-flex {
  display: flex;
  align-items: center;
}

.todo-item-title {
  display: block;
  padding: 10px;
  font-weight: bold !important;
}

.todo-item-title p {
  font-size: 0.75rem;
}

.todo-item button:first-child {
  margin-right: 3px;
}

.todo-item:last-child {
  margin-bottom: 0;
}

input[type='checkbox'] {
  -webkit-appearance: none;
  appearance: none;
  background-color: var(--form-background);
  margin: 0;

  font: inherit;
  color: currentColor;
  width: 1.5em;
  height: 1.5em;
  border: 0.15em solid currentColor;
  border-radius: 0.15em;
  transform: translateY(-0.075em);

  display: grid;
  place-content: center;
}

input[type='checkbox']::before {
  content: '';
  width: 0.65em;
  height: 0.65em;
  clip-path: polygon(14% 44%, 0 65%, 50% 100%, 100% 16%, 80% 0%, 43% 62%);
  transform: scale(0);
  transform-origin: bottom left;
  transition: 120ms transform ease-in-out;
  box-shadow: inset 1em 1em var(--form-control-color);
  background-color: CanvasText;
}

input[type='checkbox']:checked::before {
  transform: scale(1);
}

input[type='checkbox']:focus {
  outline: max(2px, 0.15em) solid currentColor;
  outline-offset: max(2px, 0.15em);
}

input[type='checkbox']:disabled {
  --form-control-color: var(--form-control-disabled);

  color: var(--form-control-disabled);
  cursor: not-allowed;
}
</style>
