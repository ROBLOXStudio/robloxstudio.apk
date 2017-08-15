public boolean onDown(MotionEvent event)
public boolean onSingleTapUp(MotionEvent e) 
public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
public boolean onDown(MotionEvent event) {
	float x = event.getX() / ((float) getWidth());
	float y = event.getY() / ((float) getHeight());
	mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
	return true;
}
 
public boolean onSingleTapUp(MotionEvent e) {
	float x = e.getX() / ((float) getWidth());
	float y = e.getY() / ((float) getHeight());
	mAngleUp = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
 
	// if we click up the same place where we clicked down, it's just a button press
	if (! Float.isNaN(mAngleDown) && ! Float.isNaN(mAngleUp) && Math.abs(mAngleUp-mAngleDown) < 10) {
		SetState(!mState);
		if (m_listener != null) m_listener.onStateChange(mState);
	}
	return true;
}
public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	float x = e2.getX() / ((float) getWidth());
	float y = e2.getY() / ((float) getHeight());
	float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
 
	if (! Float.isNaN(rotDegrees)) {
		// instead of getting 0-> 180, -180 0 , we go for 0 -> 360
		float posDegrees = rotDegrees;
		if (rotDegrees < 0) posDegrees = 360 + rotDegrees;
 
		// deny full rotation, start start and stop point, and get a linear scale
		if (posDegrees > 210 || posDegrees < 150) {
			// rotate our imageview
			setRotorPosAngle(posDegrees);
			// get a linear scale
			float scaleDegrees = rotDegrees + 150; // given the current parameters, we go from 0 to 300
			// get position percent
			int percent = (int) (scaleDegrees / 3);
			if (m_listener != null) m_listener.onRotate(percent);
			return true; //consumed
		} else
			return false;
	} else
		return false; // not consumed
}
private float cartesianToPolar(float x, float y) {
	return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
}
 public void setRotorPosAngle(float deg) {
	if (deg >= 210 || deg <= 150) {
		if (deg > 180) deg = deg - 360;
		Matrix matrix=new Matrix();
		ivRotor.setScaleType(ScaleType.MATRIX);   
		matrix.postRotate((float) deg, m_nWidth/2, m_nHeight/2);//getWidth()/2, getHeight()/2);
		ivRotor.setImageMatrix(matrix);
	}
}
public void setRotorPercentage(int percentage) {
	int posDegree = percentage * 3 - 150;
	if (posDegree < 0) posDegree = 360 + posDegree;
	setRotorPosAngle(posDegree);
}


 