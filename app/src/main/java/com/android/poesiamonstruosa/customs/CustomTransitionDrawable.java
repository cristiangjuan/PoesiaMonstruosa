package com.android.poesiamonstruosa.customs;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;

import com.android.poesiamonstruosa.utils.Constants;


public class CustomTransitionDrawable extends TransitionDrawable implements Runnable {

	
	private int duration = 0;
	/**
	 * Con este flag evitamos que se ejecute por segunda vez la animación de retorno
	 */
	private boolean finish = false;
	/**
	 * Este flag nos indica que si ha de ejecutarse la animación de retorno
	 */
	private boolean reverse = false;
	/**
	 * Este flag nos indica que si es un animación falsa para rellenar un silencio
	 */
	private boolean fake = false;
	
	public CustomTransitionDrawable(Drawable[] layers, boolean fake) {

		super(layers);
		this.fake = fake;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public boolean isFinished() {
		
		return finish;
	}
	
	public boolean isFirstStepDone() {
		
		return reverse;
	}
	
	public void reset() {
		
		finish = false;
		reverse = false;
	}
	
	
	@Override
	public void run() {

		 if (!finish && !fake) {
			 
			 if (reverse) {
				 
				 Log.v(Constants.Log.SUBS, "CustomTransitionDrawable - run - reverse - "+this.hashCode());
				 this.reverseTransition(duration);
				 finish = true;
			 }
			 else {
				 Log.v(Constants.Log.SUBS, "CustomTransitionDrawable - run - "+this.hashCode());
				 this.startTransition(duration);
				 reverse = true;
			 }
		 }
		 
	}

}
