/******************************************************************************
 *
 *  2015 (C) Copyright Open-RnD Sp. z o.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package pl.openrnd.managers.fragmentsswapper;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import pl.openrnd.utils.ViewUtils;

/**
 * Base implementation of fragment that is supported by FragmentSwapper.
 */
public class BaseFragment extends Fragment implements FragmentDescriptor {

    private FragmentDescriptorImpl mFragmentDescriptor;

    /**
     * Base fragment constructor.
     */
    public BaseFragment() {
        mFragmentDescriptor = new FragmentDescriptorImpl(this);
    }

    /**
     * @see android.support.v4.app.Fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentDescriptor.onCreate(savedInstanceState);
    }

    /**
     * @see android.support.v4.app.Fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mFragmentDescriptor.onSaveInstanceState(outState);
    }

    /**
     * @see android.support.v4.app.Fragment
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mFragmentDescriptor.onAttach(activity);
    }

    /**
     * @see android.support.v4.app.Fragment
     *
     * Method checks if animations are currently enabled in FragmentSwapper that
     * the fragment is attache to. If animations are disabled empty animation is
     * returned.
     */
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        FragmentSwapper fragmentSwapper = mFragmentDescriptor.getFragmentSwapper();

        if ((fragmentSwapper == null) || fragmentSwapper.isAnimationEnabled()) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        } else {
            Animation animation = new Animation() {};
            animation.setDuration(0);
            return animation;
        }
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void assignFragmentSwapper(FragmentSwapper fragmentSwapper) {
        mFragmentDescriptor.assignFragmentSwapper(fragmentSwapper);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public FragmentSwapper getFragmentSwapper() {
        return mFragmentDescriptor.getFragmentSwapper();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getName() {
        return mFragmentDescriptor.getName();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Integer getRequestCode() {
        return mFragmentDescriptor.getRequestCode();
    }

    /**
     * Sets fragments result code and data.
     *
     * @param resultCode Result code.
     * @param resultData Bundle object containing fragment result data.
     */
    protected void setResult(int resultCode, Bundle resultData) {
        mFragmentDescriptor.setResult(resultCode, resultData);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public int getResultCode() {
        return mFragmentDescriptor.getResultCode();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Bundle getResultData() {
        return mFragmentDescriptor.getResultData();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void setRequestCode(Integer requestCode) {
        mFragmentDescriptor.setRequestCode(requestCode);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentResult(Integer requestCode, int resultCode, Bundle data) {
        mFragmentDescriptor.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentPause() {

    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentResume() {
        ViewUtils.hideSoftKeyboard(getContextActivity());
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onBackPressed(PopParams popParams) {
        mFragmentDescriptor.onBackPressed(popParams);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Activity getContextActivity() {
        return mFragmentDescriptor.getContextActivity();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getContextString(int resId) {
        return mFragmentDescriptor.getContextString(resId);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getContextString(int resId, Object... formatArgs) {
        return mFragmentDescriptor.getContextString(resId, formatArgs);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Resources getContextResources() {
        return mFragmentDescriptor.getContextResources();
    }
}
