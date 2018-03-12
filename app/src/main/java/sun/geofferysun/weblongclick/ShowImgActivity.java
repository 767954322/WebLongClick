package sun.geofferysun.weblongclick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

import sun.geofferysun.weblongclick.imagezoom.HackyViewPager;
import sun.geofferysun.weblongclick.imagezoom.ImageViewPagerAdapter;
import sun.geofferysun.weblongclick.utils.StringUtils;

/**
 * author：Geoffery Sun
 * time：2016/12/25 18:37
 * desc：
 * version：0.0.1
 */
public class ShowImgActivity extends AppCompatActivity {
    ImageViewPagerAdapter adapter;
    HackyViewPager pager;
    private List<String> mList = new ArrayList<>();
    String mInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_showimg);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("info")) {
                mInfo = bundle.getString("info");
            }
        }

        if (StringUtils.isEmpty(mInfo)) {
            finish();
        } else {
            pager = (HackyViewPager) findViewById(R.id.pager);
            mList.add(mInfo);
            adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), mList);
            pager.setAdapter(adapter);
        }
    }
}
