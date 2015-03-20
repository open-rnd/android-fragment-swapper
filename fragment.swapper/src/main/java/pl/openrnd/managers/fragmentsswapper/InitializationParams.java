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

import android.support.v4.app.FragmentManager;

/**
 * FragmentSwapper initialization parameters class.
 */
public class InitializationParams {
    private FragmentManager mFragmentManager;
    private int mContentFrame;
    private ScreenManager mScreenManager;

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public int getContentFrame() {
        return mContentFrame;
    }

    public ScreenManager getScreenManager() {
        return mScreenManager;
    }

    private InitializationParams(Builder builder) {
        mFragmentManager = builder.mFragmentManager;
        mContentFrame = builder.mContentFrame;
        mScreenManager = builder.mScreenManager;
    }

    /**
     * Parameters builder class.
     */
    public static class Builder {
        private FragmentManager mFragmentManager;
        private Integer mContentFrame;
        private ScreenManager mScreenManager;

        /**
         * Sets FragmentManager associated with the FragmentSwapper context Activity.
         *
         * Parameter required.
         *
         * @param fragmentManager FragmentManager
         * @return Builder object
         */
        public Builder fragmentManager(FragmentManager fragmentManager) {
            mFragmentManager = fragmentManager;
            return this;
        }

        /**
         * Sets container Id that will be used in fragments transactions by the FragmentSwapper.
         *
         * Parameter required.
         *
         * @param contentFrame Container Id
         * @return Builder object
         */
        public Builder contentFrame(int contentFrame) {
            mContentFrame = contentFrame;
            return this;
        }

        /**
         * Sets ScreenManager object with applications screens triggering routines.
         *
         * Parameter required.
         *
         * @param screenManager ScreenManager object
         * @return Builder object
         */
        public Builder screenManager(ScreenManager screenManager) {
            mScreenManager = screenManager;
            return this;
        }

        /**
         * Builds InitializationParams object with current arguments state.
         *
         * @throws IllegalStateException The exception is thrown when required parameter is not provided.
         *
         * @return InitializationParams object.
         */
        public InitializationParams build() {
            if ((mFragmentManager == null)
                || (mContentFrame == null)
                || (mScreenManager == null)) {
                throw new IllegalStateException("All parameters are mandatory");
            }

            return new InitializationParams(this);
        }
    }
}
