package terrain;

import Misc.Maths;
import models.Mesh;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import renderEngine.Loader;

import java.awt.image.BufferedImage;


class MeshData{
    public float[] vertices, textureCoords, normals, texSlices;
    public int[] indices;
    public MeshData(float[] vertices, int[] indices, float[] textureCoords, float[] normals, float[] texSlices){
        this.vertices = vertices;
        this.indices = indices;
        this.texSlices = texSlices;
        this.textureCoords = textureCoords;
        this.normals = normals;
    }

}

public class TerrainMeshBuilder {
    public static int seed = 123211;
    private static final FastNoiseLite simplexNoise1 = new FastNoiseLite(seed);
    private static final FastNoiseLite simplexNoise2 = new FastNoiseLite(seed);
    private static final FastNoiseLite simplexNoise3 = new FastNoiseLite(seed);
    private static final FastNoiseLite simplexNoise4 = new FastNoiseLite(seed);


    private static final int SIZE = 128;
    private static final int VERTEX_COUNT = 500;
//
//    static {
//        simplexNoise1.SetFrequency(0.0000005f);
//        simplexNoise2.SetFrequency(0.0001f);
//
//        simplexNoise3.SetFrequency(0.0000000007f);
//        simplexNoise4.SetFrequency(0.00005f);
//        simplexNoise1.SetFractalType(FastNoiseLite.FractalType.FBm);
//        simplexNoise2.SetFractalType(FastNoiseLite.FractalType.Ridged);
//        simplexNoise3.SetFractalType(FastNoiseLite.FractalType.Ridged);
//        simplexNoise3.SetFractalGain(0.5f);
//        simplexNoise4.SetFractalType(FastNoiseLite.FractalType.Ridged);
//        simplexNoise1.SetFractalOctaves(5);
//        simplexNoise2.SetFractalOctaves(10);
//        simplexNoise3.SetFractalOctaves(7);
//        simplexNoise4.SetFractalOctaves(5);
//    }

    static {
        simplexNoise1.SetFrequency(0.00005f);
        simplexNoise2.SetFrequency(0.0005f);

        simplexNoise3.SetFrequency(0.00007f);
        simplexNoise1.SetFractalType(FastNoiseLite.FractalType.FBm);
        simplexNoise2.SetFractalType(FastNoiseLite.FractalType.FBm);
        simplexNoise3.SetFractalType(FastNoiseLite.FractalType.Ridged);
        simplexNoise1.SetFractalOctaves(15);
        simplexNoise2.SetFractalOctaves(2);
        simplexNoise3.SetFractalOctaves(7);
    }

//    public static double get3DHeightAt(float x, float y, float z){
//        double n;
//        double continentNoise = simplexNoise1.GetNoise(x,y,z);
//        //double noise1 = simplexNoise2.GetNoise(x,y,z);
//        continentNoise *= 10;
//
//        if(continentNoise > 0){
//            //Generate mountains and such
//            double noise1 = simplexNoise2.GetNoise(x,y,z);
//            double noise2 = simplexNoise3.GetNoise(x,y,z);
//            double noise3 = simplexNoise4.GetNoise(x*2f,y*2f,z*2f);
//            //double noise3 = simplexNoise3.GetNoise(x,y,z);
//
//            noise2 *= 5;
//
//
//            n =  (continentNoise * (noise1 + noise2 + noise3));
//        }
//        else {
//            n = continentNoise;
//        }
//
//        //noise3 *= 200;
////        double noise2 = simplexNoise3.GetNoise(x,y,z);
////        double noise3 = simplexNoise4.GetNoise(x,y,z);
////        noise3 *= 2.41231238272736;
////        continentNoise *= 192;
//
//
//        n = Math.max(0, n);
//        return n;
//    }

    public static double get3DHeightAt(float x, float y, float z){
        double noise = simplexNoise1.GetNoise(x,y,z);
//        double noise1 = simplexNoise2.GetNoise(x,y,z);
        double noise3 = simplexNoise3.GetNoise(x,y,z);

        noise *= 10;


        //noise3 *= 200;
//        double noise2 = simplexNoise3.GetNoise(x,y,z);
//        double noise3 = simplexNoise4.GetNoise(x,y,z);
//        noise3 *= 2.41231238272736;
//        noise *= 192;
        return (noise * noise3);
    }
  


    public static MeshData generateSphereChunk(Matrix4f localToWorld, Vector3f offset, int radius, float width, int resolution, Vector3d origin, Vector3f pos, boolean hasHeight){
        final Vector3f _D = new Vector3f();
        final Vector3f _D1 = new Vector3f();
        final Vector3f _D2 = new Vector3f();
        final Vector3f _P = new Vector3f();
        final Vector3f _H = new Vector3f();
        final Vector3f _W = new Vector3f();
        final Vector3f _C = new Vector3f();
        final Vector3f _S = new Vector3f();

        final Vector3f _N = new Vector3f();
        final Vector3f _N1 = new Vector3f();
        final Vector3f _N2 = new Vector3f();
        final Vector3f _N3 = new Vector3f();

        final Vector3f originF = new Vector3f((float) origin.x, (float)origin.y, (float)origin.z);

        int count = resolution * resolution;
        float[] positions = new float[(resolution + 1) * (resolution + 1) * 3];
        float[] normals = new float[count * 5];
        float[] uvs = new float[(resolution + 1) * (resolution + 1) * 2];
        int[] indices = new int[(resolution + 1) * (resolution + 1) * 6];
        float[] texSlices = new float[(resolution + 1) * (resolution + 1)];
       // float[] colors = new float[(resolution + 1) * (resolution + 1) * 3];

        int normalCount = 0;
        int indicesCount = 0;

        //Width = boundary.size.x
        //Radius = the sphere radius
        float half = width / 2;

        int vertexPointer = 0;
        for(int x = 0; x < resolution + 1; x++){
            float xp = width * x / resolution;
            for(int y = 0; y < resolution + 1; y++){
                float yp = width * y / resolution;

                // Compute position
                _P.set(xp - half, yp - half, radius);
                _P.add(offset);
                _P.normalize();
                _D.set(_P);
                Maths.transformDirection(_D, localToWorld);

                _P.mul(radius);
                _P.z -= radius;
                Maths.applyMatrix4To3DVector(_P, localToWorld);

                // Compute a world space position to sample noise
                _W.set(_P);
                _P.sub(originF);

                double height;
                if(!hasHeight) height = 0;
                else height = get3DHeightAt(_W.x, _W.y, _W.z);


                // Perturb height along z-vector
                _H.set(_D);
                float height1 = (float) height * 400;

                _H.mul(height1);
                _P.add(_H);
                _S.set(_W.x, _W.y, height1);
                texSlices[vertexPointer] = 1; //TODO _S
                positions[vertexPointer*3] = _P.x + pos.x;
                positions[vertexPointer*3+1] = _P.y + pos.y;
                positions[vertexPointer*3+2] = _P.z + pos.z;
                normals[vertexPointer*3] = _D.x;
                normals[vertexPointer*3+1] = _D.y;
                normals[vertexPointer*3+2] = _D.z;
                uvs[vertexPointer*2] = _P.x / 10;
                uvs[vertexPointer*2+1] = _P.y / 10;
                vertexPointer++;
                normalCount+=3;
            }
        }
        int iPointer = 0;
        for(int i = 0; i < resolution; i++){
            for(int j = 0; j < resolution; j++){
                indices[iPointer++] =  i * (resolution + 1) + j;
                indices[iPointer++] = (i + 1) * (resolution + 1) + j + 1;
                indices[iPointer++] = i * (resolution + 1) + j + 1;
                indices[iPointer++] = (i + 1) * (resolution + 1) + j;
                indices[iPointer++] = (i + 1) * (resolution + 1) + j + 1;
                indices[iPointer++] = i * (resolution + 1) + j;
                indicesCount += 6;
            }
        }

        for(int i = 0; i < indicesCount; i += 3){
            int i1 = indices[i] * 3;
            int i2 = indices[i+1] * 3;
            int i3 = indices[i+2] * 3;

            _N1.x = positions[i1];
            _N1.y = positions[i1 + 1];
            _N1.z = positions[i1 + 2];

            _N2.x = positions[i2];
            _N2.y = positions[i2 + 1];
            _N2.z = positions[i2 + 2];

            _N3.x = positions[i3];
            _N3.y = positions[i3 + 1];
            _N3.z = positions[i3 +2];

            _D1.x = _N3.x - _N2.x;
            _D1.y = _N3.y - _N2.y;
            _D1.z = _N3.z - _N2.z;

            _D2.x = _N1.x - _N2.x;
            _D2.y = _N1.y - _N2.y;
            _D2.z = _N1.z - _N2.z;

            _D1.cross(_D2);

            normals[i1] += _D1.x;
            normals[i2] += _D1.x;
            normals[i3] += _D1.x;

            normals[i1+1] += _D1.y;
            normals[i2+1] += _D1.y;
            normals[i3+1] += _D1.y;

            normals[i1+2] += _D1.z;
            normals[i2+2] += _D1.z;
            normals[i3+2] += _D1.z;
        }
        for(int i = 0; i < normalCount; i+=3){
                _N.x = normals[i];
                _N.y = normals[i + 1];
                _N.z = normals[i + 2];
                _N.normalize();

                normals[i] = _N.x;
                normals[i+1] = _N.y;
                normals[i+2] = _N.z;
        }

        return new MeshData(positions, indices, uvs, normals, texSlices);

    }


}
