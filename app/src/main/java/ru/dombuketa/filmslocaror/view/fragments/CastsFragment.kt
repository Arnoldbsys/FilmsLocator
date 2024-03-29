package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.dombuketa.filmslocaror.databinding.FragmentCastsBinding
import ru.dombuketa.filmslocaror.utils.AnimationHelper


class CastsFragment : Fragment() {
    private lateinit var binding: FragmentCastsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCastsBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.castsFragmentRoot, requireActivity(),4)

    }
}