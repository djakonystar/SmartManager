package uz.texnopos.smartmanager.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.smartmanager.core.utils.Resource
import uz.texnopos.smartmanager.data.models.GenericResponse
import uz.texnopos.smartmanager.data.models.bot.EditChatId
import uz.texnopos.smartmanager.data.models.bot.EditTime
import uz.texnopos.smartmanager.data.models.bot.Rule
import uz.texnopos.smartmanager.data.remote.ApiInterface
import uz.texnopos.smartmanager.settings.Settings

class SettingsViewModel(private val api: ApiInterface, private val settings: Settings) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var mutableRules: MutableLiveData<Resource<GenericResponse<List<Rule>>>> = MutableLiveData()
    val rules: LiveData<Resource<GenericResponse<List<Rule>>>> = mutableRules

    private var mutableEditTime: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val editTime: LiveData<Resource<Any?>> = mutableEditTime

    private var mutableEditChatId: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val editChatId: LiveData<Resource<Any?>> = mutableEditChatId

    fun getRules() {
        mutableRules.value = Resource.loading()
        compositeDisposable.add(
            api.getRule("Bearer ${settings.token}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableRules.value = Resource.success(response)
                        } else {
                            mutableRules.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableRules.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun editTime(time: EditTime) {
        mutableEditTime.value = Resource.loading()
        compositeDisposable.add(
            api.editTime("Bearer ${settings.token}", time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableEditTime.value = Resource.success(response.payload)
                        } else {
                            mutableEditTime.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableEditTime.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun editChatId(chatId: EditChatId) {
        mutableEditChatId.value = Resource.loading()
        compositeDisposable.add(
            api.editChatId("Bearer ${settings.token}", chatId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableEditChatId.value = Resource.success(response.payload)
                        } else {
                            mutableEditChatId.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableEditChatId.value = Resource.error(error.message)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}