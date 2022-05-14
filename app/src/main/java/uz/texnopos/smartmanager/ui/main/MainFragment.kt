package uz.texnopos.smartmanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private lateinit var childNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        childNavController = Navigation.findNavController(binding.mainNavHostFragment)

        binding.apply {
            bottomNav.setupWithNavController(childNavController)
        }
    }
}