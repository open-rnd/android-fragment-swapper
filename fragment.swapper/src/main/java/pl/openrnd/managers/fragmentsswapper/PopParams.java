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

/**
 * Class holding fragments pop transaction parameters.
 */
public class PopParams {
    private boolean mAnimate;

    /**
     * Gets animation status of pop transaction.
     *
     * @return True if pop animation is enabled, false otherwise.
     */
    public boolean isAnimate() {
        return mAnimate;
    }

    private PopParams(Builder builder) {
        mAnimate = builder.mAnimate;
    }

    /**
     * PopParams object builder.
     */
    public static class Builder {
        private boolean mAnimate;

        /**
         * Class constructor.
         */
        public Builder() {
            mAnimate = true;
        }

        /**
         * Sets animation status of pop transaction.
         *
         * @param animate True if animations is enabled, false otherwise.
         * @return Builder object.
         */
        public Builder animate(boolean animate) {
            mAnimate = animate;
            return this;
        }

        /**
         * Builds PopParams object with current builder arguments.
         *
         * @return PopParams object.
         */
        public PopParams build() {
            return new PopParams(this);
        }
    }
}
