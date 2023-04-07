package ru.dombuketa.filmslocaror.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.databinding.FragmentSeelaterBinding;
import ru.dombuketa.filmslocaror.utils.AnimationHelper_J;

public class SeelaterFragment_J extends Fragment {
    private FragmentSeelaterBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeelaterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout seelater_fragment_root = requireActivity().findViewById(R.id.seelater_fragment_root);
        AnimationHelper_J.performFragmentCircularRevealAnimation(seelater_fragment_root, requireActivity(),3);

    }
}
