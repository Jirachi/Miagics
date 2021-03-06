package com.miage.jirachi.miagics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.miage.jirachi.resource.ResourceAnimated;
import com.miage.jirachi.resource.ResourceAnimated.AnimationDef;

public class AnimationProvider {
    private ResourceAnimated mResource;
    private String mCurrentAnimation;
    private TextureRegion[][] mAnimationMatrix;
    private Map<String, Animation> mAnimationMap;
    private float mAccumulatedTime;
    private float mEnforcedTime;
    private String mEnforcedAnimation;
   
    
    /**
     * Constructeur
     * @param res Resource qui correspond au personnage
     * @param textures TextureRegions qui ont ete decoupees
     */
    public AnimationProvider(ResourceAnimated res, TextureRegion[][] textures) {
        mResource = res;
        mAnimationMatrix = textures;
        mCurrentAnimation = "idle";
        mAnimationMap = new HashMap<String, Animation>();
        
        // On lit les animations depuis le fichier resource et on cree toutes les 
        // classes Animation necessaires.
        Iterator<AnimationDef> iter = mResource.anims.iterator();
        while (iter.hasNext()) {
            // Pour chaque animation...
            AnimationDef anim = iter.next();
            
            // On calcule le nombre d'images dans l'animation
            int frameCount = (anim.end_col - anim.start_col + 1) + (anim.end_line-anim.start_line) * res.columns;
            
            // On cree un tableau avec chaque image de l'animation, dans l'ordre
            TextureRegion[] frames = new TextureRegion[frameCount];
            int frameIndex = 0;
            for (int line = anim.start_line; line <= anim.end_line; line++) {
                for (int col = anim.start_col; col <= anim.end_col; col++) {
                    frames[frameIndex] = mAnimationMatrix[line][col];
                    frameIndex++;
                }
            }
            
            Animation gdxAnim = new Animation(0.05f, frames);
            
            if (anim.loop) { 
                gdxAnim.setPlayMode(Animation.LOOP_PINGPONG);
            } else {
                gdxAnim.setPlayMode(Animation.NORMAL);
            }
            
            mAnimationMap.put(anim.name, gdxAnim);
        }
    }
    
    /**
     * Force la lecture (sans boucler) d'une animation avant l'animation a jouer
     * definie par playAnimation.
     * Utile pour afficher le saut avant qu'il tombe par exemple.
     * @param animName
     */
    public void enforceSingleAnimation(String animName) {
    	if (!mAnimationMap.containsKey(animName)) {
    		Log.w("AnimationProvider", "Animation " + animName + " doesn't exist - not enforced");
    		return;
    	}
    	
        mEnforcedAnimation = animName;
        mEnforcedTime = 0;
    }
    
    /**
     * Definit l'animation a jouer
     * @param animName Nom de l'animation
     */
    public void playAnimation(String animName) {
    	if (!mAnimationMap.containsKey(animName)) {
    		Log.w("AnimationProvider", "Animation " + animName + " doesn't exist - not played");
    		return;
    	}
    	
        mCurrentAnimation = animName;
    }
    
    /**
     * Retourne la frame a afficher en fonction de l'animation en cours
     * @param delta
     * @return
     */
    public TextureRegion getKeyFrame(float delta) {
        mAccumulatedTime += delta/2.0f;
        
        if (mEnforcedAnimation != null) {
            // On a une animation forcee, on la joue jusqu'a ce qu'elle soit finie
            Animation forcedAnim = mAnimationMap.get(mEnforcedAnimation);
            
            TextureRegion region = forcedAnim.getKeyFrame(mEnforcedTime);
            mEnforcedTime += delta;
            
            if (forcedAnim.isAnimationFinished(mEnforcedTime)) {
                // L'animation est finie, on jouera l'animation mCurrentAnimation a la prochaine frame
                mEnforcedAnimation = null;
                mEnforcedTime = 0;
            }
            
            return region;
        }
        else {
        	if (mAnimationMap.containsKey(mCurrentAnimation))
        		return mAnimationMap.get(mCurrentAnimation).getKeyFrame(mAccumulatedTime, true);
        	else
        		return null;
        }
    }
}


