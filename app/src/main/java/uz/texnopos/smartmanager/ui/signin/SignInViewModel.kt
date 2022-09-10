package uz.texnopos.smartmanager.ui.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.smartmanager.core.utils.Resource
import uz.texnopos.smartmanager.data.models.GenericResponse
import uz.texnopos.smartmanager.data.models.signin.SignIn
import uz.texnopos.smartmanager.data.models.signin.SignInPost
import uz.texnopos.smartmanager.data.remote.ApiInterface

class SignInViewModel(private val api: ApiInterface, application: Application) :
    AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private var mutableSignIn: MutableLiveData<Resource<GenericResponse<SignIn>>> = MutableLiveData()
    val signIn: LiveData<Resource<GenericResponse<SignIn>>> = mutableSignIn

    fun signIn(signIn: SignInPost) {
        mutableSignIn.value = Resource.loading()
        compositeDisposable.add(
            api.signIn(signIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableSignIn.value = Resource.success(response)
                        } else {
                            mutableSignIn.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableSignIn.value = Resource.error(error.message)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
