package demo.binea.com.bezierindicator;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.google.common.collect.Lists;

import java.util.List;

import demo.binea.com.bezierindicator.widget.BezierIndicator;
import demo.binea.com.bezierindicator.widget.GuideFragment;
import demo.binea.com.bezierindicator.widget.util.ScrollerUtil;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;


public class MainActivity extends ActionBarActivity {

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		BezierIndicator springIndicator = (BezierIndicator) findViewById(R.id.indicator);
//		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//		setSupportActionBar(toolbar);
		ScrollerUtil.setViewPager(viewPager);
		PagerModelManager manager = new PagerModelManager();
		manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
		ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
		viewPager.setAdapter(adapter);
		// just set viewPager
		springIndicator.setViewPager(viewPager);
	}

	private List<String> getTitles(){
		return Lists.newArrayList("1", "2", "3", "4");
	}

	private List<Integer> getBgRes(){
		return Lists.newArrayList(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
	}
}
