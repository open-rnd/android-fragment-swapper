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

import android.support.v4.app.Fragment;

/**
 * Interface used for getting notification about state and requests from FragmentSwapper.
 */
public interface OnFragmentSwapperListener {

    /**
     * Method called when new fragment enters.
     *
     * This method can be used for updating UI according to new fragment.
     *
     * @param fragmentSwapper Notification sender.
     * @param fragment New fragment instance.
     */
    void onFragmentEntered(FragmentSwapper fragmentSwapper, Fragment fragment);

    /**
     * Method called when last fragment was popped.
     *
     * This notification can be used for requesting application close.
     *
     * @param fragmentSwapper Notification sender.
     */
    void onCloseRequested(FragmentSwapper fragmentSwapper);
}
