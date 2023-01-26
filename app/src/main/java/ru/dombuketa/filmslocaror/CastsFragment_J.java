package ru.dombuketa.filmslocaror;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ru.dombuketa.filmslocaror.databinding.FragmentCastsBinding;

public class CastsFragment_J extends Fragment {
    private FragmentCastsBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCastsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ConstraintLayout casts_fragment_root = requireActivity().findViewById(R.id.casts_fragment_root);
        AnimationHelper_J.performFragmentCircularRevealAnimation(binding.castsFragmentRoot, requireActivity(),4);

    }
}
