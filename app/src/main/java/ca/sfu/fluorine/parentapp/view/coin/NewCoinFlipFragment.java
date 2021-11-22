package ca.sfu.fluorine.parentapp.view.coin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import ca.sfu.fluorine.parentapp.R;
import ca.sfu.fluorine.parentapp.databinding.FragmentNewCoinFlipBinding;
import ca.sfu.fluorine.parentapp.model.AppDatabase;
import ca.sfu.fluorine.parentapp.model.children.Child;
import ca.sfu.fluorine.parentapp.service.ImageInternalStorage;
import ca.sfu.fluorine.parentapp.view.utils.ChildrenAutoCompleteAdapter;
import ca.sfu.fluorine.parentapp.view.utils.Utility;
import ca.sfu.fluorine.parentapp.viewmodel.ChildrenViewModel;

/**
 * NewCoinFlipFragment
 *
 * Represents the UI to set up a new coin flip.
 */
public class NewCoinFlipFragment extends Fragment {
	private FragmentNewCoinFlipBinding binding;
	private ChildrenAutoCompleteAdapter childrenArrayAdapter;
	private ChildrenViewModel viewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(ChildrenViewModel.class);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentNewCoinFlipBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel.getChildrenByCoinFlipsLiveData().observe(getViewLifecycleOwner(), children -> {
			if (children.isEmpty()) {
				NavHostFragment.findNavController(this).navigate(R.id.flipping_coin_action);
			}
			childrenArrayAdapter = new ChildrenAutoCompleteAdapter(requireContext(), children, true);
			binding.dropdownSelection.setAdapter(childrenArrayAdapter);
			setupMenuWithImages();
		});
		setupButton();
	}

	public void setupMenuWithImages() {
		// Pre-select the first choice
		Child first = childrenArrayAdapter.getItem(0);
		childrenArrayAdapter.setSelectedChild(first);
		binding.dropdownSelection.setText(
				getString(R.string.full_name, first.getFirstName(), first.getLastName()),
				false);
		Utility.setupImage(requireContext(), binding.currentChild, first);

		// Add listener
		binding.dropdownSelection.setOnItemClickListener((adapterView, view, i, l) -> {
			Child child = childrenArrayAdapter.getItem(i);
			childrenArrayAdapter.setSelectedChild(child);
			Utility.setupImage(requireContext(), binding.currentChild, child);
		});
	}

	private void setupButton() {
		View.OnClickListener listener = (btnView) -> {
			boolean isHead = binding.coinSideSelection.getCheckedButtonId() == R.id.head;
			NewCoinFlipFragmentDirections.FlippingCoinAction action =
					NewCoinFlipFragmentDirections
							.flippingCoinAction()
							.setChildId(childrenArrayAdapter.getSelectedChild().getId())
							.setCoinSide(isHead);
			NavHostFragment.findNavController(this).navigate(action);
		};
		binding.flipButton.setOnClickListener(listener);
	}
}
