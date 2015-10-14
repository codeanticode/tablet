package codeanticode.tablet;

import com.jogamp.newt.Window;
import java.awt.geom.Point2D;
import java.awt.Point;
import jpen.owner.PenClip;

final class WindowPenClip
    implements PenClip {

    final WindowPenOwner windowPenOwner;

    public WindowPenClip(WindowPenOwner windowPenOwner) {
        this.windowPenOwner=windowPenOwner;
    }

    @Override
    public void evalLocationOnScreen(Point pointOnScreen) {
        //-> called holding the pen scheduler lock
        pointOnScreen.x=pointOnScreen.y=0;
        com.jogamp.nativewindow.util.Point joglPoint=windowPenOwner.getWindow().getLocationOnScreen(null);
        pointOnScreen.x=joglPoint.getX();
        pointOnScreen.y=joglPoint.getY();
    }

    @Override
    public boolean contains(Point2D.Float point) {
        //-> called holding the pen scheduler lock
        if(point.x<0 || point.y<0)
            return false;
        Window window=windowPenOwner.getWindow();
        return point.x<=window.getWidth() && point.y<=window.getHeight();
    }
}