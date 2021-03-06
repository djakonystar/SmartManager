package uz.texnopos.smartmanager.ui.admin

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.extensions.showError
import uz.texnopos.smartmanager.core.extensions.showSuccess
import uz.texnopos.smartmanager.core.extensions.showWarning
import uz.texnopos.smartmanager.core.utils.ResourceState
import uz.texnopos.smartmanager.databinding.FragmentAdminBinding
import uz.texnopos.smartmanager.settings.Settings
import uz.texnopos.smartmanager.ui.dialog.AddAdminDialog
import uz.texnopos.smartmanager.ui.dialog.EditAdminDialog

class AdminFragment : Fragment(R.layout.fragment_admin) {
    private lateinit var binding: FragmentAdminBinding
    private lateinit var navController: NavController
    private lateinit var editAdminDialog: EditAdminDialog
    private val addAdminDialog: AddAdminDialog by lazy { AddAdminDialog() }
    private val viewModel: AdminViewModel by viewModel()
    private val adapter: AdminAdapter by inject()
    private val settings: Settings by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAdminBinding.bind(view)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        binding.apply {
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                setLoading(false)
                viewModel.getAdmins()
            }

            recyclerView.adapter = adapter
            adapter.setOnEditClickListener { admin ->
                editAdminDialog = EditAdminDialog(admin)
                editAdminDialog.setOnPositiveButtonClickListener { adminPost ->
                    viewModel.editAdmin(admin.id, adminPost)

                    viewModel.editAdmin.observe(viewLifecycleOwner) {
                        when (it.status) {
                            ResourceState.LOADING -> setLoading(true)
                            ResourceState.SUCCESS -> {
                                setLoading(false)
                                showSuccess(getString(R.string.admin_edited_successfully))
                                    .setOnDismissListener {
                                        editAdminDialog.dismiss()
                                        viewModel.getAdmins()
                                    }
                            }
                            ResourceState.ERROR -> {
                                setLoading(false)
                                showError(it.message)
                            }
                        }
                    }
                }
                editAdminDialog.show(requireActivity().supportFragmentManager, editAdminDialog.tag)
            }

            adapter.setOnDeleteClickListener { admin ->
                showWarning(getString(R.string.admin_delete_message, admin.username))
                    .setOnPositiveButtonClickListener {
                        viewModel.deleteAdmin(admin.id)

                        viewModel.deleteAdmin.observe(viewLifecycleOwner) {
                            when (it.status) {
                                ResourceState.LOADING -> setLoading(true)
                                ResourceState.SUCCESS -> {
                                    setLoading(false)
                                    showSuccess(getString(R.string.admin_deleted_successfully))
                                        .setOnDismissListener {
                                            viewModel.getAdmins()
                                        }
                                }
                                ResourceState.ERROR -> {
                                    setLoading(false)
                                    showError(it.message)
                                }
                            }
                        }
                    }
            }

            fabAddAdmin.onClick {
                fabAddAdmin.isEnabled = false
                addAdminDialog.show(requireActivity().supportFragmentManager, addAdminDialog.tag)
                addAdminDialog.setOnPositiveButtonClickListener { adminPost ->
                    viewModel.addAdmin(adminPost)

                    viewModel.addAdmin.observe(viewLifecycleOwner) {
                        when (it.status) {
                            ResourceState.LOADING -> setLoading(true)
                            ResourceState.SUCCESS -> {
                                setLoading(false)
                                showSuccess(getString(R.string.admin_added_successfully))
                                    .setOnDismissListener {
                                        addAdminDialog.dismiss()
                                        viewModel.getAdmins()
                                    }
                            }
                            ResourceState.ERROR -> {
                                setLoading(false)
                                showError(it.message)
                            }
                        }
                    }
                }
                addAdminDialog.setOnDismissListener { fabAddAdmin.isEnabled = true }
            }

            toolbar.setOnMenuItemClickListener {
                settings.signedIn = false
                navController.navigate(R.id.action_mainFragment_to_signInFragment)
                true
            }
        }

        viewModel.getAdmins()
        setUpObservers()
    }

    override fun onDetach() {
        adapter.models = listOf()
        super.onDetach()
    }

    private fun setLoading(loading: Boolean) {
        binding.apply {
            progressCircular.isVisible = loading
            swipeRefresh.isEnabled = !loading
            recyclerView.isEnabled = !loading
            fabAddAdmin.isEnabled = !loading
        }
    }

    private fun setUpObservers() {
        viewModel.admins.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data?.let { admins ->
                        adapter.models = admins
                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }
    }
}
