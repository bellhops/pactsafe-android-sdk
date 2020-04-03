package com.pactsafe.pactsafeandroidsdk.data

import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.models.PSSigner
import com.pactsafe.pactsafeandroidsdk.util.injector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ActivityServiceImp(private val activityAPI: ActivityAPI) :
    ActivityService {

    override fun preloadActivity(groupKey: String, siteAccessKey: String): Single<PSGroup> {

        return activityAPI.preload(siteAccessKey, groupKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.body() }
    }

    override fun sendActivity(signer: PSSigner, group: PSGroup?): Single<Response<Unit>> {

        val applicationPreferences: ApplicationPreferences = injector()
        return activityAPI.sendActivity(
            mapOf(
                "cid" to group?.contract_ids,
                "vid" to group?.contract_versions,
                "sig" to signer.signerId,
                "et" to group?.type,
                "cus" to signer.customData.toString(),
                "gid" to group?.id.toString(),
                "cnf" to group?.confirmation_email.toString(),
                "tm" to false.toString(),
                "sid" to applicationPreferences.siteAccessId
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

}