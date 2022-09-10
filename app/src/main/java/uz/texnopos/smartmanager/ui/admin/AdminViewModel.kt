package uz.texnopos.smartmanager.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.smartmanager.core.utils.Resource
import uz.texnopos.smartmanager.data.models.user.Admin
import uz.texnopos.smartmanager.data.models.user.AdminPost
import uz.texnopos.smartmanager.data.remote.ApiInterface
import uz.texnopos.smartmanager.settings.Settings

class AdminViewModel(private val api: ApiInterface, private val settings: Settings) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var mutableAdmins: MutableLiveData<Resource<List<Admin>>> = MutableLiveData()
    val admins: LiveData<Resource<List<Admin>>> = mutableAdmins

    private var mutableAddAdmin: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val addAdmin: LiveData<Resource<Any?>> = mutableAddAdmin

    private var mutableEditAdmin: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val editAdmin: LiveData<Resource<Any?>> = mutableEditAdmin

    private var mutableDeleteAdmin: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val deleteAdmin: LiveData<Resource<Any?>> = mutableDeleteAdmin

    fun getAdmins() {
        mutableAdmins.value = Resource.loading()
        compositeDisposable.add(
            api.getAdmins("Bearer ${settings.token}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableAdmins.value = Resource.success(response.payload!!)
                        } else {
                            mutableAdmins.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableAdmins.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun addAdmin(admin: AdminPost) {
        mutableAddAdmin.value = Resource.loading()
        compositeDisposable.add(
            api.addAdmin("Bearer ${settings.token}", admin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableAddAdmin.value = Resource.success(response.payload)
                        } else {
                            mutableAddAdmin.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableAddAdmin.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun editAdmin(id: Int, admin: AdminPost) {
        mutableEditAdmin.value = Resource.loading()
        compositeDisposable.add(
            api.editAdmin("Bearer ${settings.token}", id, admin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableEditAdmin.value = Resource.success(response)
                        } else {
                            mutableEditAdmin.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableEditAdmin.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun deleteAdmin(id: Int) {
        mutableDeleteAdmin.value = Resource.loading()
        compositeDisposable.add(
            api.deleteAdmin("Bearer ${settings.token}", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableDeleteAdmin.value = Resource.success(response.payload)
                        } else {
                            mutableDeleteAdmin.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableDeleteAdmin.value = Resource.error(error.message)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
