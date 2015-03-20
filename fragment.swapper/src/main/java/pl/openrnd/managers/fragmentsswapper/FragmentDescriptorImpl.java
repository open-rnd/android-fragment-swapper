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
import android.util.Log;

/**
 * Base implementation of FragmentDescription.
 *
 * Class to be when 3rd parties fragments have to be managed by FragmentSwapper.
 *
 * @see pl.openrnd.managers.fragmentsswapper.BaseFragment
 */
public class FragmentDescriptorImpl implements FragmentDescriptor {

    private static final String TAG = FragmentDescriptorImpl.class.getSimpleName();

    private static final String DATA_REQUEST_CODE = String.format("%s_%s", TAG, "DATA_REQUEST_CODE");
    private static final String DATA_RESULT_CODE = String.format("%s_%s", TAG, "DATA_RESULT_CODE");
    private static final String DATA_RESULT_BUNDLE = String.format("%s_%s", TAG, "DATA_RESULT_BUNDLE");

    private FragmentSwapper mFragmentSwapper;
    private Activity mActivity;

    private Integer mResultCode;
    private Bundle mResultData;

    private Fragment mFragment;

    /**
     * Class constructor
     *
     * @param fragment Holding fragment
     */
    public FragmentDescriptorImpl(Fragment fragment) {
        mFragment = fragment;
    }

    /**
     * Method must be called in onAttach() method of holding fragment.
     *
     * @see android.support.v4.app.Fragment
     *
     * @param activity Activity that the fragment will be attached to.
     */
    public void onAttach(Activity activity) {
        mActivity = activity;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void assignFragmentSwapper(FragmentSwapper fragmentSwapper) {
        mFragmentSwapper = fragmentSwapper;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public FragmentSwapper getFragmentSwapper() {
        return mFragmentSwapper;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getName() {
        return mFragment.getClass().getSimpleName();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Integer getRequestCode() {
        Integer result = null;

        Bundle arguments = mFragment.getArguments();
        if ((arguments != null) && (arguments.containsKey(DATA_REQUEST_CODE))) {
            result = arguments.getInt(DATA_REQUEST_CODE);
        }

        Log.v(TAG, String.format("getResultData(): result[%s]", result));

        return result;
    }

    /**
     * Method must be called in onCreate() method of holding fragment.
     *
     * @see android.support.v4.app.Fragment
     *
     * @param savedInstanceState Bundle object containing saved state.
     */
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(DATA_REQUEST_CODE)) {
                mResultCode = savedInstanceState.getInt(DATA_REQUEST_CODE, RESULT_CANCELED);
            }

            if (savedInstanceState.containsKey(DATA_RESULT_BUNDLE)) {
                mResultData = savedInstanceState.getBundle(DATA_RESULT_BUNDLE);
            }
        }
    }

    /**
     * Method must be called in onSaveInstanceState() method of holding fragment.
     *
     * @see android.support.v4.app.Fragment
     *
     * @param outState Bundle to store saved state.
     */
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            if (mResultCode != null) {
                outState.putInt(DATA_RESULT_CODE, mResultCode);
            }

            if (mResultData != null) {
                outState.putBundle(DATA_RESULT_BUNDLE, mResultData);
            }
        }
    }

    /**
     * Sets holding fragments result code and data.
     *
     * @param resultCode Result code.
     * @param resultData Bundle object containing fragment results data.
     */
    public void setResult(int resultCode, Bundle resultData) {
        mResultCode = resultCode;
        mResultData = resultData;
    }

    /**
     * Gets holding fragment result code.
     *
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     *
     * @return Holding fragment result code.
     */
    @Override
    public int getResultCode() {
        int result = mResultCode == null ? RESULT_CANCELED : mResultCode;

        Log.v(TAG, String.format("getResultCode(): result[%d]", result));

        return result;
    }

    /**
     * Gets holding fragment result data as a Bundle object.
     *
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     *
     * @return Holding fragment result data as a Bundle object.
     */
    @Override
    public Bundle getResultData() {
        Log.v(TAG, String.format("getResultData(): result[%b]", mResultData != null));

        return mResultData;
    }

    /**
     * Sets holding fragment request code.
     *
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     *
     * @param requestCode Fragment request code.
     */
    @Override
    public void setRequestCode(Integer requestCode) {
        if (requestCode != null) {
            Bundle arguments = mFragment.getArguments();
            if (arguments == null) {
                arguments = new Bundle();
            } else {
                if (arguments.containsKey(DATA_REQUEST_CODE)) {
                    throw new IllegalStateException("Request code key already taken");
                }
            }
            arguments.putInt(DATA_REQUEST_CODE, requestCode);
            mFragment.setArguments(arguments);
        }
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentResult(Integer requestCode, int resultCode, Bundle data) {
        Log.v(TAG, String.format("onFragmentResult(): requestCode[%s], resultCode[%d], data[%b]", requestCode, resultCode, data != null));
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onBackPressed(PopParams popParams) {
        Log.v(TAG, "onBackPressed()");

        if (mFragmentSwapper != null) {
            mFragmentSwapper.popFragment(popParams);
        }
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

    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Activity getContextActivity() {
        return mActivity;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getContextString(int resId) {
        return mActivity.getString(resId);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public String getContextString(int resId, Object... formatArgs) {
        return mActivity.getString(resId, formatArgs);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public Resources getContextResources() {
        return mActivity.getResources();
    }
}
