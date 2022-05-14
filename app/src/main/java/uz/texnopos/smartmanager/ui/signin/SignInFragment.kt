package uz.texnopos.smartmanager.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.extensions.showError
import uz.texnopos.smartmanager.core.extensions.showMessage
import uz.texnopos.smartmanager.core.utils.ResourceState
import uz.texnopos.smartmanager.data.models.signin.SignIn
import uz.texnopos.smartmanager.databinding.FragmentSigninBinding
import uz.texnopos.smartmanager.settings.Settings

class SignInFragment : Fragment(R.layout.fragment_signin) {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var navController: NavController
    private val viewModel: SignInViewModel by viewModel()
    private val settings: Settings by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSigninBinding.bind(view)
        navController = findNavController()

        binding.apply {
            etUsername.addTextChangedListener { tilUsername.isErrorEnabled = false }
            etPassword.addTextChangedListener { tilPassword.isErrorEnabled = false }

            btnSignIn.onClick {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signIn(SignIn(username, password))
                } else {
                    if (username.isEmpty()) {
                        tilUsername.error = getString(R.string.required_field)
                    }
                    if (password.isEmpty()) {
                        tilPassword.error = getString(R.string.required_field)
                    }
                }
            }
        }

        setUpObservers()
    }

    private fun setLoading(loading: Boolean) {
        binding.apply {
            progressCircular.isVisible = loading
            tilUsername.isEnabled = !loading
            tilPassword.isEnabled = !loading
            btnSignIn.isEnabled = !loading
        }
    }

    private fun setUpObservers() {
        viewModel.signIn.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data?.let { token ->
                        settings.token = token
                        settings.signedIn = true
                        navController.navigate(R.id.action_signInFragment_to_mainFragment)
                        TODO("navigate to main")
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
