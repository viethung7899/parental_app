package ca.sfu.fluorine.parentapp.view.children;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.sfu.fluorine.parentapp.R;
import ca.sfu.fluorine.parentapp.databinding.FragmentChildrenBinding;
import ca.sfu.fluorine.parentapp.model.children.Child;
import ca.sfu.fluorine.parentapp.model.children.ChildrenManager;

/**
 * ChildrenFragment.java - represents the UI of the configure children feature.
 */
public class ChildrenFragment extends Fragment {
	private FragmentChildrenBinding binding;
	private ChildrenManager manager;

    @Override
	public View onCreateView(
	    @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
		// Inflate the layout for this fragment
		binding = FragmentChildrenBinding.inflate(inflater, container, false);
		return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        manager = ChildrenManager.getInstance(requireContext());

        // floating action button
        binding.buttonAddChild.setOnClickListener(
            btnView ->
                startActivity(ChildFormActivity.makeIntent(requireContext(), ChildFormActivity.ADD_CHILD))
            );

        // Populate the list
        binding.childrenList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.childrenList.setAdapter(new ChildListAdapter(this, requireContext()));
    }

    @Override
	public void onDestroy() {
		super.onDestroy();
		binding = null;
	}

    class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ViewHolder> {
        private ChildrenFragment childrenFragment;

        public ChildListAdapter(ChildrenFragment childrenFragment, Context context) {
            this.childrenFragment = childrenFragment;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleCreationName;
            CardView childCard;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                // Find all the components of the view.
                titleCreationName =  itemView.findViewById(R.id.childNameDisplay);
                childCard = itemView.findViewById(R.id.childCard);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(
            @NonNull
                ViewGroup parent,
            int viewType
        ) {
            View view = LayoutInflater.from(requireContext()).inflate(
                R.layout.child_row_layout,
                parent,
                false
            );
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
            @NonNull
                ViewHolder holder,
            int position
        ) {
            // get child object from index
            Child child = childrenFragment.manager.getChildByIndex(position);

            // change the text to display childs name
            holder.titleCreationName.setText(child.getFirstName());

            // make the list item clickable
            // TODO - change to edit activity.
            holder.itemView.setOnClickListener((View view) -> {
                Intent intent = new Intent(ChildFormActivity.makeIntent(requireContext(), position));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return childrenFragment.manager.getChildren().size();
        }
    }
}