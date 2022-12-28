package com.pactsafe.pactsafeandroidsdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.pactsafe.pactsafeandroidsdk.data.*
import com.pactsafe.pactsafeandroidsdk.di.getAppRetrofitApi
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.models.PSSigner
import com.pactsafe.pactsafeandroidsdk.models.PSSignerID
import com.pactsafe.pactsafeandroidsdk.ui.PSClickWrapActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response

object PSApp {
    val TAG = "PSApp"
    private var siteAccessId: String? = null
    private lateinit var appContext: Context
    private var testData: Boolean = false
    private var groupKey: String = ""
    private val disposables = CompositeDisposable()
    var preload: Boolean = false
    var hasPreloaded: Boolean = false
    var hasPreloadedObservable: BehaviorSubject<PSGroup> = BehaviorSubject.create()
    var debug: Boolean = false

    fun init(
        siteAccessId: String,
        groupKey: String,
        context: Context,
        debug: Boolean = false,
        testData: Boolean = false
    ) {
        this.siteAccessId = siteAccessId
        this.groupKey = groupKey
        this.appContext = context
        this.debug = debug
        this.testData = testData
    }

    fun preload() {
        val appPreferences: ApplicationPreferences = ApplicationPreferencesImp(appContext)
        appPreferences.psGroupKey = groupKey
        appPreferences.siteAccessId = siteAccessId
        preload = true

        val activityService: ActivityService = ActivityServiceImp(appContext, getAppRetrofitApi())

        siteAccessId?.let { siteAccessId ->
            disposables.add(
                activityService.preloadActivity(groupKey, siteAccessId)
                    .subscribe({
                        setPreLoaded(it)
                    }, {
                        handleThrowable(it)
                    })
            )
        } ?: Log.e(TAG,"Site Access Id is empty.")

    }

    fun clearPSApp() {
        val appPreferences: ApplicationPreferences = ApplicationPreferencesImp(appContext)
        appPreferences.group = null
    }

    private fun loadGroupData(): PSGroup? {
        val applicationPreferences: ApplicationPreferences = ApplicationPreferencesImp(appContext)
        val activityService: ActivityService =  ActivityServiceImp(appContext, getAppRetrofitApi())

        if (applicationPreferences.group == null) {
            val group = activityService.loadActivity(groupKey, siteAccessId ?: "")
            applicationPreferences.group = group
            return group
        }
        return applicationPreferences.group
    }

    fun loadAcceptanceLanguage(contracts: Map<String, Boolean> = emptyMap()): String {

        val groupData = loadGroupData()
        val acceptanceLanguage = groupData?.acceptance_language?.replace("{{contracts}}", "")

        return if (contracts.isEmpty()) {
            acceptanceLanguage + groupData.let { group ->
                group?.contract_data?.values?.joinToString(" and ") { "##${it.title}##" }
            }
        } else {
            acceptanceLanguage + groupData?.contract_data?.filter { (key, _) ->
                contracts[key] == false
            }?.values?.joinToString(" and ") { "##${it.title}##" }
        }
    }

    fun loadAlertMessage(): String {
        return loadGroupData()?.alert_message ?: ""
    }

    fun getContractLinkClickedList(context: Context, contracts: Map<String, Boolean> = emptyMap()): List<() -> Unit> {

        val contractData = loadGroupData()?.let {
            if (contracts.isNotEmpty()) {
                it.contract_data.filter { (key, _) ->
                    contracts[key] == false
                }
            } else {
                it.contract_data
            }
        } ?: emptyMap()

        return contractData.values.map {
            {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${loadGroupData()?.legal_center_url}#${it.key}")
                    )
                )
            }
        }
    }

    fun getContractLinks(context: Context, contracts: Map<String, Boolean> = emptyMap()): List<() -> Unit> {
        val activity = context as PSClickWrapActivity

        val contractData = loadGroupData()?.let {
            if (contracts.isNotEmpty()) {
                it.contract_data.filter { (key, _) ->
                    contracts[key] == false
                }
            } else {
                it.contract_data
            }
        } ?: emptyMap()

        return contractData.values.map {
            { activity.onContractLinkClicked(it.title, "${loadGroupData()?.legal_center_url}#${it.key}") }
        }
    }

    private fun setPreLoaded(psGroup: PSGroup) {
        val appPreferences: ApplicationPreferences = ApplicationPreferencesImp(appContext)
        appPreferences.group = psGroup
        hasPreloaded = true
        hasPreloadedObservable.onNext(psGroup)
    }

    fun endSubscriptions() {
        disposables.clear()
    }

    fun sendAgreed(signer: PSSigner, et: String): Single<Response<Unit>> {
        val activityService: ActivityService =  ActivityServiceImp(appContext, getAppRetrofitApi())
        return activityService.sendActivity(signer, loadGroupData(), et)
    }

    fun sendActivity(signer: PSSigner, et: String): Single<Boolean> {
        val activityService: ActivityService =  ActivityServiceImp(appContext, getAppRetrofitApi())
        return activityService.sendActivity(signer, loadGroupData(), et)
            .map { it.isSuccessful }
    }

    fun fetchSignedStatus(signer: PSSignerID): Single<Map<String, Boolean>> {
        val activityService: ActivityService =  ActivityServiceImp(appContext, getAppRetrofitApi())
        return activityService.fetchSignedStatus(signer, loadGroupData()).map { it.body() }
    }

    fun updatedTermsLanguage(contracts: Map<String, Boolean>): CharSequence? {

        val updateLanguage = "We've updated the following: "

        val contractData = loadGroupData()?.let {
            if (contracts.isNotEmpty()) {
                it.contract_data.filter { (key, _) ->
                    contracts[key] == false
                }
            } else {
                it.contract_data
            }
        } ?: emptyMap()

        return updateLanguage + contractData.map { it.value.title }.joinToString(", ") { it }
    }
}

object PSLogger {
    fun debugLog(tag: String, message: String?) {
        if (PSApp.debug) {
            Log.d(tag, message)
        }
    }

    fun errorLog(tag: String, throwable: Throwable, message: String?) {
        Log.e(tag, message, throwable)
    }

    fun warningLog(tag: String, message: String?) {
        Log.w(tag, message)
    }
}