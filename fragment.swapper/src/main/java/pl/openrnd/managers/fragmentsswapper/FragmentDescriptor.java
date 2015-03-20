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

/**
 * Interface that has to be implemented by fragments
 * managed by FragmentSwapper
 */
public interface FragmentDescriptor {
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    public static final int RESULT_FIRST_USER = Activity.RESULT_FIRST_USER;

    /**
     * Gets fragment name.
     *
     * @return Fragment name.
     */
    String getName();

    /**
     * Gets request code that the fragment was started with.
     *
     * @return Fragments request code or null if fragment was not started for result.
     */
    Integer getRequestCode();

    /**
     * Gets fragments result code.
     *
     * @return Fragments result code. RESULT_CANCELED is returned if fragment was canceled.
     */
    int getResultCode();

    /**
     * Gets Bundle object with fragment result data.
     *
     * @return Bundle object or null if not provided while fragment closing.
     */
    Bundle getResultData();

    /**
     * Sets fragment request code.
     *
     * To be used only by FragmentSwapper while Fragment creation.
     *
     * @param requestCode Fragment request code.
     */
    void setRequestCode(Integer requestCode);

    /**
     * Method called when the fragment that was started is exiting.
     *
     * The request code, result code and result data that was provided while
     * fragment closing are passed to the fragment that previously started it.
     *
     * @param requestCode Fragments request code or null if fragment was not started with request code.
     * @param resultCode Fragments result code.
     * @param data Bundle object containing fragments result data or null if not provided.
     */
    void onFragmentResult(Integer requestCode, int resultCode, Bundle data);

    /**
     * Method called when the fragment is paused.
     */
    void onFragmentPause();

    /**
     * Method called when the fragment is resumed.
     */
    void onFragmentResume();

    /**
     * Assigns FragmentSwapper that the fragment is attached to.
     *
     * Method to be used only by FragmentSwapper.
     *
     * @param fragmentSwapper Attached FragmentSwapper.
     */
    void assignFragmentSwapper(FragmentSwapper fragmentSwapper);

    /**
     * Gets FragmentSwapper that the fragment is attached to.
     *
     * @return FragmentSwapper that the fragment is attached to.
     */
    FragmentSwapper getFragmentSwapper();

    /**
     * Method to be called by the fragment when its close is requested.
     *
     * @param popParams PopParams object with fragment swapping transaction parameters.
     */
    void onBackPressed(PopParams popParams);

    /**
     * Gets Activity that the fragment is/was attached to.
     *
     * @return Activity that the fragment is/was attached to.
     */
    Activity getContextActivity();

    /**
     * Gets string from the application's resources.
     *
     * @see android.app.Activity
     *
     * @return Localized formatted string.
     */
    String getContextString(int resId, Object... formatArgs);

    /**
     * Gets string from the application's resources.
     *
     * @see android.app.Activity
     *
     * @return Localized formatted string.
     */
    String getContextString(int resId);

    /**
     * Gets Resources instance of the application.
     * @return Resources instance of the application.
     */
    Resources getContextResources();
}
