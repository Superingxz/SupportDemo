package os.imlive.demo

import com.bumptech.glide.request.target.ViewTarget
import os.imlive.sdk.FloatingApplication

/**
 * <pre>
 *     author: myz
 *     email : moyaozhi@imdawei.com
 *     time  : 2021/4/6 18:16
 *     desc  :
 * </pre>
 */
class App : FloatingApplication() {
    override fun onCreate() {
        super.onCreate()
        ViewTarget.setTagId(R.id.glide_view_tag)
    }
}
