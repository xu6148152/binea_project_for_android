package demo.binea.com.beautyprogressbar.factory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import demo.binea.com.beautyprogressbar.domain.Leaf;
import demo.binea.com.beautyprogressbar.domain.StartType;
import demo.binea.com.beautyprogressbar.widget.LeafLoadingView;

/**
 * Created by xubinggui on 15/3/26.
 */
public class LeafFactory {

	private int mAddTime;

	private static final int MAX_LEAFS = 8;
	Random random = new Random();

	// 生成一个叶子信息
	public Leaf generateLeaf() {
		Leaf leaf = new Leaf();
		int randomType = random.nextInt(3);
		// 随时类型－ 随机振幅
		StartType type = StartType.MIDDLE;
		switch (randomType) {
			case 0:
				break;
			case 1:
				type = StartType.LITTLE;
				break;
			case 2:
				type = StartType.BIG;
				break;
			default:
				break;
		}
		leaf.type = type;
		// 随机起始的旋转角度
		leaf.rotateAngle = random.nextInt(360);
		// 随机旋转方向（顺时针或逆时针）
		leaf.rotateDirection = random.nextInt(2);
		// 为了产生交错的感觉，让开始的时间有一定的随机性
		LeafLoadingView.mLeafFloatTime = LeafLoadingView.mLeafFloatTime <= 0 ? LeafLoadingView.LEAF_FLOAT_TIME : LeafLoadingView.mLeafFloatTime;
		mAddTime += random.nextInt((int) (LeafLoadingView.mLeafFloatTime * 2));
		leaf.startTime = System.currentTimeMillis() + mAddTime;
		return leaf;
	}

	// 根据最大叶子数产生叶子信息
	public List<Leaf> generateLeafs() {
		return generateLeafs(MAX_LEAFS);
	}

	// 根据传入的叶子数量产生叶子信息
	public List<Leaf> generateLeafs(int leafSize) {
		List<Leaf> leafs = new LinkedList<Leaf>();
		for (int i = 0; i < leafSize; i++) {
			leafs.add(generateLeaf());
		}
		return leafs;
	}
}
