package com.miage.jirachi.resource;

import java.util.List;

public class ResourceAnimated extends Resource {
    public int columns;
    public int lines;
    public int width;
    public int height;
    public boolean reverse;
    public int speed;
    List<AnimationDef> anims;
    
    public static class AnimationDef {
        public String name;
        public int start_col;
        public int end_col;
        public int start_line;
        public int end_line;
    }
}
