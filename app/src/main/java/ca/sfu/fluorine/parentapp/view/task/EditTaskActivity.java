package ca.sfu.fluorine.parentapp.view.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ca.sfu.fluorine.parentapp.R;
import ca.sfu.fluorine.parentapp.databinding.ActivityTaskFormBinding;
import ca.sfu.fluorine.parentapp.model.AppDatabase;
import ca.sfu.fluorine.parentapp.model.children.Child;
import ca.sfu.fluorine.parentapp.model.task.Task;
import ca.sfu.fluorine.parentapp.model.task.TaskAndChild;

public class EditTaskActivity extends AddTaskActivity {
    private static final String TASK_ID = "taskIndex";
    private TaskAndChild taskAndChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the task id from intent then fetch from database
        List<TaskAndChild> tasks = database.taskDao().getTaskByIdWithChild(getIntent().getIntExtra(TASK_ID, 0));

        if (!tasks.isEmpty()) {
            taskAndChild = tasks.get(0);
            Child child = taskAndChild.getChild();
            childId = child.getId();
            binding.editTaskName.setText(taskAndChild.getTask().getName());
            binding.dropdownSelection.setText(getString(R.string.full_name, child.getFirstName(), child.getLastName()), false);
        }

        // TODO: Add edit option for child selection

        setTitle("Edit Task");
        binding.buttonSaveTask.setOnClickListener(editTaskDialogListener);
        binding.buttonDeleteTask.setVisibility(View.VISIBLE);
        binding.buttonDeleteTask.setOnClickListener(deleteTaskDialogListener);

        binding.editTaskName.addTextChangedListener(watcher);
        binding.dropdownSelection.addTextChangedListener(watcher);
    }

    // TODO: Fix update task & child for task
    private final View.OnClickListener editTaskDialogListener = (btnView) -> makeConfirmDialog(
            R.string.edit_task,
            R.string.edit_task_confirm,
            (dialogInterface, i) -> {
                String taskName = binding.editTaskName.getText().toString();
                taskAndChild.getTask().update(taskName, childId);
                database.taskDao().updateTask(taskAndChild.getTask());
                finish();
            });

    private final View.OnClickListener deleteTaskDialogListener = (btnView) -> makeConfirmDialog(
            R.string.delete_task,
            R.string.delete_child_confirm,
            (dialogInterface, i) -> {
                database.taskDao().deleteTask(taskAndChild.getTask());
                finish();
            });

    @NonNull
    public static Intent makeIntent(Context context, int taskId) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }
}