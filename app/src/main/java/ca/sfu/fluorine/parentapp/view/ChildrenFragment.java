package ca.sfu.fluorine.parentapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.sfu.fluorine.parentapp.ChildFormActivity;
import ca.sfu.fluorine.parentapp.R;
import ca.sfu.fluorine.parentapp.databinding.FragmentChildrenBinding;


/**
 * ChildrenFragment.java - represents the UI of the configure children
 */
public class ChildrenFragment extends Fragment {
	private FragmentChildrenBinding binding;

	// Define the ListView object
	ListView childrenList;

	// Define variables for user selected contact
    long childID;

    // Adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter cursorAdapter;

    @Override
	public View onCreateView(
	    @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
		// Inflate the layout for this fragment
		binding = FragmentChildrenBinding.inflate(
		    inflater,
            container,
            false
        );

		return binding.getRoot();

    }

    /** Called when the user taps the Send button */


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // floating action button
        binding.buttonAddChild.setOnClickListener(
            btnView -> {
                Intent intent = new Intent(getContext(), ChildFormActivity.class);
            startActivity(intent);
            });

    }

    @Override
	public void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
