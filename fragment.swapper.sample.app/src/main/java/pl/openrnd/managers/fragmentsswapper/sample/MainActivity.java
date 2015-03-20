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

package pl.openrnd.managers.fragmentsswapper.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import pl.openrnd.managers.fragmentsswapper.FragmentSwapper;
import pl.openrnd.managers.fragmentsswapper.InitializationParams;
import pl.openrnd.managers.fragmentsswapper.OnFragmentSwapperListener;
import pl.openrnd.managers.fragmentsswapper.PopParams;
import pl.openrnd.managers.fragmentsswapper.SingleContainerFragmentSwapper;
import pl.openrnd.managers.fragmentsswapper.SwapParams;
import pl.openrnd.managers.fragmentsswapper.sample.fragment.Screen1Fragment;
import pl.openrnd.managers.fragmentsswapper.sample.fragment.Screen2Fragment;
import pl.openrnd.managers.fragmentsswapper.sample.fragment.Screen3Fragment;
import pl.openrnd.managers.fragmentsswapper.sample.fragment.Screen4Fragment;

public class MainActivity extends FragmentActivity implements SampleScreenManager {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SingleContainerFragmentSwapper mFragmentSwapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFragmentSwapper(savedInstanceState);
    }

    private void initializeFragmentSwapper(Bundle savedInstanceState) {
        InitializationParams.Builder builder = new InitializationParams.Builder();
        builder.screenManager(this);
        builder.contentFrame(R.id.fragmentContainer);
        builder.fragmentManager(getSupportFragmentManager());

        mFragmentSwapper = new SingleContainerFragmentSwapper();
        mFragmentSwapper.setOnFragmentSwapperListener(mOnFragmentSwapperListener);
        mFragmentSwapper.initialize(builder.build());
        mFragmentSwapper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mFragmentSwapper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFragmentSwapper.onResume();
    }

    @Override
    public void onBackPressed() {
        mFragmentSwapper.onBackPressed(new PopParams.Builder().build());
    }

    @Override
    public void onMainScreenRequested() {
        Log.v(TAG, "onMainScreenRequested()");

        onSampleScreen1Requested(new SwapParams.Builder().build());
    }

    @Override
    public void onSampleScreen1Requested(SwapParams swapParams) {
        Log.v(TAG, "onSampleScreen1Requested()");

        mFragmentSwapper.swapFragment(swapParams, Screen1Fragment.newInstance());
    }

    @Override
    public void onSampleScreen2Requested(SwapParams swapParams) {
        Log.v(TAG, "onSampleScreen2Requested()");

        mFragmentSwapper.swapFragment(swapParams, Screen2Fragment.newInstance());
    }

    @Override
    public void onSampleScreen3Requested(SwapParams swapParams) {
        Log.v(TAG, "onSampleScreen3Requested()");

        mFragmentSwapper.swapFragment(swapParams, Screen3Fragment.newInstance());
    }

    @Override
    public void onSampleScreen4Requested(SwapParams swapParams) {
        Log.v(TAG, "onSampleScreen4Requested()");

        mFragmentSwapper.swapFragment(swapParams, Screen4Fragment.newInstance());
    }

    private OnFragmentSwapperListener mOnFragmentSwapperListener = new OnFragmentSwapperListener() {

        @Override
        public void onFragmentEntered(FragmentSwapper fragmentSwapper, Fragment fragment) {
            Log.v(TAG, "onFragmentEntered()");
        }

        @Override
        public void onCloseRequested(FragmentSwapper fragmentSwapper) {
            Log.v(TAG, "onCloseRequested()");

            setResult(RESULT_CANCELED);
            finish();
        }
    };
}
