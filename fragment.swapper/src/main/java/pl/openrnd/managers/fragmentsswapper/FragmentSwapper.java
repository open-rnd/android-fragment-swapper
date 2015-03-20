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
 * Interface defining base FragmentSwapper API.
 *
 * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
 * @see android.support.v4.app.Fragment
 *
 * @param <F> Fragment class that is supported by FragmentSwapper.
 */
public interface FragmentSwapper<F extends Fragment & FragmentDescriptor> {

    /**
     * Gets information if fragments transactions animations are enabled.
     *
     * @return True if animations are enabled, false otherwise.
     */
    boolean isAnimationEnabled();

    /**
     * Method that must be called by the holding Activity in its onPause() method.
     *
     * @see android.app.Activity
     */
    void onPause();

    /**
     * Method that must be called by the holding Activity in its onResume() method.
     *
     * @see android.app.Activity
     */
    void onResume();

    /**
     * Method starting fragment enter transaction.
     *
     * @param swapParams SwapParams object with transaction parameters.
     * @param fragment Fragment to be entered.
     */
    void swapFragment(SwapParams swapParams, F fragment);

    /**
     * Method starting fragment popping transaction.
     *
     * @param popParams PopParams object with transaction parameters.
     */
    void popFragment(PopParams popParams);

    /**
     * Gets current fragment
     *
     * @return Current fragment
     */
    F getCurrentFragment();
}
