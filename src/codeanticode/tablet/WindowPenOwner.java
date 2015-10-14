package codeanticode.tablet;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.Window;
import java.util.Arrays;
import java.util.Collection;
import jpen.owner.PenClip;
import jpen.owner.PenOwner;
import jpen.PenEvent;
import jpen.PenProvider;
import jpen.provider.osx.CocoaProvider;
import jpen.provider.wintab.WintabProvider;
import jpen.provider.xinput.XinputProvider;

public class WindowPenOwner
    implements PenOwner {

    private PenManagerHandle penManagerHandle;
    private final WindowPenClip WindowPenClip=new WindowPenClip(this);
    private final Window window;
    private volatile boolean mayBeDraggingOut;

    public WindowPenOwner(Window window) {
        this.window=window;
    }

    public Window getWindow() {
        return window;
    }

    @Override
    public void setPenManagerHandle(final PenManagerHandle penManagerHandle) {
        this.penManagerHandle=penManagerHandle;
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent ev) {
                mayBeDraggingOut=true;
                // escape from the EDT to avoid thread-safety issues...
                new Thread(new Runnable() {
                  public void run() {                    
                    penManagerHandle.setPenManagerPaused(false);
                  }
                }).start();  
            }

            @Override
            public void mouseExited(MouseEvent ev) {
                mayBeDraggingOut=false;                
                // escape from the EDT to avoid thread-safety issues...
                // Note: this has a side effect: some level events continue to 
                // be fired for a "very short time" after the mouse has exited, 
                // we would have to modify jpen internals to filter them out if necessary... 
                // let me know if this needs to be done for your use case.                
                new Thread(new Runnable() {
                  public void run() {                    
                    penManagerHandle.setPenManagerPaused(true);
                  }
                }).start();  
            }
        });
    }

    @Override
    public Collection<PenProvider.Constructor> getPenProviderConstructors() {
        return Arrays.asList(
                   new PenProvider.Constructor[] {
                       //Note: do you need a newt mouse/keyboard provider (so that regular newt mouse events/keyboard are fired as jpen events too)?
                       new XinputProvider.Constructor(),
                       new WintabProvider.Constructor(),
                       new CocoaProvider.Constructor(),
                   }
               );
    }

    @Override
    public PenClip getPenClip() {
        return WindowPenClip;
    }

    @Override
    public boolean isDraggingOut() {
        return mayBeDraggingOut;
    }

    @Override
    public Object evalPenEventTag(PenEvent ev) {
        return null;
    }

    @Override
    public boolean enforceSinglePenManager() {
        return true;
    }
}