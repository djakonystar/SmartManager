package uz.texnopos.smartmanager.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.smartmanager.core.utils.Resource
import uz.texnopos.smartmanager.data.models.report.Report
import uz.texnopos.smartmanager.data.models.report.ReportDate
import uz.texnopos.smartmanager.data.models.user.Supervisor
import uz.texnopos.smartmanager.data.remote.ApiInterface
import uz.texnopos.smartmanager.settings.Settings

class ReportViewModel(private val api: ApiInterface, private val settings: Settings) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var mutableReports: MutableLiveData<Resource<List<Report>>> = MutableLiveData()
    val reports: LiveData<Resource<List<Report>>> = mutableReports

    private var mutableSupervisors: MutableLiveData<Resource<List<Supervisor>>> = MutableLiveData()
    val supervisors: LiveData<Resource<List<Supervisor>>> = mutableSupervisors

    private var mutableSupervisorsReports: MutableLiveData<Resource<List<Report>>> =
        MutableLiveData()
    val supervisorsReports: LiveData<Resource<List<Report>>> = mutableSupervisorsReports

    fun getReports(date: ReportDate) {
        mutableReports.value = Resource.loading()
        compositeDisposable.add(
            api.getReportsByDate("Bearer ${settings.token}", date.start, date.end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableReports.value = Resource.success(response.payload!!)
                        } else {
                            mutableReports.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableReports.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun getSupervisors() {
        mutableSupervisors.value = Resource.loading()
        compositeDisposable.add(
            api.getSupervisors("Bearer ${settings.token}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableSupervisors.value = Resource.success(response.payload!!)
                        } else {
                            mutableSupervisors.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableSupervisors.value = Resource.error(error.message)
                    }
                )
        )
    }

    fun getSupervisorsReports(id: Int, date: ReportDate) {
        mutableSupervisorsReports.value = Resource.loading()
        compositeDisposable.add(
            api.getSupervisorsReports("Bearer ${settings.token}", id, date.start, date.end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.success) {
                            mutableSupervisorsReports.value = Resource.success(response.payload!!)
                        } else {
                            mutableSupervisorsReports.value = Resource.error(response.message)
                        }
                    },
                    { error ->
                        mutableSupervisorsReports.value = Resource.error(error.message)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
