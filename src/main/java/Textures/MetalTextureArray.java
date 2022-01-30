package Textures;

import Misc.WorldConstants;

public class MetalTextureArray extends TextureArray{
    public MetalTextureArray() {
        super(1024, WorldConstants.TEXTURE_COUNT, "src/main/resources/terrain/");
    }

    @Override
    public void loadLayers() {
        loadToLayer("metalplate_metal.jpg", 5);
    }
}
