package demo.binea.com.parallexheaderlayout;


public interface BounceListener {

	public void onState(boolean header, BounceScroller.State state);

	public void onOffset(boolean header, int offset);

}
