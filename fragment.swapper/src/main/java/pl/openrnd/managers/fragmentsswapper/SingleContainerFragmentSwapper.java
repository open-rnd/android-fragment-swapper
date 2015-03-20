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

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.LinkedList;

/**
 * FragmentSwapper that supports only one container for fragments transactions.
 *
 * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
 * @see android.support.v4.app.Fragment
 *
 * @param <F> Fragment class that is supported by FragmentSwapper.
 */
public class SingleContainerFragmentSwapper<F extends Fragment & FragmentDescriptor> implements FragmentSwapper<F> {
    private static final String TAG = SingleContainerFragmentSwapper.class.getSimpleName();

    private InitializationParams mInitializationParams;

    private boolean mIsAnimationEnabled;
    private boolean mPopInProgress;

    private Handler mUiHandler;
    private F mContentFragment;
    private int mLastStackCount;

    private Integer mRequestCode;
    private int mResultCode;
    private Bundle mResultBundle;

    private boolean mIsSavedStateActive;

    private LinkedList<Runnable> mPendingOperations;

    private OnFragmentSwapperListener mOnFragmentSwapperListener;

    /**
     * Default constructor.
     */
    public SingleContainerFragmentSwapper() {
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * FragmentSwapper initialization routine.
     *
     * @param initializationParams InitializationParams object with initialization parameters.
     */
    public void initialize(InitializationParams initializationParams) {
        if (initializationParams == null) {
            throw new IllegalArgumentException("Argument is mandatory");
        }

        mInitializationParams = initializationParams;
        getFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        mLastStackCount = getFragmentManager().getBackStackEntryCount();
        mPendingOperations = new LinkedList<Runnable>();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");

        mIsSavedStateActive = true;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public void onResume() {
        Log.v(TAG, "onResume()");

        mIsSavedStateActive = false;

        handlePendingOperations();
    }

    /**
     * Method to be called with Bundle object containing state of holding Activity.
     *
     * Method starts main screen using attached ScreenManager if activity is created for the first time.
     *
     * @param savedInstanceState Bundle object with saved state.
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, String.format("onRestoreInstanceState(): savedInstanceState[%b]", savedInstanceState != null));

        if (savedInstanceState == null) {
            mInitializationParams.getScreenManager().onMainScreenRequested();
        } else {
            if (findCurrentFragment()) {
                notifyNewFragment(mContentFragment);
            }
            mOnBackStackChangedListener.onBackStackChanged();
        }
    }

    private void handlePendingOperations() {
        Log.v(TAG, String.format("handlePendingOperations(): size[%d]", mPendingOperations.size()));

        LinkedList<Runnable> pendingOperations = new LinkedList<Runnable>(mPendingOperations);
        mPendingOperations.clear();

        Runnable runnable = pendingOperations.pollFirst();
        while (runnable != null) {
            performOperationIfAllowed(runnable);

            runnable = pendingOperations.pollFirst();
        }
    }

    private void performOperationIfAllowed(final Runnable operation) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, String.format("performOperationIfAllowed(): isSavedStateActive[%b]", mIsSavedStateActive));

                if (mIsSavedStateActive) {
                    mPendingOperations.addLast(operation);
                } else {
                    operation.run();
                }
            }
        });
    }

    /**
     * Sets OnFragmentSwapperListener object that will receive notifications related to FragmentSwapper state and requests.
     *
     * @param listener OnFragmentSwapperListener object
     */
    public void setOnFragmentSwapperListener(OnFragmentSwapperListener listener) {
        mOnFragmentSwapperListener = listener;
    }

    private void notifyNewFragment(final F fragment) {
        Log.v(TAG, String.format("notifyNewFragment(): fragment[%s]", fragment != null ? fragment.getName() : null));

        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnFragmentSwapperListener != null) {
                    mOnFragmentSwapperListener.onFragmentEntered(SingleContainerFragmentSwapper.this, fragment);
                }
            }
        });
    }

    private void notifyPause(final F fragment) {
        Log.v(TAG, String.format("notifyPause(): fragment[%s]", fragment != null ? fragment.getName() : null));

        if (fragment != null) {
            fragment.onFragmentPause();
        }
    }

    private void notifyResume(final F fragment) {
        Log.v(TAG, String.format("notifyResume(): fragment[%s]", fragment != null ? fragment.getName() : null));

        if (fragment != null) {
            fragment.onFragmentResume();
        }
    }

    private void notifyCloseRequest() {
        Log.v(TAG, "notifyCloseRequest()");

        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnFragmentSwapperListener != null) {
                    mOnFragmentSwapperListener.onCloseRequested(SingleContainerFragmentSwapper.this);
                }
            }
        });
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public void popFragment(final PopParams popParams) {
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                int stackEntries = getFragmentManager().getBackStackEntryCount();
                Log.v(TAG, String.format("popFragment(): entries[%d]", stackEntries));

                notifyPause(getCurrentFragment());

                obtainResultsFromCurrentFragment();

                if (stackEntries > 1) {
                    setAnimationEnabled(popParams.isAnimate());
                    getFragmentManager().popBackStack();
                } else {
                    notifyCloseRequest();
                }
            }
        };
        performOperationIfAllowed(operation);
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public F getCurrentFragment() {
        return mContentFragment;
    }

    /**
     * Method must be called by holding Activity in its onBackPressed() method.
     *
     * Method pops current fragment.
     *
     * @param popParams PopParams object with pop transaction parameters.
     */
    public void onBackPressed(PopParams popParams) {
        Log.v(TAG, String.format("onBackPressed(): fragment[%s], animate[%b]", mContentFragment != null ? mContentFragment.getName() : null, popParams.isAnimate()));

        if (mContentFragment != null) {
            mContentFragment.onBackPressed(popParams);
        }
    }

    private FragmentManager getFragmentManager() {
        return mInitializationParams.getFragmentManager();
    }

    private boolean findCurrentFragment() {
        mContentFragment = (F) getFragmentManager().findFragmentById(mInitializationParams.getContentFrame());

        if (mContentFragment != null) {
            mContentFragment.assignFragmentSwapper(this);
        }

        FragmentManager fragmentManager = getFragmentManager();
        int entryCount = fragmentManager.getBackStackEntryCount();
        Log.v(TAG, String.format("findCurrentFragment()... entries [%d]", entryCount));
        return mContentFragment != null;
    }

    private void obtainResultsFromCurrentFragment() {
        FragmentDescriptor fragment = mContentFragment;

        mRequestCode = fragment.getRequestCode();
        mResultCode = fragment.getResultCode();
        mResultBundle = fragment.getResultData();

        Log.v(TAG, String.format("obtainResultsFromCurrentFragment(): current[%s], requestCode[%s], resultCode[%d], data[%b]",
                fragment.getName(), mRequestCode, mResultCode, mResultBundle != null));
    }

    private void sendResultsToCurrentFragmentAndClear() {
        Integer requestCode = mRequestCode;
        int resultCode = mResultCode;
        Bundle resultData = mResultBundle;

        mRequestCode = null;
        mResultCode = FragmentDescriptor.RESULT_CANCELED;
        mResultBundle = null;

        if (mContentFragment != null) {
            Log.v(TAG, String.format("sendResultsToCurrentFragmentAndClear(): current[%s], requestCode[%s], resultCode[%d], data[%b]",
                    mContentFragment.getName(), requestCode, resultCode, resultData != null));

            mContentFragment.onFragmentResult(requestCode, resultCode, resultData);
        }
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public boolean isAnimationEnabled() {
        return mIsAnimationEnabled;
    }

    protected void setAnimationEnabled(boolean enabled) {
        Log.v(TAG, String.format("setAnimationEnabled(): enabled[%b]", enabled));

        mIsAnimationEnabled = enabled;
    }

    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            Log.w(TAG, String.format("onBackStackChanged(): popInProgress[%b]", mPopInProgress));

            if (mPopInProgress) {
                obtainResultsFromCurrentFragment();
                return;
            }

            int stackCount = getFragmentManager().getBackStackEntryCount();

            Log.v(TAG, String.format("onBackStackChanged(): lastStackCount[%d], currentStackCount[%d]", mLastStackCount, stackCount));

            findCurrentFragment();

            setAnimationEnabled(true);

            if (stackCount < mLastStackCount) {
                sendResultsToCurrentFragmentAndClear();
            }

            notifyResume(mContentFragment);
            notifyNewFragment(mContentFragment);

            mLastStackCount = stackCount;
        }
    };

    /**
     * Clears fragments back stack removing all fragments from the hierarchy.
     */
    public void clearStack() {
        mPopInProgress = true;
        setAnimationEnabled(false);

        FragmentManager fragmentManager = getFragmentManager();
        int entryCount = fragmentManager.getBackStackEntryCount();
        Log.v(TAG, String.format("clearStack()... entries [%d]", entryCount));
        for (int i = 0; i < entryCount; ++i) {
            try {
                fragmentManager.popBackStackImmediate();
            } catch (Exception exc) {
                Log.e(TAG, "clearStack()", exc);
            }
        }

        findCurrentFragment();

        Log.v(TAG, "clearStack()... DONE");
        setAnimationEnabled(true);
        mPopInProgress = false;
    }

    private boolean clearToFragmentIfFound(F fragment) {
        mPopInProgress = true;
        boolean result = getFragmentManager().popBackStackImmediate(fragment.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mPopInProgress = false;

        Log.v(TAG, String.format("clearToFragmentIfFound(): fragmentName[%s], result[%b]", fragment.getName(), result));

        return result;
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentSwapper
     */
    @Override
    public void swapFragment(final SwapParams swapParams, final F fragment) {
        Runnable operation = new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "swapFragment()");

                fragment.assignFragmentSwapper(SingleContainerFragmentSwapper.this);

                notifyPause(mContentFragment);

                mPopInProgress = true;

                boolean popped = false;

                if (swapParams.isMainContext()) {
                    clearStack();
                    setAnimationEnabled(swapParams.isAnimate());
                } else {
                    setAnimationEnabled(swapParams.isAnimate());

                    popped = clearToFragmentIfFound(fragment);
                }

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (popped) {
                    fragmentTransaction.setCustomAnimations(0, 0, swapParams.getPopEnterAnimResId(), swapParams.getPopExitAnimResId());
                } else {
                    fragmentTransaction.setCustomAnimations(swapParams.getEnterAnimResId(), swapParams.getExitAnimResId(), swapParams.getPopEnterAnimResId(), swapParams.getPopExitAnimResId());

                    if (mContentFragment != null) {
                        if (swapParams.getRemoveOld()) {
                            fragmentTransaction.remove(mContentFragment);
                        } else {
                            fragmentTransaction.hide(mContentFragment);
                        }
                    }
                }

                fragment.setRequestCode(swapParams.getRequestCode());

                fragmentTransaction.add(mInitializationParams.getContentFrame(), fragment);
                if (swapParams.isAddToBackStack()) {
                    fragmentTransaction.addToBackStack(fragment.getName());
                }

                fragmentTransaction.commit();
                getFragmentManager().executePendingTransactions();

                if (!swapParams.isAddToBackStack()) {
                    //force to update current fragment and notify OnFragmentSwapperListener
                    mOnBackStackChangedListener.onBackStackChanged();
                }
            }
        };

        performOperationIfAllowed(operation);
    }
}
