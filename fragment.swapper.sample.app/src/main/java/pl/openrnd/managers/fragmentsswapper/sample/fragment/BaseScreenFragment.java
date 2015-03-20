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

package pl.openrnd.managers.fragmentsswapper.sample.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import pl.openrnd.managers.fragmentsswapper.BaseFragment;
import pl.openrnd.managers.fragmentsswapper.PopParams;
import pl.openrnd.managers.fragmentsswapper.SwapParams;
import pl.openrnd.managers.fragmentsswapper.sample.R;
import pl.openrnd.managers.fragmentsswapper.sample.SampleScreenManager;

public abstract class BaseScreenFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    protected abstract int getColorResId();
    protected abstract int getFragmentReqId();
    protected abstract void onNextFragmentRequested(SwapParams swapParams);

    private SampleScreenManager mSampleScreenManager;
    private CheckBox mBackStackView;
    private CheckBox mMainContextView;
    private TextView mInfoView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.v(TAG, "onAttach()");

        if (activity instanceof SampleScreenManager) {
            mSampleScreenManager = (SampleScreenManager)activity;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.v(TAG, "onDetach()");
    }

    protected boolean isLastFragment() {
        return false;
    }

    protected SampleScreenManager getSampleScreenManager() {
        return mSampleScreenManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContextActivity()).inflate(R.layout.fragment_screen, null);
        initViews(root);
        return root;
    }

    private void initViews(View root) {
        root.setBackgroundResource(getColorResId());

        mInfoView = (TextView)root.findViewById(R.id.info);
        mInfoView.setText(collectEnterInfo());

        mBackStackView = (CheckBox)root.findViewById(R.id.addToBackStack);
        mMainContextView = (CheckBox)root.findViewById(R.id.mainContext);

        root.findViewById(R.id.closeNoAnimate).setOnClickListener(mOnClickListener);
        root.findViewById(R.id.close).setOnClickListener(mOnClickListener);

        View nextScreenView = root.findViewById(R.id.nextScreen);
        View nextScreenNoAnimView = root.findViewById(R.id.nextScreenNoAnimate);

        nextScreenView.setOnClickListener(mOnClickListener);
        nextScreenNoAnimView.setOnClickListener(mOnClickListener);

        int visibility = isLastFragment() ? View.GONE : View.VISIBLE;
        mBackStackView.setVisibility(visibility);
        mMainContextView.setVisibility(visibility);
        nextScreenView.setVisibility(visibility);
        nextScreenNoAnimView.setVisibility(visibility);
    }

    private String collectEnterInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("Fragment id: ");
        builder.append(getFragmentReqId());
        builder.append("\n");

        Integer id = getRequestCode();
        if (id != null) {
            builder.append("Fragment requested with id: ");
            builder.append(id);
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentPause() {
        super.onFragmentPause();

        Log.v(TAG, "onFragmentPause()");
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();

        Log.v(TAG, "onFragmentResume()");
    }

    /**
     * @see pl.openrnd.managers.fragmentsswapper.FragmentDescriptor
     */
    @Override
    public void onFragmentResult(Integer requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        Log.v(TAG, String.format("onFragmentResult(): requestCode[%s], resultCode[%d]", requestCode, resultCode));

        StringBuilder builder = new StringBuilder(collectEnterInfo());
        builder.append("\n");
        builder.append("onFragmentResult(): requestCode: ");
        builder.append(requestCode != null ? requestCode : "N/A");
        builder.append("\n");
        builder.append("onFragmentResult(): resultCode: ");
        builder.append(resultCode == RESULT_OK ? "RESULT_OK" :
                (resultCode == RESULT_CANCELED ? "RESULT_CANCELED" : resultCode));

        mInfoView.setText(builder.toString());
    }

    private void onCloseRequested(PopParams popParams) {
        setResult(RESULT_OK, null);

        onBackPressed(popParams);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close:
                    onCloseRequested(new PopParams.Builder()
                            .animate(true)
                            .build());
                    break;

                case R.id.closeNoAnimate:
                    onCloseRequested(new PopParams.Builder()
                            .animate(false)
                            .build());
                    break;

                case R.id.nextScreen:
                    onNextFragmentRequested(new SwapParams.Builder()
                            .animate(true)
                            .requestCode(getFragmentReqId())
                            .addToBackStack(mBackStackView.isChecked())
                            .mainContext(mMainContextView.isChecked())
                            .build());
                    break;

                case R.id.nextScreenNoAnimate:
                    onNextFragmentRequested(new SwapParams.Builder()
                            .animate(false)
                            .requestCode(getFragmentReqId())
                            .addToBackStack(mBackStackView.isChecked())
                            .mainContext(mMainContextView.isChecked())
                            .build());
                    break;
            }
        }
    };
}
