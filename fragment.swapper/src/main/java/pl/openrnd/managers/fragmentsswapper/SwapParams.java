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

import pl.openrnd.managers.R;

/**
 * Class holding fragment enter transaction parameters.
 */
public class SwapParams {
    private Integer mRequestCode;
    private boolean mAddToBackStack;
    private boolean mAnimate;
    private Integer mEnterAnimResId;
    private Integer mExitAnimResId;
    private Integer mPopEnterAnimResId;
    private Integer mPopExitAnimResId;
    private boolean mIsMainContext;
    private boolean mRemoveOld;

    /**
     * Gets animation resource id.
     *
     * @see android.support.v4.app.FragmentTransaction
     *
     * @return Animation resource id.
     */
    public Integer getPopExitAnimResId() {
        return mPopExitAnimResId;
    }

    /**
     * Gets fragment request code.
     *
     * @return Fragment request code or null if not provided.
     */
    public Integer getRequestCode() {
        return mRequestCode;
    }

    /**
     * Gets information if new fragment has to be added to FragmentManagers
     * back stack.
     *
     * @return True if fragment has to be added to back stack, false otherwise.
     */
    public boolean isAddToBackStack() {
        return mAddToBackStack;
    }

    /**
     * Gets information if animation will be enabled for the fragments transaction.
     *
     * @return True if animation is enabled, false otherwise.
     */
    public boolean isAnimate() {
        return mAnimate;
    }

    /**
     * Gets animation resource id.
     *
     * @see android.support.v4.app.FragmentTransaction
     *
     * @return Animation resource id.
     */
    public Integer getEnterAnimResId() {
        return mEnterAnimResId;
    }

    /**
     * Gets animation resource id.
     *
     * @see android.support.v4.app.FragmentTransaction
     *
     * @return Animation resource id.
     */
    public Integer getExitAnimResId() {
        return mExitAnimResId;
    }

    /**
     * Gets animation resource id.
     *
     * @see android.support.v4.app.FragmentTransaction
     *
     * @return Animation resource id.
     */
    public Integer getPopEnterAnimResId() {
        return mPopEnterAnimResId;
    }

    /**
     * Gets information if new fragment will be a main context.
     *
     * When new fragment in transaction is marked as a main context,
     * all previously added fragments will be removed from back stack.
     * User will not be able to go back to them.
     *
     * @return True if new fragment will be a main context.
     */
    public boolean isMainContext() {
        return mIsMainContext;
    }

    /**
     * Gets information about current fragment hide method.
     *
     * Current fragment can be hidden and removed. While going back to it, it will be shown again or
     * recreated from scratch.
     *
     * @return True if fragment has to be removed, false if fragment has to be hidden.
     */
    public boolean getRemoveOld(){
        return mRemoveOld;
    }

    /**
     * SwapParams class builder
     */
    public SwapParams(Builder builder) {
        mRequestCode = builder.mRequestCode;
        mAddToBackStack = builder.mAddToBackStack;
        mAnimate = builder.mAnimate;
        mEnterAnimResId = builder.mEnterAnimResId;
        mExitAnimResId = builder.mExitAnimResId;
        mPopEnterAnimResId = builder.mPopEnterAnimResId;
        mPopExitAnimResId = builder.mPopExitAnimResId;
        mIsMainContext = builder.mIsMainContext;
        mRemoveOld = builder.mRemoveOld;
    }

    /**
     * SwapParams class builder
     */
    public static class Builder {
        private Integer mRequestCode;
        private boolean mAddToBackStack;
        private boolean mAnimate;
        private Integer mEnterAnimResId;
        private Integer mExitAnimResId;
        private Integer mPopEnterAnimResId;
        private Integer mPopExitAnimResId;
        private boolean mIsMainContext;
        private boolean mRemoveOld;

        /**
         * Class constructor
         */
        public Builder() {
            initDefaultValues();
        }

        private void initDefaultValues() {
            mEnterAnimResId = R.anim.slide_left_out;
            mExitAnimResId = R.anim.slide_right_out;
            mPopEnterAnimResId = R.anim.slide_left_in;
            mPopExitAnimResId = R.anim.slide_right_in;
            mAnimate = true;
            mRequestCode = null;
            mAddToBackStack = true;
            mIsMainContext = false;
            mRemoveOld = false;
        }

        /**
         * Sets adding new fragment to back stack state.
         *
         * @param addToBackStack True if new fragment has to be added to back stack, false otherwise.
         * @return Builder object.
         */
        public Builder addToBackStack(boolean addToBackStack) {
            mAddToBackStack = addToBackStack;
            return this;
        }

        /**
         * Sets new fragment request code.
         *
         * @param requestCode Fragment request code.
         * @return Builder object.
         */
        public Builder requestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        /**
         * Sets transaction animate state.
         *
         * @param animate True if transactions animation is enabled, false otherwise.
         * @return Builder object.
         */
        public Builder animate(boolean animate){
            mAnimate = animate;
            return this;
        }

        /**
         * Sets animation resource id.
         *
         * @see android.support.v4.app.FragmentTransaction
         *
         * @param enterAnimResId Animation resource id.
         * @return Builder object.
         */
        public Builder enterAnimResId(int enterAnimResId){
            mEnterAnimResId = enterAnimResId;
            return this;
        }

        /**
         * Sets animation resource id.
         *
         * @see android.support.v4.app.FragmentTransaction
         *
         * @param exitAnimResId Animation resource id.
         * @return Builder object.
         */
        public Builder exitAnimResId(int exitAnimResId){
            mExitAnimResId = exitAnimResId;
            return this;
        }

        /**
         * Sets animation resource id.
         *
         * @see android.support.v4.app.FragmentTransaction
         *
         * @param popEnterAnimResId Animation resource id.
         * @return Builder object.
         */
        public Builder popEnterAnimResId(int popEnterAnimResId){
            mPopEnterAnimResId = popEnterAnimResId;
            return this;
        }

        /**
         * Sets animation resource id.
         *
         * @see android.support.v4.app.FragmentTransaction
         *
         * @param popExitAnimResId Animation resource id.
         * @return Builder object.
         */
        public Builder popExitAnimResId(int popExitAnimResId){
            mPopExitAnimResId = popExitAnimResId;
            return this;
        }

        /**
         * Sets information if new fragment will be a main context.
         *
         * When new fragment in transaction is marked as a main context,
         * all previously added fragments will be removed from back stack.
         * User will not be able to go back to them.
         *
         * @param mainContext True if new fragment is a main context fragment, false otherwise.
         * @return Builder object.
         */
        public Builder mainContext(boolean mainContext) {
            mIsMainContext = mainContext;
            return this;
        }

        /**
         * Sets information about current fragment hide method.
         *
         * Current fragment can be hidden and removed. While going back to it, it will be shown again or
         * recreated from scratch.
         *
         * @param removeOld True if current fragment has to be removed, false if has to be hidden.
         * @return Builder object.
         */
        public Builder removeOld(boolean removeOld){
            mRemoveOld = removeOld;
            return this;
        }

        /**
         * Builds SwapParams object with current builder parameters.
         *
         * @return SwapParams object.
         */
        public SwapParams build() {
            SwapParams swapParams = new SwapParams(this);
            return swapParams;
        }
    }
}

