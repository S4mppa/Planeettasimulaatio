package particles;

import java.lang.Math;
import java.util.Random;

import Misc.DisplayTime;
import Textures.ParticleTexture;
import org.joml.*;



public class ParticleSystem {


	private float pps, averageSpeed, gravityComplient, averageLifeLength, averageScale;

	private float speedError, lifeError, scaleError = 0;
	private boolean randomRotation = false;
	private Vector3f direction;
	private float directionDeviation = 0;
	private Random random = new Random();

	private ParticleTexture texture;

	private int scattering = 0;
	private int scatteringY = 1;

	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
		this.pps = pps;
		this.texture = texture;
		this.averageSpeed = speed;
		this.gravityComplient = gravityComplient;
		this.averageLifeLength = lifeLength;
		this.averageScale = scale;
	}

	/**
	 * @param direction - The average direction in which particles are emitted.
	 * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
	 */
	public void setDirection(Vector3f direction, float deviation) {
		this.direction = new Vector3f(direction);
		this.directionDeviation = (float) (deviation * Math.PI);
	}

	/**
	 * @param scattering - The area which the particles are scattered over. 0 = none
	 */
	public void setScattering(int scattering){
		this.scattering = scattering;
	}

	/**
	 * @param scatteringY - The height which the particles are scattered over. 0 = none
	 */
	public void setScatteringY(int scatteringY){
		this.scatteringY = scatteringY;
	}

	public void randomizeRotation() {
		randomRotation = true;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setSpeedError(float error) {
		this.speedError = error * averageSpeed;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setLifeError(float error) {
		this.lifeError = error * averageLifeLength;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setScaleError(float error) {
		this.scaleError = error * averageScale;
	}

	public void generateParticles(Vector3d systemCenter) {
		float delta = DisplayTime.getRefreshTime();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			Vector3d newPos = new Vector3d(systemCenter);
			if(scattering > 0) {
				newPos.add(random.nextInt(scattering), random.nextInt(scatteringY), random.nextInt(scattering));
			}
			emitParticle(newPos);
		}
		if (Math.random() < partialParticle) {
			Vector3d newPos = new Vector3d(systemCenter);
			if(scattering > 0) {
				newPos.add(random.nextInt(scattering), random.nextInt(scatteringY), random.nextInt(scattering));
			}
			emitParticle(newPos);
		}
	}

	private void emitParticle(Vector3d center) {
		Vector3f velocity = null;
		if(direction!=null){
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		}else{
			velocity = generateRandomUnitVector();
		}
		velocity.normalize();
		velocity.mul(generateValue(averageSpeed, speedError));
		float scale = generateValue(averageScale, scaleError);
		float lifeLength = generateValue(averageLifeLength, lifeError);
		new Particle(texture, new Vector3d(center), velocity, gravityComplient, lifeLength, generateRotation(), scale);
	}

	private float generateValue(float average, float errorMargin) {
		float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
		return average + offset;
	}

	private float generateRotation() {
		if (randomRotation) {
			return random.nextFloat() * 360f;
		} else {
			return 0;
		}
	}

	private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random random = new Random();
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		Vector4f direction = new Vector4f(x, y, z, 1);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
			Vector3f rotateAxis = new Vector3f(coneDirection).cross(new Vector3f(0,0,1));
			rotateAxis.normalize();
			float rotateAngle = (float) Math.acos(new Vector3f(coneDirection).dot(new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(-rotateAngle, rotateAxis);
			rotationMatrix.transform(direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}
		return new Vector3f(direction.x, direction.y, direction.z);
	}

	private Vector3f generateRandomUnitVector() {
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = (random.nextFloat() * 2) - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		return new Vector3f(x, y, z);
	}
}
