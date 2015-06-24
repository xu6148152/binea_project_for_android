package demo.binea.com.beautyprogressbar.domain;

/**
 * Created by xubinggui on 15/3/26.
 */
public class Leaf {
	// 在绘制部分的位置
	public float x, y;
	// 控制叶子飘动的幅度
	public StartType type;
	// 旋转角度
	public int rotateAngle;
	// 旋转方向--0代表顺时针，1代表逆时针
	public int rotateDirection;
	// 起始时间(ms)
	public long startTime;
}
