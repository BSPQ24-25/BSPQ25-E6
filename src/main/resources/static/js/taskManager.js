document.addEventListener('DOMContentLoaded', (event) => {

    var dragSrcEl = null;
    
    function handleDragStart(e) {
      this.style.opacity = '0.1';
      this.style.border = '3px dashed #c4cad3';
      
      dragSrcEl = this;
  
      e.dataTransfer.effectAllowed = 'move';
      e.dataTransfer.setData('text/html', this.innerHTML);
    }
  
    function handleDragOver(e) {
      if (e.preventDefault) {
        e.preventDefault();
      }
  
      e.dataTransfer.dropEffect = 'move';
      
      return false;
    }
  
    function handleDragEnter(e) {
      this.classList.add('task-hover');
    }
  
    function handleDragLeave(e) {
      this.classList.remove('task-hover');
    }
  
    function handleDrop(e) {
      if (e.stopPropagation) {
        e.stopPropagation(); // stops the browser from redirecting.
      }
      
      if (dragSrcEl != this) {
        dragSrcEl.innerHTML = this.innerHTML;
        this.innerHTML = e.dataTransfer.getData('text/html');
      }
      
      return false;
    }
  
    function handleDragEnd(e) {
      this.style.opacity = '1';
      this.style.border = 0;
      
      items.forEach(function (item) {
        item.classList.remove('task-hover');
      });
    }
    
    
    let items = document.querySelectorAll('.task'); 
    items.forEach(function(item) {
      item.addEventListener('dragstart', handleDragStart, false);
      item.addEventListener('dragenter', handleDragEnter, false);
      item.addEventListener('dragover', handleDragOver, false);
      item.addEventListener('dragleave', handleDragLeave, false);
      item.addEventListener('drop', handleDrop, false);
      item.addEventListener('dragend', handleDragEnd, false);
    });
    // Seleccionar el formulario
const taskForm = document.querySelector('.task-form');

// Validar el formulario antes de enviarlo
if (taskForm) {
    taskForm.addEventListener('submit', (event) => {
        const taskName = document.getElementById('taskName').value.trim();
        const taskDescription = document.getElementById('taskDescription').value.trim();
        const taskDueDate = document.getElementById('taskDueDate').value;

        // Validación básica
        if (!taskName || !taskDescription || !taskDueDate) {
            event.preventDefault(); // Evitar el envío del formulario
            alert('Please fill out all fields before submitting the form.');
        }
    });
}

// Opcional: Añadir interacción visual al formulario
const inputs = document.querySelectorAll('.task-form input, .task-form textarea, .task-form select');
inputs.forEach(input => {
    input.addEventListener('focus', () => {
        input.style.borderColor = '#007bff'; // Cambiar el color del borde al enfocar
    });
    input.addEventListener('blur', () => {
        input.style.borderColor = '#ccc'; // Restaurar el color del borde al desenfocar
    });
});
  });

// function to handle task search
document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById('taskSearch');
  const clearSearch = document.getElementById('clearSearch');
  const taskColumns = document.querySelectorAll('.project-column');
  
  // Create no-results message
  const noResultsMsg = document.createElement('div');
  noResultsMsg.className = 'no-results';
  noResultsMsg.textContent = 'No tasks found';
  document.querySelector('.project-tasks').prepend(noResultsMsg);
  noResultsMsg.style.display = 'none';

  searchInput.addEventListener('input', function() {
      const searchTerm = this.value.toLowerCase();
      let hasResults = false;

      taskColumns.forEach(column => {
          const tasks = column.querySelectorAll('.task');
          let columnHasResults = false;

          tasks.forEach(task => {
              const taskText = task.textContent.toLowerCase();
              const isMatch = searchTerm === '' || taskText.includes(searchTerm);
              
              // Use visibility+height instead of display to preserve layout
              task.style.visibility = isMatch ? 'visible' : 'hidden';
              task.style.height = isMatch ? 'auto' : '0';
              task.style.margin = isMatch ? '' : '0';
              
              if (isMatch) {
                  columnHasResults = true;
                  hasResults = true;
              }
          });

          // Hide entire column if no matches
          column.style.display = columnHasResults || searchTerm === '' ? 'block' : 'none';
      });

      noResultsMsg.style.display = hasResults || searchTerm === '' ? 'none' : 'block';
  });

  clearSearch.addEventListener('click', function() {
      searchInput.value = '';
      searchInput.focus();
      searchInput.dispatchEvent(new Event('input')); // Trigger search update
  });
});

document.addEventListener('keydown', (e) => {
  // Focus search on Ctrl+K
  if (e.ctrlKey && e.key === 'k') {
      e.preventDefault();
      document.getElementById('taskSearch').focus();
  }
});

// Function to handle task progress
document.addEventListener('DOMContentLoaded', () => {
  const overallProgress = document.getElementById('overallProgress');
  const overallProgressBar = document.getElementById('overallProgressBar');

  let totalTasks = 0;
  let completedTasks = 0;

  // Select all tasks
  const allTasks = document.querySelectorAll('.task');

  // Iterate through each task to calculate totals
  allTasks.forEach(task => {
      totalTasks++; // Count every task as part of the total

      // Check if the task is in the "Done" column
      const parentColumn = task.closest('.project-column');
      if (parentColumn && parentColumn.querySelector('.project-column-heading__title').textContent.trim() === 'Done') {
          completedTasks++; // Count only tasks in the "Done" column as completed
      }
  });

  // Calculate the percentage
  const percentage = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  // Update the UI
  overallProgress.textContent = `${percentage}%`;
  overallProgressBar.value = percentage;
});