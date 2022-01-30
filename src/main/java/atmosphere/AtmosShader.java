package atmosphere;

import Misc.DisplayTime;
import models.Entity;
import models.Planet;
import org.joml.Vector3f;
import renderEngine.Camera;
import renderEngine.Transformation;
import shaders.ShaderProgram;
import terrain.TerrainChunkManager;
import terrain.TerrainRenderer;


public class AtmosShader extends ShaderProgram {
    public AtmosShader() throws Exception {
        super("postProcessing/bloom/simpleVertex.txt", "postProcessing/atmosphere/atmosFragment.glsl");
        createUniform("cameraViewDir");
        createUniform("planetCentre");
        createUniform("atmosphereRadius");
        createUniform("planetRadius");
        createUniform("worldSpaceCameraPos");
        createUniform("inverseProjection");
        createUniform("inverseView");
        createUniform("dirToSun");
        createUniform("colourTexture");
        createUniform("depthTexture");
        createUniform("weatherClarity");
    }

    public void setUniforms(Camera camera, Planet planet){
        setUniform("cameraViewDir", camera.getSightVector());
        setUniform("worldSpaceCameraPos", camera.getPosition());
        setUniform("planetRadius", (float) planet.getRadius());
        setUniform("planetCentre", planet.getWorldSpacePos());
        setUniform("atmosphereRadius", planet.getRadius() * 1.01f);
        setUniform("inverseProjection", Transformation.getProjectionMatrix().invert());
        setUniform("inverseView", Transformation.getViewMatrix(camera).invert());
        setUniform("dirToSun", new Vector3f(TerrainRenderer.light.getPosition()).sub(TerrainChunkManager.EARTH.getWorldSpacePos()).normalize());
        setUniform("weatherClarity", 0);
    }
}
