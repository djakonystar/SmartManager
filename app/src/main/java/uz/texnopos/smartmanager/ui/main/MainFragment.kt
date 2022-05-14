package uz.texnopos.smartmanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.showError
import uz.texnopos.smartmanager.databinding.FragmentMainBinding
import uz.texnopos.smartmanager.settings.Settings

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private lateinit var childNavController: NavController
    private val settings: Settings by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        childNavController =
            (childFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController

        binding.apply {
            bottomNav.setupWithNavController(childNavController)

            if (settings.role == "ADMIN") {
                bottomNav.menu.getItem(1).isEnabled = false
                bottomNav.menu.getItem(1).setOnMenuItemClickListener {
                    showError(getString(R.string.error_not_allowed))
                    return@setOnMenuItemClickListener true
                }
            }
        }
    }
}