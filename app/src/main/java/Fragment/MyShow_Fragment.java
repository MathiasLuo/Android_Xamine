package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.luowuxia.me.android_xamine.R;


public class MyShow_Fragment extends android.support.v4.app.Fragment {
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.fragment_my_show_, (ViewGroup) getActivity().findViewById(R.id.viewPager), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) mView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return mView;
    }


}
